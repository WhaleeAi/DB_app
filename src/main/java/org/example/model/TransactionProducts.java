package org.example.model;

import java.sql.*;
import java.util.*;
import java.util.Observable;

public final class TransactionProducts extends Observable {

    private static final TransactionProducts INSTANCE = new TransactionProducts();
    public static TransactionProducts getInstance() { return INSTANCE; }

    private final List<TransactionProduct> cache = new ArrayList<>();

    private TransactionProducts() { }

    public List<TransactionProduct> getAll() { return cache; }

    public List<TransactionProduct> getByTransaction(int txId) {
        return cache.stream().filter(tp -> tp.getTransactionId() == txId).toList();
    }

    public List<TransactionProduct> loadFromDb(int txId) {
        List<TransactionProduct> list = new ArrayList<>();

        String sql = """
            SELECT tp.id, tp.product_id, tp.quantity,
              tp.purchase_price, tp.transaction_id, tp.total_sum,
              p.name
              FROM transaction_products tp
              JOIN product p ON tp.product_id = p.id
             WHERE tp.transaction_id=?""";

        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, txId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TransactionProduct tp = new TransactionProduct(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getDouble("purchase_price")
                    );
                    tp.setId(rs.getInt("id"));
                    tp.setTransactionId(txId);
                    list.add(tp);
                }
            }

        } catch (SQLException e) { e.printStackTrace(); }

        cache.removeIf(tp -> tp.getTransactionId() == txId);
        cache.addAll(list);
        return list;
    }

    public void insert(int txId, Product p, int qty) {
        double unit = (qty > 5 ? p.getWholesalePrice() : p.getRetailPrice());
        double sum  = unit * qty;

        String sql = """
            INSERT INTO transaction_products
            (transaction_id, product_id, quantity, purchase_price, total_sum)
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

            setChanged();
            notifyObservers(new RepoEvent<>(Type.ADD, tp));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void remove(int txId, int productId) {
        cache.removeIf(tp -> tp.getTransactionId() == txId && tp.getProductId() == productId);

        String sql = "DELETE FROM transaction_products WHERE transaction_id=? AND product_id=?";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, txId); ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }

        setChanged();
        notifyObservers(new RepoEvent<>(Type.DELETE, productId));
    }

    public void clearByTransaction(int txId) {
        cache.removeIf(tp -> tp.getTransactionId() == txId);

        setChanged();
        notifyObservers(new RepoEvent<>(Type.RELOAD, null));
    }

    public enum Type { ADD, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) {}
}


