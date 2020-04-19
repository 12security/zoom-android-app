package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.fragment.MMSelectSessionAndBuddyFragment;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMCallbackUI.IIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI.SimpleIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMProtos.FileFilterSearchResults;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.ZMSearchBar.OnSearchBarListener;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper.PendingFileInfo;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentSearchFragment */
public class MMContentSearchFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnContentFileOperatorListener {
    private static final String ARGS_SHARE_FILE_ID = "shareFileId";
    @NonNull
    private static String ARG_CONENT_FILTER = "content_filter";
    @NonNull
    private static String ARG_CONTENT_MODE = "content_mode";
    public static final int REQUEST_DO_SHAREER = 2015;
    public static final int REQUEST_GET_SHAREER = 2014;
    public static final int REQUEST_VIEW_FILE_DETAIL = 3001;
    public static final String TAG = "MMContentSearchFragment";
    private View mBtnBack;
    /* access modifiers changed from: private */
    public Button mBtnSearch;
    @Nullable
    private String mContextAnchorMsgGUID;
    @Nullable
    private String mContextMsgReqId;
    private MMContentSearchFilesListView mFilesListView;
    private boolean mHasBlockedByIB = false;
    @NonNull
    private IIMCallbackUIListener mIMCallbackUI = new SimpleIMCallbackUIListener() {
        public void Indicate_SearchFileResponse(String str, int i, FileFilterSearchResults fileFilterSearchResults) {
            MMContentSearchFragment.this.Indicate_SearchFileResponse(str, i, fileFilterSearchResults);
        }

        public void Indicate_LocalSearchFileResponse(String str, FileFilterSearchResults fileFilterSearchResults) {
            MMContentSearchFragment.this.Indicate_LocalSearchFileResponse(str, fileFilterSearchResults);
        }
    };
    private boolean mIsOwnerMode = false;
    private View mPanelContentTitle;
    private View mPanelEmptyView;
    @Nullable
    private String mShareReqId;
    private TextView mTxtBlockedByIB;
    private View mTxtContentLoading;
    private View mTxtEmptyView;
    private TextView mTxtLoadingError;
    @Nullable
    private String mUnshareReqId;
    @Nullable
    private WaitingDialog mWaitingDialog;
    private ZMSearchBar mZMSearchBar;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
            MMContentSearchFragment.this.Indicate_PreviewDownloaded(str, str2, i);
        }

        public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
            MMContentSearchFragment.this.Indicate_FileDeleted(str, str2, i);
        }

        public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
            MMContentSearchFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
            MMContentSearchFragment.this.Indicate_FileUnshared(str, str2, i);
        }

        public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
            MMContentSearchFragment.this.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMContentSearchFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void Indicate_FileAttachInfoUpdate(String str, String str2, int i) {
            MMContentSearchFragment.this.Indicate_FileAttachInfoUpdate(str, str2, i);
        }
    };
    private boolean mbIgnoreKeyboardCloseEvent = false;
    private boolean mbKeyboardOpen = false;

    /* renamed from: com.zipow.videobox.view.mm.MMContentSearchFragment$SharerActionContextMenuItem */
    public static class SharerActionContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_JUMP = 0;
        public static final int ACTION_UNSHARE = 1;
        /* access modifiers changed from: private */
        public String mFileId;
        /* access modifiers changed from: private */
        public MMZoomShareAction mShareAction;

        public SharerActionContextMenuItem(String str, int i, String str2, MMZoomShareAction mMZoomShareAction) {
            super(i, str);
            this.mFileId = str2;
            this.mShareAction = mMZoomShareAction;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMContentSearchFragment$UnshareAlertDialog */
    public static class UnshareAlertDialog extends ZMDialogFragment {
        static final String ARG_FILE_ID = "fileId";
        static final String ARG_SHARE_ACTION = "shareAction";
        /* access modifiers changed from: private */
        @Nullable
        public MMZoomShareAction mAction;
        /* access modifiers changed from: private */
        @Nullable
        public String mFileId;

        public static void showUnshareAlertDialog(FragmentManager fragmentManager, String str, @Nullable MMZoomShareAction mMZoomShareAction) {
            if (!StringUtil.isEmptyOrNull(str) && mMZoomShareAction != null) {
                UnshareAlertDialog unshareAlertDialog = new UnshareAlertDialog();
                Bundle bundle = new Bundle();
                bundle.putString(ARG_FILE_ID, str);
                bundle.putSerializable(ARG_SHARE_ACTION, mMZoomShareAction);
                unshareAlertDialog.setArguments(bundle);
                unshareAlertDialog.show(fragmentManager, UnshareAlertDialog.class.getName());
            }
        }

        public UnshareAlertDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                this.mFileId = arguments.getString(ARG_FILE_ID);
                this.mAction = (MMZoomShareAction) arguments.getSerializable(ARG_SHARE_ACTION);
            }
            return new Builder(getActivity()).setTitle((CharSequence) getResources().getString(C4558R.string.zm_msg_delete_file_confirm_89710)).setMessage(C4558R.string.zm_msg_delete_file_warning_89710).setPositiveButton(C4558R.string.zm_btn_delete, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FragmentManager fragmentManager = UnshareAlertDialog.this.getFragmentManager();
                    if (fragmentManager != null) {
                        MMContentSearchFragment mMContentSearchFragment = (MMContentSearchFragment) fragmentManager.findFragmentByTag(MMContentSearchFragment.class.getName());
                        if (mMContentSearchFragment != null) {
                            mMContentSearchFragment.unshareZoomFile(UnshareAlertDialog.this.mFileId, UnshareAlertDialog.this.mAction);
                        }
                    }
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
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

    public static void showAsFragment(Object obj, boolean z) {
        showAsFragment(obj, z, null);
    }

    public static void showAsFragment(Object obj, boolean z, String str) {
        showAsFragment(obj, -1, z, str);
    }

    public static void showAsFragment(Object obj, int i, boolean z, String str) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_CONTENT_MODE, z);
        if (!TextUtils.isEmpty(str)) {
            bundle.putString(ARG_CONENT_FILTER, str);
        }
        if (obj instanceof Fragment) {
            SimpleActivity.show((Fragment) obj, MMContentSearchFragment.class.getName(), bundle, i, 2);
        } else if (obj instanceof ZMActivity) {
            SimpleActivity.show((ZMActivity) obj, MMContentSearchFragment.class.getName(), bundle, i, true);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(getActivity(), true, C4558R.color.zm_im_search_bar_bg);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mIsOwnerMode = arguments.getBoolean(ARG_CONTENT_MODE, false);
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_content_search, viewGroup, false);
        this.mZMSearchBar = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mFilesListView = (MMContentSearchFilesListView) inflate.findViewById(C4558R.C4560id.listViewContentFiles);
        this.mTxtLoadingError = (TextView) inflate.findViewById(C4558R.C4560id.txtLoadingError);
        this.mTxtContentLoading = inflate.findViewById(C4558R.C4560id.txtContentLoading);
        this.mTxtBlockedByIB = (TextView) inflate.findViewById(C4558R.C4560id.txtBlockedByIB);
        this.mPanelEmptyView = inflate.findViewById(C4558R.C4560id.panelEmptyView);
        this.mTxtEmptyView = inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnBack.setOnClickListener(this);
        this.mPanelContentTitle = inflate.findViewById(C4558R.C4560id.panel_listview_content_title);
        this.mBtnSearch = (Button) inflate.findViewById(C4558R.C4560id.btnSearch);
        this.mBtnSearch.setOnClickListener(this);
        this.mFilesListView.setListener(this);
        this.mFilesListView.setPullDownRefreshEnabled(false);
        this.mZMSearchBar.setOnSearchBarListener(new OnSearchBarListener() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onClearSearch() {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMContentSearchFragment.this.mBtnSearch.setVisibility(editable.length() != 0 ? 0 : 8);
            }

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    MMContentSearchFragment.this.startSearch();
                }
                return false;
            }
        });
        this.mTxtLoadingError.setOnClickListener(this);
        this.mTxtLoadingError.setText(Html.fromHtml(getString(C4558R.string.zm_lbl_content_load_error)));
        this.mFilesListView.setIsOwnerMode(this.mIsOwnerMode);
        if (bundle != null) {
            this.mIsOwnerMode = bundle.getBoolean("mIsOwnerMode", false);
            this.mContextMsgReqId = bundle.getString("mContextMsgReqId");
            this.mContextAnchorMsgGUID = bundle.getString("mContextAnchorMsgGUID");
            this.mUnshareReqId = bundle.getString("mUnshareReqId");
            this.mShareReqId = bundle.getString("mShareReqId");
            this.mbIgnoreKeyboardCloseEvent = bundle.getBoolean("mbIgnoreKeyboardCloseEvent");
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        IMCallbackUI.getInstance().addListener(this.mIMCallbackUI);
        return inflate;
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i != 3001) {
            switch (i) {
                case 2014:
                    if (i2 == -1 && intent != null) {
                        Bundle extras = intent.getExtras();
                        if (extras != null) {
                            String string = extras.getString(ARGS_SHARE_FILE_ID);
                            if (!StringUtil.isEmptyOrNull(string)) {
                                String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                                if (!StringUtil.isEmptyOrNull(stringExtra)) {
                                    ArrayList arrayList = new ArrayList();
                                    arrayList.add(stringExtra);
                                    if (arrayList.size() > 0) {
                                        doShareFile(arrayList, string);
                                        break;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    break;
                case 2015:
                    if (i2 == -1 && intent != null) {
                        this.mShareReqId = intent.getStringExtra("reqId");
                        break;
                    }
            }
        } else if (i2 == -1 && intent != null) {
            dealResultForZoomFileViewer(intent.getIntExtra(MMContentFileViewerFragment.RESULT_ACTION, 0), intent.getStringExtra(MMContentFileViewerFragment.RESULT_FILE_WEB_ID), intent.getStringExtra("reqId"));
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        if (bundle == null) {
            getActivity().getWindow().setSoftInputMode(21);
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(ARG_CONENT_FILTER);
            if (!TextUtils.isEmpty(string)) {
                this.mZMSearchBar.setText(string);
                startSearch();
            }
        }
        super.onActivityCreated(bundle);
    }

    public void onDestroyView() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        IMCallbackUI.getInstance().removeListener(this.mIMCallbackUI);
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
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
        MMContentSearchFilesListView mMContentSearchFilesListView = this.mFilesListView;
        if (mMContentSearchFilesListView != null) {
            return mMContentSearchFilesListView.isResultEmpty();
        }
        return true;
    }

    private void updateEmptyViewStatus(boolean z) {
        if (z) {
            this.mPanelContentTitle.setVisibility(this.mFilesListView.isEmpty() ? 8 : 0);
            this.mPanelEmptyView.setVisibility(8);
            return;
        }
        updateEmptyViewStatus();
    }

    private void updateEmptyViewStatus() {
        boolean isEmpty = this.mFilesListView.isEmpty();
        boolean isLoading = this.mFilesListView.isLoading();
        boolean isLoadSuccess = this.mFilesListView.isLoadSuccess();
        int i = 0;
        boolean z = isEmpty & (this.mZMSearchBar.getText().trim().length() != 0);
        this.mPanelEmptyView.setVisibility(z ? 0 : 8);
        this.mPanelContentTitle.setVisibility(z ? 8 : 0);
        if (isLoading) {
            this.mTxtContentLoading.setVisibility(0);
            this.mTxtEmptyView.setVisibility(8);
            this.mTxtLoadingError.setVisibility(8);
            this.mTxtBlockedByIB.setVisibility(8);
            return;
        }
        this.mTxtContentLoading.setVisibility(8);
        if (this.mHasBlockedByIB) {
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

    private void doShareFile(ArrayList<String> arrayList, String str) {
        MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, str, this, 2015);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mIsOwnerMode", this.mIsOwnerMode);
        bundle.putString("mContextMsgReqId", this.mContextMsgReqId);
        bundle.putString("mContextAnchorMsgGUID", this.mContextAnchorMsgGUID);
        bundle.putString("mUnshareReqId", this.mUnshareReqId);
        bundle.putString("mShareReqId", this.mShareReqId);
        bundle.putBoolean("mbIgnoreKeyboardCloseEvent", this.mbIgnoreKeyboardCloseEvent);
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    private void dealResultForZoomFileViewer(int i, @Nullable String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str) && i == 1) {
            Indicate_FileDeleted(str2, str, 0);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
        if (StringUtil.isSameString(str, this.mShareReqId)) {
            this.mFilesListView.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
        if (StringUtil.isSameString(str, this.mUnshareReqId)) {
            this.mFilesListView.Indicate_FileUnshared(str, str2, i);
        }
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        this.mFilesListView.onIndicateInfoUpdatedWithJID(str);
    }

    public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
        if (i == 1) {
            this.mFilesListView.Indicate_FileDeleted(null, str, 0);
        } else if (i == 2) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID == null) {
                    this.mFilesListView.Indicate_FileDeleted(null, str, 0);
                } else {
                    MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                    if (initWithZoomFile.getShareAction() == null || initWithZoomFile.getShareAction().size() == 0) {
                        this.mFilesListView.Indicate_FileDeleted(null, str, 0);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
        this.mFilesListView.Indicate_FileDeleted(str, str2, i);
    }

    public void Indicate_SearchFileResponse(String str, int i, FileFilterSearchResults fileFilterSearchResults) {
        updateEmptyViewStatus(this.mFilesListView.Indicate_SearchFileResponse(str, i, fileFilterSearchResults));
    }

    public void Indicate_LocalSearchFileResponse(String str, FileFilterSearchResults fileFilterSearchResults) {
        updateEmptyViewStatus(this.mFilesListView.Indicate_LocalSearchFileResponse(str, fileFilterSearchResults));
    }

    public void Indicate_FileAttachInfoUpdate(String str, String str2, int i) {
        this.mFilesListView.Indicate_FileAttachInfoUpdate(str, str2, i);
    }

    public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
        this.mFilesListView.Indicate_PreviewDownloaded(str, str2, i);
    }

    private void showWaitDialog() {
        this.mWaitingDialog = WaitingDialog.newInstance(getString(C4558R.string.zm_msg_waiting));
        this.mWaitingDialog.setCancelable(true);
        this.mWaitingDialog.show(getFragmentManager(), "WaitingDialog");
    }

    private void dismissWaitingDialog() {
        if (this.mWaitingDialog == null) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                this.mWaitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag("WaitingDialog");
            } else {
                return;
            }
        }
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null) {
            waitingDialog.dismissAllowingStateLoss();
        }
        this.mWaitingDialog = null;
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.txtLoadingError) {
                onClickTxtLoadingError();
            } else if (id == C4558R.C4560id.btnBack) {
                onClickBtnBack();
            } else if (id == C4558R.C4560id.btnSearch) {
                onClickBtnSearch();
            }
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickTxtLoadingError() {
        if (!this.mFilesListView.isLoadSuccess()) {
            this.mFilesListView.searchContent(null);
        }
        updateEmptyViewStatus();
    }

    private void onClickBtnSearch() {
        startSearch();
    }

    public void onZoomFileClick(String str, @Nullable List<String> list) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (list == null || list.isEmpty()) {
                onZoomFileClick(str);
            } else {
                MMContentsViewerFragment.showAsActivity(this, str, list, 3001);
            }
        }
    }

    public void onZoomFileClick(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MMContentFileViewerFragment.showAsActivity((Fragment) this, str, 3001);
        }
    }

    public void onZoomFileShared(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString(ARGS_SHARE_FILE_ID, str);
            MMSelectSessionAndBuddyFragment.showAsFragment(this, bundle, false, false, 2014);
        }
    }

    public void onZoomFileCancelTransfer(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            PendingFileInfo downloadPendingInfoByWebFileId = PendingFileDataHelper.getInstance().getDownloadPendingInfoByWebFileId(str);
            if (downloadPendingInfoByWebFileId != null) {
                String str2 = downloadPendingInfoByWebFileId.reqId;
                if (!StringUtil.isEmptyOrNull(str2)) {
                    MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                    if (zoomFileContentMgr != null && zoomFileContentMgr.cancelFileTransfer(str2, str)) {
                        this.mFilesListView.endFileTransfer(str);
                        PendingFileDataHelper.getInstance().removeDownloadPendingFile(str);
                    }
                }
            }
        }
    }

    public void onZoomFileSharerAction(String str, @Nullable MMZoomShareAction mMZoomShareAction, final boolean z, boolean z2) {
        if (!StringUtil.isEmptyOrNull(str) && mMZoomShareAction != null) {
            if (!NetworkUtil.hasDataNetwork(getActivity())) {
                showConnectionError();
                return;
            }
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new SharerActionContextMenuItem(getString(C4558R.string.zm_btn_jump_group_59554), 0, str, mMZoomShareAction));
            if (z2) {
                arrayList.add(new SharerActionContextMenuItem(getString(C4558R.string.zm_btn_unshare_group_59554), 1, str, mMZoomShareAction));
            }
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            TextView textView = new TextView(getActivity());
            if (VERSION.SDK_INT < 23) {
                textView.setTextAppearance(getActivity(), C4558R.style.ZMTextView_Medium);
            } else {
                textView.setTextAppearance(C4558R.style.ZMTextView_Medium);
            }
            int dip2px = UIUtil.dip2px(getActivity(), 20.0f);
            textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    String fileName = fileWithWebFileID.getFileName();
                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                    textView.setText(getString(C4558R.string.zm_title_sharer_action, fileName, mMZoomShareAction.getShareeName(getActivity())));
                    ZMAlertDialog create = new Builder(getActivity()).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MMContentSearchFragment.this.onSelectSharerActionContextMenuItem((SharerActionContextMenuItem) zMMenuAdapter.getItem(i), z);
                        }
                    }).create();
                    create.setCanceledOnTouchOutside(true);
                    create.show();
                }
            }
        }
    }

    public void onZoomFileIntegrationClick(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ZMIMUtils.openUrl(getActivity(), str);
        }
    }

    /* access modifiers changed from: private */
    public void onSelectSharerActionContextMenuItem(@Nullable SharerActionContextMenuItem sharerActionContextMenuItem, boolean z) {
        if (sharerActionContextMenuItem != null) {
            switch (sharerActionContextMenuItem.getAction()) {
                case 0:
                    jumpToChat(sharerActionContextMenuItem.mShareAction.getSharee());
                    break;
                case 1:
                    UnshareAlertDialogFragment.showUnshareAlertDialog(getFragmentManager(), sharerActionContextMenuItem.mFileId, sharerActionContextMenuItem.mShareAction, z);
                    break;
            }
        }
    }

    private void jumpToChat(String str) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    if (sessionById.isGroup()) {
                        ZoomGroup sessionGroup = sessionById.getSessionGroup();
                        if (sessionGroup != null) {
                            String groupID = sessionGroup.getGroupID();
                            if (!StringUtil.isEmptyOrNull(groupID)) {
                                MMChatActivity.showAsGroupChat(zMActivity, groupID);
                            }
                        }
                    } else {
                        ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                        if (sessionBuddy == null) {
                            if (UIMgr.isMyNotes(str)) {
                                sessionBuddy = zoomMessenger.getMyself();
                            }
                            if (sessionBuddy == null) {
                                return;
                            }
                        }
                        MMChatActivity.showAsOneToOneChat(zMActivity, sessionBuddy);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void unshareZoomFile(String str, @Nullable MMZoomShareAction mMZoomShareAction) {
        if (!StringUtil.isEmptyOrNull(str) && mMZoomShareAction != null) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(mMZoomShareAction.getSharee());
                this.mUnshareReqId = zoomFileContentMgr.unshareFile(str, arrayList);
                if (StringUtil.isEmptyOrNull(this.mUnshareReqId)) {
                    handelFileUnsharedErrorCode(-1);
                }
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 0).show();
        }
    }

    private void handelFileUnsharedErrorCode(int i) {
        if (i != 0) {
            ErrorMsgDialog.newInstance(getString(C4558R.string.zm_alert_unshare_file_failed), -1).show(getFragmentManager(), ErrorMsgDialog.class.getName());
        }
    }

    /* access modifiers changed from: private */
    public void startSearch() {
        String trim = this.mZMSearchBar.getText().trim();
        if (!StringUtil.isEmptyOrNull(trim)) {
            this.mFilesListView.setFilter(trim, null);
            updateEmptyViewStatus();
            this.mbIgnoreKeyboardCloseEvent = true;
            UIUtil.closeSoftKeyboard(getActivity(), this.mZMSearchBar.getEditText());
        }
    }
}
