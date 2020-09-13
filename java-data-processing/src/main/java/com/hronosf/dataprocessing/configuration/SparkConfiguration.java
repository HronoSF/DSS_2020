package com.hronosf.dataprocessing.configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootConfiguration
public class SparkConfiguration {

    @Bean(destroyMethod = "close")
    @Scope("singleton")
    public JavaSparkContext sc() {

        SparkConf conf = new SparkConf();
        conf.set("spark.master", "local")
                .set("es.nodes", "localhost")
                .set("es.write.operation", "upsert")
                .set("spark.es.nodes.wan.only", "true")
                .set("spark.kryoserializer.buffer", "128m")
                .set("es.index.auto.create", "false")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .setAppName("Data Processing Service");

        return new JavaSparkContext(conf);
    }
}
