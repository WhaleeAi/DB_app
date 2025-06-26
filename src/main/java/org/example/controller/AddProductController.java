package org.example.controller;

import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.*;

public class AddProductController {

    private final Stage stage;
    private final AddProductView view;
    private final Products repo = Products.getInstance();

    public AddProductController(Stage stage) {
        this.stage = stage;
        this.view  = new AddProductView();
        bindHandlers();
        stage.setScene(view.getScene());
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private void bindHandlers() {
        view.getNameButton().setOnAction(e -> addProduct());
        view.getbackToProds().setOnAction(e -> new AdminProductsController(stage));
    }

    private void addProduct() {
        String name = view.getName().getText().trim();
        String description  = view.getDescription().getText().trim();
        String retailTxt = view.getRetailPrice().getText().trim();
        String wholeTxt  = view.getWholesalePrice().getText().trim();

        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setRetailPrice(Double.parseDouble(retailTxt));
        p.setWholesalePrice(Double.parseDouble(wholeTxt));

        repo.addOrUpdateProduct(p);
    }
}