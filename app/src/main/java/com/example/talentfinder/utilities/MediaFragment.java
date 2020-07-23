package com.example.talentfinder.utilities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.talentfinder.interfaces.GlobalConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class MediaFragment extends Fragment {

    public static final String TAG = "MediaFragment";

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

    public File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
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
