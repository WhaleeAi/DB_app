package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Products {
    private static Products instance;
    private List<Product> productList = null;

    private Products() { }

    public static Products getInstance() {
        if (instance == null) {
            instance = new Products();
        }
        return instance;
    }

    public List<Product> getAllProducts() {
        if (productList == null) {
            loadProducts();
        }
        return productList;
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM product");
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setRetailPrice(rs.getBigDecimal("retail_price"));
                product.setWholesalePrice(rs.getBigDecimal("wholesale_price"));
                product.setDescription(rs.getString("description"));
                productList.add(product);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO product (name, retail_price, wholesale_price, description) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, product.getName());
            pstmt.setBigDecimal(2, product.getRetailPrice());
            pstmt.setBigDecimal(3, product.getWholesalePrice());
            pstmt.setString(4, product.getDescription());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
            rs.close();
            pstmt.close();
            if (productList != null) {
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE product SET name = ?, retail_price = ?, wholesale_price = ?, description = ? WHERE id = ?"
            );
            pstmt.setString(1, product.getName());
            pstmt.setBigDecimal(2, product.getRetailPrice());
            pstmt.setBigDecimal(3, product.getWholesalePrice());
            pstmt.setString(4, product.getDescription());
            pstmt.setInt(5, product.getId());
            pstmt.executeUpdate();
            pstmt.close();
            if (productList != null) {
                for (int i = 0; i < productList.size(); i++) {
                    if (productList.get(i).getId() == product.getId()) {
                        productList.set(i, product);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM product WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            if (productList != null) {
                productList.removeIf(product -> product.getId() == id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductById(int id) {
        if (productList == null) {
            loadProducts();
        }
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public void refresh() {
        productList = null;
        loadProducts();
    }
}
