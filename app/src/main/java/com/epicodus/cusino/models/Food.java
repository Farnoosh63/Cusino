package com.epicodus.cusino.models;


import org.parceler.Parcel;

@Parcel
public class Food {
    String title;
    String ingredients;
    String link;

    String image;
    String pushId;

    public Food() {}

    public Food(String title, String ingredients, String link, String image){
        this.title = title;
        this.ingredients = ingredients;
        this.link = link;
        this.image = image;
    }

    public String getTitle(){
        return title;
    }
    public String getIngredients(){
        return ingredients;
    }
    public String getLink(){
        return link;
    }
    public String getImage(){
        return image;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
