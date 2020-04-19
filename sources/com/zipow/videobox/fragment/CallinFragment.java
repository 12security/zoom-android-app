package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.cmmlib.AppContext;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.fragment.SelectCallInCountryFragment.CallInNumberItem;
import com.zipow.videobox.ptapp.MeetingInfoProtos.CountryCode;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.TSPCallInInfo;
import com.zipow.videobox.ptapp.MeetingInfoProtos.UserPhoneInfo;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMDomainUtil;
import com.zipow.videobox.util.ZMUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.TokenParser;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMSettingsCategory;
import p021us.zoom.androidlib.widget.ZMSpanny;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class CallinFragment extends ZMTipFragment implements OnClickListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final int REQUEST_SELECT_CALLIN_NUMBER = 100;
    private static final String TAG = "CallinFragment";
    private int mAnchorId = 0;
    private ViewGroup mCallNumberCategory;
    private ZMSettingsCategory mPanelMeetingInfo;
    private ViewGroup mPanelTeleConfInfo;
    @Nullable
    private String mSelectedCountryId = null;
    private boolean mSupportDialIn;
    private TextView mTxtCountryName;

    public static class PhoneCallConfirmDialog extends ZMDialogFragment {
        private static final String ARG_DIAL_STRING = "dialString";
        private static final String ARG_NUMBER = "number";
        private static final String TAG = "CallinFragment$PhoneCallConfirmDialog";
        /* access modifiers changed from: private */
        @Nullable
        public String mDailString;

        public static void showPhoneCallConfirmDialog(ZMActivity zMActivity, @NonNull String str, @NonNull String str2) {
            PhoneCallConfirmDialog phoneCallConfirmDialog = new PhoneCallConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_NUMBER, str);
            bundle.putString(ARG_DIAL_STRING, str2);
            phoneCallConfirmDialog.setArguments(bundle);
            phoneCallConfirmDialog.show(zMActivity.getSupportFragmentManager(), PhoneCallConfirmDialog.class.getName());
        }

        public PhoneCallConfirmDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                Button button = ((ZMAlertDialog) dialog).getButton(-1);
                if (button != null) {
                    button.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            PhoneCallConfirmDialog phoneCallConfirmDialog = PhoneCallConfirmDialog.this;
                            phoneCallConfirmDialog.doCallNumber(phoneCallConfirmDialog.mDailString);
                        }
                    });
                }
            }
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            String string = arguments.getString(ARG_NUMBER);
            this.mDailString = arguments.getString(ARG_DIAL_STRING);
            return new Builder(getActivity()).setTitle((CharSequence) string).setMessage(getString(C4558R.string.zm_alert_dial_into_meeting)).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_call, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }

        /* access modifiers changed from: protected */
        public void doCallNumber(String str) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.CALL_PHONE") == 0) {
                callNumber(str);
            } else {
                zm_requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 0);
            }
        }

        public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C24904 r2 = new EventAction("CallinPermissionResult") {
                public void run(@Nullable IUIElement iUIElement) {
                    if (iUIElement != null) {
                        ((PhoneCallConfirmDialog) iUIElement).handlePermissionResult(i2, strArr2, iArr2);
                    }
                }
            };
            getNonNullEventTaskManagerOrThrowException().pushLater("CallinPermissionResult", r2);
        }

        public void handlePermissionResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
            if (strArr != null && iArr != null) {
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    if ("android.permission.CALL_PHONE".equals(strArr[i2]) && iArr[i2] == 0) {
                        callNumber(this.mDailString);
                    }
                }
            }
        }

        @SuppressLint({"MissingPermission"})
        public void callNumber(String str) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                ZMIntentUtil.callNumber(zMActivity, str);
                dismiss();
            }
        }
    }

    public static void showInActivity(ZMActivity zMActivity) {
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, new CallinFragment(), CallinFragment.class.getName()).commit();
    }

    public static void show(@NonNull FragmentManager fragmentManager, int i) {
        CallinFragment meetingRunningInfoFragment = getMeetingRunningInfoFragment(fragmentManager);
        if (meetingRunningInfoFragment == null) {
            CallinFragment callinFragment = new CallinFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            callinFragment.setArguments(bundle);
            callinFragment.show(fragmentManager, CallinFragment.class.getName());
            return;
        }
        meetingRunningInfoFragment.setTipVisible(true);
    }

    @Nullable
    public static CallinFragment getMeetingRunningInfoFragment(FragmentManager fragmentManager) {
        return (CallinFragment) fragmentManager.findFragmentByTag(CallinFragment.class.getName());
    }

    public static boolean hide(@NonNull FragmentManager fragmentManager) {
        CallinFragment meetingRunningInfoFragment = getMeetingRunningInfoFragment(fragmentManager);
        if (meetingRunningInfoFragment != null) {
            if (!meetingRunningInfoFragment.getShowsTip()) {
                meetingRunningInfoFragment.dismiss();
                return true;
            } else if (meetingRunningInfoFragment.isTipVisible()) {
                meetingRunningInfoFragment.setTipVisible(false);
                return true;
            }
        }
        return false;
    }

    public static boolean dismiss(@NonNull FragmentManager fragmentManager) {
        CallinFragment meetingRunningInfoFragment = getMeetingRunningInfoFragment(fragmentManager);
        if (meetingRunningInfoFragment == null) {
            return false;
        }
        meetingRunningInfoFragment.dismiss();
        return true;
    }

    public void onResume() {
        super.onResume();
        refresh();
    }

    private void createMeetingInfoItemView(@Nullable Activity activity, @Nullable String str, @Nullable String str2) {
        if (str != null && str2 != null && activity != null && this.mPanelMeetingInfo != null) {
            View inflate = LayoutInflater.from(activity).inflate(C4558R.layout.zm_tsp_info_item, this.mPanelMeetingInfo, false);
            if (inflate != null) {
                TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtValue);
                ((TextView) inflate.findViewById(C4558R.C4560id.txtLabel)).setText(str);
                textView.setText(str2);
                this.mPanelMeetingInfo.addView(inflate, new LayoutParams(-1, -2));
            }
        }
    }

    public void refresh() {
        String str;
        String str2;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself != null) {
                    this.mPanelMeetingInfo.removeAllViews();
                    boolean isTspEnabled = confContext.isTspEnabled();
                    boolean notSupportTelephony = confContext.notSupportTelephony();
                    boolean isViewOnlyMeeting = ConfMgr.getInstance().isViewOnlyMeeting();
                    int i = 2;
                    AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
                    if (audioObj != null) {
                        i = audioObj.getAudioSessionType();
                    }
                    long confNumber = confContext.getConfNumber();
                    String formatConfNumber = StringUtil.formatConfNumber(confNumber, (char) TokenParser.f498SP);
                    long attendeeID = myself.getAttendeeID();
                    if (!isViewOnlyMeeting && !isTspEnabled && !notSupportTelephony && i != 0) {
                        createMeetingInfoItemView(activity, getString(C4558R.string.zm_lbl_meeting_id2), formatConfNumber);
                        createMeetingInfoItemView(activity, getString(C4558R.string.zm_lbl_participant_id), String.valueOf(attendeeID));
                        String meetingPassword = confContext.getMeetingPassword();
                        String h323Password = confContext.getH323Password();
                        if (confContext.isPSTNPassWordProtectionOn() && !StringUtil.isEmptyOrNull(meetingPassword) && !StringUtil.isEmptyOrNull(h323Password)) {
                            createMeetingInfoItemView(activity, getString(C4558R.string.zm_lbl_password), h323Password);
                        }
                    }
                    MeetingInfoProto meetingItem = confContext.getMeetingItem();
                    if (meetingItem != null) {
                        boolean pSTNNeedConfirm1 = meetingItem.getPSTNNeedConfirm1();
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        if (!StringUtil.isEmptyOrNull(this.mSelectedCountryId)) {
                            str = this.mSelectedCountryId;
                        } else {
                            str = meetingItem.getDefaultcallInCountry();
                            if (StringUtil.isEmptyOrNull(str)) {
                                str = CountryCodeUtil.US_ISO_COUNTRY_CODE;
                            }
                        }
                        String countryName = ZMUtils.getCountryName(str);
                        List<CountryCode> callinCountryCodesList = meetingItem.getCallinCountryCodesList();
                        if (callinCountryCodesList != null) {
                            for (CountryCode countryCode : callinCountryCodesList) {
                                if (countryCode != null) {
                                    if (!str.equals(countryCode.getId()) || StringUtil.isEmptyOrNull(countryCode.getDisplaynumber()) || StringUtil.isEmptyOrNull(countryCode.getNumber())) {
                                        str2 = str;
                                    } else {
                                        str2 = str;
                                        if (countryCode.getCalltype() == 1) {
                                            arrayList.add(countryCode);
                                        } else {
                                            arrayList2.add(countryCode);
                                        }
                                    }
                                    str = str2;
                                }
                            }
                        }
                        this.mTxtCountryName.setText(countryName);
                        if (!arrayList2.isEmpty() || !arrayList.isEmpty()) {
                            this.mPanelTeleConfInfo.setVisibility(0);
                            this.mCallNumberCategory.removeAllViews();
                            arrayList2.addAll(arrayList);
                            createNumbersLinkItems(activity, this.mCallNumberCategory, arrayList2, confNumber, attendeeID, pSTNNeedConfirm1);
                        } else {
                            this.mPanelTeleConfInfo.setVisibility(8);
                        }
                        if (isTspEnabled) {
                            List tspCallinInfoList = meetingItem.getTspCallinInfoList();
                            if (tspCallinInfoList != null && tspCallinInfoList.size() > 0) {
                                for (int i2 = 0; i2 < tspCallinInfoList.size(); i2++) {
                                    TSPCallInInfo tSPCallInInfo = (TSPCallInInfo) tspCallinInfoList.get(i2);
                                    createMeetingInfoItemView(activity, tSPCallInInfo.getKey(), tSPCallInInfo.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createNumbersLinkItems(Activity activity, @NonNull ViewGroup viewGroup, List<CountryCode> list, long j, long j2, boolean z) {
        ViewGroup viewGroup2 = viewGroup;
        LayoutInflater from = LayoutInflater.from(activity);
        for (CountryCode countryCode : list) {
            String trim = countryCode.getDisplaynumber().trim();
            if (!StringUtil.isEmptyOrNull(trim) && !StringUtil.isEmptyOrNull(trim)) {
                View inflate = from.inflate(C4558R.layout.zm_call_number_item, viewGroup2, false);
                TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtNumber);
                ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.imgCall);
                imageView.setVisibility(this.mSupportDialIn ? 0 : 8);
                int i = countryCode.getCalltype() == 1 ? 1 : 0;
                if (this.mSupportDialIn) {
                    imageView.setImageResource(i == 0 ? C4558R.C4559drawable.zm_call_highlight : C4558R.C4559drawable.zm_call);
                }
                decorateCallinNumberTextView(textView, imageView, trim, trim, j, j2, z, i);
                viewGroup2.addView(inflate);
            }
        }
    }

    private void decorateCallinNumberTextView(@NonNull TextView textView, @NonNull ImageView imageView, @NonNull final String str, String str2, long j, long j2, boolean z, int i) {
        TextView textView2 = textView;
        ImageView imageView2 = imageView;
        String str3 = str;
        int i2 = i;
        final String buildFullCallInNumberString = buildFullCallInNumberString(str2, j, j2, z);
        if (i2 == 1) {
            textView.setText(getString(C4558R.string.zm_lbl_number_desc_18332, str3, getString(C4558R.string.zm_lbl_toll_free_number_hint)));
        } else {
            textView.setText(str);
        }
        imageView.setContentDescription(getString(i2 == 1 ? C4558R.string.zm_content_desc_dial_free_call_18332 : C4558R.string.zm_content_desc_dial_toll_call_18332));
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ZMActivity zMActivity = (ZMActivity) CallinFragment.this.getActivity();
                if (zMActivity != null) {
                    PhoneCallConfirmDialog.showPhoneCallConfirmDialog(zMActivity, str, buildFullCallInNumberString);
                }
            }
        });
    }

    @Nullable
    private String formatNumberForDial(@Nullable String str) {
        if (str == null) {
            return null;
        }
        String replace = str.replace("(0)", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < replace.length(); i++) {
            char charAt = replace.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                sb.append(charAt);
            } else if (charAt != '+' || sb.length() != 0) {
                if (charAt == ',' || charAt == ';') {
                    break;
                }
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    @NonNull
    private String getTspDailInString() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return "";
        }
        if (getActivity() == null) {
            return "";
        }
        if (ConfMgr.getInstance().getMyself() == null) {
            return "";
        }
        if (!confContext.isTspEnabled()) {
            return "";
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return "";
        }
        return meetingItem.getDailinString();
    }

    private String buildFullCallInNumberString(String str, long j, long j2, boolean z) {
        String formatNumberForDial = formatNumberForDial(str);
        StringBuilder sb = new StringBuilder();
        sb.append(formatNumberForDial);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null || !confContext.isTspEnabled()) {
            sb.append(",,,");
            sb.append(j);
            sb.append("#,,,");
            if (z) {
                sb.append("1,");
            }
            sb.append(j2);
            sb.append("#");
        } else {
            String tspDailInString = getTspDailInString();
            if (!StringUtil.isEmptyOrNull(tspDailInString)) {
                sb.append(tspDailInString);
            }
        }
        return sb.toString();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isTipVisible", isTipVisible());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_callin_info, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtDialNumberTitle);
        this.mTxtCountryName = (TextView) inflate.findViewById(C4558R.C4560id.txtCountryName);
        this.mCallNumberCategory = (ViewGroup) inflate.findViewById(C4558R.C4560id.dialNumberList);
        this.mPanelTeleConfInfo = (ViewGroup) inflate.findViewById(C4558R.C4560id.panelTeleConfInfo);
        this.mPanelMeetingInfo = (ZMSettingsCategory) inflate.findViewById(C4558R.C4560id.panelMeetingInfo);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.llTitle).setOnClickListener(this);
        this.mSelectedCountryId = PreferenceUtil.readStringValue(PreferenceUtil.CALLIN_SELECTED_COUNTRY_ID, null);
        this.mSupportDialIn = !ResourcesUtil.getBoolean((Context) getActivity(), C4558R.bool.zm_config_no_auto_dial_in, false) && VoiceEngineCompat.isFeatureTelephonySupported(getActivity());
        textView.setText(this.mSupportDialIn ? C4558R.string.zm_lbl_dial_select_number_18332 : C4558R.string.zm_lbl_dial_pick_number_18332);
        if (ConfLocalHelper.isOnlyUseTelephoneAndUseOwnPhoneNumber() && ConfLocalHelper.getUserPhoneInfo() != null) {
            ((LinearLayout) inflate.findViewById(C4558R.C4560id.panelUseOwnPhoneTip)).setVisibility(0);
            TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtUseOwnPhoneTip);
            UserPhoneInfo userPhoneInfo = ConfLocalHelper.getUserPhoneInfo();
            if (userPhoneInfo != null) {
                String formatDisplayPhoneNumber = PhoneNumberUtil.formatDisplayPhoneNumber(userPhoneInfo.getCountryCode(), userPhoneInfo.getPhoneNumber());
                ZMSpanny zMSpanny = new ZMSpanny(getString(C4558R.string.zm_call_in_use_own_phone_number_129757, formatDisplayPhoneNumber));
                zMSpanny.setSpans(formatDisplayPhoneNumber, new StyleSpan(1), new ForegroundColorSpan(-16777216), new AbsoluteSizeSpan(15, true));
                textView2.setText(zMSpanny);
            }
        }
        return inflate;
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            CallInNumberItem callInNumberItem = (CallInNumberItem) intent.getSerializableExtra("phoneNumber");
            if (callInNumberItem != null) {
                this.mSelectedCountryId = callInNumberItem.countryId;
                PreferenceUtil.saveStringValue(PreferenceUtil.CALLIN_SELECTED_COUNTRY_ID, this.mSelectedCountryId);
                refresh();
            }
        }
    }

    @Nullable
    public ZMTip onCreateTip(Context context, LayoutInflater layoutInflater, @Nullable Bundle bundle) {
        View view = getView();
        if (view == null) {
            return null;
        }
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(view);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return null;
        }
        int i = 0;
        this.mAnchorId = arguments.getInt("anchorId", 0);
        if (this.mAnchorId > 0) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return null;
            }
            View findViewById = activity.findViewById(this.mAnchorId);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, UIMgr.isLargeMode(context) ? 1 : 3);
            }
        }
        if (bundle != null) {
            if (!bundle.getBoolean("isTipVisible", true)) {
                i = 4;
            }
            zMTip.setVisibility(i);
        }
        return zMTip;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.llTitle) {
            onClickPickCountryOrRegion();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    /* access modifiers changed from: protected */
    public void onClickOtherNumbersLink() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            String str = null;
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                str = confContext.getTeleConfURL();
            }
            if (StringUtil.isEmptyOrNull(str)) {
                String queryWithKey = new AppContext(AppContext.PREFER_NAME_CHAT).queryWithKey(ConfigReader.KEY_WEBSERVER, AppContext.APP_NAME_CHAT);
                if (StringUtil.isEmptyOrNull(queryWithKey)) {
                    queryWithKey = ZMDomainUtil.getWebDomainWithHttps();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(queryWithKey);
                sb.append("/teleconference");
                str = sb.toString();
            }
            UIUtil.openURL(activity, str);
        }
    }

    /* access modifiers changed from: protected */
    public void onClickPickCountryOrRegion() {
        ConfSelectCallInCountryFragment.showAsActivity(this, 100);
    }

    public void dismiss() {
        if (getShowsTip()) {
            super.dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private boolean isTipVisible() {
        ZMTip tip = getTip();
        boolean z = false;
        if (tip == null) {
            return false;
        }
        if (tip.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    private void setTipVisible(boolean z) {
        ZMTip tip = getTip();
        if (tip != null) {
            int i = 0;
            if ((tip.getVisibility() == 0) != z) {
                if (!z) {
                    i = 4;
                }
                tip.setVisibility(i);
                if (z) {
                    tip.startAnimation(AnimationUtils.loadAnimation(getActivity(), C4558R.anim.zm_tip_fadein));
                }
            }
        }
    }
}
