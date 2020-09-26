import grpc
import time
import logging
import datetime
import summarizer_pb2
import summarizer_pb2_grpc
from concurrent import futures
from lexrank_service import predict_lex_rank

logging.basicConfig(level=logging.DEBUG)


# gRPC server implementation:
class SummarizerServicer(summarizer_pb2_grpc.SummarizerServicer):
    def summarize(self, request, context):
        # get data from request:
        documents = request.textToSummary

        logging.info('Processing of %s texts', len(documents))

        # modify data - set summary and processing time:
        for doc in documents:
            doc.summary = predict_lex_rank(doc.text)
            doc.processedIn = datetime.datetime.now().timestamp()

        # send data back:
        return request


# server startup:
def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=4))
    summarizer_pb2_grpc.add_SummarizerServicer_to_server(
        SummarizerServicer(), server)
    logging.info('Started server on localost:6066')
    server.add_insecure_port('[::]:6066')
    server.start()
    try:
        while True:
            pass
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    serve()
