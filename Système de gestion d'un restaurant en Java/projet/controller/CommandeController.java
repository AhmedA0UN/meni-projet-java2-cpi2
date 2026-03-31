package projet.controller;

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