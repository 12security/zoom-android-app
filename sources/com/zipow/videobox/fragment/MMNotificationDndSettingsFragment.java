package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr.DndSetting;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.ZMTimePickerDialog;
import p021us.zoom.androidlib.widget.ZMTimePickerDialog.OnTimeSetListener;
import p021us.zoom.videomeetings.C4558R;

public class MMNotificationDndSettingsFragment extends ZMDialogFragment implements OnClickListener {
    private CheckedTextView mChkDndScheduled;
    @NonNull
    private INotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnDNDSettingsUpdated() {
            MMNotificationDndSettingsFragment.this.onDNDSettingsUpdated();
        }

        public void OnDNDNowSettingUpdated() {
            MMNotificationDndSettingsFragment.this.onDNDNowSettingUpdated();
        }

        public void OnSnoozeSettingsUpdated() {
            MMNotificationDndSettingsFragment.this.onSnoozeSettingsUpdated();
        }
    };
    private View mPanelDndFrom;
    private View mPanelDndTo;
    /* access modifiers changed from: private */
    @Nullable
    public ZMTimePickerDialog mTimePickerDialog;
    private TextView mTxtSnoozed;
    private TextView mTxtTimeFrom;
    private TextView mTxtTimeTo;

    public static class SnoozedContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_1_HOUR = 2;
        public static final int ACTION_20_MINUTE = 1;
        public static final int ACTION_24_HOUR = 6;
        public static final int ACTION_2_HOUR = 3;
        public static final int ACTION_4_HOUR = 4;
        public static final int ACTION_8_HOUR = 5;
        public static final int ACTION_TURN_OFF = 0;

        public SnoozedContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, MMNotificationDndSettingsFragment.class.getName(), new Bundle(), 0);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_notification_dnd, viewGroup, false);
        this.mChkDndScheduled = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkDndScheduled);
        this.mTxtTimeFrom = (TextView) inflate.findViewById(C4558R.C4560id.txtTimeFrom);
        this.mTxtTimeTo = (TextView) inflate.findViewById(C4558R.C4560id.txtTimeTo);
        this.mTxtSnoozed = (TextView) inflate.findViewById(C4558R.C4560id.txtSnoozed);
        this.mPanelDndFrom = inflate.findViewById(C4558R.C4560id.panelDndFrom);
        this.mPanelDndTo = inflate.findViewById(C4558R.C4560id.panelDndTo);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mPanelDndFrom.setOnClickListener(this);
        this.mPanelDndTo.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelSnoozed).setOnClickListener(this);
        this.mChkDndScheduled.setOnClickListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateUI();
        NotificationSettingUI.getInstance().addListener(this.mListener);
    }

    public void onPause() {
        NotificationSettingUI.getInstance().removeListener(this.mListener);
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            DndSetting dndSettings = notificationSettingMgr.getDndSettings();
            if (dndSettings != null) {
                if (dndSettings.isEnable()) {
                    this.mChkDndScheduled.setChecked(true);
                    this.mPanelDndTo.setVisibility(0);
                    this.mPanelDndFrom.setVisibility(0);
                } else {
                    this.mChkDndScheduled.setChecked(false);
                    this.mPanelDndTo.setVisibility(8);
                    this.mPanelDndFrom.setVisibility(8);
                }
                String formatTime = dndSettings.isEnable() ? TimeUtil.formatTime((Context) getActivity(), dndSettings.getStart()) : "";
                String formatTime2 = dndSettings.isEnable() ? TimeUtil.formatTime((Context) getActivity(), dndSettings.getEnd()) : "";
                this.mTxtTimeFrom.setText(formatTime);
                this.mTxtTimeTo.setText(formatTime2);
            }
            long[] snoozeSettings = notificationSettingMgr.getSnoozeSettings();
            if (snoozeSettings != null) {
                if (snoozeSettings[2] > CmmTime.getMMNow()) {
                    this.mTxtSnoozed.setText(getString(C4558R.string.zm_lbl_notification_dnd_19898, TimeUtil.formatTime((Context) getActivity(), snoozeSettings[1]), TimeUtil.formatTime((Context) getActivity(), snoozeSettings[2])));
                } else {
                    this.mTxtSnoozed.setText("");
                }
            }
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    /* access modifiers changed from: private */
    public void onDNDSettingsUpdated() {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onDNDNowSettingUpdated() {
        updateUI();
    }

    public void onSnoozeSettingsUpdated() {
        updateUI();
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                onClickBtnBack();
            } else if (id == C4558R.C4560id.panelDndFrom) {
                onClickPanelDndFrom();
            } else if (id == C4558R.C4560id.panelDndTo) {
                onClickPanelDndTo();
            } else if (id == C4558R.C4560id.chkDndScheduled) {
                onClickChkDndScheduled();
            } else if (id == C4558R.C4560id.panelSnoozed) {
                onClickPanelSnoozed();
            }
        }
    }

    private void onClickPanelSnoozed() {
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
                        MMNotificationDndSettingsFragment.this.onSelectContextMenuItem((SnoozedContextMenuItem) zMMenuAdapter.getItem(i));
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
                switch (snoozedContextMenuItem.getAction()) {
                    case 1:
                        i = 20;
                        break;
                    case 2:
                        i = 60;
                        break;
                    case 3:
                        i = 120;
                        break;
                    case 4:
                        i = DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP;
                        break;
                    case 5:
                        i = CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable;
                        break;
                    case 6:
                        i = 1440;
                        break;
                }
                long mMNow = CmmTime.getMMNow();
                notificationSettingMgr.applySnoozeSettings((long) i, mMNow, mMNow + ((long) (i * 60000)));
                updateUI();
            }
        }
    }

    private void onClickChkDndScheduled() {
        int i;
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            DndSetting dndSettings = notificationSettingMgr.getDndSettings();
            if (dndSettings != null) {
                int i2 = 0;
                if (dndSettings.isEnable()) {
                    dndSettings.setEnable(false);
                } else {
                    dndSettings.setEnable(true);
                    DndSetting historyDNDSetting = notificationSettingMgr.getHistoryDNDSetting();
                    int i3 = 17;
                    int i4 = 9;
                    if (historyDNDSetting != null) {
                        i3 = historyDNDSetting.getStart().get(11);
                        i2 = historyDNDSetting.getStart().get(12);
                        i4 = historyDNDSetting.getEnd().get(11);
                        i = historyDNDSetting.getEnd().get(12);
                    } else {
                        i = 0;
                    }
                    Calendar instance = Calendar.getInstance();
                    instance.set(11, i3);
                    instance.set(12, i2);
                    Calendar instance2 = Calendar.getInstance();
                    instance2.set(11, i4);
                    instance2.set(12, i);
                    dndSettings.setEnd(instance2);
                    dndSettings.setStart(instance);
                }
                notificationSettingMgr.applyDndSettings(dndSettings);
                updateUI();
            }
        }
    }

    private void onClickPanelDndFrom() {
        if (this.mTimePickerDialog == null) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                DndSetting dndSettings = notificationSettingMgr.getDndSettings();
                if (dndSettings != null) {
                    Calendar start = dndSettings.getStart();
                    if (start == null) {
                        start = Calendar.getInstance();
                    }
                    ZMTimePickerDialog zMTimePickerDialog = new ZMTimePickerDialog(getActivity(), new OnTimeSetListener() {
                        public void onTimeSet(TimePicker timePicker, int i, int i2) {
                            MMNotificationDndSettingsFragment.this.mTimePickerDialog = null;
                            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                            if (notificationSettingMgr != null) {
                                DndSetting dndSettings = notificationSettingMgr.getDndSettings();
                                if (dndSettings != null) {
                                    Calendar instance = Calendar.getInstance();
                                    instance.set(11, i);
                                    instance.set(12, i2);
                                    dndSettings.setStart(instance);
                                    notificationSettingMgr.applyDndSettings(dndSettings);
                                    MMNotificationDndSettingsFragment.this.updateUI();
                                }
                            }
                        }
                    }, start.get(11), start.get(12), DateFormat.is24HourFormat(getActivity()));
                    this.mTimePickerDialog = zMTimePickerDialog;
                    this.mTimePickerDialog.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            MMNotificationDndSettingsFragment.this.mTimePickerDialog = null;
                        }
                    });
                    this.mTimePickerDialog.show();
                }
            }
        }
    }

    private void onClickPanelDndTo() {
        if (this.mTimePickerDialog == null) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                DndSetting dndSettings = notificationSettingMgr.getDndSettings();
                if (dndSettings != null) {
                    Calendar end = dndSettings.getEnd();
                    if (end == null) {
                        end = Calendar.getInstance();
                    }
                    ZMTimePickerDialog zMTimePickerDialog = new ZMTimePickerDialog(getActivity(), new OnTimeSetListener() {
                        public void onTimeSet(TimePicker timePicker, int i, int i2) {
                            MMNotificationDndSettingsFragment.this.mTimePickerDialog = null;
                            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                            if (notificationSettingMgr != null) {
                                DndSetting dndSettings = notificationSettingMgr.getDndSettings();
                                if (dndSettings != null) {
                                    Calendar instance = Calendar.getInstance();
                                    instance.set(11, i);
                                    instance.set(12, i2);
                                    dndSettings.setEnd(instance);
                                    notificationSettingMgr.applyDndSettings(dndSettings);
                                    MMNotificationDndSettingsFragment.this.updateUI();
                                }
                            }
                        }
                    }, end.get(11), end.get(12), DateFormat.is24HourFormat(getActivity()));
                    this.mTimePickerDialog = zMTimePickerDialog;
                    this.mTimePickerDialog.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            MMNotificationDndSettingsFragment.this.mTimePickerDialog = null;
                        }
                    });
                    this.mTimePickerDialog.show();
                }
            }
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }
}
