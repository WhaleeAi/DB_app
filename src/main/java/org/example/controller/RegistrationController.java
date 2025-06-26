package org.example.controller;

import javafx.stage.Stage;
import org.example.model.*;
import org.example.view.RegistrationView;

public class RegistrationController {

    private final Stage stage;
    private final RegistrationView view;
    private final Users     usersRepo     = Users.getInstance();
    private final Customers customersRepo = Customers.getInstance();

    public RegistrationController(Stage stage) {
        this.stage = stage;
        this.view  = new RegistrationView();
        bindHandlers();
        stage.setScene(view.getScene());
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private void bindHandlers(){
        view.saveBtn().setOnAction(e -> handleSave());
        view.backBtn().setOnAction(e -> new LoginController(stage));
        view.person().setOnAction(e -> handleSave());
    }

    public void handleSave() {
        String login = view.login().getText().trim();
        String pass  = view.password().getText();

        if (login.isEmpty() || pass.isEmpty()){
            view.messageLbl().setText("Логин и пароль обязательны"); return;
        }
        boolean exists = usersRepo.getAllUsers()
                .stream().anyMatch(u -> u.getLogin().equalsIgnoreCase(login));
        if (exists){
            view.messageLbl().setText("Такой логин уже существует"); return;
        }

        User u = new User();
        u.setLogin(login); u.setPassword(pass); u.setRole("customer");
        usersRepo.addUser(u);

        Customer c = new Customer();
        c.setUserId(u.getId());
        c.setCompanyName(view.company().getText().trim());
        c.setPhone(view.phone().getText().trim());
        c.setAddress(view.address().getText().trim());
        c.setContactPerson(view.person().getText().trim());
        customersRepo.addCustomer(c);

        new LoginController(stage);
    }
}

