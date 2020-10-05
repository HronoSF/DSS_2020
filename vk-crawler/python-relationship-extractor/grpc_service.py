import os
import grpc
import time
import razdel
import logging
import pyspark
from concurrent import futures
import relationship_extractor_pb2
import relationship_extractor_pb2_grpc
from processing_service import extract_relations_from_docs

# set log level & disable pyspark logs:
logging.basicConfig(level=logging.DEBUG)
logging.getLogger('py4j.java_gateway').setLevel(logging.ERROR)

# init Spark context:
logging.info("Initializing PySpark context")
sc = pyspark.SparkContext(os.getenv('SPARK_ADDRESS', 'local[*]'))

def extract_relations(doc):
    relation_map = extract_relations_from_docs(doc)
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

        result = sc.parallelize(docs) \
          .map(lambda doc: extract_relations(doc)) \
          .collect()

        return relationship_extractor_pb2.RelationshipExtractorResponseDTO(dataToUpdate = result)


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
