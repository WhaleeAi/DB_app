package org.example.controller;

import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.*;

public class LoginController {

    private final Stage stage;
    private final LoginView view;
    private final Users repo = Users.getInstance();
    private final Customers customersRepo = Customers.getInstance();

    public LoginController(Stage stage) {
        this.stage = stage;
        this.view  = new LoginView();
        bindHandlers();
        stage.setScene(view.getScene());
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private void bindHandlers() {
        view.loginButton().setOnAction(e -> tryLogin());
        view.signUpButton().setOnAction(
                e -> new RegistrationController(stage));
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

        Session.setCurrentUser(u);
        Customer c = customersRepo.getByUserId(u.getId());
        Session.setCurrentCustomer(c);


        String role = u.getRole().toUpperCase();
        switch (role) {
            case "CUSTOMER":
                    new ProductsController(stage);
                    break;
            case "ADMIN":
                    new AdminProductsController(stage);
                    break;
            default:
                    view.getMessageLabel().setText("Роль «" + u.getRole() + "» не поддерживается");
        }
    }
}

