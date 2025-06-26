package org.example.view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.model.Transaction;

public class AdminHistoryView {

    private final Button btnProducts   = new Button("Товары");
    private final Button btnCusts      = new Button("Покупатели");
    private final Button btnHistory    = new Button("Общая история");
    private final Button btnProfile    = new Button("Личный кабинет");

    private final TableView<Transaction> table = new TableView<>();
    private final Scene scene;

    public AdminHistoryView() {
        HBox top = new HBox(15, btnProducts, btnCusts, btnHistory,
                new Region(), btnProfile);
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
        TableColumn<Transaction,String> cDate = new TableColumn<>("Дата");
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        cDate.setPrefWidth(150);

        TableColumn<Transaction,Integer> cQty = new TableColumn<>("Количество");
        cQty.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        cQty.setPrefWidth(120);

        TableColumn<Transaction,Double> cSum = new TableColumn<>("Сумма");
        cSum.setCellValueFactory(new PropertyValueFactory<>("totalSum"));

        table.getColumns().addAll(cDate, cQty, cSum);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("История пуста"));
    }

    public Scene getScene()             { return scene; }
    public TableView<Transaction> table(){ return table; }
    public Button btnCusts()             { return btnCusts; }
    public Button btnProfile()           { return btnProfile; }
    public Button btnProducts()          { return btnProducts; }
    public Button btnHistory()           { return btnHistory; }
}
