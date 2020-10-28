package com.hronosf.searchengine.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
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

    @Value("${spark.serializer.buffer}")
    private String sparkSerializerBufferSize;

    @Value("${app.name}")
    private String appName;

    @Setter(onMethod = @__(@Autowired))
    private Environment environment;

    // =======================> Bean definitions:
    @Bean(destroyMethod = "close")
    public JavaSparkContext sc() throws ClassNotFoundException {

        SparkConf conf = new SparkConf();
        conf.set("es.nodes", esHots)
                .set("es.port", esPort)
                .set("es.index.auto.create", esIndexAutoCreate)
                .set("es.index.read.missing.as.empty", "true")
                .set("spark.master", sparkMaster)
                .set("spark.submit.deployMode", "client")
                .set("spark.driver.bindAddress", "0.0.0.0")
                .set("spark.es.nodes.wan.only", esNodesWanOnly)
                .set("spark.kryoserializer.buffer", sparkSerializerBufferSize)
                .set("spark.kryo.registrator", "com.hronosf.searchengine.serializers.CustomKryoRegistrator")
                .setAppName(appName);

        if (Arrays.asList(environment.getActiveProfiles()).contains("docker")) {
            String[] jars = {
                    "libs/elasticsearch-spark-20_2.11-7.6.2.jar",
                    "libs/search-engine-0.0.1-all.jar"
            };

            Arrays.stream(jars)
                    .forEach(jarPath -> log.info("Attaching jar with path in container: " + jarPath));

            conf.setJars(jars);
        }

        return JavaSparkContext.fromSparkContext(SparkContext.getOrCreate(conf));
    }
}
