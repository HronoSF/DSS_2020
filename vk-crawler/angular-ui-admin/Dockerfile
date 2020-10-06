FROM node
COPY ./angular-ui-admin/ /app/
COPY ./java-proto-handler/ /app/java-proto-handler
WORKDIR /app
RUN npm install & \
    npm install -g @angular/cli
EXPOSE 4200/tcp
RUN apt update && \
    apt install -y protobuf-compiler && \ 
	protoc --plugin=protoc-gen-ts=./node_modules/.bin/protoc-gen-ts \
	--js_out=import_style=commonjs,binary:src/app/proto-gen \
	--ts_out=service=grpc-web:src/app/proto-gen -I java-proto-handler/src/main/proto/ \
	java-proto-handler/src/main/proto/crawler.proto
CMD ng serve --host 0.0.0.0
