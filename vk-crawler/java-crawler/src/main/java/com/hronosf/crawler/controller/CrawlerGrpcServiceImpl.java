package com.hronosf.crawler.controller;

import com.hronosf.crawler.controller.CrawlerGrpc.CrawlerImplBase;
import com.hronosf.crawler.services.TaskOrchestrationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;

@GrpcService
@RequiredArgsConstructor
public class CrawlerGrpcServiceImpl extends CrawlerImplBase {

    private final TaskOrchestrationService taskOrchestrationService;

    @Override
    public void startCrawling(StartParsingRequestDTO request, StreamObserver<CrawlerJobStatusDTO> responseObserver) {
        // start crawling:
        Map<String, String> crawlingJobsStatuses = taskOrchestrationService.startRecursiveCrawlingJob(request.getToParseList());

        // build response:
        CrawlerJobStatusDTO response = CrawlerJobStatusDTO.newBuilder()
                .putAllDomainToStatus(crawlingJobsStatuses)
                .build();

        // return response:
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
