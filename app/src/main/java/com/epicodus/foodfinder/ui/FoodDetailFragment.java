package com.epicodus.foodfinder.ui;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.foodfinder.Constants;
import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.models.Food;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FoodDetailFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.foodImageView) ImageView mImageLabel;
    @Bind(R.id.foodNameTextView) TextView mFoodNameTextView;
    @Bind(R.id.ingredientsTextView) TextView mIngredientsTextView;
    @Bind(R.id.websiteTextView) TextView mWebsiteLabel;
    @Bind(R.id.saveFoodButton)
    Button mSaveFoodButton;


    private Food mFood;
    private Context mContext;
    private DatabaseReference mSavedRecipe;


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


        mWebsiteLabel.setOnClickListener(this);
        mSaveFoodButton.setOnClickListener(this);
        mFoodNameTextView.setText(mFood.getTitle());
        mIngredientsTextView.setText(mFood.getIngredients());
        if (mFood.getImage().isEmpty()) {
            Picasso.with(mContext)
                    .load("http://seo.tehnoseo.ru/img/not-available.png")
                    .resize(400, 200)
                    .centerCrop()
                    .into(mImageLabel);

        } else {
            Picasso.with(mContext)
                    .load(mFood.getImage())
                    .resize(400, 200)
                    .centerCrop()
                    .into(mImageLabel);
        }
        return view;

    }




    @Override
    public void onClick(View view) {
        if (view == mWebsiteLabel) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mFood.getLink()));
            startActivity(webIntent);
        }
        if(view == mSaveFoodButton){
            mSavedRecipe = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_SAVED_RECIPE).push();
            if (mFood.getImage().isEmpty()) {
                String image = "http://seo.tehnoseo.ru/img/not-available.png";
                mFood.setImage(image);


            }
            String pushId = mSavedRecipe.getKey();
            mFood.setPushId(pushId);
            mSavedRecipe.setValue(mFood);
            Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
