package com.epicodus.foodfinder.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epicodus.foodfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;


    @Bind(R.id.etUserName)
    EditText mName;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.confirmPassword)
    EditText mConfirmPassword;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.btnSingUp)
    Button mSubmitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mSubmitButton.setOnClickListener(this);

    }


        @Override
        public void onClick(View v) {
            createNewUser();

        }

    private void createNewUser() {
        final String name = mName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Task.isSuccessful", "Authentication successful");
                        } else {
                            Toast.makeText(AccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//        Intent intent = new Intent(AccountActivity.this, UserActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("name", name);
//        startActivity(intent);
     }
}



