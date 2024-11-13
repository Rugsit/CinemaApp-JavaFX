package com.example.cinemaapp;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;

public class LoginSceneController {
    @FXML
    private FlowPane loginFlowPane;
    @FXML
    private Button goButton;
    @FXML
    private TextField id;
    @FXML
    private PasswordField password;
    @FXML
    private Label warningLabel;
    private Stage stage;
    private FXMLLoader root;
    private Scene scene, oldScene;

    @FXML
    public void initialize() {
        goButton.setStyle("-fx-background-color: #3b383d");
        loginFlowPane.setStyle("-fx-background-color: #333035");
    }

    public void switchScene(ActionEvent actionEvent) throws IOException {
            root = new FXMLLoader(getClass().getResource("ProfileScene.fxml"));
            stage = (Stage) (((Node) (actionEvent.getSource())).getScene().getWindow());
            oldScene = ((Node) actionEvent.getSource()).getScene();
            scene = new Scene(root.load());

            if (id.getText().equals("Nest") && password.getText().equals(new JSONObject(ProfileSceneController.personalDataString).getString("passwordC:\\Users\\uSeR\\Desktop\\javafx-sdk-22.0.1\\lib"))) {
                scene.getStylesheets().add(oldScene.getStylesheets().getFirst());
                stage.setScene(scene);
                stage.show();
                stage.setResizable(true);
                stage.setMaximized(true);
            }
            else {
                warningLabel.setText("Username or password incorrect!");
            }

    }
}
