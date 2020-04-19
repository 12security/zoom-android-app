package p021us.zoom.thirdparty.onedrive;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxFile;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.login.util.IPickerResult;

/* renamed from: us.zoom.thirdparty.onedrive.OneDriveAppPickerResult */
public class OneDriveAppPickerResult implements IPickerResult {
    private Bundle mData;
    protected ArrayList<String> mFilterExtensions = new ArrayList<>();

    public boolean isLocal() {
        return false;
    }

    public OneDriveAppPickerResult(Bundle bundle, List<String> list) {
        this.mData = bundle;
        if (list != null) {
            this.mFilterExtensions.addAll(list);
        }
    }

    @Nullable
    public static IPickerResult fromBundle(Bundle bundle, List<String> list) {
        if (bundle == null) {
            return null;
        }
        OneDriveAppPickerResult oneDriveAppPickerResult = new OneDriveAppPickerResult(bundle, list);
        if (oneDriveAppPickerResult.getLink() == null) {
            return null;
        }
        return oneDriveAppPickerResult;
    }

    public String getName() {
        String string = this.mData.getString(BoxFile.FIELD_EXTENSION);
        if (StringUtil.isEmptyOrNull(string)) {
            return this.mData.getString("name");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.mData.getString("name"));
        sb.append(string);
        return sb.toString();
    }

    public long getSize() {
        return this.mData.getLong("size", -1);
    }

    public Uri getLink() {
        return (Uri) this.mData.getParcelable("link");
    }

    public boolean acceptFileType() {
        if (this.mFilterExtensions.size() <= 0) {
            return true;
        }
        String string = this.mData.getString(BoxFile.FIELD_EXTENSION);
        if (!StringUtil.isEmptyOrNull(string) && this.mFilterExtensions.contains(string.toLowerCase())) {
            return true;
        }
        return false;
    }
}
