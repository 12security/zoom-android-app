package p021us.zoom.thirdparty.googledrive;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import p021us.zoom.androidlib.app.ZMFileListEntry;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.thirdparty.googledrive.GoogleDriveObjectEntry */
public class GoogleDriveObjectEntry extends ZMFileListEntry {
    private String mId;
    private String mMimeType;
    private String mParentId;

    public GoogleDriveObjectEntry(String str, File file) {
        if (file != null) {
            if (StringUtil.isEmptyOrNull(str)) {
                str = java.io.File.separator;
            }
            if (!str.endsWith(java.io.File.separator)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(java.io.File.separator);
                str = sb.toString();
            }
            this.mMimeType = file.getMimeType();
            this.mId = file.getId();
            if ("application/vnd.google-apps.folder".equals(this.mMimeType)) {
                setDir(true);
            } else {
                setDir(false);
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(file.getName());
            setPath(sb2.toString());
            DateTime modifiedTime = file.getModifiedTime();
            if (modifiedTime != null) {
                setDate(modifiedTime.getValue());
            }
            Long size = file.getSize();
            if (size != null) {
                setBytes(size.longValue());
            } else {
                setBytes(0);
            }
            setDisplayName(file.getName());
        }
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public String getId() {
        return this.mId;
    }

    public void setParentId(String str) {
        this.mParentId = str;
    }

    public String getParentId() {
        return this.mParentId;
    }
}
