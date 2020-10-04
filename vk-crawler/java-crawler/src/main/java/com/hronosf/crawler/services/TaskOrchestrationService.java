package com.hronosf.crawler.services;

import com.hronosf.crawler.controller.StartParsingAsServiceRequestDTO;
import com.hronosf.crawler.controller.StartParsingAsUserRequestDTO;

import java.util.List;
import java.util.Map;

public interface TaskOrchestrationService {

    Map<String, String> startSequentialCrawlingJob(List<String> wallsToParse);

    Map<String, String> startExecutorCrawlingJob(StartParsingAsUserRequestDTO request);

    void relaunchCrawlerFinishedTask();
}
