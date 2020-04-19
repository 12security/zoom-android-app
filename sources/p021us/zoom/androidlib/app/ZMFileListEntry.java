package p021us.zoom.androidlib.app;

import java.util.Date;

/* renamed from: us.zoom.androidlib.app.ZMFileListEntry */
public class ZMFileListEntry {
    private boolean isDir;
    private long mBytes;
    private int mChildEntryCount;
    private String mDisplayName;
    private long mLastModifiedTime;
    private String mPath;

    public String getPath() {
        return this.mPath;
    }

    public String getParentPath() {
        if (this.mPath.equals("/")) {
            return "";
        }
        return this.mPath.substring(0, this.mPath.lastIndexOf(47));
    }

    public void setPath(String str) {
        this.mPath = str;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public void setDisplayName(String str) {
        this.mDisplayName = str;
    }

    public boolean isDir() {
        return this.isDir;
    }

    public void setDir(boolean z) {
        this.isDir = z;
    }

    public long getBytes() {
        return this.mBytes;
    }

    public void setBytes(long j) {
        this.mBytes = j;
    }

    public long getDate() {
        return this.mLastModifiedTime;
    }

    public void setDate(Date date) {
        this.mLastModifiedTime = date.getTime();
    }

    public void setDate(long j) {
        this.mLastModifiedTime = j;
    }

    public int getChildEntryCount() {
        return this.mChildEntryCount;
    }

    public void setChildEntryCount(int i) {
        this.mChildEntryCount = i;
    }
}
