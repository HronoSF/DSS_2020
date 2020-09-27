# stage 1: clone project repo
FROM alpine/git:v2.24.3 as clone
WORKDIR /root/project
RUN git clone "https://github.com/HronoSF/DSS_2020.git"

# stage 2: build protobuf services from .proto files
FROM gradle as protobuf-build
COPY --from=clone /root/project/DSS_2020/vk-crawler/java-proto-hadler /app/
RUN apt update && apt install -y protobuf-compiler
RUN cd /app/ \
    && gradle clean build \
    && mv /app/build/libs/java-proto-handler-0.0.1.jar /root/

# stage 3: put jar with proto to crawler libs (because artifactiry is for weakling) && run crawler app
# we have to use gradle image instead of classic approach with .jar because of hadoop issue: https://github.com/elastic/elasticsearch-hadoop/issues/1358
FROM gradle
COPY --from=clone /root/project/DSS_2020/vk-crawler/java-data-processing /app/
COPY --from=protobuf-build /root/java-proto-handler-0.0.1.jar /app/libs/
WORKDIR /app
ENTRYPOINT ["sh", "-c"]
CMD ["SPRING_PROFILES_ACTIVE=docker gradle bootRun"]