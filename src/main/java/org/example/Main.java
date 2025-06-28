package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.LoginController;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        new LoginController(stage);
        stage.setTitle("CRM-Demo");
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
