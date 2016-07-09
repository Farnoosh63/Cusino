package com.epicodus.campfinder.models;

/**
 * Created by Guest on 7/8/16.
 */
public class Food {
    private String mTitle;
    private String mIngredients;
    private String mLink;

    public Food(String title, String ingredients, String link){
        this.mTitle = title;
        this.mIngredients = ingredients;
        this.mLink = link;
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

}
