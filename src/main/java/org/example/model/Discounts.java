package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Discounts {
    private static Discounts instance;
    private List<Discount> discountList = null;

    private Discounts() { }

    public static Discounts getInstance() {
        if (instance == null) {
            instance = new Discounts();
        }
        return instance;
    }

    private void loadDiscounts() {
        discountList = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM discount");
            while (rs.next()) {
                Discount discount = new Discount();
                discount.setId(rs.getInt("id"));
                discount.setMinQuantity(rs.getInt("min_quantity"));
                discount.setMinAmount(rs.getBigDecimal("min_amount"));
                discount.setPercentage(rs.getBigDecimal("percentage"));
                discountList.add(discount);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDiscount(Discount discount) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO discount (min_quantity, min_amount, percentage) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setInt(1, discount.getMinQuantity());
            pstmt.setBigDecimal(2, discount.getMinAmount());
            pstmt.setBigDecimal(3, discount.getPercentage());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                discount.setId(rs.getInt(1));
            }
            rs.close();
            pstmt.close();
            if (discountList != null) {
                discountList.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDiscount(Discount discount) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE discount SET min_quantity = ?, min_amount = ?, percentage = ? WHERE id = ?"
            );
            pstmt.setInt(1, discount.getMinQuantity());
            pstmt.setBigDecimal(2, discount.getMinAmount());
            pstmt.setBigDecimal(3, discount.getPercentage());
            pstmt.setInt(4, discount.getId());
            pstmt.executeUpdate();
            pstmt.close();
            if (discountList != null) {
                for (int i = 0; i < discountList.size(); i++) {
                    if (discountList.get(i).getId() == discount.getId()) {
                        discountList.set(i, discount);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDiscount(int id) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM discount WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            if (discountList != null) {
                discountList.removeIf(discount -> discount.getId() == id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Discount getDiscountById(int id) {
        if (discountList == null) {
            loadDiscounts();
        }
        for (Discount discount : discountList) {
            if (discount.getId() == id) {
                return discount;
            }
        }
        return null;
    }

    public void refresh() {
        discountList = null;
        loadDiscounts();
    }
}