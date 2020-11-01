import os
import grpc
import time
import razdel
import logging
import pyspark
import urllib.request
from concurrent import futures
import relationship_extractor_pb2
import relationship_extractor_pb2_grpc
from processing_service import SimpleExtractor

# set log level & disable pyspark logs:
logging.basicConfig(level=logging.DEBUG)
logging.getLogger('py4j.java_gateway').setLevel(logging.ERROR)

# init Spark context:
logging.info("Initializing PySpark context")
conf = pyspark.SparkConf()
conf.set("spark.driver.bindAddress", "0.0.0.0")
conf.set("spark.master", os.getenv('SPARK_ADDRESS', 'local[*]'))
conf.set("spark.cores.max", os.getenv('SPARK_CORES_MAX', '1'))

sc = pyspark.SparkContext(appName="Relation Extractor Service", conf=conf)

sc.addPyFile("processing_service.py")
sc.addPyFile("relationship_extractor_pb2.py")
sc.addPyFile("relationship_extractor_pb2_grpc.py")

urllib.request.urlretrieve(
    'https://storage.yandexcloud.net/natasha-navec/packs/navec_news_v1_1B_250K_300d_100q.tar', 'navec_news_v1_1B_250K_300d_100q.tar')
urllib.request.urlretrieve(
    'https://storage.yandexcloud.net/natasha-slovnet/packs/slovnet_syntax_news_v1.tar', 'slovnet_syntax_news_v1.tar')

extractor = SimpleExtractor(
    path_to_navec_data="navec_news_v1_1B_250K_300d_100q.tar", path_to_syntax_data="slovnet_syntax_news_v1.tar")

def extract_relations(doc):
    relation_map = extractor.extract_relations_from_docs(doc.text)
    processedIn = int(round(time.time()))

    return relationship_extractor_pb2.DataToUpdate(id=str(doc.id), relationMap=relation_map, processedIn=processedIn)


# gRPC server implementation:
class RelationshipExtractorServicer(relationship_extractor_pb2_grpc.RelationshipExtractorServicer):

    # override service method:
    def extractRelation(self, request, context):
        # get data from request:
        docs = request.textToExtract

        if not docs:
            return relationship_extractor_pb2.RelationshipExtractorResponseDTO()

        logging.info('Processing of %s text(s)', len(docs))

        result = [extract_relations(x) for x in docs] if len(docs) <= 100 else sc.parallelize(docs) \
            .map(lambda doc: extract_relations(doc)) \
            .collect()

        return relationship_extractor_pb2.RelationshipExtractorResponseDTO(dataToUpdate=result)


# server startup:
def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=4))
    relationship_extractor_pb2_grpc.add_RelationshipExtractorServicer_to_server(
        RelationshipExtractorServicer(), server)
    logging.info('Started server on localost:6067')
    server.add_insecure_port('[::]:6067')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
