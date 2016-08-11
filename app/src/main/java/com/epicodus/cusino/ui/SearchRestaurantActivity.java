package com.epicodus.cusino.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.epicodus.cusino.Constants;
import com.epicodus.cusino.R;
import com.epicodus.cusino.adapter.RestaurantListAdapter;
import com.epicodus.cusino.models.Restaurant;
import com.epicodus.cusino.services.YelpService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SearchRestaurantActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = SearchRestaurantActivity.class.getSimpleName();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient googleApiClient;


    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private String mRecentRestaurantSearched;
    private SharedPreferences.Editor mEditor;

    private double mLatitude;
    private double mLongitude;

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);

        ButterKnife.bind(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();


        Intent intent = getIntent();

        mSharedPreferences = getDefaultSharedPreferences(this);
        mRecentRestaurantSearched = mSharedPreferences.getString(Constants.PREFERENCES_RESTAURANT_KEY, null);

        if (mRecentRestaurantSearched != null) {
            getRestaurants(mRecentRestaurantSearched);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
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

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            Log.d(TAG, "onConnected: "+ lat);

        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }


}
