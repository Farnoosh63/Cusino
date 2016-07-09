package com.epicodus.campfinder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.epicodus.campfinder.R;
import com.epicodus.campfinder.models.Food;
import com.epicodus.campfinder.services.CampService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = SearchActivity.class.getSimpleName();
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    public ArrayList<Food> mFoods = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String food = intent.getStringExtra("food");

        getFoods(food);
    }

    private void getFoods(String food){
        final CampService foodService = new CampService();
        foodService.findFoods(food, new Callback() {

        @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

        }
        @Override
            public void onResponse(Call call, Response response) throws IOException {
            try {
                String jsonData = response.body().string();
                if (response.isSuccessful()) {
                    mFoods = foodService.processResults(jsonData);
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
