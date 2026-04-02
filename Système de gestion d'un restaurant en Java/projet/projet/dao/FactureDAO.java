package dao;

import model.Facture;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO implements IDao<Facture> {

    @Override
    public boolean insert(Facture f) {
        String sql = "INSERT INTO facture(date_facture,montant_total,remise,montant_final,id_commande) " +
                     "VALUES(NOW(),?,?,?,?)";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, f.getMontantTotal());
            ps.setDouble(2, f.getRemise());
            ps.setDouble(3, f.getMontantFinal());
            ps.setInt(4, f.getIdCommande());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) f.setId(keys.getInt(1));
            }
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert facture : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Facture f) {
        String sql = "UPDATE facture SET remise=?,montant_final=? WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, f.getRemise());
            ps.setDouble(2, f.getMontantFinal());
            ps.setInt(3, f.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update facture : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM facture WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete facture : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Facture findById(int id) {
        String sql = "SELECT * FROM facture WHERE id=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildFacture(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById facture : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Facture> findAll() {
        List<Facture> list = new ArrayList<>();
        String sql = "SELECT * FROM facture ORDER BY date_facture DESC";
        Connection conn = DBConnection.getInstance();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildFacture(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll factures : " + e.getMessage());
        }
        return list;
    }

    /** Retrouve la facture liee a une commande */
    public Facture findByCommande(int idCommande) {
        String sql = "SELECT * FROM facture WHERE id_commande=?";
        Connection conn = DBConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildFacture(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findByCommande facture : " + e.getMessage());
        }
        return null;
    }

    private Facture buildFacture(ResultSet rs) throws SQLException {
        Facture f = new Facture(
            rs.getInt("id_commande"),
            rs.getDouble("montant_total"),
            rs.getDouble("remise")
        );
        f.setId(rs.getInt("id"));
        f.setDateFacture(rs.getTimestamp("date_facture"));
        f.setMontantFinal(rs.getDouble("montant_final"));
        return f;
    }
}