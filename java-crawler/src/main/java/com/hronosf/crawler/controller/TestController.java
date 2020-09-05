package com.hronosf.crawler.controller;

import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.jobs.RecursiveVkParserJob;
import com.hronosf.crawler.mappers.WallPostMapper;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final VkApiClient client;
    private final WallPostMapper mapper;
    private final ServiceActor serviceActor;

    private final TaskScheduler scheduler;
    private final Map<String, List<WallPost>> mapThreadNameToParsed = new ConcurrentHashMap<>();

    @PostMapping("/api")
    public void schedule(@RequestBody List<String> toParse) {
        toParse.forEach(resource ->
                scheduler.schedule(
                        new RecursiveVkParserJob(resource, client, mapper, serviceActor, mapThreadNameToParsed),
                        new CronTrigger("*/5 * * * * *"))
        );
    }

    @GetMapping("/api")
    public Map<String, List<WallPost>> getRes() {
        return mapThreadNameToParsed;
    }
}
