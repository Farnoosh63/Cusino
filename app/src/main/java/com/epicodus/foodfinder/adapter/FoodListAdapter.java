package com.epicodus.foodfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.models.Food;
import com.epicodus.foodfinder.ui.FoodDetailActivity;
import com.squareup.picasso.Picasso;


import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {
    private ArrayList<Food> mFoods = new ArrayList<>();
    private Context mContext;


    public FoodListAdapter(Context context, ArrayList<Food> foods) {
        mContext = context;
        mFoods = foods;


    }

    @Override
    public FoodListAdapter.FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list, parent, false);
        FoodViewHolder viewHolder = new FoodViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodListAdapter.FoodViewHolder holder, int position) {
        holder.bindFood(mFoods.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.foodNameTextView)
        TextView mFoodNameTextView;
        @Bind(R.id.ingredientsTextView)
        TextView mIngredientsTextView;
        @Bind(R.id.foodImageView)
        ImageView mFoodImageView;

        private Context mContext;

        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindFood(Food food) {
            mFoodNameTextView.setText(food.getTitle());
            mIngredientsTextView.setText(food.getIngredients());

            if (food.getImage().isEmpty()) {
                Picasso.with(mContext)
                        .load("http://seo.tehnoseo.ru/img/not-available.png")
                        .resize(400, 200)
                        .centerCrop()
                        .into(mFoodImageView);

            } else{
                Picasso.with(mContext)
                        .load(food.getImage())
                        .resize(400, 200)
                        .centerCrop()
                        .into(mFoodImageView);
            }


        }

        @Override
        public void onClick(View v){
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, FoodDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("foods", Parcels.wrap(mFoods));
            mContext.startActivity(intent);
        }

    }

}