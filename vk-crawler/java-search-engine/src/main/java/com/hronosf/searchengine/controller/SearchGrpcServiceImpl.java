package com.hronosf.searchengine.controller;

import com.hronosf.search.*;
import com.hronosf.searchengine.services.ElasticSearchService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class SearchGrpcServiceImpl extends SearchGrpc.SearchImplBase {

    private final ElasticSearchService esService;

    @Override
    public void searchWithText(TestSearchRequestDTO request, StreamObserver<TextSearchResponseDTO> responseObserver) {
        log.info("Requested search with text: {}, pagination page: {}, size: {}"
                , request.getTextToSearch(), request.getPage(), request.getSize());

        // get documents from Es by requested text:
        TextSearchResponseDTO response = esService.searchWithText(request);

        // return response:
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchWithId(IdSearchRequestDTO request, StreamObserver<IdSearchResponseDTO> responseObserver) {
        log.info("Requested search with id: {}", request.getIdToSearch());

        // get data from Es by requested id:
        IdSearchResponseDTO response = esService.searchWithId(request);

        // return response:
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
