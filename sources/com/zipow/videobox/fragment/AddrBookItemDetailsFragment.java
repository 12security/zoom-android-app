package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.box.androidsdk.content.models.BoxUser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.PBXSMSActivity;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.dialog.InformationBarriesDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.fragment.InviteFragment.InviteFailedDialog;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.ContactType;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.PhoneNumber;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyUserInfo;
import com.zipow.videobox.ptapp.IMProtos.PersonalGroupAtcionResponse;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTAppProtos.UserProfileResult;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.ptapp.RoomDevice;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ContactCloudSIP;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddySearchData;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.AlertWhenAvailableHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.util.ZMDomainUtil;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.PresenceStateView;
import com.zipow.videobox.view.p014mm.PhoneLabelFragment;
import com.zipow.videobox.view.sip.sms.IPBXMessageSelectContact;
import com.zipow.videobox.view.sip.sms.PBXMessageContact;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMSendMessageFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.androidlib.widget.GridItemDecoration;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.ZMToast;
import p021us.zoom.videomeetings.C4558R;

public class AddrBookItemDetailsFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener, IABContactsCacheListener, IPhoneABListener, IPBXMessageSelectContact {
    private static final int ACTION_MAX_SIZE = 3;
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_IS_FROM_ONE_TO_ONE_CHAT = "isFromOneToOneChat";
    private static final String ARG_NEED_SAVE_OPEN_TIME = "needSaveOpenTime";
    private static final int REQUEST_COPY_BUDDY_CONTACT_GROUP = 1;
    private static final int REQUEST_PERMISSION_CALL_PHONE = 12;
    private static final int REQUEST_PERMISSION_MIC = 11;
    public static final String RESULT_EXTRA_BACK_TO_CHAT = "backToChat";
    private static final String WAITING_DIALOG_TAG = "search_key_waiting_dialog";
    private final String TAG = AddrBookItemDetailsFragment.class.getSimpleName();
    private RecyclerView actionsRecycleView;
    private RecyclerView detailRecyclerView;
    private ActionAdapter mActionAdapter;
    private GridItemDecoration mActionDecoration;
    @NonNull
    private List<ActionItem> mActions = new ArrayList();
    private DetailAdapter mAdapter;
    private AvatarView mAvatarView = null;
    private Button mBtnBack = null;
    private View mBtnMoreOpts = null;
    /* access modifiers changed from: private */
    @Nullable
    public IMAddrBookItem mContact = null;
    private String mDailString;
    @NonNull
    private ISIPCallEventListener mISIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            AddrBookItemDetailsFragment.this.updateUserInfo();
            AddrBookItemDetailsFragment.this.updateButtons();
        }

        public void OnPBXUserStatusChange(int i) {
            super.OnPBXUserStatusChange(i);
            AddrBookItemDetailsFragment.this.updateUserInfo();
            AddrBookItemDetailsFragment.this.updateButtons();
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            super.OnPBXFeatureOptionsChanged(list);
            if (!(list == null || list.size() == 0 || !ZMPhoneUtils.isPBXFeatureOptionChanged(list, CmmSIPMessageManager.getInstance().getMessageEnabledBit()))) {
                AddrBookItemDetailsFragment.this.updateButtons();
            }
        }
    };
    private SimpleISIPLineMgrEventSinkListener mISIPLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            AddrBookItemDetailsFragment.this.updateUserInfo();
            AddrBookItemDetailsFragment.this.updateButtons();
        }
    };
    private PresenceStateView mImgPresence;
    /* access modifiers changed from: private */
    public View mInvitationsTip;
    private View mLineDivider;
    private LinearLayout mPanelDepartment;
    private LinearLayout mPanelJobTitle;
    private LinearLayout mPanelLocation;
    private LinearLayout mPanelPresence;
    private Set<DetailItem> mPhoneNumbers;
    private View mRobotIcon = null;
    @Nullable
    private String mSelectedPhoneNumber = null;
    private ImageView mStarredIcon;
    /* access modifiers changed from: private */
    @Nullable
    public Timer mTimer;
    private TextView mTxtCustomStatus;
    private TextView mTxtDepartment;
    private TextView mTxtJobTitle;
    private TextView mTxtLocation;
    private TextView mTxtPresence;
    private ZMEllipsisTextView mTxtScreenName = null;
    private TextView mTxtSubName = null;
    @Nullable
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onIndicateInfoUpdatedWithJID(String str) {
            AddrBookItemDetailsFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void onIndicate_BuddyBigPictureDownloaded(String str, int i) {
            AddrBookItemDetailsFragment.this.onIndicateBuddyBigPicUpdated(str);
        }

        public void onNotifyBuddyJIDUpgrade(String str, String str2, String str3) {
            AddrBookItemDetailsFragment.this.onNotifyBuddyJIDUpgrade(str, str2, str3);
        }

        public void onRemoveBuddy(String str, int i) {
            AddrBookItemDetailsFragment.this.onRemoveBuddy(str, i);
        }

        public void Indicate_BlockedUsersUpdated() {
            AddrBookItemDetailsFragment.this.Indicate_BlockedUsersUpdated();
        }

        public void Indicate_BlockedUsersAdded(List<String> list) {
            AddrBookItemDetailsFragment.this.Indicate_BlockedUsersAdded(list);
        }

        public void Indicate_BlockedUsersRemoved(List<String> list) {
            AddrBookItemDetailsFragment.this.Indicate_BlockedUsersRemoved(list);
        }

        public void Indicate_FetchUserProfileResult(UserProfileResult userProfileResult) {
            AddrBookItemDetailsFragment.this.Indicate_FetchUserProfileResult(userProfileResult);
        }

        public void onSearchBuddyByKey(String str, int i) {
            AddrBookItemDetailsFragment.this.onSearchBuddyByKey(str, i);
        }

        public void notifyStarSessionDataUpdate() {
            AddrBookItemDetailsFragment.this.updateStarBtn(PTApp.getInstance().getZoomMessenger());
        }

        public void NotifyCallUnavailable(String str, long j) {
            if (AddrBookItemDetailsFragment.this.mContact != null && !TextUtils.isEmpty(AddrBookItemDetailsFragment.this.mContact.getJid()) && AddrBookItemDetailsFragment.this.mContact.getJid().equals(str)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    if (buddyWithJID != null) {
                        String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                        ZMActivity zMActivity = (ZMActivity) AddrBookItemDetailsFragment.this.getActivity();
                        if (zMActivity != null) {
                            ZMToast.show((Context) zMActivity, (CharSequence) String.format(AddrBookItemDetailsFragment.this.getString(C4558R.string.zm_mm_lbl_xxx_declined_the_call_62107), new Object[]{buddyDisplayName}), 1);
                        }
                    }
                }
                ZmPtUtils.onCallError(j);
            }
        }

        public void Notify_SubscriptionIsRestrict(String str, boolean z) {
            AddrBookItemDetailsFragment.this.Notify_SubscriptionIsRestrict(str, z);
        }

        public void Notify_SubscribeRequestSent(String str, int i) {
            AddrBookItemDetailsFragment.this.Notify_SubscribeRequestSent(str, i);
        }

        public void Indicate_BuddyAccountStatusChange(String str, int i) {
            if (i == 2 || i == 3) {
                FragmentActivity activity = AddrBookItemDetailsFragment.this.getActivity();
                if (activity != null) {
                    activity.finish();
                }
            }
        }

        public void Indicate_BuddyPresenceChanged(String str) {
            AddrBookItemDetailsFragment.this.onIndicate_BuddyPresenceChanged(str);
        }

        public void OnPersonalGroupResponse(byte[] bArr) {
            AddrBookItemDetailsFragment.this.OnPersonalGroupResponse(bArr);
        }
    };
    private boolean mbCheckingContactForChat = false;

    private enum ACTIONS {
        VIDEO,
        AUDIO,
        CHAT,
        UNKNOWN
    }

    static class ActionAdapter extends Adapter<ActionViewHolder> {
        private Context mContext;
        @NonNull
        private List<ActionItem> mData = new ArrayList();

        public ActionAdapter(Context context) {
            this.mContext = context;
        }

        public void updateData(@Nullable List<ActionItem> list) {
            this.mData.clear();
            if (list != null) {
                this.mData.addAll(list);
            }
            notifyDataSetChanged();
        }

        @NonNull
        public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ActionViewHolder(LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_addrbook_item_details_action, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull ActionViewHolder actionViewHolder, int i) {
            actionViewHolder.setData((ActionItem) this.mData.get(i));
        }

        public int getItemCount() {
            return this.mData.size();
        }
    }

    static class ActionItem {
        int IconId;
        boolean disable;
        OnClick onClick;
        OnLongClick onLongClick;
        int stringId;
        @NonNull
        ACTIONS type = ACTIONS.UNKNOWN;

        interface OnClick {
            void onClick(ActionItem actionItem);
        }

        interface OnLongClick {
            void onLongClick(ActionItem actionItem);
        }

        ActionItem() {
        }
    }

    static class ActionViewHolder extends ViewHolder {
        protected ActionItem data;
        private ImageView itemImg;
        private TextView itemText;
        private View itemView;

        public ActionViewHolder(@NonNull View view) {
            super(view);
            this.itemView = view;
            this.itemImg = (ImageView) view.findViewById(C4558R.C4560id.actionImg);
            this.itemText = (TextView) view.findViewById(C4558R.C4560id.actionTxt);
        }

        public void setData(@NonNull final ActionItem actionItem) {
            this.data = actionItem;
            if (actionItem.type == ACTIONS.UNKNOWN) {
                this.itemImg.setVisibility(8);
                this.itemText.setVisibility(8);
                return;
            }
            this.itemImg.setVisibility(0);
            this.itemText.setVisibility(0);
            this.itemImg.setImageDrawable(this.itemView.getContext().getResources().getDrawable(actionItem.IconId));
            this.itemText.setText(actionItem.stringId);
            View view = this.itemView;
            view.setContentDescription(view.getContext().getString(C4558R.string.zm_addr_book_item_content_desc_109011, new Object[]{this.itemText.getText().toString()}));
            if (actionItem.disable) {
                this.itemView.setEnabled(false);
            } else {
                this.itemView.setEnabled(true);
            }
            if (actionItem.onClick != null) {
                this.itemView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        actionItem.onClick.onClick(actionItem);
                    }
                });
            }
        }
    }

    public static class BlockFragment extends ZMDialogFragment {
        private static final String ARG_ADDRBOOKITEM = "addrBookItem";

        public static void show(@Nullable FragmentManager fragmentManager, @Nullable IMAddrBookItem iMAddrBookItem) {
            if (iMAddrBookItem != null && fragmentManager != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ARG_ADDRBOOKITEM, iMAddrBookItem);
                BlockFragment blockFragment = new BlockFragment();
                blockFragment.setArguments(bundle);
                blockFragment.show(fragmentManager, BlockFragment.class.getName());
            }
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_ADDRBOOKITEM);
            ZMAlertDialog create = new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_alert_block_confirm_title_127965, iMAddrBookItem.getScreenName())).setMessage(getString(C4558R.string.zm_alert_block_confirm_msg_127965, iMAddrBookItem.getScreenName(), iMAddrBookItem.getScreenName())).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).setPositiveButton(C4558R.string.zm_btn_block, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BlockFragment.this.getActivity() != null) {
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            Bundle arguments = BlockFragment.this.getArguments();
                            if (arguments != null) {
                                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(BlockFragment.ARG_ADDRBOOKITEM);
                                if (iMAddrBookItem != null) {
                                    boolean isConnectionGood = zoomMessenger.isConnectionGood();
                                    String jid = iMAddrBookItem.getJid();
                                    if (!zoomMessenger.blockUserIsBlocked(jid)) {
                                        if (!isConnectionGood) {
                                            Toast.makeText(BlockFragment.this.getActivity(), C4558R.string.zm_mm_msg_cannot_block_buddy_no_connection, 1).show();
                                            return;
                                        }
                                        ArrayList arrayList = new ArrayList();
                                        arrayList.add(jid);
                                        zoomMessenger.blockUserBlockUsers(arrayList);
                                        ZoomLogEventTracking.eventTrackBlockContact();
                                    }
                                }
                            }
                        }
                    }
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }
    }

    public static class ClickContextMenu extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 3;
        public static final int ACTION_PHONE_CALL = 1;
        public static final int ACTION_PHONE_MSG = 2;
        private String mValue;

        public ClickContextMenu(int i, String str, String str2) {
            super(i, str);
            this.mValue = str2;
        }

        public String getValue() {
            return this.mValue;
        }
    }

    public static class ContextMenuFragment extends ZMDialogFragment {
        private static final String ARG_ADDRBOOKITEM = "addrBookItem";
        @Nullable
        private ZMMenuAdapter<ContextMenuItem> mAdapter;

        public static void show(@NonNull FragmentManager fragmentManager, @NonNull IMAddrBookItem iMAddrBookItem) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_ADDRBOOKITEM, iMAddrBookItem);
            ContextMenuFragment contextMenuFragment = new ContextMenuFragment();
            contextMenuFragment.setArguments(bundle);
            contextMenuFragment.show(fragmentManager, ContextMenuFragment.class.getName());
        }

        public ContextMenuFragment() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            String str;
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return createEmptyDialog();
            }
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_ADDRBOOKITEM);
            if (iMAddrBookItem == null) {
                return createEmptyDialog();
            }
            this.mAdapter = createUpdateAdapter();
            String screenName = iMAddrBookItem.getScreenName();
            if (StringUtil.isEmptyOrNull(screenName)) {
                str = activity.getString(C4558R.string.zm_title_invite);
            } else {
                str = activity.getString(C4558R.string.zm_title_invite_xxx, new Object[]{screenName});
            }
            ZMAlertDialog create = new Builder(activity).setTitle((CharSequence) str).setAdapter(this.mAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContextMenuFragment.this.onSelectItem(i);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }

        private ZMMenuAdapter<ContextMenuItem> createUpdateAdapter() {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return null;
            }
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_ADDRBOOKITEM);
            ArrayList arrayList = new ArrayList();
            if (iMAddrBookItem != null) {
                for (int i = 0; i < iMAddrBookItem.getPhoneNumberCount(); i++) {
                    String phoneNumber = iMAddrBookItem.getPhoneNumber(i);
                    arrayList.add(new ContextMenuItem(phoneNumber, phoneNumber, null));
                }
                for (int i2 = 0; i2 < iMAddrBookItem.getEmailCount(); i2++) {
                    String email = iMAddrBookItem.getEmail(i2);
                    arrayList.add(new ContextMenuItem(email, null, email));
                }
            }
            ZMMenuAdapter<ContextMenuItem> zMMenuAdapter = this.mAdapter;
            if (zMMenuAdapter == null) {
                this.mAdapter = new ZMMenuAdapter<>(getActivity(), false);
            } else {
                zMMenuAdapter.clear();
            }
            this.mAdapter.addAll((List<MenuItemType>) arrayList);
            return this.mAdapter;
        }

        public void refresh() {
            ZMMenuAdapter createUpdateAdapter = createUpdateAdapter();
            if (createUpdateAdapter != null) {
                createUpdateAdapter.notifyDataSetChanged();
            }
        }

        /* access modifiers changed from: private */
        public void onSelectItem(int i) {
            ZMMenuAdapter<ContextMenuItem> zMMenuAdapter = this.mAdapter;
            if (zMMenuAdapter != null) {
                ContextMenuItem contextMenuItem = (ContextMenuItem) zMMenuAdapter.getItem(i);
                if (contextMenuItem != null) {
                    ZMActivity zMActivity = (ZMActivity) getActivity();
                    if (zMActivity != null) {
                        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                        if (supportFragmentManager != null) {
                            if (contextMenuItem.isPhoneNumberItem()) {
                                AddrBookItemDetailsFragment.inviteBySMS(zMActivity, supportFragmentManager, contextMenuItem.getPhoneNumber());
                            } else {
                                AddrBookItemDetailsFragment.inviteByEmail(zMActivity, supportFragmentManager, contextMenuItem.getEmail());
                            }
                        }
                    }
                }
            }
        }
    }

    static class ContextMenuItem extends ZMSimpleMenuItem {
        private String mEmail;
        private String mLabel;
        private String mPhoneNumber;

        @Nullable
        public Drawable getIcon() {
            return null;
        }

        public ContextMenuItem(String str, String str2, String str3) {
            this.mLabel = str;
            this.mPhoneNumber = str2;
            this.mEmail = str3;
        }

        @NonNull
        public String toString() {
            return this.mLabel;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public String getPhoneNumber() {
            return this.mPhoneNumber;
        }

        public String getEmail() {
            return this.mEmail;
        }

        public boolean isPhoneNumberItem() {
            return !StringUtil.isEmptyOrNull(this.mPhoneNumber);
        }

        public boolean isEmailItem() {
            return !StringUtil.isEmptyOrNull(this.mEmail);
        }
    }

    static class DetailAdapter extends Adapter<DetailViewHolder> {
        static final int TYPE_LABEL = 0;
        static final int TYPE_LABEL_VALUE = 2;
        static final int TYPE_VALUE = 1;
        Context mContext;
        @NonNull
        List<DetailItem> mDatas = new ArrayList();

        public DetailAdapter(Context context) {
            this.mContext = context;
        }

        public void updateData(@Nullable List<DetailItem> list) {
            this.mDatas.clear();
            if (list != null) {
                this.mDatas.addAll(list);
            }
            notifyDataSetChanged();
        }

        @NonNull
        public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            switch (i) {
                case 1:
                    return new ValueViewHolder(View.inflate(this.mContext, C4558R.layout.zm_addrbook_item_value, null));
                case 2:
                    return new LabelValueViewHolder(View.inflate(this.mContext, C4558R.layout.zm_addrbook_item_label_value, null));
                default:
                    return new LabelViewHolder(View.inflate(this.mContext, C4558R.layout.zm_addrbook_item_label, null));
            }
        }

        public void onBindViewHolder(@NonNull DetailViewHolder detailViewHolder, int i) {
            detailViewHolder.setData((DetailItem) this.mDatas.get(i));
        }

        public int getItemViewType(int i) {
            if (i < 0 || i >= this.mDatas.size()) {
                return 0;
            }
            return ((DetailItem) this.mDatas.get(i)).type;
        }

        public int getItemCount() {
            return this.mDatas.size();
        }
    }

    static class DetailItem {
        String label;
        OnClick onClick;
        OnLongClick onLongClick;
        int type;
        String value;

        interface OnClick {
            void onClick(DetailItem detailItem);
        }

        interface OnLongClick {
            void onLongClick(DetailItem detailItem);
        }

        DetailItem() {
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            DetailItem detailItem = (DetailItem) obj;
            if (!this.label.equals(detailItem.label)) {
                return false;
            }
            return this.value.equals(detailItem.value);
        }

        public int hashCode() {
            return (this.label.hashCode() * 31) + this.value.hashCode();
        }
    }

    static abstract class DetailViewHolder extends ViewHolder {
        protected DetailItem data;

        /* access modifiers changed from: 0000 */
        public abstract void updateUI();

        public DetailViewHolder(@NonNull View view) {
            super(view);
        }

        public void setData(@NonNull DetailItem detailItem) {
            this.data = detailItem;
            updateUI();
        }
    }

    static class LabelValueViewHolder extends DetailViewHolder implements OnClickListener, OnLongClickListener {
        private TextView label;
        private TextView value;

        public LabelValueViewHolder(@NonNull View view) {
            super(view);
            this.label = (TextView) view.findViewById(C4558R.C4560id.label);
            this.value = (TextView) view.findViewById(C4558R.C4560id.value);
        }

        public void updateUI() {
            this.label.setText(this.data.label);
            this.value.setText(this.data.value);
            if (this.data.onLongClick != null) {
                this.itemView.setOnLongClickListener(this);
            }
            if (this.data.onClick != null) {
                this.itemView.setOnClickListener(this);
            }
        }

        public void onClick(View view) {
            if (this.data.onClick != null) {
                this.data.onClick.onClick(this.data);
            }
        }

        public boolean onLongClick(View view) {
            if (this.data.onLongClick == null) {
                return false;
            }
            this.data.onLongClick.onLongClick(this.data);
            return true;
        }
    }

    static class LabelViewHolder extends DetailViewHolder {
        private TextView label;

        public LabelViewHolder(@NonNull View view) {
            super(view);
            this.label = (TextView) view.findViewById(C4558R.C4560id.label);
        }

        /* access modifiers changed from: 0000 */
        public void updateUI() {
            this.label.setText(this.data.label);
        }
    }

    static class LongClickMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 0;
        private String mValue;

        public LongClickMenuItem(int i, String str, String str2) {
            super(i, str);
            this.mValue = str2;
        }

        public String getValue() {
            return this.mValue;
        }
    }

    static class MoreOptsMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_ADD_TO_EXISTING_CONTACT = 1;
        public static final int ACTION_ADD_ZOOM_CONTACT = 2;
        public static final int ACTION_ALERT_WHEN_AVAILABLE = 7;
        public static final int ACTION_AUTO_ANSWER = 4;
        public static final int ACTION_BLOCK_USER = 5;
        public static final int ACTION_COPY_GROUP = 6;
        public static final int ACTION_CREATE_NEW_CONTACT = 0;
        public static final int ACTION_REMOVE_ZOOM_CONTACT = 3;

        public MoreOptsMenuItem(int i, String str) {
            super(i, str);
        }
    }

    public interface PhoneCall {
        void call(String str);
    }

    public static class PhoneLabelContextMenu extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 3;
        public static final int ACTION_PHONE_CALL = 1;
        public static final int ACTION_PHONE_MSG = 2;
        private DetailItem detailItem;

        public PhoneLabelContextMenu(int i, String str, DetailItem detailItem2) {
            super(i, str);
            this.detailItem = detailItem2;
        }

        public DetailItem getItem() {
            return this.detailItem;
        }
    }

    public static class SMSInviteDialog extends DialogFragment {
        private static final String ARG_PHONE_NUMBER = "ARG_PHONE_NUMBER";

        public static void showDialog(@NonNull FragmentManager fragmentManager, String str) {
            SMSInviteDialog sMSInviteDialog = new SMSInviteDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PHONE_NUMBER, str);
            sMSInviteDialog.setArguments(bundle);
            sMSInviteDialog.show(fragmentManager, SMSInviteDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            final String string = getArguments().getString(ARG_PHONE_NUMBER);
            return new Builder(getActivity()).setTitle(C4558R.string.zm_lbl_contact_invite_zoom_58879).setMessage(C4558R.string.zm_lbl_contact_invite_zoom_des_58879).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_invite, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    List querySMSActivities = AndroidAppUtil.querySMSActivities(SMSInviteDialog.this.getActivity());
                    if (!CollectionsUtil.isCollectionEmpty(querySMSActivities)) {
                        AndroidAppUtil.sendSMSVia((ResolveInfo) querySMSActivities.get(0), SMSInviteDialog.this.getActivity(), new String[]{string}, SMSInviteDialog.this.getString(C4558R.string.zm_msg_invite_by_sms_33300));
                    }
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
        }
    }

    static class ValueViewHolder extends DetailViewHolder implements OnClickListener, OnLongClickListener {
        private TextView value;

        public ValueViewHolder(@NonNull View view) {
            super(view);
            this.value = (TextView) view.findViewById(C4558R.C4560id.value);
        }

        public void updateUI() {
            this.value.setText(this.data.value);
            if (this.data.onLongClick != null) {
                this.itemView.setOnLongClickListener(this);
            }
            if (this.data.onClick != null) {
                this.itemView.setOnClickListener(this);
            }
        }

        public void onClick(View view) {
            if (this.data.onClick != null) {
                this.data.onClick.onClick(this.data);
            }
        }

        public boolean onLongClick(View view) {
            if (this.data.onLongClick == null) {
                return false;
            }
            this.data.onLongClick.onLongClick(this.data);
            return true;
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void showInActivity(@NonNull ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem) {
        showInActivity(zMActivity, false, iMAddrBookItem, false);
    }

    public static void showInActivity(@NonNull ZMActivity zMActivity, boolean z, IMAddrBookItem iMAddrBookItem) {
        showInActivity(zMActivity, z, iMAddrBookItem, false);
    }

    public static void showInActivity(ZMActivity zMActivity, boolean z, IMAddrBookItem iMAddrBookItem, boolean z2) {
        AddrBookItemDetailsFragment addrBookItemDetailsFragment = new AddrBookItemDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CONTACT, iMAddrBookItem);
        bundle.putBoolean(ARG_IS_FROM_ONE_TO_ONE_CHAT, z2);
        bundle.putBoolean(ARG_NEED_SAVE_OPEN_TIME, z);
        addrBookItemDetailsFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, addrBookItemDetailsFragment, AddrBookItemDetailsFragment.class.getName()).commit();
    }

    @Nullable
    public static AddrBookItemDetailsFragment findAddrBookItemDetailsFragment(@Nullable ZMActivity zMActivity) {
        if (zMActivity == null) {
            return null;
        }
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return null;
        }
        return (AddrBookItemDetailsFragment) supportFragmentManager.findFragmentByTag(AddrBookItemDetailsFragment.class.getName());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_addrbook_item_details, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mRobotIcon = inflate.findViewById(C4558R.C4560id.robotIcon);
        this.mBtnMoreOpts = inflate.findViewById(C4558R.C4560id.btnMoreOpts);
        this.mTxtScreenName = (ZMEllipsisTextView) inflate.findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtSubName = (TextView) inflate.findViewById(C4558R.C4560id.txtScreenSubName);
        this.mStarredIcon = (ImageView) inflate.findViewById(C4558R.C4560id.zm_mm_addr_book_detail_starred);
        this.mAvatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        this.mTxtCustomStatus = (TextView) inflate.findViewById(C4558R.C4560id.txtCustomStatus);
        this.mPanelPresence = (LinearLayout) inflate.findViewById(C4558R.C4560id.panel_presence);
        this.mImgPresence = (PresenceStateView) inflate.findViewById(C4558R.C4560id.img_presence);
        this.mImgPresence.setmTxtDeviceTypeGone();
        this.mTxtPresence = (TextView) inflate.findViewById(C4558R.C4560id.txt_presence);
        this.mPanelDepartment = (LinearLayout) inflate.findViewById(C4558R.C4560id.pannel_department);
        this.mTxtDepartment = (TextView) inflate.findViewById(C4558R.C4560id.txt_department);
        this.mPanelJobTitle = (LinearLayout) inflate.findViewById(C4558R.C4560id.panel_job_title);
        this.mTxtJobTitle = (TextView) inflate.findViewById(C4558R.C4560id.txt_job_title);
        this.mPanelLocation = (LinearLayout) inflate.findViewById(C4558R.C4560id.panel_location);
        this.mTxtLocation = (TextView) inflate.findViewById(C4558R.C4560id.txt_location);
        this.mLineDivider = inflate.findViewById(C4558R.C4560id.line_divider);
        this.detailRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.detailRecyclerView);
        this.detailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            public boolean canScrollVertically() {
                return false;
            }

            @NonNull
            public LayoutParams generateDefaultLayoutParams() {
                return new LayoutParams(-1, -2);
            }
        });
        this.mAdapter = new DetailAdapter(getActivity());
        this.detailRecyclerView.setAdapter(this.mAdapter);
        this.actionsRecycleView = (RecyclerView) inflate.findViewById(C4558R.C4560id.zm_mm_addr_book_detail_action_list);
        this.actionsRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        this.mActionAdapter = new ActionAdapter(getContext());
        if (this.mActionDecoration == null) {
            this.mActionDecoration = new GridItemDecoration.Builder(getContext()).setColorResource(C4558R.color.zm_ui_kit_color_gray_F7F7FA).setShowLastLine(false).build();
            this.actionsRecycleView.addItemDecoration(this.mActionDecoration);
        }
        this.actionsRecycleView.setAdapter(this.mActionAdapter);
        this.mStarredIcon.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnMoreOpts.setOnClickListener(this);
        this.mAvatarView.setOnClickListener(this);
        updateButtons();
        PTUI.getInstance().addPhoneABListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        CmmSIPCallManager.getInstance().addListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        fetchUserProfile();
        return inflate;
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        if (bundle == null && this.mContact != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                String jid = this.mContact.getJid();
                if (!StringUtil.isEmptyOrNull(jid)) {
                    zoomMessenger.refreshBuddyVCard(jid, true);
                    zoomMessenger.refreshBuddyBigPicture(jid);
                }
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePhoneABListener(this);
    }

    public void onDestroyView() {
        destroyTimer();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        CmmSIPCallManager.getInstance().removeListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        super.onDestroyView();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            onCopyBuddyInCustomGroup(intent);
        }
    }

    private void onCopyBuddyInCustomGroup(@Nullable Intent intent) {
        if (intent != null) {
            MMZoomBuddyGroup mMZoomBuddyGroup = (MMZoomBuddyGroup) intent.getSerializableExtra(SelectCustomGroupFragment.RESULT_GROUP);
            if (mMZoomBuddyGroup != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null && this.mContact != null) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(this.mContact.getJid());
                    zoomMessenger.addBuddyToPersonalBuddyGroup(arrayList, mMZoomBuddyGroup.getXmppGroupID());
                }
            }
        }
    }

    private boolean canAccessMoreDetails() {
        boolean z = false;
        if (this.mContact == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (zoomMessenger.isMyContact(this.mContact.getJid()) || zoomMessenger.isCompanyContact(this.mContact.getJid())) {
            z = true;
        }
        return z;
    }

    private void fetchUserProfile() {
        if (canAccessMoreDetails() && this.mContact != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.fetchUserProfileByJid(this.mContact.getJid());
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateUserInfo() {
        String str;
        String str2;
        if (this.mContact != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mContact.getJid());
                if (buddyWithJID != null) {
                    IMAddrBookItem iMAddrBookItem = this.mContact;
                    this.mContact = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (this.mContact != null) {
                        if (iMAddrBookItem.isFromWebSearch()) {
                            this.mContact.setIsFromWebSearch(true);
                        }
                        this.mContact.setContact(iMAddrBookItem.getContact());
                    } else {
                        return;
                    }
                }
            }
            String screenName = this.mContact.getScreenName();
            int i = this.mContact.getAccountStatus() == 1 ? C4558R.string.zm_lbl_deactivated_62074 : this.mContact.getAccountStatus() == 2 ? C4558R.string.zm_lbl_terminated_62074 : 0;
            if (this.mContact.getIsRoomDevice()) {
                this.mBtnMoreOpts.setVisibility(4);
                this.mStarredIcon.setVisibility(8);
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                String signature = this.mContact.getSignature();
                if (StringUtil.isEmptyOrNull(signature)) {
                    this.mTxtCustomStatus.setVisibility(8);
                } else {
                    this.mTxtCustomStatus.setVisibility(0);
                    this.mTxtCustomStatus.setText(signature);
                }
                if (this.mContact.isSharedGlobalDirectory()) {
                    this.mPanelPresence.setVisibility(8);
                }
                this.mAvatarView.show(this.mContact.getAvatarParamsBuilder());
                this.mAvatarView.setContentDescription(getString(C4558R.string.zm_accessibility_contact_avatar_75690, this.mContact.getScreenName()));
                this.mImgPresence.setState(this.mContact);
                if (!TextUtils.isEmpty(this.mImgPresence.getTxtDeviceTypeText())) {
                    this.mTxtPresence.setText(this.mImgPresence.getTxtDeviceTypeText());
                    this.mTxtPresence.setVisibility(0);
                } else {
                    this.mTxtPresence.setVisibility(8);
                }
                this.mTxtScreenName.getText().toString();
                this.mTxtScreenName.setEllipsisText(screenName, i);
                screenNameDynamicLayout();
                if (!TextUtils.isEmpty(this.mContact.getDepartment())) {
                    this.mTxtDepartment.setText(this.mContact.getDepartment());
                    this.mPanelDepartment.setVisibility(0);
                } else {
                    this.mPanelDepartment.setVisibility(8);
                }
                if (!TextUtils.isEmpty(this.mContact.getJobTitle())) {
                    this.mTxtJobTitle.setText(this.mContact.getJobTitle());
                    this.mPanelJobTitle.setVisibility(0);
                } else {
                    this.mPanelJobTitle.setVisibility(8);
                }
                if (this.mPanelDepartment.getVisibility() == 8 && this.mPanelJobTitle.getVisibility() == 8 && this.mPanelLocation.getVisibility() == 8) {
                    this.mLineDivider.setVisibility(8);
                } else {
                    this.mLineDivider.setVisibility(0);
                }
                if (!TextUtils.isEmpty(this.mContact.getLocation())) {
                    this.mTxtLocation.setText(this.mContact.getLocation());
                    this.mPanelLocation.setVisibility(0);
                } else {
                    this.mPanelLocation.setVisibility(8);
                }
                if (zoomMessenger == null || zoomMessenger.imChatGetOption() == 2) {
                    for (ActionItem actionItem : this.mActions) {
                        if (actionItem.type == ACTIONS.CHAT) {
                            actionItem.disable = true;
                        }
                    }
                }
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                if (canAccessMoreDetails() || this.mContact.isFromPhoneContacts() || this.mContact.isSharedGlobalDirectory()) {
                    if (CmmSIPCallManager.getInstance().isSipCallEnabled() && !CmmSIPCallManager.getInstance().isPBXActive() && this.mContact.isSIPAccount()) {
                        DetailItem detailItem = new DetailItem();
                        detailItem.label = getString(C4558R.string.zm_lbl_internal_number_14480);
                        detailItem.value = this.mContact.getSipPhoneNumber();
                        detailItem.type = 2;
                        detailItem.onClick = new OnClick() {
                            public void onClick(DetailItem detailItem) {
                                AddrBookItemDetailsFragment.this.onClickPhoneNumber(new PhoneCall() {
                                    public void call(@NonNull String str) {
                                        AddrBookItemDetailsFragment.this.click2CallSip(str);
                                    }
                                }, AddrBookItemDetailsFragment.this.mContact.getSipPhoneNumber(), AddrBookItemDetailsFragment.this.getString(C4558R.string.zm_lbl_internal_number_14480));
                            }
                        };
                        arrayList.add(detailItem);
                    }
                    this.mPhoneNumbers = new LinkedHashSet();
                    ContactCloudSIP iCloudSIPCallNumber = this.mContact.getICloudSIPCallNumber();
                    if (CmmSIPCallManager.getInstance().isPBXActive() && iCloudSIPCallNumber != null) {
                        String companyNumber = iCloudSIPCallNumber.getCompanyNumber();
                        String extension = iCloudSIPCallNumber.getExtension();
                        if ((CmmSIPCallManager.getInstance().isSameCompanyWithLoginUser(companyNumber) || this.mContact.isSharedGlobalDirectory()) && !StringUtil.isEmptyOrNull(extension)) {
                            DetailItem detailItem2 = new DetailItem();
                            detailItem2.label = getString(C4558R.string.zm_title_extension_35373);
                            detailItem2.value = extension;
                            detailItem2.type = 2;
                            detailItem2.onClick = new OnClick() {
                                public void onClick(@NonNull DetailItem detailItem) {
                                    AddrBookItemDetailsFragment.this.onClickPhoneNumber(new PhoneCall() {
                                        public void call(@NonNull String str) {
                                            AddrBookItemDetailsFragment.this.click2CallSip(str);
                                        }
                                    }, detailItem.value, AddrBookItemDetailsFragment.this.getString(C4558R.string.zm_title_extension_35373));
                                }
                            };
                            arrayList.add(detailItem2);
                        }
                        ArrayList directNumber = iCloudSIPCallNumber.getDirectNumber();
                        if (!CollectionsUtil.isCollectionEmpty(directNumber)) {
                            if (directNumber.size() == 1) {
                                DetailItem detailItem3 = new DetailItem();
                                detailItem3.label = getString(C4558R.string.zm_title_direct_number_31439);
                                detailItem3.value = ZMPhoneUtils.formatPhoneNumber((String) directNumber.get(0));
                                detailItem3.type = 2;
                                detailItem3.onClick = new OnClick() {
                                    public void onClick(@NonNull DetailItem detailItem) {
                                        AddrBookItemDetailsFragment.this.onClickPhoneNumber(new PhoneCall() {
                                            public void call(@NonNull String str) {
                                                AddrBookItemDetailsFragment.this.click2CallSip(str);
                                            }
                                        }, detailItem.value, AddrBookItemDetailsFragment.this.getString(C4558R.string.zm_title_direct_number_31439));
                                    }
                                };
                                arrayList.add(detailItem3);
                            } else {
                                DetailItem detailItem4 = new DetailItem();
                                detailItem4.label = getString(C4558R.string.zm_title_direct_number_31439);
                                detailItem4.value = ZMPhoneUtils.formatPhoneNumber((String) directNumber.get(0));
                                detailItem4.type = 2;
                                detailItem4.onClick = new OnClick() {
                                    public void onClick(@NonNull DetailItem detailItem) {
                                        AddrBookItemDetailsFragment.this.onClickPhoneNumber(new PhoneCall() {
                                            public void call(@NonNull String str) {
                                                AddrBookItemDetailsFragment.this.click2CallSip(str);
                                            }
                                        }, detailItem.value, AddrBookItemDetailsFragment.this.getString(C4558R.string.zm_title_direct_number_31439));
                                    }
                                };
                                arrayList.add(detailItem4);
                                for (int i2 = 1; i2 < directNumber.size(); i2++) {
                                    DetailItem detailItem5 = new DetailItem();
                                    detailItem5.value = ZMPhoneUtils.formatPhoneNumber((String) directNumber.get(i2));
                                    detailItem5.type = 1;
                                    detailItem5.onClick = new OnClick() {
                                        public void onClick(@NonNull DetailItem detailItem) {
                                            AddrBookItemDetailsFragment.this.onClickPhoneNumber(new PhoneCall() {
                                                public void call(@NonNull String str) {
                                                    AddrBookItemDetailsFragment.this.click2CallSip(str);
                                                }
                                            }, detailItem.value, AddrBookItemDetailsFragment.this.getString(C4558R.string.zm_title_direct_number_31439));
                                        }
                                    };
                                    arrayList.add(detailItem5);
                                }
                            }
                        }
                    }
                    if (!StringUtil.isEmptyOrNull(this.mContact.getBuddyPhoneNumber())) {
                        String str3 = "";
                        CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
                        if (cloudPBXInfo != null) {
                            str2 = cloudPBXInfo.getCountryCode();
                            str = cloudPBXInfo.getAreaCode();
                        } else {
                            String isoCountryCode2PhoneCountryCode = CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(activity));
                            str = str3;
                            str2 = isoCountryCode2PhoneCountryCode;
                        }
                        String formatPhoneNumber = ZMPhoneUtils.formatPhoneNumber(this.mContact.getBuddyPhoneNumber(), str2, str, true);
                        if (!StringUtil.isEmptyOrNull(formatPhoneNumber)) {
                            DetailItem detailItem6 = new DetailItem();
                            detailItem6.label = getString(C4558R.string.zm_lbl_mobile_phone_number_124795);
                            detailItem6.value = formatPhoneNumber;
                            detailItem6.type = 2;
                            arrayList2.add(formatPhoneNumber);
                            this.mPhoneNumbers.add(detailItem6);
                        }
                    }
                    if (!StringUtil.isEmptyOrNull(this.mContact.getProfilePhoneNumber())) {
                        String formatPhoneNumber2 = ZMPhoneUtils.formatPhoneNumber(this.mContact.getProfilePhoneNumber(), this.mContact.getProfileCountryCode(), "", true);
                        if (!StringUtil.isEmptyOrNull(formatPhoneNumber2) && !arrayList2.contains(formatPhoneNumber2)) {
                            arrayList2.add(formatPhoneNumber2);
                            DetailItem detailItem7 = new DetailItem();
                            detailItem7.label = getString(this.mPhoneNumbers.size() > 0 ? C4558R.string.zm_lbl_others_phone_number_124795 : C4558R.string.zm_lbl_web_phone_number_124795);
                            detailItem7.value = formatPhoneNumber2;
                            detailItem7.type = 2;
                            this.mPhoneNumbers.add(detailItem7);
                        }
                    }
                    if (this.mContact.getContact() == null) {
                        ABContactsCache instance = ABContactsCache.getInstance();
                        IMAddrBookItem iMAddrBookItem2 = this.mContact;
                        iMAddrBookItem2.setContact(instance.getFirstContactByPhoneNumber(iMAddrBookItem2.getBuddyPhoneNumber()));
                    }
                    Contact contact = this.mContact.getContact();
                    if (contact != null && !CollectionsUtil.isCollectionEmpty(contact.accounts)) {
                        if (this.mContact.isFromPhoneContacts()) {
                            String string = getString(C4558R.string.zm_lbl_contact_from_phone_58879);
                            Iterator it = contact.accounts.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                ContactType contactType = (ContactType) it.next();
                                if (contactType != null && !TextUtils.isEmpty(contactType.type)) {
                                    if (contactType.type.contains("outlook")) {
                                        string = getString(C4558R.string.zm_lbl_contact_from_outlook_58879);
                                        break;
                                    } else if (contactType.type.contains("google")) {
                                        string = getString(C4558R.string.zm_lbl_contact_from_gmail_58879);
                                        break;
                                    }
                                }
                            }
                            this.mTxtCustomStatus.setVisibility(0);
                            this.mTxtCustomStatus.setText(string);
                            this.mStarredIcon.setVisibility(8);
                            this.mBtnMoreOpts.setVisibility(8);
                            for (ActionItem actionItem2 : this.mActions) {
                                switch (actionItem2.type) {
                                    case AUDIO:
                                        actionItem2.stringId = C4558R.string.zm_mm_lbl_phone_call_68451;
                                        break;
                                    case VIDEO:
                                        actionItem2.disable = true;
                                        break;
                                    case CHAT:
                                        actionItem2.disable = true;
                                        break;
                                }
                            }
                        }
                        Iterator it2 = contact.accounts.iterator();
                        while (it2.hasNext()) {
                            ContactType contactType2 = (ContactType) it2.next();
                            if (contactType2 != null && !CollectionsUtil.isCollectionEmpty(contactType2.phoneNumbers)) {
                                Iterator it3 = contactType2.phoneNumbers.iterator();
                                while (it3.hasNext()) {
                                    PhoneNumber phoneNumber = (PhoneNumber) it3.next();
                                    String displayPhoneNumber = phoneNumber.getDisplayPhoneNumber();
                                    if (!StringUtil.isEmptyOrNull(displayPhoneNumber) && !arrayList2.contains(displayPhoneNumber)) {
                                        arrayList2.add(displayPhoneNumber);
                                        DetailItem detailItem8 = new DetailItem();
                                        detailItem8.label = phoneNumber.getLabel();
                                        detailItem8.value = displayPhoneNumber;
                                        if (this.mPhoneNumbers.size() == 0) {
                                            detailItem8.type = 2;
                                        } else {
                                            detailItem8.type = 1;
                                        }
                                        this.mPhoneNumbers.add(detailItem8);
                                    }
                                }
                            }
                        }
                    }
                    if (this.mPhoneNumbers.size() > 0) {
                        for (final DetailItem detailItem9 : this.mPhoneNumbers) {
                            if (!TextUtils.isEmpty(detailItem9.value)) {
                                detailItem9.onClick = new OnClick() {
                                    public void onClick(DetailItem detailItem) {
                                        AddrBookItemDetailsFragment.this.onClickPhoneNumber(new PhoneCall() {
                                            public void call(@NonNull String str) {
                                                if (CmmSIPCallManager.getInstance().isPBXActive()) {
                                                    AddrBookItemDetailsFragment.this.click2CallSip(str);
                                                } else {
                                                    AddrBookItemDetailsFragment.this.doCallNumber(str);
                                                }
                                            }
                                        }, detailItem9.value, detailItem9.label);
                                    }
                                };
                                detailItem9.onLongClick = new OnLongClick() {
                                    public void onLongClick(DetailItem detailItem) {
                                        AddrBookItemDetailsFragment.this.onClickPhoneNumber(new PhoneCall() {
                                            public void call(@NonNull String str) {
                                                if (CmmSIPCallManager.getInstance().isPBXActive()) {
                                                    AddrBookItemDetailsFragment.this.click2CallSip(str);
                                                } else {
                                                    AddrBookItemDetailsFragment.this.doCallNumber(str);
                                                }
                                            }
                                        }, detailItem9.value, detailItem9.label);
                                    }
                                };
                                arrayList.add(detailItem9);
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(this.mContact.getAccountEmail())) {
                        DetailItem detailItem10 = new DetailItem();
                        detailItem10.label = getString(C4558R.string.zm_lbl_zoom_account);
                        detailItem10.value = this.mContact.getAccountEmail();
                        detailItem10.type = 2;
                        arrayList.add(detailItem10);
                    }
                    this.mActionAdapter.updateData(this.mActions);
                }
                if (this.mContact.getIsRoomDevice()) {
                    DetailItem detailItem11 = new DetailItem();
                    detailItem11.label = getString(C4558R.string.zm_lbl_ip_address_82945);
                    detailItem11.value = this.mContact.getRoomDeviceInfo().getIp();
                    detailItem11.type = 2;
                    detailItem11.onLongClick = new OnLongClick() {
                        public void onLongClick(@NonNull DetailItem detailItem) {
                            AddrBookItemDetailsFragment.this.showCopyContextMenu(detailItem.value);
                        }
                    };
                    arrayList.add(detailItem11);
                }
                String introduction = this.mContact.getIntroduction();
                if (this.mContact.getIsRobot() && !TextUtils.isEmpty(introduction)) {
                    DetailItem detailItem12 = new DetailItem();
                    detailItem12.label = getString(C4558R.string.zm_lbl_robot_introduction_68798);
                    detailItem12.type = 2;
                    detailItem12.value = introduction;
                    arrayList.add(detailItem12);
                }
                this.mAdapter.updateData(arrayList);
            }
        }
    }

    /* access modifiers changed from: private */
    public void doCallNumber(String str) {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.CALL_PHONE") == 0) {
            callNumber(str);
            return;
        }
        zm_requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 12);
        this.mDailString = str;
    }

    @SuppressLint({"MissingPermission"})
    private void callNumber(String str) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ZMIntentUtil.callNumber(zMActivity, str);
        }
    }

    private String formatPersonUrl(@NonNull String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        if (str.startsWith("https://")) {
            return str.substring(8);
        }
        return str.startsWith(ZMDomainUtil.ZM_URL_HTTP) ? str.substring(7) : str;
    }

    private Bitmap getAvatarBitmap() {
        if (this.mContact == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mContact.getJid());
            String localBigPicturePath = buddyWithJID != null ? buddyWithJID.getLocalBigPicturePath() : null;
            if (ImageUtil.isValidImageFile(localBigPicturePath)) {
                return ZMBitmapFactory.decodeFile(localBigPicturePath);
            }
            if (!StringUtil.isEmptyOrNull(localBigPicturePath)) {
                File file = new File(localBigPicturePath);
                if (file.exists()) {
                    file.delete();
                }
            }
            if (buddyWithJID != null) {
                String localPicturePath = buddyWithJID.getLocalPicturePath();
                if (ImageUtil.isValidImageFile(localPicturePath)) {
                    Bitmap decodeFile = ZMBitmapFactory.decodeFile(localPicturePath);
                    if (decodeFile != null) {
                        return decodeFile;
                    }
                }
            }
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return this.mContact.getAvatarBitmap(activity);
        }
        return null;
    }

    public void onResume() {
        super.onResume();
        PTUI.getInstance().addPTUIListener(this);
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        updateBtnMoreOpts();
        if (instance.needReloadAll()) {
            instance.reloadAllContacts();
        }
        updateUserInfo();
        updateButtons();
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(iMAddrBookItem.getJid(), str)) {
            updateUserInfo();
            updateBtnMoreOpts();
            updateButtons();
        }
    }

    /* access modifiers changed from: private */
    public void OnPersonalGroupResponse(byte[] bArr) {
        if (bArr != null && this.mContact != null) {
            try {
                PersonalGroupAtcionResponse parseFrom = PersonalGroupAtcionResponse.parseFrom(bArr);
                if (parseFrom != null && parseFrom.getType() == 4) {
                    List notAllowedBuddiesList = parseFrom.getNotAllowedBuddiesList();
                    if (!CollectionsUtil.isListEmpty(notAllowedBuddiesList)) {
                        Iterator it = notAllowedBuddiesList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            BuddyUserInfo buddyUserInfo = (BuddyUserInfo) it.next();
                            if (buddyUserInfo.getNotAllowedReason() == 1 && TextUtils.equals(buddyUserInfo.getJid(), this.mContact.getJid())) {
                                InformationBarriesDialog.show(getContext(), getString(C4558R.string.zm_mm_information_barries_personal_group_add_115072, buddyUserInfo.getDisplayName()), false);
                                break;
                            }
                        }
                    }
                }
            } catch (InvalidProtocolBufferException unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicate_BuddyPresenceChanged(String str) {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(iMAddrBookItem.getJid(), str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mContact.getJid());
                if (buddyWithJID != null) {
                    IMAddrBookItem iMAddrBookItem2 = this.mContact;
                    this.mContact = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (this.mContact != null) {
                        if (iMAddrBookItem2.isFromWebSearch()) {
                            this.mContact.setIsFromWebSearch(true);
                        }
                        this.mContact.setContact(iMAddrBookItem2.getContact());
                    } else {
                        return;
                    }
                }
            }
            if (this.mContact.isSharedGlobalDirectory()) {
                this.mPanelPresence.setVisibility(8);
            }
            this.mImgPresence.setState(this.mContact);
            if (!TextUtils.isEmpty(this.mImgPresence.getTxtDeviceTypeText())) {
                this.mTxtPresence.setText(this.mImgPresence.getTxtDeviceTypeText());
                this.mTxtPresence.setVisibility(0);
            } else {
                this.mTxtPresence.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotifyBuddyJIDUpgrade(String str, String str2, String str3) {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(iMAddrBookItem.getJid(), str2)) {
            close();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyBigPicUpdated(String str) {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(iMAddrBookItem.getJid(), str)) {
            updateUserInfo();
        }
    }

    /* access modifiers changed from: private */
    public void onRemoveBuddy(String str, int i) {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(iMAddrBookItem.getJid(), str)) {
            close();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersUpdated() {
        updateButtons();
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null) {
            onIndicate_BuddyPresenceChanged(iMAddrBookItem.getJid());
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersAdded(List<String> list) {
        updateButtons();
        if (!CollectionsUtil.isListEmpty(list)) {
            IMAddrBookItem iMAddrBookItem = this.mContact;
            if (iMAddrBookItem != null && list.contains(iMAddrBookItem.getJid())) {
                onIndicate_BuddyPresenceChanged(this.mContact.getJid());
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersRemoved(List<String> list) {
        updateButtons();
        if (!CollectionsUtil.isListEmpty(list)) {
            IMAddrBookItem iMAddrBookItem = this.mContact;
            if (iMAddrBookItem != null && list.contains(iMAddrBookItem.getJid())) {
                onIndicate_BuddyPresenceChanged(this.mContact.getJid());
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FetchUserProfileResult(@Nullable UserProfileResult userProfileResult) {
        if (userProfileResult != null && this.mContact != null && StringUtil.isSameString(userProfileResult.getPeerJid(), this.mContact.getJid())) {
            updateUserInfo();
        }
    }

    public void onSearchBuddyByKey(String str, int i) {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(str, iMAddrBookItem.getAccountEmail())) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddySearchData buddySearchData = zoomMessenger.getBuddySearchData();
                if (buddySearchData == null || buddySearchData.getBuddyCount() <= 0) {
                    addBuddyByJid();
                } else {
                    UIUtil.dismissWaitingDialog(getFragmentManager(), WAITING_DIALOG_TAG);
                    addSameDomainBuddy();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Notify_SubscriptionIsRestrict(String str, boolean z) {
        String str2;
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(str, iMAddrBookItem.getJid())) {
            destroyTimer();
            UIUtil.dismissWaitingDialog(getFragmentManager(), WAITING_DIALOG_TAG);
            if (z) {
                str2 = getString(C4558R.string.zm_mm_lbl_add_contact_restrict_48295);
            } else {
                str2 = getString(C4558R.string.zm_mm_lbl_cannot_add_contact_48295);
            }
            Toast.makeText(getActivity(), str2, 1).show();
        }
    }

    /* access modifiers changed from: private */
    public void Notify_SubscribeRequestSent(String str, int i) {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && StringUtil.isSameString(str, iMAddrBookItem.getJid())) {
            destroyTimer();
            UIUtil.dismissWaitingDialog(getFragmentManager(), WAITING_DIALOG_TAG);
            if (i == 0) {
                Toast.makeText(getActivity(), C4558R.string.zm_mm_msg_add_contact_request_sent, 1).show();
            }
        }
    }

    private void startTimer() {
        destroyTimer();
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                FragmentActivity activity = AddrBookItemDetailsFragment.this.getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            AddrBookItemDetailsFragment.this.mTimer = null;
                            UIUtil.dismissWaitingDialog(AddrBookItemDetailsFragment.this.getFragmentManager(), AddrBookItemDetailsFragment.WAITING_DIALOG_TAG);
                            Toast.makeText(AddrBookItemDetailsFragment.this.getActivity(), C4558R.string.zm_mm_msg_add_contact_request_sent, 1).show();
                        }
                    });
                }
            }
        }, 5000);
    }

    private void destroyTimer() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
        ABContactsCache.getInstance().removeListener(this);
    }

    public void onPhoneABEvent(int i, final long j, Object obj) {
        if (i == 3) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((AddrBookItemDetailsFragment) iUIElement).onPhoneNumbersMatchUpdated(j);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onPhoneNumbersMatchUpdated(long j) {
        if (this.mbCheckingContactForChat) {
            this.mbCheckingContactForChat = false;
            int i = (int) j;
            if (i != 0) {
                if (i != 2) {
                    if (i == 1104) {
                        onPhoneBindByOther();
                    } else if (i != 5003) {
                        showUnnableChatError(j);
                    }
                }
                showConnectionError();
            } else {
                startChat(false);
            }
        }
    }

    private void onPhoneBindByOther() {
        onClickBtnBack();
    }

    private void showUnnableChatError(long j) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_start_chat_failed, new Object[]{Long.valueOf(j)}), 1).show();
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void onContactsCacheUpdated() {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null && iMAddrBookItem.getPhoneNumberCount() == 1) {
            Contact firstContactByPhoneNumber = ABContactsCache.getInstance().getFirstContactByPhoneNumber(this.mContact.getPhoneNumber(0));
            if (firstContactByPhoneNumber != null) {
                this.mContact.setContactId(firstContactByPhoneNumber.contactId);
                this.mContact.setScreenName(firstContactByPhoneNumber.displayName);
            } else {
                this.mContact.setContactId(-1);
            }
            updateUserInfo();
            updateBtnMoreOpts();
        }
    }

    private void updateBtnMoreOpts() {
        int i;
        int i2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && this.mContact != null) {
            this.mRobotIcon.setVisibility(8);
            if (this.mContact.getIsRobot()) {
                this.mRobotIcon.setVisibility(0);
            } else if (!this.mContact.isZoomRoomContact()) {
                if (this.mContact.getContactId() < 0) {
                    String phoneNumber = this.mContact.getPhoneNumberCount() > 0 ? this.mContact.getPhoneNumber(0) : null;
                    String accountEmail = this.mContact.getAccountEmail();
                    i2 = !StringUtil.isEmptyOrNull(phoneNumber) ? 1 : 0;
                    if (!StringUtil.isEmptyOrNull(accountEmail)) {
                        i2++;
                    }
                } else {
                    i2 = 0;
                }
                i = !zoomMessenger.isMyContact(this.mContact.getJid()) ? i2 + 1 : zoomMessenger.canRemoveBuddy(this.mContact.getJid()) ? i2 + 1 : i2;
                if (!this.mContact.getIsRoomDevice() || this.mContact.isSharedGlobalDirectory()) {
                    this.mBtnMoreOpts.setVisibility(4);
                } else if (i > 0) {
                    this.mBtnMoreOpts.setVisibility(0);
                } else {
                    this.mBtnMoreOpts.setVisibility(8);
                }
            }
            i = 0;
            if (!this.mContact.getIsRoomDevice()) {
            }
            this.mBtnMoreOpts.setVisibility(4);
        }
    }

    private boolean is2_5Buddy() {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem == null) {
            return false;
        }
        String jid = iMAddrBookItem.getJid();
        String phoneNumber = this.mContact.getPhoneNumberCount() > 0 ? this.mContact.getPhoneNumber(0) : null;
        if (jid == null || phoneNumber == null || !jid.startsWith(phoneNumber)) {
            return false;
        }
        return true;
    }

    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: private */
    public void updateStarBtn(@Nullable ZoomMessenger zoomMessenger) {
        if (zoomMessenger != null) {
            IMAddrBookItem iMAddrBookItem = this.mContact;
            if (iMAddrBookItem == null || !zoomMessenger.isStarSession(iMAddrBookItem.getJid())) {
                this.mStarredIcon.setImageResource(C4558R.C4559drawable.zm_mm_starred_title_bar_icon_normal);
                this.mStarredIcon.setContentDescription(getString(C4558R.string.zm_accessibility_starred_contact_62483));
            } else {
                this.mStarredIcon.setImageResource(C4558R.C4559drawable.zm_mm_starred_icon_on);
                this.mStarredIcon.setContentDescription(getString(C4558R.string.zm_accessibility_unstarred_contact_62483));
            }
        }
        IMAddrBookItem iMAddrBookItem2 = this.mContact;
        int i = 0;
        boolean z = iMAddrBookItem2 != null && !iMAddrBookItem2.isSharedGlobalDirectory() && !this.mContact.isFromPhoneContacts() && !this.mContact.getIsRoomDevice();
        if (z) {
            z = this.mContact.getAccountStatus() == 0;
        }
        ImageView imageView = this.mStarredIcon;
        if (!z) {
            i = 8;
        }
        imageView.setVisibility(i);
    }

    @NonNull
    private ActionItem generateActionItemByIndex(IMAddrBookItem iMAddrBookItem, int i) {
        ACTIONS actions;
        if (i == ACTIONS.AUDIO.ordinal()) {
            actions = ACTIONS.AUDIO;
        } else if (i == ACTIONS.CHAT.ordinal()) {
            actions = ACTIONS.CHAT;
        } else if (i == ACTIONS.VIDEO.ordinal()) {
            actions = ACTIONS.VIDEO;
        } else {
            actions = ACTIONS.UNKNOWN;
        }
        return generateActionItemByType(iMAddrBookItem, actions);
    }

    @NonNull
    private ActionItem generateActionItemByType(IMAddrBookItem iMAddrBookItem, @NonNull ACTIONS actions) {
        return generateActionItemByType(iMAddrBookItem, actions, new OnClick() {
            public void onClick(@Nullable ActionItem actionItem) {
                if (actionItem != null) {
                    switch (C245326.f312x37c354a0[actionItem.type.ordinal()]) {
                        case 1:
                            AddrBookItemDetailsFragment.this.onClickBtnAudioCall();
                            break;
                        case 2:
                            AddrBookItemDetailsFragment.this.onClickBtnVideoCall();
                            break;
                        case 3:
                            AddrBookItemDetailsFragment.this.onClickBtnMMChat();
                            break;
                    }
                }
            }
        });
    }

    @NonNull
    private ActionItem generateActionItemByType(@Nullable IMAddrBookItem iMAddrBookItem, @NonNull ACTIONS actions, OnClick onClick) {
        ActionItem actionItem;
        if (iMAddrBookItem == null) {
            return new ActionItem();
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        boolean z2 = zoomMessenger == null || zoomMessenger.imChatGetOption() == 2;
        boolean z3 = iMAddrBookItem.getAccountStatus() == 1;
        boolean z4 = iMAddrBookItem.getAccountStatus() == 2;
        boolean z5 = zoomMessenger == null || zoomMessenger.blockUserIsBlocked(iMAddrBookItem.getJid());
        switch (actions) {
            case AUDIO:
                actionItem = new ActionItem();
                actionItem.type = ACTIONS.AUDIO;
                actionItem.IconId = C4558R.C4559drawable.zm_addrbook_item_details_action_phone_call_ic_bg;
                actionItem.stringId = C4558R.string.zm_btn_phone_call_109011;
                actionItem.onClick = onClick;
                if (!iMAddrBookItem.getIsRobot()) {
                    if (!iMAddrBookItem.isFromPhoneContacts()) {
                        if (!iMAddrBookItem.getIsZoomUser()) {
                            actionItem.disable = true;
                            break;
                        } else {
                            if (z5 || z3 || z4) {
                                z = true;
                            }
                            actionItem.disable = z;
                            break;
                        }
                    } else {
                        actionItem.disable = false;
                        break;
                    }
                } else {
                    actionItem.disable = true;
                    break;
                }
            case VIDEO:
                actionItem = new ActionItem();
                actionItem.type = ACTIONS.VIDEO;
                actionItem.IconId = C4558R.C4559drawable.zm_addrbook_item_details_action_meet_ic_bg;
                actionItem.stringId = C4558R.string.zm_btn_meet_109011;
                actionItem.onClick = onClick;
                if (!iMAddrBookItem.getIsRobot()) {
                    if (!iMAddrBookItem.getIsZoomUser()) {
                        actionItem.disable = true;
                        break;
                    } else {
                        long callStatus = (long) PTApp.getInstance().getCallStatus();
                        if (callStatus != 1) {
                            if (callStatus != 2) {
                                if (z5 || z3 || z4) {
                                    z = true;
                                }
                                actionItem.disable = z;
                                break;
                            } else {
                                actionItem.disable = false;
                                break;
                            }
                        } else {
                            actionItem.disable = true;
                            break;
                        }
                    }
                } else {
                    actionItem.disable = true;
                    break;
                }
            case CHAT:
                actionItem = new ActionItem();
                actionItem.type = ACTIONS.CHAT;
                actionItem.IconId = C4558R.C4559drawable.zm_addrbook_item_details_action_chat_ic_bg;
                actionItem.stringId = C4558R.string.zm_btn_chat_109011;
                actionItem.onClick = onClick;
                if (!iMAddrBookItem.getIsRobot()) {
                    if (!iMAddrBookItem.getIsZoomUser()) {
                        actionItem.disable = true;
                        break;
                    } else {
                        if (z4 || z2 || iMAddrBookItem.isZoomRoomContact() || (iMAddrBookItem.getPhoneNumberCount() <= 0 && StringUtil.isEmptyOrNull(iMAddrBookItem.getJid()))) {
                            z = true;
                        }
                        actionItem.disable = z;
                        break;
                    }
                } else {
                    if (z5 || z2 || z4) {
                        z = true;
                    }
                    actionItem.disable = z;
                    break;
                }
            default:
                actionItem = new ActionItem();
                break;
        }
        return actionItem;
    }

    @NonNull
    private List<ActionItem> initActions(@Nullable IMAddrBookItem iMAddrBookItem, boolean z, boolean z2, boolean z3) {
        if (iMAddrBookItem == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            if (z && i == ACTIONS.CHAT.ordinal()) {
                ActionItem generateActionItemByIndex = generateActionItemByIndex(iMAddrBookItem, i);
                generateActionItemByIndex.disable = true;
                arrayList.add(processActionItemOnAdd(generateActionItemByIndex));
            } else if (z3 && (i == ACTIONS.CHAT.ordinal() || i == ACTIONS.VIDEO.ordinal())) {
                ActionItem generateActionItemByIndex2 = generateActionItemByIndex(iMAddrBookItem, i);
                generateActionItemByIndex2.disable = true;
                arrayList.add(processActionItemOnAdd(generateActionItemByIndex2));
            } else if (!z2 || !(i == ACTIONS.CHAT.ordinal() || i == ACTIONS.AUDIO.ordinal())) {
                arrayList.add(processActionItemOnAdd(generateActionItemByIndex(iMAddrBookItem, i)));
            } else {
                ActionItem generateActionItemByIndex3 = generateActionItemByIndex(iMAddrBookItem, i);
                generateActionItemByIndex3.disable = true;
                arrayList.add(processActionItemOnAdd(generateActionItemByIndex3));
            }
        }
        if (arrayList.size() < 3) {
            for (int size = arrayList.size(); size < 3; size++) {
                arrayList.add(generateActionItemByType(iMAddrBookItem, ACTIONS.UNKNOWN));
            }
        }
        return arrayList;
    }

    @NonNull
    private ActionItem processActionItemOnAdd(@NonNull ActionItem actionItem) {
        if (actionItem.type == ACTIONS.CHAT && actionItem.disable && couldShowSMSAction(this.mContact)) {
            actionItem.disable = false;
            actionItem.stringId = C4558R.string.zm_btn_sms_117773;
            actionItem.onClick = new OnClick() {
                public void onClick(ActionItem actionItem) {
                    AddrBookItemDetailsFragment.this.onClickBtnSMS();
                }
            };
        }
        return actionItem;
    }

    /* access modifiers changed from: private */
    public void updateButtons() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (this.mContact == null) {
                this.mContact = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
            }
            if (this.mContact != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                updateStarBtn(zoomMessenger);
                boolean z = false;
                if (zoomMessenger != null && zoomMessenger.imChatGetOption() == 2 && zoomMessenger.accountChatGetOption() == 2) {
                    z = true;
                }
                this.mActions = initActions(this.mContact, z, this.mContact.getIsRoomDevice(), this.mContact.isSharedGlobalDirectory());
                this.mActionAdapter.updateData(this.mActions);
            }
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnMoreOpts) {
            onClicKBtnMoreOpts();
        } else if (id == C4558R.C4560id.avatarView) {
            onClickAvatarView();
        } else if (id == C4558R.C4560id.zm_mm_addr_book_detail_starred) {
            onClickStarredBtn();
        }
    }

    private void onClickStarredBtn() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            IMAddrBookItem iMAddrBookItem = this.mContact;
            if (iMAddrBookItem != null && zoomMessenger.starSessionSetStar(iMAddrBookItem.getJid(), !zoomMessenger.isStarSession(this.mContact.getJid()))) {
                updateStarBtn(zoomMessenger);
            }
        }
    }

    private void onClickPanelSIIP() {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null) {
            click2CallSip(iMAddrBookItem.getSipPhoneNumber());
        }
    }

    private void onClickCompanyNumber(@NonNull String str) {
        click2CallSip(str);
    }

    /* access modifiers changed from: private */
    public void click2CallSip(@NonNull String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (!NetworkUtil.hasDataNetwork(getContext())) {
                showSipUnavailable();
            } else if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    ZMPhoneUtils.callSip(str, this.mTxtScreenName.getText().toString());
                }
            } else {
                this.mSelectedPhoneNumber = str;
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 11);
            }
        }
    }

    private void showSipUnavailable() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            SimpleMessageDialog.newInstance(getString(C4558R.string.zm_sip_error_network_unavailable_99728), false).show(activity.getSupportFragmentManager(), SimpleMessageDialog.class.getSimpleName());
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C244317 r2 = new EventAction("PhonePBXFragmentPermissionResult") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof AddrBookItemDetailsFragment) {
                        ((AddrBookItemDetailsFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                    }
                }
            };
            eventTaskManager.pushLater("PhonePBXFragmentPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 11) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    String str = this.mSelectedPhoneNumber;
                    if (str != null) {
                        ZMPhoneUtils.callSip(str, this.mTxtScreenName.getText().toString());
                    }
                    this.mSelectedPhoneNumber = null;
                }
            } else if (i == 12 && checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                callNumber(this.mDailString);
            }
        }
    }

    private void showSMSInviteDialog() {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null) {
            Contact contact = iMAddrBookItem.getContact();
            if (contact != null) {
                String str = null;
                Iterator it = contact.accounts.iterator();
                while (it.hasNext()) {
                    ContactType contactType = (ContactType) it.next();
                    if (contactType != null && !CollectionsUtil.isCollectionEmpty(contactType.phoneNumbers)) {
                        Iterator it2 = contactType.phoneNumbers.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            PhoneNumber phoneNumber = (PhoneNumber) it2.next();
                            if (!TextUtils.isEmpty(phoneNumber.normalizedNumber)) {
                                str = phoneNumber.normalizedNumber;
                                break;
                            }
                        }
                        if (str != null) {
                            break;
                        }
                    }
                }
                if (!TextUtils.isEmpty(str)) {
                    SMSInviteDialog.showDialog(getFragmentManager(), str);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickPhoneNumber(@NonNull final PhoneCall phoneCall, final String str, String str2) {
        if (this.mContact != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                boolean z = false;
                final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
                zMMenuAdapter.addItem(new ClickContextMenu(1, activity.getString(C4558R.string.zm_mm_msg_call_82273), str));
                if (CmmSIPMessageManager.getInstance().isMessageEnabled() && (getString(C4558R.string.zm_title_direct_number_31439).equals(str2) || getString(C4558R.string.zm_lbl_mobile_phone_number_124795).equals(str2) || getString(C4558R.string.zm_lbl_web_phone_number_124795).equals(str2) || getString(C4558R.string.zm_lbl_others_phone_number_124795).equals(str2))) {
                    zMMenuAdapter.addItem(new ClickContextMenu(2, activity.getString(C4558R.string.zm_sip_send_message_117773), str));
                }
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger == null) {
                    z = true;
                } else if (zoomMessenger.msgCopyGetOption() == 1) {
                    z = true;
                }
                if (z) {
                    zMMenuAdapter.addItem(new ClickContextMenu(3, activity.getString(C4558R.string.zm_mm_msg_copy_82273), str));
                }
                View inflate = View.inflate(activity, C4558R.layout.zm_phone_label_dialog_head, null);
                TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.phoneTV);
                ((TextView) inflate.findViewById(C4558R.C4560id.labelTV)).setText(str2);
                textView.setText(str);
                ZMAlertDialog create = new Builder(activity).setTheme(C4558R.style.ZMDialog_Material).setTitleView(inflate).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (((ClickContextMenu) zMMenuAdapter.getItem(i)).getAction()) {
                            case 1:
                                phoneCall.call(str);
                                return;
                            case 2:
                                if (CmmSIPMessageManager.getInstance().isMessageEnabled()) {
                                    AddrBookItemDetailsFragment.this.showSMSActivity(str);
                                    return;
                                }
                                return;
                            case 3:
                                AndroidAppUtil.copyText(AddrBookItemDetailsFragment.this.getActivity(), str);
                                return;
                            default:
                                return;
                        }
                    }
                }).create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        }
    }

    private void onClickBtnInviteToConf() {
        int callStatus = PTApp.getInstance().getCallStatus();
        if (callStatus == 1 || callStatus == 2) {
            inviteABContact();
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnVideoCall() {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null) {
            if (iMAddrBookItem.isFromPhoneContacts()) {
                showSMSInviteDialog();
            } else if (this.mContact.getIsRoomDevice()) {
                RoomDevice roomDevice = new RoomDevice();
                roomDevice.setName(this.mContact.getRoomDeviceInfo().getName());
                roomDevice.setIp(this.mContact.getRoomDeviceInfo().getIp());
                roomDevice.setE164num(this.mContact.getRoomDeviceInfo().getE164num());
                roomDevice.setDeviceType(this.mContact.getRoomDeviceInfo().getDeviceType());
                roomDevice.setEncrypt(this.mContact.getRoomDeviceInfo().getEncrypt());
                PTApp.getInstance().startVideoCallWithRoomSystem(roomDevice, 3, 0);
            } else {
                int callStatus = PTApp.getInstance().getCallStatus();
                if (callStatus == 0) {
                    callABContact(1);
                } else if (callStatus == 2) {
                    inviteToMeetingConference();
                }
            }
            ZoomLogEventTracking.eventTrackContactProfileVideoCall();
        }
    }

    private void inviteToMeetingConference() {
        if (PTApp.getInstance().getActiveMeetingItem() != null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
                if (iMAddrBookItem != null && !StringUtil.isEmptyOrNull(iMAddrBookItem.getJid())) {
                    checkInviteToMeetingConference();
                }
            }
        }
    }

    private void checkInviteToMeetingConference() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(activity, new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    AddrBookItemDetailsFragment.this.onInviteToMeetingConference();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onInviteToMeetingConference() {
        MeetingInfoProto activeMeetingItem = PTApp.getInstance().getActiveMeetingItem();
        if (activeMeetingItem != null) {
            String id = activeMeetingItem.getId();
            long meetingNumber = activeMeetingItem.getMeetingNumber();
            Bundle arguments = getArguments();
            if (arguments != null) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
                if (iMAddrBookItem != null) {
                    String[] strArr = new String[1];
                    String jid = iMAddrBookItem.getJid();
                    if (!StringUtil.isEmptyOrNull(jid)) {
                        strArr[0] = jid;
                        if (PTAppDelegation.getInstance().inviteBuddiesToConf(strArr, null, id, meetingNumber, getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                            ZMActivity zMActivity = (ZMActivity) getActivity();
                            if (zMActivity != null && zMActivity.isActive()) {
                                new InviteFailedDialog().show(getFragmentManager(), InviteFailedDialog.class.getName());
                            }
                        } else {
                            showTipInvitationsSent(strArr.length);
                        }
                    }
                }
            }
        }
    }

    private void showTipInvitationsSent(int i) {
        if (this.mInvitationsTip == null) {
            inflateInvitationsSentView(i);
        } else {
            showInvitationsSentView(i);
        }
    }

    private void inflateInvitationsSentView(final int i) {
        ViewStub viewStub = (ViewStub) getView().findViewById(C4558R.C4560id.tipsViewStub);
        viewStub.setOnInflateListener(new OnInflateListener() {
            public void onInflate(ViewStub viewStub, View view) {
                AddrBookItemDetailsFragment.this.mInvitationsTip = view;
                AddrBookItemDetailsFragment.this.showInvitationsSentView(i);
            }
        });
        viewStub.inflate();
    }

    /* access modifiers changed from: private */
    public void showInvitationsSentView(int i) {
        NormalMessageTip.show(getFragmentManager(), TipMessageType.TIP_INVITATIONS_SENT.name(), null, getResources().getQuantityString(C4558R.plurals.zm_msg_invitations_sent, i, new Object[]{Integer.valueOf(i)}), C4558R.C4559drawable.zm_ic_tick, 0, 0, 3000);
    }

    private void callABContact(int i) {
        Bundle arguments = getArguments();
        if (arguments != null && ((IMAddrBookItem) arguments.getSerializable(ARG_CONTACT)) != null) {
            checkCallABContactMeeting(i);
        }
    }

    private void checkCallABContactMeeting(final int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(activity, new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    AddrBookItemDetailsFragment.this.onCallABContact(i);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onCallABContact(int i) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
            if (iMAddrBookItem != null) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    int inviteToVideoCall = ConfActivity.inviteToVideoCall(activity, iMAddrBookItem.getJid(), i);
                    if (inviteToVideoCall != 0) {
                        StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
                    }
                }
            }
        }
    }

    private void inviteABContact() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
            if (iMAddrBookItem != null) {
                FragmentActivity activity = getActivity();
                if (activity != null && !StringUtil.isEmptyOrNull(iMAddrBookItem.getJid())) {
                    if (PTAppDelegation.getInstance().inviteBuddiesToConf(new String[]{iMAddrBookItem.getJid()}, null, PTApp.getInstance().getActiveCallId(), PTApp.getInstance().getActiveMeetingNo(), getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                        onSentInvitationFailed();
                    } else {
                        onSentInvitationDone(activity);
                    }
                }
            }
        }
    }

    private void onSentInvitationFailed() {
        new InviteFailedDialog().show(getFragmentManager(), InviteFailedDialog.class.getName());
    }

    private void onSentInvitationDone(@NonNull Activity activity) {
        ConfLocalHelper.returnToConf(activity);
        activity.finish();
    }

    /* access modifiers changed from: private */
    public void onClickBtnAudioCall() {
        if (CmmSIPCallManager.getInstance().isPBXActive()) {
            IMAddrBookItem iMAddrBookItem = this.mContact;
            if (iMAddrBookItem != null) {
                if (!CollectionsUtil.isCollectionEmpty(iMAddrBookItem.getPhoneCallNumbersForPBX())) {
                    PhoneLabelFragment.show(getFragmentManager(), this.mContact);
                } else {
                    IMAddrBookItem iMAddrBookItem2 = this.mContact;
                    if (iMAddrBookItem2 != null && !iMAddrBookItem2.isFromPhoneContacts() && PTApp.getInstance().getCallStatus() == 0) {
                        callABContact(0);
                    }
                }
                ZoomLogEventTracking.eventTrackContactProfileAudioCall();
            }
        }
        IMAddrBookItem iMAddrBookItem3 = this.mContact;
        if (iMAddrBookItem3 == null || !iMAddrBookItem3.isFromPhoneContacts()) {
            if (PTApp.getInstance().getCallStatus() == 0) {
                callABContact(0);
            }
            ZoomLogEventTracking.eventTrackContactProfileAudioCall();
        }
        if (!CollectionsUtil.isCollectionEmpty(this.mContact.getPhoneCallNumbersForPhoneContact())) {
            PhoneLabelFragment.show(getFragmentManager(), this.mContact);
        }
        ZoomLogEventTracking.eventTrackContactProfileAudioCall();
    }

    private void screenNameDynamicLayout() {
        if (this.mTxtScreenName != null && getContext() != null) {
            int i = 0;
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
            this.mTxtScreenName.measure(makeMeasureSpec, makeMeasureSpec);
            int measuredWidth = this.mTxtScreenName.getMeasuredWidth();
            int displayWidth = UIUtil.getDisplayWidth(getContext());
            int dip2px = UIUtil.dip2px(getContext(), 59.0f);
            int i2 = (displayWidth - measuredWidth) / 2;
            if (i2 >= dip2px) {
                i = i2 - dip2px;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTxtScreenName.getLayoutParams();
            layoutParams.leftMargin = i;
            this.mTxtScreenName.setLayoutParams(layoutParams);
        }
    }

    private void onClickBtnBack() {
        close();
    }

    public void close() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        if (getShowsDialog()) {
            super.dismiss();
        } else if (activity != null) {
            activity.finish();
        }
    }

    private void onClickBtnInviteToGetZoom() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
            if (iMAddrBookItem != null) {
                showNonZoomUserActions(iMAddrBookItem);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnMMChat() {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem == null || !iMAddrBookItem.isFromPhoneContacts()) {
            startChat(true);
        } else {
            showSMSInviteDialog();
        }
        ZoomLogEventTracking.eventTrackMMChat();
    }

    /* access modifiers changed from: private */
    public void onClickBtnSMS() {
        if (couldShowSMSAction(this.mContact)) {
            List externalCloudNumbers = this.mContact.getExternalCloudNumbers();
            if (CollectionsUtil.isListEmpty(externalCloudNumbers) || externalCloudNumbers.size() != 1) {
                if (this.mContact.isFromPhoneContacts()) {
                    List phoneNumberList = this.mContact.getContact().getPhoneNumberList();
                    if (phoneNumberList != null && phoneNumberList.size() == 1) {
                        selectContact(new PBXMessageContact((String) phoneNumberList.get(0), this.mContact), true);
                        return;
                    }
                }
                PhoneLabelFragment.show(getChildFragmentManager(), this.mContact, 1001);
                return;
            }
            selectContact(new PBXMessageContact((String) externalCloudNumbers.get(0), this.mContact), true);
        }
    }

    private void startChat(boolean z) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
                boolean z2 = arguments.getBoolean(ARG_IS_FROM_ONE_TO_ONE_CHAT);
                boolean z3 = arguments.getBoolean(ARG_NEED_SAVE_OPEN_TIME);
                if (iMAddrBookItem != null && getFragmentManager() != null) {
                    if (z2) {
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_EXTRA_BACK_TO_CHAT, true);
                        zMActivity.setResult(-1, intent);
                        zMActivity.finish();
                    } else if (!StringUtil.isEmptyOrNull(iMAddrBookItem.getJid())) {
                        String str = null;
                        if (iMAddrBookItem.getPhoneNumberCount() > 0) {
                            str = iMAddrBookItem.getPhoneNumber(0);
                        }
                        startChat(zMActivity, iMAddrBookItem, str, z3);
                    } else if (z) {
                        checkContactForChat(iMAddrBookItem);
                    } else {
                        showCannotChatMessage();
                    }
                }
            }
        }
    }

    private void showCannotChatMessage() {
        SimpleMessageDialog.newInstance(C4558R.string.zm_mm_msg_cannot_chat_with_old_version).show(getFragmentManager(), "CannotChatMessageDialog");
    }

    private void checkContactForChat(@NonNull IMAddrBookItem iMAddrBookItem) {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            int phoneNumberCount = iMAddrBookItem.getPhoneNumberCount();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < phoneNumberCount; i++) {
                String normalizedPhoneNumber = iMAddrBookItem.getNormalizedPhoneNumber(i);
                if (normalizedPhoneNumber != null) {
                    arrayList.add(normalizedPhoneNumber);
                }
            }
            this.mbCheckingContactForChat = false;
            if (aBContactsHelper.matchPhoneNumbers(arrayList, false) == 0) {
                this.mbCheckingContactForChat = true;
            } else {
                showUnnableChatError(-1);
            }
        }
    }

    private void onClicKBtnMoreOpts() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                IMAddrBookItem iMAddrBookItem = this.mContact;
                if (iMAddrBookItem != null) {
                    String jid = iMAddrBookItem.getJid();
                    boolean isMyContact = zoomMessenger.isMyContact(jid);
                    boolean z = this.mContact.getAccountStatus() == 0;
                    if (!this.mContact.getIsRobot() && z) {
                        if (!isMyContact && !zoomMessenger.isAddContactDisable()) {
                            zMMenuAdapter.addItem(new MoreOptsMenuItem(2, getString(C4558R.string.zm_mi_add_zoom_contact)));
                        } else if (zoomMessenger.canRemoveBuddy(jid)) {
                            zMMenuAdapter.addItem(new MoreOptsMenuItem(3, getString(C4558R.string.zm_mi_remove_zoom_contact)));
                        }
                    }
                    if (this.mContact.getContactId() < 0) {
                        String phoneNumber = this.mContact.getPhoneNumberCount() > 0 ? this.mContact.getPhoneNumber(0) : null;
                        String accountEmail = this.mContact.getAccountEmail();
                        if (!StringUtil.isEmptyOrNull(phoneNumber) || !StringUtil.isEmptyOrNull(accountEmail)) {
                            zMMenuAdapter.addItem(new MoreOptsMenuItem(0, getString(C4558R.string.zm_mi_create_new_contact)));
                            zMMenuAdapter.addItem(new MoreOptsMenuItem(1, getString(C4558R.string.zm_mi_add_to_existing_contact)));
                        }
                    }
                    boolean blockUserIsBlocked = zoomMessenger.blockUserIsBlocked(jid);
                    PTApp instance = PTApp.getInstance();
                    if (isMyContact && instance.isAutoReponseON() && !blockUserIsBlocked && !this.mContact.getIsRobot()) {
                        zMMenuAdapter.addItem(new MoreOptsMenuItem(4, getString(zoomMessenger.isAutoAcceptBuddy(jid) ? C4558R.string.zm_mi_disable_auto_answer : C4558R.string.zm_mi_enable_auto_answer)));
                    }
                    if (isMyContact && z && zoomMessenger.personalGroupGetOption() == 1) {
                        zMMenuAdapter.addItem(new MoreOptsMenuItem(6, getString(C4558R.string.zm_msg_add_contact_group_68451)));
                    }
                    if (this.mContact.getAccountStatus() != 2) {
                        if (blockUserIsBlocked) {
                            zMMenuAdapter.addItem(new MoreOptsMenuItem(5, getString(C4558R.string.zm_mi_unblock_user)));
                        } else {
                            zMMenuAdapter.addItem(new MoreOptsMenuItem(5, getString(C4558R.string.zm_mi_block_user)));
                        }
                    }
                    if (AlertWhenAvailableHelper.getInstance().showAlertWhenAvailable(this.mContact.getJid())) {
                        zMMenuAdapter.addItem(new MoreOptsMenuItem(7, AlertWhenAvailableHelper.getInstance().getMenuString(this.mContact)));
                    }
                    ZMAlertDialog create = new Builder(activity).setTitle(C4558R.string.zm_title_contact_option).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddrBookItemDetailsFragment.this.onSelectMoreOptsMenuItem((MoreOptsMenuItem) zMMenuAdapter.getItem(i));
                        }
                    }).create();
                    create.setCanceledOnTouchOutside(true);
                    create.show();
                }
            }
        }
    }

    private void onClickAvatarView() {
        if (this.mContact != null && getActivity() != null && getAvatarBitmap() != null) {
            AvatarPreviewFragment.showContactAvatar(this, this.mContact);
        }
    }

    /* access modifiers changed from: private */
    public void onSelectMoreOptsMenuItem(@NonNull MoreOptsMenuItem moreOptsMenuItem) {
        if (this.mContact != null) {
            Intent intent = new Intent();
            intent.putExtra("name", this.mContact.getScreenName());
            String phoneNumber = this.mContact.getPhoneNumberCount() > 0 ? this.mContact.getPhoneNumber(0) : null;
            if (!StringUtil.isEmptyOrNull(phoneNumber)) {
                intent.putExtra(BoxUser.FIELD_PHONE, phoneNumber);
                intent.putExtra("phone_type", 2);
            }
            String accountEmail = this.mContact.getAccountEmail();
            if (!StringUtil.isEmptyOrNull(accountEmail)) {
                intent.putExtra("email", accountEmail);
                intent.putExtra("email_type", 2);
            }
            if (moreOptsMenuItem.getAction() == 1) {
                intent.setAction("android.intent.action.INSERT_OR_EDIT");
                intent.setType("vnd.android.cursor.item/contact");
                ZoomLogEventTracking.eventTrackAddToContactsList();
            } else if (moreOptsMenuItem.getAction() == 0) {
                intent.setAction("android.intent.action.INSERT");
                intent.setType("vnd.android.cursor.dir/raw_contact");
            } else if (moreOptsMenuItem.getAction() == 4) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    String jid = this.mContact.getJid();
                    zoomMessenger.updateAutoAnswerGroupBuddy(jid, !zoomMessenger.isAutoAcceptBuddy(jid));
                    return;
                }
                return;
            } else if (moreOptsMenuItem.getAction() == 2) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger2 == null || !zoomMessenger2.isConnectionGood()) {
                        showCannotAddZoomContactForDisconnected();
                        return;
                    }
                    if (is2_5Buddy()) {
                        Toast.makeText(activity, C4558R.string.zm_mm_msg_cannot_add_contact_of_older_version, 1).show();
                    } else {
                        UIUtil.showWaitingDialog(getFragmentManager(), C4558R.string.zm_msg_waiting, WAITING_DIALOG_TAG);
                        startTimer();
                        if (!zoomMessenger2.searchBuddyByKey(this.mContact.getAccountEmail())) {
                            addBuddyByJid();
                        }
                    }
                    return;
                }
                return;
            } else if (moreOptsMenuItem.getAction() == 3) {
                FragmentActivity activity2 = getActivity();
                if (activity2 != null) {
                    ZoomMessenger zoomMessenger3 = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger3 != null) {
                        if (zoomMessenger3.canRemoveBuddy(this.mContact.getJid())) {
                            new Builder(activity2).setTitle((CharSequence) activity2.getString(C4558R.string.zm_title_remove_contact, new Object[]{this.mContact.getScreenName()})).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (AddrBookItemDetailsFragment.this.mContact != null) {
                                        AddrBookItemDetailsFragment.this.mContact.removeItem(AddrBookItemDetailsFragment.this.getActivity());
                                    }
                                }
                            }).create().show();
                        }
                        ZoomLogEventTracking.eventTrackRemoveContact();
                        return;
                    }
                    return;
                }
                return;
            } else if (moreOptsMenuItem.getAction() == 7) {
                AlertWhenAvailableHelper.getInstance().checkAndAddToAlertQueen((ZMActivity) getContext(), this.mContact);
            } else if (moreOptsMenuItem.getAction() == 5) {
                ZoomMessenger zoomMessenger4 = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger4 != null) {
                    FragmentActivity activity3 = getActivity();
                    if (activity3 != null) {
                        boolean isConnectionGood = zoomMessenger4.isConnectionGood();
                        String jid2 = this.mContact.getJid();
                        if (!zoomMessenger4.blockUserIsBlocked(jid2)) {
                            BlockFragment.show(getFragmentManager(), this.mContact);
                        } else if (!isConnectionGood) {
                            Toast.makeText(activity3, C4558R.string.zm_mm_msg_cannot_unblock_buddy_no_connection, 1).show();
                            return;
                        } else {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(jid2);
                            zoomMessenger4.blockUserUnBlockUsers(arrayList);
                            ZoomLogEventTracking.eventTrackUnblockContact();
                        }
                        return;
                    }
                    return;
                }
                return;
            } else if (moreOptsMenuItem.getAction() == 6) {
                SelectCustomGroupFragment.showAsActivity(this, getString(C4558R.string.zm_msg_add_contact_group_68451), null, 1, this.mContact.getJid());
            } else {
                return;
            }
            try {
                ActivityStartHelper.startActivityForeground(getActivity(), intent);
            } catch (Exception unused) {
            }
        }
    }

    private void addBuddyByJid() {
        if (this.mContact != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || !zoomMessenger.isConnectionGood()) {
                showCannotAddZoomContactForDisconnected();
                return;
            }
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = this.mContact.getJid();
                if (zoomMessenger.addBuddyByJID(jid, myself.getScreenName(), null, this.mContact.getScreenName(), this.mContact.getAccountEmail())) {
                    ZMBuddySyncInstance.getInsatance().onAddBuddyByJid(jid);
                } else {
                    Toast.makeText(getActivity(), C4558R.string.zm_mm_msg_add_contact_failed, 1).show();
                }
            }
        }
    }

    private void addSameDomainBuddy() {
        if (this.mContact != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || !zoomMessenger.isConnectionGood()) {
                showCannotAddZoomContactForDisconnected();
            } else if (this.mContact.isIMBlockedByIB()) {
                InformationBarriesDialog.show(getContext(), getString(C4558R.string.zm_mm_information_barries_add_contact_115072), false);
            } else {
                String jid = this.mContact.getJid();
                if (zoomMessenger.isMyContact(jid) || zoomMessenger.addSameOrgBuddyByJID(jid)) {
                    SimpleMessageDialog.newInstance(C4558R.string.zm_mm_msg_already_buddy_54665).show(getFragmentManager(), SimpleMessageDialog.class.getSimpleName());
                } else {
                    Toast.makeText(getActivity(), C4558R.string.zm_mm_msg_add_contact_failed, 1).show();
                }
            }
        }
    }

    private void showCannotAddZoomContactForDisconnected() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_mm_msg_cannot_add_buddy_no_connection, 1).show();
        }
    }

    private void showNonZoomUserActions(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    int phoneNumberCount = iMAddrBookItem.getPhoneNumberCount();
                    int emailCount = iMAddrBookItem.getEmailCount();
                    if (phoneNumberCount == 1 && emailCount == 0) {
                        inviteBySMS(zMActivity, supportFragmentManager, iMAddrBookItem.getPhoneNumber(0));
                    } else if (phoneNumberCount == 0 && emailCount == 1) {
                        inviteByEmail(zMActivity, supportFragmentManager, iMAddrBookItem.getEmail(0));
                    } else {
                        ContextMenuFragment.show(supportFragmentManager, iMAddrBookItem);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void inviteByEmail(Context context, FragmentManager fragmentManager, String str) {
        String zoomInvitationEmailSubject = PTApp.getInstance().getZoomInvitationEmailSubject();
        String zoomInvitationEmailBody = PTApp.getInstance().getZoomInvitationEmailBody();
        ZMSendMessageFragment.show(context, fragmentManager, new String[]{str}, null, zoomInvitationEmailSubject, zoomInvitationEmailBody, zoomInvitationEmailBody, null, null, 1);
    }

    /* access modifiers changed from: private */
    public static void inviteBySMS(Context context, FragmentManager fragmentManager, String str) {
        String zoomInvitationEmailSubject = PTApp.getInstance().getZoomInvitationEmailSubject();
        String string = context.getString(C4558R.string.zm_msg_sms_invitation_content);
        ZMSendMessageFragment.show(context, fragmentManager, null, new String[]{str}, zoomInvitationEmailSubject, string, string, null, null, 2);
    }

    private static void startChat(ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem, String str, boolean z) {
        MMChatActivity.showAsOneToOneChatWithPhoneNumber(zMActivity, iMAddrBookItem, z, str, true);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 22) {
            updateButtons();
        }
    }

    private void loginToUse() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PTApp.getInstance().logout(1);
            LoginActivity.show(zMActivity, false);
            close();
        }
    }

    /* access modifiers changed from: private */
    public void showCopyContextMenu(String str) {
        FragmentActivity activity = getActivity();
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = zoomMessenger != null ? zoomMessenger.msgCopyGetOption() == 1 : true;
        if (z) {
            zMMenuAdapter.addItem(new LongClickMenuItem(0, getString(C4558R.string.zm_btn_copy), str));
        }
        ZMAlertDialog create = new Builder(activity).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AndroidAppUtil.copyText(AddrBookItemDetailsFragment.this.getActivity(), ((LongClickMenuItem) zMMenuAdapter.getItem(i)).getValue());
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        if (zMMenuAdapter.getCount() != 0) {
            create.show();
        }
    }

    private boolean couldShowSMSAction(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem == null) {
            return false;
        }
        if ((!CollectionsUtil.isListEmpty(iMAddrBookItem.getExternalCloudNumbers()) || !iMAddrBookItem.isFromPhoneContacts() || !TextUtils.isEmpty(iMAddrBookItem.getContact().number)) && !CollectionsUtil.isListEmpty(CmmSIPCallManager.getInstance().getDirectNumberList())) {
            return CmmSIPMessageManager.getInstance().isMessageEnabled();
        }
        return false;
    }

    public void selectContact(@NonNull PBXMessageContact pBXMessageContact, boolean z) {
        if (z) {
            showSMSActivity(pBXMessageContact.getPhoneNumber());
        }
    }

    /* access modifiers changed from: private */
    public void showSMSActivity(@NonNull String str) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            List singletonList = Collections.singletonList(str);
            String sessionByToNumbers = CmmSIPMessageManager.getInstance().getSessionByToNumbers(singletonList);
            if (TextUtils.isEmpty(sessionByToNumbers)) {
                sessionByToNumbers = CmmSIPMessageManager.getInstance().getLocalSessionByToNumbers(singletonList);
            }
            if (!TextUtils.isEmpty(sessionByToNumbers)) {
                PBXSMSActivity.showAsSession(zMActivity, sessionByToNumbers);
            } else {
                PBXSMSActivity.showAsToNumbers(zMActivity, new ArrayList(singletonList));
            }
        }
    }
}
