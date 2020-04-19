package p021us.zoom.androidlib.cache;

import android.graphics.Bitmap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import p021us.zoom.androidlib.cache.IoUtils.CopyListener;

/* renamed from: us.zoom.androidlib.cache.DiskCache */
public interface DiskCache {
    void clear();

    void close();

    File get(String str);

    File getDirectory();

    boolean remove(String str);

    boolean save(String str, Bitmap bitmap) throws IOException;

    boolean save(String str, InputStream inputStream, CopyListener copyListener) throws IOException;
}
