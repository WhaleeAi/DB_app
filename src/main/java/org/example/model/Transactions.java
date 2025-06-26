package org.example.model;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public final class Transactions {

    private static final Transactions INSTANCE = new Transactions();
    public static Transactions getInstance() { return INSTANCE; }

    private final List<Transaction> cache = new ArrayList<>();

    private Transactions() { }

    private void loadTransactions(Integer customerId) {
        cache.clear();
        String sql = "SELECT id, customer_id, transaction_date, total_quantity, total_amount, status FROM transactions";
        if (customerId != null) sql += " WHERE customer_id=?";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement st = c.prepareStatement(sql)) {

            if (customerId != null) st.setInt(1, customerId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setId(rs.getInt("id"));
                    t.setCustomerId(rs.getInt("customer_id"));
                    Date d = rs.getDate("transaction_date");
                    if (d != null) t.setDate(d.toLocalDate());
                    t.setTotalQuantity(rs.getInt("total_quantity"));
                    t.setTotalSum(rs.getDouble("total_amount"));
                    t.setStatus(rs.getString("status"));
                    cache.add(t);
                }
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Transaction> getAllTransactions() {
        loadTransactions(null);
        return Collections.unmodifiableList(cache);
    }

    public List<Transaction> getTransactionsByCustomer(int customerId) {
        loadTransactions(customerId);
        return Collections.unmodifiableList(cache);
    }

    public int createDraft(int customerId) {
        String sql = """
    INSERT INTO transactions
        (customer_id, total_quantity, transaction_date, total_amount, status)
        VALUES (?, 0, ?, 0, 'DRAFT')
    """;

        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt  (1, customerId);
            ps.setDate (2, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) {
                if (k.next()) {
                    int id = k.getInt(1);
                    Transaction t = new Transaction();
                    t.setId(id);
                    t.setCustomerId(customerId);
                    t.setStatus("DRAFT");
                    cache.add(t);
                    return id;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public void finalizeDraft(int id, int qty, double sum, LocalDate date) {
        String sql = """
            UPDATE transactions
               SET total_quantity=?, total_amount=?, transaction_date=?, status='DONE'
             WHERE id=?""";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt   (1, qty);
            ps.setDouble(2, sum);
            ps.setDate  (3, Date.valueOf(date));
            ps.setInt   (4, id);
            ps.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }

        for (Transaction t : cache) {
            if (t.getId() == id) {
                t.setTotalQuantity(qty);
                t.setTotalSum(sum);
                t.setDate(date);
                t.setStatus("DONE");
                break;
            }
        }
    }
}
