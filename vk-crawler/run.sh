#!/bin/bash

if [[ "$#" -ge 1 ]]; then
	if [[ "$1" == "build" || "$2" == "--build" ]]; then
		tput setaf 1; echo $'\nBuilding Spark base image\n'; tput sgr0;
		eval docker build -t vk-crawler-spark-base-image ./util || exit 1

		tput setaf 1; echo $'\nBuilding and publishing to artifactory jar with generated classes from proto files\n'; tput sgr0;
		# eval docker build -t proto-handler java-proto-handler || exit 1
		# eval docker run proto-handler || exit 1

		tput setaf 1; echo $'\nBuilding backend containers stage\n'; tput sgr0;
		eval docker-compose -f docker-compose-services.yml build || exit 1
		
		tput setaf 1; echo $'\nBuilding frontend containers stage\n'; tput sgr0;
		eval docker-compose -f docker-compose-ui.yml build || exit 1

		tput setaf 2; echo $'\nCrawler images built successfully\n'; tput sgr0;
	fi

	if [ "$1" == "pull" ]; then
		tput setaf 2; echo $'\nPull infrastructure\n'; tput sgr0;
		eval docker-compose -f docker-compose-infra.yml pull || exit 1

		tput setaf 2; echo $'\nPull backend\n'; tput sgr0;
		eval docker-compose -f docker-compose-services.yml pull || exit 1

		tput setaf 2; echo $'\nPull frontend\n'; tput sgr0;
		eval docker-compose -f docker-compose-ui.yml pull || exit 1
	fi
	
	if [ "$1" == "up" ]; then
		tput setaf 2; echo $'\nStartup infrastructure\n'; tput sgr0;
		eval docker-compose -f docker-compose-infra.yml $@ --scale spark-worker=2 || exit 1

		# time for db to init
		eval sleep 10

		tput setaf 2; echo $'\nStartup backed services\n'; tput sgr0;
		eval docker-compose -f docker-compose-services.yml $@ || exit 1

		tput setaf 2; echo $'\nStartup ui apps\n'; tput sgr0;
		eval docker-compose -f docker-compose-ui.yml $@ || exit 1

		tput setaf 2; echo $'\nCrawler started successfully\n'; tput sgr0;
	fi
	
	if [ "$1" == "down" ]; then
		tput setaf 2; echo $'\nDown stage\n'; tput sgr0;
		eval docker-compose -f docker-compose-services.yml $@ || exit 1
		eval docker-compose -f docker-compose-ui.yml $@ || exit 1
		eval docker-compose -f docker-compose-infra.yml $@ || exit 1

		tput setaf 2; echo $'\nCrawler stopped successfully\n'; tput sgr0;
	fi

else
  echo "Usage:
  run.sh build :: building containers
  run.sh up :: only running already build containers
  run.sh up --build :: build and run containers
  run.sh down :: stop all service containers
  run.sh pull :: pull all service containers"
fi