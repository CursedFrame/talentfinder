package com.example.talentfinder.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
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
    private Context context;

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
        context = getContext();

        Glide.with(context)
                .load(GlobalConstants.PLACEHOLDER_URL)
                .into(binding.fragmentContributeIvContributePicture);

        binding.fragmentContributeBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fragmentContributePbCreate.setVisibility(View.VISIBLE);

                String contentDescription = binding.fragmentContributeEtContentDescription.getText().toString();
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (contentDescription.isEmpty()){
                    Toast.makeText(getContext(), "One or more fields are empty.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (photoFile == null && videoFile == null){
                    Toast.makeText(getContext(), "There is no image or video!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (videoFile == null){
                    saveContribution(contentDescription, currentUser, photoFile, GlobalConstants.MEDIA_PHOTO);
                }
                else {
                    saveContribution(contentDescription, currentUser, videoFile, GlobalConstants.MEDIA_VIDEO);
                }
            }
        });

        binding.fragmentContributeBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFile = onLaunchCamera("photo.jpg");
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

    private void saveContribution(String contentDescription, ParseUser currentUser, File contributionFile, int mediaTypeCode){
        final Contribution contribution = new Contribution();
        contribution.setContentDescription(contentDescription);
        contribution.setMedia(new ParseFile(contributionFile));
        contribution.setMediaTypeCode(mediaTypeCode);
        contribution.setPrivateContributionBool(binding.fragmentContributeSwitchPrivacy.isChecked());
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

                        binding.fragmentContributePbCreate.setVisibility(View.GONE);

                        fragmentManager.popBackStackImmediate();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == GlobalConstants.PICK_PHOTO_CODE) {
            binding.fragmentContributeVvContributeVideo.setVisibility(View.INVISIBLE);
            binding.fragmentContributeIvContributePicture.setVisibility(View.VISIBLE);

            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadImageFromUri(photoUri);

            // Convert image from bitmap to JPG file and apply to photoFile File
            try {
                photoFile = bitmapToFile(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Load the selected image into a preview
            Glide.with(getContext())
                    .load(photoFile)
                    .into(binding.fragmentContributeIvContributePicture);
        }
        else if ((data != null) && requestCode == GlobalConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            binding.fragmentContributeVvContributeVideo.setVisibility(View.INVISIBLE);
            binding.fragmentContributeIvContributePicture.setVisibility(View.VISIBLE);
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                binding.fragmentContributeIvContributePicture.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if ((data != null) && requestCode == GlobalConstants.PICK_VIDEO_CODE) {
            binding.fragmentContributeVvContributeVideo.setVisibility(View.VISIBLE);
            binding.fragmentContributeIvContributePicture.setVisibility(View.INVISIBLE);
            Uri videoUri = data.getData();

            createTempVideo(videoUri);

            binding.fragmentContributeVvContributeVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    binding.fragmentContributeVvContributeVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            binding.fragmentContributeVvContributeVideo.start();
                            binding.fragmentContributeVvContributeVideo.setOnClickListener(null);
                        }
                    });

                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            MediaController mediaController = new MediaController(getContext());
                            binding.fragmentContributeVvContributeVideo.setMediaController(mediaController);
                            mediaController.setAnchorView(binding.fragmentContributeVvContributeVideo);
                        }
                    });
                }
            });


            // Load the video located at videoUri into vvContributeVideo
            binding.fragmentContributeVvContributeVideo.setVideoPath(getContext().getCacheDir() + "/video.mp4");
            binding.fragmentContributeVvContributeVideo.start();
            videoFile = new File(getContext().getCacheDir() + "/video.mp4");
        }
    }
}