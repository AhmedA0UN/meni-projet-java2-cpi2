DROP DATABASE IF EXISTS restaurant_db;
CREATE DATABASE restaurant_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restaurant_db;


CREATE TABLE utilisateur (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nom        VARCHAR(50)  NOT NULL,
    prenom     VARCHAR(50)  NOT NULL,
    username   VARCHAR(30)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,  -- stocké hashé (SHA-256 ou BCrypt)
    role       ENUM('CLIENT', 'SERVEUSE', 'CUISINIER') NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    actif      BOOLEAN DEFAULT TRUE
);


CREATE TABLE menu (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    description TEXT
);

-- ============================================================
-- TABLE : plat
-- Les plats associés à un menu
-- ============================================================
CREATE TABLE plat (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    description TEXT,
    prix        DECIMAL(8,2) NOT NULL CHECK (prix >= 0),
    disponible  BOOLEAN DEFAULT TRUE,
    image_path  VARCHAR(255),
    id_menu     INT NOT NULL,
    CONSTRAINT fk_plat_menu FOREIGN KEY (id_menu) REFERENCES menu(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE commande (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    date_commande  DATETIME DEFAULT CURRENT_TIMESTAMP,
    statut         ENUM('EN_ATTENTE','EN_COURS','PRETE','SERVIE','ANNULEE')
                   NOT NULL DEFAULT 'EN_ATTENTE',
    num_table      INT,
    id_client      INT,
    id_serveuse    INT,
    CONSTRAINT fk_commande_client   FOREIGN KEY (id_client)   REFERENCES utilisateur(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_commande_serveuse FOREIGN KEY (id_serveuse) REFERENCES utilisateur(id)
        ON DELETE SET NULL ON UPDATE CASCADE
);


CREATE TABLE ligne_commande (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    quantite     INT NOT NULL DEFAULT 1 CHECK (quantite > 0),
    prix_unitaire DECIMAL(8,2) NOT NULL,  -- snapshot du prix au moment de la commande
    id_commande  INT NOT NULL,
    id_plat      INT NOT NULL,
    CONSTRAINT fk_lc_commande FOREIGN KEY (id_commande) REFERENCES commande(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_lc_plat     FOREIGN KEY (id_plat)     REFERENCES plat(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE facture (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    date_facture   DATETIME DEFAULT CURRENT_TIMESTAMP,
    montant_total  DECIMAL(10,2) NOT NULL,
    remise         DECIMAL(5,2)  DEFAULT 0.00,  -- pourcentage de remise
    montant_final  DECIMAL(10,2) NOT NULL,
    id_commande    INT NOT NULL UNIQUE,
    CONSTRAINT fk_facture_commande FOREIGN KEY (id_commande) REFERENCES commande(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE notification (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    message     VARCHAR(255) NOT NULL,
    date_envoi  DATETIME DEFAULT CURRENT_TIMESTAMP,
    lue         BOOLEAN DEFAULT FALSE,
    id_commande INT,
    id_serveuse INT,
    CONSTRAINT fk_notif_commande  FOREIGN KEY (id_commande) REFERENCES commande(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_notif_serveuse  FOREIGN KEY (id_serveuse) REFERENCES utilisateur(id)
        ON DELETE CASCADE
);



INSERT INTO utilisateur (nom, prenom, username, password, role) VALUES
('Admin',    'Système',  'admin',    SHA2('admin123', 256),    'CUISINIER'),
('Ben Ali',  'Fatma',    'fatma',    SHA2('fatma123', 256),    'SERVEUSE'),
('Trabelsi', 'Mohamed',  'mohamed',  SHA2('client123', 256),   'CLIENT'),
('Hamdi',    'Sarra',    'sarra',    SHA2('sarra123', 256),    'SERVEUSE'),
('Jlassi',   'Karim',    'karim',    SHA2('karim123', 256),    'CUISINIER'),
('Nefzi',    'Amira',    'amira',    SHA2('amira123', 256),    'CLIENT');


INSERT INTO menu (nom, description) VALUES
('Entrées',          'Salades, soupes et amuse-bouches'),
('Plats principaux', 'Viandes, poissons et spécialités'),
('Desserts',         'Gâteaux, crèmes et fruits'),
('Boissons',         'Jus, sodas et eaux');


INSERT INTO plat (nom, description, prix, disponible, id_menu) VALUES

('Salade méchouia',   'Salade grillée aux poivrons et tomates',       8.500,  TRUE,  1),
('Brik à l''oeuf',    'Feuilletée croustillante garnie d''oeuf',      6.000,  TRUE,  1),
('Chorba',            'Soupe traditionnelle au mouton',                7.500,  TRUE,  1),

('Couscous agneau',   'Couscous traditionnel avec légumes',           22.000, TRUE,  2),
('Grillades mixtes',  'Assortiment de grillades de viande',           28.500, TRUE,  2),
('Poisson grillé',    'Poisson du jour grillé avec légumes',          25.000, TRUE,  2),
('Tajine poulet',     'Poulet aux olives et citron confit',           19.000, TRUE,  2),

('Baklawa',           'Pâtisserie orientale au miel et pistaches',    5.500,  TRUE,  3),
('Crème caramel',     'Crème maison au caramel',                       4.500,  TRUE,  3),
('Salade de fruits',  'Fruits frais de saison',                        4.000,  TRUE,  3),

('Jus d''orange',    'Jus d''orange pressé frais',                   4.000,  TRUE,  4),
('Eau minérale',     'Eau minérale 50cl',                             2.000,  TRUE,  4),
('Limonade',         'Limonade maison à la menthe',                   3.500,  TRUE,  4);


INSERT INTO commande (date_commande, statut, num_table, id_client, id_serveuse)
VALUES (NOW(), 'EN_ATTENTE', 3, 3, 2);

INSERT INTO ligne_commande (quantite, prix_unitaire, id_commande, id_plat)
VALUES (1, 8.500, 1, 1),
       (2, 6.000, 1, 2),
       (1, 22.000, 1, 4);




CREATE VIEW vue_commandes AS
SELECT
    c.id          AS id_commande,
    c.date_commande,
    c.statut,
    c.num_table,
    CONCAT(cl.prenom, ' ', cl.nom) AS client,
    CONCAT(sv.prenom, ' ', sv.nom) AS serveuse
FROM commande c
LEFT JOIN utilisateur cl ON cl.id = c.id_client
LEFT JOIN utilisateur sv ON sv.id = c.id_serveuse;


CREATE VIEW vue_total_commande AS
SELECT
    lc.id_commande,
    SUM(lc.quantite * lc.prix_unitaire) AS montant_total
FROM ligne_commande lc
GROUP BY lc.id_commande;


CREATE VIEW vue_detail_commande AS
SELECT
    lc.id_commande,
    p.nom          AS plat,
    lc.quantite,
    lc.prix_unitaire,
    (lc.quantite * lc.prix_unitaire) AS sous_total
FROM ligne_commande lc
JOIN plat p ON p.id = lc.id_plat;



DELIMITER //


CREATE PROCEDURE sp_changer_statut(IN p_id_commande INT, IN p_statut VARCHAR(20))
BEGIN
    UPDATE commande SET statut = p_statut WHERE id = p_id_commande;
END//


CREATE PROCEDURE sp_generer_facture(IN p_id_commande INT, IN p_remise DECIMAL(5,2))
BEGIN
    DECLARE v_total DECIMAL(10,2);
    DECLARE v_final DECIMAL(10,2);
    SELECT SUM(quantite * prix_unitaire) INTO v_total
    FROM ligne_commande WHERE id_commande = p_id_commande;
    SET v_final = v_total - (v_total * p_remise / 100);
    INSERT INTO facture (montant_total, remise, montant_final, id_commande)
    VALUES (v_total, p_remise, v_final, p_id_commande);
END//

DELIMITER ;


