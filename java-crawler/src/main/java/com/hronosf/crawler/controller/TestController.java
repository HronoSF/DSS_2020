package com.hronosf.crawler.controller;

import com.hronosf.crawler.services.TaskOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TaskOrchestrationService taskOrchestrator;

    @PostMapping("/service_actor")
    public void schedule(@RequestBody List<String> toParse) {
        taskOrchestrator.startRecursiveCrawlingJob(toParse);
    }
}
