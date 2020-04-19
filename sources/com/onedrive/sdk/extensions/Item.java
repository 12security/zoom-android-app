package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItem;
import java.io.File;

public class Item extends BaseItem {
    public static final String ROOT_ITEM_ID = "root";
    private String mPItemId;

    public String getPath() {
        if (isRoot()) {
            return File.separator;
        }
        String str = this.parentReference.path;
        if (!this.parentReference.path.endsWith(File.separator)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(File.separator);
            str = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(this.name);
        return sb2.toString();
    }

    public String getItemId() {
        return (this.f300id == null || this.f300id.trim().length() == 0) ? "root" : this.f300id;
    }

    public boolean isFolder() {
        return this.folder != null;
    }

    public boolean isRoot() {
        return this.f300id == null || this.f300id.trim().length() == 0 || this.parentReference == null || this.parentReference.path == null || this.parentReference.path.trim().length() == 0;
    }

    public String getmPItemId() {
        String str = this.mPItemId;
        return (str == null || str.trim().length() == 0) ? "root" : this.mPItemId;
    }

    public void setmPItemId(String str) {
        this.mPItemId = str;
    }

    public String getDownloadUrl() {
        return getRawObject().get("@content.downloadUrl").getAsString();
    }

    public String getShowName() {
        if (isRoot()) {
            return File.separator;
        }
        return this.name;
    }
}
