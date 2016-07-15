package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.epicodus.foodfinder.Constants;
import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.adapter.FoodListAdapter;
import com.epicodus.foodfinder.models.Food;
import com.epicodus.foodfinder.services.FoodService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity  {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    private FoodListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private String mRecentFoodSearched;



    public ArrayList<Food> mFoods = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentFoodSearched = mSharedPreferences.getString(Constants.PREFERENCES_FOOD_KEY, null);
//
        if (mRecentFoodSearched != null) {
            getFoods(mRecentFoodSearched);
        }


//        Intent intent = getIntent();
//        String food = intent.getStringExtra("food");

//        getFoods(food);
    }

    private void getFoods(String food) {
        final FoodService foodService = new FoodService();
        foodService.findFoods(food, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String jsonData = response.body().string();

                    mFoods = foodService.processResults(jsonData);



                SearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mAdapter = new FoodListAdapter(getApplicationContext(), mFoods);
                            mRecyclerView.setAdapter(mAdapter);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                            mRecyclerView.setLayoutManager(layoutManager);

                            mRecyclerView.setHasFixedSize(true);
                        }
                    });
                }


        });
    }

}

