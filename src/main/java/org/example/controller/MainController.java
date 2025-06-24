package org.example.controller;

import javafx.stage.Stage;
import org.example.model.User;
import org.example.view.MainView;

public class MainController {

    private final Stage stage;
    private final MainView view;

    public MainController(Stage stage, User user) {
        this.stage = stage;
        this.view  = new MainView(user);
        bindHandlers();
        stage.setScene(view.getScene());
        stage.centerOnScreen();
    }

    private void bindHandlers() {
        view.btnLogout().setOnAction(e -> new LoginController(stage));
    }
}
