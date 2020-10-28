package com.hronosf.crawler.jobs.crawler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hronosf.crawler.dto.vk.VkResponseDto;
import com.hronosf.crawler.jobs.crawler.AbstractCrawlerJob;
import com.hronosf.crawler.util.BeanUtilService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ExecuteCrawlerJob extends AbstractCrawlerJob {

    // "autowired" beans:
    private final UserActor userActor;
    private final VkApiClient vkApiClient;

    private int iterationOffsetBorder = 0;

    private static final String GET_WALL_POST_EXECUTE_TEMPLATE =
            "API.wall.get({\"domain\": %s, \"offset\":%s, \"count\":100});";

    public ExecuteCrawlerJob(String domain, Integer timeout) {
        super(domain, timeout);

        // "autowire" beans:
        userActor = BeanUtilService.getBean(UserActor.class);
        vkApiClient = BeanUtilService.getBean(VkApiClient.class);

        // log start of process with work thread name:
        log.info("Start parsing https://vk.com/{} offsets range {}-{}, work-thread {}"
                , domain, offset, iterationOffsetBorder, Thread.currentThread().getName());
    }

    @Override
    protected void moveOffset() {
        this.offset += iterationOffsetBorder;
    }

    @Override
    protected VkResponseDto executeCrawlingLogic() throws ClientException, JsonProcessingException, ApiException {
        // make response via VK API wall.get method:
        iterationOffsetBorder = getIterationOffsetBorder();

        String code = IntStream.range(offset, iterationOffsetBorder)
                .boxed()
                .map(i -> String.format(GET_WALL_POST_EXECUTE_TEMPLATE, domain, i * 100))
                .collect(Collectors.joining("\n"));

        String content = vkApiClient.execute()
                .code(userActor, code)
                .execute()
                .getAsString();

        return objectMapper.readValue(content, VkResponseDto.class);
    }

    private int getIterationOffsetBorder() {
        // get current offset:
        int offsetBound = offset;
        int currentOffset = offset;

        // raise offset until we finish parsing or until we get offsets for 20 lines:
        while (wallPostCount < currentOffset + offsetBound && offsetBound - currentOffset <= 200) {
            offsetBound++;
        }
        return offsetBound;
    }
}