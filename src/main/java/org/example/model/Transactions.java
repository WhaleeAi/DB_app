package org.example.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Transactions {
    private static Transactions instance;
    private List<Transaction> transactionList = null;

    private Transactions() { }

    public static Transactions getInstance() {
        if (instance == null) {
            instance = new Transactions();
        }
        return instance;
    }

    public List<Transaction> getAllTransactions() {
        if (transactionList == null) {
            loadTransactions();
        }
        return transactionList;
    }

    private void loadTransactions() {
        transactionList = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transaction");
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setDiscountId(rs.getObject("discount_id") != null ? rs.getInt("discount_id") : null);
                transaction.setCustomerId(rs.getInt("customer_id"));
                transaction.setTotalQuantity(rs.getInt("total_quantity"));
                transaction.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
                transaction.setTotalAmount(rs.getBigDecimal("total_amount"));
                transactionList.add(transaction);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(Transaction transaction) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO transaction (discount_id, customer_id, total_quantity, transaction_date, total_amount) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setObject(1, transaction.getDiscountId());
            pstmt.setInt(2, transaction.getCustomerId());
            pstmt.setInt(3, transaction.getTotalQuantity());
            pstmt.setDate(4, Date.valueOf(transaction.getTransactionDate()));
            pstmt.setBigDecimal(5, transaction.getTotalAmount());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setId(rs.getInt(1));
            }
            rs.close();
            pstmt.close();
            if (transactionList != null) {
                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransaction(Transaction transaction) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE transaction SET discount_id = ?, customer_id = ?, total_quantity = ?, transaction_date = ?, total_amount = ? WHERE id = ?"
            );
            pstmt.setObject(1, transaction.getDiscountId());
            pstmt.setInt(2, transaction.getCustomerId());
            pstmt.setInt(3, transaction.getTotalQuantity());
            pstmt.setDate(4, Date.valueOf(transaction.getTransactionDate()));
            pstmt.setBigDecimal(5, transaction.getTotalAmount());
            pstmt.setInt(6, transaction.getId());
            pstmt.executeUpdate();
            pstmt.close();
            if (transactionList != null) {
                for (int i = 0; i < transactionList.size(); i++) {
                    if (transactionList.get(i).getId() == transaction.getId()) {
                        transactionList.set(i, transaction);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(int id) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM transaction WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            if (transactionList != null) {
                transactionList.removeIf(transaction -> transaction.getId() == id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Transaction getTransactionById(int id) {
        if (transactionList == null) {
            loadTransactions();
        }
        for (Transaction transaction : transactionList) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }
        return null;
    }

    public void refresh() {
        transactionList = null;
        loadTransactions();
    }
}