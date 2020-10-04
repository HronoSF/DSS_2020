// package: com.hronosf.search
// file: search.proto

import * as search_pb from "./search_pb";
import {grpc} from "@improbable-eng/grpc-web";

type SearchsearchWithText = {
  readonly methodName: string;
  readonly service: typeof Search;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof search_pb.TestSearchRequestDTO;
  readonly responseType: typeof search_pb.TextSearchResponseDTO;
};

type SearchsearchWithId = {
  readonly methodName: string;
  readonly service: typeof Search;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof search_pb.IdSearchRequestDTO;
  readonly responseType: typeof search_pb.IdSearchResponseDTO;
};

export class Search {
  static readonly serviceName: string;
  static readonly searchWithText: SearchsearchWithText;
  static readonly searchWithId: SearchsearchWithId;
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
  searchWithText(
    requestMessage: search_pb.TestSearchRequestDTO,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: search_pb.TextSearchResponseDTO|null) => void
  ): UnaryResponse;
  searchWithText(
    requestMessage: search_pb.TestSearchRequestDTO,
    callback: (error: ServiceError|null, responseMessage: search_pb.TextSearchResponseDTO|null) => void
  ): UnaryResponse;
  searchWithId(
    requestMessage: search_pb.IdSearchRequestDTO,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: search_pb.IdSearchResponseDTO|null) => void
  ): UnaryResponse;
  searchWithId(
    requestMessage: search_pb.IdSearchRequestDTO,
    callback: (error: ServiceError|null, responseMessage: search_pb.IdSearchResponseDTO|null) => void
  ): UnaryResponse;
}

