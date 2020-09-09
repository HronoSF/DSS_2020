**HOW TO:**
1. download and install Protocol Buffers (a.k.a., protobuf), I recommend via "choco" (for windows):<br>
   **<i>choco install protoc --pre</i>** 
2. docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
3. gradle build bootRun
4. docker run -p 6969:6969 -v /log:/log -d gusaul/grpcox:latest
5. go to localhost:9696
6. connect to host.docker.internal:9090 (or choose proto file manually)
7. insert VK group domain to the request
8. checkout app logs.
