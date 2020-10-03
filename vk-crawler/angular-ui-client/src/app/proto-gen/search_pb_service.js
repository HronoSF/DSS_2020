// package: com.hronosf.search
// file: search.proto

var search_pb = require("./search_pb");
var grpc = require("@improbable-eng/grpc-web").grpc;

var Search = (function () {
  function Search() {}
  Search.serviceName = "com.hronosf.search.Search";
  return Search;
}());

Search.search = {
  methodName: "search",
  service: Search,
  requestStream: false,
  responseStream: false,
  requestType: search_pb.SearchRequest,
  responseType: search_pb.SearchResponse
};

exports.Search = Search;

function SearchClient(serviceHost, options) {
  this.serviceHost = serviceHost;
  this.options = options || {};
}

SearchClient.prototype.search = function search(requestMessage, metadata, callback) {
  if (arguments.length === 2) {
    callback = arguments[1];
  }
  var client = grpc.unary(Search.search, {
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

