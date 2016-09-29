#!/usr/bin/env bash
cd ..

export PROJECT=$1

docker network create ${PROJECT}_default

docker-compose -f compose/${PROJECT}-compose.yml -p ${PROJECT} scale $2

ENV_PROJECT=${PROJECT} ansible-playbook playbook_prerequis.yml -i hosts/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k

ENV_PROJECT=${PROJECT} ansible -m ping -i hosts/hosts all -u deploy
