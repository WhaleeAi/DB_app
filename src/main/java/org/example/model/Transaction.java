package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int id;
    private Integer discountId;
    private int customerId;
    private int totalQuantity;
    private LocalDate transactionDate;
    private BigDecimal totalAmount;

    public Transaction() { }

    public Transaction(int id, Integer discountId, int customerId, int totalQuantity, LocalDate transactionDate, BigDecimal totalAmount) {
        this.id = id;
        this.discountId = discountId;
        this.customerId = customerId;
        this.totalQuantity = totalQuantity;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getDiscountId() { return discountId; }
    public void setDiscountId(Integer discountId) { this.discountId = discountId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}