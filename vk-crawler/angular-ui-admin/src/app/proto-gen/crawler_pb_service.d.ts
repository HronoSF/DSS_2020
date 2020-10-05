// package: com.hronosf.crawler.controller
// file: crawler.proto

import * as crawler_pb from "./crawler_pb";
import {grpc} from "@improbable-eng/grpc-web";

type CrawlerstartCrawlingAsUserActor = {
  readonly methodName: string;
  readonly service: typeof Crawler;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof crawler_pb.StartParsingAsUserRequestDTO;
  readonly responseType: typeof crawler_pb.CrawlerJobStatusDTO;
};

type CrawlerstartCrawlingAsServiceActor = {
  readonly methodName: string;
  readonly service: typeof Crawler;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof crawler_pb.StartParsingAsServiceRequestDTO;
  readonly responseType: typeof crawler_pb.CrawlerJobStatusDTO;
};

type CrawlergetCrawlerProgress = {
  readonly methodName: string;
  readonly service: typeof Crawler;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof crawler_pb.GetCrawlerProgressByDomainsRequestDTO;
  readonly responseType: typeof crawler_pb.CrawlerProgressByDomainsResponseDTO;
};

export class Crawler {
  static readonly serviceName: string;
  static readonly startCrawlingAsUserActor: CrawlerstartCrawlingAsUserActor;
  static readonly startCrawlingAsServiceActor: CrawlerstartCrawlingAsServiceActor;
  static readonly getCrawlerProgress: CrawlergetCrawlerProgress;
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
  startCrawlingAsUserActor(
    requestMessage: crawler_pb.StartParsingAsUserRequestDTO,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerJobStatusDTO|null) => void
  ): UnaryResponse;
  startCrawlingAsUserActor(
    requestMessage: crawler_pb.StartParsingAsUserRequestDTO,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerJobStatusDTO|null) => void
  ): UnaryResponse;
  startCrawlingAsServiceActor(
    requestMessage: crawler_pb.StartParsingAsServiceRequestDTO,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerJobStatusDTO|null) => void
  ): UnaryResponse;
  startCrawlingAsServiceActor(
    requestMessage: crawler_pb.StartParsingAsServiceRequestDTO,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerJobStatusDTO|null) => void
  ): UnaryResponse;
  getCrawlerProgress(
    requestMessage: crawler_pb.GetCrawlerProgressByDomainsRequestDTO,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerProgressByDomainsResponseDTO|null) => void
  ): UnaryResponse;
  getCrawlerProgress(
    requestMessage: crawler_pb.GetCrawlerProgressByDomainsRequestDTO,
    callback: (error: ServiceError|null, responseMessage: crawler_pb.CrawlerProgressByDomainsResponseDTO|null) => void
  ): UnaryResponse;
}

