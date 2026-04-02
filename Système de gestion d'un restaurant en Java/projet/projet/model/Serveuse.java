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