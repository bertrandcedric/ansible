#!/usr/bin/env bash

export ANSIBLE_HOST_KEY_CHECKING=False;
export ENV_PROJECT=kafka

docker network create ${ENV_PROJECT}_default
docker-compose -f compose/${ENV_PROJECT}-compose.yml -p ${ENV_PROJECT} scale kafka=2 zoo_keeper=1 graphite=1 grafana=1 client=1

ansible-playbook ansible/prerequis.yml -i ansible/hosts/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k
