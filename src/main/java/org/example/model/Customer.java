package org.example.model;

public class Customer {
    private int id;
    private Integer userId;
    private String companyName;
    private String phone;
    private String address;
    private String contactPerson;

    public Customer() { }

    public Customer(int id, Integer userId, String companyName, String phone, String address, String contactPerson) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.phone = phone;
        this.address = address;
        this.contactPerson = contactPerson;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
}
