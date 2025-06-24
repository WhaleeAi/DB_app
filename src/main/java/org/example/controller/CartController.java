package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.CartView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CartController {

    private static CartController INSTANCE;
    public  static CartController getInstance(){ return INSTANCE; }

    private final Stage stage;
    private final CartView view;

    private final TransactionProducts cartRepo = TransactionProducts.getInstance();
    private final Transactions        transRepo= Transactions.getInstance();

    public CartController(Stage stage){
        INSTANCE = this;
        this.stage = stage;
        this.view  = new CartView();

        initData();
        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    /* =============== DATA =============== */
    private void initData(){
        view.table().setItems(
                FXCollections.observableArrayList(cartRepo.getAll()));

        recalcTotals();
        /* слушаем изменения корзины */
        cartRepo.addObserver((o,arg)->{
            view.table().setItems(
                    FXCollections.observableArrayList(cartRepo.getAll()));
            recalcTotals();
        });
    }

    /* ============= HANDLERS ============= */
    private void bindHandlers(){
        view.btnProducts().setOnAction(e -> new ProductsController(stage));
        view.buyButton().setOnAction(e -> handleBuy());
    }

    public void removeItem(TransactionProduct tp){
        cartRepo.removeByProduct(tp.getProductId());
    }

    /* -------- BUY -------- */
    private void handleBuy(){
        List<TransactionProduct> items = cartRepo.getAll();
        if (items.isEmpty()) return;

        int totalQty = items.stream().mapToInt (TransactionProduct::getQuantity).sum();
        double sum   = items.stream().mapToDouble(TransactionProduct::getTotalPrice).sum();

        /* --- скидка по таблице discount --- */
        double discount = getDiscount(totalQty, sum);  // локальный приватный метод

        double finalSum = sum - discount;

        Transaction t = new Transaction();
        t.setTransactionDate(LocalDate.now());
        t.setTotalQuantity(totalQty);
        t.setTotalAmount(finalSum);
        transRepo.addTransaction(t);          // INSERT + notify

        cartRepo.clear();                     // очистить корзину
    }

    /* -------- totals -------- */
    private void recalcTotals() {
        int totalQty = cartRepo.getAll().stream().mapToInt(TransactionProduct::getQuantity).sum();
        double totalSum = cartRepo.getAll().stream().mapToDouble(TransactionProduct::getTotalPrice).sum();

        view.totalQtyLbl().setText(String.valueOf(totalQty));
        view.totalSumLbl().setText(String.format("%.2f", totalSum));
    }


    /** Возвращает абсолютную сумму скидки по таблице discount */
    private double getDiscount(int qty, double sum) {
        String sql = """
        SELECT percent
          FROM discount
         WHERE min_qty <= ? AND ? <= max_qty
         LIMIT 1""";

        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setInt(2, qty);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double percent = rs.getDouble("percent");   // например 10 → 10 %
                    return sum * percent / 100.0;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0.0;         // скидка не предусмотрена
    }

}
