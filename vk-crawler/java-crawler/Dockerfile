# stage 1: build
FROM gradle as crawler-build
COPY /java-crawler /app
RUN cd /app/ \
    && gradle clean build \
    && mv /app/build/libs/crawler-0.0.1.jar /root/

# stage 2: run
FROM bellsoft/liberica-openjdk-alpine:8
ARG VK_APP_KEY
ARG VK_APP_ID
ARG ES_ADDRESS
ENV ES_ADDRESS="${ES_ADDRESS}"
ENV VK_APP_KEY="${VK_APP_KEY}"
ENV VK_APP_ID="${VK_APP_ID}"
EXPOSE 9090
WORKDIR app/
COPY --from=crawler-build /root/crawler-0.0.1.jar .
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar -Dspring.profiles.active=docker crawler-0.0.1.jar"]