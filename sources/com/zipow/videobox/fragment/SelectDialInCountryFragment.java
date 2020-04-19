package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.util.ZMUtils;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public class SelectDialInCountryFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnEditorActionListener {
    private static final String ARG_DIALIN_COUNTRIES = "ARG_DIALIN_COUNTRIES";
    private static final String ARG_DIALIN_SELECT_COUNTRIES = "ARG_DIALIN_SELECT_COUNTRIES";
    public static final String RESULT_ARG_ADD_DIALIN_COUNTRIES = "RESULT_ARG_ADD_DIALIN_COUNTRIES";
    public static final String RESULT_ARG_MINUS_DIALIN_COUNTRIES = "RESULT_ARG_MINUS_DIALIN_COUNTRIES";
    private final ArrayList<String> mAddCountries = new ArrayList<>();
    private View mBtnClearSearchView;
    @Nullable
    private ArrayList<DialInCountry> mDialInCountries;
    /* access modifiers changed from: private */
    public DialInCountryAdapter mDialInCountryAdapter;
    /* access modifiers changed from: private */
    public QuickSearchListView mDialInListView;
    @Nullable
    private ArrayList<String> mDialInSelectedCountries;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    private EditText mEdtSearchDummy;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    private final ArrayList<String> mMinusCountries = new ArrayList<>();
    private View mPanelSearchBar;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    public final Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = SelectDialInCountryFragment.this.mEdtSearch.getText().toString();
            SelectDialInCountryFragment.this.mDialInCountryAdapter.setFilter(obj);
            if ((obj.length() <= 0 || SelectDialInCountryFragment.this.mDialInCountryAdapter.getCount() <= 0) && SelectDialInCountryFragment.this.mPanelTitleBar.getVisibility() != 0) {
                SelectDialInCountryFragment.this.mListContainer.setForeground(SelectDialInCountryFragment.this.mDimmedForground);
            } else {
                SelectDialInCountryFragment.this.mListContainer.setForeground(null);
            }
        }
    };

    public static class DialInCountry implements Parcelable {
        public static final Creator<DialInCountry> CREATOR = new Creator<DialInCountry>() {
            public DialInCountry createFromParcel(@NonNull Parcel parcel) {
                return new DialInCountry(parcel);
            }

            public DialInCountry[] newArray(int i) {
                return new DialInCountry[i];
            }
        };
        @Nullable
        public String country;
        @Nullable
        public String countryName;
        public boolean isSelected;
        /* access modifiers changed from: private */
        @Nullable
        public String sortKey;

        public int describeContents() {
            return 0;
        }

        public DialInCountry(@Nullable String str, boolean z) {
            if (str != null) {
                this.country = str;
                this.countryName = ZMUtils.getCountryName(str);
            }
            this.isSelected = z;
            this.sortKey = SortUtil.getSortKey(this.countryName, CompatUtils.getLocalDefault());
        }

        public void setCountry(@Nullable String str) {
            if (str != null) {
                this.country = str;
                this.countryName = ZMUtils.getCountryName(str);
            }
            this.sortKey = SortUtil.getSortKey(this.countryName, CompatUtils.getLocalDefault());
        }

        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(this.country);
            parcel.writeString(this.countryName);
            parcel.writeByte(this.isSelected ? (byte) 1 : 0);
            parcel.writeString(this.sortKey);
        }

        protected DialInCountry(Parcel parcel) {
            this.country = parcel.readString();
            this.countryName = parcel.readString();
            this.isSelected = parcel.readByte() != 0;
            this.sortKey = parcel.readString();
        }
    }

    public static class DialInCountryAdapter extends QuickSearchListDataAdapter {
        private final Context mContext;
        private String mFilter;
        private final SelectDialInCountryFragment mFragment;
        private final List<DialInCountry> mList = new ArrayList();
        private final List<DialInCountry> mListFiltered = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public DialInCountryAdapter(Context context, SelectDialInCountryFragment selectDialInCountryFragment) {
            this.mContext = context;
            this.mFragment = selectDialInCountryFragment;
            loadAll();
        }

        public void setFilter(String str) {
            this.mFilter = str;
            updateFilteredList();
            notifyDataSetChanged();
        }

        private void updateFilteredList() {
            this.mListFiltered.clear();
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                Locale localDefault = CompatUtils.getLocalDefault();
                String lowerCase = this.mFilter.toLowerCase(localDefault);
                for (DialInCountry dialInCountry : this.mList) {
                    if (!StringUtil.isEmptyOrNull(dialInCountry.countryName) && dialInCountry.countryName.toLowerCase(localDefault).contains(lowerCase)) {
                        this.mListFiltered.add(dialInCountry);
                    }
                }
            }
        }

        private void loadAll() {
            ArrayList access$1000 = this.mFragment.getmDialInCountries();
            if (access$1000 != null && !access$1000.isEmpty()) {
                this.mList.addAll(access$1000);
                Collections.sort(this.mList, new DialInCountryItemComparator(CompatUtils.getLocalDefault()));
            }
        }

        public void reloadAll() {
            this.mList.clear();
            ArrayList<DialInCountry> access$1000 = this.mFragment.getmDialInCountries();
            if (access$1000 != null && !access$1000.isEmpty()) {
                for (DialInCountry dialInCountry : access$1000) {
                    dialInCountry.setCountry(dialInCountry.country);
                }
                this.mList.addAll(access$1000);
                Collections.sort(this.mList, new DialInCountryItemComparator(CompatUtils.getLocalDefault()));
            }
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
                view = View.inflate(this.mContext, C4558R.layout.zm_select_dialin_country_item, null);
                view.setTag("dropdown");
            }
            bindView(i, view);
            return view;
        }

        private void bindView(int i, View view) {
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgSelected);
            DialInCountry dialInCountry = (DialInCountry) getItem(i);
            ((TextView) view.findViewById(C4558R.C4560id.txtCountryName)).setText(dialInCountry.countryName);
            imageView.setVisibility(dialInCountry.isSelected ? 0 : 4);
            if (!dialInCountry.isSelected || this.mFragment.canContinueUnSelectItem()) {
                view.setClickable(false);
                imageView.setAlpha(1.0f);
                return;
            }
            imageView.setAlpha(0.3f);
            view.setClickable(true);
        }

        @Nullable
        public String getItemSortKey(Object obj) {
            return ((DialInCountry) obj).sortKey;
        }

        public boolean isEnabled(int i) {
            DialInCountry dialInCountry = (DialInCountry) getItem(i);
            if (dialInCountry == null || !dialInCountry.isSelected || this.mFragment.canContinueUnSelectItem()) {
                return super.isEnabled(i);
            }
            return false;
        }
    }

    static class DialInCountryItemComparator implements Comparator<DialInCountry> {
        private final Collator mCollator;

        public DialInCountryItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull DialInCountry dialInCountry, @NonNull DialInCountry dialInCountry2) {
            if (dialInCountry == dialInCountry2) {
                return 0;
            }
            return this.mCollator.compare(dialInCountry.countryName, dialInCountry2.countryName);
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@Nullable Fragment fragment, int i, ArrayList<DialInCountry> arrayList, @Nullable List<String> list) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ARG_DIALIN_COUNTRIES, arrayList);
            ArrayList arrayList2 = new ArrayList();
            if (list != null && !list.isEmpty()) {
                arrayList2.addAll(list);
            }
            bundle.putStringArrayList(ARG_DIALIN_SELECT_COUNTRIES, arrayList2);
            SimpleActivity.show(fragment, SelectDialInCountryFragment.class.getName(), bundle, i, true, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ARG_DIALIN_COUNTRIES)) {
                this.mDialInCountries = arguments.getParcelableArrayList(ARG_DIALIN_COUNTRIES);
            }
            if (arguments.containsKey(ARG_DIALIN_SELECT_COUNTRIES)) {
                this.mDialInSelectedCountries = arguments.getStringArrayList(ARG_DIALIN_SELECT_COUNTRIES);
            }
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_dialin_country, viewGroup, false);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mEdtSearchDummy = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mDialInListView = (QuickSearchListView) inflate.findViewById(C4558R.C4560id.phoneNumberListView);
        this.mBtnClearSearchView = inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnOK).setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mDialInCountryAdapter = new DialInCountryAdapter(getActivity(), this);
        this.mDialInListView.setAdapter(this.mDialInCountryAdapter);
        this.mDialInListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SelectDialInCountryFragment.this.onDialInItemClick(i);
            }
        });
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SelectDialInCountryFragment.this.mHandler.removeCallbacks(SelectDialInCountryFragment.this.mRunnableFilter);
                SelectDialInCountryFragment.this.mHandler.postDelayed(SelectDialInCountryFragment.this.mRunnableFilter, 300);
                SelectDialInCountryFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateBtnClearSearchView();
        this.mDialInCountryAdapter.reloadAll();
        this.mDialInCountryAdapter.notifyDataSetChanged();
        this.mDialInListView.onResume();
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
            this.mDialInListView.post(new Runnable() {
                public void run() {
                    SelectDialInCountryFragment.this.mDialInListView.requestLayout();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    @Nullable
    public ArrayList<DialInCountry> getmDialInCountries() {
        return this.mDialInCountries;
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    /* access modifiers changed from: private */
    public void onDialInItemClick(int i) {
        Object itemAtPosition = this.mDialInListView.getItemAtPosition(i);
        if (itemAtPosition instanceof DialInCountry) {
            DialInCountry dialInCountry = (DialInCountry) itemAtPosition;
            if (!dialInCountry.isSelected || canContinueUnSelectItem()) {
                dialInCountry.isSelected = !dialInCountry.isSelected;
                if (dialInCountry.isSelected) {
                    this.mMinusCountries.remove(dialInCountry.country);
                    if (!this.mAddCountries.contains(dialInCountry.country)) {
                        ArrayList<String> arrayList = this.mDialInSelectedCountries;
                        if (arrayList != null && !arrayList.contains(dialInCountry.country)) {
                            this.mAddCountries.add(dialInCountry.country);
                        }
                    }
                } else {
                    this.mAddCountries.remove(dialInCountry.country);
                    if (!this.mMinusCountries.contains(dialInCountry.country)) {
                        ArrayList<String> arrayList2 = this.mDialInSelectedCountries;
                        if (arrayList2 != null && arrayList2.contains(dialInCountry.country)) {
                            this.mMinusCountries.add(dialInCountry.country);
                        }
                    }
                }
                this.mDialInCountryAdapter.notifyDataSetChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean canContinueUnSelectItem() {
        ArrayList<String> arrayList = this.mDialInSelectedCountries;
        return ((arrayList == null ? 0 : arrayList.size()) + this.mAddCountries.size()) - this.mMinusCountries.size() > 1;
    }

    public void dismiss() {
        if (((ZMActivity) getActivity()) != null) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        }
        finishFragment(true);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnCancel) {
            onClickBtnCancel();
        } else if (id == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
        } else if (id == C4558R.C4560id.btnOK) {
            onClickOk();
        }
    }

    private void onClickOk() {
        if (!getShowsDialog()) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_ARG_ADD_DIALIN_COUNTRIES, this.mAddCountries);
                intent.putExtra(RESULT_ARG_MINUS_DIALIN_COUNTRIES, this.mMinusCountries);
                zMActivity.setResult(-1, intent);
            } else {
                return;
            }
        }
        dismiss();
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void onClickBtnClearSearchView() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        this.mEdtSearch.setText("");
        this.mDialInCountryAdapter.setFilter(null);
    }
}
