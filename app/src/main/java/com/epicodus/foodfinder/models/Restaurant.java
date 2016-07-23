package com.epicodus.foodfinder.models;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Restaurant {
     String name;
     String phone;
     String website;
     double rating;
     String imageUrl;
     ArrayList<String> address = new ArrayList<>();
     ArrayList<String> categories = new ArrayList<>();
    double latitude;
    double longitude;


    private String pushId;

    public Restaurant() {}

    public Restaurant(String name, String phone, String website,
                      double rating, String imageUrl, ArrayList<String> address,
                      double latitude, double longitude, ArrayList<String> categories) {
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.address = address;
        this.categories = categories;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return  website;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public ArrayList<String> getAddress() {
        return address;
    }


    public ArrayList<String> getCategories() {
        return categories;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}

