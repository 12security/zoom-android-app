package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.AddFavoriteActivity;
import com.zipow.videobox.ptapp.FavoriteMgr;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IFavoriteListener;
import com.zipow.videobox.ptapp.ZoomContact;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.AddFavoriteItem;
import com.zipow.videobox.view.AddFavoriteListView;
import com.zipow.videobox.view.AddFavoriteListView.Listener;
import com.zipow.videobox.view.AvatarView;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMHorizontalListView;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class AddFavoriteFragment extends ZMTipFragment implements OnClickListener, Listener, IFavoriteListener, KeyboardListener, OnEditorActionListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    private static final String TAG = "AddFavoriteFragment";
    private int mAnchorId = 0;
    private Button mBtnClearSearchView;
    private Button mBtnConfigAccount;
    private Button mBtnInvite;
    private Button mBtnSearch;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    private boolean mFailedToGetDomainUser = false;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private boolean mIsGettingDomainUser = false;
    private boolean mIsSearching = false;
    private ZMHorizontalListView mListSelected;
    /* access modifiers changed from: private */
    public AddFavoriteListView mListView;
    private View mPanelConfigAccount;
    private View mPanelFailureMsg;
    private View mPanelLoading;
    private View mPanelSearch;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            AddFavoriteFragment.this.mListView.filter(AddFavoriteFragment.this.mEdtSearch.getText().toString());
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public SelectedListAdapter mSelectedListAdapter;

    public static class InviteFailedDialog extends ZMDialogFragment {
        public InviteFailedDialog() {
            setCancelable(true);
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_invite_failed).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }

        public void onStart() {
            super.onStart();
        }
    }

    public class SelectedListAdapter extends BaseAdapter {
        private Context mContext;
        private List<AddFavoriteItem> mItems;

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public SelectedListAdapter(Context context) {
            this.mContext = context;
        }

        public void update(List<AddFavoriteItem> list) {
            this.mItems = list;
            notifyDataSetChanged();
        }

        public int getCount() {
            List<AddFavoriteItem> list = this.mItems;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Nullable
        public Object getItem(int i) {
            List<AddFavoriteItem> list = this.mItems;
            if (list == null) {
                return null;
            }
            return list.get(i);
        }

        public long getItemId(int i) {
            return (long) ((AddFavoriteItem) getItem(i)).getUserID().hashCode();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            AddFavoriteItem addFavoriteItem = (AddFavoriteItem) getItem(i);
            if (view == null) {
                view = View.inflate(this.mContext, C4558R.layout.zm_invite_selected_listview_item, null);
            }
            AvatarView avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            if (avatarView == null) {
                view = View.inflate(this.mContext, C4558R.layout.zm_invite_selected_listview_item, null);
                avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            }
            if (addFavoriteItem == null) {
                return avatarView;
            }
            avatarView.show(addFavoriteItem.getAvatarParams());
            view.setLayoutParams(new LayoutParams(UIUtil.dip2px(this.mContext, 45.0f), UIUtil.dip2px(this.mContext, 50.0f)));
            return view;
        }
    }

    public static void show(@NonNull FragmentManager fragmentManager, int i, long j, String str) {
        AddFavoriteFragment inviteFragment = getInviteFragment(fragmentManager);
        if (inviteFragment == null) {
            AddFavoriteFragment addFavoriteFragment = new AddFavoriteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            addFavoriteFragment.setArguments(bundle);
            addFavoriteFragment.show(fragmentManager, AddFavoriteFragment.class.getName());
            return;
        }
        inviteFragment.setTipVisible(true);
    }

    @Nullable
    public static AddFavoriteFragment getInviteFragment(FragmentManager fragmentManager) {
        return (AddFavoriteFragment) fragmentManager.findFragmentByTag(AddFavoriteFragment.class.getName());
    }

    public static boolean hide(@NonNull FragmentManager fragmentManager) {
        AddFavoriteFragment inviteFragment = getInviteFragment(fragmentManager);
        if (inviteFragment != null) {
            if (!inviteFragment.getShowsTip()) {
                inviteFragment.dismiss();
                return true;
            } else if (inviteFragment.isTipVisible()) {
                inviteFragment.setTipVisible(false);
                return true;
            }
        }
        return false;
    }

    public static boolean dismiss(@NonNull FragmentManager fragmentManager) {
        AddFavoriteFragment inviteFragment = getInviteFragment(fragmentManager);
        if (inviteFragment == null) {
            return false;
        }
        inviteFragment.dismiss();
        return true;
    }

    public void onDestroy() {
        this.mHandler.removeCallbacks(this.mRunnableFilter);
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        this.mListView.setFilter(this.mEdtSearch.getText().toString());
        updateBtnClearSearchView();
        PTUI.getInstance().addFavoriteListener(this);
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removeFavoriteListener(this);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isTipVisible", isTipVisible());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view;
        if (UIMgr.isLargeMode(getActivity())) {
            view = layoutInflater.inflate(C4558R.layout.zm_add_favorite, null);
        } else {
            view = layoutInflater.inflate(C4558R.layout.zm_add_favorite_main_screen, null);
            ((ZMKeyboardDetector) view.findViewById(C4558R.C4560id.keyboardDetector)).setKeyboardListener(this);
        }
        this.mListView = (AddFavoriteListView) view.findViewById(C4558R.C4560id.buddyListView);
        this.mEdtSearch = (EditText) view.findViewById(C4558R.C4560id.edtSearch);
        this.mListSelected = (ZMHorizontalListView) view.findViewById(C4558R.C4560id.listSelected);
        this.mBtnInvite = (Button) view.findViewById(C4558R.C4560id.btnInvite);
        this.mBtnClearSearchView = (Button) view.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mBtnSearch = (Button) view.findViewById(C4558R.C4560id.btnSearch);
        this.mBtnConfigAccount = (Button) view.findViewById(C4558R.C4560id.btnConfigAccount);
        this.mPanelLoading = view.findViewById(C4558R.C4560id.panelLoading);
        this.mPanelConfigAccount = view.findViewById(C4558R.C4560id.panelConfigAccount);
        this.mPanelSearch = view.findViewById(C4558R.C4560id.panelSearch);
        this.mPanelFailureMsg = view.findViewById(C4558R.C4560id.panelFailureMsg);
        Button button = (Button) view.findViewById(C4558R.C4560id.btnBack);
        this.mSelectedListAdapter = new SelectedListAdapter(getActivity());
        this.mListSelected.setAdapter((ListAdapter) this.mSelectedListAdapter);
        this.mListSelected.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                AddFavoriteFragment.this.mListView.unselectBuddy((AddFavoriteItem) AddFavoriteFragment.this.mSelectedListAdapter.getItem(i));
            }
        });
        this.mBtnInvite.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mBtnSearch.setOnClickListener(this);
        this.mBtnConfigAccount.setOnClickListener(this);
        button.setOnClickListener(this);
        this.mListView.setListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                AddFavoriteFragment.this.mHandler.removeCallbacks(AddFavoriteFragment.this.mRunnableFilter);
                AddFavoriteFragment.this.mHandler.postDelayed(AddFavoriteFragment.this.mRunnableFilter, 300);
                AddFavoriteFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        PTUI.getInstance().addFavoriteListener(this);
        if (NetworkUtil.hasDataNetwork(getActivity())) {
            FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
            if (favoriteMgr != null) {
                favoriteMgr.getAllDomainUser();
                this.mIsGettingDomainUser = true;
            }
        } else {
            this.mListView.reloadAllBuddyItems();
        }
        updateButtonInvite(updateSelectedList());
        updateSearchField();
        updateLoadingPanel();
        updateConfigAccountPanel();
        updateFailureMsgPanel();
        return view;
    }

    private void updateLoadingPanel() {
        int i = 8;
        if (this.mFailedToGetDomainUser) {
            this.mPanelLoading.setVisibility(8);
            return;
        }
        View view = this.mPanelLoading;
        if (this.mIsSearching || this.mIsGettingDomainUser) {
            i = 0;
        }
        view.setVisibility(i);
    }

    private void updateConfigAccountPanel() {
        if (this.mFailedToGetDomainUser) {
            this.mPanelConfigAccount.setVisibility(8);
            this.mPanelSearch.setVisibility(8);
            this.mBtnInvite.setVisibility(4);
            return;
        }
        FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
        if (favoriteMgr != null) {
            if (favoriteMgr.getDomainUserCount() != 1 || this.mIsGettingDomainUser) {
                this.mPanelConfigAccount.setVisibility(8);
                if (!this.mIsGettingDomainUser) {
                    this.mPanelSearch.setVisibility(0);
                    this.mBtnInvite.setVisibility(0);
                } else {
                    this.mPanelSearch.setVisibility(8);
                    this.mBtnInvite.setVisibility(4);
                }
            } else {
                this.mPanelConfigAccount.setVisibility(0);
                this.mPanelSearch.setVisibility(8);
                this.mBtnInvite.setVisibility(4);
            }
        }
    }

    private void updateFailureMsgPanel() {
        this.mPanelFailureMsg.setVisibility(this.mFailedToGetDomainUser ? 0 : 8);
    }

    private void updateSearchField() {
        if (needSearchDomain()) {
            this.mEdtSearch.setHint(C4558R.string.zm_hint_add_favorite_email_address);
            this.mEdtSearch.setImeOptions(3);
            this.mBtnSearch.setVisibility(8);
            return;
        }
        this.mEdtSearch.setHint(C4558R.string.zm_hint_search);
        this.mEdtSearch.setImeOptions(6);
        this.mBtnSearch.setVisibility(8);
    }

    private boolean needSearchDomain() {
        FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
        boolean z = false;
        if (favoriteMgr == null) {
            return false;
        }
        if (favoriteMgr.getDomainUserCount() > 200) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    @Nullable
    public ZMTip onCreateTip(@NonNull Context context, LayoutInflater layoutInflater, @Nullable Bundle bundle) {
        View view = getView();
        if (view == null) {
            return null;
        }
        int dip2px = UIUtil.dip2px(context, 400.0f);
        if (UIUtil.getDisplayWidth(context) < dip2px) {
            dip2px = UIUtil.getDisplayWidth(context);
        }
        view.setLayoutParams(new LinearLayout.LayoutParams(dip2px, -2));
        ZMTip zMTip = new ZMTip(context);
        zMTip.setArrowSize(UIUtil.dip2px(context, 30.0f), UIUtil.dip2px(context, 11.0f));
        int i = 0;
        zMTip.setCornerArcSize(0);
        zMTip.addView(view);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return null;
        }
        this.mAnchorId = arguments.getInt("anchorId", 0);
        if (this.mAnchorId > 0) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return null;
            }
            View findViewById = activity.findViewById(this.mAnchorId);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, 1);
            }
        }
        if (bundle != null) {
            if (!bundle.getBoolean("isTipVisible", true)) {
                i = 4;
            }
            zMTip.setVisibility(i);
        }
        return zMTip;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnInvite) {
            onClickBtnInvite();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
        } else if (id == C4558R.C4560id.btnSearch) {
            onClickBtnSearch();
        } else if (id == C4558R.C4560id.btnConfigAccount) {
            onClickBtnConfigAccount();
        }
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        if (i == 3) {
            onClickBtnSearch();
        } else {
            UIUtil.closeSoftKeyboard(getActivity(), this.mBtnSearch);
        }
        return true;
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        if (getShowsTip()) {
            setTipVisible(false);
        } else {
            dismiss();
        }
    }

    private void onClickBtnInvite() {
        List<AddFavoriteItem> selectedBuddies = this.mListView.getSelectedBuddies();
        if (selectedBuddies == null || selectedBuddies.size() == 0) {
            onClickBtnBack();
            return;
        }
        FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
        if (favoriteMgr != null) {
            ArrayList arrayList = new ArrayList();
            for (AddFavoriteItem zoomContact : selectedBuddies) {
                arrayList.add(zoomContact.getZoomContact());
            }
            if (!favoriteMgr.addFavorite(arrayList)) {
                onSentInvitationFailed();
            } else {
                onSentInvitationDone(selectedBuddies);
            }
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    private void onClickBtnSearch() {
        if (needSearchDomain()) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
            startSearchDomain();
        }
    }

    private void onClickBtnConfigAccount() {
        PTApp.getInstance().navWebWithDefaultBrowser(8, null);
    }

    private void startSearchDomain() {
        String obj = this.mEdtSearch.getText().toString();
        if (!StringUtil.isEmptyOrNull(obj)) {
            FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
            if (favoriteMgr != null && favoriteMgr.searchDomainUser(obj)) {
                this.mBtnSearch.setEnabled(false);
                this.mIsSearching = true;
                updateLoadingPanel();
            }
        }
    }

    private void endSearchDomain() {
        this.mBtnSearch.setEnabled(true);
        this.mIsSearching = false;
        updateLoadingPanel();
    }

    private void onSentInvitationFailed() {
        new InviteFailedDialog().show(getFragmentManager(), InviteFailedDialog.class.getName());
    }

    private void onSentInvitationDone(@NonNull List<AddFavoriteItem> list) {
        if (getShowsTip()) {
            setTipVisible(false);
            return;
        }
        AddFavoriteActivity addFavoriteActivity = (AddFavoriteActivity) getActivity();
        if (addFavoriteActivity != null) {
            addFavoriteActivity.onSentInvitationDone(list.size());
        }
    }

    private void clearSelection() {
        this.mListView.clearSelection();
    }

    public void onSelectionChanged() {
        updateButtonInvite(updateSelectedList());
    }

    private void updateButtonInvite(int i) {
        if (i <= 0) {
            this.mBtnInvite.setText(getResources().getString(C4558R.string.zm_btn_done));
            this.mBtnInvite.setEnabled(false);
            return;
        }
        Button button = this.mBtnInvite;
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(C4558R.string.zm_btn_done));
        sb.append("(");
        sb.append(i);
        sb.append(")");
        button.setText(sb.toString());
        this.mBtnInvite.setEnabled(true);
    }

    private int updateSelectedList() {
        List selectedBuddies = this.mListView.getSelectedBuddies();
        this.mListSelected.setVisibility((selectedBuddies == null || selectedBuddies.size() <= 0) ? 8 : 0);
        this.mSelectedListAdapter.update(selectedBuddies);
        if (selectedBuddies == null) {
            return 0;
        }
        return selectedBuddies.size();
    }

    public void onKeyboardOpen() {
        this.mEdtSearch.setCursorVisible(true);
        this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_focused);
    }

    public void onKeyboardClosed() {
        this.mEdtSearch.setCursorVisible(false);
        this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_normal);
    }

    public void onFavoriteEvent(int i, long j) {
        if (i == 0) {
            this.mListView.reloadAllBuddyItems();
            boolean z = false;
            this.mIsGettingDomainUser = false;
            if (j != 0) {
                z = true;
            }
            this.mFailedToGetDomainUser = z;
        }
        updateLoadingPanel();
        updateConfigAccountPanel();
        updateSearchField();
        updateFailureMsgPanel();
    }

    public void onFavAvatarReady(String str) {
        this.mListView.updateZoomContact(str);
    }

    public void onFinishSearchDomainUser(String str, int i, int i2, @NonNull List<ZoomContact> list) {
        this.mListView.loadZoomContactsFromList(list);
        endSearchDomain();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        if (getShowsTip()) {
            super.dismiss();
            return;
        }
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

    private boolean isTipVisible() {
        ZMTip tip = getTip();
        boolean z = false;
        if (tip == null) {
            return false;
        }
        if (tip.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    private void setTipVisible(boolean z) {
        ZMTip tip = getTip();
        if (tip != null) {
            int i = 0;
            if ((tip.getVisibility() == 0) != z) {
                if (!z) {
                    i = 4;
                }
                tip.setVisibility(i);
                if (z) {
                    clearSelection();
                    tip.startAnimation(AnimationUtils.loadAnimation(getActivity(), C4558R.anim.zm_tip_fadein));
                }
            }
        }
    }
}
