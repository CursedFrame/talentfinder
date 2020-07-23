package com.example.talentfinder.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.example.talentfinder.R;
import com.example.talentfinder.databinding.FragmentContributeBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Contribution;
import com.example.talentfinder.models.Project;
import com.example.talentfinder.utilities.MediaFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ContributeFragment extends MediaFragment {

    public static final String TAG = "ContributeFragment";

    private FragmentManager fragmentManager;
    private Project project;
    private FragmentContributeBinding binding;
    private File photoFile;
    private File videoFile;
    public String photoFileName = "photo.jpg";

    public ContributeFragment() {
        // Required empty public constructor
    }

    public static ContributeFragment newInstance(Project project) {
        ContributeFragment fragment = new ContributeFragment();
        Bundle args = new Bundle();
        args.putParcelable("project", project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContributeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        project = getArguments().getParcelable("project");
        fragmentManager = getFragmentManager();

        binding.fragmentContributeBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userDescription = binding.fragmentContributeEtIntroduceYourself.getText().toString();
                String skillsDescription = binding.fragmentContributeEtDescribeSkills.getText().toString();
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (userDescription.isEmpty() || skillsDescription.isEmpty()){
                    Toast.makeText(getContext(), "One or more fields are empty.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (photoFile == null && videoFile == null){
                    Toast.makeText(getContext(), "There is no image or video!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (videoFile == null){
                    saveContribution(userDescription, skillsDescription, currentUser, photoFile, GlobalConstants.MEDIA_PHOTO);
                }
                else {
                    saveContribution(userDescription, skillsDescription, currentUser, videoFile, GlobalConstants.MEDIA_VIDEO);
                }
            }
        });

        binding.fragmentContributeBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
            }
        });

        binding.fragmentContributeBtnAttachMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), binding.fragmentContributeBtnAttachMedia);
                popupMenu.getMenuInflater().inflate(R.menu.menu_contribution_file_choice, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_photo:
                                onPickPhoto();
                                break;
                            case R.id.action_video:
                                onPickVideo();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    private void saveContribution(String userDescription, String skillsDescription,
                                  ParseUser currentUser, File contributionFile, int mediaTypeCode){
        final Contribution contribution = new Contribution();
        contribution.setUserDescription(userDescription);
        contribution.setSkillsDescription(skillsDescription);
        contribution.setMedia(new ParseFile(contributionFile));
        contribution.setMediaTypeCode(mediaTypeCode);
        contribution.setUser(currentUser);
        contribution.setProject(project);
        contribution.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving contribution", e);
                    return;
                }
                Log.i(TAG, "Contribution saved successfully");
                binding.fragmentContributeEtIntroduceYourself.setText("");
                binding.fragmentContributeEtDescribeSkills.setText("");
                binding.fragmentContributeIvContributePicture.setImageResource(0);

                project.getRelation(Project.KEY_CONTRIBUTIONS).add(contribution);
                project.setContributionCount(project.getContributionCount() + 1);
                project.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Error while saving contribution to project", e);
                            return;
                        }
                        fragmentManager.popBackStackImmediate();
                    }
                });
            }
        });
    }

    private void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "me.cursedfra.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, GlobalConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == GlobalConstants.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadImageFromUri(photoUri);

            // Load the selected image into a preview
            binding.fragmentContributeIvContributePicture.setImageBitmap(selectedImage);

            // Convert image from bitmap to JPG file and apply to photoFile File
            try {
                photoFile = bitmapToFile(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == GlobalConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                binding.fragmentContributeIvContributePicture.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        if ((data != null) && requestCode == GlobalConstants.PICK_VIDEO_CODE) {
            Uri videoUri = data.getData();

            createTempVideo(videoUri);

            // Load the video located at videoUri into vvContributeVideo
            binding.fragmentContributeVvContributeVideo.setVideoPath(getContext().getCacheDir() + "/video.mp4");
            binding.fragmentContributeVvContributeVideo.start();
            videoFile = new File(getContext().getCacheDir() + "/video.mp4");
        }
    }
}