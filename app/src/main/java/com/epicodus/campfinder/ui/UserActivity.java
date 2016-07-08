package com.epicodus.campfinder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.epicodus.campfinder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity {
    private String[] listOfCamps = new String[] {"Diamond Lake", "Timothy Lake",
            "Mount Hood National Forest", "Lake of the Woods", "Crescent Lake",
            "Clear Lake", "Umpqua National Forest" };
    @Bind(R.id.userinfo) TextView mUserInfo;
    @Bind(R.id.listView) ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mUserInfo.setText("Welcome "+ name);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfCamps);
        mListView.setAdapter(adapter);
    }
}
