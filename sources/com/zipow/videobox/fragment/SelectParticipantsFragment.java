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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.view.SelectParticipantItem;
import com.zipow.videobox.view.SelectParticipantItem.SelectParticipantItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public class SelectParticipantsFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnEditorActionListener, OnItemClickListener {
    private static final int DELAY_REFRESH_TIME = 600;
    public static final String SELECT_TYPE = "select_type";
    public static final String SOURCE_USER_ID = "source_user_id";
    public static final String SOURCE_USER_IS_MYSELF = "source_user_is_myself";
    private static final String TAG = "SelectParticipantsFragment";
    private static final int THRESHOD_SHOW_SIDEBAR = 500;
    private static final int THRESHOLD_SHOW_SEARCH = 8;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_VIDEO = 1;
    /* access modifiers changed from: private */
    @Nullable
    public SelectParticipantsAdapter mAdapter;
    private View mBtnClearSearchView;
    private View mBtnClose;
    @NonNull
    private IConfUIListener mConfUIListener = new SimpleConfUIListener() {
        public boolean onUserStatusChanged(int i, long j, int i2) {
            if (i != 1) {
                switch (i) {
                    case 44:
                    case 45:
                        break;
                    default:
                        return false;
                }
            }
            SelectParticipantsFragment.this.postRefresh();
            return true;
        }

        public boolean onUserEvent(int i, long j, int i2) {
            switch (i) {
                case 0:
                case 1:
                    SelectParticipantsFragment.this.postRefresh();
                    return true;
                default:
                    return false;
            }
        }
    };
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
    /* access modifiers changed from: private */
    public QuickSearchListView mParticipantsListView;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = SelectParticipantsFragment.this.mEdtSearch.getText().toString();
            SelectParticipantsFragment.this.mAdapter.setFilter(obj);
            if ((obj.length() <= 0 || SelectParticipantsFragment.this.mAdapter.getCount() <= 0) && SelectParticipantsFragment.this.mPanelTitleBar.getVisibility() != 0) {
                SelectParticipantsFragment.this.mListContainer.setForeground(SelectParticipantsFragment.this.mDimmedForground);
            } else {
                SelectParticipantsFragment.this.mListContainer.setForeground(null);
            }
        }
    };
    @NonNull
    private Runnable mRunnableUpdateData = new Runnable() {
        public void run() {
            SelectParticipantsFragment.this.refresh();
        }
    };
    private int mSelectType;
    private long mSourceUserId;
    private boolean mSourceUserIsMyself;
    @NonNull
    private TextWatcher mTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            SelectParticipantsFragment.this.mHandler.removeCallbacks(SelectParticipantsFragment.this.mRunnableFilter);
            SelectParticipantsFragment.this.mHandler.postDelayed(SelectParticipantsFragment.this.mRunnableFilter, 300);
            SelectParticipantsFragment.this.updateBtnClearSearchView();
        }
    };
    private TextView mTxtTitle;

    public static class SelectParticipantsAdapter extends QuickSearchListDataAdapter {
        @NonNull
        private HashMap<String, String> mCacheSortKeys = new HashMap<>();
        private Context mContext;
        @NonNull
        private List<SelectParticipantItem> mDataList = new ArrayList();
        @NonNull
        private String mFilter = "";
        private int mSelectType;

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean isDataSorted() {
            return true;
        }

        public SelectParticipantsAdapter(Context context, int i) {
            this.mContext = context;
            this.mSelectType = i;
        }

        public void setFilter(@Nullable String str) {
            String lowerCase = StringUtil.safeString(str).trim().toLowerCase();
            if (!this.mFilter.equals(lowerCase)) {
                this.mFilter = lowerCase;
                reloadAll();
            }
        }

        public void reloadAll() {
            this.mDataList.clear();
            List oriUserList = getOriUserList();
            boolean z = oriUserList.size() <= 500;
            boolean isEmptyOrNull = true ^ StringUtil.isEmptyOrNull(this.mFilter);
            for (int i = 0; i < oriUserList.size(); i++) {
                CmmUser cmmUser = (CmmUser) oriUserList.get(i);
                String screenName = cmmUser.getScreenName();
                if (!isEmptyOrNull || StringUtil.safeString(screenName).toLowerCase(CompatUtils.getLocalDefault()).contains(this.mFilter)) {
                    SelectParticipantItem selectParticipantItem = new SelectParticipantItem(cmmUser);
                    if (z) {
                        if (this.mCacheSortKeys.get(screenName) == null) {
                            String sortKey = SortUtil.getSortKey(screenName, CompatUtils.getLocalDefault());
                            selectParticipantItem.setSortKey(sortKey);
                            this.mCacheSortKeys.put(screenName, sortKey);
                        } else {
                            selectParticipantItem.setSortKey((String) this.mCacheSortKeys.get(screenName));
                        }
                    }
                    this.mDataList.add(selectParticipantItem);
                }
            }
            if (!isEmptyOrNull) {
                Collections.sort(this.mDataList, new SelectParticipantItemComparator(CompatUtils.getLocalDefault()));
            }
            notifyDataSetChanged();
        }

        private List<CmmUser> getOriUserList() {
            CmmUserList userList = ConfMgr.getInstance().getUserList();
            if (userList == null) {
                return new ArrayList();
            }
            int i = this.mSelectType;
            if (i == 2) {
                return userList.getPureCallInUsers();
            }
            if (i == 1) {
                return userList.getNoAudioClientUsers();
            }
            return new ArrayList();
        }

        public int getCount() {
            return this.mDataList.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            return this.mDataList.get(i);
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (item instanceof SelectParticipantItem) {
                return ((SelectParticipantItem) item).getView(this.mContext, view, this.mSelectType);
            }
            return null;
        }

        public String getItemSortKey(Object obj) {
            return ((SelectParticipantItem) obj).getSortKey();
        }
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity, int i, long j) {
        if (zMActivity != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(SELECT_TYPE, i);
            bundle.putLong(SOURCE_USER_ID, j);
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isMyself(j)) {
                bundle.putBoolean(SOURCE_USER_IS_MYSELF, true);
            }
            SimpleActivity.show(zMActivity, SelectParticipantsFragment.class.getName(), bundle, 0, false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_participants, viewGroup, false);
        this.mBtnClose = inflate.findViewById(C4558R.C4560id.btnClose);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mEdtSearchDummy = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mParticipantsListView = (QuickSearchListView) inflate.findViewById(C4558R.C4560id.attendeesListView);
        this.mBtnClearSearchView = inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSelectType = arguments.getInt(SELECT_TYPE, 0);
            this.mSourceUserId = arguments.getLong(SOURCE_USER_ID, 0);
            this.mSourceUserIsMyself = arguments.getBoolean(SOURCE_USER_IS_MYSELF, false);
        }
        int i = this.mSelectType;
        if (i == 2) {
            this.mTxtTitle.setText(C4558R.string.zm_mi_merge_audio_title_116180);
        } else if (i == 1) {
            this.mTxtTitle.setText(C4558R.string.zm_mi_merge_video_title_116180);
        }
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        this.mAdapter = new SelectParticipantsAdapter(getActivity(), this.mSelectType);
        this.mParticipantsListView.setCategoryTitle('*', null);
        this.mParticipantsListView.setAdapter(this.mAdapter);
        updateData();
        this.mEdtSearch.addTextChangedListener(this.mTextWatcher);
        this.mEdtSearch.setOnEditorActionListener(this);
        this.mParticipantsListView.getListView().setOnItemClickListener(this);
        ConfUI.getInstance().addListener(this.mConfUIListener);
        this.mBtnClose.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateBtnClearSearchView();
        this.mParticipantsListView.onResume();
        SelectParticipantsAdapter selectParticipantsAdapter = this.mAdapter;
        if (selectParticipantsAdapter != null) {
            selectParticipantsAdapter.notifyDataSetChanged();
        }
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

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.mAdapter != null) {
            Object itemAtPosition = this.mParticipantsListView.getItemAtPosition(i);
            if (itemAtPosition instanceof SelectParticipantItem) {
                int i2 = this.mSelectType;
                if (i2 == 2) {
                    ConfMgr.getInstance().bindTelephoneUser(this.mSourceUserId, ((SelectParticipantItem) itemAtPosition).getUserId());
                } else if (i2 == 1) {
                    ConfMgr.getInstance().bindTelephoneUser(((SelectParticipantItem) itemAtPosition).getUserId(), this.mSourceUserId);
                }
                dismiss();
            }
        }
    }

    /* access modifiers changed from: private */
    public void refresh() {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.pushLater("refreshAll", new EventAction("refreshAll") {
                public void run(IUIElement iUIElement) {
                    SelectParticipantsFragment.this.updateData();
                }
            });
        } else {
            updateData();
        }
    }

    /* access modifiers changed from: private */
    public void updateData() {
        SelectParticipantsAdapter selectParticipantsAdapter = this.mAdapter;
        if (selectParticipantsAdapter != null) {
            selectParticipantsAdapter.reloadAll();
            if (this.mAdapter.getCount() > 500) {
                if (this.mParticipantsListView.isQuickSearchEnabled()) {
                    this.mParticipantsListView.setQuickSearchEnabled(false);
                }
            } else if (!this.mParticipantsListView.isQuickSearchEnabled()) {
                this.mParticipantsListView.setQuickSearchEnabled(true);
            }
            checkShowSearchView();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        this.mHandler.removeCallbacksAndMessages(null);
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
            if (StringUtil.isEmptyOrNull(editText.getText().toString()) || this.mAdapter.getCount() == 0) {
                this.mEdtSearch.setText(null);
                checkShowSearchView();
                this.mPanelSearchBar.setVisibility(4);
                this.mListContainer.setForeground(null);
                this.mPanelTitleBar.setVisibility(0);
                this.mParticipantsListView.post(new Runnable() {
                    public void run() {
                        SelectParticipantsFragment.this.mParticipantsListView.requestLayout();
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
        checkShowSearchView();
        this.mPanelSearchBar.setVisibility(4);
        this.mListContainer.setForeground(null);
        this.mPanelTitleBar.setVisibility(0);
        this.mParticipantsListView.post(new Runnable() {
            public void run() {
                SelectParticipantsFragment.this.mParticipantsListView.requestLayout();
            }
        });
        return true;
    }

    private void checkShowSearchView() {
        SelectParticipantsAdapter selectParticipantsAdapter = this.mAdapter;
        if (selectParticipantsAdapter != null) {
            if (selectParticipantsAdapter.getCount() <= 8) {
                this.mEdtSearchDummy.setVisibility(8);
            } else {
                this.mEdtSearchDummy.setVisibility(0);
            }
        }
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

    public void onClick(View view) {
        if (view == this.mBtnClose) {
            onClickBtnClose();
        } else if (view == this.mBtnClearSearchView) {
            onClickBtnClearSearchView();
        }
    }

    private void onClickBtnClose() {
        dismiss();
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
    }
}
