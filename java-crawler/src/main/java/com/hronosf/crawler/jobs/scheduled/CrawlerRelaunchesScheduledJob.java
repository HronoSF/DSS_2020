package com.hronosf.crawler.jobs.scheduled;

import com.hronosf.crawler.services.TaskOrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlerRelaunchesScheduledJob {

    private final TaskOrchestrationService taskOrchestrationService;

    @Async
    @Scheduled(cron = "0 0/10 * * * ?")
    public void relaunchAllFinishedOrCancelledCrawlerTasks() {
        taskOrchestrationService.relaunchCrawlerFinishedTask();
    }
}
