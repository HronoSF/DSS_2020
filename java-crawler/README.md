**HOW TO:**
1. download and install Protocol Buffers (a.k.a., protobuf), I recommend via "choco" (for windows):<br>
   **<i>choco install protoc --pre</i>** 
2. docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
3. checkout project, cd to root
4. gradle build bootRun
5. docker run -p 6969:6969 -v /log:/log -d gusaul/grpcox:latest
6. go to localhost:9696
7. connect to host.docker.internal:9090 (or choose proto file manually)
8. insert VK group domain to the request
9. checkout app logs.
