package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.MMChatInfoActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMBuddyItem;
import com.zipow.videobox.view.p014mm.MMChatBuddiesGridView;
import com.zipow.videobox.view.p014mm.MMChatBuddiesGridView.BuddyOperationListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class MMGroupBuddiesFragment extends ZMDialogFragment implements OnClickListener, IABContactsCacheListener, BuddyOperationListener {
    private static final String ARG_GROUP_ID = "groupId";
    private static final int REQUEST_ONE_ON_ONE_BUDDY_DETAILS = 100;
    private static final String TAG = "MMGroupBuddiesFragment";
    private Button mBtnBack;
    @Nullable
    private String mGroupId;
    private MMChatBuddiesGridView mGvBuddies;
    private TextView mTxtTitle;
    @Nullable
    private ZMDialogFragment mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            MMGroupBuddiesFragment.this.onGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMGroupBuddiesFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMGroupBuddiesFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMGroupBuddiesFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void On_AssignGroupAdmins(int i, String str, String str2, List<String> list, long j) {
            MMGroupBuddiesFragment.this.On_AssignGroupAdmins(i, str, str2, list, j);
        }
    };

    @Nullable
    public static MMGroupBuddiesFragment findMMChatInfoFragment(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        return (MMGroupBuddiesFragment) fragmentManager.findFragmentByTag(MMGroupBuddiesFragment.class.getName());
    }

    public static void show(@NonNull ZMActivity zMActivity, String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_GROUP_ID, str);
        SimpleActivity.show(zMActivity, MMGroupBuddiesFragment.class.getName(), bundle, i, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mGroupId = getArguments().getString(ARG_GROUP_ID);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_chat_more_info, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mGvBuddies = (MMChatBuddiesGridView) inflate.findViewById(C4558R.C4560id.gvBuddies);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mGvBuddies.setBuddyOperationListener(this);
        this.mBtnBack.setOnClickListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateData();
        ABContactsCache.getInstance().addListener(this);
        if (ABContactsCache.getInstance().needReloadAll()) {
            ABContactsCache.getInstance().reloadAllContacts();
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
            if (groupById != null && !groupById.amIInGroup()) {
                onClickBtnBack();
            }
        }
    }

    private void updateData() {
        MMChatBuddiesGridView mMChatBuddiesGridView = this.mGvBuddies;
        if (mMChatBuddiesGridView != null) {
            mMChatBuddiesGridView.loadAllBuddies(null, null, this.mGroupId);
            this.mGvBuddies.notifyDataSetChanged();
        }
        updateTitle();
    }

    private void updateTitle() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    this.mTxtTitle.setText(activity.getString(groupById.isRoom() ? C4558R.string.zm_mm_title_chat_options_channel_59554 : C4558R.string.zm_mm_title_chat_options_muc_59554, new Object[]{Integer.valueOf(groupById.getBuddyCount())}));
                }
            }
        }
    }

    public void onContactsCacheUpdated() {
        MMChatBuddiesGridView mMChatBuddiesGridView = this.mGvBuddies;
        if (mMChatBuddiesGridView != null) {
            mMChatBuddiesGridView.loadAllBuddies(null, null, this.mGroupId);
            this.mGvBuddies.notifyDataSetChanged();
        }
    }

    public void onPause() {
        super.onPause();
        ABContactsCache.getInstance().removeListener(this);
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
                                MMGroupBuddiesFragment mMGroupBuddiesFragment = (MMGroupBuddiesFragment) iUIElement;
                                if (mMGroupBuddiesFragment != null) {
                                    mMGroupBuddiesFragment.handleGroupActionAddBuddies(i, groupAction);
                                }
                            }
                        });
                    } else {
                        if (isResumed()) {
                            this.mGvBuddies.loadAllBuddies(null, null, this.mGroupId);
                            this.mGvBuddies.notifyDataSetChanged();
                        }
                    }
                }
            }
        } else if (groupAction.getActionType() == 4 && StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
            if (isResumed()) {
                updateTitle();
            }
            if (!isResumed() || !groupAction.isMeInBuddies()) {
                ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger2 != null) {
                    ZoomBuddy myself2 = zoomMessenger2.getMyself();
                    if (myself2 == null || StringUtil.isSameString(myself2.getJid(), groupAction.getActionOwnerId())) {
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_REMOVE_BUDDY") {
                            public void run(IUIElement iUIElement) {
                                MMGroupBuddiesFragment mMGroupBuddiesFragment = (MMGroupBuddiesFragment) iUIElement;
                                if (mMGroupBuddiesFragment != null) {
                                    mMGroupBuddiesFragment.handleGroupActionRemoveBuddy(i, groupAction);
                                }
                            }
                        });
                    } else {
                        if (isResumed()) {
                            this.mGvBuddies.loadAllBuddies(null, null, this.mGroupId);
                            this.mGvBuddies.notifyDataSetChanged();
                        }
                    }
                }
            } else {
                onClickBtnBack();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionAddBuddies(int i, @NonNull GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            this.mGvBuddies.loadAllBuddies(null, null, this.mGroupId);
            this.mGvBuddies.notifyDataSetChanged();
            if (groupAction.getBuddyNotAllowReason() == 0) {
                List notAllowBuddies = groupAction.getNotAllowBuddies();
                if (notAllowBuddies != null && PTApp.getInstance().getZoomMessenger() != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i2 = 0; i2 < notAllowBuddies.size(); i2++) {
                        if (!StringUtil.isEmptyOrNull((String) notAllowBuddies.get(i2))) {
                            stringBuffer.append((String) notAllowBuddies.get(i2));
                            stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                        }
                    }
                    if (stringBuffer.length() > 0) {
                        String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
                        Toast.makeText(getActivity(), getString(C4558R.string.zm_mm_msg_add_buddies_not_allowed_59554, substring), 1).show();
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
            this.mGvBuddies.loadAllBuddies(null, null, this.mGroupId);
            this.mGvBuddies.setIsRemoveMode(true);
            this.mGvBuddies.notifyDataSetChanged();
            return;
        }
        showRemoveBuddyFailureMessage(i);
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Intent intent = new Intent();
                intent.putExtra(MMChatInfoActivity.RESULT_ARG_IS_QUIT_GROUP, true);
                activity.setResult(-1, intent);
                activity.finish();
            }
        }
    }

    /* access modifiers changed from: private */
    public void On_AssignGroupAdmins(final int i, String str, String str2, List<String> list, long j) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null && StringUtil.isSameString(str, myself.getJid())) {
                dismissWaitingDialog();
                if (i != 0) {
                    getNonNullEventTaskManagerOrThrowException().push(new EventAction("AssignGroupAdmins") {
                        public void run(IUIElement iUIElement) {
                            FragmentActivity activity = MMGroupBuddiesFragment.this.getActivity();
                            if (activity != null) {
                                Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_assign_admin_failed, new Object[]{Integer.valueOf(i)}), 1).show();
                            }
                        }
                    });
                } else {
                    quitGroup();
                }
            }
        }
    }

    private void quitGroup() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood()) {
                showConnectionError();
                return;
            }
            if (zoomMessenger.deleteGroup(this.mGroupId)) {
                showWaitingDialog();
            } else {
                showQuitGroupFailureMessage(1);
            }
        }
    }

    private void showQuitGroupFailureMessage(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
                return;
            }
            Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_quit_group_failed_59554, new Object[]{Integer.valueOf(i)}), 1).show();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        MMChatBuddiesGridView mMChatBuddiesGridView = this.mGvBuddies;
        if (mMChatBuddiesGridView != null) {
            mMChatBuddiesGridView.updateBuddyByJid(str);
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            updateData();
        }
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        }
    }

    public MMChatBuddiesGridView getMMChatBuddiesGridView() {
        return this.mGvBuddies;
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

    /* JADX WARNING: Removed duplicated region for block: B:14:0x004e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x004f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClickAddBtn() {
        /*
            r11 = this;
            androidx.fragment.app.FragmentActivity r0 = r11.getActivity()
            us.zoom.androidlib.app.ZMActivity r0 = (p021us.zoom.androidlib.app.ZMActivity) r0
            if (r0 != 0) goto L_0x0009
            return
        L_0x0009:
            com.zipow.videobox.view.mm.MMChatBuddiesGridView r1 = r11.mGvBuddies
            r2 = 0
            if (r1 == 0) goto L_0x0031
            java.util.List r1 = r1.getAllItems()
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
            r0 = 100
            com.zipow.videobox.MMSelectContactsActivity.show(r11, r10, r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMGroupBuddiesFragment.onClickAddBtn():void");
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            if (arrayList != null) {
                onContactsSelected(arrayList);
            }
        }
    }

    public void onRemoveBuddy(@NonNull MMBuddyItem mMBuddyItem) {
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
                        AddrBookItemDetailsActivity.show((Fragment) this, localContact, false, 100);
                    } else if (localContact.isMyContact()) {
                        AddrBookItemDetailsActivity.show((Fragment) this, localContact, false, 100);
                    }
                }
            }
        }
    }

    public void onContactsSelected(@Nullable ArrayList<IMAddrBookItem> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
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
}
