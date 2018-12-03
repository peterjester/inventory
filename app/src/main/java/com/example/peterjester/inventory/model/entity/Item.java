package com.example.peterjester.inventory.model.entity;

import java.io.Serializable;

public class Item implements Serializable {

    private int id;
    private String name = null;
    private String description = null;
    private String location = null;
    private String photoPath = null;
    private boolean checkedOut = false;

    public Item() {
        // default constructor
    }

    public Item(int id, String name, String description, String location, String photoPath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.photoPath = photoPath;
        this.checkedOut = false;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut() { checkedOut = !checkedOut; };

}
