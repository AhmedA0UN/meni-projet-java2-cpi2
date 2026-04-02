package projet.model;

public class Plat {
    private int     id;
    private String  nom;
    private String  description;
    private double  prix;
    private boolean disponible;
    private int     idMenu;
    private String  nomMenu;  

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