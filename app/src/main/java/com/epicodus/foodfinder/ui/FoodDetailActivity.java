package com.epicodus.foodfinder.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epicodus.foodfinder.R;
import com.epicodus.foodfinder.adapter.FoodPagerAdapter;
import com.epicodus.foodfinder.models.Food;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FoodDetailActivity extends AppCompatActivity {

    @Bind(R.id.viewPager)ViewPager mViewPager;
    private FoodPagerAdapter adapterViewPager;
    ArrayList<Food> mFoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);

        mFoods= Parcels.unwrap(getIntent().getParcelableExtra("foods"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new FoodPagerAdapter(getSupportFragmentManager(),mFoods);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);


    }
}
