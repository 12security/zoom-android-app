package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMCDPRConfirmDialog extends ZMDialogFragment {
    public static void showDialog(@NonNull ZMActivity zMActivity) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (shouldShow(supportFragmentManager, ZMCDPRConfirmDialog.class.getSimpleName(), null)) {
            new ZMCDPRConfirmDialog().showNow(supportFragmentManager, ZMCDPRConfirmDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setTitle(C4558R.string.zm_alert_china_meeting_privacy_title_132493).setView(createContent()).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_agree_132493, (OnClickListener) new OnClickListener() {
            public void onClick(@Nullable DialogInterface dialogInterface, int i) {
                ConfMgr.getInstance().agreeChinaMeetingPrivacy();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentActivity activity = ZMCDPRConfirmDialog.this.getActivity();
                if (activity instanceof ConfActivity) {
                    ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                    ConfLocalHelper.leaveCall((ConfActivity) activity);
                }
            }
        }).create();
        create.setCanceledOnTouchOutside(false);
        create.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
        create.show();
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_real_name_layout, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMsgContent);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            final String chinaMeetingPrivacyUrl = confContext.getChinaMeetingPrivacyUrl();
            String string = getString(C4558R.string.zm_alert_china_meeting_privacy_content_132493, chinaMeetingPrivacyUrl);
            final String string2 = getString(C4558R.string.zm_alert_china_meeting_privacy_title_132493);
            textView.setText(ZMHtmlUtil.fromHtml(getContext(), string, new OnURLSpanClickListener() {
                public void onClick(View view, String str, String str2) {
                    ZMWebPageUtil.startWebPage((Fragment) ZMCDPRConfirmDialog.this, str, string2);
                }
            }));
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ZMWebPageUtil.startWebPage((Fragment) ZMCDPRConfirmDialog.this, chinaMeetingPrivacyUrl, string2);
                    }
                });
            } else {
                textView.setOnClickListener(null);
            }
        }
        return inflate;
    }

    public void dismiss() {
        finishFragment(false);
    }
}
