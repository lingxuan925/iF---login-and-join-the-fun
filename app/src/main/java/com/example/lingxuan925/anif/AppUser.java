package com.example.lingxuan925.anif;

import java.util.ArrayList;

public class AppUser {

    private String name;
    private String email;
    private String whatsup;
    private String radius;
    private String imageUri;
    private String gender;
    private String age;

    ArrayList<String> eventIDs;

    public String getWhatsup() {
        return whatsup;
    }

    public void setWhatsup(String whatsup) {
        this.whatsup = whatsup;
    }

    public AppUser() {

    }

    public AppUser(String name, String age, String gender, String email, String whatsup, String radius, String imageUri) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.whatsup = whatsup;
        this.radius = radius;
        this.imageUri = imageUri;
        this.age = age;
        eventIDs = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }

    public void setEventIDs(ArrayList<String> eventIDs) {
        this.eventIDs = eventIDs;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
