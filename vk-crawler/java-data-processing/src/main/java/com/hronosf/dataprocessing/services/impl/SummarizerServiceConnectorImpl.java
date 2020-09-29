package com.hronosf.dataprocessing.services.impl;

import com.google.common.util.concurrent.*;
import com.hronosf.dataprocessing.services.SummarizerServiceConnector;
import com.hronosf.summarizer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummarizerServiceConnectorImpl implements SummarizerServiceConnector {

    @Value("${elasticsearch.index}")
    private String esIndex;

    @GrpcClient("local-grpc-server")
    private SummarizerGrpc.SummarizerFutureStub summarizationServiceStub;

    private final JavaSparkContext sc;
    private final ExecutorService executorService;

    public void summarizeText(JavaRDD<Map<String, Object>> toSummarize) {
        // use spark to map to grpc message entry:
        List<TextToSummary> textToSummaries = toSummarize
                .map(map -> {
                    Object text = map.get("text");

                    return TextToSummary
                            .newBuilder()
                            .setId((map.get("id").toString()))
                            .setText(text == null ? StringUtils.EMPTY : text.toString())
                            .build();
                })
                .collect();

        // build grpc message:
        SummarizeRequest request = SummarizeRequest.newBuilder()
                .addAllTextToSummary(textToSummaries)
                .build();

        // send data to neural net and get future:
        log.info("Calling for summarization of {} documents", request.getTextToSummaryList().size());
        ListenableFuture<SummarizeResponse> summarizeFuture = summarizationServiceStub.summarize(request);

        // add callback to future - update ES when neural net will return data:
        Futures.addCallback(
                summarizeFuture,
                new FutureCallback<SummarizeResponse>() {

                    @Override
                    public void onSuccess(@NullableDecl SummarizeResponse summarizeResponse) {
                        if (summarizeResponse == null || summarizeResponse.getTextToSummaryList() == null) {
                            log.error("Something went wrong, future returned null or response list is null");
                            return;
                        }

                        JavaRDD<Object> updatedData = sc
                                .parallelize(summarizeResponse.getTextToSummaryList())
                                .map(response -> {
                                    Map<String, Object> updatedDocument = new HashMap<>();
                                    updatedDocument.put("id", response.getId());
                                    updatedDocument.put("summary", response.getSummary());
                                    updatedDocument.put("processedIn", response.getProcessedIn());

                                    return updatedDocument;
                                });

                        // Save to Es processed data:
                        log.info("Updated {} records", updatedData.count());
                        JavaEsSpark.saveToEs(updatedData, esIndex, Collections.singletonMap("es.mapping.id", "id"));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("Something went wrong while future waiting:\n {}", throwable.getMessage());
                    }

                }, executorService);
    }
}
