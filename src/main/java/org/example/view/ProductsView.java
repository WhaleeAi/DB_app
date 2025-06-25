package org.example.view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.model.Product;
import org.example.controller.ProductsController;

public class ProductsView {

    /* header */
    private final Button btnProducts  = new Button("Товары");
    private final Button btnCart      = new Button("Корзина");
    private final Button btnHistory   = new Button("История");
    private final Button btnAddToCart = new Button("Добавить в корзину");
    private final Button btnProfile   = new Button("Личный кабинет");

    private final TableView<Product> table = new TableView<>();
    private final Scene scene;

    public ProductsView() {

        HBox top = new HBox(15, btnProducts, btnCart, btnHistory,
                new Region(), btnAddToCart, btnProfile);
        HBox.setHgrow(top.getChildren().get(3), Priority.ALWAYS);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color:#fff;-fx-border-color:#e0e0e0;-fx-border-width:0 0 1 0;");

        buildTable();

        BorderPane root = new BorderPane(table, top, null, null, null);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color:#f4f4f4;");

        scene = new Scene(root, 900, 600);
    }

    private void buildTable() {
        TableColumn<Product,String> cName  = new TableColumn<>("Название");
        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cName.setPrefWidth(350);

        TableColumn<Product,Double> cPrice = new TableColumn<>("Цена, ₽");
        cPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        cPrice.setPrefWidth(120);

        TableColumn<Product,Void> cQty = new TableColumn<>("Кол-во");
        cQty.setCellFactory(col -> new QuantityCell());

        table.getColumns().addAll(cName, cPrice, cQty);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /* ячейка с кнопками + / – */
    private static class QuantityCell extends TableCell<Product,Void> {
        private final Label  lbl  = new Label("0");
        private final Button plus = new Button("+");
        private final Button minus= new Button("-");

        QuantityCell() {
            minus.setOnAction(e -> change(-1));
            plus .setOnAction(e -> change(+1));
            HBox box = new HBox(6, minus, lbl, plus);
            box.setAlignment(Pos.CENTER);
            setGraphic(box);
        }
        private void change(int d){
            Product p = getTableView().getItems().get(getIndex());
            ProductsController.getInstance().updateQuantity(p,d);
        }
        @Override protected void updateItem(Void v, boolean empty){
            super.updateItem(v, empty);
            setGraphic(empty?null:getGraphic());
            if(!empty){
                Product p = getTableView().getItems().get(getIndex());
                lbl.setText(String.valueOf(
                        ProductsController.getInstance().getQuantity(p)));
            }
        }
    }

    /* ---------- getters ---------- */
    public Scene getScene()            { return scene; }
    public TableView<Product> table()  { return table; }
    public Button btnAddToCart()       { return btnAddToCart; }
    public Button btnCart()            { return btnCart; }
}
