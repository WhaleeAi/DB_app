package org.example.model;

import java.time.LocalDate;

public class Transaction {

    private int id;
    private Integer customerId;
    private LocalDate date;
    private int totalQuantity;
    private double totalSum;
    private String status;

    public Transaction() { }

    public Transaction(LocalDate date, int totalQuantity,
                       double totalSum, String status) {
        this.date = date;
        this.totalQuantity = totalQuantity;
        this.totalSum = totalSum;
        this.status = status;
    }

    public int getId() { return id; }
    public Integer getCustomerId() { return customerId; }
    public LocalDate getDate() { return date; }
    public int getTotalQuantity() { return totalQuantity; }
    public double getTotalSum() { return totalSum; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public void setTotalSum(double totalSum) { this.totalSum = totalSum; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", date=" + date +
                ", totalQuantity=" + totalQuantity +
                ", totalSum=" + totalSum +
                ", status='" + status + '\'' +
                '}';
    }
}
