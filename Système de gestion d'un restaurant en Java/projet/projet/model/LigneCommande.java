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