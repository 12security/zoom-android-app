package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.ZMUtils;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public abstract class SelectCallInCountryFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnEditorActionListener {
    public static final String RESULT_ARG_CALLIN_NUMBER = "phoneNumber";
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
    public CallInNumberAdapter mPhoneNumberAdapter;
    /* access modifiers changed from: private */
    public QuickSearchListView mPhoneNumberListView;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = SelectCallInCountryFragment.this.mEdtSearch.getText().toString();
            SelectCallInCountryFragment.this.mPhoneNumberAdapter.setFilter(obj);
            if ((obj.length() <= 0 || SelectCallInCountryFragment.this.mPhoneNumberAdapter.getCount() <= 0) && SelectCallInCountryFragment.this.mPanelTitleBar.getVisibility() != 0) {
                SelectCallInCountryFragment.this.mListContainer.setForeground(SelectCallInCountryFragment.this.mDimmedForground);
            } else {
                SelectCallInCountryFragment.this.mListContainer.setForeground(null);
            }
        }
    };

    public static class CallInNumberAdapter extends QuickSearchListDataAdapter {
        private Context mContext;
        private String mFilter;
        private SelectCallInCountryFragment mFragment;
        @NonNull
        private List<CallInNumberItem> mList = new ArrayList();
        @NonNull
        private List<CallInNumberItem> mListFiltered = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public CallInNumberAdapter(Context context, SelectCallInCountryFragment selectCallInCountryFragment) {
            this.mContext = context;
            this.mFragment = selectCallInCountryFragment;
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
                for (CallInNumberItem callInNumberItem : this.mList) {
                    if (callInNumberItem.countryName.toLowerCase(localDefault).contains(lowerCase) || callInNumberItem.countryCode.contains(lowerCase)) {
                        this.mListFiltered.add(callInNumberItem);
                    }
                }
            }
        }

        private void loadAll() {
            HashMap hashMap = new HashMap();
            this.mFragment.loadAllCountryCodes(hashMap);
            for (Entry value : hashMap.entrySet()) {
                CallInNumberItem callInNumberItem = (CallInNumberItem) value.getValue();
                if (callInNumberItem != null) {
                    this.mList.add(callInNumberItem);
                }
            }
            Collections.sort(this.mList, new CallInNumberItemComparator(CompatUtils.getLocalDefault()));
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
                view = View.inflate(this.mContext, C4558R.layout.zm_select_callin_country_item, null);
                view.setTag("dropdown");
            }
            bindView(i, view);
            return view;
        }

        private void bindView(int i, View view) {
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtCountryName);
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgCountryFlag);
            CallInNumberItem callInNumberItem = (CallInNumberItem) getItem(i);
            if (callInNumberItem != null) {
                if (ZMUtils.isSpecialCountryIdOrCode(callInNumberItem.countryCode)) {
                    textView.setText(ZMUtils.getCountryName(callInNumberItem.countryCode));
                } else {
                    textView.setText(callInNumberItem.countryName);
                }
                String lowerCase = callInNumberItem.countryId.toLowerCase(Locale.US);
                Resources resources = view.getResources();
                StringBuilder sb = new StringBuilder();
                sb.append("zm_flag_");
                sb.append(lowerCase);
                int identifier = resources.getIdentifier(sb.toString(), "drawable", VideoBoxApplication.getNonNullInstance().getPackageName());
                if (identifier != 0) {
                    imageView.setVisibility(0);
                    imageView.setImageResource(identifier);
                } else {
                    imageView.setVisibility(4);
                }
            }
        }

        public String getItemSortKey(Object obj) {
            return ((CallInNumberItem) obj).sortKey;
        }
    }

    public static class CallInNumberItem implements Serializable {
        private static final long serialVersionUID = 1;
        @NonNull
        public String countryCode = "";
        @NonNull
        public String countryId = "";
        @NonNull
        public String countryName = "";
        /* access modifiers changed from: private */
        public String sortKey;

        public CallInNumberItem(@Nullable String str, @Nullable String str2, @Nullable String str3) {
            if (str != null) {
                this.countryName = str;
            }
            if (str2 != null) {
                this.countryCode = str2;
            }
            if (str3 != null) {
                this.countryId = str3;
                String displayCountry = new Locale("", str3).getDisplayCountry();
                if (!StringUtil.isEmptyOrNull(displayCountry)) {
                    this.countryName = displayCountry;
                }
            }
            this.sortKey = SortUtil.getSortKey(this.countryName, CompatUtils.getLocalDefault());
        }
    }

    static class CallInNumberItemComparator implements Comparator<CallInNumberItem> {
        private Collator mCollator;

        public CallInNumberItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull CallInNumberItem callInNumberItem, @NonNull CallInNumberItem callInNumberItem2) {
            if (callInNumberItem == callInNumberItem2) {
                return 0;
            }
            return this.mCollator.compare(callInNumberItem.countryName, callInNumberItem2.countryName);
        }
    }

    public abstract void loadAllCountryCodes(Map<String, CallInNumberItem> map);

    public boolean onBackPressed() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_callin_country, null);
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
        this.mPhoneNumberAdapter = new CallInNumberAdapter(getActivity(), this);
        this.mPhoneNumberListView.setAdapter(this.mPhoneNumberAdapter);
        this.mPhoneNumberListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Object itemAtPosition = SelectCallInCountryFragment.this.mPhoneNumberListView.getItemAtPosition(i);
                if (itemAtPosition instanceof CallInNumberItem) {
                    SelectCallInCountryFragment.this.onSelectPhoneNumber((CallInNumberItem) itemAtPosition);
                }
            }
        });
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SelectCallInCountryFragment.this.mHandler.removeCallbacks(SelectCallInCountryFragment.this.mRunnableFilter);
                SelectCallInCountryFragment.this.mHandler.postDelayed(SelectCallInCountryFragment.this.mRunnableFilter, 300);
                SelectCallInCountryFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateBtnClearSearchView();
        this.mPhoneNumberAdapter.reloadAll();
        this.mPhoneNumberAdapter.notifyDataSetChanged();
        this.mPhoneNumberListView.onResume();
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
                    SelectCallInCountryFragment.this.mPhoneNumberListView.requestLayout();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    /* access modifiers changed from: protected */
    public void onSelectPhoneNumber(CallInNumberItem callInNumberItem) {
        if (!getShowsDialog()) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                Intent intent = new Intent();
                intent.putExtra("phoneNumber", callInNumberItem);
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
}
