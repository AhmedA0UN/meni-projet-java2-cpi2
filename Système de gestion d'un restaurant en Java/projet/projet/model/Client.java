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