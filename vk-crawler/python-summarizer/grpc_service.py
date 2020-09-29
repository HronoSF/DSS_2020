import grpc
import time
import razdel
import logging
import pyspark
import summarizer_pb2
import summarizer_pb2_grpc
from lexrank_service import lxr
from concurrent import futures

import logging
s_logger = logging.getLogger('py4j.java_gateway')
s_logger.setLevel(logging.ERROR)

logging.basicConfig(level=logging.DEBUG)

sc = pyspark.SparkContext('local[*]')

def func(doc):
    summary = predict_lex_rank(doc.text)
    processedIn = int(round(time.time() * 1000))
    return summarizer_pb2.TextToSummary(id= doc.id, text=doc.text, summary=summary, processedIn = str(processedIn))


def predict_lex_rank(text, summary_size=1, threshold=None):
    sentences = [s.text for s in razdel.sentenize(text)]
    prediction = lxr.get_summary(
        sentences, summary_size=summary_size, threshold=threshold)
    prediction = " ".join(prediction) 
    return prediction


# gRPC server implementation:
class SummarizerServicer(summarizer_pb2_grpc.SummarizerServicer):
    def summarize(self, request, context):
        docs = request.textToSummary
        # get data from request:
        logging.info('Processing of %s texts', len(docs))

        # modify data - set summary and processing time:
        updated_docs = sc.parallelize(docs) \
            .map(lambda doc: func(doc)) \
            .collect()

        return summarizer_pb2.SummarizeResponse(textToSummary=updated_docs)


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