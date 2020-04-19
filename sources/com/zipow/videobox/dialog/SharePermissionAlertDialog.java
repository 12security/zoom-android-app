package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.util.ConfLocalHelper;
import java.io.File;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SharePermissionAlertDialog extends ZMDialogFragment {
    private static final String ARG_SHARE_ALERT_IS_VIEW_AUDIO = "share_alert_view_audio";
    private static final String ARG_SHARE_ALERT_TYPE = "share_alert_msg";
    private static final String ARG_SHARE_INTENT = "share_intent";
    private static final String ARG_SHARE_LOCAL_FILE = "share_local_file";
    private static final String ARG_SHARE_PATH = "share_path";
    private static final String ARG_SHARE_TYPE = "share_type";
    public static final int SHARE_ALERT_TYPE_DISABLE_SHARE = 4;
    public static final int SHARE_ALERT_TYPE_GRAB_OTHER_SHARE = 3;
    public static final int SHARE_ALERT_TYPE_LOCK_SHARE = 1;
    public static final int SHARE_ALERT_TYPE_OTHER_SHARING = 2;
    public static final int SHARE_TYPE_BY_PATH = 2;
    public static final int SHARE_TYPE_IMAGE = 1;
    private static final int SHARE_TYPE_NONE = 0;
    public static final int SHARE_TYPE_SCREEN = 4;
    public static final int SHARE_TYPE_WEBVIEW = 3;
    private int alertType = 1;
    private boolean localFile = true;
    private boolean mIsViewPureAduio = false;
    @Nullable
    private String shareData = null;
    @Nullable
    private Intent shareIntent;
    private int shareType = 0;

    @NonNull
    public static SharePermissionAlertDialog createAlertDialog(int i, boolean z) {
        SharePermissionAlertDialog sharePermissionAlertDialog = new SharePermissionAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SHARE_ALERT_TYPE, i);
        bundle.putBoolean(ARG_SHARE_ALERT_IS_VIEW_AUDIO, z);
        sharePermissionAlertDialog.setArguments(bundle);
        return sharePermissionAlertDialog;
    }

    public SharePermissionAlertDialog() {
        setCancelable(true);
    }

    public void setShareInfo(int i, String str, boolean z) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putInt(ARG_SHARE_TYPE, i);
            arguments.putString(ARG_SHARE_PATH, str);
            arguments.putBoolean(ARG_SHARE_LOCAL_FILE, z);
        }
    }

    public void setShareInfo(int i, Intent intent) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putInt(ARG_SHARE_TYPE, i);
            arguments.putParcelable(ARG_SHARE_INTENT, intent);
        }
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, SharePermissionAlertDialog.class.getName());
    }

    /* access modifiers changed from: private */
    public void onPositiveClick() {
        Uri uri;
        int i = this.alertType;
        if (i == 3) {
            if (((ConfActivityNormal) getActivity()) != null) {
                switch (this.shareType) {
                    case 0:
                        ZMConfComponentMgr.getInstance().showShareTip();
                        break;
                    case 1:
                        if (this.localFile) {
                            String str = this.shareData;
                            if (str != null) {
                                uri = Uri.fromFile(new File(str));
                                ZMConfComponentMgr.getInstance().startShareImage(uri, this.localFile);
                                break;
                            }
                        }
                        uri = Uri.parse(this.shareData);
                        ZMConfComponentMgr.getInstance().startShareImage(uri, this.localFile);
                    case 2:
                        ZMConfComponentMgr.getInstance().shareByPathExtension(this.shareData);
                        break;
                    case 3:
                        ZMConfComponentMgr.getInstance().startShareWebview(this.shareData);
                        break;
                    case 4:
                        ZMConfComponentMgr.getInstance().startShareScreen(this.shareIntent);
                        break;
                }
            }
        } else if ((i == 2 || i == 1 || i == 4) && ConfLocalHelper.isDirectShareClient()) {
            PTAppDelegation.getInstance().stopPresentToRoom(false);
        }
    }

    /* access modifiers changed from: private */
    public void onNegativeClick() {
        if (this.alertType == 3 && ConfLocalHelper.isDirectShareClient()) {
            PTAppDelegation.getInstance().stopPresentToRoom(true);
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.alertType = arguments.getInt(ARG_SHARE_ALERT_TYPE);
        this.mIsViewPureAduio = arguments.getBoolean(ARG_SHARE_ALERT_IS_VIEW_AUDIO);
        this.shareType = arguments.getInt(ARG_SHARE_TYPE);
        this.shareData = arguments.getString(ARG_SHARE_PATH);
        this.localFile = arguments.getBoolean(ARG_SHARE_LOCAL_FILE);
        this.shareIntent = (Intent) arguments.getParcelable(ARG_SHARE_INTENT);
        Builder cancelable = new Builder(getActivity()).setCancelable(true);
        if (ConfLocalHelper.isDirectShareClient()) {
            cancelable.setCancelable(false);
        }
        int i = C4558R.string.zm_btn_ok;
        int i2 = this.alertType;
        if (i2 == 1) {
            cancelable.setTitle(C4558R.string.zm_alert_host_lock_share);
        } else if (i2 == 2) {
            cancelable.setTitle(C4558R.string.zm_alert_other_is_sharing);
        } else if (i2 == 3) {
            if (this.mIsViewPureAduio) {
                i = C4558R.string.zm_btn_continue;
                cancelable.setMessage(C4558R.string.zm_alert_grab_pure_audio_share_41468);
            } else {
                cancelable.setMessage(C4558R.string.zm_alert_grab_otherSharing);
            }
            cancelable.setTitle(C4558R.string.zm_title_start_share);
            cancelable.setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharePermissionAlertDialog.this.onNegativeClick();
                }
            });
        } else if (i2 == 4) {
            cancelable.setTitle(C4558R.string.zm_unable_to_share_in_meeting_msg_93170);
        }
        cancelable.setPositiveButton(i, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SharePermissionAlertDialog.this.onPositiveClick();
            }
        });
        return cancelable.create();
    }
}
