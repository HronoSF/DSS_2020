package com.hronosf.dataprocessing.services;

import org.apache.spark.api.java.JavaRDD;

import java.util.Map;

public interface RelationshipExtractorServiceConnector {

    void processData(JavaRDD<Map<String, Object>> toSummarize);
}
