# stage 1: build
FROM gradle as search-engine-build
COPY /java-search-engine /root/app
WORKDIR /root/app/
RUN cd /root/app/ \
    && gradle clean build shadowJar \
    && mv /root/app/build/libs/search-engine-0.0.1-all.jar /root/

# stage 2: run
FROM openjdk:8
ARG ES_ADDRESS
ARG SPARK_ADDRESS
ARG SPARK_CORES_MAX
ENV ES_ADDRESS="${ES_ADDRESS}"
ENV SPARK_ADDRESS="${SPARK_ADDRESS}"
ENV SPARK_CORES_MAX="${SPARK_CORES_MAX}"
WORKDIR /root/app/
COPY --from=search-engine-build /root/search-engine-0.0.1-all.jar .
RUN mkdir /root/app/libs/ \
    && curl https://search.maven.org/remotecontent?filepath=org/elasticsearch/elasticsearch-spark-20_2.11/7.6.2/elasticsearch-spark-20_2.11-7.6.2.jar \
    --output /root/app/libs/elasticsearch-spark-20_2.11-7.6.2.jar
COPY --from=search-engine-build /root/search-engine-0.0.1-all.jar /root/app/libs/search-engine-0.0.1-all.jar
EXPOSE 9091
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar -Dspring.profiles.active=docker search-engine-0.0.1-all.jar"]