package projet.controller;

import projet.dao.MenuDAO;
import projet.dao.PlatDAO;
import projet.model.Menu;
import projet.model.Plat;

import java.util.List;


public class MenuController {

    private final MenuDAO menuDAO = new MenuDAO();
    private final PlatDAO platDAO = new PlatDAO();

   
    public List<Menu> getAllMenus() {
        return menuDAO.findAll();
    }

    
    public Menu getMenuById(int id) {
        return menuDAO.findById(id);
    }

    
    public Menu getMenuByNom(String nom) {
        return getAllMenus().stream()
                .filter(m -> m.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }

    
    public boolean ajouterMenu(Menu menu) {
        if (menu == null || menu.getNom() == null || menu.getNom().isBlank()) {
            return false;
        }
        return menuDAO.insert(menu);
    }


    public boolean modifierMenu(Menu menu) {
        if (menu == null || menu.getId() <= 0) return false;
        return menuDAO.update(menu);
    }

    
    public boolean supprimerMenu(int id) {
        return menuDAO.delete(id);
    }

    // ══════════════════════════════════════════════
    // PLATS
    // ══════════════════════════════════════════════

    /** Retourne tous les plats (toutes catégories confondues). */
    public List<Plat> getAllPlats() {
        return platDAO.findAll();
    }

    /** Retourne un plat par son identifiant. */
    public Plat getPlatById(int id) {
        return platDAO.findById(id);
    }

    /** Retourne les plats d'un menu donné. */
    public List<Plat> getPlatsByMenu(int idMenu) {
        return platDAO.findByMenu(idMenu);
    }

    /**
     * Ajoute un nouveau plat après validation des champs.
     *
     * @return true si l'insertion a réussi, false sinon
     */
    public boolean ajouterPlat(Plat plat) {
        if (!validerPlat(plat)) return false;
        return platDAO.insert(plat);
    }

    /**
     * Modifie un plat existant après validation.
     *
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean modifierPlat(Plat plat) {
        if (plat == null || plat.getId() <= 0) return false;
        if (!validerPlat(plat)) return false;
        return platDAO.update(plat);
    }

    /**
     * Supprime un plat par son identifiant.
     *
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerPlat(int id) {
        return platDAO.delete(id);
    }

    /**
     * Bascule la disponibilité d'un plat (disponible ↔ indisponible).
     *
     * @return true si la mise à jour a réussi
     */
    public boolean toggleDisponibilite(int idPlat) {
        Plat plat = platDAO.findById(idPlat);
        if (plat == null) return false;
        plat.setDisponible(!plat.isDisponible());
        return platDAO.update(plat);
    }

    // ══════════════════════════════════════════════
    // VALIDATION INTERNE
    // ══════════════════════════════════════════════

    /**
     * Valide les champs obligatoires d'un plat.
     * Lève une IllegalArgumentException avec message si invalide.
     */
    private boolean validerPlat(Plat plat) {
        if (plat == null) return false;
        if (plat.getNom() == null || plat.getNom().isBlank())
            throw new IllegalArgumentException("Le nom du plat est obligatoire.");
        if (plat.getPrix() < 0)
            throw new IllegalArgumentException("Le prix ne peut pas être négatif.");
        if (plat.getIdMenu() <= 0)
            throw new IllegalArgumentException("Un menu doit être sélectionné.");
        return true;
    }
}
