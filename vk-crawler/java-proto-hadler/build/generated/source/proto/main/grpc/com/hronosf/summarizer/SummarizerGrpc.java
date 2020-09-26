package com.hronosf.summarizer;

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
    comments = "Source: summarizer.proto")
public class SummarizerGrpc {

  private SummarizerGrpc() {}

  public static final String SERVICE_NAME = "com.hronosf.summarizer.Summarizer";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.hronosf.summarizer.SummarizeRequest,
      com.hronosf.summarizer.SummarizeResponse> METHOD_SUMMARIZE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.hronosf.summarizer.Summarizer", "summarize"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.hronosf.summarizer.SummarizeRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.hronosf.summarizer.SummarizeResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SummarizerStub newStub(io.grpc.Channel channel) {
    return new SummarizerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SummarizerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SummarizerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static SummarizerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SummarizerFutureStub(channel);
  }

  /**
   */
  public static abstract class SummarizerImplBase implements io.grpc.BindableService {

    /**
     */
    public void summarize(com.hronosf.summarizer.SummarizeRequest request,
        io.grpc.stub.StreamObserver<com.hronosf.summarizer.SummarizeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SUMMARIZE, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_SUMMARIZE,
            asyncUnaryCall(
              new MethodHandlers<
                com.hronosf.summarizer.SummarizeRequest,
                com.hronosf.summarizer.SummarizeResponse>(
                  this, METHODID_SUMMARIZE)))
          .build();
    }
  }

  /**
   */
  public static final class SummarizerStub extends io.grpc.stub.AbstractStub<SummarizerStub> {
    private SummarizerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SummarizerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SummarizerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SummarizerStub(channel, callOptions);
    }

    /**
     */
    public void summarize(com.hronosf.summarizer.SummarizeRequest request,
        io.grpc.stub.StreamObserver<com.hronosf.summarizer.SummarizeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_SUMMARIZE, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SummarizerBlockingStub extends io.grpc.stub.AbstractStub<SummarizerBlockingStub> {
    private SummarizerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SummarizerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SummarizerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SummarizerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.hronosf.summarizer.SummarizeResponse summarize(com.hronosf.summarizer.SummarizeRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_SUMMARIZE, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SummarizerFutureStub extends io.grpc.stub.AbstractStub<SummarizerFutureStub> {
    private SummarizerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SummarizerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SummarizerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SummarizerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hronosf.summarizer.SummarizeResponse> summarize(
        com.hronosf.summarizer.SummarizeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_SUMMARIZE, getCallOptions()), request);
    }
  }

  private static final int METHODID_SUMMARIZE = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SummarizerImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(SummarizerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUMMARIZE:
          serviceImpl.summarize((com.hronosf.summarizer.SummarizeRequest) request,
              (io.grpc.stub.StreamObserver<com.hronosf.summarizer.SummarizeResponse>) responseObserver);
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
        METHOD_SUMMARIZE);
  }

}
