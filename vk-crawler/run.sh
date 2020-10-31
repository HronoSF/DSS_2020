#!/bin/bash

if [[ "$#" -ge 1 ]]; then
	if [[ "$1" == "build" || "$2" == "--build" ]]; then
		tput setaf 1; echo $'\nBuilding Spark base image\n'; tput sgr0;
		eval docker build -t vk-crawler-spark-base-image . || exit 1

		tput setaf 1; echo $'\nBuilding and publishing to artifactory jar with generated classes from proto files\n'; tput sgr0;
		eval docker build -t proto-handler java-proto-handler || exit 1
		eval docker run proto-handler || exit 1

		tput setaf 1; echo $'\nBuilding backend containers stage\n'; tput sgr0;
		eval docker-compose -f docker-compose-services.yml build || exit 1
		
		tput setaf 1; echo $'\nBuilding frontend containers stage\n'; tput sgr0;
		eval docker-compose -f docker-compose-ui.yml build || exit 1
	fi

	if [ "$1" == "up" ]; then
		tput setaf 2; echo $'\nStartup stage\n'; tput sgr0;
		eval docker-compose -f docker-compose-infra.yml $@ --scale spark-worker=4 || exit 1

		# time for db to init
		eval sleep 10

		eval docker-compose -f docker-compose-services.yml $@ || exit 1
		eval docker-compose -f docker-compose-ui.yml $@ || exit 1
	fi
	
	if [ "$1" == "down" ]; then
		tput setaf 2; echo $'\nDown stage\n'; tput sgr0;
		eval docker-compose -f docker-compose-services.yml $@ || exit 1
		eval docker-compose -f docker-compose-ui.yml $@ || exit 1
		eval docker-compose -f docker-compose-infra.yml $@ || exit 1
	fi

else
  echo "Usage:
  run.sh build :: building containers
  run.sh up :: only running already build containers
  run.sh up --build :: build and run containers
  run.sh down :: stop all service containers"
fi