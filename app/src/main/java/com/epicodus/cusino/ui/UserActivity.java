package com.epicodus.cusino.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.cusino.Constants;
import com.epicodus.cusino.R;
import com.epicodus.cusino.adapter.FirebaseAllPostViewHolder;
import com.epicodus.cusino.adapter.FirebaseUserRestaurantViewHolder;
import com.epicodus.cusino.models.Food;
import com.epicodus.cusino.models.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.RvJoiner;

public class UserActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mAllPostReference;
    private DatabaseReference mUserRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter1;
    private FirebaseRecyclerAdapter mFirebaseAdapter2;
    private RvJoiner rvJoiner = new RvJoiner(true);//auto update ON, stable ids ON

    public static final String TAG = SearchRestaurantActivity.class.getSimpleName();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient googleApiClient;

    private double mLatitude;
    private double mLongitude;
    private String zipcode;




    @Bind(R.id.userinfo) TextView mUserInfo;
    @Bind(R.id.findFoodsButton) Button mFindFoodsButton;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.userName) TextView mUserName;
    @Bind(R.id.seeAllRecipeButton) Button mSeeAllRecipeButton;
    @Bind(R.id.findRestaurantsButton) Button mFindRestaurantButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
//        boolean permissionGranted = isPermissionGranted();
//        if (permissionGranted) {
//           // getLocationInfo();
//        } else {
//            Log.d("permission", "not yet granted");
//        }

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    getSupportActionBar().setTitle("Welcome, " + user.getDisplayName() + "!");
                }
            }
        };


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        String userName = user.getDisplayName();
        mUserName.setText(userName + "'s saved Recipes and Restaurants:");

        mAllPostReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_RECIPE)
                .child(uid);

        mUserRestaurantReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_RESTAURANT)
                .child(uid);

        setUpFirebaseAdapter();
        mFindFoodsButton.setOnClickListener(this);
        mSeeAllRecipeButton.setOnClickListener(this);
        mFindRestaurantButton.setOnClickListener(this);

        Log.d(TAG, "onCreate: zipcode is" + zipcode);
    }

    private boolean isPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(this, UserActivity.class);
//                    startActivity(intent);

                    mFindRestaurantButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UserActivity.this, SearchRestaurantActivity.class);
                            intent.putExtra("zipcode", getLocationInfo());
                            startActivity(intent);
                        }
                    });
//                    getLocationInfo();
                    Log.d("permission", "granted");
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void setUpFirebaseAdapter() {
        mFirebaseAdapter1 = new FirebaseRecyclerAdapter<Food, FirebaseAllPostViewHolder>
                (Food.class, R.layout.user_saved_recipe_list, FirebaseAllPostViewHolder.class, mAllPostReference) {

            @Override
            protected void populateViewHolder(FirebaseAllPostViewHolder viewHolder, Food model, int position) {
                viewHolder.bindAllPost(model);
            }
        };
            mFirebaseAdapter2 = new FirebaseRecyclerAdapter<Restaurant, FirebaseUserRestaurantViewHolder>
                    (Restaurant.class, R.layout.user_saved_restarurant_list, FirebaseUserRestaurantViewHolder.class, mUserRestaurantReference) {


                @Override
                protected void populateViewHolder(FirebaseUserRestaurantViewHolder viewHolder, Restaurant model, int position) {
                    viewHolder.bindUserRestaurant(model);
                }


        };
        mRecyclerView.setHasFixedSize(true);
        //construct a joiner
        RvJoiner rvJoiner = new RvJoiner();

        rvJoiner.add(new JoinableAdapter(mFirebaseAdapter1));
        rvJoiner.add(new JoinableAdapter(mFirebaseAdapter2));

        //init your RecyclerView as usual
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(rvJoiner.getAdapter());

    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        mFirebaseAdapter1.cleanup();
        mFirebaseAdapter2.cleanup();

    }


    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_main, menu);
                return super.onCreateOptionsMenu(menu);
            }

    @Override
        public boolean onOptionsItemSelected(MenuItem user){
            int id = user.getItemId();
            if(id == R.id.action_logout){
                logout();
                return true;
            }
        return super.onOptionsItemSelected(user);

    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mFindFoodsButton){
            Intent intent = new Intent(UserActivity.this, SearchActivity.class);
            startActivity(intent);
//            finish();
        }
        if(v == mSeeAllRecipeButton){
            Intent intent = new Intent(UserActivity.this, AllSavedRecipes.class);
            startActivity(intent);
            //finish();
        }
//        if(v == mFindRestaurantButton){
//
//
//            Intent intent = new Intent(UserActivity.this, SearchRestaurantActivity.class);
////            zipcode = getLocationInfo();
////            intent.putExtra("zipcode", getLocationInfo());
////            Log.d(TAG, "onClick zipcode is " + zipcode);
//            startActivity(intent);
//
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        googleApiClient.disconnect();

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
            return;
        }


//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
////            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
////
////            mLatitude = lastLocation.getLatitude();
////            mLongitude = lastLocation.getLongitude();
////            Log.d(TAG, "onConnected: " + mLatitude);
////            Log.d(TAG, "user city: " + getLocationInfo());
//
//        }
    }

    @SuppressWarnings({"MissingPermission"})
    public final String getLocationInfo() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        String lat = lastLocation.getLatitude() + "";

        Log.d("lat", lat);
        Geocoder geocoder = new Geocoder(UserActivity.this, Locale.getDefault());
        ArrayList name = new ArrayList();
        try {
            List<Address> address = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
//            Address userLocationInfo = address.get(0);
//            name.add(userLocationInfo.getAddressLine(0));
            String city=address.get(0).getLocality();
            Log.d(TAG, "city is: "+ city);
            zipcode =address.get(0).getPostalCode();
            Log.d(TAG, "postal code is: "+ zipcode);
//            Intent intent = new Intent(this, SearchRestaurantActivity.class);
//            intent.putExtra("zipcode", zipcode);


        } catch (IOException e) {
            Toast.makeText(UserActivity.this, "Location not found.", Toast.LENGTH_SHORT).show();
            zipcode="";
        }
        return zipcode;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }
}
