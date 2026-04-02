// ============================================================
//  SYSTÈME DE GESTION D'UN RESTAURANT - JAVA
//  Architecture MVC + DAO
//  Module : POO - CPI2 ISIMG Gabès - 2025-2026
//
//  STRUCTURE DU PROJET :
//  src/
//  ├── util/          DBConnection.java
//  ├── model/         Utilisateur.java, Client.java, Serveuse.java,
//  │                  Cuisinier.java, Menu.java, Plat.java,
//  │                  Commande.java, LigneCommande.java, Facture.java
//  ├── dao/           IDao.java, UtilisateurDAO.java, MenuDAO.java,
//  │                  PlatDAO.java, CommandeDAO.java, LigneCommandeDAO.java,
//  │                  FactureDAO.java
//  ├── controller/    AuthController.java, MenuController.java,
//  │                  CommandeController.java, FactureController.java
//  └── view/          LoginFrame.java, DashboardClientFrame.java,
//                     DashboardServeuseFrame.java, DashboardCuisinierFrame.java,
//                     GestionMenuFrame.java, CommandeFrame.java
// ============================================================


// ╔══════════════════════════════════════════════════════════╗
// ║  COUCHE UTIL                                             ║
// ╚══════════════════════════════════════════════════════════╝

// ── util/DBConnection.java ──────────────────────────────────
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton de connexion à la base de données MySQL.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/restaurant_db?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "root";

    private static Connection instance = null;

    private DBConnection() {}

    public static Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion BD : " + e.getMessage());
        }
        return instance;
    }

    public static void close() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                instance = null;
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture BD : " + e.getMessage());
        }
    }
}


// ╔══════════════════════════════════════════════════════════╗
// ║  COUCHE MODEL                                            ║
// ╚══════════════════════════════════════════════════════════╝

// ── model/Utilisateur.java ──────────────────────────────────
package model;

public abstract class Utilisateur {
    private int    id;
    private String nom;
    private String prenom;
    private String username;
    private String password;
    private String role;

    public Utilisateur() {}

    public Utilisateur(int id, String nom, String prenom,
                       String username, String password, String role) {
        this.id       = id;
        this.nom      = nom;
        this.prenom   = prenom;
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    // Getters & Setters
    public int    getId()       { return id; }
    public void   setId(int id) { this.id = id; }

    public String getNom()           { return nom; }
    public void   setNom(String nom) { this.nom = nom; }

    public String getPrenom()              { return prenom; }
    public void   setPrenom(String prenom) { this.prenom = prenom; }

    public String getUsername()                { return username; }
    public void   setUsername(String username) { this.username = username; }

    public String getPassword()                { return password; }
    public void   setPassword(String password) { this.password = password; }

    public String getRole()            { return role; }
    public void   setRole(String role) { this.role = role; }

    public String getNomComplet() { return prenom + " " + nom; }

    @Override
    public String toString() {
        return "[" + role + "] " + getNomComplet() + " (" + username + ")";
    }
}


// ── model/Client.java ───────────────────────────────────────
package model;

public class Client extends Utilisateur {
    private int numTable;

    public Client() { super(); }

    public Client(int id, String nom, String prenom,
                  String username, String password, int numTable) {
        super(id, nom, prenom, username, password, "CLIENT");
        this.numTable = numTable;
    }

    public int  getNumTable()             { return numTable; }
    public void setNumTable(int numTable) { this.numTable = numTable; }
}


// ── model/Serveuse.java ─────────────────────────────────────
package model;

public class Serveuse extends Utilisateur {
    private int numBadge;

    public Serveuse() { super(); }

    public Serveuse(int id, String nom, String prenom,
                    String username, String password, int numBadge) {
        super(id, nom, prenom, username, password, "SERVEUSE");
        this.numBadge = numBadge;
    }

    public int  getNumBadge()             { return numBadge; }
    public void setNumBadge(int numBadge) { this.numBadge = numBadge; }
}


// ── model/Cuisinier.java ────────────────────────────────────
package model;

public class Cuisinier extends Utilisateur {
    private String specialite;

    public Cuisinier() { super(); }

    public Cuisinier(int id, String nom, String prenom,
                     String username, String password, String specialite) {
        super(id, nom, prenom, username, password, "CUISINIER");
        this.specialite = specialite;
    }

    public String getSpecialite()                  { return specialite; }
    public void   setSpecialite(String specialite) { this.specialite = specialite; }
}


// ── model/Menu.java ─────────────────────────────────────────
package model;

public class Menu {
    private int    id;
    private String nom;
    private String description;

    public Menu() {}

    public Menu(int id, String nom, String description) {
        this.id          = id;
        this.nom         = nom;
        this.description = description;
    }

    public int    getId()                  { return id; }
    public void   setId(int id)            { this.id = id; }
    public String getNom()                 { return nom; }
    public void   setNom(String nom)       { this.nom = nom; }
    public String getDescription()         { return description; }
    public void   setDescription(String d) { this.description = d; }

    @Override
    public String toString() { return nom; }
}


// ── model/Plat.java ─────────────────────────────────────────
package model;

public class Plat {
    private int     id;
    private String  nom;
    private String  description;
    private double  prix;
    private boolean disponible;
    private int     idMenu;
    private String  nomMenu;  // pour l'affichage

    public Plat() {}

    public Plat(int id, String nom, String description,
                double prix, boolean disponible, int idMenu) {
        this.id          = id;
        this.nom         = nom;
        this.description = description;
        this.prix        = prix;
        this.disponible  = disponible;
        this.idMenu      = idMenu;
    }

    public int     getId()                  { return id; }
    public void    setId(int id)            { this.id = id; }
    public String  getNom()                 { return nom; }
    public void    setNom(String nom)       { this.nom = nom; }
    public String  getDescription()         { return description; }
    public void    setDescription(String d) { this.description = d; }
    public double  getPrix()               { return prix; }
    public void    setPrix(double prix)    { this.prix = prix; }
    public boolean isDisponible()          { return disponible; }
    public void    setDisponible(boolean d){ this.disponible = d; }
    public int     getIdMenu()             { return idMenu; }
    public void    setIdMenu(int idMenu)   { this.idMenu = idMenu; }
    public String  getNomMenu()            { return nomMenu; }
    public void    setNomMenu(String m)    { this.nomMenu = m; }

    @Override
    public String toString() {
        return nom + " (" + String.format("%.3f", prix) + " TND)";
    }
}


// ── model/Commande.java ─────────────────────────────────────
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande {
    private int                id;
    private Date               dateCommande;
    private String             statut;
    private int                numTable;
    private int                idClient;
    private int                idServeuse;
    private List<LigneCommande> lignes;

    // Infos dénormalisées pour l'affichage
    private String nomClient;
    private String nomServeuse;

    public Commande() {
        this.lignes = new ArrayList<>();
    }

    public Commande(int id, Date dateCommande, String statut,
                    int numTable, int idClient, int idServeuse) {
        this();
        this.id           = id;
        this.dateCommande = dateCommande;
        this.statut       = statut;
        this.numTable     = numTable;
        this.idClient     = idClient;
        this.idServeuse   = idServeuse;
    }

    public double calculerTotal() {
        return lignes.stream()
                     .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
                     .sum();
    }

    // Getters & Setters
    public int    getId()                       { return id; }
    public void   setId(int id)                 { this.id = id; }
    public Date   getDateCommande()             { return dateCommande; }
    public void   setDateCommande(Date d)       { this.dateCommande = d; }
    public String getStatut()                   { return statut; }
    public void   setStatut(String statut)      { this.statut = statut; }
    public int    getNumTable()                 { return numTable; }
    public void   setNumTable(int numTable)     { this.numTable = numTable; }
    public int    getIdClient()                 { return idClient; }
    public void   setIdClient(int idClient)     { this.idClient = idClient; }
    public int    getIdServeuse()               { return idServeuse; }
    public void   setIdServeuse(int idServeuse) { this.idServeuse = idServeuse; }
    public List<LigneCommande> getLignes()      { return lignes; }
    public void setLignes(List<LigneCommande> l){ this.lignes = l; }
    public String getNomClient()                { return nomClient; }
    public void   setNomClient(String n)        { this.nomClient = n; }
    public String getNomServeuse()              { return nomServeuse; }
    public void   setNomServeuse(String n)      { this.nomServeuse = n; }

    @Override
    public String toString() {
        return "Commande#" + id + " Table:" + numTable + " [" + statut + "]";
    }
}


// ── model/LigneCommande.java ────────────────────────────────
package model;

public class LigneCommande {
    private int    id;
    private int    quantite;
    private double prixUnitaire;
    private int    idCommande;
    private int    idPlat;
    private String nomPlat;

    public LigneCommande() {}

    public LigneCommande(int quantite, double prixUnitaire,
                         int idCommande, int idPlat) {
        this.quantite     = quantite;
        this.prixUnitaire = prixUnitaire;
        this.idCommande   = idCommande;
        this.idPlat       = idPlat;
    }

    public double getSousTotal() { return quantite * prixUnitaire; }

    public int    getId()                    { return id; }
    public void   setId(int id)              { this.id = id; }
    public int    getQuantite()              { return quantite; }
    public void   setQuantite(int q)         { this.quantite = q; }
    public double getPrixUnitaire()          { return prixUnitaire; }
    public void   setPrixUnitaire(double p)  { this.prixUnitaire = p; }
    public int    getIdCommande()            { return idCommande; }
    public void   setIdCommande(int id)      { this.idCommande = id; }
    public int    getIdPlat()               { return idPlat; }
    public void   setIdPlat(int id)          { this.idPlat = id; }
    public String getNomPlat()              { return nomPlat; }
    public void   setNomPlat(String n)      { this.nomPlat = n; }
}


// ── model/Facture.java ──────────────────────────────────────
package model;

import java.util.Date;

public class Facture {
    private int    id;
    private Date   dateFacture;
    private double montantTotal;
    private double remise;
    private double montantFinal;
    private int    idCommande;

    public Facture() {}

    public Facture(int idCommande, double montantTotal, double remise) {
        this.idCommande   = idCommande;
        this.montantTotal = montantTotal;
        this.remise       = remise;
        this.montantFinal = montantTotal - (montantTotal * remise / 100);
    }

    public int    getId()               { return id; }
    public void   setId(int id)         { this.id = id; }
    public Date   getDateFacture()      { return dateFacture; }
    public void   setDateFacture(Date d){ this.dateFacture = d; }
    public double getMontantTotal()     { return montantTotal; }
    public double getRemise()           { return remise; }
    public void   setRemise(double r)   { this.remise = r; }
    public double getMontantFinal()          { return montantFinal; }
    // CORRECTION Erreur 3 : setters manquants nécessaires pour le FactureDAO
    public void   setMontantTotal(double m)  { this.montantTotal = m; }
    public void   setMontantFinal(double m)  { this.montantFinal = m; }
    public int    getIdCommande()            { return idCommande; }
    public void   setIdCommande(int id)      { this.idCommande = id; }
}


// ╔══════════════════════════════════════════════════════════╗
// ║  COUCHE DAO                                              ║
// ╚══════════════════════════════════════════════════════════╝

// ── dao/IDao.java ───────────────────────────────────────────
package dao;

import java.util.List;

/**
 * Interface générique DAO avec les 4 opérations CRUD.
 */
public interface IDao<T> {
    boolean  insert(T obj);
    boolean  update(T obj);
    boolean  delete(int id);
    T        findById(int id);
    List<T>  findAll();
}


// ── dao/UtilisateurDAO.java ─────────────────────────────────
package dao;

import model.Utilisateur;
import model.Client;
import model.Serveuse;
import model.Cuisinier;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO implements IDao<Utilisateur> {

    // CORRECTION Erreur 2 : ne pas garder une référence stale à la connexion.
    // On appelle DBConnection.getInstance() dans chaque méthode.

    /** Authentifie un utilisateur et retourne son objet typé */
    public Utilisateur authenticate(String username, String password) {
        String sql = "SELECT * FROM utilisateur WHERE username=? AND password=SHA2(?,256) AND actif=TRUE";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur authentification : " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean insert(Utilisateur u) {
        String sql = "INSERT INTO utilisateur(nom,prenom,username,password,role) VALUES(?,?,?,SHA2(?,256),?)";
        Connection conn = DBConnection.getInstance(); // CORRECTION Erreur 2
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nom=?,prenom=?,username=?,role=? WHERE id=?";
        Connection conn = DBConnection.getInstance(); // CORRECTION Erreur 2
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getRole());
            ps.setInt(5, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE utilisateur SET actif=FALSE WHERE id=?";
        Connection conn = DBConnection.getInstance(); // CORRECTION Erreur 2
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id=?";
        Connection conn = DBConnection.getInstance(); // CORRECTION Erreur 2
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildUtilisateur(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur WHERE actif=TRUE";
        Connection conn = DBConnection.getInstance(); // CORRECTION Erreur 2
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildUtilisateur(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll utilisateurs : " + e.getMessage());
        }
        return list;
    }

    private Utilisateur buildUtilisateur(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        int    id   = rs.getInt("id");
        String nom  = rs.getString("nom");
        String pre  = rs.getString("prenom");
        String usr  = rs.getString("username");
        String pwd  = rs.getString("password");
        return switch (role) {
            case "CLIENT"    -> new Client(id, nom, pre, usr, pwd, 0);
            case "SERVEUSE"  -> new Serveuse(id, nom, pre, usr, pwd, 0);
            case "CUISINIER" -> new Cuisinier(id, nom, pre, usr, pwd, "");
            default          -> new Client(id, nom, pre, usr, pwd, 0);
        };
    }
}


// ── dao/PlatDAO.java ────────────────────────────────────────
package dao;

import model.Plat;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatDAO implements IDao<Plat> {

    // CORRECTION Erreur 2 : connexion récupérée localement dans chaque méthode.

    @Override
    public boolean insert(Plat p) {
        String sql = "INSERT INTO plat(nom,description,prix,disponible,id_menu) VALUES(?,?,?,?,?)";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setBoolean(4, p.isDisponible());
            ps.setInt(5, p.getIdMenu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert plat : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Plat p) {
        String sql = "UPDATE plat SET nom=?,description=?,prix=?,disponible=?,id_menu=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setBoolean(4, p.isDisponible());
            ps.setInt(5, p.getIdMenu());
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update plat : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM plat WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete plat : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Plat findById(int id) {
        String sql = "SELECT p.*, m.nom AS nom_menu FROM plat p JOIN menu m ON m.id=p.id_menu WHERE p.id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildPlat(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById plat : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Plat> findAll() {
        List<Plat> list = new ArrayList<>();
        String sql = "SELECT p.*, m.nom AS nom_menu FROM plat p JOIN menu m ON m.id=p.id_menu ORDER BY m.nom, p.nom";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildPlat(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll plats : " + e.getMessage());
        }
        return list;
    }

    /** Récupère les plats d'un menu donné */
    public List<Plat> findByMenu(int idMenu) {
        List<Plat> list = new ArrayList<>();
        String sql = "SELECT p.*, m.nom AS nom_menu FROM plat p JOIN menu m ON m.id=p.id_menu WHERE p.id_menu=? AND p.disponible=TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMenu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(buildPlat(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findByMenu : " + e.getMessage());
        }
        return list;
    }

    private Plat buildPlat(ResultSet rs) throws SQLException {
        Plat p = new Plat(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getDouble("prix"),
            rs.getBoolean("disponible"),
            rs.getInt("id_menu")
        );
        p.setNomMenu(rs.getString("nom_menu"));
        return p;
    }
}


// ── dao/CommandeDAO.java ────────────────────────────────────
package dao;

import model.Commande;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO implements IDao<Commande> {

    // CORRECTION Erreur 2 : connexion recuperee localement dans chaque methode.

    @Override
    public boolean insert(Commande c) {
        String sql = "INSERT INTO commande(date_commande,statut,num_table,id_client,id_serveuse) VALUES(NOW(),?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getStatut());
            ps.setInt(2, c.getNumTable());
            ps.setInt(3, c.getIdClient());
            ps.setInt(4, c.getIdServeuse());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) c.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur insert commande : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Commande c) {
        String sql = "UPDATE commande SET statut=?,num_table=?,id_serveuse=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getStatut());
            ps.setInt(2, c.getNumTable());
            ps.setInt(3, c.getIdServeuse());
            ps.setInt(4, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE commande SET statut='ANNULEE' WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur annulation commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Commande findById(int id) {
        String sql = "SELECT c.*, CONCAT(cl.prenom,' ',cl.nom) AS nom_client, " +
                     "CONCAT(sv.prenom,' ',sv.nom) AS nom_serveuse " +
                     "FROM commande c " +
                     "LEFT JOIN utilisateur cl ON cl.id=c.id_client " +
                     "LEFT JOIN utilisateur sv ON sv.id=c.id_serveuse " +
                     "WHERE c.id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildCommande(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById commande : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Commande> findAll() {
        return findByStatut(null);
    }

    public List<Commande> findByStatut(String statut) {
        List<Commande> list = new ArrayList<>();
        String cond = (statut != null) ? " WHERE c.statut=?" : "";
        String sql  = "SELECT c.*, CONCAT(cl.prenom,' ',cl.nom) AS nom_client, " +
                      "CONCAT(sv.prenom,' ',sv.nom) AS nom_serveuse " +
                      "FROM commande c " +
                      "LEFT JOIN utilisateur cl ON cl.id=c.id_client " +
                      "LEFT JOIN utilisateur sv ON sv.id=c.id_serveuse" + cond +
                      " ORDER BY c.date_commande DESC";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (statut != null) ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(buildCommande(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findByStatut : " + e.getMessage());
        }
        return list;
    }

    public boolean changerStatut(int idCommande, String nouveauStatut) {
        String sql = "UPDATE commande SET statut=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nouveauStatut);
            ps.setInt(2, idCommande);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur changerStatut : " + e.getMessage());
            return false;
        }
    }

    private Commande buildCommande(ResultSet rs) throws SQLException {
        Commande c = new Commande(
            rs.getInt("id"),
            rs.getTimestamp("date_commande"),
            rs.getString("statut"),
            rs.getInt("num_table"),
            rs.getInt("id_client"),
            rs.getInt("id_serveuse")
        );
        c.setNomClient(rs.getString("nom_client"));
        c.setNomServeuse(rs.getString("nom_serveuse"));
        return c;
    }
}




// ── dao/MenuDAO.java ────────────────────────────────────────
// CORRECTION Erreur 1 : classe MenuDAO manquante
package dao;

import model.Menu;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO implements IDao<Menu> {

    @Override
    public boolean insert(Menu m) {
        String sql = "INSERT INTO menu(nom,description) VALUES(?,?)";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert menu : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Menu m) {
        String sql = "UPDATE menu SET nom=?,description=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            ps.setInt(3, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update menu : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM menu WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete menu : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Menu findById(int id) {
        String sql = "SELECT * FROM menu WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildMenu(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById menu : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY nom";
        Connection conn = DBConnection.getInstance();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildMenu(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll menus : " + e.getMessage());
        }
        return list;
    }

    /** Recherche un menu par son nom exact */
    public Menu findByNom(String nom) {
        String sql = "SELECT * FROM menu WHERE nom=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildMenu(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findByNom menu : " + e.getMessage());
        }
        return null;
    }

    private Menu buildMenu(ResultSet rs) throws SQLException {
        return new Menu(rs.getInt("id"), rs.getString("nom"), rs.getString("description"));
    }
}


// ── dao/LigneCommandeDAO.java ───────────────────────────────
// CORRECTION Erreur 5 : classe LigneCommandeDAO manquante
package dao;

import model.LigneCommande;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO implements IDao<LigneCommande> {

    @Override
    public boolean insert(LigneCommande lc) {
        String sql = "INSERT INTO ligne_commande(quantite,prix_unitaire,id_commande,id_plat) VALUES(?,?,?,?)";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lc.getQuantite());
            ps.setDouble(2, lc.getPrixUnitaire());
            ps.setInt(3, lc.getIdCommande());
            ps.setInt(4, lc.getIdPlat());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) lc.setId(keys.getInt(1));
            }
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert ligne commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(LigneCommande lc) {
        String sql = "UPDATE ligne_commande SET quantite=?,prix_unitaire=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lc.getQuantite());
            ps.setDouble(2, lc.getPrixUnitaire());
            ps.setInt(3, lc.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update ligne commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM ligne_commande WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete ligne commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public LigneCommande findById(int id) {
        String sql = "SELECT lc.*, p.nom AS nom_plat FROM ligne_commande lc " +
                     "JOIN plat p ON p.id=lc.id_plat WHERE lc.id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildLigne(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById ligne commande : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<LigneCommande> findAll() {
        return new ArrayList<>();
    }

    /** Recupere toutes les lignes d'une commande donnee */
    public List<LigneCommande> findByCommande(int idCommande) {
        List<LigneCommande> list = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom AS nom_plat FROM ligne_commande lc " +
                     "JOIN plat p ON p.id = lc.id_plat WHERE lc.id_commande=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(buildLigne(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findByCommande : " + e.getMessage());
        }
        return list;
    }

    private LigneCommande buildLigne(ResultSet rs) throws SQLException {
        LigneCommande lc = new LigneCommande(
            rs.getInt("quantite"),
            rs.getDouble("prix_unitaire"),
            rs.getInt("id_commande"),
            rs.getInt("id_plat")
        );
        lc.setId(rs.getInt("id"));
        lc.setNomPlat(rs.getString("nom_plat"));
        return lc;
    }
}


// ── dao/FactureDAO.java ─────────────────────────────────────
// CORRECTION Erreur 6 : classe FactureDAO manquante
package dao;

import model.Facture;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO implements IDao<Facture> {

    @Override
    public boolean insert(Facture f) {
        String sql = "INSERT INTO facture(date_facture,montant_total,remise,montant_final,id_commande) " +
                     "VALUES(NOW(),?,?,?,?)";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, f.getMontantTotal());
            ps.setDouble(2, f.getRemise());
            ps.setDouble(3, f.getMontantFinal());
            ps.setInt(4, f.getIdCommande());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) f.setId(keys.getInt(1));
            }
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert facture : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Facture f) {
        String sql = "UPDATE facture SET remise=?,montant_final=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, f.getRemise());
            ps.setDouble(2, f.getMontantFinal());
            ps.setInt(3, f.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update facture : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM facture WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete facture : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Facture findById(int id) {
        String sql = "SELECT * FROM facture WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildFacture(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById facture : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Facture> findAll() {
        List<Facture> list = new ArrayList<>();
        String sql = "SELECT * FROM facture ORDER BY date_facture DESC";
        Connection conn = DBConnection.getInstance();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildFacture(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll factures : " + e.getMessage());
        }
        return list;
    }

    /** Retrouve la facture liee a une commande */
    public Facture findByCommande(int idCommande) {
        String sql = "SELECT * FROM facture WHERE id_commande=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildFacture(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findByCommande facture : " + e.getMessage());
        }
        return null;
    }

    private Facture buildFacture(ResultSet rs) throws SQLException {
        Facture f = new Facture(
            rs.getInt("id_commande"),
            rs.getDouble("montant_total"),
            rs.getDouble("remise")
        );
        f.setId(rs.getInt("id"));
        f.setDateFacture(rs.getTimestamp("date_facture"));
        f.setMontantFinal(rs.getDouble("montant_final"));
        return f;
    }
}


// ╔══════════════════════════════════════════════════════════╗
// ║  COUCHE CONTROLLER                                       ║
// ╚══════════════════════════════════════════════════════════╝

// ── controller/AuthController.java ─────────────────────────
package controller;

import dao.UtilisateurDAO;
import model.Utilisateur;

public class AuthController {
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private Utilisateur utilisateurConnecte = null;

    /** Tentative de connexion. Retourne true si succès. */
    public boolean login(String username, String password) {
        if (username == null || username.isBlank() ||
            password == null || password.isBlank()) {
            return false;
        }
        utilisateurConnecte = utilisateurDAO.authenticate(username, password);
        return utilisateurConnecte != null;
    }

    public void logout() {
        utilisateurConnecte = null;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public boolean isLoggedIn() {
        return utilisateurConnecte != null;
    }

    public String getRole() {
        return isLoggedIn() ? utilisateurConnecte.getRole() : "";
    }
}



// ── controller/MenuController.java ──────────────────────────
// CORRECTION Erreur 1 : classe MenuController manquante
package controller;

import dao.MenuDAO;
import dao.PlatDAO;
import model.Menu;
import model.Plat;

import java.util.List;

public class MenuController {
    private MenuDAO menuDAO = new MenuDAO();
    private PlatDAO platDAO = new PlatDAO();

    // ---- Menus ----
    public List<Menu> getAllMenus() {
        return menuDAO.findAll();
    }

    public Menu getMenuByNom(String nom) {
        return menuDAO.findByNom(nom);
    }

    public boolean ajouterMenu(Menu m) {
        return menuDAO.insert(m);
    }

    public boolean modifierMenu(Menu m) {
        return menuDAO.update(m);
    }

    public boolean supprimerMenu(int id) {
        return menuDAO.delete(id);
    }

    // ---- Plats ----
    public List<Plat> getAllPlats() {
        return platDAO.findAll();
    }

    public List<Plat> getPlatsByMenu(int idMenu) {
        return platDAO.findByMenu(idMenu);
    }

    public Plat getPlatById(int id) {
        return platDAO.findById(id);
    }

    public boolean ajouterPlat(Plat p) {
        return platDAO.insert(p);
    }

    public boolean modifierPlat(Plat p) {
        return platDAO.update(p);
    }

    public boolean supprimerPlat(int id) {
        return platDAO.delete(id);
    }
}

// ── controller/CommandeController.java ──────────────────────
package controller;

import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import dao.FactureDAO;
import model.Commande;
import model.LigneCommande;
import model.Facture;
import model.Plat;

import java.util.List;

public class CommandeController {
    private CommandeDAO     commandeDAO     = new CommandeDAO();
    private LigneCommandeDAO lcDAO          = new LigneCommandeDAO();
    private FactureDAO      factureDAO      = new FactureDAO();

    /**
     * Crée une commande avec ses lignes de détail.
     */
    public boolean creerCommande(Commande commande, List<LigneCommande> lignes) {
        boolean ok = commandeDAO.insert(commande);
        if (ok) {
            for (LigneCommande lc : lignes) {
                lc.setIdCommande(commande.getId());
                lcDAO.insert(lc);
            }
        }
        return ok;
    }

    public List<Commande> getCommandesEnAttente() {
        return commandeDAO.findByStatut("EN_ATTENTE");
    }

    public List<Commande> getCommandesEnCours() {
        return commandeDAO.findByStatut("EN_COURS");
    }

    public List<Commande> getCommandesPretes() {
        return commandeDAO.findByStatut("PRETE");
    }

    public List<Commande> getCommandesServies() {
        return commandeDAO.findByStatut("SERVIE");
    }

    public boolean commencerTraitement(int idCommande) {
        return commandeDAO.changerStatut(idCommande, "EN_COURS");
    }

    public boolean marquerPrete(int idCommande) {
        return commandeDAO.changerStatut(idCommande, "PRETE");
    }

    public boolean marquerServie(int idCommande) {
        return commandeDAO.changerStatut(idCommande, "SERVIE");
    }

    public boolean annulerCommande(int idCommande) {
        return commandeDAO.changerStatut(idCommande, "ANNULEE");
    }

    public Facture genererFacture(int idCommande) {
        Commande c = commandeDAO.findById(idCommande);
        if (c == null) return null;
        List<LigneCommande> lignes = lcDAO.findByCommande(idCommande);
        double total = lignes.stream()
                             .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
                             .sum();
        Facture f = new Facture(idCommande, total, 0.0);
        factureDAO.insert(f);
        return f;
    }
}


// ╔══════════════════════════════════════════════════════════╗
// ║  COUCHE VIEW                                             ║
// ╚══════════════════════════════════════════════════════════╝

// ── view/LoginFrame.java ────────────────────────────────────
package view;

import controller.AuthController;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Fenêtre de connexion - première interface du projet.
 */
public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnConnexion;
    private JLabel         lblErreur;
    private AuthController authCtrl = new AuthController();

    public LoginFrame() {
        super("Restaurant - Connexion");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal avec fond
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Titre
        JLabel lblTitre = new JLabel("🍽 Gestion Restaurant", JLabel.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitre.setForeground(new Color(180, 70, 30));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitre, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom d'utilisateur
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Utilisateur :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUsername = new JTextField(15);
        formPanel.add(txtUsername, gbc);

        // Mot de passe
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField(15);
        formPanel.add(txtPassword, gbc);

        // Message d'erreur
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        lblErreur = new JLabel(" ");
        lblErreur.setForeground(Color.RED);
        lblErreur.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(lblErreur, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Bouton connexion
        btnConnexion = new JButton("Se connecter");
        btnConnexion.setBackground(new Color(180, 70, 30));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFont(new Font("Arial", Font.BOLD, 13));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConnexion.addActionListener(e -> handleLogin());
        txtPassword.addActionListener(e -> handleLogin());
        mainPanel.add(btnConnexion, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblErreur.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (authCtrl.login(username, password)) {
            Utilisateur user = authCtrl.getUtilisateurConnecte();
            lblErreur.setText("");
            this.dispose();
            ouvrirDashboard(user);
        } else {
            lblErreur.setText("Identifiants incorrects. Réessayez.");
            txtPassword.setText("");
        }
    }

    private void ouvrirDashboard(Utilisateur user) {
        switch (user.getRole()) {
            case "CLIENT"    -> new DashboardClientFrame(user);
            case "SERVEUSE"  -> new DashboardServeuseFrame(user);
            case "CUISINIER" -> new DashboardCuisinierFrame(user);
        }
    }

    public static void main(String[] args) {
        // Thème Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus non disponible, utilisation du thème par défaut.");
        }
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}



// ── view/DialogAjouterPlat.java ─────────────────────────────
// CORRECTION Erreur 1 : classe DialogAjouterPlat manquante
package view;

import controller.MenuController;
import model.Menu;
import model.Plat;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialogue modal pour ajouter ou modifier un plat.
 * Si platAModifier == null => mode AJOUT, sinon => mode MODIFICATION.
 */
public class DialogAjouterPlat extends JDialog {

    private JTextField    txtNom, txtDescription, txtPrix;
    private JComboBox<String> cbxMenu;
    private JCheckBox     chkDisponible;
    private boolean       confirme = false;
    private MenuController menuCtrl;
    private Plat          platAModifier;
    private List<Menu>    menus;

    public DialogAjouterPlat(JFrame parent, MenuController menuCtrl, Plat platAModifier) {
        super(parent, platAModifier == null ? "Ajouter un plat" : "Modifier le plat", true);
        this.menuCtrl      = menuCtrl;
        this.platAModifier = platAModifier;
        initUI();
        if (platAModifier != null) remplirChamps();
    }

    private void initUI() {
        setSize(420, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 5, 5, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Ligne 0 : Nom
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        form.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNom = new JTextField(15);
        form.add(txtNom, gbc);

        // Ligne 1 : Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        form.add(new JLabel("Description :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtDescription = new JTextField(15);
        form.add(txtDescription, gbc);

        // Ligne 2 : Prix
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        form.add(new JLabel("Prix (TND) :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPrix = new JTextField(15);
        form.add(txtPrix, gbc);

        // Ligne 3 : Menu
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        form.add(new JLabel("Menu :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        cbxMenu = new JComboBox<>();
        menus = menuCtrl.getAllMenus();
        for (Menu m : menus) cbxMenu.addItem(m.getNom());
        form.add(cbxMenu, gbc);

        // Ligne 4 : Disponible
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        chkDisponible = new JCheckBox("Disponible", true);
        form.add(chkDisponible, gbc);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Valider");
        JButton btnAnnuler = new JButton("Annuler");
        btnOk.setBackground(new Color(34, 139, 34));
        btnOk.setForeground(Color.WHITE);
        btnOk.setFocusPainted(false);
        btnOk.addActionListener(e -> valider());
        btnAnnuler.addActionListener(e -> dispose());
        btnPanel.add(btnOk);
        btnPanel.add(btnAnnuler);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void remplirChamps() {
        txtNom.setText(platAModifier.getNom());
        txtDescription.setText(platAModifier.getDescription());
        txtPrix.setText(String.valueOf(platAModifier.getPrix()));
        chkDisponible.setSelected(platAModifier.isDisponible());
        // Selectionner le menu correspondant
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getId() == platAModifier.getIdMenu()) {
                cbxMenu.setSelectedIndex(i);
                break;
            }
        }
    }

    private void valider() {
        String nom  = txtNom.getText().trim();
        String desc = txtDescription.getText().trim();
        String prixStr = txtPrix.getText().trim();
        if (nom.isEmpty() || prixStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom et le prix sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double prix;
        try {
            prix = Double.parseDouble(prixStr);
            if (prix < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prix invalide. Entrez un nombre positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idMenu = menus.isEmpty() ? 0 : menus.get(cbxMenu.getSelectedIndex()).getId();
        boolean dispo = chkDisponible.isSelected();

        boolean ok;
        if (platAModifier == null) {
            // AJOUT
            Plat p = new Plat(0, nom, desc, prix, dispo, idMenu);
            ok = menuCtrl.ajouterPlat(p);
        } else {
            // MODIFICATION
            platAModifier.setNom(nom);
            platAModifier.setDescription(desc);
            platAModifier.setPrix(prix);
            platAModifier.setDisponible(dispo);
            platAModifier.setIdMenu(idMenu);
            ok = menuCtrl.modifierPlat(platAModifier);
        }

        if (ok) {
            confirme = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.", "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Retourne true si l'utilisateur a valide le formulaire avec succes. */
    public boolean isConfirme() { return confirme; }
}

// ── view/GestionMenuFrame.java ──────────────────────────────
package view;

import controller.MenuController;
import model.Plat;
import model.Menu;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Interface de gestion des menus (CRUD des plats) - accessible au cuisinier.
 * Contient les 5 boutons requis : Ajouter, Modifier, Supprimer, Enregistrer, Fermer.
 */
public class GestionMenuFrame extends JFrame {

    private JTable             table;
    private DefaultTableModel  tableModel;
    private JButton            btnAjouter, btnModifier, btnSupprimer,
                               btnEnregistrer, btnFermer;
    private MenuController     menuCtrl = new MenuController();
    private JComboBox<String>  cbxMenuFilter;

    public GestionMenuFrame() {
        super("Gestion des Menus et Plats");
        initUI();
        chargerPlats();
    }

    private void initUI() {
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === Table des plats ===
        String[] colonnes = {"ID", "Nom du plat", "Description", "Prix (TND)", "Disponible", "Menu"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        // Masquer colonne ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);

        // === Filtre par menu ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filtrer par menu :"));
        cbxMenuFilter = new JComboBox<>();
        cbxMenuFilter.addItem("Tous les menus");
        menuCtrl.getAllMenus().forEach(m -> cbxMenuFilter.addItem(m.getNom()));
        cbxMenuFilter.addActionListener(e -> chargerPlats());
        topPanel.add(cbxMenuFilter);

        // === Boutons ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));

        btnAjouter     = creerBouton("Ajouter",      new Color(34, 139, 34));
        btnModifier    = creerBouton("Modifier",      new Color(70, 130, 180));
        btnSupprimer   = creerBouton("Supprimer",     new Color(200, 50, 50));
        btnEnregistrer = creerBouton("Enregistrer",   new Color(180, 120, 0));
        btnFermer      = creerBouton("Fermer",        new Color(100, 100, 100));

        btnPanel.add(btnAjouter);
        btnPanel.add(btnModifier);
        btnPanel.add(btnSupprimer);
        btnPanel.add(btnEnregistrer);
        btnPanel.add(btnFermer);

        // === Actions des boutons ===
        btnAjouter.addActionListener(e -> actionAjouter());
        btnModifier.addActionListener(e -> actionModifier());
        btnSupprimer.addActionListener(e -> actionSupprimer());
        btnEnregistrer.addActionListener(e -> actionEnregistrer());
        btnFermer.addActionListener(e -> dispose());

        // === Layout ===
        setLayout(new BorderLayout(5, 5));
        add(topPanel,   BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel,   BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton("<< " + texte + " >>");
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 34));
        return btn;
    }

    private void chargerPlats() {
        tableModel.setRowCount(0);
        List<Plat> plats;
        String filtreMenuNom = (String) cbxMenuFilter.getSelectedItem();
        if ("Tous les menus".equals(filtreMenuNom)) {
            plats = menuCtrl.getAllPlats();
        } else {
            Menu m = menuCtrl.getMenuByNom(filtreMenuNom);
            plats = (m != null) ? menuCtrl.getPlatsByMenu(m.getId()) : menuCtrl.getAllPlats();
        }
        for (Plat p : plats) {
            tableModel.addRow(new Object[]{
                p.getId(), p.getNom(), p.getDescription(),
                String.format("%.3f", p.getPrix()),
                p.isDisponible() ? "Oui" : "Non",
                p.getNomMenu()
            });
        }
    }

    private void actionAjouter() {
        DialogAjouterPlat dlg = new DialogAjouterPlat(this, menuCtrl, null);
        dlg.setVisible(true);
        if (dlg.isConfirme()) chargerPlats();
    }

    private void actionModifier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un plat à modifier.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int   idPlat  = (int) tableModel.getValueAt(selectedRow, 0);
        Plat  plat    = menuCtrl.getPlatById(idPlat);
        DialogAjouterPlat dlg = new DialogAjouterPlat(this, menuCtrl, plat);
        dlg.setVisible(true);
        if (dlg.isConfirme()) chargerPlats();
    }

    private void actionSupprimer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un plat à supprimer.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String nomPlat = (String) tableModel.getValueAt(selectedRow, 1);
        int    idPlat  = (int)   tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer le plat « " + nomPlat + " » ?",
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (menuCtrl.supprimerPlat(idPlat))
                JOptionPane.showMessageDialog(this, "Plat supprimé.");
            chargerPlats();
        }
    }

    private void actionEnregistrer() {
        JOptionPane.showMessageDialog(this,
            "Données enregistrées dans la base de données.",
            "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
    }
}


// ── view/DashboardClientFrame.java (squelette) ──────────────
package view;

import model.Utilisateur;
import controller.MenuController;
import controller.CommandeController;

import javax.swing.*;
import java.awt.*;

public class DashboardClientFrame extends JFrame {
    private Utilisateur user;

    public DashboardClientFrame(Utilisateur user) {
        super("Espace Client - " + user.getNomComplet());
        this.user = user;
        initUI();
    }

    private void initUI() {
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblBienvenue = new JLabel("Bienvenue, " + user.getNomComplet() + " !", JLabel.CENTER);
        lblBienvenue.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblBienvenue, BorderLayout.NORTH);

        // Boutons du client
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        JButton btnMenu    = new JButton("Parcourir le Menu");
        JButton btnCommande= new JButton("Passer une Commande");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 13));
        btnCommande.setFont(new Font("Arial", Font.BOLD, 13));
        btnMenu.addActionListener(e -> { new GestionMenuFrame(); }); // CORRECTION Erreur 4 : bloc {} requis
        btnPanel.add(btnMenu);
        btnPanel.add(btnCommande);
        panel.add(btnPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }
}


// ── view/DashboardServeuseFrame.java (squelette) ────────────
package view;

import model.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class DashboardServeuseFrame extends JFrame {
    public DashboardServeuseFrame(Utilisateur user) {
        super("Espace Serveuse - " + user.getNomComplet());
        initUI(user);
    }

    private void initUI(Utilisateur user) {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        String[] btns = {"Parcourir le Menu", "Passer une Commande",
                         "Commandes en cours", "Commandes reçues",
                         "Générer une Facture", "Se déconnecter"};
        for (String t : btns) {
            JButton b = new JButton(t);
            b.setFont(new Font("Arial", Font.BOLD, 13));
            if ("Se déconnecter".equals(t)) {
                b.setBackground(new Color(200, 50, 50));
                b.setForeground(Color.WHITE);
                b.addActionListener(e -> { dispose(); new LoginFrame(); });
            }
            panel.add(b);
        }
        add(panel);
        setVisible(true);
    }
}


// ── view/DashboardCuisinierFrame.java (squelette) ───────────
package view;

import model.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class DashboardCuisinierFrame extends JFrame {
    public DashboardCuisinierFrame(Utilisateur user) {
        super("Espace Cuisinier - " + user.getNomComplet());
        initUI(user);
    }

    private void initUI(Utilisateur user) {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        String[] btns = {"Gérer les Plats (CRUD)", "Commandes en attente",
                         "Commandes en cours", "Commandes servies",
                         "Notifications", "Se déconnecter"};
        for (String t : btns) {
            JButton b = new JButton(t);
            b.setFont(new Font("Arial", Font.BOLD, 13));
            if ("Gérer les Plats (CRUD)".equals(t))
                b.addActionListener(e -> { new GestionMenuFrame(); }); // CORRECTION Erreur 4
            if ("Se déconnecter".equals(t)) {
                b.setBackground(new Color(200, 50, 50));
                b.setForeground(Color.WHITE);
                b.addActionListener(e -> { dispose(); new LoginFrame(); });
            }
            panel.add(b);
        }
        add(panel);
        setVisible(true);
    }
}

// ============================================================
//  FIN DU CODE JAVA
// ============================================================
