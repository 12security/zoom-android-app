package p021us.zoom.androidlib.app;

import android.content.Context;

/* renamed from: us.zoom.androidlib.app.IZMAppUtil */
public interface IZMAppUtil {
    Context getAppContext();

    String getCachePath();

    String getShareCachePathByExtension(String str, String str2);

    boolean hasEnoughDiskSpace(String str, long j);

    boolean isZoomApp();
}
