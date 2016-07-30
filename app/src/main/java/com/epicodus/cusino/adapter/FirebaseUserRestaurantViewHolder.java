package com.epicodus.cusino.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.cusino.R;

import com.epicodus.cusino.models.Restaurant;

import com.squareup.picasso.Picasso;



public class FirebaseUserRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View mView;
    Context mContext;

    private Restaurant mRestaurant;



    public FirebaseUserRestaurantViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);

    }

    public void bindUserRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
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

    @Override
    public void onClick(View v) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRestaurant.getWebsite()));
        mContext.startActivity(webIntent);
    }
}
