# Demo

## Etape 0:

- Mise à jour Ansible
```
git pull
git submodule update --init --recursive
```

- Build de l'image docker
```
docker-machine create --driver virtualbox dev
docker-machine start dev
eval "$(docker-machine env dev)"

docker rm -f $(docker ps -a -q)
docker rmi -f $(docker images -a -q)

docker build --build-arg pwd=XXXX --no-cache -t centos_ssh centos_ssh/.
```

## Etape 1:
- Démarrage des instances de replica rs0 + client
```
export ANSIBLE_HOST_KEY_CHECKING=False
export NB_MACHINE=3;
docker-compose -p sample scale client=1 replica=3
docker network inspect bridge
```

- Installation des prérequis => Use case courant connexion sans mot de passe (Intégration continue)
```
ansible -m ping -i hosts/mongo all -u root -k
ansible-playbook prerequis.yml -i hosts/mongo --extra-vars "{'public_ssh_key' : '$(cat ~/.ssh/id_rsa.pub)'}" -k
ansible -m ping -i hosts/mongo all -u deploy
```

- Création d'un replica rs0
```
ansible-playbook mongo.yml -i hosts/mongo -t replica
ssh 127.0.0.1 -p `docker port sample_client_1 22| sed 's/.*://'` -l deploy
sudo su - mongod
mongo --host {{machine server}}
rs.status() => montrer les secondaires et primaire
```

- Simulation coupure reseau => docker pause / docker unpause
```
docker pause {{docker_id}}
docker unpause {{docker_id}}

ansible-playbook mongo.yml -i hosts/mongo -t replica
```

- Simulation arrêt machine => docker stop / docker start
```
docker stop {{docker_id}}
docker start {{docker_id}}

ansible-playbook mongo.yml -i hosts/mongo -t replica
```

- Alimentation en données du replica => manuel
```
sudo su - mongod
mongo --host {{machine server}}
mongostat --host {{machine server}} --port 27017

mongo --host {{machine server}} --port 27017
rs.slaveOk()
load("/script/data.js") => sur le master

db.test_collection.count()
db.test_collection.find()
```

## Etape 2:
- Démarrage des instances de replica rs1 + sharding + config
```
docker-compose -p sample scale client=1 replica=3 config=3 sharding=1
```

- Installation des prérequis
```
ansible-playbook prerequis.yml -i hosts/mongo_shard --extra-vars "{'public_ssh_key' : '$(cat ~/.ssh/id_rsa.pub)'}" -k
ansible -m ping -i hosts/mongo_shard all -u deploy
```

- Mise en place du sharding
```
ansible-playbook mongo.yml -i hosts/mongo_shard

sudo su - mongod
mongo --host {{sharding}} --port 27017 => connection sur la machine sharding

db.stats() => resultset rs1 vide

mongostat --host {{machine server}} --port 27017

sh.enableSharding( "test" )
db.test_collection.createIndex( { number : 1 } )
sh.shardCollection( "test.test_collection", { "number" : 1 } )
 => traitement long (penser à présenter autre choses en attendant)
db.stats()
db.printShardingStatus(true)

```
