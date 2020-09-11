**HOW TO:**
1. download and install Protocol Buffers (a.k.a., protobuf), I recommend via "choco" (for windows):<br>
   **<i>choco install protoc --pre</i>** 
2. checkout project, cd to root
3. gradle build bootRun
4. docker-compose up -d
6. go to localhost:9696
7. connect to host.docker.internal:9090 (or choose proto file manually)
8. choose CrawlerService from service dropdown, choose method startCrawling
9. insert VK group domain to the request list
10. checkout Elastic Search index "wall_posts" on localhost:1358
