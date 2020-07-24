package com.example.talentfinder.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.talentfinder.databinding.FragmentChangeProfilePhotoDialogBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.utilities.MediaDialogFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ChangeProfilePhotoDialogFragment extends MediaDialogFragment {

    public static final String TAG = "ChangeProfilePhotoDialo";

    private FragmentChangeProfilePhotoDialogBinding binding;
    private File photoFile;

    public ChangeProfilePhotoDialogFragment() {
        // Required empty public constructor
    }

    public static ChangeProfilePhotoDialogFragment newInstance() {
        ChangeProfilePhotoDialogFragment fragment = new ChangeProfilePhotoDialogFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeProfilePhotoDialogBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(getContext())
                .load(ParseUser.getCurrentUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                .circleCrop()
                .into(binding.fragmentChangeProfilePhotoDialogIvProfilePicture);

        binding.fragmentChangeProfilePhotoDialogBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFile = onLaunchCamera("photo.jpg");
            }
        });

        binding.fragmentChangeProfilePhotoDialogBtnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        binding.fragmentChangeProfilePhotoDialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfilePicture();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((data != null) && (requestCode == GlobalConstants.PICK_PHOTO_CODE)){
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadImageFromUri(photoUri);

            // Load the selected image into a preview
//            binding.fragmentChangeProfilePhotoDialogIvProfilePicture.setImageBitmap(selectedImage);
            Glide.with(getContext())
                    .load(selectedImage)
                    .circleCrop()
                    .into(binding.fragmentChangeProfilePhotoDialogIvProfilePicture);

            // Convert image from bitmap to JPG file and apply to photoFile File
            try {
                photoFile = bitmapToFile(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if ((data != null) && (requestCode == GlobalConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)){
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//                binding.fragmentChangeProfilePhotoDialogIvProfilePicture.setImageBitmap(takenImage);
                Glide.with(getContext())
                        .load(takenImage)
                        .circleCrop()
                        .into(binding.fragmentChangeProfilePhotoDialogIvProfilePicture);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveProfilePicture(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(ParseUserKey.PROFILE_IMAGE, new ParseFile(photoFile));
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error saving new profile picture!", e);
                    return;
                }
                dismiss();
            }
        });
    }
}