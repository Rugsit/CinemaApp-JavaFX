package com.example.cinemaapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class BookingHistoryController {
    JSONObject jsonObject = new JSONObject(BookingController.bookingData);
    @FXML
    private TableColumn<BookingData, String> status;
    @FXML
    private TableColumn<BookingData, String> firstName;
    @FXML
    private TableColumn<BookingData, String> lastName;
    @FXML
    private TableColumn<BookingData, String> film;
    @FXML
    private TableColumn<BookingData, String> date;
    @FXML
    private TableColumn<BookingData, String> time;
    @FXML
    private TableColumn<BookingData, String> seat;
    @FXML
    private TableColumn<BookingData, String> id;
    @FXML
    private TableView<BookingData> tableHistory;
    @FXML
    private AnchorPane bookHistoryAnchor;

    ObservableList<BookingData> list = FXCollections.observableArrayList();
    Iterator<String> keys = jsonObject.keys();

    @FXML
    public void initialize() {
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject currentJsonObject = jsonObject.getJSONObject(key);
            list.add(new BookingData(currentJsonObject.getString("status"),
                    currentJsonObject.getString("firstName"),
                    currentJsonObject.getString("lastName"),
                    currentJsonObject.getString("film"),
                    currentJsonObject.getString("date"),
                    currentJsonObject.getString("time"),
                    currentJsonObject.getString("seat"),
                    key));
        }

        status.setCellValueFactory(new PropertyValueFactory<BookingData, String>("status"));
        firstName.setCellValueFactory(new PropertyValueFactory<BookingData, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<BookingData, String>("lastName"));
        film.setCellValueFactory(new PropertyValueFactory<BookingData, String>("film"));
        date.setCellValueFactory(new PropertyValueFactory<BookingData, String>("date"));
        time.setCellValueFactory(new PropertyValueFactory<BookingData, String>("time"));
        seat.setCellValueFactory(new PropertyValueFactory<BookingData, String>("seat"));
        id.setCellValueFactory(new PropertyValueFactory<BookingData, String>("id"));

        tableHistory.setItems(list);
    }

    public void back(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookingScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("ALL.css").toExternalForm());

        ((AnchorPane) scene.getRoot()).setPrefWidth(bookHistoryAnchor.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(bookHistoryAnchor.getHeight());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void cancelBooking(ActionEvent event) {
        JSONObject jsonSeatStatus = new JSONObject(BookingController.seatStatus);
        BookingData dataSelected = tableHistory.getSelectionModel().getSelectedItem();
        if (dataSelected == null) {
            return;
        }
        tableHistory.getItems().set(Integer.parseInt(dataSelected.getId()), dataSelected.setStatus("cancelled"));

        jsonObject.put(dataSelected.getId(), jsonObject.getJSONObject(dataSelected.getId()).put("status", "cancelled"));
        BookingController.bookingData = jsonObject.toString();

        jsonSeatStatus.put(dataSelected.getSeat(), true);
        BookingController.seatStatus = jsonSeatStatus.toString();
        BookingController.saveToJsonFile();
    }
}

