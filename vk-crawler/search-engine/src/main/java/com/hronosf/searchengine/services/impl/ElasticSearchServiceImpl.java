package com.hronosf.searchengine.services.impl;

import com.hronosf.search.*;
import com.hronosf.searchengine.services.ElasticSearchService;
import com.hronosf.searchengine.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final JavaSparkContext sc;

    @Value(
            "#{T(com.hronosf.searchengine.util.ResourceReader).readFileToString('classpath:query/es_query.json')}"
    )
    private String esQuery;

    @Value("${elasticsearch.index}")
    private String esIndex;

    @Override
    public SearchResponse searchWithText(SearchRequest request) {
        String query = esQuery
                .replaceAll("%text%", request.getTextToSearch())
                .replace("%size%", String.valueOf(request.getSize()))
                .replace("%page%", String.valueOf(request.getPage()));

        log.info("Used query: {}", query);

        List<WallPost> searchResults = JavaEsSpark.esRDD(sc, esIndex, query)
                .values()
                .map(Utils::mapToPost)
                .collect();

        log.info("Extracted {} documents from Elastic Search", searchResults.size());

        return SearchResponse.newBuilder().addAllContent(searchResults).build();
    }
}
