package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.InviteActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.meeting.ConfParams;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingInfoProtos.CountryCode;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.MeetingInvitationUtil;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.ParamsList;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class InviteViaDialogFragment extends ZMDialogFragment {
    private static final String ARG_CONTENT = "content";
    private static final String ARG_MEETING_ID = "meetingId";
    private static final String ARG_MEETING_PSW = "meetingPassword";
    private static final String ARG_MEETING_RAW_PSW = "meetingRawPassword";
    private static final String ARG_MEETING_URL = "meetingUrl";
    private static final String ARG_REQUEST_CODE_FOR_INVITE_BUDDIES = "requestCodeForInviteBuddies";
    private static final String ARG_REQUEST_CODE_FOR_INVITE_BY_PHONE = "requestCodeForInviteByPhone";
    private static final String ARG_REQUEST_CODE_FOR_INVITE_ROOM_SYSTEM = "requestCodeForInviteRoomSystem";
    private static final String ARG_SMS_CONTENT = "smsContent";
    private static final String ARG_TOPIC = "topic";
    private static final String TAG = "InviteViaDialogFragment";
    /* access modifiers changed from: private */
    public OptionListAdapter mAdapter;

    static class BasicItem {
        int iconRes;
        String label;

        public BasicItem(int i, String str) {
            this.iconRes = i;
            this.label = str;
        }
    }

    static class Invite3rdPartyIMContactsItem extends BasicItem {
        public Invite3rdPartyIMContactsItem(int i, String str) {
            super(i, str);
        }
    }

    static class InviteAddrBookItem extends BasicItem {
        public InviteAddrBookItem(int i, String str) {
            super(i, str);
        }
    }

    static class InviteAppItem {
        public static final int TYPE_EMAIL = 0;
        public static final int TYPE_SMS = 1;
        public static final int TYPE_ZOOM_MEETING_INVITE = 2;
        ResolveInfo app;
        int type = 0;

        public InviteAppItem(ResolveInfo resolveInfo, int i) {
            this.app = resolveInfo;
            this.type = i;
        }
    }

    static class InviteByPhoneItem extends BasicItem {
        public InviteByPhoneItem(int i, String str) {
            super(i, str);
        }
    }

    static class InviteCopyItem extends BasicItem {
        public InviteCopyItem(int i, String str) {
            super(i, str);
        }
    }

    static class InviteRoomSystemItem extends BasicItem {
        public InviteRoomSystemItem(int i, String str) {
            super(i, str);
        }
    }

    static class InviteZoomRoomsItem extends BasicItem {
        public InviteZoomRoomsItem(int i, String str) {
            super(i, str);
        }
    }

    static class OptionListAdapter extends BaseAdapter {
        private ZMActivity mActivity;
        @NonNull
        private List<Object> mList = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public OptionListAdapter(ZMActivity zMActivity) {
            this.mActivity = zMActivity;
        }

        public void reloadAll() {
            int i;
            int i2;
            this.mList.clear();
            int inviteOptions = getInviteOptions();
            for (ResolveInfo inviteAppItem : AndroidAppUtil.queryZoomMeetingInviteActivities(this.mActivity)) {
                this.mList.add(new InviteAppItem(inviteAppItem, 2));
            }
            boolean z = false;
            if (!ResourcesUtil.getBoolean((Context) this.mActivity, C4558R.bool.zm_config_invite_by_only_action_meeting_invite, false)) {
                if ((inviteOptions & 1) != 0) {
                    for (ResolveInfo inviteAppItem2 : AndroidAppUtil.querySMSActivities(this.mActivity)) {
                        this.mList.add(new InviteAppItem(inviteAppItem2, 1));
                    }
                }
                if ((inviteOptions & 2) != 0) {
                    for (ResolveInfo inviteAppItem3 : AndroidAppUtil.queryEmailActivities(this.mActivity)) {
                        this.mList.add(new InviteAppItem(inviteAppItem3, 0));
                    }
                }
                int pTLoginType = PTAppDelegation.getInstance().getPTLoginType();
                boolean isWebSignedOn = PTAppDelegation.getInstance().isWebSignedOn();
                if (isWebSignedOn && ZmLoginHelper.isTypeSupportIM(pTLoginType)) {
                    if (pTLoginType != 0) {
                        if (pTLoginType == 2 && ConfMgr.getInstance().isGoogleImEnabled()) {
                            i2 = C4558R.string.zm_lbl_invite_buddy_google;
                            i = C4558R.C4559drawable.zm_ic_setting_google;
                            if (i2 > 0 && i > 0) {
                                this.mList.add(new Invite3rdPartyIMContactsItem(i, this.mActivity.getString(i2)));
                            }
                        }
                    } else if (ConfMgr.getInstance().isFacebookImEnabled()) {
                        i2 = C4558R.string.zm_lbl_invite_buddy_fb;
                        i = C4558R.C4559drawable.zm_ic_setting_fb;
                        this.mList.add(new Invite3rdPartyIMContactsItem(i, this.mActivity.getString(i2)));
                    }
                    i2 = 0;
                    i = 0;
                    this.mList.add(new Invite3rdPartyIMContactsItem(i, this.mActivity.getString(i2)));
                }
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext != null) {
                    z = confContext.hasZoomIM();
                }
                if (isWebSignedOn && z && !VideoBoxApplication.getInstance().isSDKMode()) {
                    this.mList.add(new InviteAddrBookItem(C4558R.C4559drawable.zm_invite_contacts, this.mActivity.getString(C4558R.string.zm_lbl_invite_buddy_zoom)));
                }
                if (isWebSignedOn && z && !VideoBoxApplication.getInstance().isSDKMode() && (confContext.getUserOption2() & 32768) != 0) {
                    this.mList.add(new InviteZoomRoomsItem(C4558R.C4559drawable.zm_invite_zoom_rooms, this.mActivity.getString(C4558R.string.zm_lbl_invite_zoom_rooms)));
                }
                if (isCalloutSupported()) {
                    this.mList.add(new InviteByPhoneItem(C4558R.C4559drawable.zm_ic_invitebyphone, this.mActivity.getString(C4558R.string.zm_callout_title_invite)));
                }
                if (isRoomSystemSupported()) {
                    this.mList.add(new InviteRoomSystemItem(C4558R.C4559drawable.zm_ic_invite_roomsystem, this.mActivity.getString(C4558R.string.zm_lbl_invite_room_system)));
                }
                if (!isCopyURLDisabled() && (inviteOptions & 4) != 0) {
                    this.mList.add(new InviteCopyItem(C4558R.C4559drawable.zm_copy, this.mActivity.getString(C4558R.string.zm_lbl_copy_url)));
                }
            }
        }

        private boolean isCopyURLDisabled() {
            return ResourcesUtil.getBoolean((Context) this.mActivity, C4558R.bool.zm_config_no_copy_url, false);
        }

        private boolean isCalloutSupported() {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            boolean z = false;
            if (confContext == null || confContext.getAppContextParams().getBoolean(ConfParams.CONF_PARAM_NO_DIAL_OUT, false)) {
                return false;
            }
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem == null) {
                return false;
            }
            if (meetingItem.getSupportCallOutType() != 0 && !meetingItem.getTelephonyOff() && !ConfLocalHelper.isPSTNHideInviteByPhone()) {
                z = true;
            }
            return z;
        }

        private boolean isRoomSystemSupported() {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                return false;
            }
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem == null) {
                return false;
            }
            return meetingItem.getIsH323Enabled();
        }

        private int getInviteOptions() {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                return 255;
            }
            ParamsList appContextParams = confContext.getAppContextParams();
            if (appContextParams == null) {
                return 255;
            }
            return appContextParams.getInt(ConfParams.CONF_PARAM_INVITE_OPTIONS, 255);
        }

        public int getCount() {
            return this.mList.size();
        }

        public Object getItem(int i) {
            return this.mList.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(this.mActivity, C4558R.layout.zm_app_item, null);
            }
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgIcon);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            Object obj = this.mList.get(i);
            if (obj instanceof InviteAppItem) {
                InviteAppItem inviteAppItem = (InviteAppItem) obj;
                if (inviteAppItem.type == 2) {
                    ResolveInfo resolveInfo = inviteAppItem.app;
                    textView.setText(AndroidAppUtil.getActivityLabel(this.mActivity, resolveInfo));
                    imageView.setImageDrawable(AndroidAppUtil.getActivityIcon(this.mActivity, resolveInfo));
                } else {
                    ResolveInfo resolveInfo2 = inviteAppItem.app;
                    textView.setText(AndroidAppUtil.getApplicationLabel((Context) this.mActivity, resolveInfo2));
                    imageView.setImageDrawable(AndroidAppUtil.getApplicationIcon((Context) this.mActivity, resolveInfo2));
                }
            } else if (obj instanceof BasicItem) {
                BasicItem basicItem = (BasicItem) obj;
                textView.setText(basicItem.label);
                imageView.setImageResource(basicItem.iconRes);
            }
            return view;
        }
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, String str4, long j, String str5, String str6, int i, int i2, int i3) {
        if (StringUtil.isEmptyOrNull(str3)) {
            str3 = str2;
        }
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TOPIC, str);
        bundle.putString("content", str2);
        bundle.putString(ARG_SMS_CONTENT, str3);
        bundle.putString(ARG_MEETING_URL, str4);
        bundle.putLong("meetingId", j);
        bundle.putString(ARG_MEETING_PSW, str5);
        bundle.putString(ARG_MEETING_RAW_PSW, str6);
        bundle.putInt(ARG_REQUEST_CODE_FOR_INVITE_BUDDIES, i);
        bundle.putInt(ARG_REQUEST_CODE_FOR_INVITE_BY_PHONE, i2);
        bundle.putInt(ARG_REQUEST_CODE_FOR_INVITE_ROOM_SYSTEM, i3);
        InviteViaDialogFragment inviteViaDialogFragment = new InviteViaDialogFragment();
        inviteViaDialogFragment.setArguments(bundle);
        inviteViaDialogFragment.show(fragmentManager, InviteViaDialogFragment.class.getName());
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        InviteViaDialogFragment inviteViaDialogFragment = (InviteViaDialogFragment) fragmentManager.findFragmentByTag(InviteViaDialogFragment.class.getName());
        if (inviteViaDialogFragment == null) {
            return false;
        }
        inviteViaDialogFragment.dismissAllowingStateLoss();
        return true;
    }

    public InviteViaDialogFragment() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        this.mAdapter = new OptionListAdapter((ZMActivity) getActivity());
        ZMAlertDialog create = new Builder(getActivity()).setTitle(C4558R.string.zm_title_invite).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                InviteViaDialogFragment inviteViaDialogFragment = InviteViaDialogFragment.this;
                inviteViaDialogFragment.onClickItem(inviteViaDialogFragment.mAdapter, i);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void onResume() {
        super.onResume();
        this.mAdapter.reloadAll();
        if (this.mAdapter.getCount() == 1) {
            onClickItem(this.mAdapter, 0);
            dismiss();
            return;
        }
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void onClickItem(@NonNull OptionListAdapter optionListAdapter, int i) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(ARG_TOPIC);
            String string2 = arguments.getString("content");
            String string3 = arguments.getString(ARG_SMS_CONTENT);
            String string4 = arguments.getString(ARG_MEETING_URL);
            long j = arguments.getLong("meetingId", 0);
            String string5 = arguments.getString(ARG_MEETING_PSW);
            String string6 = arguments.getString(ARG_MEETING_RAW_PSW);
            Object item = optionListAdapter.getItem(i);
            if (item instanceof InviteAppItem) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    InviteAppItem inviteAppItem = (InviteAppItem) item;
                    ResolveInfo resolveInfo = inviteAppItem.app;
                    if (inviteAppItem.type == 0) {
                        AndroidAppUtil.sendEmailVia(resolveInfo, activity, null, string, string2, null);
                    } else if (inviteAppItem.type == 1) {
                        AndroidAppUtil.sendSMSVia(resolveInfo, activity, null, string3);
                        ZMConfEventTracking.logInviteToMeeting(11);
                    } else if (inviteAppItem.type == 2) {
                        onClickCustomMeetingInvite(resolveInfo, activity, string4, string, string2, j, string5, string6);
                    }
                }
            } else if (item instanceof Invite3rdPartyIMContactsItem) {
                onClickSendIM();
                ZMConfEventTracking.logInviteToMeeting(10);
            } else if (item instanceof InviteCopyItem) {
                FragmentActivity activity2 = getActivity();
                if (activity2 != null && MeetingInvitationUtil.copyInviteURL(activity2) && AccessibilityUtil.isSpokenFeedbackEnabled(activity2)) {
                    AccessibilityUtil.announceForAccessibilityCompat(activity2.getWindow().getDecorView(), C4558R.string.zm_lbl_turn_on_auto_copy_meeting_link_topic_64735);
                }
            } else if (item instanceof InviteAddrBookItem) {
                onClickInviteAddrBookItem();
            } else if (item instanceof InviteZoomRoomsItem) {
                onClickInviteZoomRoomsItem();
                ZMConfEventTracking.logInviteToMeeting(13);
            } else if (item instanceof InviteByPhoneItem) {
                onClickInviteByPhoneItem();
                ZMConfEventTracking.logInviteToMeeting(12);
            } else if (item instanceof InviteRoomSystemItem) {
                onClickInviteRoomSystem();
                ZMConfEventTracking.logInviteToMeeting(14);
            }
        }
    }

    private void onClickCustomMeetingInvite(ResolveInfo resolveInfo, @NonNull Activity activity, String str, String str2, String str3, long j, String str4, String str5) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            AndroidAppUtil.sendZoomMeetingInvitationVia(resolveInfo, activity, str, str2, str3, j, str4, str5, arguments.getInt(ARG_REQUEST_CODE_FOR_INVITE_BUDDIES));
        }
    }

    private void onClickSendIM() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                showInviteBuddies(activity, false, arguments.getInt(ARG_REQUEST_CODE_FOR_INVITE_BUDDIES));
            }
        }
    }

    private void showInviteBuddies(@NonNull Activity activity, boolean z, int i) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            Intent intent = new Intent(getActivity(), InviteActivity.class);
            intent.putExtra(InviteFragment.ARG_MEETING_NUMBER, confContext.getConfNumber());
            intent.putExtra("meetingId", confContext.getMeetingId());
            if (z) {
                intent.putExtra(InviteFragment.ARG_SELECT_FROM_ADDRBOOK, true);
            }
            ActivityStartHelper.startActivityForResult(activity, intent, i);
        }
    }

    private void showInviteZoomRooms(@NonNull Activity activity, int i) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            Intent intent = new Intent(getActivity(), InviteActivity.class);
            intent.putExtra(InviteFragment.ARG_MEETING_NUMBER, confContext.getConfNumber());
            intent.putExtra("meetingId", confContext.getMeetingId());
            intent.putExtra(InviteFragment.ARG_SELECT_FROM_ZOOMROOMS, true);
            ActivityStartHelper.startActivityForResult(activity, intent, i);
        }
    }

    private void onClickInviteAddrBookItem() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                showInviteBuddies(activity, true, arguments.getInt(ARG_REQUEST_CODE_FOR_INVITE_BUDDIES));
            }
        }
    }

    private void onClickInviteZoomRoomsItem() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                showInviteZoomRooms(activity, arguments.getInt(ARG_REQUEST_CODE_FOR_INVITE_BUDDIES));
            }
        }
    }

    private void onClickInviteByPhoneItem() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    int supportCallOutType = meetingItem.getSupportCallOutType();
                    List<CountryCode> calloutCountryCodesList = meetingItem.getCalloutCountryCodesList();
                    ArrayList arrayList = null;
                    if (calloutCountryCodesList != null && calloutCountryCodesList.size() > 0) {
                        arrayList = new ArrayList();
                        for (CountryCode countryCode : calloutCountryCodesList) {
                            String code = countryCode.getCode();
                            if (code.startsWith("+")) {
                                code = code.substring(1);
                            }
                            CountryCodeItem countryCodeItem = new CountryCodeItem(code, countryCode.getId(), countryCode.getName(), countryCode.getNumber(), countryCode.getDisplaynumber(), countryCode.getCalltype());
                            arrayList.add(countryCodeItem);
                        }
                    }
                    Bundle arguments = getArguments();
                    if (arguments != null) {
                        InviteByPhoneFragment.showAsActivity(zMActivity, supportCallOutType, arrayList, arguments.getInt(ARG_REQUEST_CODE_FOR_INVITE_BY_PHONE));
                    }
                }
            }
        }
    }

    private void onClickInviteRoomSystem() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                InviteRoomSystemFragment.showAsActivity(zMActivity, null, arguments.getInt(ARG_REQUEST_CODE_FOR_INVITE_ROOM_SYSTEM));
            }
        }
    }
}
