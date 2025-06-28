package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.Transaction;
import org.example.controller.CheckTransactionController;

public class HistoryView {

    private final Button btnProducts = new Button("Товары");
    private final Button btnCart     = new Button("Корзина");
    private final Button btnHistory  = new Button("История");
    private final Button btnProfile  = new Button("Личный кабинет");

    private final TableView<Transaction> table = new TableView<>();
    private final Scene scene;

    public HistoryView() {
        HBox top = new HBox(15, btnProducts, btnCart, btnHistory,
                new Region(), btnProfile);
        HBox.setHgrow(top.getChildren().get(3), Priority.ALWAYS);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color:#ffffff;" +
                "-fx-border-color:#e0e0e0;-fx-border-width:0 0 1 0;");

        buildTable();

        BorderPane root = new BorderPane(table, top, null, null, null);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color:#f4f4f4;");

        scene = new Scene(root, 900, 600);
    }

    private void buildTable() {
        TableColumn<Transaction, String> cDate = new TableColumn<>("Дата");
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        cDate.setPrefWidth(150);

        TableColumn<Transaction, Integer> cQty = new TableColumn<>("Количество");
        cQty.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        cQty.setPrefWidth(120);

        TableColumn<Transaction, Double> cSum = new TableColumn<>("Сумма");
        cSum.setCellValueFactory(new PropertyValueFactory<>("totalSum"));

        TableColumn<Transaction, Void> cView = new TableColumn<>("");
        cView.setCellFactory(col -> new TableCell<Transaction, Void>() {
            private final Button btn = new Button("Просмотреть заказ");

            {
                btn.setOnAction(e -> {
                    Transaction t = getTableView().getItems().get(getIndex());
                    Stage st = (Stage) getTableView().getScene().getWindow();
                    new CheckTransactionController(st, t.getId());
                });
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(cDate, cQty, cSum, cView);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("История пуста"));
    }

    public Scene getScene() { return scene; }
    public TableView<Transaction> table() { return table; }
    public Button btnProducts() { return btnProducts; }
    public Button btnCart() { return btnCart; }
    public Button btnHistory() { return btnHistory; }
    public Button btnProfile() { return btnProfile; }
}