FROM bitnami/spark:2.4.6
COPY requirements.txt requirements.txt
USER root
RUN apt update && apt install -y build-essential && apt install -y cmake
RUN pip install -r requirements.txt
RUN python -m dostoevsky download fasttext-social-network-model
RUN python -m nltk.downloader punkt
USER 1001
RUN curl https://search.maven.org/remotecontent?filepath=org/elasticsearch/elasticsearch-spark-20_2.11/7.6.2/elasticsearch-spark-20_2.11-7.6.2.jar \
    --output /opt/bitnami/spark/jars/elasticsearch-spark-20_2.11-7.6.2.jar
RUN rm /opt/bitnami/spark/jars/protobuf-java-2.5.0.jar
RUN curl https://search.maven.org/remotecontent?filepath=com/google/protobuf/protobuf-java/3.12.0/protobuf-java-3.12.0.jar \
    --output /opt/bitnami/spark/jars/protobuf-java-3.12.0.jar