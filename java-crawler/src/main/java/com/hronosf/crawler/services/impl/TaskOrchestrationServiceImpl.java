package com.hronosf.crawler.services.impl;

import com.hronosf.crawler.jobs.crawler.SequentialCrawlerJob;
import com.hronosf.crawler.services.TaskOrchestrationService;
import com.hronosf.crawler.util.CrawlerTaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskOrchestrationServiceImpl implements TaskOrchestrationService {

    @Value("${vk.request.timeout}")
    private String requestTimeout;

    private final ExecutorService executorService;
    private final Map<String, CrawlerTaskInfo> crawlerTasks = new ConcurrentHashMap<>();

    @Override
    public Map<String, String> startRecursiveCrawlingJob(List<String> wallsToParse) {
        Map<String, String> crawlingJobsStatuses = new HashMap<>();

        // stop crawling if it's running && check is any of passed domains already parsing, if true - not interrupt them:
        crawlerTasks.forEach((domain, taskInfo) -> {

            if (!wallsToParse.contains(domain)) {
                log.info("Interrupting crawling of https://vk.com/{}", domain);
                taskInfo.getRunnable().cancel();

                crawlingJobsStatuses.put(domain, "Crawling job interrupted at " + ZonedDateTime.now());

            } else if (!taskInfo.getFuture().isDone() && !taskInfo.getFuture().isCancelled()) {
                log.info("Crawling https://vk.com/{} already running, ignore it", domain);
                wallsToParse.remove(domain);

                crawlingJobsStatuses.put(domain, "Crawling job already running" + ZonedDateTime.now());
            }
        });

        // start crawling:
        wallsToParse.forEach(wall -> {
            crawlingJobsStatuses.put(wall, "Crawling job started at " + ZonedDateTime.now());
            startCrawling(wall);
        });

        return crawlingJobsStatuses;
    }

    @Override
    public void relaunchCrawlerFinishedTask() {
        log.info("Checking for relaunch crawler jobs:");
        crawlerTasks.forEach((domain, taskInfo) -> {

            if (taskInfo.getFuture().isDone() || taskInfo.getFuture().isCancelled()) {
                log.info("Re-launch crawling https://vk.com/{}", domain);
                startCrawling(domain);
            }

        });
    }

    private void startCrawling(String domain) {
        // create crawling cancelable runnable job:
        SequentialCrawlerJob cancelableRunnable = new SequentialCrawlerJob(domain, Integer.parseInt(requestTimeout));

        // submit crawler task to Executor Service with WorkStealingThreadPool:
        Future<?> future = executorService.submit(cancelableRunnable);

        // save info about started job:
        crawlerTasks.put(domain, new CrawlerTaskInfo(future, cancelableRunnable));
    }
}
