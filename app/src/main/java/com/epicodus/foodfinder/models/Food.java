package com.epicodus.foodfinder.models;


import org.parceler.Parcel;

@Parcel
public class Food {
    private String mTitle;
    private String mIngredients;
    private String mLink;
    private String mImage;
    private String pushId;

    public Food() {}

    public Food(String title, String ingredients, String link, String image){
        this.mTitle = title;
        this.mIngredients = ingredients;
        this.mLink = link;
        this.mImage = image;
    }

    public String getTitle(){
        return mTitle;
    }
    public String getIngredients(){
        return mIngredients;
    }
    public String getLink(){
        return mLink;
    }
    public String getImage(){
        return mImage;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

}
