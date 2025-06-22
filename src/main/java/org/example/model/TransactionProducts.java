package org.example.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionProducts {
    private static TransactionProducts instance;
    private List<TransactionProduct> transactionProductList = null;

    private TransactionProducts() { }

    public static TransactionProducts getInstance() {
        if (instance == null) {
            instance = new TransactionProducts();
        }
        return instance;
    }

    public List<TransactionProduct> getAllTransactionItems() {
        if (transactionProductList == null) {
            loadTransactionProducts();
        }
        return transactionProductList;
    }

    private void loadTransactionProducts() {
        transactionProductList = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transactionitem");
            while (rs.next()) {
                TransactionProduct item = new TransactionProduct();
                item.setId(rs.getInt("id"));
                item.setProductId(rs.getInt("product_id"));
                item.setTransactionId(rs.getInt("transaction_id"));
                item.setPurchasePrice(rs.getBigDecimal("purchase_price"));
                item.setQuantity(rs.getInt("quantity"));
                transactionProductList.add(item);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTransactionItem(TransactionProduct item) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO transactionitem (product_id, transaction_id, purchase_price, quantity) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setInt(1, item.getProductId());
            pstmt.setInt(2, item.getTransactionId());
            pstmt.setBigDecimal(3, item.getPurchasePrice());
            pstmt.setInt(4, item.getQuantity());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                item.setId(rs.getInt(1));
            }
            rs.close();
            pstmt.close();
            if (transactionProductList != null) {
                transactionProductList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransactionItem(TransactionProduct item) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE transactionitem SET product_id = ?, transaction_id = ?, purchase_price = ?, quantity = ? WHERE id = ?"
            );
            pstmt.setInt(1, item.getProductId());
            pstmt.setInt(2, item.getTransactionId());
            pstmt.setBigDecimal(3, item.getPurchasePrice());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setInt(5, item.getId());
            pstmt.executeUpdate();
            pstmt.close();
            if (transactionProductList != null) {
                for (int i = 0; i < transactionProductList.size(); i++) {
                    if (transactionProductList.get(i).getId() == item.getId()) {
                        transactionProductList.set(i, item);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransactionItem(int id) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM transactionitem WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            if (transactionProductList != null) {
                transactionProductList.removeIf(item -> item.getId() == id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TransactionProduct getTransactionItemById(int id) {
        if (transactionProductList == null) {
            loadTransactionProducts();
        }
        for (TransactionProduct item : transactionProductList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void refresh() {
        transactionProductList = null;
        loadTransactionProducts();
    }
}
