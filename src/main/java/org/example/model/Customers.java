package org.example.model;

import java.sql.*;
import java.util.*;
import java.util.Observable;
import java.util.Observer;

public class Customers extends Observable implements Observer {

    private static final Customers INSTANCE = new Customers();
    public static Customers getInstance() { return INSTANCE; }
    private Customers() { loadCustomers(); }

    private final List<Customer> cache = new ArrayList<>();
    public List<Customer> getAllCustomers() { return Collections.unmodifiableList(cache); }


    private void loadCustomers() {
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
                cache.add(customer);
            }
            rs.close();
            stmt.close();

            setChanged();
            notifyObservers(new Users.RepoEvent<>(Users.Type.RELOAD, null));
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
            if (cache != null) {
                cache.add(customer);
            }

            setChanged();
            notifyObservers(new Users.RepoEvent<>(Users.Type.RELOAD, null));
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
            if (cache != null) {
                for (int i = 0; i < cache.size(); i++) {
                    if (cache.get(i).getId() == customer.getId()) {
                        cache.set(i, customer);
                        break;
                    }
                }
            }

            setChanged();
            notifyObservers(new Users.RepoEvent<>(Users.Type.RELOAD, null));
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
            if (cache != null) {
                cache.removeIf(customer -> customer.getId() == id);
            }

            setChanged();
            notifyObservers(new Users.RepoEvent<>(Users.Type.RELOAD, null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerById(int id) {
        if (cache == null) {
            loadCustomers();
        }
        for (Customer customer : cache) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    public Customer getByUserId(int userId) {
        if (cache == null) { loadCustomers(); }
        for (Customer c : cache) {
            if (c.getUserId() != null && c.getUserId() == userId) {
                return c;
            }
        }
        return null;
    }


    @Override public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public enum Type { ADD, UPDATE, DELETE, RELOAD }
    public record RepoEvent<T>(Type type, T payload) { }
}