package com.example.peterjester.inventory.model.entity;

public class Item {

    private int id;
    private String name = null;
    private String description = null;
    private String location = null;
//    private List<String> tags = null;
    private String photoPath = null;

    public Item(int id, String name, String description, String location, String photoPath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.photoPath = photoPath;
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

//    public List<String> getTags() {
//        return tags;
//    }
}
