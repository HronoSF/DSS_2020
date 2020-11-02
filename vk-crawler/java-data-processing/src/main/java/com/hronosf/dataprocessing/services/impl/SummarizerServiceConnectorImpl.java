package com.hronosf.dataprocessing.services.impl;

import com.google.common.util.concurrent.*;
import com.hronosf.dataprocessing.services.AbstractDataProcessingServiceConnector;
import com.hronosf.dataprocessing.services.SummarizerServiceConnector;
import com.hronosf.summarizer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummarizerServiceConnectorImpl extends AbstractDataProcessingServiceConnector implements SummarizerServiceConnector {

    @GrpcClient("summarizer-grpc-server")
    private SummarizerGrpc.SummarizerFutureStub summarizationServiceStub;

    public void processData(JavaRDD<Map<String, Object>> toSummarize) {
        // use spark to map to grpc message entry:
        List<TextToSummarize> textToSummaries = toSummarize
                .map(map -> {
                    Object text = map.get("text");

                    return TextToSummarize
                            .newBuilder()
                            .setId(map.get("id").toString())
                            .setText(text == null ? StringUtils.EMPTY : text.toString())
                            .build();
                })
                .collect();

        // build grpc message:
        SummarizeRequestDTO request = SummarizeRequestDTO.newBuilder()
                .addAllTextToSummarize(textToSummaries)
                .build();

        // send data to neural net and get future:
        log.info("Calling for summarization of {} documents", request.getTextToSummarizeList().size());
        ListenableFuture<SummarizeResponseDTO> summarizeFuture = summarizationServiceStub.summarize(request);

        // add callback to future - update ES when neural net will return data:
        Futures.addCallback(
                summarizeFuture,
                new FutureCallback<SummarizeResponseDTO>() {

                    @Override
                    public void onSuccess(@NullableDecl SummarizeResponseDTO summarizeResponse) {
                        // return if response empty:do
                        if (summarizeResponse == null || summarizeResponse.getDataToUpdateList() == null) {
                            log.error("Something went wrong, future returned null or response list is null");
                            return;
                        }

                        // Prepare data to update with spark:
                        JavaRDD<Object> updatedData = sc
                                .parallelize(summarizeResponse.getDataToUpdateList())
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
                        log.error("Something went wrong while summarization future waiting:\n {}", throwable.getMessage());
                    }

                }, executorService);
    }
}
