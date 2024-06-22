package com.example.cinemaapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class AllFilmsSceneController {
    static String jsonString = """
            {"StarWars":{"time1":"19:00","time2":"20:00","time3":"hh:mm","endDate":"2018-02-01","description":"Having taken her first steps into the Jedi world, Rey joins Luke Skywalker on an adventure with Leia, Finn and Poe that unlocks mysteries of the Force and secrets of the past.","startDate":"2018-01-19","trailer":"https://www.youtube.com/watch?v=Q0CbN8sfihY"},"TheMatrix":{"time1":"21:00","time2":"22:00","time3":"hh:mm","endDate":"2018-02-01","description":"A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.","startDate":"2018-01-19","trailer":"https://www.youtube.com/watch?v=m8e-FF8MsqU"},"ShutterIsland":{"time1":"21:00","time2":"22:00","time3":"hh:mm","endDate":"2018-02-15","description":"In 1954, a U.S. Marshal investigates the disappearance of a murderer, who escaped from a hospital for the criminally insane.","startDate":"2018-02-02","trailer":"https://www.youtube.com/watch?v=qdPw9x9h5CY"},"Transformers":{"time1":"19:00","time2":"20:00","time3":"hh:mm","endDate":"2018-03-01","description":"Autobots and Decepticons are at war, with humans on the sidelines. Optimus Prime is gone. The key to saving our future lies buried in the secrets of the past, in the hidden history of Transformers on Earth.","startDate":"2018-02-16","trailer":"https://www.youtube.com/watch?v=AntcyqJ6brc"},"Inception":{"time1":"21:00","time2":"22:00","time3":"hh:mm","endDate":"2018-01-18","description":"A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.","startDate":"2018-01-05","trailer":"https://www.youtube.com/watch?v=8hP9D6kZseM"},"Dunkirk":{"time1":"21:00","time2":"22:00","time3":"hh:mm","endDate":"2018-03-01","description":"Allied soldiers from Belgium, the British Empire and France are surrounded by the German Army, and evacuated during a fierce battle in World War II.","startDate":"2018-02-16","trailer":"https://www.youtube.com/watch?v=F-eMt3SrfFU"},"Titanic":{"time1":"19:00","time2":"20:00","time3":"hh:mm","endDate":"2018-02-15","description":"A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.","startDate":"2018-02-02","trailer":"https://www.youtube.com/watch?v=2e-eXJ6HgkQ"},"Suburbicon":{"time1":"19:00","time2":"20:00","time3":"hh:mm","endDate":"2018-01-18","description":"A home invasion rattles a quiet family town.","startDate":"2018-01-05","trailer":"https://www.youtube.com/watch?v=cBezc1S1BAQ"}}
            """;
    @FXML
    private FlowPane flowAllFilms;
    @FXML
    private AnchorPane allFilmsPane;
    @FXML
    private ImageView eachImageFilm;
    @FXML
    private ScrollPane allFilmsScroll;
    @FXML
    public void initialize() throws IOException {
        JSONObject jsonObject = new JSONObject(jsonString);
        ArrayList<String> listTitleFilms = new ArrayList<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Image filmImage = new Image(getClass().getResource("images/FilmsImage/" + key + ".png").toExternalForm());
            ImageView filmImageView = new ImageView(filmImage);
            filmImageView.setFitWidth(300);
            filmImageView.setFitHeight(450);
            filmImageView.setPreserveRatio(false);
            filmImageView.setId(key);
            filmImageView.setCursor(Cursor.HAND);
            filmImageView.setOnMouseClicked(event -> {
                try {
                    filmsOnMouse(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            StackPane stackPane = new StackPane(filmImageView);
            stackPane.setPadding(new Insets(10));
            flowAllFilms.getChildren().add(stackPane);
        }

    }

    public void back(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProfileScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ((AnchorPane) scene.getRoot()).setPrefWidth(allFilmsPane.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(allFilmsPane.getHeight());
        scene.getStylesheets().add(getClass().getResource("ALL.css").toExternalForm());
        stage.setScene(scene);

        stage.show();
    }

    public void filmsOnMouse(MouseEvent event) throws IOException{
        JSONObject jsonObject = new JSONObject(jsonString);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EachFilmsDetails.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((AnchorPane) scene.getRoot()).setPrefWidth(allFilmsPane.getWidth());
        ((AnchorPane) scene.getRoot()).setPrefHeight(allFilmsPane.getHeight());

        String filmName = ((Node) event.getSource()).getId();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        EachFilmController controller = fxmlLoader.getController();
        controller.setScene(((JSONObject)jsonObject.get(filmName)).put("name", filmName));
    }

    static public String getJsonString() {
        return jsonString;
    }
}
