package com.hronosf.dataprocessing.services;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@NoArgsConstructor
public abstract class AbstractDataProcessingServiceConnector {

    @Value("${elasticsearch.index}")
    protected String esIndex;

    @Setter(onMethod = @__(@Autowired))
    protected JavaSparkContext sc;

    @Setter(onMethod = @__(@Autowired))
    protected ExecutorService executorService;
}
