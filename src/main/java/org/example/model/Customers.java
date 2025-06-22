package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Customers {
    private static Customers instance;
    private List<Customer> customerList = null;

    private Customers() { }

    public static Customers getInstance() {
        if (instance == null) {
            instance = new Customers();
        }
        return instance;
    }

    public List<Customer> getAllCustomers() {
        if (customerList == null) {
            loadCustomers();
        }
        return customerList;
    }

    private void loadCustomers() {
        customerList = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                customer.setCompanyName(rs.getString("company_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setContactPerson(rs.getString("contact_person"));
                customerList.add(customer);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(Customer customer) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO customer (user_id, company_name, phone, address, contact_person) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setObject(1, customer.getUserId());
            pstmt.setString(2, customer.getCompanyName());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getContactPerson());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                customer.setId(rs.getInt(1));
            }
            rs.close();
            pstmt.close();
            if (customerList != null) {
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomer(Customer customer) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE customer SET user_id = ?, company_name = ?, phone = ?, address = ?, contact_person = ? WHERE id = ?"
            );
            pstmt.setObject(1, customer.getUserId());
            pstmt.setString(2, customer.getCompanyName());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getContactPerson());
            pstmt.setInt(6, customer.getId());
            pstmt.executeUpdate();
            pstmt.close();
            if (customerList != null) {
                for (int i = 0; i < customerList.size(); i++) {
                    if (customerList.get(i).getId() == customer.getId()) {
                        customerList.set(i, customer);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(int id) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM customer WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            if (customerList != null) {
                customerList.removeIf(customer -> customer.getId() == id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerById(int id) {
        if (customerList == null) {
            loadCustomers();
        }
        for (Customer customer : customerList) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    public void refresh() {
        customerList = null;
        loadCustomers();
    }
}