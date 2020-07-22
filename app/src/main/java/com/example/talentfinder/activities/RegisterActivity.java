package com.example.talentfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.adapters.ChipAdapter;
import com.example.talentfinder.databinding.ActivityRegisterBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.interfaces.ParseUserKey;
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
    ChipAdapter skillsAdapter;

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recycler view and adapter creation
        linearLayoutManager = new LinearLayoutManager(this);
        skills = new ArrayList<>();
        skillsAdapter = new ChipAdapter(this, skills, GlobalConstants.CHIP_ENTRY);
        binding.activityRegisterRvSkills.setAdapter(skillsAdapter);
        binding.activityRegisterRvSkills.setLayoutManager(linearLayoutManager);

        setOnKeyEtSkills();
        setOnClickBtnCreateAccount();

//        builder = MaterialDatePicker.Builder.datePicker();
//        picker = builder.build();
//        picker.show(getSupportFragmentManager(), picker.toString());
    }

    private void setOnKeyEtSkills(){
        binding.activityRegisterEtSkills.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    skills.add(binding.activityRegisterEtSkills.getText().toString());
                    binding.activityRegisterEtSkills.setText("");
                    skillsAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }

    private void setOnClickBtnCreateAccount(){
        binding.activityRegisterBtnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount(){
        String location = binding.activityRegisterEtCity.getText().toString() + ", " +
                binding.activityRegisterEtState.getText().toString() + ", " +
                binding.activityRegisterEtCountry.getText().toString();
        ParseUser newUser = new ParseUser();

        newUser.setUsername(binding.activityRegisterEtUsername.getText().toString());
        newUser.setPassword(binding.activityRegisterEtPassword.getText().toString());
        newUser.put(ParseUserKey.PROFILE_NAME, binding.activityRegisterEtName.getText().toString());
        newUser.put(ParseUserKey.PROFILE_LOCATION, location);
        newUser.put(ParseUserKey.PROFILE_SKILLS_EXPERIENCE, skills);
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
}