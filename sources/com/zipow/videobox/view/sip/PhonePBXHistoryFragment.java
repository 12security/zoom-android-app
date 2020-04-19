package com.zipow.videobox.view.sip;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.sip.server.CmmCallHistoryFilterDataBean;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.view.BigRoundListDialog;
import com.zipow.videobox.view.BigRoundListDialog.DialogCallback;
import com.zipow.videobox.view.CallHistoryFilterItem;
import com.zipow.videobox.view.ZMListAdapter;
import com.zipow.videobox.view.sip.PhonePBXTabFragment.IListCoverListener;
import com.zipow.videobox.view.sip.PhonePBXTabFragment.IPhonePBX;
import com.zipow.videobox.view.sip.PhonePBXTabFragment.IPhonePBXAccessbility;
import com.zipow.videobox.view.sip.PhonePBXTabFragment.OnFragmentShowListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.IZMListItem;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXHistoryFragment extends ZMDialogFragment implements OnClickListener, IPhonePBXParentFragment, IPhonePBXAccessbility, IPhonePBX, OnFragmentShowListener, IListCoverListener {
    private static final int MSG_FILTER = 100;
    private static final String TAG = "PhonePBXHistoryFragment";
    private String mAccessibilitySelectId;
    /* access modifiers changed from: private */
    public TextView mBtnFilter;
    private View mBtnKeyboard;
    private View mEmptyPanel;
    /* access modifiers changed from: private */
    public List<CmmCallHistoryFilterDataBean> mFilterDataList = null;
    /* access modifiers changed from: private */
    public BigRoundListDialog mFilterDialog;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            if (message.what == 100) {
                if (PhonePBXHistoryFragment.this.mFilterDataList != null) {
                    PhonePBXHistoryFragment.this.mListview.forceRefreshData();
                }
                PhonePBXHistoryFragment.this.updateEmptyView();
            }
        }
    };
    private boolean mHasShow = false;
    private View mLayoutFilter;
    /* access modifiers changed from: private */
    public PhonePBXHistoryListView mListview;
    private TextView mTxtEmptyView;
    private TextView mTxtEmptyViewTitle;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_pbx_history, viewGroup, false);
        this.mLayoutFilter = inflate.findViewById(C4558R.C4560id.layout_filter);
        this.mBtnFilter = (TextView) inflate.findViewById(C4558R.C4560id.btnFilter);
        this.mListview = (PhonePBXHistoryListView) inflate.findViewById(C4558R.C4560id.listviewAllCalls);
        this.mEmptyPanel = inflate.findViewById(C4558R.C4560id.panelEmptyView);
        this.mTxtEmptyViewTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyViewTitle);
        this.mTxtEmptyView = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mBtnKeyboard = inflate.findViewById(C4558R.C4560id.ivKeyboard);
        this.mListview.setEmptyView(this.mEmptyPanel);
        this.mListview.setParentFragment(this);
        this.mListview.setOnAccessibilityListener(new OnAccessibilityListener() {
            public void onAccessbility() {
                PhonePBXHistoryFragment.this.accessibilityControl(1000);
            }
        });
        this.mBtnKeyboard.setOnClickListener(this);
        this.mBtnFilter.setOnClickListener(this);
        if (bundle != null) {
            this.mHasShow = bundle.getBoolean("mHasShow");
        }
        return inflate;
    }

    /* access modifiers changed from: private */
    public void updateUIOnVisible() {
        if (isUserVisible() && isAdded()) {
            PhonePBXHistoryListView phonePBXHistoryListView = this.mListview;
            if (phonePBXHistoryListView != null) {
                phonePBXHistoryListView.loadData();
                updateFilterLayout();
                updateEmptyView();
            }
            CmmPBXCallHistoryManager.getInstance().clearMissedCallHistory();
        }
    }

    public void updateEmptyView() {
        if (this.mListview.getDataCount() <= 0) {
            CmmCallHistoryFilterDataBean selectedFilterBean = getSelectedFilterBean();
            int i = C4558R.string.zm_sip_call_history_empty_view_title_61381;
            int i2 = C4558R.string.zm_sip_call_history_empty_view_61381;
            if (selectedFilterBean != null) {
                switch (selectedFilterBean.getFilterType()) {
                    case 2:
                        i = C4558R.string.zm_sip_call_history_missed_empty_view_title_109884;
                        i2 = C4558R.string.zm_sip_call_history_missed_empty_view_109884;
                        break;
                    case 3:
                        i = C4558R.string.zm_sip_call_history_recording_empty_view_title_109884;
                        i2 = C4558R.string.zm_sip_call_history_recording_empty_view_109884;
                        break;
                }
            }
            this.mTxtEmptyViewTitle.setText(i);
            this.mTxtEmptyView.setText(i2);
        }
    }

    public void displayCoverView(@NonNull PBXCallHistory pBXCallHistory, View view, boolean z) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            ((PhonePBXTabFragment) parentFragment).displayCoverView(pBXCallHistory, view, z);
        }
    }

    private CmmCallHistoryFilterDataBean getSelectedFilterBean() {
        List<CmmCallHistoryFilterDataBean> list = this.mFilterDataList;
        CmmCallHistoryFilterDataBean cmmCallHistoryFilterDataBean = null;
        if (list == null) {
            return null;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CmmCallHistoryFilterDataBean cmmCallHistoryFilterDataBean2 = (CmmCallHistoryFilterDataBean) this.mFilterDataList.get(i);
            if (cmmCallHistoryFilterDataBean2.isChecked()) {
                cmmCallHistoryFilterDataBean = cmmCallHistoryFilterDataBean2;
            }
        }
        return cmmCallHistoryFilterDataBean;
    }

    /* access modifiers changed from: private */
    public void updateFilter() {
        updateFilterLayout();
        updateFilterDialog();
        if (isHasShow() && isUserVisible()) {
            this.mHandler.removeMessages(100);
            this.mHandler.sendEmptyMessageDelayed(100, 300);
        }
    }

    public void updateFilterLayout() {
        List<CmmCallHistoryFilterDataBean> list = this.mFilterDataList;
        if (list == null || list.isEmpty()) {
            this.mFilterDataList = CmmPBXCallHistoryManager.getInstance().getCallHistoryFilterDataList();
        }
        this.mLayoutFilter.setVisibility(isInSelectMode() ? 8 : 0);
        updateBtnFilter();
    }

    private void updateBtnFilter() {
        List<CmmCallHistoryFilterDataBean> list = this.mFilterDataList;
        boolean z = list != null && list.size() > 1;
        this.mBtnFilter.setVisibility(z ? 0 : 8);
        if (z) {
            CmmCallHistoryFilterDataBean selectedFilterBean = getSelectedFilterBean();
            this.mBtnFilter.setText((selectedFilterBean == null || selectedFilterBean.getFilterType() == 1) ? getString(C4558R.string.zm_pbx_call_history_filter_all_title_108317) : selectedFilterBean.getDisplayName(getContext()));
            this.mBtnFilter.setContentDescription(getString(C4558R.string.zm_pbx_call_history_filter_desc_108317, this.mBtnFilter.getText().toString()));
        }
    }

    private void updateFilterDialog() {
        BigRoundListDialog bigRoundListDialog = this.mFilterDialog;
        if (bigRoundListDialog != null && bigRoundListDialog.isShowing()) {
            ZMListAdapter adapter = this.mFilterDialog.getAdapter();
            if (adapter != null) {
                List generateFilterDataList = generateFilterDataList();
                if (generateFilterDataList != null) {
                    adapter.setList(generateFilterDataList);
                } else {
                    adapter.getList().clear();
                }
                adapter.notifyDataSetChanged();
                this.mFilterDialog.invalidate();
            }
        }
    }

    @Nullable
    private List<CallHistoryFilterItem> generateFilterDataList() {
        List<CmmCallHistoryFilterDataBean> list = this.mFilterDataList;
        if (list == null) {
            return null;
        }
        int size = list.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            CallHistoryFilterItem callHistoryFilterItem = new CallHistoryFilterItem((CmmCallHistoryFilterDataBean) this.mFilterDataList.get(i));
            callHistoryFilterItem.init(getContext());
            arrayList.add(callHistoryFilterItem);
        }
        return arrayList;
    }

    public boolean isHasShow() {
        if (!this.mHasShow) {
            return false;
        }
        return isParentHasShow();
    }

    private boolean isParentHasShow() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            return ((PhonePBXTabFragment) parentFragment).isHasShow();
        }
        return false;
    }

    public boolean isUserVisible() {
        if (!getUserVisibleHint()) {
            return false;
        }
        return isParentUserVisible();
    }

    public boolean isParentUserVisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            return parentFragment.getUserVisibleHint();
        }
        return false;
    }

    public void onSelectLastAccessibilityId(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAccessibilitySelectId = str;
        }
    }

    public boolean isInSelectMode() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            return ((PhonePBXTabFragment) parentFragment).isInSelectMode();
        }
        return false;
    }

    public void checkDelete() {
        PhonePBXHistoryListView phonePBXHistoryListView = this.mListview;
        if (phonePBXHistoryListView != null) {
            phonePBXHistoryListView.checkDelete();
        }
    }

    public boolean setSelectAllMode() {
        PhonePBXHistoryListView phonePBXHistoryListView = this.mListview;
        if (phonePBXHistoryListView != null) {
            return phonePBXHistoryListView.setSelectAllMode();
        }
        return false;
    }

    public void onDeleteInSelectMode() {
        PhonePBXHistoryListView phonePBXHistoryListView = this.mListview;
        String dialogTitle = phonePBXHistoryListView != null ? phonePBXHistoryListView.dialogTitle() : "";
        if (!TextUtils.isEmpty(dialogTitle)) {
            DialogUtils.showAlertDialog((ZMActivity) getActivity(), dialogTitle, getString(C4558R.string.zm_sip_delete_warn_61381), C4558R.string.zm_btn_delete, C4558R.string.zm_btn_cancel, true, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PhonePBXHistoryFragment.this.checkDelete();
                    Fragment parentFragment = PhonePBXHistoryFragment.this.getParentFragment();
                    if (parentFragment instanceof PhonePBXTabFragment) {
                        ((PhonePBXTabFragment) parentFragment).exitSelectMode();
                    }
                }
            }, null);
        }
    }

    public View getListView() {
        return this.mListview;
    }

    public void enterSelectMode() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && (parentFragment instanceof PhonePBXTabFragment)) {
            ((PhonePBXTabFragment) parentFragment).enterSelectMode();
        }
    }

    public void onEnterSelectMode() {
        PhonePBXHistoryListView phonePBXHistoryListView = this.mListview;
        if (phonePBXHistoryListView != null) {
            phonePBXHistoryListView.setSelectMode(true);
        }
        updateFilterLayout();
    }

    public void onExitSelectMode() {
        PhonePBXHistoryListView phonePBXHistoryListView = this.mListview;
        if (phonePBXHistoryListView != null) {
            phonePBXHistoryListView.clearSelectList();
            this.mListview.setSelectMode(false);
        }
        updateFilterLayout();
        updateEmptyView();
    }

    public void onListViewDatasetChanged(boolean z) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && (parentFragment instanceof PhonePBXTabFragment)) {
            ((PhonePBXTabFragment) parentFragment).onListViewDatasetChanged(z);
        }
    }

    public void accessibilityControl(long j) {
        if (!TextUtils.isEmpty(this.mAccessibilitySelectId) && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            PhonePBXHistoryListView phonePBXHistoryListView = this.mListview;
            if (phonePBXHistoryListView == null) {
                this.mAccessibilitySelectId = null;
                return;
            }
            PhonePBXCallHistoryAdapter dataAdapter = phonePBXHistoryListView.getDataAdapter();
            if (dataAdapter == null) {
                this.mAccessibilitySelectId = null;
                return;
            }
            int indexById = dataAdapter.getIndexById(this.mAccessibilitySelectId);
            if (this.mListview.getDataCount() <= indexById) {
                this.mAccessibilitySelectId = null;
                return;
            }
            final View childAt = this.mListview.getChildAt(indexById + this.mListview.getHeaderViewsCount());
            if (childAt == null) {
                this.mAccessibilitySelectId = null;
            } else {
                childAt.postDelayed(new Runnable() {
                    public void run() {
                        if (PhonePBXHistoryFragment.this.isResumed() && PhonePBXHistoryFragment.this.isUserVisible()) {
                            PhonePBXHistoryFragment.this.mListview.requestFocus();
                            AccessibilityUtil.sendAccessibilityFocusEvent(childAt);
                        }
                    }
                }, j);
            }
        }
    }

    public void onShow() {
        this.mHasShow = true;
        this.mHandler.post(new Runnable() {
            public void run() {
                PhonePBXHistoryFragment.this.updateUIOnVisible();
            }
        });
    }

    public void onClick(View view) {
        if (view == this.mBtnFilter) {
            this.mAccessibilitySelectId = null;
            onClickBtnFilter();
        } else if (view == this.mBtnKeyboard) {
            this.mAccessibilitySelectId = null;
            onClickKeyboard();
        }
    }

    private void onClickBtnFilter() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mFilterDataList = CmmPBXCallHistoryManager.getInstance().getCallHistoryFilterDataList();
            List<CmmCallHistoryFilterDataBean> list = this.mFilterDataList;
            if (list != null && list.size() > 1) {
                BigRoundListDialog bigRoundListDialog = this.mFilterDialog;
                if (bigRoundListDialog == null || !bigRoundListDialog.isShowing()) {
                    this.mFilterDialog = new BigRoundListDialog(activity);
                    this.mFilterDialog.setDismissOnItemClicked(true);
                    this.mFilterDialog.setTitle(C4558R.string.zm_pbx_call_history_filter_title_108317);
                    PBXFilterAdapter pBXFilterAdapter = new PBXFilterAdapter(getContext());
                    pBXFilterAdapter.setList(generateFilterDataList());
                    this.mFilterDialog.setAdapter(pBXFilterAdapter);
                    this.mFilterDialog.setDialogCallback(new DialogCallback() {
                        public void onItemSelected(int i) {
                            ZMListAdapter adapter = PhonePBXHistoryFragment.this.mFilterDialog.getAdapter();
                            if (adapter != null) {
                                List list = adapter.getList();
                                int size = list.size() - 1;
                                while (size >= 0) {
                                    IZMListItem iZMListItem = (IZMListItem) list.get(size);
                                    if (iZMListItem instanceof CallHistoryFilterItem) {
                                        CallHistoryFilterItem callHistoryFilterItem = (CallHistoryFilterItem) iZMListItem;
                                        callHistoryFilterItem.setSelected(size == i);
                                        if (size == i) {
                                            CmmPBXCallHistoryManager.getInstance().checkCallHistoryFilterType(callHistoryFilterItem.getFilterType(), true);
                                        }
                                        ((CmmCallHistoryFilterDataBean) PhonePBXHistoryFragment.this.mFilterDataList.get(size)).setChecked(iZMListItem.isSelected());
                                    }
                                    size--;
                                }
                                if (PhonePBXHistoryFragment.this.mFilterDialog.getAdapter() != null) {
                                    PhonePBXHistoryFragment.this.mFilterDialog.getAdapter().notifyDataSetChanged();
                                }
                            }
                            PhonePBXHistoryFragment.this.updateFilter();
                        }

                        public void onCancel() {
                            PhonePBXHistoryFragment.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    AccessibilityUtil.sendAccessibilityFocusEvent(PhonePBXHistoryFragment.this.mBtnFilter);
                                }
                            }, 1000);
                        }

                        public void onClose() {
                            PhonePBXHistoryFragment.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    AccessibilityUtil.sendAccessibilityFocusEvent(PhonePBXHistoryFragment.this.mBtnFilter);
                                }
                            }, 1000);
                        }
                    });
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        this.mFilterDialog.show();
                    }
                    return;
                }
                this.mFilterDialog.dismiss();
                this.mFilterDialog = null;
            }
        }
    }

    private void onClickKeyboard() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            ((PhonePBXTabFragment) parentFragment).openKeyboard();
        }
    }

    public void onPickSipResult(@Nullable String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof PhonePBXTabFragment) {
                ((PhonePBXTabFragment) parentFragment).onPickSipResult(str, str2);
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mListview.onDestroy();
    }

    public void onExpandStart() {
        this.mListview.setVerticalScrollBarEnabled(false);
    }

    public void onCollapseEnd() {
        this.mListview.setVerticalScrollBarEnabled(true);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mHasShow", this.mHasShow);
    }

    public void onViewStateRestored(@Nullable Bundle bundle) {
        super.onViewStateRestored(bundle);
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z && isAdded()) {
            this.mHasShow = true;
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                PhonePBXHistoryFragment.this.updateUIOnVisible();
            }
        });
        accessibilityControl(1000);
    }

    public void onBlockNumber(@NonNull PBXCallHistory pBXCallHistory) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            ((PhonePBXTabFragment) parentFragment).blockNumber(pBXCallHistory);
        }
    }
}
