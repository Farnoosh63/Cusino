package com.epicodus.foodfinder.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.models.Food;
import com.squareup.picasso.Picasso;

public class FirebaseAllPostViewHolder extends RecyclerView.ViewHolder {
    View mView;
    Context mContext;

    public FirebaseAllPostViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }


    public void bindAllPost(Food food) {
        ImageView foodImage = (ImageView) mView.findViewById(R.id.foodImage);
        TextView foodName = (TextView) mView.findViewById(R.id.foodName);
        TextView ingredients = (TextView) mView.findViewById(R.id.ingredients);

        foodName.setText(food.getTitle());
        ingredients.setText(food.getIngredients());
        Picasso.with(mContext)
                .load(food.getImage())
                .resize(400, 200)
                .centerCrop()
                .into(foodImage);

    }
}
