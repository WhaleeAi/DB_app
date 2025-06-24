package org.example.model;

import java.sql.*;
import java.util.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Кэш всех товаров из таблицы product.
 */
public final class Products extends Observable implements Observer {

    private static final Products INSTANCE = new Products();
    public static Products getInstance() { return INSTANCE; }

    private final List<Product> cache = new ArrayList<>();

    private Products() { loadProducts(); }

    /* ---------------- PUBLIC API ---------------- */

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(cache);
    }

    /* если нужно добавить/обновить товар из админ-панели */
    public void addOrUpdateProduct(Product p) {
        if (p.getId() == 0) insert(p); else update(p);
    }

    /* ---------------- JDBC helpers --------------- */

    private void loadProducts() {
        cache.clear();
        String sql = "SELECT id, name, retail_price, wholesale_price FROM product";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             Statement  st = c.createStatement();
             ResultSet  rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setRetailPrice(rs.getDouble("retail_price"));
                p.setWholesalePrice(rs.getDouble("wholesale_price"));
                cache.add(p);
            }
            setChanged();
            notifyObservers(new RepoEvent<>(Type.RELOAD, null));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void insert(Product p) {
        String sql = "INSERT INTO product (name, retail_price, wholesale_price) VALUES (?,?,?)";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getName());
            ps.setDouble(2, p.getRetailPrice());
            ps.setDouble(3, p.getWholesalePrice());
            ps.executeUpdate();

            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) p.setId(k.getInt(1)); }
            cache.add(p);

            setChanged();
            notifyObservers(new RepoEvent<>(Type.ADD, p));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void update(Product p) {
        String sql = "UPDATE product SET name=?, retail_price=?, wholesale_price=? WHERE id=?";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setDouble(2, p.getRetailPrice());
            ps.setDouble(3, p.getWholesalePrice());
            ps.setInt   (4, p.getId());
            ps.executeUpdate();

            for (int i=0;i<cache.size();i++)
                if (cache.get(i).getId()==p.getId()) { cache.set(i,p); break; }

            setChanged();
            notifyObservers(new RepoEvent<>(Type.UPDATE, p));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    /* --------------- Observer proxy -------------- */
    @Override public void update(Observable o, Object arg) {
        setChanged(); notifyObservers(arg);        // если Product станет Observable
    }

    /* --------------- helper record --------------- */
    public enum Type { ADD, UPDATE, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) { }
}

