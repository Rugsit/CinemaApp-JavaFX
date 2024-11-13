package com.example.cinemaapp;

public class BookingData {
    private String status;
    private String firstName;
    private String lastName;
    private String film;
    private String date;
    private String time;
    private String seat;
    private String id;

    public BookingData(String status, String firstName, String lastName, String film, String date, String time, String seat, String id) {
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.film = film;
        this.date = date;
        this.time = time;
        this.seat = seat;
        this.id = id;
    }

    public BookingData setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFilm() {
        return film;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSeat() {
        return seat;
    }

    public String getId() {
        return id;
    }
}
