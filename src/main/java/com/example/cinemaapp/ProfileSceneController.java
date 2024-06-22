package com.example.cinemaapp;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileSceneController {
    static String personalDataString;
    static {
        try {
            personalDataString = new String(Files.readAllBytes((Paths.get("C:/Users/uSeR/IdeaProjects/CinemaApp/src/main/resources/com/example/cinemaapp/PersonalData.json"))));
            if (personalDataString.equals("{}")) {
                personalDataString = """
                            {"lastName":"LastName","password":"","mail":"mail@gmail.com","name":"FirstName","urlPicture":"file:/C:/Users/uSeR/IdeaProjects/JavaFX-tutorial/src/main/resources/com/example/javafxtutorial/nestPicture.jpg"}
                        """;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static JSONObject personalDataJsonObject = new JSONObject(personalDataString);
    @FXML
    private Button sendEmail;
    @FXML
    private TextField cc;
    @FXML
    private AnchorPane profileRootPane;
    @FXML
    private Button viewFilmsButton;
    @FXML
    private Button viewBookingButton;
    @FXML
    private Button uploadButton;
    @FXML
    private Button editButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label sendEmailLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private TextField to;
    @FXML
    private TextField subject;
    @FXML
    private ImageView imageProfile;
    @FXML
    private AnchorPane blurPane;
    @FXML
    private FlowPane invisiblePane;
    @FXML
    private TextArea contentEmail;
    @FXML
    public void initialize() throws MalformedURLException {
        to.setText("rugsit.nest@gmail.com");

        Image image = new Image(personalDataJsonObject.getString("urlPicture"));
        imageProfile.setImage(image);

        nameLabel.setText(personalDataJsonObject.getString("name") + "\n" + personalDataJsonObject.getString("lastName"));

        Image filmsImage = new Image(getClass().getResource("images/cinema-film-icon.png").toExternalForm());
        ImageView filmsImageView = new ImageView(filmsImage);
        filmsImageView.setFitHeight(30);
        filmsImageView.setFitWidth(30);
        viewFilmsButton.setGraphic(filmsImageView);

        Image bookImage = new Image (getClass().getResource("images/book-solid.png").toExternalForm());
        ImageView bookImageView = new ImageView(bookImage);
        bookImageView.setFitWidth(30);
        bookImageView.setFitHeight(30);
        viewBookingButton.setGraphic(bookImageView);

        Image uploadImage = new Image (getClass().getResource("images/arrow-up.png").toExternalForm());
        ImageView uploadImageView = new ImageView(uploadImage);
        uploadImageView.setFitWidth(20);
        uploadImageView.setFitHeight(20);
        uploadButton.setGraphic(uploadImageView);

        Image penImage = new Image (getClass().getResource("images/pen-solid.png").toExternalForm());
        ImageView penImageView = new ImageView(penImage);
        penImageView.setFitWidth(20);
        penImageView.setFitHeight(20);
        editButton.setGraphic(penImageView);

        Image profileImage = new Image (getClass().getResource("images/circle-user-solid.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(25);
        profileImageView.setFitHeight(25);
        nameLabel.setGraphic(profileImageView);

        Image emailImage = new Image (getClass().getResource("images/envelope-solid.png").toExternalForm());
        ImageView emailImageView = new ImageView(emailImage);
        emailImageView.setFitWidth(25);
        emailImageView.setFitHeight(25);
        sendEmailLabel.setGraphic(emailImageView);

        Image customerImage = new Image (getClass().getResource("images/suitcase-solid.png").toExternalForm());
        ImageView customerImageView = new ImageView(customerImage);
        customerImageView.setFitWidth(25);
        customerImageView.setFitHeight(25);
        customerLabel.setGraphic(customerImageView);
    }

    static public String getFirstName() {
        return personalDataJsonObject.getString("name");
    }

    static public String getLastName() {
        return personalDataJsonObject.getString("lastName");
    }

    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setFirstName(String first) {
        personalDataJsonObject.put("name", first);
        personalDataString = personalDataJsonObject.toString();
    }

    public void setLastName(String last) {
        personalDataJsonObject.put("lastName", last);
        personalDataString = personalDataJsonObject.toString();
    }

    public void setEmailLabel(String newMail) {
        personalDataJsonObject.put("mail", newMail);
        personalDataString = personalDataJsonObject.toString();
    }

    public void setPassword(String password) {
        personalDataJsonObject.put("password", password);
        personalDataString = personalDataJsonObject.toString();
    }

    public void setCssFile(Scene scene) throws IOException {
        String css = getClass().getResource("ALL.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    public AnchorPane getRootPane() {
        return profileRootPane;
    }

    public void editProfile(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditCustomerScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(((Node) event.getSource()).getScene().getStylesheets().getFirst());

        EditCustomerScene editCustomerScene = fxmlLoader.getController();
        editCustomerScene.setEmail(personalDataJsonObject.getString("mail"));
        editCustomerScene.setNameLastName(nameLabel.getText());


        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        ((AnchorPane)scene.getRoot()).setPrefWidth(profileRootPane.getWidth());
        ((AnchorPane)scene.getRoot()).setPrefHeight(profileRootPane.getHeight());
        stage.setScene(scene);
        stage.show();

    }

    public void uploadImage(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = new Image(file.toURI().toURL().toExternalForm());
            personalDataJsonObject.put("urlPicture", file.toURI().toURL().toExternalForm());
            personalDataString = personalDataJsonObject.toString();
            try {
                FileWriter fileWriter = new FileWriter("PersonalData.json");
                fileWriter.write(personalDataString);
                fileWriter.close();
            }
            catch (IOException e) {
                throw new IOException(e);
            }
            imageProfile.setImage(image);
        }
    }

    public void viewFilms(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AllFilmsScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ((AnchorPane) scene.getRoot()).setPrefWidth(profileRootPane.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(profileRootPane.getHeight());
        stage.setScene(scene);

        stage.show();
    }

    public void logout(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("ALL.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.setWidth(976);
        stage.setHeight(620);
        stage.setResizable(false);

        stage.show();
    }

    public void sendEmail(MouseEvent event) {
        invisiblePane.setVisible(true);
        blurPane.setVisible(true);
    }

    public void viewBooking(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookingScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("ALL.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ((AnchorPane) scene.getRoot()).setPrefWidth(profileRootPane.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(profileRootPane.getHeight());
        stage.setScene(scene);

        stage.show();
    }

    public void sendBtnOnAction(ActionEvent event) throws MessagingException {
        String recipientEmail = to.getText();
        sendEmail(recipientEmail);

    }

    public void sendEmail(String recipientEmail) throws MessagingException {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myEmail = "rugsit.nest@gmail.com";
        String password = "hupp wpuw lsbv qwab";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });
        Message message = prepareMessage(session,myEmail, recipientEmail);
        if (message != null) {
            Alert sendEmail = new Alert(Alert.AlertType.INFORMATION, "Send Email Successfully");
            if (sendEmail.showAndWait().get() == ButtonType.OK) {
                invisiblePane.setVisible(false);
                blurPane.setVisible(false);
            }
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Please Try Again").show();
        }
        assert message != null;
        Transport.send(message);
    }

    private Message prepareMessage(Session session, String myEmail, String recipientEmail) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            if (cc != null) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc.getText()));
            }
            message.setSubject("Received mail from " + personalDataJsonObject.getString("mail")+ ": " + subject.getText());
            message.setText(contentEmail.getText());
            return message;
        }
        catch (Exception e) {
            Logger.getLogger(ProfileSceneController.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public void closePopup(ActionEvent event) {
        invisiblePane.setVisible(false);
        blurPane.setVisible(false);
    }
}
