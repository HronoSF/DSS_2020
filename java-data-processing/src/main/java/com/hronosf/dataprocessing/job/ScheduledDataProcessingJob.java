package com.hronosf.dataprocessing.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledDataProcessingJob {

    private final JavaSparkContext sc;

    @Async
    @Scheduled(cron = "0/10 * * * * ?")
    public void get() {
        //TODO: logic - 1 time process all data from db, then only new/ edited. ADD meta field: process_by_neural_networks - time when document were processed.
        //TODO: elastic search query for smart extracting after 1 time

        JavaRDD<Map<String, Object>> modifiedData = JavaEsSpark.esRDD(sc, "wall_posts", "?q=*")
                .map(pair -> {
                    Map<String, Object> map = pair._2();

                    map.put("summary", stubNeuralNetWorksSummarization(map));
                    map.put("personToAttitude", stubNeuralNetWorksPersonToAttitude(map));

                    return map;
                });

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
