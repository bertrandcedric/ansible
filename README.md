# Utilisation du container docker avec CENTOS

## 1. Build et lancement d'un container centos

### => Build d'une image
```
docker build --build-arg pwd=XXXX --no-cache -t centos_ssh centos_ssh/.
```

### => Suppression des containers
```
docker rm -f $(docker ps -a -q)
```

### => Lancement d'une image
```
docker run -ti centos_ssh /bin/sh
docker run -ti --volume /data:/titi centos_ssh /bin/sh
docker run -dP centos_ssh
```

## 2. Provisionning avec ansible

### => Mise à jour ansible
```
git submodule update --init --recursive
```

### Test ansible sur une vm docker
```
docker-compose -f compose/kafka-compose.yml -p kafka stop
docker-compose -f compose/kafka-compose.yml -p kafka rm
docker network rm kafka_default
docker network create kafka_default
docker-compose -f compose/kafka-compose.yml -p kafka scale kafka=2 zoo_keeper=1 graphite=1 grafana=1 client=1

export ENV_PROJECT=kafka

ansible -m ping -i hosts/hosts all -u root -k

ansible-playbook prerequis.yml -i hosts/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k

ansible -m debug -a "var=hostvars[inventory_hostname]" -i hosts/hosts all -u deploy
```
