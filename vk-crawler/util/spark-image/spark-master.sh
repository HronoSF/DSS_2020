#!/bin/bash

unset SPARK_MASTER_PORT

echo "$(hostname -i) spark-master" >> /etc/hosts
echo "spark.driver.host $(hostname -i)" >> /opt/bitnami/spark/conf/spark-defaults.conf
echo "spark.driver.bindAddress $(hostname -i)" >> /opt/bitnami/spark/conf/spark-defaults.conf

/opt/bitnami/java/bin/java -cp /opt/bitnami/spark/conf/:/opt/bitnami/spark/jars/* -Xmx1g org.apache.spark.deploy.master.Master --ip spark-master --port 7077 --webui-port 8080