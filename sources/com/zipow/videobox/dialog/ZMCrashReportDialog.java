package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.androidlib.util.ZMLocaleUtils;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMCrashReportDialog extends ZMDialogFragment {
    public static void showDialog(@NonNull ZMActivity zMActivity) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (shouldShow(supportFragmentManager, ZMCrashReportDialog.class.getSimpleName(), null)) {
            new ZMCrashReportDialog().showNow(supportFragmentManager, ZMCrashReportDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setTitle(C4558R.string.zm_alert_crash_report_title_150320).setView(createContent()).setCancelable(false).setVerticalOptionStyle(true).setPositiveButton(C4558R.string.zm_alert_crash_report_btn_send_150320, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PTApp.getInstance().uploadCrashDumpFile(true, 0, "");
            }
        }).setNegativeButton(C4558R.string.zm_alert_crash_report_btn_not_send_150320, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PTApp.getInstance().uploadCrashDumpFile(false, 0, "");
            }
        }).create();
        create.setCanceledOnTouchOutside(false);
        create.show();
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_real_name_layout, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMsgContent);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        final String safeString = StringUtil.safeString(PTApp.getInstance().getURLByType(ZMLocaleUtils.isChineseLanguage() ? 20 : 21));
        String string = getString(C4558R.string.zm_alert_crash_report_desc_150320, safeString);
        final String string2 = getString(C4558R.string.zm_alert_crash_report_title_150320);
        textView.setText(ZMHtmlUtil.fromHtml(getContext(), string, new OnURLSpanClickListener() {
            public void onClick(View view, String str, String str2) {
                ZMWebPageUtil.startWebPage((Fragment) ZMCrashReportDialog.this, str, string2);
            }
        }));
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ZMWebPageUtil.startWebPage((Fragment) ZMCrashReportDialog.this, safeString, string2);
                }
            });
        } else {
            textView.setOnClickListener(null);
        }
        return inflate;
    }

    public void dismiss() {
        finishFragment(false);
    }
}
