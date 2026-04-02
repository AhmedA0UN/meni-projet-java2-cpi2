package projet.dao;

import projet.model.Menu;
import projet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MenuDAO implements IDao<Menu> {

    private final Connection conn = DBConnection.getInstance();

    @Override
    public boolean insert(Menu menu) {
        String sql = "INSERT INTO menu (nom, description) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, menu.getNom());
            ps.setString(2, menu.getDescription());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) menu.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur insert menu : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Menu menu) {
        String sql = "UPDATE menu SET nom = ?, description = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menu.getNom());
            ps.setString(2, menu.getDescription());
            ps.setInt(3, menu.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update menu : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete menu : " + e.getMessage());
        }
        return false;
    }

    @Override
    public Menu findById(int id) {
        String sql = "SELECT * FROM menu WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return build(rs);
        } catch (SQLException e) {
            System.err.println("Erreur findById menu : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY nom";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(build(rs));
        } catch (SQLException e) {
            System.err.println("Erreur findAll menus : " + e.getMessage());
        }
        return list;
    }

    private Menu build(ResultSet rs) throws SQLException {
        return new Menu(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description")
        );
    }
}
