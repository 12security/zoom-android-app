package p021us.zoom.androidlib.cache.impl;

import android.graphics.Bitmap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import p021us.zoom.androidlib.cache.IoUtils.CopyListener;
import p021us.zoom.androidlib.cache.naming.FileNameGenerator;
import p021us.zoom.androidlib.cache.naming.SH1FileNameGenerator;

/* renamed from: us.zoom.androidlib.cache.impl.LimitedAgeDiskCache */
public class LimitedAgeDiskCache extends BaseDiskCache {
    private final Map<File, Long> loadingDates;
    private final long maxFileAge;

    public LimitedAgeDiskCache(File file, long j) {
        this(file, null, new SH1FileNameGenerator(), j);
    }

    public LimitedAgeDiskCache(File file, File file2, long j) {
        this(file, file2, new SH1FileNameGenerator(), j);
    }

    public LimitedAgeDiskCache(File file, File file2, FileNameGenerator fileNameGenerator, long j) {
        super(file, file2, fileNameGenerator);
        this.loadingDates = Collections.synchronizedMap(new HashMap());
        this.maxFileAge = j * 1000;
    }

    public File get(String str) {
        boolean z;
        File file = super.get(str);
        if (file != null && file.exists()) {
            Long l = (Long) this.loadingDates.get(file);
            if (l == null) {
                l = Long.valueOf(file.lastModified());
                z = false;
            } else {
                z = true;
            }
            if (System.currentTimeMillis() - l.longValue() > this.maxFileAge) {
                file.delete();
                this.loadingDates.remove(file);
            } else if (!z) {
                this.loadingDates.put(file, l);
            }
        }
        return file;
    }

    public boolean save(String str, InputStream inputStream, CopyListener copyListener) throws IOException {
        boolean save = super.save(str, inputStream, copyListener);
        rememberUsage(str);
        return save;
    }

    public boolean save(String str, Bitmap bitmap) throws IOException {
        boolean save = super.save(str, bitmap);
        rememberUsage(str);
        return save;
    }

    public boolean remove(String str) {
        this.loadingDates.remove(getFile(str));
        return super.remove(str);
    }

    public void clear() {
        super.clear();
        this.loadingDates.clear();
    }

    private void rememberUsage(String str) {
        File file = getFile(str);
        long currentTimeMillis = System.currentTimeMillis();
        file.setLastModified(currentTimeMillis);
        this.loadingDates.put(file, Long.valueOf(currentTimeMillis));
    }
}
