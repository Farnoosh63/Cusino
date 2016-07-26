package com.epicodus.foodfinder.ui;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FoodDetailFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.foodImageView)
    ImageView mImageLabel;
    @Bind(R.id.foodNameTextView)
    TextView mFoodNameTextView;
    @Bind(R.id.ingredientsTextView)
    TextView mIngredientsTextView;
    @Bind(R.id.websiteTextView)
    TextView mWebsiteLabel;
    @Bind(R.id.saveFoodButton)
    Button mSaveFoodButton;
    @Bind(R.id.messageToFriend) TextView mMessageToFriend;


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
        mMessageToFriend.setOnClickListener(this);
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
        if (view == mSaveFoodButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();

            mSavedRecipe = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_SAVED_RECIPE).child(uid);
            DatabaseReference mSavedRecipe2 = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_SAVED_RECIPE_2);

            if (mFood.getImage().isEmpty()) {
                String image = "http://seo.tehnoseo.ru/img/not-available.png";
                mFood.setImage(image);

            }
            DatabaseReference pushRef = mSavedRecipe.push();
            DatabaseReference pushRef2 = mSavedRecipe2.push();
            String pushId = pushRef.getKey();
            String pushId2 = pushRef2.getKey();
            mFood.setPushId(pushId);

            pushRef.setValue(mFood);
            pushRef2.setValue(mFood);

            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            Intent refresh = new Intent(this.getActivity(), UserActivity.class);
            startActivity(refresh);
        }
        if (view == mWebsiteLabel) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mFood.getLink()));
            startActivity(webIntent);

        }if (view == mMessageToFriend){
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_TEXT , "Hey There! I am using Cusino app for Android. Check out this recipe: " +mFood.getTitle()+ " link: "+  mFood.getLink());
            startActivity(emailIntent);

        }
    }

}
