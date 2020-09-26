FROM alpine/git:v2.24.3 as clone
WORKDIR /root/project
RUN git clone "https://github.com/HronoSF/DSS_2020.git"

FROM gradle as protobuf-build
COPY --from=clone /root/project/DSS_2020/vk-crawler /app/
RUN apt update
RUN apt install -y protobuf-compiler
RUN cd /app/java-proto-handler && gradle clean build
