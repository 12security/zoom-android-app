package com.zipow.videobox;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.RecordMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.ConfChatItem;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLocaleUtils;
import p021us.zoom.videomeetings.C4558R;

class ConfAccessibilityActivity extends ConfActivity {
    private static final String TAG = "com.zipow.videobox.ConfAccessibilityActivity";
    @Nullable
    private AccessibilityRetainedFragment mAccessibilityRetainedFragment;
    private String mLastRecordStatusDescription;

    public static class AccessibilityRetainedFragment extends ZMFragment {
        private String mLastRecordStatusDescription;

        public AccessibilityRetainedFragment() {
            setRetainInstance(true);
        }

        public void savemLastRecordStatusDescription(String str) {
            this.mLastRecordStatusDescription = str;
        }

        public String restoremLastRecordStatusDescription() {
            return this.mLastRecordStatusDescription;
        }
    }

    ConfAccessibilityActivity() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initAccessibilityRetainedFragment();
        restoreAccessibilityRetainedFragment();
    }

    public static String getConfChatAccessibilityDescription(@Nullable Context context, @Nullable ConfChatItem confChatItem) {
        if (confChatItem == null || context == null) {
            return "";
        }
        String str = confChatItem.receiverName;
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(confChatItem.receiver)) {
            str = context.getString(C4558R.string.zm_webinar_txt_me);
        }
        boolean isWebinar = ConfLocalHelper.isWebinar();
        if (confChatItem.msgType == 0) {
            str = context.getString(isWebinar ? C4558R.string.zm_mi_panelists_and_attendees_11380 : C4558R.string.zm_mi_everyone_122046);
        } else if (confChatItem.msgType == 2) {
            str = context.getString(C4558R.string.zm_webinar_txt_label_ccPanelist, new Object[]{str, context.getString(C4558R.string.zm_webinar_txt_all_panelists)});
        } else if (confChatItem.msgType == 1) {
            str = context.getString(C4558R.string.zm_webinar_txt_all_panelists);
        }
        return context.getString(C4558R.string.zm_accessibility_receive_message_19147, new Object[]{confChatItem.senderName, str, confChatItem.content});
    }

    /* access modifiers changed from: protected */
    public void processSpokenAccessibilityForConfCmd(View view, int i, long j) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this) && i == 79) {
            processRecord(view);
        }
    }

    /* access modifiers changed from: protected */
    public void processSpokenAccessibilityForUserCmd(View view, int i, long j, int i2) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
            switch (i) {
                case 36:
                    processRaisedHand(view, j, true);
                    return;
                case 37:
                    processRaisedHand(view, j, false);
                    return;
                default:
                    return;
            }
        }
    }

    private void restoreAccessibilityRetainedFragment() {
        this.mLastRecordStatusDescription = getAccessibilityRetainedFragment().restoremLastRecordStatusDescription();
    }

    private void initAccessibilityRetainedFragment() {
        this.mAccessibilityRetainedFragment = getAccessibilityRetainedFragment();
        if (this.mAccessibilityRetainedFragment == null) {
            this.mAccessibilityRetainedFragment = new AccessibilityRetainedFragment();
            getSupportFragmentManager().beginTransaction().add((Fragment) this.mAccessibilityRetainedFragment, AccessibilityRetainedFragment.class.getName()).commit();
        }
    }

    @Nullable
    private AccessibilityRetainedFragment getAccessibilityRetainedFragment() {
        AccessibilityRetainedFragment accessibilityRetainedFragment = this.mAccessibilityRetainedFragment;
        if (accessibilityRetainedFragment != null) {
            return accessibilityRetainedFragment;
        }
        return (AccessibilityRetainedFragment) getSupportFragmentManager().findFragmentByTag(AccessibilityRetainedFragment.class.getName());
    }

    private void processRecord(View view) {
        String str;
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                boolean theMeetingisBeingRecording = recordMgr.theMeetingisBeingRecording();
                boolean isRecordingInProgress = recordMgr.isRecordingInProgress();
                if (!isRecordingInProgress) {
                    str = getString(theMeetingisBeingRecording ? C4558R.string.zm_accessibility_record_started_23040 : C4558R.string.zm_accessibility_record_stoped_23040);
                } else if (confStatusObj.isCMRInConnecting()) {
                    str = getString(C4558R.string.zm_record_status_preparing);
                } else {
                    str = getString(recordMgr.isCMRPaused() ? C4558R.string.zm_record_status_paused : C4558R.string.zm_record_status_recording);
                }
                if (StringUtil.isEmptyOrNull(this.mLastRecordStatusDescription)) {
                    this.mLastRecordStatusDescription = getString(C4558R.string.zm_accessibility_record_stoped_23040);
                }
                if (!StringUtil.isSameString(this.mLastRecordStatusDescription, str)) {
                    this.mLastRecordStatusDescription = str;
                    getAccessibilityRetainedFragment().savemLastRecordStatusDescription(this.mLastRecordStatusDescription);
                    if (isRecordingInProgress || !ZMLocaleUtils.isEnglishLanguage()) {
                        AccessibilityUtil.announceForAccessibilityCompat(view, str, false);
                    } else {
                        AccessibilityUtil.announceForAccessibilityCompat(view, "", false);
                    }
                }
            }
        }
    }

    private void processRaisedHand(View view, long j, boolean z) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null) {
            String screenName = userById.getScreenName();
            if (!StringUtil.isEmptyOrNull(screenName)) {
                AccessibilityUtil.announceForAccessibilityCompat(view, getString(z ? C4558R.string.zm_accessibility_someone_raised_hand_23051 : C4558R.string.zm_description_msg_xxx_lower_hand, new Object[]{screenName}), false);
            }
        }
    }
}
