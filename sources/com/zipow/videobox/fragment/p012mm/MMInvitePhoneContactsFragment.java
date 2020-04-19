package com.zipow.videobox.fragment.p012mm;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.ContactsComparator;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.mm.MMInvitePhoneContactsFragment */
public class MMInvitePhoneContactsFragment extends ZMDialogFragment implements ExtListener, OnEditorActionListener, IABContactsCacheListener, OnClickListener, OnItemClickListener {
    private static final int REQUEST_PERMISSION_CONTACTS = 100;
    private boolean isKeyboardOpen;
    private ContactAdapter mAdapter;
    private Button mBtnClearSearchView;
    private Button mBtnInvite;
    /* access modifiers changed from: private */
    public QuickSearchListView mContactListView;
    @NonNull
    private List<Contact> mContacts = new ArrayList();
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    private EditText mEdtSearch;
    /* access modifiers changed from: private */
    public EditText mEdtSearchReal;
    @Nullable
    private String mFilterKey;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    @NonNull
    private IPhoneABListener mIPhoneABListener = new IPhoneABListener() {
        public void onPhoneABEvent(int i, long j, Object obj) {
            MMInvitePhoneContactsFragment.this.loadZoomContactInPhoneGroup();
        }
    };
    @NonNull
    private SimpleZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_BuddyGroupsRemoved(List<String> list) {
            MMInvitePhoneContactsFragment.this.loadZoomContactInPhoneGroup();
        }

        public void Indicate_BuddyGroupAdded(String str) {
            MMInvitePhoneContactsFragment.this.loadZoomContactInPhoneGroup();
        }

        public void Indicate_BuddyGroupInfoUpdated(String str) {
            MMInvitePhoneContactsFragment.this.loadZoomContactInPhoneGroup();
        }

        public void Indicate_BuddyGroupMembersAdded(String str, List<String> list) {
            MMInvitePhoneContactsFragment.this.loadZoomContactInPhoneGroup();
        }

        public void Indicate_BuddyGroupMembersRemoved(String str, List<String> list) {
            MMInvitePhoneContactsFragment.this.loadZoomContactInPhoneGroup();
        }

        public void Indicate_BuddyGroupMembersChanged(ChangedBuddyGroups changedBuddyGroups, boolean z) {
            MMInvitePhoneContactsFragment.this.loadZoomContactInPhoneGroup();
        }

        public void onIndicateInfoUpdatedWithJID(@NonNull String str) {
            MMInvitePhoneContactsFragment.this.onIndicateInfoUpdatedWithJID(str);
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
            String obj = MMInvitePhoneContactsFragment.this.mEdtSearchReal.getText().toString();
            MMInvitePhoneContactsFragment.this.filter(obj);
            if ((obj.length() <= 0 || MMInvitePhoneContactsFragment.this.mContactListView.getListView().getCount() <= 0) && MMInvitePhoneContactsFragment.this.mPanelTitleBar.getVisibility() != 0) {
                MMInvitePhoneContactsFragment.this.mPanelListViews.setForeground(MMInvitePhoneContactsFragment.this.mDimmedForground);
            } else {
                MMInvitePhoneContactsFragment.this.mPanelListViews.setForeground(null);
            }
        }
    };
    @NonNull
    private Map<String, String> mZMContacts = new HashMap();

    /* renamed from: com.zipow.videobox.fragment.mm.MMInvitePhoneContactsFragment$ContactAdapter */
    static class ContactAdapter extends QuickSearchListDataAdapter {
        @NonNull
        private Set<String> mCheckedPhoneNumbers = new HashSet();
        @Nullable
        private Context mContext;
        @NonNull
        private List<Contact> mData = new ArrayList();
        @NonNull
        private Set<String> mZoomPhoneContacts = new HashSet();

        public long getItemId(int i) {
            return 0;
        }

        @NonNull
        public Set<String> getCheckedPhoneNumbers() {
            return this.mCheckedPhoneNumbers;
        }

        public ContactAdapter(@Nullable Context context) {
            if (context != null) {
                this.mContext = context;
                return;
            }
            throw new RuntimeException("can not init ContactAdapter with context null");
        }

        public void setData(@Nullable List<Contact> list) {
            this.mData.clear();
            if (list != null) {
                this.mData.addAll(list);
            }
        }

        public void notifyDataSetChanged() {
            int i = 0;
            while (i < this.mData.size()) {
                if (this.mZoomPhoneContacts.contains(((Contact) this.mData.get(i)).normalizedNumber)) {
                    this.mData.remove(i);
                    i--;
                }
                i++;
            }
            super.notifyDataSetChanged();
        }

        public void setZoomPhoneContacts(@Nullable Collection<String> collection) {
            this.mZoomPhoneContacts.clear();
            if (collection != null) {
                this.mZoomPhoneContacts.addAll(collection);
            }
        }

        public void setContactChecked(String str, boolean z) {
            if (!StringUtil.isEmptyOrNull(str)) {
                if (z) {
                    this.mCheckedPhoneNumbers.add(str);
                } else {
                    this.mCheckedPhoneNumbers.remove(str);
                }
            }
        }

        @Nullable
        public String getItemSortKey(Object obj) {
            if (obj instanceof Contact) {
                return ((Contact) obj).sortKey;
            }
            return null;
        }

        public int getCount() {
            return this.mData.size();
        }

        @Nullable
        public Contact getItem(int i) {
            if (i < 0 || i >= this.mData.size()) {
                return null;
            }
            return (Contact) this.mData.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(this.mContext, C4558R.layout.zm_phone_contact_item, null);
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtContactName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtContactNumber);
            TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.txtInZoom);
            CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(C4558R.C4560id.checked);
            Contact item = getItem(i);
            if (item == null) {
                return view;
            }
            textView.setText(item.displayName);
            textView2.setText(item.normalizedNumber);
            textView3.setVisibility(8);
            checkedTextView.setVisibility(0);
            checkedTextView.setChecked(this.mCheckedPhoneNumbers.contains(item.normalizedNumber));
            return view;
        }
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, MMInvitePhoneContactsFragment.class.getName(), bundle, i, true, 1);
    }

    public static void showAsActivity(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, MMInvitePhoneContactsFragment.class.getName(), bundle, i, true, 1);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_invite_phone_contacts, viewGroup, false);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mPanelListViews = (FrameLayout) inflate.findViewById(C4558R.C4560id.panelListViews);
        this.mPanelSearchBarReal = inflate.findViewById(C4558R.C4560id.panelSearchBarReal);
        this.mEdtSearchReal = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchReal);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelSearch = inflate.findViewById(C4558R.C4560id.panelSearch);
        this.mContactListView = (QuickSearchListView) inflate.findViewById(C4558R.C4560id.contactListView);
        this.mBtnInvite = (Button) inflate.findViewById(C4558R.C4560id.btnInvite);
        this.mAdapter = new ContactAdapter(getActivity());
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        this.mContactListView.setOnItemClickListener(this);
        this.mContactListView.setAdapter(this.mAdapter);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mBtnInvite.setOnClickListener(this);
        this.mEdtSearchReal.setOnEditorActionListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mEdtSearchReal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                MMInvitePhoneContactsFragment.this.mHandler.removeCallbacks(MMInvitePhoneContactsFragment.this.mRunnableFilter);
                MMInvitePhoneContactsFragment.this.mHandler.postDelayed(MMInvitePhoneContactsFragment.this.mRunnableFilter, 300);
                MMInvitePhoneContactsFragment.this.updateClearSearchView();
            }
        });
        compatPCModeForSearch();
        return inflate;
    }

    private void compatPCModeForSearch() {
        this.mEdtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(@NonNull final View view, boolean z) {
                if (z) {
                    MMInvitePhoneContactsFragment.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (MMInvitePhoneContactsFragment.this.isAdded() && MMInvitePhoneContactsFragment.this.isResumed() && view.getId() == C4558R.C4560id.edtSearch && ((EditText) view).hasFocus()) {
                                MMInvitePhoneContactsFragment.this.onKeyboardOpen();
                            }
                        }
                    }, 500);
                }
            }
        });
    }

    public void onStart() {
        super.onStart();
        loadZoomContactInPhoneGroup();
        loadContacts();
        ABContactsCache instance = ABContactsCache.getInstance();
        if (instance.needReloadAll()) {
            instance.reloadAllContacts();
        }
        instance.addListener(this);
        PTUI.getInstance().addPhoneABListener(this.mIPhoneABListener);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
    }

    public void onStop() {
        PTUI.getInstance().removePhoneABListener(this.mIPhoneABListener);
        ABContactsCache.getInstance().removeListener(this);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        requestContactPermission();
        this.mContactListView.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(@NonNull String str) {
        IMAddrBookItem buddyByJid = ZMBuddySyncInstance.getInsatance().getBuddyByJid(str);
        if (buddyByJid == null) {
            this.mZMContacts.remove(str);
        } else {
            String buddyPhoneNumber = buddyByJid.getBuddyPhoneNumber();
            if (StringUtil.isEmptyOrNull(buddyPhoneNumber)) {
                this.mZMContacts.remove(str);
            } else {
                this.mZMContacts.put(str, buddyPhoneNumber);
            }
        }
        this.mAdapter.setZoomPhoneContacts(this.mZMContacts.values());
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void loadZoomContactInPhoneGroup() {
        this.mZMContacts.clear();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddyGroup addressbookContactBuddyGroup = zoomMessenger.getAddressbookContactBuddyGroup();
            if (addressbookContactBuddyGroup != null && addressbookContactBuddyGroup.getBuddyCount() != 0) {
                for (int i = 0; i < addressbookContactBuddyGroup.getBuddyCount(); i++) {
                    ZoomBuddy buddyAt = addressbookContactBuddyGroup.getBuddyAt(i);
                    if (buddyAt != null) {
                        String phoneNumber = buddyAt.getPhoneNumber();
                        if (!StringUtil.isEmptyOrNull(phoneNumber)) {
                            String jid = buddyAt.getJid();
                            if (jid != null) {
                                this.mZMContacts.put(jid, phoneNumber);
                            }
                        }
                    }
                }
                this.mAdapter.setZoomPhoneContacts(this.mZMContacts.values());
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void requestContactPermission() {
        if (VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
            zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 100);
            AppUtil.saveRequestContactPermissionTime();
        }
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((MMInvitePhoneContactsFragment) iUIElement).handleContactsRequestPermissionResult(i, strArr, iArr);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleContactsRequestPermissionResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 100) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                dismiss();
            } else {
                ABContactsCache.getInstance().reloadAllContacts();
            }
        }
    }

    private void doFilter() {
        ArrayList arrayList = new ArrayList();
        if (StringUtil.isEmptyOrNull(this.mFilterKey)) {
            arrayList.addAll(this.mContacts);
        } else {
            for (Contact contact : this.mContacts) {
                if (contact != null && contact.filter(this.mFilterKey)) {
                    arrayList.add(contact);
                }
            }
        }
        this.mAdapter.setData(arrayList);
        this.mAdapter.notifyDataSetChanged();
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

    private void loadContacts() {
        ABContactsCache instance = ABContactsCache.getInstance();
        int cachedContactsCount = instance.getCachedContactsCount();
        this.mContacts.clear();
        for (int i = 0; i < cachedContactsCount; i++) {
            this.mContacts.add(instance.getCachedContact(i));
        }
        Collections.sort(this.mContacts, new ContactsComparator(CompatUtils.getLocalDefault()));
        doFilter();
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
                        MMInvitePhoneContactsFragment.this.mPanelListViews.getParent().requestLayout();
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
                    MMInvitePhoneContactsFragment.this.mContactListView.requestLayout();
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
        loadContacts();
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnCancel) {
                dismiss();
            } else if (id == C4558R.C4560id.btnInvite) {
                onClickBtnInvite();
            } else if (id == C4558R.C4560id.btnClearSearchView) {
                onClickBtnClearSearchView();
            }
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearchReal.setText("");
        if (!this.isKeyboardOpen) {
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelSearchBarReal.setVisibility(8);
            this.mPanelSearch.setVisibility(0);
            this.mHandler.post(new Runnable() {
                public void run() {
                    MMInvitePhoneContactsFragment.this.mContactListView.requestLayout();
                }
            });
        }
    }

    private void onClickBtnInvite() {
        Set checkedPhoneNumbers = this.mAdapter.getCheckedPhoneNumbers();
        if (!CollectionsUtil.isCollectionEmpty(checkedPhoneNumbers)) {
            String[] strArr = new String[checkedPhoneNumbers.size()];
            checkedPhoneNumbers.toArray(strArr);
            List querySMSActivities = AndroidAppUtil.querySMSActivities(getActivity());
            if (!CollectionsUtil.isCollectionEmpty(querySMSActivities)) {
                AndroidAppUtil.sendSMSVia((ResolveInfo) querySMSActivities.get(0), getActivity(), strArr, getString(C4558R.string.zm_msg_invite_by_sms_33300));
                dismiss();
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String str;
        Object itemAtPosition = this.mContactListView.getItemAtPosition(i);
        if (itemAtPosition instanceof Contact) {
            Contact contact = (Contact) itemAtPosition;
            this.mAdapter.setContactChecked(contact.normalizedNumber, !this.mAdapter.getCheckedPhoneNumbers().contains(contact.normalizedNumber));
            this.mAdapter.notifyDataSetChanged();
            this.mBtnInvite.setEnabled(!this.mAdapter.getCheckedPhoneNumbers().isEmpty());
            int size = this.mAdapter.getCheckedPhoneNumbers().size();
            if (size == 0) {
                str = getString(C4558R.string.zm_btn_invite);
            } else {
                str = getString(C4558R.string.zm_btn_invite_33300, Integer.valueOf(size));
            }
            this.mBtnInvite.setText(str);
        }
    }

    public void dismiss() {
        finishFragment(true);
    }
}
