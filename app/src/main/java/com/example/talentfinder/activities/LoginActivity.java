package com.example.talentfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talentfinder.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        setOnClickBtnLogin();
        setOnClickTvRegisterUser();
    }

    // On "login" button click, check to see if user info is correct and if so, navigate to MainActivity
    public void setOnClickBtnLogin(){
        binding.activityLoginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.activityLoginEtUsername.getText().toString();
                String password = binding.activityLoginEtPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Username or password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Log in failed");
                            Toast.makeText(LoginActivity.this, "One or more fields are incorrect.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        goMainActivity();
                    }
                });
            }
        });
    }

    public void setOnClickTvRegisterUser(){
        binding.activityLoginTvRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegisterActivity();
            }
        });
    }

    public void goMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void goRegisterActivity(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}