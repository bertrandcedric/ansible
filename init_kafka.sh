#!/usr/bin/env bash
cd ansible

export ENV_PROJECT=kafka

docker-compose -f compose/${ENV_PROJECT}-compose.yml -p ${ENV_PROJECT} stop
docker-compose -f compose/${ENV_PROJECT}-compose.yml -p ${ENV_PROJECT} rm
docker network rm ${ENV_PROJECT}_default

docker network create ${ENV_PROJECT}_default
docker-compose -f compose/${ENV_PROJECT}-compose.yml -p ${ENV_PROJECT} scale kafka=2 zoo_keeper=1 graphite=1 grafana=1 client=1

ansible-playbook prerequis.yml -i hosts/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k
