CREATE DATABASE IF NOT EXISTS hotia
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hotia;

CREATE TABLE ref_divertissements (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE ref_comportements (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE villes (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nom       VARCHAR(100)  NOT NULL,
    latitude  DECIMAL(9,6)  NOT NULL,
    longitude DECIMAL(9,6)  NOT NULL
);

CREATE TABLE utilisateurs (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nom              VARCHAR(100) NOT NULL,
    prenom           VARCHAR(100) NOT NULL,
    email            VARCHAR(255) NOT NULL UNIQUE,
    date_naissance   DATE         NOT NULL,
    sexe             ENUM('Homme','Femme','Autre') NOT NULL,
    mot_de_passe     VARCHAR(64)  NOT NULL,
    statut           ENUM('libre','en_matching') NOT NULL DEFAULT 'libre',
    profil_complet   TINYINT(1)   NOT NULL DEFAULT 0,
    date_inscription DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE profils (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT          NOT NULL UNIQUE,
    telephone      VARCHAR(20)  NOT NULL,
    bio            TEXT,
    ville_id       INT,
    latitude       DECIMAL(9,6),
    longitude      DECIMAL(9,6),
    CONSTRAINT fk_profil_utilisateur FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CONSTRAINT fk_profil_ville FOREIGN KEY (ville_id)
        REFERENCES villes(id) ON DELETE SET NULL
);

CREATE TABLE photos (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT          NOT NULL,
    chemin         VARCHAR(500) NOT NULL,
    est_principale TINYINT(1)   NOT NULL DEFAULT 0,
    date_upload    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_photo_utilisateur FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE TABLE preferences (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id      INT NOT NULL,
    type                ENUM('recherche','description') NOT NULL,
    sexe_cible          ENUM('Homme','Femme','Autre','Peu importe'),
    situation_familiale ENUM('Célibataire','Divorcé','Veuf','Peu importe'),
    type_relation       ENUM('Relation sérieuse','Amitié','Aventure','Peu importe'),
    age_min             TINYINT UNSIGNED,
    age_max             TINYINT UNSIGNED,
    CONSTRAINT fk_pref_utilisateur FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CONSTRAINT uq_pref_type UNIQUE (utilisateur_id, type)
);

CREATE TABLE preferences_divertissements (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    preference_id       INT NOT NULL,
    divertissement_id   INT NOT NULL,
    CONSTRAINT fk_pd_preference FOREIGN KEY (preference_id)
        REFERENCES preferences(id) ON DELETE CASCADE,
    CONSTRAINT fk_pd_divertissement FOREIGN KEY (divertissement_id)
        REFERENCES ref_divertissements(id) ON DELETE CASCADE,
    CONSTRAINT uq_pd UNIQUE (preference_id, divertissement_id)
);

CREATE TABLE preferences_comportements (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    preference_id    INT NOT NULL,
    comportement_id  INT NOT NULL,
    CONSTRAINT fk_pc_preference FOREIGN KEY (preference_id)
        REFERENCES preferences(id) ON DELETE CASCADE,
    CONSTRAINT fk_pc_comportement FOREIGN KEY (comportement_id)
        REFERENCES ref_comportements(id) ON DELETE CASCADE,
    CONSTRAINT uq_pc UNIQUE (preference_id, comportement_id)
);

CREATE TABLE demandes_match (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    demandeur_id  INT NOT NULL,
    cible_id      INT NOT NULL,
    statut        ENUM('en_attente','acceptée','refusée') NOT NULL DEFAULT 'en_attente',
    date_demande  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dm_demandeur FOREIGN KEY (demandeur_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CONSTRAINT fk_dm_cible FOREIGN KEY (cible_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CONSTRAINT uq_demande UNIQUE (demandeur_id, cible_id)
);

CREATE TABLE matchs (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur1_id INT        NOT NULL,
    utilisateur2_id INT        NOT NULL,
    actif           TINYINT(1) NOT NULL DEFAULT 1,
    date_match      DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_match_u1 FOREIGN KEY (utilisateur1_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CONSTRAINT fk_match_u2 FOREIGN KEY (utilisateur2_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE TABLE separations (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    match_id     INT NOT NULL,
    demandeur_id INT NOT NULL,
    statut       ENUM('en_attente','confirmée','refusée') NOT NULL DEFAULT 'en_attente',
    date_demande DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sep_match FOREIGN KEY (match_id)
        REFERENCES matchs(id) ON DELETE CASCADE,
    CONSTRAINT fk_sep_demandeur FOREIGN KEY (demandeur_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE TABLE messages (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    match_id    INT  NOT NULL,
    emetteur_id INT  NOT NULL,
    contenu     TEXT NOT NULL,
    date_envoi  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_msg_match FOREIGN KEY (match_id)
        REFERENCES matchs(id) ON DELETE CASCADE,
    CONSTRAINT fk_msg_emetteur FOREIGN KEY (emetteur_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE TABLE swipes_passes (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    cible_id       INT NOT NULL,
    date_swipe     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sp_utilisateur FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CONSTRAINT fk_sp_cible FOREIGN KEY (cible_id)
        REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CONSTRAINT uq_swipe_passe UNIQUE (utilisateur_id, cible_id)
);

INSERT INTO ref_divertissements (libelle) VALUES
    ('Musique'), ('Sport'), ('Cinéma'), ('Lecture'), ('Voyage'),
    ('Gaming'), ('Cuisine'), ('Art & Peinture'), ('Danse'), ('Photographie'),
    ('Randonnée'), ('Yoga & Méditation'), ('Natation'), ('Théâtre'), ('Mode & Stylisme'),
    ('Jardinage'), ('Bricolage'), ('Animaux'), ('Manga & Anime'), ('Podcast & Radio'),
    ('Jeux de société'), ('Moto & Automobile'), ('Pêche'), ('Football'), ('Basketball'),
    ('Arts martiaux'), ('Volley-ball'), ('Cyclisme'), ('Surf & Sports nautiques'), ('Astronomie');

INSERT INTO ref_comportements (libelle) VALUES
    ('Romantique'), ('Aventurier'), ('Calme'), ('Ambitieux'), ('Drôle & Humoriste'),
    ('Protecteur'), ('Intellectuel'), ('Spontané'), ('Attentionné'), ('Indépendant'),
    ('Sociable'), ('Introverti'), ('Créatif'), ('Passionné'), ('Loyal & Fidèle'),
    ('Optimiste'), ('Réaliste'), ('Curieux'), ('Leader'), ('Empathique'),
    ('Sérieux'), ('Joueur'), ('Travailleur'), ('Mystérieux'), ('Direct & Honnête');

INSERT INTO villes (nom, latitude, longitude) VALUES
    ('Antananarivo', -18.913700, 47.536100),
    ('Toamasina',    -18.149200, 49.402300),
    ('Antsirabe',    -19.865900, 47.033700),
    ('Fianarantsoa', -21.453800, 47.085000),
    ('Mahajanga',    -15.716700, 46.316700),
    ('Toliara',      -23.356800, 43.685800),
    ('Antsiranana',  -12.348700, 49.295600),
    ('Ambovombe',    -25.174200, 46.088000),
    ('Morondava',    -20.283300, 44.283300),
    ('Nosy Be',      -13.333300, 48.250000);
