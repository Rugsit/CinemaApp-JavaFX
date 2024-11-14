package com.example.cinemaapp;

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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingController {
    static String seatStatus;
    static {
        try {
            seatStatus = new String(Files.readAllBytes((Paths.get(System.getProperty("user.dir"), "data", "SeatStatus.json"))));
            if (seatStatus.equals("{}")) {
                seatStatus = """
                        {"C1":true,"B1":true,"C2":true,"A1":true,"B2":true,"C3":true,"A2":true,"B3":true,"C4":true,"A3":true,"B4":true,"C5":true,"A4":true,"B5":true,"C6":true,"A5":true,"B6":true,"A6":true}
                        """;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String bookingData;
    static {
        try {
            bookingData = new String(Files.readAllBytes((Paths.get(System.getProperty("user.dir"),"data", "DataBooking.json"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static ImageView selectedSeat = null;
    @FXML
    private AnchorPane bookAnchorPane;
    @FXML
    private Label namePopup;
    @FXML
    private Label filmPopup;
    @FXML
    private Label datePopup;
    @FXML
    private Label timePopup;
    @FXML
    private Label seatPopup;
    @FXML
    private FlowPane popup;
    @FXML
    private FlowPane blurPane;
    @FXML
    private DatePicker selectDate;
    @FXML
    private Button viewHistoryButton;
    @FXML
    private Button bookSeatButton;
    @FXML
    private ComboBox<String> selectFilm;
    @FXML
    private GridPane gridSeat;
    @FXML
    private ComboBox<String> selectTime;
    @FXML
    private FlowPane invisablePane;
    @FXML
    public void initialize() {
        reloadSeat();
        selectDate.getEditor().setStyle("-fx-font-size: 20px; -fx-font-family: Roboto Medium;");

        selectTime.setStyle("-fx-font-size: 20px; -fx-font-family: Roboto Medium;");

        selectFilm.setStyle("-fx-font-size: 20px; -fx-font-family: Roboto Medium;");
        selectFilm.setOnAction(event -> {
            getFilmName(event);
        });

    }

    public static String getBookingData() {
        return bookingData;
    }

    public void getDate(ActionEvent event) {
        selectFilm.getItems().clear();
        selectTime.getItems().clear();
        String jsonString = AllFilmsSceneController.getJsonString();
        JSONObject jsonObject = new JSONObject(jsonString);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            LocalDate endDate = LocalDate.parse(jsonObject.getJSONObject(key).getString("endDate"));
            LocalDate startDate = LocalDate.parse(jsonObject.getJSONObject(key).getString("startDate"));
            LocalDate targetDate = selectDate.getValue();
            if ((targetDate.isBefore(endDate) && targetDate.isAfter(startDate)) || targetDate.isEqual(endDate) || targetDate.isEqual(startDate)) {
                selectFilm.getItems().add(key);
            }
        }
    }

    public void getFilmName(ActionEvent event) {
        selectTime.getItems().clear();
        String jsonString = AllFilmsSceneController.getJsonString();
        JSONObject jsonObject = new JSONObject(jsonString);
        if (selectFilm.getValue() != null) {
            selectTime.getItems().add(jsonObject.getJSONObject(selectFilm.getValue()).getString("time1"));
            selectTime.getItems().add(jsonObject.getJSONObject(selectFilm.getValue()).getString("time2"));
            selectTime.getItems().add(jsonObject.getJSONObject(selectFilm.getValue()).getString("time3"));
        }
    }

    public void clickBooked(MouseEvent event, String id) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("The seat " + id.toUpperCase() + " is booked already!");
        alert.show();
    }

    public void chooseSeat(MouseEvent event) {
        Image redSeat = new Image(getClass().getResource("images/red-chair.png").toExternalForm());
        Image blackSeat = new Image(getClass().getResource("images/black-chair.png").toExternalForm());
        ImageView imageView = (ImageView) event.getSource();
        if (selectedSeat == null) {
            imageView.setImage(redSeat);
            selectedSeat = imageView;
        }
        else if (imageView == selectedSeat) {
            imageView.setImage(blackSeat);
            selectedSeat = null;
        }
        else {
            selectedSeat.setImage(blackSeat);
            imageView.setImage(redSeat);
            selectedSeat = imageView;
        }
    }

    public void bookSeat(ActionEvent event) {
        Alert warning = new Alert(Alert.AlertType.WARNING);
        warning.setContentText("Please select film and time");

        Alert warningSeatNull = new Alert(Alert.AlertType.WARNING);
        warningSeatNull.setContentText("Please select seat");

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Do you want to proceed with the booking?");
        if (selectFilm.getValue() == null || selectTime.getValue() == null) {
            warning.show();
        } else if (selectedSeat == null) {
            warningSeatNull.show();
        } else {
            if (confirm.showAndWait().get() == ButtonType.OK) {
                JSONObject jsonSeatStatus = new JSONObject(seatStatus);
                jsonSeatStatus.put(selectedSeat.getId(), false);
                seatStatus = jsonSeatStatus.toString();
                saveToJsonFile();
                reloadSeat();

                blurPane.setVisible(true);
                invisablePane.setVisible(true);

                JSONObject jsonBookingData = new JSONObject();
                JSONObject jsonBookingStatic = new JSONObject(bookingData);
                jsonBookingData.put("firstName", ProfileSceneController.getFirstName());
                namePopup.setText("Name: " + ProfileSceneController.getFirstName() + " " + ProfileSceneController.getLastName());
                jsonBookingData.put("lastName", ProfileSceneController.getLastName());
                jsonBookingData.put("film", selectFilm.getValue());
                filmPopup.setText("Film: " + selectFilm.getValue());
                jsonBookingData.put("date", selectDate.getValue().toString());
                datePopup.setText("Date: " + selectDate.getValue().toString());
                jsonBookingData.put("time", selectTime.getValue());
                timePopup.setText("Time: " + selectTime.getValue());
                jsonBookingData.put("seat", selectedSeat.getId());
                seatPopup.setText("Seats: " + selectedSeat.getId().toUpperCase());
                jsonBookingData.put("status", "booked");
                jsonBookingStatic.put(String.valueOf(jsonBookingStatic.length()), jsonBookingData );
                bookingData =  jsonBookingStatic.toString();
                selectedSeat = null;
                saveToJsonFile();
            }
        }



    }

    public void closePopup(ActionEvent event) {
        blurPane.setVisible(false);
        invisablePane.setVisible(false);
    }

    public void reloadSeat() {
        gridSeat.getChildren().forEach(seat -> {
            if (seat.getStyleClass().toString().equals("image-view")) {
                Image image;
                JSONObject jsonObject = new JSONObject(seatStatus);
                if (jsonObject.getBoolean(seat.getId())) {
                    image = new Image(getClass().getResource("images/black-chair.png").toExternalForm());
                    ((ImageView) seat).setOnMouseClicked(this::chooseSeat);
                }
                else {
                    image = new Image(getClass().getResource("images/gray-chair.png").toExternalForm());
                    ((ImageView) seat).setOnMouseClicked(event ->  {
                        clickBooked(event, seat.getId());
                    });
                }
                ((ImageView) seat).setImage(image);
            }
        });
    }

    public void back(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProfileScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("ALL.css").toExternalForm());

        ((AnchorPane) scene.getRoot()).setPrefWidth(bookAnchorPane.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(bookAnchorPane.getHeight());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void viewHistory(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookingHistoryScene.fxml"));
        Scene scene = new Scene(fxmlLoader. load());
        scene.getStylesheets().add(getClass().getResource("ALL.css").toExternalForm());

        ((AnchorPane) scene.getRoot()).setPrefWidth(bookAnchorPane.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(bookAnchorPane.getHeight());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    static void saveToJsonFile() {
        try {
            FileWriter fileWriter = new FileWriter("data" + File.separator + "DataBooking.json");
            fileWriter.write(bookingData);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter fileWriter = new FileWriter("data" + File.separator + "SeatStatus.json");
            fileWriter.write(seatStatus);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailDetailBook(ActionEvent event) throws MessagingException {
        String recipientEmail = ProfileSceneController.personalDataJsonObject.getString("mail");
        sendEmail(recipientEmail);
    }

    private void sendEmail(String recipientEmail) throws MessagingException {
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
            new Alert(Alert.AlertType.INFORMATION, "Send Email Successfully").show();
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Please Try Again").show();
        }
        assert message != null;
        Transport.send(message);
    }

    private Message prepareMessage(Session session, String myEmail, String recipientEmail) {
        JSONObject jsonObject = new JSONObject(bookingData);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Film booking successfully details");
            message.setText(namePopup.getText() + "\n" + filmPopup.getText() + "\n" + datePopup.getText() + "\n" + timePopup.getText() +
                    "\n" + seatPopup.getText());
            return message;
        }
        catch (Exception e) {
            Logger.getLogger(ProfileSceneController.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
