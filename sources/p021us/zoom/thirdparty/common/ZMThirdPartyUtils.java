package p021us.zoom.thirdparty.common;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.thirdparty.box.BoxMgr;
import p021us.zoom.thirdparty.dropbox.ZMDropbox;
import p021us.zoom.thirdparty.googledrive.GoogleDriveMgr;
import p021us.zoom.thirdparty.login.C4538R;
import p021us.zoom.thirdparty.onedrive.OneDriveManager;

/* renamed from: us.zoom.thirdparty.common.ZMThirdPartyUtils */
public class ZMThirdPartyUtils {
    public static final void init(@NonNull IZMAppUtil iZMAppUtil, int i, int i2, int i3, int i4) {
        ZMDropbox.getInstance().init(iZMAppUtil);
        ZMDropbox.mFileIntegrationType = i;
        OneDriveManager.getInstance().init(iZMAppUtil);
        OneDriveManager.mFileIntegrationType = i2;
        BoxMgr.getInstance().init(iZMAppUtil);
        BoxMgr.mFileIntegrationType = i3;
        GoogleDriveMgr.getInstance().init(iZMAppUtil);
        GoogleDriveMgr.mFileIntegrationType = i4;
    }

    public static void checkShareCloudFileClientInfo(@NonNull Context context, boolean z) {
        if (!z) {
            Resources resources = context.getResources();
            ZMDropbox.setAppKeyPair(context, resources.getString(C4538R.string.zm_config_dropbox_app_key), resources.getString(C4538R.string.zm_config_dropbox_app_secret));
            OneDriveManager.setClientID(resources.getString(C4538R.string.zm_config_onedrive_app_client_id));
            OneDriveManager.setBusinessAppInfo(resources.getString(C4538R.string.zm_config_onedrive_business_app_client_id), resources.getString(C4538R.string.zm_config_onedrive_business_app_redirect_url));
            BoxMgr.getInstance();
            BoxMgr.setAppKeyPair(resources.getString(C4538R.string.zm_config_box_app_key), resources.getString(C4538R.string.zm_config_box_app_secret), resources.getString(C4538R.string.zm_config_box_app_redirect_url));
            GoogleDriveMgr.setAppInfo(resources.getString(C4538R.string.zm_config_googledrive_app_client_id), resources.getString(C4538R.string.zm_config_googledrive_app_redirect_url));
        }
    }
}
