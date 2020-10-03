// package: com.hronosf.search
// file: search.proto

import * as search_pb from "./search_pb";
import {grpc} from "@improbable-eng/grpc-web";

type Searchsearch = {
  readonly methodName: string;
  readonly service: typeof Search;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof search_pb.SearchRequest;
  readonly responseType: typeof search_pb.SearchResponse;
};

export class Search {
  static readonly serviceName: string;
  static readonly search: Searchsearch;
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

export class SearchClient {
  readonly serviceHost: string;

  constructor(serviceHost: string, options?: grpc.RpcOptions);
  search(
    requestMessage: search_pb.SearchRequest,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: search_pb.SearchResponse|null) => void
  ): UnaryResponse;
  search(
    requestMessage: search_pb.SearchRequest,
    callback: (error: ServiceError|null, responseMessage: search_pb.SearchResponse|null) => void
  ): UnaryResponse;
}

