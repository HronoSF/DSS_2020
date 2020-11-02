import os
import grpc
import time
import logging
import pyspark
import summarizer_pb2
import summarizer_pb2_grpc
from concurrent import futures
from summarizers import LexRank

# set log level & disable pyspark logs:
logging.basicConfig(level=logging.DEBUG)
s_logger = logging.getLogger('py4j.java_gateway').setLevel(logging.ERROR)

# init LexRank:
logging.info("Initializing LexRank with precounted idf")
lxr = LexRank(path_to_idf_pickle='weights/idf.pickle')

# init Spark context:
logging.info("Initializing PySpark context")
conf = pyspark.SparkConf()
conf.set("spark.driver.bindAddress", "0.0.0.0")
conf.set("spark.master", os.getenv('SPARK_ADDRESS', 'local[*]'))
conf.set("spark.cores.max", os.getenv('SPARK_CORES_MAX', '1'))

sc = pyspark.SparkContext(appName="Summarization Service", conf=conf)

sc.addPyFile("summarizers.py")
sc.addPyFile("summarizer_pb2.py")
sc.addPyFile("summarizer_pb2_grpc.py")


# define data transfromation to proto entity method:
def summarize_text_with_lex_rank(doc):
    summary = lxr.get_prediction_lex_rank(doc.text)
    processedIn = int(round(time.time() * 1000))
    return summarizer_pb2.DataToUpdate(id=doc.id, summary=summary, processedIn=processedIn)


# gRPC server implementation:
class SummarizerServicer(summarizer_pb2_grpc.SummarizerServicer):

    # override service method:
    def summarize(self, request, context):
        # get data from request:
        docs = request.textToSummarize

        if not docs:
            return summarizer_pb2.SummarizeResponseDTO()

        logging.info('Processing of %s text(s)', len(docs))

        # modify data - set summary and processing time:
        updated_docs = [summarize_text_with_lex_rank(x) for x in docs] if len(docs) <= 100 else sc.parallelize(docs) \
            .map(lambda doc: summarize_text_with_lex_rank(doc)) \
            .collect()

        # return SummarizeResponse:
        return summarizer_pb2.SummarizeResponseDTO(dataToUpdate=updated_docs)


# server startup:
def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=4))
    summarizer_pb2_grpc.add_SummarizerServicer_to_server(
        SummarizerServicer(), server)
    logging.info('Started server on localost:6066')
    server.add_insecure_port('[::]:6066')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
