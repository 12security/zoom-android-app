package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.cmmlib.AppUtil;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.FingerprintOption;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.SelectCallInCountryFragment.CallInNumberItem;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodePT;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodelistProto;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimpleMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMUtils;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableEmitter;
import p015io.reactivex.ObservableOnSubscribe;
import p015io.reactivex.android.schedulers.AndroidSchedulers;
import p015io.reactivex.disposables.CompositeDisposable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.schedulers.Schedulers;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.FingerprintUtil;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class MyProfileFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    private static final int AVATAR_COMPRESS_QUALITY = 60;
    private static final int AVATAR_COMPRESS_THRESHOLD = 51200;
    /* access modifiers changed from: private */
    public static final String AVATAR_PATH;
    private static final int PROFILE_PHOTO_HEIGHT = 400;
    private static final int PROFILE_PHOTO_WIDTH = 400;
    private static final int REQUEST_CAPTURE_PHOTO = 101;
    private static final int REQUEST_CHOOSE_PICTURE = 100;
    private static final int REQUEST_CROP_PHOTO = 102;
    private static final int REQUEST_ENABLE_ADDRBOOK = 103;
    private static final int REQUEST_PERMISSION_BY_CHOOSE_PHOTO = 107;
    private static final int REQUEST_PERMISSION_BY_TAKE_PHOTO = 106;
    private static final int REQUEST_SELECT_CALLIN_COUNTRY = 104;
    private static final String TAG = "MyProfileFragment";
    private ImageView mAvatarArrow;
    @Nullable
    private String mAvatarToUploadOnSignedOn = null;
    private AvatarView mAvatarView;
    private Button mBtnBack;
    private View mBtnCallinCountry;
    private View mBtnDepartment;
    private View mBtnJobTitle;
    private View mBtnLocation;
    private View mBtnMeetingRoomName;
    private View mBtnPMI;
    private View mBtnPassword;
    private View mBtnSignout;
    private Uri mCaptureUri;
    private CheckedTextView mChkOpenFingerprint;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private ImageView mDisplayNameArrow;
    @Nullable
    private FingerprintUtil mFingerprintUtil;
    /* access modifiers changed from: private */
    public Uri mImageUri;
    @NonNull
    private INotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnDNDSettingsUpdated() {
            MyProfileFragment.this.updatePresence();
        }

        public void OnDNDNowSettingUpdated() {
            MyProfileFragment.this.updatePresence();
        }

        public void OnSnoozeSettingsUpdated() {
            MyProfileFragment.this.updatePresence();
        }
    };
    @Nullable
    private IMeetingMgrListener mMeetingMgrListener = null;
    private View mOptionDisplayName;
    private View mOptionFingerprint;
    private View mOptionPresenceStatus;
    private View mOptionProfilePhoto;
    private View mPanelCustomStatus;
    @Nullable
    private String mSelectedCountryId = null;
    private TextView mTxtAccountDesp;
    private TextView mTxtCallinCountry;
    private TextView mTxtCustomStatus;
    private TextView mTxtDepartment;
    private TextView mTxtDisplayName;
    private TextView mTxtJobTitle;
    private TextView mTxtLocation;
    private TextView mTxtMeetingRoomName;
    private TextView mTxtPersonalMeetingId;
    private TextView mTxtPhoneNumber;
    private TextView mTxtUserType;
    private IZoomMessengerUIListener mZoomMessengerListener;
    private ImageView presenceStatus;
    private TextView txtPresenceStatus;

    static class ContextMenuItem extends ZMSimpleMenuItem {
        public ContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static class GetPhotoMenuFragment extends ZMDialogFragment {
        private static final int ACTION_CHOOSE_PHOTO = 1;
        private static final int ACTION_TAKE_PHOTO = 0;
        private ZMMenuAdapter<ContextMenuItem> mAdapter;

        public static void show(@NonNull FragmentManager fragmentManager) {
            Bundle bundle = new Bundle();
            GetPhotoMenuFragment getPhotoMenuFragment = new GetPhotoMenuFragment();
            getPhotoMenuFragment.setArguments(bundle);
            getPhotoMenuFragment.show(fragmentManager, GetPhotoMenuFragment.class.getName());
        }

        public GetPhotoMenuFragment() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return createEmptyDialog();
            }
            this.mAdapter = createUpdateAdapter(activity);
            ZMAlertDialog create = new Builder(activity).setTitle(C4558R.string.zm_title_change_profile_photo).setAdapter(this.mAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    GetPhotoMenuFragment.this.onSelectItem(i);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }

        private ZMMenuAdapter<ContextMenuItem> createUpdateAdapter(Context context) {
            ContextMenuItem[] contextMenuItemArr = {new ContextMenuItem(context.getString(C4558R.string.zm_lbl_take_photo), 0), new ContextMenuItem(context.getString(C4558R.string.zm_lbl_choose_photo), 1)};
            ZMMenuAdapter<ContextMenuItem> zMMenuAdapter = this.mAdapter;
            if (zMMenuAdapter == null) {
                this.mAdapter = new ZMMenuAdapter<>(getActivity(), false);
            } else {
                zMMenuAdapter.clear();
            }
            this.mAdapter.addAll((MenuItemType[]) contextMenuItemArr);
            return this.mAdapter;
        }

        /* access modifiers changed from: private */
        public void onSelectItem(int i) {
            ContextMenuItem contextMenuItem = (ContextMenuItem) this.mAdapter.getItem(i);
            if (contextMenuItem != null) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                    if (supportFragmentManager != null) {
                        MyProfileFragment findMyProfileFragment = MyProfileFragment.findMyProfileFragment(supportFragmentManager);
                        if (findMyProfileFragment != null) {
                            switch (contextMenuItem.getAction()) {
                                case 0:
                                    findMyProfileFragment.onSelectCamera();
                                    break;
                                case 1:
                                    findMyProfileFragment.choosePhoto();
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static class SetNameDialog extends ZMDialogFragment implements TextWatcher, OnEditorActionListener {
        private static final String ARG_FIRST_NAME = "firstName";
        private static final String ARG_LAST_NAME = "lastName";
        private Button mBtnOK = null;
        /* access modifiers changed from: private */
        @Nullable
        public ZMAlertDialog mDialog = null;
        private EditText mEdtFirstName = null;
        private EditText mEdtLastName = null;

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public static void showSetNameDialog(FragmentManager fragmentManager, String str, String str2) {
            SetNameDialog setNameDialog = new SetNameDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_FIRST_NAME, str);
            bundle.putString(ARG_LAST_NAME, str2);
            setNameDialog.setArguments(bundle);
            setNameDialog.show(fragmentManager, SetNameDialog.class.getName());
        }

        public SetNameDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            String str = "";
            String str2 = "";
            Bundle arguments = getArguments();
            if (arguments != null) {
                str = arguments.getString(ARG_FIRST_NAME);
                str2 = arguments.getString(ARG_LAST_NAME);
            }
            View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_set_name, null, false);
            this.mEdtFirstName = (EditText) inflate.findViewById(C4558R.C4560id.edtFirstName);
            this.mEdtLastName = (EditText) inflate.findViewById(C4558R.C4560id.edtLastName);
            if (str != null) {
                this.mEdtFirstName.setText(str);
            }
            if (str2 != null) {
                this.mEdtLastName.setText(str2);
            }
            this.mEdtLastName.setImeOptions(2);
            this.mEdtLastName.setOnEditorActionListener(this);
            this.mEdtFirstName.addTextChangedListener(this);
            this.mEdtLastName.addTextChangedListener(this);
            this.mDialog = new Builder(getActivity()).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    if (SetNameDialog.this.mDialog != null) {
                        UIUtil.closeSoftKeyboard(SetNameDialog.this.getActivity(), SetNameDialog.this.mDialog.getCurrentFocus());
                    }
                    dialogInterface.cancel();
                }
            }).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
            return this.mDialog;
        }

        public void onResume() {
            super.onResume();
            registerTouchListener();
            this.mBtnOK = ((ZMAlertDialog) getDialog()).getButton(-1);
            Button button = this.mBtnOK;
            if (button != null) {
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (SetNameDialog.this.checkInput()) {
                            SetNameDialog.this.onClickBtnOK();
                        }
                    }
                });
            }
            updateButtons();
        }

        private void registerTouchListener() {
            ZMAlertDialog zMAlertDialog = this.mDialog;
            if (zMAlertDialog != null && zMAlertDialog.getWindow() != null) {
                this.mDialog.getWindow().getDecorView().setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
                        if (!(motionEvent.getAction() != 0 || SetNameDialog.this.mDialog == null || SetNameDialog.this.mDialog.getCurrentFocus() == null)) {
                            UIUtil.closeSoftKeyboard(SetNameDialog.this.getActivity(), SetNameDialog.this.mDialog.getCurrentFocus());
                        }
                        return false;
                    }
                });
            }
        }

        /* access modifiers changed from: private */
        public void onClickBtnOK() {
            UIUtil.closeSoftKeyboard(getActivity(), this.mBtnOK);
            String trim = this.mEdtFirstName.getText().toString().trim();
            String trim2 = this.mEdtLastName.getText().toString().trim();
            if (trim.length() == 0) {
                this.mEdtFirstName.requestFocus();
            } else if (trim2.length() == 0) {
                this.mEdtLastName.requestFocus();
            } else {
                PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                if (currentUserProfile == null || !StringUtil.isSameString(currentUserProfile.getFirstName(), trim) || !StringUtil.isSameString(currentUserProfile.getLastName(), trim2)) {
                    ZMActivity zMActivity = (ZMActivity) getActivity();
                    if (zMActivity != null) {
                        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                        if (supportFragmentManager != null) {
                            MyProfileFragment findMyProfileFragment = MyProfileFragment.findMyProfileFragment(supportFragmentManager);
                            if (findMyProfileFragment != null) {
                                findMyProfileFragment.setDisplayName(trim, trim2);
                                dismissAllowingStateLoss();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                dismissAllowingStateLoss();
            }
        }

        public void afterTextChanged(Editable editable) {
            updateButtons();
        }

        private void updateButtons() {
            Button button = this.mBtnOK;
            if (button != null) {
                button.setEnabled(checkInput());
            }
        }

        /* access modifiers changed from: private */
        public boolean checkInput() {
            return !StringUtil.isEmptyOrNull(this.mEdtFirstName.getText().toString().trim()) && !StringUtil.isEmptyOrNull(this.mEdtLastName.getText().toString().trim());
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 2) {
                return false;
            }
            onClickBtnOK();
            return true;
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(AppUtil.getPublicFilesPath());
        sb.append("/my-avatar.jpg");
        AVATAR_PATH = sb.toString();
    }

    public static void showInActivity(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
            if (supportFragmentManager != null) {
                supportFragmentManager.beginTransaction().add(16908290, new MyProfileFragment(), MyProfileFragment.class.getName()).commit();
            }
        }
    }

    @Nullable
    public static MyProfileFragment findMyProfileFragment(FragmentManager fragmentManager) {
        return (MyProfileFragment) fragmentManager.findFragmentByTag(MyProfileFragment.class.getName());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_my_profile, null);
        if (OsUtil.isAtLeastN()) {
            this.mFingerprintUtil = new FingerprintUtil((ZMActivity) getActivity());
        }
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mOptionProfilePhoto = inflate.findViewById(C4558R.C4560id.optionProfilePhoto);
        this.mOptionDisplayName = inflate.findViewById(C4558R.C4560id.optionDisplayName);
        this.mAvatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        this.mOptionPresenceStatus = inflate.findViewById(C4558R.C4560id.optionPresenceStatus);
        this.mTxtDisplayName = (TextView) inflate.findViewById(C4558R.C4560id.txtDisplayName);
        this.mDisplayNameArrow = (ImageView) inflate.findViewById(C4558R.C4560id.displayNameArrow);
        this.mAvatarArrow = (ImageView) inflate.findViewById(C4558R.C4560id.avatarArrow);
        this.mBtnSignout = inflate.findViewById(C4558R.C4560id.btnSignout);
        this.mBtnPMI = inflate.findViewById(C4558R.C4560id.btnPMI);
        this.mTxtPersonalMeetingId = (TextView) inflate.findViewById(C4558R.C4560id.txtMeetingId);
        this.mBtnPassword = inflate.findViewById(C4558R.C4560id.btnPassword);
        this.mTxtPhoneNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtPhoneNumber);
        this.mBtnCallinCountry = inflate.findViewById(C4558R.C4560id.btnCallinCountry);
        this.mTxtCallinCountry = (TextView) inflate.findViewById(C4558R.C4560id.txtCallinCountry);
        this.mTxtUserType = (TextView) inflate.findViewById(C4558R.C4560id.txtUserType);
        this.mTxtAccountDesp = (TextView) inflate.findViewById(C4558R.C4560id.txtAccountDesp);
        this.mBtnMeetingRoomName = inflate.findViewById(C4558R.C4560id.btnMeetingRoomName);
        this.mTxtMeetingRoomName = (TextView) inflate.findViewById(C4558R.C4560id.txtMeetingRoomName);
        this.mTxtCustomStatus = (TextView) inflate.findViewById(C4558R.C4560id.txtCustomStatus);
        this.mPanelCustomStatus = inflate.findViewById(C4558R.C4560id.panelCustomStatus);
        this.mOptionFingerprint = inflate.findViewById(C4558R.C4560id.optionFingerprint);
        this.presenceStatus = (ImageView) inflate.findViewById(C4558R.C4560id.presenceStatus);
        this.txtPresenceStatus = (TextView) inflate.findViewById(C4558R.C4560id.txtPresenceStatus);
        this.mChkOpenFingerprint = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkOpenFingerprint);
        this.mTxtDepartment = (TextView) inflate.findViewById(C4558R.C4560id.txt_department);
        this.mTxtJobTitle = (TextView) inflate.findViewById(C4558R.C4560id.txt_job_title);
        this.mTxtLocation = (TextView) inflate.findViewById(C4558R.C4560id.txt_location);
        this.mOptionFingerprint.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        if (PTApp.getInstance().isChangeNameEnabled()) {
            this.mOptionDisplayName.setOnClickListener(this);
        } else {
            this.mDisplayNameArrow.setVisibility(8);
        }
        if (!PTApp.getInstance().isChangePictureEnabled() || !PTApp.getInstance().isImportPhotosFromDeviceEnable()) {
            this.mAvatarArrow.setVisibility(8);
        } else {
            this.mOptionProfilePhoto.setOnClickListener(this);
        }
        this.mBtnPassword.setVisibility(ZmLoginHelper.isExcludeModifyPasswd(PTApp.getInstance().getPTLoginType()) ? 8 : 0);
        this.mBtnSignout.setOnClickListener(this);
        this.mBtnPMI.setOnClickListener(this);
        this.mBtnPassword.setOnClickListener(this);
        this.mBtnCallinCountry.setOnClickListener(this);
        this.mBtnMeetingRoomName.setOnClickListener(this);
        this.mPanelCustomStatus.setOnClickListener(this);
        this.mOptionPresenceStatus.setOnClickListener(this);
        if (bundle != null) {
            String string = bundle.getString("mImageUri");
            if (string != null) {
                this.mImageUri = Uri.parse(string);
            }
            String string2 = bundle.getString("mCaptureUri");
            if (string2 != null) {
                this.mCaptureUri = Uri.parse(string2);
            }
            this.mAvatarToUploadOnSignedOn = bundle.getString("mAvatarToUploadOnSignedOn");
        }
        PTUI.getInstance().addPTUIListener(this);
        if (!PTApp.getInstance().hasZoomMessenger()) {
            this.mPanelCustomStatus.setVisibility(8);
        }
        this.mSelectedCountryId = PreferenceUtil.readStringValue(PreferenceUtil.CALLIN_SELECTED_COUNTRY_ID, null);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerListener);
        PTUI.getInstance().removeMeetingMgrListener(this.mMeetingMgrListener);
    }

    public void onResume() {
        super.onResume();
        updateEnableFingerprintOption();
        int loginType = getLoginType();
        if (loginType == 97 || loginType == 102) {
            dismiss();
            return;
        }
        if (this.mMeetingMgrListener == null) {
            this.mMeetingMgrListener = new SimpleMeetingMgrListener() {
                public void onListMeetingResult(int i) {
                    MyProfileFragment.this.updatePMI();
                }
            };
        }
        PTUI.getInstance().addMeetingMgrListener(this.mMeetingMgrListener);
        if (this.mZoomMessengerListener == null) {
            this.mZoomMessengerListener = new SimpleZoomMessengerUIListener() {
                public void Indicate_VCardInfoReady(String str) {
                    MyProfileFragment.this.Indicate_VCardInfoReady(str);
                }
            };
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerListener);
        updateUI();
    }

    public void onStart() {
        super.onStart();
        NotificationSettingUI.getInstance().addListener(this.mListener);
    }

    public void onStop() {
        NotificationSettingUI.getInstance().removeListener(this.mListener);
        super.onStop();
    }

    /* access modifiers changed from: private */
    public void Indicate_VCardInfoReady(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null && StringUtil.isSameString(str, myself.getJid())) {
                updateCustomMessage();
            }
        }
    }

    public void updateUI() {
        updateDisplayName();
        updateAvatar();
        updatePMI();
        updateEnableAddrBook();
        updateAdditionalValue();
        updateVanityURL();
        updateCallinCountry();
        updateCustomMessage();
        updatePresence();
        updateUserType();
    }

    private void updateUserType() {
        String str;
        Object[] objArr;
        int i;
        if (this.mTxtUserType != null && this.mTxtAccountDesp != null) {
            boolean z = false;
            if (PTApp.getInstance().isPaidUser()) {
                boolean isCorpUser = PTApp.getInstance().isCorpUser();
                this.mTxtUserType.setText(getString(isCorpUser ? C4558R.string.zm_lbl_profile_user_type_onprem_122473 : C4558R.string.zm_lbl_profile_user_type_licensed_122473));
                if (isCorpUser) {
                    i = C4558R.string.zm_lbl_onprem_learn_more_122473;
                    objArr = new Object[]{""};
                } else {
                    i = C4558R.string.zm_lbl_licensed_learn_more_122473;
                    objArr = new Object[]{""};
                }
                str = getString(i, objArr);
            } else {
                this.mTxtUserType.setText(getString(C4558R.string.zm_lbl_profile_user_type_basic_88385));
                str = getString(C4558R.string.zm_lbl_basic_learn_more_122473, "");
            }
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                z = currentUserProfile.isSupportFeatureEnablePaidUserForCN();
            }
            if (PTApp.getInstance().isPaidUser() || !z) {
                this.mTxtAccountDesp.setMovementMethod(LinkMovementMethod.getInstance());
                this.mTxtAccountDesp.setText(ZMHtmlUtil.fromHtml(getContext(), str, new OnURLSpanClickListener() {
                    public void onClick(View view, String str, String str2) {
                        PTApp.getInstance().navWebWithDefaultBrowser(19, null);
                    }
                }));
            } else {
                this.mTxtAccountDesp.setText(C4558R.string.zm_lbl_ncp_epidemic_cn_profile_137975);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updatePresence() {
        updatePresence(getPresence());
    }

    private void updateAdditionalValue() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            if (!TextUtils.isEmpty(currentUserProfile.getDepartment())) {
                this.mTxtDepartment.setText(currentUserProfile.getDepartment());
            }
            if (!TextUtils.isEmpty(currentUserProfile.getJobTitle())) {
                this.mTxtJobTitle.setText(currentUserProfile.getJobTitle());
            }
            if (!TextUtils.isEmpty(currentUserProfile.getLocation())) {
                this.mTxtLocation.setText(currentUserProfile.getLocation());
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        String str = TAG;
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C28605 r2 = new EventAction("MyProfileFragmentPermissionResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ((MyProfileFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        nonNullEventTaskManagerOrThrowException.pushLater(str, r2);
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 106 && checkCameraAndExternalSoragePermission()) {
                takePhoto();
            } else if (i == 107 && checkExternalSoragePermission()) {
                doChoosePhoteGromGallery();
            }
        }
    }

    private boolean checkExternalSoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private boolean checkCameraAndExternalSoragePermission() {
        return VERSION.SDK_INT < 23 || (checkSelfPermission("android.permission.CAMERA") == 0 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0);
    }

    /* access modifiers changed from: private */
    public void onSelectCamera() {
        if (checkCameraAndExternalSoragePermission()) {
            takePhoto();
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (checkSelfPermission("android.permission.CAMERA") != 0) {
            arrayList.add("android.permission.CAMERA");
        }
        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        zm_requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 106);
    }

    private void destroyDisposable() {
        CompositeDisposable compositeDisposable = this.mCompositeDisposable;
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        destroyDisposable();
        PTUI.getInstance().removePTUIListener(this);
    }

    private int getLoginType() {
        int pTLoginType = PTApp.getInstance().getPTLoginType();
        if (pTLoginType == 100 && PTApp.getInstance().getSavedZoomAccount() == null) {
            return 102;
        }
        return pTLoginType;
    }

    public void onPTAppEvent(int i, final long j) {
        if (i == 49) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("PT_EVENT_ON_UPDATE_PROFILE") {
                public void run(IUIElement iUIElement) {
                    MyProfileFragment myProfileFragment = (MyProfileFragment) iUIElement;
                    if (myProfileFragment != null) {
                        myProfileFragment.handleOnUpdateProfile(j);
                    }
                }
            });
        } else if (i == 48) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("PT_EVENT_ON_UPLOAD_PICTURE") {
                public void run(IUIElement iUIElement) {
                    MyProfileFragment myProfileFragment = (MyProfileFragment) iUIElement;
                    if (myProfileFragment != null) {
                        myProfileFragment.handleOnUploadPicture(j);
                    }
                }
            });
        } else if (i == 9 || i == 12) {
            if (isResumed()) {
                updateDisplayName();
                updateAvatar();
                if (i == 9) {
                    updateAdditionalValue();
                }
            }
        } else if (i == 1 && isResumed()) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void handleOnUpdateProfile(long j) {
        dismissWaitingDialog();
        if (j == 0) {
            updateDisplayName();
        } else {
            showChangeUserNameFailureMessage();
        }
    }

    /* access modifiers changed from: private */
    public void handleOnUploadPicture(long j) {
        dismissWaitingDialog();
        if (j == 0) {
            updateAvatar();
        } else {
            showUploadProfilePhotoFailureMessage();
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Uri uri = this.mImageUri;
        if (uri != null) {
            bundle.putString("mImageUri", uri.toString());
        }
        Uri uri2 = this.mCaptureUri;
        if (uri2 != null) {
            bundle.putString("mCaptureUri", uri2.toString());
        }
        bundle.putString("mAvatarToUploadOnSignedOn", this.mAvatarToUploadOnSignedOn);
    }

    private void updateDisplayName() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            String myName = PTApp.getInstance().getMyName();
            if (StringUtil.isEmptyOrNull(myName)) {
                myName = activity.getString(C4558R.string.zm_mm_lbl_not_set);
            }
            this.mTxtDisplayName.setText(myName);
        }
    }

    private void updateAvatar() {
        if (getActivity() != null) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            String pictureLocalPath = currentUserProfile != null ? currentUserProfile.getPictureLocalPath() : null;
            if (!StringUtil.isEmptyOrNull(pictureLocalPath) && !ImageUtil.isValidImageFile(pictureLocalPath)) {
                File file = new File(pictureLocalPath);
                if (file.exists()) {
                    file.delete();
                }
                pictureLocalPath = null;
            }
            this.mAvatarView.show(new ParamsBuilder().setName(PTApp.getInstance().getMyName(), getMyJid()).setPath(pictureLocalPath));
        }
    }

    private String getMyJid() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                return myself.getJid();
            }
        }
        return null;
    }

    private void updateCustomMessage() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String signature = myself.getSignature();
                if (signature != null) {
                    signature = signature.trim();
                }
                if (StringUtil.isEmptyOrNull(signature)) {
                    this.mTxtCustomStatus.setText(C4558R.string.zm_mm_lbl_not_set);
                } else {
                    this.mTxtCustomStatus.setText(signature);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updatePMI() {
        if (ZmLoginHelper.isNormalTypeLogin(getLoginType())) {
            this.mBtnPMI.setVisibility(0);
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper == null) {
                this.mTxtPersonalMeetingId.setText("");
                return;
            }
            MeetingInfoProto pmiMeetingItem = meetingHelper.getPmiMeetingItem();
            if (pmiMeetingItem == null) {
                this.mTxtPersonalMeetingId.setText("");
            } else {
                this.mTxtPersonalMeetingId.setText(StringUtil.formatConfNumber(pmiMeetingItem.getMeetingNumber()));
            }
        } else {
            this.mBtnPMI.setVisibility(8);
        }
    }

    private void updateEnableFingerprintOption() {
        boolean z;
        boolean z2 = true;
        if (OsUtil.isAtLeastM()) {
            FingerprintUtil fingerprintUtil = this.mFingerprintUtil;
            z = fingerprintUtil != null && fingerprintUtil.isSupportFingerprint();
        } else {
            z = false;
        }
        if (z) {
            this.mOptionFingerprint.setVisibility(0);
            FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
            CheckedTextView checkedTextView = this.mChkOpenFingerprint;
            if (readFromPreference == null || !readFromPreference.ismEnableFingerprint()) {
                z2 = false;
            }
            checkedTextView.setChecked(z2);
            return;
        }
        this.mOptionFingerprint.setVisibility(8);
    }

    private void updateEnableAddrBook() {
        if (this.mTxtPhoneNumber != null) {
            String registeredPhoneNumber = PTApp.getInstance().getRegisteredPhoneNumber();
            if (StringUtil.isEmptyOrNull(registeredPhoneNumber)) {
                this.mTxtPhoneNumber.setText(C4558R.string.zm_lbl_not_registered);
            } else {
                this.mTxtPhoneNumber.setText(registeredPhoneNumber);
            }
        }
    }

    private void updateVanityURL() {
        if (this.mTxtMeetingRoomName != null) {
            if (PTApp.getInstance().isPremiumFeatureEnabled()) {
                this.mBtnMeetingRoomName.setVisibility(0);
                this.mTxtMeetingRoomName.setText(getVanityURLMeetingName());
            } else {
                this.mBtnMeetingRoomName.setVisibility(8);
            }
        }
    }

    private void updateCallinCountry() {
        String str;
        if (this.mTxtCallinCountry != null) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                CountryCodelistProto callinCountryCodes = currentUserProfile.getCallinCountryCodes();
                if (callinCountryCodes != null) {
                    String defaultCallinTollCountry = currentUserProfile.getDefaultCallinTollCountry();
                    if (StringUtil.isEmptyOrNull(defaultCallinTollCountry)) {
                        this.mTxtCallinCountry.setText(getString(C4558R.string.zm_lbl_callin_country_not_set));
                        return;
                    }
                    CountryCodePT countryCodePT = null;
                    List callinCountryCodesList = callinCountryCodes.getCallinCountryCodesList();
                    if (callinCountryCodesList != null) {
                        Iterator it = callinCountryCodesList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            CountryCodePT countryCodePT2 = (CountryCodePT) it.next();
                            if (defaultCallinTollCountry.equals(countryCodePT2.getId())) {
                                countryCodePT = countryCodePT2;
                                break;
                            }
                        }
                    }
                    if (countryCodePT == null) {
                        str = ZMUtils.getCountryName(defaultCallinTollCountry);
                    } else {
                        str = ZMUtils.getCountryName(countryCodePT.getId());
                        String code = countryCodePT.getCode();
                        if (!StringUtil.isEmptyOrNull(code)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str);
                            sb.append("(");
                            sb.append(code);
                            sb.append(")");
                            str = sb.toString();
                        }
                    }
                    this.mTxtCallinCountry.setText(str);
                }
            }
        }
    }

    @NonNull
    private String getVanityURLMeetingName() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        String pMIVanityURL = currentUserProfile != null ? currentUserProfile.getPMIVanityURL() : null;
        if (StringUtil.isEmptyOrNull(pMIVanityURL)) {
            return "";
        }
        if (pMIVanityURL.endsWith("/")) {
            return "";
        }
        String substring = pMIVanityURL.substring(pMIVanityURL.lastIndexOf(47) + 1);
        int lastIndexOf = substring.lastIndexOf("?pwd=");
        if (lastIndexOf > 0) {
            substring = substring.substring(0, lastIndexOf);
        }
        return substring;
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mOptionProfilePhoto) {
            onClickOptProfilePhoto();
        } else if (view == this.mOptionDisplayName) {
            onClickOptDisplayName();
        } else if (view == this.mOptionPresenceStatus) {
            onClickOptPresenceStatus();
        } else if (view == this.mBtnSignout) {
            onClickBtnSignout();
        } else if (view == this.mBtnPMI) {
            onClickBtnPMI();
        } else if (view == this.mBtnPassword) {
            onClickBtnPassword();
        } else if (view == this.mBtnMeetingRoomName) {
            onClickVanityURL();
        } else if (view == this.mBtnCallinCountry) {
            onClickCallinCountry();
        } else if (view == this.mPanelCustomStatus) {
            onClickPanelCustomStatus();
        } else if (view == this.mOptionFingerprint) {
            onClickFingerprint();
        }
    }

    private void updateTxtPresenceStatus() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            long[] snoozeSettings = notificationSettingMgr.getSnoozeSettings();
            if (snoozeSettings != null) {
                if (snoozeSettings[2] > CmmTime.getMMNow()) {
                    String formatTime = TimeUtil.formatTime((Context) getActivity(), snoozeSettings[1]);
                    String formatTime2 = TimeUtil.formatTime((Context) getActivity(), snoozeSettings[2]);
                    this.txtPresenceStatus.setText(getString(C4558R.string.zm_lbl_notification_dnd_19898, formatTime, formatTime2));
                } else {
                    this.txtPresenceStatus.setText("");
                }
            }
        }
    }

    public void updatePresence(int i) {
        updateTxtPresenceStatus();
        switch (i) {
            case 1:
                this.presenceStatus.setImageResource(C4558R.C4559drawable.zm_away);
                this.presenceStatus.setContentDescription(getResources().getString(C4558R.string.zm_description_mm_presence_away_40739));
                return;
            case 2:
                this.presenceStatus.setImageResource(C4558R.C4559drawable.zm_status_idle);
                this.presenceStatus.setContentDescription(getResources().getString(C4558R.string.zm_description_mm_presence_dnd_19903));
                return;
            case 3:
                this.presenceStatus.setImageResource(C4558R.C4559drawable.zm_status_available);
                this.presenceStatus.setContentDescription(getResources().getString(C4558R.string.zm_description_mm_presence_available));
                return;
            case 4:
                this.presenceStatus.setImageResource(C4558R.C4559drawable.zm_status_dnd);
                this.presenceStatus.setContentDescription(getResources().getString(C4558R.string.zm_description_mm_presence_xa_19903));
                return;
            default:
                this.presenceStatus.setImageResource(C4558R.C4559drawable.zm_offline);
                this.presenceStatus.setContentDescription(getResources().getString(C4558R.string.zm_description_mm_presence_offline));
                return;
        }
    }

    public int getPresence() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return 0;
        }
        return zoomMessenger.getMyPresence();
    }

    private void onClickPanelCustomStatus() {
        CustomStatusFragment.showAsActivity(this, 0);
    }

    private void onClickFingerprint() {
        FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
        if (!this.mChkOpenFingerprint.isChecked()) {
            if (readFromPreference == null || (StringUtil.isEmptyOrNull(readFromPreference.getmUser()) && StringUtil.isEmptyOrNull(readFromPreference.getmVar2()))) {
                DialogUtils.showAlertDialog((ZMActivity) getActivity(), C4558R.string.zm_title_confirm_logout_enable_fingerprint_22438, C4558R.string.zm_btn_ok, C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FingerprintOption fingerprintOption = new FingerprintOption();
                        fingerprintOption.setmEnableFingerprint(true);
                        fingerprintOption.savePreference();
                        MyProfileFragment.this.signOut();
                    }
                });
                return;
            }
            readFromPreference.setmEnableFingerprint(true);
            readFromPreference.savePreference();
            this.mChkOpenFingerprint.setChecked(true);
        } else if (readFromPreference != null) {
            readFromPreference.setmEnableFingerprint(false);
            readFromPreference.savePreference();
            this.mChkOpenFingerprint.setChecked(false);
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        finishFragment(true);
    }

    private void onClickOptProfilePhoto() {
        AppUtil.getPublicFilesPath();
        if (AndroidAppUtil.hasCameraApp(getActivity())) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                GetPhotoMenuFragment.show(fragmentManager);
            }
        } else {
            choosePhoto();
        }
    }

    private void doChoosePhoteGromGallery() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        try {
            ActivityStartHelper.startActivityForResult((Fragment) this, intent, 100);
        } catch (Exception unused) {
        }
    }

    public void choosePhoto() {
        if (checkExternalSoragePermission()) {
            doChoosePhoteGromGallery();
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        zm_requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 107);
    }

    public void takePhoto() {
        String newFilePathForTakingPhoto = ImageUtil.getNewFilePathForTakingPhoto();
        if (OsUtil.isAtLeastQ()) {
            this.mCaptureUri = ImageUtil.createImageUri();
        } else if (!OsUtil.isAtLeastN()) {
            StringBuilder sb = new StringBuilder();
            sb.append("file://");
            sb.append(newFilePathForTakingPhoto);
            this.mCaptureUri = Uri.parse(sb.toString());
        } else if (getActivity() != null) {
            this.mCaptureUri = FileProvider.getUriForFile(getActivity(), getResources().getString(C4558R.string.zm_app_provider), new File(newFilePathForTakingPhoto));
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (OsUtil.isAtLeastN()) {
            intent.addFlags(3);
        }
        intent.putExtra("output", this.mCaptureUri);
        try {
            ActivityStartHelper.startActivityForResult((Fragment) this, intent, 101);
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:8|9|(1:11)|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x006e, code lost:
        if (r7 != false) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0070, code lost:
        r0 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0071, code lost:
        onActivityResult(102, r0, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        r7 = com.zipow.videobox.util.ImageUtil.translateImageAsSmallJpeg((android.content.Context) getActivity(), r7, 400, true, r8.getPath());
        r8 = new android.content.Intent();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x005b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cropImageUri(@androidx.annotation.Nullable android.net.Uri r7, @androidx.annotation.NonNull android.net.Uri r8, int r9, int r10) {
        /*
            r6 = this;
            if (r7 != 0) goto L_0x0003
            return
        L_0x0003:
            r0 = 0
            r1 = 102(0x66, float:1.43E-43)
            r2 = 1
            android.content.Intent r3 = new android.content.Intent     // Catch:{ Exception -> 0x005b }
            java.lang.String r4 = "com.android.camera.action.CROP"
            r3.<init>(r4)     // Catch:{ Exception -> 0x005b }
            boolean r4 = p021us.zoom.androidlib.util.OsUtil.isAtLeastN()     // Catch:{ Exception -> 0x005b }
            if (r4 == 0) goto L_0x0018
            r4 = 3
            r3.addFlags(r4)     // Catch:{ Exception -> 0x005b }
        L_0x0018:
            java.lang.String r4 = "image/*"
            r3.setDataAndType(r7, r4)     // Catch:{ Exception -> 0x005b }
            java.lang.String r4 = "crop"
            java.lang.String r5 = "true"
            r3.putExtra(r4, r5)     // Catch:{ Exception -> 0x005b }
            java.lang.String r4 = "aspectX"
            r3.putExtra(r4, r2)     // Catch:{ Exception -> 0x005b }
            java.lang.String r4 = "aspectY"
            r3.putExtra(r4, r2)     // Catch:{ Exception -> 0x005b }
            java.lang.String r4 = "outputX"
            r3.putExtra(r4, r9)     // Catch:{ Exception -> 0x005b }
            java.lang.String r9 = "outputY"
            r3.putExtra(r9, r10)     // Catch:{ Exception -> 0x005b }
            java.lang.String r9 = "scale"
            r3.putExtra(r9, r2)     // Catch:{ Exception -> 0x005b }
            java.lang.String r9 = "output"
            r3.putExtra(r9, r8)     // Catch:{ Exception -> 0x005b }
            java.lang.String r9 = "return-data"
            r3.putExtra(r9, r0)     // Catch:{ Exception -> 0x005b }
            java.lang.String r9 = "outputFormat"
            android.graphics.Bitmap$CompressFormat r10 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x005b }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x005b }
            r3.putExtra(r9, r10)     // Catch:{ Exception -> 0x005b }
            java.lang.String r9 = "noFaceDetection"
            r3.putExtra(r9, r2)     // Catch:{ Exception -> 0x005b }
            r6.startActivityForResult(r3, r1)     // Catch:{ Exception -> 0x005b }
            goto L_0x0074
        L_0x005b:
            androidx.fragment.app.FragmentActivity r9 = r6.getActivity()     // Catch:{ FileNotFoundException -> 0x0074 }
            r10 = 400(0x190, float:5.6E-43)
            java.lang.String r8 = r8.getPath()     // Catch:{ FileNotFoundException -> 0x0074 }
            boolean r7 = com.zipow.videobox.util.ImageUtil.translateImageAsSmallJpeg(r9, r7, r10, r2, r8)     // Catch:{ FileNotFoundException -> 0x0074 }
            android.content.Intent r8 = new android.content.Intent     // Catch:{ FileNotFoundException -> 0x0074 }
            r8.<init>()     // Catch:{ FileNotFoundException -> 0x0074 }
            if (r7 == 0) goto L_0x0071
            r0 = -1
        L_0x0071:
            r6.onActivityResult(r1, r0, r8)     // Catch:{ FileNotFoundException -> 0x0074 }
        L_0x0074:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MyProfileFragment.cropImageUri(android.net.Uri, android.net.Uri, int, int):void");
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            switch (i) {
                case 100:
                    if (intent != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("file://");
                        sb.append(AVATAR_PATH);
                        this.mImageUri = Uri.parse(sb.toString());
                        Uri data = intent.getData();
                        if (VERSION.SDK_INT < 24) {
                            String pathFromUri = ImageUtil.getPathFromUri(getActivity(), data);
                            if (!StringUtil.isEmptyOrNull(pathFromUri)) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("file://");
                                sb2.append(pathFromUri);
                                data = Uri.parse(sb2.toString());
                            }
                        }
                        if (data != null) {
                            if (!OsUtil.isAtLeastN()) {
                                cropImageUri(data, this.mImageUri, 400, 400);
                                break;
                            } else {
                                onChooseImage(data);
                                break;
                            }
                        }
                    } else {
                        return;
                    }
                    break;
                case 101:
                    Uri uri = this.mCaptureUri;
                    if (uri != null) {
                        if (!StringUtil.isEmptyOrNull(uri.getPath())) {
                            AndroidAppUtil.addImageToGallery(getActivity(), new File(this.mCaptureUri.getPath()));
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("file://");
                        sb3.append(AVATAR_PATH);
                        this.mImageUri = Uri.parse(sb3.toString());
                        cropImageUri(this.mCaptureUri, this.mImageUri, 400, 400);
                        break;
                    }
                    break;
                case 102:
                    Uri uri2 = this.mImageUri;
                    if (uri2 != null) {
                        onSelectedPhoto(uri2);
                        break;
                    }
                    break;
                case 104:
                    CallInNumberItem callInNumberItem = (CallInNumberItem) intent.getSerializableExtra("phoneNumber");
                    if (callInNumberItem != null) {
                        this.mSelectedCountryId = callInNumberItem.countryId;
                        PreferenceUtil.saveStringValue(PreferenceUtil.CALLIN_SELECTED_COUNTRY_ID, this.mSelectedCountryId);
                        updateUI();
                        break;
                    }
                    break;
            }
        }
    }

    private void onSelectedPhoto(Uri uri) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            String pathFromUri = ImageUtil.getPathFromUri(activity, uri);
            if (pathFromUri != null) {
                onSelectedPhoto(pathFromUri);
            }
        }
    }

    public void onSelectedPhoto(String str) {
        if (PTApp.getInstance().isWebSignedOn()) {
            uploadAvatar();
        } else if (PTApp.getInstance().isAuthenticating()) {
            uploadAvatarOnSignedOn(str);
        }
    }

    private void uploadAvatar() {
        if (!NetworkUtil.hasDataNetwork(getActivity())) {
            showConnectionError();
            return;
        }
        File file = new File(AVATAR_PATH);
        if (file.length() > 51200) {
            StringBuilder sb = new StringBuilder();
            sb.append(AVATAR_PATH);
            sb.append(".bak");
            String sb2 = sb.toString();
            File file2 = new File(sb2);
            if (file2.exists()) {
                file2.delete();
            }
            if (file.renameTo(file2)) {
                if (ImageUtil.compressImageFile(sb2, AVATAR_PATH, 60)) {
                    file2.delete();
                } else {
                    file2.renameTo(file);
                }
            }
        }
        if (PTApp.getInstance().user_UploadMyPicture(AVATAR_PATH)) {
            showWaitingDialog();
        } else {
            showUploadProfilePhotoFailureMessage();
        }
    }

    private void uploadAvatarOnSignedOn(String str) {
        this.mAvatarToUploadOnSignedOn = str;
        showWaitingDialog();
    }

    private void onClickOptDisplayName() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            String str = "";
            String str2 = "";
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                str = currentUserProfile.getFirstName();
                str2 = currentUserProfile.getLastName();
            }
            SetNameDialog.showSetNameDialog(fragmentManager, str, str2);
        }
    }

    private void onClickOptPresenceStatus() {
        PresenceStatusFragment.showAsActivity(this);
    }

    private void onClickBtnPMI() {
        if (!PTApp.getInstance().isPaidUser()) {
            showMessageOnlyPaidUserCanModifyId();
        } else if (this.mTxtPersonalMeetingId.getText().length() != 0) {
            SimpleActivity.show((Fragment) this, PMIModifyIDFragment.class.getName(), (Bundle) null, 0, false);
        }
    }

    private void onClickBtnPassword() {
        PasswordEditFragment.showAsActivity((Fragment) this);
    }

    private void onClickVanityURL() {
        String str = "";
        TextView textView = this.mTxtMeetingRoomName;
        if (textView != null) {
            str = textView.getText().toString();
        }
        VanityURLModifyFragment.showAsActivity((Fragment) this, str);
    }

    private void onClickCallinCountry() {
        ChangeCallInCountryFragment.showAsActivity(this, 104);
    }

    private void onClickBtnSignout() {
        String str;
        String str2;
        String str3;
        String str4;
        int sipIdCountInCache = CmmSIPCallManager.getInstance().getSipIdCountInCache();
        if (sipIdCountInCache == 1) {
            str4 = getString(C4558R.string.zm_sip_incall_logout_dialog_title_85332);
            str3 = getString(C4558R.string.zm_sip_incall_logout_dialog_msg_85332);
            str2 = getString(C4558R.string.zm_btn_cancel);
            str = getString(C4558R.string.zm_btn_end_call);
        } else if (sipIdCountInCache > 1) {
            str4 = getString(C4558R.string.zm_sip_incall_multi_logout_dialog_title_85332);
            str3 = getString(C4558R.string.zm_sip_incall_multi_logout_dialog_msg_85332);
            str2 = getString(C4558R.string.zm_btn_cancel);
            str = getString(C4558R.string.zm_btn_end_call);
        } else {
            str4 = getString(C4558R.string.zm_alert_logout);
            str2 = getString(C4558R.string.zm_btn_no);
            str = getString(C4558R.string.zm_btn_yes);
            str3 = null;
        }
        new Builder(getActivity()).setCancelable(false).setTitle((CharSequence) str4).setMessage(str3).setNegativeButton(str2, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MyProfileFragment.this.signOut();
            }
        }).create().show();
    }

    /* access modifiers changed from: private */
    public void signOut() {
        LogoutHandler.getInstance().startLogout();
        showWaitingDialog(false);
    }

    private void showMessageOnlyPaidUserCanModifyId() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            new Builder(zMActivity).setTitle(C4558R.string.zm_msg_only_paid_user_can_modify_pmi).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    /* access modifiers changed from: private */
    public void setDisplayName(String str, String str2) {
        if (!NetworkUtil.hasDataNetwork(getActivity())) {
            showConnectionError();
            return;
        }
        if (PTApp.getInstance().user_UpdateMyName(str, str2)) {
            showWaitingDialog();
        } else {
            showChangeUserNameFailureMessage();
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 0).show();
        }
    }

    private void showWaitingDialog() {
        showWaitingDialog(true);
    }

    private void showWaitingDialog(boolean z) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(z);
            newInstance.show(fragmentManager, "WaitingDialog");
        }
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    private void showChangeUserNameFailureMessage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_mm_msg_change_user_name_failed, 0).show();
        }
    }

    private void showUploadProfilePhotoFailureMessage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_mm_msg_upload_profile_photo_failed, 0).show();
        }
    }

    public void onWebLogin() {
        updateUI();
        if (this.mAvatarToUploadOnSignedOn != null) {
            dismissWaitingDialog();
            uploadAvatar();
        }
    }

    private void onChooseImage(final Uri uri) {
        this.mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<String>() {
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                AndroidAppUtil.getFileExtendNameFromMimType(VideoBoxApplication.getNonNullInstance().getContentResolver().getType(uri));
                File file = new File(MyProfileFragment.AVATAR_PATH);
                if (file.exists()) {
                    file.delete();
                }
                if (FileUtils.copyFileFromUri(VideoBoxApplication.getNonNullInstance(), uri, MyProfileFragment.AVATAR_PATH)) {
                    observableEmitter.onNext(MyProfileFragment.AVATAR_PATH);
                } else {
                    observableEmitter.onNext("");
                }
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.m266io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<? super T>) new Consumer<String>() {
            public void accept(String str) throws Exception {
                if (!StringUtil.isEmptyOrNull(str) && !ZMActivity.isActivityDestroyed(MyProfileFragment.this.getActivity())) {
                    Uri uriForFile = FileProvider.getUriForFile(MyProfileFragment.this.getActivity(), MyProfileFragment.this.getResources().getString(C4558R.string.zm_app_provider), new File(str));
                    MyProfileFragment myProfileFragment = MyProfileFragment.this;
                    myProfileFragment.cropImageUri(uriForFile, myProfileFragment.mImageUri, 400, 400);
                }
            }
        }));
    }
}
