package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LoginView {
    private final TextField loginField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Войти");
    private final Button signUpButton = new Button("Регистрация");
    private final Label messageLabel = new Label();

    private final Scene scene;

    public LoginView() {
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
        scene = new Scene(root, 420, 320);
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

        addRow(grid, "Логин:",    loginField,    0);
        addRow(grid, "Пароль:",   passwordField, 1);

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
        stylePrimary(loginButton);
        styleSecondary(signUpButton);
        HBox box = new HBox(12, loginButton, signUpButton);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void stylePrimary(Button b){
        b.setPrefWidth(140);
        b.setStyle("""
                   -fx-background-color:#1976d2;
                   -fx-text-fill:white;-fx-font-weight:bold;
                   -fx-padding:10 24;-fx-background-radius:5;
                   """); }
    private void styleSecondary(Button b){
        b.setPrefWidth(140);
        b.setStyle("""
                   -fx-background-color:#616161;
                   -fx-text-fill:white;-fx-font-weight:bold;
                   -fx-padding:10 24;-fx-background-radius:5;
                   """); }

    public Scene getScene() { return scene; }
    public TextField getLoginField() { return loginField; }
    public PasswordField getPasswordField()  { return passwordField; }
    public Button loginButton() { return loginButton; }
    public Button signUpButton() { return signUpButton; }
    public Label getMessageLabel() { return messageLabel; }
}


