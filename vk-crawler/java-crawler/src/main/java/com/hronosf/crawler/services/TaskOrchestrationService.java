package com.hronosf.crawler.services;

import java.util.List;
import java.util.Map;

public interface TaskOrchestrationService {

    Map<String, String> startRecursiveCrawlingJob(List<String> wallsToParse);

    void relaunchCrawlerFinishedTask();
}
