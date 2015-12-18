# Utilisation du container CENTOS

## Build et lancement d'un container centos

docker build --no-cache -t centos centos_ssh/.
docker rm -f centos
docker run -d -P --name centos centos

## Build et lancement de plusieurs containers centos avec docker compose

## Autres commandes

docker run --name=env -d test /bin/sh -c "while true; do echo hello world; sleep 1; done"
docker run --link env -ti test
ping dev
docker create --volume /c/Users/cb1791dn:/cb1791dn --name fs test /bin/true
docker run --volumes-from fs -ti test

cat ~/.ssh/id_rsa.pub | docker exec --interactive centos sh -c 'umask 077; mkdir -p ~/.ssh && cat >> ~/.ssh/authorized_keys'


## Provisionning avec ansible sur les containers centos

```
docker-compose -p ansible up -d

export ANSIBLE_HOST_KEY_CHECKING=False

ansible -m ping -i ansible/hosts all -u root -k -c paramiko
ansible-playbook ansible/prerequis.yml -i ansible/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k -c paramiko
ansible -m ping -i ansible/hosts all -u deploy
ansible-playbook ansible/deploy.yml -i ansible/hosts

docker-compose -p ansible stop

ansible -m debug -a var=hostvars -i ansible/hosts env -u deploy
```


TODO :
- ajouter dans le mongo dans le PATH
- exporter les ports mongod dans docker
