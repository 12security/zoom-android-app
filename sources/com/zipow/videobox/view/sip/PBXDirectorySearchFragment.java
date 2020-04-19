package com.zipow.videobox.view.sip;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.ContactType;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.PhoneNumber;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ContactCloudSIP;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.ZMBuddyListListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.ZMSearchBar.OnSearchBarListener;
import com.zipow.videobox.view.p014mm.PhoneLabelFragment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class PBXDirectorySearchFragment extends ZMDialogFragment implements OnClickListener, ExtListener, ZMBuddyListListener, OnItemClickListener {
    public static final String REQUEST_CODE = "request_code";
    public static final int REQUEST_PICK_SIP = 109;
    public static final String RESULT_DISPLAY_NAME = "RESULT_DISPLAY_NAME";
    public static final String RESULT_PHONE_NUMBER = "RESULT_PHONE_NUMBER";
    private boolean isKeyboardOpen = false;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public PBXDirectorySearchListView mDirectoryListView;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    @NonNull
    private ISIPCallEventListener mISIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableRefresh);
            PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableFilter);
            PBXDirectorySearchFragment.this.mHandler.postDelayed(PBXDirectorySearchFragment.this.mRunnableRefresh, 300);
        }
    };
    private SimpleISIPLineMgrEventSinkListener mISIPLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableRefresh);
            PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableFilter);
            PBXDirectorySearchFragment.this.mHandler.postDelayed(PBXDirectorySearchFragment.this.mRunnableRefresh, 300);
        }
    };
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    /* access modifiers changed from: private */
    public ZMSearchBar mPanelSearch;
    private ZMSearchBar mPanelSearchBar;
    private View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String trim = PBXDirectorySearchFragment.this.mPanelSearch.getText().trim();
            PBXDirectorySearchFragment.this.mDirectoryListView.filter(trim);
            if ((trim.length() <= 0 || PBXDirectorySearchFragment.this.mDirectoryListView.getDataItemCount() <= 0) && PBXDirectorySearchFragment.this.mDirectoryListView.getVisibility() != 0) {
                PBXDirectorySearchFragment.this.mListContainer.setForeground(PBXDirectorySearchFragment.this.mDimmedForground);
            } else {
                PBXDirectorySearchFragment.this.mListContainer.setForeground(null);
            }
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableRefresh = new Runnable() {
        public void run() {
            PBXDirectorySearchFragment.this.mDirectoryListView.refresh();
            if ((PBXDirectorySearchFragment.this.mPanelSearch.getText().trim().length() <= 0 || PBXDirectorySearchFragment.this.mDirectoryListView.getDataItemCount() <= 0) && PBXDirectorySearchFragment.this.mDirectoryListView.getVisibility() != 0) {
                PBXDirectorySearchFragment.this.mListContainer.setForeground(PBXDirectorySearchFragment.this.mDimmedForground);
            } else {
                PBXDirectorySearchFragment.this.mListContainer.setForeground(null);
            }
        }
    };
    private View mSearchBarDivideLine;
    private View mTxtEmptyView;
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onSearchBuddyByKey(String str, int i) {
            if (i == 0 && StringUtil.isSameString(PBXDirectorySearchFragment.this.mPanelSearch.getText().trim(), str)) {
                PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableFilter);
                PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableRefresh);
                PBXDirectorySearchFragment.this.mHandler.postDelayed(PBXDirectorySearchFragment.this.mRunnableRefresh, 300);
            }
        }

        public void onSearchBuddyByKeyV2(String str, String str2, String str3, int i) {
            if (i == 0 && StringUtil.isSameString(PBXDirectorySearchFragment.this.mPanelSearch.getText().trim(), str) && PBXDirectorySearchFragment.this.searchV2RequestID.equals(str3)) {
                PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableFilter);
                PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableRefresh);
                PBXDirectorySearchFragment.this.mHandler.postDelayed(PBXDirectorySearchFragment.this.mRunnableRefresh, 300);
            }
        }
    };
    /* access modifiers changed from: private */
    public String searchV2RequestID = "";

    public boolean onBackPressed() {
        return false;
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsFragment(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_CODE, i);
        SimpleActivity.show(fragment, PBXDirectorySearchFragment.class.getName(), bundle, i, 2);
    }

    /* access modifiers changed from: private */
    public String getContactType(int... iArr) {
        if (iArr == null || iArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int append : iArr) {
            sb.append(append);
            sb.append(PreferencesConstants.COOKIE_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_sip_directory_search, viewGroup, false);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mPanelSearch = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearch);
        this.mPanelSearchBar = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mSearchBarDivideLine = inflate.findViewById(C4558R.C4560id.searchBarDivideLine);
        this.mDirectoryListView = (PBXDirectorySearchListView) inflate.findViewById(C4558R.C4560id.directoryListView);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.mListContainer);
        this.mTxtEmptyView = inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mPanelSearch.setOnSearchBarListener(new OnSearchBarListener() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                String trim = PBXDirectorySearchFragment.this.mPanelSearch.getText().trim();
                if (!StringUtil.isEmptyOrNull(trim) && trim.length() > 2) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        PBXDirectorySearchFragment pBXDirectorySearchFragment = PBXDirectorySearchFragment.this;
                        pBXDirectorySearchFragment.searchV2RequestID = zoomMessenger.searchBuddyByKeyV2(trim, pBXDirectorySearchFragment.getContactType(0, 1, 4, 6, 7, 8, 3, 2, 5));
                    }
                }
                PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableFilter);
                PBXDirectorySearchFragment.this.mHandler.removeCallbacks(PBXDirectorySearchFragment.this.mRunnableRefresh);
                PBXDirectorySearchFragment.this.mHandler.postDelayed(PBXDirectorySearchFragment.this.mRunnableFilter, 300);
            }

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                UIUtil.closeSoftKeyboard(PBXDirectorySearchFragment.this.getActivity(), PBXDirectorySearchFragment.this.mPanelSearch.getEditText());
                return true;
            }

            public void onClearSearch() {
                UIUtil.closeSoftKeyboard(PBXDirectorySearchFragment.this.getActivity(), PBXDirectorySearchFragment.this.mPanelSearch.getEditText());
            }
        });
        this.mDirectoryListView.setOnItemClickListener(this);
        this.mDirectoryListView.setEmptyView(this.mTxtEmptyView);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        CmmSIPCallManager.getInstance().addListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        return inflate;
    }

    public void onStart() {
        super.onStart();
        ZMBuddySyncInstance.getInsatance().addListener(this);
    }

    public void onResume() {
        super.onResume();
        PBXDirectorySearchListView pBXDirectorySearchListView = this.mDirectoryListView;
        if (pBXDirectorySearchListView != null) {
            pBXDirectorySearchListView.onResume();
        }
    }

    public void onStop() {
        ZMBuddySyncInstance.getInsatance().removeListener(this);
        super.onStop();
    }

    public void onClick(@Nullable View view) {
        if (view != null && C4558R.C4560id.btnCancel == view.getId()) {
            finishFragment(true);
        }
    }

    public void onKeyboardOpen() {
        this.isKeyboardOpen = true;
        if (getView() != null) {
            this.mPanelTitleBar.setVisibility(8);
            this.mPanelSearch.setVisibility(0);
            this.mPanelSearch.getEditText().requestFocus();
            this.mPanelSearchBar.setVisibility(8);
            this.mSearchBarDivideLine.setVisibility(8);
            this.mListContainer.setForeground(this.mDimmedForground);
        }
    }

    public void onKeyboardClosed() {
        this.isKeyboardOpen = false;
        String trim = this.mPanelSearch.getText().trim();
        if (!this.mDirectoryListView.hasData() || StringUtil.isEmptyOrNull(trim)) {
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelSearch.setVisibility(4);
            this.mPanelSearchBar.setVisibility(0);
            this.mSearchBarDivideLine.setVisibility(0);
            this.mPanelSearch.setText("");
        }
    }

    public void onBuddyListUpdate() {
        PBXDirectorySearchListView pBXDirectorySearchListView = this.mDirectoryListView;
        if (pBXDirectorySearchListView != null) {
            pBXDirectorySearchListView.onBuddyListUpdate();
        }
    }

    public void onBuddyInfoUpdate(List<String> list, List<String> list2) {
        PBXDirectorySearchListView pBXDirectorySearchListView = this.mDirectoryListView;
        if (pBXDirectorySearchListView != null) {
            pBXDirectorySearchListView.onBuddyInfoUpdate(list, list2);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (!StringUtil.isEmptyOrNull(this.mPanelSearch.getText().trim()) || !this.isKeyboardOpen) {
            Object itemAtPosition = this.mDirectoryListView.getItemAtPosition(i);
            if (itemAtPosition instanceof IMAddrBookItem) {
                int i2 = 0;
                if (getArguments() != null) {
                    i2 = getArguments().getInt(REQUEST_CODE, 0);
                }
                if (i2 != 0) {
                    PhoneLabelFragment.show(getFragmentManager(), (IMAddrBookItem) itemAtPosition, i2);
                } else {
                    PhoneLabelFragment.show(getFragmentManager(), (IMAddrBookItem) itemAtPosition);
                }
            }
            return;
        }
        UIUtil.closeSoftKeyboardInActivity((ZMActivity) getActivity());
    }

    @NonNull
    private List<String> getAllCallNumbers(IMAddrBookItem iMAddrBookItem) {
        ContactCloudSIP iCloudSIPCallNumber = iMAddrBookItem.getICloudSIPCallNumber();
        ArrayList arrayList = new ArrayList(5);
        if (iCloudSIPCallNumber != null) {
            String companyNumber = iCloudSIPCallNumber.getCompanyNumber();
            if (!StringUtil.isEmptyOrNull(companyNumber) && CmmSIPCallManager.getInstance().isCloudPBXEnabled() && (CmmSIPCallManager.getInstance().isSameCompanyWithLoginUser(companyNumber) || iMAddrBookItem.isSharedGlobalDirectory())) {
                arrayList.add(iCloudSIPCallNumber.getExtension());
            }
            ArrayList directNumber = iCloudSIPCallNumber.getDirectNumber();
            if (!CollectionsUtil.isListEmpty(directNumber)) {
                arrayList.addAll(directNumber);
            }
        }
        if (iMAddrBookItem.getContact() == null || CollectionsUtil.isListEmpty(iMAddrBookItem.getContact().accounts)) {
            return arrayList;
        }
        arrayList.addAll(filterSamePhoneNumber(iMAddrBookItem.getContact().accounts));
        return arrayList;
    }

    private List<String> filterSamePhoneNumber(List<ContactType> list) {
        ArrayList<PhoneNumber> arrayList = new ArrayList<>();
        for (ContactType contactType : list) {
            arrayList.addAll(contactType.phoneNumbers);
        }
        HashSet hashSet = new HashSet();
        for (PhoneNumber phoneNumber : arrayList) {
            hashSet.add(phoneNumber.normalizedNumber);
        }
        return new ArrayList(hashSet);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        CmmSIPCallManager.getInstance().removeListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
    }
}
