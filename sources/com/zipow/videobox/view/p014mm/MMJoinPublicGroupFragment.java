package com.zipow.videobox.view.p014mm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomPublicRoomSearchUI;
import com.zipow.videobox.ptapp.ZoomPublicRoomSearchUI.IZoomPublicRoomSearchUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomPublicRoomSearchData;
import com.zipow.videobox.view.p014mm.MMJoinPublicGroupListView.OnItemSelectChangeListener;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMJoinPublicGroupFragment */
public class MMJoinPublicGroupFragment extends ZMDialogFragment implements IZoomPublicRoomSearchUIListener, OnClickListener, OnEditorActionListener, ExtListener, OnItemSelectChangeListener {
    public static final String RESULT_ARG_SELECTED_ITEMS = "selectItems";
    private Button mBtnClearSearchView;
    private Button mBtnSearch;
    @Nullable
    private Drawable mDimmedForground = null;
    private EditText mEdtSearch;
    private EditText mEdtSearchDummy;
    /* access modifiers changed from: private */
    public MMJoinPublicGroupListView mGroupListView;
    private FrameLayout mListContainer;
    private View mPanelNoItemMsg;
    private View mPanelSearchBar;
    private View mPanelTitleBar;
    @Nullable
    private ProgressDialog mProgressDialog;

    public boolean onBackPressed() {
        return false;
    }

    public void onForbidJoinRoom(String str, int i) {
    }

    public void onJoinRoom(String str, int i) {
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, MMJoinPublicGroupFragment.class.getName(), bundle, i, true, 1);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_join_public_group, viewGroup, false);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mGroupListView = (MMJoinPublicGroupListView) inflate.findViewById(C4558R.C4560id.groupListView);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mBtnSearch = (Button) inflate.findViewById(C4558R.C4560id.btnSearch);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mEdtSearchDummy = (EditText) inflate.findViewById(C4558R.C4560id.edtSearchDummy);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        this.mGroupListView.setOnItemSelectChangeListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                MMJoinPublicGroupFragment.this.updateSearchButtons();
            }
        });
        this.mBtnSearch.setOnClickListener(this);
        this.mEdtSearch.setOnEditorActionListener(this);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        ZoomPublicRoomSearchUI.getInstance().addListener(this);
        doSearchGroups("");
        return inflate;
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        doSearchGroups(this.mEdtSearch.getText().toString());
        return true;
    }

    public void onDestroyView() {
        ZoomPublicRoomSearchUI.getInstance().removeListener(this);
        super.onDestroyView();
    }

    public void onSearchResponse(int i, int i2, int i3) {
        this.mGroupListView.onSearchResponse(i, i2, i3);
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
        if (this.mGroupListView.getEmptyView() == null) {
            this.mGroupListView.setEmptyView(this.mPanelNoItemMsg);
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnCancel) {
                dismiss();
            } else if (id == C4558R.C4560id.btnClearSearchView) {
                onClickBtnClearSearchView();
            } else if (id == C4558R.C4560id.btnSearch) {
                onClickBtnSearch();
            }
        }
    }

    public void onDestroy() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomPublicRoomSearchData publicRoomSearchData = zoomMessenger.getPublicRoomSearchData();
            if (publicRoomSearchData != null) {
                publicRoomSearchData.cancelSearch();
            }
        }
        super.onDestroy();
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    private void onClickBtnSearch() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        doSearchGroups(this.mEdtSearch.getText().toString());
    }

    private void doSearchGroups(String str) {
        if (this.mGroupListView.doSearchPublicGroups(str)) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                this.mProgressDialog = UIUtil.showSimpleWaitingDialog((Activity) activity, C4558R.string.zm_msg_waiting);
            }
        }
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
            this.mGroupListView.post(new Runnable() {
                public void run() {
                    MMJoinPublicGroupFragment.this.mGroupListView.requestLayout();
                }
            });
        }
    }

    public boolean onSearchRequested() {
        this.mEdtSearchDummy.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    /* access modifiers changed from: private */
    public void updateSearchButtons() {
        if (this.mEdtSearch.getText().length() > 0) {
            this.mBtnClearSearchView.setVisibility(0);
            this.mBtnSearch.setVisibility(0);
            return;
        }
        this.mBtnClearSearchView.setVisibility(8);
        this.mBtnSearch.setVisibility(8);
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onItemSelectChange() {
        ArrayList selectGroups = this.mGroupListView.getSelectGroups();
        if (selectGroups.size() > 0) {
            Intent intent = new Intent();
            intent.putExtra("selectItems", selectGroups);
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.setResult(-1, intent);
            }
            dismiss();
        }
    }
}
