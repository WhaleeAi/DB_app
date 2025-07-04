package org.example.model;

import java.sql.*;
import java.util.*;
import java.util.Observable;

public class Users {

    private static final Users INSTANCE = new Users();

    private Users() { loadUsers(); }

    private final List<User> cache = new ArrayList<>();

    public List<User> getAllUsers() { return Collections.unmodifiableList(cache); }

    private void loadUsers() {
        cache.clear();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery("SELECT * FROM user")) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setLogin(rs.getString("login"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                cache.add(u);
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void addUser(User u) {
        String sql = "INSERT INTO user (login, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getLogin());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) u.setId(keys.getInt(1)); }

            cache.add(u);

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateUser(User u) {
        String sql = "UPDATE user SET login=?, password=?, role=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getLogin());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setInt   (4, u.getId());
            ps.executeUpdate();

            for (int i = 0; i < cache.size(); i++)
                if (cache.get(i).getId() == u.getId()) { cache.set(i, u); break; }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            cache.removeIf(u -> u.getId() == id);

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public enum Type { ADD, UPDATE, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) { }

    public static Users getInstance() { return INSTANCE; }
}
