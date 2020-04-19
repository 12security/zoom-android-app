package com.zipow.videobox.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.CustomizeInfo;
import com.zipow.videobox.confapp.RecordMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class RecordingReminderDialog extends ZMDialogFragment {
    @NonNull
    public static RecordingReminderDialog newRecordingReminderDialog() {
        return new RecordingReminderDialog();
    }

    public RecordingReminderDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        final FragmentActivity activity = getActivity();
        int i = C4558R.string.zm_alert_remind_recording_content_meeting_68355;
        int i2 = C4558R.string.zm_alert_remind_recording_title_meeting_68355;
        int i3 = C4558R.string.zm_bo_btn_leave_meeting;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.isWebinar()) {
            i = C4558R.string.zm_alert_remind_recording_content_webinar_68355;
            i2 = C4558R.string.zm_alert_remind_recording_title_webinar_68355;
            i3 = C4558R.string.zm_bo_btn_leave_webinar_68355;
        }
        Builder positiveButton = new Builder(activity).setNegativeButton(i3, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
                if (recordMgr != null) {
                    recordMgr.disagreeContinueRecording();
                }
                Activity activity = activity;
                if (activity instanceof ConfActivity) {
                    ConfLocalHelper.leaveCallWithNotify((ConfActivity) activity);
                }
            }
        }).setPositiveButton(C4558R.string.zm_btn_continue, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
                if (recordMgr != null) {
                    recordMgr.agreeContinueRecording();
                }
            }
        });
        CustomizeInfo customizeInfo = null;
        if (confContext != null) {
            customizeInfo = confContext.getRecordingReminderCustomizeInfo();
        }
        if (customizeInfo == null || customizeInfo.isEmpty()) {
            positiveButton.setTitle(i2);
            positiveButton.setMessage(i);
        } else {
            positiveButton.setTitle((CharSequence) customizeInfo.title);
            if (StringUtil.isEmptyOrNull(customizeInfo.linkUrl)) {
                positiveButton.setMessage(customizeInfo.description);
            } else {
                positiveButton.setView(createContent(customizeInfo));
            }
        }
        return positiveButton.create();
    }

    public boolean isShowing() {
        Dialog dialog = getDialog();
        return dialog != null && dialog.isShowing();
    }

    private View createContent(@NonNull final CustomizeInfo customizeInfo) {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_record_reminder_layout, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        Button button = (Button) inflate.findViewById(C4558R.C4560id.btnPolicy);
        button.getPaint().setFlags(8);
        button.getPaint().setAntiAlias(true);
        button.setText(customizeInfo.linkText);
        button.setContentDescription(getString(C4558R.string.zm_accessibility_link_99842, customizeInfo.linkText));
        textView.setText(customizeInfo.description);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ZMWebPageUtil.startWebPage((Fragment) RecordingReminderDialog.this, customizeInfo.linkUrl, customizeInfo.linkText);
            }
        });
        return inflate;
    }
}
