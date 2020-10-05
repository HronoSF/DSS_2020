// package: com.hronosf.crawler.controller
// file: crawler.proto

import * as jspb from "google-protobuf";

export class StartParsingAsServiceRequestDTO extends jspb.Message {
  clearToparseList(): void;
  getToparseList(): Array<string>;
  setToparseList(value: Array<string>): void;
  addToparse(value: string, index?: number): string;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): StartParsingAsServiceRequestDTO.AsObject;
  static toObject(includeInstance: boolean, msg: StartParsingAsServiceRequestDTO): StartParsingAsServiceRequestDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: StartParsingAsServiceRequestDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): StartParsingAsServiceRequestDTO;
  static deserializeBinaryFromReader(message: StartParsingAsServiceRequestDTO, reader: jspb.BinaryReader): StartParsingAsServiceRequestDTO;
}

export namespace StartParsingAsServiceRequestDTO {
  export type AsObject = {
    toparseList: Array<string>,
  }
}

export class StartParsingAsUserRequestDTO extends jspb.Message {
  getUserid(): number;
  setUserid(value: number): void;

  getAccesskey(): string;
  setAccesskey(value: string): void;

  clearToparseList(): void;
  getToparseList(): Array<string>;
  setToparseList(value: Array<string>): void;
  addToparse(value: string, index?: number): string;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): StartParsingAsUserRequestDTO.AsObject;
  static toObject(includeInstance: boolean, msg: StartParsingAsUserRequestDTO): StartParsingAsUserRequestDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: StartParsingAsUserRequestDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): StartParsingAsUserRequestDTO;
  static deserializeBinaryFromReader(message: StartParsingAsUserRequestDTO, reader: jspb.BinaryReader): StartParsingAsUserRequestDTO;
}

export namespace StartParsingAsUserRequestDTO {
  export type AsObject = {
    userid: number,
    accesskey: string,
    toparseList: Array<string>,
  }
}

export class CrawlerJobStatusDTO extends jspb.Message {
  getDomaintostatusMap(): jspb.Map<string, string>;
  clearDomaintostatusMap(): void;
  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): CrawlerJobStatusDTO.AsObject;
  static toObject(includeInstance: boolean, msg: CrawlerJobStatusDTO): CrawlerJobStatusDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: CrawlerJobStatusDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): CrawlerJobStatusDTO;
  static deserializeBinaryFromReader(message: CrawlerJobStatusDTO, reader: jspb.BinaryReader): CrawlerJobStatusDTO;
}

export namespace CrawlerJobStatusDTO {
  export type AsObject = {
    domaintostatusMap: Array<[string, string]>,
  }
}

export class GetCrawlerProgressByDomainsRequestDTO extends jspb.Message {
  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): GetCrawlerProgressByDomainsRequestDTO.AsObject;
  static toObject(includeInstance: boolean, msg: GetCrawlerProgressByDomainsRequestDTO): GetCrawlerProgressByDomainsRequestDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: GetCrawlerProgressByDomainsRequestDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): GetCrawlerProgressByDomainsRequestDTO;
  static deserializeBinaryFromReader(message: GetCrawlerProgressByDomainsRequestDTO, reader: jspb.BinaryReader): GetCrawlerProgressByDomainsRequestDTO;
}

export namespace GetCrawlerProgressByDomainsRequestDTO {
  export type AsObject = {
  }
}

export class CrawlerProgressByDomainsResponseDTO extends jspb.Message {
  getDomaintoprogresspercentsMap(): jspb.Map<string, number>;
  clearDomaintoprogresspercentsMap(): void;
  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): CrawlerProgressByDomainsResponseDTO.AsObject;
  static toObject(includeInstance: boolean, msg: CrawlerProgressByDomainsResponseDTO): CrawlerProgressByDomainsResponseDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: CrawlerProgressByDomainsResponseDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): CrawlerProgressByDomainsResponseDTO;
  static deserializeBinaryFromReader(message: CrawlerProgressByDomainsResponseDTO, reader: jspb.BinaryReader): CrawlerProgressByDomainsResponseDTO;
}

export namespace CrawlerProgressByDomainsResponseDTO {
  export type AsObject = {
    domaintoprogresspercentsMap: Array<[string, number]>,
  }
}

