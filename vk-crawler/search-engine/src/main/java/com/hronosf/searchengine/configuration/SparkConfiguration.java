package com.hronosf.searchengine.configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SparkConfiguration {

    // =======================> ES settings:
    @Value("${elasticsearch.host}")
    private String esHots;

    @Value("${elasticsearch.port}")
    private String esPort;

    @Value("${elasticsearch.index.autocreate}")
    private String esIndexAutoCreate;

    @Value("${elasticsearch.nodes.wan}")
    private String esNodesWanOnly;

    // =======================> Spark-ES settings:
    @Value("${spark.master}")
    private String sparkMaster;

    @Value("${spark.serializer}")
    private String sparkSerializer;

    @Value("${spark.serializer.buffer}")
    private String sparkSerializerBufferSize;

    @Value("${app.name}")
    private String appName;

    // =======================> Bean definitions:
    @Bean(destroyMethod = "close")
    public JavaSparkContext sc() {

        SparkConf conf = new SparkConf();
        conf.set("es.nodes", esHots)
                .set("es.port", esPort)
                .set("spark.master", sparkMaster)
                .set("es.index.auto.create", esIndexAutoCreate)
                .set("spark.es.nodes.wan.only", esNodesWanOnly)
                .set("spark.kryoserializer.buffer", sparkSerializerBufferSize)
                .set("spark.serializer", sparkSerializer)
                .set("spark.kryo.registrator", "com.hronosf.searchengine.serializers.CustomKryoRegistrator")
                .setAppName(appName);

        return new JavaSparkContext(conf);
    }
}
