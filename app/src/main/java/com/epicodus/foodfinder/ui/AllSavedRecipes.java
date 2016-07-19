package com.epicodus.foodfinder.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.foodfinder.Constants;
import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.adapter.FirebaseAllPostViewHolder;
import com.epicodus.foodfinder.models.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AllSavedRecipes extends AppCompatActivity {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private DatabaseReference mAllPostReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_saved_recipes);
        ButterKnife.bind(this);

        mAllPostReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_RECIPE_2);
        setUpFirebaseAdapter();

    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Food, FirebaseAllPostViewHolder>
                (Food.class, R.layout.all_user_saved_recipe_list, FirebaseAllPostViewHolder.class, mAllPostReference) {
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
}
