package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.WelcomeActivity;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettingItem;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettings;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomProductHelper;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr.DndSetting;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.view.RoundedBackgroundSpan;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MMChatSettingsFragment extends ZMDialogFragment implements OnClickListener {
    private View mAlertOptionTitle;
    private CheckedTextView mChkAlertSound;
    private CheckedTextView mChkAlertVibrate;
    private CheckedTextView mChkCallAlertSound;
    private CheckedTextView mChkCallAlertVibrate;
    private CheckedTextView mChkDisableInMeeting;
    private CheckedTextView mChkDropMode;
    private CheckedTextView mChkMessagePreview;
    private CheckedTextView mChkNotification4Follow;
    private CheckedTextView mChkShowLinkPreviewDetail;
    private CheckedTextView mChkUnreadAtTop;
    private CheckedTextView mChkUnreadCount;
    private ImageView mImgAllMsg;
    private ImageView mImgNotificationIdle;
    private ImageView mImgNotificationInstant;
    private ImageView mImgNotificationNo;
    private ImageView mImgNotificationPrivate;
    private View mImgStartEnd;
    private View mImgStartFirst;
    @NonNull
    private INotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnDNDSettingsUpdated() {
            MMChatSettingsFragment.this.onDNDSettingsUpdated();
        }

        public void OnBlockAllSettingsUpdated() {
            MMChatSettingsFragment.this.onBlockAllSettingsUpdated();
        }

        public void OnInCallSettingUpdated() {
            MMChatSettingsFragment.this.onInCallSettingUpdated();
        }

        public void OnMUCSettingUpdated() {
            MMChatSettingsFragment.this.onMUCSettingUpdated();
        }

        public void OnUnreadOnTopSettingUpdated() {
            MMChatSettingsFragment.this.onUnreadOnTopSettingUpdated();
        }

        public void OnUnreadBadgeSettingUpdated() {
            MMChatSettingsFragment.this.onUnreadBadgeSettingUpdated();
        }

        public void OnHintLineOptionUpdated() {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                MMChatSettingsFragment.this.toggleHintLineOptionPanel(notificationSettingMgr.getHintLineForChannels());
            }
        }

        public void OnReplyFollowThreadNotifySettingUpdated() {
            MMChatSettingsFragment.this.onReplyFollowThreadNotifySettingUpdated();
        }
    };
    private View mMessageNotificationSettings;
    private View mOptionShowLinkPreviewDetail;
    private View mOptionShowMessagePreviewDetail;
    private View mPanelAlertOptions;
    private View mPanelDisturb;
    private View mPanelDropMode;
    private View mPanelExceptionGroups;
    private View mPanelMessageNotificationSettings;
    private View mPanelNotification4Follow;
    private View mPanelNotificationContancts;
    private View mPanelNotificationKeys;
    private View mPanelTurnOnNotification;
    private View mPanelUnread;
    private View mPanelUnreadAtTop;
    private View mPanelUnreadHint;
    private TextView mTxtContanctsNumber;
    private TextView mTxtDisturb;
    private TextView mTxtDropMode;
    private TextView mTxtGroupNumber;
    private TextView mTxtKeysNumber;
    private View mTxtUnreadHintDes;
    private TextView mUnreadLabel;
    @NonNull
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMChatSettingsFragment.this.onGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMChatSettingsFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };
    private View panelNotificationFor;
    private View panelNotificationOtherSettings;
    private View panelNotificationWhen;
    private View panelViewUnreadMsg;
    private View txtNotificationFor;
    private View txtNotificationWhen;
    private View txtTurnOnNotification;
    private View txtViewUnreadMsg;

    public static class DropSwitchDialog extends DialogFragment {
        @NonNull
        public Dialog onCreateDialog(@Nullable Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_lbl_drop_message_hint_88133).setPositiveButton(C4558R.string.zm_btn_restart_zoom_88133, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    DropSwitchDialog.this.switchDropMode();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
        }

        /* access modifiers changed from: private */
        public void switchDropMode() {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    threadDataProvider.setThreadSortType(threadDataProvider.getThreadSortType() == 1 ? 0 : 1);
                    LoginUtil.getDefaultVendor();
                    ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
                    if (zoomProductHelper != null) {
                        zoomProductHelper.getCurrentVendor();
                    }
                    PTApp.getInstance().logout(1, false);
                    WelcomeActivity.show(VideoBoxApplication.getGlobalContext(), true, true);
                    int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
                    if (inProcessActivityCountInStack > 0) {
                        for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                            ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                            if (inProcessActivityInStackAt != null) {
                                inProcessActivityInStackAt.finish();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, MMChatSettingsFragment.class.getName(), new Bundle(), 0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_chat_settings, viewGroup, false);
        this.mChkDisableInMeeting = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkDisableInMeeting);
        this.mChkAlertSound = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAlertSound);
        this.mChkAlertVibrate = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAlertVibrate);
        this.mChkCallAlertSound = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkCallAlertSound);
        this.mChkCallAlertVibrate = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkCallAlertVibrate);
        this.mTxtDisturb = (TextView) inflate.findViewById(C4558R.C4560id.txtDisturb);
        this.mPanelDisturb = inflate.findViewById(C4558R.C4560id.panelDisturb);
        this.mImgAllMsg = (ImageView) inflate.findViewById(C4558R.C4560id.imgAllMsg);
        this.mImgNotificationPrivate = (ImageView) inflate.findViewById(C4558R.C4560id.imgNotificationPrivate);
        this.mImgNotificationNo = (ImageView) inflate.findViewById(C4558R.C4560id.imgNotificationNo);
        this.mImgNotificationInstant = (ImageView) inflate.findViewById(C4558R.C4560id.imgNotificationInstant);
        this.mImgNotificationIdle = (ImageView) inflate.findViewById(C4558R.C4560id.imgNotificationIdle);
        this.mPanelTurnOnNotification = inflate.findViewById(C4558R.C4560id.panelTurnOnNotification);
        this.mTxtGroupNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtGroupNumber);
        this.mPanelExceptionGroups = inflate.findViewById(C4558R.C4560id.panelExceptionGroups);
        this.mPanelNotificationKeys = inflate.findViewById(C4558R.C4560id.panelNotificationKeywords);
        this.mTxtKeysNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtNotificationKeywords);
        this.mPanelNotificationContancts = inflate.findViewById(C4558R.C4560id.panelNotificationContacts);
        this.mTxtContanctsNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtNotificationContacts);
        this.mPanelAlertOptions = inflate.findViewById(C4558R.C4560id.panelAlertOptions);
        this.mPanelMessageNotificationSettings = inflate.findViewById(C4558R.C4560id.panelMessageNotificationSettings);
        this.mMessageNotificationSettings = inflate.findViewById(C4558R.C4560id.message_notification_settings);
        this.mAlertOptionTitle = inflate.findViewById(C4558R.C4560id.alertOptionTitle);
        this.mChkUnreadAtTop = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkUnreadAtTop);
        this.mChkUnreadCount = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkUnreadCount);
        this.mPanelUnreadAtTop = inflate.findViewById(C4558R.C4560id.panelUnreadAtTop);
        this.mPanelUnread = inflate.findViewById(C4558R.C4560id.panelUnread);
        this.mUnreadLabel = (TextView) inflate.findViewById(C4558R.C4560id.unreadLabel);
        this.mChkShowLinkPreviewDetail = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkShowLinkPreviewDetail);
        this.mOptionShowLinkPreviewDetail = inflate.findViewById(C4558R.C4560id.optionShowLinkPreviewDetail);
        this.txtTurnOnNotification = inflate.findViewById(C4558R.C4560id.txtTurnOnNotification);
        this.txtNotificationFor = inflate.findViewById(C4558R.C4560id.txtNotificationFor);
        this.panelNotificationFor = inflate.findViewById(C4558R.C4560id.panelNotificationFor);
        this.panelNotificationOtherSettings = inflate.findViewById(C4558R.C4560id.panelNotificationOtherSettings);
        this.txtViewUnreadMsg = inflate.findViewById(C4558R.C4560id.txtViewUnreadMsg);
        this.panelViewUnreadMsg = inflate.findViewById(C4558R.C4560id.panelViewUnreadMsg);
        this.txtNotificationWhen = inflate.findViewById(C4558R.C4560id.txtNotificationWhen);
        this.panelNotificationWhen = inflate.findViewById(C4558R.C4560id.panelNotificationWhen);
        this.mChkNotification4Follow = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkNotification4Follow);
        this.mPanelNotification4Follow = inflate.findViewById(C4558R.C4560id.panelNotification4Follow);
        this.mChkMessagePreview = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkShowMessagePreviewDetail);
        this.mOptionShowMessagePreviewDetail = inflate.findViewById(C4558R.C4560id.optionShowMessagePreviewDetail);
        this.mTxtUnreadHintDes = inflate.findViewById(C4558R.C4560id.txtUnreadHintDes);
        this.mPanelUnreadHint = inflate.findViewById(C4558R.C4560id.panelUnreadHint);
        this.mImgStartFirst = inflate.findViewById(C4558R.C4560id.imgStartFirst);
        this.mImgStartEnd = inflate.findViewById(C4558R.C4560id.imgStartEnd);
        this.mTxtDropMode = (TextView) inflate.findViewById(C4558R.C4560id.txtDropMode);
        this.mChkDropMode = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkDropMode);
        this.mPanelDropMode = inflate.findViewById(C4558R.C4560id.panelDropMode);
        if (OsUtil.isAtLeastO()) {
            this.mAlertOptionTitle.setVisibility(8);
            this.mPanelAlertOptions.setVisibility(8);
            this.mPanelMessageNotificationSettings.setVisibility(0);
        } else {
            this.mAlertOptionTitle.setVisibility(0);
            this.mPanelAlertOptions.setVisibility(0);
            this.mPanelMessageNotificationSettings.setVisibility(8);
        }
        inflate.findViewById(C4558R.C4560id.panelAllMsg).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelAllMsg).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelPrivateMsg).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelNoMsg).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelNotificationInstant).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelNotificationIdle).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelAllMsg).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnTurnOnNotification).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelStartFirst).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelStartEnd).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelDropMode).setOnClickListener(this);
        this.mPanelDisturb.setOnClickListener(this);
        this.mChkDisableInMeeting.setOnClickListener(this);
        this.mChkAlertSound.setOnClickListener(this);
        this.mChkAlertVibrate.setOnClickListener(this);
        this.mChkCallAlertSound.setOnClickListener(this);
        this.mChkCallAlertVibrate.setOnClickListener(this);
        this.mPanelExceptionGroups.setOnClickListener(this);
        this.mPanelNotificationContancts.setOnClickListener(this);
        this.mPanelNotificationKeys.setOnClickListener(this);
        this.mMessageNotificationSettings.setOnClickListener(this);
        this.mPanelUnreadAtTop.setOnClickListener(this);
        this.mPanelUnread.setOnClickListener(this);
        this.mOptionShowLinkPreviewDetail.setOnClickListener(this);
        this.mChkNotification4Follow.setOnClickListener(this);
        this.mOptionShowMessagePreviewDetail.setOnClickListener(this);
        updateUnreadLable();
        return inflate;
    }

    public void onResume() {
        super.onResume();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        NotificationSettingUI.getInstance().addListener(this.mListener);
        updateUI();
    }

    public void onPause() {
        NotificationSettingUI.getInstance().removeListener(this.mListener);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onPause();
    }

    private void updateUnreadLable() {
        String string = getString(C4558R.string.zm_lbl_show_unread_msg_all_58475);
        int indexOf = string.indexOf("%%");
        if (indexOf >= 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string.subSequence(0, indexOf));
            spannableStringBuilder.append(" 1 ");
            spannableStringBuilder.append(string.substring(indexOf + 2));
            spannableStringBuilder.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getContext(), C4558R.color.zm_pure_red), ContextCompat.getColor(getContext(), C4558R.color.zm_white)), indexOf, indexOf + 3, 33);
            this.mUnreadLabel.setText(spannableStringBuilder);
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(int i, GroupAction groupAction, String str) {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onBlockAllSettingsUpdated() {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onInCallSettingUpdated() {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onMUCSettingUpdated() {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onUnreadOnTopSettingUpdated() {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onUnreadBadgeSettingUpdated() {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onDNDSettingsUpdated() {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onReplyFollowThreadNotifySettingUpdated() {
        updateUI();
    }

    private void updateUI() {
        int i;
        int i2;
        int i3;
        String str;
        String str2;
        String str3;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            int i4 = 8;
            if (NotificationMgr.areNotificationsEnabled(activity)) {
                this.mPanelTurnOnNotification.setVisibility(8);
                this.txtTurnOnNotification.setVisibility(8);
                this.txtNotificationFor.setVisibility(0);
                this.panelNotificationFor.setVisibility(0);
                this.panelNotificationOtherSettings.setVisibility(0);
                this.txtNotificationWhen.setVisibility(0);
                this.panelNotificationWhen.setVisibility(0);
                this.mPanelNotification4Follow.setVisibility(0);
                NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                if (notificationSettingMgr != null) {
                    int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
                    if (blockAllSettings != null) {
                        int i5 = blockAllSettings[0];
                        int i6 = blockAllSettings[1];
                        int i7 = blockAllSettings[2];
                        this.mImgAllMsg.setVisibility((i5 == 1 && i6 == 1) ? 0 : 8);
                        if (i5 == 2) {
                            this.mImgNotificationNo.setVisibility(0);
                            this.mPanelNotificationContancts.setVisibility(0);
                        } else {
                            this.mImgNotificationNo.setVisibility(8);
                            this.mPanelNotificationContancts.setVisibility(8);
                        }
                        this.mImgNotificationPrivate.setVisibility((i5 == 1 && i6 == 4) ? 0 : 8);
                        this.mImgNotificationInstant.setVisibility(i7 == 1 ? 0 : 8);
                        this.mImgNotificationIdle.setVisibility(i7 == 2 ? 0 : 8);
                        DndSetting dndSettings = notificationSettingMgr.getDndSettings();
                        if (dndSettings == null || !dndSettings.isEnable()) {
                            this.mTxtDisturb.setText("");
                        } else {
                            this.mTxtDisturb.setText(getString(C4558R.string.zm_lbl_notification_dnd_19898, TimeUtil.formatTime((Context) getActivity(), dndSettings.getStart()), TimeUtil.formatTime((Context) getActivity(), dndSettings.getEnd())));
                        }
                        this.mChkDisableInMeeting.setChecked(isImNotificationDisableInMeeting());
                        MUCNotifySettings mUCDiffFromGeneralSetting = notificationSettingMgr.getMUCDiffFromGeneralSetting();
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            if (mUCDiffFromGeneralSetting != null) {
                                i = 0;
                                for (MUCNotifySettingItem sessionId : mUCDiffFromGeneralSetting.getItemsList()) {
                                    if (zoomMessenger.getGroupById(sessionId.getSessionId()) != null) {
                                        i++;
                                    }
                                }
                            } else {
                                i = 0;
                            }
                            NotificationSettingMgr notificationSettingMgr2 = PTApp.getInstance().getNotificationSettingMgr();
                            if (notificationSettingMgr2 != null) {
                                List<String> personSetting = notificationSettingMgr.getPersonSetting();
                                if (personSetting != null) {
                                    i2 = 0;
                                    for (String buddyWithJID : personSetting) {
                                        if (!TextUtils.isEmpty(BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(buddyWithJID), null))) {
                                            i2++;
                                        }
                                    }
                                } else {
                                    i2 = 0;
                                }
                                List keywordSetting = notificationSettingMgr.getKeywordSetting();
                                i3 = keywordSetting != null ? keywordSetting.size() : 0;
                                this.mChkUnreadCount.setChecked(notificationSettingMgr2.showUnreadForChannels());
                                this.mChkUnreadAtTop.setChecked(notificationSettingMgr2.keepAllUnreadChannelOnTop());
                                this.mChkNotification4Follow.setChecked(notificationSettingMgr2.getFollowedThreadNotifySetting());
                            } else {
                                i3 = 0;
                                i2 = 0;
                            }
                            TextView textView = this.mTxtGroupNumber;
                            if (i <= 0) {
                                str = getString(C4558R.string.zm_mm_lbl_not_set);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("");
                                sb.append(i);
                                str = sb.toString();
                            }
                            textView.setText(str);
                            TextView textView2 = this.mTxtKeysNumber;
                            if (i3 == 0) {
                                str2 = getString(C4558R.string.zm_mm_lbl_not_set);
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("");
                                sb2.append(i3);
                                str2 = sb2.toString();
                            }
                            textView2.setText(str2);
                            TextView textView3 = this.mTxtContanctsNumber;
                            if (i2 == 0) {
                                str3 = getString(C4558R.string.zm_mm_lbl_not_set);
                            } else {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("");
                                sb3.append(i2);
                                str3 = sb3.toString();
                            }
                            textView3.setText(str3);
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                this.mPanelTurnOnNotification.setVisibility(0);
                this.txtTurnOnNotification.setVisibility(0);
                this.txtNotificationFor.setVisibility(8);
                this.panelNotificationFor.setVisibility(8);
                this.mTxtUnreadHintDes.setVisibility(8);
                this.mPanelUnreadHint.setVisibility(8);
                this.panelNotificationOtherSettings.setVisibility(8);
                this.txtNotificationWhen.setVisibility(8);
                this.panelNotificationWhen.setVisibility(8);
                this.mPanelNotification4Follow.setVisibility(8);
            }
            this.mChkAlertVibrate.setChecked(getPlayAlertVibrate());
            this.mChkAlertSound.setChecked(getPlayAlertSound());
            this.mChkCallAlertVibrate.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.INCOMING_CALL_PLAY_ALERT_VIBRATE, true));
            this.mChkCallAlertSound.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.INCOMING_CALL_PLAY_ALERT_SOUND, true));
            if (!PTApp.getInstance().hasMessenger() || (PTApp.getInstance().getZoomMessenger() != null && PTApp.getInstance().getZoomMessenger().imChatGetOption() == 2)) {
                this.mOptionShowLinkPreviewDetail.setVisibility(8);
                this.mOptionShowMessagePreviewDetail.setVisibility(8);
                this.mTxtUnreadHintDes.setVisibility(8);
                this.mPanelUnread.setVisibility(8);
                this.mPanelUnreadHint.setVisibility(8);
                this.txtViewUnreadMsg.setVisibility(8);
                this.panelViewUnreadMsg.setVisibility(8);
                this.mPanelDropMode.setVisibility(8);
                this.mPanelNotification4Follow.setVisibility(8);
            } else {
                this.mOptionShowLinkPreviewDetail.setVisibility(0);
                this.mOptionShowMessagePreviewDetail.setVisibility(0);
                this.mTxtUnreadHintDes.setVisibility(0);
                this.mPanelUnread.setVisibility(0);
                this.mPanelUnreadHint.setVisibility(0);
                boolean isUnreadStartFirst = isUnreadStartFirst();
                this.mImgStartEnd.setVisibility(isUnreadStartFirst ? 8 : 0);
                View view = this.mImgStartFirst;
                if (isUnreadStartFirst) {
                    i4 = 0;
                }
                view.setVisibility(i4);
                this.txtViewUnreadMsg.setVisibility(0);
                this.panelViewUnreadMsg.setVisibility(0);
                this.mPanelDropMode.setVisibility(0);
                this.mPanelNotification4Follow.setVisibility(0);
                updateThreadSortType();
            }
            this.mChkShowLinkPreviewDetail.setChecked(isImLlinkPreviewDescription());
            this.mChkMessagePreview.setChecked(isImNotificationMessagePreview());
        }
    }

    private void updateThreadSortType() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                this.mChkDropMode.setChecked(threadDataProvider.getThreadSortType() == 0);
            }
        }
    }

    private boolean isImNotificationDisableInMeeting() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr == null) {
            return false;
        }
        return notificationSettingMgr.getInCallSettings();
    }

    private void saveImNotificationDisableInMeeting(boolean z) {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            notificationSettingMgr.applyInCallSettings(z);
            this.mChkDisableInMeeting.setChecked(isImNotificationDisableInMeeting());
        }
    }

    private boolean isUnreadStartFirst() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        boolean z = true;
        if (notificationSettingMgr == null) {
            return true;
        }
        if (notificationSettingMgr.getHintLineForChannels() != 1) {
            z = false;
        }
        return z;
    }

    private boolean getPlayAlertSound() {
        return PTSettingHelper.getPlayAlertSound();
    }

    private void savePlayAlertSound(boolean z) {
        PTSettingHelper.savePlayAlertSound(z);
        this.mChkAlertSound.setChecked(getPlayAlertSound());
    }

    private boolean getPlayAlertVibrate() {
        return PTSettingHelper.getPlayAlertVibrate();
    }

    private void savePlayAlertVibrate(boolean z) {
        PTSettingHelper.savePlayAlertVibrate(z);
        this.mChkAlertVibrate.setChecked(getPlayAlertVibrate());
    }

    private boolean isImLlinkPreviewDescription() {
        return PTSettingHelper.isImLlinkPreviewDescription();
    }

    private boolean isImNotificationMessagePreview() {
        return PTSettingHelper.isImNotificationMessagePreview();
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                onClickBtnBack();
            } else if (id == C4558R.C4560id.panelAllMsg) {
                onClickPanelAllMsg();
            } else if (id == C4558R.C4560id.panelPrivateMsg) {
                onClickPanelPrivateMsg();
            } else if (id == C4558R.C4560id.panelNoMsg) {
                onClickPanelNoMsg();
            } else if (id == C4558R.C4560id.panelNotificationInstant) {
                onClickPanelNotificationInstant();
            } else if (id == C4558R.C4560id.panelNotificationIdle) {
                onClickPanelNotificationIdle();
            } else if (id == C4558R.C4560id.chkAlertSound) {
                onClickChkAlertSound();
            } else if (id == C4558R.C4560id.chkDisableInMeeting) {
                onClickChkDisableInMeeting();
            } else if (id == C4558R.C4560id.chkAlertVibrate) {
                onClickChkAlertVibrate();
            } else if (id == C4558R.C4560id.panelDisturb) {
                onClickPanelDisturb();
            } else if (id == C4558R.C4560id.btnTurnOnNotification) {
                onClickTurnOnNotification();
            } else if (id == C4558R.C4560id.panelExceptionGroups) {
                onClickPanelExceptionGroups();
            } else if (id == C4558R.C4560id.chkCallAlertSound) {
                onClickChkCallAlertSound();
            } else if (id == C4558R.C4560id.chkCallAlertVibrate) {
                onClickChkCallAlertVibrate();
            } else if (id == C4558R.C4560id.panelNotificationContacts) {
                onClickPanelNotificationContacts();
            } else if (id == C4558R.C4560id.panelNotificationKeywords) {
                onClickPanelNotificationKeywords();
            } else if (id == C4558R.C4560id.message_notification_settings) {
                onClickEditPanelNotificationSettings();
            } else if (id == C4558R.C4560id.panelUnread) {
                onClickPanelUnread();
            } else if (id == C4558R.C4560id.panelUnreadAtTop) {
                onClickEditPanelUnreadAtTop();
            } else if (id == C4558R.C4560id.optionShowLinkPreviewDetail) {
                onClickOptionShowLinkPreviewDetail();
            } else if (id == C4558R.C4560id.panelStartFirst) {
                onClickPanelStartFirst();
            } else if (id == C4558R.C4560id.panelStartEnd) {
                onClickPanelStartEnd();
            } else if (id == C4558R.C4560id.panelDropMode) {
                onClickPanelNotDrop();
            } else if (id == C4558R.C4560id.chkNotification4Follow) {
                onClickChkNotification4Follow();
            } else if (id == C4558R.C4560id.optionShowMessagePreviewDetail) {
                onClickChkNotificationMessagePreview();
            }
        }
    }

    private void onClickChkNotificationMessagePreview() {
        PTSettingHelper.saveImImNotificationMessagePreview(!this.mChkMessagePreview.isChecked());
        this.mChkMessagePreview.setChecked(isImNotificationMessagePreview());
    }

    private void onClickChkNotification4Follow() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            boolean z = !this.mChkNotification4Follow.isChecked();
            if (notificationSettingMgr.applyFollowedThreadNotifySetting(z)) {
                this.mChkNotification4Follow.setChecked(z);
            }
        }
    }

    private void onClickPanelNotDrop() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            new DropSwitchDialog().show(fragmentManager, DropSwitchDialog.class.getName());
        }
    }

    private void onClickPanelStartFirst() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null && notificationSettingMgr.setHintLineForChannels(1)) {
            toggleHintLineOptionPanel(1);
        }
    }

    private void onClickPanelStartEnd() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null && notificationSettingMgr.setHintLineForChannels(2)) {
            toggleHintLineOptionPanel(2);
        }
    }

    /* access modifiers changed from: private */
    public void toggleHintLineOptionPanel(int i) {
        if (i == 1) {
            this.mImgStartFirst.setVisibility(0);
            this.mImgStartEnd.setVisibility(8);
            return;
        }
        this.mImgStartFirst.setVisibility(8);
        this.mImgStartEnd.setVisibility(0);
    }

    private void onClickOptionShowLinkPreviewDetail() {
        PTSettingHelper.saveImLlinkPreviewDescription(!this.mChkShowLinkPreviewDetail.isChecked());
        this.mChkShowLinkPreviewDetail.setChecked(isImLlinkPreviewDescription());
    }

    private void jumpToAppNotification() {
        if (OsUtil.isAtLeastO()) {
            try {
                Intent intent = new Intent("android.settings.CHANNEL_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getApplicationContext().getPackageName());
                intent.putExtra("android.provider.extra.CHANNEL_ID", NotificationMgr.getNotificationChannelId());
                startActivity(intent);
            } catch (Exception unused) {
            }
        }
    }

    private void onClickEditPanelNotificationSettings() {
        if (OsUtil.isAtLeastO()) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                NotificationMgr.getNotificationCompatBuilder(activity);
                try {
                    if (UIUtil.isMIUI()) {
                        Intent intent = new Intent();
                        String packageName = getContext().getPackageName();
                        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationFilterActivity");
                        Bundle bundle = new Bundle();
                        bundle.putString("appName", getResources().getString(getContext().getApplicationInfo().labelRes));
                        bundle.putString("packageName", packageName);
                        bundle.putString(":android:show_fragment", "NotificationAccessSettings");
                        intent.putExtras(bundle);
                        intent.setComponent(componentName);
                        startActivity(intent);
                    } else {
                        jumpToAppNotification();
                    }
                } catch (Exception unused) {
                    jumpToAppNotification();
                }
            }
        }
    }

    private void onClickPanelNotificationContacts() {
        MMNotificationsAddContactFragment.showAsActivity(this);
    }

    private void onClickPanelNotificationKeywords() {
        MMNotificationsAddKeyWordsFragment.showAsActivity(this);
    }

    private void onClickPanelExceptionGroups() {
        MMNotificationExceptionGroupsSettingsFragment.showAsActivity(this, 0);
    }

    private void onClickTurnOnNotification() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (!NotificationMgr.areNotificationsEnabled(activity)) {
                StringBuilder sb = new StringBuilder();
                sb.append("package:");
                sb.append(getActivity().getPackageName());
                startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(sb.toString())));
            } else {
                updateUI();
            }
        }
    }

    private void onClickPanelDisturb() {
        MMNotificationDndSettingsFragment.showAsActivity(this);
    }

    private void onClickChkAlertSound() {
        savePlayAlertSound(!this.mChkAlertSound.isChecked());
    }

    private void onClickChkDisableInMeeting() {
        saveImNotificationDisableInMeeting(!this.mChkDisableInMeeting.isChecked());
    }

    private void onClickChkAlertVibrate() {
        savePlayAlertVibrate(!this.mChkAlertVibrate.isChecked());
    }

    private void onClickChkCallAlertSound() {
        boolean isChecked = this.mChkCallAlertSound.isChecked();
        PreferenceUtil.saveBooleanValue(PreferenceUtil.INCOMING_CALL_PLAY_ALERT_SOUND, !isChecked);
        this.mChkCallAlertSound.setChecked(!isChecked);
    }

    private void onClickChkCallAlertVibrate() {
        boolean isChecked = this.mChkCallAlertVibrate.isChecked();
        PreferenceUtil.saveBooleanValue(PreferenceUtil.INCOMING_CALL_PLAY_ALERT_VIBRATE, !isChecked);
        this.mChkCallAlertVibrate.setChecked(!isChecked);
    }

    private void onClickPanelNotificationIdle() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
            if (blockAllSettings != null) {
                notificationSettingMgr.applyBlockAllSettings(blockAllSettings[0], blockAllSettings[1], 2);
                updateUI();
            }
        }
    }

    private void onClickPanelNotificationInstant() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
            if (blockAllSettings != null) {
                notificationSettingMgr.applyBlockAllSettings(blockAllSettings[0], blockAllSettings[1], 1);
                updateUI();
            }
        }
    }

    private void onClickPanelNoMsg() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
            if (blockAllSettings != null) {
                notificationSettingMgr.applyBlockAllSettings(2, 1, blockAllSettings[2]);
                updateUI();
            }
        }
    }

    private void onClickPanelPrivateMsg() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
            if (blockAllSettings != null) {
                notificationSettingMgr.applyBlockAllSettings(1, 4, blockAllSettings[2]);
                updateUI();
            }
        }
    }

    private void onClickPanelAllMsg() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
            if (blockAllSettings != null) {
                notificationSettingMgr.applyBlockAllSettings(1, 1, blockAllSettings[2]);
                updateUI();
            }
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickPanelUnread() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            notificationSettingMgr.setShowUnreadForChannels(!notificationSettingMgr.showUnreadForChannels());
            updateUI();
        }
    }

    private void onClickEditPanelUnreadAtTop() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            notificationSettingMgr.setKeepAllUnreadChannelOnTop(!notificationSettingMgr.keepAllUnreadChannelOnTop());
            updateUI();
        }
    }
}
