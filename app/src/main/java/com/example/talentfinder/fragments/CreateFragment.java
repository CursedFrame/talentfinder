package com.example.talentfinder.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.R;
import com.example.talentfinder.adapters.ChipAdapter;
import com.example.talentfinder.databinding.FragmentCreateBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Project;
import com.example.talentfinder.utilities.MediaFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Fragment for allowing user to create a project and upload to Parse server
public class CreateFragment extends MediaFragment {

    public static final String TAG = "CreateFragment";

    private FragmentCreateBinding binding;
    private LinearLayoutManager linearLayoutManager;
    private File photoFile;
    private List<String> tags;
    private FragmentManager fragmentManager;
    private ChipAdapter tagsAdapter;

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

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tags = new ArrayList<>();
        tagsAdapter = new ChipAdapter(getContext(), tags, GlobalConstants.CHIP_ENTRY);
        binding.fragmentCreateRvTags.setAdapter(tagsAdapter);
        binding.fragmentCreateRvTags.setLayoutManager(linearLayoutManager);

        binding.fragmentCreateBtnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        binding.fragmentCreateEtAddTag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    tags.add(binding.fragmentCreateEtAddTag.getText().toString());
                    binding.fragmentCreateEtAddTag.setText("");
                    tagsAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        binding.fragmentCreateBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project project = new Project();
                project.setUser(ParseUser.getCurrentUser());
                project.setTitle(binding.fragmentCreateEtProjectTitle.getText().toString());
                if (photoFile != null) {
                    project.setImage(new ParseFile(photoFile));
                }
                project.setDescription(binding.fragmentCreateTvDescribeProject.getText().toString());
                project.setTags(tags);
                project.setContributionCount(0);
                project.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Error while saving", e);
                            return;
                        }
                        Log.i(TAG, "Contribution saved successfully");
                        Fragment fragment = HomeFeedFragment.newInstance();
                        fragmentManager.beginTransaction().replace(R.id.activityMain_clContainer, fragment).commit();
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
}