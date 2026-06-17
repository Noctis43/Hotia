# HoTia 

> **Application web de rencontres** — Java EE · Tomcat 9 · MySQL · JSP/Servlet  

---

## Présentation

**HoTia** (du malgache : *être aimé*) est une application web de mise en relation entre utilisateurs, à la manière d'un Tinder localisé. Elle permet à des adultes de créer un profil détaillé, de définir leurs préférences de relation, puis d'envoyer et recevoir des demandes de match. Une fois deux utilisateurs matchés, ils peuvent échanger des messages en temps réel via une messagerie intégrée.

Le projet est entièrement construit en **Java EE** sur un serveur **Apache Tomcat 9**, avec une base de données **MySQL** gérée via JDBC pur — sans framework ORM — et des vues en **JSP**.

---

## Fonctionnalités

### Gestion des utilisateurs
- Inscription et connexion sécurisée (mot de passe haché SHA-256)
- Profil complet : nom, prénom, date de naissance, sexe, bio, téléphone, ville
- Upload de photos (principale + galerie)
- Statuts : `libre` ou `en_matching`

### Préférences et matching
- Définition de deux types de préférences : *ce que je recherche* et *comment je me décris*
- Filtres par sexe cible, situation familiale, type de relation, âge min/max
- Sélection de **30 divertissements** (musique, sport, gaming, voyage…) et de **25 traits de comportement** (romantique, aventurier, calme…)
- Mécanisme de **swipe** : passer un profil le mémorise pour ne plus le voir
- Envoi de demandes de match, acceptation / refus

### Messagerie
- Messagerie one-to-one entre utilisateurs matchés
- Historique complet des échanges par match

### Rupture de match
- Demande de séparation avec workflow de confirmation (en attente / confirmée / refusée)

### Référentiels géographiques
- Pré-chargement des **10 principales villes de Madagascar** avec coordonnées GPS
- Latitude / longitude sur le profil pour des évolutions géolocalisées

---

## Stack technique

| Couche | Technologie |
|---|---|
| Langage | Java 21 |
| Serveur d'application | Apache Tomcat 9.0 |
| Vues | JSP + JSTL |
| Persistance | JDBC (MySQL Connector) |
| Base de données | MySQL 8 |
| Frontend | HTML / CSS / JavaScript |
| IDE | Eclipse IDE for Enterprise Java |
| Build | Eclipse WTP (Web Tools Platform) |

---

## Structure du projet

```
HoTia/
├── src/
│   └── main/
│       └── java/           ← Servlets, DAO, modèles Java
├── .settings/              ← Configuration Eclipse WTP
├── build/
│   └── classes/            ← Classes compilées (généré)
├── .classpath              ← Classpath Eclipse (Java 21 + Tomcat 9)
├── .project                ← Descripteur projet Eclipse
├── hotia.sql               ← Script SQL complet (schéma + données initiales)
├── LICENSE                 ← Licence MIT
└── README.md
```

---

## Schéma de la base de données

Le fichier `hotia.sql` crée la base `hotia` (charset `utf8mb4`) et initialise les tables suivantes :

```
utilisateurs          — compte et statut de chaque membre
profils               — informations complémentaires (bio, ville, coordonnées)
photos                — galerie et photo principale
preferences           — critères de recherche et d'auto-description
preferences_divertissements  — loisirs associés à une préférence
preferences_comportements    — traits associés à une préférence
ref_divertissements   — référentiel de 30 loisirs
ref_comportements     — référentiel de 25 comportements
villes                — 10 villes de Madagascar avec lat/lng
demandes_match        — demandes envoyées (en_attente / acceptée / refusée)
matchs                — paires actives
separations           — demandes de rupture de match
messages              — messagerie par match
swipes_passes         — historique des profils ignorés
```

---

## Prérequis

- **Java 21** (JDK)
- **Apache Tomcat 9.0**
- **MySQL 8.x**
- **Eclipse IDE for Enterprise Java and Web Developers** (ou équivalent)

---

## Installation

### 1. Cloner le dépôt

```bash
git clone https://github.com/Noctis43/Hotia.git
cd Hotia
```

### 2. Créer la base de données

Connectez-vous à votre serveur MySQL et exécutez le script fourni :

```bash
mysql -u root -p < hotia.sql
```

Cela crée la base `hotia`, toutes les tables et insère les données de référence (divertissements, comportements, villes).

### 3. Configurer la connexion JDBC

Dans le fichier de configuration de la source de données (ou dans la classe utilitaire `DBConnection.java`), renseignez vos paramètres :

```java
private static final String URL      = "jdbc:mysql://localhost:3306/hotia?useSSL=false&serverTimezone=UTC";
private static final String USER     = "votre_utilisateur";
private static final String PASSWORD = "votre_mot_de_passe";
```

### 4. Déployer sur Tomcat

#### Via Eclipse
1. Ouvrir le projet dans **Eclipse IDE for Enterprise Java**
2. Vérifier que le **JRE System Library** pointe vers Java 21
3. Vérifier que **Apache Tomcat v9.0** est bien configuré dans les *Server Runtimes*
4. Clic droit sur le projet → **Run As** → **Run on Server**

#### Via déploiement manuel
1. Exporter le projet en `.war` : **File → Export → WAR file**
2. Copier le `.war` dans le dossier `webapps/` de Tomcat
3. Démarrer Tomcat : `./bin/startup.sh` (Linux/Mac) ou `bin\startup.bat` (Windows)

### 5. Accéder à l'application

```
http://localhost:8080/Hotia/
```

---

## Données pré-chargées

Après exécution du script SQL, les référentiels suivants sont disponibles immédiatement :

**Divertissements (30)** — Musique, Sport, Cinéma, Lecture, Voyage, Gaming, Cuisine, Art & Peinture, Danse, Photographie, Randonnée, Yoga & Méditation, Natation, Théâtre, Mode & Stylisme, Jardinage, Bricolage, Animaux, Manga & Anime, Podcast & Radio, Jeux de société, Moto & Automobile, Pêche, Football, Basketball, Arts martiaux, Volley-ball, Cyclisme, Surf & Sports nautiques, Astronomie

**Comportements (25)** — Romantique, Aventurier, Calme, Ambitieux, Drôle & Humoriste, Protecteur, Intellectuel, Spontané, Attentionné, Indépendant, Sociable, Introverti, Créatif, Passionné, Loyal & Fidèle, Optimiste, Réaliste, Curieux, Leader, Empathique, Sérieux, Joueur, Travailleur, Mystérieux, Direct & Honnête

**Villes de Madagascar (10)** — Antananarivo, Toamasina, Antsirabe, Fianarantsoa, Mahajanga, Toliara, Antsiranana, Ambovombe, Morondava, Nosy Be

---

## Pistes d'évolution

- Filtrage géographique par rayon (les coordonnées GPS sont déjà en base)
- Notifications en temps réel (WebSocket ou Server-Sent Events)
- Système de signalement d'utilisateurs
- Messagerie avec indicateur de lecture
- Migration vers Spring Boot + JPA pour un déploiement cloud

---

## Licence

Ce projet est distribué sous licence **MIT**. Voir le fichier [LICENSE](./LICENSE) pour les détails.

---

## Auteur

**Noctis43**  
Dépôt : [https://github.com/Noctis43/Hotia](https://github.com/Noctis43/Hotia)
