package com.zipow.videobox.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.zipow.videobox.AudioOptionActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.confapp.meeting.AudioOptionParcelItem;
import com.zipow.videobox.confapp.meeting.SelectAlterHostItem;
import com.zipow.videobox.dialog.conf.ZMPasswordRulePopview;
import com.zipow.videobox.fragment.ScheduleChooseUserTypeFragment;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AuthProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.Builder;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMScheduleUtil;
import com.zipow.videobox.util.ZmPtUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public abstract class ZMBaseMeetingOptionLayout extends LinearLayout implements OnClickListener {
    public static final int ACTION_RECORD_CLOUD = 1;
    private static final int ACTION_RECORD_INVALIDATE = -1;
    public static final int ACTION_RECORD_LOCAL = 0;
    @Nullable
    private List<AlterHost> mAlterHostList;
    private boolean mAttendeeVideoOn;
    @NonNull
    private AudioOptionParcelItem mAudioOptionParcelItem;
    private LoginMeetingAuthItem mAuthItem;
    private ArrayList<LoginMeetingAuthItem> mAuthList;
    private CheckedTextView mChkAttendeeVideo;
    private CheckedTextView mChkAudioWaterMark;
    private CheckedTextView mChkAutoRecording;
    private CheckedTextView mChkCNMeeting;
    private CheckedTextView mChkEnableJBH;
    private CheckedTextView mChkEnableWaitingRoom;
    private CheckedTextView mChkHostVideo;
    private String mDeletedAuthId;
    private EditText mEdt3rdPartyAudioInfo;
    private boolean mHostVideoOn;
    @NonNull
    protected TextWatcher mInputWatcher;
    private int mJoinUserType;
    private Set<String> mManualInputEmails;
    /* access modifiers changed from: private */
    public MeetingOptionListener mMeetingOptionListener;
    private View mOption3rdPartyAudioInfo;
    private View mOptionAlterHost;
    protected View mOptionAttendeeVideo;
    private View mOptionAudio;
    private View mOptionAudioWaterMark;
    private View mOptionAutoRecording;
    private View mOptionEnableCNMeeting;
    private View mOptionEnableJBH;
    private View mOptionEnableWaitingRoom;
    protected View mOptionHostVideo;
    private View mOptionJoinUserType;
    private View mOptionRecordLocation;
    protected ZMPasswordRulePopview mPasswordRulePopviewDialog;
    @Nullable
    private List<AlterHost> mPmiAlterHostList;
    private LoginMeetingAuthItem mPmiAuthItem;
    private int mPmiDefaultSelectedAudioType;
    private int mPmiDefaultSelectedRecordLocation;
    private int mPmiJoinUserType;
    @Nullable
    private ScheduledMeetingItem mPmiMeetingItem;
    private List<String> mPmiSelectedDialInCountries;
    @Nullable
    private RetainedFragment mRetainedFragment;
    private int mSelectedRecordLocation;
    private TextView mTvAdvancedOptions;
    private TextView mTvAlterHost;
    private TextView mTxtAudioOption;
    private TextView mTxtDialInDesc;
    private TextView mTxtJoinUserType;
    private TextView mTxtRecordLocationDesc;

    public interface MeetingOptionListener {
        @NonNull
        Fragment getFragmentContext();

        @Nullable
        ScheduledMeetingItem getMeetingItem();

        boolean isEditMeeting();

        void onOptionChanged();
    }

    public static class PasswordKeyListener extends DigitsKeyListener {
        private final char[] mAcceptedChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*.?_-+=<>()[]{},'\\\"/\\\\|:;~`".toCharArray();

        public int getInputType() {
            return 1;
        }

        public PasswordKeyListener() {
            super(false, false);
        }

        /* access modifiers changed from: protected */
        @NonNull
        public char[] getAcceptedChars() {
            return this.mAcceptedChars;
        }
    }

    public static class RetainedFragment extends ZMFragment {
        @Nullable
        private List<AlterHost> mAlterHostItems = null;
        private AudioOptionParcelItem mAudioOptionParcelItem = new AudioOptionParcelItem();
        private Set<String> mManualInputEmails = new HashSet();

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveAlterHostItems(List<AlterHost> list) {
            this.mAlterHostItems = list;
        }

        @Nullable
        public List<AlterHost> restoreAlterHostItems() {
            return this.mAlterHostItems;
        }

        public void savManualInputEmails(Set<String> set) {
            this.mManualInputEmails = set;
        }

        public Set<String> restoreManualInputEmails() {
            return this.mManualInputEmails;
        }

        public void saveAudioOptionParcelItem(AudioOptionParcelItem audioOptionParcelItem) {
            this.mAudioOptionParcelItem = audioOptionParcelItem;
        }

        public AudioOptionParcelItem restoreAudioOptionParcelItem() {
            return this.mAudioOptionParcelItem;
        }
    }

    public void checkPmiSettingChangeByUi() {
    }

    public abstract int getLayout();

    public ZMBaseMeetingOptionLayout(Context context) {
        this(context, null);
    }

    public ZMBaseMeetingOptionLayout(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHostVideoOn = true;
        this.mAttendeeVideoOn = true;
        this.mAudioOptionParcelItem = new AudioOptionParcelItem();
        this.mManualInputEmails = new HashSet();
        this.mSelectedRecordLocation = -1;
        this.mAuthItem = null;
        this.mPmiDefaultSelectedRecordLocation = -1;
        this.mPmiDefaultSelectedAudioType = 2;
        this.mPmiAuthItem = null;
        this.mInputWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (ZMBaseMeetingOptionLayout.this.mMeetingOptionListener != null) {
                    ZMBaseMeetingOptionLayout.this.mMeetingOptionListener.onOptionChanged();
                }
                if (ZMBaseMeetingOptionLayout.this.mPasswordRulePopviewDialog != null && ZMBaseMeetingOptionLayout.this.mPasswordRulePopviewDialog.isPopupWindowShowing()) {
                    ZMBaseMeetingOptionLayout.this.mPasswordRulePopviewDialog.onPasswordChange(editable.toString());
                }
            }
        };
        init();
    }

    public void setmMeetingOptionListener(MeetingOptionListener meetingOptionListener) {
        this.mMeetingOptionListener = meetingOptionListener;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putBoolean("enableWaitingRoom", this.mChkEnableWaitingRoom.isChecked());
        bundle.putBoolean("enableJBH", this.mChkEnableJBH.isChecked());
        bundle.putBoolean("cnMeeting", this.mChkCNMeeting.isChecked());
        bundle.putBoolean("mHostVideoOn", this.mHostVideoOn);
        bundle.putBoolean("mAttendeeVideoOn", this.mAttendeeVideoOn);
        bundle.putParcelable("mAudioOptionParcelItem", this.mAudioOptionParcelItem);
        bundle.putInt("mJoinUserType", this.mJoinUserType);
        bundle.putParcelableArrayList("mAuthsList", this.mAuthList);
        bundle.putParcelable("mAuthItem", this.mAuthItem);
        bundle.putInt("mSelectedRecordLocation", this.mSelectedRecordLocation);
        bundle.putBoolean("mChkAudioWaterMark", this.mChkAudioWaterMark.isChecked());
        bundle.putBoolean("mChkAutoRecording", this.mChkAutoRecording.isChecked());
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            retainedFragment.saveAlterHostItems(this.mAlterHostList);
            this.mRetainedFragment.saveAudioOptionParcelItem(this.mAudioOptionParcelItem);
            this.mRetainedFragment.savManualInputEmails(this.mManualInputEmails);
        }
    }

    public void restoreSaveInstance(@Nullable Bundle bundle) {
        if (bundle != null) {
            this.mChkEnableWaitingRoom.setChecked(bundle.getBoolean("enableWaitingRoom"));
            this.mChkEnableJBH.setChecked(bundle.getBoolean("enableJBH"));
            this.mChkCNMeeting.setChecked(bundle.getBoolean("cnMeeting"));
            CheckedTextView checkedTextView = this.mChkAudioWaterMark;
            checkedTextView.setChecked(bundle.getBoolean("mChkAudioWaterMark", checkedTextView.isChecked()));
            CheckedTextView checkedTextView2 = this.mChkAutoRecording;
            checkedTextView2.setChecked(bundle.getBoolean("mChkAutoRecording", checkedTextView2.isChecked()));
            this.mHostVideoOn = bundle.getBoolean("mHostVideoOn");
            this.mAttendeeVideoOn = bundle.getBoolean("mAttendeeVideoOn");
            AudioOptionParcelItem audioOptionParcelItem = (AudioOptionParcelItem) bundle.getParcelable("mAudioOptionParcelItem");
            if (audioOptionParcelItem != null) {
                this.mAudioOptionParcelItem = audioOptionParcelItem;
            }
            this.mJoinUserType = bundle.getInt("mJoinUserType");
            this.mAuthList = bundle.getParcelableArrayList("mAuthsList");
            this.mAuthItem = (LoginMeetingAuthItem) bundle.getParcelable("mAuthItem");
            this.mSelectedRecordLocation = bundle.getInt("mSelectedRecordLocation", this.mSelectedRecordLocation);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0040, code lost:
        if (r1 == null) goto L_0x0042;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initViewData(@androidx.annotation.Nullable com.zipow.videobox.view.ScheduledMeetingItem r9) {
        /*
            r8 = this;
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.PTUserProfile r0 = r0.getCurrentUserProfile()
            if (r0 != 0) goto L_0x000b
            return
        L_0x000b:
            com.zipow.videobox.ptapp.MeetingInfoProtos$AvailableDialinCountry r1 = r0.getAvailableDiallinCountry()
            if (r1 == 0) goto L_0x002c
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r2 = r8.mAudioOptionParcelItem
            java.lang.String r3 = r1.getHash()
            r2.setHash(r3)
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r2 = r8.mAudioOptionParcelItem
            java.util.List r3 = r1.getAllCountriesList()
            r2.setmAllDialInCountries(r3)
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r2 = r8.mAudioOptionParcelItem
            java.util.List r3 = r1.getSelectedCountriesList()
            r2.setmSelectedDialInCountries(r3)
        L_0x002c:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r2 = r2.isCNMeetingON()
            r3 = 0
            r4 = 1
            if (r9 == 0) goto L_0x0142
            boolean r1 = r9.isUsePmiAsMeetingID()
            if (r1 == 0) goto L_0x0042
            com.zipow.videobox.view.ScheduledMeetingItem r1 = r8.mPmiMeetingItem
            if (r1 != 0) goto L_0x0043
        L_0x0042:
            r1 = r9
        L_0x0043:
            r8.initMeetingOptions(r0, r1)
            if (r2 == 0) goto L_0x0051
            android.widget.CheckedTextView r2 = r8.mChkCNMeeting
            boolean r5 = r9.isCnMeetingOn()
            r2.setChecked(r5)
        L_0x0051:
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r2 = r8.mAudioOptionParcelItem
            int r1 = com.zipow.videobox.util.ZmPtUtils.getMeetingDefaultAudioOption(r0, r1)
            r2.setmSelectedAudioType(r1)
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.MeetingHelper r1 = r1.getMeetingHelper()
            if (r1 != 0) goto L_0x0065
            return
        L_0x0065:
            boolean r2 = com.zipow.videobox.util.ZMScheduleUtil.isDefaultEnableOnlyAuthUsersCanJoinInEdit(r0, r9)
            if (r2 == 0) goto L_0x006d
            r2 = 2
            goto L_0x006e
        L_0x006d:
            r2 = 1
        L_0x006e:
            r8.mJoinUserType = r2
            boolean r2 = r8.isOnlySignInJoin()
            r8.setSignInChecked(r2, r9)
            java.util.ArrayList r2 = com.zipow.videobox.util.ZMScheduleUtil.getAuthList(r0)
            r8.mAuthList = r2
            long r5 = r9.getMeetingNo()
            com.zipow.videobox.ptapp.MeetingInfoProtos$MeetingInfoProto r1 = r1.getMeetingItemByNumber(r5)
            if (r1 == 0) goto L_0x01b3
            com.zipow.videobox.ptapp.MeetingInfoProtos$AvailableDialinCountry r2 = r1.getAvailableDialinCountry()
            if (r2 == 0) goto L_0x00a9
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r5 = r8.mAudioOptionParcelItem
            boolean r6 = r2.getIncludedTollfree()
            r5.setIncludeTollFree(r6)
            java.util.List r5 = r2.getSelectedCountriesList()
            boolean r5 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r5)
            if (r5 != 0) goto L_0x00a9
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r5 = r8.mAudioOptionParcelItem
            java.util.List r2 = r2.getSelectedCountriesList()
            r5.setmSelectedDialInCountries(r2)
        L_0x00a9:
            java.util.List r2 = r1.getAlterHostList()
            r8.mAlterHostList = r2
            boolean r2 = r8.isOnlySignInJoin()
            if (r2 == 0) goto L_0x01b3
            com.zipow.videobox.ptapp.MeetingInfoProtos$AuthProto r1 = r1.getAuthProto()
            if (r1 == 0) goto L_0x01b3
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r2 = new com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem
            r2.<init>(r1)
            r8.mAuthItem = r2
            boolean r1 = r0.isLockOnlyAuthUsersCanJoin()
            if (r1 == 0) goto L_0x00d8
            java.util.ArrayList<com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem> r1 = r8.mAuthList
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r2 = r8.mAuthItem
            java.lang.String r2 = r2.getAuthId()
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r1 = com.zipow.videobox.util.ZMScheduleUtil.getAuthItemById(r1, r2)
            r8.mAuthItem = r1
            goto L_0x01b3
        L_0x00d8:
            java.util.ArrayList<com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem> r1 = r8.mAuthList
            java.util.Iterator r1 = r1.iterator()
            r2 = 0
        L_0x00df:
            boolean r5 = r1.hasNext()
            if (r5 == 0) goto L_0x0130
            java.lang.Object r5 = r1.next()
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r5 = (com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem) r5
            java.lang.String r6 = r5.getAuthId()
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r7 = r8.mAuthItem
            java.lang.String r7 = r7.getAuthId()
            boolean r6 = r6.equalsIgnoreCase(r7)
            if (r6 == 0) goto L_0x0116
            boolean r2 = r0.isLockOnlyAuthUsersCanJoin()
            if (r2 == 0) goto L_0x010b
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r2 = r8.mAuthItem
            java.lang.String r5 = r5.getAuthDomain()
            r2.setAuthDomain(r5)
            goto L_0x0114
        L_0x010b:
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r2 = r8.mAuthItem
            java.lang.String r2 = r2.getAuthDomain()
            r5.setAuthDomain(r2)
        L_0x0114:
            r2 = 1
            goto L_0x00df
        L_0x0116:
            int r6 = r5.getAuthType()
            if (r6 != 0) goto L_0x00df
            java.lang.String r6 = r5.getAuthName()
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r7 = r8.mAuthItem
            java.lang.String r7 = r7.getAuthName()
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isSameString(r6, r7)
            if (r6 == 0) goto L_0x00df
            r8.mAuthItem = r5
            r2 = 1
            goto L_0x00df
        L_0x0130:
            if (r2 != 0) goto L_0x01b3
            java.util.ArrayList<com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem> r1 = r8.mAuthList
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r2 = r8.mAuthItem
            r1.add(r2)
            com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem r1 = r8.mAuthItem
            java.lang.String r1 = r1.getAuthId()
            r8.mDeletedAuthId = r1
            goto L_0x01b3
        L_0x0142:
            boolean r5 = com.zipow.videobox.util.ZMScheduleUtil.isUsePmi(r0)
            if (r1 == 0) goto L_0x015a
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r6 = r8.mAudioOptionParcelItem
            boolean r7 = r1.getIncludedTollfree()
            r6.setIncludeTollFree(r7)
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r6 = r8.mAudioOptionParcelItem
            java.util.List r1 = r1.getSelectedCountriesList()
            r6.setmSelectedDialInCountries(r1)
        L_0x015a:
            if (r5 == 0) goto L_0x017c
            com.zipow.videobox.view.ScheduledMeetingItem r1 = r8.mPmiMeetingItem
            if (r1 == 0) goto L_0x017c
            r8.initMeetingOptions(r0, r1)
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r1 = r8.mAudioOptionParcelItem
            com.zipow.videobox.view.ScheduledMeetingItem r5 = r8.mPmiMeetingItem
            int r5 = com.zipow.videobox.util.ZmPtUtils.getMeetingDefaultAudioOption(r0, r5)
            r1.setmSelectedAudioType(r5)
            if (r2 == 0) goto L_0x01b3
            android.widget.CheckedTextView r1 = r8.mChkCNMeeting
            com.zipow.videobox.view.ScheduledMeetingItem r2 = r8.mPmiMeetingItem
            boolean r2 = r2.isCnMeetingOn()
            r1.setChecked(r2)
            goto L_0x01b3
        L_0x017c:
            boolean r1 = com.zipow.videobox.util.ZMScheduleUtil.isHostVideoOnInitalInCreate(r0)
            r8.mHostVideoOn = r1
            boolean r1 = com.zipow.videobox.util.ZMScheduleUtil.isAttendeeVideoOnInitalInCreate(r0)
            r8.mAttendeeVideoOn = r1
            android.widget.CheckedTextView r1 = r8.mChkEnableWaitingRoom
            boolean r5 = com.zipow.videobox.util.ZMScheduleUtil.isEnableWaitingRoomInitalInCreate(r0)
            r1.setChecked(r5)
            android.widget.CheckedTextView r1 = r8.mChkEnableJBH
            boolean r5 = com.zipow.videobox.util.ZMScheduleUtil.isEnableJBHInitalInCreate(r0)
            r1.setChecked(r5)
            com.zipow.videobox.confapp.meeting.AudioOptionParcelItem r1 = r8.mAudioOptionParcelItem
            int r5 = com.zipow.videobox.util.ZmPtUtils.getDefaultAudioOption(r0)
            r1.setmSelectedAudioType(r5)
            r8.initSignIn(r0)
            if (r2 == 0) goto L_0x01b3
            android.widget.CheckedTextView r1 = r8.mChkCNMeeting
            java.lang.String r2 = "schedule_opt.cn_meeting"
            boolean r2 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r2, r3)
            r1.setChecked(r2)
        L_0x01b3:
            boolean r1 = r8.isShowAutoRecordOption(r0)
            r2 = -1
            if (r1 == 0) goto L_0x01bf
            int r1 = r8.getDefaultRecordOption(r0, r9)
            goto L_0x01c0
        L_0x01bf:
            r1 = -1
        L_0x01c0:
            r8.mSelectedRecordLocation = r1
            int r1 = r8.mSelectedRecordLocation
            if (r1 == r2) goto L_0x01fd
            boolean r1 = r0.isDefaultEnableRecording()
            if (r1 == 0) goto L_0x01d6
            if (r9 == 0) goto L_0x01d4
            boolean r1 = r0.isLockAutomaticRecording()
            if (r1 == 0) goto L_0x01d6
        L_0x01d4:
            r1 = 1
            goto L_0x01d7
        L_0x01d6:
            r1 = 0
        L_0x01d7:
            android.widget.CheckedTextView r2 = r8.mChkAutoRecording
            if (r1 == 0) goto L_0x01dc
            goto L_0x01ec
        L_0x01dc:
            if (r9 == 0) goto L_0x01eb
            boolean r1 = r9.ismIsEnableCloudRecording()
            if (r1 != 0) goto L_0x01ec
            boolean r9 = r9.ismIsEnableLocalRecording()
            if (r9 == 0) goto L_0x01eb
            goto L_0x01ec
        L_0x01eb:
            r4 = 0
        L_0x01ec:
            r2.setChecked(r4)
            android.widget.TextView r9 = r8.mTxtRecordLocationDesc
            int r1 = r8.mSelectedRecordLocation
            if (r1 != 0) goto L_0x01f8
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_local_computer_57100
            goto L_0x01fa
        L_0x01f8:
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_in_the_cloud_57100
        L_0x01fa:
            r9.setText(r1)
        L_0x01fd:
            r8.checkLockOptions()
            r8.getSomePmiSettingValueForUiChangeCheck(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ZMBaseMeetingOptionLayout.initViewData(com.zipow.videobox.view.ScheduledMeetingItem):void");
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i != 2001) {
            switch (i) {
                case 2004:
                    if (i2 == -1 && intent != null) {
                        this.mAlterHostList = ConfLocalHelper.transformIMAddrBookItemsToAlterHosts((ArrayList) intent.getSerializableExtra("selectedItems"), this.mManualInputEmails);
                        this.mTvAlterHost.setText(ConfLocalHelper.getDescAlternativeHosts(getContext(), this.mAlterHostList));
                        MeetingOptionListener meetingOptionListener = this.mMeetingOptionListener;
                        if (meetingOptionListener != null) {
                            meetingOptionListener.onOptionChanged();
                        }
                        RetainedFragment retainedFragment = this.mRetainedFragment;
                        if (retainedFragment != null) {
                            retainedFragment.saveAlterHostItems(this.mAlterHostList);
                            this.mRetainedFragment.savManualInputEmails(this.mManualInputEmails);
                            break;
                        }
                    }
                    break;
                case 2005:
                    if (i2 == -1 && intent != null) {
                        this.mAudioOptionParcelItem = (AudioOptionParcelItem) intent.getParcelableExtra(AudioOptionActivity.RESULT_SELECT_AUDIO_OPTION_ITEM);
                        MeetingOptionListener meetingOptionListener2 = this.mMeetingOptionListener;
                        if (meetingOptionListener2 != null) {
                            meetingOptionListener2.onOptionChanged();
                        }
                        RetainedFragment retainedFragment2 = this.mRetainedFragment;
                        if (retainedFragment2 != null) {
                            retainedFragment2.saveAudioOptionParcelItem(this.mAudioOptionParcelItem);
                        }
                        updateAudioOptions();
                        break;
                    }
            }
        } else {
            if (intent != null && i2 == -1) {
                this.mJoinUserType = intent.getIntExtra(ZMScheduleUtil.ARG_JOIN_USER_TYPE, 1);
                if (isOnlySignInJoin()) {
                    this.mAuthItem = (LoginMeetingAuthItem) intent.getParcelableExtra(ZMScheduleUtil.ARG_MEETING_AUTH_ITEM);
                    LoginMeetingAuthItem loginMeetingAuthItem = this.mAuthItem;
                    if (loginMeetingAuthItem != null && loginMeetingAuthItem.getAuthType() == 1) {
                        Iterator it = this.mAuthList.iterator();
                        while (it.hasNext()) {
                            LoginMeetingAuthItem loginMeetingAuthItem2 = (LoginMeetingAuthItem) it.next();
                            if (loginMeetingAuthItem2.getAuthId().equalsIgnoreCase(this.mAuthItem.getAuthId())) {
                                loginMeetingAuthItem2.setAuthDomain(this.mAuthItem.getAuthDomain());
                            }
                        }
                    }
                }
            }
            updateJoinUserType();
            updateWaterMark();
        }
        checkPmiSettingChangeByUi();
    }

    public boolean validate3rdPartyAudioInfo() {
        boolean z = true;
        if (!this.mOption3rdPartyAudioInfo.isShown()) {
            return true;
        }
        if (this.mEdt3rdPartyAudioInfo.getText().length() <= 0) {
            z = false;
        }
        return z;
    }

    public void hideAdvancedOptions() {
        this.mTvAdvancedOptions.setVisibility(0);
        this.mOptionEnableWaitingRoom.setVisibility(8);
        this.mOptionEnableJBH.setVisibility(8);
        this.mOptionEnableCNMeeting.setVisibility(8);
        this.mOptionJoinUserType.setVisibility(8);
        this.mOptionAlterHost.setVisibility(8);
        this.mOptionAutoRecording.setVisibility(8);
        this.mOptionRecordLocation.setVisibility(8);
        this.mOptionAudioWaterMark.setVisibility(8);
    }

    public void updateUIStatus() {
        updateVideoOptions();
        updateAudioOptions();
        updateJoinUserType();
        this.mTvAlterHost.setText(ConfLocalHelper.getDescAlternativeHosts(getContext(), this.mAlterHostList));
        int i = this.mSelectedRecordLocation;
        if (i != -1) {
            this.mTxtRecordLocationDesc.setText(i == 0 ? C4558R.string.zm_lbl_local_computer_57100 : C4558R.string.zm_lbl_in_the_cloud_57100);
        }
    }

    public void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
            return;
        }
        this.mAlterHostList = retainedFragment.restoreAlterHostItems();
        this.mManualInputEmails = this.mRetainedFragment.restoreManualInputEmails();
        this.mAudioOptionParcelItem = this.mRetainedFragment.restoreAudioOptionParcelItem();
        updateAudioOptions();
        this.mTvAlterHost.setText(ConfLocalHelper.getDescAlternativeHosts(getContext(), this.mAlterHostList));
    }

    public void saveMeetingOptionsAsDefault() {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SCHEDULE_OPT_ENABLE_WAITING_ROOM, this.mChkEnableWaitingRoom.isChecked());
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SCHEDULE_OPT_JBH, this.mChkEnableJBH.isChecked());
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SCHEDULE_OPT_HOST_VIDEO_ON, this.mHostVideoOn);
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SCHEDULE_OPT_ATTENDEE_VIDEO_ON, this.mAttendeeVideoOn);
        PreferenceUtil.saveIntValue(PreferenceUtil.SCHEDULE_OPT_AUDIO_OPTION, this.mAudioOptionParcelItem.getmSelectedAudioType());
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SCHEDULE_OPT_CN_MEETING, this.mChkCNMeeting.isChecked());
        PreferenceUtil.saveIntValue(PreferenceUtil.SCHEDULE_OPT_JOIN_USER_TYPE, this.mJoinUserType);
        if (isOnlySignInJoin()) {
            LoginMeetingAuthItem loginMeetingAuthItem = this.mAuthItem;
            if (loginMeetingAuthItem != null && !StringUtil.isEmptyOrNull(loginMeetingAuthItem.getAuthId())) {
                PreferenceUtil.saveStringValue(PreferenceUtil.SCHEDULE_OPT_JOIN_AUTH_ID, this.mAuthItem.getAuthId());
                return;
            }
        }
        PreferenceUtil.saveStringValue(PreferenceUtil.SCHEDULE_OPT_JOIN_AUTH_ID, "");
    }

    public void saveAlterHostsForOnlyEmail() {
        ConfLocalHelper.saveAlterHostsForOnlyEmail(this.mAlterHostList, this.mManualInputEmails);
    }

    public void fillMeetingOptions(@NonNull Builder builder, @NonNull PTUserProfile pTUserProfile) {
        PTApp instance = PTApp.getInstance();
        boolean z = true;
        builder.setIsSupportWaitingRoom(true);
        builder.setIsEnableWaitingRoom(this.mChkEnableWaitingRoom.isChecked());
        builder.setCanJoinBeforeHost(this.mChkEnableJBH.isChecked());
        builder.setIsCnMeeting(this.mChkCNMeeting.isChecked());
        builder.setIsEnableAudioWatermark(isOnlySignInJoin() && pTUserProfile.isEnableAudioWatermark() && this.mChkAudioWaterMark.isChecked());
        int i = -1;
        if (isOnlySignInJoin() && this.mAuthItem != null) {
            AuthProto.Builder newBuilder = AuthProto.newBuilder();
            int authType = this.mAuthItem.getAuthType();
            newBuilder.setAuthDomain(this.mAuthItem.getAuthDomain());
            newBuilder.setAuthId(this.mAuthItem.getAuthId());
            newBuilder.setAuthType(authType);
            newBuilder.setAuthName(this.mAuthItem.getAuthName());
            builder.setAuthProto(newBuilder.build());
            i = authType;
        }
        if (isOnlySignInJoin() && (i == 1 || i == 0)) {
            builder.setIsTurnOnExternalAuth(false);
            builder.setIsOnlySignJoin(true);
        } else if (!isOnlySignInJoin() || i != 2) {
            builder.setIsTurnOnExternalAuth(false);
            builder.setIsOnlySignJoin(false);
        } else {
            builder.setIsTurnOnExternalAuth(true);
            builder.setIsOnlySignJoin(false);
        }
        if (this.mChkAutoRecording.isChecked()) {
            if (this.mSelectedRecordLocation == 0) {
                builder.setIsEnableAutoRecordingLocal(true);
                builder.setIsEnableAutoRecordingCloud(false);
            } else {
                builder.setIsEnableAutoRecordingLocal(false);
                builder.setIsEnableAutoRecordingCloud(true);
            }
            builder.setIsEnableAutoRecordingMtgLevelFirst(true);
        } else {
            builder.setIsEnableAutoRecordingMtgLevelFirst(true);
            builder.setIsEnableAutoRecordingLocal(false);
            builder.setIsEnableAutoRecordingCloud(false);
        }
        builder.setHostVideoOff(!this.mHostVideoOn);
        builder.setAttendeeVideoOff(!this.mAttendeeVideoOn);
        if (!pTUserProfile.hasSelfTelephony() || this.mAudioOptionParcelItem.getmSelectedAudioType() != 3) {
            builder.setIsSelfTelephonyOn(false);
            if (pTUserProfile.hasSelfTelephony() || !pTUserProfile.isDisablePSTN()) {
                builder.setVoipOff((this.mAudioOptionParcelItem.getmSelectedAudioType() == 0 || this.mAudioOptionParcelItem.getmSelectedAudioType() == 2) ? false : true);
                if (this.mAudioOptionParcelItem.getmSelectedAudioType() == 1 || this.mAudioOptionParcelItem.getmSelectedAudioType() == 2) {
                    z = false;
                }
                builder.setTelephonyOff(z);
            }
        } else {
            builder.setIsSelfTelephonyOn(true);
            builder.setOtherTeleConfInfo(this.mEdt3rdPartyAudioInfo.getText().toString());
        }
        if (instance.isPaidUser()) {
            if (this.mAlterHostList == null) {
                this.mAlterHostList = new ArrayList();
            }
            builder.addAllAlterHost(this.mAlterHostList);
        }
        builder.setAvailableDialinCountry(this.mAudioOptionParcelItem.getAvailableDialinCountry());
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.optionEnableJBH) {
            onClickEnableJBH();
        } else if (id == C4558R.C4560id.optionEnableWaitingRoom) {
            onClickChkEnableWaitingRoom();
        } else if (id == C4558R.C4560id.optionHostVideo) {
            onClickChkHostVideo();
        } else if (id == C4558R.C4560id.optionAttendeeVideo) {
            onClickChkAttendeeVideo();
        } else if (id == C4558R.C4560id.optionAudio) {
            onClickOptionAudio();
        } else if (id == C4558R.C4560id.optionEnableCNMeeting) {
            onClickEnableCnMeeting();
        } else if (id == C4558R.C4560id.optionJoinUserType) {
            onClickOptionJoinUserType();
        } else if (id == C4558R.C4560id.tvAdvancedOptions) {
            showAdvancedOptions();
        } else if (id == C4558R.C4560id.optionAlterHost) {
            onClickOptionAlterHost();
        } else if (id == C4558R.C4560id.optionAutoRecording) {
            onClickAutoRecording();
        } else if (id == C4558R.C4560id.optionRecordLocation) {
            onClickRecordLocation();
        } else if (id == C4558R.C4560id.optionAudioWaterMark) {
            onClickOptionAudioWaterMark();
        }
    }

    private void onClickAutoRecording() {
        CheckedTextView checkedTextView = this.mChkAutoRecording;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        this.mOptionRecordLocation.setVisibility(this.mChkAutoRecording.isChecked() ? 0 : 8);
    }

    private void onClickRecordLocation() {
        Context context = getContext();
        if (context != null) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
                if (currentUserProfile.isEnableLocalRecording()) {
                    zMMenuAdapter.addItem(new ZMSimpleMenuItem(0, context.getString(C4558R.string.zm_lbl_local_computer_57100)));
                }
                if (currentUserProfile.isEnableCloudRecording()) {
                    zMMenuAdapter.addItem(new ZMSimpleMenuItem(1, context.getString(C4558R.string.zm_lbl_in_the_cloud_57100)));
                }
                if (zMMenuAdapter.getCount() >= 2) {
                    ZMAlertDialog create = new ZMAlertDialog.Builder(context).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ZMBaseMeetingOptionLayout.this.onSelectRecordLocation((ZMSimpleMenuItem) zMMenuAdapter.getItem(i));
                        }
                    }).create();
                    create.setCanceledOnTouchOutside(true);
                    create.show();
                }
            }
        }
    }

    private void onClickOptionAudioWaterMark() {
        CheckedTextView checkedTextView = this.mChkAudioWaterMark;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    /* access modifiers changed from: protected */
    public void onClickEnableJBH() {
        CheckedTextView checkedTextView = this.mChkEnableJBH;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickChkEnableWaitingRoom() {
        CheckedTextView checkedTextView = this.mChkEnableWaitingRoom;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickChkHostVideo() {
        CheckedTextView checkedTextView = this.mChkHostVideo;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        this.mHostVideoOn = this.mChkHostVideo.isChecked();
    }

    private void onClickChkAttendeeVideo() {
        CheckedTextView checkedTextView = this.mChkAttendeeVideo;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        this.mAttendeeVideoOn = this.mChkAttendeeVideo.isChecked();
    }

    private void onClickOptionAudio() {
        if (PTApp.getInstance().getCurrentUserProfile() != null) {
            MeetingOptionListener meetingOptionListener = this.mMeetingOptionListener;
            if (meetingOptionListener != null) {
                AudioOptionActivity.show(meetingOptionListener.getFragmentContext(), 2005, this.mAudioOptionParcelItem);
            }
        }
    }

    private void onClickEnableCnMeeting() {
        CheckedTextView checkedTextView = this.mChkCNMeeting;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickOptionJoinUserType() {
        if (this.mMeetingOptionListener != null) {
            LoginMeetingAuthItem loginMeetingAuthItem = this.mAuthItem;
            ScheduleChooseUserTypeFragment.showInActivity(this.mMeetingOptionListener.getFragmentContext(), 2001, this.mJoinUserType, loginMeetingAuthItem == null ? "" : loginMeetingAuthItem.getAuthId(), this.mDeletedAuthId, this.mAuthList);
        }
    }

    public boolean isShowAdvancedOptions() {
        return this.mTvAdvancedOptions.getVisibility() != 0;
    }

    public void showAdvancedOptions() {
        int i = 8;
        this.mTvAdvancedOptions.setVisibility(8);
        this.mOptionEnableWaitingRoom.setVisibility(0);
        this.mOptionEnableJBH.setVisibility(0);
        if (!PTApp.getInstance().isCNMeetingON()) {
            this.mChkCNMeeting.setChecked(false);
            this.mOptionEnableCNMeeting.setVisibility(8);
        } else {
            this.mOptionEnableCNMeeting.setVisibility(0);
        }
        this.mOptionJoinUserType.setVisibility(0);
        if (PTApp.getInstance().isPaidUser()) {
            this.mOptionAlterHost.setVisibility(0);
        } else {
            this.mOptionAlterHost.setVisibility(8);
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            this.mOptionJoinUserType.setVisibility(currentUserProfile.isSupportOnlyAuthUsersCanJoin() ? 0 : 8);
            if (isShowAutoRecordOption(currentUserProfile)) {
                this.mOptionAutoRecording.setVisibility(0);
                View view = this.mOptionRecordLocation;
                if (this.mChkAutoRecording.isChecked()) {
                    i = 0;
                }
                view.setVisibility(i);
            } else {
                this.mOptionAutoRecording.setVisibility(8);
                this.mOptionRecordLocation.setVisibility(8);
            }
            updateWaterMark();
        }
    }

    private void setSignInChecked(boolean z, @Nullable ScheduledMeetingItem scheduledMeetingItem) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            boolean z2 = z && currentUserProfile.isEnableAudioWatermark();
            this.mOptionAudioWaterMark.setVisibility((this.mTvAdvancedOptions.getVisibility() == 0 || !z2) ? 8 : 0);
            if (!z2) {
                this.mChkAudioWaterMark.setChecked(false);
            } else if (scheduledMeetingItem == null) {
                this.mChkAudioWaterMark.setChecked(isDefaultAudioWaterMark(currentUserProfile, true));
            } else if (!currentUserProfile.isLockAudioWatermark()) {
                this.mChkAudioWaterMark.setChecked(scheduledMeetingItem.ismIsEnableAudioWaterMark());
            } else {
                this.mChkAudioWaterMark.setChecked(isDefaultAudioWaterMark(currentUserProfile, true));
            }
        }
    }

    private void updateWaterMark() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            int i = 0;
            boolean z = isOnlySignInJoin() && currentUserProfile.isEnableAudioWatermark();
            View view = this.mOptionAudioWaterMark;
            if (this.mTvAdvancedOptions.getVisibility() == 0 || !z) {
                i = 8;
            }
            view.setVisibility(i);
        }
    }

    private boolean isOnlySignInJoin() {
        return this.mJoinUserType == 2;
    }

    public boolean isEnableJBH() {
        return this.mChkEnableJBH.isChecked();
    }

    private void onClickOptionAlterHost() {
        ArrayList<String> arrayList = new ArrayList<>();
        Gson gson = new Gson();
        String string = getContext().getString(C4558R.string.zm_lbl_schedule_alter_host_127873);
        List<AlterHost> list = this.mAlterHostList;
        if (list != null && !list.isEmpty()) {
            for (AlterHost alterHost : this.mAlterHostList) {
                if (alterHost != null) {
                    SelectAlterHostItem selectAlterHostItem = new SelectAlterHostItem();
                    selectAlterHostItem.setEmail(alterHost.getEmail());
                    selectAlterHostItem.setLastName(alterHost.getLastName());
                    selectAlterHostItem.setFirstName(alterHost.getFirstName());
                    selectAlterHostItem.setHostID(alterHost.getHostID());
                    selectAlterHostItem.setPicUrl(alterHost.getPicUrl());
                    selectAlterHostItem.setPmi(alterHost.getPmi());
                    arrayList.add(gson.toJson((Object) selectAlterHostItem));
                }
            }
            string = getContext().getString(C4558R.string.zm_title_select_alternative_host_127873, new Object[]{Integer.valueOf(arrayList.size())});
        }
        SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
        selectContactsParamter.title = string;
        selectContactsParamter.preSelectedItems = arrayList;
        selectContactsParamter.btnOkText = getContext().getString(C4558R.string.zm_btn_ok);
        selectContactsParamter.instructionMessage = null;
        selectContactsParamter.isAnimBottomTop = true;
        selectContactsParamter.isOnlySameOrganization = false;
        selectContactsParamter.includeRobot = false;
        selectContactsParamter.isContainsAllInGroup = false;
        selectContactsParamter.isAlternativeHost = true;
        MeetingOptionListener meetingOptionListener = this.mMeetingOptionListener;
        if (meetingOptionListener != null) {
            MMSelectContactsActivity.show(meetingOptionListener.getFragmentContext(), selectContactsParamter, 2004, (Bundle) null);
        }
    }

    /* access modifiers changed from: protected */
    public void init() {
        View.inflate(getContext(), getLayout(), this);
        this.mChkEnableWaitingRoom = (CheckedTextView) findViewById(C4558R.C4560id.chkEnableWaitingRoom);
        this.mOptionEnableWaitingRoom = findViewById(C4558R.C4560id.optionEnableWaitingRoom);
        this.mChkEnableJBH = (CheckedTextView) findViewById(C4558R.C4560id.chkEnableJBH);
        this.mOptionEnableJBH = findViewById(C4558R.C4560id.optionEnableJBH);
        this.mOptionEnableCNMeeting = findViewById(C4558R.C4560id.optionEnableCNMeeting);
        this.mChkHostVideo = (CheckedTextView) findViewById(C4558R.C4560id.chkHostVideo);
        this.mOptionHostVideo = findViewById(C4558R.C4560id.optionHostVideo);
        this.mChkAttendeeVideo = (CheckedTextView) findViewById(C4558R.C4560id.chkAttendeeVideo);
        this.mOptionAttendeeVideo = findViewById(C4558R.C4560id.optionAttendeeVideo);
        this.mTxtAudioOption = (TextView) findViewById(C4558R.C4560id.txtAudioOption);
        this.mTxtDialInDesc = (TextView) findViewById(C4558R.C4560id.txtDialInDesc);
        this.mOptionAudio = findViewById(C4558R.C4560id.optionAudio);
        this.mEdt3rdPartyAudioInfo = (EditText) findViewById(C4558R.C4560id.edt3rdPartyAudioInfo);
        this.mOption3rdPartyAudioInfo = findViewById(C4558R.C4560id.option3rdPartyAudioInfo);
        this.mChkCNMeeting = (CheckedTextView) findViewById(C4558R.C4560id.chkEnableCNMeeting);
        this.mOptionJoinUserType = findViewById(C4558R.C4560id.optionJoinUserType);
        this.mTxtJoinUserType = (TextView) findViewById(C4558R.C4560id.txtJoinUserType);
        this.mTvAdvancedOptions = (TextView) findViewById(C4558R.C4560id.tvAdvancedOptions);
        this.mOptionAlterHost = findViewById(C4558R.C4560id.optionAlterHost);
        this.mTvAlterHost = (TextView) findViewById(C4558R.C4560id.txtAlterHost);
        this.mOptionAutoRecording = findViewById(C4558R.C4560id.optionAutoRecording);
        this.mChkAutoRecording = (CheckedTextView) findViewById(C4558R.C4560id.chkAutoRecording);
        this.mOptionRecordLocation = findViewById(C4558R.C4560id.optionRecordLocation);
        this.mTxtRecordLocationDesc = (TextView) findViewById(C4558R.C4560id.txtRecordLocationDesc);
        this.mOptionAudioWaterMark = findViewById(C4558R.C4560id.optionAudioWaterMark);
        this.mChkAudioWaterMark = (CheckedTextView) findViewById(C4558R.C4560id.chkAudioWaterMark);
        this.mOptionEnableCNMeeting.setOnClickListener(this);
        this.mOptionEnableWaitingRoom.setOnClickListener(this);
        this.mOptionEnableJBH.setOnClickListener(this);
        this.mOptionHostVideo.setOnClickListener(this);
        this.mOptionAttendeeVideo.setOnClickListener(this);
        this.mOptionAudio.setOnClickListener(this);
        this.mOptionJoinUserType.setOnClickListener(this);
        this.mTvAdvancedOptions.setOnClickListener(this);
        this.mOptionAlterHost.setOnClickListener(this);
        this.mOptionAutoRecording.setOnClickListener(this);
        this.mOptionRecordLocation.setOnClickListener(this);
        this.mOptionAudioWaterMark.setOnClickListener(this);
        this.mEdt3rdPartyAudioInfo.addTextChangedListener(this.mInputWatcher);
        this.mTxtJoinUserType.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                ZMBaseMeetingOptionLayout.this.updateJoinUserType();
            }
        });
        if (this.mPmiMeetingItem == null) {
            this.mPmiMeetingItem = ZmPtUtils.getPMIMeetingItem();
        }
    }

    /* access modifiers changed from: private */
    public void updateJoinUserType() {
        switch (this.mJoinUserType) {
            case 1:
                this.mTxtJoinUserType.setText(C4558R.string.zm_lbl_allow_join_everyone);
                return;
            case 2:
                LoginMeetingAuthItem loginMeetingAuthItem = this.mAuthItem;
                if (loginMeetingAuthItem == null) {
                    return;
                }
                if (loginMeetingAuthItem.getAuthType() == 1) {
                    this.mTxtJoinUserType.setText(getContext().getString(C4558R.string.zm_lbl_internal_domain_120783, new Object[]{Integer.valueOf(ZMScheduleUtil.getDomainListSizeFromStr(this.mAuthItem.getAuthDomain()))}));
                    return;
                }
                this.mTxtJoinUserType.setText(this.mAuthItem.getAuthName());
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void onSelectRecordLocation(@Nullable ZMSimpleMenuItem zMSimpleMenuItem) {
        if (zMSimpleMenuItem != null) {
            this.mSelectedRecordLocation = zMSimpleMenuItem.getAction();
            this.mTxtRecordLocationDesc.setText(zMSimpleMenuItem.getLabel());
            checkPmiSettingChangeByUi();
        }
    }

    private void updateAudioOptions() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null || !currentUserProfile.isDisablePSTN() || currentUserProfile.hasSelfTelephony()) {
            this.mOptionAudio.setVisibility(0);
            String str = this.mAudioOptionParcelItem.getmSelectedDialInCountryDesc(getContext());
            if (!this.mAudioOptionParcelItem.isCanEditCountry() || StringUtil.isEmptyOrNull(str)) {
                this.mTxtDialInDesc.setVisibility(8);
            } else {
                this.mTxtDialInDesc.setVisibility(0);
                this.mTxtDialInDesc.setText(str);
            }
            switch (this.mAudioOptionParcelItem.getmSelectedAudioType()) {
                case 0:
                    this.mTxtAudioOption.setText(C4558R.string.zm_lbl_audio_option_voip);
                    this.mOption3rdPartyAudioInfo.setVisibility(8);
                    break;
                case 1:
                    this.mTxtAudioOption.setText(C4558R.string.zm_lbl_audio_option_telephony);
                    this.mOption3rdPartyAudioInfo.setVisibility(8);
                    break;
                case 2:
                    this.mTxtAudioOption.setText(C4558R.string.zm_lbl_audio_option_voip_and_telephony_detail_127873);
                    this.mOption3rdPartyAudioInfo.setVisibility(8);
                    break;
                case 3:
                    this.mTxtAudioOption.setText(C4558R.string.zm_lbl_audio_option_3rd_party_127873);
                    this.mOption3rdPartyAudioInfo.setVisibility(0);
                    if (this.mEdt3rdPartyAudioInfo.getText().length() == 0 && currentUserProfile != null) {
                        String myTelephoneInfo = currentUserProfile.getMyTelephoneInfo();
                        if (myTelephoneInfo != null) {
                            this.mEdt3rdPartyAudioInfo.setText(myTelephoneInfo);
                            break;
                        }
                    }
                    break;
            }
            MeetingOptionListener meetingOptionListener = this.mMeetingOptionListener;
            if (meetingOptionListener != null) {
                meetingOptionListener.onOptionChanged();
            }
            return;
        }
        this.mOptionAudio.setVisibility(8);
        this.mOption3rdPartyAudioInfo.setVisibility(8);
    }

    public void initMeetingOptions(@NonNull PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        this.mChkEnableWaitingRoom.setChecked(ZMScheduleUtil.isEnableWaitingRoomInitalInEdit(pTUserProfile, scheduledMeetingItem));
        this.mChkEnableJBH.setChecked(ZMScheduleUtil.isEnableJBHInitalInEdit(pTUserProfile, scheduledMeetingItem));
        this.mHostVideoOn = ZMScheduleUtil.isHostVideoOnInitalInEdit(pTUserProfile, scheduledMeetingItem);
        this.mAttendeeVideoOn = ZMScheduleUtil.isAttendeeVideoOnInitalInEdit(pTUserProfile, scheduledMeetingItem);
        updateWaterMark();
    }

    public boolean isUiChangePmiSetting(@NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (scheduledMeetingItem.isHostVideoOff() == this.mChkHostVideo.isChecked() || scheduledMeetingItem.isAttendeeVideoOff() == this.mChkAttendeeVideo.isChecked() || scheduledMeetingItem.getCanJoinBeforeHost() != this.mChkEnableJBH.isChecked() || scheduledMeetingItem.isEnableWaitingRoom() != this.mChkEnableWaitingRoom.isChecked() || scheduledMeetingItem.ismIsEnableAudioWaterMark() != this.mChkAudioWaterMark.isChecked()) {
            return true;
        }
        if ((scheduledMeetingItem.ismIsEnableCloudRecording() || scheduledMeetingItem.ismIsEnableLocalRecording()) != this.mChkAutoRecording.isChecked() || this.mSelectedRecordLocation != this.mPmiDefaultSelectedRecordLocation || this.mPmiDefaultSelectedAudioType != this.mAudioOptionParcelItem.getmSelectedAudioType() || ZMScheduleUtil.isDiffStringList(this.mPmiSelectedDialInCountries, this.mAudioOptionParcelItem.getmShowSelectedDialInCountries()) || this.mPmiJoinUserType != this.mJoinUserType) {
            return true;
        }
        if (isOnlySignInJoin()) {
            LoginMeetingAuthItem loginMeetingAuthItem = this.mAuthItem;
            if (!(loginMeetingAuthItem == null || this.mPmiAuthItem == null || (StringUtil.isSameString(loginMeetingAuthItem.getAuthId(), this.mPmiAuthItem.getAuthId()) && StringUtil.isSameString(this.mAuthItem.getAuthDomain(), this.mPmiAuthItem.getAuthDomain())))) {
                return true;
            }
        }
        if (ZMScheduleUtil.isDiffAlterList(this.mAlterHostList, this.mPmiAlterHostList)) {
            return true;
        }
        return false;
    }

    private boolean isDefaultAudioWaterMark(@NonNull PTUserProfile pTUserProfile, boolean z) {
        boolean isEnableAudioWatermark = pTUserProfile.isEnableAudioWatermark();
        boolean isLockAudioWatermark = pTUserProfile.isLockAudioWatermark();
        boolean z2 = false;
        if (z && isEnableAudioWatermark && !isLockAudioWatermark) {
            return false;
        }
        if (isEnableAudioWatermark && isLockAudioWatermark) {
            z2 = true;
        }
        return z2;
    }

    private void initSignIn(@NonNull PTUserProfile pTUserProfile) {
        this.mJoinUserType = ZMScheduleUtil.isDefaultEnableOnlyAuthUsersCanJoin(pTUserProfile) ? 2 : 1;
        this.mAuthList = ZMScheduleUtil.getAuthList(pTUserProfile);
        if (!isOnlySignInJoin() || this.mAuthList.size() == 0) {
            this.mJoinUserType = 1;
        } else {
            boolean isLockOnlyAuthUsersCanJoin = pTUserProfile.isLockOnlyAuthUsersCanJoin();
            String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.SCHEDULE_OPT_JOIN_AUTH_ID, "");
            this.mAuthItem = ZMScheduleUtil.getAuthItemById(this.mAuthList, isLockOnlyAuthUsersCanJoin ? "" : readStringValue);
            LoginMeetingAuthItem loginMeetingAuthItem = this.mAuthItem;
            if (loginMeetingAuthItem != null && !isLockOnlyAuthUsersCanJoin && !StringUtil.isSameString(readStringValue, loginMeetingAuthItem.getAuthId()) && !pTUserProfile.isDefaultEnableOnlyAuthUsersCanJoin()) {
                this.mAuthItem = null;
                this.mJoinUserType = 1;
            }
        }
        setSignInChecked(isOnlySignInJoin(), null);
        updateWaterMark();
    }

    /* access modifiers changed from: protected */
    public void checkLockOptions() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            boolean isLockHostVideo = currentUserProfile.isLockHostVideo();
            this.mOptionHostVideo.setEnabled(!isLockHostVideo);
            this.mChkHostVideo.setEnabled(!isLockHostVideo);
            boolean isLockParticipants = currentUserProfile.isLockParticipants();
            this.mOptionAttendeeVideo.setEnabled(!isLockParticipants);
            this.mChkAttendeeVideo.setEnabled(!isLockParticipants);
            this.mOptionAudio.setEnabled(!currentUserProfile.isLockAudioType());
            boolean isLockWaitingRoom = currentUserProfile.isLockWaitingRoom();
            this.mOptionEnableWaitingRoom.setEnabled(!isLockWaitingRoom);
            this.mChkEnableWaitingRoom.setEnabled(!isLockWaitingRoom);
            boolean isLockJoinBeforeHost = currentUserProfile.isLockJoinBeforeHost();
            this.mOptionEnableJBH.setEnabled(!isLockJoinBeforeHost);
            this.mChkEnableJBH.setEnabled(!isLockJoinBeforeHost);
            boolean isLockAutomaticRecording = currentUserProfile.isLockAutomaticRecording();
            this.mOptionAutoRecording.setEnabled(!isLockAutomaticRecording);
            this.mChkAutoRecording.setEnabled(!isLockAutomaticRecording);
            this.mOptionRecordLocation.setEnabled(!isLockAutomaticRecording);
            boolean isLockAudioWatermark = currentUserProfile.isLockAudioWatermark();
            this.mChkAudioWaterMark.setEnabled(!isLockAudioWatermark);
            this.mOptionAudioWaterMark.setEnabled(!isLockAudioWatermark);
        }
    }

    private void updateVideoOptions() {
        this.mChkHostVideo.setChecked(this.mHostVideoOn);
        this.mChkAttendeeVideo.setChecked(this.mAttendeeVideoOn);
    }

    private boolean isShowAutoRecordOption(@NonNull PTUserProfile pTUserProfile) {
        return pTUserProfile.isEnableLocalRecording() || pTUserProfile.isEnableCloudRecording();
    }

    private int getDefaultRecordOption(@NonNull PTUserProfile pTUserProfile, @Nullable ScheduledMeetingItem scheduledMeetingItem) {
        if (!pTUserProfile.isLockAutomaticRecording() && scheduledMeetingItem != null) {
            if (scheduledMeetingItem.ismIsEnableLocalRecording()) {
                return 0;
            }
            if (scheduledMeetingItem.ismIsEnableCloudRecording()) {
                return 1;
            }
        }
        return pTUserProfile.isDefaultEnableRecording() ? pTUserProfile.isDefaultEnableCloudRecording() ? 1 : 0 : pTUserProfile.isEnableCloudRecording() ? 1 : 0;
    }

    private void getSomePmiSettingValueForUiChangeCheck(@NonNull PTUserProfile pTUserProfile) {
        if (this.mPmiMeetingItem != null) {
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper != null) {
                MeetingInfoProto meetingItemByNumber = meetingHelper.getMeetingItemByNumber(this.mPmiMeetingItem.getMeetingNo());
                if (meetingItemByNumber != null) {
                    this.mPmiAlterHostList = meetingItemByNumber.getAlterHostList();
                }
            }
            this.mPmiDefaultSelectedRecordLocation = isShowAutoRecordOption(pTUserProfile) ? getDefaultRecordOption(pTUserProfile, this.mPmiMeetingItem) : -1;
            this.mPmiDefaultSelectedAudioType = ZmPtUtils.getMeetingDefaultAudioOption(pTUserProfile, this.mPmiMeetingItem);
        }
        this.mPmiSelectedDialInCountries = this.mAudioOptionParcelItem.getmShowSelectedDialInCountries();
        this.mPmiJoinUserType = this.mJoinUserType;
        this.mPmiAuthItem = this.mAuthItem;
    }

    public ScheduledMeetingItem getPmiMeetingItem() {
        return this.mPmiMeetingItem;
    }

    @Nullable
    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }
}
