package com.hronosf.crawler.jobs.scheduled;

import com.hronosf.crawler.services.EmailSenderService;
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

    private final EmailSenderService emailSenderService;
    private final TaskOrchestrationService taskOrchestrationService;

    @Async
    @Scheduled(cron = "${cron.expression}")
    public void relaunchAllFinishedOrCancelledCrawlerTasks() {
        taskOrchestrationService.relaunchCrawlerFinishedTask();
    }

    @Async
    @Scheduled(cron = "0 0/12 * * * ?")
    public void sendEmailWithReasonsOfCrawlerFall() {
        // check if we have errors in crawler && send messages if it's are:
        emailSenderService.sendMessageAboutCrawlerFailureToAdmin();
    }
}
