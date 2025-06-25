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

    private void loadTransactions() {
        cache.clear();
        String sql = "SELECT id, transaction_date, total_quantity, total_amount, status FROM transactions";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                Date d = rs.getDate("transaction_date");
                if (d != null) t.setDate(d.toLocalDate());
                t.setTotalQuantity(rs.getInt("total_quantity"));
                t.setTotalSum(rs.getDouble("total_amount"));
                t.setStatus(rs.getString("status"));
                cache.add(t);
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Transaction> getAllTransactions() {
        loadTransactions();
        return Collections.unmodifiableList(cache);
    }

    /** создаём «черновик» (status=DRAFT) — возвращаем id */
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
                    t.setStatus("DRAFT");
                    cache.add(t);
                    return id;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** финализируем заказ: проставляем qty, sum, дату, статус DONE */
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
    }
}
