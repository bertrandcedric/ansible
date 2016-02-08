#Ansible pour les développeurs

##Ansible ou comment le développeur reprend le pouvoir sur la production

Après une présentation des concepts autour d'Ansible, nous verrons comment le développeur peut mettre en place les playbooks de déploiement de son application travaillant ainsi main dans la main avec les équipes d'intégration/production.
Un outil idéal dans la mise en place d'un démarche DevOps.
Démonstration des concepts en couplant Ansible avec Docker.

## Demo

Etape 1:
- Démarrage des instances de replica rs0 + client
- Installation des prérequis => Use case courant connexion sans mot de passe (Intégration continue)

Etape 2:
- Mise en place du replica rs0
- Création d'un replica rs0
- Alimentation en données du replica => manuel
- Simulation coupure reseau => docker pause / docker unpause
- Simulation perte machine => docker rm -f / docker-compose scale

Etape 3:
- Démarrage des instances de replica rs1 + sharding + config
- Installation des prérequis
- Mise en place du sharding
- Création d'un replica rs1
- Création des serveurs de configuration
- Création du serveur de sharding
- Activation du sharding + creation index + sharding de la collection => manuel
- Supervision pendant les tests

##Presentation des concepts Ansible
- Placer Ansible par rapport aux autres outils (Puppet/Chef/Salt ...)
- Principe de base => sans agent / SSH
- Script / Ansible = Ant / Maven
- Hosts
- Module et notion d'idempotence
- Tâches et playbooks
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

##Pourquoi et comment le developpeur doit utiliser Ansible
- Aller plus rapidement et plus sereinement en production => tester les scripts de deploiement le plus tôt
- Travailler main dans la main avec les OPS => mise en place d'une démarche DEVOPS
- Ansible via cygwin sur un poste de développeur windows
- Test unitaire (presenter mais pas de démo)

##Demo Ansible avec VM + Docker
- Explication du container Docker avec sshd
- Faire un schema de la cible mongo à atteindre
- Mise en place d'un cluster MongoDB (replica + shard)
    - Installation des prérequis
    - Mise en place du replica rs0
        - Création d'un replica rs0
        - Alimentation en données du replica => manuel
        - Simulation coupure reseau => docker pause / docker unpause
        - Simulation perte machine => docker rm -f / docker-compose scale
    - Installation des prérequis
    - Mise en place du sharding
        - Création d'un replica rs1
        - Création des serveurs de configuration
        - Création du serveur de sharding
        - Activation du sharding + creation index + sharding de la collection => manuel
                
- Supervision pendant les tests
