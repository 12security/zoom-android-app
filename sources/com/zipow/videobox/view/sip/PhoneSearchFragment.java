package com.zipow.videobox.view.sip;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.ZMBuddyListListener;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhoneSearchFragment extends ZMDialogFragment implements OnClickListener, ExtListener, OnEditorActionListener, ZMBuddyListListener {
    public static final int ACTION_PICK_SIP = 1090;
    public static final String ARG_IM_ADDR_BOOK_ITEM = "arg_im_addr_book_item";
    private static final String ARG_SEARCH_FILTER = "search_filter";
    private Button mBtnClearSearchView;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearchReal;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private View mPanelSearchBarReal;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String trim = PhoneSearchFragment.this.mEdtSearchReal.getText().toString().trim();
            PhoneSearchFragment.this.mSipPhoneListView.filter(trim);
            if (trim.length() <= 0 || PhoneSearchFragment.this.mSipPhoneListView.getCount() <= 0) {
                if (OsUtil.isAtLeastJB()) {
                    PhoneSearchFragment.this.mSipPhoneListView.setBackground(PhoneSearchFragment.this.getResources().getDrawable(C4558R.C4559drawable.zm_listview_bg));
                } else {
                    PhoneSearchFragment.this.mSipPhoneListView.setBackgroundDrawable(PhoneSearchFragment.this.mDimmedForground);
                }
            } else if (OsUtil.isAtLeastJB()) {
                PhoneSearchFragment.this.mSipPhoneListView.setBackground(PhoneSearchFragment.this.getResources().getDrawable(C4558R.C4559drawable.zm_listview_bg));
            } else {
                PhoneSearchFragment.this.mSipPhoneListView.setBackgroundDrawable(PhoneSearchFragment.this.getResources().getDrawable(C4558R.C4559drawable.zm_listview_bg));
            }
        }
    };
    /* access modifiers changed from: private */
    public ZoomSipPhoneListView mSipPhoneListView;
    private boolean mbKeyboardOpen = false;

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsFragment(Object obj, String str, int i) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putString(ARG_SEARCH_FILTER, str);
        }
        if (obj instanceof Fragment) {
            SimpleActivity.show((Fragment) obj, PhoneSearchFragment.class.getName(), bundle, i, 2);
        } else if (obj instanceof ZMActivity) {
            SimpleActivity.show((ZMActivity) obj, PhoneSearchFragment.class.getName(), bundle, i, true);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C4558R.layout.zm_phone_search, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mPanelSearchBarReal = view.findViewById(C4558R.C4560id.panelSearchBarReal);
        this.mEdtSearchReal = (EditText) view.findViewById(C4558R.C4560id.edtSearchReal);
        this.mBtnClearSearchView = (Button) view.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mSipPhoneListView = (ZoomSipPhoneListView) view.findViewById(C4558R.C4560id.sipPhoneListView);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        if (OsUtil.isAtLeastJB()) {
            this.mSipPhoneListView.setBackground(this.mDimmedForground);
        } else {
            this.mSipPhoneListView.setBackgroundDrawable(this.mDimmedForground);
        }
        this.mSipPhoneListView.setSelectListener(new ZoomSipPhoneSelectListener() {
            public void onSelected(IMAddrBookItem iMAddrBookItem) {
                if (CmmSIPCallManager.getInstance().checkNetwork(PhoneSearchFragment.this.getContext())) {
                    FragmentActivity activity = PhoneSearchFragment.this.getActivity();
                    if (activity != null) {
                        Intent intent = new Intent();
                        intent.putExtra(PhoneSearchFragment.ARG_IM_ADDR_BOOK_ITEM, iMAddrBookItem);
                        activity.setResult(-1, intent);
                    }
                    PhoneSearchFragment.this.dismiss();
                }
            }
        });
        this.mEdtSearchReal.setOnEditorActionListener(this);
        this.mEdtSearchReal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                PhoneSearchFragment.this.mHandler.removeCallbacks(PhoneSearchFragment.this.mRunnableFilter);
                PhoneSearchFragment.this.mHandler.postDelayed(PhoneSearchFragment.this.mRunnableFilter, 300);
                PhoneSearchFragment.this.updateClearSearchView();
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearchReal.getText().length() > 0 ? 0 : 8);
    }

    public void onActivityCreated(Bundle bundle) {
        getActivity().getWindow().setSoftInputMode(21);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(ARG_SEARCH_FILTER);
            if (!TextUtils.isEmpty(string)) {
                this.mEdtSearchReal.setText(string);
                EditText editText = this.mEdtSearchReal;
                editText.setSelection(editText.getText().length());
            }
        }
        super.onActivityCreated(bundle);
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        return true;
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearchReal.setText("");
    }

    public void onBuddyListUpdate() {
        this.mSipPhoneListView.reloadAllData();
    }

    public void onBuddyInfoUpdate(List<String> list, List<String> list2) {
        this.mSipPhoneListView.onBuddyInfoUpdate(list2);
    }

    public void onKeyboardOpen() {
        this.mbKeyboardOpen = true;
    }

    public void onKeyboardClosed() {
        if (this.mbKeyboardOpen) {
            this.mbKeyboardOpen = false;
        }
    }

    public boolean onBackPressed() {
        if (!this.mbKeyboardOpen) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearchReal);
        return true;
    }

    public void onResume() {
        super.onResume();
        this.mSipPhoneListView.reloadAllData();
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onPause() {
        super.onPause();
        UIUtil.closeSoftKeyboard(getContext(), this.mEdtSearchReal);
    }

    public boolean isResultEmpty() {
        return this.mSipPhoneListView.getCount() <= 0;
    }
}
