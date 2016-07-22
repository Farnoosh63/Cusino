package com.epicodus.foodfinder.models;

import java.util.ArrayList;

public class Restaurant {
    private String mName;
    private String mPhone;
    private String mWebsite;
    private double mRating;
    private String mImageUrl;
    private ArrayList<String> mAddress = new ArrayList<>();
    private ArrayList<String> mCategories = new ArrayList<>();


    public Restaurant(String name, String phone, String website,
                      double rating, String imageUrl, ArrayList<String> address, ArrayList<String> categories) {
        this.mName = name;
        this.mPhone = phone;
        this.mWebsite = website;
        this.mRating = rating;
        this.mImageUrl = imageUrl;
        this.mAddress = address;
        this.mCategories = categories;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getWebsite() {
        return  mWebsite;
    }

    public double getRating() {
        return mRating;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public ArrayList<String> getAddress() {
        return mAddress;
    }


    public ArrayList<String> getCategories() {
        return mCategories;
    }
}

