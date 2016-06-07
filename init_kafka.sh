#!/usr/bin/env bash

export ANSIBLE_HOST_KEY_CHECKING=False;
export NB_MACHINE=2;

docker-compose -f compose/kafka-compose.yml -p kafka up -d
docker-compose -f compose/kafka-compose.yml -p kafka scale kafka=2

ansible-playbook ansible/prerequis.yml -i ansible/hosts/kafka/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k 
