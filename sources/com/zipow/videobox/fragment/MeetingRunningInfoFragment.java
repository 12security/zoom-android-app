package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.cmmlib.AppContext;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.fragment.SelectCallInNumberFragment.CallInNumberItem;
import com.zipow.videobox.ptapp.MeetingInfoProtos.CountryCode;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMDomainUtil;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.util.List;
import java.util.Locale;
import org.apache.http.message.TokenParser;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class MeetingRunningInfoFragment extends ZMTipFragment implements OnClickListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final String ARG_DISPLAY_FLAG = "displayFlag";
    public static final int FLAG_SHOW_ALL = 255;
    public static final int FLAG_SHOW_BASIC_INFO = 1;
    public static final int FLAG_SHOW_H323_INFO = 4;
    public static final int FLAG_SHOW_TELE_INFO = 2;
    public static final int REQUEST_SELECT_CALLIN_NUMBER = 100;
    private int mAnchorId = 0;
    private Button mBtnBack;
    private ImageView mImgCountryFlag;
    private ImageView mImgNextArrow;
    private View mPanelBasicInfo;
    private ViewGroup mPanelCallinNumbers;
    private View mPanelH323Info;
    private View mPanelH323MeetingPassword;
    private View mPanelMeetingId;
    private View mPanelMeetingTopic;
    private View mPanelTeleConfInfo;
    private View mPanelTollFree;
    private ViewGroup mPanelTollFreeNumbers;
    @Nullable
    private String mSelectedCountryId = null;
    @Nullable
    private String mSelectedNumber = null;
    private boolean mShowBasicInfo = true;
    private boolean mShowH323Info = true;
    private boolean mShowTeleInfo = true;
    private TextView mTxtAccessCode;
    private TextView mTxtAttendeeId;
    private TextView mTxtH323Info;
    private TextView mTxtH323MeetingId;
    private TextView mTxtH323MeetingPassword;
    private TextView mTxtMeetingId;
    private TextView mTxtMeetingTopic;
    private TextView mTxtOtherNumbers;

    public static class PhoneCallConfirmDialog extends ZMDialogFragment {
        private static final String ARG_DIAL_STRING = "dialString";
        private static final String ARG_NUMBER = "number";
        private static final String TAG = "MeetingRunningInfoFragment$PhoneCallConfirmDialog";

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
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            String string = arguments.getString(ARG_NUMBER);
            final String string2 = arguments.getString(ARG_DIAL_STRING);
            return new Builder(getActivity()).setTitle((CharSequence) string).setMessage(getString(C4558R.string.zm_alert_dial_into_meeting)).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_call, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PhoneCallConfirmDialog.this.callNumber(string2);
                }
            }).create();
        }

        /* access modifiers changed from: private */
        @SuppressLint({"MissingPermission"})
        public void callNumber(String str) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                ZMIntentUtil.callNumber(zMActivity, str);
            }
        }
    }

    public static void showInActivity(ZMActivity zMActivity, int i) {
        MeetingRunningInfoFragment meetingRunningInfoFragment = new MeetingRunningInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_DISPLAY_FLAG, i);
        meetingRunningInfoFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, meetingRunningInfoFragment, MeetingRunningInfoFragment.class.getName()).commit();
    }

    public static void show(@NonNull FragmentManager fragmentManager, int i, int i2) {
        MeetingRunningInfoFragment meetingRunningInfoFragment = getMeetingRunningInfoFragment(fragmentManager);
        if (meetingRunningInfoFragment == null) {
            MeetingRunningInfoFragment meetingRunningInfoFragment2 = new MeetingRunningInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            bundle.putInt(ARG_DISPLAY_FLAG, i2);
            meetingRunningInfoFragment2.setArguments(bundle);
            meetingRunningInfoFragment2.show(fragmentManager, MeetingRunningInfoFragment.class.getName());
            return;
        }
        meetingRunningInfoFragment.setTipVisible(true);
    }

    @Nullable
    public static MeetingRunningInfoFragment getMeetingRunningInfoFragment(FragmentManager fragmentManager) {
        return (MeetingRunningInfoFragment) fragmentManager.findFragmentByTag(MeetingRunningInfoFragment.class.getName());
    }

    public static boolean hide(@NonNull FragmentManager fragmentManager) {
        MeetingRunningInfoFragment meetingRunningInfoFragment = getMeetingRunningInfoFragment(fragmentManager);
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
        MeetingRunningInfoFragment meetingRunningInfoFragment = getMeetingRunningInfoFragment(fragmentManager);
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

    public void refresh() {
        String str;
        String str2;
        String str3;
        String str4;
        int i;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself != null) {
                    if (this.mShowBasicInfo) {
                        this.mPanelBasicInfo.setVisibility(8);
                        MeetingInfoProto meetingItem = confContext.getMeetingItem();
                        if (meetingItem != null) {
                            String topic = meetingItem.getTopic();
                            if (StringUtil.isEmptyOrNull(topic)) {
                                this.mPanelMeetingTopic.setVisibility(8);
                            } else {
                                this.mPanelMeetingTopic.setVisibility(0);
                            }
                            this.mTxtMeetingTopic.setText(topic);
                            this.mTxtMeetingId.setText(StringUtil.formatConfNumber(meetingItem.getMeetingNumber()));
                        } else {
                            return;
                        }
                    } else {
                        this.mPanelBasicInfo.setVisibility(8);
                    }
                    long confNumber = confContext.getConfNumber();
                    String formatConfNumber = StringUtil.formatConfNumber(confNumber, (char) TokenParser.f498SP);
                    if (!this.mShowTeleInfo || !isTelephonyEnabled()) {
                        str = formatConfNumber;
                        this.mPanelTeleConfInfo.setVisibility(8);
                    } else {
                        confContext.getPhoneCallInNumber();
                        String str5 = this.mSelectedCountryId;
                        if (StringUtil.isEmptyOrNull(str5) || !isInCallInNumbers(this.mSelectedNumber)) {
                            String phoneCallInNumber = confContext.getPhoneCallInNumber();
                            if (phoneCallInNumber != null) {
                                str3 = CountryCodeUtil.phoneCountryCodeToIsoCountryCode(PhoneNumberUtil.getCountryCodeFromFormatedPhoneNumber(PhoneNumberUtil.formatNumber(phoneCallInNumber, CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(activity)))));
                                str2 = phoneCallInNumber;
                            } else {
                                str3 = "us";
                                str2 = phoneCallInNumber;
                            }
                        } else {
                            str2 = this.mSelectedNumber;
                            str3 = str5;
                        }
                        if (str3 != null) {
                            str3 = str3.toLowerCase(Locale.US);
                        }
                        Resources resources = getResources();
                        StringBuilder sb = new StringBuilder();
                        sb.append("zm_flag_");
                        sb.append(str3);
                        int identifier = resources.getIdentifier(sb.toString(), "drawable", VideoBoxApplication.getInstance().getPackageName());
                        if (identifier != 0) {
                            this.mImgCountryFlag.setImageResource(identifier);
                        }
                        String tollFreeCallInNumber = confContext.getTollFreeCallInNumber();
                        long attendeeID = myself.getAttendeeID();
                        if (!StringUtil.isEmptyOrNull(str2) || !StringUtil.isEmptyOrNull(tollFreeCallInNumber)) {
                            this.mPanelTeleConfInfo.setVisibility(0);
                            MeetingInfoProto meetingItem2 = confContext.getMeetingItem();
                            boolean pSTNNeedConfirm1 = meetingItem2 != null ? meetingItem2.getPSTNNeedConfirm1() : false;
                            if (StringUtil.isEmptyOrNull(str2)) {
                                this.mPanelCallinNumbers.setVisibility(8);
                                i = identifier;
                                str4 = formatConfNumber;
                            } else {
                                i = identifier;
                                str4 = formatConfNumber;
                                createNumbersLinks(activity, this.mPanelCallinNumbers, str2, confNumber, attendeeID, pSTNNeedConfirm1);
                            }
                            if (StringUtil.isEmptyOrNull(tollFreeCallInNumber)) {
                                this.mPanelTollFree.setVisibility(8);
                            } else {
                                this.mPanelTollFree.setVisibility(0);
                                createNumbersLinks(activity, this.mPanelTollFreeNumbers, tollFreeCallInNumber, confNumber, attendeeID, pSTNNeedConfirm1);
                            }
                            str = str4;
                            this.mTxtAccessCode.setText(str);
                            this.mTxtAttendeeId.setText(String.valueOf(attendeeID));
                            if (meetingItem2 != null) {
                                if (ResourcesUtil.getBoolean((Context) getActivity(), C4558R.bool.zm_config_no_global_callin_numbers, false) || meetingItem2.getCallinCountryCodesCount() == 0) {
                                    this.mImgCountryFlag.setVisibility(8);
                                    this.mImgNextArrow.setVisibility(8);
                                } else {
                                    if (i != 0) {
                                        this.mImgCountryFlag.setVisibility(0);
                                    } else {
                                        this.mImgCountryFlag.setVisibility(8);
                                    }
                                    this.mImgNextArrow.setVisibility(0);
                                }
                            }
                        } else {
                            this.mPanelTeleConfInfo.setVisibility(8);
                            str = formatConfNumber;
                        }
                        C28471 r0 = new OnClickListener() {
                            public void onClick(View view) {
                                MeetingRunningInfoFragment.this.onClickOtherNumbers();
                            }
                        };
                        this.mImgCountryFlag.setOnClickListener(r0);
                        this.mImgNextArrow.setOnClickListener(r0);
                        TextView textView = this.mTxtOtherNumbers;
                        if (textView != null) {
                            textView.setOnClickListener(new OnClickListener() {
                                public void onClick(View view) {
                                    MeetingRunningInfoFragment.this.onClickOtherNumbersLink();
                                }
                            });
                        }
                    }
                    if (this.mShowH323Info) {
                        String h323ConfInfo = confContext.getH323ConfInfo();
                        String h323Password = confContext.getH323Password();
                        if (StringUtil.isEmptyOrNull(h323ConfInfo)) {
                            this.mPanelH323Info.setVisibility(8);
                        } else {
                            this.mPanelH323Info.setVisibility(0);
                            String[] split = h323ConfInfo.split(";");
                            if (split.length > 1) {
                                StringBuilder sb2 = new StringBuilder();
                                int length = split.length;
                                int i2 = 0;
                                boolean z = true;
                                while (i2 < length) {
                                    String str6 = split[i2];
                                    if (!z) {
                                        sb2.append(FontStyleHelper.SPLITOR);
                                    }
                                    sb2.append(str6.trim());
                                    i2++;
                                    z = false;
                                }
                                this.mTxtH323Info.setText(sb2.toString());
                            } else {
                                this.mTxtH323Info.setText(h323ConfInfo);
                            }
                            this.mTxtH323MeetingId.setText(str);
                            if (StringUtil.isEmptyOrNull(h323Password)) {
                                this.mPanelH323MeetingPassword.setVisibility(8);
                            } else {
                                this.mPanelH323MeetingPassword.setVisibility(0);
                                this.mTxtH323MeetingPassword.setText(h323Password);
                            }
                        }
                    } else {
                        this.mPanelH323Info.setVisibility(8);
                    }
                }
            }
        }
    }

    private boolean isInCallInNumbers(String str) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return false;
        }
        List<CountryCode> callinCountryCodesList = meetingItem.getCallinCountryCodesList();
        if (callinCountryCodesList == null) {
            return false;
        }
        for (CountryCode countryCode : callinCountryCodesList) {
            if (countryCode != null && StringUtil.isSameString(this.mSelectedNumber, countryCode.getNumber())) {
                return true;
            }
        }
        return false;
    }

    private boolean isTelephonyEnabled() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                return !meetingItem.getTelephonyOff();
            }
        }
        return true;
    }

    private void createNumbersLinks(@NonNull Activity activity, ViewGroup viewGroup, String str, long j, long j2, boolean z) {
        ViewGroup viewGroup2 = viewGroup;
        String[] split = str.trim().split("\\s*;\\s*");
        viewGroup.removeAllViews();
        LayoutInflater from = LayoutInflater.from(activity);
        for (String trim : split) {
            String trim2 = trim.trim();
            if (trim2.length() != 0) {
                View inflate = from.inflate(C4558R.layout.zm_callin_number, viewGroup2, false);
                decorateCallinNumberTextView(activity, (TextView) inflate.findViewById(C4558R.C4560id.txtCallinNumber), trim2, j, j2, z);
                viewGroup2.addView(inflate);
            }
        }
    }

    private void decorateCallinNumberTextView(@NonNull Activity activity, @NonNull TextView textView, @Nullable final String str, long j, long j2, boolean z) {
        String str2 = str;
        Activity activity2 = activity;
        if (ResourcesUtil.getBoolean((Context) activity, C4558R.bool.zm_config_no_auto_dial_in, false)) {
            TextView textView2 = textView;
        } else if (!VoiceEngineCompat.isFeatureTelephonySupported(activity)) {
            TextView textView3 = textView;
        } else {
            final String buildFullCallInNumberString = buildFullCallInNumberString(activity, str, j, j2, z);
            C28493 r1 = new OnClickListener() {
                public void onClick(View view) {
                    ZMActivity zMActivity = (ZMActivity) MeetingRunningInfoFragment.this.getActivity();
                    if (zMActivity != null) {
                        String str = str;
                        if (str != null) {
                            PhoneCallConfirmDialog.showPhoneCallConfirmDialog(zMActivity, str, buildFullCallInNumberString);
                        }
                    }
                }
            };
            TextView textView4 = textView;
            UIUtil.buildLinkTextView(textView, str, r1);
            return;
        }
        textView.setText(str);
    }

    private String buildFullCallInNumberString(Activity activity, String str, long j, long j2, boolean z) {
        String formatNumber = PhoneNumberUtil.formatNumber(str, (String) null);
        StringBuilder sb = new StringBuilder();
        sb.append(formatNumber);
        sb.append(",,,");
        sb.append(j);
        sb.append("#,,");
        if (z) {
            sb.append("1,");
        }
        sb.append(j2);
        sb.append("#");
        return sb.toString();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isTipVisible", isTipVisible());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_meeting_running_info, null);
        this.mPanelMeetingTopic = inflate.findViewById(C4558R.C4560id.panelMeetingTopic);
        this.mPanelMeetingId = inflate.findViewById(C4558R.C4560id.panelMeetingId);
        this.mTxtMeetingTopic = (TextView) inflate.findViewById(C4558R.C4560id.txtMeetingTopic);
        this.mTxtMeetingId = (TextView) inflate.findViewById(C4558R.C4560id.txtMeetingId);
        this.mPanelBasicInfo = inflate.findViewById(C4558R.C4560id.panelBasicInfo);
        this.mPanelTeleConfInfo = inflate.findViewById(C4558R.C4560id.panelTeleConfInfo);
        this.mPanelH323Info = inflate.findViewById(C4558R.C4560id.panelH323Info);
        this.mPanelCallinNumbers = (ViewGroup) inflate.findViewById(C4558R.C4560id.panelCallInNumbers);
        this.mPanelTollFreeNumbers = (ViewGroup) inflate.findViewById(C4558R.C4560id.panelTollFreeNumbers);
        this.mTxtAccessCode = (TextView) inflate.findViewById(C4558R.C4560id.txtAccessCode);
        this.mTxtAttendeeId = (TextView) inflate.findViewById(C4558R.C4560id.txtAttendeeId);
        this.mTxtOtherNumbers = (TextView) inflate.findViewById(C4558R.C4560id.txtOtherNumbers);
        this.mTxtH323Info = (TextView) inflate.findViewById(C4558R.C4560id.txtH323Info);
        this.mTxtH323MeetingId = (TextView) inflate.findViewById(C4558R.C4560id.txtH323MeetingId);
        this.mImgCountryFlag = (ImageView) inflate.findViewById(C4558R.C4560id.imgCountryFlag);
        this.mImgNextArrow = (ImageView) inflate.findViewById(C4558R.C4560id.imgNextArrow);
        this.mPanelTollFree = inflate.findViewById(C4558R.C4560id.panelTollFree);
        this.mPanelH323MeetingPassword = inflate.findViewById(C4558R.C4560id.panelH323MeetingPassword);
        this.mTxtH323MeetingPassword = (TextView) inflate.findViewById(C4558R.C4560id.txtH323MeetingPassword);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            int i = arguments.getInt(ARG_DISPLAY_FLAG);
            this.mShowBasicInfo = (i & 1) != 0;
            this.mShowTeleInfo = (i & 2) != 0;
            this.mShowH323Info = (i & 4) != 0;
            if (this.mShowBasicInfo) {
                textView.setText(C4558R.string.zm_title_meeting_information);
            } else {
                textView.setText(C4558R.string.zm_btn_dial_in);
            }
        }
        this.mBtnBack.setOnClickListener(this);
        if (ResourcesUtil.getBoolean((Context) getActivity(), C4558R.bool.zm_config_no_global_callin_numbers, false)) {
            this.mImgCountryFlag.setVisibility(8);
            this.mImgNextArrow.setVisibility(8);
        }
        if (ResourcesUtil.getBoolean((Context) getActivity(), C4558R.bool.zm_config_no_global_callin_link, true)) {
            this.mTxtOtherNumbers.setVisibility(8);
        }
        this.mSelectedCountryId = PreferenceUtil.readStringValue(PreferenceUtil.CALLIN_SELECTED_COUNTRY_ID, null);
        this.mSelectedNumber = PreferenceUtil.readStringValue(PreferenceUtil.CALLIN_SELECTED_NUMBER, null);
        return inflate;
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            CallInNumberItem callInNumberItem = (CallInNumberItem) intent.getSerializableExtra("phoneNumber");
            if (callInNumberItem != null) {
                this.mSelectedCountryId = callInNumberItem.countryId;
                this.mSelectedNumber = callInNumberItem.phoneNumber;
                if ("us".equalsIgnoreCase(callInNumberItem.countryId)) {
                    PreferenceUtil.removeValue(PreferenceUtil.CALLIN_SELECTED_COUNTRY_ID);
                    PreferenceUtil.removeValue(PreferenceUtil.CALLIN_SELECTED_NUMBER);
                } else {
                    PreferenceUtil.saveStringValue(PreferenceUtil.CALLIN_SELECTED_COUNTRY_ID, this.mSelectedCountryId);
                    PreferenceUtil.saveStringValue(PreferenceUtil.CALLIN_SELECTED_NUMBER, this.mSelectedNumber);
                }
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
        int i = 0;
        this.mAnchorId = getArguments().getInt("anchorId", 0);
        if (this.mAnchorId > 0) {
            View findViewById = getActivity().findViewById(this.mAnchorId);
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
        if (view.getId() == C4558R.C4560id.btnBack) {
            onClickBtnBack();
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
    public void onClickOtherNumbers() {
        SelectCallInNumberFragment.showAsActivity(this, 100);
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
