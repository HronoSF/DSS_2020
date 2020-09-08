package com.hronosf.crawler.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.crawler.jobs.crawler.SequentialCrawlerJob;
import com.hronosf.crawler.mappers.WallPostMapper;
import com.hronosf.crawler.repository.CrawledPostRepository;
import com.hronosf.crawler.services.TaskOrchestrationService;
import com.hronosf.crawler.util.DataTransferObject;
import com.hronosf.crawler.util.DataTransferObject.DataTransferObjectBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskOrchestrationServiceImpl implements TaskOrchestrationService {

    @Value("${vk.request.timeout}")
    private String requestTimeout;

    private final VkApiClient client;
    private final WallPostMapper mapper;
    private final ServiceActor serviceActor;
    private final ObjectMapper objectMapper;
    private final CrawledPostRepository repository;
    private final ExecutorService executorService;

    private DataTransferObjectBuilder queryTemplate;
    private final Map<String, Future<?>> crawlerTasks = new ConcurrentHashMap<>();

    @PostConstruct
    public void buildDataObject() {
        // build template query:
        queryTemplate = DataTransferObject.builder()
                .mapper(mapper)
                .objectMapper(objectMapper)
                .crawledPostRepository(repository)
                .requestTimeout(Integer.parseInt(requestTimeout));
    }

    @Override
    public void startRecursiveCrawlingJob(List<String> wallsToParse) {
        crawlerTasks.forEach((domain, future) -> {
            log.info("Interrupting crawling of https://vk.com/{}", domain);
            future.cancel(true);
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
        // build query template:
        WallGetQuery query = client.wall()
                .get(serviceActor)
                .domain(domain)
                .count(100);

        // it done to log domain in crawling job:
        queryTemplate.query(query).domain(domain);

        // start parsing:
        Future<?> future = executorService.submit(new SequentialCrawlerJob(queryTemplate.build()));
        crawlerTasks.put(domain, future);
    }
}
