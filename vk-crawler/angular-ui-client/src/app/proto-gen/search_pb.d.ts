// package: com.hronosf.search
// file: search.proto

import * as jspb from "google-protobuf";

export class TestSearchRequestDTO extends jspb.Message {
  getTexttosearch(): string;
  setTexttosearch(value: string): void;

  getPage(): number;
  setPage(value: number): void;

  getSize(): number;
  setSize(value: number): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): TestSearchRequestDTO.AsObject;
  static toObject(includeInstance: boolean, msg: TestSearchRequestDTO): TestSearchRequestDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: TestSearchRequestDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): TestSearchRequestDTO;
  static deserializeBinaryFromReader(message: TestSearchRequestDTO, reader: jspb.BinaryReader): TestSearchRequestDTO;
}

export namespace TestSearchRequestDTO {
  export type AsObject = {
    texttosearch: string,
    page: number,
    size: number,
  }
}

export class TextSearchResponseDTO extends jspb.Message {
  clearContentList(): void;
  getContentList(): Array<WallPost>;
  setContentList(value: Array<WallPost>): void;
  addContent(value?: WallPost, index?: number): WallPost;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): TextSearchResponseDTO.AsObject;
  static toObject(includeInstance: boolean, msg: TextSearchResponseDTO): TextSearchResponseDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: TextSearchResponseDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): TextSearchResponseDTO;
  static deserializeBinaryFromReader(message: TextSearchResponseDTO, reader: jspb.BinaryReader): TextSearchResponseDTO;
}

export namespace TextSearchResponseDTO {
  export type AsObject = {
    contentList: Array<WallPost.AsObject>,
  }
}

export class IdSearchRequestDTO extends jspb.Message {
  getIdtosearch(): string;
  setIdtosearch(value: string): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): IdSearchRequestDTO.AsObject;
  static toObject(includeInstance: boolean, msg: IdSearchRequestDTO): IdSearchRequestDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: IdSearchRequestDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): IdSearchRequestDTO;
  static deserializeBinaryFromReader(message: IdSearchRequestDTO, reader: jspb.BinaryReader): IdSearchRequestDTO;
}

export namespace IdSearchRequestDTO {
  export type AsObject = {
    idtosearch: string,
  }
}

export class IdSearchResponseDTO extends jspb.Message {
  getFromid(): number;
  setFromid(value: number): void;

  getSignerid(): number;
  setSignerid(value: number): void;

  clearRelationmapList(): void;
  getRelationmapList(): Array<ObjectToRelation>;
  setRelationmapList(value: Array<ObjectToRelation>): void;
  addRelationmap(value?: ObjectToRelation, index?: number): ObjectToRelation;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): IdSearchResponseDTO.AsObject;
  static toObject(includeInstance: boolean, msg: IdSearchResponseDTO): IdSearchResponseDTO.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: IdSearchResponseDTO, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): IdSearchResponseDTO;
  static deserializeBinaryFromReader(message: IdSearchResponseDTO, reader: jspb.BinaryReader): IdSearchResponseDTO;
}

export namespace IdSearchResponseDTO {
  export type AsObject = {
    fromid: number,
    signerid: number,
    relationmapList: Array<ObjectToRelation.AsObject>,
  }
}

export class WallPost extends jspb.Message {
  getId(): string;
  setId(value: string): void;

  getFromid(): number;
  setFromid(value: number): void;

  getOwnerid(): number;
  setOwnerid(value: number): void;

  getSignerid(): number;
  setSignerid(value: number): void;

  getText(): string;
  setText(value: string): void;

  getEdited(): number;
  setEdited(value: number): void;

  getDate(): number;
  setDate(value: number): void;

  getSummary(): string;
  setSummary(value: string): void;

  getProcessedin(): number;
  setProcessedin(value: number): void;

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
    fromid: number,
    ownerid: number,
    signerid: number,
    text: string,
    edited: number,
    date: number,
    summary: string,
    processedin: number,
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

