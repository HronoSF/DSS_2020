// package: com.hronosf.crawler.controller
// file: crawler.proto

import * as crawler_pb from "./crawler_pb";
import {grpc} from "@improbable-eng/grpc-web";

type CrawlerstartCrawling = {
  readonly methodName: string;
  readonly service: typeof Crawler;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof crawler_pb.StartParsingRequest;
  readonly responseType: typeof crawler_pb.CrawlerJobStatus;
};

export class Crawler {
  static readonly serviceName: string;
  static readonly startCrawling: CrawlerstartCrawling;
}

export type ServiceError = { message: string, code: number; metadata: grpc.Metadata }
export type Status = { details: string, code: number; metadata: grpc.Metadata }

interface UnaryResponse {
  cancel(): void;
}
interface ResponseStream<T> {
  cancel(): void;
  on(type: 'data', handler: (message: T) => void): ResponseStream<T>;
  on(type: 'end', handler: (status?: Status) => void): ResponseStream<T>;
  on(type: 'status', handler: (status: Status) => void): ResponseStream<T>;
}
interface RequestStream<T> {
  write(message: T): RequestStream<T>;
  end(): void;
  cancel(): void;
  on(type: 'end', handler: (status?: Status) => void): RequestStream<T>;
  on(type: 'status', handler: (status: Status) => void): RequestStream<T>;
}
interface BidirectionalStream<ReqT, ResT> {
  write(message: ReqT): BidirectionalStream<ReqT, ResT>;
  end(): void;
  cancel(): void;
  on(type: 'data', handler: (message: ResT) => void): BidirectionalStream<ReqT, ResT>;
  on(type: 'end', handler: (status?: Status) => void): BidirectionalStream<ReqT, ResT>;
  on(type: 'status', handler: (status: Status) => void): BidirectionalStream<ReqT, ResT>;
}

export class CrawlerClient {
  readonly serviceHost: string;

  constructor(serviceHost: string, options?: grpc.RpcOptions);
  startCrawling(
    requestMessage: crawler_pb.StartParsingRequest,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerJobStatus|null) => void
  ): UnaryResponse;
  startCrawling(
    requestMessage: crawler_pb.StartParsingRequest,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerJobStatus|null) => void
  ): UnaryResponse;
}

