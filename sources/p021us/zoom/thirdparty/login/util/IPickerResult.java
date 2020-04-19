package p021us.zoom.thirdparty.login.util;

import android.net.Uri;

/* renamed from: us.zoom.thirdparty.login.util.IPickerResult */
public interface IPickerResult {
    boolean acceptFileType();

    Uri getLink();

    String getName();

    long getSize();

    boolean isLocal();
}
