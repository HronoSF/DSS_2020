#!/bin/bash

DOCKER_BASE="docker-compose -f docker-compose-infra.yml \
      -f docker-compose-ui.yml \
	  -f docker-compose-services.yml"
	 
if [[ "$#" -ge 1 ]]; then
	if [[ "$1" == "build" || "$2" == "--build" ]]; then
		echo "Building backend containers stage"
		eval docker-compose -f docker-compose-services.yml build || exit 1
		
		echo "Building frontend containers stage"
		eval docker-compose -f docker-compose-ui.yml build || exit 1
	fi
	
	echo "Container stage"
	eval $DOCKER_BASE | docker-compose -f - $@ || exit 1
else
  echo "Usage:
  run.sh build :: building containers
  run.sh up :: only running already build containers
  run.sh up --build :: build and run containers"
fi