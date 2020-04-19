package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettingItem;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettings;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.view.p014mm.MMNotificationExceptionGroupListView;
import com.zipow.videobox.view.p014mm.MMZoomGroup;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMNotificationExceptionGroupFragment extends ZMDialogFragment implements OnClickListener, OnItemClickListener {
    private static final int REQUEST_ADD_GROUP = 1;
    private MMNotificationExceptionGroupListView mListView;
    @NonNull
    private INotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnMUCSettingUpdated() {
            MMNotificationExceptionGroupFragment.this.OnMUCSettingUpdated();
        }
    };
    @NonNull
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMNotificationExceptionGroupFragment.this.onGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMNotificationExceptionGroupFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, MMNotificationExceptionGroupFragment.class.getName(), new Bundle(), 0);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_notification_exception_group, viewGroup, false);
        this.mListView = (MMNotificationExceptionGroupListView) inflate.findViewById(C4558R.C4560id.listView);
        this.mListView.setParentFragment(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mListView.setOnItemClickListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        NotificationSettingUI.getInstance().addListener(this.mListener);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        updateUI();
    }

    public void onPause() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        NotificationSettingUI.getInstance().removeListener(this.mListener);
        super.onPause();
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
    public void OnMUCSettingUpdated() {
        updateUI();
    }

    private void updateUI() {
        MMNotificationExceptionGroupListView mMNotificationExceptionGroupListView = this.mListView;
        if (mMNotificationExceptionGroupListView != null) {
            mMNotificationExceptionGroupListView.updateData();
        }
    }

    public void addExceptionGroup() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            ArrayList arrayList = new ArrayList();
            MUCNotifySettings mUCDiffFromGeneralSetting = notificationSettingMgr.getMUCDiffFromGeneralSetting();
            if (mUCDiffFromGeneralSetting != null) {
                for (MUCNotifySettingItem sessionId : mUCDiffFromGeneralSetting.getItemsList()) {
                    arrayList.add(sessionId.getSessionId());
                }
            }
            MMSelectGroupFragment.showAsActivity(this, true, arrayList, getString(C4558R.string.zm_lbl_notification_add_exception_group_59554), 1);
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 1 && intent != null && i2 == -1) {
            ArrayList stringArrayListExtra = intent.getStringArrayListExtra(MMSelectGroupFragment.RESULT_SELECT_GROUPS);
            if (!CollectionsUtil.isListEmpty(stringArrayListExtra)) {
                setExceptionForGroup(stringArrayListExtra);
            }
        }
    }

    private void setExceptionForGroup(List<String> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
                if (blockAllSettings != null) {
                    int i = blockAllSettings[0];
                    int i2 = 1;
                    int i3 = blockAllSettings[1];
                    if (!(i == 1 && i3 == 1)) {
                        if (i == 2) {
                            i2 = 3;
                        } else if (i == 1 && i3 == 4) {
                            i2 = 2;
                        }
                    }
                    notificationSettingMgr.applyMUCSettings(list, i2);
                    this.mListView.updateData();
                }
            }
        }
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        MMZoomGroup item = this.mListView.getItem(i);
        if (item != null) {
            MMNotificationGroupSettingsFragment.showAsActivity(this, item.getGroupId());
        }
    }
}
