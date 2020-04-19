package com.zipow.videobox.fragment;

import android.content.Intent;
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
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.AutoStreamConflictChecker;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.p014mm.MMSelectSessionAndBuddyListView;
import com.zipow.videobox.view.p014mm.MMSelectSessionAndBuddyListView.OnInformationBarriesListener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class MMSelectSessionAndBuddyFragment extends ZMFragment implements OnClickListener, OnEditorActionListener, IABContactsCacheListener, ExtListener {
    public static final String ARG_CONTAINS_BLOCK = "containBlock";
    public static final String ARG_CONTAINS_E2E_GROUP = "containE2E";
    public static final String ARG_CONTAINS_MYNOTES = "containMyNotes";
    public static final String ARG_RESULT_DATA = "resultData";
    public static final String RESULT_ARG_SELECTED_ITEM = "selectedItem";
    public static final String RESULT_ARG_SELECTED_ITEM_IS_GROUP = "isgroup";
    private final String TAG = MMSelectSessionAndBuddyFragment.class.getSimpleName();
    private boolean isKeyboardOpen = false;
    private Button mBtnCancel;
    private ImageButton mBtnClearSearchView;
    private Button mBtnClose;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    private EditText mEdtSearchDummy;
    private View mEmptyPanel;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    private IPTUIListener mNetworkStateReceiver;
    private View mPanelSearchBar;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = MMSelectSessionAndBuddyFragment.this.mEdtSearch.getText().toString();
            MMSelectSessionAndBuddyFragment.this.mSessionsListView.filter(obj);
            if ((obj.length() <= 0 || MMSelectSessionAndBuddyFragment.this.mSessionsListView.getCount() <= 0) && MMSelectSessionAndBuddyFragment.this.mPanelTitleBar.getVisibility() != 0) {
                MMSelectSessionAndBuddyFragment.this.mListContainer.setForeground(MMSelectSessionAndBuddyFragment.this.mDimmedForground);
            } else {
                MMSelectSessionAndBuddyFragment.this.mListContainer.setForeground(null);
            }
        }
    };
    /* access modifiers changed from: private */
    public MMSelectSessionAndBuddyListView mSessionsListView;
    /* access modifiers changed from: private */
    public TextView mTxtEmptyView;
    /* access modifiers changed from: private */
    public TextView mTxtIBTips;
    private TextView mTxtTitle;
    @Nullable
    private WaitingDialog mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onBeginConnect() {
            MMSelectSessionAndBuddyFragment.this.onBeginConnect();
        }

        public void onConnectReturn(int i) {
            MMSelectSessionAndBuddyFragment.this.onConnectReturn(i);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMSelectSessionAndBuddyFragment.this.onIndicateBuddyInfoUpdatedWithJID(str);
        }

        public void onIndicateBuddyListUpdated() {
            MMSelectSessionAndBuddyFragment.this.onIndicateBuddyListUpdated();
        }

        public void Indicate_BuddyPresenceChanged(String str) {
            MMSelectSessionAndBuddyFragment.this.onIndicateBuddyInfoUpdatedWithJID(str);
        }

        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMSelectSessionAndBuddyFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_ChatSessionListUpdate() {
            MMSelectSessionAndBuddyFragment.this.onNotify_ChatSessionListUpdate();
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMSelectSessionAndBuddyFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void onSearchBuddyByKey(String str, int i) {
            MMSelectSessionAndBuddyFragment.this.onSearchBuddyByKey(str, i);
        }
    };

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsFragment(Fragment fragment, Bundle bundle, boolean z, boolean z2, int i) {
        showAsFragment(fragment, bundle, z, z2, true, i);
    }

    public static void showAsFragment(Fragment fragment, @Nullable Bundle bundle, boolean z, boolean z2, boolean z3, int i) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            bundle2.putBundle("resultData", bundle);
        }
        bundle2.putBoolean(ARG_CONTAINS_E2E_GROUP, z);
        bundle2.putBoolean(ARG_CONTAINS_BLOCK, z2);
        bundle2.putBoolean(ARG_CONTAINS_MYNOTES, z3);
        SimpleActivity.show(fragment, MMSelectSessionAndBuddyFragment.class.getName(), bundle2, i, false, 1);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_share_session_buddy_list, viewGroup, false);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mSessionsListView = (MMSelectSessionAndBuddyListView) inflate.findViewById(C4558R.C4560id.sessionsListView);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mEdtSearchDummy = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mBtnClearSearchView = (ImageButton) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mBtnClose = (Button) inflate.findViewById(C4558R.C4560id.btnClose);
        this.mBtnCancel = (Button) inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mEmptyPanel = inflate.findViewById(C4558R.C4560id.emptyLinear);
        this.mTxtIBTips = (TextView) inflate.findViewById(C4558R.C4560id.txtIBTipsCenter);
        this.mTxtEmptyView = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mSessionsListView.setParentFragment(this);
        this.mSessionsListView.setEmptyView(this.mEmptyPanel);
        this.mBtnClose.setOnClickListener(this);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mSessionsListView.setOnInformationBarriesListener(new OnInformationBarriesListener() {
            public void onInfoBarries(boolean z) {
                if (z) {
                    MMSelectSessionAndBuddyFragment.this.mTxtEmptyView.setVisibility(8);
                    MMSelectSessionAndBuddyFragment.this.mTxtIBTips.setVisibility(0);
                    return;
                }
                MMSelectSessionAndBuddyFragment.this.mTxtEmptyView.setVisibility(0);
                MMSelectSessionAndBuddyFragment.this.mTxtIBTips.setVisibility(8);
            }
        });
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@Nullable Editable editable) {
                MMSelectSessionAndBuddyFragment.this.mHandler.removeCallbacks(MMSelectSessionAndBuddyFragment.this.mRunnableFilter);
                MMSelectSessionAndBuddyFragment.this.mHandler.postDelayed(MMSelectSessionAndBuddyFragment.this.mRunnableFilter, (editable == null || editable.length() == 0) ? 0 : 300);
                MMSelectSessionAndBuddyFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        onKeyboardClosed();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        if (!PTApp.getInstance().hasZoomMessenger()) {
            this.mEdtSearchDummy.setVisibility(8);
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSessionsListView.setContainsE2E(arguments.getBoolean(ARG_CONTAINS_E2E_GROUP));
            this.mSessionsListView.setContainsBlock(arguments.getBoolean(ARG_CONTAINS_BLOCK));
            this.mSessionsListView.setmContainMyNotes(arguments.getBoolean(ARG_CONTAINS_MYNOTES));
        }
        compatPCModeForSearch();
        return inflate;
    }

    private void compatPCModeForSearch() {
        this.mEdtSearchDummy.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(final View view, boolean z) {
                if (z) {
                    MMSelectSessionAndBuddyFragment.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (MMSelectSessionAndBuddyFragment.this.isAdded() && MMSelectSessionAndBuddyFragment.this.isResumed() && ((EditText) view).hasFocus()) {
                                MMSelectSessionAndBuddyFragment.this.onKeyboardOpen();
                            }
                        }
                    }, 500);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onKeyboardOpen() {
        if (getView() != null && !this.isKeyboardOpen) {
            this.isKeyboardOpen = true;
            if (this.mEdtSearchDummy.hasFocus()) {
                this.mEdtSearchDummy.setVisibility(8);
                this.mPanelTitleBar.setVisibility(8);
                this.mPanelSearchBar.setVisibility(0);
                this.mListContainer.setForeground(this.mDimmedForground);
                this.mEdtSearch.requestFocus();
            }
        }
    }

    public void onKeyboardClosed() {
        EditText editText = this.mEdtSearch;
        if (editText != null) {
            this.isKeyboardOpen = false;
            if (editText.getText().length() == 0 || this.mSessionsListView.getCount() == 0) {
                this.mEdtSearchDummy.setVisibility(0);
                this.mPanelSearchBar.setVisibility(4);
                this.mListContainer.setForeground(null);
                this.mPanelTitleBar.setVisibility(0);
                this.mEdtSearch.setText("");
            }
            this.mSessionsListView.post(new Runnable() {
                public void run() {
                    MMSelectSessionAndBuddyFragment.this.mSessionsListView.requestLayout();
                }
            });
        }
    }

    public boolean onBackPressed() {
        if (this.mPanelSearchBar.getVisibility() != 0) {
            return false;
        }
        this.mEdtSearchDummy.setVisibility(0);
        this.mPanelSearchBar.setVisibility(4);
        this.mListContainer.setForeground(null);
        this.mPanelTitleBar.setVisibility(0);
        this.mEdtSearch.setText("");
        this.isKeyboardOpen = false;
        return true;
    }

    public void onResume() {
        super.onResume();
        MMSelectSessionAndBuddyListView mMSelectSessionAndBuddyListView = this.mSessionsListView;
        if (mMSelectSessionAndBuddyListView != null) {
            mMSelectSessionAndBuddyListView.onParentFragmentResume();
        }
        updateTitle();
        updateBtnClearSearchView();
        ABContactsCache.getInstance().addListener(this);
        if (ABContactsCache.getInstance().needReloadAll()) {
            ABContactsCache.getInstance().reloadAllContacts();
        }
    }

    public void onStart() {
        super.onStart();
        MMSelectSessionAndBuddyListView mMSelectSessionAndBuddyListView = this.mSessionsListView;
        if (mMSelectSessionAndBuddyListView != null) {
            mMSelectSessionAndBuddyListView.onParentFragmentStart();
        }
    }

    private void updateTitle() {
        switch (ZoomMessengerUI.getInstance().getConnectionStatus()) {
            case -1:
            case 0:
            case 1:
                TextView textView = this.mTxtTitle;
                if (textView != null) {
                    textView.setText(C4558R.string.zm_mm_title_share_to);
                    break;
                }
                break;
            case 2:
                TextView textView2 = this.mTxtTitle;
                if (textView2 != null) {
                    textView2.setText(C4558R.string.zm_mm_title_chats_connecting);
                    break;
                }
                break;
        }
        TextView textView3 = this.mTxtTitle;
        if (textView3 != null) {
            textView3.getParent().requestLayout();
        }
    }

    public void onContactsCacheUpdated() {
        MMSelectSessionAndBuddyListView mMSelectSessionAndBuddyListView = this.mSessionsListView;
        if (mMSelectSessionAndBuddyListView != null) {
            mMSelectSessionAndBuddyListView.loadData();
            this.mSessionsListView.notifyDataSetChanged();
        }
    }

    public void onPause() {
        super.onPause();
        ABContactsCache.getInstance().removeListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void dismiss() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public boolean onSearchRequested() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onSelect(String str, boolean z) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_ARG_SELECTED_ITEM, str);
        intent.putExtra(RESULT_ARG_SELECTED_ITEM_IS_GROUP, z);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Bundle bundle = arguments.getBundle("resultData");
            if (bundle != null) {
                intent.putExtras(bundle);
            }
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setResult(-1, intent);
        }
        dismiss();
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null && this.mWaitingDialog == null) {
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
                WaitingDialog waitingDialog = this.mWaitingDialog;
                if (waitingDialog != null) {
                    try {
                        waitingDialog.dismissAllowingStateLoss();
                    } catch (Exception unused) {
                    }
                }
            }
            this.mWaitingDialog = null;
        }
    }

    public void onClickViewMore() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.searchBuddyByKey(this.mEdtSearch.getText().toString().trim().toLowerCase(CompatUtils.getLocalDefault()))) {
            this.mSessionsListView.setIsWebSearchMode(true);
            showWaitingDialog();
        }
    }

    public void onClick(View view) {
        if (view == this.mBtnClearSearchView) {
            onClickBtnClearSearchView();
        } else if (view == this.mBtnClose) {
            onClickBtnClose();
        } else if (view == this.mBtnCancel) {
            onClickBtnCancle();
        }
    }

    private void onClickBtnClose() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        dismiss();
    }

    private void onClickBtnCancle() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
    }

    private void onClickPanelConnectionAlert() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (!NetworkUtil.hasDataNetwork(activity)) {
                Toast.makeText(activity, C4558R.string.zm_alert_network_disconnected, 1).show();
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

    /* access modifiers changed from: private */
    public void onBeginConnect() {
        if (NetworkUtil.hasDataNetwork(getActivity()) && isResumed()) {
            updateTitle();
        }
    }

    /* access modifiers changed from: private */
    public void onConnectReturn(int i) {
        if (PTApp.getInstance().getZoomMessenger() != null && isResumed()) {
            updateTitle();
            MMSelectSessionAndBuddyListView mMSelectSessionAndBuddyListView = this.mSessionsListView;
            if (mMSelectSessionAndBuddyListView != null) {
                mMSelectSessionAndBuddyListView.notifyDataSetChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyInfoUpdatedWithJID(String str) {
        MMSelectSessionAndBuddyListView mMSelectSessionAndBuddyListView = this.mSessionsListView;
        if (mMSelectSessionAndBuddyListView != null) {
            mMSelectSessionAndBuddyListView.onIndicateBuddyInfoUpdatedWithJID(str);
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(int i, @Nullable GroupAction groupAction, String str) {
        if (groupAction != null) {
            this.mSessionsListView.onGroupAction(i, groupAction, str);
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_ChatSessionListUpdate() {
        if (this.mSessionsListView != null && isResumed()) {
            this.mSessionsListView.loadData();
            this.mSessionsListView.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyListUpdated() {
        if (this.mSessionsListView != null && isResumed()) {
            this.mSessionsListView.loadData();
            this.mSessionsListView.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        MMSelectSessionAndBuddyListView mMSelectSessionAndBuddyListView = this.mSessionsListView;
        if (mMSelectSessionAndBuddyListView != null) {
            mMSelectSessionAndBuddyListView.onNotify_MUCGroupInfoUpdatedImpl(str);
        }
    }

    /* access modifiers changed from: private */
    public void onSearchBuddyByKey(String str, int i) {
        if (StringUtil.isSameString(this.mEdtSearch.getText().toString().trim().toLowerCase(CompatUtils.getLocalDefault()), str)) {
            dismissWaitingDialog();
        }
        MMSelectSessionAndBuddyListView mMSelectSessionAndBuddyListView = this.mSessionsListView;
        if (mMSelectSessionAndBuddyListView != null) {
            mMSelectSessionAndBuddyListView.onSearchBuddyByKey(str, i);
        }
    }
}
