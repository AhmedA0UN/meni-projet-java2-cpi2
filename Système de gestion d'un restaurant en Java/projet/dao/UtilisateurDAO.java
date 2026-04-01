package projet.dao;

import projet.model.Utilisateur;
import projet.model.Client;
import projet.model.Serveuse;
import projet.model.Cuisinier;
import projet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO implements IDao<Utilisateur> {

    private Connection conn = DBConnection.getInstance();

    
    public Utilisateur authenticate(String username, String password) {
        String sql = "SELECT * FROM utilisateur WHERE username=? AND password=SHA2(?,256) AND actif=TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur authentification : " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean insert(Utilisateur u) {
        String sql = "INSERT INTO utilisateur(nom,prenom,username,password,role) VALUES(?,?,?,SHA2(?,256),?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur insert utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nom=?,prenom=?,username=?,role=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getRole());
            ps.setInt(5, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE utilisateur SET actif=FALSE WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildUtilisateur(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur WHERE actif=TRUE";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(buildUtilisateur(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll utilisateurs : " + e.getMessage());
        }
        return list;
    }

    private Utilisateur buildUtilisateur(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        int    id   = rs.getInt("id");
        String nom  = rs.getString("nom");
        String pre  = rs.getString("prenom");
        String usr  = rs.getString("username");
        String pwd  = rs.getString("password");
        return switch (role) {
            case "CLIENT"    -> new Client(id, nom, pre, usr, pwd, 0);
            case "SERVEUSE"  -> new Serveuse(id, nom, pre, usr, pwd, 0);
            case "CUISINIER" -> new Cuisinier(id, nom, pre, usr, pwd, "");
            default          -> new Client(id, nom, pre, usr, pwd, 0);
        };
    }
}
