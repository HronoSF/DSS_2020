package com.hronosf.dataprocessing.configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SparkConfiguration {

    @Bean(destroyMethod = "close")
    public JavaSparkContext sc() {

        SparkConf conf = new SparkConf();
        conf.set("spark.master", "local")
                .set("es.nodes", "localhost")
                .set("es.write.operation", "upsert")
                .set("spark.es.nodes.wan.only", "true")
                .set("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
                .setAppName("Data Processing Service");

        return new JavaSparkContext(conf);
    }
}
