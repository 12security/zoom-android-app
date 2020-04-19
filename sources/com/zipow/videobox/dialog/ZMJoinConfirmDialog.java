package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.UrlActionData;
import com.zipow.videobox.util.ZMUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMJoinConfirmDialog extends ZMDialogFragment {
    private static final String MEETINGID = "MEETINGID";
    private static final String MEETINGNUM = "MEETINGNUM";
    private DialogActionCallBack mDialogActionCallBack;

    public static boolean show(@NonNull final ZMActivity zMActivity, @NonNull final Uri uri) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(ZMJoinConfirmDialog.class.getName());
        if (findFragmentByTag != null) {
            ((ZMJoinConfirmDialog) findFragmentByTag).dismiss();
        }
        ZMJoinConfirmDialog zMJoinConfirmDialog = new ZMJoinConfirmDialog();
        Bundle bundle = new Bundle();
        UrlActionData parseURLActionData = PTApp.getInstance().parseURLActionData(uri.toString());
        String confno = parseURLActionData != null ? parseURLActionData.getConfno() : "";
        String confid = parseURLActionData != null ? parseURLActionData.getConfid() : null;
        if (TextUtils.isEmpty(confno) && TextUtils.isEmpty(confid)) {
            return true;
        }
        bundle.putString(MEETINGNUM, confno);
        bundle.putString(MEETINGID, confid);
        zMJoinConfirmDialog.setArguments(bundle);
        zMJoinConfirmDialog.setDialogActionCallBack(new DialogActionCallBack() {
            public void performDialogAction(int i, int i2, Bundle bundle) {
                if (i2 == -1) {
                    ConfActivity.joinByUrl((Context) zMActivity, uri.toString());
                }
                zMActivity.finish();
            }
        });
        zMJoinConfirmDialog.show(supportFragmentManager, ZMJoinConfirmDialog.class.getName());
        return false;
    }

    public static boolean show(@NonNull final ZMActivity zMActivity, @NonNull UrlActionData urlActionData, @NonNull final String str, final String str2) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(ZMJoinConfirmDialog.class.getName());
        if (findFragmentByTag != null) {
            ((ZMJoinConfirmDialog) findFragmentByTag).dismiss();
        }
        ZMJoinConfirmDialog zMJoinConfirmDialog = new ZMJoinConfirmDialog();
        Bundle bundle = new Bundle();
        String confno = urlActionData.getConfno();
        String confid = urlActionData.getConfid();
        if (TextUtils.isEmpty(confno) && TextUtils.isEmpty(confid)) {
            return true;
        }
        bundle.putString(MEETINGNUM, confno);
        bundle.putString(MEETINGID, confid);
        zMJoinConfirmDialog.setArguments(bundle);
        zMJoinConfirmDialog.setDialogActionCallBack(new DialogActionCallBack() {
            public void performDialogAction(int i, int i2, Bundle bundle) {
                if (i2 == -1) {
                    ConfActivity.joinByUrl(zMActivity, str, str2);
                }
                zMActivity.finish();
            }
        });
        zMJoinConfirmDialog.show(supportFragmentManager, ZMJoinConfirmDialog.class.getName());
        return false;
    }

    public void setDialogActionCallBack(DialogActionCallBack dialogActionCallBack) {
        this.mDialogActionCallBack = dialogActionCallBack;
    }

    public ZMJoinConfirmDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        String str = "";
        String str2 = "";
        if (arguments != null) {
            str = arguments.getString(MEETINGNUM);
            str2 = arguments.getString(MEETINGID);
        }
        Builder title = new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_title_confirm_join_90859));
        int i = C4558R.string.zm_msg_confirm_join_message_90859;
        Object[] objArr = new Object[1];
        if (!TextUtils.isEmpty(str)) {
            str2 = ZMUtils.formatMeetingNumberWithChar(str, '-');
        }
        objArr[0] = str2;
        return title.setMessage(getString(i, objArr)).setNegativeButton(C4558R.string.zm_btn_confirm_join_not_now_90859, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMJoinConfirmDialog.this.onClickNegativeBtn();
            }
        }).setPositiveButton(C4558R.string.zm_btn_join, (OnClickListener) new OnClickListener() {
            public void onClick(@Nullable DialogInterface dialogInterface, int i) {
                ZMJoinConfirmDialog.this.onClickPositiveBtn();
                if (dialogInterface != null) {
                    dialogInterface.cancel();
                }
            }
        }).create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    /* access modifiers changed from: private */
    public void onClickPositiveBtn() {
        DialogActionCallBack dialogActionCallBack = this.mDialogActionCallBack;
        if (dialogActionCallBack != null) {
            dialogActionCallBack.performDialogAction(0, -1, null);
        }
    }

    /* access modifiers changed from: private */
    public void onClickNegativeBtn() {
        DialogActionCallBack dialogActionCallBack = this.mDialogActionCallBack;
        if (dialogActionCallBack != null) {
            dialogActionCallBack.performDialogAction(0, -2, null);
        }
    }

    public void dismiss() {
        finishFragment(false);
    }
}
