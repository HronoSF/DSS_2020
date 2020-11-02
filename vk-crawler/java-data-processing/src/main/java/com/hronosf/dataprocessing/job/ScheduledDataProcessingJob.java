package com.hronosf.dataprocessing.job;

import com.hronosf.dataprocessing.services.RelationshipExtractorServiceConnector;
import com.hronosf.dataprocessing.services.SummarizerServiceConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledDataProcessingJob {

    @Value(
            "#{T(com.hronosf.dataprocessing.util.ResourceReader).readFileToString('classpath:query/es_query.json')}"
    )
    private String esQuery;

    @Value("${elasticsearch.index}")
    private String esIndex;

    private final JavaSparkContext sc;
    private final SummarizerServiceConnector summarizerConnector;
    private final RelationshipExtractorServiceConnector relationshipExtractorConnector;

    @Async
    @Scheduled(cron = "${cron.expression}")
    public void processEsData() {
        // extract data from Es with spark:
        JavaRDD<Map<String, Object>> wallPosts = sc.parallelize(
                JavaEsSpark
                        .esRDD(sc, esIndex, esQuery)
                        .values()
                        .take(2000)
        );

        long postCount = wallPosts.count();
        log.info("Extracted {} documents from ES", postCount);

        // skip if nothing to process:
        if (postCount == 0) {
            return;
        }

        // call for neural nets work:
        summarizerConnector.processData(wallPosts);
        relationshipExtractorConnector.processData(wallPosts);
    }
}
