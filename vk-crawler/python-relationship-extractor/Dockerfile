# stage 1: clone project repo
FROM python:3.6-slim as clone
WORKDIR /root/project
COPY /python-relationship-extractor /root/project/python-relationship-extractor
COPY /java-proto-handler/src/main/proto /root/project/java-proto-handler

# stage 2: build protobuf services from .proto files
FROM python:3.6-slim as protobuf-build
COPY --from=clone /root/project/java-proto-handler /app/
RUN apt update \
    && apt install -y protobuf-compiler \
    && pip install grpcio grpcio-tools
RUN cd /app \
    && mkdir /root/python_proto/ \
    && python -m grpc_tools.protoc -I. --python_out=/root/python_proto/ --grpc_python_out=/root/python_proto/ summarizer.proto

# stage 3: run python service
FROM python:3.6-slim
RUN apt-get update
RUN apt-get install -y apt-utils build-essential gcc zip
ENV JAVA_FOLDER java-se-8u41-ri
ENV JVM_ROOT /usr/lib/jvm
ENV JAVA_PKG_NAME openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz
ENV JAVA_TAR_GZ_URL https://download.java.net/openjdk/jdk8u41/ri/$JAVA_PKG_NAME
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*    && \
    apt-get clean                                                               && \
    apt-get autoremove                                                          && \
    echo Downloading $JAVA_TAR_GZ_URL                                           && \
    wget -q $JAVA_TAR_GZ_URL                                                    && \
    tar -xvf $JAVA_PKG_NAME                                                     && \
    rm $JAVA_PKG_NAME                                                           && \
    mkdir -p /usr/lib/jvm                                                       && \
    mv ./$JAVA_FOLDER $JVM_ROOT                                                 && \
    update-alternatives --install /usr/bin/java java $JVM_ROOT/$JAVA_FOLDER/bin/java 1        && \
    update-alternatives --install /usr/bin/javac javac $JVM_ROOT/$JAVA_FOLDER/bin/javac 1     && \
    java -version
COPY --from=clone /root/project/python-relationship-extractor/ /root/app/
COPY --from=protobuf-build /root/python_proto/ /root/app/
ARG SPARK_ADDRESS
ENV SPARK_ADDRESS="${SPARK_ADDRESS}"
WORKDIR /root/app
EXPOSE 6067
RUN pip install -r requirements.txt && \
    python -m dostoevsky download fasttext-social-network-model
ENTRYPOINT ["sh", "-c"]
CMD ["python grpc_service.py"]
