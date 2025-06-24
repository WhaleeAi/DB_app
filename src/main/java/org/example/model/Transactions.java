package org.example.model;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.Observable;
import java.util.Observer;

public final class Transactions extends Observable implements Observer {

    private static final Transactions INSTANCE = new Transactions();
    public static Transactions getInstance() { return INSTANCE; }

    private final List<Transaction> cache = new ArrayList<>();

    private Transactions() { loadHistory(); }

    /* ---------------- PUBLIC ---------------- */

    public List<Transaction> getAll() { return Collections.unmodifiableList(cache); }

    public void addTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (date, total_qty, total_sum) VALUES (?,?,?)";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate (1, Date.valueOf(t.getTransactionDate()));
            ps.setInt  (2, t.getTotalQuantity());
            ps.setDouble(3, t.getTotalAmount());
            ps.executeUpdate();

            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) t.setId(k.getInt(1)); }
            cache.add(t);

            setChanged(); notifyObservers(new RepoEvent<>(Type.ADD,t));

        } catch (SQLException e){ e.printStackTrace();}
    }

    /* ---------------- JDBC ---------------- */
    private void loadHistory() {
        cache.clear();
        String sql = "SELECT id, date, total_qty, total_sum FROM transactions ORDER BY date DESC";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()){
                Transaction t = new Transaction();
                t.setId            (rs.getInt   ("id"));
                t.setTransactionDate          (rs.getDate  ("date").toLocalDate());
                t.setTotalQuantity (rs.getInt   ("total_qty"));
                t.setTotalAmount      (rs.getDouble("total_sum"));
                cache.add(t);
            }
            setChanged(); notifyObservers(new RepoEvent<>(Type.RELOAD,null));
        } catch (SQLException e){ e.printStackTrace();}
    }

    /* ---------- proxy ---------- */
    @Override public void update(Observable o,Object arg){
        setChanged(); notifyObservers(arg);
    }

    public enum Type { ADD, UPDATE, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) { }
}
