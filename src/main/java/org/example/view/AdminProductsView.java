package org.example.view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.model.Product;

public class AdminProductsView {

    private final Button btnProducts  = new Button("Товары");
    private final Button btnCusts      = new Button("Покупатели");
    private final Button btnHistory   = new Button("Общая история");
    private final Button btnAddProduct = new Button("Добавить товар");
    private final Button btnProfile   = new Button("Личный кабинет");

    private final TableView<Product> table = new TableView<>();
    private final Scene scene;

    public AdminProductsView() {

        HBox top = new HBox(15, btnProducts, btnCusts, btnHistory,
                new Region(), btnAddProduct, btnProfile);
        HBox.setHgrow(top.getChildren().get(3), Priority.ALWAYS);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color:#fff;-fx-border-color:#e0e0e0;-fx-border-width:0 0 1 0;");

        buildTable();

        BorderPane root = new BorderPane(table, top, null, null, null);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color:#f4f4f4;");
        root.setBottom(buildBottomBar());

        scene = new Scene(root, 900, 600);
    }

    private void buildTable() {
        TableColumn<Product,String> cName  = new TableColumn<>("Название");
        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cName.setPrefWidth(350);

        TableColumn<Product,Double> cPrice = new TableColumn<>("Цена, ₽");
        cPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        cPrice.setPrefWidth(120);

        TableColumn<Product,Double> c2Price = new TableColumn<>("Цена оптовая, ₽");
        c2Price.setCellValueFactory(new PropertyValueFactory<>("wholesalePrice"));
        c2Price.setPrefWidth(120);

        table.getColumns().addAll(cName, cPrice, c2Price);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private HBox buildBottomBar(){
        HBox box = new HBox(20,
                new Region(),                btnAddProduct);
        HBox.setHgrow(box.getChildren().get(1), Priority.ALWAYS);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);
        btnAddProduct.setStyle("""
            -fx-background-color:#1976d2;-fx-text-fill:white;
            -fx-font-weight:bold;-fx-padding:10 24;
            -fx-background-radius:5;""");
        return box;
    }

    public Scene getScene()            { return scene; }
    public TableView<Product> table()  { return table; }
    public Button btnAddProduct()       { return btnAddProduct; }
    public Button btnCart()            { return btnCusts; }
    public Button getBtnHistory(){ return btnHistory; }
    public Button btnProfile()         { return btnProfile; }
    public Button btnProducts()        { return btnProducts; }
}