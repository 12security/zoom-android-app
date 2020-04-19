package com.zipow.videobox.fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.view.p014mm.MMSavedSessionsListView;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMSavedSessionsFragment extends ZMDialogFragment implements OnClickListener, OnEditorActionListener, ExtListener {
    private Button mBtnClearSearchView;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    private View mPanelNoItemMsg;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = IMSavedSessionsFragment.this.mEdtSearch.getText().toString();
            IMSavedSessionsFragment.this.mSavedSessionsListView.filter(obj);
            if ((obj.length() <= 0 || IMSavedSessionsFragment.this.mSavedSessionsListView.getCount() <= 0) && IMSavedSessionsFragment.this.mPanelTitleBar.getVisibility() != 0) {
                IMSavedSessionsFragment.this.mListContainer.setForeground(IMSavedSessionsFragment.this.mDimmedForground);
            } else {
                IMSavedSessionsFragment.this.mListContainer.setForeground(null);
            }
        }
    };
    /* access modifiers changed from: private */
    public MMSavedSessionsListView mSavedSessionsListView;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, GroupAction groupAction, String str) {
            IMSavedSessionsFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_ChatSessionListUpdate() {
            IMSavedSessionsFragment.this.onNotify_ChatSessionListUpdate();
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            IMSavedSessionsFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }
    };

    public boolean onBackPressed() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, IMSavedSessionsFragment.class.getName(), bundle, i, false, 1);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_saved_sessions, viewGroup, false);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mSavedSessionsListView = (MMSavedSessionsListView) inflate.findViewById(C4558R.C4560id.sessionsListView);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        this.mSavedSessionsListView.setEmptyView(this.mPanelNoItemMsg);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                IMSavedSessionsFragment.this.mHandler.removeCallbacks(IMSavedSessionsFragment.this.mRunnableFilter);
                IMSavedSessionsFragment.this.mHandler.postDelayed(IMSavedSessionsFragment.this.mRunnableFilter, 300);
                IMSavedSessionsFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        this.mSavedSessionsListView.reloadAll();
    }

    public void onDestroyView() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onDestroyView();
    }

    public void onGroupAction(int i, GroupAction groupAction, String str) {
        this.mSavedSessionsListView.onGroupAction(i, groupAction, str);
    }

    public void onNotify_ChatSessionListUpdate() {
        this.mSavedSessionsListView.onNotify_ChatSessionListUpdate();
    }

    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        this.mSavedSessionsListView.onNotify_MUCGroupInfoUpdatedImpl(str);
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onKeyboardOpen() {
        if (getView() != null && this.mEdtSearch.hasFocus()) {
            this.mEdtSearch.setCursorVisible(true);
            this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_focused);
            this.mPanelTitleBar.setVisibility(8);
            this.mListContainer.setForeground(this.mDimmedForground);
        }
    }

    public void onKeyboardClosed() {
        EditText editText = this.mEdtSearch;
        if (editText != null) {
            editText.setCursorVisible(false);
            this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_normal);
            this.mListContainer.setForeground(null);
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (IMSavedSessionsFragment.this.isResumed()) {
                        IMSavedSessionsFragment.this.mPanelTitleBar.setVisibility(0);
                    }
                }
            });
        }
    }

    public boolean onSearchRequested() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                dismiss();
            } else if (id == C4558R.C4560id.btnClearSearchView) {
                onClickBtnClearSearchView();
            }
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    public void dismiss() {
        finishFragment(true);
    }
}
