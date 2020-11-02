package com.hronosf.dataprocessing.services.impl;

import com.google.common.util.concurrent.*;
import com.hronosf.dataprocessing.services.AbstractDataProcessingServiceConnector;
import com.hronosf.dataprocessing.services.RelationshipExtractorServiceConnector;
import com.hronosf.relationship.extractor.*;
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
public class RelationshipExtractorServiceConnectorImpl extends AbstractDataProcessingServiceConnector
        implements RelationshipExtractorServiceConnector {

    @GrpcClient("relation-extractor-grpc-server")
    private RelationshipExtractorGrpc.RelationshipExtractorFutureStub relationExtractorServiceStub;

    public void processData(JavaRDD<Map<String, Object>> toSummarize) {

        // use spark to map to grpc message entry:
        List<TextToExtract> textToRelationMap = toSummarize
                .map(map -> {
                    Object text = map.get("text");

                    return TextToExtract
                            .newBuilder()
                            .setId(map.get("id").toString())
                            .setText(text == null ? StringUtils.EMPTY : text.toString())
                            .build();
                })
                .collect();

        // build grpc message:
        RelationshipExtractorRequestDTO request = RelationshipExtractorRequestDTO.newBuilder()
                .addAllTextToExtract(textToRelationMap)
                .build();

        // send data to neural net and get future:
        log.info("Calling for relation extraction of {} documents", request.getTextToExtractList().size());
        ListenableFuture<RelationshipExtractorResponseDTO> relationsFuture = relationExtractorServiceStub.extractRelation(request);

        // add callback to future - update ES when neural net will return data:
        Futures.addCallback(
                relationsFuture,
                new FutureCallback<RelationshipExtractorResponseDTO>() {

                    @Override
                    public void onSuccess(@NullableDecl RelationshipExtractorResponseDTO relationsResponse) {
                        // return if response empty:do
                        if (relationsResponse == null || relationsResponse.getDataToUpdateList() == null) {
                            log.error("Something went wrong, future returned null or response list is null");
                            return;
                        }

                        // Prepare data to update with spark:
                        JavaRDD<Object> updatedData = sc
                                .parallelize(relationsResponse.getDataToUpdateList())
                                .map(response -> {
                                    Map<String, Object> updatedDocument = new HashMap<>();
                                    updatedDocument.put("id", response.getId());
                                    updatedDocument.put("relationMap", response.getRelationMap());
                                    updatedDocument.put("processedIn", response.getProcessedIn());

                                    return updatedDocument;
                                });

                        // Save to Es processed data:
                        log.info("Updated {} records", updatedData.count());
                        JavaEsSpark.saveToEs(updatedData, esIndex, Collections.singletonMap("es.mapping.id", "id"));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("Something went wrong while relationship extraction future waiting:\n {}", throwable.getMessage());
                    }

                }, executorService);
    }
}
