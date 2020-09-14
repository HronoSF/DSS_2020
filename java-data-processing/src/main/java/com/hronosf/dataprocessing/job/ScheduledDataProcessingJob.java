package com.hronosf.dataprocessing.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledDataProcessingJob {

    @Value(
            "#{T(com.hronosf.dataprocessing.util.ResourceReader).readFileToString('classpath:es_query.json')}"
    )
    private String esQuery;

    private final JavaSparkContext sc;

    @Async
    @Scheduled(cron = "* 0/15 * * * ?")
    public void get() {
        JavaRDD<Map<String, Object>> modifiedData = JavaEsSpark.esRDD(sc, "wall_posts", esQuery)
                .map(pair -> {
                    Map<String, Object> map = pair._2();

                    map.put("summary", stubNeuralNetWorksSummarization(map));
                    map.put("personToAttitude", stubNeuralNetWorksPersonToAttitude(map));
                    map.put("processedIn", Calendar.getInstance().getTimeInMillis());

                    return map;
                });

        log.info("<=========================== Updating {} documents ===========================>", modifiedData.count());

        JavaEsSpark.saveToEs(modifiedData, "wall_posts", Collections.singletonMap("es.mapping.id", "id"));
    }

    private static Map<String, String> stubNeuralNetWorksPersonToAttitude(Map<String, Object> text) {
        Map<String, String> map = new HashMap<>();

        map.put("Лисица", "Глебица любит Лисицу на " + text.get("id") + " миллионов раз в " + ZonedDateTime.now());
        map.put("Пиццита", "Глебица любит Пицциту в " + text.get("id") + " миллионов раз меньше Лисицы в " + ZonedDateTime.now());

        return map;
    }

    private static String stubNeuralNetWorksSummarization(Map<String, Object> text) {
        return "Тестовое саммари для " + text.get("id") + " в " + ZonedDateTime.now();
    }
}
