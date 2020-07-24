package com.example.talentfinder.activities;

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
import com.example.talentfinder.interfaces.ParseUserKey;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "RegisterActivity";

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

        // Array adapter for "Skill" spinner
        ArrayAdapter<CharSequence> spnSkillAdapter = ArrayAdapter.createFromResource(this, R.array.skill, R.layout.support_simple_spinner_dropdown_item);
        spnSkillAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.activityRegisterSpnSkill.setAdapter(spnSkillAdapter);

        // Array adapter for "Talent" spinner
        ArrayAdapter<CharSequence> spnTalentAdapter = ArrayAdapter.createFromResource(this, R.array.talent, R.layout.support_simple_spinner_dropdown_item);
        spnTalentAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.activityRegisterSpnTalent.setAdapter(spnTalentAdapter);

        // Binding listener for when talent item is selected. Initializes and updates an array adapter for "Subtalent" spinner
        binding.activityRegisterSpnTalent.setOnItemSelectedListener(this);

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
        newUser.put(ParseUserKey.TAG_SKILL, binding.activityRegisterSpnSkill.getSelectedItem().toString());
        newUser.put(ParseUserKey.TAG_TALENT, binding.activityRegisterSpnTalent.getSelectedItem().toString());
        newUser.put(ParseUserKey.TAG_SUBTALENT, binding.activityRegisterSpnSubTalent.getSelectedItem().toString());
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
        switch (position){
            case 0:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.art_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 1:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.comedy_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 2:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.drawing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 3:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.graphics_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 4:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.music_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 5:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.photography_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 6:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.programming_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 7:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.singing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 8:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.teaching_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 9:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(this, R.array.writing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            default:
                break;
        }
        binding.activityRegisterSpnSubTalent.setAdapter(spnSubTalentAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}