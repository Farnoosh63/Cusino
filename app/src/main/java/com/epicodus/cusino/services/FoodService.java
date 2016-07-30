package com.epicodus.cusino.services;


import android.util.Log;

import com.epicodus.cusino.Constants;
import com.epicodus.cusino.models.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FoodService {



    public static void findFoods(String foodSearch, Callback callback) {

        OkHttpClient client = new OkHttpClient.Builder().build();


        //BUILD YOUR HTTP STRING HERE
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.API_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.FOOD_QUERY_PARAMETER, foodSearch);




        String url = urlBuilder.build().toString();

        Log.d("URL", url);

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Food> processResults(String jsonData) {
        ArrayList<Food> foods = new ArrayList<>();

        try {

            JSONObject foodJSON = new JSONObject(jsonData);



            JSONArray resultsJSON = foodJSON.getJSONArray("results");

            for (int i = 0; i < resultsJSON.length(); i++) {

                JSONObject itemJSON = resultsJSON.getJSONObject(i);

                String title = itemJSON.getString("title");
                String ingredients = itemJSON.getString("ingredients");
                String link = itemJSON.getString("href");
                String image = itemJSON.optString("thumbnail", "Image not available");



                Food food = new Food(title, ingredients,link, image);
                foods.add(food);

            }

        }

        catch (JSONException e) {
            e.printStackTrace();

        }
        return foods;
    }
}
