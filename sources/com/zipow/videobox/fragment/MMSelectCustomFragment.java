package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.fragment.MMSelectContactsFragment.GuestureListener;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;
import com.zipow.videobox.view.MMSelectCustomListItemSpan;
import com.zipow.videobox.view.ZMReplaceSpanMovementMethod;
import com.zipow.videobox.view.p014mm.MMSelectCustomListView;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMEditText;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.videomeetings.C4558R;

public class MMSelectCustomFragment extends ZMDialogFragment implements OnItemClickListener, OnClickListener, KeyboardListener {
    private static final String ARGS_IS_MULT_SELECT = "isMultSelect";
    private static final String ARGS_PRE_SEELECTS = "preSelects";
    private static final String ARGS_RESULT_DATA = "resultData";
    private static final String ARGS_TITLE = "title";
    private static final String ARG_SELECT_LIST_DATA = "selectData";
    public static final String RESULT_SELECT_ITEMS = "selectItems";
    private Button mBtnOK;
    /* access modifiers changed from: private */
    public ZMEditText mEdtSearch;
    /* access modifiers changed from: private */
    @Nullable
    public GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private boolean mIsMultSelect = false;
    @NonNull
    private SearchFilterRunnable mRunnableFilter = new SearchFilterRunnable();
    /* access modifiers changed from: private */
    public MMSelectCustomListView mSelectCustomListView;
    private TextView mTxtTitle;
    @Nullable
    private Bundle resultData;

    public class SearchFilterRunnable implements Runnable {
        @NonNull
        private String mKey = "";

        public SearchFilterRunnable() {
        }

        public void setKey(@Nullable String str) {
            if (str == null) {
                str = "";
            }
            this.mKey = str;
        }

        @NonNull
        public String getKey() {
            return this.mKey;
        }

        public void run() {
            MMSelectCustomFragment.this.mSelectCustomListView.setFilter(this.mKey);
        }
    }

    public static void showAsActivity(Fragment fragment, boolean z, List<IMessageTemplateSelectItem> list, List<IMessageTemplateSelectItem> list2, String str, int i) {
        showAsActivity(fragment, z, list, list2, str, i, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0044 A[SYNTHETIC, Splitter:B:21:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x008c A[SYNTHETIC, Splitter:B:48:0x008c] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x001a A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0062 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void showAsActivity(@androidx.annotation.Nullable androidx.fragment.app.Fragment r6, boolean r7, @androidx.annotation.Nullable java.util.List<com.zipow.videobox.tempbean.IMessageTemplateSelectItem> r8, @androidx.annotation.Nullable java.util.List<com.zipow.videobox.tempbean.IMessageTemplateSelectItem> r9, java.lang.String r10, int r11, @androidx.annotation.Nullable android.os.Bundle r12) {
        /*
            if (r6 != 0) goto L_0x0003
            return
        L_0x0003:
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            r1 = 0
            if (r8 == 0) goto L_0x0051
            boolean r2 = r8.isEmpty()
            if (r2 != 0) goto L_0x0051
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.util.Iterator r8 = r8.iterator()
        L_0x001a:
            boolean r3 = r8.hasNext()
            if (r3 == 0) goto L_0x004c
            java.lang.Object r3 = r8.next()
            com.zipow.videobox.tempbean.IMessageTemplateSelectItem r3 = (com.zipow.videobox.tempbean.IMessageTemplateSelectItem) r3
            java.io.StringWriter r4 = new java.io.StringWriter     // Catch:{ Exception -> 0x0048, all -> 0x0041 }
            r4.<init>()     // Catch:{ Exception -> 0x0048, all -> 0x0041 }
            com.google.gson.stream.JsonWriter r5 = new com.google.gson.stream.JsonWriter     // Catch:{ Exception -> 0x0048, all -> 0x0041 }
            r5.<init>(r4)     // Catch:{ Exception -> 0x0048, all -> 0x0041 }
            r3.writeJson(r5)     // Catch:{ Exception -> 0x0049, all -> 0x003e }
            java.lang.String r3 = r4.toString()     // Catch:{ Exception -> 0x0049, all -> 0x003e }
            r2.add(r3)     // Catch:{ Exception -> 0x0049, all -> 0x003e }
        L_0x003a:
            r5.close()     // Catch:{ IOException -> 0x001a }
            goto L_0x001a
        L_0x003e:
            r6 = move-exception
            r1 = r5
            goto L_0x0042
        L_0x0041:
            r6 = move-exception
        L_0x0042:
            if (r1 == 0) goto L_0x0047
            r1.close()     // Catch:{ IOException -> 0x0047 }
        L_0x0047:
            throw r6
        L_0x0048:
            r5 = r1
        L_0x0049:
            if (r5 == 0) goto L_0x001a
            goto L_0x003a
        L_0x004c:
            java.lang.String r8 = "preSelects"
            r0.putStringArrayList(r8, r2)
        L_0x0051:
            if (r9 == 0) goto L_0x0099
            boolean r8 = r9.isEmpty()
            if (r8 != 0) goto L_0x0099
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
            java.util.Iterator r9 = r9.iterator()
        L_0x0062:
            boolean r2 = r9.hasNext()
            if (r2 == 0) goto L_0x0094
            java.lang.Object r2 = r9.next()
            com.zipow.videobox.tempbean.IMessageTemplateSelectItem r2 = (com.zipow.videobox.tempbean.IMessageTemplateSelectItem) r2
            java.io.StringWriter r3 = new java.io.StringWriter     // Catch:{ Exception -> 0x0090, all -> 0x0089 }
            r3.<init>()     // Catch:{ Exception -> 0x0090, all -> 0x0089 }
            com.google.gson.stream.JsonWriter r4 = new com.google.gson.stream.JsonWriter     // Catch:{ Exception -> 0x0090, all -> 0x0089 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x0090, all -> 0x0089 }
            r2.writeJson(r4)     // Catch:{ Exception -> 0x0091, all -> 0x0086 }
            java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x0091, all -> 0x0086 }
            r8.add(r2)     // Catch:{ Exception -> 0x0091, all -> 0x0086 }
        L_0x0082:
            r4.close()     // Catch:{ IOException -> 0x0062 }
            goto L_0x0062
        L_0x0086:
            r6 = move-exception
            r1 = r4
            goto L_0x008a
        L_0x0089:
            r6 = move-exception
        L_0x008a:
            if (r1 == 0) goto L_0x008f
            r1.close()     // Catch:{ IOException -> 0x008f }
        L_0x008f:
            throw r6
        L_0x0090:
            r4 = r1
        L_0x0091:
            if (r4 == 0) goto L_0x0062
            goto L_0x0082
        L_0x0094:
            java.lang.String r9 = "selectData"
            r0.putStringArrayList(r9, r8)
        L_0x0099:
            java.lang.String r8 = "isMultSelect"
            r0.putBoolean(r8, r7)
            java.lang.String r7 = "title"
            r0.putString(r7, r10)
            if (r12 == 0) goto L_0x00aa
            java.lang.String r7 = "resultData"
            r0.putBundle(r7, r12)
        L_0x00aa:
            java.lang.Class<com.zipow.videobox.fragment.MMSelectCustomFragment> r7 = com.zipow.videobox.fragment.MMSelectCustomFragment.class
            java.lang.String r7 = r7.getName()
            r8 = 1
            com.zipow.videobox.SimpleActivity.show(r6, r7, r0, r11, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMSelectCustomFragment.showAsActivity(androidx.fragment.app.Fragment, boolean, java.util.List, java.util.List, java.lang.String, int, android.os.Bundle):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0052 A[SYNTHETIC, Splitter:B:22:0x0052] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0058 A[SYNTHETIC, Splitter:B:28:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0064  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onItemClick(android.widget.AdapterView<?> r3, android.view.View r4, int r5, long r6) {
        /*
            r2 = this;
            com.zipow.videobox.view.mm.MMSelectCustomListView r3 = r2.mSelectCustomListView
            com.zipow.videobox.tempbean.IMessageTemplateSelectItem r3 = r3.getItem(r5)
            if (r3 != 0) goto L_0x0009
            return
        L_0x0009:
            boolean r4 = r2.mIsMultSelect
            if (r4 == 0) goto L_0x001f
            com.zipow.videobox.view.mm.MMSelectCustomListView r4 = r2.mSelectCustomListView
            r4.onItemClicked(r3)
            com.zipow.videobox.view.mm.MMSelectCustomListView r4 = r2.mSelectCustomListView
            boolean r4 = r4.isItemSelected(r3)
            r2.onSelected(r4, r3)
            r2.updateButtonOK()
            goto L_0x006e
        L_0x001f:
            androidx.fragment.app.FragmentActivity r4 = r2.getActivity()
            if (r4 == 0) goto L_0x006b
            android.content.Intent r5 = new android.content.Intent
            r5.<init>()
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r7 = 0
            java.io.StringWriter r0 = new java.io.StringWriter     // Catch:{ Exception -> 0x0056, all -> 0x004f }
            r0.<init>()     // Catch:{ Exception -> 0x0056, all -> 0x004f }
            com.google.gson.stream.JsonWriter r1 = new com.google.gson.stream.JsonWriter     // Catch:{ Exception -> 0x0056, all -> 0x004f }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0056, all -> 0x004f }
            r3.writeJson(r1)     // Catch:{ Exception -> 0x004d, all -> 0x004a }
            java.lang.String r3 = r0.toString()     // Catch:{ Exception -> 0x004d, all -> 0x004a }
            r6.add(r3)     // Catch:{ Exception -> 0x004d, all -> 0x004a }
            r1.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x005b
        L_0x0048:
            goto L_0x005b
        L_0x004a:
            r3 = move-exception
            r7 = r1
            goto L_0x0050
        L_0x004d:
            r7 = r1
            goto L_0x0056
        L_0x004f:
            r3 = move-exception
        L_0x0050:
            if (r7 == 0) goto L_0x0055
            r7.close()     // Catch:{ IOException -> 0x0055 }
        L_0x0055:
            throw r3
        L_0x0056:
            if (r7 == 0) goto L_0x005b
            r7.close()     // Catch:{ IOException -> 0x0048 }
        L_0x005b:
            java.lang.String r3 = "selectItems"
            r5.putStringArrayListExtra(r3, r6)
            android.os.Bundle r3 = r2.resultData
            if (r3 == 0) goto L_0x0067
            r5.putExtras(r3)
        L_0x0067:
            r3 = -1
            r4.setResult(r3, r5)
        L_0x006b:
            r2.dismiss()
        L_0x006e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMSelectCustomFragment.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
    }

    public void onKeyboardOpen() {
        this.mEdtSearch.setCursorVisible(true);
    }

    public void onKeyboardClosed() {
        this.mEdtSearch.setCursorVisible(false);
        this.mHandler.post(new Runnable() {
            public void run() {
                MMSelectCustomFragment.this.mEdtSearch.requestLayout();
            }
        });
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    private void updateButtonOK() {
        if (this.mIsMultSelect) {
            this.mBtnOK.setEnabled(getSelectedItemCount() > 0);
        } else {
            this.mBtnOK.setVisibility(8);
        }
    }

    private int getSelectedItemCount() {
        return this.mSelectCustomListView.getSelectedItems().size();
    }

    private void updateUI() {
        updateButtonOK();
    }

    public void onResume() {
        super.onResume();
        updateUI();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (MMSelectCustomFragment.this.isResumed()) {
                    MMSelectCustomFragment.this.mEdtSearch.requestFocus();
                    UIUtil.openSoftKeyboard(MMSelectCustomFragment.this.getActivity(), MMSelectCustomFragment.this.mEdtSearch);
                }
            }
        }, 100);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_custom, viewGroup, false);
        this.mSelectCustomListView = (MMSelectCustomListView) inflate.findViewById(C4558R.C4560id.listView);
        this.mEdtSearch = (ZMEditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnOK = (Button) inflate.findViewById(C4558R.C4560id.btnOK);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mSelectCustomListView.setOnItemClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mBtnOK.setOnClickListener(this);
        this.mEdtSearch.setOnClickListener(this);
        this.mEdtSearch.setSelected(true);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (i3 < i2) {
                    final MMSelectCustomListItemSpan[] mMSelectCustomListItemSpanArr = (MMSelectCustomListItemSpan[]) MMSelectCustomFragment.this.mEdtSearch.getText().getSpans(i3 + i, i + i2, MMSelectCustomListItemSpan.class);
                    if (mMSelectCustomListItemSpanArr.length > 0) {
                        MMSelectCustomFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                if (MMSelectCustomFragment.this.isResumed()) {
                                    for (MMSelectCustomListItemSpan item : mMSelectCustomListItemSpanArr) {
                                        IMessageTemplateSelectItem item2 = item.getItem();
                                        if (item2 != null) {
                                            MMSelectCustomFragment.this.mSelectCustomListView.unsesectItem(item2);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                MMSelectCustomFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (MMSelectCustomFragment.this.isResumed()) {
                            MMSelectCustomFragment.this.formatSearchEditText();
                            MMSelectCustomFragment.this.startFilter(MMSelectCustomFragment.this.getFilter());
                        }
                    }
                });
            }
        });
        this.mEdtSearch.setMovementMethod(ZMReplaceSpanMovementMethod.getInstance());
        this.mEdtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, @NonNull KeyEvent keyEvent) {
                return i == 6 || (keyEvent.getKeyCode() == 66 && keyEvent.getAction() == 0);
            }
        });
        this.mGestureDetector = new GestureDetector(getActivity(), new GuestureListener(this.mSelectCustomListView, this.mEdtSearch));
        this.mSelectCustomListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return MMSelectCustomFragment.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mIsMultSelect = arguments.getBoolean(ARGS_IS_MULT_SELECT);
            ArrayList stringArrayList = arguments.getStringArrayList(ARGS_PRE_SEELECTS);
            ArrayList stringArrayList2 = arguments.getStringArrayList(ARG_SELECT_LIST_DATA);
            this.resultData = arguments.getBundle("resultData");
            this.mSelectCustomListView.setIsMultSelect(this.mIsMultSelect);
            this.mSelectCustomListView.setPreSelects(stringArrayList);
            this.mSelectCustomListView.setData(stringArrayList2);
            String string = arguments.getString("title");
            if (!StringUtil.isEmptyOrNull(string)) {
                this.mTxtTitle.setText(string);
            }
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnOK) {
            onClickBtnOK();
        }
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        dismiss();
    }

    private void onClickBtnOK() {
        onSelectionConfirmed();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0060 A[SYNTHETIC, Splitter:B:27:0x0060] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0066 A[SYNTHETIC, Splitter:B:33:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0033 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onSelectionConfirmed() {
        /*
            r8 = this;
            com.zipow.videobox.view.mm.MMSelectCustomListView r0 = r8.mSelectCustomListView
            java.util.ArrayList r0 = r0.getSelectedItems()
            int r1 = r0.size()
            if (r1 != 0) goto L_0x0010
            r8.onClickBtnBack()
            return
        L_0x0010:
            androidx.fragment.app.FragmentActivity r1 = r8.getActivity()
            if (r1 != 0) goto L_0x0017
            return
        L_0x0017:
            android.os.Bundle r2 = r8.getArguments()
            if (r2 != 0) goto L_0x001e
            return
        L_0x001e:
            android.view.View r2 = r8.getView()
            p021us.zoom.androidlib.util.UIUtil.closeSoftKeyboard(r1, r2)
            android.content.Intent r2 = new android.content.Intent
            r2.<init>()
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            java.util.Iterator r0 = r0.iterator()
        L_0x0033:
            boolean r4 = r0.hasNext()
            if (r4 == 0) goto L_0x006a
            java.lang.Object r4 = r0.next()
            com.zipow.videobox.tempbean.IMessageTemplateSelectItem r4 = (com.zipow.videobox.tempbean.IMessageTemplateSelectItem) r4
            r5 = 0
            java.io.StringWriter r6 = new java.io.StringWriter     // Catch:{ Exception -> 0x0064, all -> 0x005d }
            r6.<init>()     // Catch:{ Exception -> 0x0064, all -> 0x005d }
            com.google.gson.stream.JsonWriter r7 = new com.google.gson.stream.JsonWriter     // Catch:{ Exception -> 0x0064, all -> 0x005d }
            r7.<init>(r6)     // Catch:{ Exception -> 0x0064, all -> 0x005d }
            r4.writeJson(r7)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.lang.String r4 = r6.toString()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r3.add(r4)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r7.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0033
        L_0x0058:
            r0 = move-exception
            r5 = r7
            goto L_0x005e
        L_0x005b:
            r5 = r7
            goto L_0x0064
        L_0x005d:
            r0 = move-exception
        L_0x005e:
            if (r5 == 0) goto L_0x0063
            r5.close()     // Catch:{ IOException -> 0x0063 }
        L_0x0063:
            throw r0
        L_0x0064:
            if (r5 == 0) goto L_0x0033
            r5.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0033
        L_0x006a:
            java.lang.String r0 = "selectItems"
            r2.putStringArrayListExtra(r0, r3)
            android.os.Bundle r0 = r8.resultData
            if (r0 == 0) goto L_0x0076
            r2.putExtras(r0)
        L_0x0076:
            r0 = -1
            r1.setResult(r0, r2)
            r8.dismiss()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMSelectCustomFragment.onSelectionConfirmed():void");
    }

    /* access modifiers changed from: private */
    public void formatSearchEditText() {
        int i;
        Editable editableText = this.mEdtSearch.getEditableText();
        MMSelectCustomListItemSpan[] mMSelectCustomListItemSpanArr = (MMSelectCustomListItemSpan[]) StringUtil.getSortedSpans(editableText, MMSelectCustomListItemSpan.class);
        if (mMSelectCustomListItemSpanArr != null && mMSelectCustomListItemSpanArr.length > 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editableText);
            boolean z = false;
            for (int i2 = 0; i2 < mMSelectCustomListItemSpanArr.length; i2++) {
                int spanStart = spannableStringBuilder.getSpanStart(mMSelectCustomListItemSpanArr[i2]);
                if (i2 == 0) {
                    i = 0;
                } else {
                    i = spannableStringBuilder.getSpanEnd(mMSelectCustomListItemSpanArr[i2 - 1]);
                }
                if (spanStart != i) {
                    CharSequence subSequence = spannableStringBuilder.subSequence(i, spanStart);
                    spannableStringBuilder.replace(i, spanStart, "");
                    int spanEnd = spannableStringBuilder.getSpanEnd(mMSelectCustomListItemSpanArr[mMSelectCustomListItemSpanArr.length - 1]);
                    spannableStringBuilder.replace(spanEnd, spanEnd, subSequence);
                    z = true;
                }
            }
            if (z) {
                this.mEdtSearch.setText(spannableStringBuilder);
                this.mEdtSearch.setSelection(spannableStringBuilder.length());
            }
        }
    }

    /* access modifiers changed from: private */
    public void startFilter(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        if (!str.equals(this.mRunnableFilter.getKey())) {
            this.mRunnableFilter.setKey(str);
            this.mHandler.removeCallbacks(this.mRunnableFilter);
            this.mHandler.postDelayed(this.mRunnableFilter, 300);
        }
    }

    /* access modifiers changed from: private */
    @NonNull
    public String getFilter() {
        String str = "";
        Editable text = this.mEdtSearch.getText();
        MMSelectCustomListItemSpan[] mMSelectCustomListItemSpanArr = (MMSelectCustomListItemSpan[]) text.getSpans(0, text.length(), MMSelectCustomListItemSpan.class);
        if (mMSelectCustomListItemSpanArr.length <= 0) {
            return text.toString();
        }
        int spanEnd = text.getSpanEnd(mMSelectCustomListItemSpanArr[mMSelectCustomListItemSpanArr.length - 1]);
        int length = text.length();
        return spanEnd < length ? text.subSequence(spanEnd, length).toString() : str;
    }

    private void onSelected(boolean z, @Nullable IMessageTemplateSelectItem iMessageTemplateSelectItem) {
        if (iMessageTemplateSelectItem != null) {
            Editable text = this.mEdtSearch.getText();
            int i = 0;
            MMSelectCustomListItemSpan[] mMSelectCustomListItemSpanArr = (MMSelectCustomListItemSpan[]) text.getSpans(0, text.length(), MMSelectCustomListItemSpan.class);
            MMSelectCustomListItemSpan mMSelectCustomListItemSpan = null;
            int length = mMSelectCustomListItemSpanArr.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                MMSelectCustomListItemSpan mMSelectCustomListItemSpan2 = mMSelectCustomListItemSpanArr[i];
                if (iMessageTemplateSelectItem.equals(mMSelectCustomListItemSpan2.getItem())) {
                    mMSelectCustomListItemSpan = mMSelectCustomListItemSpan2;
                    break;
                }
                i++;
            }
            if (z) {
                if (mMSelectCustomListItemSpan != null) {
                    mMSelectCustomListItemSpan.setItem(iMessageTemplateSelectItem);
                    return;
                }
                int length2 = mMSelectCustomListItemSpanArr.length;
                if (length2 > 0) {
                    int spanEnd = text.getSpanEnd(mMSelectCustomListItemSpanArr[length2 - 1]);
                    int length3 = text.length();
                    if (spanEnd < length3) {
                        text.delete(spanEnd, length3);
                    }
                } else {
                    text.clear();
                }
                MMSelectCustomListItemSpan mMSelectCustomListItemSpan3 = new MMSelectCustomListItemSpan(getActivity(), iMessageTemplateSelectItem);
                mMSelectCustomListItemSpan3.setInterval(UIUtil.dip2px(getActivity(), 2.0f));
                String text2 = iMessageTemplateSelectItem.getText();
                int length4 = text.length();
                int length5 = text2.length() + length4;
                text.append(text2);
                text.setSpan(mMSelectCustomListItemSpan3, length4, length5, 33);
                this.mEdtSearch.setSelection(length5);
                this.mEdtSearch.setCursorVisible(true);
            } else if (mMSelectCustomListItemSpan != null) {
                int spanStart = text.getSpanStart(mMSelectCustomListItemSpan);
                int spanEnd2 = text.getSpanEnd(mMSelectCustomListItemSpan);
                if (spanStart >= 0 && spanEnd2 >= 0 && spanEnd2 >= spanStart) {
                    text.delete(spanStart, spanEnd2);
                    text.removeSpan(mMSelectCustomListItemSpan);
                }
            }
        }
    }
}
