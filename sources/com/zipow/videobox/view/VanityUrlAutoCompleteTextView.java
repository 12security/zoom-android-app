package com.zipow.videobox.view;

import android.content.Context;
import android.text.Editable;
import android.text.Editable.Factory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.CmmSavedMeeting;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class VanityUrlAutoCompleteTextView extends AutoCompleteTextView {
    private AutoCompleteAdapter mAdapter;

    private class AutoCompleteAdapter extends BaseAdapter implements Filterable {
        private Context mContext;
        private AutoCompleteFilter mFilter;
        @Nullable
        private LayoutInflater mInflater;
        /* access modifiers changed from: private */
        public final Object mLock = new Object();
        /* access modifiers changed from: private */
        public ArrayList<CmmSavedMeeting> mOriginalValues;
        private int mResource;
        /* access modifiers changed from: private */
        public List<CmmSavedMeeting> mValues;

        private class AutoCompleteFilter extends Filter {
            private AutoCompleteFilter() {
            }

            /* access modifiers changed from: protected */
            @NonNull
            public FilterResults performFiltering(@Nullable CharSequence charSequence) {
                ArrayList arrayList;
                ArrayList arrayList2;
                FilterResults filterResults = new FilterResults();
                if (AutoCompleteAdapter.this.mOriginalValues == null) {
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        AutoCompleteAdapter.this.mOriginalValues = new ArrayList(AutoCompleteAdapter.this.mValues);
                    }
                }
                if (charSequence == null || charSequence.length() == 0) {
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        arrayList = new ArrayList(AutoCompleteAdapter.this.mOriginalValues);
                    }
                    filterResults.values = arrayList;
                    filterResults.count = arrayList.size();
                } else {
                    String lowerCase = charSequence.toString().toLowerCase(CompatUtils.getLocalDefault());
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        arrayList2 = new ArrayList(AutoCompleteAdapter.this.mOriginalValues);
                    }
                    int size = arrayList2.size();
                    ArrayList arrayList3 = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        CmmSavedMeeting cmmSavedMeeting = (CmmSavedMeeting) arrayList2.get(i);
                        String str = cmmSavedMeeting.getmConfID();
                        if (!StringUtil.isEmptyOrNull(str) && str.toLowerCase(CompatUtils.getLocalDefault()).startsWith(lowerCase)) {
                            arrayList3.add(cmmSavedMeeting);
                        }
                    }
                    filterResults.values = arrayList3;
                    filterResults.count = arrayList3.size();
                }
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, @NonNull FilterResults filterResults) {
                AutoCompleteAdapter.this.mValues = (List) filterResults.values;
                if (filterResults.count > 0) {
                    AutoCompleteAdapter.this.notifyDataSetChanged();
                } else {
                    AutoCompleteAdapter.this.notifyDataSetInvalidated();
                }
            }
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public AutoCompleteAdapter(Context context, int i, List<CmmSavedMeeting> list) {
            init(context, i, list);
        }

        private void init(Context context, int i, List<CmmSavedMeeting> list) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            this.mResource = i;
            this.mValues = list;
        }

        public int getCount() {
            return this.mValues.size();
        }

        @Nullable
        public String getItem(int i) {
            CmmSavedMeeting objectItem = getObjectItem(i);
            if (objectItem == null) {
                return "";
            }
            return objectItem.getmConfID();
        }

        private CmmSavedMeeting getObjectItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            return (CmmSavedMeeting) this.mValues.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(this.mResource, viewGroup, false);
            }
            CmmSavedMeeting objectItem = getObjectItem(i);
            if (objectItem != null) {
                Editable newEditable = Factory.getInstance().newEditable(objectItem.getmConfID());
                TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtTopic);
                if (textView != null) {
                    textView.setText(newEditable.toString());
                }
                TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtId);
                if (textView2 != null) {
                    textView2.setVisibility(8);
                }
            }
            return view;
        }

        public Filter getFilter() {
            if (this.mFilter == null) {
                this.mFilter = new AutoCompleteFilter();
            }
            return this.mFilter;
        }
    }

    public VanityUrlAutoCompleteTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public VanityUrlAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public VanityUrlAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        ArrayList arrayList = new ArrayList();
        if (!isInEditMode()) {
            arrayList.addAll(ConfNumberMgr.loadVanityUrlFromDB());
        }
        this.mAdapter = new AutoCompleteAdapter(getContext(), C4558R.layout.zm_simple_dropdown_item_1line, arrayList);
        setAdapter(this.mAdapter);
    }

    public void clearHistory() {
        this.mAdapter = new AutoCompleteAdapter(getContext(), C4558R.layout.zm_simple_dropdown_item_1line, new ArrayList());
        setAdapter(this.mAdapter);
    }
}
