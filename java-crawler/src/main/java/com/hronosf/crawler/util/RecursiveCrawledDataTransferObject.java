package com.hronosf.crawler.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.crawler.mappers.WallPostMapper;
import com.hronosf.crawler.repository.CrawledPostRepository;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecursiveCrawledDataTransferObject {

    private final Integer offset;
    private final String domain;
    private final WallGetQuery query;
    private final int requestTimeout;
    private final WallPostMapper mapper;
    private final ObjectMapper objectMapper;
    private final CrawledPostRepository crawledPostRepository;
}
