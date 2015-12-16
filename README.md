# README du container docker

## Lancement d'un premier container

```
docker run --name=env -d test /bin/sh -c "while true; do echo hello world; sleep 1; done"
```

## Lancement d'un second container lié au container précédemment crée

```
docker run --link env -ti test

ping dev
```

## Montage d'un filesystem externe au container

```
docker create --volume /c/Users/cb1791dn:/cb1791dn --name fs test /bin/true

docker run --volumes-from fs -ti test
```

cat ~/.ssh/id_rsa.pub | docker exec --interactive centos sh -c 'umask 077; mkdir -p ~/.ssh && cat >> ~/.ssh/authorized_keys'

## Ansible

```
ansible -m ping -i hosts all -u root -k -c paramiko

ansible-playbook prerequis.yml -i hosts --extra-vars "{\"public_ssh_key\" : \"$(cat ~/.ssh/id_rsa.pub)\"}" -k -c paramiko

ansible -m ping -i hosts all -u deploy
```
