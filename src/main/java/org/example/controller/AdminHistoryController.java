package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.Transactions;
import org.example.view.AdminHistoryView;

public class AdminHistoryController {

    private final Stage stage;
    private final AdminHistoryView view;

    private final Transactions txRepo = Transactions.getInstance();

    public AdminHistoryController(Stage stage) {
        this.stage = stage;
        this.view  = new AdminHistoryView();

        initData();
        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    private void initData() {
        view.table().setItems(FXCollections.observableArrayList(txRepo.getAllTransactions()));
    }

    private void bindHandlers() {
        view.btnProducts().setOnAction(e -> new AdminProductsController(stage));
        view.btnCusts().setOnAction(e -> new CustomersController(stage));
        view.btnHistory().setOnAction(e -> new AdminHistoryController(stage));
        view.btnProfile().setOnAction(e -> new ProfileController(stage, "admin"));
    }
}