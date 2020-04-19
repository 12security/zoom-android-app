package com.zipow.videobox.util.photopicker;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.loader.content.CursorLoader;
import p021us.zoom.androidlib.util.AndroidAppUtil;

@RequiresApi(api = 29)
public class PhotoDirectoryLoader extends CursorLoader {
    final String[] IMAGE_PROJECTION = {"_id", "_data", "bucket_id", "bucket_display_name", "date_added", "_size"};

    public PhotoDirectoryLoader(@NonNull Context context, boolean z) {
        String[] strArr;
        super(context);
        setProjection(this.IMAGE_PROJECTION);
        setUri(Media.EXTERNAL_CONTENT_URI);
        setSortOrder("date_added DESC");
        StringBuilder sb = new StringBuilder();
        sb.append("mime_type=? or mime_type=? or mime_type=? ");
        sb.append(z ? "or mime_type=?" : "");
        setSelection(sb.toString());
        if (z) {
            strArr = new String[]{AndroidAppUtil.IMAGE_MIME_TYPE_JPG, AndroidAppUtil.IMAGE_MIME_TYPE_PNG, "image/jpg", AndroidAppUtil.IMAGE_MIME_TYPE_GIF};
        } else {
            strArr = new String[]{AndroidAppUtil.IMAGE_MIME_TYPE_JPG, AndroidAppUtil.IMAGE_MIME_TYPE_PNG, "image/jpg"};
        }
        setSelectionArgs(strArr);
    }

    private PhotoDirectoryLoader(@NonNull Context context, @NonNull Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        super(context, uri, strArr, str, strArr2, str2);
    }
}
