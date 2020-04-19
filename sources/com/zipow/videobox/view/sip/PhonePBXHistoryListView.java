package com.zipow.videobox.view.sip;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.PBXSMSActivity;
import com.zipow.videobox.fragment.InviteFragment.InviteFailedDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItem;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItemBean;
import com.zipow.videobox.sip.server.CmmSIPCallHistoryItemBean;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.SimpleISIPCallRepositoryEventSinkListener;
import com.zipow.videobox.sip.server.PBXLoginConflictListenerUI.SimplePBXLoginConflictListener;
import com.zipow.videobox.util.CmmPBXListUtil;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import com.zipow.videobox.view.sip.BasePBXHistoryAdapter.IPBXListView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.PullDownRefreshListView;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXHistoryListView extends PullDownRefreshListView implements OnItemClickListener, OnScrollListener, IPBXListView {
    public static final int PAGE_SIZE = 50;
    private static final String TAG = "PhonePBXHistoryListView";
    /* access modifiers changed from: private */
    public OnAccessibilityListener mAccessibilityControl;
    /* access modifiers changed from: private */
    public PhonePBXCallHistoryAdapter mAdapter;
    private boolean mCanClearMissedCallHistory = false;
    private ZMAlertDialog mContextMenuDialog;
    /* access modifiers changed from: private */
    public boolean mIsLoadingMore = false;
    @NonNull
    private SimplePBXLoginConflictListener mLoginConflictListener = new SimplePBXLoginConflictListener() {
        public void onConflict() {
            super.onConflict();
            PhonePBXHistoryListView.this.setPullDownRefreshEnabled(false);
            PhonePBXHistoryListView.this.onLoginConflict();
        }

        public void onResumeFromConflict() {
            super.onResumeFromConflict();
            if (!PhonePBXHistoryListView.this.getParentFragment().isInSelectMode()) {
                PhonePBXHistoryListView.this.clearAndLoadData(true);
            }
            PhonePBXHistoryListView.this.setPullDownRefreshEnabled(true);
        }
    };
    SimpleISIPCallRepositoryEventSinkListener mPBXRepositoryListener = new SimpleISIPCallRepositoryEventSinkListener() {
        public void OnFullCallHistorySyncFinished(boolean z) {
            super.OnFullCallHistorySyncFinished(z);
            if (z) {
                PhonePBXHistoryListView.this.getParentFragment().onListViewDatasetChanged(true);
                if (PhonePBXHistoryListView.this.getParentFragment().isHasShow()) {
                    PhonePBXHistoryListView.this.clearAndLoadData(false);
                    PhonePBXHistoryListView.this.showRefreshing(false);
                }
            }
        }

        public void OnMoreCallHistorySyncFinished(List<String> list, List<String> list2, boolean z) {
            super.OnMoreCallHistorySyncFinished(list, list2, z);
            if (z && PhonePBXHistoryListView.this.getParentFragment().isHasShow()) {
                List list3 = null;
                List filterCallHistoryListByID = (list == null || list.isEmpty()) ? null : CmmPBXCallHistoryManager.getInstance().filterCallHistoryListByID(list);
                if (list2 != null && !list2.isEmpty()) {
                    list3 = CmmPBXCallHistoryManager.getInstance().filterCallHistoryListByID(list2);
                }
                if (!(filterCallHistoryListByID == null && list3 == null)) {
                    PhonePBXHistoryListView.this.getParentFragment().onListViewDatasetChanged(true);
                    PhonePBXHistoryListView.this.onMoreSyncFinished(filterCallHistoryListByID, list3);
                }
                if (PhonePBXHistoryListView.this.mAccessibilityControl != null) {
                    PhonePBXHistoryListView.this.mAccessibilityControl.onAccessbility();
                }
            }
            PhonePBXHistoryListView.this.showRefreshing(false);
            if (PhonePBXHistoryListView.this.mIsLoadingMore && (list == null || list.size() <= 0)) {
                PhonePBXHistoryListView.this.mIsLoadingMore = false;
            }
            PhonePBXHistoryListView.this.updateLoadMoreViewState();
        }

        public void OnCallHistoryDeleted(List<String> list, boolean z) {
            super.OnCallHistoryDeleted(list, z);
            if (z) {
                PhonePBXHistoryListView.this.getParentFragment().onListViewDatasetChanged(true);
                PhonePBXHistoryListView.this.onHistoyItemsDeleted(list);
                PhonePBXHistoryListView.this.mParentFragment.updateEmptyView();
            }
        }

        public void OnCallHistoryAllCleared(boolean z) {
            super.OnCallHistoryAllCleared(z);
            if (z) {
                PhonePBXHistoryListView.this.getParentFragment().onListViewDatasetChanged(true);
                PhonePBXHistoryListView.this.mAdapter.clearAll();
                PhonePBXHistoryListView.this.mParentFragment.updateEmptyView();
            }
        }

        public void OnAudioFileDownloadFinished(String str, int i, int i2) {
            super.OnAudioFileDownloadFinished(str, i, i2);
            CmmSIPAudioFileItem audioFileItemByID = CmmPBXCallHistoryManager.getInstance().getAudioFileItemByID(str, i);
            if (i2 == 0 && i == 0) {
                PhonePBXHistoryListView.this.setRecordAudioFileDownloadComplete(audioFileItemByID);
            }
        }
    };
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onDataNetworkStatusChanged(boolean z) {
            super.onDataNetworkStatusChanged(z);
            PhonePBXHistoryListView.this.mAdapter.notifyDataSetChanged();
            if (z && PhonePBXHistoryListView.this.getParentFragment().isHasShow()) {
                PhonePBXHistoryListView.this.showRefreshing(true);
                PhonePBXHistoryListView.this.onPullDownRefresh();
            }
        }
    };
    private View mPanelLoadMoreView;
    /* access modifiers changed from: private */
    public IPhonePBXParentFragment mParentFragment;
    private ProgressBar mProgressLoadMore;
    private List<String> mSelectList = new ArrayList();
    private TextView mTxtLoadMore;

    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    public void updateZoomBuddyInfo(List<String> list) {
    }

    /* access modifiers changed from: private */
    public void onMoreSyncFinished(List<CmmSIPCallHistoryItemBean> list, List<CmmSIPCallHistoryItemBean> list2) {
        List addLatestList = CmmPBXListUtil.addLatestList(list, this.mAdapter.getData());
        if (addLatestList == null) {
            addLatestList = new ArrayList();
        }
        if (!CollectionsUtil.isListEmpty(list2)) {
            addLatestList.addAll(list2);
        }
        updateData(addLatestList);
    }

    public PhonePBXHistoryListView(Context context) {
        super(context);
        init();
    }

    public PhonePBXHistoryListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PhonePBXHistoryListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void init() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_list_load_more_footer, null);
        this.mPanelLoadMoreView = inflate.findViewById(C4558R.C4560id.panelLoadMoreView);
        this.mProgressLoadMore = (ProgressBar) inflate.findViewById(C4558R.C4560id.progressBar);
        this.mTxtLoadMore = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        addFooterView(inflate);
        this.mAdapter = new PhonePBXCallHistoryAdapter(getContext(), this);
        setAdapter(this.mAdapter);
        setTextResources(C4558R.string.zm_lbl_release_to_load_more, C4558R.string.zm_lbl_pull_down_to_load_more, C4558R.string.zm_empty_string);
        setOnItemClickListener(this);
        setOnScrollListener(this);
        CmmPBXCallHistoryManager.getInstance().addISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        CmmSIPCallManager.getInstance().addPBXLoginConflictListener(this.mLoginConflictListener);
        PTUI.getInstance().addPTUIListener(this.mPTUIListener);
    }

    public void setRecordAudioFileDownloadComplete(CmmSIPAudioFileItem cmmSIPAudioFileItem) {
        int count = this.mAdapter.getCount();
        PhonePBXCallHistoryAdapter phonePBXCallHistoryAdapter = this.mAdapter;
        for (int i = 0; i < count; i++) {
            CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) phonePBXCallHistoryAdapter.getItem(i);
            if (cmmSIPCallHistoryItemBean != null) {
                CmmSIPAudioFileItemBean recordingAudioFile = cmmSIPCallHistoryItemBean.getRecordingAudioFile();
                if (!(recordingAudioFile == null || cmmSIPAudioFileItem == null || !cmmSIPAudioFileItem.getID().equals(recordingAudioFile.getId()))) {
                    cmmSIPAudioFileItem.toSIPAudioFileItemBean(recordingAudioFile);
                    return;
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (!CmmSIPCallManager.getInstance().isLoginConflict()) {
            onItemClick(i);
        }
    }

    private void onItemClick(int i) {
        if (!getParentFragment().isInSelectMode()) {
            int max = Math.max(0, i - getHeaderViewsCount());
            if (max == (getAdapter().getCount() - getHeaderViewsCount()) - 1) {
                onLoadMore();
                updateLoadMoreViewState();
                return;
            }
            CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) this.mAdapter.getItem(max);
            if (cmmSIPCallHistoryItemBean != null) {
                onNormalItemClick(new PBXCallHistory(cmmSIPCallHistoryItemBean));
            }
        }
    }

    public void setParentFragment(ZMDialogFragment zMDialogFragment) {
        this.mParentFragment = (IPhonePBXParentFragment) zMDialogFragment;
    }

    public IPhonePBXParentFragment getParentFragment() {
        return this.mParentFragment;
    }

    public void checkDelete() {
        onHistoyItemsDeleted(this.mSelectList);
        if (!this.mSelectList.isEmpty()) {
            if (CmmPBXCallHistoryManager.getInstance().deletePBXCallHistory(this.mSelectList)) {
                ZMLog.m286i(TAG, "onDeleteHistoryCall success", new Object[0]);
            } else {
                ZMLog.m286i(TAG, "onDeleteHistoryCall fail", new Object[0]);
            }
            this.mSelectList.clear();
        }
    }

    public void checkLoadMore() {
        if (this.mAdapter.getCount() == 0 && canLoadMore()) {
            onLoadMore();
        }
    }

    public void clearSelectList() {
        if (!this.mSelectList.isEmpty()) {
            this.mSelectList.clear();
        }
    }

    public void clearAll() {
        this.mAdapter.clearAll();
        CmmPBXCallHistoryManager.getInstance().clearPBXCallHistory();
    }

    public void clearAndLoadData(boolean z) {
        this.mAdapter.clearAll();
        loadData();
        if (z) {
            CmmPBXCallHistoryManager.getInstance().requestSyncMoreCallHistory(false);
        }
    }

    public void loadData() {
        if (this.mAdapter.getCount() <= 0) {
            loadDataByPage();
        }
    }

    public void loadDataByPage() {
        CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) this.mAdapter.getItem(Math.max(0, this.mAdapter.getCount() - 1));
        String str = "";
        if (cmmSIPCallHistoryItemBean != null) {
            str = cmmSIPCallHistoryItemBean.getId();
        }
        List callHistoryListByPage = CmmPBXCallHistoryManager.getInstance().getCallHistoryListByPage(str, 50);
        if (callHistoryListByPage == null || callHistoryListByPage.isEmpty()) {
            updateLoadMoreViewState();
        } else {
            addData(callHistoryListByPage);
        }
    }

    public void forceRefreshData() {
        this.mAdapter.clearAll();
        loadData();
    }

    private void updateData(List list) {
        this.mAdapter.updateData(list);
        this.mParentFragment.updateEmptyView();
    }

    private void addData(List list) {
        this.mAdapter.addData(list);
        this.mParentFragment.updateEmptyView();
    }

    public void setSelectMode(boolean z) {
        if (this.mAdapter.isSelectMode() != z) {
            this.mAdapter.setSelectMode(z);
            this.mAdapter.notifyDataSetChanged();
        }
        setPullDownRefreshEnabled(!z);
        updateLoadMoreViewState();
    }

    public void onNormalItemClick(PBXCallHistory pBXCallHistory) {
        if (!(this.mAdapter == null || pBXCallHistory == null || ((ZMActivity) getContext()) == null || StringUtil.isEmptyOrNull(pBXCallHistory.number))) {
            if (!pBXCallHistory.isRestricted) {
                sendSipCall(pBXCallHistory.number, pBXCallHistory.displayName);
            }
            getParentFragment().onSelectLastAccessibilityId(pBXCallHistory.f349id);
            if (pBXCallHistory.highLight) {
                CmmPBXCallHistoryManager.getInstance().clearMissedCallHistory();
            }
        }
    }

    private void sendSipCall(String str, String str2) {
        if (CmmSIPCallManager.getInstance().checkNetwork(getContext()) && CmmSIPCallManager.getInstance().checkIsPbxActive(getContext())) {
            getParentFragment().onPickSipResult(str, str2);
        }
    }

    public void sendSipCallWithCheckError(String str, String str2) {
        if (CmmSIPCallManager.getInstance().checkNetwork(getContext()) && CmmSIPCallManager.getInstance().checkIsPbxActive(getContext())) {
            this.mParentFragment.onPickSipResult(str, str2);
        }
    }

    private void inviteToConf(String str) {
        int callStatus = PTApp.getInstance().getCallStatus();
        if (callStatus == 1 || callStatus == 2) {
            inviteABContact(str);
        }
    }

    private void startVideoCall(String str) {
        if (PTApp.getInstance().getCallStatus() == 0) {
            callABContact(1, str);
        }
    }

    private void startAudioCall(String str) {
        if (PTApp.getInstance().getCallStatus() == 0) {
            callABContact(0, str);
        }
    }

    private void callABContact(int i, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Activity activity = (Activity) getContext();
            if (activity != null) {
                int inviteToVideoCall = ConfActivity.inviteToVideoCall(activity, str, i);
                if (inviteToVideoCall != 0) {
                    StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
                }
            }
        }
    }

    private void inviteABContact(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Activity activity = (Activity) getContext();
            if (activity != null) {
                if (PTAppDelegation.getInstance().inviteBuddiesToConf(new String[]{str}, null, PTApp.getInstance().getActiveCallId(), PTApp.getInstance().getActiveMeetingNo(), activity.getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                    onSentInvitationFailed();
                } else {
                    onSentInvitationDone(activity);
                }
            }
        }
    }

    private void onSentInvitationFailed() {
        new InviteFailedDialog().show(((ZMActivity) getContext()).getSupportFragmentManager(), InviteFailedDialog.class.getName());
    }

    private void onSentInvitationDone(Activity activity) {
        ConfLocalHelper.returnToConf(activity);
        activity.finish();
    }

    public PhonePBXCallHistoryAdapter getDataAdapter() {
        return this.mAdapter;
    }

    public void delete(String str, boolean z) {
        if (!z) {
            this.mSelectList.remove(str);
        } else if (!this.mSelectList.contains(str)) {
            this.mSelectList.add(str);
        }
    }

    public boolean setSelectAllMode() {
        this.mSelectList.clear();
        boolean markAllElements = this.mAdapter.markAllElements();
        if (markAllElements) {
            this.mSelectList.addAll(this.mAdapter.getAllIds());
        }
        this.mAdapter.notifyDataSetChanged();
        return markAllElements;
    }

    public String dialogTitle() {
        if (this.mSelectList.size() >= getDataCount()) {
            return getResources().getString(C4558R.string.zm_sip_delete_all_items_61381);
        }
        return getResources().getQuantityString(C4558R.plurals.zm_sip_delete_x_items_61381, this.mSelectList.size(), new Object[]{Integer.valueOf(this.mSelectList.size())});
    }

    public boolean itemLongClick(final int i) {
        String str;
        if (CmmSIPCallManager.getInstance().isLoginConflict()) {
            return false;
        }
        dismissContextMenuDialog();
        CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) this.mAdapter.getItem(Math.max(0, i));
        if (cmmSIPCallHistoryItemBean == null) {
            return false;
        }
        boolean hasDataNetwork = NetworkUtil.hasDataNetwork(getContext());
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
        ArrayList arrayList = new ArrayList();
        if (hasDataNetwork && !cmmSIPCallHistoryItemBean.isRestricted()) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_lbl_context_menu_call_back), 0));
        }
        if (cmmSIPCallHistoryItemBean.isRecordingExist()) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_play_recording_104213), 7));
        }
        String fromPhoneNumber = cmmSIPCallHistoryItemBean.isInBound() ? cmmSIPCallHistoryItemBean.getFromPhoneNumber() : cmmSIPCallHistoryItemBean.getToPhoneNumber();
        if (!cmmSIPCallHistoryItemBean.isRestricted()) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_copy_number_85339), 5));
            boolean hasMessenger = PTApp.getInstance().hasMessenger();
            boolean z = ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(fromPhoneNumber) != null;
            if (hasMessenger && z) {
                arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_view_profile_94136), 6));
            }
            if (ZMPhoneUtils.isE164Format(fromPhoneNumber) && hasDataNetwork) {
                arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_block_caller_70435), 3));
                if (hasMessenger && !z) {
                    arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_mi_create_new_contact), 8));
                    arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_mi_add_to_existing_contact), 9));
                }
                if (CmmSIPMessageManager.getInstance().isMessageEnabled()) {
                    arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_send_message_117773), 10));
                }
            }
        }
        arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_select_item_61381), 2));
        if (hasDataNetwork) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_delete_item_61381), 1));
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        if (cmmSIPCallHistoryItemBean.isInBound()) {
            str = cmmSIPCallHistoryItemBean.getFromUserName();
        } else {
            str = cmmSIPCallHistoryItemBean.getToUserName();
        }
        String[] strArr = {getContext().getString(C4558R.string.zm_sip_name_duration_90945, new Object[]{TimeUtil.formateDuration((long) cmmSIPCallHistoryItemBean.getCallDuration())})};
        getParentFragment().onSelectLastAccessibilityId(cmmSIPCallHistoryItemBean.getId());
        this.mContextMenuDialog = new Builder(getContext()).setTitleView(DialogUtils.createListViewDialogTitleView(getContext(), strArr, str)).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PhonePBXHistoryListView.this.onSelectContextMenuItem((PhonePBXContextMenuItem) zMMenuAdapter.getItem(i), i);
            }
        }).create();
        this.mContextMenuDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                if (PhonePBXHistoryListView.this.mAccessibilityControl != null) {
                    PhonePBXHistoryListView.this.mAccessibilityControl.onAccessbility();
                }
            }
        });
        this.mContextMenuDialog.setCanceledOnTouchOutside(true);
        this.mContextMenuDialog.show();
        return true;
    }

    public int getDataCount() {
        PhonePBXCallHistoryAdapter phonePBXCallHistoryAdapter = this.mAdapter;
        if (phonePBXCallHistoryAdapter == null) {
            return 0;
        }
        return phonePBXCallHistoryAdapter.getCount();
    }

    public boolean shouldShowEmptyView() {
        return getDataCount() == 0 && this.mPanelLoadMoreView.getVisibility() == 8;
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (i > 0 && i2 + i == i3 && canLoadMore()) {
            onLoadMore();
        }
    }

    private boolean canLoadMore() {
        return !CmmSIPCallManager.getInstance().isLoginConflict() && !this.mAdapter.isSelectMode() && !CmmSIPCallManager.getInstance().isInOffline();
    }

    private void onLoadMore() {
        if (hasNotDeletePendingCallHistory()) {
            loadDataByPage();
        } else if (CmmPBXCallHistoryManager.getInstance().hasMorePastCallHistory() && !CmmPBXCallHistoryManager.getInstance().isCallHistorySyncStarted()) {
            this.mIsLoadingMore = CmmPBXCallHistoryManager.getInstance().requestSyncMoreCallHistory(true);
            updateLoadMoreViewState();
        }
    }

    private boolean hasNotDeletePendingCallHistory() {
        List data = this.mAdapter.getData();
        return CmmPBXCallHistoryManager.getInstance().hasNotDeletePendingCallHistory(data.isEmpty() ? null : ((CmmSIPCallHistoryItemBean) data.get(data.size() - 1)).getId());
    }

    /* access modifiers changed from: protected */
    public void onPullDownRefresh() {
        super.onPullDownRefresh();
        if (CmmSIPCallManager.getInstance().isLoginConflict()) {
            showRefreshing(false);
            return;
        }
        if (!CmmPBXCallHistoryManager.getInstance().requestSyncMoreCallHistory(false)) {
            showRefreshing(false);
        }
    }

    public void onDestroy() {
        dismissContextMenuDialog();
        CmmPBXCallHistoryManager.getInstance().removeISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        CmmSIPCallManager.getInstance().removePBXLoginConflictListener(this.mLoginConflictListener);
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
    }

    private void dismissContextMenuDialog() {
        ZMAlertDialog zMAlertDialog = this.mContextMenuDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mContextMenuDialog.dismiss();
            this.mContextMenuDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void onHistoyItemsDeleted(List<String> list) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            boolean z = false;
            for (int i = 0; i < size; i++) {
                if (this.mAdapter.removeCall((String) list.get(i))) {
                    z = true;
                }
            }
            if (z) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        checkCanClearMissedCallHistory();
    }

    public void checkCanClearMissedCallHistory() {
        if (getVisibility() == 0 && this.mParentFragment.getUserVisibleHint()) {
            this.mCanClearMissedCallHistory = true;
        }
    }

    public void checkAndClearMissedCallHistory() {
        if (this.mCanClearMissedCallHistory) {
            CmmPBXCallHistoryManager.getInstance().clearMissedCallHistory();
            this.mCanClearMissedCallHistory = false;
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(PhonePBXContextMenuItem phonePBXContextMenuItem, int i) {
        if (phonePBXContextMenuItem != null && !phonePBXContextMenuItem.isDisable()) {
            CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) this.mAdapter.getItem(Math.max(0, i));
            if (cmmSIPCallHistoryItemBean != null) {
                String fromPhoneNumber = cmmSIPCallHistoryItemBean.isInBound() ? cmmSIPCallHistoryItemBean.getFromPhoneNumber() : cmmSIPCallHistoryItemBean.getToPhoneNumber();
                switch (phonePBXContextMenuItem.getAction()) {
                    case 0:
                        if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
                            onItemClick(i + getHeaderViewsCount());
                            break;
                        }
                        break;
                    case 1:
                        if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
                            delete(cmmSIPCallHistoryItemBean.getId(), true);
                            checkDelete();
                            getParentFragment().updateEmptyView();
                            break;
                        }
                        break;
                    case 2:
                        getParentFragment().enterSelectMode();
                        View childAt = getChildAt((i + getHeaderViewsCount()) - getFirstVisiblePosition());
                        if (childAt != null) {
                            final CheckBox checkBox = (CheckBox) childAt.findViewById(C4558R.C4560id.checkDeleteItem);
                            if (checkBox != null) {
                                post(new Runnable() {
                                    public void run() {
                                        checkBox.setChecked(true);
                                    }
                                });
                                break;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    case 3:
                        onBlockNumber(cmmSIPCallHistoryItemBean);
                        break;
                    case 5:
                        Toast.makeText(getContext(), getContext().getString(C4558R.string.zm_sip_copy_number_toast_85339), 0).show();
                        AndroidAppUtil.copyText(getContext(), fromPhoneNumber);
                        break;
                    case 6:
                        IPhonePBXParentFragment iPhonePBXParentFragment = this.mParentFragment;
                        if (iPhonePBXParentFragment instanceof Fragment) {
                            AddrBookItemDetailsActivity.show((Fragment) iPhonePBXParentFragment, ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(fromPhoneNumber), 106);
                            break;
                        }
                        break;
                    case 7:
                        playRecording(i);
                        break;
                    case 8:
                        IPhonePBXParentFragment iPhonePBXParentFragment2 = this.mParentFragment;
                        if (iPhonePBXParentFragment2 instanceof Fragment) {
                            ZMPhoneUtils.addNumberToPhoneContact(((Fragment) iPhonePBXParentFragment2).getActivity(), fromPhoneNumber, false);
                            break;
                        } else {
                            return;
                        }
                    case 9:
                        IPhonePBXParentFragment iPhonePBXParentFragment3 = this.mParentFragment;
                        if (iPhonePBXParentFragment3 instanceof Fragment) {
                            ZMPhoneUtils.addNumberToPhoneContact(((Fragment) iPhonePBXParentFragment3).getActivity(), fromPhoneNumber, true);
                            break;
                        } else {
                            return;
                        }
                    case 10:
                        if (CmmSIPMessageManager.getInstance().isMessageEnabled() && !StringUtil.isEmptyOrNull(fromPhoneNumber)) {
                            IPhonePBXParentFragment iPhonePBXParentFragment4 = this.mParentFragment;
                            if (iPhonePBXParentFragment4 instanceof Fragment) {
                                FragmentActivity activity = ((Fragment) iPhonePBXParentFragment4).getActivity();
                                if (activity instanceof ZMActivity) {
                                    PBXSMSActivity.showAsToNumbers((ZMActivity) activity, new ArrayList(Collections.singletonList(fromPhoneNumber)));
                                    break;
                                } else {
                                    return;
                                }
                            }
                        }
                        return;
                }
            }
        }
    }

    private void playRecording(int i) {
        if (!CmmSIPCallManager.getInstance().isLoginConflict()) {
            View childAt = getChildAt((getHeaderViewsCount() + i) - getFirstVisiblePosition());
            CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) this.mAdapter.getItem(Math.max(0, i));
            if (cmmSIPCallHistoryItemBean != null) {
                getParentFragment().displayCoverView(new PBXCallHistory(cmmSIPCallHistoryItemBean), childAt, true);
            }
        }
    }

    private boolean isFileExist(CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean) {
        String localFileName = cmmSIPAudioFileItemBean.getLocalFileName();
        boolean z = false;
        if (!cmmSIPAudioFileItemBean.isFileInLocal()) {
            return false;
        }
        File file = new File(localFileName);
        if (file.exists() && file.length() > 0) {
            z = true;
        }
        return z;
    }

    public void setOnAccessibilityListener(OnAccessibilityListener onAccessibilityListener) {
        this.mAccessibilityControl = onAccessibilityListener;
    }

    public void onLoginConflict() {
        dismissContextMenuDialog();
    }

    public void onResume() {
        PhonePBXCallHistoryAdapter phonePBXCallHistoryAdapter = this.mAdapter;
        if (phonePBXCallHistoryAdapter != null) {
            phonePBXCallHistoryAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void updateLoadMoreViewState() {
        if (this.mAdapter.isSelectMode()) {
            this.mPanelLoadMoreView.setVisibility(8);
        } else if (!CmmPBXCallHistoryManager.getInstance().hasMorePastCallHistory()) {
            this.mPanelLoadMoreView.setVisibility(8);
            this.mParentFragment.updateEmptyView();
        } else {
            this.mPanelLoadMoreView.setVisibility(0);
            if (this.mIsLoadingMore) {
                this.mTxtLoadMore.setText(C4558R.string.zm_msg_loading);
                this.mTxtLoadMore.setEnabled(false);
                this.mProgressLoadMore.setVisibility(0);
            } else {
                this.mTxtLoadMore.setText(C4558R.string.zm_btn_view_more);
                this.mTxtLoadMore.setEnabled(true);
                this.mProgressLoadMore.setVisibility(8);
            }
        }
    }

    private void onBlockNumber(CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean) {
        if (getContext() != null && cmmSIPCallHistoryItemBean != null) {
            getParentFragment().onBlockNumber(new PBXCallHistory(cmmSIPCallHistoryItemBean));
        }
    }
}
