package com.hronosf.searchengine.services.impl;

import com.hronosf.search.*;
import com.hronosf.searchengine.services.ElasticSearchService;
import com.hronosf.searchengine.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final JavaSparkContext sc;

    @Value(
            "#{T(com.hronosf.searchengine.util.ResourceReader).readFileToString('classpath:query/es_full_text_query.json')}"
    )
    private String esFullTextQuery;

    @Value(
            "#{T(com.hronosf.searchengine.util.ResourceReader).readFileToString('classpath:query/es_id_query.json')}"
    )
    private String esIdQuery;

    @Value("${elasticsearch.index}")
    private String esIndex;

    @Override
    public TextSearchResponseDTO searchWithText(TestSearchRequestDTO request) {
        // set id instead of placeholders in es-query:
        String query = esFullTextQuery.replace("%s", request.getTextToSearch());

        // query Es with Spark && map results:
        List<WallPost> searchResults = JavaEsSpark.esRDD(sc, esIndex, query)
                .values()
                .map(Utils::mapToFullSearchWallPost)
                .take(request.getSize() * request.getPage() <= 0 ? 1 : request.getPage())
                .stream()
                .skip(request.getPage() <= 1 ? 0 : request.getSize() * (request.getPage() - 1))
                .collect(Collectors.toList());


        log.info("Extracted {} documents from Elastic Search", searchResults.size());

        return TextSearchResponseDTO.newBuilder().addAllContent(searchResults).build();
    }

    @Override
    public IdSearchResponseDTO searchWithId(IdSearchRequestDTO request) {
        if (!request.getIdToSearch().contains("/id")) {
            return IdSearchResponseDTO.newBuilder().build();
        }
        // extract user/group id:
        String userId = StringUtils.substringAfter(request.getIdToSearch(), "/id");
        // set id instead of placeholders in es-query:
        String query = esIdQuery.replaceAll("%s", userId);

        // query Es with Spark:
        JavaRDD<Map<String, Object>> extractedDocs = JavaEsSpark.esRDD(sc, esIndex, query)
                .values();

        log.info("Extracted {} documents from Elastic Search", extractedDocs.count());

        // map results:
        return Utils.mapToIdSearchWallPost(userId, extractedDocs);
    }
}
