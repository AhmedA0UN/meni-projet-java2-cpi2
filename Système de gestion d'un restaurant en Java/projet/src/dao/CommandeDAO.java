package projet.dao;

import projet.model.Commande;
import projet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO implements IDao<Commande> {

    private Connection conn = DBConnection.getInstance();

    @Override
    public boolean insert(Commande c) {
        String sql = "INSERT INTO commande(date_commande,statut,num_table,id_client,id_serveuse) VALUES(NOW(),?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getStatut());
            ps.setInt(2, c.getNumTable());
            ps.setInt(3, c.getIdClient());
            ps.setInt(4, c.getIdServeuse());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) c.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur insert commande : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Commande c) {
        String sql = "UPDATE commande SET statut=?,num_table=?,id_serveuse=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getStatut());
            ps.setInt(2, c.getNumTable());
            ps.setInt(3, c.getIdServeuse());
            ps.setInt(4, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE commande SET statut='ANNULEE' WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur annulation commande : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Commande findById(int id) {
        String sql = "SELECT c.*, CONCAT(cl.prenom,' ',cl.nom) AS nom_client, " +
                     "CONCAT(sv.prenom,' ',sv.nom) AS nom_serveuse " +
                     "FROM commande c " +
                     "LEFT JOIN utilisateur cl ON cl.id=c.id_client " +
                     "LEFT JOIN utilisateur sv ON sv.id=c.id_serveuse " +
                     "WHERE c.id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildCommande(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById commande : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Commande> findAll() {
        return findByStatut(null);
    }

    public List<Commande> findByStatut(String statut) {
        List<Commande> list = new ArrayList<>();
        String cond = (statut != null) ? " WHERE c.statut=?" : "";
        String sql  = "SELECT c.*, CONCAT(cl.prenom,' ',cl.nom) AS nom_client, " +
                      "CONCAT(sv.prenom,' ',sv.nom) AS nom_serveuse " +
                      "FROM commande c " +
                      "LEFT JOIN utilisateur cl ON cl.id=c.id_client " +
                      "LEFT JOIN utilisateur sv ON sv.id=c.id_serveuse" + cond +
                      " ORDER BY c.date_commande DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (statut != null) ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(buildCommande(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findByStatut : " + e.getMessage());
        }
        return list;
    }

    public boolean changerStatut(int idCommande, String nouveauStatut) {
        String sql = "UPDATE commande SET statut=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nouveauStatut);
            ps.setInt(2, idCommande);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur changerStatut : " + e.getMessage());
            return false;
        }
    }

    private Commande buildCommande(ResultSet rs) throws SQLException {
        Commande c = new Commande(
            rs.getInt("id"),
            rs.getTimestamp("date_commande"),
            rs.getString("statut"),
            rs.getInt("num_table"),
            rs.getInt("id_client"),
            rs.getInt("id_serveuse")
        );
        c.setNomClient(rs.getString("nom_client"));
        c.setNomServeuse(rs.getString("nom_serveuse"));
        return c;
    }
}
