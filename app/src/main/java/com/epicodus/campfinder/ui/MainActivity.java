package com.epicodus.campfinder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.epicodus.campfinder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.radioButtonYes) RadioButton mRadioButtonYes;
    @Bind(R.id.radioButtonNo) RadioButton mRadioButtonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRadioButtonYes.setOnClickListener(this);
        mRadioButtonNo.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        if (v == mRadioButtonYes) {
            Intent intent = new Intent(MainActivity.this, ExistingUserActivity.class);
            startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
        }
    }

}


