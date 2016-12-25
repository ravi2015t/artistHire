package com.eventplanner.models;

/**
 * Created by TMI on 12/22/2016.
 */

public class MapConfirmedEvent {
    private Address venue;
    private String usr;
    private String artist;
    private String username;
    private String date;
    private String category;

    public Address getVenue() {
        return venue;
    }

    public void setAddress(Address venue) {
        this.venue = venue;
    }

    public String getUsr() {
        return usr;
    }

    public void setUser(String usr) {
        this.usr = usr;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setVendor(String artist) {
        this.artist = artist;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "vendor" + this.getArtist() + "user" + this.getUsr();
    }

}
