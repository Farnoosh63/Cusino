package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.foodfinder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExistingUserActivity extends AppCompatActivity {

    @Bind(R.id.password) EditText mPassword;
    @Bind(R.id.name) EditText mName;
    @Bind(R.id.loginbutton) Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);

        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String password = mPassword.getText().toString();
                Intent intent = new Intent(ExistingUserActivity.this, UserActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
