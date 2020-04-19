package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.AddrBookSetNumberActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.view.InviteLocalContactsListView;
import com.zipow.videobox.view.LocalContactItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMSendMessageFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.IZMMenuItem;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

@Deprecated
public class InviteLocalContactsFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnEditorActionListener, IPhoneABListener, IPTUIListener, IIMListener, IABContactsCacheListener {
    private static final String ARG_SHOW_AS_DIALOG = "showAsDialog";
    private static final int REQUEST_SET_PHONE_NUMBER = 100;
    private final String TAG = InviteLocalContactsFragment.class.getSimpleName();
    private Button mBtnBack;
    private Button mBtnClearSearchView;
    @Nullable
    private View mContentView;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private ImageView mImgNoBuddy;
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    /* access modifiers changed from: private */
    public InviteLocalContactsListView mListView;
    private View mPanelNoItemMsg;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            if (ABContactsHelper.isMatchPhoneNumbersCalled()) {
                String obj = InviteLocalContactsFragment.this.mEdtSearch.getText().toString();
                InviteLocalContactsFragment.this.mListView.filter(obj);
                if ((obj.length() <= 0 || InviteLocalContactsFragment.this.mListView.getCount() <= 0) && InviteLocalContactsFragment.this.mPanelTitleBar.getVisibility() != 0) {
                    InviteLocalContactsFragment.this.mListContainer.setForeground(InviteLocalContactsFragment.this.mDimmedForground);
                } else {
                    InviteLocalContactsFragment.this.mListContainer.setForeground(null);
                }
            }
        }
    };
    private TextView mTxtNoContactsMessage;
    private TextView mTxtTitle;

    public static class ContextMenuFragment extends ZMDialogFragment {
        private static final String ARG_ADDRBOOKITEM = "addrBookItem";
        private ZMMenuAdapter<ContextMenuItem> mAdapter;

        public static void show(@NonNull FragmentManager fragmentManager, @NonNull LocalContactItem localContactItem) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_ADDRBOOKITEM, localContactItem);
            ContextMenuFragment contextMenuFragment = new ContextMenuFragment();
            contextMenuFragment.setArguments(bundle);
            contextMenuFragment.show(fragmentManager, ContextMenuFragment.class.getName());
        }

        public ContextMenuFragment() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            String str;
            if (getArguments() == null) {
                return createEmptyDialog();
            }
            LocalContactItem localContactItem = (LocalContactItem) getArguments().getSerializable(ARG_ADDRBOOKITEM);
            this.mAdapter = createUpdateAdapter();
            String screenName = localContactItem.getScreenName();
            if (StringUtil.isEmptyOrNull(screenName)) {
                str = getString(C4558R.string.zm_title_invite);
            } else {
                str = getString(C4558R.string.zm_title_invite_xxx, screenName);
            }
            ZMAlertDialog create = new Builder(getActivity()).setTitle((CharSequence) str).setAdapter(this.mAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContextMenuFragment.this.onSelectItem(i);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }

        private ZMMenuAdapter<ContextMenuItem> createUpdateAdapter() {
            IZMMenuItem[] iZMMenuItemArr = null;
            if (getArguments() == null) {
                return null;
            }
            LocalContactItem localContactItem = (LocalContactItem) getArguments().getSerializable(ARG_ADDRBOOKITEM);
            if (localContactItem != null) {
                ContextMenuItem[] contextMenuItemArr = new ContextMenuItem[(localContactItem.getPhoneNumberCount() + localContactItem.getEmailCount())];
                int i = 0;
                int i2 = 0;
                while (i < localContactItem.getPhoneNumberCount()) {
                    String phoneNumber = localContactItem.getPhoneNumber(i);
                    int i3 = i2 + 1;
                    contextMenuItemArr[i2] = new ContextMenuItem(phoneNumber, phoneNumber, null);
                    i++;
                    i2 = i3;
                }
                int i4 = 0;
                while (i4 < localContactItem.getEmailCount()) {
                    String email = localContactItem.getEmail(i4);
                    int i5 = i2 + 1;
                    contextMenuItemArr[i2] = new ContextMenuItem(email, null, email);
                    i4++;
                    i2 = i5;
                }
                iZMMenuItemArr = contextMenuItemArr;
            }
            ZMMenuAdapter<ContextMenuItem> zMMenuAdapter = this.mAdapter;
            if (zMMenuAdapter == null) {
                this.mAdapter = new ZMMenuAdapter<>(getActivity(), false);
            } else {
                zMMenuAdapter.clear();
            }
            if (iZMMenuItemArr != null) {
                this.mAdapter.addAll((MenuItemType[]) iZMMenuItemArr);
            }
            return this.mAdapter;
        }

        public void refresh() {
            ZMMenuAdapter createUpdateAdapter = createUpdateAdapter();
            if (createUpdateAdapter != null) {
                createUpdateAdapter.notifyDataSetChanged();
            }
        }

        /* access modifiers changed from: private */
        public void onSelectItem(int i) {
            ContextMenuItem contextMenuItem = (ContextMenuItem) this.mAdapter.getItem(i);
            if (contextMenuItem != null) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                    if (supportFragmentManager != null) {
                        if (contextMenuItem.isPhoneNumberItem()) {
                            InviteLocalContactsFragment.inviteBySMS(zMActivity, supportFragmentManager, contextMenuItem.getPhoneNumber());
                        } else {
                            InviteLocalContactsFragment.inviteByEmail(zMActivity, supportFragmentManager, contextMenuItem.getEmail());
                        }
                    }
                }
            }
        }
    }

    static class ContextMenuItem extends ZMSimpleMenuItem {
        private String mEmail;
        private String mPhoneNumber;

        public ContextMenuItem(String str, String str2, String str3) {
            super(0, str);
            this.mPhoneNumber = str2;
            this.mEmail = str3;
        }

        public String getPhoneNumber() {
            return this.mPhoneNumber;
        }

        public String getEmail() {
            return this.mEmail;
        }

        public boolean isPhoneNumberItem() {
            return !StringUtil.isEmptyOrNull(this.mPhoneNumber);
        }

        public boolean isEmailItem() {
            return !StringUtil.isEmptyOrNull(this.mEmail);
        }
    }

    private void onPhoneBindByOther() {
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
    }

    public void onIMBuddySort() {
    }

    public void onIMLocalStatusChanged(int i) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public void onSubscriptionRequest() {
    }

    public void onSubscriptionUpdate() {
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_SHOW_AS_DIALOG, false);
        SimpleActivity.show(zMActivity, InviteLocalContactsFragment.class.getName(), bundle, 0);
    }

    public static void showAsActivity(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_SHOW_AS_DIALOG, false);
        SimpleActivity.show(fragment, InviteLocalContactsFragment.class.getName(), bundle, 0);
    }

    @NonNull
    public static InviteLocalContactsFragment newInstance(int i) {
        Bundle bundle = new Bundle();
        InviteLocalContactsFragment inviteLocalContactsFragment = new InviteLocalContactsFragment();
        inviteLocalContactsFragment.setArguments(bundle);
        return inviteLocalContactsFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null && !arguments.getBoolean(ARG_SHOW_AS_DIALOG)) {
            setShowsDialog(false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_invite_local_contacts_list, viewGroup, false);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mListView = (InviteLocalContactsListView) inflate.findViewById(C4558R.C4560id.addrBookListView);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mTxtNoContactsMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtNoContactsMessage);
        this.mImgNoBuddy = (ImageView) inflate.findViewById(C4558R.C4560id.imgNoBuddy);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                InviteLocalContactsFragment.this.mHandler.removeCallbacks(InviteLocalContactsFragment.this.mRunnableFilter);
                InviteLocalContactsFragment.this.mHandler.postDelayed(InviteLocalContactsFragment.this.mRunnableFilter, 300);
                InviteLocalContactsFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mListView.setParentFragment(this);
        if ((getActivity() instanceof IMActivity) && !((IMActivity) getActivity()).isKeyboardOpen()) {
            onKeyboardClosed();
        }
        this.mPanelNoItemMsg.setVisibility(8);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        SparseArray sparseArray = new SparseArray();
        View view = getView();
        if (view != null) {
            view.saveHierarchyState(sparseArray);
        } else {
            View view2 = this.mContentView;
            if (view2 != null) {
                view2.saveHierarchyState(sparseArray);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(InviteLocalContactsFragment.class.getName());
        sb.append(".State");
        bundle.putSparseParcelableArray(sb.toString(), sparseArray);
        super.onSaveInstanceState(bundle);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        String str;
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            String str2 = null;
            if (intent != null) {
                str2 = intent.getStringExtra(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
                str = intent.getStringExtra("number");
            } else {
                str = null;
            }
            onSetPhoneNumberDone(str2, str);
        }
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
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
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
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

    public void onResume() {
        boolean z;
        super.onResume();
        PTUI.getInstance().addPTUIListener(this);
        PTUI.getInstance().addIMListener(this);
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            if (!instance.needReloadAll() || StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
                z = true;
            } else {
                matchNewNumbers();
                z = instance.reloadAllContacts();
                if (z) {
                    InviteLocalContactsListView.clearCaches();
                }
            }
            if (z && PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            } else if (!StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
                matchNewNumbers();
            }
            refreshAll(true);
        }
    }

    private void matchNewNumbers() {
        if (PTApp.getInstance().isWebSignedOn()) {
            int matchNewNumbers = ContactsMatchHelper.getInstance().matchNewNumbers(getActivity());
            if (matchNewNumbers != 0) {
                if (matchNewNumbers == -1) {
                    reloadAllItems(true);
                } else {
                    showErrorDialog(matchNewNumbers);
                }
            }
        }
    }

    private void updateUI() {
        if (PTApp.getInstance().isPhoneNumberRegistered()) {
            this.mEdtSearch.setVisibility(0);
            this.mTxtTitle.setText(C4558R.string.zm_title_mm_add_phone_contacts);
            return;
        }
        this.mEdtSearch.setVisibility(8);
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
        PTUI.getInstance().removeIMListener(this);
        ABContactsCache.getInstance().removeListener(this);
    }

    public void onContactsCacheUpdated() {
        InviteLocalContactsListView.clearCaches();
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            if (PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            } else if (!StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
                matchNewNumbers();
            }
            if (isResumed()) {
                refreshAll(true);
            }
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        SparseArray sparseArray;
        super.onActivityCreated(bundle);
        if (bundle != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(InviteLocalContactsFragment.class.getName());
            sb.append(".State");
            sparseArray = bundle.getSparseParcelableArray(sb.toString());
        } else {
            sparseArray = null;
        }
        this.mContentView = getView();
        View view = this.mContentView;
        if (!(view == null || sparseArray == null)) {
            view.restoreHierarchyState(sparseArray);
        }
        if (this.mContentView == null) {
            this.mContentView = onCreateView(getLayoutInflater(bundle), null, bundle);
            View view2 = this.mContentView;
            if (!(view2 == null || sparseArray == null)) {
                view2.restoreHierarchyState(sparseArray);
            }
        }
        PTUI.getInstance().addPhoneABListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        FragmentActivity activity = getActivity();
        if ((activity != null && activity.isFinishing()) || isRemoving()) {
            PTUI.getInstance().removePhoneABListener(this);
        }
        this.mHandler.removeCallbacks(this.mRunnableFilter);
    }

    public boolean onSearchRequested() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void enableAddrBook() {
        AddrBookSetNumberActivity.show((Fragment) this, 100);
    }

    public void onSetPhoneNumberDone(String str, String str2) {
        onAddressBookEnabled();
    }

    public void onAddressBookEnabled() {
        this.mListView.startABMatching();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity instanceof IMActivity) {
            ((IMActivity) zMActivity).onAddressBookEnabled(true);
        } else {
            refreshAll(true);
        }
    }

    private void refreshAll(boolean z) {
        if (getView() != null) {
            updateUI();
            this.mListView.setFilter(this.mEdtSearch.getText().toString());
            reloadAllItems(z);
            updateBtnClearSearchView();
        }
    }

    public void reloadAllItems() {
        reloadAllItems(false);
    }

    public void reloadAllItems(boolean z) {
        if (getView() != null && this.mListView != null) {
            if (!PTApp.getInstance().isPhoneNumberRegistered()) {
                this.mPanelNoItemMsg.setVisibility(8);
                this.mListView.reloadAllItems();
            } else if (ABContactsHelper.isMatchPhoneNumbersCalled() || z) {
                this.mListView.reloadAllItems();
                if (this.mListView.getContactsItemCount() > 0 || this.mEdtSearch.getText().length() > 0) {
                    this.mPanelNoItemMsg.setVisibility(8);
                } else {
                    this.mPanelNoItemMsg.setVisibility(0);
                    this.mImgNoBuddy.setImageResource(C4558R.C4559drawable.zm_ic_no_buddy);
                    this.mTxtNoContactsMessage.setText(C4558R.string.zm_msg_no_system_contacts);
                }
            }
        }
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            onWebLogin(j);
        }
    }

    private void onWebLogin(long j) {
        if (j == 0) {
            ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
            if (aBContactsHelper != null && PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            }
        }
    }

    public void onPhoneABEvent(int i, long j, Object obj) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final int i2 = i;
        final long j2 = j;
        final Object obj2 = obj;
        C26393 r1 = new EventAction("handlePhoneABEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ((InviteLocalContactsFragment) iUIElement).handlePhoneABEvent(i2, j2, obj2);
            }
        };
        nonNullEventTaskManagerOrThrowException.push(r1);
    }

    /* access modifiers changed from: private */
    public void handlePhoneABEvent(int i, long j, Object obj) {
        switch (i) {
            case 3:
                onPhoneABMatchUpdated(j);
                return;
            default:
                return;
        }
    }

    private void onPhoneABMatchUpdated(long j) {
        this.mListView.onPhoneABMatchUpdated(j);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            int i = (int) j;
            if (i == 0) {
                reloadAllItems(true);
            } else if (i == 1104) {
                onPhoneBindByOther();
            }
        }
    }

    public boolean startABMatching() {
        int startABMatching = this.mListView.startABMatching();
        if (startABMatching == 0) {
            return true;
        }
        if (startABMatching == -1) {
            reloadAllItems(true);
        } else {
            showErrorDialog(startABMatching);
        }
        return false;
    }

    private void showErrorDialog(int i) {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_match_contacts_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    public void onIMReceived(IMMessage iMMessage) {
        InviteLocalContactsListView inviteLocalContactsListView = this.mListView;
        if (inviteLocalContactsListView != null) {
            inviteLocalContactsListView.updateUnreadMessageCountBubble();
        }
    }

    public void showNonZoomUserActions(@Nullable LocalContactItem localContactItem) {
        if (localContactItem != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    int phoneNumberCount = localContactItem.getPhoneNumberCount();
                    int emailCount = localContactItem.getEmailCount();
                    if (phoneNumberCount == 1 && emailCount == 0) {
                        inviteBySMS(zMActivity, supportFragmentManager, localContactItem.getPhoneNumber(0));
                    } else if (phoneNumberCount == 0 && emailCount == 1) {
                        inviteByEmail(zMActivity, supportFragmentManager, localContactItem.getEmail(0));
                    } else {
                        ContextMenuFragment.show(supportFragmentManager, localContactItem);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void inviteByEmail(Context context, FragmentManager fragmentManager, String str) {
        String zoomInvitationEmailSubject = PTApp.getInstance().getZoomInvitationEmailSubject();
        String zoomInvitationEmailBody = PTApp.getInstance().getZoomInvitationEmailBody();
        ZMSendMessageFragment.show(context, fragmentManager, new String[]{str}, null, zoomInvitationEmailSubject, zoomInvitationEmailBody, zoomInvitationEmailBody, null, null, 1);
    }

    /* access modifiers changed from: private */
    public static void inviteBySMS(Context context, FragmentManager fragmentManager, String str) {
        String zoomInvitationEmailSubject = PTApp.getInstance().getZoomInvitationEmailSubject();
        String string = context.getString(C4558R.string.zm_msg_sms_invitation_content);
        ZMSendMessageFragment.show(context, fragmentManager, null, new String[]{str}, zoomInvitationEmailSubject, string, string, null, null, 2);
    }
}
