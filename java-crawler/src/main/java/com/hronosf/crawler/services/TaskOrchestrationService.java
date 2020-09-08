package com.hronosf.crawler.services;

import java.util.List;

public interface TaskOrchestrationService {

    void startRecursiveCrawlingJob(List<String> wallsToParse);

    void relaunchCrawlerFinishedTask();
}
