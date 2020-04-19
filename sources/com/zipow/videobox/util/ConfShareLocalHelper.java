package com.zipow.videobox.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfShareLocalHelper {
    public static void updateShareTitle(@NonNull Context context, long j, View view) {
        view.setBackgroundResource(C4558R.color.zm_sharing_title_bg);
        if (j > 0) {
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById != null) {
                String safeString = StringUtil.safeString(userById.getScreenName());
                TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtSharingTitle);
                if (safeString.endsWith("s")) {
                    textView.setText(context.getString(C4558R.string.zm_msg_sharing_s, new Object[]{safeString}));
                } else {
                    textView.setText(context.getString(C4558R.string.zm_msg_sharing, new Object[]{safeString}));
                }
            }
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtMyScreenName);
            textView2.setVisibility(8);
            if (ConfLocalHelper.isNeedShowPresenterNameToWaterMark()) {
                view.setBackgroundResource(C4558R.color.zm_sharing_title_half_bg);
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself != null) {
                    textView2.setVisibility(0);
                    textView2.setText(myself.getScreenName());
                }
            }
        }
    }

    public static boolean isOtherScreenSharing() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        boolean z = false;
        if (shareObj == null) {
            return false;
        }
        if (shareObj.getShareStatus() == 3) {
            z = true;
        }
        return z;
    }

    public static boolean isOtherPureAudioSharing() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        return shareObj != null && shareObj.isViewingPureComputerAudio();
    }

    public static boolean isSharingOut() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        boolean z = false;
        if (shareObj == null) {
            return false;
        }
        if (shareObj.getShareStatus() == 2) {
            z = true;
        }
        return z;
    }

    public static boolean isSendSharing() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        boolean z = false;
        if (shareObj == null) {
            return false;
        }
        int shareStatus = shareObj.getShareStatus();
        if (shareStatus == 2 || shareStatus == 1) {
            z = true;
        }
        return z;
    }
}
