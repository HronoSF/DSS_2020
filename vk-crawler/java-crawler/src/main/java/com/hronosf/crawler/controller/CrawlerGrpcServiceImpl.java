package com.hronosf.crawler.controller;

import com.hronosf.crawler.controller.CrawlerGrpc.CrawlerImplBase;
import com.hronosf.crawler.services.TaskOrchestrationService;
import com.hronosf.crawler.util.CrawlerStateStorage;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;

@GrpcService
@RequiredArgsConstructor
public class CrawlerGrpcServiceImpl extends CrawlerImplBase {

    private final TaskOrchestrationService taskOrchestrationService;

    @Override
    public void startCrawlingAsServiceActor(StartParsingAsServiceRequestDTO request, StreamObserver<CrawlerJobStatusDTO> responseObserver) {
        // start crawling:
        Map<String, String> crawlingJobsStatuses = taskOrchestrationService.startSequentialCrawlingJob(request.getToParseList());

        // build response:
        CrawlerJobStatusDTO response = CrawlerJobStatusDTO.newBuilder()
                .putAllDomainToStatus(crawlingJobsStatuses)
                .build();

        // return response:
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void startCrawlingAsUserActor(StartParsingAsUserRequestDTO request, StreamObserver<CrawlerJobStatusDTO> responseObserver) {
        // start crawling:
        Map<String, String> crawlingJobsStatuses = taskOrchestrationService.startExecutorCrawlingJob(request);

        // build response:
        CrawlerJobStatusDTO response = CrawlerJobStatusDTO.newBuilder()
                .putAllDomainToStatus(crawlingJobsStatuses)
                .build();

        // return response:
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCrawlerProgress(GetCrawlerProgressByDomainsRequestDTO request, StreamObserver<CrawlerProgressByDomainsResponseDTO> responseObserver) {
        // build response:
        CrawlerProgressByDomainsResponseDTO response = CrawlerProgressByDomainsResponseDTO
                .newBuilder()
                .putAllDomainToProgressPercents(CrawlerStateStorage.getCrawlerProgressByDomain())
                .build();

        // return response:
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
