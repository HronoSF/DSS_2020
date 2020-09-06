package com.hronosf.crawler.services;

import com.hronosf.crawler.dto.StartCrawlingRequestDto;

import java.util.List;

public interface TaskOrchestrationService {

    void startRecursiveCrawlingJob(List<String> wallsToParse);

    void startRecursiveCrawlingJob(StartCrawlingRequestDto request);
}
