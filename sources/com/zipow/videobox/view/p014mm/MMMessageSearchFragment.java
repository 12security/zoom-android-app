package com.zipow.videobox.view.p014mm;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMCallbackUI.IIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI.SimpleIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchResponse;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.ZMSearchBar.OnSearchBarListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageSearchFragment */
public class MMMessageSearchFragment extends ZMDialogFragment implements ExtListener, OnClickListener {
    private static final String ARG_SEARCH_FILTER = "search_filter";
    public static final String TAG = "MMMessageSearchFragment";
    private View mBtnBack;
    /* access modifiers changed from: private */
    public Button mBtnSearch;
    @Nullable
    private String mContextAnchorMsgGUID;
    @Nullable
    private String mContextMsgReqId;
    private boolean mHasBlockedByIBMsg = false;
    @NonNull
    private IIMCallbackUIListener mIMCallbackUIListener = new SimpleIMCallbackUIListener() {
        public void Indicate_SearchMessageResponse(String str, int i, @Nullable MessageContentSearchResponse messageContentSearchResponse) {
            MMMessageSearchFragment.this.Indicate_SearchMessageResponse(str, i, messageContentSearchResponse);
        }

        public void Indicate_LocalSearchMSGResponse(String str, MessageContentSearchResponse messageContentSearchResponse) {
            MMMessageSearchFragment.this.Indicate_LocalSearchMSGResponse(str, messageContentSearchResponse);
        }
    };
    private MMContentSearchMessagesListView mMessagesListView;
    private View mPanelEmptyView;
    private View mPanelMessageTitle;
    private int mResultSortType = ZMIMUtils.getSearchMessageSortType();
    private TextView mSortByButton;
    private TextView mTxtBlockedByIB;
    private View mTxtContentLoading;
    private View mTxtEmptyView;
    private TextView mTxtLoadingError;
    private ZMSearchBar mZMSearchBar;
    private boolean mbIgnoreKeyboardCloseEvent = false;
    private boolean mbKeyboardOpen = false;
    @NonNull
    private IZoomMessengerUIListener zoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onIndicateInfoUpdatedWithJID(String str) {
            MMMessageSearchFragment.this.onIndicateInfoUpdatedWithJID(str);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMMessageSearchFragment$SortByMenuItem */
    static class SortByMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_MOST_RECENT = 1;
        public static final int ACTION_MOST_RELEVANT = 0;

        public SortByMenuItem(String str, int i, boolean z) {
            super(i, str, null, z);
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsFragment(Object obj) {
        showAsFragment(obj, null);
    }

    public static void showAsFragment(Object obj, String str) {
        showAsFragment(obj, -1, str);
    }

    public static void showAsFragment(Object obj, int i, String str) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putString(ARG_SEARCH_FILTER, str);
        }
        if (obj instanceof Fragment) {
            SimpleActivity.show((Fragment) obj, MMMessageSearchFragment.class.getName(), bundle, i, 2);
        } else if (obj instanceof ZMActivity) {
            SimpleActivity.show((ZMActivity) obj, MMMessageSearchFragment.class.getName(), bundle, i, true);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(getActivity(), true, C4558R.color.zm_im_search_bar_bg);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_message_search, viewGroup, false);
        this.mZMSearchBar = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mMessagesListView = (MMContentSearchMessagesListView) inflate.findViewById(C4558R.C4560id.listViewContentMessages);
        this.mTxtLoadingError = (TextView) inflate.findViewById(C4558R.C4560id.txtLoadingError);
        this.mTxtContentLoading = inflate.findViewById(C4558R.C4560id.txtContentLoading);
        this.mTxtBlockedByIB = (TextView) inflate.findViewById(C4558R.C4560id.txtBlockedByIB);
        this.mPanelEmptyView = inflate.findViewById(C4558R.C4560id.panelEmptyView);
        this.mTxtEmptyView = inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mPanelMessageTitle = inflate.findViewById(C4558R.C4560id.panel_listview_message_title);
        this.mSortByButton = (TextView) inflate.findViewById(C4558R.C4560id.sort_by_button);
        if (this.mResultSortType == 2) {
            this.mSortByButton.setText(C4558R.string.zm_lbl_search_sort_by_relevant_119637);
        } else {
            this.mSortByButton.setText(C4558R.string.zm_lbl_search_sort_by_recent_119637);
        }
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSearch = (Button) inflate.findViewById(C4558R.C4560id.btnSearch);
        this.mBtnSearch.setOnClickListener(this);
        this.mSortByButton.setOnClickListener(this);
        this.mMessagesListView.setParentFragment(this);
        this.mZMSearchBar.setOnSearchBarListener(new OnSearchBarListener() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onClearSearch() {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMMessageSearchFragment.this.mBtnSearch.setVisibility(editable.length() != 0 ? 0 : 8);
            }

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    MMMessageSearchFragment.this.startSearch();
                }
                return false;
            }
        });
        this.mTxtLoadingError.setOnClickListener(this);
        this.mTxtLoadingError.setText(Html.fromHtml(getString(C4558R.string.zm_lbl_content_load_error)));
        if (bundle != null) {
            this.mContextMsgReqId = bundle.getString("mContextMsgReqId");
            this.mContextAnchorMsgGUID = bundle.getString("mContextAnchorMsgGUID");
            this.mbIgnoreKeyboardCloseEvent = bundle.getBoolean("mbIgnoreKeyboardCloseEvent");
        }
        IMCallbackUI.getInstance().addListener(this.mIMCallbackUIListener);
        ZoomMessengerUI.getInstance().addListener(this.zoomMessengerUIListener);
        return inflate;
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        if (bundle == null && getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(16);
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(ARG_SEARCH_FILTER);
            if (!TextUtils.isEmpty(string)) {
                this.mZMSearchBar.setText(string);
                startSearch();
            }
        }
        super.onActivityCreated(bundle);
    }

    public void onDestroyView() {
        IMCallbackUI.getInstance().removeListener(this.mIMCallbackUIListener);
        ZoomMessengerUI.getInstance().removeListener(this.zoomMessengerUIListener);
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        if (this.mMessagesListView.isEmpty()) {
            UIUtil.openSoftKeyboard(getContext(), this.mZMSearchBar.getEditText());
        }
    }

    public void onKeyboardOpen() {
        this.mbKeyboardOpen = true;
        this.mbIgnoreKeyboardCloseEvent = false;
    }

    public void onKeyboardClosed() {
        if (this.mbKeyboardOpen) {
            this.mbKeyboardOpen = false;
        }
    }

    private boolean isResultEmpty() {
        MMContentSearchMessagesListView mMContentSearchMessagesListView = this.mMessagesListView;
        if (mMContentSearchMessagesListView != null) {
            return mMContentSearchMessagesListView.isResultEmpty();
        }
        return true;
    }

    public void updateEmptyViewStatus(boolean z) {
        if (z) {
            this.mPanelMessageTitle.setVisibility(this.mMessagesListView.isEmpty() ? 8 : 0);
            this.mPanelEmptyView.setVisibility(8);
            return;
        }
        updateEmptyViewStatus();
    }

    private void updateEmptyViewStatus() {
        boolean isEmpty = this.mMessagesListView.isEmpty();
        boolean isLoading = this.mMessagesListView.isLoading();
        boolean isLoadSuccess = this.mMessagesListView.isLoadSuccess();
        int i = 0;
        boolean z = isEmpty & (this.mZMSearchBar.getText().trim().length() != 0);
        this.mPanelEmptyView.setVisibility(z ? 0 : 8);
        this.mPanelMessageTitle.setVisibility(z ? 8 : 0);
        if (isLoading) {
            this.mTxtContentLoading.setVisibility(0);
            this.mTxtEmptyView.setVisibility(8);
            this.mTxtLoadingError.setVisibility(8);
            return;
        }
        this.mTxtContentLoading.setVisibility(8);
        if (this.mHasBlockedByIBMsg) {
            this.mTxtEmptyView.setVisibility(8);
            this.mTxtLoadingError.setVisibility(8);
            this.mTxtBlockedByIB.setVisibility(0);
            return;
        }
        this.mTxtEmptyView.setVisibility(isLoadSuccess ? 0 : 8);
        TextView textView = this.mTxtLoadingError;
        if (isLoadSuccess) {
            i = 8;
        }
        textView.setVisibility(i);
        this.mTxtBlockedByIB.setVisibility(8);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mContextMsgReqId", this.mContextMsgReqId);
        bundle.putString("mContextAnchorMsgGUID", this.mContextAnchorMsgGUID);
        bundle.putBoolean("mbIgnoreKeyboardCloseEvent", this.mbIgnoreKeyboardCloseEvent);
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        this.mMessagesListView.onIndicateInfoUpdatedWithJID(str);
    }

    public void Indicate_SearchMessageResponse(String str, int i, @Nullable MessageContentSearchResponse messageContentSearchResponse) {
        updateEmptyViewStatus(this.mMessagesListView.Indicate_SearchMessageResponse(str, i, messageContentSearchResponse));
    }

    public void Indicate_LocalSearchMSGResponse(String str, MessageContentSearchResponse messageContentSearchResponse) {
        updateEmptyViewStatus(this.mMessagesListView.Indicate_LocalSearchMSGResponse(str, messageContentSearchResponse));
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnClearSearchView) {
                onClickBtnClearSearchView();
            } else if (id == C4558R.C4560id.txtLoadingError) {
                onClickTxtLoadingError();
            } else if (id == C4558R.C4560id.btnBack) {
                onClickBtnBack();
            } else if (id == C4558R.C4560id.sort_by_button) {
                onClickBtnSortBy();
            } else if (id == C4558R.C4560id.btnSearch) {
                onClickBtnSearch();
            }
        }
    }

    private void onClickBtnSortBy() {
        Activity activity = (Activity) getContext();
        if (activity != null) {
            boolean z = false;
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new SortByMenuItem(getString(C4558R.string.zm_lbl_search_sort_by_relevant_119637), 0, this.mResultSortType == 2));
            String string = getString(C4558R.string.zm_lbl_search_sort_by_recent_119637);
            if (this.mResultSortType == 1) {
                z = true;
            }
            arrayList.add(new SortByMenuItem(string, 1, z));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            zMMenuAdapter.setShowSelectedStatus(true);
            TextView textView = new TextView(activity);
            if (VERSION.SDK_INT < 23) {
                textView.setTextAppearance(activity, C4558R.style.ZMTextView_ExtremLarge_OnLight);
            } else {
                textView.setTextAppearance(C4558R.style.ZMTextView_ExtremLarge_OnLight);
            }
            int dip2px = UIUtil.dip2px(activity, 20.0f);
            textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
            textView.setText(getString(C4558R.string.zm_lbl_sort_by_119637));
            ZMAlertDialog create = new Builder(activity).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMMessageSearchFragment.this.onSortTypeChanged((SortByMenuItem) zMMenuAdapter.getItem(i));
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void onSortTypeChanged(@NonNull SortByMenuItem sortByMenuItem) {
        int action = sortByMenuItem.getAction();
        int i = 2;
        if (action == 0) {
            this.mSortByButton.setText(C4558R.string.zm_lbl_search_sort_by_relevant_119637);
        } else if (action == 1) {
            this.mSortByButton.setText(C4558R.string.zm_lbl_search_sort_by_recent_119637);
            i = 1;
        }
        if (i != this.mResultSortType) {
            this.mResultSortType = i;
            this.mMessagesListView.setSortType(i);
            ZMIMUtils.setSearchMessageSortType(i);
            startSearch();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickTxtLoadingError() {
        if (!this.mMessagesListView.isLoadSuccess()) {
            this.mMessagesListView.searchMessage(null);
        }
        updateEmptyViewStatus();
    }

    private void onClickBtnClearSearchView() {
        this.mZMSearchBar.setText("");
    }

    private void onClickBtnSearch() {
        startSearch();
    }

    /* access modifiers changed from: private */
    public void startSearch() {
        String trim = this.mZMSearchBar.getText().trim();
        if (!StringUtil.isEmptyOrNull(trim)) {
            this.mMessagesListView.setFilter(trim, null);
            updateEmptyViewStatus();
            this.mbIgnoreKeyboardCloseEvent = true;
            UIUtil.closeSoftKeyboard(getActivity(), this.mZMSearchBar.getEditText());
        }
    }
}
