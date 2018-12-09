package com.example.lingxuan925.anif;

import java.util.ArrayList;

public class AppUser {

    String name;
    String email;
    String birthDate;
    String whatsup;

    ArrayList<String> eventIDs;


    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getWhatsup() {
        return whatsup;
    }

    public void setWhatsup(String whatsup) {
        this.whatsup = whatsup;
    }

    public AppUser() {

    }

    public AppUser(String name, String email, String birthDate, String whatsup) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.whatsup = whatsup;
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
}
