package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.epicodus.foodfinder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btnSingUp) Button mButtonNo;
    @Bind(R.id.btnSignIn) Button mButtonYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mButtonYes.setOnClickListener(this);
        mButtonNo.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        if (v == mButtonYes) {
            Intent intent = new Intent(MainActivity.this, ExistingUserActivity.class);
            startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
        }
    }

}


