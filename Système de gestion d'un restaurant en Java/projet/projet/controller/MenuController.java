package controller;

import dao.MenuDAO;
import dao.PlatDAO;
import model.Menu;
import model.Plat;

import java.util.List;

public class MenuController {
    private MenuDAO menuDAO = new MenuDAO();
    private PlatDAO platDAO = new PlatDAO();

    // ---- Menus ----
    public List<Menu> getAllMenus() {
        return menuDAO.findAll();
    }

    public Menu getMenuByNom(String nom) {
        return menuDAO.findByNom(nom);
    }

    public boolean ajouterMenu(Menu m) {
        return menuDAO.insert(m);
    }

    public boolean modifierMenu(Menu m) {
        return menuDAO.update(m);
    }

    public boolean supprimerMenu(int id) {
        return menuDAO.delete(id);
    }

    // ---- Plats ----
    public List<Plat> getAllPlats() {
        return platDAO.findAll();
    }

    public List<Plat> getPlatsByMenu(int idMenu) {
        return platDAO.findByMenu(idMenu);
    }

    public Plat getPlatById(int id) {
        return platDAO.findById(id);
    }

    public boolean ajouterPlat(Plat p) {
        return platDAO.insert(p);
    }

    public boolean modifierPlat(Plat p) {
        return platDAO.update(p);
    }

    public boolean supprimerPlat(int id) {
        return platDAO.delete(id);
    }
}
