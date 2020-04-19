package com.zipow.videobox.view.sip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
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
import com.zipow.videobox.PBXSMSActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItem;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItemBean;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.CmmSIPVoiceMailItemBean;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.SimpleISIPCallRepositoryEventSinkListener;
import com.zipow.videobox.sip.server.PBXLoginConflictListenerUI.SimplePBXLoginConflictListener;
import com.zipow.videobox.util.CmmPBXListUtil;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.sip.BasePBXHistoryAdapter.IPBXListView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.PullDownRefreshListView;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXVoiceMailListView extends PullDownRefreshListView implements OnItemClickListener, OnScrollListener, IPBXListView {
    private static final int PAGE_SIZE = 50;
    private static final String TAG = "PhonePBXVoiceMailListView";
    /* access modifiers changed from: private */
    public OnAccessibilityListener mAccessibilityListener;
    private PhonePBXVoiceMailHistoryAdapter mAdapter;
    private ZMAlertDialog mContextMenuDialog;
    /* access modifiers changed from: private */
    public boolean mIsLoadingMore = false;
    @NonNull
    private SimplePBXLoginConflictListener mLoginConflictListener = new SimplePBXLoginConflictListener() {
        public void onConflict() {
            super.onConflict();
            PhonePBXVoiceMailListView.this.setPullDownRefreshEnabled(false);
            PhonePBXVoiceMailListView.this.onLoginConflict();
        }

        public void onResumeFromConflict() {
            super.onResumeFromConflict();
            if (!PhonePBXVoiceMailListView.this.getParentFragment().isInSelectMode()) {
                PhonePBXVoiceMailListView.this.clearAndLoadData(true);
            }
            PhonePBXVoiceMailListView.this.setPullDownRefreshEnabled(true);
        }
    };
    SimpleISIPCallRepositoryEventSinkListener mPBXRepositoryListener = new SimpleISIPCallRepositoryEventSinkListener() {
        public void OnFullVoiceMailSyncFinished(boolean z) {
            super.OnFullVoiceMailSyncFinished(z);
            PhonePBXVoiceMailListView.this.getParentFragment().onListViewDatasetChanged(false);
            if (PhonePBXVoiceMailListView.this.getParentFragment().isHasShow()) {
                PhonePBXVoiceMailListView.this.clearAndLoadData(false);
                PhonePBXVoiceMailListView.this.showRefreshing(false);
            }
        }

        public void OnMoreVoiceMailSyncFinished(List<String> list, List<String> list2, boolean z) {
            super.OnMoreVoiceMailSyncFinished(list, list2, z);
            if (z && PhonePBXVoiceMailListView.this.getParentFragment().isHasShow()) {
                List list3 = null;
                List filterVoiceMailHistoryListByID = (list == null || list.isEmpty()) ? null : CmmPBXCallHistoryManager.getInstance().filterVoiceMailHistoryListByID(list);
                if (list2 != null && !list2.isEmpty()) {
                    list3 = CmmPBXCallHistoryManager.getInstance().filterVoiceMailHistoryListByID(list2);
                }
                if (!(filterVoiceMailHistoryListByID == null && list3 == null)) {
                    PhonePBXVoiceMailListView.this.getParentFragment().onListViewDatasetChanged(false);
                    PhonePBXVoiceMailListView.this.onMoreSyncFinished(filterVoiceMailHistoryListByID, list3);
                }
            }
            PhonePBXVoiceMailListView.this.showRefreshing(false);
            if (PhonePBXVoiceMailListView.this.mIsLoadingMore && (list == null || list.size() <= 0)) {
                PhonePBXVoiceMailListView.this.mIsLoadingMore = false;
            }
            PhonePBXVoiceMailListView.this.updateLoadMoreViewState();
        }

        public void OnVoiceMailStatusChanged(List<String> list, boolean z, boolean z2) {
            super.OnVoiceMailStatusChanged(list, z, z2);
            if (z2) {
                PhonePBXVoiceMailListView.this.onVoiceMailStatusChanged(list, z);
            }
        }

        public void OnVoiceMailDeleted(List<String> list, boolean z) {
            super.OnVoiceMailDeleted(list, z);
            if (z) {
                PhonePBXVoiceMailListView.this.mParentFragment.onListViewDatasetChanged(false);
                PhonePBXVoiceMailListView.this.onVoiceMailItemsDeleted(list);
                PhonePBXVoiceMailListView.this.mParentFragment.updateEmptyView();
            }
        }

        public void OnAudioFileDownloadFinished(String str, int i, int i2) {
            super.OnAudioFileDownloadFinished(str, i, i2);
            CmmSIPAudioFileItem audioFileItemByID = CmmPBXCallHistoryManager.getInstance().getAudioFileItemByID(str, i);
            if (i2 == 0 && i == 1) {
                PhonePBXVoiceMailListView.this.setVoiceMailAudioFileDownloadComplete(audioFileItemByID);
            }
        }
    };
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onDataNetworkStatusChanged(boolean z) {
            super.onDataNetworkStatusChanged(z);
            if (z && PhonePBXVoiceMailListView.this.getParentFragment().isHasShow()) {
                PhonePBXVoiceMailListView.this.showRefreshing(true);
                PhonePBXVoiceMailListView.this.onPullDownRefresh();
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
    public void onMoreSyncFinished(List<CmmSIPVoiceMailItemBean> list, List<CmmSIPVoiceMailItemBean> list2) {
        List addLatestList = CmmPBXListUtil.addLatestList(list, this.mAdapter.getData());
        if (addLatestList == null) {
            addLatestList = new ArrayList();
        }
        if (!CollectionsUtil.isListEmpty(list2)) {
            addLatestList.addAll(list2);
        }
        updateData(addLatestList);
    }

    public PhonePBXVoiceMailListView(Context context) {
        super(context);
        init();
    }

    public PhonePBXVoiceMailListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PhonePBXVoiceMailListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void init() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_list_load_more_footer, null);
        this.mPanelLoadMoreView = inflate.findViewById(C4558R.C4560id.panelLoadMoreView);
        this.mProgressLoadMore = (ProgressBar) inflate.findViewById(C4558R.C4560id.progressBar);
        this.mTxtLoadMore = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        addFooterView(inflate);
        this.mAdapter = new PhonePBXVoiceMailHistoryAdapter(getContext(), this);
        setAdapter(this.mAdapter);
        setTextResources(C4558R.string.zm_lbl_release_to_load_more, C4558R.string.zm_lbl_pull_down_to_load_more, C4558R.string.zm_empty_string);
        setOnItemClickListener(this);
        setOnScrollListener(this);
        CmmPBXCallHistoryManager.getInstance().addISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        CmmSIPCallManager.getInstance().addPBXLoginConflictListener(this.mLoginConflictListener);
        PTUI.getInstance().addPTUIListener(this.mPTUIListener);
    }

    public void setVoiceMailAudioFileDownloadComplete(CmmSIPAudioFileItem cmmSIPAudioFileItem) {
        int count = this.mAdapter.getCount();
        PhonePBXVoiceMailHistoryAdapter phonePBXVoiceMailHistoryAdapter = this.mAdapter;
        for (int i = 0; i < count; i++) {
            CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = (CmmSIPVoiceMailItemBean) phonePBXVoiceMailHistoryAdapter.getItem(i);
            if (cmmSIPVoiceMailItemBean != null) {
                int size = cmmSIPVoiceMailItemBean.getAudioFileList() != null ? cmmSIPVoiceMailItemBean.getAudioFileList().size() : 0;
                if (size > 0) {
                    List audioFileList = cmmSIPVoiceMailItemBean.getAudioFileList();
                    int i2 = 0;
                    while (i2 < size) {
                        CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean = (CmmSIPAudioFileItemBean) audioFileList.get(i2);
                        if (cmmSIPAudioFileItemBean == null || cmmSIPAudioFileItem == null || !cmmSIPAudioFileItem.getID().equals(cmmSIPAudioFileItemBean.getId())) {
                            i2++;
                        } else {
                            cmmSIPAudioFileItem.toSIPAudioFileItemBean(cmmSIPAudioFileItemBean);
                            return;
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (!CmmSIPCallManager.getInstance().isLoginConflict() && !this.mParentFragment.isInSelectMode()) {
            int max = Math.max(0, i - getHeaderViewsCount());
            if (max == (getAdapter().getCount() - getHeaderViewsCount()) - 1) {
                onLoadMore();
                updateLoadMoreViewState();
                return;
            }
            CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = (CmmSIPVoiceMailItemBean) this.mAdapter.getItem(max);
            if (cmmSIPVoiceMailItemBean != null) {
                this.mParentFragment.onSelectLastAccessibilityId(cmmSIPVoiceMailItemBean.getId());
                if (cmmSIPVoiceMailItemBean.getAudioFileList() != null && !cmmSIPVoiceMailItemBean.getAudioFileList().isEmpty()) {
                    PBXCallHistory pBXCallHistory = new PBXCallHistory(cmmSIPVoiceMailItemBean);
                    if (NetworkUtil.hasDataNetwork(getContext()) || isFileExist(pBXCallHistory.audioFile)) {
                        this.mParentFragment.displayCoverView(pBXCallHistory, view, cmmSIPVoiceMailItemBean.isUnread());
                    } else {
                        new Builder(getContext()).setTitle(C4558R.string.zm_sip_error_network_unavailable_99728).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null).show();
                    }
                }
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

    public void setParentFragment(IPhonePBXParentFragment iPhonePBXParentFragment) {
        this.mParentFragment = iPhonePBXParentFragment;
    }

    public IPhonePBXParentFragment getParentFragment() {
        return this.mParentFragment;
    }

    public void checkDeleteVoiceMails() {
        onVoiceMailItemsDeleted(this.mSelectList);
        if (!this.mSelectList.isEmpty()) {
            CmmPBXCallHistoryManager.getInstance().deleteVoiceMailHistory(this.mSelectList);
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
        CmmPBXCallHistoryManager.getInstance().clearVoiceMailHistory();
    }

    public void clearAndLoadData(boolean z) {
        this.mAdapter.clearAll();
        loadData();
        if (z) {
            CmmPBXCallHistoryManager.getInstance().requestSyncMoreVoiceMail(false);
        }
    }

    public void loadData() {
        if (this.mAdapter.getCount() <= 0) {
            loadDataByPage();
        }
    }

    public void loadDataByPage() {
        CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = (CmmSIPVoiceMailItemBean) this.mAdapter.getItem(Math.max(0, this.mAdapter.getCount() - 1));
        String str = "";
        if (cmmSIPVoiceMailItemBean != null) {
            str = cmmSIPVoiceMailItemBean.getId();
        }
        List voiceMailHistoryListByPage = CmmPBXCallHistoryManager.getInstance().getVoiceMailHistoryListByPage(str, 50);
        if (voiceMailHistoryListByPage == null || voiceMailHistoryListByPage.isEmpty()) {
            updateLoadMoreViewState();
        } else {
            addData(voiceMailHistoryListByPage);
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

    public void sendSipCall(String str, String str2) {
        if (CmmSIPCallManager.getInstance().checkNetwork(getContext()) && CmmSIPCallManager.getInstance().checkIsPbxActive(getContext())) {
            this.mParentFragment.onPickSipResult(str, str2);
        }
    }

    public void sendSipCallWithCheckError(String str, String str2) {
        if (CmmSIPCallManager.getInstance().checkNetwork(getContext()) && CmmSIPCallManager.getInstance().checkIsPbxActive(getContext())) {
            this.mParentFragment.onPickSipResult(str, str2);
        }
    }

    public int getDataCount() {
        PhonePBXVoiceMailHistoryAdapter phonePBXVoiceMailHistoryAdapter = this.mAdapter;
        if (phonePBXVoiceMailHistoryAdapter == null) {
            return 0;
        }
        return phonePBXVoiceMailHistoryAdapter.getCount();
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
        if (hasNotDeletePendingVoiceMail()) {
            loadDataByPage();
        } else if (CmmPBXCallHistoryManager.getInstance().hasMorePastVoiceMail() && !CmmPBXCallHistoryManager.getInstance().isVoiceMailSyncStarted()) {
            this.mIsLoadingMore = CmmPBXCallHistoryManager.getInstance().requestSyncMoreVoiceMail(true);
            updateLoadMoreViewState();
        }
    }

    private boolean hasNotDeletePendingVoiceMail() {
        List data = this.mAdapter.getData();
        return CmmPBXCallHistoryManager.getInstance().hasNotDeletePendingVoiceMail(data.isEmpty() ? null : ((CmmSIPVoiceMailItemBean) data.get(data.size() - 1)).getId());
    }

    /* access modifiers changed from: protected */
    public void onPullDownRefresh() {
        super.onPullDownRefresh();
        if (CmmSIPCallManager.getInstance().isLoginConflict()) {
            showRefreshing(false);
            return;
        }
        if (!CmmPBXCallHistoryManager.getInstance().requestSyncMoreVoiceMail(false)) {
            showRefreshing(false);
        }
    }

    public void onDestroy() {
        dismissContextMenuDialog();
        CmmSIPCallManager.getInstance().removePBXLoginConflictListener(this.mLoginConflictListener);
        CmmPBXCallHistoryManager.getInstance().removeISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
    }

    public void delete(String str, boolean z) {
        if (!z) {
            this.mSelectList.remove(str);
        } else if (!this.mSelectList.contains(str)) {
            this.mSelectList.add(str);
        }
    }

    public void changeVoiceMailStatusToRead(String str) {
        if (CmmPBXCallHistoryManager.getInstance().changeVoiceMailStatusToRead(str)) {
            updateUIToRead(str);
        }
    }

    private void updateUIToRead(String str) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(str);
        onVoiceMailStatusChanged(arrayList, false);
    }

    /* access modifiers changed from: private */
    public void onVoiceMailStatusChanged(List<String> list, boolean z) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            boolean z2 = false;
            for (int i = 0; i < size; i++) {
                if (this.mAdapter.changeVoiceMailStatus((String) list.get(i), z)) {
                    z2 = true;
                }
            }
            if (z2) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onVoiceMailItemsDeleted(List<String> list) {
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

    public String dialogTitle() {
        if (this.mSelectList.size() >= getDataCount()) {
            return getResources().getString(C4558R.string.zm_sip_delete_all_items_61381);
        }
        return getResources().getQuantityString(C4558R.plurals.zm_sip_delete_x_items_61381, this.mSelectList.size(), new Object[]{Integer.valueOf(this.mSelectList.size())});
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
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

    public boolean itemLongClick(final int i) {
        boolean z = false;
        if (CmmSIPCallManager.getInstance().isLoginConflict()) {
            return false;
        }
        dismissContextMenuDialog();
        CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = (CmmSIPVoiceMailItemBean) this.mAdapter.getItem(Math.max(0, i));
        String id = cmmSIPVoiceMailItemBean.getId();
        boolean hasDataNetwork = NetworkUtil.hasDataNetwork(getContext());
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
        ArrayList arrayList = new ArrayList();
        if (hasDataNetwork && !cmmSIPVoiceMailItemBean.isRestricted()) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_lbl_context_menu_call_back), 0));
        }
        if (!cmmSIPVoiceMailItemBean.isRestricted()) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_copy_number_85339), 5));
            boolean hasMessenger = PTApp.getInstance().hasMessenger();
            if (ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(cmmSIPVoiceMailItemBean.getFromPhoneNumber()) != null) {
                z = true;
            }
            if (hasMessenger && z) {
                arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_view_profile_94136), 6));
            }
            if (hasDataNetwork && ZMPhoneUtils.isE164Format(cmmSIPVoiceMailItemBean.getFromPhoneNumber())) {
                if (hasMessenger && !z) {
                    arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_mi_create_new_contact), 8));
                    arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_mi_add_to_existing_contact), 9));
                }
                if (CmmSIPMessageManager.getInstance().isMessageEnabled()) {
                    arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_send_message_117773), 10));
                }
                arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_block_caller_70435), 3));
            }
        }
        arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_select_item_61381), 2));
        if (hasDataNetwork) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_delete_item_61381), 1));
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        getParentFragment().onSelectLastAccessibilityId(id);
        this.mContextMenuDialog = new Builder(getContext()).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PhonePBXVoiceMailListView.this.onSelectContextMenuItem((PhonePBXContextMenuItem) zMMenuAdapter.getItem(i), i);
            }
        }).create();
        this.mContextMenuDialog.setCanceledOnTouchOutside(true);
        this.mContextMenuDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                if (PhonePBXVoiceMailListView.this.mAccessibilityListener != null) {
                    PhonePBXVoiceMailListView.this.mAccessibilityListener.onAccessbility();
                }
            }
        });
        this.mContextMenuDialog.show();
        return true;
    }

    private void dismissContextMenuDialog() {
        ZMAlertDialog zMAlertDialog = this.mContextMenuDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mContextMenuDialog.dismiss();
            this.mContextMenuDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(PhonePBXContextMenuItem phonePBXContextMenuItem, int i) {
        if (phonePBXContextMenuItem != null && !phonePBXContextMenuItem.isDisable()) {
            CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = (CmmSIPVoiceMailItemBean) this.mAdapter.getItem(Math.max(0, i));
            if (cmmSIPVoiceMailItemBean != null) {
                String fromPhoneNumber = cmmSIPVoiceMailItemBean.getFromPhoneNumber();
                switch (phonePBXContextMenuItem.getAction()) {
                    case 0:
                        if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
                            sendSipCallWithCheckError(fromPhoneNumber, cmmSIPVoiceMailItemBean.getDisplayName());
                            break;
                        }
                        break;
                    case 1:
                        if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
                            String id = cmmSIPVoiceMailItemBean.getId();
                            if (!TextUtils.isEmpty(id)) {
                                delete(id, true);
                                checkDeleteVoiceMails();
                                getParentFragment().updateEmptyView();
                                break;
                            } else {
                                return;
                            }
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
                        onBlockNumber(cmmSIPVoiceMailItemBean);
                        break;
                    case 5:
                        if (!StringUtil.isEmptyOrNull(fromPhoneNumber)) {
                            Toast.makeText(getContext(), getContext().getString(C4558R.string.zm_sip_copy_number_toast_85339), 0).show();
                            AndroidAppUtil.copyText(getContext(), fromPhoneNumber);
                            break;
                        }
                        break;
                    case 6:
                        IPhonePBXParentFragment iPhonePBXParentFragment = this.mParentFragment;
                        if (iPhonePBXParentFragment instanceof Fragment) {
                            AddrBookItemDetailsActivity.show((Fragment) iPhonePBXParentFragment, ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(fromPhoneNumber), 106);
                            break;
                        }
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

    public void onLoginConflict() {
        dismissContextMenuDialog();
    }

    /* access modifiers changed from: private */
    public void updateLoadMoreViewState() {
        if (this.mAdapter.isSelectMode()) {
            this.mPanelLoadMoreView.setVisibility(8);
        } else if (!CmmPBXCallHistoryManager.getInstance().hasMorePastVoiceMail()) {
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

    public PhonePBXVoiceMailHistoryAdapter getDataAdapter() {
        return this.mAdapter;
    }

    public void setAccessibilityListener(OnAccessibilityListener onAccessibilityListener) {
        this.mAccessibilityListener = onAccessibilityListener;
    }

    private void onBlockNumber(CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean) {
        if (getContext() != null && cmmSIPVoiceMailItemBean != null) {
            getParentFragment().onBlockNumber(new PBXCallHistory(cmmSIPVoiceMailItemBean));
        }
    }
}
