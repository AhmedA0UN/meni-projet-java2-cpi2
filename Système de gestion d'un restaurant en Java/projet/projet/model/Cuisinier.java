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