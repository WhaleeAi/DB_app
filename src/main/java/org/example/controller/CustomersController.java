package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.Customers;
import org.example.view.CustomersView;

public class CustomersController {

    private final Stage stage;
    private final CustomersView view;

    private final Customers repo = Customers.getInstance();

    public CustomersController(Stage stage) {
        this.stage = stage;
        this.view  = new CustomersView();

        initData();
        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    private void initData() {
        view.table().setItems(FXCollections.observableArrayList(repo.getAllCustomers()));
    }

    private void bindHandlers() {
        view.btnProducts().setOnAction(e -> new AdminProductsController(stage));
        view.btnCusts().setOnAction(e -> new CustomersController(stage));
        view.btnHistory().setOnAction(e -> new AdminHistoryController(stage));
        view.btnProfile().setOnAction(e -> new ProfileController(stage, "admin"));
    }
}