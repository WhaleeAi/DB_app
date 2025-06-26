package org.example.controller;

import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.ProfileView;

public class ProfileController {

    private final Stage stage;
    private final ProfileView view;
    private final Users usersRepo = Users.getInstance();
    private final Customers customersRepo = Customers.getInstance();
    private final String role;

    public ProfileController(Stage stage, String role) {
        this.stage = stage;
        this.role = role;
        this.view  = new ProfileView(role);
        initData();
        bindHandlers();
        stage.setScene(view.getScene());
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private void initData() {
        User u = Session.getCurrentUser();
        Customer c = Session.getCurrentCustomer();
        if (u != null) {
            view.login().setText(u.getLogin());
            view.password().setText(u.getPassword());
        }
        if (c != null) {
            view.company().setText(c.getCompanyName());
            view.phone().setText(c.getPhone());
            view.address().setText(c.getAddress());
            view.person().setText(c.getContactPerson());
        }
    }

    private void bindHandlers() {
        view.saveBtn().setOnAction(e -> handleSave());
        view.backBtn().setOnAction(e -> {
            User u = Session.getCurrentUser();
            if (u != null && "ADMIN".equalsIgnoreCase(u.getRole()))
                new AdminProductsController(stage);
            else
                new ProductsController(stage);
        });
        view.person().setOnAction(e -> handleSave());
    }

    private void handleSave() {
        User u = Session.getCurrentUser();
        Customer c = Session.getCurrentCustomer();
        if (u != null) {
            u.setLogin(view.login().getText().trim());
            u.setPassword(view.password().getText());
            usersRepo.updateUser(u);
        }
        if (c != null & role == "customer") {
            c.setCompanyName(view.company().getText().trim());
            c.setPhone(view.phone().getText().trim());
            c.setAddress(view.address().getText().trim());
            c.setContactPerson(view.person().getText().trim());
            customersRepo.updateCustomer(c);
        }
        view.messageLbl().setText("Сохранено");
    }
}