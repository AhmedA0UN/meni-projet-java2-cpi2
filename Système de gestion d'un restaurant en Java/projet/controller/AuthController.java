package projet.controller;

import projet.dao.UtilisateurDAO;
import projet.model.Utilisateur;

public class AuthController {
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private Utilisateur utilisateurConnecte = null;


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
