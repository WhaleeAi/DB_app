package org.example.model;

import java.math.BigDecimal;

public class TransactionProduct {
    private int id;
    private int productId;
    private int transactionId;
    private BigDecimal purchasePrice;
    private int quantity;

    public TransactionProduct() { }

    public TransactionProduct(int id, int productId, int transactionId, BigDecimal purchasePrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.transactionId = transactionId;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
