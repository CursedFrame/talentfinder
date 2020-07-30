package com.example.talentfinder.utilities;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public abstract class MediaUtils {

    public static final String TAG = "MediaUtils";

//    public static File urlToFile(String url, Context context) throws IOException {
//        URL imageurl = new URL(url);
//
//        Log.i(TAG, "urlToFile: " + url);
//
//        Bitmap bitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
//        return bitmapToFile(bitmap, context);
//    }
//
//    // Takes a bitmap and converts it into a File. Used for converting to ParseFile
//    public static File bitmapToFile(Bitmap bitmap, Context context) throws IOException {
//        File file = new File(context.getCacheDir(), "photo.jpg");
//        file.createNewFile();
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        byte[] bitmapdata = byteArrayOutputStream.toByteArray();
//
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        fileOutputStream.write(bitmapdata);
//        fileOutputStream.flush();
//        fileOutputStream.close();
//
//        return file;
//    }

    public static File urlToImage(String imageUrl, Context context) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();

        File file = new File(context.getCacheDir(), "photo.jpg");
        file.createNewFile();

        OutputStream os = new FileOutputStream(file);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();

        return file;
    }
}
