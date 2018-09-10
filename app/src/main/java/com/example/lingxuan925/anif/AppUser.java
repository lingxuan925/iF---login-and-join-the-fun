package com.example.lingxuan925.anif;

public class AppUser {

    String name;
    String email;

    public AppUser(String userName, String userEmail) {
        this.email = userEmail;
        this.name = userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
