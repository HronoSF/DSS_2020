package com.hronosf.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

        return new ConcurrentTaskScheduler(Executors.newWorkStealingPool(), scheduledThreadPool);
    }
}
