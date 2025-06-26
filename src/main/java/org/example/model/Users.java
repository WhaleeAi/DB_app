package org.example.model;

import java.sql.*;
import java.util.*;
import java.util.Observable;   // <—- Java 9+: добавьте --add-opens при запуске
import java.util.Observer;

public class Users extends Observable implements Observer {

    /* ---------- Singleton ---------- */
    private static final Users INSTANCE = new Users();

    private Users() { loadUsers(); }

    /* ---------- Данные в памяти ---------- */
    private final List<User> cache = new ArrayList<>();

    public List<User> getAllUsers() { return Collections.unmodifiableList(cache); }

    /* ---------- Загрузка всего списка ---------- */
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
            /* событие RELOAD */
            setChanged();
            notifyObservers(new RepoEvent<>(Type.RELOAD, null));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    /* ---------- CRUD ---------- */
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
            /* событие ADD */
            setChanged();
            notifyObservers(new RepoEvent<>(Type.ADD, u));

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

            /* заменяем объект в кеше */
            for (int i = 0; i < cache.size(); i++)
                if (cache.get(i).getId() == u.getId()) { cache.set(i, u); break; }

            /* событие UPDATE */
            setChanged();
            notifyObservers(new RepoEvent<>(Type.UPDATE, u));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            cache.removeIf(u -> u.getId() == id);
            /* событие DELETE */
            setChanged();
            notifyObservers(new RepoEvent<>(Type.DELETE, id));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    /* ---------- Observer ---------- */
    @Override public void update(Observable src, Object arg) {
        /* Если когда-нибудь User станет Observable, проксируем его события */
        setChanged();
        notifyObservers(arg);  // пересылаем наверх
    }

    /* ---------- Вспомогательное ---------- */
    public enum Type { ADD, UPDATE, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) { }

    public static Users getInstance() { return INSTANCE; }
}
