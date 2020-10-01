// package: 
// file: datahash.proto

var datahash_pb = require("./datahash_pb");
var grpc = require("@improbable-eng/grpc-web").grpc;

var DataHash = (function () {
  function DataHash() {}
  DataHash.serviceName = "DataHash";
  return DataHash;
}());

DataHash.hash_md5 = {
  methodName: "hash_md5",
  service: DataHash,
  requestStream: false,
  responseStream: false,
  requestType: datahash_pb.Text,
  responseType: datahash_pb.Text
};

DataHash.hash_sha256 = {
  methodName: "hash_sha256",
  service: DataHash,
  requestStream: false,
  responseStream: false,
  requestType: datahash_pb.Text,
  responseType: datahash_pb.Text
};

exports.DataHash = DataHash;

function DataHashClient(serviceHost, options) {
  this.serviceHost = serviceHost;
  this.options = options || {};
}

DataHashClient.prototype.hash_md5 = function hash_md5(requestMessage, metadata, callback) {
  if (arguments.length === 2) {
    callback = arguments[1];
  }
  var client = grpc.unary(DataHash.hash_md5, {
    request: requestMessage,
    host: this.serviceHost,
    metadata: metadata,
    transport: this.options.transport,
    debug: this.options.debug,
    onEnd: function (response) {
      if (callback) {
        if (response.status !== grpc.Code.OK) {
          var err = new Error(response.statusMessage);
          err.code = response.status;
          err.metadata = response.trailers;
          callback(err, null);
        } else {
          callback(null, response.message);
        }
      }
    }
  });
  return {
    cancel: function () {
      callback = null;
      client.close();
    }
  };
};

DataHashClient.prototype.hash_sha256 = function hash_sha256(requestMessage, metadata, callback) {
  if (arguments.length === 2) {
    callback = arguments[1];
  }
  var client = grpc.unary(DataHash.hash_sha256, {
    request: requestMessage,
    host: this.serviceHost,
    metadata: metadata,
    transport: this.options.transport,
    debug: this.options.debug,
    onEnd: function (response) {
      if (callback) {
        if (response.status !== grpc.Code.OK) {
          var err = new Error(response.statusMessage);
          err.code = response.status;
          err.metadata = response.trailers;
          callback(err, null);
        } else {
          callback(null, response.message);
        }
      }
    }
  });
  return {
    cancel: function () {
      callback = null;
      client.close();
    }
  };
};

exports.DataHashClient = DataHashClient;

