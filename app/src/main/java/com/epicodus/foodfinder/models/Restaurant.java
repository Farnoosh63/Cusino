package com.epicodus.foodfinder.models;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Restaurant {
     String mName;
     String mPhone;
     String mWebsite;
     double mRating;
     String mImageUrl;
     ArrayList<String> mAddress = new ArrayList<>();
     ArrayList<String> mCategories = new ArrayList<>();
    double mLatitude;
    double mLongitude;

    public Restaurant() {}

    public Restaurant(String name, String phone, String website,
                      double rating, String imageUrl, ArrayList<String> address,
                      double latitude, double longitude, ArrayList<String> categories) {
        this.mName = name;
        this.mPhone = phone;
        this.mWebsite = website;
        this.mRating = rating;
        this.mImageUrl = imageUrl;
        this.mAddress = address;
        this.mCategories = categories;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
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

    public double getLatitude() {
        return mLatitude;
    }
    public double getLongitude() {
        return mLongitude;
    }
}

