package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.SimpleInMeetingActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.meeting.ConfParams;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMConfUtil;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import com.zipow.videobox.view.ConfChatItem;
import com.zipow.videobox.view.ConfChatListView;
import com.zipow.videobox.view.ConfChatListView.OnClickMessageListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatFragment extends ZMDialogFragment implements OnClickListener, ExtListener, OnScrollListener, OnClickMessageListener, OnEditorActionListener {
    public static final int DEFALUT_ROLE = -1;
    private static final String EXTRA_CHAT_ITEM = "EXTRA_CHAT_ITEM";
    private static final int REQUEST_CHAT_BUDDY = 10;
    private View llDisabledAlert;
    /* access modifiers changed from: private */
    public Button mBtnSend;
    private View mChatBuddyPanel;
    /* access modifiers changed from: private */
    public ConfChatListView mChatListView;
    @Nullable
    private SimpleConfUIListener mConfUIListener = new SimpleConfUIListener() {
        public boolean onUserEvent(int i, long j, int i2) {
            EventTaskManager nonNullEventTaskManagerOrThrowException = ConfChatFragment.this.getNonNullEventTaskManagerOrThrowException();
            final int i3 = i;
            final long j2 = j;
            final int i4 = i2;
            C25041 r1 = new EventAction() {
                public void run(IUIElement iUIElement) {
                    ConfChatFragment confChatFragment = (ConfChatFragment) iUIElement;
                    if (confChatFragment != null) {
                        confChatFragment.handleOnUserEvent(i3, j2, i4);
                    }
                }
            };
            nonNullEventTaskManagerOrThrowException.push(r1);
            return ConfChatFragment.this.mChatListView.onUserEvent(i, j, i2);
        }

        public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
            boolean onChatMessageReceived = ConfChatFragment.this.mChatListView.onChatMessageReceived(str, j, str2, j2, str3, str4, j3);
            if (ConfChatFragment.this.getActivity() instanceof ConfActivity) {
                ((ConfActivity) ConfChatFragment.this.getActivity()).refreshUnreadChatCount();
            }
            return onChatMessageReceived;
        }

        public boolean onConfStatusChanged2(int i, long j) {
            return ConfChatFragment.this.onConfStatusChanged2(i, j);
        }

        public boolean onUserStatusChanged(int i, long j, int i2) {
            return ConfChatFragment.this.onUserStatusChanged(i, j);
        }
    };
    @Nullable
    private ConfChatAttendeeItem mCurrentItem;
    /* access modifiers changed from: private */
    public EditText mEdtMessage;
    /* access modifiers changed from: private */
    public boolean mFlagChatBuddyPanelChanged = false;
    private LinearLayout mInputLayout;
    private boolean mIsAttendee;
    private boolean mIsWebinar;
    private TextView mTxtCurrentItem;
    private TextView mTxtDisabledAlert;

    static class MessageContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 0;
        private ConfChatItem mChatItem;

        public MessageContextMenuItem(String str, int i, ConfChatItem confChatItem) {
            super(i, str);
            this.mChatItem = confChatItem;
        }

        @Nullable
        public String getChatMessage() {
            ConfChatItem confChatItem = this.mChatItem;
            if (confChatItem == null) {
                return "";
            }
            return confChatItem.content;
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity, int i, long j) {
        if (zMActivity != null) {
            Bundle bundle = new Bundle();
            if (j != 0) {
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext == null || !confContext.isWebinar()) {
                    CmmUser userById = ConfMgr.getInstance().getUserById(j);
                    if (userById != null) {
                        bundle.putSerializable(EXTRA_CHAT_ITEM, new ConfChatAttendeeItem(userById));
                    } else {
                        return;
                    }
                } else {
                    ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(j);
                    if (zoomQABuddyByNodeId == null) {
                        CmmUser userById2 = ConfMgr.getInstance().getUserById(j);
                        if (userById2 != null) {
                            bundle.putSerializable(EXTRA_CHAT_ITEM, new ConfChatAttendeeItem(userById2));
                        } else {
                            return;
                        }
                    } else {
                        bundle.putSerializable(EXTRA_CHAT_ITEM, new ConfChatAttendeeItem(zoomQABuddyByNodeId));
                    }
                }
            }
            SimpleInMeetingActivity.show(zMActivity, ConfChatFragment.class.getName(), bundle, i, false);
        }
    }

    public static void showAsFragment(@NonNull FragmentManager fragmentManager, long j) {
        if (getConfChatFragment(fragmentManager) == null) {
            ConfChatFragmentOld confChatFragmentOld = new ConfChatFragmentOld();
            Bundle bundle = new Bundle();
            if (j != 0) {
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext == null || !confContext.isWebinar()) {
                    CmmUser userById = ConfMgr.getInstance().getUserById(j);
                    if (userById != null) {
                        bundle.putSerializable(EXTRA_CHAT_ITEM, new ConfChatAttendeeItem(userById));
                    } else {
                        return;
                    }
                } else {
                    ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(j);
                    if (zoomQABuddyByNodeId != null) {
                        bundle.putSerializable(EXTRA_CHAT_ITEM, new ConfChatAttendeeItem(zoomQABuddyByNodeId));
                    } else {
                        return;
                    }
                }
            }
            bundle.putLong("userId", j);
            confChatFragmentOld.setArguments(bundle);
            confChatFragmentOld.show(fragmentManager, ConfChatFragmentOld.class.getName());
        }
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity, int i, @Nullable ConfChatAttendeeItem confChatAttendeeItem) {
        if (zMActivity != null) {
            Bundle bundle = new Bundle();
            if (confChatAttendeeItem != null) {
                bundle.putSerializable(EXTRA_CHAT_ITEM, confChatAttendeeItem);
            }
            SimpleInMeetingActivity.show(zMActivity, ConfChatFragment.class.getName(), bundle, i, false);
        }
    }

    public static void showAsFragment(@NonNull FragmentManager fragmentManager, long j, @Nullable ConfChatAttendeeItem confChatAttendeeItem) {
        if (getConfChatFragment(fragmentManager) == null) {
            ConfChatFragmentOld confChatFragmentOld = new ConfChatFragmentOld();
            Bundle bundle = new Bundle();
            if (confChatAttendeeItem != null) {
                bundle.putSerializable(EXTRA_CHAT_ITEM, confChatAttendeeItem);
            }
            bundle.putLong("userId", j);
            confChatFragmentOld.setArguments(bundle);
            confChatFragmentOld.show(fragmentManager, ConfChatFragmentOld.class.getName());
        }
    }

    @Nullable
    public static ConfChatFragment getConfChatFragment(FragmentManager fragmentManager) {
        return (ConfChatFragment) fragmentManager.findFragmentByTag(ConfChatFragment.class.getName());
    }

    public long getUserId() {
        ConfChatAttendeeItem confChatAttendeeItem = this.mCurrentItem;
        if (confChatAttendeeItem == null) {
            return 0;
        }
        return confChatAttendeeItem.nodeID;
    }

    @SuppressLint({"NewApi"})
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_webinar_chat, viewGroup, false);
        this.mChatListView = (ConfChatListView) inflate.findViewById(C4558R.C4560id.chatListView);
        this.llDisabledAlert = inflate.findViewById(C4558R.C4560id.llDisabledAlert);
        this.mTxtDisabledAlert = (TextView) inflate.findViewById(C4558R.C4560id.txtDisabledAlert);
        this.mChatBuddyPanel = inflate.findViewById(C4558R.C4560id.chatBuddyPanel);
        this.mTxtCurrentItem = (TextView) inflate.findViewById(C4558R.C4560id.txtCurrentItem);
        this.mEdtMessage = (EditText) inflate.findViewById(C4558R.C4560id.edtMessage);
        this.mInputLayout = (LinearLayout) inflate.findViewById(C4558R.C4560id.inputLayout);
        this.mBtnSend = (Button) inflate.findViewById(C4558R.C4560id.btnSend);
        this.mTxtCurrentItem.setTextColor(getResources().getColorStateList(C4558R.color.zm_button_text_no_disable));
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = true;
        this.mIsWebinar = confContext != null && confContext.isWebinar();
        if (this.mIsWebinar) {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null && !qAComponent.isWebinarAttendee()) {
                z = false;
            }
            this.mIsAttendee = z;
        } else {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                if (myself.isHost() || myself.isCoHost()) {
                    z = false;
                }
                this.mIsAttendee = z;
            }
        }
        if (bundle != null) {
            this.mCurrentItem = (ConfChatAttendeeItem) bundle.getSerializable(EXTRA_CHAT_ITEM);
        }
        CmmConfContext confContext2 = ConfMgr.getInstance().getConfContext();
        if (confContext2 == null) {
            return null;
        }
        if (!this.mIsAttendee && this.mCurrentItem == null && !confContext2.isPrivateChatOFF()) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                this.mCurrentItem = (ConfChatAttendeeItem) arguments.getSerializable(EXTRA_CHAT_ITEM);
            }
        }
        if (this.mIsAttendee) {
            if (confContext2.isPrivateChatOFF()) {
                this.mChatBuddyPanel.setEnabled(false);
                this.mTxtCurrentItem.setEnabled(false);
                this.mTxtCurrentItem.setCompoundDrawables(null, null, null, null);
            } else {
                Bundle arguments2 = getArguments();
                if (arguments2 != null) {
                    this.mCurrentItem = (ConfChatAttendeeItem) arguments2.getSerializable(EXTRA_CHAT_ITEM);
                }
            }
            if (this.mCurrentItem == null) {
                this.mCurrentItem = ConfMgr.getInstance().getConfDataHelper().getmConfChatAttendeeItem();
            }
            judgeChatEnable();
        } else {
            this.mEdtMessage.setHint(C4558R.string.zm_webinar_txt_panelist_send_hint);
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        if (this.mCurrentItem == null) {
            this.mCurrentItem = ConfMgr.getInstance().getConfDataHelper().getmConfChatAttendeeItem();
        }
        refreshTxtCurrentItem(false);
        this.mBtnSend.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mTxtCurrentItem.setOnClickListener(this);
        this.mChatBuddyPanel.setOnClickListener(this);
        this.mChatListView.setOnScrollListener(this);
        this.mChatListView.setOnClickMessageListener(this);
        this.mChatBuddyPanel.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (!ConfChatFragment.this.mFlagChatBuddyPanelChanged) {
                    ConfChatFragment.this.refreshTxtCurrentItem(true);
                    ConfChatFragment.this.mFlagChatBuddyPanelChanged = true;
                }
            }
        });
        this.mEdtMessage.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ConfChatFragment.this.mBtnSend.setEnabled(ConfChatFragment.this.mEdtMessage.getEditableText().length() != 0);
            }
        });
        this.mEdtMessage.setOnEditorActionListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        this.mChatListView.updateUI();
        if (getActivity() instanceof ConfActivity) {
            ((ConfActivity) getActivity()).refreshUnreadChatCount();
        }
    }

    public void onPause() {
        this.mChatListView.onParentFragmentPause();
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void handleOnUserEvent(int i, long j, int i2) {
        switch (i) {
            case 0:
                if (!(!this.mIsWebinar || this.mCurrentItem.nodeID == 0 || this.mCurrentItem.nodeID == 1)) {
                    ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.mCurrentItem.nodeID);
                    if (zoomQABuddyByNodeId == null) {
                        zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByJid(this.mCurrentItem.jid);
                        if (zoomQABuddyByNodeId != null) {
                            this.mCurrentItem = new ConfChatAttendeeItem(zoomQABuddyByNodeId);
                            refreshTxtCurrentItem(false);
                        }
                    }
                    if (zoomQABuddyByNodeId != null && !zoomQABuddyByNodeId.isOfflineUser()) {
                        this.llDisabledAlert.setVisibility(8);
                        return;
                    }
                }
            case 1:
                judgeChatEnable();
                break;
        }
    }

    private void judgeChatEnable() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null || !confStatusObj.isChatDisabledByInfoBarrier()) {
            if (this.mIsAttendee) {
                ConfMgr instance = ConfMgr.getInstance();
                if (!instance.isAllowAttendeeChat()) {
                    this.llDisabledAlert.setVisibility(0);
                    this.mTxtDisabledAlert.setText(C4558R.string.zm_webinar_txt_chat_disabled_65892);
                    this.mInputLayout.setVisibility(8);
                    this.mChatBuddyPanel.setVisibility(8);
                } else {
                    this.llDisabledAlert.setVisibility(8);
                    this.mInputLayout.setVisibility(0);
                    this.mChatBuddyPanel.setVisibility(0);
                    CmmConfStatus confStatusObj2 = instance.getConfStatusObj();
                    if (confStatusObj2 != null) {
                        int attendeeChatPriviledge = confStatusObj2.getAttendeeChatPriviledge();
                        CmmUserList userList = ConfMgr.getInstance().getUserList();
                        if (userList != null) {
                            CmmUser hostUser = userList.getHostUser();
                            if (hostUser != null) {
                                if (attendeeChatPriviledge == 3) {
                                    ConfChatAttendeeItem confChatAttendeeItem = this.mCurrentItem;
                                    if (confChatAttendeeItem == null || confChatAttendeeItem.nodeID == 0 || this.mCurrentItem.nodeID == 1) {
                                        ConfChatAttendeeItem confChatAttendeeItem2 = new ConfChatAttendeeItem(hostUser.getScreenName(), null, hostUser.getNodeId(), -1);
                                        this.mCurrentItem = confChatAttendeeItem2;
                                    }
                                } else if (attendeeChatPriviledge == 2) {
                                    ConfChatAttendeeItem confChatAttendeeItem3 = this.mCurrentItem;
                                    if (confChatAttendeeItem3 == null) {
                                        ConfChatAttendeeItem confChatAttendeeItem4 = new ConfChatAttendeeItem(getString(C4558R.string.zm_webinar_txt_all_panelists), null, 1, -1);
                                        this.mCurrentItem = confChatAttendeeItem4;
                                    } else if (confChatAttendeeItem3.nodeID == 0) {
                                        this.mCurrentItem.name = getString(C4558R.string.zm_webinar_txt_all_panelists);
                                        ConfChatAttendeeItem confChatAttendeeItem5 = this.mCurrentItem;
                                        confChatAttendeeItem5.nodeID = 1;
                                        confChatAttendeeItem5.role = -1;
                                    }
                                } else if (attendeeChatPriviledge == 4) {
                                    this.llDisabledAlert.setVisibility(0);
                                    this.mTxtDisabledAlert.setText(C4558R.string.zm_webinar_txt_chat_disabled_65892);
                                    this.mInputLayout.setVisibility(8);
                                    this.mChatBuddyPanel.setVisibility(8);
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                refreshTxtCurrentItem(false);
            }
            return;
        }
        this.llDisabledAlert.setVisibility(0);
        this.mTxtDisabledAlert.setText(C4558R.string.zm_disable_in_meeting_93170);
        this.mInputLayout.setVisibility(8);
        this.mChatBuddyPanel.setVisibility(8);
    }

    public boolean onConfStatusChanged2(int i, long j) {
        if (i == 28) {
            judgeChatEnable();
        } else if (i == 161 && (j & ((long) ConfParams.InfoBarrierFieldChat)) == ((long) ConfParams.InfoBarrierFieldChat)) {
            judgeChatEnable();
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean onUserStatusChanged(int i, long j) {
        if (i != 1) {
            switch (i) {
                case 44:
                case 45:
                    break;
            }
        }
        processOnHostOrCoHostChanged();
        return true;
    }

    private void processOnHostOrCoHostChanged() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (!this.mIsWebinar && myself != null) {
            this.mIsAttendee = !myself.isHost() && !myself.isCoHost();
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (this.mIsAttendee) {
                if (confContext.isPrivateChatOFF()) {
                    this.mChatBuddyPanel.setEnabled(false);
                    this.mTxtCurrentItem.setEnabled(false);
                    this.mTxtCurrentItem.setCompoundDrawables(null, null, null, null);
                }
                judgeChatEnable();
            } else {
                this.llDisabledAlert.setVisibility(8);
                this.mInputLayout.setVisibility(0);
                this.mChatBuddyPanel.setVisibility(0);
                this.mEdtMessage.setHint(C4558R.string.zm_webinar_txt_panelist_send_hint);
            }
        }
    }

    public void onDestroy() {
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        super.onDestroy();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable(EXTRA_CHAT_ITEM, this.mCurrentItem);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 10 && i2 == -1 && intent != null) {
            ConfChatAttendeeItem confChatAttendeeItem = (ConfChatAttendeeItem) intent.getSerializableExtra(ConfChatBuddyChooseFragment.EXTRA_WEBINAR_BUDDY);
            if (confChatAttendeeItem != null) {
                this.mCurrentItem = confChatAttendeeItem;
                this.llDisabledAlert.setVisibility(8);
            }
            refreshTxtCurrentItem(false);
        }
    }

    /* access modifiers changed from: private */
    public void refreshTxtCurrentItem(boolean z) {
        if (z) {
            this.mFlagChatBuddyPanelChanged = false;
        }
        this.mTxtCurrentItem.setEnabled(true);
        this.mChatBuddyPanel.setEnabled(true);
        this.mTxtCurrentItem.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(C4558R.C4559drawable.zm_dropdown), null);
        checkResetCurrentItem();
        if (this.mCurrentItem == null) {
            if (this.mIsWebinar) {
                ConfChatAttendeeItem confChatAttendeeItem = new ConfChatAttendeeItem(getString(C4558R.string.zm_webinar_txt_all_panelists), null, 1, -1);
                this.mCurrentItem = confChatAttendeeItem;
            } else if (this.mIsAttendee) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null) {
                    if (confStatusObj.getAttendeeChatPriviledge() == 3) {
                        CmmUserList userList = ConfMgr.getInstance().getUserList();
                        if (userList != null) {
                            CmmUser hostUser = userList.getHostUser();
                            if (hostUser != null) {
                                ConfChatAttendeeItem confChatAttendeeItem2 = new ConfChatAttendeeItem(hostUser.getScreenName(), null, hostUser.getNodeId(), -1);
                                this.mCurrentItem = confChatAttendeeItem2;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        ConfChatAttendeeItem confChatAttendeeItem3 = new ConfChatAttendeeItem(getString(C4558R.string.zm_mi_everyone_122046), null, 0, -1);
                        this.mCurrentItem = confChatAttendeeItem3;
                    }
                } else {
                    return;
                }
            } else {
                ConfChatAttendeeItem confChatAttendeeItem4 = new ConfChatAttendeeItem(getString(C4558R.string.zm_mi_everyone_122046), null, 0, -1);
                this.mCurrentItem = confChatAttendeeItem4;
            }
        }
        ViewParent parent = this.mTxtCurrentItem.getParent();
        if (this.mCurrentItem.role != 0 || !(parent instanceof ViewGroup) || TextUtils.isEmpty(this.mCurrentItem.name)) {
            if (this.mCurrentItem.role == 2 || this.mCurrentItem.role == 1) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(C4558R.string.zm_webinar_txt_private_label));
                spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(C4558R.color.zm_txt_warn)), 0, spannableStringBuilder.length(), 33);
                spannableStringBuilder.append(this.mCurrentItem.name);
                this.mTxtCurrentItem.setText(spannableStringBuilder);
            } else {
                if (this.mIsAttendee) {
                    if (this.mCurrentItem.nodeID == 0) {
                        this.mEdtMessage.setHint(C4558R.string.zm_webinar_txt_attendee_send_hint_everyone);
                    } else if (this.mCurrentItem.nodeID == 1) {
                        this.mEdtMessage.setHint(C4558R.string.zm_webinar_txt_attendee_send_hint_panelist);
                    } else {
                        this.mEdtMessage.setHint(C4558R.string.zm_webinar_txt_attendee_send_hint_11380);
                        CmmUser userById = ConfMgr.getInstance().getUserById(this.mCurrentItem.nodeID);
                        if (userById != null && userById.isHost()) {
                            CmmConfStatus confStatusObj2 = ConfMgr.getInstance().getConfStatusObj();
                            if (confStatusObj2 != null) {
                                int attendeeChatPriviledge = confStatusObj2.getAttendeeChatPriviledge();
                                CmmUserList userList2 = ConfMgr.getInstance().getUserList();
                                if (this.mIsAttendee && userList2 != null && attendeeChatPriviledge == 3 && !userList2.hasCoHostUserInMeeting()) {
                                    this.mTxtCurrentItem.setEnabled(false);
                                    this.mChatBuddyPanel.setEnabled(false);
                                    this.mTxtCurrentItem.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                }
                            }
                        }
                    }
                }
                this.mTxtCurrentItem.setText(this.mCurrentItem.name);
            }
            View view = this.mChatBuddyPanel;
            StringBuilder sb = new StringBuilder();
            sb.append(getString(C4558R.string.zm_webinar_txt_send_to));
            sb.append(this.mTxtCurrentItem.getText());
            view.setContentDescription(sb.toString());
        } else {
            ViewGroup viewGroup = (ViewGroup) parent;
            if (viewGroup.getMeasuredWidth() > 0) {
                String string = getString(C4558R.string.zm_webinar_txt_label_ccPanelist, "", getString(C4558R.string.zm_webinar_txt_all_panelists));
                TextPaint paint = this.mTxtCurrentItem.getPaint();
                if (paint == null) {
                    this.mTxtCurrentItem.setText(this.mCurrentItem.name);
                    View view2 = this.mChatBuddyPanel;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(getString(C4558R.string.zm_webinar_txt_send_to));
                    sb2.append(this.mTxtCurrentItem.getText());
                    view2.setContentDescription(sb2.toString());
                    return;
                }
                int measuredWidth = viewGroup.getMeasuredWidth() - viewGroup.getPaddingRight();
                int compoundPaddingLeft = this.mTxtCurrentItem.getCompoundPaddingLeft() + this.mTxtCurrentItem.getCompoundPaddingRight();
                CharSequence ellipsize = TextUtils.ellipsize(this.mCurrentItem.name, paint, ((float) ((measuredWidth - compoundPaddingLeft) - this.mTxtCurrentItem.getLeft())) - paint.measureText(string), TruncateAt.END);
                this.mTxtCurrentItem.setText(getString(C4558R.string.zm_webinar_txt_label_ccPanelist, ellipsize, getString(C4558R.string.zm_webinar_txt_all_panelists)));
            } else {
                this.mTxtCurrentItem.setText(getString(C4558R.string.zm_webinar_txt_label_ccPanelist, this.mCurrentItem.name, getString(C4558R.string.zm_webinar_txt_all_panelists)));
            }
            View view3 = this.mChatBuddyPanel;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(getString(C4558R.string.zm_webinar_txt_send_to));
            sb3.append(this.mTxtCurrentItem.getText());
            view3.setContentDescription(sb3.toString());
        }
        if (this.mIsAttendee && this.mIsWebinar) {
            CmmConfStatus confStatusObj3 = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj3 != null && confStatusObj3.getAttendeeChatPriviledge() == 2) {
                this.mTxtCurrentItem.setEnabled(false);
                this.mChatBuddyPanel.setEnabled(false);
                this.mTxtCurrentItem.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
        if (this.mCurrentItem != null) {
            ConfMgr.getInstance().getConfDataHelper().setmConfChatAttendeeItem(this.mCurrentItem);
            this.mBtnSend.setContentDescription(this.mCurrentItem.getSendContentDescription(getActivity()));
        }
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) parent;
            viewGroup2.requestLayout();
            viewGroup2.invalidate();
        }
    }

    private void checkResetCurrentItem() {
        ConfChatAttendeeItem confChatAttendeeItem = this.mCurrentItem;
        if (confChatAttendeeItem != null && confChatAttendeeItem.nodeID == 2 && !ConfLocalHelper.canChatWithSilentModePeople()) {
            this.mCurrentItem = null;
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnSend) {
                doSend();
            } else if (id == C4558R.C4560id.btnBack) {
                UIUtil.closeSoftKeyboard(getActivity(), this.mEdtMessage);
                dismiss();
            } else if (id == C4558R.C4560id.chatBuddyPanel || id == C4558R.C4560id.txtCurrentItem) {
                ConfChatBuddyChooseFragment.showAsActivity((Fragment) this, 10);
            }
        }
    }

    private void doSend() {
        boolean z;
        String obj = this.mEdtMessage.getText().toString();
        if (!TextUtils.isEmpty(obj.trim())) {
            ConfChatAttendeeItem confChatAttendeeItem = this.mCurrentItem;
            if (confChatAttendeeItem == null || confChatAttendeeItem.nodeID != 2 || ConfLocalHelper.isHostCoHost()) {
                ConfChatAttendeeItem confChatAttendeeItem2 = this.mCurrentItem;
                if (!(confChatAttendeeItem2 == null || confChatAttendeeItem2.nodeID == 0 || this.mCurrentItem.nodeID == 2 || this.mCurrentItem.nodeID == 1 || this.mCurrentItem.nodeID == -1)) {
                    if (this.mIsWebinar) {
                        ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.mCurrentItem.nodeID);
                        if (zoomQABuddyByNodeId == null) {
                            zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByJid(this.mCurrentItem.jid);
                            if (zoomQABuddyByNodeId != null) {
                                this.mCurrentItem = new ConfChatAttendeeItem(zoomQABuddyByNodeId);
                                refreshTxtCurrentItem(false);
                            }
                        }
                        if (zoomQABuddyByNodeId == null || zoomQABuddyByNodeId.isOfflineUser()) {
                            this.llDisabledAlert.setVisibility(0);
                            this.mTxtDisabledAlert.setText(getString(C4558R.string.zm_webinar_txt_chat_attendee_not_session_11380, this.mCurrentItem.name));
                            return;
                        }
                    } else if (ConfMgr.getInstance().getUserById(this.mCurrentItem.nodeID) == null) {
                        this.llDisabledAlert.setVisibility(0);
                        this.mTxtDisabledAlert.setText(getString(C4558R.string.zm_webinar_txt_chat_attendee_not_session_11380, this.mCurrentItem.name));
                        return;
                    }
                }
                if (this.mIsAttendee) {
                    ConfChatAttendeeItem confChatAttendeeItem3 = this.mCurrentItem;
                    if (confChatAttendeeItem3 == null || confChatAttendeeItem3.nodeID == 0) {
                        z = ConfMgr.getInstance().sendChatMessageTo(0, obj, false, false, 0);
                    } else if (this.mCurrentItem.nodeID == 1) {
                        z = ConfMgr.getInstance().sendXmppChatToAllPanelists(obj);
                    } else {
                        if (!this.mIsWebinar) {
                            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                            if (confStatusObj != null && (confStatusObj.getAttendeeChatPriviledge() == 3 || confStatusObj.getAttendeeChatPriviledge() == 5)) {
                                CmmUser userById = ConfMgr.getInstance().getUserById(this.mCurrentItem.nodeID);
                                if (userById != null && !userById.isHost() && !userById.isCoHost()) {
                                    Toast makeText = Toast.makeText(getActivity(), getString(C4558R.string.zm_webinar_msg_no_permisson_11380, this.mCurrentItem.name), 1);
                                    makeText.setGravity(17, 0, 0);
                                    makeText.show();
                                    return;
                                }
                            }
                        }
                        z = ConfMgr.getInstance().sendChatMessageTo(this.mCurrentItem.nodeID, obj, false, false, 0);
                    }
                } else {
                    ConfChatAttendeeItem confChatAttendeeItem4 = this.mCurrentItem;
                    if (confChatAttendeeItem4 != null) {
                        if (confChatAttendeeItem4.nodeID == 0) {
                            z = ConfMgr.getInstance().sendChatMessageTo(0, obj, false, false, 0);
                        } else if (this.mCurrentItem.nodeID == 2) {
                            z = ConfMgr.getInstance().sendChatToSilentModeUsers(obj);
                        } else if (this.mCurrentItem.nodeID == 1) {
                            z = ConfMgr.getInstance().sendChatMessageTo(0, obj, true, false, 0);
                        } else {
                            if (this.mCurrentItem.nodeID != -1) {
                                if (this.mIsWebinar) {
                                    if (ConfMgr.getInstance().getQAComponent() != null) {
                                        ZoomQABuddy zoomQABuddyByNodeId2 = ZMConfUtil.getZoomQABuddyByNodeId(this.mCurrentItem.nodeID);
                                        if (zoomQABuddyByNodeId2 == null || zoomQABuddyByNodeId2.isOfflineUser()) {
                                            this.llDisabledAlert.setVisibility(0);
                                            this.mTxtDisabledAlert.setText(getString(C4558R.string.zm_webinar_txt_chat_attendee_not_session_11380, this.mCurrentItem.name));
                                            return;
                                        } else if (zoomQABuddyByNodeId2.getRole() == 0) {
                                            z = ConfMgr.getInstance().sendXmppChatToIndividual(obj, this.mCurrentItem.jid, true);
                                            if (z) {
                                                z = ConfMgr.getInstance().sendChatMessageTo(0, obj, true, true, this.mCurrentItem.nodeID);
                                            }
                                        } else {
                                            z = ConfMgr.getInstance().sendChatMessageTo(this.mCurrentItem.nodeID, obj, false, false, 0);
                                        }
                                    } else {
                                        return;
                                    }
                                } else if (ConfMgr.getInstance().getUserById(this.mCurrentItem.nodeID) != null) {
                                    z = ConfMgr.getInstance().sendChatMessageTo(this.mCurrentItem.nodeID, obj, false, false, 0);
                                }
                            }
                            z = false;
                        }
                    } else {
                        return;
                    }
                }
                if (z) {
                    if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
                        AccessibilityUtil.announceForAccessibilityCompat((View) this.mBtnSend, C4558R.string.zm_accessibility_sent_19147);
                    }
                    this.llDisabledAlert.setVisibility(8);
                    this.mEdtMessage.setText("");
                } else {
                    ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                    if (qAComponent != null) {
                        if (!qAComponent.isConnected()) {
                            Toast makeText2 = Toast.makeText(getActivity(), C4558R.string.zm_description_mm_msg_failed, 1);
                            makeText2.setGravity(17, 0, 0);
                            makeText2.show();
                        }
                        if (this.mIsAttendee && !this.mIsWebinar) {
                            CmmConfStatus confStatusObj2 = ConfMgr.getInstance().getConfStatusObj();
                            if (confStatusObj2 != null && confStatusObj2.getAttendeeChatPriviledge() == 3) {
                                CmmUserList userList = ConfMgr.getInstance().getUserList();
                                if (userList != null) {
                                    CmmUser hostUser = userList.getHostUser();
                                    if (hostUser != null) {
                                        ConfChatAttendeeItem confChatAttendeeItem5 = this.mCurrentItem;
                                        if (confChatAttendeeItem5 == null) {
                                            ConfChatAttendeeItem confChatAttendeeItem6 = new ConfChatAttendeeItem(hostUser.getScreenName(), null, hostUser.getNodeId(), -1);
                                            this.mCurrentItem = confChatAttendeeItem6;
                                        } else {
                                            confChatAttendeeItem5.name = hostUser.getScreenName();
                                            this.mCurrentItem.nodeID = hostUser.getNodeId();
                                            this.mCurrentItem.role = -1;
                                        }
                                        refreshTxtCurrentItem(false);
                                    } else {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                }
                return;
            }
            this.llDisabledAlert.setVisibility(0);
            this.mTxtDisabledAlert.setText(getString(C4558R.string.zm_webinar_txt_only_host_cohost_send_waiting_room_chat_122046));
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    @Nullable
    private ConfChatAttendeeItem createAttendeeInstance(@Nullable ConfChatItem confChatItem) {
        long j;
        String str;
        String str2;
        ConfChatAttendeeItem confChatAttendeeItem;
        ConfChatItem confChatItem2 = confChatItem;
        ConfChatAttendeeItem confChatAttendeeItem2 = null;
        if (confChatItem2 == null) {
            return null;
        }
        if (confChatItem2.isSelfSend) {
            String str3 = confChatItem2.receiverName;
            long j2 = confChatItem2.receiver;
            str2 = confChatItem2.receiverJid;
            switch (confChatItem2.msgType) {
                case 0:
                    str = getString(C4558R.string.zm_mi_everyone_122046);
                    j = 0;
                    break;
                case 1:
                    str = getString(C4558R.string.zm_webinar_txt_all_panelists);
                    j = 1;
                    break;
                default:
                    str = str3;
                    j = j2;
                    break;
            }
        } else {
            String str4 = confChatItem2.senderName;
            long j3 = confChatItem2.sender;
            str2 = confChatItem2.senderJid;
            str = str4;
            j = j3;
        }
        if (j == 0 || j == 1) {
            confChatAttendeeItem = new ConfChatAttendeeItem(str, null, j, -1);
        } else {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && confContext.isPrivateChatOFF()) {
                return null;
            }
            if (this.mIsWebinar) {
                ZoomQABuddy zoomQABuddyByNodeIdOrJid = ZMConfUtil.getZoomQABuddyByNodeIdOrJid(j, str2);
                if (zoomQABuddyByNodeIdOrJid == null || zoomQABuddyByNodeIdOrJid.isOfflineUser()) {
                    return null;
                }
                if (zoomQABuddyByNodeIdOrJid.getRole() == 0) {
                    confChatAttendeeItem2 = new ConfChatAttendeeItem(str, zoomQABuddyByNodeIdOrJid.getJID(), j, 0);
                } else {
                    confChatAttendeeItem2 = new ConfChatAttendeeItem(str, zoomQABuddyByNodeIdOrJid.getJID(), j, 1);
                }
            } else if (ConfMgr.getInstance().getUserById(j) != null) {
                ConfChatAttendeeItem confChatAttendeeItem3 = new ConfChatAttendeeItem(str, null, j, 1);
                confChatAttendeeItem2 = confChatAttendeeItem3;
            }
            confChatAttendeeItem = confChatAttendeeItem2;
        }
        return confChatAttendeeItem;
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 1) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtMessage);
        }
    }

    public void onClickMessage(@Nullable ConfChatItem confChatItem) {
        if (!this.mIsAttendee && confChatItem != null) {
            ConfChatAttendeeItem createAttendeeInstance = createAttendeeInstance(confChatItem);
            if (createAttendeeInstance != null) {
                this.mCurrentItem = createAttendeeInstance;
                refreshTxtCurrentItem(false);
                UIUtil.openSoftKeyboard(getActivity(), this.mEdtMessage);
            }
        }
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        doSend();
        return true;
    }

    public void onLongClickMessage(ConfChatItem confChatItem) {
        showMessageContextMenu(confChatItem);
    }

    public void showMessageContextMenu(ConfChatItem confChatItem) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null || confContext.canCopyChatContent()) {
                zMMenuAdapter.addItem(new MessageContextMenuItem(activity.getString(C4558R.string.zm_mm_lbl_copy_message), 0, confChatItem));
            }
            if (zMMenuAdapter.getCount() > 0) {
                ZMAlertDialog create = new Builder(activity).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConfChatFragment.this.onSelectContextMenuItem((MessageContextMenuItem) zMMenuAdapter.getItem(i));
                    }
                }).create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(MessageContextMenuItem messageContextMenuItem) {
        if (messageContextMenuItem.getAction() == 0) {
            String chatMessage = messageContextMenuItem.getChatMessage();
            if (!StringUtil.isEmptyOrNull(chatMessage)) {
                AndroidAppUtil.copyText(getActivity(), chatMessage);
            }
        }
    }
}
