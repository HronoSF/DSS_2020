// package: com.hronosf.search
// file: search.proto

var search_pb = require("./search_pb");
var grpc = require("@improbable-eng/grpc-web").grpc;

var Search = (function () {
  function Search() {}
  Search.serviceName = "com.hronosf.search.Search";
  return Search;
}());

Search.searchWithText = {
  methodName: "searchWithText",
  service: Search,
  requestStream: false,
  responseStream: false,
  requestType: search_pb.TestSearchRequestDTO,
  responseType: search_pb.TextSearchResponseDTO
};

Search.searchWithId = {
  methodName: "searchWithId",
  service: Search,
  requestStream: false,
  responseStream: false,
  requestType: search_pb.IdSearchRequestDTO,
  responseType: search_pb.IdSearchResponseDTO
};

exports.Search = Search;

function SearchClient(serviceHost, options) {
  this.serviceHost = serviceHost;
  this.options = options || {};
}

SearchClient.prototype.searchWithText = function searchWithText(requestMessage, metadata, callback) {
  if (arguments.length === 2) {
    callback = arguments[1];
  }
  var client = grpc.unary(Search.searchWithText, {
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

SearchClient.prototype.searchWithId = function searchWithId(requestMessage, metadata, callback) {
  if (arguments.length === 2) {
    callback = arguments[1];
  }
  var client = grpc.unary(Search.searchWithId, {
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

exports.SearchClient = SearchClient;

