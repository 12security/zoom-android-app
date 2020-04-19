package com.zipow.videobox.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
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
import com.zipow.videobox.view.p014mm.MMNotificationExceptionGroupSettingsListView;
import com.zipow.videobox.view.p014mm.MMZoomGroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.videomeetings.C4558R;

public class MMNotificationExceptionGroupsSettingsFragment extends ZMDialogFragment implements OnItemClickListener, OnClickListener, KeyboardListener, OnScrollListener, ExtListener {
    private final int REQUEST_MUC_SETTINGS = 1;
    private View btnRight;
    private boolean isKeyboardOpen;
    /* access modifiers changed from: private */
    public Button mBtnClearSearchView;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    private EditText mEdtSearch;
    private EditText mEdtSearchReal;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public MMNotificationExceptionGroupSettingsListView mListView;
    @NonNull
    private INotificationSettingUIListener mNotificationListener = new SimpleNotificationSettingUIListener() {
        public void OnMUCSettingUpdated() {
            MMNotificationExceptionGroupsSettingsFragment.this.OnMUCSettingUpdated();
        }

        public void OnDNDSettingsUpdated() {
            MMNotificationExceptionGroupsSettingsFragment.this.OnDNDSettingsUpdated();
        }
    };
    /* access modifiers changed from: private */
    public FrameLayout mPanelListView;
    private View mPanelSearch;
    private View mPanelSearchBarReal;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    @NonNull
    private SearchFilterRunnable mRunnableFilter = new SearchFilterRunnable();
    @NonNull
    private HashMap<String, Integer> mSettings = new HashMap<>();
    @NonNull
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMNotificationExceptionGroupsSettingsFragment.this.onGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMNotificationExceptionGroupsSettingsFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    public class SearchFilterRunnable implements Runnable {
        @NonNull
        private String mKey = "";

        public SearchFilterRunnable() {
        }

        public void setKey(@Nullable String str) {
            if (str == null) {
                str = "";
            }
            this.mKey = str;
        }

        @NonNull
        public String getKey() {
            return this.mKey;
        }

        public void run() {
            String key = getKey();
            MMNotificationExceptionGroupsSettingsFragment.this.mListView.setFilter(this.mKey);
            if ((key.length() <= 0 || MMNotificationExceptionGroupsSettingsFragment.this.mListView.getCount() <= 0) && MMNotificationExceptionGroupsSettingsFragment.this.mPanelTitleBar.getVisibility() != 0) {
                MMNotificationExceptionGroupsSettingsFragment.this.mPanelListView.setForeground(MMNotificationExceptionGroupsSettingsFragment.this.mDimmedForground);
            } else {
                MMNotificationExceptionGroupsSettingsFragment.this.mPanelListView.setForeground(null);
            }
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@Nullable Fragment fragment, int i) {
        if (fragment != null) {
            SimpleActivity.show(fragment, MMNotificationExceptionGroupsSettingsFragment.class.getName(), new Bundle(), i, true);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_notification_exception_groups_settings, viewGroup, false);
        this.mListView = (MMNotificationExceptionGroupSettingsListView) inflate.findViewById(C4558R.C4560id.listView);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mPanelListView = (FrameLayout) inflate.findViewById(C4558R.C4560id.panelListView);
        this.mPanelSearchBarReal = inflate.findViewById(C4558R.C4560id.panelSearchBarReal);
        this.mEdtSearchReal = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchReal);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelSearch = inflate.findViewById(C4558R.C4560id.panelSearch);
        this.btnRight = inflate.findViewById(C4558R.C4560id.btnRight);
        this.mListView.setOnItemClickListener(this);
        this.mListView.setOnScrollListener(this);
        this.mEdtSearch.setCursorVisible(false);
        this.mBtnClearSearchView.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.btnRight.setOnClickListener(this);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable("mSettings");
            if (serializable != null) {
                this.mSettings = (HashMap) serializable;
            }
        }
        this.mEdtSearchReal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull final Editable editable) {
                MMNotificationExceptionGroupsSettingsFragment.this.mBtnClearSearchView.setVisibility(editable.length() > 0 ? 0 : 8);
                MMNotificationExceptionGroupsSettingsFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (MMNotificationExceptionGroupsSettingsFragment.this.isResumed()) {
                            MMNotificationExceptionGroupsSettingsFragment.this.startFilter(editable.toString());
                        }
                    }
                });
            }
        });
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mSettings", this.mSettings);
    }

    private void updateUI() {
        this.mListView.updateData(this.mSettings);
        this.btnRight.setEnabled(!this.mSettings.isEmpty());
    }

    public void onKeyboardOpen() {
        this.isKeyboardOpen = true;
        if (this.mEdtSearch.hasFocus()) {
            this.mPanelTitleBar.setVisibility(8);
            this.mPanelListView.setForeground(this.mDimmedForground);
            this.mPanelSearchBarReal.setVisibility(0);
            this.mPanelSearch.setVisibility(8);
            this.mEdtSearchReal.setText("");
            this.mEdtSearchReal.requestFocus();
        }
    }

    public void onKeyboardClosed() {
        this.isKeyboardOpen = false;
        if (this.mEdtSearchReal.length() == 0 || this.mListView.getCount() == 0) {
            this.mPanelListView.setForeground(null);
            this.mEdtSearchReal.setText("");
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelSearchBarReal.setVisibility(4);
            this.mPanelSearch.setVisibility(0);
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                MMNotificationExceptionGroupsSettingsFragment.this.mListView.requestLayout();
            }
        });
    }

    public boolean onBackPressed() {
        return handleBackPressed();
    }

    public void onStop() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        NotificationSettingUI.getInstance().removeListener(this.mNotificationListener);
        super.onStop();
    }

    public void onStart() {
        super.onStart();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        NotificationSettingUI.getInstance().addListener(this.mNotificationListener);
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    /* access modifiers changed from: private */
    public void startFilter(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        if (!str.equals(this.mRunnableFilter.getKey())) {
            this.mRunnableFilter.setKey(str);
            this.mHandler.removeCallbacks(this.mRunnableFilter);
            this.mHandler.postDelayed(this.mRunnableFilter, 300);
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(int i, @Nullable GroupAction groupAction, String str) {
        if (groupAction != null) {
            this.mListView.updateGroup(groupAction.getGroupId());
        }
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        this.mListView.removeGroup(str);
    }

    public void OnMUCSettingUpdated() {
        this.mListView.updateData(this.mSettings);
    }

    public void OnDNDSettingsUpdated() {
        this.mListView.updateData(this.mSettings);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        int i2;
        MMZoomGroup item = this.mListView.getItem(i);
        if (item != null) {
            Integer num = (Integer) this.mSettings.get(item.getGroupId());
            if (num != null) {
                i2 = num.intValue();
            } else {
                i2 = item.getNotifyType();
            }
            if (i2 == 0) {
                i2 = getDefaultSettings();
            }
            MMNotificationGroupSettingsFragment.showAsActivity(this, item.getGroupId(), i2, 1);
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 1 && i2 == -1 && intent != null) {
            onGroupNotificationSettings(intent.getStringExtra("sessionId"), intent.getIntExtra(MMNotificationGroupSettingsFragment.RESULT_MUC_TYPE, 1));
        }
    }

    private void onGroupNotificationSettings(String str, int i) {
        if (!StringUtil.isEmptyOrNull(str)) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                MUCNotifySettings mUCDiffFromGeneralSetting = notificationSettingMgr.getMUCDiffFromGeneralSetting();
                if (mUCDiffFromGeneralSetting != null) {
                    int i2 = 0;
                    for (MUCNotifySettingItem mUCNotifySettingItem : mUCDiffFromGeneralSetting.getItemsList()) {
                        if (StringUtil.isSameString(mUCNotifySettingItem.getSessionId(), str)) {
                            i2 = mUCNotifySettingItem.getType();
                        }
                    }
                    if (i == i2) {
                        this.mSettings.remove(str);
                    } else if (i2 == 0 && i == getDefaultSettings()) {
                        this.mSettings.remove(str);
                    } else {
                        this.mSettings.put(str, Integer.valueOf(i));
                    }
                    updateUI();
                }
            }
        }
    }

    private int getDefaultSettings() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr == null) {
            return 1;
        }
        int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
        if (blockAllSettings == null) {
            return 1;
        }
        int i = blockAllSettings[0];
        int i2 = blockAllSettings[1];
        if (i == 1 && i2 == 1) {
            return 1;
        }
        if (i == 2) {
            return 3;
        }
        if (i == 1 && i2 == 4) {
            return 2;
        }
        return 1;
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
        } else if (id == C4558R.C4560id.btnRight) {
            onClickBtnRight();
        }
    }

    private void onClickBtnRight() {
        if (!this.mSettings.isEmpty()) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                ArrayList arrayList4 = new ArrayList();
                for (Entry entry : this.mSettings.entrySet()) {
                    Integer num = (Integer) entry.getValue();
                    if (num != null) {
                        switch (num.intValue()) {
                            case 1:
                                arrayList.add(entry.getKey());
                                break;
                            case 2:
                                arrayList2.add(entry.getKey());
                                break;
                            case 3:
                                arrayList3.add(entry.getKey());
                                break;
                            default:
                                arrayList4.add(entry.getKey());
                                break;
                        }
                    }
                }
                if (!CollectionsUtil.isListEmpty(arrayList)) {
                    notificationSettingMgr.applyMUCSettings((List<String>) arrayList, 1);
                }
                if (!CollectionsUtil.isListEmpty(arrayList2)) {
                    notificationSettingMgr.applyMUCSettings((List<String>) arrayList2, 2);
                }
                if (!CollectionsUtil.isListEmpty(arrayList3)) {
                    notificationSettingMgr.applyMUCSettings((List<String>) arrayList3, 3);
                }
                if (!CollectionsUtil.isListEmpty(arrayList4)) {
                    notificationSettingMgr.resetMUCSettings(arrayList4);
                }
                this.mSettings.clear();
                dismiss();
            }
        }
    }

    public boolean handleBackPressed() {
        if (this.mPanelSearchBarReal.getVisibility() != 0) {
            return false;
        }
        this.mPanelTitleBar.setVisibility(0);
        this.mPanelSearchBarReal.setVisibility(4);
        this.mPanelSearch.setVisibility(0);
        this.mEdtSearchReal.setText("");
        return true;
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearchReal.setText("");
        if (!this.isKeyboardOpen) {
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelSearchBarReal.setVisibility(4);
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelSearch.setVisibility(0);
            this.mHandler.post(new Runnable() {
                public void run() {
                    MMNotificationExceptionGroupsSettingsFragment.this.mListView.requestLayout();
                }
            });
        }
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        dismiss();
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 2) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        }
    }
}
