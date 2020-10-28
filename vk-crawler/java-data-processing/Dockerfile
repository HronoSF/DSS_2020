FROM gradle
COPY /java-data-processing /root/app/
ARG SPARK_ADDRESS
ARG ES_ADDRESS
ENV SPARK_ADDRESS="${SPARK_ADDRESS}"
ENV ES_ADDRESS="${ES_ADDRESS}"
WORKDIR /root/app/
ENTRYPOINT ["sh", "-c"]
CMD ["SPRING_PROFILES_ACTIVE=docker gradle bootRun -b /root/app/build.gradle"]