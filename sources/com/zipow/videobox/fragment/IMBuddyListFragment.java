package com.zipow.videobox.fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.BuddyInviteActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMBuddyListView;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.thirdparty.login.facebook.AuthToken;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;
import p021us.zoom.videomeetings.C4558R;

public class IMBuddyListFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnEditorActionListener, IPTUIListener, IIMListener {
    public static final String ARG_SHOW_BACK_BUTTON = "showBackButton";
    private final String TAG = IMBuddyListFragment.class.getSimpleName();
    private AvatarView mAvatar;
    private View mBtnBack;
    private Button mBtnClearSearchView;
    private Button mBtnInviteBuddy;
    private Button mBtnReconnect;
    /* access modifiers changed from: private */
    public IMBuddyListView mBuddyListView;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    /* access modifiers changed from: private */
    public View mPanelConnecting;
    /* access modifiers changed from: private */
    public View mPanelNoItemMsg;
    /* access modifiers changed from: private */
    public View mPanelReconnect;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    @NonNull
    private Runnable mRunnableCheckToShowNoBuddyMessage = new Runnable() {
        public void run() {
            if (!StringUtil.isEmptyOrNull(IMBuddyListFragment.this.mBuddyListView.getFilter()) || IMBuddyListFragment.this.mBuddyListView.getCount() != 0) {
                IMBuddyListFragment.this.mPanelNoItemMsg.setVisibility(8);
                IMBuddyListFragment.this.mPanelReconnect.setVisibility(8);
                return;
            }
            IMBuddyListFragment.this.mPanelNoItemMsg.setVisibility(0);
            IMBuddyListFragment.this.mPanelConnecting.setVisibility(8);
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = IMBuddyListFragment.this.mEdtSearch.getText().toString();
            IMBuddyListFragment.this.mBuddyListView.filter(obj);
            if ((obj.length() <= 0 || IMBuddyListFragment.this.mBuddyListView.getCount() <= 0) && IMBuddyListFragment.this.mPanelTitleBar.getVisibility() != 0) {
                IMBuddyListFragment.this.mListContainer.setForeground(IMBuddyListFragment.this.mDimmedForground);
            } else {
                IMBuddyListFragment.this.mListContainer.setForeground(null);
            }
            IMBuddyListFragment.this.checkToShowNoBuddyMessage();
        }
    };
    private TextView mTxtInvitationsCount;
    private TextView mTxtLocalStatus;
    private TextView mTxtScreenName;

    private void showMyInfo() {
    }

    private void updateBuddyInvitationsMessage() {
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public void updateLocalStatus() {
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackButton", true);
        SimpleActivity.show(zMActivity, IMBuddyListFragment.class.getName(), bundle, 0);
    }

    public static void showAsActivity(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackButton", true);
        SimpleActivity.show(fragment, IMBuddyListFragment.class.getName(), bundle, 0);
    }

    /* access modifiers changed from: private */
    public void checkToShowNoBuddyMessage() {
        this.mHandler.removeCallbacks(this.mRunnableCheckToShowNoBuddyMessage);
        this.mHandler.postDelayed(this.mRunnableCheckToShowNoBuddyMessage, 1000);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_imview_buddylist, viewGroup, false);
        this.mBuddyListView = (IMBuddyListView) inflate.findViewById(C4558R.C4560id.buddyListView);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mPanelConnecting = inflate.findViewById(C4558R.C4560id.panelConnecting);
        this.mPanelReconnect = inflate.findViewById(C4558R.C4560id.panelReconnect);
        this.mBtnReconnect = (Button) inflate.findViewById(C4558R.C4560id.btnReconnect);
        this.mTxtLocalStatus = (TextView) inflate.findViewById(C4558R.C4560id.txtLocalStatus);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtNoBuddiesMsg);
        if (PTApp.getInstance().getPTLoginType() == 2) {
            textView.setText(C4558R.string.zm_msg_no_buddies_google);
        } else if (PTApp.getInstance().getPTLoginType() == 0) {
            textView.setText(C4558R.string.zm_msg_no_buddies_fb);
        }
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.toolbar);
        this.mTxtScreenName = (TextView) this.mPanelTitleBar.findViewById(C4558R.C4560id.txtScreenName);
        TextView textView2 = (TextView) this.mPanelTitleBar.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnBack = this.mPanelTitleBar.findViewById(C4558R.C4560id.btnBack);
        if (UIMgr.isLargeMode(getActivity())) {
            this.mTxtScreenName.setVisibility(0);
        } else {
            this.mTxtScreenName.setVisibility(8);
            textView2.setVisibility(0);
            int pTLoginType = PTApp.getInstance().getPTLoginType();
            if (pTLoginType == 0) {
                textView2.setText(C4558R.string.zm_tab_buddylist_facebook);
            } else if (pTLoginType == 2) {
                textView2.setText(C4558R.string.zm_tab_buddylist_google);
            }
        }
        this.mBtnInviteBuddy = (Button) this.mPanelTitleBar.findViewById(C4558R.C4560id.btnInviteBuddy);
        this.mAvatar = (AvatarView) this.mPanelTitleBar.findViewById(C4558R.C4560id.avatarView);
        this.mTxtInvitationsCount = (TextView) this.mPanelTitleBar.findViewById(C4558R.C4560id.txtInvitationsCount);
        this.mBtnInviteBuddy.setVisibility(isInviteBuddySupported() ? 0 : 8);
        this.mPanelNoItemMsg.setVisibility(8);
        this.mBtnReconnect.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mAvatar.setOnClickListener(this);
        this.mBtnInviteBuddy.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                IMBuddyListFragment.this.mHandler.removeCallbacks(IMBuddyListFragment.this.mRunnableFilter);
                IMBuddyListFragment.this.mHandler.postDelayed(IMBuddyListFragment.this.mRunnableFilter, 300);
                IMBuddyListFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (((zMActivity instanceof IMActivity) && !((IMActivity) zMActivity).isKeyboardOpen()) || ((zMActivity instanceof SimpleActivity) && !((SimpleActivity) zMActivity).isKeyboardOpen())) {
            onKeyboardClosed();
        }
        Bundle arguments = getArguments();
        if (arguments != null ? arguments.getBoolean("showBackButton", false) : false) {
            this.mBtnBack.setVisibility(0);
            this.mAvatar.setVisibility(8);
        } else {
            this.mBtnBack.setVisibility(8);
            this.mAvatar.setVisibility(8);
        }
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        return inflate;
    }

    private boolean isInviteBuddySupported() {
        return PTApp.getInstance().getPTLoginType() == 2;
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnReconnect) {
            reconnect();
        } else if (id == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
        } else if (id == C4558R.C4560id.btnInviteBuddy) {
            onClickBtnInviteBuddy();
        } else if (id == C4558R.C4560id.avatarView) {
            onClickAvatarView(view.getId());
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    private void onClickBtnBack() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    private void onClickBtnInviteBuddy() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            if (UIMgr.isLargeMode(zMActivity)) {
                BuddyInviteFragment.showDialog(getActivity().getSupportFragmentManager(), null);
            } else {
                BuddyInviteActivity.show((ZMActivity) getActivity(), zMActivity instanceof IMActivity ? 102 : 0, null);
            }
        }
    }

    private void onClickAvatarView(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null && UIMgr.isLargeMode(activity)) {
            SettingFragment.show(activity.getSupportFragmentManager(), i);
        }
    }

    public boolean isFocusedOnSearchField() {
        if (getView() == null) {
            return false;
        }
        return this.mEdtSearch.hasFocus();
    }

    public void onKeyboardOpen() {
        if (getView() != null && this.mEdtSearch.hasFocus()) {
            this.mEdtSearch.setCursorVisible(true);
            this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_focused);
            this.mPanelTitleBar.setVisibility(8);
            this.mListContainer.setForeground(this.mDimmedForground);
        }
    }

    public void onKeyboardClosed() {
        EditText editText = this.mEdtSearch;
        if (editText != null) {
            editText.setCursorVisible(false);
            this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_normal);
            this.mPanelTitleBar.setVisibility(0);
            this.mListContainer.setForeground(null);
        }
    }

    public void updateLocalStatus(int i) {
        if (getView() != null) {
            switch (i) {
                case 0:
                case 5:
                    this.mPanelNoItemMsg.setVisibility(8);
                    this.mPanelReconnect.setVisibility(0);
                    this.mPanelConnecting.setVisibility(8);
                    this.mBtnReconnect.setVisibility(0);
                    break;
                case 1:
                    this.mTxtLocalStatus.setText(C4558R.string.zm_login_step_connecting);
                    this.mPanelNoItemMsg.setVisibility(8);
                    this.mPanelReconnect.setVisibility(0);
                    this.mPanelConnecting.setVisibility(0);
                    this.mBtnReconnect.setVisibility(8);
                    break;
                case 2:
                    this.mTxtLocalStatus.setText(C4558R.string.zm_login_step_negotiating);
                    this.mPanelNoItemMsg.setVisibility(8);
                    this.mPanelReconnect.setVisibility(0);
                    this.mPanelConnecting.setVisibility(0);
                    this.mBtnReconnect.setVisibility(8);
                    break;
                case 3:
                    this.mTxtLocalStatus.setText(C4558R.string.zm_login_step_authenticating);
                    this.mPanelNoItemMsg.setVisibility(8);
                    this.mPanelReconnect.setVisibility(0);
                    this.mPanelConnecting.setVisibility(0);
                    this.mBtnReconnect.setVisibility(8);
                    break;
                case 4:
                    this.mPanelReconnect.setVisibility(8);
                    checkToShowNoBuddyMessage();
                    break;
            }
        }
    }

    public void onResume() {
        super.onResume();
        refreshAll();
        PTUI.getInstance().addPTUIListener(this);
        PTUI.getInstance().addIMListener(this);
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
        PTUI.getInstance().removeIMListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(this.mRunnableFilter);
        this.mHandler.removeCallbacks(this.mRunnableCheckToShowNoBuddyMessage);
    }

    public void onIMReceived(@NonNull IMMessage iMMessage) {
        if (isResumed() && getView() != null) {
            this.mBuddyListView.updateBuddyItem(iMMessage.getFromScreenName());
        }
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
        if (isResumed() && getView() != null) {
            this.mBuddyListView.updateBuddyItem(buddyItem);
            this.mRunnableCheckToShowNoBuddyMessage.run();
            this.mPanelReconnect.setVisibility(8);
        }
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
        if (isResumed() && getView() != null) {
            this.mBuddyListView.updateBuddyItem(buddyItem);
        }
    }

    public void onIMBuddySort() {
        if (isResumed() && getView() != null) {
            this.mBuddyListView.reloadAllBuddyItems();
            this.mRunnableCheckToShowNoBuddyMessage.run();
        }
    }

    public void reloadAllBuddyItems() {
        if (getView() != null) {
            this.mBuddyListView.reloadAllBuddyItems();
            this.mRunnableCheckToShowNoBuddyMessage.run();
        }
    }

    public void onSubscriptionRequest() {
        if (isResumed()) {
            updateBuddyInvitationsMessage();
        }
    }

    public void onSubscriptionUpdate() {
        if (isResumed()) {
            updateBuddyInvitationsMessage();
        }
    }

    public void onCallPlistChanged() {
        IMBuddyListView iMBuddyListView = this.mBuddyListView;
        if (iMBuddyListView != null) {
            iMBuddyListView.refreshContextMenu();
        }
    }

    public void onCallStatusChanged(long j) {
        IMBuddyListView iMBuddyListView = this.mBuddyListView;
        if (iMBuddyListView != null) {
            iMBuddyListView.refreshContextMenu();
        }
    }

    public void onWebLogin(long j) {
        IMBuddyListView iMBuddyListView = this.mBuddyListView;
        if (iMBuddyListView != null) {
            iMBuddyListView.refreshContextMenu();
        }
    }

    public boolean onSearchRequested() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onMyInfoReady() {
        showMyInfo();
    }

    public void onMyPictureReady() {
        showMyInfo();
    }

    private void reconnect() {
        AuthToken session = FBSessionStore.getSession(getContext(), FBSessionStore.FACEBOOK_KEY);
        if (PTApp.getInstance().getPTLoginType() == 0 && (session.shouldExtendAccessToken() || !session.isSessionValid())) {
            showLoginUI(true);
        } else if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            PTUI.getInstance().reconnectIM();
        }
    }

    private void showLoginUI(boolean z) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            LoginActivity.show(zMActivity, z);
            zMActivity.finish();
        }
    }

    private void refreshAll() {
        if (getView() != null) {
            showMyInfo();
            updateLocalStatus();
            this.mBuddyListView.setFilter(this.mEdtSearch.getText().toString());
            reloadAllBuddyItems();
            updateBuddyInvitationsMessage();
            this.mBuddyListView.refreshContextMenu();
            updateBtnClearSearchView();
        }
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            onWebLogin(j);
        } else if (i == 9) {
            onMyInfoReady();
        } else if (i == 12) {
            onMyPictureReady();
        } else if (i != 37) {
            switch (i) {
                case 22:
                    onCallStatusChanged(j);
                    return;
                case 23:
                    onCallPlistChanged();
                    return;
                default:
                    return;
            }
        } else {
            updateLocalStatus(5);
        }
    }

    public void onIMLocalStatusChanged(int i) {
        if (isResumed()) {
            updateLocalStatus(i);
        }
    }
}
