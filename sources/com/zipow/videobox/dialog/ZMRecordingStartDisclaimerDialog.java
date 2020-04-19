package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.CustomizeInfo;
import com.zipow.videobox.fragment.PrivacyDisclaimerFragment;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMRecordingStartDisclaimerDialog extends ZMDialogFragment {
    private CustomizeInfo customizeInfo;

    public static void showDialog(@NonNull ZMActivity zMActivity, CustomizeInfo customizeInfo2) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PrivacyDisclaimerFragment.KEY_INFO, customizeInfo2);
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (shouldShow(supportFragmentManager, ZMRecordingStartDisclaimerDialog.class.getSimpleName(), null)) {
            ZMRecordingStartDisclaimerDialog zMRecordingStartDisclaimerDialog = new ZMRecordingStartDisclaimerDialog();
            zMRecordingStartDisclaimerDialog.setArguments(bundle);
            zMRecordingStartDisclaimerDialog.showNow(supportFragmentManager, ZMRecordingStartDisclaimerDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        this.customizeInfo = (CustomizeInfo) getArguments().getSerializable(PrivacyDisclaimerFragment.KEY_INFO);
        if (this.customizeInfo == null) {
            this.customizeInfo = new CustomizeInfo();
        }
        if (this.customizeInfo.isEmpty()) {
            this.customizeInfo.title = getResources().getString(C4558R.string.zm_alert_disclaimer_start_recording_meeting_title_133459);
            this.customizeInfo.description = getResources().getString(C4558R.string.zm_alert_disclaimer_start_recording_desc_133459);
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && confContext.isWebinar()) {
                this.customizeInfo.title = getResources().getString(C4558R.string.zm_alert_disclaimer_start_recording_webinar_title_133459);
            }
        }
        ZMAlertDialog create = new Builder(getActivity()).setTitle((CharSequence) this.customizeInfo.getTitle()).setView(createContent()).setCancelable(false).setVerticalOptionStyle(true).setPositiveButton(C4558R.string.zm_btn_continue, (OnClickListener) new OnClickListener() {
            public void onClick(@Nullable DialogInterface dialogInterface, int i) {
                ZMConfEventTracking.logRecordingDisclaimer(50);
                ConfMgr.getInstance().agreeStartRecordingDisclaimer();
                ConfLocalHelper.startCMR();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMConfEventTracking.logRecordingDisclaimer(47);
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
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_record_reminder_layout, null);
        Button button = (Button) inflate.findViewById(C4558R.C4560id.btnPolicy);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtMessage)).setText(this.customizeInfo.getDescription());
        final String linkText = this.customizeInfo.getLinkText();
        if (!StringUtil.isEmptyOrNull(linkText)) {
            button.setVisibility(0);
            button.getPaint().setFlags(8);
            button.getPaint().setAntiAlias(true);
            button.setText(linkText);
            button.setContentDescription(getString(C4558R.string.zm_accessibility_link_99842, linkText));
            final String linkUrl = this.customizeInfo.getLinkUrl();
            if (!StringUtil.isEmptyOrNull(linkUrl)) {
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ZMActivity zMActivity = (ZMActivity) ZMRecordingStartDisclaimerDialog.this.getActivity();
                        if (zMActivity != null) {
                            ZMWebPageUtil.startWebPage(zMActivity, linkUrl, linkText);
                        }
                    }
                });
            }
        } else {
            button.setVisibility(8);
        }
        return inflate;
    }

    public void dismiss() {
        finishFragment(false);
    }
}
