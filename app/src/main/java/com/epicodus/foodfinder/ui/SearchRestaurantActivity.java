package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
//    @Bind(R.id.error) TextView mError;
    private RestaurantListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private String mRecentRestaurantSearched;
    private SharedPreferences.Editor mEditor;

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);

        ButterKnife.bind(this);



        Intent intent = getIntent();
        String foodType = intent.getStringExtra("foodType");
        getRestaurants(foodType);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentRestaurantSearched = mSharedPreferences.getString(Constants.PREFERENCES_RESTAURANT_KEY, null);
//        Log.d("RECENTRESTAURANT", mRecentRestaurantSearched);
        if (mRecentRestaurantSearched != null) {
             getRestaurants(mRecentRestaurantSearched);
        }

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
//                        if(mRestaurants.toString()== "[]") {
//                            mError.setText( "Please search by cuisine type, ex: italian, southern, mexican, persian, etc");
//                        }else {
//                            Log.d(TAG, "run: " + mRestaurants.toString());
                            RestaurantListAdapter adapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                            mRecyclerView.setAdapter(adapter);
                            RecyclerView.LayoutManager layoutManager =
                                    new LinearLayoutManager(SearchRestaurantActivity.this);
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setHasFixedSize(true);
//                        }
                    }
                });
            }

        });
    }

    private void addToSharedPreferences(String foodType) {
        mEditor.putString(Constants.PREFERENCES_RESTAURANT_KEY, foodType).apply();
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
                getRestaurants(query);
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
