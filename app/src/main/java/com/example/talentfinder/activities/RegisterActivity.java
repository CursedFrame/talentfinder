package com.example.talentfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.adapters.SkillsAdapter;
import com.example.talentfinder.databinding.ActivityRegisterBinding;
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";

//    MaterialDatePicker.Builder<Long> builder;
//    MaterialDatePicker<Long> picker;

    LinearLayoutManager linearLayoutManager;
    List<String> skills;
    SkillsAdapter skillsAdapter;

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recycler view and adapter creation
        linearLayoutManager = new LinearLayoutManager(this);
        skills = new ArrayList<>();
        skillsAdapter = new SkillsAdapter(this, skills);
        binding.rvSkills.setAdapter(skillsAdapter);
        binding.rvSkills.setLayoutManager(linearLayoutManager);

        setOnEnterEtSkills();

        setOnClickBtnCreateAccount();
//        builder = MaterialDatePicker.Builder.datePicker();
//        picker = builder.build();
//        picker.show(getSupportFragmentManager(), picker.toString());
    }

    public void setOnEnterEtSkills(){
        binding.etSkills.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    skills.add(binding.etSkills.getText().toString());
                    binding.etSkills.setText("");
                    skillsAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }

    public void setOnClickBtnCreateAccount(){
        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = binding.etCity.getText().toString() + ", " + binding.etState.getText().toString() + ", " + binding.etCountry.getText().toString();
                ParseUser newUser = new ParseUser();

                newUser.setUsername(binding.etUsername.getText().toString());
                newUser.setPassword(binding.etPassword.getText().toString());
                newUser.put(Key_ParseUser.PROFILE_NAME, binding.etName.getText().toString());
                newUser.put(Key_ParseUser.PROFILE_LOCATION, location);
                newUser.put(Key_ParseUser.PROFILE_SKILLS_EXPERIENCE, skills);
                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Error signing up", e);
                            return;
                        }
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}