package com.hronosf.crawler.services.impl;

import com.hronosf.crawler.jobs.crawler.SequentialCrawlerJob;
import com.hronosf.crawler.services.TaskOrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskOrchestrationServiceImpl implements TaskOrchestrationService {

    @Value("${vk.request.timeout}")
    private String requestTimeout;

    private final ExecutorService executorService;
    private final Map<String, Future<?>> crawlerTasks = new ConcurrentHashMap<>();

    @Override
    public void startRecursiveCrawlingJob(List<String> wallsToParse) {
        crawlerTasks.forEach((domain, future) -> {

            if (!wallsToParse.contains(domain)) {
                log.info("Interrupting crawling of https://vk.com/{}", domain);
                future.cancel(true);

            } else if (!future.isDone() && !future.isCancelled()) {

                log.info("Crawling https://vk.com/{} already running, ignore it's crawling", domain);
                wallsToParse.remove(domain);
            }

        });

        wallsToParse.forEach(this::startCrawling);
    }

    @Override
    public void relaunchCrawlerFinishedTask() {
        log.info("Checking for relaunch crawler jobs:");
        crawlerTasks.forEach((domain, future) -> {

            if (future.isDone() || future.isCancelled()) {
                log.info("Re-launch crawling https://vk.com/{}", domain);
                startCrawling(domain);
            }

        });
    }

    private void startCrawling(String domain) {
        // submit crawler task to Executor Service with WorkStealingThreadPool:
        Future<?> future = executorService.submit(new SequentialCrawlerJob(domain, Integer.parseInt(requestTimeout)));
        crawlerTasks.put(domain, future);
    }
}
