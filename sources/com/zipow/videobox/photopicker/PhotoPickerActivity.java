package com.zipow.videobox.photopicker;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.photopicker.entity.PhotoDirectory;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class PhotoPickerActivity extends ZMActivity {
    private int mMaxSelectCount = 9;
    private PhotoPagerFragment mPhotoPagerFragment;
    @Nullable
    private PhotoPickerFragment mPhotoPickerFragment;
    private PhotoDirectory mSelectedPhotoDirectory;
    private boolean mShowGif = false;

    @NonNull
    public PhotoPickerActivity getActivity() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        disableFinishActivityByGesture(true);
        boolean booleanExtra = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_GIF, false);
        this.mMaxSelectCount = getIntent().getIntExtra("MAX_COUNT", 9);
        setShowGif(booleanExtra);
        setContentView(C4558R.layout.zm_picker_activity_photo_picker);
        this.mPhotoPickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (this.mPhotoPickerFragment == null) {
            this.mPhotoPickerFragment = PhotoPickerFragment.newInstance(getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_CAMERA, false), booleanExtra, getIntent().getBooleanExtra(PhotoPicker.EXTRA_PREVIEW_ENABLED, true), getIntent().getIntExtra(PhotoPicker.EXTRA_GRID_COLUMN, 4), this.mMaxSelectCount, getIntent().getStringArrayListExtra(PhotoPicker.EXTRA_ORIGINAL_PHOTOS));
            getSupportFragmentManager().beginTransaction().replace(C4558R.C4560id.container, this.mPhotoPickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    public void onBackPressed() {
        PhotoPagerFragment photoPagerFragment = this.mPhotoPagerFragment;
        if (photoPagerFragment == null || !photoPagerFragment.isVisible()) {
            super.onBackPressed();
            return;
        }
        this.mPhotoPickerFragment.resetSelectedPhotoPaths(this.mPhotoPagerFragment.getSelectedPhotos());
        this.mPhotoPickerFragment.setSourceChecked(this.mPhotoPagerFragment.isSourceChecked());
        this.mPhotoPagerFragment.runExitAnimation(new Runnable() {
            public void run() {
                if (PhotoPickerActivity.this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    PhotoPickerActivity.this.getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    public void addImagePagerFragment(PhotoPagerFragment photoPagerFragment) {
        this.mPhotoPagerFragment = photoPagerFragment;
        getSupportFragmentManager().beginTransaction().replace(C4558R.C4560id.container, this.mPhotoPagerFragment).addToBackStack(null).commit();
    }

    public void completeSelect(boolean z, ArrayList<String> arrayList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS, arrayList);
        setResult(-1, intent);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    public boolean isShowGif() {
        return this.mShowGif;
    }

    public void setShowGif(boolean z) {
        this.mShowGif = z;
    }

    public PhotoDirectory getSelectedPhotoDirectory() {
        return this.mSelectedPhotoDirectory;
    }

    public void setSelectedPhotoDirectory(PhotoDirectory photoDirectory) {
        this.mSelectedPhotoDirectory = photoDirectory;
    }
}
