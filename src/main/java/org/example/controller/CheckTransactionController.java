package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.TransactionProduct;
import org.example.model.TransactionProducts;
import org.example.view.CheckTransactionView;

import java.util.List;

public class CheckTransactionController {

    private final Stage stage;
    private final CheckTransactionView view;
    private final TransactionProducts repo = TransactionProducts.getInstance();
    private final int txId;

    public CheckTransactionController(Stage stage, int txId) {
        this.stage = stage;
        this.txId = txId;
        this.view = new CheckTransactionView();

        initData();
        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    private void initData() {
        List<TransactionProduct> items = repo.loadFromDb(txId);
        view.table().setItems(FXCollections.observableArrayList(items));
        int qty = items.stream().mapToInt(TransactionProduct::getQuantity).sum();
        double sum = items.stream().mapToDouble(TransactionProduct::getTotalPrice).sum();
        view.totalQtyLbl().setText(String.valueOf(qty));
        view.totalSumLbl().setText(String.format("%.2f", sum));
    }

    private void bindHandlers() {
        view.backBtn().setOnAction(e -> new HistoryController(stage));
    }
}