// package: com.hronosf.search
// file: search.proto

import * as jspb from "google-protobuf";

export class SearchRequest extends jspb.Message {
  getTexttosearch(): string;
  setTexttosearch(value: string): void;

  getPage(): number;
  setPage(value: number): void;

  getSize(): number;
  setSize(value: number): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): SearchRequest.AsObject;
  static toObject(includeInstance: boolean, msg: SearchRequest): SearchRequest.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: SearchRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): SearchRequest;
  static deserializeBinaryFromReader(message: SearchRequest, reader: jspb.BinaryReader): SearchRequest;
}

export namespace SearchRequest {
  export type AsObject = {
    texttosearch: string,
    page: number,
    size: number,
  }
}

export class SearchResponse extends jspb.Message {
  clearContentList(): void;
  getContentList(): Array<WallPost>;
  setContentList(value: Array<WallPost>): void;
  addContent(value?: WallPost, index?: number): WallPost;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): SearchResponse.AsObject;
  static toObject(includeInstance: boolean, msg: SearchResponse): SearchResponse.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: SearchResponse, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): SearchResponse;
  static deserializeBinaryFromReader(message: SearchResponse, reader: jspb.BinaryReader): SearchResponse;
}

export namespace SearchResponse {
  export type AsObject = {
    contentList: Array<WallPost.AsObject>,
  }
}

export class WallPost extends jspb.Message {
  getId(): string;
  setId(value: string): void;

  getFromid(): string;
  setFromid(value: string): void;

  getOwnerid(): string;
  setOwnerid(value: string): void;

  getSignerid(): string;
  setSignerid(value: string): void;

  getText(): string;
  setText(value: string): void;

  getEdited(): string;
  setEdited(value: string): void;

  getDate(): string;
  setDate(value: string): void;

  getSummary(): string;
  setSummary(value: string): void;

  getProcessedin(): string;
  setProcessedin(value: string): void;

  clearRelationmapList(): void;
  getRelationmapList(): Array<ObjectToRelation>;
  setRelationmapList(value: Array<ObjectToRelation>): void;
  addRelationmap(value?: ObjectToRelation, index?: number): ObjectToRelation;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): WallPost.AsObject;
  static toObject(includeInstance: boolean, msg: WallPost): WallPost.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: WallPost, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): WallPost;
  static deserializeBinaryFromReader(message: WallPost, reader: jspb.BinaryReader): WallPost;
}

export namespace WallPost {
  export type AsObject = {
    id: string,
    fromid: string,
    ownerid: string,
    signerid: string,
    text: string,
    edited: string,
    date: string,
    summary: string,
    processedin: string,
    relationmapList: Array<ObjectToRelation.AsObject>,
  }
}

export class ObjectToRelation extends jspb.Message {
  getObject(): string;
  setObject(value: string): void;

  getRelation(): string;
  setRelation(value: string): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): ObjectToRelation.AsObject;
  static toObject(includeInstance: boolean, msg: ObjectToRelation): ObjectToRelation.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: ObjectToRelation, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): ObjectToRelation;
  static deserializeBinaryFromReader(message: ObjectToRelation, reader: jspb.BinaryReader): ObjectToRelation;
}

export namespace ObjectToRelation {
  export type AsObject = {
    object: string,
    relation: string,
  }
}

