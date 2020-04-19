package com.zipow.videobox.util.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import p021us.zoom.androidlib.cache.naming.SH1FileNameGenerator;

public abstract class FileCacheMgr {
    @NonNull
    private SH1FileNameGenerator fileNameGenerator = new SH1FileNameGenerator();

    public void fileCollection() {
    }

    @Nullable
    public abstract File getCacheDir();

    public String generateFileCacheName(@NonNull String str) {
        return this.fileNameGenerator.generate(str);
    }

    @Nullable
    public File getFile(@NonNull String str) {
        fileCollection();
        File file = new File(getCacheDir(), this.fileNameGenerator.generate(str));
        if (file.exists()) {
            return file;
        }
        return null;
    }
}
