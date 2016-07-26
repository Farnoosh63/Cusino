package com.epicodus.foodfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.models.Food;
import com.epicodus.foodfinder.models.Restaurant;
import com.squareup.picasso.Picasso;


public class FirebaseUserRestaurantViewHolder extends RecyclerView.ViewHolder  {
    View mView;
    Context mContext;

    public FirebaseUserRestaurantViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindUserRestaurant(Restaurant restaurant) {
        ImageView imageUrl = (ImageView) mView.findViewById(R.id.imageUrl);
        TextView restaurantName = (TextView) mView.findViewById(R.id.restaurantName);
        TextView categories = (TextView) mView.findViewById(R.id.categories);

        restaurantName.setText(restaurant.getName());
        categories.setText(restaurant.getCategories().get(0));
        Picasso.with(mContext)
                .load(restaurant.getImageUrl())
                .resize(400, 200)
                .centerCrop()
                .into(imageUrl);

    }
}
