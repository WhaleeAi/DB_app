package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.Session;
import org.example.model.Transactions;
import org.example.view.HistoryView;

public class HistoryController {

    private final Stage stage;
    private final HistoryView view;

    private final Transactions txRepo = Transactions.getInstance();

    public HistoryController(Stage stage) {
        this.stage = stage;
        this.view  = new HistoryView();

        initData();
        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    private void initData() {
        var customer = Session.getCurrentCustomer();
        if (customer != null)
            view.table().setItems(FXCollections.observableArrayList(
                    txRepo.getTransactionsByCustomer(customer.getId())));
        else
            view.table().setItems(FXCollections.observableArrayList(txRepo.getAllTransactions()));
    }

    private void bindHandlers() {
        view.btnProducts().setOnAction(e -> new ProductsController(stage));
        view.btnCart().setOnAction(e -> {
            Integer draftId = Session.getCurrentDraftId();
            new CartController(stage, draftId != null ? draftId : -1);
        });
        view.btnProfile().setOnAction(e -> new ProfileController(stage, "customer"));
    }
}