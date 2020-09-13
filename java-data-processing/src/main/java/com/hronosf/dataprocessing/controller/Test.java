package com.hronosf.dataprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Test {

    private final JavaSparkContext sc;
    private final ObjectMapper mapper;

    @GetMapping("/api")
    public void get() {
        //TODO: modify summary && personToAttitude via sending protobuf to neural nets and pass result to es
        JavaEsSpark.
                esRDD(sc, "wall_posts", "?q=*").map(x -> x._2);
    }
}
