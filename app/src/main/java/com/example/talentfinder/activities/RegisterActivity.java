package com.example.talentfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.R;
import com.example.talentfinder.adapters.ChipAdapter;
import com.example.talentfinder.databinding.ActivityRegisterBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.User;
import com.facebook.login.LoginManager;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "RegisterActivity";

    LinearLayoutManager linearLayoutManager;
    List<String> skills;
    ChipAdapter skillsAdapter;
    private Context context;

    boolean facebookCheck;
    int userId;
    String userName;
    String userLocation;
    String userLink;
    String userProfilePicture;

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        Bundle loginActivityData = getIntent().getBundleExtra("bundle");

        // Boolean to check if activity was triggered by Facebook login
        facebookCheck = loginActivityData.getBoolean("facebookCheck");

        if (facebookCheck) {
            userId = loginActivityData.getInt("userId");
            userName = loginActivityData.getString("userName");
            userLocation = loginActivityData.getString("userLocation");
            userLink = loginActivityData.getString("facebookLink");
            userProfilePicture = loginActivityData.getString("facebookPicture");
            String[] userLocationArray = userLocation.split(", ", 2);


            binding.activityRegisterEtName.setText(userName);
            binding.activityRegisterEtCity.setText(userLocationArray[0]);
            binding.activityRegisterEtState.setText(userLocationArray[1]);

            binding.activityRegisterEtUsername.setVisibility(View.GONE);
            binding.activityRegisterTvUsername.setVisibility(View.GONE);
            binding.activityRegisterEtPassword.setVisibility(View.GONE);
            binding.activityRegisterTvPassword.setVisibility(View.GONE);
            binding.activityRegisterEtConfirmPassword.setVisibility(View.GONE);
            binding.activityRegisterTvConfirmPassword.setVisibility(View.GONE);
        }

//        // Recycler view and adapter creation
//        linearLayoutManager = new LinearLayoutManager(this);
//        skills = new ArrayList<>();
//        skillsAdapter = new ChipAdapter(this, skills, GlobalConstants.CHIP_ENTRY);
//        binding.activityRegisterRvSkills.setAdapter(skillsAdapter);
//        binding.activityRegisterRvSkills.setLayoutManager(linearLayoutManager);

        // Array adapter for "Skill" spinner
        ArrayAdapter<CharSequence> spnSkillAdapter = ArrayAdapter.createFromResource(this, R.array.skill, R.layout.support_simple_spinner_dropdown_item);
        spnSkillAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.activityRegisterSpnSkill.setAdapter(spnSkillAdapter);

        // Array adapter for "Talent" spinner
        ArrayAdapter<CharSequence> spnTalentAdapter = ArrayAdapter.createFromResource(this, R.array.talent, R.layout.support_simple_spinner_dropdown_item);
        spnTalentAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.activityRegisterSpnTalent.setAdapter(spnTalentAdapter);

        binding.activityRegisterSpnTalent.setOnItemSelectedListener(this);
        binding.activityRegisterSpnSubTalent.setOnItemSelectedListener(this);
        binding.activityRegisterSpnSkill.setOnItemSelectedListener(this);

        binding.activityRegisterBtnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebookCheck) {
                    try {
                        createAccountFromFacebook();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    createAccount();
                }
            }
        });
    }

    private void createAccountFromFacebook() throws IOException {
        ParseUser newUser = new ParseUser();

        newUser.setUsername(Integer.toString(userId));
        newUser.setPassword(Integer.toString(userId));
        newUser.put(User.KEY_FACEBOOK_CONNECTED, true);
        newUser.put(User.KEY_FACEBOOK_LINK, userLink);
        newUser.put(User.KEY_FACEBOOK_ID, userId);
        newUser.put(User.KEY_NAME, userName);
        newUser.put(User.KEY_LOCATION, userLocation);
//        newUser.put(ParseUserKey.PROFILE_IMAGE, new ParseFile(MediaUtils.urlToImage(userProfilePicture, this)));
        newUser.put(User.KEY_TAG_SKILL, binding.activityRegisterSpnSkill.getSelectedItem().toString());
        newUser.put(User.KEY_TAG_TALENT, binding.activityRegisterSpnTalent.getSelectedItem().toString());
        newUser.put(User.KEY_TAG_SUBTALENT, binding.activityRegisterSpnSubTalent.getSelectedItem().toString());
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error signing up", e);
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createAccount(){
        String location = binding.activityRegisterEtCity.getText().toString() + ", " +
                binding.activityRegisterEtState.getText().toString();
        ParseUser newUser = new ParseUser();

        newUser.setUsername(binding.activityRegisterEtUsername.getText().toString());
        newUser.setPassword(binding.activityRegisterEtPassword.getText().toString());
        newUser.put(User.KEY_NAME, binding.activityRegisterEtName.getText().toString());
        newUser.put(User.KEY_LOCATION, location);
        newUser.put(User.KEY_TAG_SKILL, binding.activityRegisterSpnSkill.getSelectedItem().toString());
        newUser.put(User.KEY_TAG_TALENT, binding.activityRegisterSpnTalent.getSelectedItem().toString());
        newUser.put(User.KEY_TAG_SUBTALENT, binding.activityRegisterSpnSubTalent.getSelectedItem().toString());
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error signing up", e);
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter<CharSequence> spnSubTalentAdapter = null;

        if (parent.getId() == R.id.activityRegister_spnTalent) {
            switch (position) {
                case GlobalConstants.POSITION_TALENT_NO:
                    break;
                case GlobalConstants.POSITION_TALENT_ART:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.art_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_COMEDY:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.comedy_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_DRAWING:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.drawing_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_GRAPHICS:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.graphics_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_MUSIC:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.music_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_PHOTOGRAPHY:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.photography_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_PROGRAMMING:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.programming_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_SINGING:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.singing_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_TEACHING:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.teaching_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
                case GlobalConstants.POSITION_TALENT_WRITING:
                    spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.writing_subs, R.layout.support_simple_spinner_dropdown_item);
                    break;
            }

            if (position == GlobalConstants.POSITION_TALENT_NO){
                binding.activityRegisterSpnSubTalent.setVisibility(View.GONE);
            }
            else {
                binding.activityRegisterSpnSubTalent.setVisibility(View.VISIBLE);
            }

            binding.activityRegisterSpnSubTalent.setAdapter(spnSubTalentAdapter);
        }

        if (!binding.activityRegisterSpnTalent.getSelectedItem().toString().equals(GlobalConstants.TALENT_TAG)
                && !binding.activityRegisterSpnSubTalent.getSelectedItem().toString().equals(GlobalConstants.SUBTALENT_TAG)
                && !binding.activityRegisterSpnSkill.getSelectedItem().toString().equals(GlobalConstants.SKILL_TAG)) {
            binding.activityRegisterBtnCreateAccount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        LoginManager.getInstance().logOut();
        super.onBackPressed();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}