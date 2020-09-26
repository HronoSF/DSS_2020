package com.hronosf.crawler.controller;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.0-pre2)",
    comments = "Source: crawler.proto")
public class CrawlerGrpc {

  private CrawlerGrpc() {}

  public static final String SERVICE_NAME = "com.hronosf.crawler.controller.Crawler";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.hronosf.crawler.controller.StartParsingRequest,
      com.hronosf.crawler.controller.CrawlerJobStatus> METHOD_START_CRAWLING =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.hronosf.crawler.controller.Crawler", "startCrawling"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.hronosf.crawler.controller.StartParsingRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.hronosf.crawler.controller.CrawlerJobStatus.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CrawlerStub newStub(io.grpc.Channel channel) {
    return new CrawlerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CrawlerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CrawlerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static CrawlerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CrawlerFutureStub(channel);
  }

  /**
   */
  public static abstract class CrawlerImplBase implements io.grpc.BindableService {

    /**
     */
    public void startCrawling(com.hronosf.crawler.controller.StartParsingRequest request,
        io.grpc.stub.StreamObserver<com.hronosf.crawler.controller.CrawlerJobStatus> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_START_CRAWLING, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_START_CRAWLING,
            asyncUnaryCall(
              new MethodHandlers<
                com.hronosf.crawler.controller.StartParsingRequest,
                com.hronosf.crawler.controller.CrawlerJobStatus>(
                  this, METHODID_START_CRAWLING)))
          .build();
    }
  }

  /**
   */
  public static final class CrawlerStub extends io.grpc.stub.AbstractStub<CrawlerStub> {
    private CrawlerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CrawlerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CrawlerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CrawlerStub(channel, callOptions);
    }

    /**
     */
    public void startCrawling(com.hronosf.crawler.controller.StartParsingRequest request,
        io.grpc.stub.StreamObserver<com.hronosf.crawler.controller.CrawlerJobStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_START_CRAWLING, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CrawlerBlockingStub extends io.grpc.stub.AbstractStub<CrawlerBlockingStub> {
    private CrawlerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CrawlerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CrawlerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CrawlerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.hronosf.crawler.controller.CrawlerJobStatus startCrawling(com.hronosf.crawler.controller.StartParsingRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_START_CRAWLING, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CrawlerFutureStub extends io.grpc.stub.AbstractStub<CrawlerFutureStub> {
    private CrawlerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CrawlerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CrawlerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CrawlerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hronosf.crawler.controller.CrawlerJobStatus> startCrawling(
        com.hronosf.crawler.controller.StartParsingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_START_CRAWLING, getCallOptions()), request);
    }
  }

  private static final int METHODID_START_CRAWLING = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CrawlerImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(CrawlerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_START_CRAWLING:
          serviceImpl.startCrawling((com.hronosf.crawler.controller.StartParsingRequest) request,
              (io.grpc.stub.StreamObserver<com.hronosf.crawler.controller.CrawlerJobStatus>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_START_CRAWLING);
  }

}
