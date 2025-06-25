package org.example.model;

import java.time.LocalDate;

/** Одна позиция в заказе/черновике. */
public class TransactionProduct {

    private int       id;
    private int       productId;
    private String    productName;
    private int       quantity;
    private double    unitPrice;
    private double    totalPrice;
    private int       transactionId;   // 0 – ещё не привязан
    private LocalDate createdDate;

    public TransactionProduct() { }

    public TransactionProduct(int productId, String productName,
                              int quantity, double unitPrice) {
        this.productId   = productId;
        this.productName = productName;
        setQuantity(quantity);
        setUnitPrice(unitPrice);
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
    public void setId(int id)                     { this.id = id; }
    public void setProductId(int productId)       { this.productId = productId; }
    public void setProductName(String productName){ this.productName = productName; }
    public void setTransactionId(int txId)        { this.transactionId = txId; }
    public void setCreatedDate(LocalDate d)       { this.createdDate = d; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        recalcTotal();
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        recalcTotal();
    }
    /** нужен при загрузке из ResultSet */
    public void setTotalPrice(double totalPrice)  { this.totalPrice = totalPrice; }

    private void recalcTotal() { this.totalPrice = unitPrice * quantity; }
}
