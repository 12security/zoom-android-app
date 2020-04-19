package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.AddrBookSetNumberActivity;
import com.zipow.videobox.AddrBookSettingActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.ptapp.SystemInfoHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class IMAddrBookSettingFragment extends ZMFragment implements OnClickListener, IPhoneABListener {
    public static final int STATUS_ADDRBOOK_DISABLED = 0;
    public static final int STATUS_ADDRBOOK_ENABLED = 1;
    public static final int STATUS_ADDRBOOK_NUM_VERIFIED = 2;
    private final String TAG = IMAddrBookSettingFragment.class.getSimpleName();
    private Button mBtnBack;
    private Button mBtnDisable;
    private Button mBtnDone;
    private Button mBtnEnable;
    @Nullable
    private View mContentView;
    @Nullable
    private String mCountryCode = null;
    private ImageView mImgIcon;
    private View mPanelOptions;
    private View mPanelPhoneNumber;
    private View mPanelTitleBar;
    @Nullable
    private String mPhoneNumber = null;
    private boolean mShowTitlebar = true;
    private int mStatus = -1;
    private TextView mTxtMessage;
    private TextView mTxtPhoneNumber;

    public static class DisableAddrBookConfirmDialog extends ZMDialogFragment {
        public DisableAddrBookConfirmDialog() {
            setCancelable(true);
        }

        public static void show(FragmentManager fragmentManager) {
            Bundle bundle = new Bundle();
            DisableAddrBookConfirmDialog disableAddrBookConfirmDialog = new DisableAddrBookConfirmDialog();
            disableAddrBookConfirmDialog.setArguments(bundle);
            disableAddrBookConfirmDialog.show(fragmentManager, DisableAddrBookConfirmDialog.class.getName());
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_msg_warning_disable_address_book_matching_title).setMessage(C4558R.string.zm_msg_warning_disable_address_book_matching_content).setPositiveButton(C4558R.string.zm_btn_yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    DisableAddrBookConfirmDialog.this.onClickOK();
                }
            }).setNegativeButton(C4558R.string.zm_btn_no, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }

        /* access modifiers changed from: private */
        public void onClickOK() {
            IMAddrBookSettingFragment iMAddrBookSettingFragment = (IMAddrBookSettingFragment) getParentFragment();
            if (iMAddrBookSettingFragment != null) {
                iMAddrBookSettingFragment.disableAddressBook();
            }
        }
    }

    public static void showInActivity(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            IMAddrBookSettingFragment newInstance = newInstance(true);
            newInstance.setArguments(new Bundle());
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, newInstance, IMAddrBookSettingFragment.class.getName()).commit();
        }
    }

    @NonNull
    public static IMAddrBookSettingFragment newInstance(boolean z) {
        return newInstance(z, -1);
    }

    @NonNull
    public static IMAddrBookSettingFragment newInstance(boolean z, int i) {
        IMAddrBookSettingFragment iMAddrBookSettingFragment = new IMAddrBookSettingFragment();
        iMAddrBookSettingFragment.mShowTitlebar = z;
        if (i >= 0) {
            iMAddrBookSettingFragment.mStatus = i;
        }
        return iMAddrBookSettingFragment;
    }

    @Nullable
    public static IMAddrBookSettingFragment findFragment(@Nullable ZMActivity zMActivity) {
        if (zMActivity == null) {
            return null;
        }
        return (IMAddrBookSettingFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(IMAddrBookSettingFragment.class.getName());
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        SparseArray sparseArray;
        super.onActivityCreated(bundle);
        PTUI.getInstance().addPhoneABListener(this);
        if (bundle != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(IMAddrBookSettingFragment.class.getName());
            sb.append(".State");
            sparseArray = bundle.getSparseParcelableArray(sb.toString());
        } else {
            sparseArray = null;
        }
        this.mContentView = getView();
        View view = this.mContentView;
        if (!(view == null || sparseArray == null)) {
            view.restoreHierarchyState(sparseArray);
        }
        if (this.mContentView == null) {
            this.mContentView = onCreateView(getLayoutInflater(bundle), null, bundle);
            View view2 = this.mContentView;
            if (view2 != null && sparseArray != null) {
                view2.restoreHierarchyState(sparseArray);
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        SparseArray sparseArray = new SparseArray();
        View view = getView();
        if (view != null) {
            view.saveHierarchyState(sparseArray);
        } else {
            View view2 = this.mContentView;
            if (view2 != null) {
                view2.saveHierarchyState(sparseArray);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(IMAddrBookSettingFragment.class.getName());
        sb.append(".State");
        bundle.putSparseParcelableArray(sb.toString(), sparseArray);
        bundle.putInt("addrbookStatus", this.mStatus);
        bundle.putString("mCountryCode", this.mCountryCode);
        bundle.putString("mPhoneNumber", this.mPhoneNumber);
        bundle.putBoolean("mShowTitlebar", this.mShowTitlebar);
        super.onSaveInstanceState(bundle);
    }

    public void onDestroy() {
        FragmentActivity activity = getActivity();
        if ((activity != null && activity.isFinishing()) || isRemoving()) {
            PTUI.getInstance().removePhoneABListener(this);
        }
        super.onDestroy();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_addrbook_setting, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnEnable = (Button) inflate.findViewById(C4558R.C4560id.btnEnable);
        this.mBtnDone = (Button) inflate.findViewById(C4558R.C4560id.btnDone);
        this.mBtnDisable = (Button) inflate.findViewById(C4558R.C4560id.btnDisable);
        this.mTxtMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mImgIcon = (ImageView) inflate.findViewById(C4558R.C4560id.imgIcon);
        this.mTxtPhoneNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtPhoneNumber);
        this.mPanelOptions = inflate.findViewById(C4558R.C4560id.panelOptions);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mPanelPhoneNumber = inflate.findViewById(C4558R.C4560id.panelPhoneNumber);
        this.mBtnEnable.setOnClickListener(this);
        this.mBtnDone.setOnClickListener(this);
        this.mBtnDisable.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        if (this.mStatus < 0) {
            this.mStatus = isPhoneNumberRegistered() ? 1 : 0;
        }
        if (bundle != null) {
            this.mStatus = bundle.getInt("addrbookStatus", this.mStatus);
            this.mCountryCode = bundle.getString("mCountryCode");
            this.mPhoneNumber = bundle.getString("mPhoneNumber");
            this.mShowTitlebar = bundle.getBoolean("mShowTitlebar", true);
        }
        updateUI();
        return inflate;
    }

    private String getRegisteredPhoneNumber() {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            return aBContactsHelper.getVerifiedPhoneNumber();
        }
        return null;
    }

    private boolean isPhoneNumberRegistered() {
        return !StringUtil.isEmptyOrNull(getRegisteredPhoneNumber());
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnEnable) {
            onClickBtnEnable();
        } else if (id == C4558R.C4560id.btnDone) {
            onClickBtnDone();
        } else if (id == C4558R.C4560id.btnDisable) {
            onClickBtnDisable();
        }
    }

    private void onClickBtnBack() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((IMAddrBookSettingFragment) iUIElement).handleRequestPermissionResult(i, strArr, iArr);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            ABContactsCache.getInstance().registerContentObserver();
            enableAddrDone();
        }
    }

    private void onClickBtnDone() {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            enableAddrDone();
            return;
        }
        zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
        AppUtil.saveRequestContactPermissionTime();
    }

    private void enableAddrDone() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ABContactsHelper.setAddrBookEnabledDone(true);
            if (activity instanceof IMActivity) {
                ((IMActivity) activity).onAddressBookEnabled(true);
            } else {
                activity.setResult(-1);
                activity.finish();
            }
        }
    }

    private void onClickBtnDisable() {
        DisableAddrBookConfirmDialog.show(getChildFragmentManager());
    }

    private void onClickBtnEnable() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            AddrBookSetNumberActivity.show(zMActivity, 100);
        }
    }

    public void onSetPhoneNumberDone(String str, String str2) {
        this.mStatus = 2;
        this.mCountryCode = str;
        this.mPhoneNumber = str2;
        updateUI();
    }

    private void onAddressBookDisabled() {
        FragmentActivity activity = getActivity();
        if (activity instanceof AddrBookSettingActivity) {
            activity.setResult(-1);
            activity.finish();
            return;
        }
        this.mStatus = 0;
        updateUI();
    }

    private void updateUI() {
        this.mPanelTitleBar.setVisibility(this.mShowTitlebar ? 0 : 8);
        switch (this.mStatus) {
            case 0:
                this.mBtnEnable.setVisibility(0);
                this.mBtnDone.setVisibility(8);
                this.mBtnDisable.setVisibility(8);
                this.mTxtMessage.setText(C4558R.string.zm_msg_enable_addrbook);
                this.mImgIcon.setImageResource(C4558R.C4559drawable.zm_addrbook_no_match);
                this.mPanelPhoneNumber.setVisibility(8);
                this.mPanelOptions.setVisibility(0);
                break;
            case 1:
                this.mBtnEnable.setVisibility(8);
                this.mBtnDone.setVisibility(8);
                this.mBtnDisable.setVisibility(0);
                this.mTxtMessage.setText(C4558R.string.zm_msg_addrbook_enabled);
                this.mImgIcon.setImageResource(C4558R.C4559drawable.zm_addrbook_matched);
                this.mPanelPhoneNumber.setVisibility(0);
                this.mPanelOptions.setVisibility(0);
                String registeredPhoneNumber = getRegisteredPhoneNumber();
                if (registeredPhoneNumber != null) {
                    this.mTxtPhoneNumber.setText(getString(C4558R.string.zm_lbl_addrbook_phone_number, registeredPhoneNumber));
                    break;
                } else {
                    return;
                }
            case 2:
                this.mBtnEnable.setVisibility(8);
                this.mBtnDone.setVisibility(0);
                this.mBtnDisable.setVisibility(8);
                this.mTxtMessage.setText(C4558R.string.zm_msg_addrbook_enabled);
                this.mImgIcon.setImageResource(C4558R.C4559drawable.zm_addrbook_matched);
                this.mPanelPhoneNumber.setVisibility(0);
                this.mPanelOptions.setVisibility(0);
                String str = this.mPhoneNumber;
                if (str != null) {
                    if (!str.startsWith("+") && !StringUtil.isEmptyOrNull(this.mCountryCode)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("+");
                        sb.append(this.mCountryCode);
                        sb.append(str);
                        str = sb.toString();
                    }
                    this.mTxtPhoneNumber.setText(getString(C4558R.string.zm_lbl_addrbook_phone_number, str));
                    break;
                } else {
                    String registeredPhoneNumber2 = getRegisteredPhoneNumber();
                    if (registeredPhoneNumber2 != null) {
                        this.mTxtPhoneNumber.setText(getString(C4558R.string.zm_lbl_addrbook_phone_number, registeredPhoneNumber2));
                        break;
                    } else {
                        return;
                    }
                }
        }
    }

    /* access modifiers changed from: private */
    public void disableAddressBook() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_alert_network_disconnected).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            return;
        }
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            int unregisterPhoneNumber = aBContactsHelper.unregisterPhoneNumber(aBContactsHelper.getVerifiedPhoneNumber(), SystemInfoHelper.getDeviceId());
            if (unregisterPhoneNumber == 0) {
                WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
            } else {
                showErrorDialog(unregisterPhoneNumber);
            }
        }
    }

    public void onPhoneABEvent(int i, long j, Object obj) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final int i2 = i;
        final long j2 = j;
        final Object obj2 = obj;
        C25912 r1 = new EventAction("handlePhoneABEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ((IMAddrBookSettingFragment) iUIElement).handlePhoneABEvent(i2, j2, obj2);
            }
        };
        nonNullEventTaskManagerOrThrowException.push(r1);
    }

    /* access modifiers changed from: private */
    public void handlePhoneABEvent(int i, long j, Object obj) {
        switch (i) {
            case 1:
                onPhoneUnregisterComplete(j);
                return;
            default:
                return;
        }
    }

    private void onPhoneUnregisterComplete(long j) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            if (j == 0) {
                onAddressBookDisabled();
            } else {
                showErrorDialog((int) j);
            }
        }
    }

    private void showErrorDialog(int i) {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_unregister_phone_number_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }
}
