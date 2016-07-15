package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.foodfinder.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity implements View.OnClickListener  {

    @Bind(R.id.userinfo) TextView mUserInfo;
    @Bind(R.id.findFoodsButton) Button mFindFoodsButton;
    @Bind(R.id.searchFood) EditText mSearchFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mUserInfo.setText("Welcome "+ name);

        mFindFoodsButton.setOnClickListener(this);


    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_main, menu);
                return super.onCreateOptionsMenu(menu);
            }

    @Override
        public boolean onOptionsItemSelected(MenuItem user){
            int id = user.getItemId();
            if(id == R.id.action_logout){
                logout();
                return true;
            }
        return super.onOptionsItemSelected(user);

    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        String foodName = mSearchFood.getText().toString();
        Intent intent = new Intent(UserActivity.this, SearchActivity.class);
        intent.putExtra("food", foodName);
        startActivity(intent);
    }
}
