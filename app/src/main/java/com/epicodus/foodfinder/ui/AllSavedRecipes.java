package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.epicodus.foodfinder.Constants;
import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.adapter.FirebaseAllPostViewHolder;
import com.epicodus.foodfinder.adapter.FirebaseUserRestaurantViewHolder;
import com.epicodus.foodfinder.models.Food;
import com.epicodus.foodfinder.models.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.RvJoiner;

public class AllSavedRecipes extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.goBackToFavoritesButton)
    Button mGoBackToFavoritesButton;

    private DatabaseReference mAllPostReference;
    private DatabaseReference mAllRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter1;
    private FirebaseRecyclerAdapter mFirebaseAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_saved_recipes);
        ButterKnife.bind(this);

        mAllPostReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_RECIPE_2);

        mAllRestaurantReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_RESTAURANT_2);

        setUpFirebaseAdapter();

        mGoBackToFavoritesButton.setOnClickListener(this);

    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter1 = new FirebaseRecyclerAdapter<Food, FirebaseAllPostViewHolder>
                (Food.class, R.layout.user_saved_recipe_list, FirebaseAllPostViewHolder.class, mAllPostReference) {
            @Override
            protected void populateViewHolder(FirebaseAllPostViewHolder viewHolder, Food model, int position) {
                viewHolder.bindAllPost(model);

            }
        };

            mFirebaseAdapter2=new FirebaseRecyclerAdapter<Restaurant, FirebaseUserRestaurantViewHolder>
                    (Restaurant.class,R.layout.user_saved_restarurant_list,FirebaseUserRestaurantViewHolder.class, mAllRestaurantReference) {
                @Override
                protected void populateViewHolder (FirebaseUserRestaurantViewHolder
                viewHolder, Restaurant model,int position){
                viewHolder.bindUserRestaurant(model);
            }
        };


        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(mFirebaseAdapter1);

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
    public void onClick(View view) {
        if (view == mGoBackToFavoritesButton){
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        }
    }
}
