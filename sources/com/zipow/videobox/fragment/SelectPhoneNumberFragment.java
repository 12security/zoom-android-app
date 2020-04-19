package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public class SelectPhoneNumberFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnEditorActionListener, IABContactsCacheListener {
    private static final String ARG_FILTER_COUNTRY_CODES = "filterContryCodes";
    public static final String RESULT_ARG_PHONE_NUMBER = "phoneNumber";
    private View mBtnCancel;
    private View mBtnClearSearchView;
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
    public PhoneNumberAdapter mPhoneNumberAdapter;
    /* access modifiers changed from: private */
    public QuickSearchListView mPhoneNumberListView;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = SelectPhoneNumberFragment.this.mEdtSearch.getText().toString();
            SelectPhoneNumberFragment.this.mPhoneNumberAdapter.setFilter(obj);
            if ((obj.length() <= 0 || SelectPhoneNumberFragment.this.mPhoneNumberAdapter.getCount() <= 0) && SelectPhoneNumberFragment.this.mPanelTitleBar.getVisibility() != 0) {
                SelectPhoneNumberFragment.this.mListContainer.setForeground(SelectPhoneNumberFragment.this.mDimmedForground);
            } else {
                SelectPhoneNumberFragment.this.mListContainer.setForeground(null);
            }
        }
    };

    public static class PhoneNumberAdapter extends QuickSearchListDataAdapter {
        private Context mContext;
        @Nullable
        private String mFilter;
        private ArrayList<CountryCodeItem> mFilterCountryCodes;
        @NonNull
        private List<PhoneNumberItem> mList = new ArrayList();
        @NonNull
        private List<PhoneNumberItem> mListFiltered = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public PhoneNumberAdapter(Context context, ArrayList<CountryCodeItem> arrayList) {
            this.mContext = context;
            this.mFilterCountryCodes = arrayList;
        }

        public void setFilterCountryCodes(ArrayList<CountryCodeItem> arrayList) {
            this.mFilterCountryCodes = arrayList;
            reloadAll();
        }

        public void setFilter(@Nullable String str) {
            if (str != null) {
                str = str.trim();
            }
            this.mFilter = str;
            updateFilteredList();
            notifyDataSetChanged();
        }

        private void updateFilteredList() {
            this.mListFiltered.clear();
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                Locale localDefault = CompatUtils.getLocalDefault();
                String lowerCase = this.mFilter.toLowerCase(localDefault);
                for (PhoneNumberItem phoneNumberItem : this.mList) {
                    if (phoneNumberItem.contactName.toLowerCase(localDefault).contains(lowerCase) || phoneNumberItem.phoneNumber.contains(lowerCase)) {
                        this.mListFiltered.add(phoneNumberItem);
                    }
                }
            }
        }

        private void loadAll() {
            ABContactsCache instance = ABContactsCache.getInstance();
            int cachedContactsCount = instance.getCachedContactsCount();
            for (int i = 0; i < cachedContactsCount; i++) {
                Contact cachedContact = instance.getCachedContact(i);
                if (cachedContact != null) {
                    ArrayList<CountryCodeItem> arrayList = this.mFilterCountryCodes;
                    boolean z = true;
                    if (arrayList != null) {
                        Iterator it = arrayList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                z = false;
                                break;
                            }
                            CountryCodeItem countryCodeItem = (CountryCodeItem) it.next();
                            if (countryCodeItem != null && countryCodeItem.countryCode != null && countryCodeItem.countryCode.equalsIgnoreCase(cachedContact.normalizeCountryCode)) {
                                break;
                            }
                        }
                    }
                    if (z) {
                        this.mList.add(new PhoneNumberItem(cachedContact.displayName, cachedContact.number, cachedContact.normalizeCountryCode, cachedContact.normalizedNumber));
                    }
                }
            }
        }

        public void reloadAll() {
            this.mList.clear();
            loadAll();
        }

        public int getCount() {
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                return this.mListFiltered.size();
            }
            return this.mList.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                return this.mListFiltered.get(i);
            }
            return this.mList.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"dropdown".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_select_phone_number_item, null);
                view.setTag("dropdown");
            }
            bindView(i, view);
            return view;
        }

        private void bindView(int i, View view) {
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtNumber);
            PhoneNumberItem phoneNumberItem = (PhoneNumberItem) getItem(i);
            textView.setText(phoneNumberItem.contactName);
            textView2.setText(phoneNumberItem.phoneNumber);
        }

        public String getItemSortKey(Object obj) {
            return ((PhoneNumberItem) obj).sortKey;
        }
    }

    public static class PhoneNumberItem implements Serializable {
        private static final long serialVersionUID = 1;
        public String contactName;
        public String countryCode;
        public String normalizedNumber;
        public String phoneNumber;
        /* access modifiers changed from: private */
        public String sortKey;

        public PhoneNumberItem(String str, String str2, String str3, String str4) {
            this.contactName = str;
            this.phoneNumber = str2;
            this.countryCode = str3;
            this.normalizedNumber = str4;
            this.sortKey = SortUtil.getSortKey(str, CompatUtils.getLocalDefault());
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(Fragment fragment, int i) {
        showAsActivity(fragment, null, i);
    }

    public static void showAsActivity(@Nullable Fragment fragment, ArrayList<CountryCodeItem> arrayList, int i) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_FILTER_COUNTRY_CODES, arrayList);
            SimpleActivity.show(fragment, SelectPhoneNumberFragment.class.getName(), bundle, i, true, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ArrayList arrayList = null;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_phone_number, null);
        this.mBtnCancel = inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mEdtSearchDummy = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mPhoneNumberListView = (QuickSearchListView) inflate.findViewById(C4558R.C4560id.phoneNumberListView);
        this.mBtnClearSearchView = inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        FragmentActivity activity = getActivity();
        Bundle arguments = getArguments();
        if (arguments != null) {
            arrayList = (ArrayList) arguments.getSerializable(ARG_FILTER_COUNTRY_CODES);
        }
        this.mPhoneNumberAdapter = new PhoneNumberAdapter(activity, arrayList);
        this.mPhoneNumberListView.setAdapter(this.mPhoneNumberAdapter);
        this.mPhoneNumberListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Object itemAtPosition = SelectPhoneNumberFragment.this.mPhoneNumberListView.getItemAtPosition(i);
                if (itemAtPosition instanceof PhoneNumberItem) {
                    SelectPhoneNumberFragment.this.onSelectPhoneNumber((PhoneNumberItem) itemAtPosition);
                }
            }
        });
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SelectPhoneNumberFragment.this.mHandler.removeCallbacks(SelectPhoneNumberFragment.this.mRunnableFilter);
                SelectPhoneNumberFragment.this.mHandler.postDelayed(SelectPhoneNumberFragment.this.mRunnableFilter, 300);
                SelectPhoneNumberFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateBtnClearSearchView();
        ABContactsCache.getInstance().addListener(this);
        reloadAll();
        this.mPhoneNumberAdapter.notifyDataSetChanged();
        this.mPhoneNumberListView.onResume();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_CONTACTS".equals(strArr[i2]) && iArr[i2] != 0) {
                    dismiss();
                    return;
                }
            }
            final int i3 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C29794 r2 = new EventAction("SelectPhonePermissionResult") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((SelectPhoneNumberFragment) iUIElement).handleRequestPermissionResult(i3, strArr2, iArr2);
                }
            };
            getNonNullEventTaskManagerOrThrowException().pushLater("SelectPhonePermissionResult", r2);
        }
    }

    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_CONTACTS".equals(strArr[i2]) && iArr[i2] == 0) {
                    ABContactsCache instance = ABContactsCache.getInstance();
                    if (instance.needReloadAll()) {
                        instance.reloadAllContacts(true);
                    }
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        ABContactsCache.getInstance().removeListener(this);
    }

    public void onContactsCacheUpdated() {
        reloadAll();
        this.mPhoneNumberAdapter.notifyDataSetChanged();
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
        if (this.mEdtSearch != null) {
            this.mEdtSearchDummy.setVisibility(0);
            this.mPanelSearchBar.setVisibility(4);
            this.mListContainer.setForeground(null);
            this.mPanelTitleBar.setVisibility(0);
            this.mPhoneNumberListView.post(new Runnable() {
                public void run() {
                    SelectPhoneNumberFragment.this.mPhoneNumberListView.requestLayout();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    /* access modifiers changed from: private */
    public void onSelectPhoneNumber(PhoneNumberItem phoneNumberItem) {
        if (!getShowsDialog()) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                Intent intent = new Intent();
                intent.putExtra("phoneNumber", phoneNumberItem);
                zMActivity.setResult(-1, intent);
            } else {
                return;
            }
        }
        dismiss();
    }

    public void dismiss() {
        if (((ZMActivity) getActivity()) != null) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        }
        finishFragment(true);
    }

    public void onClick(View view) {
        if (view == this.mBtnCancel) {
            onClickBtnCancel();
        } else if (view == this.mBtnClearSearchView) {
            onClickBtnClearSearchView();
        }
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void onClickBtnClearSearchView() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        this.mEdtSearch.setText("");
        this.mPhoneNumberAdapter.setFilter(null);
    }

    public boolean hasReadContactsPermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_CONTACTS") == 0;
    }

    public void requestContactsPermission() {
        zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
    }

    private void reloadAll() {
        ABContactsCache instance = ABContactsCache.getInstance();
        if (!hasReadContactsPermission()) {
            requestContactsPermission();
        } else if (instance.needReloadAll()) {
            instance.reloadAllContacts();
        }
        this.mPhoneNumberAdapter.reloadAll();
    }
}
