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
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPVoiceMailSharedRelationshipBean;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.SimpleISIPCallRepositoryEventSinkListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.view.BigRoundListDialog;
import com.zipow.videobox.view.BigRoundListDialog.DialogCallback;
import com.zipow.videobox.view.VoiceMailFilterItem;
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

public class PhonePBXVoiceMailFragment extends ZMDialogFragment implements OnClickListener, IPhonePBXParentFragment, IPhonePBXAccessbility, IPhonePBX, OnFragmentShowListener, IListCoverListener {
    private static final int MSG_FILTER = 100;
    private static final String TAG = "PhonePBXVoiceMailFragment";
    private String mAccessibilitySelectId = null;
    /* access modifiers changed from: private */
    public TextView mBtnFilter;
    private View mBtnKeyboard;
    private View mEmptyPanel;
    /* access modifiers changed from: private */
    public List<CmmSIPVoiceMailSharedRelationshipBean> mFilterDataList = null;
    /* access modifiers changed from: private */
    public BigRoundListDialog mFilterDialog;
    private boolean mForceUpdate = false;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            if (message.what == 100) {
                if (PhonePBXVoiceMailFragment.this.mFilterDataList != null) {
                    PhonePBXVoiceMailFragment.this.mListview.forceRefreshData();
                }
                PhonePBXVoiceMailFragment.this.updateEmptyView();
            }
        }
    };
    private boolean mHasShow = false;
    /* access modifiers changed from: private */
    public View mLayoutFilter;
    private SimpleISIPLineMgrEventSinkListener mLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            if (cmmSIPCallRegResult.isRegistered() && CmmSIPLineManager.getInstance().isMineLine(str)) {
                PhonePBXVoiceMailFragment.this.updateFilter();
            }
        }
    };
    /* access modifiers changed from: private */
    public PhonePBXVoiceMailListView mListview;
    @Nullable
    SimpleISIPCallRepositoryEventSinkListener mPBXRepositoryListener = new SimpleISIPCallRepositoryEventSinkListener() {
        public void OnUpdateVoicemailSharedRelationship() {
            super.OnUpdateVoicemailSharedRelationship();
            PhonePBXVoiceMailFragment.this.forceUpdateFilter();
        }
    };
    private TextView mTxtEmptyView;
    private TextView mTxtEmptyViewTitle;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_pbx_voicemail, viewGroup, false);
        this.mLayoutFilter = inflate.findViewById(C4558R.C4560id.layout_filter);
        this.mBtnFilter = (TextView) inflate.findViewById(C4558R.C4560id.btnFilter);
        this.mListview = (PhonePBXVoiceMailListView) inflate.findViewById(C4558R.C4560id.listviewVoiceMails);
        this.mEmptyPanel = inflate.findViewById(C4558R.C4560id.panelEmptyView);
        this.mTxtEmptyViewTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyViewTitle);
        this.mTxtEmptyView = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mBtnKeyboard = inflate.findViewById(C4558R.C4560id.ivKeyboard);
        this.mListview.setEmptyView(this.mEmptyPanel);
        this.mListview.setParentFragment(this);
        this.mListview.setAccessibilityListener(new OnAccessibilityListener() {
            public void onAccessbility() {
                PhonePBXVoiceMailFragment.this.accessibilityControl(1000);
            }
        });
        this.mBtnKeyboard.setOnClickListener(this);
        this.mBtnFilter.setOnClickListener(this);
        CmmPBXCallHistoryManager.getInstance().addISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
        if (bundle != null) {
            this.mHasShow = bundle.getBoolean("mHasShow");
        }
        return inflate;
    }

    private void updateUI() {
        updateFilter();
        updateEmptyView();
    }

    /* access modifiers changed from: private */
    public void updateUIOnVisible() {
        if (isUserVisible() && isAdded()) {
            PhonePBXVoiceMailListView phonePBXVoiceMailListView = this.mListview;
            if (phonePBXVoiceMailListView != null) {
                phonePBXVoiceMailListView.loadData();
                updateFilterLayout();
                updateEmptyView();
            }
        }
    }

    public void updateEmptyView() {
        if (this.mListview.getDataCount() <= 0) {
            this.mTxtEmptyViewTitle.setText(C4558R.string.zm_sip_call_mail_empty_view_title_61381);
            this.mTxtEmptyView.setText(C4558R.string.zm_sip_call_mail_empty_view_61381);
        }
    }

    public void displayCoverView(@NonNull PBXCallHistory pBXCallHistory, View view, boolean z) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            ((PhonePBXTabFragment) parentFragment).displayCoverView(pBXCallHistory, view, z);
        }
    }

    /* access modifiers changed from: private */
    public void forceUpdateFilter() {
        if (isAdded()) {
            this.mForceUpdate = true;
            List<CmmSIPVoiceMailSharedRelationshipBean> list = this.mFilterDataList;
            if (list != null) {
                list.clear();
            }
            if (isResumed()) {
                updateFilter();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateFilter() {
        if (updateFilterLayout()) {
            updateFilterDialog();
            if (!isHasShow()) {
                return;
            }
            if (this.mForceUpdate || isUserVisible()) {
                this.mForceUpdate = false;
                this.mHandler.removeMessages(100);
                this.mHandler.sendEmptyMessageDelayed(100, 300);
            }
        }
    }

    public boolean updateFilterLayout() {
        boolean z;
        List<CmmSIPVoiceMailSharedRelationshipBean> list = this.mFilterDataList;
        int i = 0;
        if (list == null || list.isEmpty()) {
            this.mFilterDataList = CmmPBXCallHistoryManager.getInstance().getVoicemailSharedRelationships();
            z = true;
        } else {
            z = false;
        }
        boolean isInSelectMode = isInSelectMode();
        View view = this.mLayoutFilter;
        if (isInSelectMode) {
            i = 8;
        }
        view.setVisibility(i);
        updateBtnFilter();
        return z;
    }

    private void updateBtnFilter() {
        String str;
        List<CmmSIPVoiceMailSharedRelationshipBean> list = this.mFilterDataList;
        if (list == null || list.size() <= 1) {
            this.mBtnFilter.setText("");
            this.mBtnFilter.setVisibility(8);
            return;
        }
        this.mBtnFilter.setVisibility(0);
        int size = this.mFilterDataList.size();
        CmmSIPVoiceMailSharedRelationshipBean cmmSIPVoiceMailSharedRelationshipBean = null;
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            CmmSIPVoiceMailSharedRelationshipBean cmmSIPVoiceMailSharedRelationshipBean2 = (CmmSIPVoiceMailSharedRelationshipBean) this.mFilterDataList.get(i2);
            if (cmmSIPVoiceMailSharedRelationshipBean2.isChecked()) {
                i++;
                cmmSIPVoiceMailSharedRelationshipBean = i == 1 ? cmmSIPVoiceMailSharedRelationshipBean2 : null;
            }
        }
        if (i == 0) {
            str = getString(C4558R.string.zm_pbx_voicemail_filter_no_inbox_100064);
        } else if (i == 1) {
            if (cmmSIPVoiceMailSharedRelationshipBean.getExtensionLevel() == -1) {
                str = getResources().getString(C4558R.string.zm_pbx_voicemail_filter_inbox_100064, new Object[]{getString(C4558R.string.zm_pbx_your_inbox_100064)});
            } else {
                str = getResources().getQuantityString(C4558R.plurals.zm_pbx_voicemail_filter_inboxes_100064, i, new Object[]{cmmSIPVoiceMailSharedRelationshipBean.getExtensionName()});
            }
        } else if (i < size) {
            str = getResources().getQuantityString(C4558R.plurals.zm_pbx_voicemail_filter_inboxes_100064, i, new Object[]{String.valueOf(i)});
        } else {
            str = getString(C4558R.string.zm_pbx_voicemail_filter_all_inboxes_100064);
        }
        this.mBtnFilter.setText(str);
    }

    private void updateFilterDialog() {
        BigRoundListDialog bigRoundListDialog = this.mFilterDialog;
        if (bigRoundListDialog != null && bigRoundListDialog.isShowing()) {
            ZMListAdapter adapter = this.mFilterDialog.getAdapter();
            if (adapter != null) {
                List generateFilterList = generateFilterList();
                if (generateFilterList != null) {
                    adapter.setList(generateFilterList);
                } else {
                    adapter.getList().clear();
                }
                adapter.notifyDataSetChanged();
                this.mFilterDialog.invalidate();
            }
        }
    }

    @Nullable
    private List<VoiceMailFilterItem> generateFilterList() {
        List<CmmSIPVoiceMailSharedRelationshipBean> list = this.mFilterDataList;
        if (list == null) {
            return null;
        }
        int size = list.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            VoiceMailFilterItem voiceMailFilterItem = new VoiceMailFilterItem((CmmSIPVoiceMailSharedRelationshipBean) this.mFilterDataList.get(i));
            voiceMailFilterItem.init(getContext());
            arrayList.add(voiceMailFilterItem);
        }
        return arrayList;
    }

    public boolean isHasShow() {
        boolean z = false;
        if (!this.mHasShow) {
            return false;
        }
        boolean isParentHasShow = isParentHasShow();
        if (this.mHasShow && isParentHasShow) {
            z = true;
        }
        return z;
    }

    private boolean isParentHasShow() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            return ((PhonePBXTabFragment) parentFragment).isHasShow();
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

    public void checkDeleteHistoryCall() {
        PhonePBXVoiceMailListView phonePBXVoiceMailListView = this.mListview;
        if (phonePBXVoiceMailListView != null) {
            phonePBXVoiceMailListView.checkDeleteVoiceMails();
        }
    }

    public boolean setSelectAllMode() {
        PhonePBXVoiceMailListView phonePBXVoiceMailListView = this.mListview;
        if (phonePBXVoiceMailListView != null) {
            return phonePBXVoiceMailListView.setSelectAllMode();
        }
        return false;
    }

    public void onDeleteInSelectMode() {
        PhonePBXVoiceMailListView phonePBXVoiceMailListView = this.mListview;
        String dialogTitle = phonePBXVoiceMailListView != null ? phonePBXVoiceMailListView.dialogTitle() : "";
        if (!TextUtils.isEmpty(dialogTitle)) {
            DialogUtils.showAlertDialog((ZMActivity) getActivity(), dialogTitle, getString(C4558R.string.zm_sip_delete_warn_61381), C4558R.string.zm_btn_delete, C4558R.string.zm_btn_cancel, true, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PhonePBXVoiceMailFragment.this.checkDeleteHistoryCall();
                    Fragment parentFragment = PhonePBXVoiceMailFragment.this.getParentFragment();
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
        PhonePBXVoiceMailListView phonePBXVoiceMailListView = this.mListview;
        if (phonePBXVoiceMailListView != null) {
            phonePBXVoiceMailListView.setSelectMode(true);
        }
        updateFilterLayout();
    }

    public void onExitSelectMode() {
        PhonePBXVoiceMailListView phonePBXVoiceMailListView = this.mListview;
        if (phonePBXVoiceMailListView != null) {
            phonePBXVoiceMailListView.clearSelectList();
            this.mListview.setSelectMode(false);
        }
        updateUI();
    }

    public void onListViewDatasetChanged(boolean z) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && (parentFragment instanceof PhonePBXTabFragment)) {
            ((PhonePBXTabFragment) parentFragment).onListViewDatasetChanged(z);
        }
    }

    public void accessibilityControl(long j) {
        if (!TextUtils.isEmpty(this.mAccessibilitySelectId) && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            PhonePBXVoiceMailListView phonePBXVoiceMailListView = this.mListview;
            if (phonePBXVoiceMailListView == null) {
                this.mAccessibilitySelectId = null;
                return;
            }
            PhonePBXVoiceMailHistoryAdapter dataAdapter = phonePBXVoiceMailListView.getDataAdapter();
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
                        if (PhonePBXVoiceMailFragment.this.isResumed() && PhonePBXVoiceMailFragment.this.isUserVisible()) {
                            PhonePBXVoiceMailFragment.this.mListview.requestFocus();
                            AccessibilityUtil.sendAccessibilityFocusEvent(childAt);
                        }
                    }
                }, j);
            }
        }
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

    public void onShow() {
        this.mHasShow = true;
        this.mHandler.post(new Runnable() {
            public void run() {
                PhonePBXVoiceMailFragment.this.updateUIOnVisible();
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
            this.mFilterDataList = CmmPBXCallHistoryManager.getInstance().getVoicemailSharedRelationships();
            List<CmmSIPVoiceMailSharedRelationshipBean> list = this.mFilterDataList;
            if (list != null && list.size() > 1) {
                BigRoundListDialog bigRoundListDialog = this.mFilterDialog;
                if (bigRoundListDialog == null || !bigRoundListDialog.isShowing()) {
                    this.mFilterDialog = new BigRoundListDialog(activity);
                    this.mFilterDialog.setCloseText(getString(C4558R.string.zm_pbx_voicemail_filter_results_button_100064));
                    this.mFilterDialog.setDismissOnItemClicked(false);
                    this.mFilterDialog.setTitle(C4558R.string.zm_pbx_voicemail_filter_title_100064);
                    PBXFilterAdapter pBXFilterAdapter = new PBXFilterAdapter(getContext());
                    pBXFilterAdapter.setList(generateFilterList());
                    this.mFilterDialog.setAdapter(pBXFilterAdapter);
                    this.mFilterDialog.setDialogCallback(new DialogCallback() {
                        public void onItemSelected(int i) {
                            IZMListItem item = PhonePBXVoiceMailFragment.this.mFilterDialog.getAdapter().getItem(i);
                            if (item != null && (item instanceof VoiceMailFilterItem)) {
                                ((VoiceMailFilterItem) item).setSelected(!item.isSelected());
                                if (PhonePBXVoiceMailFragment.this.mFilterDialog.getAdapter() != null) {
                                    PhonePBXVoiceMailFragment.this.mFilterDialog.getAdapter().notifyDataSetChanged();
                                }
                            }
                        }

                        public void onCancel() {
                            PhonePBXVoiceMailFragment.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    AccessibilityUtil.sendAccessibilityFocusEvent(PhonePBXVoiceMailFragment.this.mLayoutFilter);
                                }
                            }, 1000);
                        }

                        public void onClose() {
                            if (PhonePBXVoiceMailFragment.this.mFilterDialog.getAdapter() != null) {
                                List list = PhonePBXVoiceMailFragment.this.mFilterDialog.getAdapter().getList();
                                if (list != null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        Object obj = list.get(i);
                                        if (obj instanceof VoiceMailFilterItem) {
                                            VoiceMailFilterItem voiceMailFilterItem = (VoiceMailFilterItem) obj;
                                            CmmPBXCallHistoryManager.getInstance().checkVoicemailSharedRelationship(voiceMailFilterItem.getId(), voiceMailFilterItem.isSelected());
                                            ((CmmSIPVoiceMailSharedRelationshipBean) PhonePBXVoiceMailFragment.this.mFilterDataList.get(i)).setChecked(voiceMailFilterItem.isSelected());
                                        }
                                    }
                                }
                            }
                            PhonePBXVoiceMailFragment.this.forceUpdateFilter();
                            PhonePBXVoiceMailFragment.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    AccessibilityUtil.sendAccessibilityFocusEvent(PhonePBXVoiceMailFragment.this.mBtnFilter);
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
        CmmPBXCallHistoryManager.getInstance().removeISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
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

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z && isAdded()) {
            this.mHasShow = true;
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                PhonePBXVoiceMailFragment.this.updateUIOnVisible();
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
