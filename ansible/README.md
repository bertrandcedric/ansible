## Build d'une image CENTOS
```
docker build --build-arg pwd=XXXX --no-cache -t centos_ssh centos_ssh/.
```

## Provisionning avec ansible pour l'exemple elasticsearch

```
docker network create elasticsearch_default
docker-compose -f ansible/compose/elasticsearch-compose.yml -p elasticsearch scale elasticsearch=2 kibana=1

ENV_PROJECT=elasticsearch ansible-playbook ansible/playbook_prerequis.yml -i ansible/hosts/hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k
ENV_PROJECT=elasticsearch ansible-playbook ansible/playbook_elasticsearch.yml -i ansible/hosts/hosts

ssh 127.0.0.1 -p `docker port elasticsearch_elasticsearch_1 22| sed 's/.*://'` -l deploy
```
