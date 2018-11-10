package com.example.peterjester.inventory.model.entity;

public class Item {

    private String name = null;
    private String description = null;
    private String location = null;
//    private List<String> tags = null;
//    private Image photo = null;


    public Item(String name, String description, String location) {
        this.name = name;
        this.description = description;
        this.location = location;
//        this.tags = tags;
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

//    public List<String> getTags() {
//        return tags;
//    }
}
