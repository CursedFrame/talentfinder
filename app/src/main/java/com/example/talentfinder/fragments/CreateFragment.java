package com.example.talentfinder.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.talentfinder.databinding.FragmentCreateBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Project;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Fragment for allowing user to create a project and upload to Parse server
public class CreateFragment extends Fragment {

    public static final String TAG = "CreateFragment";

    private FragmentCreateBinding binding;
    private File photoFile;
    private List<String> tags;
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

        tags = new ArrayList<>();

        binding.btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        binding.etAddTag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    tags.add(binding.etAddTag.getText().toString());
                    binding.etAddTag.setText("");
                    return true;
                }
                return false;
            }
        });

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project project = new Project();
                project.setUser(ParseUser.getCurrentUser());
                project.setTitle(binding.etProjectTitle.getText().toString());
                project.setImage(new ParseFile(photoFile));
                project.setDescription(binding.tvDescribeProject.getText().toString());
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
                        fragmentManager.popBackStackImmediate();
                    }
                });
            }
        });
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, GlobalConstants.PICK_PHOTO_CODE);
        }
    }

    public Bitmap loadImageFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && requestCode == GlobalConstants.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadImageFromUri(photoUri);

            // Load the selected image into a preview
            binding.ivOptionalContext.setImageBitmap(selectedImage);

            // Change bitmap to File and input into photoFile
            try {
                photoFile = bitmapToFile(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}