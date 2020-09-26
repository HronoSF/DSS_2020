# stage 1: clone project repo
FROM alpine/git:v2.24.3 as clone
WORKDIR /root/project
RUN git clone "https://github.com/HronoSF/DSS_2020.git"

# stage 2: build protobuf services from .proto files
FROM gradle as protobuf-build
COPY --from=clone /root/project/DSS_2020/vk-crawler /app/
RUN apt update && \
    apt install -y protobuf-compiler
RUN cd /app/java-proto-hadler \
    && gradle clean build \
    && mv /app/java-proto-hadler/build/libs/java-proto-handler-0.0.1.jar /root/

# stage 3: put jar with proto to crawler libs (because artifactiry is for weakling) && build jar with crawler
FROM gradle as crawler-build
COPY --from=clone /root/project/DSS_2020/vk-crawler /app/
COPY --from=protobuf-build /root/java-proto-handler-0.0.1.jar /app/java-crawler/libs/
RUN cd /app/java-crawler \
    && gradle clean build \
    && mv /app/java-crawler/build/libs/crawler-0.0.1.jar /root/

# stage 4: run crawler app
FROM bellsoft/liberica-openjdk-alpine:8
ARG VK_APP_KEY
ARG VK_APP_ID
ENV VK_APP_KEY="${VK_APP_KEY}"
ENV VK_APP_ID="${VK_APP_ID}"
EXPOSE 9090
WORKDIR app/
COPY --from=crawler-build /root/crawler-0.0.1.jar .
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar -Dspring.profiles.active=docker crawler-0.0.1.jar"]
