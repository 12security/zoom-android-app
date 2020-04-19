package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.SubscriptionRestrictReason;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddySearchData;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.ZMSearchBar.OnSearchBarListener;
import com.zipow.videobox.view.p014mm.MMAddBuddySearchAdapter;
import com.zipow.videobox.view.p014mm.MMAddBuddySearchAdapter.OnRecyclerViewListener;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MMAddBuddySearchFragment extends ZMDialogFragment implements OnClickListener, OnRecyclerViewListener {
    private static final int BLOCK_TIME_FOR_406 = 900000;
    private static final String EXTRA_RESULT = "result_code";
    private static final String EXTRA_SEARCH_BUDDY = "search_buddy";
    private static final String EXTRA_SEARCH_EMAIL = "result_email";
    private static final int RESULT_EXITED = 2;
    private static final int RESULT_NETERROR = 4;
    private static final int RESULT_NOFINISH = -1;
    private static final int RESULT_NO_MATCH = 3;
    private static final int RESULT_SUCCESS = 1;
    private static long mTimeExceedTimestamp;
    private MMAddBuddySearchAdapter mAdapter;
    private View mBtnBack;
    /* access modifiers changed from: private */
    public Button mBtnSearch;
    /* access modifiers changed from: private */
    @Nullable
    public String mJid;
    private String mKeyword;
    @NonNull
    private IZoomMessengerUIListener mMessengerUI = new SimpleZoomMessengerUIListener() {
        public void onQueryJidByEmail(String str, int i) {
            MMAddBuddySearchFragment.this.onQueryJidByEmail(str, i);
        }

        public void onSearchBuddyByKey(String str, int i) {
            MMAddBuddySearchFragment.this.onSearchBuddyByKey(str, i);
        }

        public void onSearchBuddyPicDownloaded(String str) {
            MMAddBuddySearchFragment.this.onSearchBuddyPicDownloaded(str);
        }

        public void Notify_SubscriptionIsRestrictV2(String str, int i) {
            MMAddBuddySearchFragment.this.Notify_SubscriptionIsRestrict(str, i);
        }

        public void Notify_SubscribeRequestSent(String str, int i) {
            MMAddBuddySearchFragment.this.Notify_SubscribeRequestSent(str, i);
        }
    };
    private RecyclerView mSearchResultListView;
    /* access modifiers changed from: private */
    @Nullable
    public Timer mTimer;
    @Nullable
    private ProgressDialog mWaitDialog;
    /* access modifiers changed from: private */
    public ZMSearchBar mZMSearchBar;

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            SimpleActivity.show(fragment, MMAddBuddySearchFragment.class.getName(), bundle, 0, true, 1);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(getActivity(), true, C4558R.color.zm_im_search_bar_bg);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUI);
    }

    public void onActivityCreated(Bundle bundle) {
        getActivity().getWindow().setSoftInputMode(21);
        super.onActivityCreated(bundle);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_add_buddy_search, viewGroup, false);
        this.mAdapter = new MMAddBuddySearchAdapter(getContext());
        this.mAdapter.setOnRecyclerViewListener(this);
        this.mSearchResultListView = (RecyclerView) inflate.findViewById(C4558R.C4560id.searchRecyclerView);
        this.mSearchResultListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mSearchResultListView.setAdapter(this.mAdapter);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSearch = (Button) inflate.findViewById(C4558R.C4560id.btnSearch);
        this.mZMSearchBar = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mZMSearchBar.setImeOptions(3);
        this.mZMSearchBar.setOnSearchBarListener(new OnSearchBarListener() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMAddBuddySearchFragment.this.mBtnSearch.setVisibility(editable.length() != 0 ? 0 : 8);
            }

            public void onClearSearch() {
                MMAddBuddySearchFragment.this.onClickBtnClearSearchView();
            }

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    MMAddBuddySearchFragment mMAddBuddySearchFragment = MMAddBuddySearchFragment.this;
                    mMAddBuddySearchFragment.doSearch(mMAddBuddySearchFragment.mZMSearchBar.getText().trim());
                }
                return false;
            }
        });
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSearch.setOnClickListener(this);
        checkTimeExceed();
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(EXTRA_SEARCH_EMAIL, this.mZMSearchBar.getText().toString());
        if (!StringUtil.isEmptyOrNull(this.mJid)) {
            bundle.putString(EXTRA_SEARCH_BUDDY, this.mJid);
        }
    }

    public void onViewStateRestored(@Nullable Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle != null) {
            this.mJid = bundle.getString(EXTRA_SEARCH_BUDDY);
            this.mZMSearchBar.setText(bundle.getString(EXTRA_SEARCH_EMAIL));
        }
    }

    private void showProgressDialog() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            closeProgressDialog();
            this.mWaitDialog = UIUtil.showSimpleWaitingDialog((Activity) activity, C4558R.string.zm_msg_waiting);
            this.mWaitDialog.setCancelable(false);
        }
    }

    /* access modifiers changed from: private */
    public void closeProgressDialog() {
        ProgressDialog progressDialog = this.mWaitDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mWaitDialog.dismiss();
        }
    }

    private int getBlockTime4TimeExceed() {
        long mMNow = CmmTime.getMMNow() - mTimeExceedTimestamp;
        if (mMNow < 0 || mMNow >= 900000) {
            return -1;
        }
        int i = ((int) ((900000 - mMNow) / 60000)) + 1;
        if (i > 15) {
            i = 15;
        }
        return i;
    }

    private void checkTimeExceed() {
        long blockTime4TimeExceed = (long) getBlockTime4TimeExceed();
        if (blockTime4TimeExceed > 0) {
            closeProgressDialog();
            this.mZMSearchBar.setEnabled(false);
            int i = (int) blockTime4TimeExceed;
            ShowTimeExceedDialog(getResources().getQuantityString(C4558R.plurals.zm_add_buddy_time_exceed_44781, i, new Object[]{Integer.valueOf(i)}));
        }
    }

    private void ShowTimeExceedDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            new Builder(activity).setTitle((CharSequence) str).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMAddBuddySearchFragment.this.dismiss();
                }
            }).create().show();
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnSearch) {
            onClickBtnSearch();
        } else if (id == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mZMSearchBar.getEditText() != null) {
            this.mZMSearchBar.getEditText().requestFocus();
        }
    }

    public void onDestroy() {
        destroyTimer();
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUI);
        closeProgressDialog();
        super.onDestroy();
    }

    public void dismiss() {
        finishFragment(true);
    }

    private void destroyTimer() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mZMSearchBar.getEditText());
        dismiss();
    }

    private void onClickBtnSearch() {
        doSearch(this.mZMSearchBar.getText().trim());
    }

    /* access modifiers changed from: private */
    public void onClickBtnClearSearchView() {
        this.mKeyword = "";
        this.mAdapter.clearKeyword();
    }

    private void sendEmail(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                String[] strArr = {str};
                AndroidAppUtil.sendEmail(zMActivity, strArr, zoomMessenger.getAddBuddySubject(), zoomMessenger.getAddBuddyEmail(), null);
            }
        }
    }

    private boolean validateInput(String str) {
        return StringUtil.isValidEmailAddress(str);
    }

    private boolean isMyselfEmail(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String email = myself.getEmail();
                if (email != null && StringUtil.isSameString(str, email.toLowerCase(Locale.US))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    /* access modifiers changed from: private */
    public void doSearch(@NonNull String str) {
        if (!TextUtils.isEmpty(str)) {
            String lowerCase = str.toLowerCase(Locale.US);
            if (!validateInput(lowerCase)) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    Toast.makeText(activity, C4558R.string.zm_lbl_invalid_email_112365, 1).show();
                }
            } else if (isMyselfEmail(lowerCase)) {
                FragmentActivity activity2 = getActivity();
                if (activity2 != null) {
                    Toast.makeText(activity2, C4558R.string.zm_mm_lbl_can_not_add_self_48295, 1).show();
                }
                this.mAdapter.clearKeyword();
                this.mAdapter.notifyDataSetChanged();
            } else {
                this.mKeyword = str;
                UIUtil.closeSoftKeyboard(getActivity(), this.mZMSearchBar.getEditText());
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (!zoomMessenger.isConnectionGood() || !NetworkUtil.hasDataNetwork(getActivity())) {
                        showConnectionError();
                        return;
                    }
                    this.mAdapter.clearKeyword();
                    if (zoomMessenger.searchBuddyByKey(lowerCase)) {
                        showProgressDialog();
                    } else {
                        showAddBuddyFinish(3);
                    }
                }
            }
        }
    }

    private void showAddBuddyFinish(int i) {
        showAddBuddyFinish(i, null);
    }

    private void showAddBuddyFinish(int i, IMAddrBookItem iMAddrBookItem) {
        if (isAdded()) {
            closeProgressDialog();
            UIUtil.closeSoftKeyboard(getActivity(), this.mZMSearchBar.getEditText());
            switch (i) {
                case 1:
                case 2:
                    this.mAdapter.setKeyword(this.mKeyword, i);
                    this.mAdapter.add(iMAddrBookItem);
                    break;
                case 3:
                    this.mAdapter.setKeyword(this.mKeyword, i);
                    this.mAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    showConnectionError();
                    break;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void ShowAddBuddySuccessDialog() {
        FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            new Builder(activity).setTitle((CharSequence) getString(C4558R.string.zm_lbl_contact_request_sent, this.mKeyword)).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMAddBuddySearchFragment.this.dismiss();
                }
            }).create().show();
        }
    }

    private void ShowAddBuddyFailedDialog(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            String str = "";
            if (i == 424) {
                str = getString(C4558R.string.zm_mm_lbl_add_contact_restrict_48295);
            } else if (i == 425) {
                str = getString(C4558R.string.zm_mm_lbl_cannot_add_contact_48295);
            } else if (i == 426) {
                str = getString(C4558R.string.zm_mm_information_barries_add_contact_115072);
            }
            new Builder(activity).setTitle((CharSequence) str).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMAddBuddySearchFragment.this.dismiss();
                }
            }).create().show();
        }
    }

    private void inviteByEmail(@Nullable IMAddrBookItem iMAddrBookItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && iMAddrBookItem != null && !TextUtils.isEmpty(iMAddrBookItem.getJid())) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String screenName = iMAddrBookItem.getScreenName();
                String accountEmail = iMAddrBookItem.getAccountEmail();
                showProgressDialog();
                if (zoomMessenger.addBuddyByJID(iMAddrBookItem.getJid(), myself.getScreenName(), null, screenName, accountEmail)) {
                    this.mJid = iMAddrBookItem.getJid();
                    destroyTimer();
                    this.mTimer = new Timer();
                    this.mTimer.schedule(new TimerTask() {
                        public void run() {
                            FragmentActivity activity = MMAddBuddySearchFragment.this.getActivity();
                            if (activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        MMAddBuddySearchFragment.this.mJid = "";
                                        MMAddBuddySearchFragment.this.mTimer = null;
                                        MMAddBuddySearchFragment.this.closeProgressDialog();
                                        MMAddBuddySearchFragment.this.ShowAddBuddySuccessDialog();
                                    }
                                });
                            }
                        }
                    }, 5000);
                    ZMBuddySyncInstance.getInsatance().onAddBuddyByJid(iMAddrBookItem.getJid());
                } else {
                    showAddBuddyFinish(4);
                }
            }
        }
    }

    private void onClickBtnChat(@Nullable IMAddrBookItem iMAddrBookItem) {
        FragmentActivity activity = getActivity();
        if ((activity instanceof ZMActivity) && iMAddrBookItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.imChatGetOption() != 2) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(iMAddrBookItem.getJid());
                if (buddyWithJID != null && !buddyWithJID.isPending()) {
                    MMChatActivity.showAsOneToOneChat((ZMActivity) activity, buddyWithJID);
                }
            }
        }
    }

    private ZoomBuddy getSearchBuddy() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        ZoomBuddy zoomBuddy = null;
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddySearchData buddySearchData = zoomMessenger.getBuddySearchData();
        if (buddySearchData == null) {
            return null;
        }
        if (buddySearchData.getBuddyCount() > 0) {
            zoomBuddy = buddySearchData.getBuddyAt(0);
        }
        return zoomBuddy;
    }

    /* access modifiers changed from: private */
    public void onQueryJidByEmail(String str, int i) {
        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(getSearchBuddy());
        if (i == 0 && fromZoomBuddy != null) {
            if (fromZoomBuddy.isIMBlockedByIB()) {
                ShowAddBuddyFailedDialog(SubscriptionRestrictReason.SubscriptionRestrictReason_ByIB);
                return;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || !zoomMessenger.isMyContact(fromZoomBuddy.getJid())) {
                showAddBuddyFinish(1, fromZoomBuddy);
            } else {
                showAddBuddyFinish(2, fromZoomBuddy);
            }
        }
        if (i == 406) {
            mTimeExceedTimestamp = CmmTime.getMMNow();
            checkTimeExceed();
        } else if (i != 0 || fromZoomBuddy == null) {
            showAddBuddyFinish(3);
        }
    }

    /* access modifiers changed from: private */
    public void onSearchBuddyByKey(String str, int i) {
        if (!StringUtil.isEmptyOrNull(this.mKeyword)) {
            String lowerCase = this.mKeyword.toLowerCase(Locale.US);
            if (StringUtil.isSameString(str, lowerCase)) {
                IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(getSearchBuddy());
                if (fromZoomBuddy != null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        if (fromZoomBuddy.isIMBlockedByIB()) {
                            ShowAddBuddyFailedDialog(SubscriptionRestrictReason.SubscriptionRestrictReason_ByIB);
                            return;
                        }
                        if (!zoomMessenger.isMyContact(fromZoomBuddy.getJid())) {
                            zoomMessenger.addSameOrgBuddyByJID(fromZoomBuddy.getJid());
                        }
                        showAddBuddyFinish(2, fromZoomBuddy);
                    }
                } else if (validateInput(lowerCase)) {
                    ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger2 != null) {
                        zoomMessenger2.queryJidByEmail(str);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSearchBuddyPicDownloaded(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                if (fromZoomBuddy != null) {
                    this.mAdapter.update(fromZoomBuddy);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Notify_SubscriptionIsRestrict(String str, int i) {
        if (StringUtil.isSameString(str, this.mJid)) {
            this.mJid = "";
            destroyTimer();
            closeProgressDialog();
            ShowAddBuddyFailedDialog(i);
        }
    }

    /* access modifiers changed from: private */
    public void Notify_SubscribeRequestSent(String str, int i) {
        if (StringUtil.isSameString(str, this.mJid)) {
            this.mJid = "";
            destroyTimer();
            closeProgressDialog();
            if (i == 0) {
                ShowAddBuddySuccessDialog();
            }
        }
    }

    public void onActionButtonClick(View view, int i, int i2) {
        IMAddrBookItem item = this.mAdapter.getItem(i);
        if (item == null) {
            return;
        }
        if (i2 == 2) {
            onClickBtnChat(item);
        } else if (i2 == 1) {
            inviteByEmail(item);
        }
    }

    public void onInviteButtonClick(View view, String str) {
        sendEmail(str);
    }

    public void onItemClick(View view, int i) {
        IMAddrBookItem item = this.mAdapter.getItem(i);
        if (item != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.isMyContact(item.getJid())) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(item.getJid());
                if (buddyWithJID != null && !buddyWithJID.isPending()) {
                    FragmentActivity activity = getActivity();
                    if (activity instanceof ZMActivity) {
                        AddrBookItemDetailsActivity.show((ZMActivity) activity, item, 106);
                    }
                }
            }
        }
    }
}
