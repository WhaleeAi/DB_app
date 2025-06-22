package org.example.model;

import java.math.BigDecimal;

public class Discount {
    private int id;
    private int minQuantity;
    private BigDecimal minAmount;
    private BigDecimal percentage;

    public Discount() { }

    public Discount(int id, int minQuantity, BigDecimal minAmount, BigDecimal percentage) {
        this.id = id;
        this.minQuantity = minQuantity;
        this.minAmount = minAmount;
        this.percentage = percentage;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMinQuantity() { return minQuantity; }
    public void setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; }

    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }

    public BigDecimal getPercentage() { return percentage; }
    public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }
}