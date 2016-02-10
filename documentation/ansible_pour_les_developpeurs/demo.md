# Demo

## Etape 1:
- Démarrage des instances de replica rs0 + client
- Installation des prérequis => Use case courant connexion sans mot de passe (Intégration continue)
- Mise en place du replica rs0
- Création d'un replica rs0
- Alimentation en données du replica => manuel
- Simulation coupure reseau => docker pause / docker unpause
- Simulation perte machine => docker rm -f / docker-compose scale

## Etape 2:
- Démarrage des instances de replica rs1 + sharding + config
- Installation des prérequis
- Mise en place du sharding
- Création d'un replica rs1
- Création des serveurs de configuration
- Création du serveur de sharding
- Activation du sharding + creation index + sharding de la collection => manuel
- Supervision pendant les tests
