package com.alisha.roomfinderapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ImageManager {
    public static final String TAG = "ImageManager";
    public static final int IMAGE_SAVE_QUALITY = 90;


    public static Bitmap getBitmap(String imgUrl) {
        byte[] data = null;
        File imageFile = new File(imgUrl);
        FileInputStream fis = null;
        Bitmap bitmap = null;


        try {
            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 1, stream);
            data = stream.toByteArray();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "getBitmap: exception:" + e.getMessage());
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                Log.d(TAG, "getBitmap: exception:" + e.getMessage());
            }
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int i) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, i, baos);
        return baos.toByteArray();
    }
}
