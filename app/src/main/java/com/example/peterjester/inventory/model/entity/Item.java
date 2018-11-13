package com.example.peterjester.inventory.model.entity;

import android.graphics.Bitmap;

public class Item {

    private int id;
    private String name = null;
    private String description = null;
    private String location = null;
//    private List<String> tags = null;
    private Bitmap photo = null;

    public Item(int id, String name, String description, String location, Bitmap photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.photo = photo;
//        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Bitmap getPhoto() {
        return photo;
    }

//    public List<String> getTags() {
//        return tags;
//    }
}
