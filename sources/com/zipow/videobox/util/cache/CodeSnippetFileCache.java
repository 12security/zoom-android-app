package com.zipow.videobox.util.cache;

import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import java.io.File;

public class CodeSnippetFileCache extends FileCacheMgr {
    private static final String CACHE_DIR_NAME = "codesnippet";

    @Nullable
    public File getCacheDir() {
        File filesDir = VideoBoxApplication.getInstance().getFilesDir();
        if (filesDir == null) {
            return null;
        }
        File file = new File(filesDir, CACHE_DIR_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
