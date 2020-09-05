package com.hronosf.crawler.jobs;

import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.mappers.WallPostMapper;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class VkParserTestScheduledJob {

    private final VkApiClient client;
    private final WallPostMapper mapper;
    private final ServiceActor serviceActor;

    public Runnable processWallParsing(String domain, Map<String, List<WallPost>> mapThreadNameToParsed) {
        return () -> {
            try {
                String currentThreadName = Thread.currentThread().getName();
                log.info("Start parsing domain {} with offset {} in {}, thread {}", domain, 0, ZonedDateTime.now(), currentThreadName);
                GetResponse response = client.wall()
                        .get(serviceActor)
                        .domain(domain)
                        .offset(0)
                        .count(100)
                        .execute();

                mapThreadNameToParsed.putIfAbsent(currentThreadName, new ArrayList<>());

                response.getItems().forEach(item -> mapThreadNameToParsed.get(currentThreadName).add(mapper.fromDto(item)));
            } catch (ApiException e) {
                // handle
            } catch (ClientException e) {
                e.printStackTrace();
            }
        };
    }
}
