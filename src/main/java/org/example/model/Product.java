package org.example.model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private double retailPrice;
    private double wholesalePrice;
    private String description;

    public Product() { }

    public Product(int id, String name, double retailPrice, double wholesalePrice, String description) {
        this.id = id;
        this.name = name;
        this.retailPrice = retailPrice;
        this.wholesalePrice = wholesalePrice;
        this.description = description;
    }

    // --- Геттеры и сеттеры ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getRetailPrice() { return retailPrice; }
    public void setRetailPrice(double retailPrice) { this.retailPrice = retailPrice; }

    public double getWholesalePrice() { return wholesalePrice; }
    public void setWholesalePrice(double wholesalePrice) { this.wholesalePrice = wholesalePrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

