package org.example.view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.controller.ProductsController;
import org.example.model.Product;

public class ProductsView {

    /* ---------- header ---------- */
    private final Button btnProducts = new Button("Товары");
    private final Button btnCart     = new Button("Корзина");
    private final Button btnHistory  = new Button("История");
    private final Button btnProfile  = new Button("Личный кабинет");

    /* ---------- таблица ---------- */
    private final TableView<Product> table = new TableView<>();

    /* сцена */
    private final Scene scene;

    public ProductsView() {
        /* — верхняя панель — */
        HBox top = new HBox(15, btnProducts, btnCart, btnHistory,
                new Region(), btnProfile);
        HBox.setHgrow(top.getChildren().get(3), Priority.ALWAYS);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);
        styleHeader(top);

        /* — таблица — */
        buildTable();

        BorderPane root = new BorderPane(table, top, null, null, null);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color:#f4f4f4;");

        scene = new Scene(root, 900, 600);
    }

    /* --------------------------------------------------------------------- */
    private void buildTable() {
        TableColumn<Product,String> cName  = new TableColumn<>("Название");
        cName .setCellValueFactory(new PropertyValueFactory<>("name"));
        cName .setPrefWidth(300);

        TableColumn<Product,Double> cRetail = new TableColumn<>("Розничная цена");
        cRetail.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));

        TableColumn<Product,Double> cWhole = new TableColumn<>("Оптовая цена");
        cWhole .setCellValueFactory(new PropertyValueFactory<>("wholesalePrice"));

        /* колонка с «- / +» */
        TableColumn<Product,Void> cControls = new TableColumn<>("Количество");
        cControls.setCellFactory(col -> new QuantityCell());

        table.getColumns().addAll(cName, cRetail, cWhole, cControls);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /* --- кастомная ячейка с кнопками --- */
    private static class QuantityCell extends TableCell<Product,Void> {
        private final Label qty = new Label("0");
        private final Button plus = new Button("+");
        private final Button minus= new Button("-");

        QuantityCell() {
            HBox box = new HBox(6, minus, qty, plus);
            box.setAlignment(Pos.CENTER);
            setGraphic(box);
            plus .setOnAction(e -> change(+1));
            minus.setOnAction(e -> change(-1));
        }
        private void change(int delta){
            Product p = getTableView().getItems().get(getIndex());
            ProductsController.getInstance().updateQuantity(p, delta);
        }
        @Override protected void updateItem(Void v, boolean empty){
            super.updateItem(v, empty);
            setGraphic(empty?null:getGraphic());
            if (!empty){
                Product p = getTableView().getItems().get(getIndex());
                int q = ProductsController.getInstance().getQuantity(p);
                qty.setText(String.valueOf(q));
            }
        }
    }

    /* — простая стилизация шапки — */
    private void styleHeader(HBox h){
        h.setStyle("-fx-background-color:#ffffff;" +
                "-fx-border-color:#e0e0e0;-fx-border-width:0 0 1 0;");
        for (javafx.scene.Node n : h.getChildren())
            if (n instanceof Button b) {
                b.setStyle("""
                    -fx-background-color:transparent;
                    -fx-text-fill:#1976d2;
                    -fx-font-weight:bold;
                    -fx-padding:6 12;
                    """);
            }
    }

    /* ---------- getters ---------- */
    public Scene getScene()                { return scene;     }
    public TableView<Product> table()      { return table;     }
    public Button btnProducts()            { return btnProducts; }
    public Button btnCart()                { return btnCart;   }
    public Button btnHistory()             { return btnHistory;}
    public Button btnProfile()             { return btnProfile;}
}
