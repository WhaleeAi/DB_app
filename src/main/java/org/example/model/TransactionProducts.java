package org.example.model;

import java.sql.*;
import java.util.*;
import java.util.Observable;

public final class TransactionProducts extends Observable {

    private static final TransactionProducts INSTANCE = new TransactionProducts();
    public static TransactionProducts getInstance() { return INSTANCE; }

    /* кеш всех позиций текущей сессии приложения */
    private final List<TransactionProduct> cache = new ArrayList<>();

    private TransactionProducts() { /* лениво; не грузим при старте */ }

    /* ---------- API ---------- */

    public List<TransactionProduct> getAll() { return Collections.unmodifiableList(cache); }

    public List<TransactionProduct> getByTransaction(int txId) {
        return cache.stream().filter(tp -> tp.getTransactionId() == txId).toList();
    }

    /** вставка НОВОЙ позиции под конкретный transaction_id */
    public void insert(int txId, Product p, int qty) {
        double unit = (qty > 5 ? p.getWholesalePrice() : p.getRetailPrice());
        double sum  = unit * qty;

        String sql = """
            INSERT INTO transaction_products
            (transaction_id, product_id, quantity, unit_price, total_price)
            VALUES (?,?,?,?,?)""";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt   (1, txId);
            ps.setInt   (2, p.getId());
            ps.setInt   (3, qty);
            ps.setDouble(4, unit);
            ps.setDouble(5, sum);
            ps.executeUpdate();

            int id = 0;
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) id = k.getInt(1); }

            TransactionProduct tp = new TransactionProduct(p.getId(), p.getName(), qty, unit);
            tp.setId(id);
            tp.setTransactionId(txId);
            cache.add(tp);

            setChanged(); notifyObservers(new RepoEvent<>(Type.ADD, tp));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** удалить одну позицию из корзины */
    public void remove(int txId, int productId) {
        cache.removeIf(tp -> tp.getTransactionId() == txId && tp.getProductId() == productId);

        String sql = "DELETE FROM transaction_products WHERE transaction_id=? AND product_id=?";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, txId); ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }

        setChanged(); notifyObservers(new RepoEvent<>(Type.DELETE, productId));
    }

    /** очистить корзину после покупки / отмены */
    public void clearByTransaction(int txId) {
        cache.removeIf(tp -> tp.getTransactionId() == txId);
        String sql = "DELETE FROM transaction_products WHERE transaction_id=?";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, txId); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        setChanged(); notifyObservers(new RepoEvent<>(Type.RELOAD, null));
    }

    /* ---------- служебное ---------- */
    public enum Type { ADD, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) {}
}


