package org.example.model;

import java.time.LocalDate;

/**
 * Модель одного заказа (transaction).
 * Используется как «черновик» (status = "DRAFT") до момента покупки
 * и как финальный заказ (status = "DONE") после оплаты.
 */
public class Transaction {

    private int id;                     // PK
    private LocalDate date;             // дата оформления (null, пока черновик)
    private int totalQuantity;          // общее количество всех позиций
    private double totalSum;            // итоговая сумма со скидкой
    private String status;              // "DRAFT" | "DONE"

    /* ---------- конструкторы ---------- */
    public Transaction() { }

    public Transaction(LocalDate date, int totalQuantity,
                       double totalSum, String status) {
        this.date = date;
        this.totalQuantity = totalQuantity;
        this.totalSum = totalSum;
        this.status = status;
    }

    /* ---------- getters ---------- */
    public int        getId()            { return id; }
    public LocalDate  getDate()          { return date; }
    public int        getTotalQuantity() { return totalQuantity; }
    public double     getTotalSum()      { return totalSum; }
    public String     getStatus()        { return status; }

    /* ---------- setters ---------- */
    public void setId(int id)                         { this.id = id; }
    public void setDate(LocalDate date)               { this.date = date; }
    public void setTotalQuantity(int totalQuantity)   { this.totalQuantity = totalQuantity; }
    public void setTotalSum(double totalSum)          { this.totalSum = totalSum; }
    public void setStatus(String status)              { this.status = status; }

    /* ---------- удобный toString() ---------- */
    @Override public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", totalQuantity=" + totalQuantity +
                ", totalSum=" + totalSum +
                ", status='" + status + '\'' +
                '}';
    }
}
