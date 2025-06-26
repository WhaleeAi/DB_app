package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ProfileView {

    private final TextField loginField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final TextField companyField = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField addressField = new TextField();
    private final TextField personField = new TextField();

    private final String role;

    private final Button saveButton = new Button("Сохранить");
    private final Button backButton = new Button("Назад");
    private final Label  message    = new Label();

    private final Scene scene;

    public ProfileView(String role) {
        this.role = role;
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#f4f4f4;");

        Label title = new Label("Личный кабинет");
        title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#333;");

        GridPane form = buildFormCard();
        HBox buttons  = buildButtonBar();

        message.setTextFill(Color.web("#d32f2f"));
        message.setStyle("-fx-font-size:12px;");

        root.getChildren().addAll(title, form, buttons, message);
        scene = new Scene(root, 520, 540);
    }

    private GridPane buildFormCard() {
        GridPane g = new GridPane();
        g.setAlignment(Pos.CENTER);
        g.setHgap(10); g.setVgap(15);
        g.setPadding(new Insets(25));
        g.setStyle("""
                   -fx-background-color:white;
                   -fx-background-radius:10;
                   -fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.1),10,0,0,0);
                   """);
        int r = 0;
        addRow(g,"Логин:", loginField, r++);
        addRow(g,"Пароль:", passwordField, r++);
        if (role == "customer"){
            addRow(g,"Компания:", companyField, r++);
            addRow(g,"Телефон:", phoneField, r++);
            addRow(g,"Адрес:", addressField, r++);
            addRow(g,"Контактное лицо:", personField, r++);
        }
        return g;
    }

    private void addRow(GridPane g,String caption,Control field,int row){
        Label l = new Label(caption);
        l.setStyle("-fx-font-weight:bold;-fx-text-fill:#555;");
        field.setPrefWidth(240);
        field.setStyle("-fx-padding:8;-fx-border-radius:5;-fx-background-radius:5;");
        g.add(l,0,row); g.add(field,1,row);
    }

    private HBox buildButtonBar(){
        stylePrimary(saveButton); styleSecondary(backButton);
        HBox box = new HBox(12, saveButton, backButton);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void stylePrimary(Button b){
        b.setPrefWidth(150);
        b.setStyle("""
                   -fx-background-color:#1976d2;
                   -fx-text-fill:white;-fx-font-weight:bold;
                   -fx-padding:10 24;-fx-background-radius:5;""");
    }

    private void styleSecondary(Button b){
        b.setPrefWidth(150);
        b.setStyle("""
                   -fx-background-color:#616161;
                   -fx-text-fill:white;-fx-font-weight:bold;
                   -fx-padding:10 24;-fx-background-radius:5;""");
    }

    public Scene getScene() { return scene; }
    public TextField login() { return loginField; }
    public PasswordField password() { return passwordField; }
    public TextField company() { return companyField; }
    public TextField phone() { return phoneField; }
    public TextField address() { return addressField; }
    public TextField person() { return personField; }
    public Button saveBtn() { return saveButton; }
    public Button backBtn() { return backButton; }
    public Label messageLbl() { return message; }
}
