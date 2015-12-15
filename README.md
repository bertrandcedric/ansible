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
