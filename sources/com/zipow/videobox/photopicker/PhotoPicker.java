package com.zipow.videobox.photopicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.photopicker.PermissionsUtils;
import java.util.ArrayList;

public class PhotoPicker {
    public static final int DEFAULT_COLUMN_NUMBER = 4;
    public static final int DEFAULT_MAX_COUNT = 9;
    public static final String EXTRA_GRID_COLUMN = "column";
    public static final String EXTRA_MAX_COUNT = "MAX_COUNT";
    public static final String EXTRA_ORIGINAL_PHOTOS = "ORIGINAL_PHOTOS";
    public static final String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";
    public static final String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public static final String EXTRA_SHOW_GIF = "SHOW_GIF";
    public static final String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";
    public static final int REQUEST_CODE = 233;

    public static class PhotoPickerBuilder {
        private Intent mPickerIntent = new Intent();
        private Bundle mPickerOptionsBundle = new Bundle();

        public void start(@NonNull Activity activity, int i) {
            if (PermissionsUtils.checkReadStoragePermission(activity)) {
                ActivityStartHelper.startActivityForResult(activity, getIntent(activity), i);
            }
        }

        public void start(@NonNull Fragment fragment, int i) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null && PermissionsUtils.checkReadStoragePermission(activity)) {
                ActivityStartHelper.startActivityForResult(fragment, getIntent(activity), i);
            }
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment, int i) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                ActivityStartHelper.startActivityForResult(fragment, getIntent(context), i);
            }
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                ActivityStartHelper.startActivityForResult(fragment, getIntent(context), 233);
            }
        }

        public Intent getIntent(@NonNull Context context) {
            this.mPickerIntent.setClass(context, PhotoPickerActivity.class);
            this.mPickerIntent.putExtras(this.mPickerOptionsBundle);
            return this.mPickerIntent;
        }

        public void start(@NonNull Activity activity) {
            start(activity, 233);
        }

        @NonNull
        public PhotoPickerBuilder setPhotoCount(int i) {
            this.mPickerOptionsBundle.putInt("MAX_COUNT", i);
            return this;
        }

        @NonNull
        public PhotoPickerBuilder setGridColumnCount(int i) {
            this.mPickerOptionsBundle.putInt(PhotoPicker.EXTRA_GRID_COLUMN, i);
            return this;
        }

        @NonNull
        public PhotoPickerBuilder setShowGif(boolean z) {
            this.mPickerOptionsBundle.putBoolean(PhotoPicker.EXTRA_SHOW_GIF, z);
            return this;
        }

        @NonNull
        public PhotoPickerBuilder setShowCamera(boolean z) {
            this.mPickerOptionsBundle.putBoolean(PhotoPicker.EXTRA_SHOW_CAMERA, z);
            return this;
        }

        @NonNull
        public PhotoPickerBuilder setSelected(ArrayList<String> arrayList) {
            this.mPickerOptionsBundle.putStringArrayList(PhotoPicker.EXTRA_ORIGINAL_PHOTOS, arrayList);
            return this;
        }

        @NonNull
        public PhotoPickerBuilder setPreviewEnabled(boolean z) {
            this.mPickerOptionsBundle.putBoolean(PhotoPicker.EXTRA_PREVIEW_ENABLED, z);
            return this;
        }
    }

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }
}
