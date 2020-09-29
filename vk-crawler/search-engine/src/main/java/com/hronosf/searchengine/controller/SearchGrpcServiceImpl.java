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
    public void search(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        log.info("Requested search with text:4 {}, pagination page: {}, size: {}"
                , request.getTextToSearch(), request.getPage(), request.getSize());

        // get documents from Es by requested text:
        SearchResponse wallPosts = esService.searchWithText(request);

        // return response:
        responseObserver.onNext(wallPosts);
        responseObserver.onCompleted();
    }
}
