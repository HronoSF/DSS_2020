# stage 1: clone project repo
FROM gradle as clone
WORKDIR /root/project
COPY . .
ENTRYPOINT ["sh", "-c"]
CMD ["gradle build"]