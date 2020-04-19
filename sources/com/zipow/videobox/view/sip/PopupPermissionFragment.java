package com.zipow.videobox.view.sip;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PopupPermissionFragment extends ZMDialogFragment {
    private static final String ACTION_CANCEL_IF_DENY = "cancel_if_deny";
    public static final int OVERLAY_PERMISSION_REQ_CODE = 199;

    public static void showDialog(@NonNull FragmentManager fragmentManager) {
        showDialog(fragmentManager, false);
    }

    public static void showDialog(FragmentManager fragmentManager, boolean z) {
        if (((ZMDialogFragment) fragmentManager.findFragmentByTag(PopupPermissionFragment.class.getName())) == null) {
            PopupPermissionFragment popupPermissionFragment = new PopupPermissionFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(ACTION_CANCEL_IF_DENY, z);
            popupPermissionFragment.setArguments(bundle);
            popupPermissionFragment.show(fragmentManager, PopupPermissionFragment.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Builder positiveButton = new Builder(getActivity()).setCancelable(true).setTitle(C4558R.string.zm_title_permission_prompt).setMessage(C4558R.string.zm_sip_ask_pop_permission_67420).setPositiveButton(C4558R.string.zm_btn_got_it, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (OsUtil.isAtLeastM()) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(VideoBoxApplication.getInstance().getPackageName());
                        ActivityStartHelper.startActivityForResult((Activity) PopupPermissionFragment.this.getActivity(), new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())), 199);
                    } catch (Exception unused) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("package:");
                        sb2.append(PopupPermissionFragment.this.getActivity().getPackageName());
                        intent.setData(Uri.parse(sb2.toString()));
                        PopupPermissionFragment.this.startActivity(intent);
                    }
                }
            }
        });
        if (ZMActivity.getActivity(IMActivity.class.getName()) != null) {
            positiveButton.setNegativeButton(C4558R.string.zm_sip_minimize_permission_deny_85332, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (PopupPermissionFragment.this.getArguments() == null || !PopupPermissionFragment.this.getArguments().getBoolean(PopupPermissionFragment.ACTION_CANCEL_IF_DENY)) {
                        FragmentActivity activity = PopupPermissionFragment.this.getActivity();
                        if (activity != null) {
                            CmmSIPCallManager.getInstance().checkShowSipFloatWindow();
                            activity.finish();
                        }
                    }
                }
            });
        }
        ZMAlertDialog create = positiveButton.create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }
}
