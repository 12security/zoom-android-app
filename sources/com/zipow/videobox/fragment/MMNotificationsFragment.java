package com.zipow.videobox.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettingItem;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettings;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr.DndSetting;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.view.RoundedBackgroundSpan;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMNotificationsFragment extends ZMDialogFragment implements OnClickListener {
    private View mAlertOptionTitle;
    private CheckedTextView mChkAlertSound;
    private CheckedTextView mChkAlertVibrate;
    private CheckedTextView mChkCallAlertSound;
    private CheckedTextView mChkCallAlertVibrate;
    private CheckedTextView mChkDisableInMeeting;
    private CheckedTextView mChkUnreadAtTop;
    private CheckedTextView mChkUnreadCount;
    private ImageView mImgAllMsg;
    private ImageView mImgNotificationIdle;
    private ImageView mImgNotificationInstant;
    private ImageView mImgNotificationNo;
    private ImageView mImgNotificationPrivate;
    @NonNull
    private INotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnDNDSettingsUpdated() {
            MMNotificationsFragment.this.onDNDSettingsUpdated();
        }

        public void OnBlockAllSettingsUpdated() {
            MMNotificationsFragment.this.onBlockAllSettingsUpdated();
        }

        public void OnInCallSettingUpdated() {
            MMNotificationsFragment.this.onInCallSettingUpdated();
        }

        public void OnMUCSettingUpdated() {
            MMNotificationsFragment.this.onMUCSettingUpdated();
        }

        public void OnUnreadOnTopSettingUpdated() {
            MMNotificationsFragment.this.onUnreadOnTopSettingUpdated();
        }

        public void OnUnreadBadgeSettingUpdated() {
            MMNotificationsFragment.this.onUnreadBadgeSettingUpdated();
        }
    };
    private View mMessageNotificationSettings;
    private View mPanelAlertOptions;
    private View mPanelDisturb;
    private View mPanelExceptionGroups;
    private View mPanelMessageNotificationSettings;
    private View mPanelNotificationContancts;
    private View mPanelNotificationKeys;
    private View mPanelNotificationSettings;
    private View mPanelTurnOnNotification;
    private View mPanelUnread;
    private View mPanelUnreadAtTop;
    private TextView mTxtContanctsNumber;
    private TextView mTxtDisturb;
    private TextView mTxtGroupNumber;
    private TextView mTxtKeysNumber;
    private TextView mUnreadLabel;
    @NonNull
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMNotificationsFragment.this.onGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMNotificationsFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, MMNotificationsFragment.class.getName(), new Bundle(), 0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_notification, viewGroup, false);
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
        this.mPanelNotificationSettings = inflate.findViewById(C4558R.C4560id.panelNotificationSettings);
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

    private void updateUI() {
        int i;
        int i2;
        String str;
        String str2;
        String str3;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            int i3 = 8;
            int i4 = 0;
            if (NotificationMgr.areNotificationsEnabled(activity)) {
                this.mPanelTurnOnNotification.setVisibility(8);
                this.mPanelNotificationSettings.setVisibility(0);
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
                        ImageView imageView = this.mImgNotificationIdle;
                        if (i7 == 2) {
                            i3 = 0;
                        }
                        imageView.setVisibility(i3);
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
                                List personSetting = notificationSettingMgr.getPersonSetting();
                                i2 = personSetting != null ? personSetting.size() : 0;
                                List keywordSetting = notificationSettingMgr.getKeywordSetting();
                                if (keywordSetting != null) {
                                    i4 = keywordSetting.size();
                                }
                                this.mChkUnreadCount.setChecked(notificationSettingMgr2.showUnreadForChannels());
                                this.mChkUnreadAtTop.setChecked(notificationSettingMgr2.keepAllUnreadChannelOnTop());
                            } else {
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
                            if (i4 == 0) {
                                str2 = getString(C4558R.string.zm_mm_lbl_not_set);
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("");
                                sb2.append(i4);
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
                this.mPanelNotificationSettings.setVisibility(8);
            }
            this.mChkAlertVibrate.setChecked(getPlayAlertVibrate());
            this.mChkAlertSound.setChecked(getPlayAlertSound());
            this.mChkCallAlertVibrate.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.INCOMING_CALL_PLAY_ALERT_VIBRATE, true));
            this.mChkCallAlertSound.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.INCOMING_CALL_PLAY_ALERT_SOUND, true));
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

    private boolean getPlayAlertSound() {
        if (PTApp.getInstance().getSettingHelper() != null) {
            return PTSettingHelper.getPlayAlertSound();
        }
        return true;
    }

    private void savePlayAlertSound(boolean z) {
        if (PTApp.getInstance().getSettingHelper() != null) {
            PTSettingHelper.savePlayAlertSound(z);
        }
        this.mChkAlertSound.setChecked(getPlayAlertSound());
    }

    private boolean getPlayAlertVibrate() {
        if (PTApp.getInstance().getSettingHelper() != null) {
            return PTSettingHelper.getPlayAlertVibrate();
        }
        return true;
    }

    private void savePlayAlertVibrate(boolean z) {
        if (PTApp.getInstance().getSettingHelper() != null) {
            PTSettingHelper.savePlayAlertVibrate(z);
        }
        this.mChkAlertVibrate.setChecked(getPlayAlertVibrate());
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
            }
        }
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
        if (activity == null || NotificationMgr.areNotificationsEnabled(activity)) {
            updateUI();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(activity.getPackageName());
        startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(sb.toString())));
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
