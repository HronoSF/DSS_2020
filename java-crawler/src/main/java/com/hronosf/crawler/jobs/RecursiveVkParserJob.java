package com.hronosf.crawler.jobs;

import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.mappers.WallPostMapper;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.RecursiveTask;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("java:S1948")
public class RecursiveVkParserJob extends RecursiveTask<Map<String, List<WallPost>>> implements Runnable {

    private final String domain;
    private Integer offset = 0;
    private final VkApiClient client;
    private final WallPostMapper mapper;
    private final ServiceActor serviceActor;
    private final Map<String, List<WallPost>> workTreadToResult;

    @Override
    public void run() {
        compute();
    }

    @Override
    protected Map<String, List<WallPost>> compute() {
        parseWallWithOffset(offset);
        return workTreadToResult;
    }

    @SneakyThrows
    private void parseWallWithOffset(Integer offset) {
        if (offset >= 200) {
            return;
        }

        String currentThreadName = Thread.currentThread().getName();
        log.info("Start parsing domain {} with offset {} in {}, thread {}", domain, offset, ZonedDateTime.now(), currentThreadName);

        GetResponse response = client.wall()
                .get(serviceActor)
                .domain(domain)
                .offset(offset)
                .count(100)
                .execute();

        workTreadToResult.putIfAbsent(currentThreadName, new ArrayList<>());

        response.getItems().forEach(item -> workTreadToResult.get(currentThreadName).add(mapper.fromDto(item)));

        this.offset += 100;
        parseWallWithOffset(this.offset);
    }
}
