package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    private SharedPreferences.Editor mEditor;




    public ArrayList<Food> mFoods = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String food = intent.getStringExtra("food");

        getFoods(food);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentFoodSearched = mSharedPreferences.getString(Constants.PREFERENCES_FOOD_KEY, null);

        if (mRecentFoodSearched != null) {
            getFoods(mRecentFoodSearched);
        }

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
    private void addToSharedPreferences(String food) {
        mEditor.putString(Constants.PREFERENCES_FOOD_KEY, food).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getFoods(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}

