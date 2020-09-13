// package: com.hronosf.crawler.controller
// file: crawler.proto

import * as jspb from "google-protobuf";

export class StartParsingRequest extends jspb.Message {
  clearToparseList(): void;
  getToparseList(): Array<string>;
  setToparseList(value: Array<string>): void;
  addToparse(value: string, index?: number): string;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): StartParsingRequest.AsObject;
  static toObject(includeInstance: boolean, msg: StartParsingRequest): StartParsingRequest.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: StartParsingRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): StartParsingRequest;
  static deserializeBinaryFromReader(message: StartParsingRequest, reader: jspb.BinaryReader): StartParsingRequest;
}

export namespace StartParsingRequest {
  export type AsObject = {
    toparseList: Array<string>,
  }
}

export class CrawlerJobStatus extends jspb.Message {
  getDomaintostatusMap(): jspb.Map<string, string>;
  clearDomaintostatusMap(): void;
  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): CrawlerJobStatus.AsObject;
  static toObject(includeInstance: boolean, msg: CrawlerJobStatus): CrawlerJobStatus.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: CrawlerJobStatus, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): CrawlerJobStatus;
  static deserializeBinaryFromReader(message: CrawlerJobStatus, reader: jspb.BinaryReader): CrawlerJobStatus;
}

export namespace CrawlerJobStatus {
  export type AsObject = {
    domaintostatusMap: Array<[string, string]>,
  }
}

