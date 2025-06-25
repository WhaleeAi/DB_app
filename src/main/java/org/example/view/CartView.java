package org.example.view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import org.example.controller.CartController;
import org.example.model.TransactionProduct;

public class CartView {

    /* header */
    private final Button btnProducts = new Button("Товары");
    private final Button btnCart     = new Button("Корзина");
    private final Button btnHistory  = new Button("История");
    private final Button btnProfile  = new Button("Личный кабинет");

    /* table + totals */
    private final TableView<TransactionProduct> table = new TableView<>();
    private final Label totalQtyLbl   = new Label("0");
    private final Label totalSumLbl   = new Label("0.00");
    private final Button buyButton    = new Button("Купить");

    private final Scene scene;

    public CartView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color:#f4f4f4;");

        root.setTop(buildHeader());
        root.setCenter(buildTable());
        root.setBottom(buildBottomBar());

        scene = new Scene(root, 900, 600);
    }

    private HBox buildHeader(){
        HBox h = new HBox(15, btnProducts, btnCart, btnHistory,
                new Region(), btnProfile);
        HBox.setHgrow(h.getChildren().get(3), Priority.ALWAYS);
        h.setPadding(new Insets(10));
        h.setAlignment(Pos.CENTER_LEFT);
        /* стиль такой же, как в ProductsView */
        h.setStyle("-fx-background-color:#ffffff;" +
                "-fx-border-color:#e0e0e0;-fx-border-width:0 0 1 0;");
        return h;
    }

    private TableView<TransactionProduct> buildTable(){
        TableColumn<TransactionProduct,String> cName =
                new TableColumn<>("Товар");
        cName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cName.setPrefWidth(300);

        TableColumn<TransactionProduct,Integer> cQty =
                new TableColumn<>("Кол-во");
        cQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<TransactionProduct,Double> cPrice =
                new TableColumn<>("Цена/шт");
        cPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        TableColumn<TransactionProduct,Double> cSum =
                new TableColumn<>("Сумма");
        cSum.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<TransactionProduct,Void> cDel =
                new TableColumn<>("");      // колонка удаления
        cDel.setCellFactory(col -> new DeleteCell());

        table.getColumns().addAll(cName,cQty,cPrice,cSum,cDel);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private static class DeleteCell extends TableCell<TransactionProduct,Void>{
        private final Button btn = new Button("Удалить");
        DeleteCell(){
            btn.setOnAction(e -> {
                TransactionProduct tp = getTableView().getItems().get(getIndex());
                CartController.getInstance().removeItem(tp);
            });
            setAlignment(Pos.CENTER);
        }

        @Override protected void updateItem(Void v, boolean empty){
            super.updateItem(v, empty);
            setGraphic(empty?null:btn);
        }
    }

    private HBox buildBottomBar(){
        HBox box = new HBox(20,
                new Label("Всего позиций:"), totalQtyLbl,
                new Label("Сумма:"),         totalSumLbl,
                new Region(),                buyButton);
        HBox.setHgrow(box.getChildren().get(4), Priority.ALWAYS);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);
        buyButton.setStyle("""
            -fx-background-color:#1976d2;-fx-text-fill:white;
            -fx-font-weight:bold;-fx-padding:10 24;
            -fx-background-radius:5;""");
        return box;
    }

    /* -------- getters -------- */
    public Scene getScene()                { return scene; }
    public TableView<TransactionProduct> table(){ return table; }
    public Label totalQtyLbl(){ return totalQtyLbl; }
    public Label totalSumLbl(){ return totalSumLbl; }
    public Button buyButton(){ return buyButton; }
    public Button btnProducts(){ return btnProducts; }
    public Button btnCart(){ return btnCart; }
    public Button btnHistory(){ return btnHistory; }
    public Button btnProfile(){ return btnProfile; }
}
