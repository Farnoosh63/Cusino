package com.epicodus.campfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.epicodus.campfinder.R;
import com.epicodus.campfinder.models.Food;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 7/8/16.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {
    private ArrayList<Food> mFoods = new ArrayList<>();
    private Context mContext;


    public FoodListAdapter(Context context, ArrayList<Food> foods) {
        mContext = context;
        mFoods = foods;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foodNameTextView) TextView mFoodNameTextView;
        @Bind(R.id.ingredientsTextView) TextView mIngredientsTextView;


        private Context mContext;

        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindFood(Food food) {
            mFoodNameTextView.setText(food.getTitle());
            mIngredientsTextView.setText(food.getIngredients());
        }
    }
}