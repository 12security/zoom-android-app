package p021us.zoom.thirdparty.onedrive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.login.util.IPicker;
import p021us.zoom.thirdparty.login.util.IPickerResult;

/* renamed from: us.zoom.thirdparty.onedrive.OneDrivePicker */
public class OneDrivePicker implements IPicker {
    private static final String ONEDRIVE_PACKAGE_NAME = "com.microsoft.skydrive";
    private static final String ONEDRIVE_PICKER_ACTION = "onedrive.intent.action.PICKER";
    private static final int SDK_MIN_VERSION_CODE = 1460029606;
    private ArrayList<String> mFilterExtensions = new ArrayList<>();
    private boolean mIsBusiness;
    private int mRequestCode;

    @NonNull
    public static IPicker createPicker(int i, String[] strArr, boolean z) {
        return new OneDrivePicker(i, strArr, z);
    }

    private OneDrivePicker(int i, String[] strArr, boolean z) {
        this.mRequestCode = i;
        this.mIsBusiness = z;
        setFilterExtens(strArr);
    }

    public static boolean hasPicker(Context context, boolean z) {
        if (context == null || !AndroidAppUtil.hasActivityForIntent(context, OneDriveManager.getInstance().createOneDriveIntent(ONEDRIVE_PICKER_ACTION, z))) {
            return false;
        }
        PackageInfo packageInfo = AndroidAppUtil.getPackageInfo(context, ONEDRIVE_PACKAGE_NAME);
        if (packageInfo == null || packageInfo.versionCode < SDK_MIN_VERSION_CODE) {
            return false;
        }
        return true;
    }

    @Nullable
    public IPickerResult getPickerResult(int i, int i2, Intent intent) {
        if (this.mRequestCode != i || intent == null) {
            return null;
        }
        return OneDriveAppPickerResult.fromBundle(intent.getExtras(), this.mFilterExtensions);
    }

    public void startPicking(ZMActivity zMActivity) {
        if (zMActivity != null) {
            OneDriveLinkType oneDriveLinkType = OneDriveLinkType.DownloadLink;
            Intent createOneDriveIntent = OneDriveManager.getInstance().createOneDriveIntent(ONEDRIVE_PICKER_ACTION, this.mIsBusiness);
            createOneDriveIntent.putExtra("linkType", oneDriveLinkType.toString());
            zMActivity.startActivityForResult(createOneDriveIntent, this.mRequestCode);
        }
    }

    public void startPicking(ZMDialogFragment zMDialogFragment) {
        if (zMDialogFragment != null) {
            OneDriveLinkType oneDriveLinkType = OneDriveLinkType.DownloadLink;
            Intent createOneDriveIntent = OneDriveManager.getInstance().createOneDriveIntent(ONEDRIVE_PICKER_ACTION, this.mIsBusiness);
            createOneDriveIntent.putExtra("linkType", oneDriveLinkType.toString());
            zMDialogFragment.startActivityForResult(createOneDriveIntent, this.mRequestCode);
        }
    }

    public void startPicking(ZMFragment zMFragment) {
        if (zMFragment != null) {
            OneDriveLinkType oneDriveLinkType = OneDriveLinkType.DownloadLink;
            Intent createOneDriveIntent = OneDriveManager.getInstance().createOneDriveIntent(ONEDRIVE_PICKER_ACTION, this.mIsBusiness);
            createOneDriveIntent.putExtra("linkType", oneDriveLinkType.toString());
            zMFragment.startActivityForResult(createOneDriveIntent, this.mRequestCode);
        }
    }

    private void setFilterExtens(String[] strArr) {
        this.mFilterExtensions.clear();
        if (strArr != null && strArr.length > 0) {
            for (String str : strArr) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    this.mFilterExtensions.add(str);
                }
            }
        }
    }
}
