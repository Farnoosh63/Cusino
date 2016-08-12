package com.epicodus.cusino.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.epicodus.cusino.Constants;
import com.epicodus.cusino.R;
import com.epicodus.cusino.adapter.RestaurantListAdapter;
import com.epicodus.cusino.models.Restaurant;
import com.epicodus.cusino.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SearchRestaurantActivity extends AppCompatActivity  {
    public static final String TAG = SearchRestaurantActivity.class.getSimpleName();


    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
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

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentRestaurantSearched = mSharedPreferences.getString(Constants.PREFERENCES_RESTAURANT_KEY, null);

        if (mRecentRestaurantSearched != null) {
            getRestaurants(mRecentRestaurantSearched);
        }
    }



    private void getRestaurants(String foodType) {
        Intent intent= getIntent();
        String zipcode= intent.getStringExtra("zipcode");
        Log.d(TAG, "getRestaurants zipcode is: "+ zipcode);
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(foodType,zipcode, new Callback() {
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

    private void addToSharedPreferences(String foodType) {
        mEditor.putString(Constants.PREFERENCES_RESTAURANT_KEY, foodType).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String trimQuery = query.trim();
                addToSharedPreferences(trimQuery);
                getRestaurants(trimQuery);
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
