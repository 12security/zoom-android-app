package com.zipow.videobox.view.p014mm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.ptapp.IMProtos.zGroupProperty;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.util.ZMWebLinkFilter;
import com.zipow.videobox.view.JoinConfView.CannotJoinDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSessionDescriptionFragment */
public class MMSessionDescriptionFragment extends ZMDialogFragment implements OnClickListener {
    public static final String EXTRA_ARGS_GROUP_JID = "groupJid";
    private static final int REQUEST_PERMISSION_MIC = 1001;
    private static final String TAG = "MMSessionDescriptionFragment";
    /* access modifiers changed from: private */
    public Button mBtnDone;
    @Nullable
    private Context mContext;
    /* access modifiers changed from: private */
    public EditText mEdtDesc;
    /* access modifiers changed from: private */
    @Nullable
    public String mGroupId;
    private LinearLayout mPannelDesc;
    private String mSelectedPhoneNumber = null;
    @Nullable
    private ZMDialogFragment mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            MMSessionDescriptionFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMSessionDescriptionFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            MMSessionDescriptionFragment.this.On_DestroyGroup(i, str, str2, str3, j);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMSessionDescriptionFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMSessionDescriptionFragment$MeetingNoHookInfo */
    private static class MeetingNoHookInfo {
        public int end;
        public String lable;

        /* renamed from: no */
        public String f344no;
        public int start;

        private MeetingNoHookInfo() {
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSessionDescriptionFragment$MeetingNoMenuItem */
    private static class MeetingNoMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_CALL = 1;
        public static final int ACTION_COPY = 2;
        public static final int ACTION_JOIN_MEETING = 0;

        public MeetingNoMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static void showAsActivity(Fragment fragment, String str, int i) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("groupJid", str);
            SimpleActivity.show(fragment, MMSessionDescriptionFragment.class.getName(), bundle, i, false, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(32);
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_session_description, viewGroup, false);
        this.mBtnDone = (Button) inflate.findViewById(C4558R.C4560id.btnDone);
        this.mPannelDesc = (LinearLayout) inflate.findViewById(C4558R.C4560id.pannel_Desc);
        this.mEdtDesc = (EditText) inflate.findViewById(C4558R.C4560id.edtDesc);
        this.mEdtDesc.setLinkTextColor(getResources().getColor(C4558R.color.zm_link_text_color));
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mBtnDone.setOnClickListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(18);
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtDesc);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onResume() {
        super.onResume();
        updateData();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C37852 r2 = new EventAction("MMSessionDescriptionFragmentPermissionResult") {
            public void run(IUIElement iUIElement) {
                ((MMSessionDescriptionFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("MMSessionDescriptionFragmentPermissionResult", r2);
    }

    private void updateData() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    String groupDesc = groupById.getGroupDesc();
                    this.mEdtDesc.setText(groupDesc);
                    if (groupById.isGroupOperatorable()) {
                        this.mEdtDesc.setFilters(new InputFilter[]{new LengthFilter(500)});
                        if (groupById.isRoom()) {
                            this.mEdtDesc.setHint(getString(C4558R.string.zm_mm_description_channel_def_hint_108993));
                        } else {
                            this.mEdtDesc.setHint(getString(C4558R.string.zm_mm_description_chat_def_hint_108993));
                        }
                        this.mBtnDone.setVisibility(0);
                        this.mBtnDone.setEnabled(false);
                        this.mEdtDesc.setFocusable(true);
                        this.mEdtDesc.setFocusableInTouchMode(true);
                        this.mEdtDesc.setCursorVisible(true);
                        EditText editText = this.mEdtDesc;
                        editText.setSelection(editText.getText().length());
                        this.mEdtDesc.postDelayed(new Runnable() {
                            public void run() {
                                MMSessionDescriptionFragment.this.mEdtDesc.requestFocus();
                                UIUtil.openSoftKeyboard(MMSessionDescriptionFragment.this.getActivity(), MMSessionDescriptionFragment.this.mEdtDesc, 2);
                            }
                        }, 300);
                    } else {
                        this.mEdtDesc.setHint(getString(C4558R.string.zm_mm_description_not_add_hint_108993));
                        if (!TextUtils.isEmpty(groupDesc)) {
                            this.mEdtDesc.setFilters(new InputFilter[]{new LengthFilter(501)});
                            EditText editText2 = this.mEdtDesc;
                            StringBuilder sb = new StringBuilder();
                            sb.append(groupDesc);
                            sb.append("　");
                            editText2.setText(TextUtils.concat(new CharSequence[]{sb.toString()}));
                        }
                        this.mBtnDone.setVisibility(8);
                        this.mEdtDesc.setMovementMethod(LinkMovementMethod.getInstance());
                        this.mEdtDesc.setFocusable(false);
                        this.mEdtDesc.setFocusableInTouchMode(false);
                        this.mEdtDesc.setCursorVisible(false);
                        this.mEdtDesc.clearFocus();
                        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtDesc);
                    }
                    hookZoomURL(this.mEdtDesc);
                    ZMWebLinkFilter.filter(this.mEdtDesc);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(final int i, final GroupAction groupAction, String str) {
        if (groupAction.getGroupDescAction() != 0 && StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    if (!StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                        if (isResumed()) {
                            updateData();
                        }
                        return;
                    }
                    EventTaskManager eventTaskManager = getEventTaskManager();
                    if (eventTaskManager != null) {
                        eventTaskManager.pushLater(new EventAction("GroupAction.GROUP_DESC") {
                            public void run(IUIElement iUIElement) {
                                MMSessionDescriptionFragment mMSessionDescriptionFragment = (MMSessionDescriptionFragment) iUIElement;
                                if (mMSessionDescriptionFragment != null) {
                                    mMSessionDescriptionFragment.handleGroupAction(i, groupAction);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            updateData();
        }
    }

    /* access modifiers changed from: private */
    public void On_DestroyGroup(final int i, String str, String str2, String str3, long j) {
        if (StringUtil.isSameString(str2, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("DestroyGroup") {
                public void run(IUIElement iUIElement) {
                    if (i == 0) {
                        MMSessionDescriptionFragment.this.finishFragment(true);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("NotifyGroupDestroy") {
                public void run(IUIElement iUIElement) {
                    MMSessionDescriptionFragment.this.finishFragment(true);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupAction(int i, @NonNull GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtDesc);
            dismiss();
            return;
        }
        showChangeDescFailureMessage(i);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mGroupId = getArguments().getString("groupJid");
        this.mEdtDesc.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMSessionDescriptionFragment.this.mBtnDone.setEnabled(false);
                if (!StringUtil.isEmptyOrNull(MMSessionDescriptionFragment.this.mGroupId)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomGroup groupById = zoomMessenger.getGroupById(MMSessionDescriptionFragment.this.mGroupId);
                        if (groupById != null && !MMSessionDescriptionFragment.this.formatDesc(editable.toString().trim()).equalsIgnoreCase(groupById.getGroupDesc())) {
                            MMSessionDescriptionFragment.this.mBtnDone.setEnabled(true);
                        }
                    }
                }
            }
        });
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                UIUtil.closeSoftKeyboard(getActivity(), this.mEdtDesc);
                dismiss();
            } else if (id == C4558R.C4560id.btnDone) {
                onClickBtnDone();
            }
        }
    }

    private void onClickBtnDone() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
            String obj = this.mEdtDesc.getText().toString();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null && !obj.equalsIgnoreCase(groupById.getGroupDesc())) {
                    String formatDesc = formatDesc(obj.trim());
                    zGroupProperty groupProperty = groupById.getGroupProperty();
                    if (groupProperty == null || !zoomMessenger.modifyGroupProperty(this.mGroupId, groupById.getGroupName(), formatDesc, groupProperty.getIsPublic(), groupProperty.getIsRestrictSameOrg(), groupProperty.getIsNewMemberCanSeeMessageHistory(), groupProperty.getIsMuc())) {
                        showChangeDescFailureMessage(1);
                    } else {
                        showWaitingDialog();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        if (strArr != null && iArr != null && i == 1001 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
            String str = this.mSelectedPhoneNumber;
            if (str != null) {
                ZMPhoneUtils.callSip(str, null);
            }
            this.mSelectedPhoneNumber = null;
        }
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            this.mWaitingDialog = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            this.mWaitingDialog.setCancelable(true);
            this.mWaitingDialog.show(fragmentManager, "WaitingDialog");
        }
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            } else {
                ZMDialogFragment zMDialogFragment2 = this.mWaitingDialog;
                if (zMDialogFragment2 != null) {
                    try {
                        zMDialogFragment2.dismissAllowingStateLoss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.mWaitingDialog = null;
        }
    }

    private void showChangeDescFailureMessage(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
            } else {
                Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_description_save_failure_msg_108993), 1).show();
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    /* access modifiers changed from: private */
    public String formatDesc(String str) {
        int length = str.length();
        while (length > 0) {
            int i = length - 1;
            if (str.charAt(i) > 13 && str.charAt(i) > 10) {
                break;
            }
            length--;
        }
        return length < str.length() ? str.substring(0, length) : str;
    }

    private void hookZoomURL(@Nullable EditText editText) {
        if (editText != null) {
            Spannable text = editText.getText();
            Matcher matcher = Pattern.compile("(?<!\\d)(?:([0-9]{9,11}))(?!\\d)|(?<!\\d)(?:([0-9]{3})-([0-9]{3})-([0-9]{3,5}))(?!\\d)|(?<!\\d)(?:([0-9]{3}) ([0-9]{3}) ([0-9]{3,5}))(?!\\d)").matcher(text);
            ArrayList<MeetingNoHookInfo> arrayList = new ArrayList<>();
            while (matcher.find()) {
                MeetingNoHookInfo meetingNoHookInfo = new MeetingNoHookInfo();
                meetingNoHookInfo.end = matcher.end();
                meetingNoHookInfo.start = matcher.start();
                meetingNoHookInfo.lable = matcher.group();
                meetingNoHookInfo.f344no = meetingNoHookInfo.lable.replace("-", "").replace(OAuth.SCOPE_DELIMITER, "");
                arrayList.add(meetingNoHookInfo);
            }
            if (arrayList.size() > 0 && !(text instanceof Spannable)) {
                Spannable spannableString = new SpannableString(text);
                editText.setText(spannableString);
                text = spannableString;
            }
            if (text instanceof Spannable) {
                Spannable spannable = text;
                URLSpan[] urls = editText.getUrls();
                if ((urls != null && urls.length >= 1) || arrayList.size() != 0) {
                    if (urls != null && urls.length > 0) {
                        for (URLSpan uRLSpan : urls) {
                            final String url = uRLSpan.getURL();
                            if (url.startsWith("http://https://") || url.startsWith("http://http://")) {
                                url = url.substring(7);
                            } else if (url.startsWith("tel:")) {
                                url = url.substring(4);
                            }
                            if (isZoomURL(url)) {
                                C37918 r5 = new URLSpan(url) {
                                    public void onClick(View view) {
                                        MMSessionDescriptionFragment.this.joinByURL(url);
                                    }
                                };
                                int spanStart = spannable.getSpanStart(uRLSpan);
                                int spanEnd = spannable.getSpanEnd(uRLSpan);
                                int spanFlags = spannable.getSpanFlags(uRLSpan);
                                if (spanStart >= 0 && spanEnd > spanStart) {
                                    spannable.removeSpan(uRLSpan);
                                    spannable.setSpan(r5, spanStart, spanEnd, spanFlags);
                                }
                                removeMeetingNoHookInfoInArea(arrayList, spanStart, spanEnd);
                            } else if (isZoomMeetingNo(url)) {
                                C37929 r52 = new URLSpan(url) {
                                    public void onClick(View view) {
                                        MMSessionDescriptionFragment.this.onClickMeetingNO(url);
                                    }
                                };
                                int spanStart2 = spannable.getSpanStart(uRLSpan);
                                int spanEnd2 = spannable.getSpanEnd(uRLSpan);
                                int spanFlags2 = spannable.getSpanFlags(uRLSpan);
                                if (spanStart2 >= 0 && spanEnd2 > spanStart2) {
                                    spannable.removeSpan(uRLSpan);
                                    spannable.setSpan(r52, spanStart2, spanEnd2, spanFlags2);
                                }
                                removeMeetingNoHookInfoInArea(arrayList, spanStart2, spanEnd2);
                            }
                        }
                    }
                    for (MeetingNoHookInfo meetingNoHookInfo2 : arrayList) {
                        final String str = meetingNoHookInfo2.f344no;
                        C378110 r3 = new URLSpan(str) {
                            public void onClick(View view) {
                                MMSessionDescriptionFragment.this.onClickMeetingNO(str);
                            }
                        };
                        if (meetingNoHookInfo2.start >= 0 && meetingNoHookInfo2.end > meetingNoHookInfo2.start) {
                            spannable.setSpan(r3, meetingNoHookInfo2.start, meetingNoHookInfo2.end, 33);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void joinByURL(String str) {
        Intent intent = new Intent(getContext(), JoinByURLActivity.class);
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        startActivity(intent);
    }

    private void removeMeetingNoHookInfoInArea(@Nullable List<MeetingNoHookInfo> list, int i, int i2) {
        if (!CollectionsUtil.isListEmpty(list) && i >= 0 && i < i2) {
            int i3 = 0;
            while (i3 < list.size()) {
                MeetingNoHookInfo meetingNoHookInfo = (MeetingNoHookInfo) list.get(i3);
                if (meetingNoHookInfo.start >= i && meetingNoHookInfo.end <= i2) {
                    list.remove(i3);
                    i3--;
                }
                i3++;
            }
        }
    }

    private boolean isZoomURL(String str) {
        return str.matches("https?://.+\\.zoom\\.us/[j|w]/.+");
    }

    private boolean isZoomMeetingNo(@NonNull String str) {
        return !StringUtil.isEmptyOrNull(str) && str.matches("^[0-9]{9,11}$");
    }

    public void onClickMeetingNO(@Nullable final String str) {
        Activity activity = (Activity) getContext();
        if (activity != null && !TextUtils.isEmpty(str)) {
            EditText editText = this.mEdtDesc;
            editText.setSelection(editText.getText().length(), this.mEdtDesc.getText().length());
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new MeetingNoMenuItem(activity.getString(C4558R.string.zm_btn_call), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            TextView textView = new TextView(activity);
            if (VERSION.SDK_INT < 23) {
                textView.setTextAppearance(activity, C4558R.style.ZMTextView_Medium);
            } else {
                textView.setTextAppearance(C4558R.style.ZMTextView_Medium);
            }
            int dip2px = UIUtil.dip2px(activity, 20.0f);
            textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
            textView.setText(str);
            ZMAlertDialog create = new Builder(activity).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMSessionDescriptionFragment.this.onSelectMeetingNoMenuItem((MeetingNoMenuItem) zMMenuAdapter.getItem(i), str);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void onSelectMeetingNoMenuItem(@Nullable MeetingNoMenuItem meetingNoMenuItem, @Nullable String str) {
        if (meetingNoMenuItem != null && !StringUtil.isEmptyOrNull(str)) {
            switch (meetingNoMenuItem.getAction()) {
                case 0:
                    joinMeetingByNO(str);
                    break;
                case 1:
                    AndroidAppUtil.sendDial(getContext(), str);
                    break;
                case 2:
                    AndroidAppUtil.copyText(getContext(), str);
                    Toast.makeText(getContext(), getContext().getString(C4558R.string.zm_msg_link_copied_to_clipboard_91380), 0).show();
                    break;
            }
        }
    }

    private void click2CallSip(@NonNull String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (!NetworkUtil.hasDataNetwork(getContext())) {
                showSipUnavailable();
            } else if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    ZMPhoneUtils.callSip(str, null);
                }
            } else {
                this.mSelectedPhoneNumber = str;
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 1001);
            }
        }
    }

    private void showSipUnavailable() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            SimpleMessageDialog.newInstance(getString(C4558R.string.zm_sip_error_network_unavailable_99728), false).show(activity.getSupportFragmentManager(), SimpleMessageDialog.class.getSimpleName());
        }
    }

    private void joinMeetingByNO(@NonNull String str) {
        if (!NetworkUtil.hasDataNetwork(getContext())) {
            CannotJoinDialog.show((ZMActivity) getContext(), getResources().getString(C4558R.string.zm_alert_network_disconnected));
            return;
        }
        try {
            checkJoinMeeting(Long.parseLong(str));
        } catch (Exception unused) {
        }
    }

    private void checkJoinMeeting(final long j) {
        if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
            new Builder(getContext()).setCancelable(false).setTitle(C4558R.string.zm_sip_incall_start_meeting_diallog_title_85332).setMessage(C4558R.string.zm_sip_incall_start_meeting_diallog_msg_85332).setNegativeButton(C4558R.string.zm_btn_no, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    CmmSIPCallManager.getInstance().hangupAllCalls();
                    MMSessionDescriptionFragment.this.startMeeting(j);
                }
            }).create().show();
        } else {
            startMeeting(j);
        }
    }

    /* access modifiers changed from: private */
    public void startMeeting(long j) {
        Context context = getContext();
        if (context instanceof ZMActivity) {
            ConfActivity.checkExistingCallAndJoinMeeting((ZMActivity) context, j, "", "", "");
        }
    }
}
