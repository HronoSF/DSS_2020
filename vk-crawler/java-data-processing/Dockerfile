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

# stage 3: put jar with proto to crawler libs (because artifactory is for weakling) && build jar with crawler
FROM gradle as dataprocessing-build
COPY --from=clone /root/project/DSS_2020/vk-crawler/java-data-processing /app/
COPY --from=protobuf-build /root/java-proto-handler-0.0.1.jar /app/libs/
RUN cd /app/ \
    && gradle clean build \
    && mv /app/build/libs/data-processing-0.0.1.jar /root/

# stage 4: run data-processing app
FROM bellsoft/liberica-openjdk-alpine:8
WORKDIR app/
COPY --from=crawler-build /root/data-processing-0.0.1.jar .
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar -Dspring.profiles.active=docker data-processing-0.0.1.jar"]