package com.epicodus.foodfinder.ui;

import android.content.Intent;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.epicodus.foodfinder.Constants;
import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.adapter.FirebaseAllPostViewHolder;
import com.epicodus.foodfinder.adapter.FirebaseUserRestaurantViewHolder;
import com.epicodus.foodfinder.models.Food;

import com.epicodus.foodfinder.models.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




import butterknife.Bind;
import butterknife.ButterKnife;
import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.RvJoiner;

public class UserActivity extends AppCompatActivity implements View.OnClickListener  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mAllPostReference;
    private DatabaseReference mUserRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter1;
    private FirebaseRecyclerAdapter mFirebaseAdapter2;
    private RvJoiner rvJoiner = new RvJoiner(true);//auto update ON, stable ids ON





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


        //init your RecyclerView as usual
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //construct a joiner
        RvJoiner rvJoiner = new RvJoiner();

//        rvJoiner.add(new JoinableLayout(R.layout.user_saved_recipe_list));
        rvJoiner.add(new JoinableAdapter(mFirebaseAdapter1));

//        rvJoiner.add(new JoinableLayout(R.layout.user_saved_restarurant_list));
        rvJoiner.add(new JoinableAdapter(mFirebaseAdapter2));

        //set join adapter to your RecyclerView
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
        if(v == mFindRestaurantButton){


            Intent intent = new Intent(UserActivity.this, SearchRestaurantActivity.class);
            startActivity(intent);

        }

    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
