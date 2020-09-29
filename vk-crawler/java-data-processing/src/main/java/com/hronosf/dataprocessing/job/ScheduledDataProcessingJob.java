package com.hronosf.dataprocessing.job;

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
            "#{T(com.hronosf.dataprocessing.util.ResourceReader).readFileToString('classpath:es_query.json')}"
    )
    private String esQuery;

    @Value("${elasticsearch.index}")
    private String esIndex;

    private final JavaSparkContext sc;
    private final SummarizerServiceConnector summarizerConnector;

    @Async
    @Scheduled(cron = "${cron.expression}")
    public void processEsData() {
        // extract data from Es with spark:
        JavaRDD<Map<String, Object>> wallPosts = JavaEsSpark
                .esRDD(sc, esIndex, esQuery)
                .values();

        log.info("Extracted {} documents from ES", wallPosts.count());

        // call for neural nets work:
        summarizerConnector.summarizeText(wallPosts);
    }
}
