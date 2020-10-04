package com.hronosf.crawler.jobs.crawler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hronosf.crawler.dto.vk.VkResponseDto;
import com.hronosf.crawler.jobs.crawler.AbstractCrawlerJob;
import com.hronosf.crawler.util.BeanUtilService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SequentialCrawlerJob extends AbstractCrawlerJob {

    private final WallGetQuery query;

    public SequentialCrawlerJob(String domain, Integer timeout) {
        super(domain, timeout);

        // build query:
        query = BeanUtilService.getBean(VkApiClient.class)
                .wall()
                .get(BeanUtilService.getBean(ServiceActor.class))
                .domain(domain)
                .count(100);
    }

    @Override
    protected void moveOffset() {
        this.offset += 100;
    }

    @Override
    protected VkResponseDto executeCrawlingLogic() throws ClientException, JsonProcessingException {
        // log start of process with work thread name:
        log.info("Start parsing https://vk.com/{} with offset {}, work-thread {}", domain, offset, Thread.currentThread().getName());

        // make response via VK API wall.get method:
        String content = query.offset(offset).executeAsString();
        return objectMapper.readValue(content, VkResponseDto.class);
    }
}
