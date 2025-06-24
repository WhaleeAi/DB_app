package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.model.User;

public class MainView {

    private final Scene scene;
    private final Button btnLogout = new Button("Выйти");

    public MainView(User user) {
        Label hello = new Label("Добро пожаловать, " + user.getLogin());
        hello.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        VBox root = new VBox(20, hello, btnLogout);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(400,250);
        scene = new Scene(root);
    }

    public Scene  getScene()   { return scene; }
    public Button btnLogout()  { return btnLogout; }
}
