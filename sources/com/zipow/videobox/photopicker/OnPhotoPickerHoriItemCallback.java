package com.zipow.videobox.photopicker;

import android.view.View;

public interface OnPhotoPickerHoriItemCallback {
    boolean canSelected(String str, int i);

    void onItemClick(View view, String str, int i);
}
