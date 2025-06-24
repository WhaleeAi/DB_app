package org.example.model;

import java.time.LocalDate;

public class TransactionProduct {

    private int       id;
    private int       productId;
    private String    productName;
    private int       quantity;
    private double    unitPrice;
    private double    totalPrice;
    private int       transactionId;
    private LocalDate createdDate;

    /* ---------- конструкторы ---------- */
    public TransactionProduct() { }

    public TransactionProduct(int productId, String productName,
                              int quantity, double unitPrice) {
        this.productId   = productId;
        this.productName = productName;
        setQuantity(quantity);        // посчитает totalPrice
        setUnitPrice(unitPrice);      // скорректирует totalPrice
    }

    /* ---------- getters ---------- */
    public int       getId()            { return id; }
    public int       getProductId()     { return productId; }
    public String    getProductName()   { return productName; }
    public int       getQuantity()      { return quantity; }
    public double    getUnitPrice()     { return unitPrice; }
    public double    getTotalPrice()    { return totalPrice; }
    public int       getTransactionId() { return transactionId; }
    public LocalDate getCreatedDate()   { return createdDate; }

    /* ---------- setters ---------- */
    public void setId(int id) { this.id = id; }

    public void setProductId(int productId) { this.productId = productId; }

    public void setProductName(String productName) { this.productName = productName; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        recalcTotal();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        recalcTotal();
    }

    public void setTotalPrice(double totalPrice) {   // нужен для ResultSet-mapper
        this.totalPrice = totalPrice;
    }

    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    /* ---------- util ---------- */
    private void recalcTotal() {
        this.totalPrice = unitPrice * quantity;
    }
}
