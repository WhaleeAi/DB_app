package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.CartView;
import org.example.controller.HistoryController;

import java.time.LocalDate;
import java.util.List;

public class CartController {

    private static CartController INSTANCE;
    public  static CartController getInstance(){ return INSTANCE; }

    private final Stage stage;
    private final CartView view;

    private final TransactionProducts cartRepo = TransactionProducts.getInstance();
    private final Transactions        txRepo   = Transactions.getInstance();

    private final int draftId;   // привязка корзины к одному transaction_id

    public CartController(Stage stage, int draftId) {
        INSTANCE = this;
        this.stage   = stage;
        this.draftId = draftId;
        this.view    = new CartView();

        initData();
        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    private void initData() {
        view.table().setItems(FXCollections.observableArrayList(
                cartRepo.getByTransaction(draftId)));
        recalcTotals();

        /* слушаем изменения корзины */
        cartRepo.addObserver((o,a)->{
            view.table().setItems(FXCollections.observableArrayList(
                    cartRepo.getByTransaction(draftId)));
            recalcTotals();
        });
    }

    private void bindHandlers() {
        view.buyButton().setOnAction(e -> handleBuy());
        view.btnProducts().setOnAction(e -> new ProductsController(stage));
        view.btnHistory().setOnAction(e -> new HistoryController(stage));
        view.btnProfile().setOnAction(e -> new ProfileController(stage));
    }

    /* удалить одну позицию */
    public void removeItem(TransactionProduct tp){
        cartRepo.remove(draftId, tp.getProductId());
    }

    /* оформление покупки */
    private void handleBuy() {
        List<TransactionProduct> items = cartRepo.getByTransaction(draftId);
        if (items.isEmpty()) return;

        int totalQty = items.stream().mapToInt(TransactionProduct::getQuantity).sum();
        double totalSum = items.stream().mapToDouble(TransactionProduct::getTotalPrice).sum();
        double discount = getDiscount(totalQty, totalSum);
        double finalSum = totalSum - discount;

        txRepo.finalizeDraft(draftId, totalQty, finalSum, LocalDate.now());
        cartRepo.clearByTransaction(draftId);
        Session.clearCurrentDraftId();

        new ProductsController(stage);   // назад к товарам
    }

    /* простая заглушка расчёта скидки */
    private double getDiscount(int qty, double sum){ return 0.0; }

    private void recalcTotals(){
        int qty = cartRepo.getByTransaction(draftId).stream()
                .mapToInt(TransactionProduct::getQuantity).sum();
        double sum = cartRepo.getByTransaction(draftId).stream()
                .mapToDouble(TransactionProduct::getTotalPrice).sum();
        view.totalQtyLbl().setText(String.valueOf(qty));
        view.totalSumLbl().setText(String.format("%.2f", sum));
    }
}

