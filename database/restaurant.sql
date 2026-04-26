-- Créer la base de données
CREATE DATABASE IF NOT EXISTS restaurant_db;
USE restaurant_db;

-- Table personnes (base pour clients et employés)
CREATE TABLE personnes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    type ENUM('client', 'employe') NOT NULL
);

-- Table clients
CREATE TABLE clients (
    id INT PRIMARY KEY,
    adresse VARCHAR(200),
    FOREIGN KEY (id) REFERENCES personnes(id) ON DELETE CASCADE
);

-- Table employes
CREATE TABLE employes (
    id INT PRIMARY KEY,
    role ENUM('Administrateur', 'Serveur', 'Caissier') NOT NULL,
    salaire DOUBLE NOT NULL,
    FOREIGN KEY (id) REFERENCES personnes(id) ON DELETE CASCADE
);

-- Table plats
CREATE TABLE plats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    categorie VARCHAR(50),
    prix DOUBLE NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);

-- Table tables_restaurant
CREATE TABLE tables_restaurant (
    numero INT PRIMARY KEY,
    capacite INT NOT NULL,
    etat ENUM('libre', 'occupee') DEFAULT 'libre'
);

-- Table commandes
CREATE TABLE commandes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_commande DATETIME DEFAULT NOW(),
    etat ENUM('en cours', 'terminee', 'annulee') DEFAULT 'en cours',
    client_id INT NOT NULL,
    serveur_id INT NOT NULL,
    table_numero INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (serveur_id) REFERENCES employes(id),
    FOREIGN KEY (table_numero) REFERENCES tables_restaurant(numero)
);

-- Table de liaison commande <-> plat (plusieurs plats par commande)
CREATE TABLE commande_plats (
    commande_id INT NOT NULL,
    plat_id INT NOT NULL,
    quantite INT DEFAULT 1,
    PRIMARY KEY (commande_id, plat_id),
    FOREIGN KEY (commande_id) REFERENCES commandes(id) ON DELETE CASCADE,
    FOREIGN KEY (plat_id) REFERENCES plats(id)
);

-- Table factures
CREATE TABLE factures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_facture DATETIME DEFAULT NOW(),
    montant_total DOUBLE NOT NULL,
    commande_id INT UNIQUE NOT NULL,
    FOREIGN KEY (commande_id) REFERENCES commandes(id)
);

-- Table paiements
CREATE TABLE paiements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    montant DOUBLE NOT NULL,
    mode_paiement ENUM('especes', 'carte', 'virement') NOT NULL,
    statut ENUM('en attente', 'paye') DEFAULT 'en attente',
    facture_id INT UNIQUE NOT NULL,
    FOREIGN KEY (facture_id) REFERENCES factures(id)
);

-- Données de test
INSERT INTO personnes VALUES (1, 'Youssef Alami', '0612345678', 'client');
INSERT INTO clients VALUES (1, 'Fès, Maroc');

INSERT INTO personnes VALUES (2, 'Sara Benali', '0611111111', 'employe');
INSERT INTO employes VALUES (2, 'Serveur', 4500);

INSERT INTO personnes VALUES (3, 'Ahmed Idrissi', '0622222222', 'employe');
INSERT INTO employes VALUES (3, 'Administrateur', 7000);

INSERT INTO plats VALUES (1, 'Tajine Poulet', 'Plat principal', 65.0, TRUE);
INSERT INTO plats VALUES (2, 'Salade Marocaine', 'Entrée', 30.0, TRUE);
INSERT INTO plats VALUES (3, 'Thé à la menthe', 'Boisson', 15.0, TRUE);
INSERT INTO plats VALUES (4, 'Couscous Royal', 'Plat principal', 85.0, TRUE);

INSERT INTO tables_restaurant VALUES (1, 2, 'libre');
INSERT INTO tables_restaurant VALUES (2, 4, 'libre');
INSERT INTO tables_restaurant VALUES (3, 6, 'libre');