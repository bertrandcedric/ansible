# Utilisation du container CENTOS

## Configuration vm virtualbox et partage d'un repertoire de travail

```
docker-machine create --driver virtualbox --engine-insecure-registry zed33xg1.distribution.edf.fr:1025 dev

VBoxManage sharedfolder add dev --name /apps --hostpath c:\\apps --automount
sudo mkdir /data
sudo mount -t vboxsf /apps /data

docker-machine create --driver virtualbox dev

fichier de configuration du daemon docker => /var/lib/boot2docker/profile
```

## Build et lancement d'un container centos

### Build d'une image
```
docker build --build-arg pwd=XXXX --no-cache -t centos_ssh centos_ssh/.
```

### Suppression des containers
```
docker rm -f $(docker ps -a -q)
```

### Lancement d'une image
```
docker run -ti centos_ssh /bin/sh
docker run -ti --volume /data:/titi centos_ssh /bin/sh
docker run -dP centos_ssh
```

## Provisionning avec ansible pour l'exemple

### Mise à jour ansible
```
git submodule update --init --recursive
```

### Test ansible sur une vm docker
```
docker-compose -p sample scale client=1

ansible -m ping -i hosts/hosts all -u root -k
ansible-playbook sample.yml -i hosts/hosts -u root -k
```

## Provisionning avec ansible d'un cluster Zookeeper

### Initialisation du client et des serveurs pour Zookeeper
```
export ANSIBLE_HOST_KEY_CHECKING=False
export NB_MACHINE=3;

docker-compose -p sample scale client=1 server=3
docker network inspect bridge

ansible -m ping -i hosts/sample all -u root -k
ansible-playbook prerequis.yml -i hosts/sample --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k
ansible -m ping -i hosts/sample all -u deploy
ansible-playbook zookeeper.yml -i hosts/sample
```

### Test à réaliser
```
ssh 192.168.99.100 -p `docker port sample_client_1 | sed 's/.*://'` -l deploy

echo stat | nc 172.17.0.3 2181 | grep Mode && echo stat | nc 172.17.0.4 2181 | grep Mode  && echo stat | nc 172.17.0.5 2181 | grep Mode
zkCli.sh -server 172.17.0.3:2181

docker pause sample_server_2
docker unpause sample_server_2

docker stop sample_server_2
docker stop sample_server_2

docker rm -f sample_server_2
docker-compose -p sample scale client=1 server=3
```

## Provisionning avec ansible d'un cluster Mongodb

### Initialisation du client et des serveurs pour Mongodb
```
export ANSIBLE_HOST_KEY_CHECKING=False
export NB_MACHINE=3;

docker-compose -p sample scale client=1 config=3 replica=3 sharding=1
docker network inspect bridge

ansible -m ping -i hosts/mongo all -u root -k
ansible-playbook prerequis.yml -i hosts/mongo --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k
ansible -m ping -i hosts/mongo all -u deploy
ansible-playbook mongo.yml -i hosts/mongo
```

### Test config mongodb

```
mongo --host {{machine server}}
```

### Test avec données

```
mongo {{sharding_nodename}}:27017
load("/script/data.js")

db.test_collection.count()
db.test_collection.find()
sh.enableSharding( "test" )
db.test_collection.createIndex( { number : 1 } )
sh.shardCollection( "test.test_collection", { "number" : 1 } )
db.stats()
db.printShardingStatus(true)
```

## Autres commandes

```
ansible -m debug -a var=hostvars -i {{host}} env -u deploy
ansible -m setup -i {{host}} env -u deploy

docker run --name=env -d test /bin/sh -c "while true; do echo hello world; sleep 1; done"
docker run --link env -ti test
docker create --volume /c/Users/cb1791dn:/cb1791dn --name fs test /bin/true
docker run --volumes-from fs -ti test
```
