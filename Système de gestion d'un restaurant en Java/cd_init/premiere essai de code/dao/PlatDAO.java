package projet.dao;

import projet.model.Plat;
import projet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatDAO implements IDao<Plat> {

    private Connection conn = DBConnection.getInstance();

    @Override
    public boolean insert(Plat p) {
        String sql = "INSERT INTO plat(nom,description,prix,disponible,id_menu) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setBoolean(4, p.isDisponible());
            ps.setInt(5, p.getIdMenu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert plat : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Plat p) {
        String sql = "UPDATE plat SET nom=?,description=?,prix=?,disponible=?,id_menu=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setBoolean(4, p.isDisponible());
            ps.setInt(5, p.getIdMenu());
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update plat : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM plat WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete plat : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Plat findById(int id) {
        String sql = "SELECT p.*, m.nom AS nom_menu FROM plat p JOIN menu m ON m.id=p.id_menu WHERE p.id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildPlat(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById plat : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Plat> findAll() {
        List<Plat> list = new ArrayList<>();
        String sql = "SELECT p.*, m.nom AS nom_menu FROM plat p JOIN menu m ON m.id=p.id_menu ORDER BY m.nom, p.nom";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildPlat(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll plats : " + e.getMessage());
        }
        return list;
    }

    
    public List<Plat> findByMenu(int idMenu) {
        List<Plat> list = new ArrayList<>();
        String sql = "SELECT p.*, m.nom AS nom_menu FROM plat p JOIN menu m ON m.id=p.id_menu WHERE p.id_menu=? AND p.disponible=TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMenu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(buildPlat(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findByMenu : " + e.getMessage());
        }
        return list;
    }

    private Plat buildPlat(ResultSet rs) throws SQLException {
        Plat p = new Plat(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getDouble("prix"),
            rs.getBoolean("disponible"),
            rs.getInt("id_menu")
        );
        p.setNomMenu(rs.getString("nom_menu"));
        return p;
    }
}
