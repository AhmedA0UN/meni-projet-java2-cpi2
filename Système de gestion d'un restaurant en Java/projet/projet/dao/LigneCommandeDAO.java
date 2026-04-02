package dao;

import model.LigneCommande;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO implements IDao<LigneCommande> {

    @Override
    public boolean insert(LigneCommande lc) {
        String sql = "INSERT INTO ligne_commande(quantite,prix_unitaire,id_commande,id_plat) VALUES(?,?,?,?)";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lc.getQuantite());
            ps.setDouble(2, lc.getPrixUnitaire());
            ps.setInt(3, lc.getIdCommande());
            ps.setInt(4, lc.getIdPlat());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) lc.setId(keys.getInt(1));
            }
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert ligne commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(LigneCommande lc) {
        String sql = "UPDATE ligne_commande SET quantite=?,prix_unitaire=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lc.getQuantite());
            ps.setDouble(2, lc.getPrixUnitaire());
            ps.setInt(3, lc.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update ligne commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM ligne_commande WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete ligne commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public LigneCommande findById(int id) {
        String sql = "SELECT lc.*, p.nom AS nom_plat FROM ligne_commande lc " +
                     "JOIN plat p ON p.id=lc.id_plat WHERE lc.id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildLigne(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById ligne commande : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<LigneCommande> findAll() {
        return new ArrayList<>();
    }

    /** Recupere toutes les lignes d'une commande donnee */
    public List<LigneCommande> findByCommande(int idCommande) {
        List<LigneCommande> list = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom AS nom_plat FROM ligne_commande lc " +
                     "JOIN plat p ON p.id = lc.id_plat WHERE lc.id_commande=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(buildLigne(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findByCommande : " + e.getMessage());
        }
        return list;
    }

    private LigneCommande buildLigne(ResultSet rs) throws SQLException {
        LigneCommande lc = new LigneCommande(
            rs.getInt("quantite"),
            rs.getDouble("prix_unitaire"),
            rs.getInt("id_commande"),
            rs.getInt("id_plat")
        );
        lc.setId(rs.getInt("id"));
        lc.setNomPlat(rs.getString("nom_plat"));
        return lc;
    }
}