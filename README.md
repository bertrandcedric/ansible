# Utilisation du container CENTOS

## Configuration vm virtualbox et partage d'un repertoire de travail

```
/cygdrive/c/Program\ Files/Oracle/VirtualBox/VBoxManage sharedfolder add dev --name /apps --hostpath c:\\apps --automount
sudo mkdir /data
sudo mount -t vboxsf /apps /data

docker-machine create --driver virtualbox --engine-insecure-registry zed33xg1.distribution.edf.fr:444 dev
/var/lib/boot2docker/profile
```

## Build et lancement d'un container centos

```
docker rm $(docker ps -a -q)
docker build --build-arg pwd=XXXX --no-cache -t centos_ssh centos_ssh/.

docker run -ti --volume /data:/titi centos
```

## Provisionning avec ansible sur les containers centos

```
docker-compose -p sample scale client=1 config=3 replica=3 sharding=1
docker-compose -p sample scale client=1 server=3
docker network inspect bridge

export ANSIBLE_HOST_KEY_CHECKING=False
export NB_MACHINE=3;
ansible -m ping -i ansible/mongo_hosts all -u root -k -c paramiko
ansible-playbook ansible/prerequis.yml -i ansible/mongo_hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k -c paramiko
ansible -m ping -i ansible/mongo_hosts all -u deploy
ansible-playbook ansible/mongo.yml -i ansible/mongo_hosts

ansible -m debug -a var=hostvars -i ansible/mongo_hosts env -u deploy
ansible -m setup -i ansible/mongo_hosts env -u deploy

docker-compose -p sample pause machine_1
docker-compose -p sample unpause machine_1
docker-compose -p sample stop machine_1
docker-compose -p sample start machine_1
docker-compose -p sample stop
docker-compose -p sample rm
```

## Test config mongodb

```
ssh 192.168.99.100 -p `docker port sample_client_1 | sed 's/.*://'` -l deploy

mongo --host {{machine server}}
```

## Test avec données

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

docker run --name=env -d test /bin/sh -c "while true; do echo hello world; sleep 1; done"
docker run --link env -ti test
ping dev
docker create --volume /c/Users/cb1791dn:/cb1791dn --name fs test /bin/true
docker run --volumes-from fs -ti test

cat ~/.ssh/id_rsa.pub | docker exec --interactive centos sh -c 'umask 077; mkdir -p ~/.ssh && cat >> ~/.ssh/authorized_keys'

echo stat | nc 127.0.0.1 2181
zkCli.sh -server 127.0.0.1:2181

TODO :
- passage a ansible v2
- mettre à jour le README
- tester la mise en pause d'un container
- tester l'arrêt d'un container et le redémarrage
- tester la suppression et la recreation d'un container
