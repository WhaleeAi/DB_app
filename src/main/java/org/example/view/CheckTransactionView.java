package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.model.TransactionProduct;

public class CheckTransactionView {

    private final Button backButton = new Button("Назад");
    private final TableView<TransactionProduct> table = new TableView<>();
    private final Label totalQtyLbl = new Label("0");
    private final Label totalSumLbl = new Label("0.00");

    private final Scene scene;

    public CheckTransactionView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color:#f4f4f4;");

        buildTable();
        root.setCenter(table);
        root.setBottom(buildBottomBar());

        scene = new Scene(root, 900, 600);
    }

    private void buildTable() {
        TableColumn<TransactionProduct, String> cName = new TableColumn<>("Товар");
        cName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cName.setPrefWidth(300);

        TableColumn<TransactionProduct, Integer> cQty = new TableColumn<>("Кол-во");
        cQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<TransactionProduct, Double> cPrice = new TableColumn<>("Цена/шт");
        cPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        TableColumn<TransactionProduct, Double> cSum = new TableColumn<>("Сумма");
        cSum.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        table.getColumns().addAll(cName, cQty, cPrice, cSum);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("Товары отсутствуют"));
    }

    private HBox buildBottomBar() {
        HBox box = new HBox(20,
                new Label("Всего позиций:"), totalQtyLbl,
                new Label("Сумма:"), totalSumLbl,
                new Region(), backButton);
        HBox.setHgrow(box.getChildren().get(4), Priority.ALWAYS);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    public Scene getScene() { return scene; }
    public TableView<TransactionProduct> table() { return table; }
    public Label totalQtyLbl() { return totalQtyLbl; }
    public Label totalSumLbl() { return totalSumLbl; }
    public Button backBtn() { return backButton; }
}