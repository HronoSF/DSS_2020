# stage 1: clone project repo
FROM alpine/git:v2.24.3 as clone
WORKDIR /root/project
RUN git clone "https://github.com/HronoSF/DSS_2020.git"

FROM node
COPY --from=clone /root/project/DSS_2020/vk-crawler/angular-ui-admin /app/
COPY --from=clone /root/project/DSS_2020/vk-crawler/java-proto-hadler /app/java-proto-hadler
WORKDIR /app
RUN npm install & \
    npm install -g @angular/cli
EXPOSE 4200/tcp

#Compile proto files
RUN apt update && \
    apt install -y protobuf-compiler
RUN protoc --plugin=protoc-gen-ts=./node_modules/.bin/protoc-gen-ts --js_out=import_style=commonjs,binary:src/app/proto-gen --ts_out=service=grpc-web:src/app/proto-gen -I java-proto-handler/src/main/proto/ java-proto-handler/src/main/proto/crawler.proto

# Serve the app
RUN npm run proxy&
RUN npm run proxy2&
CMD ng serve --host 0.0.0.0
