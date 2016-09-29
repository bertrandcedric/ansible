#!/usr/bin/env bash
cd ..

export PROJECT=$1

docker-compose -f compose/${PROJECT}-compose.yml -p ${PROJECT} stop
docker-compose -f compose/${PROJECT}-compose.yml -p ${PROJECT} rm
docker network rm ${PROJECT}_default
