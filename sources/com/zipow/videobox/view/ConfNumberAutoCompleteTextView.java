package com.zipow.videobox.view;

import android.content.Context;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
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
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfNumberAutoCompleteTextView extends AutoCompleteTextView {
    public static final int FORMAT_TYPE_34X = 1;
    public static final int FORMAT_TYPE_43X = 2;
    public static final int FORMAT_TYPE_DEFAULT = 0;
    @NonNull
    private static MyKeyListener mKeyListener = new MyKeyListener();
    /* access modifiers changed from: private */
    public int mFormatType = 0;
    private TextWatcher mTextChangedListener;

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
                    String replaceAll = charSequence.toString().replaceAll("\\D", "");
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        arrayList2 = new ArrayList(AutoCompleteAdapter.this.mOriginalValues);
                    }
                    int size = arrayList2.size();
                    ArrayList arrayList3 = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        CmmSavedMeeting cmmSavedMeeting = (CmmSavedMeeting) arrayList2.get(i);
                        String str = cmmSavedMeeting.getmConfID();
                        if (!StringUtil.isEmptyOrNull(str) && str.startsWith(replaceAll)) {
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

        @NonNull
        public String getItem(int i) {
            CmmSavedMeeting objectItem = getObjectItem(i);
            if (objectItem == null) {
                return "";
            }
            String str = objectItem.getmConfID();
            if (str == null) {
                return "";
            }
            Editable newEditable = Factory.getInstance().newEditable(str);
            ConfNumberMgr.formatText(newEditable, ConfNumberAutoCompleteTextView.this.mFormatType);
            return newEditable.toString();
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
                ConfNumberMgr.formatText(newEditable, ConfNumberAutoCompleteTextView.this.mFormatType);
                TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtTopic);
                ((TextView) view.findViewById(C4558R.C4560id.txtId)).setText(newEditable.toString());
                textView.setText(objectItem.getmConfTopic());
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

    private static class MyKeyListener extends DigitsKeyListener {
        private static final char[] mAcceptedChars = "0123456789 ".toCharArray();

        public MyKeyListener() {
            super(false, false);
        }

        /* access modifiers changed from: protected */
        @NonNull
        public char[] getAcceptedChars() {
            return mAcceptedChars;
        }
    }

    public ConfNumberAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    public ConfNumberAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ConfNumberAutoCompleteTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setKeyListener(mKeyListener);
        this.mTextChangedListener = new TextWatcher() {
            boolean bAppend = false;
            boolean bDelete = false;
            boolean bInsert = false;
            int bStart = 0;

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(@NonNull CharSequence charSequence, int i, int i2, int i3) {
                boolean z = true;
                this.bAppend = charSequence.length() == i && i2 == 0;
                this.bDelete = i2 > 0 && i3 == 0;
                if (charSequence.length() <= i || i2 != 0) {
                    z = false;
                }
                this.bInsert = z;
                this.bStart = i;
            }

            public void afterTextChanged(@NonNull Editable editable) {
                ConfNumberAutoCompleteTextView.this.removeTextChangedListener(this);
                int selectionEnd = Selection.getSelectionEnd(editable);
                Editable editableText = ConfNumberAutoCompleteTextView.this.getEditableText();
                ConfNumberMgr.formatText(editableText, ConfNumberAutoCompleteTextView.this.mFormatType);
                int selectionEnd2 = Selection.getSelectionEnd(editableText);
                if (selectionEnd2 > 0 && selectionEnd2 <= editableText.length()) {
                    if (this.bDelete && editableText.charAt(selectionEnd2 - 1) == ' ' && editable.charAt(selectionEnd - 1) != ' ') {
                        selectionEnd2--;
                    }
                    if (this.bInsert && this.bStart < selectionEnd2 && editableText.charAt(selectionEnd2 - 1) == ' ') {
                        selectionEnd2--;
                    }
                }
                Selection.setSelection(editableText, selectionEnd2);
                ConfNumberAutoCompleteTextView.this.addTextChangedListener(this);
            }
        };
        addTextChangedListener(this.mTextChangedListener);
        ArrayList arrayList = new ArrayList();
        if (!isInEditMode()) {
            arrayList.addAll(ConfNumberMgr.loadConfNumberFromDB());
        }
        setAdapter(new AutoCompleteAdapter(getContext(), C4558R.layout.zm_simple_dropdown_item_1line, arrayList));
    }

    public void clearHistory() {
        setAdapter(new AutoCompleteAdapter(getContext(), C4558R.layout.zm_simple_dropdown_item_1line, new ArrayList()));
    }

    public void setFormatType(int i) {
        this.mFormatType = i;
        TextWatcher textWatcher = this.mTextChangedListener;
        if (textWatcher != null) {
            removeTextChangedListener(textWatcher);
        }
        ConfNumberMgr.formatText(getEditableText(), this.mFormatType);
        TextWatcher textWatcher2 = this.mTextChangedListener;
        if (textWatcher2 != null) {
            addTextChangedListener(textWatcher2);
        }
    }

    public int getFormatType() {
        return this.mFormatType;
    }
}
