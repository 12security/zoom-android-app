package com.zipow.videobox.util.photopicker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageCaptureManager {
    private static final String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    public static final int REQUEST_TAKE_PHOTO = 1;
    private Context mContext;
    @Nullable
    private String mCurrentPhotoPath;

    public ImageCaptureManager(Context context) {
        this.mContext = context;
    }

    @NonNull
    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("JPEG_");
        sb.append(format);
        sb.append(".jpg");
        String sb2 = sb.toString();
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (externalStoragePublicDirectory.exists() || externalStoragePublicDirectory.mkdir()) {
            File file = new File(externalStoragePublicDirectory, sb2);
            this.mCurrentPhotoPath = file.getAbsolutePath();
            return file;
        }
        Log.e("TAG", "Throwing Errors....");
        throw new IOException();
    }

    @NonNull
    public Intent dispatchTakePictureIntent() throws IOException {
        Uri uri;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(this.mContext.getPackageManager()) != null) {
            File createImageFile = createImageFile();
            if (VERSION.SDK_INT >= 24) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.mContext.getApplicationInfo().packageName);
                sb.append(".provider");
                uri = FileProvider.getUriForFile(this.mContext.getApplicationContext(), sb.toString(), createImageFile);
            } else {
                uri = Uri.fromFile(createImageFile);
            }
            if (uri != null) {
                intent.putExtra("output", uri);
            }
        }
        return intent;
    }

    public void galleryAddPic() {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        if (!TextUtils.isEmpty(this.mCurrentPhotoPath)) {
            intent.setData(Uri.fromFile(new File(this.mCurrentPhotoPath)));
            try {
                this.mContext.sendBroadcast(intent);
            } catch (Exception unused) {
            }
        }
    }

    @Nullable
    public String getCurrentPhotoPath() {
        return this.mCurrentPhotoPath;
    }

    public void onSaveInstanceState(@Nullable Bundle bundle) {
        if (bundle != null) {
            String str = this.mCurrentPhotoPath;
            if (str != null) {
                bundle.putString(CAPTURED_PHOTO_PATH_KEY, str);
            }
        }
    }

    public void onRestoreInstanceState(@Nullable Bundle bundle) {
        if (bundle != null && bundle.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            this.mCurrentPhotoPath = bundle.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }
}
