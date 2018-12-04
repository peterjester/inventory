package com.example.peterjester.inventory.model.entity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import com.example.peterjester.inventory.model.entity.MapLocation;

public class Item implements Serializable {

    private int id;
    private String name = null;
    private String description = null;
    private String location = null;
    private String photoPath = null;
    private boolean checkedOut = false;

    private MapLocation geolocation = null;

    public Item() {
        // default constructor
    }

    public Item(int id, String name, String description, String location, String photoPath, MapLocation geolocation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.photoPath = photoPath;
        this.checkedOut = false;
        this.geolocation = geolocation;
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

    public void setCheckedOut() { checkedOut = !checkedOut; }

    public MapLocation getGeolocation() {
        return geolocation;
    }

}
