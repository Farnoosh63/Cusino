package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;

import com.epicodus.foodfinder.Constants;
import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.adapter.RestaurantListAdapter;
import com.epicodus.foodfinder.models.Restaurant;
import com.epicodus.foodfinder.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class SearchRestaurantActivity extends AppCompatActivity {
    public static final String TAG = SearchRestaurantActivity.class.getSimpleName();

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private String mRecentRestaurantSearched;

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);

        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentRestaurantSearched = mSharedPreferences.getString(Constants.PREFERENCES_RESTAURANT_KEY, null);
        Log.d("RECENTRESTAURANT", mRecentRestaurantSearched);
        if (mRecentRestaurantSearched != null) {
             getRestaurants(mRecentRestaurantSearched);
        }

        Intent intent = getIntent();
        String foodType = intent.getStringExtra("foodType");
        getRestaurants(foodType);

    }


    private void getRestaurants(String foodType) {
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(foodType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mRestaurants = yelpService.processResults(response);
                SearchRestaurantActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: " + mRestaurants.toString());
                        RestaurantListAdapter adapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                        mRecyclerView.setAdapter(adapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(SearchRestaurantActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }

        });
    }
}
