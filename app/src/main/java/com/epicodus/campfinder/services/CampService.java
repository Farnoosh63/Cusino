package com.epicodus.campfinder.services;

import android.util.Log;

import com.epicodus.campfinder.Constants;
import com.epicodus.campfinder.models.Food;
import com.squareup.picasso.Downloader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class CampService {

    public static void findFoods (String foodSearch, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().build();


         HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.API_BASE_URL).newBuilder();
//        urlBuilder.addQueryParameter(Constants.FOOD_QUERY_PARAMETER);
         urlBuilder.addQueryParameter(Constants.FOOD_QUERY_PARAMETER, foodSearch);
         String url = urlBuilder.build().toString();
         Log.d("URL", url);


         Request request = new Request.Builder().url(url).build();

         Call call = client.newCall(request);
         call.enqueue(callback);

    }

    public ArrayList<Food> processResults (String jsonData) {
        ArrayList<Food> foods = new ArrayList<>();
        try {
            JSONObject foodJSON = new JSONObject(jsonData);
            JSONArray resultsJSON = foodJSON.getJSONArray("results");

            for (int i = 0; i < resultsJSON.length(); i++) {
                JSONObject queryJSON = resultsJSON.getJSONObject(i);

                String title = queryJSON.getString("title");
                String ingredients = queryJSON.getString("ingredients");
                String link = queryJSON.getString("href");

                Food food = new Food(title, ingredients, link);
                foods.add(food);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foods;
    }
}
