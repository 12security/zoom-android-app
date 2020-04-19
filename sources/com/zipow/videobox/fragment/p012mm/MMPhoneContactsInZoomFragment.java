package com.zipow.videobox.fragment.p012mm;

import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.fragment.p012mm.ContactsAdapter.OnContactOPListener;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.ChangedBuddyGroups;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.mm.MMPhoneContactsInZoomFragment */
public class MMPhoneContactsInZoomFragment extends ZMDialogFragment implements ExtListener, OnEditorActionListener, IABContactsCacheListener, OnClickListener, OnContactOPListener {
    private static final String TAG = "MMPhoneContactsInZoomFragment";
    private boolean isKeyboardOpen;
    private ContactsAdapter mAdapter;
    private Button mBtnClearSearchView;
    /* access modifiers changed from: private */
    public QuickSearchListView mContactListView;
    @NonNull
    private List<ContactItem> mContacts = new ArrayList();
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    private EditText mEdtSearch;
    /* access modifiers changed from: private */
    public EditText mEdtSearchReal;
    private View mEmptyView;
    @Nullable
    private String mFilterKey;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    @NonNull
    private IPhoneABListener mIPhoneABListener = new IPhoneABListener() {
        public void onPhoneABEvent(int i, long j, Object obj) {
            MMPhoneContactsInZoomFragment.this.loadAllZoomPhoneContacts();
        }
    };
    @NonNull
    private SimpleZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_BuddyGroupsRemoved(List<String> list) {
            MMPhoneContactsInZoomFragment.this.loadAllZoomPhoneContacts();
        }

        public void Indicate_BuddyGroupAdded(String str) {
            MMPhoneContactsInZoomFragment.this.loadAllZoomPhoneContacts();
        }

        public void Indicate_BuddyGroupInfoUpdated(String str) {
            MMPhoneContactsInZoomFragment.this.loadAllZoomPhoneContacts();
        }

        public void Indicate_BuddyGroupMembersAdded(String str, List<String> list) {
            MMPhoneContactsInZoomFragment.this.loadAllZoomPhoneContacts();
        }

        public void Indicate_BuddyGroupMembersRemoved(String str, List<String> list) {
            MMPhoneContactsInZoomFragment.this.loadAllZoomPhoneContacts();
        }

        public void Indicate_BuddyGroupMembersChanged(ChangedBuddyGroups changedBuddyGroups, boolean z) {
            MMPhoneContactsInZoomFragment.this.loadAllZoomPhoneContacts();
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMPhoneContactsInZoomFragment.this.onIndicateInfoUpdatedWithJID(str);
        }
    };
    /* access modifiers changed from: private */
    public FrameLayout mPanelListViews;
    private View mPanelSearch;
    private View mPanelSearchBarReal;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = MMPhoneContactsInZoomFragment.this.mEdtSearchReal.getText().toString();
            MMPhoneContactsInZoomFragment.this.filter(obj);
            if (obj.length() > 0 || MMPhoneContactsInZoomFragment.this.mPanelTitleBar.getVisibility() == 0) {
                MMPhoneContactsInZoomFragment.this.mPanelListViews.setForeground(null);
            } else {
                MMPhoneContactsInZoomFragment.this.mPanelListViews.setForeground(MMPhoneContactsInZoomFragment.this.mDimmedForground);
            }
        }
    };

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, MMPhoneContactsInZoomFragment.class.getName(), bundle, i, false, 1);
    }

    public static void showAsActivity(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, MMPhoneContactsInZoomFragment.class.getName(), bundle, i, false, 1);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_contacts_in_zoom, viewGroup, false);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mContactListView = (QuickSearchListView) inflate.findViewById(C4558R.C4560id.contactListView);
        this.mEmptyView = inflate.findViewById(C4558R.C4560id.emptyView);
        inflate.findViewById(C4558R.C4560id.btnInviteZoom).setOnClickListener(this);
        this.mAdapter = new ContactsAdapter(getActivity(), this);
        this.mContactListView.setAdapter(this.mAdapter);
        this.mPanelListViews = (FrameLayout) inflate.findViewById(C4558R.C4560id.panelListViews);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mPanelSearchBarReal = inflate.findViewById(C4558R.C4560id.panelSearchBarReal);
        this.mEdtSearchReal = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchReal);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelSearch = inflate.findViewById(C4558R.C4560id.panelSearch);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        this.mEdtSearchReal.setOnEditorActionListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mEdtSearchReal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                MMPhoneContactsInZoomFragment.this.mHandler.removeCallbacks(MMPhoneContactsInZoomFragment.this.mRunnableFilter);
                MMPhoneContactsInZoomFragment.this.mHandler.postDelayed(MMPhoneContactsInZoomFragment.this.mRunnableFilter, 300);
                MMPhoneContactsInZoomFragment.this.updateClearSearchView();
            }
        });
        compatPCModeForSearch();
        return inflate;
    }

    private void addFooterView() {
        View inflate = View.inflate(getActivity(), C4558R.layout.zm_item_invite_people, null);
        inflate.findViewById(C4558R.C4560id.btnInviteZoom).setOnClickListener(this);
        this.mContactListView.getListView().addFooterView(inflate);
    }

    private void compatPCModeForSearch() {
        this.mEdtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(@NonNull final View view, boolean z) {
                if (z) {
                    MMPhoneContactsInZoomFragment.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (MMPhoneContactsInZoomFragment.this.isAdded() && MMPhoneContactsInZoomFragment.this.isResumed() && view.getId() == C4558R.C4560id.edtSearch && ((EditText) view).hasFocus()) {
                                MMPhoneContactsInZoomFragment.this.onKeyboardOpen();
                            }
                        }
                    }, 500);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            int i = 0;
            if (buddyWithJID == null || buddyWithJID.getPhoneNumber() == null) {
                boolean z = false;
                while (i < this.mContacts.size()) {
                    ContactItem contactItem = (ContactItem) this.mContacts.get(i);
                    if (contactItem.getBuddy() != null && TextUtils.equals(str, contactItem.getBuddy().getJid())) {
                        this.mContacts.remove(i);
                        z = true;
                    }
                    i++;
                }
                if (z) {
                    doFilter();
                }
            } else {
                String phoneNumber = buddyWithJID.getPhoneNumber();
                boolean z2 = false;
                while (i < this.mContacts.size()) {
                    ContactItem contactItem2 = (ContactItem) this.mContacts.get(i);
                    if (contactItem2.getBuddy() != null && TextUtils.equals(str, contactItem2.getBuddy().getJid())) {
                        z2 = true;
                    }
                    i++;
                }
                if (!z2) {
                    Contact firstContactByPhoneNumber = ABContactsCache.getInstance().getFirstContactByPhoneNumber(phoneNumber);
                    if (firstContactByPhoneNumber != null) {
                        ContactItem contactItem3 = new ContactItem();
                        contactItem3.setContact(firstContactByPhoneNumber);
                        contactItem3.setBuddy(IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                        this.mContacts.add(contactItem3);
                        doFilter();
                    }
                }
            }
        }
    }

    private void doFilter() {
        ArrayList arrayList = new ArrayList();
        if (StringUtil.isEmptyOrNull(this.mFilterKey)) {
            arrayList.addAll(this.mContacts);
        } else {
            for (ContactItem contactItem : this.mContacts) {
                if (contactItem.getContact() != null && contactItem.getContact().filter(this.mFilterKey)) {
                    arrayList.add(contactItem);
                }
            }
        }
        this.mAdapter.setData(arrayList);
        this.mAdapter.notifyDataSetChanged();
        this.mEmptyView.setVisibility(this.mAdapter.getCount() == 0 ? 0 : 8);
    }

    /* access modifiers changed from: private */
    public void filter(@Nullable String str) {
        if (str != null) {
            String lowerCase = str.trim().toLowerCase(CompatUtils.getLocalDefault());
            if (!StringUtil.isSameString(lowerCase, this.mFilterKey)) {
                this.mFilterKey = lowerCase;
                doFilter();
            }
        }
    }

    public void onKeyboardOpen() {
        if (!this.isKeyboardOpen) {
            this.isKeyboardOpen = true;
            if (this.mEdtSearch.hasFocus()) {
                this.mPanelTitleBar.setVisibility(8);
                this.mPanelListViews.setForeground(this.mDimmedForground);
                this.mPanelSearchBarReal.setVisibility(0);
                this.mPanelSearch.setVisibility(8);
                this.mEdtSearchReal.setText("");
                this.mEdtSearchReal.requestFocus();
                this.mHandler.post(new Runnable() {
                    public void run() {
                        MMPhoneContactsInZoomFragment.this.mPanelListViews.getParent().requestLayout();
                    }
                });
            }
        }
    }

    public void onKeyboardClosed() {
        this.isKeyboardOpen = false;
        if (this.mEdtSearch != null) {
            if (this.mEdtSearchReal.length() == 0 || this.mContactListView.getListView().getCount() == 0) {
                this.mPanelListViews.setForeground(null);
                this.mEdtSearchReal.setText("");
                this.mPanelTitleBar.setVisibility(0);
                this.mPanelSearchBarReal.setVisibility(4);
                this.mPanelSearch.setVisibility(0);
            }
            this.mHandler.post(new Runnable() {
                public void run() {
                    MMPhoneContactsInZoomFragment.this.mContactListView.requestLayout();
                }
            });
        }
    }

    public boolean onBackPressed() {
        return handleBackPressed();
    }

    public boolean handleBackPressed() {
        if (this.mPanelSearchBarReal.getVisibility() != 0) {
            return false;
        }
        this.mPanelTitleBar.setVisibility(0);
        this.mPanelSearchBarReal.setVisibility(4);
        this.mPanelSearch.setVisibility(0);
        this.mEdtSearchReal.setText("");
        this.isKeyboardOpen = false;
        return true;
    }

    /* access modifiers changed from: private */
    public void updateClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearchReal.getText().length() > 0 ? 0 : 8);
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onContactsCacheUpdated() {
        loadAllZoomPhoneContacts();
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearchReal.setText("");
        if (!this.isKeyboardOpen) {
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelSearchBarReal.setVisibility(8);
            this.mPanelSearch.setVisibility(0);
            this.mHandler.post(new Runnable() {
                public void run() {
                    MMPhoneContactsInZoomFragment.this.mContactListView.requestLayout();
                }
            });
        }
    }

    public void onResume() {
        super.onResume();
        this.mContactListView.onResume();
        loadAllZoomPhoneContacts();
    }

    public void onStart() {
        super.onStart();
        ABContactsCache instance = ABContactsCache.getInstance();
        if (instance.needReloadAll()) {
            matchNewNumbers();
            instance.reloadAllContacts();
        }
        instance.addListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
        PTUI.getInstance().addPhoneABListener(this.mIPhoneABListener);
    }

    public void onStop() {
        ABContactsCache.getInstance().removeListener(this);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        PTUI.getInstance().removePhoneABListener(this.mIPhoneABListener);
        super.onStop();
    }

    private void matchNewNumbers() {
        if (PTApp.getInstance().isWebSignedOn()) {
            ContactsMatchHelper.getInstance().matchNewNumbers(getContext());
        }
    }

    /* access modifiers changed from: private */
    public void loadAllZoomPhoneContacts() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddyGroup addressbookContactBuddyGroup = zoomMessenger.getAddressbookContactBuddyGroup();
            if (addressbookContactBuddyGroup != null) {
                ABContactsCache instance = ABContactsCache.getInstance();
                List<Contact> allCacheContacts = instance.getAllCacheContacts();
                if (!CollectionsUtil.isCollectionEmpty(allCacheContacts)) {
                    this.mContacts.clear();
                    HashSet hashSet = new HashSet();
                    if (PTApp.getInstance().isPhoneNumberRegistered()) {
                        for (int i = 0; i < addressbookContactBuddyGroup.getBuddyCount(); i++) {
                            ZoomBuddy buddyAt = addressbookContactBuddyGroup.getBuddyAt(i);
                            if (buddyAt != null) {
                                ZMLog.m286i(TAG, "loadAllZoomPhoneContacts find buddy %s ", buddyAt.getJid());
                                String phoneNumber = buddyAt.getPhoneNumber();
                                if (StringUtil.isEmptyOrNull(phoneNumber)) {
                                    ZMLog.m280e(TAG, "loadAllZoomPhoneContacts buddy in AddressbookContactBuddyGroup but no phone %s ", buddyAt.getJid());
                                } else {
                                    Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(phoneNumber);
                                    if (firstContactByPhoneNumber == null) {
                                        ZMLog.m280e(TAG, "loadAllZoomPhoneContacts buddy in AddressbookContactBuddyGroup but can not match %s number:%s", buddyAt.getJid(), phoneNumber);
                                    } else {
                                        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyAt);
                                        if (fromZoomBuddy != null) {
                                            hashSet.add(Integer.valueOf(firstContactByPhoneNumber.contactId));
                                            fromZoomBuddy.setContact(firstContactByPhoneNumber);
                                            ContactItem contactItem = new ContactItem();
                                            contactItem.setBuddy(IMAddrBookItem.fromZoomBuddy(buddyAt));
                                            contactItem.setContact(firstContactByPhoneNumber);
                                            this.mContacts.add(contactItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (Contact contact : allCacheContacts) {
                        if (!hashSet.contains(Integer.valueOf(contact.contactId))) {
                            IMAddrBookItem fromContact = IMAddrBookItem.fromContact(contact);
                            if (fromContact != null) {
                                ContactItem contactItem2 = new ContactItem();
                                contactItem2.setBuddy(fromContact);
                                contactItem2.setContact(contact);
                                this.mContacts.add(contactItem2);
                            }
                        }
                    }
                    doFilter();
                }
            }
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnCancel) {
                finishFragment(true);
            } else if (id == C4558R.C4560id.btnInviteZoom) {
                onBtnInviteClick();
            } else if (id == C4558R.C4560id.btnClearSearchView) {
                onClickBtnClearSearchView();
            }
        }
    }

    private void onBtnInviteClick() {
        MMInvitePhoneContactsFragment.showAsActivity((Fragment) this, 0);
    }

    public void onContactAddClick(@Nullable ContactItem contactItem) {
        String str;
        if (contactItem != null) {
            if (!contactItem.getBuddy().isFromPhoneContacts() || contactItem.getContact() == null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (!zoomMessenger.isConnectionGood()) {
                        showConnectionError();
                        return;
                    }
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself == null) {
                        str = "";
                    } else {
                        str = myself.getScreenName();
                    }
                    if (zoomMessenger.addBuddyByJID(contactItem.getBuddy().getJid(), str, null, contactItem.getBuddy().getScreenName(), contactItem.getBuddy().getAccountEmail())) {
                        ZMBuddySyncInstance.getInsatance().onAddBuddyByJid(contactItem.getBuddy().getJid());
                        contactItem.getBuddy().setPending(true);
                        this.mAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                String[] strArr = {contactItem.getContact().normalizedNumber};
                List querySMSActivities = AndroidAppUtil.querySMSActivities(getActivity());
                if (!CollectionsUtil.isCollectionEmpty(querySMSActivities)) {
                    AndroidAppUtil.sendSMSVia((ResolveInfo) querySMSActivities.get(0), getActivity(), strArr, getString(C4558R.string.zm_msg_invite_by_sms_33300));
                }
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }
}
