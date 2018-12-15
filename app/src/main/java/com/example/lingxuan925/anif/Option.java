package com.example.lingxuan925.anif;

public class Option {

    private String name;
    private int imageId;

    public Option(String n, int i){
        name = n;
        imageId = i;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId(){
        return imageId;
    }
}
