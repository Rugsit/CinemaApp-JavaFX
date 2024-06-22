package com.example.cinemaapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EachFilmController {
    @FXML
    private AnchorPane eachFilmAnchorPane;
    @FXML
    private Label filmName;
    @FXML
    private Label filmDesc;
    @FXML
    private Label from;
    @FXML
    private Label to;
    @FXML
    private Label time;
    @FXML
    private ImageView filmImage;

    public void setScene(Object object) {
        JSONObject jsonObject = (JSONObject) object;
        Image image = new Image(getClass().getResource("images/FilmsImage/" + jsonObject.getString("name") + ".png").toExternalForm());
        filmImage.setImage(image);
        filmImage.setOnMouseClicked(event -> {
            try {
                openTrailer(event, jsonObject.getString("trailer"));
            }
            catch (Exception e) {
                System.out.println(e);
            }
        });

        filmName.setText(jsonObject.getString("name"));
        filmDesc.setText(jsonObject.getString("description"));
        from.setText("From: " + jsonObject.getString("startDate"));
        to.setText("To: " + jsonObject.getString("endDate"));
        time.setText("Times: " + jsonObject.getString("time1") + " " +jsonObject.getString("time2"));
    }

    public void back(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AllFilmsScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ((AnchorPane) scene.getRoot()).setPrefHeight(eachFilmAnchorPane.getHeight());
        ((AnchorPane) scene.getRoot()).setPrefWidth(eachFilmAnchorPane.getWidth());
        stage.setScene(scene);

        stage.show();
    }

    public void openTrailer(MouseEvent event, String mediaLink) {
        String videoId = extractVideoId(mediaLink);
        WebView webView = new WebView();
        webView.getEngine().load("https://www.youtube.com/embed/" + videoId);
        webView.setPrefHeight(eachFilmAnchorPane.getHeight());
        webView.setPrefWidth(eachFilmAnchorPane.getWidth());

        Button exitButton = new Button("BACK");
        exitButton.setStyle("-fx-background-color:  #252226; -fx-text-fill: white;");
        exitButton.setFont(Font.font("Roboto Medium", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        exitButton.setLayoutX(27);
        exitButton.setLayoutY(18);
        exitButton.setPrefHeight(50);
        exitButton.setPrefWidth(100);
        exitButton.setCursor(Cursor.HAND);
        exitButton.setOnAction((eventBack) -> {
            webView.getEngine().load(null);
            eachFilmAnchorPane.getChildren().remove(webView);
            eachFilmAnchorPane.getChildren().remove(exitButton);

        });

        eachFilmAnchorPane.getChildren().add(webView);
        eachFilmAnchorPane.getChildren().add(exitButton);
        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);
    }

    public String extractVideoId(String youtubeLink) {
        String videoId = null;
        Pattern compilePattern = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*");
        Matcher matcher = compilePattern.matcher(youtubeLink);

        if (matcher.find()) {
            videoId = matcher.group();
        }
        return videoId;
    }

    public void bookFilm(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookingScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("ALL.css").toExternalForm());

        ((AnchorPane) scene.getRoot()).setPrefWidth(eachFilmAnchorPane.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(eachFilmAnchorPane.getHeight());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
