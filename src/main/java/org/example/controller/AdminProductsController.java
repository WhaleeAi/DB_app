package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.AdminProductsView;

public class AdminProductsController {

    private static AdminProductsController INSTANCE;
    public  static AdminProductsController getInstance(){ return INSTANCE; }

    private final Stage stage;
    private final AdminProductsView view;

    private final Products            prodRepo = Products.getInstance();

    public AdminProductsController(Stage stage) {
        INSTANCE = this;
        this.stage = stage;
        this.view  = new AdminProductsView();

        view.table().setItems(FXCollections.observableArrayList(prodRepo.getAllProducts()));

        view.btnAddProduct().setOnAction(e -> handleAddProduct());
        view.btnCart().setOnAction(e -> new CustomersController(stage));
        view.getBtnHistory().setOnAction(e -> new AdminHistoryController(stage));
        view.btnProfile().setOnAction(e -> new ProfileController(stage, "admin"));
        view.btnProducts().setOnAction(e -> new AdminProductsController(stage));

        stage.setScene(view.getScene());
        stage.show();
    }

    private void handleAddProduct() {
        new AddProductController(stage);

        view.table().refresh();
    }
}