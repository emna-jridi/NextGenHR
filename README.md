
---

### Points clés du fichier `README.md`

1. **Description du projet** : Une introduction claire et concise.
2. **Fonctionnalités** : Une liste détaillée des fonctionnalités offertes par l'application.
3. **Technologies utilisées** : Les outils et langages utilisés pour développer l'application.
4. **Structure de la base de données** : Un aperçu des tables et de leur rôle.
5. **Prérequis** : Les outils nécessaires pour exécuter le projet.

# Application de Gestion des Ressources Humaines (GRH)

Cette application est une solution complète pour la gestion des ressources humaines dans une entreprise. Elle permet de gérer les employés, les congés, les contrats, les formations, les réunions, les bureaux, les candidatures, et bien plus encore.

## Fonctionnalités principales

- **Gestion des employés** :
  - Ajouter, modifier, supprimer et consulter les informations des employés.
  - Gérer les postes, les dates d'embauche, et les coordonnées des employés.

- **Gestion des congés** :
  - Enregistrer les demandes de congés (maladie, maternité, etc.).
  - Suivre l'état des congés (en attente, approuvé, refusé).

- **Gestion des contrats** :
  - Créer et gérer les contrats des employés.
  - Suivre les dates de début et de fin des contrats, ainsi que leur statut.

- **Gestion des formations** :
  - Planifier des formations pour les employés.
  - Suivre les thèmes, les descriptions et les participants.

- **Gestion des réunions** :
  - Organiser des réunions (en ligne ou en présentiel).
  - Gérer les participants, les dates et les descriptions.

- **Gestion des bureaux** :
  - Réserver des bureaux pour les employés.
  - Suivre la disponibilité et la capacité des bureaux.

- **Gestion des candidatures** :
  - Enregistrer les candidatures pour les offres d'emploi.
  - Suivre les dates de soumission et les statuts.

- **Gestion du télétravail** :
  - Enregistrer les demandes de télétravail.
  - Suivre les dates de début, de fin, et les raisons.

- **Gestion des tests** :
  - Enregistrer les résultats des tests techniques et psychotechniques des employés.

## Technologies utilisées

- **Backend** :
  - Java
  - JDBC (Java Database Connectivity) pour la connexion à la base de données.
  - MySQL comme système de gestion de base de données.

- **Frontend** (optionnel, si applicable) :
  - JavaFX ou une interface en ligne de commande (CLI).

- **Outils** :
  - Maven pour la gestion des dépendances.
  - Git pour le contrôle de version.

## Structure de la base de données

La base de données `gestion_ressources_humaines` contient les tables suivantes :

- `employés` : Informations sur les employés.
- `conges` : Demandes de congés des employés.
- `contrat` : Contrats des employés.
- `formations` : Formations proposées aux employés.
- `reunion` : Réunions organisées.
- `bureau` : Bureaux disponibles.
- `reservation_bureau` : Réservations de bureaux.
- `candidature` : Candidatures pour les offres d'emploi.
- `offreemploi` : Offres d'emploi.
- `teletravail` : Demandes de télétravail.
- `tests` : Résultats des tests des employés.
- `utilisateurs` : Informations sur les utilisateurs de l'application.

## Prérequis

Avant de commencer, assurez-vous d'avoir installé les éléments suivants :

- **Java Development Kit (JDK)** : Version 11 ou supérieure.
- **MySQL** : Serveur de base de données.
- **Maven** : Pour la gestion des dépendances.
- **Git** : Pour cloner le dépôt.
