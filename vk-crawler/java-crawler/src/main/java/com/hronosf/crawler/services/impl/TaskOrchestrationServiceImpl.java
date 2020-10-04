package com.hronosf.crawler.services.impl;

import com.google.protobuf.ProtocolStringList;
import com.hronosf.crawler.controller.StartParsingAsUserRequestDTO;
import com.hronosf.crawler.jobs.crawler.AbstractCrawlerJob;
import com.hronosf.crawler.jobs.crawler.impl.ExecuteCrawlerJob;
import com.hronosf.crawler.jobs.crawler.impl.SequentialCrawlerJob;
import com.hronosf.crawler.services.TaskOrchestrationService;
import com.hronosf.crawler.util.CrawlerTaskInfo;
import com.vk.api.sdk.client.actors.UserActor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
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
    private final ConfigurableApplicationContext context;
    private final Map<String, CrawlerTaskInfo> crawlerExecuteTasks = new ConcurrentHashMap<>();
    private final Map<String, CrawlerTaskInfo> crawlerSequentialTasks = new ConcurrentHashMap<>();

    @Override
    public Map<String, String> startSequentialCrawlingJob(List<String> wallsToParse) {
        Map<String, String> crawlingJobsStatuses = stopCrawlerJobs(wallsToParse, crawlerSequentialTasks);

        // start crawling:
        wallsToParse.forEach(wall -> {
            crawlingJobsStatuses.put(wall, "Crawling job started at " + ZonedDateTime.now());
            startCrawling(wall, true);
        });

        return crawlingJobsStatuses;
    }

    @Override
    public Map<String, String> startExecutorCrawlingJob(StartParsingAsUserRequestDTO request) {
        ProtocolStringList wallsToParse = request.getToParseList();

        Map<String, String> crawlingJobsStatuses = stopCrawlerJobs(wallsToParse, crawlerExecuteTasks);

        try {
            // destroy if we have user actor:
            UserActor userActorBean = context.getBean(UserActor.class);
            context.getAutowireCapableBeanFactory().destroyBean(userActorBean);

        } catch (NoSuchBeanDefinitionException e) {
            // empty block in case actor bean doesn't exists
        }

        // register new user actor bean:
        UserActor actor = new UserActor(request.getUserId(), request.getAccessKey());
        context.getBeanFactory().registerSingleton("UserActor", actor);

        // start crawling:
        wallsToParse.forEach(wall -> {
            crawlingJobsStatuses.put(wall, "Crawling job started at " + ZonedDateTime.now());
            startCrawling(wall, false);
        });

        return crawlingJobsStatuses;
    }

    @Override
    public void relaunchCrawlerFinishedTask() {
        log.info("Checking for relaunch sequential crawler jobs:");
        relaunch(crawlerSequentialTasks);

        log.info("Checking for relaunch execute crawler jobs:");
        relaunch(crawlerExecuteTasks);
    }

    private void startCrawling(String domain, boolean asAdmin) {
        // create crawling cancelable runnable job:
        AbstractCrawlerJob cancelableRunnable = asAdmin ?
                new SequentialCrawlerJob(domain, Integer.parseInt(requestTimeout))
                : new ExecuteCrawlerJob(domain, Integer.parseInt(requestTimeout));

        // submit crawler task to Executor Service with WorkStealingThreadPool:
        Future<?> future = executorService.submit(cancelableRunnable);

        // save info about started job:
        if (asAdmin) {
            crawlerSequentialTasks.put(domain, new CrawlerTaskInfo(future, cancelableRunnable));
        } else {
            crawlerExecuteTasks.put(domain, new CrawlerTaskInfo(future, cancelableRunnable));
        }
    }

    private Map<String, String> stopCrawlerJobs(List<String> wallsToParse, Map<String, CrawlerTaskInfo> jobs) {
        Map<String, String> crawlingJobsStatuses = new HashMap<>();

        // stop crawling if it's running && check is any of passed domains already parsing, if true - not interrupt them:
        jobs.forEach((domain, taskInfo) -> {

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

        return crawlingJobsStatuses;
    }

    private void relaunch(Map<String, CrawlerTaskInfo> jobs) {
        jobs.forEach((domain, taskInfo) -> {

            if (taskInfo.getFuture().isDone() || taskInfo.getFuture().isCancelled()) {
                log.info("Re-launch crawling https://vk.com/{}", domain);
                startCrawling(domain, false);
            }

        });
    }
}
