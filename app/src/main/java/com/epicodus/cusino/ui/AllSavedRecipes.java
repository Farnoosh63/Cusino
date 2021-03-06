package com.epicodus.cusino.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.epicodus.cusino.Constants;
import com.epicodus.cusino.R;
import com.epicodus.cusino.adapter.FirebaseAllPostViewHolder;
import com.epicodus.cusino.adapter.FirebaseUserRestaurantViewHolder;
import com.epicodus.cusino.models.Food;
import com.epicodus.cusino.models.Restaurant;
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
        RvJoiner rvJoiner = new RvJoiner();


        if(mAllPostReference != null){
            mFirebaseAdapter1 = new FirebaseRecyclerAdapter<Food, FirebaseAllPostViewHolder>
                    (Food.class, R.layout.user_saved_recipe_list, FirebaseAllPostViewHolder.class, mAllPostReference) {
                @Override
                protected void populateViewHolder(FirebaseAllPostViewHolder viewHolder, Food model, int position) {
                    viewHolder.bindAllPost(model);

                }
            };
            rvJoiner.add(new JoinableAdapter(mFirebaseAdapter1));
        }

        if(mAllRestaurantReference != null) {

            mFirebaseAdapter2 = new FirebaseRecyclerAdapter<Restaurant, FirebaseUserRestaurantViewHolder>
                    (Restaurant.class, R.layout.user_saved_restarurant_list, FirebaseUserRestaurantViewHolder.class, mAllRestaurantReference) {
                @Override
                protected void populateViewHolder(FirebaseUserRestaurantViewHolder
                                                          viewHolder, Restaurant model, int position) {

                    viewHolder.bindUserRestaurant(model);
                }
            };
            rvJoiner.add(new JoinableAdapter(mFirebaseAdapter2));
        }

        mRecyclerView.setHasFixedSize(true);
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
    public void onClick(View view) {
        if (view == mGoBackToFavoritesButton) {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        }

//        if (view == mRecyclerView) {
//            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mFood.getLink()));
//            startActivity(webIntent);
//        }
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
}
