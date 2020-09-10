package com.hronosf.crawler.controller;

import com.hronosf.crawler.services.TaskOrchestrationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;

@GrpcService
@RequiredArgsConstructor
public class CrawlerGrpcServiceImpl extends com.hronosf.crawler.controller.CrawlerGrpc.CrawlerImplBase {

    private final TaskOrchestrationService taskOrchestrationService;

    @Override
    public void startCrawling(com.hronosf.crawler.controller.StartParsingRequest request, StreamObserver<com.hronosf.crawler.controller.CrawlerJobStatus> responseObserver) {
        // start crawling:
        Map<String, String> domainToStatus = taskOrchestrationService.startRecursiveCrawlingJob(request.getToParseList());

        // build response:
        com.hronosf.crawler.controller.CrawlerJobStatus response = com.hronosf.crawler.controller.CrawlerJobStatus.newBuilder()
                .putAllDomainToStatus(domainToStatus)
                .build();

        // return response:
        responseObserver.onNext(response);
    }
}
