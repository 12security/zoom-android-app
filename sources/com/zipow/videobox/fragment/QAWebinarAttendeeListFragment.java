package com.zipow.videobox.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI.IAttentionTrackEventSinkUIListener;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI.SimpleAttentionTrackEventSinkUIListener;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public class QAWebinarAttendeeListFragment extends ConfChatAttendeeListFragment implements ExtListener, OnClickListener, OnEditorActionListener {
    private static final int DELAY_REFRESH_TIME = 600;
    public static final int MENU_ITEM_EXPEL = 1;
    public static final int MENU_ITEM_LOWERHAND = 2;
    public static final int MENU_ITEM_PROMOTE_TO_PANELIST = 0;
    private static final String TAG = "QAWebinarAttendeeListFragment";
    private static final int THRESHOD_SHOW_SIDEBAR = 500;
    /* access modifiers changed from: private */
    public QuickSearchListView mAttendeeListView;
    private IAttentionTrackEventSinkUIListener mAttentionTrackEventSinkUIListener;
    private View mBtnCancel;
    private View mBtnClearSearchView;
    private View mBtnLowerHandAll;
    /* access modifiers changed from: private */
    @Nullable
    public WebinarAttendeeListAdapter mBuddyAdapter;
    @Nullable
    private IConfUIListener mConfUIListener;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    private EditText mEdtSearchDummy;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    private View mPanelSearchBar;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    private IZoomQAUIListener mQAUIListener;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = QAWebinarAttendeeListFragment.this.mEdtSearch.getText().toString();
            QAWebinarAttendeeListFragment.this.mBuddyAdapter.setFilter(obj);
            if ((obj.length() <= 0 || QAWebinarAttendeeListFragment.this.mBuddyAdapter.getCount() <= 0) && QAWebinarAttendeeListFragment.this.mPanelTitleBar.getVisibility() != 0) {
                QAWebinarAttendeeListFragment.this.mListContainer.setForeground(QAWebinarAttendeeListFragment.this.mDimmedForground);
            } else {
                QAWebinarAttendeeListFragment.this.mListContainer.setForeground(null);
            }
            if (StringUtil.isEmptyOrNull(obj.trim())) {
                QAWebinarAttendeeListFragment.this.refresh();
            }
            QAWebinarAttendeeListFragment.this.checkSideBar();
            QAWebinarAttendeeListFragment.this.mBuddyAdapter.notifyDataSetChanged();
        }
    };
    @NonNull
    private Runnable mRunnableUpdateData = new Runnable() {
        public void run() {
            QAWebinarAttendeeListFragment.this.refresh();
        }
    };
    private TextView mTxtTitle;

    public static class WebinarAttendeeListAdapter extends QuickSearchListDataAdapter {
        @NonNull
        private HashMap<String, String> mCacheSortKeys = new HashMap<>();
        private Context mContext;
        @Nullable
        private String mFilter;
        @NonNull
        private List<ConfChatAttendeeItem> mList = new ArrayList();
        @NonNull
        private List<ConfChatAttendeeItem> mListFiltered = new ArrayList();
        @Nullable
        private ConfChatAttendeeItem mTelephonyUserCountItem = null;

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean isDataSorted() {
            return true;
        }

        public WebinarAttendeeListAdapter(Context context) {
            this.mContext = context;
        }

        public void setFilter(@Nullable String str) {
            if (str != null) {
                str = str.trim();
            }
            this.mFilter = str;
            updateFilteredList();
        }

        private void updateFilteredList() {
            this.mListFiltered.clear();
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                String lowerCase = this.mFilter.toLowerCase(CompatUtils.getLocalDefault());
                ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                if (qAComponent != null) {
                    List buddyListByNameFilter = qAComponent.getBuddyListByNameFilter(lowerCase);
                    if (buddyListByNameFilter != null) {
                        int size = buddyListByNameFilter.size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                ZoomQABuddy zoomQABuddy = (ZoomQABuddy) buddyListByNameFilter.get(i);
                                if (zoomQABuddy != null && zoomQABuddy.getRole() == 0) {
                                    this.mListFiltered.add(new ConfChatAttendeeItem(zoomQABuddy));
                                }
                            }
                        }
                    }
                }
            }
        }

        public int getBuddyCount() {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent == null) {
                return 0;
            }
            return qAComponent.getBuddyCount();
        }

        private void loadAll() {
            ConfChatAttendeeItem confChatAttendeeItem;
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null) {
                List buddyListByNameFilter = qAComponent.getBuddyListByNameFilter(this.mFilter);
                if (buddyListByNameFilter != null) {
                    int size = buddyListByNameFilter.size();
                    if (size > 0) {
                        int i = 0;
                        if (size <= 500) {
                            while (i < size) {
                                ZoomQABuddy zoomQABuddy = (ZoomQABuddy) buddyListByNameFilter.get(i);
                                if (zoomQABuddy != null && zoomQABuddy.getRole() == 0) {
                                    String name = zoomQABuddy.getName();
                                    String str = name != null ? (String) this.mCacheSortKeys.get(name) : null;
                                    if (str == null) {
                                        ConfChatAttendeeItem confChatAttendeeItem2 = new ConfChatAttendeeItem(zoomQABuddy);
                                        this.mCacheSortKeys.put(name, confChatAttendeeItem2.getSortKey());
                                        confChatAttendeeItem = confChatAttendeeItem2;
                                    } else {
                                        confChatAttendeeItem = new ConfChatAttendeeItem(zoomQABuddy, str);
                                    }
                                    this.mList.add(confChatAttendeeItem);
                                }
                                i++;
                            }
                        } else {
                            while (i < size) {
                                ZoomQABuddy zoomQABuddy2 = (ZoomQABuddy) buddyListByNameFilter.get(i);
                                if (zoomQABuddy2 != null && zoomQABuddy2.getRole() == 0) {
                                    this.mList.add(new ConfChatAttendeeItem(zoomQABuddy2, null));
                                }
                                i++;
                            }
                        }
                        refreshTelephonyUserCountItem();
                    }
                }
            }
        }

        public void reloadAll() {
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                updateFilteredList();
                return;
            }
            this.mList.clear();
            loadAll();
        }

        public void refreshTelephonyUserCountItem() {
            int viewOnlyTelephonyUserCount = ConfMgr.getInstance().getViewOnlyTelephonyUserCount();
            if (viewOnlyTelephonyUserCount > 0) {
                ConfChatAttendeeItem confChatAttendeeItem = new ConfChatAttendeeItem(this.mContext.getResources().getQuantityString(C4558R.plurals.zm_lbl_webinar_telephony_user_count, viewOnlyTelephonyUserCount, new Object[]{Integer.valueOf(viewOnlyTelephonyUserCount)}), "", 0, 0);
                confChatAttendeeItem.setSortKey("*");
                confChatAttendeeItem.isPlaceholder = true;
                clearTelephonyUserCountInfo();
                this.mTelephonyUserCountItem = confChatAttendeeItem;
                this.mList.add(0, confChatAttendeeItem);
                return;
            }
            clearTelephonyUserCountInfo();
        }

        private void clearTelephonyUserCountInfo() {
            ConfChatAttendeeItem confChatAttendeeItem = this.mTelephonyUserCountItem;
            if (confChatAttendeeItem != null) {
                this.mList.remove(confChatAttendeeItem);
                this.mTelephonyUserCountItem = null;
            }
        }

        public int getCount() {
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                return this.mListFiltered.size();
            }
            return this.mList.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                return this.mListFiltered.get(i);
            }
            return this.mList.get(i);
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (!(item instanceof ConfChatAttendeeItem)) {
                return null;
            }
            return ((ConfChatAttendeeItem) item).getView(this.mContext, view);
        }

        public String getItemSortKey(Object obj) {
            return ((ConfChatAttendeeItem) obj).getSortKey();
        }
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity, int i) {
        if (zMActivity != null) {
            SimpleActivity.show(zMActivity, QAWebinarAttendeeListFragment.class.getName(), new Bundle(), i, true);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_qa_webinar_attendee, viewGroup, false);
        this.mBtnCancel = inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mBtnLowerHandAll = inflate.findViewById(C4558R.C4560id.btnLowerHandAll);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mEdtSearchDummy = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mAttendeeListView = (QuickSearchListView) inflate.findViewById(C4558R.C4560id.attendeesListView);
        this.mBtnClearSearchView = inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnLowerHandAll.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        FragmentActivity activity = getActivity();
        enableAttendeeItemPopMenu(this.mAttendeeListView.getListView());
        this.mBuddyAdapter = new WebinarAttendeeListAdapter(activity);
        this.mAttendeeListView.setCategoryChars("*#ABCDEFGHIJKLMNOPQRSTUVWXYZ", "*#ABCDEFGHIJKLMNOPQRSTUVWXYZ", "*#AB.IJK.RST.Z", "*#A.IJ.RS.Z", "*#A.I.R.Z");
        this.mAttendeeListView.setCategoryTitle('*', null);
        this.mAttendeeListView.setAdapter(this.mBuddyAdapter);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                QAWebinarAttendeeListFragment.this.mHandler.removeCallbacks(QAWebinarAttendeeListFragment.this.mRunnableFilter);
                QAWebinarAttendeeListFragment.this.mHandler.postDelayed(QAWebinarAttendeeListFragment.this.mRunnableFilter, 300);
                QAWebinarAttendeeListFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, final long j) {
                    EventTaskManager eventTaskManager = QAWebinarAttendeeListFragment.this.getEventTaskManager();
                    if (eventTaskManager != null && i == 108) {
                        eventTaskManager.pushLater("onTelephonyUserCountChanged", new EventAction("onTelephonyUserCountChanged") {
                            public void run(IUIElement iUIElement) {
                                ((QAWebinarAttendeeListFragment) iUIElement).onTelephonyUserCountChanged((int) j);
                            }
                        });
                    }
                    return true;
                }

                public boolean onUserStatusChanged(int i, long j, int i2) {
                    if (i != 1) {
                        if (i != 9 && i != 21) {
                            switch (i) {
                                case 28:
                                case 29:
                                    QAWebinarAttendeeListFragment.this.talkPrivilegeChange(j);
                                    break;
                                default:
                                    switch (i) {
                                        case 44:
                                        case 45:
                                            break;
                                        case 46:
                                            QAWebinarAttendeeListFragment.this.userGuestStatusChanged(j);
                                            break;
                                    }
                            }
                        } else {
                            QAWebinarAttendeeListFragment.this.updateUserAudioStatus(j);
                            return true;
                        }
                    }
                    QAWebinarAttendeeListFragment.this.processOnHostOrCoHostChanged(j);
                    return true;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onUserListUpdated() {
                    QAWebinarAttendeeListFragment.this.postRefresh();
                }

                public void onUserListInitialized() {
                    QAWebinarAttendeeListFragment.this.refresh();
                }

                public void onUserRemoved(@NonNull String str) {
                    QAWebinarAttendeeListFragment.this.onUserRemoved(str);
                }

                public void onWebinarAttendeeRaisedHand(long j) {
                    QAWebinarAttendeeListFragment.this.attendeeRaiseOrLowerHand(j);
                }

                public void onWebinarAttendeeLowerHand(long j) {
                    QAWebinarAttendeeListFragment.this.attendeeRaiseOrLowerHand(j);
                }

                public void onChattedAttendeeUpdated(long j) {
                    QAWebinarAttendeeListFragment.this.postRefresh();
                }
            };
        }
        if (this.mAttentionTrackEventSinkUIListener == null) {
            this.mAttentionTrackEventSinkUIListener = new SimpleAttentionTrackEventSinkUIListener() {
                public void OnConfAttentionTrackStatusChanged(boolean z) {
                }

                public void OnWebinarAttendeeAttentionStatusChanged(int i, boolean z) {
                    QAWebinarAttendeeListFragment.this.postRefresh();
                }
            };
        }
        AttentionTrackEventSinkUI.getInstance().addListener(this.mAttentionTrackEventSinkUIListener);
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        if (this.mBuddyAdapter.getBuddyCount() >= 600) {
            showWaitingDialog();
            this.mHandler.postDelayed(this.mRunnableUpdateData, 500);
        } else {
            updateData();
        }
        updateTitle();
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateBtnClearSearchView();
        this.mAttendeeListView.onResume();
        this.mBuddyAdapter.notifyDataSetChanged();
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRunnableFilter);
        this.mHandler.removeCallbacks(this.mRunnableUpdateData);
        super.onDestroyView();
    }

    /* access modifiers changed from: private */
    public void postRefresh() {
        this.mHandler.removeCallbacks(this.mRunnableUpdateData);
        this.mHandler.postDelayed(this.mRunnableUpdateData, 600);
    }

    /* access modifiers changed from: private */
    public void onTelephonyUserCountChanged(int i) {
        this.mBuddyAdapter.refreshTelephonyUserCountItem();
        checkSideBar();
        this.mBuddyAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void refresh() {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.pushLater("refreshAll", new EventAction("refreshAll") {
                public void run(IUIElement iUIElement) {
                    ((QAWebinarAttendeeListFragment) iUIElement).updateData();
                }
            });
        } else {
            updateData();
        }
    }

    /* access modifiers changed from: private */
    public void updateData() {
        this.mBuddyAdapter.reloadAll();
        dismissWaitingDialog();
        if (this.mBuddyAdapter.getCount() > 500) {
            if (this.mAttendeeListView.isQuickSearchEnabled()) {
                this.mAttendeeListView.setQuickSearchEnabled(false);
            }
        } else if (!this.mAttendeeListView.isQuickSearchEnabled()) {
            this.mAttendeeListView.setQuickSearchEnabled(true);
        }
        this.mBuddyAdapter.notifyDataSetChanged();
        updateTitle();
    }

    /* access modifiers changed from: private */
    public void onUserRemoved(@NonNull String str) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.dismissPAttendeeListActionDialogForUserId(zMActivity.getSupportFragmentManager(), str);
        }
    }

    /* access modifiers changed from: private */
    public void updateUserAudioStatus(long j) {
        postRefresh();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.refreshAction(zMActivity.getSupportFragmentManager(), j);
        }
    }

    /* access modifiers changed from: private */
    public void talkPrivilegeChange(long j) {
        postRefresh();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.refreshAction(zMActivity.getSupportFragmentManager(), j);
        }
    }

    /* access modifiers changed from: private */
    public void processOnHostOrCoHostChanged(long j) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.refreshAction(zMActivity.getSupportFragmentManager(), j);
        }
    }

    /* access modifiers changed from: private */
    public void userGuestStatusChanged(long j) {
        postRefresh();
    }

    /* access modifiers changed from: private */
    public void attendeeRaiseOrLowerHand(long j) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.refreshAction(zMActivity.getSupportFragmentManager(), j);
        }
    }

    /* access modifiers changed from: private */
    public void checkSideBar() {
        if (this.mBuddyAdapter.getCount() >= 500) {
            if (this.mAttendeeListView.isQuickSearchEnabled()) {
                this.mAttendeeListView.setQuickSearchEnabled(false);
            }
        } else if (!this.mAttendeeListView.isQuickSearchEnabled()) {
            refresh();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        AttentionTrackEventSinkUI.getInstance().removeListener(this.mAttentionTrackEventSinkUIListener);
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public boolean onSearchRequested() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onKeyboardOpen() {
        if (getView() != null && this.mEdtSearchDummy.hasFocus()) {
            this.mEdtSearchDummy.setVisibility(8);
            this.mPanelTitleBar.setVisibility(8);
            this.mPanelSearchBar.setVisibility(0);
            this.mListContainer.setForeground(this.mDimmedForground);
            this.mEdtSearch.requestFocus();
        }
    }

    public void onKeyboardClosed() {
        EditText editText = this.mEdtSearch;
        if (editText != null) {
            if (StringUtil.isEmptyOrNull(editText.getText().toString()) || this.mBuddyAdapter.getBuddyCount() == 0) {
                this.mEdtSearch.setText(null);
                this.mEdtSearchDummy.setVisibility(0);
                this.mPanelSearchBar.setVisibility(4);
                this.mListContainer.setForeground(null);
                this.mPanelTitleBar.setVisibility(0);
                this.mAttendeeListView.post(new Runnable() {
                    public void run() {
                        QAWebinarAttendeeListFragment.this.mAttendeeListView.requestLayout();
                    }
                });
            }
        }
    }

    public boolean onBackPressed() {
        if (this.mPanelSearchBar.getVisibility() != 0) {
            return false;
        }
        this.mEdtSearch.setText(null);
        this.mEdtSearchDummy.setVisibility(0);
        this.mPanelSearchBar.setVisibility(4);
        this.mListContainer.setForeground(null);
        this.mPanelTitleBar.setVisibility(0);
        this.mAttendeeListView.post(new Runnable() {
            public void run() {
                QAWebinarAttendeeListFragment.this.mAttendeeListView.requestLayout();
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public void dismiss() {
        if (((ZMActivity) getActivity()) != null) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        }
        finishFragment(true);
    }

    private void updateTitle() {
        if (isAdded()) {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            this.mTxtTitle.setText(getString(C4558R.string.zm_title_webinar_attendee, Integer.valueOf(qAComponent != null ? qAComponent.getBuddyCount() : 0)));
        }
    }

    public void onClick(View view) {
        if (view == this.mBtnCancel) {
            onClickBtnCancel();
        } else if (view == this.mBtnClearSearchView) {
            onClickBtnClearSearchView();
        } else if (view == this.mBtnLowerHandAll) {
            onClickBtnLowerHandAll();
        }
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void onClickBtnLowerHandAll() {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null) {
            raiseHandAPIObj.lowerAllHand();
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        this.mBuddyAdapter.setFilter(null);
    }

    /* access modifiers changed from: protected */
    public void onRemoveItem(String str) {
        postRefresh();
    }

    @Nullable
    public ConfChatAttendeeItem getItemAtPosition(int i) {
        Object itemAtPosition = this.mAttendeeListView.getItemAtPosition(i);
        if (itemAtPosition instanceof ConfChatAttendeeItem) {
            return (ConfChatAttendeeItem) itemAtPosition;
        }
        return null;
    }
}
