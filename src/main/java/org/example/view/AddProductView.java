package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class AddProductView {

    private final TextField     name    = new TextField();
    private final TextField description = new TextField();
    private final TextField wholesalePrice = new TextField();
    private final TextField retailPrice  = new TextField();
    private final Button        setNameButton   = new Button("Добавить продукт");
    private final Button        backToProds   = new Button("Вернуться");
    private final Label         messageLabel  = new Label();

    private final Scene scene;

    public AddProductView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#f4f4f4;");

        Label title = new Label("Вход в систему");
        title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#333;");

        GridPane form = buildFormCard();
        HBox buttons  = buildButtonBar();

        messageLabel.setTextFill(Color.web("#d32f2f"));
        messageLabel.setStyle("-fx-font-size:12px;");

        root.getChildren().addAll(title, form, buttons, messageLabel);
        scene = new Scene(root, 420, 500);
    }

    private GridPane buildFormCard() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); grid.setVgap(15);
        grid.setPadding(new Insets(25));
        grid.setStyle("""
                      -fx-background-color:white;
                      -fx-background-radius:10;
                      -fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.1),10,0,0,0);
                      """);

        addRow(grid, "Название:",    name,    0);
        addRow(grid, "Розничная цена:",   retailPrice, 1);
        addRow(grid, "Оптовая цена:",   wholesalePrice, 2);
        addRow(grid, "Описание:",   description, 3);

        return grid;
    }

    private void addRow(GridPane g, String caption, Control field, int row) {
        Label lbl = new Label(caption);
        lbl.setStyle("-fx-font-weight:bold;-fx-text-fill:#555;");
        g.add(lbl,0,row); g.add(field,1,row);
        field.setPrefWidth(220);
        field.setStyle("-fx-padding:8;-fx-border-radius:5;-fx-background-radius:5;");
    }

    private HBox buildButtonBar() {
        stylePrimary(setNameButton);
        stylePrimary(backToProds);
        HBox box = new HBox(12, setNameButton, backToProds);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void stylePrimary(Button b){
        b.setPrefWidth(140);
        b.setStyle("""
                   -fx-background-color:#1976d2;
                   -fx-text-fill:white;-fx-font-weight:bold;
                   -fx-padding:10 24;-fx-background-radius:5;
                   """);
    }

    public Scene getScene() { return scene; }
    public Button getNameButton() { return setNameButton; }
    public Button getbackToProds() { return backToProds; }
    public TextField getName() { return name; }
    public TextField getRetailPrice() { return retailPrice; }
    public TextField getWholesalePrice() { return wholesalePrice; }
    public TextField getDescription() { return description; }
}