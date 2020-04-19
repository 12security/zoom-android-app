package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.AddrBookSetNumberActivity;
import com.zipow.videobox.AddrBookSettingActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.dialog.InformationBarriesDialog;
import com.zipow.videobox.eventbus.ZMAlertAvailable;
import com.zipow.videobox.eventbus.ZMContactsBuddLongClickyEvent;
import com.zipow.videobox.eventbus.ZMContactsBuddyEvent;
import com.zipow.videobox.eventbus.ZMContactsGroupLongClickEvent;
import com.zipow.videobox.eventbus.ZMContactsPhoneAddressEvent;
import com.zipow.videobox.eventbus.ZMStarEvent;
import com.zipow.videobox.fragment.InviteFragment.InviteFailedDialog;
import com.zipow.videobox.fragment.p012mm.MMPhoneContactsInZoomFragment;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.AutoStreamConflictChecker;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMCallbackUI.IIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI.SimpleIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.IMProtos.PersonalGroupAtcionResponse;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTAppProtos.MakeGroupResult;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomPublicRoomSearchUI;
import com.zipow.videobox.ptapp.ZoomPublicRoomSearchUI.IZoomPublicRoomSearchUIListener;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.ZMBuddyListListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomPublicRoomSearchData;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.AlertWhenAvailableHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMAddrBookListView;
import com.zipow.videobox.view.IMDirectoryAdapter;
import com.zipow.videobox.view.IMDirectoryRecyclerView;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.ZMSearchBar.OnSearchBarListener;
import com.zipow.videobox.view.p014mm.MMContactSearchFragment;
import com.zipow.videobox.view.p014mm.MMContactsAppsListView;
import com.zipow.videobox.view.p014mm.MMContactsGroupListView;
import com.zipow.videobox.view.p014mm.MMCreateGroupFragment;
import com.zipow.videobox.view.p014mm.MMJoinPublicGroupFragment;
import com.zipow.videobox.view.p014mm.MMZoomXMPPRoom;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMSendMessageFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.IZMMenuItem;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMPopupWindow;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class IMAddrBookListFragment extends ZMDialogFragment implements ExtListener, OnClickListener, IPhoneABListener, IPTUIListener, IIMListener, IABContactsCacheListener {
    private static final String EXTRA_BUDDY_GROUP = "buddyGroup";
    private static final String EXTRA_BUDDY_IN_CUSTOM_GROUP = "EXTRA_BUDDY_IN_CUSTOM_GROUP";
    private static final String EXTRA_UI_MODE = "uiMode";
    private static final int MSG_REFRESH_ALL = 1;
    private static final int PERMISSION_REQUEST_ENABLE_ADDRBOOK = 1000;
    private static final int REQUEST_ADD_BUDDY_CONTACT_GROUP = 106;
    private static final int REQUEST_COPY_BUDDY_CONTACT_GROUP = 107;
    private static final int REQUEST_CREATE_CONTACT_GROUP = 105;
    private static final int REQUEST_CREATE_GROUP = 101;
    private static final int REQUEST_DIALOG_ENABLE_PHONE_MATCH = 104;
    private static final int REQUEST_ENABLE_ADDRBOOK = 100;
    private static final int REQUEST_ENABLE_PHONE_MATCH = 103;
    private static final int REQUEST_JOIN_GROUP = 102;
    private static final int REQUEST_MOVE_BUDDY_CONTACT_GROUP = 108;
    private static final int UI_MODE_APPS = 2;
    private static final int UI_MODE_CONTACTS = 0;
    private static final int UI_MODE_GROUPS = 1;
    private final String TAG = IMAddrBookListFragment.class.getSimpleName();
    private boolean isKeyboardOpen = false;
    /* access modifiers changed from: private */
    public View mAddContactsFTEView;
    /* access modifiers changed from: private */
    public MMContactsAppsListView mAppsListView;
    /* access modifiers changed from: private */
    public View mBtnInvite;
    @NonNull
    private ZMBuddyListListener mBuddyListLisener = new ZMBuddyListListener() {
        public void onBuddyListUpdate() {
            IMAddrBookListFragment.this.onBuddyListUpdate();
        }

        public void onBuddyInfoUpdate(@NonNull List<String> list, @NonNull List<String> list2) {
            IMAddrBookListFragment.this.onBuddyInfoUpdate(list, list2);
        }
    };
    @Nullable
    private View mContentView;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    private IMDirectoryRecyclerView mDirectoryRecyclerView;
    /* access modifiers changed from: private */
    public ZMSearchBar mEdtAppSearch;
    /* access modifiers changed from: private */
    public ZMSearchBar mEdtGroupSearch;
    private ZMSearchBar mEdtSearch;
    @NonNull
    private IIMCallbackUIListener mGroupMemberSynchronizer = new SimpleIMCallbackUIListener() {
        public void Notify_AsyncMUCGroupInfoUpdated(String str) {
            IMAddrBookListFragment.this.onZoomMessengerMUCGroupInfoUpdatedImpl(str);
        }
    };
    /* access modifiers changed from: private */
    public MMContactsGroupListView mGroupsListView;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new MyHandler(this);
    @NonNull
    private Set<String> mIgnoreHintRequestId = new HashSet();
    private ImageView mImgNoBuddy;
    private View mPanelAppSearchBar;
    /* access modifiers changed from: private */
    public FrameLayout mPanelApps;
    private View mPanelContacts;
    private View mPanelGroupOperator;
    private View mPanelGroupSearchBar;
    /* access modifiers changed from: private */
    public FrameLayout mPanelGroups;
    private View mPanelJoinPublicGroup;
    private View mPanelNoItemMsg;
    private View mPanelSearchBar;
    /* access modifiers changed from: private */
    public ZMSearchBar mPanelSearchBarReal;
    private View mPanelTabApps;
    private View mPanelTabContacts;
    private View mPanelTabGroups;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Set<String> mPendingUpdatedGroups = new HashSet();
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableAppFilter = new Runnable() {
        public void run() {
            String text = IMAddrBookListFragment.this.mPanelSearchBarReal.getText();
            IMAddrBookListFragment.this.mAppsListView.filter(text);
            if ((text.length() <= 0 || IMAddrBookListFragment.this.mAppsListView.getCount() <= 0) && IMAddrBookListFragment.this.mPanelTitleBar.getVisibility() != 0) {
                IMAddrBookListFragment.this.mPanelApps.setForeground(IMAddrBookListFragment.this.mDimmedForground);
            } else {
                IMAddrBookListFragment.this.mPanelApps.setForeground(null);
            }
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableGroupFilter = new Runnable() {
        public void run() {
            String text = IMAddrBookListFragment.this.mPanelSearchBarReal.getText();
            IMAddrBookListFragment.this.mGroupsListView.filter(text);
            if ((text.length() <= 0 || IMAddrBookListFragment.this.mGroupsListView.getCount() <= 0) && IMAddrBookListFragment.this.mPanelTitleBar.getVisibility() != 0) {
                IMAddrBookListFragment.this.mPanelGroups.setForeground(IMAddrBookListFragment.this.mDimmedForground);
            } else {
                IMAddrBookListFragment.this.mPanelGroups.setForeground(null);
            }
        }
    };
    private TextView mTxtNoContactsMessage;
    /* access modifiers changed from: private */
    public int mUIMode = 0;
    @NonNull
    private HashSet<String> mUpdatedBuddyJids = new HashSet<>();
    @Nullable
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onSearchBuddy(String str, int i) {
            IMAddrBookListFragment.this.onZoomMessengerSearchBuddy(true, str, i);
        }

        public void onSearchBuddyByKey(String str, int i) {
            IMAddrBookListFragment.this.onZoomMessengerSearchBuddy(false, str, i);
        }

        public void notifyStarSessionDataUpdate() {
            IMAddrBookListFragment.this.onStarSessionDataUpdate();
        }

        public void onConnectReturn(int i) {
            IMAddrBookListFragment.this.onZoomMessengerConnectReturn(i);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            IMAddrBookListFragment.this.onZoomMessengerMUCGroupInfoUpdatedImpl(str);
        }

        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            IMAddrBookListFragment.this.onZoomMessengerGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            if (IMAddrBookListFragment.this.mGroupsListView != null) {
                IMAddrBookListFragment.this.mGroupsListView.onGroupDestory(str);
            }
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            if (i == 0 && IMAddrBookListFragment.this.mGroupsListView != null) {
                IMAddrBookListFragment.this.mGroupsListView.onGroupDestory(str2);
            }
        }

        public void OnPersonalGroupResponse(byte[] bArr) {
            IMAddrBookListFragment.this.OnPersonalGroupResponse(bArr);
        }

        public void NotifyPersonalGroupSync(int i, String str, @NonNull List<String> list, String str2, String str3) {
            IMAddrBookListFragment.this.NotifyPersonalGroupSync(i, str, list, str2, str3);
        }

        public void indicate_BuddyBlockedByIB(List<String> list) {
            IMAddrBookListFragment.this.indicate_BuddyBlockedByIB(list);
        }
    };
    @NonNull
    private IZoomPublicRoomSearchUIListener mZoomPublicRoomSearchUIListener = new IZoomPublicRoomSearchUIListener() {
        public void onSearchResponse(int i, int i2, int i3) {
        }

        public void onJoinRoom(String str, int i) {
            IMAddrBookListFragment.this.onJoinRoom(str, i);
        }

        public void onForbidJoinRoom(String str, int i) {
            IMAddrBookListFragment.this.onForbidJoinRoom(str, i);
        }
    };

    public static class BuddyContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_ALERT_WHEN_AVAILABLE = 4;
        public static final int ACTION_COLLAPSE_CURRENT_GROUP = 6;
        public static final int ACTION_COPY = 1;
        public static final int ACTION_DELETE_CONTACTS = 5;
        public static final int ACTION_MOVE = 2;
        public static final int ACTION_REMOVE = 3;
        public static final int ACTION_STAR = 0;

        public BuddyContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static class ContactsPermissionFailedDialog extends ZMDialogFragment {
        public static void show(@NonNull FragmentManager fragmentManager) {
            Bundle bundle = new Bundle();
            ContactsPermissionFailedDialog contactsPermissionFailedDialog = new ContactsPermissionFailedDialog();
            contactsPermissionFailedDialog.setArguments(bundle);
            contactsPermissionFailedDialog.show(fragmentManager, ContactsPermissionFailedDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            ZMAlertDialog create = new Builder(getActivity()).setMessage(C4558R.string.zm_lbl_open_contacts_permission_33300).setPositiveButton(C4558R.string.zm_btn_open_settings_33300, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("package:");
                    sb.append(ContactsPermissionFailedDialog.this.getActivity().getPackageName());
                    ContactsPermissionFailedDialog.this.startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(sb.toString())));
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
            create.setCanceledOnTouchOutside(true);
            return create;
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
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_ADDRBOOKITEM);
            this.mAdapter = createUpdateAdapter();
            String screenName = iMAddrBookItem.getScreenName();
            if (StringUtil.isEmptyOrNull(screenName)) {
                str = getString(C4558R.string.zm_title_invite);
            } else {
                str = getString(C4558R.string.zm_title_invite_xxx, screenName);
            }
            ZMAlertDialog create = new Builder(getActivity()).setTitle((CharSequence) str).setAdapter(this.mAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContextMenuFragment.this.onSelectItem(i);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }

        private ZMMenuAdapter<ContextMenuItem> createUpdateAdapter() {
            Bundle arguments = getArguments();
            IZMMenuItem[] iZMMenuItemArr = null;
            if (arguments == null) {
                return null;
            }
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_ADDRBOOKITEM);
            if (iMAddrBookItem != null) {
                ContextMenuItem[] contextMenuItemArr = new ContextMenuItem[(iMAddrBookItem.getPhoneNumberCount() + iMAddrBookItem.getEmailCount())];
                int i = 0;
                int i2 = 0;
                while (i < iMAddrBookItem.getPhoneNumberCount()) {
                    String phoneNumber = iMAddrBookItem.getPhoneNumber(i);
                    int i3 = i2 + 1;
                    contextMenuItemArr[i2] = new ContextMenuItem(phoneNumber, phoneNumber, null);
                    i++;
                    i2 = i3;
                }
                int i4 = 0;
                while (i4 < iMAddrBookItem.getEmailCount()) {
                    String email = iMAddrBookItem.getEmail(i4);
                    int i5 = i2 + 1;
                    contextMenuItemArr[i2] = new ContextMenuItem(email, null, email);
                    i4++;
                    i2 = i5;
                }
                iZMMenuItemArr = contextMenuItemArr;
            }
            ZMMenuAdapter<ContextMenuItem> zMMenuAdapter = this.mAdapter;
            if (zMMenuAdapter == null) {
                this.mAdapter = new ZMMenuAdapter<>(getActivity(), false);
            } else {
                zMMenuAdapter.clear();
            }
            if (iZMMenuItemArr != null) {
                this.mAdapter.addAll((MenuItemType[]) iZMMenuItemArr);
            }
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
            ContextMenuItem contextMenuItem = (ContextMenuItem) this.mAdapter.getItem(i);
            if (contextMenuItem != null) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                    if (supportFragmentManager != null) {
                        if (contextMenuItem.isPhoneNumberItem()) {
                            IMAddrBookListFragment.inviteBySMS(zMActivity, supportFragmentManager, contextMenuItem.getPhoneNumber());
                        } else {
                            IMAddrBookListFragment.inviteByEmail(zMActivity, supportFragmentManager, contextMenuItem.getEmail());
                        }
                    }
                }
            }
        }
    }

    static class ContextMenuItem extends ZMSimpleMenuItem {
        private String mEmail;
        private String mPhoneNumber;

        public ContextMenuItem(String str, String str2, String str3) {
            super(0, str);
            this.mPhoneNumber = str2;
            this.mEmail = str3;
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

    public static class CustomGroupContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_ADD = 1;
        public static final int ACTION_DELETE = 0;
        public static final int ACTION_RENAME = 2;

        public CustomGroupContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static class EnablePhoneMatchDialog extends ZMDialogFragment {
        public static void show(@NonNull FragmentManager fragmentManager, Fragment fragment, int i) {
            Bundle bundle = new Bundle();
            EnablePhoneMatchDialog enablePhoneMatchDialog = new EnablePhoneMatchDialog();
            enablePhoneMatchDialog.setArguments(bundle);
            enablePhoneMatchDialog.setTargetFragment(fragment, i);
            enablePhoneMatchDialog.show(fragmentManager, EnablePhoneMatchDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            ZMAlertDialog create = new Builder(getActivity()).setTitle(C4558R.string.zm_title_enable_phone_match_33300).setMessage(C4558R.string.zm_lbl_enable_phone_match_33300).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Fragment targetFragment = EnablePhoneMatchDialog.this.getTargetFragment();
                    if (targetFragment != null) {
                        targetFragment.onActivityResult(EnablePhoneMatchDialog.this.getTargetRequestCode(), -1, null);
                    }
                }
            }).setNegativeButton(C4558R.string.zm_btn_not_allow_33300, (DialogInterface.OnClickListener) null).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }
    }

    static class MyHandler extends Handler {
        static final int MSG_REFRESH_ALL = 1;
        static final int MSG_REFRESH_GROUP = 2;
        WeakReference<IMAddrBookListFragment> fragmentWeakReference;

        MyHandler(IMAddrBookListFragment iMAddrBookListFragment) {
            this.fragmentWeakReference = new WeakReference<>(iMAddrBookListFragment);
        }

        public void handleMessage(@NonNull Message message) {
            IMAddrBookListFragment iMAddrBookListFragment = (IMAddrBookListFragment) this.fragmentWeakReference.get();
            if (iMAddrBookListFragment != null) {
                switch (message.what) {
                    case 1:
                        boolean z = true;
                        if (message.arg1 != 1) {
                            z = false;
                        }
                        iMAddrBookListFragment.refreshAll(z);
                        break;
                    case 2:
                        if (!iMAddrBookListFragment.mPendingUpdatedGroups.isEmpty()) {
                            for (String addOrUpdateGroup : iMAddrBookListFragment.mPendingUpdatedGroups) {
                                iMAddrBookListFragment.mGroupsListView.addOrUpdateGroup(addOrUpdateGroup, false);
                            }
                            iMAddrBookListFragment.mGroupsListView.notifyDataSetChanged();
                            iMAddrBookListFragment.mPendingUpdatedGroups.clear();
                            break;
                        }
                        break;
                }
            }
        }
    }

    public static class RoomListContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_AUDIO_CALL = 1;
        public static final int ACTION_COPY_GROUPS = 5;
        public static final int ACTION_INVITE_CALL = 2;
        public static final int ACTION_STAR_ROOM = 3;
        public static final int ACTION_UNSTAR_ROOM = 4;
        public static final int ACTION_VIDEO_CALL = 0;

        public RoomListContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    private void onPhoneBindByOther() {
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
    }

    public void onIMBuddySort() {
    }

    public void onIMLocalStatusChanged(int i) {
    }

    public void onIMReceived(IMMessage iMMessage) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public void onSubscriptionRequest() {
    }

    public void onSubscriptionUpdate() {
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    private void pushBuddyUpdateEventToQueue(@Nullable String str) {
        if (str != null) {
            this.mUpdatedBuddyJids.add(str);
        }
    }

    @NonNull
    public static IMAddrBookListFragment newInstance(int i) {
        Bundle bundle = new Bundle();
        IMAddrBookListFragment iMAddrBookListFragment = new IMAddrBookListFragment();
        iMAddrBookListFragment.setArguments(bundle);
        return iMAddrBookListFragment;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_addrbook_list, viewGroup, false);
        this.mDirectoryRecyclerView = (IMDirectoryRecyclerView) inflate.findViewById(C4558R.C4560id.directoryRecyclerView);
        this.mEdtSearch = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mPanelSearchBarReal = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearchBarReal);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mTxtNoContactsMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtNoContactsMessage);
        this.mImgNoBuddy = (ImageView) inflate.findViewById(C4558R.C4560id.imgNoBuddy);
        this.mBtnInvite = inflate.findViewById(C4558R.C4560id.btnInvite);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mPanelTabContacts = inflate.findViewById(C4558R.C4560id.panelTabContacts);
        this.mPanelTabGroups = inflate.findViewById(C4558R.C4560id.panelTabGroups);
        this.mPanelGroupOperator = inflate.findViewById(C4558R.C4560id.panelGroupsOperator);
        this.mPanelContacts = inflate.findViewById(C4558R.C4560id.panelContacts);
        this.mPanelGroups = (FrameLayout) inflate.findViewById(C4558R.C4560id.panelGroups);
        this.mGroupsListView = (MMContactsGroupListView) inflate.findViewById(C4558R.C4560id.groupsListView);
        this.mEdtGroupSearch = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.edtGroupSearch);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mPanelGroupSearchBar = inflate.findViewById(C4558R.C4560id.panelGroupSearchBar);
        this.mPanelJoinPublicGroup = inflate.findViewById(C4558R.C4560id.panelJoinPublicGroup);
        this.mPanelTabApps = inflate.findViewById(C4558R.C4560id.panelTabApps);
        this.mPanelApps = (FrameLayout) inflate.findViewById(C4558R.C4560id.panelApps);
        this.mAppsListView = (MMContactsAppsListView) inflate.findViewById(C4558R.C4560id.appsListView);
        this.mPanelAppSearchBar = inflate.findViewById(C4558R.C4560id.panelAppSearchBar);
        this.mEdtAppSearch = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.edtAppSearch);
        this.mPanelSearchBarReal.setVisibility(4);
        this.mPanelTabContacts.setOnClickListener(this);
        this.mPanelTabGroups.setOnClickListener(this);
        this.mGroupsListView.setEmptyView(inflate.findViewById(C4558R.C4560id.panelGroupsNoItemMsg));
        this.mGroupsListView.setParentFragment(this);
        this.mAppsListView.setEmptyView(inflate.findViewById(C4558R.C4560id.panelAppsNoItemMsg));
        this.mAppsListView.setParentFragment(this);
        inflate.findViewById(C4558R.C4560id.panelNewGroup).setOnClickListener(this);
        this.mPanelJoinPublicGroup.setOnClickListener(this);
        this.mPanelTabApps.setOnClickListener(this);
        if (!isFTEShowed()) {
            this.mBtnInvite.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (VERSION.SDK_INT >= 16) {
                        IMAddrBookListFragment.this.mBtnInvite.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        IMAddrBookListFragment.this.mBtnInvite.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    IMAddrBookListFragment.this.showAddContactFTE();
                }
            });
        }
        this.mEdtSearch.setOnClickListener(this);
        this.mPanelSearchBarReal.setOnSearchBarListener(new OnSearchBarListener() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                if (IMAddrBookListFragment.this.mUIMode == 1) {
                    IMAddrBookListFragment.this.mHandler.removeCallbacks(IMAddrBookListFragment.this.mRunnableGroupFilter);
                    IMAddrBookListFragment.this.mHandler.postDelayed(IMAddrBookListFragment.this.mRunnableGroupFilter, 300);
                } else if (IMAddrBookListFragment.this.mUIMode == 2) {
                    IMAddrBookListFragment.this.mHandler.removeCallbacks(IMAddrBookListFragment.this.mRunnableAppFilter);
                    IMAddrBookListFragment.this.mHandler.postDelayed(IMAddrBookListFragment.this.mRunnableAppFilter, 300);
                }
            }

            public void onClearSearch() {
                IMAddrBookListFragment.this.onClickBtnClearSearchView();
            }
        });
        this.mBtnInvite.setOnClickListener(this);
        if ((getActivity() instanceof IMActivity) && !((IMActivity) getActivity()).isKeyboardOpen()) {
            onKeyboardClosed();
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = zoomMessenger == null || zoomMessenger.imChatGetOption() == 2;
        if ((zoomMessenger == null || zoomMessenger.isAddContactDisable()) && z) {
            this.mBtnInvite.setVisibility(8);
        }
        this.mPanelNoItemMsg.setVisibility(8);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        if (bundle != null) {
            updateUIMode(bundle.getInt(EXTRA_UI_MODE, 0), true);
        }
        ZoomPublicRoomSearchUI.getInstance().addListener(this.mZoomPublicRoomSearchUIListener);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        ZMBuddySyncInstance.getInsatance().addListener(this.mBuddyListLisener);
        IMCallbackUI.getInstance().addListener(this.mGroupMemberSynchronizer);
        compatPCModeForSearch();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return inflate;
    }

    private void compatPCModeForSearch() {
        C25859 r0 = new OnFocusChangeListener() {
            public void onFocusChange(@NonNull final View view, boolean z) {
                if (z) {
                    IMAddrBookListFragment.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (IMAddrBookListFragment.this.isAdded() && IMAddrBookListFragment.this.isResumed()) {
                                if ((view == IMAddrBookListFragment.this.mEdtAppSearch.getEditText() || view == IMAddrBookListFragment.this.mEdtGroupSearch.getEditText()) && ((EditText) view).hasFocus()) {
                                    IMAddrBookListFragment.this.onKeyboardOpen();
                                }
                            }
                        }
                    }, 500);
                }
            }
        };
        this.mEdtAppSearch.getEditText().setOnFocusChangeListener(r0);
        this.mEdtGroupSearch.getEditText().setOnFocusChangeListener(r0);
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
        sb.append(IMAddrBookListFragment.class.getName());
        sb.append(".State");
        bundle.putSparseParcelableArray(sb.toString(), sparseArray);
        bundle.putInt(EXTRA_UI_MODE, this.mUIMode);
        super.onSaveInstanceState(bundle);
    }

    public void onJoinRoom(String str, final int i) {
        dismissWaitingMakeGroupDialog();
        if (i == 0) {
            this.mGroupsListView.refreshAllData();
            this.mGroupsListView.jump2Group(str);
        }
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                IMAddrBookListFragment iMAddrBookListFragment = (IMAddrBookListFragment) iUIElement;
                if (iMAddrBookListFragment != null) {
                    iMAddrBookListFragment.handleJoinGroup(i);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onForbidJoinRoom(String str, int i) {
        if (i == 1) {
            InformationBarriesDialog.show((Context) getActivity(), C4558R.string.zm_mm_information_barries_dialog_join_channel_115072, false);
            dismissWaitingMakeGroupDialog();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            onSetPhoneNumberDone();
        } else if (i == 101 && i2 == -1) {
            createGroup(intent);
        } else if (i == 102 && i2 == -1) {
            joinPublicGroup(intent);
        } else if (i == 103 && i2 == -1) {
            if (PTApp.getInstance().isPhoneNumberRegistered()) {
                showPhoneContactsInZoom();
            }
        } else if (i == 104 && i2 == -1) {
            showPhoneMatchFragment();
        } else if (i == 106 && i2 == -1) {
            addBuddyToCustomGroup(intent);
        } else if (i == 107 && i2 == -1) {
            onCopyBuddyInCustomGroup(intent);
        } else if (i == 108 && i2 == -1) {
            onMoveBuddyInCustomGroup(intent);
        }
    }

    private void onMoveBuddyInCustomGroup(@Nullable Intent intent) {
        if (intent != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) intent.getSerializableExtra("EXTRA_BUDDY_IN_CUSTOM_GROUP");
            MMZoomBuddyGroup mMZoomBuddyGroup = (MMZoomBuddyGroup) intent.getSerializableExtra(EXTRA_BUDDY_GROUP);
            MMZoomBuddyGroup mMZoomBuddyGroup2 = (MMZoomBuddyGroup) intent.getSerializableExtra(SelectCustomGroupFragment.RESULT_GROUP);
            if (iMAddrBookItem != null && mMZoomBuddyGroup2 != null && mMZoomBuddyGroup != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(iMAddrBookItem.getJid());
                    String moveBuddyFromPersonalBuddyGroup = zoomMessenger.moveBuddyFromPersonalBuddyGroup(arrayList, mMZoomBuddyGroup.getXmppGroupID(), mMZoomBuddyGroup2.getXmppGroupID());
                    if (!TextUtils.isEmpty(moveBuddyFromPersonalBuddyGroup)) {
                        this.mIgnoreHintRequestId.add(moveBuddyFromPersonalBuddyGroup);
                    }
                }
            }
        }
    }

    private void onCopyBuddyInCustomGroup(@Nullable Intent intent) {
        if (intent != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) intent.getSerializableExtra("EXTRA_BUDDY_IN_CUSTOM_GROUP");
            MMZoomBuddyGroup mMZoomBuddyGroup = (MMZoomBuddyGroup) intent.getSerializableExtra(SelectCustomGroupFragment.RESULT_GROUP);
            if (iMAddrBookItem != null && mMZoomBuddyGroup != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(iMAddrBookItem.getJid());
                    zoomMessenger.addBuddyToPersonalBuddyGroup(arrayList, mMZoomBuddyGroup.getXmppGroupID());
                }
            }
        }
    }

    private void addBuddyToCustomGroup(@Nullable Intent intent) {
        if (intent != null) {
            MMZoomBuddyGroup mMZoomBuddyGroup = (MMZoomBuddyGroup) intent.getSerializableExtra(EXTRA_BUDDY_GROUP);
            List<IMAddrBookItem> list = (List) intent.getSerializableExtra("selectedItems");
            if (mMZoomBuddyGroup != null && !CollectionsUtil.isCollectionEmpty(list)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ArrayList arrayList = new ArrayList();
                    for (IMAddrBookItem jid : list) {
                        arrayList.add(jid.getJid());
                    }
                    zoomMessenger.addBuddyToPersonalBuddyGroup(arrayList, mMZoomBuddyGroup.getXmppGroupID());
                }
            }
        }
    }

    private void joinPublicGroup(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectItems");
            if (arrayList != null && arrayList.size() != 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (!zoomMessenger.isConnectionGood()) {
                        showConnectionError();
                        return;
                    }
                    ZoomPublicRoomSearchData publicRoomSearchData = zoomMessenger.getPublicRoomSearchData();
                    if (publicRoomSearchData != null) {
                        boolean z = false;
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            z |= publicRoomSearchData.joinRoom(((MMZoomXMPPRoom) it.next()).getJid());
                        }
                        if (z) {
                            showWaitingMakeGroupDialog();
                        } else {
                            showMakeGroupFailureMessage(1, null);
                        }
                    }
                }
            }
        }
    }

    private void createGroup(@Nullable Intent intent) {
        if (intent != null) {
            String stringExtra = intent.getStringExtra(MMCreateGroupFragment.RESULT_ARG_GROUP_NAME);
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            int intExtra = intent.getIntExtra(MMCreateGroupFragment.RESULT_ARGS_GROUP_TYPE, 16);
            if (!StringUtil.isEmptyOrNull(stringExtra) && intExtra != 16) {
                if (intExtra == 14 || !(arrayList == null || arrayList.size() == 0 || StringUtil.isEmptyOrNull(stringExtra))) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        if (!zoomMessenger.isConnectionGood()) {
                            showConnectionError();
                            return;
                        }
                        if (intExtra == 12 && !intent.getBooleanExtra(MMCreateGroupFragment.RESULT_ARGS_ONLY_ORGANIZAION, false)) {
                            intExtra = 8;
                        }
                        if (intent.getBooleanExtra(MMCreateGroupFragment.RESULT_ARGS_ACCESS_HISTORY, false)) {
                            intExtra |= 32;
                        }
                        ArrayList arrayList2 = new ArrayList();
                        if (arrayList != null) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                String jid = ((IMAddrBookItem) arrayList.get(i)).getJid();
                                if (!StringUtil.isEmptyOrNull(jid)) {
                                    arrayList2.add(jid);
                                }
                            }
                        }
                        MakeGroupResult makeGroup = zoomMessenger.makeGroup(arrayList2, stringExtra, (long) intExtra);
                        if (makeGroup == null || !makeGroup.getResult()) {
                            showMakeGroupFailureMessage(1, null);
                        } else {
                            showWaitingMakeGroupDialog();
                        }
                    }
                }
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnInvite) {
            onClickBtnInvite();
        } else if (id == C4558R.C4560id.panelConnectionAlert) {
            onClickPanelConnectionAlert();
        } else if (id == C4558R.C4560id.edtSearch) {
            onClickEdtSearch();
        } else if (id == C4558R.C4560id.panelTabContacts) {
            onClickPanelTabContacts();
        } else if (id == C4558R.C4560id.panelTabGroups) {
            onClickPanelTabGroups();
        } else if (id == C4558R.C4560id.panelNewGroup) {
            onClickPanelNewGroup();
        } else if (id == C4558R.C4560id.panelJoinPublicGroup) {
            onClickPanelJoinPublicGroup();
        } else if (id == C4558R.C4560id.panelTabApps) {
            onClickPanelTabApps();
        }
    }

    /* access modifiers changed from: private */
    public void onClickPanelJoinPublicGroup() {
        MMJoinPublicGroupFragment.showAsActivity(this, 102);
        ZoomLogEventTracking.eventTrackJoinGroup();
    }

    /* access modifiers changed from: private */
    public void onClickPanelNewGroup() {
        MMCreateGroupFragment.showAsActivity((Fragment) this, 101);
    }

    private void onClickPanelTabContacts() {
        updateUIMode(0, true);
    }

    private void onClickPanelTabGroups() {
        updateUIMode(1, true);
    }

    private void onClickPanelTabApps() {
        updateUIMode(2, true);
    }

    private void onClickEdtSearch() {
        MMContactSearchFragment.showAsFragment(this);
    }

    /* access modifiers changed from: private */
    public void onClickBtnClearSearchView() {
        this.mPanelSearchBarReal.setText("");
        if (!this.isKeyboardOpen) {
            int i = this.mUIMode;
            if (i == 2) {
                this.mPanelTitleBar.setVisibility(0);
                this.mPanelSearchBarReal.setVisibility(4);
                this.mPanelAppSearchBar.setVisibility(0);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        IMAddrBookListFragment.this.mAppsListView.requestLayout();
                    }
                });
            } else if (i == 1) {
                this.mPanelTitleBar.setVisibility(0);
                this.mPanelSearchBarReal.setVisibility(4);
                this.mPanelGroupSearchBar.setVisibility(0);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        IMAddrBookListFragment.this.mGroupsListView.requestLayout();
                    }
                });
            }
        }
    }

    private void onClickBtnInvite() {
        if (PTApp.getInstance().getZoomMessenger() != null) {
            showInvitePopWindow();
        }
    }

    /* access modifiers changed from: private */
    public void onClickPanelAddContact() {
        MMAddBuddyNewFragment.showAsActivity(this);
    }

    /* access modifiers changed from: private */
    public void onClickPanelAddApp() {
        String marketplaceURL = PTApp.getInstance().getMarketplaceURL();
        if (!TextUtils.isEmpty(marketplaceURL) && !TextUtils.isEmpty(marketplaceURL) && getContext() != null) {
            UIUtil.openURL(getContext(), marketplaceURL);
        }
    }

    /* access modifiers changed from: private */
    public void onClickPanelAddContactGroup() {
        AddContactGroupFragment.showAsActivity(this, 105);
    }

    private void onClickPanelConnectionAlert() {
        IMActivity iMActivity = (IMActivity) getActivity();
        if (iMActivity != null) {
            if (!NetworkUtil.hasDataNetwork(iMActivity)) {
                Toast.makeText(iMActivity, C4558R.string.zm_alert_network_disconnected, 1).show();
                return;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (zoomMessenger.isStreamConflict()) {
                    AutoStreamConflictChecker.getInstance().showStreamConflictMessage(getActivity());
                } else {
                    zoomMessenger.trySignon();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZMContactsPhoneAddressEvent zMContactsPhoneAddressEvent) {
        if (isAdded()) {
            showPhoneContactsInZoom();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(@NonNull ZMContactsBuddyEvent zMContactsBuddyEvent) {
        if (isAdded()) {
            onBuddyClick(zMContactsBuddyEvent.getBuddy());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(@NonNull ZMContactsGroupLongClickEvent zMContactsGroupLongClickEvent) {
        if (isAdded()) {
            onCustomGroupLongClick(zMContactsGroupLongClickEvent.getGroup());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(@NonNull ZMContactsBuddLongClickyEvent zMContactsBuddLongClickyEvent) {
        if (isAdded()) {
            onBuddyLongClick(zMContactsBuddLongClickyEvent.getBuddy(), zMContactsBuddLongClickyEvent.getGroup());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZMStarEvent zMStarEvent) {
        if (isAdded()) {
            IMDirectoryRecyclerView iMDirectoryRecyclerView = this.mDirectoryRecyclerView;
            if (iMDirectoryRecyclerView != null) {
                iMDirectoryRecyclerView.onStarSessionDataUpdate();
            }
            MMContactsGroupListView mMContactsGroupListView = this.mGroupsListView;
            if (mMContactsGroupListView != null) {
                mMContactsGroupListView.refreshAllData();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(@NonNull ZMAlertAvailable zMAlertAvailable) {
        if (isAdded()) {
            IMDirectoryAdapter iMDirectoryAdapter = (IMDirectoryAdapter) this.mDirectoryRecyclerView.getAdapter();
            ArrayList arrayList = new ArrayList();
            arrayList.add(zMAlertAvailable.getJid());
            if (iMDirectoryAdapter != null) {
                iMDirectoryAdapter.updateBuddyInfo(arrayList);
            }
        }
    }

    public void showPhoneContactsInZoom() {
        if (isVisible()) {
            if (!PTApp.getInstance().isPhoneNumberRegistered()) {
                showPhoneMatchFragment();
            } else if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
                MMPhoneContactsInZoomFragment.showAsActivity((ZMActivity) getContext(), 0);
            } else {
                zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 1000);
            }
        }
    }

    private void onCustomGroupLongClick(@Nullable final MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (mMZoomBuddyGroup != null && isVisible()) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
                ArrayList arrayList = new ArrayList();
                String name = mMZoomBuddyGroup.getName();
                arrayList.add(new CustomGroupContextMenuItem(zMActivity.getString(C4558R.string.zm_msg_copy_contact_68451), 1));
                arrayList.add(new CustomGroupContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_rename_contact_group_68451), 2));
                arrayList.add(new CustomGroupContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_delete_group_68451), 0));
                zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
                ZMAlertDialog create = new Builder(zMActivity).setTitle((CharSequence) name).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMAddrBookListFragment.this.onSelectCustomGroupContextMenuItem(mMZoomBuddyGroup, (CustomGroupContextMenuItem) zMMenuAdapter.getItem(i));
                    }
                }).create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectCustomGroupContextMenuItem(@Nullable MMZoomBuddyGroup mMZoomBuddyGroup, @Nullable CustomGroupContextMenuItem customGroupContextMenuItem) {
        if (mMZoomBuddyGroup != null && customGroupContextMenuItem != null) {
            switch (customGroupContextMenuItem.getAction()) {
                case 0:
                    deleteBuddyGroup(mMZoomBuddyGroup);
                    break;
                case 1:
                    addBuddyToBuddyGroup(mMZoomBuddyGroup);
                    break;
                case 2:
                    RenameContactGroupFragment.showAsActivity(this, mMZoomBuddyGroup.getXmppGroupID(), 0);
                    break;
            }
        }
    }

    private void addBuddyToBuddyGroup(@Nullable MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (mMZoomBuddyGroup != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddyGroup buddyGroupByJid = zoomMessenger.getBuddyGroupByJid(mMZoomBuddyGroup.getId());
                    if (buddyGroupByJid != null) {
                        SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
                        selectContactsParamter.includeRobot = true;
                        selectContactsParamter.title = activity.getString(C4558R.string.zm_mm_title_add_contacts);
                        selectContactsParamter.isSingleChoice = false;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(EXTRA_BUDDY_GROUP, mMZoomBuddyGroup);
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (int i = 0; i < buddyGroupByJid.getBuddyCount(); i++) {
                            ZoomBuddy buddyAt = buddyGroupByJid.getBuddyAt(i);
                            if (buddyAt != null) {
                                arrayList.add(buddyAt.getJid());
                            }
                        }
                        selectContactsParamter.preSelectedItems = arrayList;
                        MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, 106, bundle);
                    }
                }
            }
        }
    }

    private void deleteBuddyGroup(MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (mMZoomBuddyGroup.getBuddyCount() == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.deletePersonalBuddyGroup(mMZoomBuddyGroup.getXmppGroupID());
            }
        } else {
            DeleteCustomGroupDialog newInstance = DeleteCustomGroupDialog.newInstance(mMZoomBuddyGroup);
            if (newInstance != null) {
                newInstance.show(getFragmentManager(), DeleteCustomGroupDialog.class.getName());
            }
        }
    }

    private void onBuddyLongClick(@Nullable final IMAddrBookItem iMAddrBookItem, @Nullable final MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (isVisible() && iMAddrBookItem != null && !iMAddrBookItem.isPending() && ((!iMAddrBookItem.isFromPhoneContacts() || AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) && !iMAddrBookItem.isMyNote() && (!iMAddrBookItem.getIsRoomDevice() || AccessibilityUtil.isSpokenFeedbackEnabled(getContext())))) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
                    ArrayList arrayList = new ArrayList();
                    String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(null, iMAddrBookItem);
                    if (!((iMAddrBookItem.isFromPhoneContacts() && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) || (iMAddrBookItem.getIsRoomDevice() && !AccessibilityUtil.isSpokenFeedbackEnabled(getContext())))) {
                        boolean isStarSession = zoomMessenger.isStarSession(iMAddrBookItem.getJid());
                        if (AlertWhenAvailableHelper.getInstance().showAlertWhenAvailable(iMAddrBookItem.getJid())) {
                            arrayList.add(new BuddyContextMenuItem(AlertWhenAvailableHelper.getInstance().getMenuString(iMAddrBookItem), 4));
                        }
                        arrayList.add(new BuddyContextMenuItem(zMActivity.getString(isStarSession ? C4558R.string.zm_msg_unstar_contact_68451 : C4558R.string.zm_msg_star_contact_68451), 0));
                        if ((mMZoomBuddyGroup == null || !mMZoomBuddyGroup.isZoomRoomGroup()) && zoomMessenger.personalGroupGetOption() == 1) {
                            arrayList.add(new BuddyContextMenuItem(zMActivity.getString(C4558R.string.zm_msg_copy_to_group_68451), 1));
                        }
                        if (mMZoomBuddyGroup != null) {
                            if (mMZoomBuddyGroup.getType() == 500 && zoomMessenger.personalGroupGetOption() == 1) {
                                arrayList.add(new BuddyContextMenuItem(zMActivity.getString(C4558R.string.zm_msg_move_contact_68451), 2));
                                arrayList.add(new BuddyContextMenuItem(zMActivity.getString(C4558R.string.zm_msg_remove_from_group_68451), 3));
                            } else if (mMZoomBuddyGroup.getType() == 0 && zoomMessenger.canRemoveBuddy(iMAddrBookItem.getJid())) {
                                arrayList.add(new BuddyContextMenuItem(zMActivity.getString(C4558R.string.zm_mi_remove_zoom_contact), 5));
                            }
                        }
                    }
                    if (AccessibilityUtil.isSpokenFeedbackEnabled(zMActivity)) {
                        arrayList.add(new BuddyContextMenuItem(zMActivity.getString(C4558R.string.zm_msg_collapse_current_group_103635), 6));
                    }
                    if (arrayList.size() != 0) {
                        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
                        ZMAlertDialog create = new Builder(zMActivity).setTitle((CharSequence) buddyDisplayName).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                IMAddrBookListFragment.this.onSelectBuddyContextMenuItem(iMAddrBookItem, mMZoomBuddyGroup, (BuddyContextMenuItem) zMMenuAdapter.getItem(i));
                            }
                        }).create();
                        create.setCanceledOnTouchOutside(true);
                        create.show();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectBuddyContextMenuItem(@Nullable IMAddrBookItem iMAddrBookItem, MMZoomBuddyGroup mMZoomBuddyGroup, @Nullable BuddyContextMenuItem buddyContextMenuItem) {
        if (iMAddrBookItem != null && buddyContextMenuItem != null) {
            switch (buddyContextMenuItem.getAction()) {
                case 0:
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        zoomMessenger.starSessionSetStar(iMAddrBookItem.getJid(), !zoomMessenger.isStarSession(iMAddrBookItem.getJid()));
                        break;
                    } else {
                        return;
                    }
                case 1:
                    copyBuddyToContactGroup(iMAddrBookItem);
                    break;
                case 2:
                    moveBuddyToContactGroup(iMAddrBookItem, mMZoomBuddyGroup);
                    break;
                case 3:
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        RemoveBuddyFromGroupDialogFragment newInstance = RemoveBuddyFromGroupDialogFragment.newInstance(iMAddrBookItem, mMZoomBuddyGroup);
                        if (newInstance != null) {
                            newInstance.show(fragmentManager, RemoveBuddyFromGroupDialogFragment.class.getSimpleName());
                            break;
                        }
                    }
                    break;
                case 4:
                    AlertWhenAvailableHelper.getInstance().checkAndAddToAlertQueen((ZMActivity) getActivity(), iMAddrBookItem);
                    break;
                case 5:
                    deleteContacts(iMAddrBookItem, mMZoomBuddyGroup);
                    break;
                case 6:
                    this.mDirectoryRecyclerView.collapseGroup(iMAddrBookItem);
                    break;
            }
        }
    }

    private void deleteContacts(@Nullable final IMAddrBookItem iMAddrBookItem, MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (iMAddrBookItem != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (zoomMessenger.canRemoveBuddy(iMAddrBookItem.getJid())) {
                        new Builder(activity).setTitle((CharSequence) activity.getString(C4558R.string.zm_title_remove_contact, new Object[]{iMAddrBookItem.getScreenName()})).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                iMAddrBookItem.removeItem(IMAddrBookListFragment.this.getActivity());
                            }
                        }).create().show();
                    }
                    ZoomLogEventTracking.eventTrackRemoveContact();
                }
            }
        }
    }

    private void copyBuddyToContactGroup(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("EXTRA_BUDDY_IN_CUSTOM_GROUP", iMAddrBookItem);
            SelectCustomGroupFragment.showAsActivity(this, getString(C4558R.string.zm_msg_copy_contact_68451), bundle, 107, iMAddrBookItem.getJid());
        }
    }

    private void moveBuddyToContactGroup(@Nullable IMAddrBookItem iMAddrBookItem, @Nullable MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (iMAddrBookItem != null && mMZoomBuddyGroup != null && mMZoomBuddyGroup.getType() == 500) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("EXTRA_BUDDY_IN_CUSTOM_GROUP", iMAddrBookItem);
            bundle.putSerializable(EXTRA_BUDDY_GROUP, mMZoomBuddyGroup);
            SelectCustomGroupFragment.showAsActivity(this, getString(C4558R.string.zm_msg_move_contact_68451), bundle, 108, iMAddrBookItem.getJid());
        }
    }

    private void onBuddyClick(@Nullable final IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null && isAdded() && isVisible()) {
            if (iMAddrBookItem.isPending()) {
                new Builder(getContext()).setTitle((CharSequence) getString(C4558R.string.zm_title_remove_contact, iMAddrBookItem.getScreenName())).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        iMAddrBookItem.removeItem(IMAddrBookListFragment.this.getContext());
                    }
                }).create().show();
            } else {
                showUserActions(iMAddrBookItem);
            }
        }
    }

    private void showUserActions(@Nullable final IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (iMAddrBookItem.getAccountStatus() != 2) {
                    if (iMAddrBookItem.isZoomRoomContact()) {
                        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
                        ArrayList arrayList = new ArrayList();
                        String screenName = iMAddrBookItem.getScreenName();
                        arrayList.add(new RoomListContextMenuItem(zMActivity.getString(C4558R.string.zm_btn_video_call), 0));
                        arrayList.add(new RoomListContextMenuItem(zMActivity.getString(C4558R.string.zm_btn_audio_call), 1));
                        if (zoomMessenger != null) {
                            if (zoomMessenger.isStarSession(iMAddrBookItem.getJid()) || (!TextUtils.isEmpty(iMAddrBookItem.getSortKey()) && iMAddrBookItem.getSortKey().charAt(0) == '!')) {
                                arrayList.add(new RoomListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_unstarred_zoom_room_65147), 4));
                            } else {
                                arrayList.add(new RoomListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_starred_zoom_room_65147), 3));
                            }
                            if (zoomMessenger.personalGroupGetOption() == 1) {
                                arrayList.add(new RoomListContextMenuItem(zMActivity.getString(C4558R.string.zm_msg_add_contact_group_68451), 5));
                            }
                        }
                        if (PTApp.getInstance().getCallStatus() == 2) {
                            arrayList.add(new RoomListContextMenuItem(zMActivity.getString(C4558R.string.zm_btn_invite_to_conf), 2));
                        }
                        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
                        ZMAlertDialog create = new Builder(zMActivity).setTitle((CharSequence) screenName).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                IMAddrBookListFragment.this.onRoomSelectContextMenuItem(iMAddrBookItem, (RoomListContextMenuItem) zMMenuAdapter.getItem(i));
                            }
                        }).create();
                        create.setCanceledOnTouchOutside(true);
                        create.show();
                    } else if (!iMAddrBookItem.getIsRobot() && !UIMgr.isMyNotes(iMAddrBookItem.getJid())) {
                        AddrBookItemDetailsActivity.show(zMActivity, iMAddrBookItem, 106);
                    } else if (zoomMessenger != null) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(iMAddrBookItem.getJid());
                        if (buddyWithJID != null) {
                            MMChatActivity.showAsOneToOneChat(zMActivity, buddyWithJID);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onRoomSelectContextMenuItem(@NonNull IMAddrBookItem iMAddrBookItem, RoomListContextMenuItem roomListContextMenuItem) {
        if (roomListContextMenuItem.getAction() == 1) {
            if (PTApp.getInstance().getCallStatus() == 0) {
                callABContact(0, iMAddrBookItem);
            }
        } else if (roomListContextMenuItem.getAction() == 0) {
            if (PTApp.getInstance().getCallStatus() == 0) {
                callABContact(1, iMAddrBookItem);
            }
        } else if (roomListContextMenuItem.getAction() == 2) {
            int callStatus = PTApp.getInstance().getCallStatus();
            if (callStatus == 1 || callStatus == 2) {
                inviteABContact(iMAddrBookItem);
            }
        } else if (roomListContextMenuItem.getAction() == 3) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.starSessionSetStar(iMAddrBookItem.getJid(), true)) {
                this.mDirectoryRecyclerView.onStarSessionDataUpdate();
            }
        } else if (roomListContextMenuItem.getAction() == 4) {
            ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger2 != null && zoomMessenger2.starSessionSetStar(iMAddrBookItem.getJid(), false)) {
                this.mDirectoryRecyclerView.onStarSessionDataUpdate();
            }
        } else if (roomListContextMenuItem.getAction() == 5) {
            copyBuddyToContactGroup(iMAddrBookItem);
        }
    }

    private void callABContact(int i, @Nullable IMAddrBookItem iMAddrBookItem) {
        String str;
        if (iMAddrBookItem != null) {
            Context context = getContext();
            if (context instanceof Activity) {
                if (TextUtils.isEmpty(iMAddrBookItem.getJid()) || TextUtils.isEmpty(iMAddrBookItem.getSortKey()) || iMAddrBookItem.getSortKey().charAt(0) != '!') {
                    str = iMAddrBookItem.getJid();
                } else {
                    str = iMAddrBookItem.getJid().replace(iMAddrBookItem.getSortKey(), "");
                }
                Activity activity = (Activity) context;
                int inviteToVideoCall = ConfActivity.inviteToVideoCall(activity, str, i);
                if (inviteToVideoCall != 0) {
                    StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
                }
            }
        }
    }

    private void inviteABContact(@Nullable IMAddrBookItem iMAddrBookItem) {
        String str;
        if (iMAddrBookItem != null) {
            Context context = getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (!StringUtil.isEmptyOrNull(iMAddrBookItem.getJid())) {
                    if (TextUtils.isEmpty(iMAddrBookItem.getJid()) || TextUtils.isEmpty(iMAddrBookItem.getSortKey()) || iMAddrBookItem.getSortKey().charAt(0) != '!') {
                        str = iMAddrBookItem.getJid();
                    } else {
                        str = iMAddrBookItem.getJid().replace(iMAddrBookItem.getSortKey(), "");
                    }
                    if (PTAppDelegation.getInstance().inviteBuddiesToConf(new String[]{str}, null, PTApp.getInstance().getActiveCallId(), PTApp.getInstance().getActiveMeetingNo(), activity.getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                        onSentInvitationFailed();
                    } else {
                        onSentInvitationDone(activity);
                    }
                }
            }
        }
    }

    private void onSentInvitationFailed() {
        Context context = getContext();
        if (context instanceof ZMActivity) {
            new InviteFailedDialog().show(((ZMActivity) context).getSupportFragmentManager(), InviteFailedDialog.class.getName());
        }
    }

    private void onSentInvitationDone(@NonNull Activity activity) {
        ConfLocalHelper.returnToConf(activity);
    }

    private void showPhoneMatchFragment() {
        AddrBookSetNumberActivity.show((Fragment) this, 103);
    }

    private void onContactsPermissionRequestByPhoneMatch() {
        if (checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            showPhoneContactsInZoom();
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ContactsPermissionFailedDialog.show(fragmentManager);
        }
    }

    public boolean isFocusedOnSearchField() {
        if (getView() == null) {
            return false;
        }
        return this.mEdtSearch.hasFocus();
    }

    public void onKeyboardOpen() {
        if (getView() != null && !this.isKeyboardOpen) {
            this.isKeyboardOpen = true;
            if (this.mUIMode == 1 && this.mEdtGroupSearch.getEditText().hasFocus()) {
                this.mPanelTitleBar.setVisibility(8);
                this.mPanelGroupOperator.setVisibility(8);
                this.mPanelGroups.setForeground(this.mDimmedForground);
                this.mPanelGroupSearchBar.setVisibility(8);
                this.mPanelSearchBarReal.setVisibility(0);
                this.mPanelSearchBarReal.setHint(this.mEdtGroupSearch.getHint());
                this.mPanelSearchBarReal.setText("");
                this.mPanelSearchBarReal.getEditText().requestFocus();
            } else if (this.mUIMode == 2 && this.mEdtAppSearch.hasFocus()) {
                this.mPanelTitleBar.setVisibility(8);
                this.mPanelApps.setForeground(this.mDimmedForground);
                this.mPanelAppSearchBar.setVisibility(8);
                this.mPanelSearchBarReal.setVisibility(0);
                this.mPanelSearchBarReal.setHint(this.mEdtAppSearch.getHint());
                this.mPanelSearchBarReal.setText("");
                this.mPanelSearchBarReal.getEditText().requestFocus();
            }
        }
    }

    public void onSessionDelete(String str) {
        this.mGroupsListView.refreshAllData();
    }

    public void onGroupDelete(@NonNull String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof IMActivity) {
            ((IMActivity) activity).onMMSessionDeleted(str);
        }
    }

    private void showWaitingMakeGroupDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "WaitingMakeGroupDialog");
        }
    }

    private boolean dismissWaitingMakeGroupDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) {
            return false;
        }
        ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingMakeGroupDialog");
        if (zMDialogFragment == null) {
            return false;
        }
        zMDialogFragment.dismissAllowingStateLoss();
        return true;
    }

    private void showMakeGroupFailureMessage(int i, @Nullable GroupAction groupAction) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 8) {
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

    /* access modifiers changed from: private */
    public void handleGroupActionMakeGroup(int i, @NonNull GroupAction groupAction) {
        dismissWaitingMakeGroupDialog();
        if (i != 0) {
            showMakeGroupFailureMessage(i, groupAction);
        } else if (groupAction.getBuddyNotAllowReason() == 0) {
            List notAllowBuddies = groupAction.getNotAllowBuddies();
            if (notAllowBuddies != null && PTApp.getInstance().getZoomMessenger() != null) {
                StringBuilder sb = new StringBuilder();
                for (int i2 = 0; i2 < notAllowBuddies.size(); i2++) {
                    if (!StringUtil.isEmptyOrNull((String) notAllowBuddies.get(i2))) {
                        sb.append((String) notAllowBuddies.get(i2));
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                }
                if (sb.length() > 0) {
                    String substring = sb.substring(0, sb.length() - 1);
                    Toast.makeText(getActivity(), getString(C4558R.string.zm_mm_msg_add_buddies_not_allowed_59554, substring), 1).show();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleJoinGroup(int i) {
        dismissWaitingMakeGroupDialog();
        if (i != 0) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_join_group_failed_59554, new Object[]{Integer.valueOf(i)}), 1).show();
            }
        }
    }

    private void showInvitePopWindow() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            View inflate = View.inflate(getActivity(), C4558R.layout.zm_mm_addrbook_invite_pop, null);
            View findViewById = inflate.findViewById(C4558R.C4560id.panelAddContact);
            View findViewById2 = inflate.findViewById(C4558R.C4560id.panelAddApp);
            View findViewById3 = inflate.findViewById(C4558R.C4560id.panelAddContactGroup);
            View findViewById4 = inflate.findViewById(C4558R.C4560id.panelNewGroup);
            View findViewById5 = inflate.findViewById(C4558R.C4560id.panelJoinPublicGroup);
            if (zoomMessenger.imChatGetOption() == 2) {
                findViewById4.setVisibility(8);
                findViewById5.setVisibility(8);
                findViewById2.setVisibility(8);
            } else if (TextUtils.isEmpty(PTApp.getInstance().getMarketplaceURL())) {
                findViewById2.setVisibility(8);
            } else {
                findViewById2.setVisibility(0);
            }
            if (zoomMessenger.isAddContactDisable()) {
                findViewById.setVisibility(8);
            }
            if (zoomMessenger.personalGroupGetOption() != 1) {
                findViewById3.setVisibility(8);
            }
            inflate.measure(0, 0);
            final ZMPopupWindow zMPopupWindow = new ZMPopupWindow(inflate, inflate.getMeasuredWidth(), inflate.getMeasuredHeight());
            C257120 r7 = new OnClickListener() {
                public void onClick(View view) {
                    int id = view.getId();
                    if (id == C4558R.C4560id.panelAddContact) {
                        IMAddrBookListFragment.this.onClickPanelAddContact();
                    } else if (id == C4558R.C4560id.panelNewGroup) {
                        IMAddrBookListFragment.this.onClickPanelNewGroup();
                    } else if (id == C4558R.C4560id.panelJoinPublicGroup) {
                        IMAddrBookListFragment.this.onClickPanelJoinPublicGroup();
                    } else if (id == C4558R.C4560id.panelAddApp) {
                        IMAddrBookListFragment.this.onClickPanelAddApp();
                    } else if (id == C4558R.C4560id.panelAddContactGroup) {
                        IMAddrBookListFragment.this.onClickPanelAddContactGroup();
                    }
                    zMPopupWindow.dismiss();
                }
            };
            findViewById.setOnClickListener(r7);
            findViewById2.setOnClickListener(r7);
            findViewById4.setOnClickListener(r7);
            findViewById5.setOnClickListener(r7);
            findViewById3.setOnClickListener(r7);
            zMPopupWindow.setContentView(inflate);
            zMPopupWindow.showAsDropDown(this.mBtnInvite);
        }
    }

    private boolean isPhoneNumberRegistered() {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            return !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber());
        }
        return false;
    }

    public boolean handleBackPressed() {
        if (this.mPanelSearchBarReal.getVisibility() != 0) {
            return false;
        }
        this.mPanelTitleBar.setVisibility(0);
        this.mPanelSearchBarReal.setVisibility(4);
        int i = this.mUIMode;
        if (i == 2) {
            this.mPanelAppSearchBar.setVisibility(0);
        } else if (i == 1) {
            this.mPanelGroupSearchBar.setVisibility(0);
        }
        this.mPanelSearchBarReal.setText("");
        this.isKeyboardOpen = false;
        return true;
    }

    public void onKeyboardClosed() {
        if (this.mEdtGroupSearch != null) {
            this.isKeyboardOpen = false;
            this.mPanelGroups.setForeground(null);
            this.mPanelApps.setForeground(null);
            int length = this.mPanelSearchBarReal.getText().length();
            int i = this.mUIMode;
            if (i == 2) {
                if (length == 0 || this.mAppsListView.getCount() == 0) {
                    this.mPanelSearchBarReal.setText("");
                    this.mPanelTitleBar.setVisibility(0);
                    this.mPanelSearchBarReal.setVisibility(4);
                    this.mPanelAppSearchBar.setVisibility(0);
                }
                this.mHandler.post(new Runnable() {
                    public void run() {
                        IMAddrBookListFragment.this.mAppsListView.requestLayout();
                    }
                });
            } else if (i == 1) {
                if (length == 0 || this.mGroupsListView.getCount() == 0) {
                    this.mPanelSearchBarReal.setText("");
                    this.mPanelTitleBar.setVisibility(0);
                    this.mPanelSearchBarReal.setVisibility(4);
                    this.mPanelGroupSearchBar.setVisibility(0);
                }
                this.mHandler.post(new Runnable() {
                    public void run() {
                        IMAddrBookListFragment.this.mGroupsListView.requestLayout();
                    }
                });
            }
        }
    }

    public void onResume() {
        boolean z;
        super.onResume();
        PTUI.getInstance().addPTUIListener(this);
        PTUI.getInstance().addIMListener(this);
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            if (!instance.needReloadAll() || (StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && !CmmSIPCallManager.getInstance().isCloudPBXEnabled())) {
                z = true;
            } else {
                matchNewNumbers();
                z = instance.reloadAllContacts();
                if (z) {
                    IMAddrBookListView.clearCaches();
                }
            }
            if (z && PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            } else if (!StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
                matchNewNumbers();
            }
            MMContactsAppsListView mMContactsAppsListView = this.mAppsListView;
            if (mMContactsAppsListView != null) {
                mMContactsAppsListView.onResume();
            }
            MMContactsGroupListView mMContactsGroupListView = this.mGroupsListView;
            if (mMContactsGroupListView != null) {
                mMContactsGroupListView.onResume();
            }
            updateUIMode(this.mUIMode, true);
            updateUIForIMChatOption();
        }
    }

    private void updateUIForIMChatOption() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || zoomMessenger.imChatGetOption() == 2) {
            this.mPanelTabGroups.setVisibility(8);
        }
    }

    private void updateUIMode(int i, boolean z) {
        this.mUIMode = i;
        if (this.mPanelSearchBarReal.getVisibility() != 0) {
            switch (i) {
                case 0:
                    this.mPanelTabContacts.setSelected(true);
                    this.mPanelTabGroups.setSelected(false);
                    this.mPanelTabApps.setSelected(false);
                    this.mPanelGroupOperator.setVisibility(8);
                    this.mPanelContacts.setVisibility(0);
                    this.mPanelGroups.setVisibility(8);
                    this.mPanelSearchBar.setVisibility(0);
                    this.mPanelGroupSearchBar.setVisibility(8);
                    this.mPanelApps.setVisibility(8);
                    this.mPanelAppSearchBar.setVisibility(8);
                    break;
                case 1:
                    this.mPanelTabContacts.setSelected(false);
                    this.mPanelTabGroups.setSelected(true);
                    this.mPanelTabApps.setSelected(false);
                    this.mPanelGroupOperator.setVisibility(0);
                    this.mPanelContacts.setVisibility(8);
                    this.mPanelGroups.setVisibility(0);
                    this.mPanelSearchBar.setVisibility(8);
                    this.mPanelGroupSearchBar.setVisibility(0);
                    this.mPanelApps.setVisibility(8);
                    this.mPanelAppSearchBar.setVisibility(8);
                    break;
                case 2:
                    this.mPanelTabContacts.setSelected(false);
                    this.mPanelTabGroups.setSelected(false);
                    this.mPanelTabApps.setSelected(true);
                    this.mPanelGroupOperator.setVisibility(8);
                    this.mPanelContacts.setVisibility(8);
                    this.mPanelGroups.setVisibility(8);
                    this.mPanelSearchBar.setVisibility(8);
                    this.mPanelGroupSearchBar.setVisibility(8);
                    this.mPanelApps.setVisibility(0);
                    this.mPanelAppSearchBar.setVisibility(0);
                    break;
            }
            if (z) {
                UIUtil.closeSoftKeyboard(getActivity(), this.mEdtGroupSearch.getEditText());
            }
        }
    }

    private void matchNewNumbers() {
        if (PTApp.getInstance().isWebSignedOn()) {
            int matchNewNumbers = ContactsMatchHelper.getInstance().matchNewNumbers(getActivity());
            if (!(matchNewNumbers == 0 || matchNewNumbers == -1)) {
                showErrorDialog(matchNewNumbers);
            }
        }
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
        PTUI.getInstance().removeIMListener(this);
        ABContactsCache.getInstance().removeListener(this);
    }

    public void onContactsCacheUpdated() {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            if (PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            } else if (!StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
                matchNewNumbers();
            }
            this.mDirectoryRecyclerView.reloadPhoneAddressGroup(false);
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        SparseArray sparseArray;
        super.onActivityCreated(bundle);
        if (bundle != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(IMAddrBookListFragment.class.getName());
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
            this.mContentView = onCreateView(onGetLayoutInflater(bundle), null, bundle);
            View view2 = this.mContentView;
            if (!(view2 == null || sparseArray == null)) {
                view2.restoreHierarchyState(sparseArray);
            }
        }
        PTUI.getInstance().addPhoneABListener(this);
    }

    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        ZoomPublicRoomSearchUI.getInstance().removeListener(this.mZoomPublicRoomSearchUIListener);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        ZMBuddySyncInstance.getInsatance().removeListener(this.mBuddyListLisener);
        IMCallbackUI.getInstance().removeListener(this.mGroupMemberSynchronizer);
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        FragmentActivity activity = getActivity();
        if ((activity != null && activity.isFinishing()) || isRemoving()) {
            PTUI.getInstance().removePhoneABListener(this);
        }
        if (isRemoving()) {
            IMAddrBookListView.clearCaches();
        }
    }

    public boolean onSearchRequested() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void enableAddrBook() {
        AddrBookSettingActivity.show((Fragment) this, 100);
    }

    public void onSetPhoneNumberDone() {
        onAddressBookEnabled();
    }

    public void onSelected() {
        if (PTApp.getInstance().isPhoneNumberRegistered() || (CmmSIPCallManager.getInstance().isCloudPBXEnabled() && CmmSIPCallManager.getInstance().isPBXActive())) {
            ABContactsCache instance = ABContactsCache.getInstance();
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
                if (instance.needReloadAll()) {
                    instance.reloadAllContacts();
                }
            } else if (AppUtil.canRequestContactPermission()) {
                requestContactPermission();
            }
        }
        refreshAll(false);
    }

    public void onAddressBookEnabled() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity instanceof IMActivity) {
            ((IMActivity) zMActivity).onAddressBookEnabled(true);
        }
    }

    /* access modifiers changed from: private */
    public void refreshAll(boolean z) {
        if (getView() != null) {
            reloadAllItems(!z);
            this.mGroupsListView.refreshAllData();
            this.mAppsListView.refreshAllData();
            updateUIMode(this.mUIMode, false);
            checkNoAppMessage();
        }
    }

    public void reloadAllItems() {
        reloadAllItems(false);
    }

    public void reloadAllItems(boolean z) {
        if (getView() != null) {
            IMDirectoryRecyclerView iMDirectoryRecyclerView = this.mDirectoryRecyclerView;
            if (iMDirectoryRecyclerView != null) {
                iMDirectoryRecyclerView.reloadAllItems(z);
            }
        }
    }

    private void checkNoAppMessage() {
        this.mPanelTabApps.setVisibility(this.mAppsListView.hasApps() ? 0 : 8);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            onWebLogin(j);
        }
    }

    private void onWebLogin(long j) {
        if (j == 0) {
            ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
            if (aBContactsHelper != null && PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            }
        }
    }

    public void onPhoneABEvent(int i, long j, Object obj) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final int i2 = i;
        final long j2 = j;
        final Object obj2 = obj;
        C257423 r1 = new EventAction("handlePhoneABEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ((IMAddrBookListFragment) iUIElement).handlePhoneABEvent(i2, j2, obj2);
            }
        };
        nonNullEventTaskManagerOrThrowException.push(r1);
    }

    /* access modifiers changed from: private */
    public void handlePhoneABEvent(int i, long j, Object obj) {
        switch (i) {
            case 3:
                onPhoneABMatchUpdated(j);
                return;
            default:
                return;
        }
    }

    private void onPhoneABMatchUpdated(long j) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            int i = (int) j;
            if (i == 0) {
                this.mDirectoryRecyclerView.reloadPhoneAddressGroup(false);
            } else if (i == 1104) {
                onPhoneBindByOther();
            }
        }
    }

    public boolean startABMatching() {
        int startABMatching = this.mDirectoryRecyclerView.startABMatching();
        if (startABMatching == 0) {
            return true;
        }
        if (startABMatching == -1) {
            reloadAllItems();
        } else {
            showErrorDialog(startABMatching);
        }
        return false;
    }

    private void showErrorDialog(int i) {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_match_contacts_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    public void showNonZoomUserActions(@Nullable IMAddrBookItem iMAddrBookItem) {
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

    /* access modifiers changed from: private */
    public void onStarSessionDataUpdate() {
        this.mDirectoryRecyclerView.onStarSessionDataUpdate();
        if (isResumed()) {
            this.mGroupsListView.refreshAllData();
            this.mAppsListView.refreshAllData();
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerSearchBuddy(boolean z, String str, int i) {
        if (isResumed()) {
            dismissWaitingDialog();
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerConnectReturn(int i) {
        if (PTApp.getInstance().getZoomMessenger() != null && isResumed()) {
            IMDirectoryRecyclerView iMDirectoryRecyclerView = this.mDirectoryRecyclerView;
            if (iMDirectoryRecyclerView != null) {
                iMDirectoryRecyclerView.onZoomMessengerConnectReturn(i);
            }
        }
    }

    private void lazyRefreshAll(boolean z) {
        IMDirectoryRecyclerView iMDirectoryRecyclerView = this.mDirectoryRecyclerView;
        if (iMDirectoryRecyclerView == null || !iMDirectoryRecyclerView.isDataEmpty()) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, z ? 1 : 0, 0), 2000);
            return;
        }
        refreshAll(true);
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerMUCGroupInfoUpdatedImpl(String str) {
        if (isResumed()) {
            this.mPendingUpdatedGroups.add(str);
            if (!this.mHandler.hasMessages(2)) {
                this.mHandler.sendEmptyMessageDelayed(2, 2000);
            }
        }
    }

    public void onZoomMessengerGroupAction(final int i, @NonNull final GroupAction groupAction, String str) {
        if (groupAction.getActionType() == 0) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_ADD_BUDDIES") {
                public void run(IUIElement iUIElement) {
                    IMAddrBookListFragment iMAddrBookListFragment = (IMAddrBookListFragment) iUIElement;
                    if (iMAddrBookListFragment != null) {
                        iMAddrBookListFragment.handleGroupActionMakeGroup(i, groupAction);
                    }
                }
            });
        }
        this.mGroupsListView.onGroupAction(i, groupAction, str);
    }

    /* access modifiers changed from: private */
    public void OnPersonalGroupResponse(byte[] bArr) {
        try {
            PersonalGroupAtcionResponse parseFrom = PersonalGroupAtcionResponse.parseFrom(bArr);
            if (parseFrom != null) {
                this.mDirectoryRecyclerView.onPersonalGroupResponse(parseFrom.getType(), parseFrom.getGroupId(), parseFrom.getReqId(), parseFrom.getResult(), new ArrayList(parseFrom.getChangeListList()), parseFrom.getFromGroupId(), parseFrom.getToGroupId(), parseFrom.getNotAllowedBuddiesList());
                int type = parseFrom.getType();
                if (type != 0) {
                    switch (type) {
                        case 40:
                            Toast.makeText(getContext(), getString(C4558R.string.zm_msg_max_buddies_in_group_79838, Long.valueOf(parseFrom.getMaxMemberCount())), 1).show();
                            break;
                        case 41:
                            Toast.makeText(getContext(), getString(C4558R.string.zm_msg_max_buddy_groups_79838, Long.valueOf(parseFrom.getMaxGroupCount())), 1).show();
                            break;
                    }
                } else if (this.mIgnoreHintRequestId.remove(parseFrom.getReqId())) {
                }
            }
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void indicate_BuddyBlockedByIB(List<String> list) {
        this.mDirectoryRecyclerView.onBlockedByIB(list);
    }

    /* access modifiers changed from: private */
    public void NotifyPersonalGroupSync(int i, String str, @NonNull List<String> list, String str2, String str3) {
        this.mDirectoryRecyclerView.notifyPersonalGroupSync(i, str, list, str2, str3);
    }

    public void onBuddyListUpdate() {
        this.mDirectoryRecyclerView.onBuddyListUpdate();
        if (isResumed()) {
            this.mAppsListView.refreshAllData();
            this.mGroupsListView.refreshAllData();
            checkNoAppMessage();
        }
    }

    public void onBuddyInfoUpdate(@NonNull List<String> list, @NonNull List<String> list2) {
        this.mDirectoryRecyclerView.onBuddyInfoUpdate(list, list2);
        if (isResumed()) {
            this.mAppsListView.onBuddyInfoUpdate(list, list2);
        }
    }

    public void searchMore(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.searchBuddyByKey(str)) {
                showWaitingDialog();
            }
        }
    }

    public void gotoSavedSessions() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            IMSavedSessionsFragment.showAsActivity(zMActivity, 0);
        }
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
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

    private void requestContactPermission() {
        zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
        AppUtil.saveRequestContactPermissionTime();
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((IMAddrBookListFragment) iUIElement).handleRequestPermissionResult(i, strArr, iArr);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 1000) {
                onContactsPermissionRequestByPhoneMatch();
            }
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_CONTACTS".equals(strArr[i2]) && iArr[i2] == 0) {
                    ABContactsCache instance = ABContactsCache.getInstance();
                    instance.registerContentObserver();
                    if (instance.needReloadAll()) {
                        instance.reloadAllContacts();
                    }
                }
            }
        }
    }

    private boolean isFTEShowed() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_ADDRBOOK_ADD_CONTACT), false);
    }

    /* access modifiers changed from: private */
    public void showAddContactFTE() {
        if (!isFTEShowed() && !ZMIMUtils.isAddContactDisable() && ZMIMUtils.isContactEmpty()) {
            View view = this.mAddContactsFTEView;
            if (view == null) {
                inflateAddContactsFTEView();
            } else {
                view.setVisibility(0);
            }
        }
    }

    private void inflateAddContactsFTEView() {
        View view = getView();
        if (view != null) {
            ViewStub viewStub = (ViewStub) view.findViewById(C4558R.C4560id.addContactViewStub);
            viewStub.setOnInflateListener(new OnInflateListener() {
                public void onInflate(ViewStub viewStub, View view) {
                    IMAddrBookListFragment.this.mAddContactsFTEView = view;
                    IMAddrBookListFragment.this.mAddContactsFTEView.findViewById(C4558R.C4560id.layoutFTE).setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_ADDRBOOK_ADD_CONTACT), true);
                            IMAddrBookListFragment.this.mAddContactsFTEView.setVisibility(8);
                        }
                    });
                    if (IMAddrBookListFragment.this.mBtnInvite != null) {
                        int[] iArr = new int[2];
                        IMAddrBookListFragment.this.mBtnInvite.getLocationOnScreen(iArr);
                        TextView textView = (TextView) IMAddrBookListFragment.this.mAddContactsFTEView.findViewById(C4558R.C4560id.addContactFTE);
                        LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
                        layoutParams.topMargin = ((iArr[1] + IMAddrBookListFragment.this.mBtnInvite.getMeasuredHeight()) - UIUtil.getStatusBarHeight(IMAddrBookListFragment.this.getContext())) - UIUtil.dip2px(IMAddrBookListFragment.this.getContext(), 0.5f);
                        textView.setLayoutParams(layoutParams);
                    }
                }
            });
            viewStub.inflate();
        }
    }
}
