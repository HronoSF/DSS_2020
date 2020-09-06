package com.hronosf.crawler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.*;

@EnableAsync
@EnableScheduling
@SpringBootApplication(exclude = TaskSchedulingAutoConfiguration.class)
public class CrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

    @Bean("ConcurrentStealingScheduler")
    public TaskScheduler concurrentStealingScheduler() {
        ScheduledExecutorService scheduledThreadPool = Executors
                .newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        return new ConcurrentTaskScheduler(stealingThreadPoolExecutor(), scheduledThreadPool);
    }

    @Bean
    public ExecutorService stealingThreadPoolExecutor() {
        return Executors.newWorkStealingPool();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
        return mapper;
    }
}
