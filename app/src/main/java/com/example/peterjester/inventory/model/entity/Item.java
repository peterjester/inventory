package com.example.peterjester.inventory.model.entity;

import java.util.List;

public class Item {

    private String name = null;
    private String description = null;
    private String location = null;
    private List<String> tags = null;
//    private Image photo = null;


    public Item(String name, String description, String location, List<String> tags) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.tags = tags;
    }



}
