package p021us.zoom.androidlib.data;

/* renamed from: us.zoom.androidlib.data.FileInfo */
public class FileInfo {
    private String displayName;
    private String ext;
    private String mimeType;
    private long size;

    public FileInfo(String str, long j, String str2, String str3) {
        this.displayName = str;
        this.size = j;
        this.mimeType = str2;
        this.ext = str3;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String str) {
        this.ext = str;
    }
}
