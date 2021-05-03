package com.example.koohestantest1.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cache {

    public static final String TAG = Cache.class.getSimpleName();

    private static final String CHILD_DIR = "images";
    private static final String TEMP_FILE_NAME = "img";
    private static final String FILE_EXTENSION = ".jpg";
    private static final String FILE_EXTENSION2 = ".png";

    private static final int COMPRESS_QUALITY = 100;

    private Context context;

    public Cache(Context context) {
        this.context = context;
    }

    /**
     * Save image to the App cache
     *
     * @param bitmap to save to the cache
     * @param name   file name in the cache.
     *               If name is null file will be named by default {@link #TEMP_FILE_NAME}
     * @return file dir when file was saved
     */
    public File saveImgToCache(Bitmap bitmap, @Nullable String name) {
        File cachePath = null;
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        try {
            cachePath = new File(context.getCacheDir(), CHILD_DIR);
            cachePath.mkdirs();

            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fileName + FILE_EXTENSION);
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream);
            stream.close();

        } catch (IOException e) {
            Log.e(TAG, "saveImgToCache error: " + bitmap, e);
        }
        return cachePath;
    }
    public File saveImgToCache2(Bitmap bitmap, @Nullable String name) {
        File cachePath = null;
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        try {
            cachePath = new File(context.getCacheDir(), CHILD_DIR);
            cachePath.mkdirs();

            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fileName + FILE_EXTENSION2);
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, stream);
            stream.close();

        } catch (IOException e) {
            Log.e(TAG, "saveImgToCache error: " + bitmap, e);
        }
        return cachePath;
    }





    /**
     * Save an image to the App cache dir and return it {@link Uri}
     *
     * @param bitmap to save to the cache
     */
    public Uri saveToCacheAndGetUri(Bitmap bitmap) {
        return saveToCacheAndGetUri(bitmap, null);
    }

    /**
     * Save an image to the App cache dir and return it {@link Uri}
     *
     * @param bitmap to save to the cache
     * @param name   file name in the cache.
     *               If name is null file will be named by default {@link #TEMP_FILE_NAME}
     */
    public Uri saveToCacheAndGetUri(Bitmap bitmap, @Nullable String name) {
        File file = saveImgToCache(bitmap, name);
        return getImageUri(file, name);
    }

    public Uri saveToCacheAndGetUri2(Bitmap bitmap, @Nullable String name) {
        File file = saveImgToCache2(bitmap, name);
        return getImageUri(file, name);
    }

    public File saveToCacheAndGetFile(Bitmap bitmap, @Nullable String name) {
        File file = saveImgToCache(bitmap, name);
        return getImageFile(file, name);
    }

    public File saveToCacheAndGetFile2(Bitmap bitmap, @Nullable String name) {
        File file = saveImgToCache2(bitmap, name);
        return getImageFile2(file, name);
    }



    /**
     * Get a file {@link Uri}
     *
     * @param name of the file
     * @return file Uri in the App cache or null if file wasn't found
     */
    @Nullable
    public Uri getUriByFileName(String name) {
        String fileName;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        } else {
            return null;
        }

        File imagePath = new File(context.getCacheDir(), CHILD_DIR);
        File newFile = new File(imagePath, fileName + FILE_EXTENSION);
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
    }

    // Get an image Uri by name without extension from a file dir
    private Uri getImageUri(File fileDir, @Nullable String name) {
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        File newFile = new File(fileDir, fileName + FILE_EXTENSION);
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
    }

    private File getImageFile(File fileDir, @Nullable String name) {
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        File newFile = new File(fileDir, fileName + FILE_EXTENSION);
        return newFile;
    }

    private File getImageFile2(File fileDir, @Nullable String name) {
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        File newFile = new File(fileDir, fileName + FILE_EXTENSION2);
        return newFile;
    }

    /**
     * Get Uri type by {@link Uri}
     */
    public String getContentType(Uri uri) {
        return context.getContentResolver().getType(uri);
    }



    static Bitmap trim(Bitmap source) {
        int firstX = 0, firstY = 0;
        int lastX = source.getWidth();
        int lastY = source.getHeight();
        int[] pixels = new int[source.getWidth() * source.getHeight()];
        source.getPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
        loop:
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    firstX = x;
                    break loop;
                }
            }
        }
        loop:
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = firstX; x < source.getWidth(); x++) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    firstY = y;
                    break loop;
                }
            }
        }
        loop:
        for (int x = source.getWidth() - 1; x >= firstX; x--) {
            for (int y = source.getHeight() - 1; y >= firstY; y--) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    lastX = x;
                    break loop;
                }
            }
        }
        loop:
        for (int y = source.getHeight() - 1; y >= firstY; y--) {
            for (int x = source.getWidth() - 1; x >= firstX; x--) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    lastY = y;
                    break loop;
                }
            }
        }
        return Bitmap.createBitmap(source, firstX, firstY, lastX - firstX, lastY - firstY);
    }

}