package com.example.lingxuan925.anif;

import java.util.ArrayList;

class Event {
    private String name;
    private String latitude;
    private String longitude;
    private int numLimit; //number of participants limit
    private String owner;
    private ArrayList<String> participants;
    private String description;

    public Event() {}

    public Event(String name, String latitude, String longitude, int numLimit){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numLimit = numLimit;
        participants = new ArrayList<>();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public int getNumLimit() {

        return numLimit;
    }

    public void setNumLimit(int numLimit) {
        this.numLimit = numLimit;
    }

    public String getLongitude() {

        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {

        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
