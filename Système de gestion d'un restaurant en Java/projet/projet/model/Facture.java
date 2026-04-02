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