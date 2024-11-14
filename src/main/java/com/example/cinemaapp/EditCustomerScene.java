package com.example.cinemaapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class EditCustomerScene {
    private String firstName = "Name";
    private String lastName = "Lastname";
    private String mail = "mail@gmail.com";
    private String password = "";
    @FXML
    private AnchorPane rootPaneEdit;
    @FXML
    private Label newNameLabel;
    @FXML
    private Label oldNameLabel;
    @FXML
    private Label newEmailLabel;
    @FXML
    private Label oldEmailLabel;
    @FXML
    private Label newCustomer;
    @FXML
    private Label oldCustomer;
    @FXML
    private Button saveButton;
    @FXML
    public void initialize() {
        newNameLabel.setText(firstName + '\n' + lastName);
        oldNameLabel.setText(firstName + '\n' + lastName);
        newEmailLabel.setText((new JSONObject(ProfileSceneController.personalDataString)).getString("mail"));
        oldEmailLabel.setText((new JSONObject(ProfileSceneController.personalDataString)).getString("mail"));

        Image userImage = new Image(getClass().getResource("images/circle-user-solid.png").toExternalForm());
        ImageView userImageView = new ImageView(userImage);
        userImageView.setFitHeight(25);
        userImageView.setFitWidth(25);
        newNameLabel.setGraphic(userImageView);
        newNameLabel.setText("Name\nLastname");

        ImageView userImageView2 = new ImageView(userImage);
        userImageView2.setFitHeight(25);
        userImageView2.setFitWidth(25);
        oldNameLabel.setGraphic(userImageView2);
        oldNameLabel.setText("Name\nLastname");

        Image emailImage = new Image(getClass().getResource("images/envelope-solid.png").toExternalForm());
        ImageView emailImageView = new ImageView(emailImage);
        emailImageView.setFitWidth(25);
        emailImageView.setFitHeight(25);
        newEmailLabel.setGraphic(emailImageView);

        ImageView emailImageView2 = new ImageView(emailImage);
        emailImageView2.setFitWidth(25);
        emailImageView2.setFitHeight(25);
        oldEmailLabel.setGraphic(emailImageView2);

        Image customerImage = new Image(getClass().getResource("images/suitcase-solid.png").toExternalForm());
        ImageView customerImageView = new ImageView(customerImage);
        customerImageView.setFitWidth(25);
        customerImageView.setFitHeight(25);
        newCustomer.setGraphic(customerImageView);

        ImageView customerImageView2 = new ImageView(customerImage);
        customerImageView2.setFitWidth(25);
        customerImageView2.setFitHeight(25);
        oldCustomer.setGraphic(customerImageView2);

        Image saveImage = new Image(getClass().getResource("images/floppy-disk-solid.png").toExternalForm());
        ImageView saveImageView = new ImageView(saveImage);
        saveImageView.setFitHeight(25);
        saveImageView.setFitWidth(25);
        saveButton.setGraphic(saveImageView);

    }

    public void inputFirstName(KeyEvent keyEvent) {
        TextField firstNameInput = (TextField) keyEvent.getSource();
        firstName = firstNameInput.getText();
        newNameLabel.setText(firstName + '\n' + lastName);
    }

    public void inputLastName(KeyEvent keyEvent) {
        TextField lastNameInput = (TextField) keyEvent.getSource();
        lastName = lastNameInput.getText();
        newNameLabel.setText(firstName + '\n' + lastName);
    }

    public void inputEmail(KeyEvent keyEvent) {
        TextField emailInput = (TextField) keyEvent.getSource();
        mail = emailInput.getText();
        newEmailLabel.setText(mail);
    }

    public void inputPassword(KeyEvent keyEvent) {
        TextField passwordInput = (TextField) keyEvent.getSource();
        password = passwordInput.getText();
    }

    public void setNameLastName(String name) {
        newNameLabel.setText(name);
        oldNameLabel.setText(name);
    }

    public void setEmail(String mail) {
        newEmailLabel.setText(mail);
        oldEmailLabel.setText(mail);
    }

    public AnchorPane getRootPane() {
        return rootPaneEdit;
    }

    public void save(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProfileScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ProfileSceneController profileSceneController = fxmlLoader.getController();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to update your information?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        (alert.getDialogPane().getScene().getWindow()).setOnCloseRequest(e -> {
            alert.hide();
        });

        Optional<ButtonType> check = alert.showAndWait();
        if (check.isPresent() && check.get() == yesButton) {
            profileSceneController.setNameLabel(firstName + '\n' + lastName);
            profileSceneController.setFirstName(firstName);
            profileSceneController.setLastName(lastName);
            profileSceneController.setEmailLabel(mail);
            profileSceneController.setPassword(password);

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            ((AnchorPane) scene.getRoot()).setPrefHeight(rootPaneEdit.getHeight());
            ((AnchorPane) scene.getRoot()).setPrefWidth(rootPaneEdit.getWidth());
            profileSceneController.setCssFile(scene);
            try {
                FileWriter fileWriter = new FileWriter(System.getProperty("user.dir")+ File.separator + "data" + File.separator + "PersonalData.json");
                fileWriter.write(ProfileSceneController.personalDataString);
                fileWriter.close();
            }
            catch (IOException e) {
                throw new IOException(e);
            }


            stage.setScene(scene);

            stage.show();
        }
    }

    public void back(MouseEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProfileScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        ((AnchorPane) scene.getRoot()).setPrefHeight(rootPaneEdit.getHeight());
        ((AnchorPane) scene.getRoot()).setPrefWidth(rootPaneEdit.getWidth());
        stage.setScene(scene);

        ProfileSceneController profileSceneController = fxmlLoader.getController();
        profileSceneController.setNameLabel(oldNameLabel.getText());
        profileSceneController.setEmailLabel(oldEmailLabel.getText());
        profileSceneController.setCssFile(scene);
    }
}
