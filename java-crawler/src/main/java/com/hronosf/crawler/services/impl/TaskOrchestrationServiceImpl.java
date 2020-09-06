package com.hronosf.crawler.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.crawler.dto.StartCrawlingRequestDto;
import com.hronosf.crawler.jobs.RecursiveCrawlerJob;
import com.hronosf.crawler.mappers.WallPostMapper;
import com.hronosf.crawler.repository.CrawledPostRepository;
import com.hronosf.crawler.services.TaskOrchestrationService;
import com.hronosf.crawler.util.RecursiveCrawledDataTransferObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskOrchestrationServiceImpl implements TaskOrchestrationService {

    @Value("${vk.request.timeout}")
    private String requestTimeout;

    private RecursiveCrawledDataTransferObject.RecursiveCrawledDataTransferObjectBuilder queryTemplate;

    private final VkApiClient client;
    private final WallPostMapper mapper;
    private final TaskScheduler scheduler;
    private final ServiceActor serviceActor;
    private final ObjectMapper objectMapper;
    private final CrawledPostRepository repository;
    private final ExecutorService executorService;

    @PostConstruct
    public void buildDataObject() {
        // build template data:
        queryTemplate = RecursiveCrawledDataTransferObject.builder()
                .mapper(mapper)
                .objectMapper(objectMapper)
                .crawledPostRepository(repository)
                .requestTimeout(Integer.parseInt(requestTimeout));
    }

    public void startRecursiveCrawlingJob(List<String> wallsToParse) {
        List<Runnable> killedRunnable = executorService.shutdownNow();
        log.info("Force shutdown all running crawler threads: {}", killedRunnable);

        log.info("Preparing to parse new domains: {}", wallsToParse);
        wallsToParse.forEach(domain -> {
                    // build query template:
                    WallGetQuery query = client.wall()
                            .get(serviceActor)
                            .domain(domain)
                            .count(100);

                    queryTemplate.query(query).domain(domain);

                    // start parsing:
                    scheduler.schedule(new RecursiveCrawlerJob(queryTemplate.build()), new Date());
                }
        );
    }

    @Override
    public void startRecursiveCrawlingJob(StartCrawlingRequestDto request) {
        // create actor for API:
        UserActor actor = new UserActor(request.getUserId(), request.getAccessToken());
    }
}
