package com.example.talentfinder.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.activities.MainActivity;
import com.example.talentfinder.databinding.FragmentCreateBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Project;
import com.example.talentfinder.utilities.MediaFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

// Fragment for allowing user to create a project and upload to Parse server
public class CreateFragment extends MediaFragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "CreateFragment";

    private FragmentCreateBinding binding;
    private File photoFile;
    private FragmentManager fragmentManager;
    private Context context;

    public CreateFragment() {
        // Required empty public constructor
    }

    public static CreateFragment newInstance() {
        CreateFragment fragment = new CreateFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getFragmentManager();
        context = getContext();

        Glide.with(context)
                .load("https://generative-placeholders.glitch.me/image?width=600&height=300&style=triangles&gap=30")
                .into(binding.fragmentCreateProjectIvContext);

        // Array adapter for "Skill" spinner
        ArrayAdapter<CharSequence> spnSkillAdapter = ArrayAdapter.createFromResource(context, R.array.skill, R.layout.support_simple_spinner_dropdown_item);
        spnSkillAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.fragmentCreateProjectSpnSkill.setAdapter(spnSkillAdapter);

        // Array adapter for "Talent" spinner
        ArrayAdapter<CharSequence> spnTalentAdapter = ArrayAdapter.createFromResource(context, R.array.talent, R.layout.support_simple_spinner_dropdown_item);
        spnTalentAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.fragmentCreateProjectSpnTalent.setAdapter(spnTalentAdapter);

        binding.fragmentCreateProjectSpnTalent.setOnItemSelectedListener(this);
        binding.fragmentCreateProjectSpnSubtalent.setOnItemSelectedListener(this);
        binding.fragmentCreateProjectSpnSkill.setOnItemSelectedListener(this);

        // On "import" button click, move to media gallery activity and allow user to pick photo
        binding.fragmentCreateProjectBtnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        // Create project on "Create" button press
        binding.fragmentCreateProjectBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Project project = new Project();
                project.setUser(ParseUser.getCurrentUser());
                project.setTitle(binding.fragmentCreateProjectEtTitle.getText().toString());
                if (photoFile != null) {
                    project.setImage(new ParseFile(photoFile));
                }
                project.setDescription(binding.fragmentCreateProjectEtDescription.getText().toString());
                project.setTalentTag(binding.fragmentCreateProjectSpnTalent.getSelectedItem().toString());
                project.setSubTalentTag(binding.fragmentCreateProjectSpnSubtalent.getSelectedItem().toString());
                project.setSkillTag(binding.fragmentCreateProjectSpnSkill.getSelectedItem().toString());
                project.setContributionCount(0);
                project.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Error while saving project", e);
                            return;
                        }
                        ParseUser.getCurrentUser().getRelation(ParseUserKey.CURRENT_PROJECTS).add(project);
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null){
                                    Log.e(TAG, "Error while saving project to user", e);
                                    return;
                                }
                                Log.i(TAG, "Contribution saved successfully");
                                MainActivity mainActivity = (MainActivity) getActivity();
                                Fragment fragment = HomeFeedFragment.newInstance(mainActivity.projects);
                                fragmentManager.beginTransaction().replace(R.id.includeMainViewContainer_mainContainer, fragment).commit();
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && requestCode == GlobalConstants.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadImageFromUri(photoUri);

            // Load the selected image into a preview
            binding.fragmentCreateProjectIvContext.setImageBitmap(selectedImage);

            // Change bitmap to File and input into photoFile
            try {
                photoFile = bitmapToFile(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ArrayAdapter<CharSequence> spnSubTalentAdapter = null;

        if (parent.getId() == R.id.fragmentCreateProject_spnTalent) {
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
                binding.fragmentCreateProjectSpnSubtalent.setVisibility(View.GONE);
            }
            else {
                binding.fragmentCreateProjectSpnSubtalent.setVisibility(View.VISIBLE);
            }

            binding.fragmentCreateProjectSpnSubtalent.setAdapter(spnSubTalentAdapter);
        }

        if (!binding.fragmentCreateProjectSpnTalent.getSelectedItem().toString().equals(GlobalConstants.TALENT_TAG)
                && !binding.fragmentCreateProjectSpnSubtalent.getSelectedItem().toString().equals(GlobalConstants.SUBTALENT_TAG)
                && !binding.fragmentCreateProjectSpnSkill.getSelectedItem().toString().equals(GlobalConstants.SKILL_TAG)) {
            binding.fragmentCreateProjectBtnCreate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}