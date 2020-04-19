package p021us.zoom.thirdparty.box;

import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxItem;
import java.io.File;
import java.util.Date;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.thirdparty.box.BoxFileObject */
public class BoxFileObject {
    private boolean isFileObject = false;
    private boolean isFolder = false;
    private String mName;
    private BoxItem mOwner;
    private String mPath;
    private long mSize;

    public BoxFileObject(String str, BoxItem boxItem) {
        if (boxItem != null) {
            this.mOwner = boxItem;
            this.isFileObject = true;
            if (StringUtil.isEmptyOrNull(str)) {
                str = File.separator;
            }
            this.mName = this.mOwner.getName();
            if (str.endsWith(File.separator)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(this.mName);
                this.mPath = sb.toString();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(File.separator);
                sb2.append(this.mName);
                this.mPath = sb2.toString();
            }
            if (boxItem instanceof BoxFolder) {
                this.isFolder = true;
                this.mSize = 0;
                return;
            }
            Long size = this.mOwner.getSize();
            if (size != null) {
                this.mSize = size.longValue();
            } else {
                this.mSize = 0;
            }
        }
    }

    public String getPath() {
        if (!this.isFileObject) {
            return "";
        }
        return this.mPath;
    }

    public String getName() {
        if (!this.isFileObject) {
            return "";
        }
        return this.mName;
    }

    public Date getLastModifiedDate() {
        if (!this.isFileObject) {
            return null;
        }
        Date modifiedAt = this.mOwner.getModifiedAt();
        if (modifiedAt == null) {
            modifiedAt = this.mOwner.getCreatedAt();
        }
        return modifiedAt;
    }

    public long getSize() {
        if (!this.isFileObject) {
            return -1;
        }
        return this.mSize;
    }

    public String getId() {
        BoxItem boxItem = this.mOwner;
        if (boxItem == null) {
            return "";
        }
        return boxItem.getId();
    }

    public boolean isFileObject() {
        return this.isFileObject;
    }

    public boolean isDir() {
        return this.isFolder;
    }
}
