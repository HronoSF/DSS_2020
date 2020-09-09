package com.hronosf.crawler.jobs.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.crawler.mappers.WallPostMapper;
import com.hronosf.crawler.services.ElasticSearchWrapperService;
import com.hronosf.crawler.util.BeanUtilService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.queries.execute.ExecuteCodeQuery;

public class ExecuteCrawlerJob implements Runnable {

    private final String domain;
    private final Integer timeout;

    // "autowired" beans:
    private final ExecuteCodeQuery query;
    private final WallPostMapper mapper;
    private final ObjectMapper objectMapper;
    private final ElasticSearchWrapperService elasticSearchWrapperService;

    // constructor:
    public ExecuteCrawlerJob(String domain, Integer timeout) {
        this.domain = domain;
        this.timeout = timeout;

        // "autowire" beans:
        mapper = BeanUtilService.getBean(WallPostMapper.class);
        objectMapper = BeanUtilService.getBean(ObjectMapper.class);
        elasticSearchWrapperService = BeanUtilService.getBean(ElasticSearchWrapperService.class);

        // build query:
        query = BeanUtilService.getBean(VkApiClient.class)
                .execute()
                .code(BeanUtilService.getBean(UserActor.class), null);
    }


    @Override
    public void run() {

    }
}
