package com.zipow.videobox.view.p014mm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.MMChatInfoFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMSessionMembersListAdapter.OnRecyclerViewListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSessionMembersListFragment */
public class MMSessionMembersListFragment extends ZMDialogFragment implements OnClickListener, OnRecyclerViewListener {
    public static final String EXTRA_ARGS_GROUP_JID = "groupJid";
    public static final int REQUEST_GROUP_SELECT_CONTACTS = 101;
    private static final int REQUEST_ONE_ON_ONE_BUDDY_DETAILS = 100;
    private static final String TAG = "MMSessionMembersListFragment";
    private String mAdminId;
    List<String> mAdmins;
    private Button mBackBtn;
    List<MMBuddyItem> mBuddyItemListCache;
    private Button mCancelBtn;
    /* access modifiers changed from: private */
    public ImageButton mClearSearchBtn;
    private String mFilter;
    @Nullable
    private String mGroupId;
    /* access modifiers changed from: private */
    @Nullable
    public Handler mHandler;
    private ImageView mInviteImg;
    private MMSessionMembersListAdapter mMembersListAdapter;
    private RecyclerView mMembersRecyclerView;
    private LinearLayout mPanelConnectAleartBar;
    private RelativeLayout mPanelSearchBar;
    private LinearLayout mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            MMSessionMembersListFragment.this.filter(MMSessionMembersListFragment.this.mSearchEdt.getText().toString());
        }
    };
    private EditText mSearchDummyEdt;
    /* access modifiers changed from: private */
    public EditText mSearchEdt;
    private TextView mTitleTxt;
    @Nullable
    private ZMDialogFragment mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            MMSessionMembersListFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMSessionMembersListFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void On_AssignGroupAdmins(int i, String str, String str2, List<String> list, long j) {
            MMSessionMembersListFragment.this.On_AssignGroupAdmins(i, str, str2, list, j);
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            MMSessionMembersListFragment.this.On_DestroyGroup(i, str, str2, str3, j);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMSessionMembersListFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMSessionMembersListFragment$BuddyContextMenuItem */
    private static class BuddyContextMenuItem extends ZMSimpleMenuItem {
        private static final int ACTION_REMOVE = 1;

        private BuddyContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static void showAsActivity(Fragment fragment, String str, int i) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("groupJid", str);
            SimpleActivity.show(fragment, MMSessionMembersListFragment.class.getName(), bundle, i, false, 1);
        }
    }

    @Nullable
    public static MMSessionMembersListFragment findMMChatInfoFragment(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        return (MMSessionMembersListFragment) fragmentManager.findFragmentByTag(MMChatInfoFragment.class.getName());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(32);
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_session_members, viewGroup, false);
        this.mPanelTitleBar = (LinearLayout) inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mBackBtn = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mInviteImg = (ImageView) inflate.findViewById(C4558R.C4560id.invite_img);
        this.mTitleTxt = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mPanelSearchBar = (RelativeLayout) inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mSearchEdt = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mClearSearchBtn = (ImageButton) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mCancelBtn = (Button) inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mSearchDummyEdt = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mSearchDummyEdt.clearFocus();
        this.mPanelConnectAleartBar = (LinearLayout) inflate.findViewById(C4558R.C4560id.panelConnectionAlert);
        this.mMembersRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.members_recycler_view);
        this.mMembersListAdapter = new MMSessionMembersListAdapter(getContext());
        this.mMembersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mMembersRecyclerView.setAdapter(this.mMembersListAdapter);
        this.mBackBtn.setOnClickListener(this);
        this.mInviteImg.setOnClickListener(this);
        this.mClearSearchBtn.setOnClickListener(this);
        this.mCancelBtn.setOnClickListener(this);
        this.mSearchDummyEdt.setOnClickListener(this);
        this.mMembersListAdapter.setOnRecyclerViewListener(this);
        this.mHandler = new Handler();
        this.mSearchEdt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@Nullable Editable editable) {
                MMSessionMembersListFragment.this.mHandler.removeCallbacks(MMSessionMembersListFragment.this.mRunnableFilter);
                MMSessionMembersListFragment.this.mHandler.postDelayed(MMSessionMembersListFragment.this.mRunnableFilter, (editable == null || editable.length() == 0) ? 0 : 300);
                MMSessionMembersListFragment.this.mClearSearchBtn.setVisibility(MMSessionMembersListFragment.this.mSearchEdt.getText().length() > 0 ? 0 : 8);
            }
        });
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(18);
        }
        hideIME();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onResume() {
        super.onResume();
        updateTitle();
        loadAllBuddies(this.mGroupId);
        hideIME();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mGroupId = getArguments().getString("groupJid");
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 101 && i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            if (arrayList != null) {
                onContactsSelected(arrayList);
            }
        }
    }

    private void updateTitle() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId) && getContext() != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    int buddyCount = groupById.getBuddyCount();
                    this.mTitleTxt.setText(getString(C4558R.string.zm_mm_lbl_group_members_count_108993, Integer.valueOf(buddyCount)));
                }
            }
        }
    }

    public void loadAllBuddies(String str) {
        this.mMembersListAdapter.clear();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str)) {
            ZoomGroup groupById = zoomMessenger.getGroupById(str);
            if (groupById != null) {
                String groupOwner = groupById.getGroupOwner();
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    this.mAdmins = groupById.getGroupAdmins();
                    if (this.mAdmins == null) {
                        this.mAdmins = new ArrayList();
                    }
                    if (!CollectionsUtil.isListEmpty(this.mAdmins)) {
                        this.mAdminId = (String) this.mAdmins.get(0);
                    } else if (!TextUtils.isEmpty(groupOwner)) {
                        this.mAdminId = groupOwner;
                        this.mAdmins.add(groupOwner);
                    }
                    this.mMembersListAdapter.setAdminList(this.mAdmins);
                    int buddyCount = groupById.getBuddyCount();
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < buddyCount; i++) {
                        ZoomBuddy buddyAt = groupById.getBuddyAt(i);
                        if (!(buddyAt == null || buddyAt.getJid() == null)) {
                            MMBuddyItem mMBuddyItem = new MMBuddyItem(buddyAt, ZMBuddySyncInstance.getInsatance().getBuddyByJid(buddyAt.getJid()));
                            if (StringUtil.isSameString(buddyAt.getJid(), myself.getJid())) {
                                mMBuddyItem.setIsMySelf(true);
                                String screenName = myself.getScreenName();
                                if (!StringUtil.isEmptyOrNull(screenName)) {
                                    mMBuddyItem.setScreenName(screenName);
                                }
                            }
                            if (StringUtil.isSameString(this.mAdminId, buddyAt.getJid())) {
                                mMBuddyItem.setSortKey("!");
                            } else {
                                mMBuddyItem.setSortKey(SortUtil.getSortKey(mMBuddyItem.screenName, CompatUtils.getLocalDefault()));
                            }
                            arrayList.add(mMBuddyItem);
                        }
                    }
                    this.mBuddyItemListCache = new ArrayList(arrayList);
                    this.mMembersListAdapter.setData(arrayList);
                }
            }
        }
    }

    private void loadSearchBuddies() {
        if (!CollectionsUtil.isListEmpty(this.mBuddyItemListCache)) {
            this.mMembersListAdapter.setData(this.mBuddyItemListCache);
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(final int i, final GroupAction groupAction, String str) {
        if (groupAction.getActionType() == 3) {
            if (StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
                if (isResumed()) {
                    updateTitle();
                }
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself == null || StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_ADD_BUDDIES") {
                            public void run(IUIElement iUIElement) {
                                MMSessionMembersListFragment mMSessionMembersListFragment = (MMSessionMembersListFragment) iUIElement;
                                if (mMSessionMembersListFragment != null) {
                                    mMSessionMembersListFragment.handleGroupActionAddBuddies(i, groupAction);
                                }
                            }
                        });
                    } else {
                        if (isResumed()) {
                            loadAllBuddies(this.mGroupId);
                        }
                    }
                }
            }
        } else if (groupAction.getActionType() == 4) {
            if (StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
                if (!isResumed() || !groupAction.isMeInBuddies()) {
                    if (isResumed()) {
                        updateTitle();
                    }
                    ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger2 != null) {
                        ZoomBuddy myself2 = zoomMessenger2.getMyself();
                        if (myself2 == null || StringUtil.isSameString(myself2.getJid(), groupAction.getActionOwnerId())) {
                            getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_REMOVE_BUDDY") {
                                public void run(IUIElement iUIElement) {
                                    MMSessionMembersListFragment mMSessionMembersListFragment = (MMSessionMembersListFragment) iUIElement;
                                    if (mMSessionMembersListFragment != null) {
                                        mMSessionMembersListFragment.handleGroupActionRemoveBuddy(i, groupAction);
                                    }
                                }
                            });
                        } else {
                            if (isResumed()) {
                                loadAllBuddies(this.mGroupId);
                            }
                        }
                    }
                } else {
                    dismiss();
                }
            }
        } else if ((groupAction.getActionType() == 2 || groupAction.getActionType() == 5) && StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
            ZoomMessenger zoomMessenger3 = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger3 != null) {
                ZoomBuddy myself3 = zoomMessenger3.getMyself();
                if (myself3 == null || StringUtil.isSameString(myself3.getJid(), groupAction.getActionOwnerId())) {
                    getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_DELETE_GROUP") {
                        public void run(IUIElement iUIElement) {
                            MMSessionMembersListFragment mMSessionMembersListFragment = (MMSessionMembersListFragment) iUIElement;
                            if (mMSessionMembersListFragment != null) {
                                mMSessionMembersListFragment.handleGroupActionDeleteGroup(i, groupAction);
                            }
                        }
                    });
                } else {
                    if (isResumed()) {
                        updateTitle();
                        loadAllBuddies(this.mGroupId);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            updateTitle();
            loadAllBuddies(str);
        }
    }

    /* access modifiers changed from: private */
    public void On_AssignGroupAdmins(int i, String str, String str2, @Nullable List<String> list, long j) {
        if (StringUtil.isSameString(str2, this.mGroupId)) {
            updateTitle();
            loadAllBuddies(str2);
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionAddBuddies(int i, @NonNull GroupAction groupAction) {
        int i2;
        Object[] objArr;
        dismissWaitingDialog();
        if (i == 0) {
            loadAllBuddies(this.mGroupId);
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
                            }
                        }
                    }
                }
            }
        } else {
            showAddBuddiesFailureMessage(i, groupAction);
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionRemoveBuddy(int i, GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            loadAllBuddies(this.mGroupId);
        } else {
            showRemoveBuddyFailureMessage(i);
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionDeleteGroup(int i, GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void On_DestroyGroup(final int i, String str, String str2, String str3, long j) {
        if (StringUtil.isSameString(str2, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("DestroyGroup") {
                public void run(IUIElement iUIElement) {
                    if (i == 0) {
                        MMSessionMembersListFragment.this.finishFragment(true);
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
                    MMSessionMembersListFragment.this.finishFragment(true);
                }
            });
        }
    }

    public void filter(String str) {
        if (str == null) {
            str = "";
        }
        String lowerCase = str.trim().toLowerCase(CompatUtils.getLocalDefault());
        String str2 = this.mFilter;
        if (str2 == null) {
            str2 = "";
        }
        this.mFilter = lowerCase;
        this.mMembersListAdapter.setFilter(lowerCase);
        if (!StringUtil.isSameString(str2, this.mFilter)) {
            loadSearchBuddies();
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            if (view == this.mBackBtn) {
                dismiss();
            } else if (view == this.mInviteImg) {
                onClickInvite();
            } else if (view == this.mSearchDummyEdt) {
                this.mPanelTitleBar.setVisibility(8);
                this.mSearchDummyEdt.setVisibility(8);
                this.mPanelSearchBar.setVisibility(0);
                this.mSearchEdt.requestFocus();
                showIME();
            } else if (view == this.mClearSearchBtn) {
                this.mSearchEdt.setText("");
            } else if (view == this.mCancelBtn) {
                this.mSearchEdt.setText("");
                hideIME();
                this.mPanelSearchBar.setVisibility(8);
                this.mPanelTitleBar.setVisibility(0);
                this.mSearchDummyEdt.setVisibility(0);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x004e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x004f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onClickInvite() {
        /*
            r11 = this;
            androidx.fragment.app.FragmentActivity r0 = r11.getActivity()
            us.zoom.androidlib.app.ZMActivity r0 = (p021us.zoom.androidlib.app.ZMActivity) r0
            if (r0 != 0) goto L_0x0009
            return
        L_0x0009:
            com.zipow.videobox.view.mm.MMSessionMembersListAdapter r1 = r11.mMembersListAdapter
            r2 = 0
            if (r1 == 0) goto L_0x0031
            java.util.List r1 = r1.getDatas()
            if (r1 == 0) goto L_0x0031
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            java.util.Iterator r1 = r1.iterator()
        L_0x001d:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x0032
            java.lang.Object r4 = r1.next()
            com.zipow.videobox.view.mm.MMBuddyItem r4 = (com.zipow.videobox.view.p014mm.MMBuddyItem) r4
            java.lang.String r4 = r4.getBuddyJid()
            r3.add(r4)
            goto L_0x001d
        L_0x0031:
            r3 = r2
        L_0x0032:
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_mm_title_add_contacts
            java.lang.String r1 = r0.getString(r1)
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_ok
            java.lang.String r0 = r0.getString(r4)
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_msg_select_buddies_to_join_group_instructions_59554
            java.lang.String r4 = r11.getString(r4)
            com.zipow.videobox.ptapp.PTApp r5 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r5 = r5.getZoomMessenger()
            if (r5 != 0) goto L_0x004f
            return
        L_0x004f:
            java.lang.String r6 = r11.mGroupId
            com.zipow.videobox.ptapp.mm.ZoomGroup r6 = r5.getGroupById(r6)
            if (r6 != 0) goto L_0x0058
            return
        L_0x0058:
            int r7 = r6.getMucType()
            r7 = r7 & 4
            r8 = 1
            r9 = 0
            if (r7 == 0) goto L_0x0064
            r7 = 1
            goto L_0x0065
        L_0x0064:
            r7 = 0
        L_0x0065:
            com.zipow.videobox.MMSelectContactsActivity$SelectContactsParamter r10 = new com.zipow.videobox.MMSelectContactsActivity$SelectContactsParamter
            r10.<init>()
            r10.title = r1
            r10.preSelectedItems = r3
            r10.btnOkText = r0
            r10.instructionMessage = r4
            r10.isAnimBottomTop = r8
            r10.isOnlySameOrganization = r7
            boolean r0 = r6.isPublicRoom()
            int r0 = r5.getGroupLimitCount(r0)
            r10.maxSelectCount = r0
            r10.includeRobot = r9
            r10.isContainsAllInGroup = r9
            r0 = 101(0x65, float:1.42E-43)
            com.zipow.videobox.MMSelectContactsActivity.show(r11, r10, r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMSessionMembersListFragment.onClickInvite():void");
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

    public void onItemClick(MMBuddyItem mMBuddyItem) {
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
                        AddrBookItemDetailsActivity.show((Fragment) this, localContact, false, 100);
                    } else if (localContact.isMyContact()) {
                        AddrBookItemDetailsActivity.show((Fragment) this, localContact, false, 100);
                    }
                }
            }
        }
    }

    public void onItemLongClick(final MMBuddyItem mMBuddyItem) {
        if (mMBuddyItem != null && !mMBuddyItem.isRobot() && !mMBuddyItem.isMySelf() && !TextUtils.isEmpty(this.mAdminId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.getMyself() != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null && groupById.isGroupOperatorable()) {
                    ZMActivity zMActivity = (ZMActivity) getContext();
                    if (zMActivity != null) {
                        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
                        ArrayList arrayList = new ArrayList();
                        String screenName = mMBuddyItem.getScreenName();
                        arrayList.add(new BuddyContextMenuItem(zMActivity.getString(groupById.isRoom() ? C4558R.string.zm_mm_group_members_chanel_remove_buddy_108993 : C4558R.string.zm_mm_group_members_chat_remove_buddy_108993), 1));
                        if (arrayList.size() != 0) {
                            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
                            ZMAlertDialog create = new Builder(zMActivity).setTitle((CharSequence) screenName).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MMSessionMembersListFragment.this.onSelectBuddyContextMenuItem(mMBuddyItem, (BuddyContextMenuItem) zMMenuAdapter.getItem(i));
                                }
                            }).create();
                            create.setCanceledOnTouchOutside(true);
                            create.show();
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectBuddyContextMenuItem(@Nullable MMBuddyItem mMBuddyItem, @Nullable BuddyContextMenuItem buddyContextMenuItem) {
        if (!(mMBuddyItem == null || buddyContextMenuItem == null || buddyContextMenuItem.getAction() != 1)) {
            removeMember(mMBuddyItem);
        }
    }

    private void removeMember(MMBuddyItem mMBuddyItem) {
        if (getActivity() != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself == null || StringUtil.isSameString(myself.getJid(), mMBuddyItem.getBuddyJid())) {
                    return;
                }
                if (!zoomMessenger.isConnectionGood()) {
                    showConnectionError();
                    return;
                }
                if (zoomMessenger.removeBuddyFromGroup(this.mGroupId, mMBuddyItem.getBuddyJid())) {
                    showWaitingDialog();
                } else {
                    showRemoveBuddyFailureMessage(1);
                }
            }
        }
    }

    public void onContactsSelected(@Nullable ArrayList<IMAddrBookItem> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                String string = getString(C4558R.string.zm_mm_title_add_contacts);
                AccessibilityUtil.announceForAccessibilityCompat((View) this.mMembersRecyclerView, (CharSequence) getString(C4558R.string.zm_accessibility_select_contacts_success_22861, string));
            }
            addBuddiesToGroup(arrayList);
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
                    } catch (Exception unused) {
                    }
                }
            }
            this.mWaitingDialog = null;
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

    private void showIME() {
        if (getActivity() != null) {
            ((InputMethodManager) getActivity().getSystemService("input_method")).showSoftInput(this.mSearchEdt, 1);
        }
    }

    private void hideIME() {
        if (getActivity() != null) {
            this.mSearchEdt.clearFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService("input_method");
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(this.mSearchEdt.getWindowToken(), 0);
            }
        }
    }
}
