package org.example.controller;

import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.*;

public class LoginController {

    private final Stage stage;
    private final LoginView view;
    private final Users repo = Users.getInstance();

    public LoginController(Stage stage) {
        this.stage = stage;
        this.view  = new LoginView();          // создаём View
        bindHandlers();
        stage.setScene(view.getScene());       // сразу показываем
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    /* ----------- handlers ----------- */
    private void bindHandlers() {
        view.loginButton().setOnAction(e -> tryLogin());
        view.signUpButton().setOnAction(
                e -> new RegistrationController(stage));   // открываем регистрацию
        view.getPasswordField().setOnAction(e -> tryLogin());
    }

    private void tryLogin() {
        String login = view.getLoginField().getText().trim();
        String pass  = view.getPasswordField().getText();

        User u = repo.getAllUsers().stream()
                .filter(x -> x.getLogin().equals(login)
                        && x.getPassword().equals(pass))
                .findFirst().orElse(null);

        if (u == null) {
            view.getMessageLabel().setText("Неверный логин или пароль");
            return;
        }

        String role = u.getRole().toUpperCase();
        switch (role) {
            case "CUSTOMER":
                    new ProductsController(stage);
//            case "ADMIN" ->
//                    new AdminController(stage);
            default:
                    view.getMessageLabel().setText("Роль «" + u.getRole() + "» не поддерживается");
        }
    }
}

