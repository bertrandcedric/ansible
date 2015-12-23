# Utilisation du container CENTOS

## Build et lancement d'un container centos

```
docker rm $(docker ps -a -q)
docker build --no-cache -t centos_ssh centos_ssh/.
```

## Provisionning avec ansible sur les containers centos

```
docker-compose -p sample up -d
docker network inspect bridge

export ANSIBLE_HOST_KEY_CHECKING=False
export NB_MACHINE=3;
ansible -m ping -i ansible/hosts all -u root -k -c paramiko
ansible-playbook ansible/prerequis.yml -i ansible/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k -c paramiko
ansible -m ping -i ansible/hosts all -u deploy
ansible-playbook ansible/deploy.yml -i ansible/hosts

ansible -m debug -a var=hostvars -i ansible/hosts env -u deploy

ssh 192.168.99.100 -p {{port_machine client}} -l deploy

mongo --host {{machine server}}

docker-compose -p sample pause machine_1
docker-compose -p sample unpause machine_1
docker-compose -p sample stop machine_1
docker-compose -p sample start machine_1
docker-compose -p sample stop
```

## Autres commandes

docker run --name=env -d test /bin/sh -c "while true; do echo hello world; sleep 1; done"
docker run --link env -ti test
ping dev
docker create --volume /c/Users/cb1791dn:/cb1791dn --name fs test /bin/true
docker run --volumes-from fs -ti test

cat ~/.ssh/id_rsa.pub | docker exec --interactive centos sh -c 'umask 077; mkdir -p ~/.ssh && cat >> ~/.ssh/authorized_keys'


TODO :
- passage a ansible v2
- mettre à jour le README
- tester la mise en pause d'un container
- tester l'arrêt d'un container et le redémarrage
- tester la suppression et la recreation d'un container
- configuration des configs
- configuration des shards
