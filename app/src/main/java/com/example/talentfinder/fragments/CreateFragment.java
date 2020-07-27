package com.example.talentfinder.fragments;

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

        // Array adapter for "Skill" spinner
        ArrayAdapter<CharSequence> spnSkillAdapter = ArrayAdapter.createFromResource(getContext(), R.array.skill, R.layout.support_simple_spinner_dropdown_item);
        spnSkillAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.fragmentCreateSpnSkill.setAdapter(spnSkillAdapter);

        // Array adapter for "Talent" spinner
        ArrayAdapter<CharSequence> spnTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.talent, R.layout.support_simple_spinner_dropdown_item);
        spnTalentAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.fragmentCreateSpnTalent.setAdapter(spnTalentAdapter);

        // Binding listener for when talent item is selected. Initializes and updates an array adapter for "Subtalent" spinner
        binding.fragmentCreateSpnTalent.setOnItemSelectedListener(this);

        // On "import" button click, move to media gallery activity and allow user to pick photo
        binding.fragmentCreateBtnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        // Create project on "Create" button press
        binding.fragmentCreateBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Project project = new Project();
                project.setUser(ParseUser.getCurrentUser());
                project.setTitle(binding.fragmentCreateEtProjectTitle.getText().toString());
                if (photoFile != null) {
                    project.setImage(new ParseFile(photoFile));
                }
                project.setDescription(binding.fragmentCreateTvDescribeProject.getText().toString());
                project.setTalentTag(binding.fragmentCreateSpnTalent.getSelectedItem().toString());
                project.setSubTalentTag(binding.fragmentCreateSpnSubTalent.getSelectedItem().toString());
                project.setSkillTag(binding.fragmentCreateSpnSkill.getSelectedItem().toString());
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
            binding.fragmentCreateIvImage.setImageBitmap(selectedImage);

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

        switch (position){
            case 0:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.art_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 1:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.comedy_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 2:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.drawing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 3:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.graphics_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 4:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.music_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 5:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.photography_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 6:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.programming_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 7:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.singing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 8:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.teaching_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 9:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(getContext(), R.array.writing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            default:
                break;
        }
        binding.fragmentCreateSpnSubTalent.setAdapter(spnSubTalentAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}