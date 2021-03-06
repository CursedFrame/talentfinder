package com.example.talentfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talentfinder.databinding.ActivityLoginBinding;
import com.example.talentfinder.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private static final String EMAIL = "email";
    private static final String LOCATION = "user_location";
    private static final String LINK = "user_link";
    private static final String PUBLIC_PROFILE = "public_profile";

    ActivityLoginBinding binding;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        // Callback manager for Facebook login callback
        callbackManager = CallbackManager.Factory.create();

        setOnClickBtnFacebookLogin();
        setOnClickBtnLogin();
        setOnClickTvRegisterUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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

    public void setOnClickBtnFacebookLogin(){

        binding.activityLoginFbLoginButton.setReadPermissions(Arrays.asList(EMAIL, LOCATION, LINK));
        binding.activityLoginFbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final AccessToken accessToken = loginResult.getAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                // Request user profile from Facebook Graph
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    final JSONObject facebookObject,
                                    GraphResponse response) {
                                try {
                                    final String userId = facebookObject.getString("id");
                                    final String userName = facebookObject.getString("name");
                                    final String userLocation = facebookObject.getJSONObject("location").getString("name");
                                    final String userLink = facebookObject.getString("link");

                                    ParseUser.getQuery()
                                            .whereEqualTo(User.KEY_USERNAME, userId)
                                            .getFirstInBackground(new GetCallback<ParseUser>() {
                                                @Override
                                                public void done(ParseUser object, ParseException e) {
                                                    // If a user exists, log in, else, go to populated RegisterActivity
                                                    if (e != null){
                                                        Log.e(TAG, "done: exception here", e);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("userId", userId);
                                                        bundle.putString("userName", userName);
                                                        bundle.putString("userLocation", userLocation);
                                                        bundle.putString("facebookLink", userLink);
                                                        goRegisterActivityForFacebook(bundle);
                                                        return;
                                                    }

                                                    ParseUser.logInInBackground(userId, userId, new LogInCallback() {
                                                        @Override
                                                        public void done(ParseUser user, ParseException e) {
                                                            if (e != null){
                                                                Log.e(TAG, "Log in failed");
                                                                Toast.makeText(LoginActivity.this, "Facebook login failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                            goMainActivity();
                                                        }
                                                    });
                                                }
                                            });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, link, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: Facebook login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: Problem with Facebook login", error);
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
        finish();
    }

    public void goRegisterActivity(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("facebookCheck", false);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void goRegisterActivityForFacebook(Bundle bundle){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        bundle.putBoolean("facebookCheck", true);
        intent.putExtra("bundle",  bundle);
        startActivity(intent);
    }
}