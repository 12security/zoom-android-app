package com.zipow.videobox.fragment;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.MMSelectContactsFragment.GuestureListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.view.MMSelectGroupListItemSpan;
import com.zipow.videobox.view.ZMReplaceSpanMovementMethod;
import com.zipow.videobox.view.p014mm.MMSelectGroupListView;
import com.zipow.videobox.view.p014mm.MMZoomGroup;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMEditText;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.videomeetings.C4558R;

public class MMSelectGroupFragment extends ZMDialogFragment implements OnItemClickListener, OnClickListener, KeyboardListener {
    private static final String ARGS_IS_CONTAIN_MUC = "isContainMuc";
    private static final String ARGS_IS_MULT_SELECT = "isMultSelect";
    private static final String ARGS_PRE_SEELECTS = "preSelects";
    private static final String ARGS_RESULT_DATA = "resultData";
    private static final String ARGS_TITLE = "title";
    public static final String RESULT_SELECT_GROUPS = "selectGroups";
    private Button mBtnOK;
    /* access modifiers changed from: private */
    public ZMEditText mEdtSearch;
    /* access modifiers changed from: private */
    @Nullable
    public GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private boolean mIsContainMUC = true;
    private boolean mIsMultSelect = false;
    @NonNull
    private SearchFilterRunnable mRunnableFilter = new SearchFilterRunnable();
    /* access modifiers changed from: private */
    public MMSelectGroupListView mSelectGroupListView;
    private TextView mTxtTitle;
    @NonNull
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMSelectGroupFragment.this.onGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMSelectGroupFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };
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
            MMSelectGroupFragment.this.mSelectGroupListView.setFilter(this.mKey);
        }
    }

    public static void showAsActivity(Fragment fragment, boolean z, ArrayList<String> arrayList, String str, int i) {
        showAsActivity(fragment, z, arrayList, str, i, null);
    }

    public static void showAsActivity(@Nullable Fragment fragment, boolean z, @Nullable ArrayList<String> arrayList, String str, int i, @Nullable Bundle bundle) {
        showAsActivity(fragment, z, true, arrayList, str, i, bundle);
    }

    public static void showAsActivity(@Nullable Fragment fragment, boolean z, boolean z2, @Nullable ArrayList<String> arrayList, String str, int i, @Nullable Bundle bundle) {
        if (fragment != null) {
            Bundle bundle2 = new Bundle();
            if (arrayList != null) {
                bundle2.putStringArrayList(ARGS_PRE_SEELECTS, arrayList);
            }
            bundle2.putBoolean(ARGS_IS_MULT_SELECT, z);
            bundle2.putBoolean(ARGS_IS_CONTAIN_MUC, z2);
            bundle2.putString("title", str);
            if (bundle != null) {
                bundle2.putBundle("resultData", bundle);
            }
            SimpleActivity.show(fragment, MMSelectGroupFragment.class.getName(), bundle2, i, true);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mIsMultSelect = arguments.getBoolean(ARGS_IS_MULT_SELECT);
            this.mIsContainMUC = arguments.getBoolean(ARGS_IS_CONTAIN_MUC);
            ArrayList stringArrayList = arguments.getStringArrayList(ARGS_PRE_SEELECTS);
            this.resultData = arguments.getBundle("resultData");
            this.mSelectGroupListView.setIsMultSelect(this.mIsMultSelect);
            this.mSelectGroupListView.setmIsContanMUC(this.mIsContainMUC);
            this.mSelectGroupListView.setPreSelects(stringArrayList);
            String string = arguments.getString("title");
            if (!StringUtil.isEmptyOrNull(string)) {
                this.mTxtTitle.setText(string);
            }
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_groups, viewGroup, false);
        this.mSelectGroupListView = (MMSelectGroupListView) inflate.findViewById(C4558R.C4560id.listView);
        this.mEdtSearch = (ZMEditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnOK = (Button) inflate.findViewById(C4558R.C4560id.btnOK);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mSelectGroupListView.setOnItemClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mBtnOK.setOnClickListener(this);
        this.mEdtSearch.setOnClickListener(this);
        this.mEdtSearch.setSelected(true);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (i3 < i2) {
                    final MMSelectGroupListItemSpan[] mMSelectGroupListItemSpanArr = (MMSelectGroupListItemSpan[]) MMSelectGroupFragment.this.mEdtSearch.getText().getSpans(i3 + i, i + i2, MMSelectGroupListItemSpan.class);
                    if (mMSelectGroupListItemSpanArr.length > 0) {
                        MMSelectGroupFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                if (MMSelectGroupFragment.this.isResumed()) {
                                    for (MMSelectGroupListItemSpan item : mMSelectGroupListItemSpanArr) {
                                        MMZoomGroup item2 = item.getItem();
                                        if (item2 != null) {
                                            MMSelectGroupFragment.this.mSelectGroupListView.unselectBuddy(item2.getGroupId());
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                MMSelectGroupFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (MMSelectGroupFragment.this.isResumed()) {
                            MMSelectGroupFragment.this.formatSearchEditText();
                            MMSelectGroupFragment.this.startFilter(MMSelectGroupFragment.this.getFilter());
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
        this.mGestureDetector = new GestureDetector(getActivity(), new GuestureListener(this.mSelectGroupListView, this.mEdtSearch));
        this.mSelectGroupListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return MMSelectGroupFragment.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        return inflate;
    }

    private void updateUI() {
        this.mSelectGroupListView.updateUI();
        updateButtonOK();
    }

    public void onKeyboardOpen() {
        this.mEdtSearch.setCursorVisible(true);
    }

    public void onKeyboardClosed() {
        this.mEdtSearch.setCursorVisible(false);
        this.mHandler.post(new Runnable() {
            public void run() {
                MMSelectGroupFragment.this.mEdtSearch.requestLayout();
            }
        });
    }

    public void onResume() {
        super.onResume();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        updateUI();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (MMSelectGroupFragment.this.isResumed()) {
                    MMSelectGroupFragment.this.mEdtSearch.requestFocus();
                    UIUtil.openSoftKeyboard(MMSelectGroupFragment.this.getActivity(), MMSelectGroupFragment.this.mEdtSearch);
                }
            }
        }, 100);
    }

    public void onPause() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void formatSearchEditText() {
        int i;
        Editable editableText = this.mEdtSearch.getEditableText();
        MMSelectGroupListItemSpan[] mMSelectGroupListItemSpanArr = (MMSelectGroupListItemSpan[]) StringUtil.getSortedSpans(editableText, MMSelectGroupListItemSpan.class);
        if (mMSelectGroupListItemSpanArr != null && mMSelectGroupListItemSpanArr.length > 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editableText);
            boolean z = false;
            for (int i2 = 0; i2 < mMSelectGroupListItemSpanArr.length; i2++) {
                int spanStart = spannableStringBuilder.getSpanStart(mMSelectGroupListItemSpanArr[i2]);
                if (i2 == 0) {
                    i = 0;
                } else {
                    i = spannableStringBuilder.getSpanEnd(mMSelectGroupListItemSpanArr[i2 - 1]);
                }
                if (spanStart != i) {
                    CharSequence subSequence = spannableStringBuilder.subSequence(i, spanStart);
                    spannableStringBuilder.replace(i, spanStart, "");
                    int spanEnd = spannableStringBuilder.getSpanEnd(mMSelectGroupListItemSpanArr[mMSelectGroupListItemSpanArr.length - 1]);
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
    @NonNull
    public String getFilter() {
        String str = "";
        Editable text = this.mEdtSearch.getText();
        MMSelectGroupListItemSpan[] mMSelectGroupListItemSpanArr = (MMSelectGroupListItemSpan[]) text.getSpans(0, text.length(), MMSelectGroupListItemSpan.class);
        if (mMSelectGroupListItemSpanArr.length <= 0) {
            return text.toString();
        }
        int spanEnd = text.getSpanEnd(mMSelectGroupListItemSpanArr[mMSelectGroupListItemSpanArr.length - 1]);
        int length = text.length();
        return spanEnd < length ? text.subSequence(spanEnd, length).toString() : str;
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
    public void onGroupAction(int i, @Nullable GroupAction groupAction, String str) {
        if (groupAction != null) {
            this.mSelectGroupListView.updateGroup(groupAction.getGroupId());
        }
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        this.mSelectGroupListView.removeGroup(str);
    }

    private void updateButtonOK() {
        if (this.mIsMultSelect) {
            this.mBtnOK.setEnabled(getSelectedBuddiesCount() > 0);
        } else {
            this.mBtnOK.setVisibility(8);
        }
    }

    private int getSelectedBuddiesCount() {
        return this.mSelectGroupListView.getSelectedBuddies().size();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        MMZoomGroup item = this.mSelectGroupListView.getItem(i);
        if (item != null) {
            if (this.mIsMultSelect) {
                this.mSelectGroupListView.onItemClicked(item.getGroupId());
                onSelected(this.mSelectGroupListView.isGroupSelected(item.getGroupId()), item);
                updateButtonOK();
            } else {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ArrayList arrayList = new ArrayList();
                    Intent intent = new Intent();
                    arrayList.add(item.getGroupId());
                    intent.putStringArrayListExtra(RESULT_SELECT_GROUPS, arrayList);
                    Bundle bundle = this.resultData;
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    activity.setResult(-1, intent);
                }
                dismiss();
            }
        }
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnOK) {
            onClickBtnOK();
        }
    }

    private void onClickBtnOK() {
        onSelectionConfirmed();
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        dismiss();
    }

    private void onSelectionConfirmed() {
        ArrayList selectedBuddies = this.mSelectGroupListView.getSelectedBuddies();
        if (selectedBuddies.size() == 0) {
            onClickBtnBack();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null && getArguments() != null) {
            UIUtil.closeSoftKeyboard(activity, getView());
            Intent intent = new Intent();
            intent.putStringArrayListExtra(RESULT_SELECT_GROUPS, selectedBuddies);
            Bundle bundle = this.resultData;
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.setResult(-1, intent);
            dismiss();
        }
    }

    private void onSelected(boolean z, @Nullable MMZoomGroup mMZoomGroup) {
        if (mMZoomGroup != null) {
            Editable text = this.mEdtSearch.getText();
            int i = 0;
            MMSelectGroupListItemSpan[] mMSelectGroupListItemSpanArr = (MMSelectGroupListItemSpan[]) text.getSpans(0, text.length(), MMSelectGroupListItemSpan.class);
            MMSelectGroupListItemSpan mMSelectGroupListItemSpan = null;
            int length = mMSelectGroupListItemSpanArr.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                MMSelectGroupListItemSpan mMSelectGroupListItemSpan2 = mMSelectGroupListItemSpanArr[i];
                if (StringUtil.isSameString(mMZoomGroup.getGroupId(), mMSelectGroupListItemSpan2.getItem().getGroupId())) {
                    mMSelectGroupListItemSpan = mMSelectGroupListItemSpan2;
                    break;
                }
                i++;
            }
            if (z) {
                if (mMSelectGroupListItemSpan != null) {
                    mMSelectGroupListItemSpan.setItem(mMZoomGroup);
                    return;
                }
                int length2 = mMSelectGroupListItemSpanArr.length;
                if (length2 > 0) {
                    int spanEnd = text.getSpanEnd(mMSelectGroupListItemSpanArr[length2 - 1]);
                    int length3 = text.length();
                    if (spanEnd < length3) {
                        text.delete(spanEnd, length3);
                    }
                } else {
                    text.clear();
                }
                MMSelectGroupListItemSpan mMSelectGroupListItemSpan3 = new MMSelectGroupListItemSpan(getActivity(), mMZoomGroup);
                mMSelectGroupListItemSpan3.setInterval(UIUtil.dip2px(getActivity(), 2.0f));
                String groupName = mMZoomGroup.getGroupName();
                int length4 = text.length();
                int length5 = groupName.length() + length4;
                text.append(mMZoomGroup.getGroupName());
                text.setSpan(mMSelectGroupListItemSpan3, length4, length5, 33);
                this.mEdtSearch.setSelection(length5);
                this.mEdtSearch.setCursorVisible(true);
            } else if (mMSelectGroupListItemSpan != null) {
                int spanStart = text.getSpanStart(mMSelectGroupListItemSpan);
                int spanEnd2 = text.getSpanEnd(mMSelectGroupListItemSpan);
                if (spanStart >= 0 && spanEnd2 >= 0 && spanEnd2 >= spanStart) {
                    text.delete(spanStart, spanEnd2);
                    text.removeSpan(mMSelectGroupListItemSpan);
                }
            }
        }
    }
}
