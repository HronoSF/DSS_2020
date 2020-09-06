package com.hronosf.crawler.controller;

import com.hronosf.crawler.dto.StartCrawlingRequestDto;
import com.hronosf.crawler.services.TaskOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TaskOrchestrationService taskOrchestrator;

    @PostMapping("/api")
    public void schedule(@RequestBody List<String> toParse) {
        taskOrchestrator.startRecursiveCrawlingJob(toParse);
    }

    @PostMapping("/api_user")
    public void schedule(@RequestBody StartCrawlingRequestDto request) {
        taskOrchestrator.startRecursiveCrawlingJob(request);
    }
}
