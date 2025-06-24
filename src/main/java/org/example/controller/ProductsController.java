package org.example.controller;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.ProductsView;

import java.util.HashMap;
import java.util.Map;

public class ProductsController {

    /* Singleton-контроллер — чтобы QuantityCell могла обращаться */
    private static ProductsController INSTANCE;
    public static ProductsController getInstance() { return INSTANCE; }

    private final Stage stage;
    private final ProductsView view;

    private final Products             prodRepo   = Products.getInstance();
    private final TransactionProducts  cartRepo   = TransactionProducts.getInstance();

    /* локальный кэш «productId -> qty» для быстрого доступа */
    private final Map<Integer,Integer> qtyMap = new HashMap<>();

    public ProductsController(Stage stage) {
        INSTANCE = this;
        this.stage = stage;
        this.view  = new ProductsView();

        initData();
        bindHandlers();

        stage.setScene(view.getScene());
        stage.show();
    }

    /* --------------------------- DATA --------------------------- */
    private void initData() {
        view.table().setItems(
                FXCollections.observableArrayList(prodRepo.getAllProducts()));

        /* если корзина уже есть (вернулись со страницы Cart) */
        cartRepo.getAll().forEach(tp -> qtyMap.put(tp.getProductId(), tp.getQuantity()));
    }

    /* ------------------------- HANDLERS ------------------------- */
    private void bindHandlers() {
        view.btnCart().setOnAction(e -> new CartController(stage));
        // btnProducts – активна, ничего не делает
    }

    /* ----------------- методы для QuantityCell ------------------ */
    public void updateQuantity(Product p, int delta) {
        int current = qtyMap.getOrDefault(p.getId(), 0);
        int newQty  = Math.max(0, current + delta);

        if (newQty == 0) {
            qtyMap.remove(p.getId());
            cartRepo.removeByProduct(p.getId());
        } else {
            qtyMap.put(p.getId(), newQty);
            cartRepo.addOrUpdate(p, newQty);          // создаём/обновляем позицию
        }
        /* перерисовать строку */
        view.table().refresh();
    }

    public int getQuantity(Product p) {
        return qtyMap.getOrDefault(p.getId(), 0);
    }
}
