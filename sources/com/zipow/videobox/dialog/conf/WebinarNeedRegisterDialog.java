package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class WebinarNeedRegisterDialog extends ZMDialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        ZMAlertDialog create = new Builder(getActivity()).setTitle(confContext != null && confContext.isWebinar() ? C4558R.string.zm_msg_webinar_need_register : C4558R.string.zm_msg_meeting_need_register).setPositiveButton(C4558R.string.zm_btn_register, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                WebinarNeedRegisterDialog.this.registerWebninar();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfLocalHelper.leaveCall((ConfActivity) WebinarNeedRegisterDialog.this.getActivity());
            }
        }).create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        ConfLocalHelper.leaveCall((ConfActivity) getActivity());
    }

    /* access modifiers changed from: private */
    public void registerWebninar() {
        ConfLocalHelper.leaveCall((ConfActivity) getActivity());
        ConfActivityNormal confActivityNormal = (ConfActivityNormal) getActivity();
        if (confActivityNormal != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    String webinarRegUrl = meetingItem.getWebinarRegUrl();
                    if (!StringUtil.isEmptyOrNull(webinarRegUrl)) {
                        UIUtil.openURL(confActivityNormal, webinarRegUrl);
                    }
                }
            }
        }
    }
}
