// package: 
// file: datahash.proto

import * as datahash_pb from "./datahash_pb";
import {grpc} from "@improbable-eng/grpc-web";

type DataHashhash_md5 = {
  readonly methodName: string;
  readonly service: typeof DataHash;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof datahash_pb.Text;
  readonly responseType: typeof datahash_pb.Text;
};

type DataHashhash_sha256 = {
  readonly methodName: string;
  readonly service: typeof DataHash;
  readonly requestStream: false;
  readonly responseStream: false;
  readonly requestType: typeof datahash_pb.Text;
  readonly responseType: typeof datahash_pb.Text;
};

export class DataHash {
  static readonly serviceName: string;
  static readonly hash_md5: DataHashhash_md5;
  static readonly hash_sha256: DataHashhash_sha256;
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

export class DataHashClient {
  readonly serviceHost: string;

  constructor(serviceHost: string, options?: grpc.RpcOptions);
  hash_md5(
    requestMessage: datahash_pb.Text,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: datahash_pb.Text|null) => void
  ): UnaryResponse;
  hash_md5(
    requestMessage: datahash_pb.Text,
    callback: (error: ServiceError|null, responseMessage: datahash_pb.Text|null) => void
  ): UnaryResponse;
  hash_sha256(
    requestMessage: datahash_pb.Text,
    metadata: grpc.Metadata,
    callback: (error: ServiceError|null, responseMessage: datahash_pb.Text|null) => void
  ): UnaryResponse;
  hash_sha256(
    requestMessage: datahash_pb.Text,
    callback: (error: ServiceError|null, responseMessage: datahash_pb.Text|null) => void
  ): UnaryResponse;
}

