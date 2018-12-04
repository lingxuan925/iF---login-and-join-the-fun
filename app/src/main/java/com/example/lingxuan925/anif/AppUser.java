package com.example.lingxuan925.anif;

import java.util.ArrayList;

public class AppUser {

    private String name;
    private String email;
    private String[] events;

    AppUser() {
        //events = new String[]{"hhhh","aaaa"};
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEventId(String[] events){this.events = events; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String[] getEvents() { return events; }

}
