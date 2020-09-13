// package: com.hronosf.crawler.controller
// file: crawler.proto

var crawler_pb = require("./crawler_pb");
var grpc = require("@improbable-eng/grpc-web").grpc;

var Crawler = (function () {
  function Crawler() {}
  Crawler.serviceName = "com.hronosf.crawler.controller.Crawler";
  return Crawler;
}());

Crawler.startCrawling = {
  methodName: "startCrawling",
  service: Crawler,
  requestStream: false,
  responseStream: false,
  requestType: crawler_pb.StartParsingRequest,
  responseType: crawler_pb.CrawlerJobStatus
};

exports.Crawler = Crawler;

function CrawlerClient(serviceHost, options) {
  this.serviceHost = serviceHost;
  this.options = options || {};
}

CrawlerClient.prototype.startCrawling = function startCrawling(requestMessage, metadata, callback) {
  if (arguments.length === 2) {
    callback = arguments[1];
  }
  var client = grpc.unary(Crawler.startCrawling, {
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

exports.CrawlerClient = CrawlerClient;

