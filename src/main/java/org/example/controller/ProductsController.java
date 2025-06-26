package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.ProductsView;

import java.util.HashMap;
import java.util.Map;

public class ProductsController {

    private static ProductsController INSTANCE;
    public  static ProductsController getInstance(){ return INSTANCE; }

    private final Stage stage;
    private final ProductsView view;

    private final Products            prodRepo = Products.getInstance();
    private final TransactionProducts cartRepo = TransactionProducts.getInstance();
    private final Transactions        txRepo   = Transactions.getInstance();

    private final Map<Integer,Integer> qtyBuffer = new HashMap<>();

    private Integer draftId = null;

    public ProductsController(Stage stage) {
        INSTANCE = this;
        this.stage = stage;
        this.view  = new ProductsView();
        this.draftId = Session.getCurrentDraftId();

        view.table().setItems(FXCollections.observableArrayList(prodRepo.getAllProducts()));

        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    public void updateQuantity(Product p, int delta) {
        int q = Math.max(0, qtyBuffer.getOrDefault(p.getId(),0) + delta);
        if (q == 0)
            qtyBuffer.remove(p.getId());
        else qtyBuffer.put(p.getId(), q);
        view.table().refresh();
    }
    public int getQuantity(Product p){ return qtyBuffer.getOrDefault(p.getId(),0); }

    private void bindHandlers() {
        view.btnAddToCart().setOnAction(e -> handleAddToCart());
        view.btnCart()     .setOnAction(e ->
                new CartController(stage, draftId != null ? draftId : -1)
        );
        view.getBtnHistory().setOnAction(e -> new HistoryController(stage));
        view.btnProfile() .setOnAction(e -> new ProfileController(stage));
    }

    private void handleAddToCart() {
        if (qtyBuffer.isEmpty()) return;

        if (draftId == null) {
            Customer cust = Session.getCurrentCustomer();
            if (cust == null) return;
            draftId = txRepo.createDraft(cust.getId());
            Session.setCurrentDraftId(draftId);
        }

        qtyBuffer.forEach((pid, qty) -> {
            Product p = prodRepo.getAllProducts().stream()
                    .filter(pr -> pr.getId()==pid).findFirst().orElse(null);
            if (p != null)
                cartRepo.insert(draftId, p, qty);
        });

        qtyBuffer.clear();
        view.table().refresh();
        new CartController(stage, draftId);
    }
}
