package com.zipow.videobox.view.sip;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.dialog.ConfirmAlertDialog;
import com.zipow.videobox.dialog.ConfirmAlertDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.view.sip.PhonePBXTabFragment.IPhonePBXAccessbility;
import com.zipow.videobox.view.sip.PhonePBXTabFragment.OnFragmentShowListener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXLinesFragment extends ZMDialogFragment implements OnClickListener, IPhonePBXLinesParentFragment, OnFragmentShowListener, IPhonePBXAccessbility {
    public static final int REQUEST_PERMISSION_ACCEPT = 13;
    public static final int REQUEST_PERMISSION_PICKUP = 14;
    private static final String TAG = "PhonePBXLinesFragment";
    private View mBtnKeyboard;
    private boolean mHasShow = false;
    private int mSelectPosition = -1;
    private String mSelectedCallId;
    private String mSelectedLineCallId;
    /* access modifiers changed from: private */
    public PhonePBXSharedLineRecyclerView mSharedLineRecyclerView;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_pbx_shared_lines, viewGroup, false);
        this.mSharedLineRecyclerView = (PhonePBXSharedLineRecyclerView) inflate.findViewById(C4558R.C4560id.sharedLineRecyclerView);
        this.mBtnKeyboard = inflate.findViewById(C4558R.C4560id.ivKeyboard);
        this.mSharedLineRecyclerView.setParentFragment(this);
        this.mBtnKeyboard.setOnClickListener(this);
        if (bundle != null) {
            this.mSelectedLineCallId = bundle.getString("mSelectedLineCallId");
            this.mSelectedCallId = bundle.getString("mSelectedCallId");
        }
        return inflate;
    }

    public void onClick(View view) {
        if (view == this.mBtnKeyboard) {
            this.mSelectPosition = -1;
            onClickKeyboard();
        }
    }

    private void onClickKeyboard() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            ((PhonePBXTabFragment) parentFragment).openKeyboard();
        }
    }

    public boolean isHasShow() {
        Fragment parentFragment = getParentFragment();
        boolean isHasShow = parentFragment instanceof PhonePBXTabFragment ? ((PhonePBXTabFragment) parentFragment).isHasShow() : false;
        if (!this.mHasShow || !isHasShow) {
            return false;
        }
        return true;
    }

    public void onPickSipResult(String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof PhonePBXTabFragment) {
                ((PhonePBXTabFragment) parentFragment).onPickSipResult(str, str2);
            }
        }
    }

    public void onSelectLastAccessibilityId(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mSelectPosition = Integer.parseInt(str);
        }
    }

    public void onAcceptCallResult(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                acceptCall(str);
                return;
            }
            this.mSelectedCallId = str;
            zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 13);
        }
    }

    public void onPickupCallResult(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                pickupCall(str);
                return;
            }
            this.mSelectedLineCallId = str;
            zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 14);
        }
    }

    public void onShow() {
        this.mHasShow = true;
    }

    private void acceptCall(@Nullable String str) {
        if (CmmSIPCallManager.isPhoneCallOffHook()) {
            Context context = getContext();
            if (context != null) {
                CmmSIPCallManager.getInstance().showErrorDialogImmediately(context.getString(C4558R.string.zm_title_error), context.getString(C4558R.string.zm_sip_can_not_accept_on_phone_call_111899), 0);
                return;
            }
            return;
        }
        CmmSIPCallManager.getInstance().acceptCall(str);
    }

    private void pickupCall(@Nullable final String str) {
        if (CmmSIPCallManager.isPhoneCallOffHook()) {
            Context context = getContext();
            if (context != null) {
                CmmSIPCallManager.getInstance().showErrorDialogImmediately(context.getString(C4558R.string.zm_title_error), context.getString(C4558R.string.zm_sip_can_not_pickup_on_phone_call_111899), 0);
            }
        } else if (!CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
            CmmSIPLineManager.getInstance().pickUp(str);
        } else if (getActivity() != null) {
            ConfirmAlertDialog.show(getActivity(), getString(C4558R.string.zm_sip_callpeer_inmeeting_title_108086), getString(C4558R.string.zm_sip_pickup_inmeeting_msg_108086), new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    CmmSIPLineManager.getInstance().pickUp(str);
                }
            });
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C40412 r2 = new EventAction("PhonePBXLineFragmentPermissionResult") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof PhonePBXLinesFragment) {
                        PhonePBXLinesFragment phonePBXLinesFragment = (PhonePBXLinesFragment) iUIElement;
                        if (phonePBXLinesFragment.isAdded()) {
                            phonePBXLinesFragment.handleRequestPermissionResult(i2, strArr2, iArr2);
                        }
                    }
                }
            };
            eventTaskManager.pushLater("PhonePBXLineFragmentPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 13) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    String str = this.mSelectedCallId;
                    if (str != null) {
                        acceptCall(str);
                    }
                    this.mSelectedCallId = null;
                }
            } else if (i == 14 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
                String str2 = this.mSelectedLineCallId;
                if (str2 != null) {
                    pickupCall(str2);
                }
                this.mSelectedLineCallId = null;
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mSelectedCallId", this.mSelectedCallId);
        bundle.putString("mSelectedLineCallId", this.mSelectedLineCallId);
    }

    public void onDestroy() {
        super.onDestroy();
        PhonePBXSharedLineRecyclerView phonePBXSharedLineRecyclerView = this.mSharedLineRecyclerView;
        if (phonePBXSharedLineRecyclerView != null) {
            phonePBXSharedLineRecyclerView.onDestroy();
        }
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            PhonePBXSharedLineRecyclerView phonePBXSharedLineRecyclerView = this.mSharedLineRecyclerView;
            if (phonePBXSharedLineRecyclerView != null) {
                phonePBXSharedLineRecyclerView.onResume();
                return;
            }
            return;
        }
        PhonePBXSharedLineRecyclerView phonePBXSharedLineRecyclerView2 = this.mSharedLineRecyclerView;
        if (phonePBXSharedLineRecyclerView2 != null) {
            phonePBXSharedLineRecyclerView2.onPause();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mSharedLineRecyclerView != null && getUserVisibleHint()) {
            this.mSharedLineRecyclerView.onResume();
        }
    }

    public void onPause() {
        super.onPause();
        PhonePBXSharedLineRecyclerView phonePBXSharedLineRecyclerView = this.mSharedLineRecyclerView;
        if (phonePBXSharedLineRecyclerView != null) {
            phonePBXSharedLineRecyclerView.onPause();
        }
    }

    public void accessibilityControl(long j) {
        if (this.mSelectPosition >= 0) {
            int dataCount = this.mSharedLineRecyclerView.getDataCount();
            int i = this.mSelectPosition;
            if (dataCount > i) {
                final View childAt = this.mSharedLineRecyclerView.getChildAt(i);
                if (childAt != null) {
                    childAt.postDelayed(new Runnable() {
                        public void run() {
                            if (PhonePBXLinesFragment.this.isResumed() && PhonePBXLinesFragment.this.isUserVisible()) {
                                PhonePBXLinesFragment.this.mSharedLineRecyclerView.requestFocus();
                                AccessibilityUtil.sendAccessibilityFocusEvent(childAt);
                            }
                        }
                    }, j);
                }
            }
        }
    }

    public boolean isUserVisible() {
        if (!getUserVisibleHint()) {
            return false;
        }
        return isParentUserVisible();
    }

    public boolean isParentUserVisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            return parentFragment.getUserVisibleHint();
        }
        return false;
    }
}
