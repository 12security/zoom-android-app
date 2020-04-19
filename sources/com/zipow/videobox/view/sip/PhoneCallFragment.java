package com.zipow.videobox.view.sip;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.ZMBuddyListListener;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.PBXLoginConflictListenerUI.SimplePBXLoginConflictListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMView.OnFragmentShowListener;
import java.io.Serializable;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkStatusReceiver.SimpleNetworkStatusListener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PhoneCallFragment extends ZMDialogFragment implements OnClickListener, OnEditorActionListener, ZMBuddyListListener, OnFragmentShowListener {
    private static final int REQUEST_PERMISSION_MIC = 11;
    private static final String TAG = "PhoneCallFragment";
    private Button mBtnClearAll;
    private Button mBtnEdit;
    private Button mBtnKeyboard;
    private TextView mBubble;
    @Nullable
    private ZMAlertDialog mClearAlertDialog;
    private ImageView mDot;
    private ImageView mEmail;
    private Handler mHandler = new Handler();
    private boolean mIsAllCallHistoryMode = true;
    /* access modifiers changed from: private */
    public boolean mIsInEditMode;
    private SimpleISIPLineMgrEventSinkListener mLineListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            if (CmmSIPLineManager.getInstance().isMineLine(str)) {
                PhoneCallFragment.this.updateServiceState();
            }
        }
    };
    /* access modifiers changed from: private */
    public PhoneCallsListview mListviewAllCalls;
    /* access modifiers changed from: private */
    public PhoneCallsListview mListviewMissedCalls;
    @NonNull
    private SimplePBXLoginConflictListener mLoginConflictListener = new SimplePBXLoginConflictListener() {
        public void onConflict() {
            super.onConflict();
            PhoneCallFragment.this.dismissClearAlertDialog();
            PhoneCallFragment.this.mListviewAllCalls.onLoginConflict();
            PhoneCallFragment.this.mListviewMissedCalls.onLoginConflict();
            PhoneCallFragment.this.mIsInEditMode = false;
            PhoneCallFragment.this.updateUIMode();
        }

        public void onResumeFromConflict() {
            super.onResumeFromConflict();
            PhoneCallFragment.this.updateUIMode();
        }
    };
    SimpleNetworkStatusListener mNetworkStatusListener = new SimpleNetworkStatusListener() {
        public void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2) {
            super.networkStatusChanged(z, i, str, z2, i2, str2);
            PhoneCallFragment.this.updateServiceState();
        }
    };
    /* access modifiers changed from: private */
    public View mPanelTabAll;
    private View mPanelTabMissed;
    /* access modifiers changed from: private */
    public View mPanelTabVoiceMailPlus;
    SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnSIPCallServiceStoped(boolean z) {
            super.OnSIPCallServiceStoped(z);
            PhoneCallFragment.this.updateServiceState();
        }

        public void OnWMIActive(boolean z) {
            super.OnWMIActive(z);
            PhoneCallFragment.this.mPanelTabVoiceMailPlus.setVisibility(z ? 0 : 8);
        }

        public void OnWMIMessageCountChanged(int i, int i2, boolean z) {
            super.OnWMIMessageCountChanged(i, i2, z);
            PhoneCallFragment.this.updateTxtVoiceMailPlusBubble(i2, z);
        }

        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            PhoneCallFragment.this.onShow();
        }

        public void OnUnloadSIPService(int i) {
            super.OnUnloadSIPService(i);
            if (i == 0 && !CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
                PhoneCallFragment.this.onSipConflict();
            }
        }
    };
    @Nullable
    private String mSelectedDisplayName;
    @Nullable
    private String mSelectedPhoneNumber;
    private TextView mTvSearch;
    private TextView mTxtConflict;
    private TextView mTxtEmptyView;

    /* access modifiers changed from: private */
    public void updateServiceState() {
    }

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_sip_call, viewGroup, false);
        this.mPanelTabAll = inflate.findViewById(C4558R.C4560id.panelTabAll);
        this.mPanelTabMissed = inflate.findViewById(C4558R.C4560id.panelTabMissed);
        this.mPanelTabVoiceMailPlus = inflate.findViewById(C4558R.C4560id.panelTabVoiceMailPlus);
        this.mListviewAllCalls = (PhoneCallsListview) inflate.findViewById(C4558R.C4560id.listviewAllCalls);
        this.mListviewMissedCalls = (PhoneCallsListview) inflate.findViewById(C4558R.C4560id.listviewMissedCalls);
        this.mBtnClearAll = (Button) inflate.findViewById(C4558R.C4560id.btnClearAll);
        this.mBtnEdit = (Button) inflate.findViewById(C4558R.C4560id.btnEdit);
        this.mBtnKeyboard = (Button) inflate.findViewById(C4558R.C4560id.btnKeyboard);
        this.mTvSearch = (TextView) inflate.findViewById(C4558R.C4560id.tvSearch);
        this.mTxtEmptyView = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mBubble = (TextView) inflate.findViewById(C4558R.C4560id.bubble);
        this.mEmail = (ImageView) inflate.findViewById(C4558R.C4560id.email);
        this.mDot = (ImageView) inflate.findViewById(C4558R.C4560id.dot);
        this.mTxtConflict = (TextView) inflate.findViewById(C4558R.C4560id.txtConflict);
        this.mPanelTabAll.setOnClickListener(this);
        this.mPanelTabMissed.setOnClickListener(this);
        this.mBtnClearAll.setOnClickListener(this);
        this.mBtnEdit.setOnClickListener(this);
        this.mBtnKeyboard.setOnClickListener(this);
        this.mTvSearch.setOnClickListener(this);
        this.mEmail.setOnClickListener(this);
        if (bundle != null) {
            this.mIsAllCallHistoryMode = bundle.getBoolean("mIsAllCallHistoryMode", true);
            this.mIsInEditMode = bundle.getBoolean("mIsInEditMode");
        }
        this.mListviewAllCalls.setParentFragment(this);
        this.mListviewMissedCalls.setParentFragment(this);
        this.mListviewMissedCalls.setShowMissedHistory(true);
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineListener);
        CmmSIPCallManager.getInstance().addNetworkListener(this.mNetworkStatusListener);
        CmmSIPCallManager.getInstance().addPBXLoginConflictListener(this.mLoginConflictListener);
        return inflate;
    }

    public void updateTxtVoiceMailPlusBubble(int i, boolean z) {
        String str = "";
        if (i > 99) {
            str = "99+";
        } else if (i > 0) {
            str = String.valueOf(i);
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mBubble.setText(str);
            this.mBubble.setVisibility(0);
            this.mDot.setVisibility(4);
        } else if (i != 0 || !z) {
            this.mBubble.setVisibility(4);
            this.mDot.setVisibility(4);
        } else {
            this.mBubble.setVisibility(4);
            this.mDot.setVisibility(0);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (SipBatteryOptDialog.checkIfShow() && CmmSIPCallManager.getInstance().isOldAccount()) {
            SipBatteryOptDialog.newInstance().show(getActivity().getSupportFragmentManager(), SipBatteryOptDialog.class.getName());
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mIsAllCallHistoryMode", this.mIsAllCallHistoryMode);
        bundle.putBoolean("mIsInEditMode", this.mIsInEditMode);
    }

    public void onCallHistoryDeleted(String str) {
        this.mListviewAllCalls.onDeleteHistoryCall(str);
        this.mListviewMissedCalls.onDeleteHistoryCall(str);
        updateEmptyView();
    }

    public void onResume() {
        super.onResume();
        updateUIMode();
        ZMBuddySyncInstance.getInsatance().addListener(this);
    }

    public void onPause() {
        ZMBuddySyncInstance.getInsatance().removeListener(this);
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void updateUIMode() {
        this.mPanelTabAll.setSelected(this.mIsAllCallHistoryMode);
        this.mPanelTabMissed.setSelected(!this.mIsAllCallHistoryMode);
        if (this.mIsAllCallHistoryMode) {
            this.mListviewAllCalls.setVisibility(0);
            this.mListviewMissedCalls.setVisibility(8);
        } else {
            this.mListviewAllCalls.setVisibility(8);
            this.mListviewMissedCalls.setVisibility(0);
        }
        upateEditMode();
        this.mListviewAllCalls.setDeleteMode(this.mIsInEditMode);
        this.mListviewMissedCalls.setDeleteMode(this.mIsInEditMode);
        updateEmptyView();
        updateServiceState();
    }

    private void upateEditMode() {
        if (this.mIsInEditMode) {
            this.mBtnKeyboard.setVisibility(8);
            this.mBtnEdit.setText(C4558R.string.zm_btn_done);
            this.mBtnClearAll.setVisibility(0);
            return;
        }
        this.mBtnKeyboard.setVisibility(0);
        this.mBtnEdit.setText(C4558R.string.zm_btn_edit);
        this.mBtnClearAll.setVisibility(8);
        boolean z = true;
        boolean z2 = this.mListviewAllCalls.getVisibility() != 0 ? this.mListviewMissedCalls.getCount() == 0 : this.mListviewAllCalls.getCount() == 0;
        Button button = this.mBtnEdit;
        if (z2 || CmmSIPCallManager.getInstance().isLoginConflict()) {
            z = false;
        }
        button.setEnabled(z);
    }

    /* access modifiers changed from: private */
    public void updateEmptyView() {
        if (this.mListviewAllCalls.getVisibility() == 0) {
            if (this.mListviewAllCalls.getCount() == 0) {
                this.mTxtEmptyView.setVisibility(0);
                this.mIsInEditMode = false;
            } else {
                this.mTxtEmptyView.setVisibility(8);
            }
        } else if (this.mListviewMissedCalls.getCount() == 0) {
            this.mTxtEmptyView.setVisibility(0);
            this.mIsInEditMode = false;
        } else {
            this.mTxtEmptyView.setVisibility(8);
        }
        upateEditMode();
    }

    private void onClickPanelTabAll() {
        this.mIsAllCallHistoryMode = true;
        this.mIsInEditMode = false;
        updateUIMode();
    }

    private void onClickPanelTabMissed() {
        this.mIsAllCallHistoryMode = false;
        this.mIsInEditMode = false;
        updateUIMode();
    }

    private void onClickBtnClear() {
        this.mIsInEditMode = false;
        updateUIMode();
    }

    private void onClickBtnEdit() {
        this.mIsInEditMode = !this.mIsInEditMode;
        updateUIMode();
    }

    private void onClickBtnKeyboard() {
        SipDialKeyboardFragment.showAsActivity((Fragment) this, 0);
    }

    private void onClickBtnClearAll() {
        dismissClearAlertDialog();
        this.mClearAlertDialog = new Builder(getActivity()).setTitle(C4558R.string.zm_mm_msg_sip_clear_all_recent_14480).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
                if (callHistoryMgr != null) {
                    callHistoryMgr.clearAllCallHistory();
                }
                PhoneCallFragment.this.mListviewAllCalls.loadAllRecentCalls();
                PhoneCallFragment.this.mListviewMissedCalls.loadAllRecentCalls();
                PhoneCallFragment.this.updateEmptyView();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
        this.mClearAlertDialog.show();
    }

    /* access modifiers changed from: private */
    public void dismissClearAlertDialog() {
        ZMAlertDialog zMAlertDialog = this.mClearAlertDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mClearAlertDialog.dismiss();
            this.mClearAlertDialog = null;
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.panelTabAll) {
                onClickPanelTabAll();
            } else if (id == C4558R.C4560id.panelTabMissed) {
                onClickPanelTabMissed();
            } else if (id == C4558R.C4560id.btnClear) {
                onClickBtnClear();
            } else if (id == C4558R.C4560id.btnEdit) {
                onClickBtnEdit();
            } else if (id == C4558R.C4560id.btnKeyboard) {
                onClickBtnKeyboard();
            } else if (id == C4558R.C4560id.btnClearAll) {
                onClickBtnClearAll();
            } else if (id == C4558R.C4560id.tvSearch) {
                onClickSearch();
            } else if (id == C4558R.C4560id.email) {
                onClickPanelTabVoiceMailPlus();
            }
        }
    }

    private void onClickPanelTabVoiceMailPlus() {
        SipPhoneIntegration sipIntergration = CmmSIPCallManager.getInstance().getSipIntergration();
        if (sipIntergration != null) {
            String voiceMail = sipIntergration.getVoiceMail();
            if (!StringUtil.isEmptyOrNull(voiceMail)) {
                onPickSipResult(voiceMail, voiceMail);
            }
        }
    }

    private void onClickSearch() {
        this.mSelectedPhoneNumber = null;
        this.mSelectedDisplayName = null;
        PhoneSearchFragment.showAsFragment(this, null, PhoneSearchFragment.ACTION_PICK_SIP);
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mTvSearch);
        return true;
    }

    public void onBuddyListUpdate() {
        this.mListviewAllCalls.loadAllRecentCalls();
        this.mListviewMissedCalls.loadAllRecentCalls();
    }

    public void onBuddyInfoUpdate(List<String> list, @NonNull List<String> list2) {
        this.mListviewAllCalls.updateZoomBuddyInfo(list2);
        this.mListviewMissedCalls.updateZoomBuddyInfo(list2);
    }

    public void onActivityResult(int i, int i2, @NonNull Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1090 && i2 == -1) {
            Serializable serializableExtra = intent.getSerializableExtra(PhoneSearchFragment.ARG_IM_ADDR_BOOK_ITEM);
            if (serializableExtra != null) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) serializableExtra;
                onPickSipResult(iMAddrBookItem.getSipPhoneNumber(), iMAddrBookItem.getScreenName());
            }
        }
    }

    public void onPickSipResult(@Nullable String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                callSip(str, str2);
                return;
            }
            this.mSelectedPhoneNumber = str;
            this.mSelectedDisplayName = str2;
            zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 11);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C40216 r2 = new EventAction("PhoneCallFragmentPermissionResult") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof PhoneCallFragment) {
                        PhoneCallFragment phoneCallFragment = (PhoneCallFragment) iUIElement;
                        if (phoneCallFragment.isAdded()) {
                            phoneCallFragment.handleRequestPermissionResult(i2, strArr2, iArr2);
                        }
                    }
                }
            };
            eventTaskManager.pushLater("PhoneCallFragmentPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null && i == 11 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
            String str = this.mSelectedPhoneNumber;
            if (str != null) {
                callSip(str, this.mSelectedDisplayName);
            }
            this.mSelectedPhoneNumber = null;
            this.mSelectedDisplayName = null;
        }
    }

    private void callSip(@NonNull String str, String str2) {
        if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
            CmmSIPCallManager.getInstance().callPeer(str, str2);
        }
    }

    public void onDestroyView() {
        dismissClearAlertDialog();
        CmmSIPCallManager.getInstance().removeNetworkListener(this.mNetworkStatusListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineListener);
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
        CmmSIPCallManager.getInstance().removePBXLoginConflictListener(this.mLoginConflictListener);
        super.onDestroyView();
    }

    public boolean isInEditMode() {
        return this.mIsInEditMode;
    }

    public boolean onBackPressed() {
        if (!this.mIsInEditMode) {
            return false;
        }
        onClickBtnEdit();
        return true;
    }

    public void onShow() {
        if (!this.mIsInEditMode) {
            PhoneCallsListview phoneCallsListview = this.mListviewAllCalls;
            if (phoneCallsListview != null) {
                phoneCallsListview.loadAllRecentCalls();
            }
            PhoneCallsListview phoneCallsListview2 = this.mListviewMissedCalls;
            if (phoneCallsListview2 != null) {
                phoneCallsListview2.loadAllRecentCalls();
            }
            updateEmptyView();
        }
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z && isAdded() && this.mListviewAllCalls != null && this.mListviewMissedCalls != null) {
            onShow();
        }
    }

    public void switchToHistory() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (PhoneCallFragment.this.isAdded() && PhoneCallFragment.this.mPanelTabAll != null) {
                    PhoneCallFragment.this.mPanelTabAll.performClick();
                }
            }
        }, 200);
    }

    /* access modifiers changed from: private */
    public void onSipConflict() {
        this.mTxtConflict.setVisibility(0);
    }
}
