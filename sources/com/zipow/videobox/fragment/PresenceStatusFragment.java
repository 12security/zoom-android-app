package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr.DndSetting;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class PresenceStatusFragment extends ZMDialogFragment implements OnClickListener {
    private static final String TAG = "PresenceStatusFragment";
    private ImageView imgPsAvailable;
    private ImageView imgPsDnd;
    @NonNull
    private INotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnDNDSettingsUpdated() {
            PresenceStatusFragment.this.updateTimeInterval();
        }

        public void OnDNDNowSettingUpdated() {
            PresenceStatusFragment.this.updateTimeInterval();
        }

        public void OnSnoozeSettingsUpdated() {
            PresenceStatusFragment.this.updateTimeInterval();
        }
    };
    private IZoomMessengerUIListener mZoomMessengerUIListener;
    private View panelPsAvailable;
    private View panelPsDnd;
    private TextView txtTimeInterval;

    public static class SnoozedContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_1_HOUR = 2;
        public static final int ACTION_20_MINUTE = 1;
        public static final int ACTION_2_HOUR = 3;
        public static final int ACTION_4_HOUR = 4;
        public static final int ACTION_8_HOUR = 5;
        public static final int ACTION_SET_TIME_PERIOD = 6;
        public static final int ACTION_TURN_OFF = 0;

        public SnoozedContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_presence_status, null);
        this.panelPsDnd = inflate.findViewById(C4558R.C4560id.panelPsDnd);
        this.txtTimeInterval = (TextView) inflate.findViewById(C4558R.C4560id.txtTimeInterval);
        this.panelPsAvailable = inflate.findViewById(C4558R.C4560id.panelPsAvailable);
        this.imgPsDnd = (ImageView) inflate.findViewById(C4558R.C4560id.imgPsDnd);
        this.imgPsAvailable = (ImageView) inflate.findViewById(C4558R.C4560id.imgPsAvailable);
        this.panelPsDnd.setOnClickListener(this);
        this.panelPsAvailable.setOnClickListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        setPresence(getPresence());
        updateTimeInterval();
    }

    public void onStart() {
        super.onStart();
        if (this.mZoomMessengerUIListener == null) {
            this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                public void On_MyPresenceChanged(int i, int i2) {
                    PresenceStatusFragment.this.setPresence(i);
                    PresenceStatusFragment.this.updateTimeInterval();
                }
            };
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        NotificationSettingUI.getInstance().addListener(this.mListener);
    }

    public void onStop() {
        NotificationSettingUI.getInstance().removeListener(this.mListener);
        if (this.mZoomMessengerUIListener != null) {
            ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        }
        super.onStop();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.panelPsDnd) {
            onClickPanelDnd();
        } else if (id == C4558R.C4560id.panelPsAvailable) {
            onClickPanelAvailable();
        }
    }

    public boolean setPresence(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        switch (i) {
            case 2:
            case 4:
                this.imgPsDnd.setVisibility(0);
                this.imgPsAvailable.setVisibility(4);
                break;
            case 3:
                this.imgPsAvailable.setVisibility(0);
                this.imgPsDnd.setVisibility(4);
                break;
        }
        return zoomMessenger.setPresence(i);
    }

    public int getPresence() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return 0;
        }
        return zoomMessenger.getMyPresence();
    }

    private void onClickPanelAvailable() {
        setPresence(3);
        shutdownDND();
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, PresenceStatusFragment.class.getName(), new Bundle(), 0);
        }
    }

    private void onClickBtnBack() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void onClickPanelDnd() {
        String str;
        String str2;
        String string = getString(C4558R.string.zm_lbl_notification_snoozed_19898);
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            long[] snoozeSettings = notificationSettingMgr.getSnoozeSettings();
            if (snoozeSettings != null) {
                long j = snoozeSettings[2] - snoozeSettings[1];
                if (j > 0) {
                    long mMNow = CmmTime.getMMNow();
                    if (snoozeSettings[2] - mMNow >= 0 && mMNow - snoozeSettings[1] >= 0) {
                        j = snoozeSettings[2] - mMNow;
                    }
                    int i = (int) (j / 60000);
                    if (i == 0) {
                        i = 1;
                    }
                    int i2 = i / 60;
                    if (i2 > 0) {
                        str = getResources().getQuantityString(C4558R.plurals.zm_lbl_notification_snoozed_hour_19898, i2, new Object[]{Integer.valueOf(i2)});
                    } else {
                        str = "";
                    }
                    int i3 = i % 60;
                    if (i3 > 0) {
                        str2 = getResources().getQuantityString(C4558R.plurals.zm_lbl_notification_snoozed_min_19898, i3, new Object[]{Integer.valueOf(i3)});
                    } else {
                        str2 = "";
                    }
                    string = getString(C4558R.string.zm_lbl_notification_snoozed_resume_in_19898, str, str2);
                }
                final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
                ArrayList arrayList = new ArrayList();
                if (j > 0) {
                    arrayList.add(new SnoozedContextMenuItem(getString(C4558R.string.zm_lbl_notification_snoozed_turn_off_19898), 0));
                }
                arrayList.add(new SnoozedContextMenuItem(getResources().getQuantityString(C4558R.plurals.zm_lbl_notification_snoozed_min_19898, 20, new Object[]{Integer.valueOf(20)}), 1));
                arrayList.add(new SnoozedContextMenuItem(getResources().getQuantityString(C4558R.plurals.zm_lbl_notification_snoozed_hour_19898, 1, new Object[]{Integer.valueOf(1)}), 2));
                arrayList.add(new SnoozedContextMenuItem(getResources().getQuantityString(C4558R.plurals.zm_lbl_notification_snoozed_hour_19898, 2, new Object[]{Integer.valueOf(2)}), 3));
                arrayList.add(new SnoozedContextMenuItem(getResources().getQuantityString(C4558R.plurals.zm_lbl_notification_snoozed_hour_19898, 4, new Object[]{Integer.valueOf(4)}), 4));
                arrayList.add(new SnoozedContextMenuItem(getResources().getQuantityString(C4558R.plurals.zm_lbl_notification_snoozed_hour_19898, 8, new Object[]{Integer.valueOf(8)}), 5));
                arrayList.add(new SnoozedContextMenuItem(getResources().getString(C4558R.string.zm_lbl_profile_set_time_period_40739), 6));
                TextView textView = new TextView(getActivity());
                if (VERSION.SDK_INT < 23) {
                    textView.setTextAppearance(getActivity(), C4558R.style.ZMTextView_Small);
                } else {
                    textView.setTextAppearance(C4558R.style.ZMTextView_Small);
                }
                int dip2px = UIUtil.dip2px(getActivity(), 20.0f);
                textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
                textView.setText(string);
                zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
                ZMAlertDialog create = new Builder(getActivity()).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PresenceStatusFragment.this.onSelectContextMenuItem((SnoozedContextMenuItem) zMMenuAdapter.getItem(i));
                    }
                }).create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(@Nullable SnoozedContextMenuItem snoozedContextMenuItem) {
        if (snoozedContextMenuItem != null) {
            int i = 0;
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                if (snoozedContextMenuItem.getAction() == 6) {
                    MMNotificationDndSettingsFragment.showAsActivity(this);
                    return;
                }
                switch (snoozedContextMenuItem.getAction()) {
                    case 0:
                        onClickPanelAvailable();
                        return;
                    case 1:
                        i = 20;
                        setPresence(4);
                        break;
                    case 2:
                        i = 60;
                        setPresence(4);
                        break;
                    case 3:
                        i = 120;
                        setPresence(4);
                        break;
                    case 4:
                        i = DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP;
                        setPresence(4);
                        break;
                    case 5:
                        i = CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable;
                        setPresence(4);
                        break;
                }
                long mMNow = CmmTime.getMMNow();
                notificationSettingMgr.applySnoozeSettings((long) i, mMNow, mMNow + ((long) (i * 60000)));
                updateTimeInterval();
            }
        }
    }

    private void shutdownDND() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            long mMNow = CmmTime.getMMNow();
            notificationSettingMgr.applySnoozeSettings(0, mMNow, mMNow);
            DndSetting dndSettings = notificationSettingMgr.getDndSettings();
            this.txtTimeInterval.setVisibility(4);
            if (dndSettings != null && dndSettings.isEnable()) {
                dndSettings.setEnable(false);
                notificationSettingMgr.applyDndSettings(dndSettings);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateTimeInterval() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            long[] snoozeSettings = notificationSettingMgr.getSnoozeSettings();
            if (snoozeSettings != null) {
                this.txtTimeInterval.setVisibility(0);
                if (snoozeSettings[2] > CmmTime.getMMNow()) {
                    String formatTime = TimeUtil.formatTime((Context) getActivity(), snoozeSettings[1]);
                    String formatTime2 = TimeUtil.formatTime((Context) getActivity(), snoozeSettings[2]);
                    this.txtTimeInterval.setText(getString(C4558R.string.zm_lbl_notification_dnd_19898, formatTime, formatTime2));
                } else {
                    this.txtTimeInterval.setText("");
                }
            }
        }
    }
}
