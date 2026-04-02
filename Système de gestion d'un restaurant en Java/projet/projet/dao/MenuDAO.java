package dao;

import model.Menu;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO implements IDao<Menu> {

    @Override
    public boolean insert(Menu m) {
        String sql = "INSERT INTO menu(nom,description) VALUES(?,?)";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert menu : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Menu m) {
        String sql = "UPDATE menu SET nom=?,description=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            ps.setInt(3, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update menu : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM menu WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete menu : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Menu findById(int id) {
        String sql = "SELECT * FROM menu WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildMenu(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById menu : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY nom";
        Connection conn = DBConnection.getInstance();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildMenu(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll menus : " + e.getMessage());
        }
        return list;
    }

    /** Recherche un menu par son nom exact */
    public Menu findByNom(String nom) {
        String sql = "SELECT * FROM menu WHERE nom=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildMenu(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findByNom menu : " + e.getMessage());
        }
        return null;
    }

    private Menu buildMenu(ResultSet rs) throws SQLException {
        return new Menu(rs.getInt("id"), rs.getString("nom"), rs.getString("description"));
    }
}    

