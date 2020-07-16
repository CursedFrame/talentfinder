package com.example.talentfinder.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.talentfinder.R;
import com.example.talentfinder.databinding.FragmentContributeBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Contribution;
import com.example.talentfinder.models.Project;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;

public class ContributeFragment extends Fragment {

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

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userDescription = binding.etIntroduceYourself.getText().toString();
                String skillsDescription = binding.etDescribeSkills.getText().toString();
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
                    saveContribution(userDescription, skillsDescription, currentUser, photoFile);
                }
                else {
                    saveContribution(userDescription, skillsDescription, currentUser, videoFile);
                }
            }
        });

        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
            }
        });

        binding.btnAttachMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), binding.btnAttachMedia);
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
                                  ParseUser currentUser, File contributionFile){
        Contribution contribution = new Contribution();
        contribution.setUserDescription(userDescription);
        contribution.setSkillsDescription(skillsDescription);
        contribution.setMedia(new ParseFile(contributionFile));
        contribution.setUser(currentUser);
        contribution.setProject(project);
        contribution.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    return;
                }
                Log.i(TAG, "Contribution saved successfully");
                binding.etIntroduceYourself.setText("");
                binding.etDescribeSkills.setText("");
                binding.ivContributePicture.setImageResource(0);
                fragmentManager.popBackStackImmediate();
            }
        });
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, GlobalConstants.PICK_PHOTO_CODE);
        }
    }

    // Trigger gallery selection for a video
    public void onPickVideo() {
        // Create intent for picking a video from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a video
            startActivityForResult(intent, GlobalConstants.PICK_VIDEO_CODE);
        }
    }

    public Bitmap loadImageFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            if(Build.VERSION.SDK_INT > 27){
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
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

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == GlobalConstants.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadImageFromUri(photoUri);

            // Load the selected image into a preview
            binding.ivContributePicture.setImageBitmap(selectedImage);

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
                binding.ivContributePicture.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        if ((data != null) && requestCode == GlobalConstants.PICK_VIDEO_CODE) {
            Uri videoUri = data.getData();

            createTempVideo(videoUri);

            // Load the video located at videoUri into vvContributeVideo
            binding.vvContributeVideo.setVideoPath(getContext().getCacheDir() + "/video.mp4");
            binding.vvContributeVideo.start();
            videoFile = new File(getContext().getCacheDir() + "/video.mp4");
        }
    }

    // Takes a bitmap and converts it into a File. Used for converting to ParseFile
    public File bitmapToFile(Bitmap bitmap) throws IOException {
        File file = new File(getContext().getCacheDir(), "photo.jpg");
        file.createNewFile();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bitmapdata = byteArrayOutputStream.toByteArray();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bitmapdata);
        fileOutputStream.flush();
        fileOutputStream.close();

        return file;
    }

    public void createTempVideo(Uri videoUri){
        try {
            File directory = new File(String.valueOf(getContext().getCacheDir()));

            InputStream inputStream = getContext().getContentResolver().openInputStream(videoUri);
            OutputStream outputStream = new FileOutputStream(new File(directory, "video.mp4"));
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) > 0){
                outputStream.write(bytes, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}