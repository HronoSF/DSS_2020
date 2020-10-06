# stage 1: clone project repo
FROM python as clone
WORKDIR /root/project
COPY . .

# stage 2: build protobuf services from .proto files
FROM python as protobuf-build
COPY --from=clone /root/project/java-proto-handler /app/
RUN apt update \
    && apt install -y protobuf-compiler \
    && pip install grpcio grpcio-tools
RUN cd /app/src/main/proto \
    && mkdir /root/python_proto/ \
    && python -m grpc_tools.protoc -I. --python_out=/root/python_proto/ --grpc_python_out=/root/python_proto/ summarizer.proto

# stage 3: run python service
FROM rappdw/docker-java-python:openjdk1.8.0_171-python3.6.6
COPY --from=clone /root/project/python-relationship-extractor/ /app/
COPY --from=protobuf-build /root/python_proto/ /app/
ARG SPARK_ADDRESS
ENV SPARK_ADDRESS="${SPARK_ADDRESS}"
EXPOSE 6066
WORKDIR /app
RUN pip install -r requirements.txt
ENTRYPOINT ["sh", "-c"]
CMD ["python grpc_service.py"]
