module com.example.cinemaapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    requires javafx.media;
    requires javafx.web;
    requires java.logging;
    requires javax.mail.api;


    opens com.example.cinemaapp to javafx.fxml;
    exports com.example.cinemaapp;
}