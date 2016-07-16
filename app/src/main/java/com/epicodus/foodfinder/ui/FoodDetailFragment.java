package com.epicodus.foodfinder.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.models.Food;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FoodDetailFragment extends Fragment {
    @Bind(R.id.foodImageView)
    ImageView mImageLabel;
    @Bind(R.id.foodNameTextView)
    TextView mFoodNameTextView;
    @Bind(R.id.ingredientsTextView) TextView mIngredientsTextView;

    private Food mFood;
    private Context mContext;



    public static FoodDetailFragment newInstance(Food food) {
        FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("food", Parcels.wrap(food));
        foodDetailFragment.setArguments(args);
        return foodDetailFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFood = Parcels.unwrap(getArguments().getParcelable("food"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        ButterKnife.bind(this, view);
//        Picasso.with(view.getContext()).load(mFood.getImage()).into(mImageLabel);

        mFoodNameTextView.setText(mFood.getTitle());
        mIngredientsTextView.setText(mFood.getIngredients());
        if (mFood.getImage().isEmpty()) {
            Picasso.with(mContext)
                    .load("http://seo.tehnoseo.ru/img/not-available.png")
                    .resize(400, 200)
                    .centerCrop()
                    .into(mImageLabel);

        } else{
            Picasso.with(mContext)
                    .load(mFood.getImage())
                    .resize(400, 200)
                    .centerCrop()
                    .into(mImageLabel);
        }
        return view;
    }

}
