# stage 1: clone project repo
FROM gradle as clone
WORKDIR /root/project
COPY . .

# stage 2: build protobuf services from .proto files
FROM gradle as protobuf-build
COPY --from=clone /root/project/java-proto-handler /app/
RUN apt update && apt install -y protobuf-compiler
RUN cd /app/ \
    && gradle clean build \
    && mv /app/build/libs/java-proto-handler-0.0.1.jar /root/

# stage 3: put jar with proto to crawler libs (because artifactory is for weakling) && build jar with crawler
FROM gradle as dataprocessing-build
COPY --from=clone /root/project/java-search-engine /app/
COPY --from=protobuf-build /root/java-proto-handler-0.0.1.jar /app/libs/
RUN cd /app/ \
    && gradle clean build \
    && mv /app/build/libs/search-engine-0.0.1.jar /root/

# stage 4: run data-processing app
FROM bellsoft/liberica-openjdk-alpine:8
WORKDIR app/
ARG SPARK_ADDRESS
ARG ES_ADDRESS
ENV SPARK_ADDRESS="${SPARK_ADDRESS}"
ENV ES_ADDRESS="${ES_ADDRESS}"
COPY --from=dataprocessing-build /root/search-engine-0.0.1.jar .
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar -Dspring.profiles.active=docker search-engine-0.0.1.jar"]