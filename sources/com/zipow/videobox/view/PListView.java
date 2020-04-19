package com.zipow.videobox.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.fragment.PAttendeeListActionDialog;
import com.zipow.videobox.fragment.PListFragment;
import com.zipow.videobox.fragment.PListItemActionDialog;
import com.zipow.videobox.fragment.QAWebinarAttendeeListFragment;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PListView extends ListView implements OnItemClickListener, OnClickListener {
    private static final String TAG = "PListView";
    private boolean isEnableSileMode = false;
    private boolean isEnableWaitingList = false;
    private PListAdapter mAdapter;
    @Nullable
    private String mFilter;
    @NonNull
    private Handler mHandler = new Handler();
    private Runnable mReloadAllItemsRunnable = new Runnable() {
        public void run() {
            PListView.this.reloadAllItemsNow();
        }
    };

    public enum StatusPListItem {
        MySelf,
        Host,
        ComputerAudio,
        RaisedHands,
        Cohost,
        Interpreter,
        UnmuteAudio,
        Others
    }

    public PListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public PListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public PListView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    private void initView() {
        this.mAdapter = new PListAdapter(getContext(), this);
        setItemsCanFocus(true);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (ConfMgr.getInstance().isConfConnected() && confContext.isWebinar()) {
                View inflate = View.inflate(getContext(), C4558R.layout.zm_plist_foot_attendees, null);
                inflate.findViewById(C4558R.C4560id.btnViewAttendee).setOnClickListener(this);
                addFooterView(inflate, null, false);
                this.mAdapter.setIsWebinar(true);
            }
            this.isEnableSileMode = confContext.isMeetingSupportSilentMode();
            this.isEnableWaitingList = confContext.supportPutUserinWaitingListUponEntryFeature();
            this.mAdapter.setEnableWaitingList(this.isEnableWaitingList);
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
    }

    private void loadAllItems(@NonNull PListAdapter pListAdapter) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            boolean isWebinar = confContext.isWebinar();
            CmmUserList userList = ConfMgr.getInstance().getUserList();
            if (userList != null) {
                BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
                boolean isInBOMeeting = bOMgr != null ? bOMgr.isInBOMeeting() : false;
                int userCount = userList.getUserCount();
                boolean z = this.isEnableSileMode || this.isEnableWaitingList;
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                HashMap hashMap = new HashMap();
                ConfUI instance = ConfUI.getInstance();
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                for (int i = 0; i < userCount; i++) {
                    CmmUser userAt = userList.getUserAt(i);
                    if (userAt != null && !userAt.isMMRUser() && ((isInBOMeeting || !userAt.isInBOMeeting()) && userAt.containsKeyInScreenName(this.mFilter))) {
                        if (z && userAt.inSilentMode() && ConfLocalHelper.canControlWaitingRoom()) {
                            arrayList.add(new WaitingListItem(userAt));
                        } else if (userAt.isViewOnlyUserCanTalk()) {
                            arrayList2.add(new PAttendeeItem(userAt));
                        } else if (!userAt.inSilentMode()) {
                            PListItem pListItem = new PListItem(userAt);
                            pListItem.setWebinar(isWebinar);
                            ConfLocalHelper.addPlistItemByCategory(pListItem, userAt, instance, hashMap, confStatusObj);
                        }
                    }
                }
                if (!arrayList.isEmpty()) {
                    pListAdapter.addWaitItems(arrayList);
                }
                if (!hashMap.isEmpty()) {
                    ArrayList arrayList3 = new ArrayList();
                    ArrayList arrayList4 = new ArrayList();
                    ConfLocalHelper.fillPlistItems(hashMap, arrayList3, arrayList4);
                    pListAdapter.addViewPlistItems(arrayList3);
                    pListAdapter.addExcludeViewPlistItems(arrayList4);
                }
                if (!arrayList2.isEmpty()) {
                    pListAdapter.addPAttendeeItems(arrayList2);
                }
                pListAdapter.sortAll();
            }
        }
    }

    public void setInSearchProgress(boolean z) {
        this.mAdapter.setInSearchProgress(z);
        this.mAdapter.notifyDataSetChanged();
    }

    public void reloadAllItems(boolean z) {
        int clientUserCount = ConfMgr.getInstance().getClientUserCount();
        if (z || clientUserCount < ZMConfiguration.MAX_PLIST_REFRESH_NOW_USER_COUNT) {
            reloadAllItemsNow();
            return;
        }
        this.mHandler.removeCallbacks(this.mReloadAllItemsRunnable);
        this.mHandler.postDelayed(this.mReloadAllItemsRunnable, (long) (clientUserCount / 10));
    }

    /* access modifiers changed from: private */
    public void reloadAllItemsNow() {
        this.mAdapter.clear();
        loadAllItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void filter(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        String lowerCase = str.trim().toLowerCase(CompatUtils.getLocalDefault());
        String str2 = this.mFilter;
        this.mFilter = lowerCase;
        if (str2 == null) {
            str2 = "";
        }
        if (!str2.equals(lowerCase)) {
            if (StringUtil.isEmptyOrNull(lowerCase)) {
                reloadAllItems(true);
            } else if (StringUtil.isEmptyOrNull(str2) || !lowerCase.contains(str2)) {
                reloadAllItems(true);
            } else {
                this.mAdapter.filter(lowerCase);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        int headerViewsCount = i - getHeaderViewsCount();
        if (headerViewsCount >= 0 && headerViewsCount < this.mAdapter.getCount()) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                Object item = this.mAdapter.getItem(headerViewsCount);
                if (item != null) {
                    if (item instanceof PListItem) {
                        PListItem pListItem = (PListItem) item;
                        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                        if (confContext != null) {
                            if (confStatusObj.isMyself(pListItem.userId)) {
                                showPListItemActionDialog(pListItem.userId);
                            } else {
                                CmmUser userById = ConfMgr.getInstance().getUserById(pListItem.userId);
                                if (userById != null) {
                                    if (ConfLocalHelper.isHostCoHostBOModerator()) {
                                        showPListItemActionDialog(pListItem.userId);
                                    } else if (!userById.isH323User() && !userById.isPureCallInUser() && !userById.inSilentMode()) {
                                        if (canControlUserCamera(userById) && confContext.isMeetingSupportCameraControl()) {
                                            showPListItemActionDialog(pListItem.userId);
                                        } else if (!confContext.isChatOff() && (userById.isHost() || userById.isCoHost() || userById.isBOModerator() || confStatusObj.getAttendeeChatPriviledge() != 3)) {
                                            showChatUI(pListItem);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (item instanceof PAttendeeItem) {
                        PAttendeeItem pAttendeeItem = (PAttendeeItem) item;
                        if (ConfLocalHelper.isNeedShowAttendeeActionList()) {
                            ZMActivity zMActivity = (ZMActivity) getContext();
                            if (zMActivity != null) {
                                PAttendeeListActionDialog.show(zMActivity.getSupportFragmentManager(), pAttendeeItem.getConfChatAttendeeItem());
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canControlUserCamera(CmmUser cmmUser) {
        boolean z;
        boolean z2;
        int i;
        CmmVideoStatus videoStatusObj = cmmUser.getVideoStatusObj();
        if (videoStatusObj != null) {
            z2 = videoStatusObj.getIsSending();
            z = videoStatusObj.getIsSource();
            i = videoStatusObj.getCamFecc();
        } else {
            i = 0;
            z2 = false;
            z = false;
        }
        if (!((cmmUser.supportSwitchCam() && z2) || i > 0) || !z2 || !z) {
            return false;
        }
        return true;
    }

    private void showPListItemActionDialog(long j) {
        PListItemActionDialog.show(((ZMActivity) getContext()).getSupportFragmentManager(), j);
    }

    private void showChatUI(PListItem pListItem) {
        ConfLocalHelper.showChatUI((ZMFragment) PListFragment.getPListFragment(((ZMActivity) getContext()).getSupportFragmentManager()), pListItem.userId);
    }

    private void showWebinarAttendees() {
        QAWebinarAttendeeListFragment.showAsActivity((ZMActivity) getContext(), 0);
    }

    public void updateAttendeeCount() {
        this.mAdapter.notifyDataSetChanged();
    }

    private boolean checkUserValid(@NonNull CmmUser cmmUser) {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        return !cmmUser.isFailoverUser() || (bOMgr != null && bOMgr.isInBOMeeting() == cmmUser.isInBOMeeting());
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onUserJoin(@androidx.annotation.Nullable java.util.Collection<java.lang.String> r9, int r10) {
        /*
            r8 = this;
            if (r9 == 0) goto L_0x0076
            boolean r0 = r9.isEmpty()
            if (r0 == 0) goto L_0x000a
            goto L_0x0076
        L_0x000a:
            com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            java.util.Iterator r9 = r9.iterator()
            r1 = 0
            r2 = 0
        L_0x0014:
            boolean r3 = r9.hasNext()
            if (r3 == 0) goto L_0x0062
            java.lang.Object r3 = r9.next()
            java.lang.String r3 = (java.lang.String) r3
            long r3 = java.lang.Long.parseLong(r3)
            com.zipow.videobox.confapp.CmmUser r5 = r0.getUserById(r3)
            r6 = 1
            if (r5 != 0) goto L_0x003b
            com.zipow.videobox.confapp.CmmUserList r7 = r0.getUserList()
            if (r7 == 0) goto L_0x003b
            com.zipow.videobox.confapp.CmmUser r5 = r7.getLeftUserById(r3)
            if (r5 == 0) goto L_0x0039
            r3 = 1
            goto L_0x003c
        L_0x0039:
            r3 = r10
            goto L_0x003c
        L_0x003b:
            r3 = r10
        L_0x003c:
            if (r5 != 0) goto L_0x003f
            goto L_0x0014
        L_0x003f:
            boolean r4 = r8.checkUserValid(r5)
            if (r4 == 0) goto L_0x0014
            java.lang.String r4 = r8.mFilter
            boolean r4 = r5.containsKeyInScreenName(r4)
            if (r4 == 0) goto L_0x0014
            boolean r4 = r5.isViewOnlyUserCanTalk()
            if (r4 == 0) goto L_0x0055
            r1 = 1
            goto L_0x0056
        L_0x0055:
            r2 = 1
        L_0x0056:
            com.zipow.videobox.view.PListAdapter r4 = r8.mAdapter
            boolean r6 = com.zipow.videobox.util.ConfLocalHelper.canControlWaitingRoom()
            boolean r7 = r8.isEnableSileMode
            r4.updateItem(r5, r6, r7, r3)
            goto L_0x0014
        L_0x0062:
            if (r1 == 0) goto L_0x0069
            com.zipow.videobox.view.PListAdapter r9 = r8.mAdapter
            r9.sortAttendee()
        L_0x0069:
            if (r2 == 0) goto L_0x0070
            com.zipow.videobox.view.PListAdapter r9 = r8.mAdapter
            r9.sortPanelist()
        L_0x0070:
            com.zipow.videobox.view.PListAdapter r9 = r8.mAdapter
            r9.notifyDataSetChanged()
            return
        L_0x0076:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.PListView.onUserJoin(java.util.Collection, int):void");
    }

    public void onUserLeave(@Nullable Collection<String> collection) {
        if (collection != null && !collection.isEmpty()) {
            CmmUserList userList = ConfMgr.getInstance().getUserList();
            if (userList != null) {
                ZMActivity zMActivity = (ZMActivity) getContext();
                FragmentManager fragmentManager = null;
                if (zMActivity != null) {
                    fragmentManager = zMActivity.getSupportFragmentManager();
                }
                boolean z = false;
                boolean z2 = false;
                for (String parseLong : collection) {
                    long parseLong2 = Long.parseLong(parseLong);
                    CmmUser leftUserById = userList.getLeftUserById(parseLong2);
                    if (leftUserById != null) {
                        boolean z3 = true;
                        if (leftUserById.isViewOnlyUserCanTalk()) {
                            if (fragmentManager != null) {
                                PAttendeeListActionDialog.dismissPAttendeeListActionDialogForUserId(fragmentManager, parseLong2);
                            }
                            z = true;
                        } else {
                            if (fragmentManager != null) {
                                PListItemActionDialog.dismissPListActionDialogForUserId(fragmentManager, parseLong2);
                            }
                            z2 = true;
                        }
                        PListAdapter pListAdapter = this.mAdapter;
                        if (!this.isEnableSileMode && !this.isEnableWaitingList) {
                            z3 = false;
                        }
                        pListAdapter.removeItem(parseLong2, z3);
                    }
                }
                if (z) {
                    this.mAdapter.sortAttendee();
                }
                if (z2) {
                    this.mAdapter.sortPanelist();
                }
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onPromoteOrDowngrade(long j) {
        this.mAdapter.removeItem(j, this.isEnableSileMode || this.isEnableWaitingList);
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null) {
            if (userById.isViewOnlyUserCanTalk()) {
                this.mAdapter.sortAttendee();
            } else {
                this.mAdapter.sortPanelist();
            }
        }
        this.mAdapter.notifyDataSetChanged();
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf(j));
        checkUserLeavesForActionDialog(arrayList);
    }

    public void checkUserLeavesForActionDialog(@Nullable Collection<String> collection) {
        if (collection != null && !collection.isEmpty()) {
            FragmentManager supportFragmentManager = ((ZMActivity) getContext()).getSupportFragmentManager();
            if (supportFragmentManager != null) {
                for (String parseLong : collection) {
                    long parseLong2 = Long.parseLong(parseLong);
                    PListItemActionDialog.dismissPListActionDialogForUserId(supportFragmentManager, parseLong2);
                    PAttendeeListActionDialog.dismissPAttendeeListActionDialogForUserId(supportFragmentManager, parseLong2);
                }
            }
        }
    }

    public void onHostCohostChanged(long j) {
        reloadAllItems(false);
        FragmentManager supportFragmentManager = ((ZMActivity) getContext()).getSupportFragmentManager();
        if (supportFragmentManager != null) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null) {
                    if (confStatusObj.isSameUser(myself.getNodeId(), j)) {
                        PListItemActionDialog.dismissPListItemActionDialog(supportFragmentManager);
                    } else {
                        PListItemActionDialog.dismissPListActionDialogForUserId(supportFragmentManager, j);
                    }
                }
                PAttendeeListActionDialog.refreshAction(supportFragmentManager, j);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0067  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateUser(@androidx.annotation.Nullable java.util.Collection<java.lang.String> r11, int r12) {
        /*
            r10 = this;
            if (r11 == 0) goto L_0x008b
            boolean r0 = r11.isEmpty()
            if (r0 == 0) goto L_0x000a
            goto L_0x008b
        L_0x000a:
            com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            android.content.Context r1 = r10.getContext()
            us.zoom.androidlib.app.ZMActivity r1 = (p021us.zoom.androidlib.app.ZMActivity) r1
            r2 = 0
            if (r1 == 0) goto L_0x001b
            androidx.fragment.app.FragmentManager r2 = r1.getSupportFragmentManager()
        L_0x001b:
            java.util.Iterator r11 = r11.iterator()
            r1 = 0
            r3 = 0
        L_0x0021:
            boolean r4 = r11.hasNext()
            if (r4 == 0) goto L_0x0077
            java.lang.Object r4 = r11.next()
            java.lang.String r4 = (java.lang.String) r4
            long r4 = java.lang.Long.parseLong(r4)
            com.zipow.videobox.confapp.CmmUser r6 = r0.getUserById(r4)
            r7 = 1
            if (r6 != 0) goto L_0x0048
            com.zipow.videobox.confapp.CmmUserList r8 = r0.getUserList()
            if (r8 == 0) goto L_0x0048
            com.zipow.videobox.confapp.CmmUser r6 = r8.getLeftUserById(r4)
            if (r6 == 0) goto L_0x0046
            r8 = 1
            goto L_0x0049
        L_0x0046:
            r8 = r12
            goto L_0x0049
        L_0x0048:
            r8 = r12
        L_0x0049:
            if (r6 != 0) goto L_0x004c
            goto L_0x0021
        L_0x004c:
            boolean r9 = r10.checkUserValid(r6)
            if (r9 == 0) goto L_0x0021
            java.lang.String r9 = r10.mFilter
            boolean r9 = r6.containsKeyInScreenName(r9)
            if (r9 == 0) goto L_0x0021
            boolean r9 = r6.isViewOnlyUserCanTalk()
            if (r9 == 0) goto L_0x0067
            if (r2 == 0) goto L_0x0065
            com.zipow.videobox.fragment.PAttendeeListActionDialog.refreshAction(r2, r4)
        L_0x0065:
            r1 = 1
            goto L_0x006d
        L_0x0067:
            if (r2 == 0) goto L_0x006c
            com.zipow.videobox.fragment.PListItemActionDialog.refreshAction(r2, r4)
        L_0x006c:
            r3 = 1
        L_0x006d:
            com.zipow.videobox.view.PListAdapter r4 = r10.mAdapter
            boolean r5 = com.zipow.videobox.util.ConfLocalHelper.canControlWaitingRoom()
            r4.updateItem(r6, r5, r8)
            goto L_0x0021
        L_0x0077:
            if (r1 == 0) goto L_0x007e
            com.zipow.videobox.view.PListAdapter r11 = r10.mAdapter
            r11.sortAttendee()
        L_0x007e:
            if (r3 == 0) goto L_0x0085
            com.zipow.videobox.view.PListAdapter r11 = r10.mAdapter
            r11.sortPanelist()
        L_0x0085:
            com.zipow.videobox.view.PListAdapter r11 = r10.mAdapter
            r11.notifyDataSetChanged()
            return
        L_0x008b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.PListView.updateUser(java.util.Collection, int):void");
    }

    public void onLeavingSilentModeStatusChanged(@Nullable Collection<String> collection, int i) {
        int i2;
        if (collection != null && !collection.isEmpty()) {
            ConfMgr instance = ConfMgr.getInstance();
            for (String parseLong : collection) {
                long parseLong2 = Long.parseLong(parseLong);
                CmmUser userById = instance.getUserById(parseLong2);
                if (userById == null) {
                    CmmUserList userList = instance.getUserList();
                    if (userList != null) {
                        userById = userList.getLeftUserById(parseLong2);
                        i2 = userById != null ? 1 : i;
                        if (userById != null && checkUserValid(userById) && userById.containsKeyInScreenName(this.mFilter)) {
                            this.mAdapter.onLeavingSilentModeStatusChanged(userById, ConfLocalHelper.canControlWaitingRoom(), i2);
                        }
                    }
                }
                i2 = i;
                this.mAdapter.onLeavingSilentModeStatusChanged(userById, ConfLocalHelper.canControlWaitingRoom(), i2);
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void updateUserAndNotReSort(@Nullable Collection<String> collection, int i) {
        int i2;
        if (collection != null && !collection.isEmpty()) {
            ConfMgr instance = ConfMgr.getInstance();
            for (String parseLong : collection) {
                long parseLong2 = Long.parseLong(parseLong);
                CmmUser userById = instance.getUserById(parseLong2);
                if (userById == null) {
                    CmmUserList userList = instance.getUserList();
                    if (userList != null) {
                        userById = userList.getLeftUserById(parseLong2);
                        i2 = userById != null ? 1 : i;
                        if (userById != null && checkUserValid(userById) && userById.containsKeyInScreenName(this.mFilter)) {
                            this.mAdapter.updateItem(userById, ConfLocalHelper.canControlWaitingRoom(), i2);
                            this.mAdapter.notifyDataSetChanged();
                        }
                    }
                }
                i2 = i;
                this.mAdapter.updateItem(userById, ConfLocalHelper.canControlWaitingRoom(), i2);
                this.mAdapter.notifyDataSetChanged();
            }
            updateActionDialog(collection);
        }
    }

    public void updateActionDialog(@Nullable Collection<String> collection) {
        if (collection != null && !collection.isEmpty()) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    ConfMgr instance = ConfMgr.getInstance();
                    for (String parseLong : collection) {
                        long parseLong2 = Long.parseLong(parseLong);
                        CmmUser userById = instance.getUserById(parseLong2);
                        if (userById != null) {
                            if (userById.isViewOnlyUserCanTalk()) {
                                PAttendeeListActionDialog.refreshAction(supportFragmentManager, parseLong2);
                            } else {
                                PListItemActionDialog.refreshAction(supportFragmentManager, parseLong2);
                            }
                        }
                    }
                }
            }
        }
    }

    public void ccPrivilegeChange(long j) {
        this.mAdapter.notifyDataSetChanged();
        FragmentManager supportFragmentManager = ((ZMActivity) getContext()).getSupportFragmentManager();
        if (supportFragmentManager != null) {
            PListItemActionDialog.refreshAction(supportFragmentManager, j);
        }
    }

    public void onTalkPrivilegeChange(long j) {
        reloadAllItems(false);
        FragmentManager supportFragmentManager = ((ZMActivity) getContext()).getSupportFragmentManager();
        if (supportFragmentManager != null) {
            PAttendeeListActionDialog.dismissPAttendeeListActionDialogForUserId(supportFragmentManager, j);
        }
    }

    public void onConfAdmitAllSilentUsersStatusChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onClick(@Nullable View view) {
        if (view != null && view.getId() == C4558R.C4560id.btnViewAttendee) {
            showWebinarAttendees();
        }
    }
}
