# stage 1: clone project repo
FROM alpine/git:v2.24.3 as clone
WORKDIR /root/project
RUN git clone "https://github.com/HronoSF/DSS_2020.git"

# stage 2: build protobuf services from .proto files
FROM python as protobuf-build
COPY --from=clone /root/project/DSS_2020/vk-crawler /app/
RUN apt update && apt install -y protobuf-compiler && pip install grpcio grpcio-tools
RUN cd /app/java-proto-hadler/src/main/proto \
    && mkdir /root/python_proto/ \
    && python -m grpc_tools.protoc -I. --python_out=/root/python_proto/ --grpc_python_out=/root/python_proto/ summarizer.proto

# stage 3: run python service
FROM python
COPY --from=clone /root/project/DSS_2020/vk-crawler/python-summarizer/ /app/
COPY --from=protobuf-build /root/python_proto/ /app/
EXPOSE 6066
RUN pip install -r /app/requirements.txt
ENTRYPOINT ["sh", "-c"]
CMD ["python /app/grpc_service.py"]
