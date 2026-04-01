# <!--meni-projet-java2-cpi2 -->



\# Documentation Technique

\## Système de Gestion d'un Restaurant en Java



\---



\*\*Module\*\* : Programmation Orientée Objet  

\*\*Filière\*\* : Cycle Préparatoire Intégré — MPI/MI  

\*\*Niveau\*\* : CPI 2 — Groupe A  

\*\*Enseignant\*\* : M. Sofiane HACHICHA  

\*\*Encadrante TP\*\* : Mme. Nesrine AKROUT  

\*\*Année Universitaire\*\* : 2025 — 2026  

\*\*Établissement\*\* : ISIMG Gabès



\---



\## Table des matières



1\. \[Introduction](#1-introduction)

2\. \[Technologies utilisées](#2-technologies-utilisées)

3\. \[Architecture du projet](#3-architecture-du-projet)

4\. \[Conception UML](#4-conception-uml)

&#x20;  - 4.1 \[Diagramme de cas d'utilisation](#41-diagramme-de-cas-dutilisation)

&#x20;  - 4.2 \[Diagramme de classes](#42-diagramme-de-classes)

5\. \[Base de données](#5-base-de-données)

&#x20;  - 5.1 \[Schéma relationnel](#51-schéma-relationnel)

&#x20;  - 5.2 \[Description des tables](#52-description-des-tables)

6\. \[Couche Modèle (Model)](#6-couche-modèle-model)

7\. \[Couche DAO](#7-couche-dao)

8\. \[Couche Contrôleur (Controller)](#8-couche-contrôleur-controller)

9\. \[Couche Vue (View)](#9-couche-vue-view)

10\. \[Fonctionnalités par rôle](#10-fonctionnalités-par-rôle)

11\. \[Guide d'installation](#11-guide-dinstallation)

12\. \[Conclusion](#12-conclusion)



\---



\## 1. Introduction



Ce document présente la documentation technique complète du système de gestion d'un restaurant développé dans le cadre du mini-projet du module \*\*Programmation Orientée Objet (POO)\*\* en CPI 2 à l'ISIMG Gabès.



L'application permet la gestion complète des menus, des plats et des commandes d'un restaurant, en distinguant trois profils d'utilisateurs : le \*\*client\*\*, la \*\*serveuse\*\* et le \*\*cuisinier\*\*. Chaque acteur dispose d'un espace dédié avec des fonctionnalités spécifiques accessibles après authentification.



\### Objectifs du projet



\- Développer une application Java avec interface graphique Swing

\- Connecter l'application à une base de données MySQL

\- Appliquer l'architecture \*\*MVC\*\* (Modèle – Vue – Contrôleur)

\- Implémenter le patron de conception \*\*DAO\*\* pour l'accès aux données

\- Respecter les principes de la POO : héritage, polymorphisme, encapsulation



\---



\## 2. Technologies utilisées



| Technologie | Version | Rôle |

|---|---|---|

| Java | JDK 17+ | Langage principal |

| Java Swing | — | Interface graphique |

| MySQL | 8.0+ | Base de données relationnelle |

| MySQL Connector/J | 8.0+ | Driver JDBC |

| IntelliJ IDEA / Eclipse | — | Environnement de développement |

| draw.io | — | Conception UML |

| Git | — | Gestion de version |



\---



\## 3. Architecture du projet



Le projet respecte une architecture en \*\*4 couches\*\* séparées, conformément au patron \*\*MVC + DAO\*\* :



```

RestaurantApp/

├── src/

│   ├── util/

│   │   └── DBConnection.java          ← Singleton connexion MySQL

│   │

│   ├── model/                         ← Entités métier (POJOs)

│   │   ├── Utilisateur.java           ← Classe abstraite parente

│   │   ├── Client.java

│   │   ├── Serveuse.java

│   │   ├── Cuisinier.java

│   │   ├── Menu.java

│   │   ├── Plat.java

│   │   ├── Commande.java

│   │   ├── LigneCommande.java

│   │   └── Facture.java

│   │

│   ├── dao/                           ← Accès base de données (JDBC)

│   │   ├── IDao.java                  ← Interface générique CRUD

│   │   ├── UtilisateurDAO.java

│   │   ├── MenuDAO.java

│   │   ├── PlatDAO.java

│   │   ├── CommandeDAO.java

│   │   ├── LigneCommandeDAO.java

│   │   └── FactureDAO.java

│   │

│   ├── controller/                    ← Logique métier

│   │   ├── AuthController.java

│   │   ├── MenuController.java

│   │   ├── CommandeController.java

│   │   └── FactureController.java

│   │

│   └── view/                          ← Interfaces graphiques Swing

│       ├── LoginFrame.java            ← Écran de connexion

│       ├── DashboardClientFrame.java

│       ├── DashboardServeuseFrame.java

│       ├── DashboardCuisinierFrame.java

│       ├── GestionMenuFrame.java      ← CRUD plats (5 boutons)

│       ├── CommandeFrame.java

│       └── FactureFrame.java

│

├── lib/

│   └── mysql-connector-j-8.x.x.jar   ← Driver JDBC

│

└── sql/

&#x20;   └── restaurant\_db.sql              ← Script de création BDD

```



\### Flux de données



```

Vue (Swing) → Contrôleur → DAO → Base de données MySQL

&#x20;               ↑                        ↓

&#x20;             Modèle ←────────────────────

```



\---



\## 4. Conception UML



\### 4.1 Diagramme de cas d'utilisation



Le système identifie \*\*3 acteurs\*\* interagissant avec le système :



\#### Acteurs et leurs cas d'utilisation



| Acteur | Cas d'utilisation |

|---|---|

| \*\*Tous\*\* | Se connecter au système |

| \*\*Client\*\* | Parcourir le menu, Commander des plats |

| \*\*Serveuse\*\* | Parcourir le menu, Commander, Voir commandes en cours, Voir commandes reçues, Générer une facture |

| \*\*Cuisinier\*\* | Gérer les plats (CRUD), Commencer/annuler une commande, Notifier la serveuse |



\#### Relations UML



\- `Commander` \*\*<<include>>\*\* `Parcourir le menu` : on ne peut pas commander sans parcourir le menu

\- `Notifier la serveuse` \*\*<<include>>\*\* `Commencer/annuler commande` : la notification est déclenchée automatiquement lors du traitement



\### 4.2 Diagramme de classes



\#### Hiérarchie d'héritage



```

&#x20;          ┌─────────────────┐

&#x20;          │  «abstract»     │

&#x20;          │  Utilisateur    │

&#x20;          │─────────────────│

&#x20;          │ -id : int       │

&#x20;          │ -nom : String   │

&#x20;          │ -username : ... │

&#x20;          │ -role : String  │

&#x20;          │─────────────────│

&#x20;          │ +login()        │

&#x20;          └────────┬────────┘

&#x20;                   │  (héritage)

&#x20;        ┌──────────┼──────────┐

&#x20;        ▼          ▼          ▼

&#x20;   ┌─────────┐ ┌─────────┐ ┌───────────┐

&#x20;   │ Client  │ │Serveuse │ │ Cuisinier │

&#x20;   └─────────┘ └─────────┘ └───────────┘

```



\#### Relations principales



| Relation | Type | Multiplicité | Description |

|---|---|---|---|

| `Menu` → `Plat` | Composition | 1 à 1..\* | Un menu contient plusieurs plats |

| `Commande` → `LigneCommande` | Composition | 1 à 1..\* | Une commande comprend des lignes |

| `LigneCommande` → `Plat` | Association | 1 à 1 | Chaque ligne référence un plat |

| `Commande` → `Facture` | Association | 1 à 0..1 | Une commande peut générer une facture |

| `Client` → `Commande` | Association | 1 à 0..\* | Un client passe des commandes |

| `Serveuse` → `Commande` | Association | 1 à 0..\* | Une serveuse gère des commandes |



\---



\## 5. Base de données



\### 5.1 Schéma relationnel



```

UTILISATEUR (id PK, nom, prenom, username, password, role, actif)

&#x20;    │

&#x20;    ├──(id\_client)────────► COMMANDE (id PK, date\_commande, statut,

&#x20;    └──(id\_serveuse)──────►           num\_table, id\_client FK, id\_serveuse FK)

&#x20;                                             │

MENU (id PK, nom, description)               ├──────► LIGNE\_COMMANDE (id PK,

&#x20; │                                          │                quantite,

&#x20; └──(id\_menu)──► PLAT (id PK, nom,         │                prix\_unitaire,

&#x20;                       description,          │                id\_commande FK,

&#x20;                       prix, disponible,     │◄───(id\_plat)── id\_plat FK)

&#x20;                       id\_menu FK)           │

&#x20;                                             └──────► FACTURE (id PK,

&#x20;                                                              date\_facture,

NOTIFICATION (id PK, message, date\_envoi,                      montant\_total,

&#x20;             lue, id\_commande FK,                             remise,

&#x20;             id\_serveuse FK)                                  montant\_final,

&#x20;                                                              id\_commande FK UQ)

```



\### 5.2 Description des tables



\#### Table `utilisateur`



| Colonne | Type | Contrainte | Description |

|---|---|---|---|

| id | INT | PK, AUTO\_INCREMENT | Identifiant unique |

| nom | VARCHAR(50) | NOT NULL | Nom de famille |

| prenom | VARCHAR(50) | NOT NULL | Prénom |

| username | VARCHAR(30) | NOT NULL, UNIQUE | Identifiant de connexion |

| password | VARCHAR(255) | NOT NULL | Mot de passe hashé SHA-256 |

| role | ENUM | NOT NULL | CLIENT / SERVEUSE / CUISINIER |

| actif | BOOLEAN | DEFAULT TRUE | Compte actif ou désactivé |



\#### Table `plat`



| Colonne | Type | Contrainte | Description |

|---|---|---|---|

| id | INT | PK, AUTO\_INCREMENT | Identifiant unique |

| nom | VARCHAR(100) | NOT NULL | Nom du plat |

| description | TEXT | — | Description détaillée |

| prix | DECIMAL(8,2) | NOT NULL, ≥ 0 | Prix en dinars tunisiens |

| disponible | BOOLEAN | DEFAULT TRUE | Disponibilité en cuisine |

| id\_menu | INT | FK → menu(id) | Catégorie du plat |



\#### Table `commande`



| Colonne | Type | Contrainte | Description |

|---|---|---|---|

| id | INT | PK, AUTO\_INCREMENT | Identifiant unique |

| date\_commande | DATETIME | DEFAULT NOW() | Date et heure de création |

| statut | ENUM | DEFAULT 'EN\_ATTENTE' | EN\_ATTENTE / EN\_COURS / PRETE / SERVIE / ANNULEE |

| num\_table | INT | — | Numéro de table du client |

| id\_client | INT | FK → utilisateur(id) | Client ayant passé la commande |

| id\_serveuse | INT | FK → utilisateur(id) | Serveuse en charge |



\#### Table `ligne\_commande`



| Colonne | Type | Contrainte | Description |

|---|---|---|---|

| id | INT | PK, AUTO\_INCREMENT | Identifiant unique |

| quantite | INT | NOT NULL, > 0 | Nombre d'exemplaires du plat |

| prix\_unitaire | DECIMAL(8,2) | NOT NULL | Prix au moment de la commande (snapshot) |

| id\_commande | INT | FK → commande(id), CASCADE | Commande parente |

| id\_plat | INT | FK → plat(id) | Plat commandé |



\#### Table `facture`



| Colonne | Type | Contrainte | Description |

|---|---|---|---|

| id | INT | PK, AUTO\_INCREMENT | Identifiant unique |

| date\_facture | DATETIME | DEFAULT NOW() | Date d'émission |

| montant\_total | DECIMAL(10,2) | NOT NULL | Somme brute des lignes |

| remise | DECIMAL(5,2) | DEFAULT 0.00 | Pourcentage de remise |

| montant\_final | DECIMAL(10,2) | NOT NULL | Montant après remise |

| id\_commande | INT | FK → commande(id), UNIQUE | Commande facturée |



\---



\## 6. Couche Modèle (Model)



\### Classe `Utilisateur` (abstraite)



```java

package model;



public abstract class Utilisateur {

&#x20;   private int    id;

&#x20;   private String nom;

&#x20;   private String prenom;

&#x20;   private String username;

&#x20;   private String password;

&#x20;   private String role;



&#x20;   // Constructeur, getters, setters

&#x20;   public abstract String getNomComplet();

&#x20;   public String toString() { return "\[" + role + "] " + getNomComplet(); }

}

```



\*\*Sous-classes :\*\* `Client` (attribut `numTable`), `Serveuse` (attribut `numBadge`), `Cuisinier` (attribut `specialite`)



\### Classe `Commande`



Gère le cycle de vie d'une commande via l'attribut `statut` :



```

EN\_ATTENTE → EN\_COURS → PRETE → SERVIE

&#x20;               ↘ ANNULEE

```



\### Classe `Facture`



Calculée automatiquement à partir des lignes de commande :



```java

montantFinal = montantTotal - (montantTotal × remise / 100)

```



\---



\## 7. Couche DAO



\### Interface générique `IDao<T>`



```java

package dao;

import java.util.List;



public interface IDao<T> {

&#x20;   boolean  insert(T obj);       // CREATE

&#x20;   boolean  update(T obj);       // UPDATE

&#x20;   boolean  delete(int id);      // DELETE

&#x20;   T        findById(int id);    // READ (un)

&#x20;   List<T>  findAll();           // READ (tous)

}

```



Chaque entité dispose d'une implémentation :



| Classe DAO | Méthodes supplémentaires |

|---|---|

| `UtilisateurDAO` | `authenticate(username, password)` |

| `PlatDAO` | `findByMenu(idMenu)` |

| `CommandeDAO` | `findByStatut(statut)`, `changerStatut(id, statut)` |

| `LigneCommandeDAO` | `findByCommande(idCommande)` |

| `FactureDAO` | `findByCommande(idCommande)` |



\### Connexion base de données — Singleton



```java

package util;

import java.sql.\*;



public class DBConnection {

&#x20;   private static final String URL  = "jdbc:mysql://localhost:3306/restaurant\_db";

&#x20;   private static final String USER = "root";

&#x20;   private static final String PWD  = "root";

&#x20;   private static Connection instance = null;



&#x20;   public static Connection getInstance() {

&#x20;       try {

&#x20;           if (instance == null || instance.isClosed()) {

&#x20;               Class.forName("com.mysql.cj.jdbc.Driver");

&#x20;               instance = DriverManager.getConnection(URL, USER, PWD);

&#x20;           }

&#x20;       } catch (Exception e) { e.printStackTrace(); }

&#x20;       return instance;

&#x20;   }

}

```



\---



\## 8. Couche Contrôleur (Controller)



\### `AuthController`



Gère l'authentification et la session :



```java

public boolean login(String username, String password) {

&#x20;   utilisateurConnecte = utilisateurDAO.authenticate(username, password);

&#x20;   return utilisateurConnecte != null;

}

public String getRole() { return utilisateurConnecte.getRole(); }

```



\### `CommandeController`



Orchestre le cycle de vie complet d'une commande :



| Méthode | Statut résultant |

|---|---|

| `creerCommande()` | EN\_ATTENTE |

| `commencerTraitement()` | EN\_COURS |

| `marquerPrete()` | PRETE → notification serveuse |

| `marquerServie()` | SERVIE |

| `annulerCommande()` | ANNULEE |

| `genererFacture()` | Création dans table `facture` |



\---



\## 9. Couche Vue (View)



\### `LoginFrame` — Écran de connexion



Interface initiale commune aux 3 rôles :



\- Champ \*\*Utilisateur\*\* (JTextField)

\- Champ \*\*Mot de passe\*\* (JPasswordField, masqué)

\- Bouton \*\*Se connecter\*\* → authentification + redirection selon le rôle

\- Affichage du message d'erreur en cas d'échec



\### `GestionMenuFrame` — Gestion des plats (Cuisinier)



Conformément à l'énoncé, contient les \*\*5 boutons obligatoires\*\* :



| Bouton | Action |

|---|---|

| `<< Ajouter >>` | Ouvre un formulaire pour créer un nouveau plat |

| `<< Modifier >>` | Charge le plat sélectionné dans un formulaire d'édition |

| `<< Supprimer >>` | Supprime le plat après confirmation |

| `<< Enregistrer >>` | Sauvegarde les modifications en base de données |

| `<< Fermer >>` | Ferme la fenêtre |



\### Tableaux de bord



Chaque rôle dispose d'un tableau de bord dédié (`JFrame`) affichant uniquement les fonctionnalités autorisées :



\- \*\*DashboardClientFrame\*\* : Menu + Commander

\- \*\*DashboardServeuseFrame\*\* : Menu + Commander + Commandes + Facture

\- \*\*DashboardCuisinierFrame\*\* : Gestion plats + Traitement commandes + Notifications



\---



\## 10. Fonctionnalités par rôle



\### Client



```

1\. Se connecter avec username/password

2\. Parcourir le menu (liste des plats par catégorie)

3\. Sélectionner des plats et valider une commande

```



\### Serveuse



```

1\. Se connecter

2\. Parcourir le menu et commander pour un client

3\. Visualiser les commandes EN\_ATTENTE

4\. Visualiser les commandes EN\_COURS

5\. Générer la facture pour les commandes SERVIES

```



\### Cuisinier



```

1\. Se connecter

2\. Ajouter / Modifier / Supprimer des plats du menu

3\. Afficher les commandes EN\_ATTENTE → Commencer ou Annuler

4\. Afficher les commandes EN\_COURS → Marquer comme PRÊTE

5\. Envoyer une notification automatique à la serveuse

6\. Afficher les commandes SERVIES (historique)

```



\---



\## 11. Guide d'installation



\### Prérequis



\- JDK 17 ou supérieur

\- MySQL Server 8.0+

\- MySQL Workbench (optionnel)

\- IntelliJ IDEA ou Eclipse



\### Étapes d'installation



\*\*1. Créer la base de données\*\*



```sql

\-- Exécuter dans MySQL Workbench ou CLI

source /chemin/vers/restaurant\_db.sql;

```



\*\*2. Configurer la connexion\*\*



Modifier dans `src/util/DBConnection.java` :



```java

private static final String URL      = "jdbc:mysql://localhost:3306/restaurant\_db";

private static final String USER     = "root";       // votre utilisateur MySQL

private static final String PASSWORD = "votre\_mdp";  // votre mot de passe MySQL

```



\*\*3. Ajouter le driver JDBC\*\*



Télécharger `mysql-connector-j-8.x.x.jar` depuis le site officiel MySQL et l'ajouter au classpath du projet.



\*\*4. Compiler et lancer\*\*



```bash

\# Compilation

javac -cp ".:lib/mysql-connector-j-8.x.x.jar" src/\*\*/\*.java



\# Lancement

java -cp ".:lib/mysql-connector-j-8.x.x.jar:src" view.LoginFrame

```



\### Comptes de test (insérés par le script SQL)



| Utilisateur | Mot de passe | Rôle |

|---|---|---|

| `admin` | `admin123` | Cuisinier |

| `fatma` | `fatma123` | Serveuse |

| `sarra` | `sarra123` | Serveuse |

| `mohamed` | `client123` | Client |

| `amira` | `amira123` | Client |



\---



\## 12. Conclusion



Ce projet a permis de mettre en pratique les concepts fondamentaux de la \*\*Programmation Orientée Objet\*\* en Java à travers un cas d'usage complet et réaliste.



\### Points clés maîtrisés



\- \*\*Héritage et polymorphisme\*\* : hiérarchie `Utilisateur` → `Client` / `Serveuse` / `Cuisinier`

\- \*\*Interface générique\*\* : `IDao<T>` avec les 4 opérations CRUD réutilisables

\- \*\*Patron Singleton\*\* : `DBConnection` pour une connexion unique à la base

\- \*\*Architecture MVC\*\* : séparation stricte entre présentation, logique et données

\- \*\*Patron DAO\*\* : abstraction complète de l'accès aux données via JDBC

\- \*\*Java Swing\*\* : interfaces ergonomiques avec gestion des événements



\### Améliorations possibles



\- Génération de factures au format PDF avec iText

\- Système de réservation de tables en ligne

\- Tableau de bord statistiques (chiffre d'affaires, plats populaires)

\- Notification en temps réel via WebSocket

\- Migration vers une architecture REST avec Spring Boot



\---



\*Documentation générée pour le Mini-Projet POO — CPI 2 — ISIMG Gabès — 2025/2026\*



