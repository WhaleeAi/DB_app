package org.example.view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.model.Customer;

public class CustomersView {

    private final Button btnProducts   = new Button("Товары");
    private final Button btnCusts      = new Button("Покупатели");
    private final Button btnHistory    = new Button("Общая история");
    private final Button btnProfile    = new Button("Личный кабинет");

    private final TableView<Customer> table = new TableView<>();
    private final Scene scene;

    public CustomersView() {
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
        TableColumn<Customer,String> cName = new TableColumn<>("Компания");
        cName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        cName.setPrefWidth(200);

        TableColumn<Customer,String> cPhone = new TableColumn<>("Телефон");
        cPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cPhone.setPrefWidth(120);

        TableColumn<Customer,String> cAddr = new TableColumn<>("Адрес");
        cAddr.setCellValueFactory(new PropertyValueFactory<>("address"));
        cAddr.setPrefWidth(220);

        TableColumn<Customer,String> cPerson = new TableColumn<>("Контактное лицо");
        cPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        cPerson.setPrefWidth(150);

        table.getColumns().addAll(cName, cPhone, cAddr, cPerson);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("Список пуст"));
    }

    public Scene getScene()          { return scene; }
    public TableView<Customer> table(){ return table; }
    public Button btnCusts()          { return btnCusts; }
    public Button btnProducts()       { return btnProducts; }
    public Button btnProfile()        { return btnProfile; }
    public Button btnHistory()        { return btnHistory; }
}