package com.epicodus.foodfinder.ui;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.foodfinder.Constants;
import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.adapter.FirebaseAllPostViewHolder;
import com.epicodus.foodfinder.models.Food;
import com.epicodus.foodfinder.services.YelpService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity implements View.OnClickListener  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mAllPostReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;




    @Bind(R.id.userinfo) TextView mUserInfo;
    @Bind(R.id.findFoodsButton) Button mFindFoodsButton;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.userName) TextView mUserName;
    @Bind(R.id.seeAllRecipeButton) Button mSeeAllRecipeButton;
    @Bind(R.id.findRestaurantsButton) Button mFindRestaurantButton;
    @Bind(R.id.foodTypeEditText)
    EditText mFoodTypeEditText;



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
        mUserName.setText(userName + "'s saved Recipes:");

        mAllPostReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_RECIPE)
                .child(uid);

        setUpFirebaseAdapter();
        mFindFoodsButton.setOnClickListener(this);
        mSeeAllRecipeButton.setOnClickListener(this);
        mFindRestaurantButton.setOnClickListener(this);
//        getRestaurants(foodType);



    }
    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Food, FirebaseAllPostViewHolder>
                (Food.class, R.layout.user_saved_recipe_list, FirebaseAllPostViewHolder.class, mAllPostReference) {
            @Override
            protected void populateViewHolder(FirebaseAllPostViewHolder viewHolder,Food model, int position) {
                viewHolder.bindAllPost(model);

            }
        };
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mFirebaseAdapter.cleanup();
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
            String foodType = mFoodTypeEditText.getText().toString();
            Intent intent = new Intent(UserActivity.this, SearchRestaurantActivity.class);
            intent.putExtra("foodType", foodType);
            Log.d("foodTypeUserActivity", foodType);
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
