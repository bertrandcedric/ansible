#Ansible pour les développeurs

##Ansible ou comment le développeur reprend le pouvoir sur la production

Après une présentation des concepts autour d'Ansible, nous verrons comment le développeur peut mettre en place les playbooks de déploiement de son application travaillant ainsi main dans la main avec les équipes d'intégration/production.
Un outil idéal dans la mise en place d'un démarche DevOps.
Démonstration des concepts en couplant Ansible avec Docker.

##Presentation des concepts Ansible
- Placer Ansible par rapport aux autres outils (Puppet/Chef/Salt ...)
- Principe de base => sans agent / SSH
- Hosts => test d'un ping de machine
- Module et notion d'idempotence => test d'un module (creation d'un repertoire)
- Tâches et playbooks => sample.yml
- Role
- Variable
- Notion importante :
    - Templating avec Jinja2
```   
rs.initiate()
sleep(2000)
{% for host in groups['config'] %}
rs.add("{{ hostvars[host]['ansible_nodename'] }}:{{config.port}}")
sleep(2000)
{% endfor %}
printjson(rs.status())
```
    - Condition
    - Boucle
    - Blocks (v2)
    - Stratégie (v2)

##Pourquoi le developpeur doit utiliser Ansible

- Aller plus rapidement et plus sereinement en production => tester les scripts de deploiement le plus tôt
- Travailler main dans la main avec les OPS => mise en place d'une démarche DEVOPS
- Test unitaire (presenter mais pas de démo)

##Demo Ansible avec VM + Docker
- Explication du container Docker avec sshd
- Mise en place d'un cluster MongoDB (replica + shard)
- Supervision pendant les tests