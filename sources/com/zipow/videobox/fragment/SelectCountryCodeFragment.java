package com.zipow.videobox.fragment;

import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMUtils;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class SelectCountryCodeFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnEditorActionListener {
    private static final String ARG_FILTER_COUNTRY_CODES = "supportCountryCodes";
    private static final String ARG_SUPPORT_SIP = "supportSip";
    public static final String RESULT_ARG_COUNTRY_CODE = "countryCode";
    private View mBtnCancel;
    private View mBtnClearSearchView;
    /* access modifiers changed from: private */
    public CountryCodeAdapter mCountryCodeAdapter;
    /* access modifiers changed from: private */
    public ListView mCountryCodeListView;
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
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = SelectCountryCodeFragment.this.mEdtSearch.getText().toString();
            SelectCountryCodeFragment.this.mCountryCodeAdapter.setFilter(obj);
            if ((obj.length() <= 0 || SelectCountryCodeFragment.this.mCountryCodeAdapter.getCount() <= 0) && SelectCountryCodeFragment.this.mPanelTitleBar.getVisibility() != 0) {
                SelectCountryCodeFragment.this.mListContainer.setForeground(SelectCountryCodeFragment.this.mDimmedForground);
            } else {
                SelectCountryCodeFragment.this.mListContainer.setForeground(null);
            }
        }
    };

    public static class CountryCodeAdapter extends BaseAdapter {
        private Context mContext;
        @Nullable
        private String mFilter;
        private ArrayList<CountryCodeItem> mFilterCountryCodes;
        @NonNull
        private List<CountryCodeItem> mList = new ArrayList();
        @NonNull
        private List<CountryCodeItem> mListFiltered = new ArrayList();
        private boolean mSupportSip;

        public long getItemId(int i) {
            return (long) i;
        }

        public CountryCodeAdapter(Context context, ArrayList<CountryCodeItem> arrayList, boolean z) {
            this.mContext = context;
            this.mFilterCountryCodes = arrayList;
            this.mSupportSip = z;
            loadAll();
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
                for (CountryCodeItem countryCodeItem : this.mList) {
                    if (countryCodeItem.countryName.toLowerCase(localDefault).contains(lowerCase) || countryCodeItem.countryCode.contains(lowerCase)) {
                        this.mListFiltered.add(countryCodeItem);
                    }
                }
            }
        }

        public void loadAll() {
            ArrayList<CountryCodeItem> arrayList = this.mFilterCountryCodes;
            if (arrayList == null || arrayList.isEmpty()) {
                loadAllDefault();
            } else if (this.mSupportSip) {
                loadAllForSupportSip();
            } else {
                loadAllForNoSupportSip();
            }
        }

        public int getCountryCodeIndex(@NonNull String str) {
            int i = 0;
            for (CountryCodeItem countryCodeItem : this.mList) {
                if (countryCodeItem.isoCountryCode.equals(str.toUpperCase(Locale.US))) {
                    return i;
                }
                i++;
            }
            return -1;
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
                view = View.inflate(this.mContext, C4558R.layout.zm_menu_item, null);
                view.setTag("dropdown");
            }
            bindView(i, view);
            return view;
        }

        private void bindView(int i, View view) {
            String str;
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgIcon);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            CountryCodeItem countryCodeItem = (CountryCodeItem) getItem(i);
            if (countryCodeItem != null) {
                if (countryCodeItem.callType == 999) {
                    str = countryCodeItem.countryName.replace("@", "");
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ZMUtils.getCountryName(countryCodeItem.isoCountryCode));
                    sb.append("(+");
                    sb.append(countryCodeItem.countryCode);
                    sb.append(")");
                    str = sb.toString();
                }
                textView.setText(str);
                view.setContentDescription(this.mContext.getString(countryCodeItem.isSelected ? C4558R.string.zm_accessibility_region_country_code_selected_46328 : C4558R.string.zm_accessibility_region_country_code_not_selected_46328, new Object[]{str}));
                imageView.setVisibility(8);
            }
        }

        private void loadAllForSupportSip() {
            String str;
            HashMap hashMap = new HashMap();
            Iterator it = this.mFilterCountryCodes.iterator();
            while (it.hasNext()) {
                CountryCodeItem countryCodeItem = (CountryCodeItem) it.next();
                if (!(countryCodeItem == null || countryCodeItem.isoCountryCode == null)) {
                    String str2 = countryCodeItem.isoCountryCode;
                    if (!hashMap.containsKey(str2)) {
                        hashMap.put(str2, str2);
                        if (ZMUtils.isSpecialCountryIdOrCode(str2)) {
                            str = ZMUtils.getCountryName(str2);
                        } else {
                            str = countryCodeItem.countryName;
                            if (StringUtil.isEmptyOrNull(str)) {
                                str = new Locale("", str2.toLowerCase(Locale.US)).getDisplayCountry();
                            }
                        }
                        CountryCodeItem countryCodeItem2 = new CountryCodeItem(countryCodeItem.countryCode, str2, str, countryCodeItem.number, countryCodeItem.displayNumber, countryCodeItem.callType);
                        countryCodeItem2.isSelected = countryCodeItem.isSelected;
                        this.mList.add(countryCodeItem2);
                    }
                }
            }
        }

        private void loadAllForNoSupportSip() {
            String str;
            HashMap hashMap = new HashMap();
            for (int i = 0; i < CountryCodeUtil.countryCodeTable.length; i++) {
                hashMap.put(CountryCodeUtil.countryCodeTable[i][0].toString(), CountryCodeUtil.countryCodeTable[i][1].toString());
            }
            HashMap hashMap2 = new HashMap();
            Iterator it = this.mFilterCountryCodes.iterator();
            while (it.hasNext()) {
                CountryCodeItem countryCodeItem = (CountryCodeItem) it.next();
                if (!(countryCodeItem == null || countryCodeItem.isoCountryCode == null)) {
                    String str2 = countryCodeItem.isoCountryCode;
                    if (hashMap.containsKey(str2) && !hashMap2.containsKey(str2)) {
                        hashMap2.put(str2, str2);
                        if (ZMUtils.isSpecialCountryIdOrCode(str2)) {
                            str = ZMUtils.getCountryName(str2);
                        } else {
                            str = countryCodeItem.countryName;
                            if (StringUtil.isEmptyOrNull(str)) {
                                str = new Locale("", str2.toLowerCase(Locale.US)).getDisplayCountry();
                            }
                        }
                        CountryCodeItem countryCodeItem2 = new CountryCodeItem(countryCodeItem.countryCode, str2, str, countryCodeItem.number, countryCodeItem.displayNumber, countryCodeItem.callType);
                        countryCodeItem2.isSelected = countryCodeItem.isSelected;
                        this.mList.add(countryCodeItem2);
                    }
                }
            }
        }

        private void loadAllDefault() {
            String str;
            for (int i = 0; i < CountryCodeUtil.countryCodeTable.length; i++) {
                String obj = CountryCodeUtil.countryCodeTable[i][0].toString();
                String obj2 = CountryCodeUtil.countryCodeTable[i][1].toString();
                Locale locale = new Locale("", obj.toLowerCase(Locale.US));
                if (ZMUtils.isSpecialCountryIdOrCode(obj)) {
                    str = ZMUtils.getCountryName(obj);
                } else {
                    str = locale.getDisplayCountry();
                }
                this.mList.add(new CountryCodeItem(obj2, obj, str));
            }
            Collections.sort(this.mList, new CountryCodeItemComparator(CompatUtils.getLocalDefault()));
        }
    }

    public static class CountryCodeItem implements Serializable {
        private static final long serialVersionUID = 1;
        public int callType;
        public String countryCode;
        public String countryName;
        public String displayNumber;
        public boolean isSelected;
        public String isoCountryCode;
        public String number;

        public CountryCodeItem(String str, String str2, String str3, String str4, String str5, int i) {
            this.isSelected = false;
            this.countryCode = str;
            this.isoCountryCode = str2;
            this.countryName = str3;
            this.number = str4;
            this.displayNumber = str5;
            this.callType = i;
        }

        public CountryCodeItem(String str, String str2, String str3) {
            this(str, str2, str3, "", "", 0);
        }

        @Nullable
        public static CountryCodeItem from(@Nullable CountryCodeItem countryCodeItem) {
            if (countryCodeItem == null) {
                return null;
            }
            CountryCodeItem countryCodeItem2 = new CountryCodeItem(countryCodeItem.countryCode, countryCodeItem.isoCountryCode, countryCodeItem.countryName, countryCodeItem.number, countryCodeItem.displayNumber, countryCodeItem.callType);
            return countryCodeItem2;
        }

        public static CountryCodeItem readFromPreference(String str) {
            int i;
            HashSet hashSet = new HashSet();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
            hashSet.add(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("isoCountryCode");
            hashSet.add(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append("countryName");
            hashSet.add(sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append("number");
            hashSet.add(sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append("displayNumber");
            hashSet.add(sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append(str);
            sb6.append("callType");
            hashSet.add(sb6.toString());
            HashMap readMapStringValues = PreferenceUtil.readMapStringValues(hashSet, null);
            if (readMapStringValues == null) {
                return null;
            }
            try {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str);
                sb7.append("callType");
                i = Integer.valueOf((String) readMapStringValues.get(sb7.toString())).intValue();
            } catch (Exception unused) {
                i = 0;
            }
            StringBuilder sb8 = new StringBuilder();
            sb8.append(str);
            sb8.append(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
            String str2 = (String) readMapStringValues.get(sb8.toString());
            StringBuilder sb9 = new StringBuilder();
            sb9.append(str);
            sb9.append("isoCountryCode");
            String str3 = (String) readMapStringValues.get(sb9.toString());
            StringBuilder sb10 = new StringBuilder();
            sb10.append(str);
            sb10.append("countryName");
            String str4 = (String) readMapStringValues.get(sb10.toString());
            StringBuilder sb11 = new StringBuilder();
            sb11.append(str);
            sb11.append("number");
            String str5 = (String) readMapStringValues.get(sb11.toString());
            StringBuilder sb12 = new StringBuilder();
            sb12.append(str);
            sb12.append("displayNumber");
            CountryCodeItem countryCodeItem = new CountryCodeItem(str2, str3, str4, str5, (String) readMapStringValues.get(sb12.toString()), i);
            return countryCodeItem;
        }

        public void savePreference(String str) {
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
            hashMap.put(sb.toString(), this.countryCode);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("isoCountryCode");
            hashMap.put(sb2.toString(), this.isoCountryCode);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append("countryName");
            hashMap.put(sb3.toString(), this.countryName);
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append("number");
            hashMap.put(sb4.toString(), this.number);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append("displayNumber");
            hashMap.put(sb5.toString(), this.displayNumber);
            StringBuilder sb6 = new StringBuilder();
            sb6.append(str);
            sb6.append("callType");
            hashMap.put(sb6.toString(), String.valueOf(this.callType));
            PreferenceUtil.saveMapStringValues(hashMap);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CountryCodeItem countryCodeItem = (CountryCodeItem) obj;
            if (this.callType != countryCodeItem.callType || !Objects.equals(this.countryCode, countryCodeItem.countryCode) || !Objects.equals(this.isoCountryCode, countryCodeItem.isoCountryCode) || !Objects.equals(this.countryName, countryCodeItem.countryName) || !Objects.equals(this.number, countryCodeItem.number) || !Objects.equals(this.displayNumber, countryCodeItem.displayNumber)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.countryCode, this.isoCountryCode, this.countryName, this.number, this.displayNumber, Integer.valueOf(this.callType)});
        }
    }

    static class CountryCodeItemComparator implements Comparator<CountryCodeItem> {
        private Collator mCollator;

        public CountryCodeItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull CountryCodeItem countryCodeItem, @NonNull CountryCodeItem countryCodeItem2) {
            if (countryCodeItem == countryCodeItem2) {
                return 0;
            }
            return this.mCollator.compare(countryCodeItem.countryName, countryCodeItem2.countryName);
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(Fragment fragment, int i) {
        showAsActivity(fragment, null, false, i);
    }

    public static void showAsActivity(@Nullable Fragment fragment, ArrayList<CountryCodeItem> arrayList, boolean z, int i) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_FILTER_COUNTRY_CODES, arrayList);
            bundle.putBoolean(ARG_SUPPORT_SIP, z);
            SimpleActivity.show(fragment, SelectCountryCodeFragment.class.getName(), bundle, i, true);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ArrayList arrayList;
        boolean z = false;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_country_code, viewGroup, false);
        this.mBtnCancel = inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mEdtSearchDummy = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mCountryCodeListView = (ListView) inflate.findViewById(C4558R.C4560id.countryCodeListView);
        this.mBtnClearSearchView = inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        FragmentActivity activity = getActivity();
        Bundle arguments = getArguments();
        if (arguments != null) {
            arrayList = (ArrayList) arguments.getSerializable(ARG_FILTER_COUNTRY_CODES);
            z = arguments.getBoolean(ARG_SUPPORT_SIP, false);
        } else {
            arrayList = null;
        }
        this.mCountryCodeAdapter = new CountryCodeAdapter(activity, arrayList, z);
        this.mCountryCodeListView.setAdapter(this.mCountryCodeAdapter);
        this.mCountryCodeListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Object itemAtPosition = SelectCountryCodeFragment.this.mCountryCodeListView.getItemAtPosition(i);
                if (itemAtPosition instanceof CountryCodeItem) {
                    SelectCountryCodeFragment.this.onSelectCountryCode((CountryCodeItem) itemAtPosition);
                }
            }
        });
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SelectCountryCodeFragment.this.mHandler.removeCallbacks(SelectCountryCodeFragment.this.mRunnableFilter);
                SelectCountryCodeFragment.this.mHandler.postDelayed(SelectCountryCodeFragment.this.mRunnableFilter, 300);
                SelectCountryCodeFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateBtnClearSearchView();
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
            this.mCountryCodeListView.post(new Runnable() {
                public void run() {
                    SelectCountryCodeFragment.this.mCountryCodeListView.requestLayout();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    /* access modifiers changed from: private */
    public void onSelectCountryCode(CountryCodeItem countryCodeItem) {
        if (!getShowsDialog()) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_ARG_COUNTRY_CODE, countryCodeItem);
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
        this.mCountryCodeAdapter.setFilter(null);
    }
}
