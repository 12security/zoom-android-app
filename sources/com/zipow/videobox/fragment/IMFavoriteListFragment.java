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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.AddFavoriteActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IFavoriteListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.ZoomContact;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.FavoriteListView;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMFavoriteListFragment extends ZMDialogFragment implements ExtListener, OnClickListener, IFavoriteListener, OnEditorActionListener, IPTUIListener {
    public static final String ARG_SHOW_BACK_BUTTON = "showBackButton";
    private AvatarView mAvatar;
    private View mBtnBack;
    private Button mBtnClearSearchView;
    private Button mBtnInviteBuddy;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    /* access modifiers changed from: private */
    public FavoriteListView mFavoriteListView;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private View mPanelNoItemMsg;
    /* access modifiers changed from: private */
    public ViewGroup mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = IMFavoriteListFragment.this.mEdtSearch.getText().toString();
            IMFavoriteListFragment.this.mFavoriteListView.filter(obj);
            if ((obj.length() <= 0 || IMFavoriteListFragment.this.mFavoriteListView.getDataItemCount() <= 0) && IMFavoriteListFragment.this.mPanelTitleBar.getVisibility() != 0) {
                IMFavoriteListFragment.this.mFavoriteListView.setForeground(IMFavoriteListFragment.this.mDimmedForground);
            } else {
                IMFavoriteListFragment.this.mFavoriteListView.setForeground(null);
            }
            IMFavoriteListFragment.this.checkToShowNoBuddyMessage();
        }
    };
    private TextView mTxtScreenName;

    private void showMyInfo() {
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onFinishSearchDomainUser(String str, int i, int i2, List<ZoomContact> list) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackButton", true);
        SimpleActivity.show(zMActivity, IMFavoriteListFragment.class.getName(), bundle, 0);
    }

    public static void showAsActivity(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackButton", true);
        SimpleActivity.show(fragment, IMFavoriteListFragment.class.getName(), bundle, 0);
    }

    /* access modifiers changed from: private */
    public void checkToShowNoBuddyMessage() {
        if (!PTApp.getInstance().isWebSignedOn() || !StringUtil.isEmptyOrNull(this.mFavoriteListView.getFilter()) || this.mFavoriteListView.getDataItemCount() != 0) {
            this.mPanelNoItemMsg.setVisibility(8);
        } else {
            this.mPanelNoItemMsg.setVisibility(0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_imview_favoritelist, viewGroup, false);
        this.mFavoriteListView = (FavoriteListView) inflate.findViewById(C4558R.C4560id.favoriteListView);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = (ViewGroup) inflate.findViewById(C4558R.C4560id.toolbar);
        this.mTxtScreenName = (TextView) this.mPanelTitleBar.findViewById(C4558R.C4560id.txtScreenName);
        TextView textView = (TextView) this.mPanelTitleBar.findViewById(C4558R.C4560id.txtTitle);
        TextView textView2 = (TextView) this.mPanelTitleBar.findViewById(C4558R.C4560id.txtInvitationsCount);
        if (UIMgr.isLargeMode(getActivity())) {
            this.mTxtScreenName.setVisibility(0);
        } else {
            this.mTxtScreenName.setVisibility(8);
            textView.setVisibility(0);
            textView.setText(C4558R.string.zm_tab_favorite_contacts);
        }
        textView2.setVisibility(8);
        this.mBtnInviteBuddy = (Button) this.mPanelTitleBar.findViewById(C4558R.C4560id.btnInviteBuddy);
        this.mAvatar = (AvatarView) this.mPanelTitleBar.findViewById(C4558R.C4560id.avatarView);
        this.mBtnBack = this.mPanelTitleBar.findViewById(C4558R.C4560id.btnBack);
        this.mBtnInviteBuddy.setText(C4558R.string.zm_btn_invite_buddy_favorite);
        this.mBtnInviteBuddy.setVisibility(0);
        this.mPanelNoItemMsg.setVisibility(8);
        this.mBtnClearSearchView.setOnClickListener(this);
        if (UIMgr.isLargeMode(getActivity())) {
            this.mAvatar.setOnClickListener(this);
        }
        this.mBtnInviteBuddy.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                IMFavoriteListFragment.this.mHandler.removeCallbacks(IMFavoriteListFragment.this.mRunnableFilter);
                IMFavoriteListFragment.this.mHandler.postDelayed(IMFavoriteListFragment.this.mRunnableFilter, 300);
                IMFavoriteListFragment.this.updateBtnClearSearchView();
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

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnClearSearchView) {
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

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    private void onClickBtnInviteBuddy() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            AddFavoriteActivity.show(zMActivity, zMActivity instanceof IMActivity ? 102 : 0);
        }
    }

    private void onClickAvatarView(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null && UIMgr.isLargeMode(activity)) {
            SettingFragment.show(activity.getSupportFragmentManager(), i);
        }
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
            this.mFavoriteListView.setForeground(this.mDimmedForground);
        }
    }

    public void onKeyboardClosed() {
        EditText editText = this.mEdtSearch;
        if (editText != null) {
            editText.setCursorVisible(false);
            this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_normal);
            this.mPanelTitleBar.setVisibility(0);
            this.mFavoriteListView.setForeground(null);
        }
    }

    public void onResume() {
        super.onResume();
        PTUI.getInstance().addFavoriteListener(this);
        PTUI.getInstance().addPTUIListener(this);
        refreshAll();
        FavoriteListView favoriteListView = this.mFavoriteListView;
        if (favoriteListView != null) {
            favoriteListView.onResume();
        }
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removeFavoriteListener(this);
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(this.mRunnableFilter);
    }

    public void onIMReceived(@NonNull IMMessage iMMessage) {
        if (getView() != null) {
            this.mFavoriteListView.updateZoomContact(iMMessage.getFromScreenName());
        }
    }

    public void reloadFavoriteItems() {
        if (getView() != null) {
            this.mFavoriteListView.reloadFavoriteItems();
            checkToShowNoBuddyMessage();
        }
    }

    public void onCallPlistChanged() {
        FavoriteListView favoriteListView = this.mFavoriteListView;
        if (favoriteListView != null) {
            favoriteListView.refreshContextMenu();
        }
    }

    public void onCallStatusChanged(long j) {
        FavoriteListView favoriteListView = this.mFavoriteListView;
        if (favoriteListView != null) {
            favoriteListView.refreshContextMenu();
        }
    }

    public void onWebLogin(long j) {
        FavoriteListView favoriteListView = this.mFavoriteListView;
        if (favoriteListView != null) {
            favoriteListView.refreshContextMenu();
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

    private void refreshAll() {
        if (getView() != null) {
            showMyInfo();
            this.mFavoriteListView.setFilter(this.mEdtSearch.getText().toString());
            reloadFavoriteItems();
            this.mFavoriteListView.refreshContextMenu();
            updateBtnClearSearchView();
        }
    }

    public void onFavoriteEvent(int i, long j) {
        if (i == 2) {
            reloadFavoriteItems();
        }
    }

    public void onFavAvatarReady(String str) {
        this.mFavoriteListView.updateZoomContact(str);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            onWebLogin(j);
        } else if (i == 9) {
            onMyInfoReady();
        } else if (i != 12) {
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
            onMyPictureReady();
        }
    }
}
