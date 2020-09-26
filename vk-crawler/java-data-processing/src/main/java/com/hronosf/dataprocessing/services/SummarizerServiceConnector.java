package com.hronosf.dataprocessing.services;

import org.apache.spark.api.java.JavaRDD;

import java.util.Map;

public interface SummarizerServiceConnector {

    void summarizeDocuments(JavaRDD<Map<String, Object>> toSummarize);
}
