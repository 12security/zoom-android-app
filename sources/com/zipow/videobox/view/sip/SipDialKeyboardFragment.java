package com.zipow.videobox.view.sip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.dialog.ConfirmAlertDialog;
import com.zipow.videobox.dialog.ConfirmAlertDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSipLineInfoForCallerID;
import com.zipow.videobox.ptapp.PTAppProtos.PBXNumber;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.ZMPhoneSearchHelper.NumberMatchedItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.sip.server.ZMPhoneNumberHelper;
import com.zipow.videobox.util.E911Utils;
import com.zipow.videobox.util.SipPopUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.BigRoundListDialog;
import com.zipow.videobox.view.BigRoundListDialog.DialogCallback;
import com.zipow.videobox.view.CallerIdListItem;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.LineCallerIdListItem;
import com.zipow.videobox.view.PBXNumberCallerIdListItem;
import com.zipow.videobox.view.sip.DialKeyboardView.OnKeyDialListener;
import com.zipow.videobox.view.sip.SipTransferOptionAdapter.SipTransferMenuItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkStatusReceiver.SimpleNetworkStatusListener;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.IZMListItem;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMPopupWindow;
import p021us.zoom.videomeetings.C4558R;

public class SipDialKeyboardFragment extends ZMDialogFragment implements OnClickListener, OnKeyDialListener, OnLongClickListener {
    public static final int ACTION_ADD_CALL = 1;
    public static final int ACTION_NORMAL = 0;
    public static final int ACTION_TRANSFER = 2;
    public static final String ARGS_DIAL_ACTION = "dial_action";
    public static final String ARGS_PHONE_NUMBER = "phone_number";
    public static final String ARGS_RELATED_CALL_ID = "related_call_id";
    public static final int REQUEST_PERMISSION_MIC = 12;
    private static final String TAG = "SipDialKeyboardFragment";
    public static final String TAG_RELOAD_USER_CONFIG = "reload_user_config";
    public static final int TONE_LENGTH_MS = 150;
    @Nullable
    private AudioManager mAudioManager;
    private TextView mBtnCoseInSip;
    private ImageView mBtnDial;
    /* access modifiers changed from: private */
    @Nullable
    public BigRoundListDialog mCallerIDDialog;
    @NonNull
    private Runnable mDTMFReleaseRunnable = new Runnable() {
        public void run() {
            if (SipDialKeyboardFragment.this.mDtmfGenerator != null) {
                SipDialKeyboardFragment.this.mDtmfGenerator.release();
            }
            SipDialKeyboardFragment.this.mDtmfGenerator = null;
        }
    };
    private Runnable mDTMFSearchRunnable = new Runnable() {
        public void run() {
            String trim = SipDialKeyboardFragment.this.mTxtDialNum.getText().toString().trim();
            if (TextUtils.isEmpty(trim)) {
                SipDialKeyboardFragment.this.mTxtDialUserName.setText("");
                SipDialKeyboardFragment.this.mTxtDialUserName.setTag(null);
            } else if (ZMPhoneNumberHelper.isInvalidPhoneNumberLength(trim)) {
                SipDialKeyboardFragment.this.mTxtDialUserName.setText("");
                SipDialKeyboardFragment.this.mTxtDialUserName.setTag(null);
            } else {
                NumberMatchedItem searchByNumber = ZMPhoneSearchHelper.getInstance().searchByNumber(trim);
                NumberMatchedItem searchByNumber2 = (trim.length() <= 6 || (searchByNumber != null && searchByNumber.hasMatched())) ? searchByNumber : ZMPhoneSearchHelper.getInstance().searchByNumber(ZMPhoneUtils.formatPhoneNumberAsE164(trim));
                boolean z = searchByNumber2 != null && searchByNumber2.hasMatched();
                String safeString = StringUtil.safeString(z ? searchByNumber2.getDisplayName() : "");
                TextView access$200 = SipDialKeyboardFragment.this.mTxtDialUserName;
                if (!z) {
                    searchByNumber2 = null;
                }
                access$200.setTag(searchByNumber2);
                if (!TextUtils.isEmpty(safeString) || !(SipDialKeyboardFragment.this.mTxtDialNum.getTag() instanceof String)) {
                    SipDialKeyboardFragment.this.mTxtDialUserName.setText(safeString);
                    SipDialKeyboardFragment.this.mTxtDialNum.setTag(null);
                } else {
                    SipDialKeyboardFragment.this.mTxtDialUserName.setText((String) SipDialKeyboardFragment.this.mTxtDialNum.getTag());
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int mDialAction;
    /* access modifiers changed from: private */
    @Nullable
    public ToneGenerator mDtmfGenerator;
    @NonNull
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ISIPCallEventListener mISIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnSIPCallServiceStarted() {
            SipDialKeyboardFragment.this.HandleSIPOnSIPServiceStatusChangedNotification(1);
        }

        public void OnSIPCallServiceStoped(boolean z) {
            SipDialKeyboardFragment.this.HandleSIPOnSIPServiceStatusChangedNotification(0);
        }

        public void OnCallStatusUpdate(String str, int i) {
            SipDialKeyboardFragment.this.onSipCallEvent(i, str);
        }

        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            if (!SipDialKeyboardFragment.this.isRelatedCallTerminate(str) || SipDialKeyboardFragment.this.mDialAction == 0) {
                SipDialKeyboardFragment.this.updateUI();
            } else {
                SipDialKeyboardFragment.this.dismiss();
            }
        }

        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            SipDialKeyboardFragment.this.updateUI();
            SipDialKeyboardFragment.this.dismissRegistCallerIDDialog();
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            SipDialKeyboardFragment.this.toggleLBRViewState();
        }

        public void OnPBXServiceRangeChanged(int i) {
            super.OnPBXServiceRangeChanged(i);
            SipDialKeyboardFragment.this.toggleLBRViewState();
        }
    };
    private ImageView mImgDelete;
    /* access modifiers changed from: private */
    public ImageView mImgSearch;
    private ImageView mIvOutOfRange;
    private SimpleISIPLineMgrEventSinkListener mLineEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnCurrentSelectedLineChanged(String str) {
            super.OnCurrentSelectedLineChanged(str);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }

        public void OnNewSharedUserAdded(String str) {
            super.OnNewSharedUserAdded(str);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }

        public void OnSharedUserDeleted(String str) {
            super.OnSharedUserDeleted(str);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }

        public void OnSharedUserUpdated(String str) {
            super.OnSharedUserUpdated(str);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }

        public void OnNewSharedLineAdded(String str) {
            super.OnNewSharedLineAdded(str);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }

        public void OnSharedLineUpdated(String str, boolean z, int i) {
            super.OnSharedLineUpdated(str, z, i);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }

        public void OnSharedLineDeleted(String str) {
            super.OnSharedLineDeleted(str);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }

        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            SipDialKeyboardFragment.this.HandleSIPRegistrarResponse();
        }

        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            SipDialKeyboardFragment.this.handleOnLineChanged();
        }
    };
    @NonNull
    SimpleNetworkStatusListener mNetworkStatusListener = new SimpleNetworkStatusListener() {
        public void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2) {
            super.networkStatusChanged(z, i, str, z2, i2, str2);
            SipDialKeyboardFragment.this.updateUI();
        }
    };
    /* access modifiers changed from: private */
    public ZMPopupWindow mOutOfRangeTipPop;
    private View mPanelCallBtns;
    private DialKeyboardView mPanelKeybord;
    /* access modifiers changed from: private */
    public View mPanelRegisterSipNo;
    private Runnable mQueryUserPbxInfoRunnable = new Runnable() {
        public void run() {
            CmmSIPCallManager.getInstance().queryUserPbxInfo();
        }
    };
    @Nullable
    private String mRelatedCallId;
    @Nullable
    private ZMAlertDialog mTransferOptionDialog;
    /* access modifiers changed from: private */
    public EditText mTxtDialNum;
    /* access modifiers changed from: private */
    public TextView mTxtDialUserName;
    private TextView mTxtRegisterSipNo;
    private TextView mTxtSipUnavailable;
    private TextView mTxtTitle;

    public static void showAsActivity(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, SipDialKeyboardFragment.class.getName(), bundle, i, true, 1);
    }

    public static void showAsActivity(Fragment fragment, int i, Bundle bundle) {
        SimpleActivity.show(fragment, SipDialKeyboardFragment.class.getName(), bundle, i, true, 1);
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        showAsActivity(zMActivity, i, 0, null);
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i, int i2) {
        showAsActivity(zMActivity, i, i2, null);
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i, int i2, String str) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_DIAL_ACTION, i2);
        bundle.putString(ARGS_RELATED_CALL_ID, str);
        SimpleActivity.show(zMActivity, SipDialKeyboardFragment.class.getName(), bundle, i, true, 1);
    }

    public static void showAsActivity(ZMActivity zMActivity, String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_PHONE_NUMBER, str);
        SimpleActivity.show(zMActivity, SipDialKeyboardFragment.class.getName(), bundle, 0, true, 1);
    }

    /* access modifiers changed from: private */
    public boolean isRelatedCallTerminate(String str) {
        return StringUtil.isEmptyOrNull(this.mRelatedCallId) || this.mRelatedCallId.equals(str);
    }

    private boolean isTransfer() {
        return this.mDialAction == 2;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        int i = 0;
        if (bundle != null) {
            this.mDialAction = bundle.getInt("mDialAction", 0);
        } else {
            if (getArguments() != null) {
                i = getArguments().getInt(ARGS_DIAL_ACTION, 0);
            }
            this.mDialAction = i;
        }
        UIUtil.renderStatueBar(getActivity(), true, C4409R.color.zm_ui_kit_color_white_ffffff);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        int i = 0;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_sip_dialpad, viewGroup, false);
        this.mPanelKeybord = (DialKeyboardView) inflate.findViewById(C4558R.C4560id.panelKeybord);
        this.mTxtDialNum = (EditText) inflate.findViewById(C4558R.C4560id.txtDialNum);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnDial = (ImageView) inflate.findViewById(C4558R.C4560id.btnDial);
        this.mTxtDialUserName = (TextView) inflate.findViewById(C4558R.C4560id.txtDialUserName);
        this.mTxtRegisterSipNo = (TextView) inflate.findViewById(C4558R.C4560id.txtRegisterSipNo);
        this.mTxtSipUnavailable = (TextView) inflate.findViewById(C4558R.C4560id.txtSipUnavailable);
        this.mImgDelete = (ImageView) inflate.findViewById(C4558R.C4560id.imgDelete);
        this.mPanelRegisterSipNo = inflate.findViewById(C4558R.C4560id.panelRegisterSipNo);
        this.mImgSearch = (ImageView) inflate.findViewById(C4558R.C4560id.imgSearch);
        this.mBtnCoseInSip = (TextView) inflate.findViewById(C4558R.C4560id.btnCloseInSip);
        this.mPanelCallBtns = inflate.findViewById(C4558R.C4560id.panelCallBtns);
        this.mIvOutOfRange = (ImageView) inflate.findViewById(C4558R.C4560id.iv_out_of_range);
        this.mPanelKeybord.setOnKeyDialListener(this);
        this.mImgDelete.setOnClickListener(this);
        this.mImgDelete.setOnLongClickListener(this);
        this.mImgSearch.setOnClickListener(this);
        this.mBtnDial.setOnClickListener(this);
        this.mBtnCoseInSip.setOnClickListener(this);
        this.mTxtSipUnavailable.setOnClickListener(this);
        this.mIvOutOfRange.setOnClickListener(this);
        if (OsUtil.isAtLeastL()) {
            this.mTxtDialNum.setShowSoftInputOnFocus(false);
        } else {
            this.mTxtDialNum.setFocusableInTouchMode(false);
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = true;
        if (!(zoomMessenger == null || zoomMessenger.msgCopyGetOption() == 1)) {
            z = false;
        }
        if (!z) {
            this.mTxtDialNum.setCursorVisible(false);
        }
        this.mTxtDialNum.addTextChangedListener(new TextWatcher() {
            boolean isPreEmpty = true;

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                this.isPreEmpty = TextUtils.isEmpty(charSequence);
            }

            public void afterTextChanged(@NonNull Editable editable) {
                String obj = editable.toString();
                if (this.isPreEmpty) {
                    String dialNumberFilter = ZMPhoneUtils.dialNumberFilter(obj);
                    if (!StringUtil.isSameString(obj, dialNumberFilter == null ? "" : dialNumberFilter)) {
                        SipDialKeyboardFragment.this.updateDialNum(dialNumberFilter);
                        SipDialKeyboardFragment.this.mTxtDialNum.setSelection(SipDialKeyboardFragment.this.mTxtDialNum.getText().length());
                        EditText access$100 = SipDialKeyboardFragment.this.mTxtDialNum;
                        StringBuilder sb = new StringBuilder();
                        sb.append("\"");
                        sb.append(obj);
                        sb.append("\"");
                        access$100.setTag(sb.toString());
                        SipDialKeyboardFragment.this.updateCallBtns();
                    }
                }
                SipDialKeyboardFragment.this.mTxtDialNum.setTag(null);
                SipDialKeyboardFragment.this.updateDialNum();
                SipDialKeyboardFragment.this.updateCallBtns();
            }
        });
        this.mTxtDialNum.setAccessibilityDelegate(new AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                String str;
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                String obj = SipDialKeyboardFragment.this.mTxtDialNum.getText().toString();
                if (obj.length() <= 0) {
                    str = SipDialKeyboardFragment.this.getString(C4558R.string.zm_accessibility_sip_enter_number_149527);
                } else {
                    str = StringUtil.digitJoin(obj.split(""), PreferencesConstants.COOKIE_DELIMITER);
                    if (str.contains("*")) {
                        str = str.replaceAll("\\*", SipDialKeyboardFragment.this.getString(C4558R.string.zm_sip_accessbility_keypad_star_61381));
                    }
                    if (str.contains("#")) {
                        str = str.replaceAll("\\#", SipDialKeyboardFragment.this.getString(C4558R.string.zm_sip_accessbility_keypad_pound_61381));
                    }
                }
                accessibilityNodeInfo.setText(str);
                accessibilityNodeInfo.setContentDescription(str);
            }
        });
        String str = "";
        if (bundle != null) {
            str = bundle.getString("mDialNum");
            this.mDialAction = bundle.getInt("mDialAction", 0);
        } else {
            if (getArguments() != null) {
                i = getArguments().getInt(ARGS_DIAL_ACTION, 0);
            }
            this.mDialAction = i;
        }
        updateDialNum(str);
        EditText editText = this.mTxtDialNum;
        editText.setSelection(editText.getText().length());
        String str2 = null;
        if (getArguments() != null) {
            str2 = getArguments().getString(ARGS_RELATED_CALL_ID, null);
        }
        this.mRelatedCallId = str2;
        CmmSIPCallManager.getInstance().addListener(this.mISIPCallEventListener);
        CmmSIPCallManager.getInstance().addNetworkListener(this.mNetworkStatusListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineEventSinkListener);
        String string = getArguments() != null ? getArguments().getString(ARGS_PHONE_NUMBER) : "";
        if (!TextUtils.isEmpty(string)) {
            this.mTxtDialNum.setText(string);
        }
        return inflate;
    }

    @Nullable
    private String GetPreviousNumber() {
        return CmmSIPCallManager.getInstance().getPreviousCalloutNumber();
    }

    private void updateTitle() {
        int i = 0;
        switch (this.mDialAction) {
            case 1:
                this.mTxtTitle.setText(C4558R.string.zm_sip_title_add_call_26673);
                this.mTxtTitle.setVisibility(0);
                this.mBtnCoseInSip.setVisibility(0);
                this.mBtnCoseInSip.setText(C4558R.string.zm_btn_back_to_call_61381);
                return;
            case 2:
                this.mTxtTitle.setText(C4558R.string.zm_sip_title_transfer_to_61381);
                this.mTxtTitle.setVisibility(0);
                this.mBtnCoseInSip.setVisibility(0);
                this.mBtnCoseInSip.setText(C4558R.string.zm_btn_back_to_call_61381);
                return;
            default:
                this.mTxtTitle.setVisibility(8);
                TextView textView = this.mBtnCoseInSip;
                if (getActivity() instanceof IMActivity) {
                    i = 8;
                }
                textView.setVisibility(i);
                this.mBtnCoseInSip.setText(C4558R.string.zm_btn_cancel);
                return;
        }
    }

    /* access modifiers changed from: private */
    public void updateCallBtns() {
        if (isAdded()) {
            if (this.mDialAction != 2) {
                this.mBtnDial.setImageResource(C4558R.C4559drawable.zm_sip_start_call);
                this.mBtnDial.setContentDescription(getString(C4558R.string.zm_accessibility_sip_call_dial));
            } else {
                this.mBtnDial.setImageResource(C4558R.C4559drawable.zm_sip_transfer);
                this.mBtnDial.setContentDescription(getString(C4558R.string.zm_sip_transfer_31432));
            }
            this.mBtnDial.setEnabled(true);
        }
    }

    public void HandleSIPRegistrarResponse() {
        updateUI();
    }

    public void handleOnLineChanged() {
        updateRegisterNo();
        dismissRegistCallerIDDialog();
    }

    public void HandleSIPOnSIPServiceStatusChangedNotification(int i) {
        if (i != 0 || !isTransfer()) {
            updateUI();
        } else {
            dismiss();
        }
    }

    public void onResume() {
        super.onResume();
        updateUI();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setRequestedOrientation(7);
        }
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        EditText editText = this.mTxtDialNum;
        if (editText != null) {
            editText.setVisibility(z ? 0 : 8);
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onParentUserVisibleHint(boolean z) {
        EditText editText = this.mTxtDialNum;
        if (editText != null) {
            editText.setVisibility(z ? 0 : 8);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mDialNum", this.mTxtDialNum.getText().toString());
        bundle.putInt("mDialAction", this.mDialAction);
    }

    public void onDestroyView() {
        dismissRegistCallerIDDialog();
        ZMAlertDialog zMAlertDialog = this.mTransferOptionDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mTransferOptionDialog.dismiss();
        }
        CmmSIPCallManager.getInstance().removeNetworkListener(this.mNetworkStatusListener);
        CmmSIPCallManager.getInstance().removeListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineEventSinkListener);
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        updateDialNum();
        updateCallBtns();
        updateKeyboard();
        updateRegisterNo();
        updateTitle();
    }

    private void updateKeyboard() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.getSipIntergration() == null) {
            this.mTxtSipUnavailable.setVisibility(0);
            this.mTxtSipUnavailable.setText(C4558R.string.zm_sip_error_user_configuration_99728);
            this.mTxtSipUnavailable.setTag("reload_user_config");
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                AccessibilityUtil.sendAccessibilityFocusEvent(this.mTxtSipUnavailable);
                TextView textView = this.mTxtSipUnavailable;
                AccessibilityUtil.announceForAccessibilityCompat((View) textView, (CharSequence) textView.getText().toString());
            }
        } else if (instance.isSipError()) {
            String registerErrorString = CmmSIPCallManager.getInstance().getRegisterErrorString();
            if (registerErrorString == null) {
                this.mTxtSipUnavailable.setVisibility(8);
            } else {
                this.mTxtSipUnavailable.setVisibility(0);
                this.mTxtSipUnavailable.setText(registerErrorString);
                this.mTxtSipUnavailable.setTag(null);
                if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                    AccessibilityUtil.announceForAccessibilityCompat((View) this.mTxtSipUnavailable, (CharSequence) registerErrorString);
                }
            }
        } else {
            this.mTxtSipUnavailable.setVisibility(8);
        }
        this.mTxtDialNum.setVisibility(0);
        this.mPanelKeybord.setEnabled(true);
        this.mPanelKeybord.setAlpha(1.0f);
        toggleLBRViewState();
        updateDialNum();
    }

    /* access modifiers changed from: private */
    public void updateRegisterNo() {
        String str;
        if (isAdded()) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            boolean isCloudPBXEnabled = instance.isCloudPBXEnabled();
            if (instance.isBlockedCallerIDSelected()) {
                str = getString(C4558R.string.zm_sip_caller_id_hidden_64644);
            } else {
                str = ZMPhoneUtils.formatPhoneNumber(instance.getCallerNumberForCallpeer());
            }
            if (TextUtils.isEmpty(str)) {
                this.mPanelRegisterSipNo.setVisibility(8);
            } else {
                this.mPanelRegisterSipNo.setVisibility(0);
                if (isCloudPBXEnabled) {
                    this.mTxtRegisterSipNo.setText(getString(C4558R.string.zm_sip_my_caller_id_61381, str));
                    if (instance.isCallerIdLocked()) {
                        this.mTxtRegisterSipNo.setCompoundDrawables(null, null, null, null);
                        this.mPanelRegisterSipNo.setOnClickListener(null);
                    } else {
                        this.mTxtRegisterSipNo.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(C4558R.C4559drawable.zm_sip_ic_outbound_dropdown), null);
                        this.mPanelRegisterSipNo.setOnClickListener(this);
                    }
                } else {
                    this.mTxtRegisterSipNo.setCompoundDrawables(null, null, null, null);
                    this.mTxtRegisterSipNo.setText(getString(C4558R.string.zm_sip_register_no_61381, str));
                    this.mPanelRegisterSipNo.setOnClickListener(null);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateDialNum() {
        updateDialNum(this.mTxtDialNum.getText().toString());
    }

    /* access modifiers changed from: private */
    public void updateDialNum(@Nullable String str) {
        boolean isEmpty = TextUtils.isEmpty(str);
        if (str == null) {
            this.mTxtDialNum.setText("");
        } else if (!this.mTxtDialNum.getText().toString().equals(str)) {
            this.mTxtDialNum.setText(str);
        }
        this.mImgSearch.setEnabled(true);
        this.mImgDelete.setVisibility(isEmpty ? 4 : 0);
        searchDialUserName(str);
    }

    private void searchDialUserName(String str) {
        this.mHandler.removeCallbacks(this.mDTMFSearchRunnable);
        if (ZMPhoneNumberHelper.isInvalidPhoneNumberLength(str)) {
            this.mTxtDialUserName.setText("");
            this.mTxtDialUserName.setTag(null);
            return;
        }
        this.mHandler.postDelayed(this.mDTMFSearchRunnable, 450);
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.imgDelete) {
                onClickImgDelete();
            } else if (id == C4558R.C4560id.btnDial) {
                onClickBtnDial();
            } else if (id == C4558R.C4560id.panelRegisterSipNo) {
                onClickPanelRegisterNo();
            } else if (id == C4558R.C4560id.imgSearch) {
                onClickSearch();
            } else if (id == C4558R.C4560id.btnCloseInSip) {
                dismiss();
            } else if (id == C4558R.C4560id.txtSipUnavailable) {
                onClickTxtSipUnavailable();
            } else if (id == C4558R.C4560id.iv_out_of_range) {
                onClickTvOutOfRange();
            }
        }
    }

    private void onClickSearch() {
        if (CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            PBXDirectorySearchFragment.showAsFragment(this, 109);
        } else {
            PhoneSearchFragment.showAsFragment(this, null, PhoneSearchFragment.ACTION_PICK_SIP);
        }
    }

    private void onClickPanelRegisterNo() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            BigRoundListDialog bigRoundListDialog = this.mCallerIDDialog;
            if (bigRoundListDialog == null || !bigRoundListDialog.isShowing()) {
                List callerIdList = CmmSIPCallManager.getInstance().getCallerIdList();
                this.mCallerIDDialog = new BigRoundListDialog(activity);
                this.mCallerIDDialog.setTitle(C4558R.string.zm_sip_title_my_caller_id_61381);
                CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
                if (cloudPBXInfo != null) {
                    String extension = cloudPBXInfo.getExtension();
                    if (!StringUtil.isEmptyOrNull(extension)) {
                        this.mCallerIDDialog.setSubtitle(getString(C4558R.string.zm_sip_title_my_extension_61381, extension));
                    }
                }
                String emptyIfNull = StringUtil.emptyIfNull(CmmSIPCallManager.getInstance().getCallerNumberForCallpeer());
                PBXCallerIDListAdapter pBXCallerIDListAdapter = new PBXCallerIDListAdapter(getActivity());
                ArrayList arrayList = new ArrayList();
                Context context = getContext();
                if (callerIdList != null) {
                    int size = callerIdList.size();
                    for (int i = 0; i < size; i++) {
                        PBXNumber pBXNumber = (PBXNumber) callerIdList.get(i);
                        PBXNumberCallerIdListItem pBXNumberCallerIdListItem = new PBXNumberCallerIdListItem(pBXNumber);
                        pBXNumberCallerIdListItem.init(context);
                        pBXNumberCallerIdListItem.setSelected(emptyIfNull.equals(pBXNumber.getNumber()));
                        arrayList.add(pBXNumberCallerIdListItem);
                    }
                }
                List allLineInfoforCallerID = CmmSIPLineManager.getInstance().getAllLineInfoforCallerID();
                if (allLineInfoforCallerID != null) {
                    int size2 = allLineInfoforCallerID.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        CmmSipLineInfoForCallerID cmmSipLineInfoForCallerID = (CmmSipLineInfoForCallerID) allLineInfoforCallerID.get(i2);
                        LineCallerIdListItem lineCallerIdListItem = new LineCallerIdListItem(cmmSipLineInfoForCallerID);
                        lineCallerIdListItem.init(context);
                        lineCallerIdListItem.setSelected(ZMPhoneUtils.isNumberMatched(emptyIfNull, cmmSipLineInfoForCallerID.getLineOwnerNumber()));
                        arrayList.add(lineCallerIdListItem);
                    }
                }
                if (CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
                    CallerIdListItem callerIdListItem = new CallerIdListItem();
                    callerIdListItem.setLabel(getString(C4558R.string.zm_sip_hide_my_caller_id_64644));
                    callerIdListItem.setSublabel("");
                    callerIdListItem.setSelected(CmmSIPCallManager.getInstance().isBlockedCallerIDSelected());
                    arrayList.add(callerIdListItem);
                }
                pBXCallerIDListAdapter.setList(arrayList);
                this.mCallerIDDialog.setAdapter(pBXCallerIDListAdapter);
                this.mCallerIDDialog.setDialogCallback(new DialogCallback() {
                    public void onCancel() {
                        SipDialKeyboardFragment sipDialKeyboardFragment = SipDialKeyboardFragment.this;
                        sipDialKeyboardFragment.viewAccessibilityControl(sipDialKeyboardFragment.mPanelRegisterSipNo);
                    }

                    public void onClose() {
                        SipDialKeyboardFragment sipDialKeyboardFragment = SipDialKeyboardFragment.this;
                        sipDialKeyboardFragment.viewAccessibilityControl(sipDialKeyboardFragment.mPanelRegisterSipNo);
                    }

                    public void onItemSelected(int i) {
                        boolean z;
                        IZMListItem item = SipDialKeyboardFragment.this.mCallerIDDialog.getAdapter().getItem(i);
                        if (item != null) {
                            if (item instanceof PBXNumberCallerIdListItem) {
                                String number = ((PBXNumberCallerIdListItem) item).getNumber();
                                if (!TextUtils.isEmpty(number)) {
                                    z = CmmSIPCallManager.getInstance().setCallFromNumber(number);
                                    CmmSIPCallManager.getInstance().selectBlockedCallerID(false);
                                    CmmSIPLineManager.getInstance().switchMimeExtensionLine();
                                } else {
                                    return;
                                }
                            } else if (item instanceof LineCallerIdListItem) {
                                String id = ((LineCallerIdListItem) item).getId();
                                if (!TextUtils.isEmpty(id)) {
                                    z = CmmSIPLineManager.getInstance().switchLine(id);
                                    CmmSIPCallManager.getInstance().selectBlockedCallerID(false);
                                } else {
                                    return;
                                }
                            } else if (item instanceof CallerIdListItem) {
                                boolean selectBlockedCallerID = CmmSIPCallManager.getInstance().selectBlockedCallerID(true);
                                CmmSIPLineManager.getInstance().switchMimeExtensionLine();
                                z = selectBlockedCallerID;
                            } else {
                                z = false;
                            }
                            if (z) {
                                SipDialKeyboardFragment.this.updateRegisterNo();
                            } else {
                                Toast.makeText(SipDialKeyboardFragment.this.getContext(), C4558R.string.zm_dialog_pick_outbound_error_31444, 1).show();
                            }
                            SipDialKeyboardFragment sipDialKeyboardFragment = SipDialKeyboardFragment.this;
                            sipDialKeyboardFragment.viewAccessibilityControl(sipDialKeyboardFragment.mPanelRegisterSipNo);
                        }
                    }
                });
                if (getActivity() != null && !getActivity().isFinishing()) {
                    this.mCallerIDDialog.show();
                }
                return;
            }
            this.mCallerIDDialog.dismiss();
            this.mCallerIDDialog = null;
        }
    }

    private void onClickTxtSipUnavailable() {
        if ("reload_user_config".equals(this.mTxtSipUnavailable.getTag())) {
            this.mTxtSipUnavailable.setVisibility(8);
            this.mHandler.removeCallbacks(this.mQueryUserPbxInfoRunnable);
            this.mHandler.postDelayed(this.mQueryUserPbxInfoRunnable, 500);
        }
    }

    private void onClickTvOutOfRange() {
        showOutOfRangeTipPop();
    }

    /* access modifiers changed from: private */
    public void dismissRegistCallerIDDialog() {
        BigRoundListDialog bigRoundListDialog = this.mCallerIDDialog;
        if (bigRoundListDialog != null) {
            if (bigRoundListDialog.isShowing()) {
                this.mCallerIDDialog.dismiss();
            }
            this.mCallerIDDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void viewAccessibilityControl(@NonNull final View view) {
        if (getContext() != null) {
            AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                view.postDelayed(new Runnable() {
                    public void run() {
                        view.sendAccessibilityEvent(8);
                    }
                }, 1000);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnDial() {
        String obj = this.mTxtDialNum.getText().toString();
        if (StringUtil.isEmptyOrNull(obj)) {
            updateDialNum(GetPreviousNumber());
            EditText editText = this.mTxtDialNum;
            editText.setSelection(editText.getText().length());
            return;
        }
        String dialNumberFilter = ZMPhoneUtils.dialNumberFilter(obj);
        if (dialNumberFilter != null && !toE911(dialNumberFilter) && CmmSIPCallManager.getInstance().checkNetwork(getContext()) && CmmSIPCallManager.getInstance().checkIsPbxActive(getContext())) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (this.mDialAction != 2) {
                    onStartCall();
                } else {
                    onShowTransferOptionDialog();
                }
                return;
            }
            if (getParentFragment() == null) {
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 12);
            } else if (getParentFragment() instanceof ZMDialogFragment) {
                ((ZMDialogFragment) getParentFragment()).zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 12);
            }
        }
    }

    private void showAudioSwitchedDialog(String str, SimpleOnButtonClickListener simpleOnButtonClickListener) {
        if (getActivity() != null) {
            ConfirmAlertDialog.show(getActivity(), getString(C4558R.string.zm_sip_callpeer_inmeeting_title_108086), str, simpleOnButtonClickListener);
        }
    }

    private void onShowTransferOptionDialog() {
        ZMAlertDialog zMAlertDialog = this.mTransferOptionDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mTransferOptionDialog.dismiss();
            this.mTransferOptionDialog = null;
        }
        if (this.mTransferOptionDialog == null) {
            final SipTransferOptionAdapter sipTransferOptionAdapter = new SipTransferOptionAdapter(getActivity());
            sipTransferOptionAdapter.addItem(new SipTransferMenuItem(1, getString(C4558R.string.zm_sip_btn_warm_transfer_61381), getString(C4558R.string.zm_sip_warm_transfer_des_95826)));
            sipTransferOptionAdapter.addItem(new SipTransferMenuItem(0, getString(C4558R.string.zm_sip_btn_blind_transfer_61381), getString(C4558R.string.zm_sip_blind_transfer_des_95826)));
            if (CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
                sipTransferOptionAdapter.addItem(new SipTransferMenuItem(2, getString(C4558R.string.zm_sip_btn_voice_transfer_82784), getString(C4558R.string.zm_sip_voice_transfer_des_82784)));
            }
            int dimensionPixelSize = getResources().getDimensionPixelSize(C4558R.dimen.zm_dialog_radius_normal);
            this.mTransferOptionDialog = new Builder(getActivity()).setAdapter(sipTransferOptionAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SipTransferMenuItem sipTransferMenuItem = (SipTransferMenuItem) sipTransferOptionAdapter.getItem(i);
                    if (sipTransferMenuItem != null) {
                        switch (sipTransferMenuItem.getAction()) {
                            case 0:
                                SipDialKeyboardFragment.this.onClickBlindTransfer();
                                return;
                            case 1:
                                SipDialKeyboardFragment.this.onClickWarmTransfer();
                                return;
                            case 2:
                                SipDialKeyboardFragment.this.onClickVoiceMailTransfer();
                                return;
                            default:
                                return;
                        }
                    }
                }
            }).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_RoundRect_NormalCorners).setContentPadding(0, dimensionPixelSize, 0, dimensionPixelSize).create();
        }
        this.mTransferOptionDialog.show();
    }

    private void onStartCall() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        String dialNumberFilter = ZMPhoneUtils.dialNumberFilter(this.mTxtDialNum.getText().toString());
        if (!TextUtils.isEmpty(dialNumberFilter)) {
            String replaceAll = this.mTxtDialNum.getTag() instanceof String ? ((String) this.mTxtDialNum.getTag()).replaceAll("\"", "") : null;
            if (TextUtils.isEmpty(replaceAll)) {
                replaceAll = this.mTxtDialUserName.getText().toString();
            }
            int sipNumberType = ZMPhoneUtils.getSipNumberType(dialNumberFilter);
            if (this.mTxtDialUserName.getText().length() > 0 && (this.mTxtDialUserName.getTag() instanceof NumberMatchedItem)) {
                NumberMatchedItem numberMatchedItem = (NumberMatchedItem) this.mTxtDialUserName.getTag();
                if (numberMatchedItem != null && numberMatchedItem.hasMatched()) {
                    sipNumberType = numberMatchedItem.getNumberType();
                }
            }
            if (instance.callPeer(dialNumberFilter, sipNumberType, replaceAll) == 0) {
                updateDialNum("");
                EditText editText = this.mTxtDialNum;
                editText.setSelection(editText.getText().length());
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickWarmTransfer() {
        if (!CmmSIPCallManager.getInstance().hasMeetings() || !CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
            transfer(2);
        } else {
            showAudioSwitchedDialog(getString(C4558R.string.zm_sip_transfer_inmeeting_msg_108086), new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    SipDialKeyboardFragment.this.transfer(2);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onClickBlindTransfer() {
        transfer(0);
    }

    /* access modifiers changed from: private */
    public void onClickVoiceMailTransfer() {
        transfer(1);
    }

    /* access modifiers changed from: private */
    public void transfer(int i) {
        String dialNumberFilter = ZMPhoneUtils.dialNumberFilter(this.mTxtDialNum.getText().toString());
        if (!StringUtil.isEmptyOrNull(dialNumberFilter)) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            int sipNumberType = ZMPhoneUtils.getSipNumberType(dialNumberFilter);
            if (this.mTxtDialUserName.getText().length() > 0 && (this.mTxtDialUserName.getTag() instanceof NumberMatchedItem)) {
                sipNumberType = ((NumberMatchedItem) this.mTxtDialUserName.getTag()).getNumberType();
            }
            if (instance.transferCall(this.mRelatedCallId, dialNumberFilter, sipNumberType, i)) {
                switch (i) {
                    case 0:
                    case 1:
                        SipTransferResultActivity.show(getActivity(), this.mRelatedCallId);
                        break;
                }
                updateDialNum("");
                EditText editText = this.mTxtDialNum;
                editText.setSelection(editText.getText().length());
            }
        }
    }

    private void onClickImgDelete() {
        String obj = this.mTxtDialNum.getText().toString();
        if (!TextUtils.isEmpty(obj)) {
            int selectionStart = this.mTxtDialNum.getSelectionStart();
            int selectionEnd = this.mTxtDialNum.getSelectionEnd();
            int min = Math.min(selectionStart, selectionEnd);
            int max = Math.max(selectionStart, selectionEnd);
            if (min == max) {
                min--;
            }
            int max2 = Math.max(0, Math.min(min, obj.length()));
            CharSequence subSequence = this.mTxtDialNum.getText().subSequence(max2, max);
            if (OsUtil.isAtLeastJB()) {
                String digitJoin = StringUtil.digitJoin(subSequence.toString().split(""), PreferencesConstants.COOKIE_DELIMITER);
                if (digitJoin.contains("*")) {
                    digitJoin = digitJoin.replaceAll("\\*", getString(C4558R.string.zm_sip_accessbility_keypad_star_61381));
                }
                if (digitJoin.contains("#")) {
                    digitJoin = digitJoin.replaceAll("\\#", getString(C4558R.string.zm_sip_accessbility_keypad_pound_61381));
                }
                sendAccessbilityEvent(16384, getString(C4558R.string.zm_accessbility_sip_dial_delete_61381, digitJoin));
            }
            this.mTxtDialNum.getText().delete(max2, max);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C411213 r2 = new EventAction("SipDialKeyboardFragmentPermissionResult") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof SipDialKeyboardFragment) {
                        ((SipDialKeyboardFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                    }
                }
            };
            eventTaskManager.pushLater("SipDialKeyboardFragmentPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null && i == 12 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
            onClickBtnDial();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onKeyDial(@NonNull String str) {
        if (OsUtil.isAtLeastJB()) {
            String str2 = str.equals("*") ? getString(C4558R.string.zm_sip_accessbility_keypad_star_61381) : str.equals("#") ? getString(C4558R.string.zm_sip_accessbility_keypad_pound_61381) : str;
            sendAccessbilityEvent(16384, str2);
        }
        int selectionStart = this.mTxtDialNum.getSelectionStart();
        if (selectionStart < 0) {
            selectionStart = 0;
        }
        this.mTxtDialNum.getText().insert(selectionStart, str);
        this.mTxtDialNum.setSelection(selectionStart + str.length());
        playTone(str);
    }

    private void sendAccessbilityEvent(int i, String str) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
            AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
            if (accessibilityManager != null) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
                obtain.setContentDescription(str);
                accessibilityManager.sendAccessibilityEvent(obtain);
            }
        }
    }

    private void playTone(@Nullable String str) {
        if (getActivity() != null) {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) getActivity().getSystemService("audio");
            }
            int ringerMode = this.mAudioManager.getRingerMode();
            if (ringerMode != 0) {
                int i = 1;
                if (ringerMode != 1 && !StringUtil.isEmptyOrNull(str)) {
                    char charAt = str.charAt(0);
                    if (charAt == '#') {
                        i = 11;
                    } else if (charAt != '*') {
                        switch (charAt) {
                            case '0':
                                i = 0;
                                break;
                            case '1':
                                break;
                            case '2':
                                i = 2;
                                break;
                            case '3':
                                i = 3;
                                break;
                            case '4':
                                i = 4;
                                break;
                            case '5':
                                i = 5;
                                break;
                            case '6':
                                i = 6;
                                break;
                            case '7':
                                i = 7;
                                break;
                            case '8':
                                i = 8;
                                break;
                            case '9':
                                i = 9;
                                break;
                            default:
                                i = 0;
                                break;
                        }
                    } else {
                        i = 10;
                    }
                    try {
                        if (this.mDtmfGenerator == null) {
                            this.mDtmfGenerator = new ToneGenerator(8, 60);
                        }
                        this.mDtmfGenerator.startTone(i, 150);
                        this.mHandler.removeCallbacks(this.mDTMFReleaseRunnable);
                        this.mHandler.postDelayed(this.mDTMFReleaseRunnable, 450);
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    private boolean toE911(String str) {
        boolean isE911Number = E911Utils.isE911Number(str);
        if (isE911Number) {
            AndroidAppUtil.sendDial(getActivity(), str);
        }
        return isE911Number;
    }

    public boolean onLongClick(@Nullable View view) {
        if (view == null || view.getId() != C4558R.C4560id.imgDelete) {
            return false;
        }
        return onLongClickDelete();
    }

    private boolean onLongClickDelete() {
        if (OsUtil.isAtLeastJB()) {
            sendAccessbilityEvent(16384, getString(C4558R.string.zm_accessbility_sip_dial_delete_all_61381));
        }
        updateDialNum("");
        EditText editText = this.mTxtDialNum;
        editText.setSelection(editText.getText().length());
        return true;
    }

    public void onSipCallEvent(int i, String str) {
        updateUI();
    }

    public void onActivityResult(int i, int i2, @NonNull Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 109) {
            if (i2 == -1) {
                String stringExtra = intent.getStringExtra(PBXDirectorySearchFragment.RESULT_PHONE_NUMBER);
                if (!TextUtils.isEmpty(stringExtra)) {
                    onPickSipResult(stringExtra);
                }
            } else {
                this.mImgSearch.postDelayed(new Runnable() {
                    public void run() {
                        SipDialKeyboardFragment.this.mImgSearch.sendAccessibilityEvent(8);
                    }
                }, 1500);
            }
        } else if (i == 1090) {
            if (i2 == -1) {
                Serializable serializableExtra = intent.getSerializableExtra(PhoneSearchFragment.ARG_IM_ADDR_BOOK_ITEM);
                if (serializableExtra != null) {
                    String sipPhoneNumber = ((IMAddrBookItem) serializableExtra).getSipPhoneNumber();
                    if (sipPhoneNumber != null) {
                        onPickSipResult(sipPhoneNumber);
                    }
                }
            } else {
                this.mImgSearch.postDelayed(new Runnable() {
                    public void run() {
                        SipDialKeyboardFragment.this.mImgSearch.sendAccessibilityEvent(8);
                    }
                }, 1500);
            }
        }
        AccessibilityUtil.sendAccessibilityFocusEvent(this.mImgSearch);
    }

    private void onPickSipResult(final String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SipDialKeyboardFragment.this.updateDialNum(str);
                    SipDialKeyboardFragment.this.mTxtDialNum.setSelection(SipDialKeyboardFragment.this.mTxtDialNum.getText().length());
                    SipDialKeyboardFragment.this.onClickBtnDial();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void toggleLBRViewState() {
        if (CmmSIPCallManager.getInstance().getServiceRangeState() == 1) {
            this.mIvOutOfRange.setVisibility(0);
            return;
        }
        this.mIvOutOfRange.setVisibility(8);
        ZMPopupWindow zMPopupWindow = this.mOutOfRangeTipPop;
        if (zMPopupWindow != null) {
            zMPopupWindow.dismiss();
        }
    }

    private void showOutOfRangeTipPop() {
        if (this.mOutOfRangeTipPop == null) {
            this.mOutOfRangeTipPop = SipPopUtils.getTipsPop(getContext(), getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_pop_width), getString(C4558R.string.zm_sip_out_of_range_tip_101964), 2, null);
        }
        ZMPopupWindow zMPopupWindow = this.mOutOfRangeTipPop;
        if (zMPopupWindow != null) {
            zMPopupWindow.showAsDropDown(this.mIvOutOfRange);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (SipDialKeyboardFragment.this.mOutOfRangeTipPop.isShowing() && AccessibilityUtil.isSpokenFeedbackEnabled(SipDialKeyboardFragment.this.getContext())) {
                        AccessibilityUtil.announceNoInterruptForAccessibilityCompat(SipDialKeyboardFragment.this.mOutOfRangeTipPop.getContentView(), C4558R.string.zm_sip_out_of_range_tip_101964);
                    }
                }
            }, 1500);
        }
    }

    public void fillDialNumber(String str) {
        if (!StringUtil.isEmptyOrNull(ZMPhoneUtils.dialNumberFilter(str))) {
            updateDialNum(str);
            EditText editText = this.mTxtDialNum;
            editText.setSelection(editText.getText().length());
        }
    }
}
