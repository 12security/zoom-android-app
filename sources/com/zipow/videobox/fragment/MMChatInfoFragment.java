package com.zipow.videobox.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.MMChatInfoActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.StarredMessageActivity;
import com.zipow.videobox.eventbus.ZMAlertAvailable;
import com.zipow.videobox.eventbus.ZMChatSession;
import com.zipow.videobox.fragment.AddrBookItemDetailsFragment.BlockFragment;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.MakeGroupResult;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.AlertWhenAvailableHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.RoundedBackgroundSpan;
import com.zipow.videobox.view.p014mm.MMBuddyItem;
import com.zipow.videobox.view.p014mm.MMSessionContentsFragment;
import com.zipow.videobox.view.p014mm.MMSessionDescriptionFragment;
import com.zipow.videobox.view.p014mm.MMSessionMembersListFragment;
import com.zipow.videobox.view.p014mm.MMSessionMoreOptionsFragment;
import com.zipow.videobox.view.p014mm.MMSessionNotificationsFragment;
import com.zipow.videobox.view.p014mm.MMSetGroupInformationFragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MMChatInfoFragment extends ZMDialogFragment implements OnClickListener, IABContactsCacheListener {
    private static final String ARG_BUDDY_ID = "buddyId";
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_GROUP_ID = "groupId";
    private static final String ARG_IS_GROUP = "isGroup";
    public static int MAX = 45;
    private static final int REQUEST_CHAT_INFO = 102;
    private static final int REQUEST_COPY_BUDDY_CONTACT_GROUP = 104;
    private static final int REQUEST_ONE_ON_ONE_BUDDY_DETAILS = 100;
    private static final String TAG = "MMChatInfoFragment";
    private AvatarView mAvatarView;
    private Button mBtnBack;
    private View mBtnClearHistory;
    private ImageView mBtnStared;
    /* access modifiers changed from: private */
    @Nullable
    public String mBuddyId;
    /* access modifiers changed from: private */
    public CheckedTextView mChkAlertAvailable;
    private CheckedTextView mChkBlockUser;
    private CheckedTextView mChkNotification;
    private CheckedTextView mChkSaveSession;
    private CheckedTextView mChkUnreadCount;
    private TextView mCustomMsgTxt;
    private String mDesc;
    private ImageView mDescriptionArrowImg;
    private TextView mDescriptionInfoTxt;
    private View mDescriptionLayout;
    @Nullable
    private String mGroupId;
    private View mGroupNotificationLayout;
    private int mGroupNotificationType;
    private TextView mGroupNotificayionInfoTxt;
    private View mGroupTopicAndDescPanel;
    @Nullable
    private IMAddrBookItem mIMAddrBookItem;
    private ImageView mImgTopicArrow;
    private boolean mIsE2EDesc;
    private boolean mIsGroup = false;
    private boolean mIsNotes = false;
    private boolean mIsRobot = false;
    private TextView mLblGroupInfo;
    @Nullable
    private MMBuddyItem mMMBuddyItem;
    private View mMembersCountLayout;
    private TextView mMembersCountTxt;
    private View mMembersInviteLayout;
    private View mMoreOptionsLayout;
    @NonNull
    private INotificationSettingUIListener mNotificationSettingUI = new SimpleNotificationSettingUIListener() {
        public void OnMUCSettingUpdated() {
            MMChatInfoFragment.this.updateData();
        }

        public void OnChannelsUnreadBadgeSettingUpdated() {
            MMChatInfoFragment.this.updateData();
        }
    };
    private View mOneChatInfoLayout;
    private View mOneChatInfoPanel;
    private View mOneChatInviteLayout;
    private View mOneChatOptionsPanel;
    private View mOptionBlockUser;
    private View mOptionCopyGroup;
    private View mOptionNotification;
    private View mOptionSaveSession;
    private View mOptionShareFiles;
    private View mOptionShareImages;
    private View mOptionStarredMessage;
    private View mOptionTopic;
    private View mPanelAlertAvailable;
    private View mPanelMembers;
    private View mPanelMoreOptions;
    private View mPanelShareFiles;
    private TextView mScreenNameTxt;
    @Nullable
    private String mSessionId;
    private TextView mTxtClearHistory;
    private TextView mTxtTitle;
    private TextView mTxtTopic;
    private View mTxtUnreadMessageCount;
    private TextView mUnreadLabel;
    private View mUnreadNotificationPanel;
    @Nullable
    private ZMDialogFragment mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            MMChatInfoFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMChatInfoFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMChatInfoFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void Indicate_BlockedUsersUpdated() {
            MMChatInfoFragment.this.Indicate_BlockedUsersUpdated();
        }

        public void Indicate_BlockedUsersAdded(List<String> list) {
            MMChatInfoFragment.this.Indicate_BlockedUsersUpdated();
        }

        public void Indicate_BlockedUsersRemoved(List<String> list) {
            MMChatInfoFragment.this.Indicate_BlockedUsersUpdated();
        }

        public void notifyStarSessionDataUpdate() {
            MMChatInfoFragment.this.updateStarBtn();
        }

        public void Indicate_AvailableAlert(String str, String str2) {
            if (StringUtil.isSameString(str, MMChatInfoFragment.this.mBuddyId)) {
                MMChatInfoFragment.this.mChkAlertAvailable.setChecked(false);
            }
        }

        public void Indicate_BuddyAccountStatusChange(String str, int i) {
            if (i == 2 || i == 3) {
                FragmentActivity activity = MMChatInfoFragment.this.getActivity();
                if (activity != null) {
                    activity.setResult(0);
                    activity.finish();
                }
            }
        }

        public void On_AssignGroupAdmins(int i, String str, String str2, List<String> list, long j) {
            MMChatInfoFragment.this.On_AssignGroupAdmins(i, str, str2, list, j);
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            MMChatInfoFragment.this.On_DestroyGroup(i, str, str2, str3, j);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMChatInfoFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    public static void showAsOneToOneInActivity(@Nullable ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem, @Nullable String str) {
        if (zMActivity != null && str != null) {
            MMChatInfoFragment mMChatInfoFragment = new MMChatInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_CONTACT, iMAddrBookItem);
            bundle.putString(ARG_BUDDY_ID, str);
            bundle.putBoolean(ARG_IS_GROUP, false);
            mMChatInfoFragment.setArguments(bundle);
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, mMChatInfoFragment, MMChatInfoFragment.class.getName()).commit();
        }
    }

    public static void showAsGroupChatInActivity(@Nullable ZMActivity zMActivity, @Nullable String str) {
        if (zMActivity != null && str != null) {
            MMChatInfoFragment mMChatInfoFragment = new MMChatInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_GROUP_ID, str);
            bundle.putBoolean(ARG_IS_GROUP, true);
            mMChatInfoFragment.setArguments(bundle);
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, mMChatInfoFragment, MMChatInfoFragment.class.getName()).commit();
        }
    }

    @Nullable
    public static MMChatInfoFragment findMMChatInfoFragment(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        return (MMChatInfoFragment) fragmentManager.findFragmentByTag(MMChatInfoFragment.class.getName());
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        this.mIsGroup = arguments.getBoolean(ARG_IS_GROUP);
        this.mIMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
        this.mBuddyId = arguments.getString(ARG_BUDDY_ID);
        this.mGroupId = arguments.getString(ARG_GROUP_ID);
        this.mSessionId = this.mIsGroup ? this.mGroupId : this.mBuddyId;
        this.mIsNotes = UIMgr.isMyNotes(this.mSessionId);
        IMAddrBookItem iMAddrBookItem = this.mIMAddrBookItem;
        if (iMAddrBookItem != null) {
            this.mIsRobot = iMAddrBookItem.getIsRobot();
        }
    }

    public void onStart() {
        super.onStart();
        updateStarBtn();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_chat_info, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnStared = (ImageView) inflate.findViewById(C4558R.C4560id.starredBtn);
        this.mGroupTopicAndDescPanel = inflate.findViewById(C4558R.C4560id.topic_and_desc_panel);
        this.mOptionTopic = inflate.findViewById(C4558R.C4560id.optionTopic);
        this.mLblGroupInfo = (TextView) inflate.findViewById(C4558R.C4560id.lblGroupInfo);
        this.mTxtTopic = (TextView) inflate.findViewById(C4558R.C4560id.txtTopic);
        this.mImgTopicArrow = (ImageView) inflate.findViewById(C4558R.C4560id.imgTopicArrow);
        this.mDescriptionLayout = inflate.findViewById(C4558R.C4560id.description_layout);
        this.mDescriptionInfoTxt = (TextView) inflate.findViewById(C4558R.C4560id.description_info_tv);
        this.mDescriptionArrowImg = (ImageView) inflate.findViewById(C4558R.C4560id.imgDescriptionArrow);
        this.mPanelMembers = inflate.findViewById(C4558R.C4560id.panelMembers);
        this.mMembersCountLayout = inflate.findViewById(C4558R.C4560id.members_count_layout);
        this.mMembersCountTxt = (TextView) inflate.findViewById(C4558R.C4560id.members_count_tv);
        this.mMembersInviteLayout = inflate.findViewById(C4558R.C4560id.members_invite_layout);
        this.mOptionShareImages = inflate.findViewById(C4558R.C4560id.optionShareImages);
        this.mOptionShareFiles = inflate.findViewById(C4558R.C4560id.optionShareFiles);
        this.mPanelShareFiles = inflate.findViewById(C4558R.C4560id.panelShareFiles);
        this.mOptionStarredMessage = inflate.findViewById(C4558R.C4560id.optionStarredMessage);
        this.mUnreadLabel = (TextView) inflate.findViewById(C4558R.C4560id.unreadLabel);
        this.mUnreadNotificationPanel = inflate.findViewById(C4558R.C4560id.unread_and_notification);
        this.mTxtUnreadMessageCount = inflate.findViewById(C4558R.C4560id.txtUnreadMessageCount);
        this.mChkUnreadCount = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkUnreadCount);
        this.mGroupNotificationLayout = inflate.findViewById(C4558R.C4560id.notification_layout);
        this.mGroupNotificayionInfoTxt = (TextView) inflate.findViewById(C4558R.C4560id.group_notification_info_tv);
        this.mPanelMoreOptions = inflate.findViewById(C4558R.C4560id.panelMoreOptions);
        this.mMoreOptionsLayout = inflate.findViewById(C4558R.C4560id.optionMoreOptions);
        this.mOneChatInfoPanel = inflate.findViewById(C4558R.C4560id.one_chat_info_panel);
        this.mOneChatInfoLayout = inflate.findViewById(C4558R.C4560id.one_chat_info_layout);
        this.mAvatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        this.mScreenNameTxt = (TextView) inflate.findViewById(C4558R.C4560id.txtScreenName);
        this.mCustomMsgTxt = (TextView) inflate.findViewById(C4558R.C4560id.txtCustomMessage);
        this.mOneChatInviteLayout = inflate.findViewById(C4558R.C4560id.one_chat_invite_layout);
        this.mOneChatOptionsPanel = inflate.findViewById(C4558R.C4560id.one_chat_option_panel);
        this.mChkNotification = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkNotification);
        this.mOptionNotification = inflate.findViewById(C4558R.C4560id.optionNotification);
        this.mPanelAlertAvailable = inflate.findViewById(C4558R.C4560id.panelAlertAvailable);
        this.mChkAlertAvailable = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAlertAvailable);
        this.mOptionBlockUser = inflate.findViewById(C4558R.C4560id.optionBlockUser);
        this.mChkBlockUser = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkBlockUser);
        this.mBtnClearHistory = inflate.findViewById(C4558R.C4560id.btnClearHistory);
        this.mTxtClearHistory = (TextView) inflate.findViewById(C4558R.C4560id.txtClearHistory);
        this.mOptionCopyGroup = inflate.findViewById(C4558R.C4560id.optionCopyGroup);
        this.mOptionSaveSession = inflate.findViewById(C4558R.C4560id.optionSaveSession);
        this.mChkSaveSession = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkSaveSession);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnStared.setOnClickListener(this);
        this.mOptionTopic.setOnClickListener(this);
        this.mBtnClearHistory.setOnClickListener(this);
        this.mOptionNotification.setOnClickListener(this);
        this.mOptionSaveSession.setOnClickListener(this);
        this.mOptionShareImages.setOnClickListener(this);
        this.mOptionShareFiles.setOnClickListener(this);
        this.mPanelAlertAvailable.setOnClickListener(this);
        this.mOptionBlockUser.setOnClickListener(this);
        this.mOptionStarredMessage.setOnClickListener(this);
        this.mChkUnreadCount.setOnClickListener(this);
        this.mOptionCopyGroup.setOnClickListener(this);
        this.mOneChatInfoLayout.setOnClickListener(this);
        this.mOneChatInviteLayout.setOnClickListener(this);
        this.mDescriptionLayout.setOnClickListener(this);
        this.mMembersCountLayout.setOnClickListener(this);
        this.mMembersInviteLayout.setOnClickListener(this);
        this.mGroupNotificationLayout.setOnClickListener(this);
        this.mMoreOptionsLayout.setOnClickListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        EventBus.getDefault().register(this);
        return inflate;
    }

    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlertAvailableEvent(@NonNull ZMAlertAvailable zMAlertAvailable) {
        if (StringUtil.isSameString(this.mBuddyId, zMAlertAvailable.getJid())) {
            updateAlertWhenAvailable();
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null && intent.getBooleanExtra(AddrBookItemDetailsFragment.RESULT_EXTRA_BACK_TO_CHAT, false)) {
            onClickBtnBack();
        }
        if (i == 104 && i2 == -1) {
            onCopyBuddyInCustomGroup(intent);
        }
    }

    private void onCopyBuddyInCustomGroup(@Nullable Intent intent) {
        if (intent != null && this.mIMAddrBookItem != null) {
            MMZoomBuddyGroup mMZoomBuddyGroup = (MMZoomBuddyGroup) intent.getSerializableExtra(SelectCustomGroupFragment.RESULT_GROUP);
            if (mMZoomBuddyGroup != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(this.mIMAddrBookItem.getJid());
                    zoomMessenger.addBuddyToPersonalBuddyGroup(arrayList, mMZoomBuddyGroup.getXmppGroupID());
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        getDesc();
        updateData();
        ABContactsCache.getInstance().addListener(this);
        if (ABContactsCache.getInstance().needReloadAll()) {
            ABContactsCache.getInstance().reloadAllContacts();
        }
        if (this.mIsGroup) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null && !groupById.amIInGroup()) {
                    onClickBtnBack();
                }
            }
        }
        NotificationSettingUI.getInstance().addListener(this.mNotificationSettingUI);
    }

    /* access modifiers changed from: private */
    public void updateData() {
        updateContact();
        updateTitle();
        updateUnreadLable();
        updateBasicInfo();
        updateOptions();
    }

    private void updateAlertWhenAvailable() {
        if (!AlertWhenAvailableHelper.getInstance().showAlertWhenAvailable(this.mBuddyId) || StringUtil.isEmptyOrNull(this.mBuddyId)) {
            this.mPanelAlertAvailable.setVisibility(8);
            return;
        }
        this.mPanelAlertAvailable.setVisibility(0);
        this.mChkAlertAvailable.setChecked(AlertWhenAvailableHelper.getInstance().isInAlertQueen(this.mBuddyId));
    }

    private void updateContact() {
        if (!this.mIsGroup) {
            IMAddrBookItem iMAddrBookItem = this.mIMAddrBookItem;
            if (iMAddrBookItem != null && iMAddrBookItem.getPhoneNumberCount() > 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mIMAddrBookItem.getJid());
                    if (buddyWithJID != null) {
                        zoomMessenger.refreshBuddyVCard(buddyWithJID.getJid(), true);
                        this.mIMAddrBookItem = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    }
                }
            }
        }
    }

    private void updateTitle() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                if (this.mIsGroup) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                    if (groupById != null) {
                        groupById.getBuddyCount();
                        this.mTxtTitle.setText(activity.getString(groupById.isRoom() ? C4558R.string.zm_mm_title_session_channel_108993 : C4558R.string.zm_mm_title_session_muc_108993));
                    }
                } else if (this.mIsNotes) {
                    this.mTxtTitle.setText(activity.getString(C4558R.string.zm_mm_my_notes_title_chat_options_62453));
                } else {
                    this.mTxtTitle.setText(activity.getString(C4558R.string.zm_mm_title_session_muc_108993));
                }
            }
        }
    }

    private void updateBasicInfo() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int i = 8;
            if (this.mIsGroup) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                    if (groupById != null) {
                        this.mOneChatInfoPanel.setVisibility(8);
                        if (groupById.isRoom()) {
                            this.mLblGroupInfo.setText(activity.getString(C4558R.string.zm_mm_lbl_channel_name_108993));
                        } else {
                            this.mLblGroupInfo.setText(activity.getString(C4558R.string.zm_mm_lbl_muc_name_108993));
                        }
                        String groupName = groupById.getGroupName();
                        if (StringUtil.isEmptyOrNull(groupName)) {
                            this.mTxtTopic.setText(activity.getString(C4558R.string.zm_mm_lbl_not_set));
                        } else {
                            this.mTxtTopic.setText(groupName);
                        }
                        this.mDescriptionInfoTxt.setText(this.mDesc);
                        this.mImgTopicArrow.setVisibility(0);
                        this.mDescriptionArrowImg.setVisibility(0);
                        this.mGroupTopicAndDescPanel.setVisibility(0);
                        View view = this.mDescriptionLayout;
                        if (!ZMIMUtils.isAnnouncement(this.mSessionId)) {
                            i = 0;
                        }
                        view.setVisibility(i);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                this.mGroupTopicAndDescPanel.setVisibility(8);
                if (this.mIsNotes) {
                    this.mOneChatInfoPanel.setVisibility(8);
                } else {
                    updateBuddyInfo();
                    this.mOneChatInfoPanel.setVisibility(0);
                }
            }
            updateGroupMembers();
        }
    }

    private void getDesc() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && this.mIsGroup && getActivity() != null) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
            if (groupById != null) {
                this.mDesc = groupById.getGroupDesc();
            }
        }
    }

    private boolean isE2EDesc() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (zoomMessenger.e2eGetMyOption() == 2) {
            return true;
        }
        if (this.mIsGroup) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
            if (groupById != null) {
                return groupById.isForceE2EGroup();
            }
        }
        return false;
    }

    private void updateBuddyInfo() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
            if (buddyWithJID != null) {
                this.mMMBuddyItem = new MMBuddyItem(buddyWithJID, IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                if (this.mMMBuddyItem.getLocalContact() != null) {
                    this.mAvatarView.show(this.mMMBuddyItem.getLocalContact().getAvatarParamsBuilder());
                } else {
                    ParamsBuilder paramsBuilder = new ParamsBuilder();
                    paramsBuilder.setPath(this.mMMBuddyItem.getAvatar()).setName(this.mMMBuddyItem.getScreenName(), this.mMMBuddyItem.getBuddyJid());
                    this.mAvatarView.show(paramsBuilder);
                }
                this.mScreenNameTxt.setText(this.mMMBuddyItem.getScreenName());
                int i = 0;
                if (this.mMMBuddyItem.getLocalContact() == null || TextUtils.isEmpty(this.mMMBuddyItem.getLocalContact().getSignature())) {
                    this.mCustomMsgTxt.setVisibility(8);
                } else {
                    this.mCustomMsgTxt.setText(this.mMMBuddyItem.getLocalContact().getSignature());
                    this.mCustomMsgTxt.setVisibility(0);
                }
                View view = this.mOneChatInviteLayout;
                if (this.mIsRobot) {
                    i = 8;
                }
                view.setVisibility(i);
            }
        }
    }

    private void updateGroupMembers() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!this.mIsGroup || ZMIMUtils.isAnnouncement(this.mSessionId)) {
                this.mPanelMembers.setVisibility(8);
            } else {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                    if (groupById != null) {
                        int buddyCount = groupById.getBuddyCount();
                        this.mMembersCountTxt.setText(activity.getString(C4558R.string.zm_mm_lbl_group_members_count_108993, new Object[]{Integer.valueOf(buddyCount)}));
                        this.mPanelMembers.setVisibility(0);
                    }
                }
            }
        }
    }

    private void updateOptions() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            this.mChkSaveSession.setChecked(zoomMessenger.savedSessionIsSaved(this.mSessionId));
            boolean checkE2EStatus = checkE2EStatus();
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            boolean z = zoomFileContentMgr != null && zoomFileContentMgr.getFileContentMgmtOption() == 1;
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                List disableMUCSettings = notificationSettingMgr.getDisableMUCSettings();
                List hLMUCSettings = notificationSettingMgr.getHLMUCSettings();
                List receiveAllMUCSettings = notificationSettingMgr.getReceiveAllMUCSettings();
                if (this.mIsGroup) {
                    this.mOneChatInfoPanel.setVisibility(8);
                    this.mOneChatOptionsPanel.setVisibility(8);
                    this.mUnreadNotificationPanel.setVisibility(0);
                    this.mPanelMoreOptions.setVisibility(0);
                    this.mChkUnreadCount.setChecked(notificationSettingMgr.sessionShowUnreadBadge(this.mSessionId));
                    if (disableMUCSettings != null && disableMUCSettings.contains(this.mGroupId)) {
                        this.mGroupNotificationType = 2;
                        this.mGroupNotificayionInfoTxt.setText(getString(C4558R.string.zm_lbl_notification_nothing_19898));
                    } else if (hLMUCSettings != null && hLMUCSettings.contains(this.mGroupId)) {
                        this.mGroupNotificationType = 1;
                        this.mGroupNotificayionInfoTxt.setText(getString(C4558R.string.zm_lbl_notification_private_msg_in_group_19898));
                    } else if (receiveAllMUCSettings == null || !receiveAllMUCSettings.contains(this.mGroupId)) {
                        int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
                        if (blockAllSettings == null) {
                            this.mGroupNotificationType = 0;
                            this.mGroupNotificayionInfoTxt.setText(getString(C4558R.string.zm_lbl_notification_all_msg_19898));
                        } else {
                            int i = blockAllSettings[0];
                            int i2 = blockAllSettings[1];
                            if (i == 1 && i2 == 1) {
                                this.mGroupNotificationType = 0;
                                this.mGroupNotificayionInfoTxt.setText(getString(C4558R.string.zm_lbl_notification_all_msg_19898));
                            } else if (i == 2) {
                                this.mGroupNotificationType = 2;
                                this.mGroupNotificayionInfoTxt.setText(getString(C4558R.string.zm_lbl_notification_nothing_19898));
                            } else if (i == 1 && i2 == 4) {
                                this.mGroupNotificationType = 1;
                                this.mGroupNotificayionInfoTxt.setText(getString(C4558R.string.zm_lbl_notification_private_msg_in_group_19898));
                            }
                        }
                    } else {
                        this.mGroupNotificationType = 0;
                        this.mGroupNotificayionInfoTxt.setText(getString(C4558R.string.zm_lbl_notification_all_msg_19898));
                    }
                } else {
                    this.mUnreadNotificationPanel.setVisibility(8);
                    this.mPanelMoreOptions.setVisibility(8);
                    this.mOneChatOptionsPanel.setVisibility(0);
                    if (this.mIsNotes) {
                        this.mOptionBlockUser.setVisibility(8);
                        this.mOptionNotification.setVisibility(8);
                        this.mOptionCopyGroup.setVisibility(8);
                    } else {
                        this.mOptionBlockUser.setVisibility(0);
                        if (disableMUCSettings == null || !disableMUCSettings.contains(this.mSessionId)) {
                            this.mOptionNotification.setVisibility(8);
                        } else {
                            this.mChkNotification.setChecked(false);
                            this.mOptionNotification.setVisibility(0);
                        }
                        this.mChkBlockUser.setChecked(zoomMessenger.blockUserIsBlocked(this.mBuddyId));
                        if (zoomMessenger.personalGroupGetOption() == 1) {
                            this.mOptionCopyGroup.setVisibility(0);
                        } else {
                            this.mOptionCopyGroup.setVisibility(8);
                        }
                    }
                }
                if (checkE2EStatus || !z || PTApp.getInstance().isFileTransferDisabled() || this.mIsRobot) {
                    this.mOptionShareImages.setVisibility(8);
                    this.mOptionShareFiles.setVisibility(8);
                } else {
                    this.mOptionShareImages.setVisibility(0);
                    this.mOptionShareFiles.setVisibility(0);
                }
                if (!checkE2EStatus) {
                    this.mOptionStarredMessage.setVisibility(0);
                } else if (!this.mIsGroup || checkE2EGroup()) {
                    this.mOptionStarredMessage.setVisibility(8);
                } else {
                    this.mOptionStarredMessage.setVisibility(0);
                }
                if (this.mOptionShareImages.getVisibility() == 8 && this.mOptionShareFiles.getVisibility() == 8 && this.mOptionStarredMessage.getVisibility() == 8) {
                    this.mPanelShareFiles.setVisibility(8);
                } else {
                    this.mPanelShareFiles.setVisibility(0);
                }
                updateAlertWhenAvailable();
            }
        }
    }

    private boolean checkE2EGroup() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && this.mIsGroup) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
            if (groupById != null) {
                return groupById.isForceE2EGroup();
            }
        }
        return false;
    }

    private boolean checkE2EStatus() {
        boolean z = false;
        if (this.mIsNotes) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        int e2eGetMyOption = zoomMessenger.e2eGetMyOption();
        if (e2eGetMyOption == 2) {
            return true;
        }
        if (this.mIsGroup) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
            if (groupById != null) {
                z = groupById.isForceE2EGroup();
            }
        } else {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
            if (buddyWithJID != null && buddyWithJID.getE2EAbility(e2eGetMyOption) == 2) {
                z = true;
            }
        }
        return z;
    }

    private void updateUnreadLable() {
        if (this.mIsGroup) {
            String string = getString(C4558R.string.zm_lbl_show_unread_msg_58475);
            int indexOf = string.indexOf("%%");
            if (indexOf >= 0) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string.subSequence(0, indexOf));
                spannableStringBuilder.append(" 1 ");
                spannableStringBuilder.append(string.substring(indexOf + 2));
                spannableStringBuilder.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getContext(), C4558R.color.zm_pure_red), ContextCompat.getColor(getContext(), C4558R.color.zm_white)), indexOf, indexOf + 3, 33);
                this.mUnreadLabel.setText(spannableStringBuilder);
            }
        }
    }

    private void updateUnreadCount() {
        if (!this.mIsGroup) {
            this.mUnreadNotificationPanel.setVisibility(8);
            return;
        }
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            this.mChkUnreadCount.setChecked(notificationSettingMgr.sessionShowUnreadBadge(this.mSessionId));
        }
    }

    public void onContactsCacheUpdated() {
        updateContact();
        if (!this.mIsGroup) {
            updateBuddyInfo();
        }
    }

    public void onPause() {
        super.onPause();
        ABContactsCache.getInstance().removeListener(this);
        NotificationSettingUI.getInstance().removeListener(this.mNotificationSettingUI);
    }

    public void onDestroy() {
        super.onDestroy();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: private */
    public void onGroupAction(final int i, final GroupAction groupAction, String str) {
        if (!this.mIsGroup || StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    if (groupAction.getActionType() == 1) {
                        if (!StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                            if (isResumed()) {
                                updateBasicInfo();
                            }
                            return;
                        }
                        getNonNullEventTaskManagerOrThrowException().pushLater(new EventAction("GroupAction.ACTION_MODIFY_NAME") {
                            public void run(IUIElement iUIElement) {
                                MMChatInfoFragment mMChatInfoFragment = (MMChatInfoFragment) iUIElement;
                                if (mMChatInfoFragment != null) {
                                    mMChatInfoFragment.handleGroupActionModifyName(i, groupAction);
                                }
                            }
                        });
                    } else if (groupAction.getActionType() == 0) {
                        if (StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                            getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_MAKE_GROUP") {
                                public void run(IUIElement iUIElement) {
                                    MMChatInfoFragment mMChatInfoFragment = (MMChatInfoFragment) iUIElement;
                                    if (mMChatInfoFragment != null) {
                                        mMChatInfoFragment.handleGroupActionMakeGroup(i, groupAction);
                                    }
                                }
                            });
                        } else {
                            return;
                        }
                    } else if (groupAction.getActionType() == 3) {
                        if (!StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                            if (isResumed()) {
                                updateGroupMembers();
                            }
                            return;
                        }
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_ADD_BUDDIES") {
                            public void run(IUIElement iUIElement) {
                                MMChatInfoFragment mMChatInfoFragment = (MMChatInfoFragment) iUIElement;
                                if (mMChatInfoFragment != null) {
                                    mMChatInfoFragment.handleGroupActionAddBuddies(i, groupAction);
                                }
                            }
                        });
                    } else if (groupAction.getActionType() == 4) {
                        if (!StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                            if (groupAction.isMeInBuddies()) {
                                finishFragment(true);
                            } else if (isResumed()) {
                                updateGroupMembers();
                            }
                            return;
                        }
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_REMOVE_BUDDY") {
                            public void run(IUIElement iUIElement) {
                                MMChatInfoFragment mMChatInfoFragment = (MMChatInfoFragment) iUIElement;
                                if (mMChatInfoFragment != null) {
                                    mMChatInfoFragment.handleGroupActionRemoveBuddy(i, groupAction);
                                }
                            }
                        });
                    } else if (groupAction.getActionType() == 2 || groupAction.getActionType() == 5) {
                        if (!StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                            if (isResumed()) {
                                updateGroupMembers();
                            }
                            return;
                        }
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_DELETE_GROUP") {
                            public void run(IUIElement iUIElement) {
                                MMChatInfoFragment mMChatInfoFragment = (MMChatInfoFragment) iUIElement;
                                if (mMChatInfoFragment != null) {
                                    mMChatInfoFragment.handleGroupActionDeleteGroup(i, groupAction);
                                }
                            }
                        });
                    }
                    if (groupAction.getGroupDescAction() != 0 && isResumed()) {
                        getDesc();
                        updateData();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        if (!this.mIsGroup && StringUtil.isSameString(str, this.mBuddyId)) {
            updateBuddyInfo();
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (this.mIsGroup && StringUtil.isSameString(str, this.mGroupId)) {
            updateData();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersUpdated() {
        updateData();
    }

    /* access modifiers changed from: private */
    public void On_AssignGroupAdmins(int i, String str, String str2, @Nullable List<String> list, long j) {
        if (this.mIsGroup && StringUtil.isSameString(str2, this.mGroupId)) {
            updateData();
        }
    }

    /* access modifiers changed from: private */
    public void On_DestroyGroup(final int i, String str, String str2, String str3, long j) {
        if (StringUtil.isSameString(str2, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("DestroyGroup") {
                public void run(IUIElement iUIElement) {
                    MMChatInfoFragment.this.dismissWaitingDialog();
                    ZMActivity zMActivity = (ZMActivity) MMChatInfoFragment.this.getActivity();
                    if (i == 0 && (zMActivity instanceof MMChatInfoActivity)) {
                        ((MMChatInfoActivity) zMActivity).onQuitGroup();
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
                    ZMActivity zMActivity = (ZMActivity) MMChatInfoFragment.this.getActivity();
                    if (zMActivity instanceof MMChatInfoActivity) {
                        ((MMChatInfoActivity) zMActivity).onQuitGroup();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionModifyName(int i, GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            updateBasicInfo();
        } else {
            showChangeTopicFailureMessage(i);
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionAddBuddies(int i, @NonNull GroupAction groupAction) {
        int i2;
        Object[] objArr;
        dismissWaitingDialog();
        if (i == 0) {
            if (groupAction.getBuddyNotAllowReason() == 0) {
                List notAllowBuddies = groupAction.getNotAllowBuddies();
                if (notAllowBuddies != null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i3 = 0; i3 < notAllowBuddies.size(); i3++) {
                            if (!StringUtil.isEmptyOrNull((String) notAllowBuddies.get(i3))) {
                                sb.append((String) notAllowBuddies.get(i3));
                                sb.append(PreferencesConstants.COOKIE_DELIMITER);
                            }
                        }
                        if (sb.length() > 0) {
                            String substring = sb.substring(0, sb.length() - 1);
                            ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                            if (groupById != null) {
                                FragmentActivity activity = getActivity();
                                if (groupById.isRoom()) {
                                    i2 = C4558R.string.zm_mm_msg_add_buddies_not_allowed_59554;
                                    objArr = new Object[]{substring};
                                } else {
                                    i2 = C4558R.string.zm_mm_chat_msg_add_buddies_not_allowed_108993;
                                    objArr = new Object[]{substring};
                                }
                                Toast.makeText(activity, getString(i2, objArr), 1).show();
                            } else {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
            updateGroupMembers();
        } else {
            showAddBuddiesFailureMessage(i, groupAction);
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionRemoveBuddy(int i, GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            updateGroupMembers();
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionDeleteGroup(int i, GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity instanceof MMChatInfoActivity) {
                ((MMChatInfoActivity) zMActivity).onQuitGroup();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionMakeGroup(int i, GroupAction groupAction) {
        dismissWaitingDialog();
        String groupId = groupAction.getGroupId();
        if (i == 0) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null && !StringUtil.isEmptyOrNull(groupId)) {
                startGroupChat(zMActivity, groupId);
            }
        } else {
            showMakeGroupFailureMessage(i, groupAction);
        }
    }

    private static void startGroupChat(ZMActivity zMActivity, String str) {
        zMActivity.finish();
        MMChatActivity.showAsGroupChat(zMActivity, str);
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mBtnStared) {
            onClickStarredBtn();
        } else if (view == this.mOptionTopic) {
            onClickOptionTopic();
        } else if (view == this.mDescriptionLayout) {
            onClickOptionDescription();
        } else if (view == this.mMembersCountLayout) {
            onClickOptionMembersCount();
        } else if (view == this.mMembersInviteLayout || view == this.mOneChatInviteLayout) {
            onClickOptionInvite();
        } else if (view == this.mOneChatInfoLayout) {
            MMBuddyItem mMBuddyItem = this.mMMBuddyItem;
            if (mMBuddyItem != null) {
                onClickBuddyItem(mMBuddyItem);
            }
        } else if (view == this.mBtnClearHistory) {
            onClickBtnClearHistory();
        } else if (view == this.mOptionNotification) {
            onClickChkNotification();
        } else if (view == this.mOptionSaveSession) {
            onClickChkSaveSession();
        } else if (view == this.mOptionShareFiles) {
            onClickOptionShareFiles();
        } else if (view == this.mOptionShareImages) {
            onClickOptionShareImages();
        } else if (view == this.mOptionBlockUser) {
            onClickOptionBlockUser();
        } else if (view == this.mChkUnreadCount) {
            onClickChkUnreadCount();
        } else if (view == this.mOptionStarredMessage) {
            onClickStarredMessage();
        } else if (view == this.mOptionCopyGroup) {
            onClickPanelCopyGroup();
        } else if (view == this.mPanelAlertAvailable) {
            onClickAlertWhenAvailable();
        } else if (view == this.mGroupNotificationLayout) {
            onClickOptionNotification();
        } else if (view == this.mMoreOptionsLayout) {
            onClickOptionMoreOptions();
        }
    }

    private void onClickChkUnreadCount() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            notificationSettingMgr.setShowUnreadBadge(this.mSessionId, !notificationSettingMgr.sessionShowUnreadBadge(this.mSessionId));
            updateUnreadCount();
        }
    }

    private void onClickPanelCopyGroup() {
        if (this.mIMAddrBookItem != null) {
            SelectCustomGroupFragment.showAsActivity(this, getString(C4558R.string.zm_msg_add_contact_group_68451), null, 104, this.mIMAddrBookItem.getJid());
        }
    }

    private void onClickAlertWhenAvailable() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && isConnectionGood() && !StringUtil.isEmptyOrNull(this.mBuddyId)) {
            if (AlertWhenAvailableHelper.getInstance().isInAlertQueen(this.mBuddyId)) {
                AlertWhenAvailableHelper.getInstance().removeJidFromAlertQueen(this.mBuddyId);
            } else if (AlertWhenAvailableHelper.getInstance().isDesktopPresenceOnline(this.mBuddyId)) {
                AlertWhenAvailableHelper.getInstance().showErrorToast(zMActivity, this.mBuddyId);
            } else {
                AlertWhenAvailableHelper.getInstance().addJidToAlertQueen(this.mBuddyId);
            }
        }
    }

    private void onClickStarredMessage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            StarredMessageActivity.launch(activity, this.mSessionId);
        }
    }

    private void onClickOptionBlockUser() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            FragmentActivity activity = getActivity();
            if (activity != null && !StringUtil.isEmptyOrNull(this.mBuddyId)) {
                boolean isConnectionGood = zoomMessenger.isConnectionGood();
                if (!zoomMessenger.blockUserIsBlocked(this.mBuddyId)) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                    if (buddyWithJID != null) {
                        BlockFragment.show(getFragmentManager(), IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                    }
                } else if (!isConnectionGood) {
                    Toast.makeText(activity, C4558R.string.zm_mm_msg_cannot_unblock_buddy_no_connection, 1).show();
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(this.mBuddyId);
                    zoomMessenger.blockUserUnBlockUsers(arrayList);
                }
            }
        }
    }

    private void onClickOptionShareFiles() {
        MMSessionContentsFragment.showAsActivity(this, this.mSessionId, 0, 0);
        ZoomLogEventTracking.eventTrackBrowseContent(this.mSessionId);
    }

    private void onClickOptionShareImages() {
        MMSessionContentsFragment.showAsActivity(this, this.mSessionId, 1, 0);
    }

    private void onClickOptionNotification() {
        MMSessionNotificationsFragment.showAsActivity(this, this.mSessionId, this.mGroupNotificationType, 0);
    }

    private void onClickOptionMoreOptions() {
        MMSessionMoreOptionsFragment.showAsActivity(this, this.mSessionId, 0);
    }

    private void onClickChkSaveSession() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.savedSessionSet(this.mSessionId, !this.mChkSaveSession.isChecked())) {
            CheckedTextView checkedTextView = this.mChkSaveSession;
            checkedTextView.setChecked(!checkedTextView.isChecked());
        }
    }

    private void onClickOptionTopic() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.getGroupById(this.mGroupId) != null) {
            MMSetGroupInformationFragment.showAsActivity(this, this.mGroupId, 0);
        }
    }

    private void onClickOptionDescription() {
        MMSessionDescriptionFragment.showAsActivity(this, this.mGroupId, 0);
    }

    private void onClickOptionMembersCount() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.getGroupById(this.mGroupId) != null) {
            MMSessionMembersListFragment.showAsActivity(this, this.mGroupId, 0);
        }
    }

    private void onClickOptionInvite() {
        boolean z;
        int i;
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            if (this.mIsGroup) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                    if (groupById != null) {
                        i = zoomMessenger.getGroupLimitCount(groupById.isPublicRoom());
                        z = (groupById.getMucType() & 4) != 0;
                        for (int i2 = 0; i2 < groupById.getBuddyCount(); i2++) {
                            ZoomBuddy buddyAt = groupById.getBuddyAt(i2);
                            if (buddyAt != null) {
                                arrayList.add(buddyAt.getJid());
                            }
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                arrayList.add(this.mBuddyId);
                i = 0;
                z = false;
            }
            String string = zMActivity.getString(this.mIsGroup ? C4558R.string.zm_mm_title_add_contacts : C4558R.string.zm_mm_title_select_contacts);
            String string2 = zMActivity.getString(C4558R.string.zm_btn_ok);
            String string3 = getString(C4558R.string.zm_msg_select_buddies_to_join_group_instructions_59554);
            SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
            selectContactsParamter.title = string;
            selectContactsParamter.preSelectedItems = arrayList;
            selectContactsParamter.btnOkText = string2;
            selectContactsParamter.instructionMessage = string3;
            selectContactsParamter.isAnimBottomTop = true;
            selectContactsParamter.isOnlySameOrganization = z;
            selectContactsParamter.maxSelectCount = i;
            selectContactsParamter.includeRobot = false;
            selectContactsParamter.isContainsAllInGroup = false;
            MMSelectContactsActivity.show((Activity) zMActivity, selectContactsParamter, 100, (Bundle) null);
        }
    }

    private void onClickBtnBack() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        if (getShowsDialog()) {
            dismiss();
        } else if (activity != null) {
            activity.setResult(0);
            activity.finish();
        }
    }

    /* access modifiers changed from: private */
    public void updateStarBtn() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (UIMgr.isMyNotes(this.mSessionId)) {
            this.mBtnStared.setVisibility(8);
        } else {
            this.mBtnStared.setVisibility(0);
        }
        if (zoomMessenger == null) {
            return;
        }
        if (zoomMessenger.isStarSession(this.mSessionId)) {
            this.mBtnStared.setImageResource(C4558R.C4559drawable.zm_mm_starred_icon_on);
            this.mBtnStared.setContentDescription(getString(C4558R.string.zm_accessibility_unstarred_channel_62483));
            return;
        }
        this.mBtnStared.setImageResource(C4558R.C4559drawable.zm_mm_starred_title_bar_icon_normal);
        this.mBtnStared.setContentDescription(getString(C4558R.string.zm_accessibility_starred_channel_62483));
    }

    private void onClickStarredBtn() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            String str = this.mSessionId;
            if (zoomMessenger.starSessionSetStar(str, !zoomMessenger.isStarSession(str))) {
                updateStarBtn();
            }
        }
    }

    private void onClickBtnClearHistory() {
        ZoomLogEventTracking.eventTrackClearHistory(this.mIsGroup);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                boolean z = this.mIsGroup;
                boolean z2 = !z;
                if (z) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                    if (groupById != null) {
                        z2 = !groupById.isRoom();
                    } else {
                        return;
                    }
                }
                new Builder(activity).setTitle(z2 ? C4558R.string.zm_mm_msg_delete_p2p_chat_history_confirm : C4558R.string.zm_mm_msg_delete_group_chat_history_confirm_59554).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MMChatInfoFragment.this.clearHistory();
                    }
                }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
            }
        }
    }

    private void onClickChkNotification() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood() || !NetworkUtil.hasDataNetwork(getActivity())) {
                showConnectionError();
                return;
            }
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                new ArrayList().add(this.mSessionId);
                if (this.mChkNotification.isChecked()) {
                    notificationSettingMgr.applyMUCSettings(this.mSessionId, 3);
                } else {
                    notificationSettingMgr.applyMUCSettings(this.mSessionId, 1);
                }
                updateData();
            }
        }
    }

    /* access modifiers changed from: private */
    public void clearHistory() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null && sessionById.clearAllMessages()) {
                if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                    AccessibilityUtil.announceForAccessibilityCompat(this.mBtnClearHistory, C4558R.string.zm_accessibility_history_clear_22864);
                }
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity instanceof MMChatInfoActivity) {
                    ((MMChatInfoActivity) zMActivity).onHistoryCleared();
                }
                EventBus.getDefault().post(new ZMChatSession(this.mSessionId, 1));
            }
        }
    }

    public void onClickBuddyItem(@NonNull MMBuddyItem mMBuddyItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(mMBuddyItem.getBuddyJid());
                if (buddyWithJID != null && !StringUtil.isSameString(buddyWithJID.getJid(), myself.getJid())) {
                    IMAddrBookItem localContact = mMBuddyItem.getLocalContact();
                    if (localContact == null) {
                        localContact = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    }
                    if (localContact != null) {
                        localContact.setIsZoomUser(true);
                    }
                    if (localContact == null || !localContact.getIsRobot()) {
                        AddrBookItemDetailsActivity.show((Fragment) this, localContact, true ^ this.mIsGroup, 100);
                    } else if (localContact.isMyContact()) {
                        AddrBookItemDetailsActivity.show((Fragment) this, localContact, true ^ this.mIsGroup, 100);
                    }
                }
            }
        }
    }

    public void onContactsSelected(@Nullable ArrayList<IMAddrBookItem> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                AccessibilityUtil.announceForAccessibilityCompat(this.mMembersCountLayout, (CharSequence) getString(C4558R.string.zm_accessibility_select_contacts_success_22861, getString(this.mIsGroup ? C4558R.string.zm_mm_title_add_contacts : C4558R.string.zm_mm_title_select_contacts)));
            }
            if (this.mIsGroup) {
                addBuddiesToGroup(arrayList);
            } else {
                makeGroupWithNewBuddies(arrayList);
            }
        }
    }

    private void addBuddiesToGroup(@NonNull ArrayList<IMAddrBookItem> arrayList) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) it.next();
                if (!StringUtil.isEmptyOrNull(iMAddrBookItem.getJid())) {
                    arrayList2.add(iMAddrBookItem.getJid());
                }
            }
            if (!zoomMessenger.isConnectionGood()) {
                showConnectionError();
                return;
            }
            if (zoomMessenger.addBuddyToGroup(this.mGroupId, arrayList2)) {
                showWaitingDialog();
            } else {
                showAddBuddiesFailureMessage(1, null);
            }
        }
    }

    private void makeGroupWithNewBuddies(@NonNull ArrayList<IMAddrBookItem> arrayList) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) it.next();
                if (!StringUtil.isEmptyOrNull(iMAddrBookItem.getJid())) {
                    arrayList2.add(iMAddrBookItem.getJid());
                }
            }
            if (!StringUtil.isEmptyOrNull(this.mBuddyId)) {
                arrayList2.add(this.mBuddyId);
            }
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = myself.getJid();
                if (!StringUtil.isEmptyOrNull(jid)) {
                    if (!arrayList2.contains(jid)) {
                        arrayList2.add(jid);
                    }
                    if (!zoomMessenger.isConnectionGood()) {
                        showConnectionError();
                        return;
                    }
                    MakeGroupResult makeGroup = zoomMessenger.makeGroup(arrayList2, "", 80);
                    if (makeGroup == null || !makeGroup.getResult()) {
                        showMakeGroupFailureMessage(1, null);
                    } else if (makeGroup.getValid()) {
                        String reusableGroupId = makeGroup.getReusableGroupId();
                        ZMActivity zMActivity = (ZMActivity) getActivity();
                        if (zMActivity != null && !StringUtil.isEmptyOrNull(reusableGroupId)) {
                            startGroupChat(zMActivity, reusableGroupId);
                        }
                    } else {
                        showWaitingDialog();
                    }
                }
            }
        }
    }

    private boolean isConnectionGood() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isConnectionGood();
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            this.mWaitingDialog = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            this.mWaitingDialog.setCancelable(true);
            this.mWaitingDialog.show(fragmentManager, "WaitingDialog");
        }
    }

    /* access modifiers changed from: private */
    public void dismissWaitingDialog() {
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
                    } catch (Exception unused) {
                    }
                }
            }
            this.mWaitingDialog = null;
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    private void showAddBuddiesFailureMessage(int i, @Nullable GroupAction groupAction) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
            } else if (i == 8) {
                Toast.makeText(activity, C4558R.string.zm_mm_msg_add_buddies_to_group_failed_too_many_buddies_59554, 1).show();
            } else {
                String string = activity.getString(C4558R.string.zm_mm_msg_add_buddies_to_group_failed_59554, new Object[]{Integer.valueOf(i)});
                if (i == 40 && groupAction != null && groupAction.getMaxAllowed() > 0) {
                    string = activity.getString(C4558R.string.zm_mm_msg_max_allowed_buddies_50731, new Object[]{Integer.valueOf(groupAction.getMaxAllowed())});
                }
                Toast.makeText(activity, string, 1).show();
            }
        }
    }

    private void showRemoveBuddyFailureMessage(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
                return;
            }
            Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_remove_buddy_from_group_failed_59554, new Object[]{Integer.valueOf(i)}), 1).show();
        }
    }

    private void showChangeTopicFailureMessage(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
            } else {
                Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_change_group_topic_failed), 1).show();
            }
        }
    }

    private void showMakeGroupFailureMessage(int i, @Nullable GroupAction groupAction) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
            } else if (i == 8) {
                Toast.makeText(activity, C4558R.string.zm_mm_msg_make_group_failed_too_many_buddies_59554, 1).show();
            } else {
                String string = activity.getString(C4558R.string.zm_mm_msg_make_group_failed_59554, new Object[]{Integer.valueOf(i)});
                if (i == 40 && groupAction != null && groupAction.getMaxAllowed() > 0) {
                    string = activity.getString(C4558R.string.zm_mm_msg_max_allowed_buddies_50731, new Object[]{Integer.valueOf(groupAction.getMaxAllowed())});
                }
                Toast.makeText(activity, string, 1).show();
            }
        }
    }
}
