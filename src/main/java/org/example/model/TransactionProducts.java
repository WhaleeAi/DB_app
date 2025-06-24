package org.example.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Корзина текущего клиента: позиции из transaction_products
 * со status = 'CART' (или transaction_id IS NULL – как удобно).
 */
public final class TransactionProducts extends Observable implements Observer {

    private static final TransactionProducts INSTANCE = new TransactionProducts();
    public static TransactionProducts getInstance() { return INSTANCE; }

    private final List<TransactionProduct> cache = new ArrayList<>();

    private TransactionProducts() { loadCart(); }

    /* ---------------- PUBLIC ---------------- */

    public List<TransactionProduct> getAll() {
        return Collections.unmodifiableList(cache);
    }

    /** +1 / -1 / любое qty. Если qty==0 → удаляем. */
    public void addOrUpdate(Product p, int qty) {
        double unit = (qty > 5) ? p.getWholesalePrice() : p.getRetailPrice();

        TransactionProduct tp = cache.stream()
                .filter(t -> t.getProductId() == p.getId())
                .findFirst()
                .orElse(null);

        if (tp == null) {
            tp = new TransactionProduct(p.getId(), p.getName(), qty, unit);
            insert(tp);           // ваш метод INSERT в БД + cache.add(tp) + notify
        } else {
            tp.setQuantity(qty);
            tp.setUnitPrice(unit);
            update(tp);           // ваш метод UPDATE в БД + notify
        }
    }


    public void removeByProduct(int productId) {
        cache.stream()
                .filter(tp -> tp.getProductId()==productId)
                .findFirst()
                .ifPresent(this::delete);
    }

    public void clear() {
        String sql = "DELETE FROM transaction_products WHERE status='CART'";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeUpdate();
            cache.clear();
            setChanged(); notifyObservers(new RepoEvent<>(Type.RELOAD,null));
        } catch (SQLException e){ e.printStackTrace();}
    }

    /* ---------------- JDBC ---------------- */

    private void loadCart() {
        cache.clear();
        String sql = """
            SELECT tp.product_id, p.name, tp.quantity,
                   tp.unit_price, tp.total_price
              FROM transaction_products tp
              JOIN product p ON p.id = tp.product_id
             WHERE tp.status = 'CART'""";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                TransactionProduct tp = new TransactionProduct();
                tp.setProductId (rs.getInt("product_id"));
                tp.setProductName(rs.getString("name"));
                tp.setQuantity  (rs.getInt("quantity"));
                tp.setUnitPrice (rs.getDouble("unit_price"));
                tp.setTotalPrice(rs.getDouble("total_price"));
                cache.add(tp);
            }
            setChanged(); notifyObservers(new RepoEvent<>(Type.RELOAD,null));
        } catch (SQLException e){ e.printStackTrace(); }
    }

    private void insert(TransactionProduct tp){
        String sql = """
            INSERT INTO transaction_products
            (product_id, quantity, unit_price, total_price, status)
            VALUES (?,?,?,?, 'CART')""";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt   (1, tp.getProductId());
            ps.setInt   (2, tp.getQuantity());
            ps.setDouble(3, tp.getUnitPrice());
            ps.setDouble(4, tp.getTotalPrice());
            ps.executeUpdate();

            cache.add(tp);
            setChanged(); notifyObservers(new RepoEvent<>(Type.ADD,tp));

        } catch (SQLException e){ e.printStackTrace();}
    }

    private void update(TransactionProduct tp){
        String sql = """
            UPDATE transaction_products
               SET quantity=?, unit_price=?, total_price=?
             WHERE product_id=? AND status='CART'""";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt   (1, tp.getQuantity());
            ps.setDouble(2, tp.getUnitPrice());
            ps.setDouble(3, tp.getTotalPrice());
            ps.setInt   (4, tp.getProductId());
            ps.executeUpdate();

            setChanged(); notifyObservers(new RepoEvent<>(Type.UPDATE,tp));

        } catch (SQLException e){ e.printStackTrace();}
    }

    private void delete(TransactionProduct tp){
        String sql = "DELETE FROM transaction_products WHERE product_id=? AND status='CART'";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, tp.getProductId());
            ps.executeUpdate();
            cache.remove(tp);
            setChanged(); notifyObservers(new RepoEvent<>(Type.DELETE,tp));
        } catch (SQLException e){ e.printStackTrace();}
    }

    /* --------------- proxy --------------- */
    @Override public void update(Observable o,Object arg){
        setChanged(); notifyObservers(arg);
    }

    public enum Type { ADD, UPDATE, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) {}
}

