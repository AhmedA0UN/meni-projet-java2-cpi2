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
