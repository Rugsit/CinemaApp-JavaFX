package com.example.cinemaapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CinemaApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CinemaApp.class.getResource("ProfileScene.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            stage.setResizable(!fxmlLoader.equals(new FXMLLoader(CinemaApp.class.getResource("LoginScene.fxml"))));

            String css = getClass().getResource("ALL.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setMinHeight(620);
            stage.setMinWidth(620);
            stage.setTitle("CinemaApp!");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}