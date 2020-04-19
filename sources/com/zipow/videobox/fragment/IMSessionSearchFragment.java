package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMCallbackUI.IIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI.SimpleIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMProtos.FileFilterSearchResults;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchResponse;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
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
import com.zipow.videobox.view.p014mm.MMContentFileViewerFragment;
import com.zipow.videobox.view.p014mm.MMContentSearchFilesListView;
import com.zipow.videobox.view.p014mm.MMContentSearchMessagesListView;
import com.zipow.videobox.view.p014mm.MMShareZoomFileDialogFragment;
import com.zipow.videobox.view.p014mm.MMZoomFile;
import com.zipow.videobox.view.p014mm.MMZoomShareAction;
import com.zipow.videobox.view.p014mm.OnContentFileOperatorListener;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper.PendingFileInfo;
import com.zipow.videobox.view.p014mm.UnshareAlertDialogFragment;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class IMSessionSearchFragment extends ZMDialogFragment implements ExtListener, OnClickListener, OnContentFileOperatorListener {
    private static final String ARGS_SHARE_FILE_ID = "shareFileId";
    @NonNull
    public static final String ARG_CONTENT_MODE = "content_mode";
    @NonNull
    public static final String ARG_MESSAGE_FIRST = "message_first";
    @NonNull
    public static final String ARG_SESSION_ID = "session_id";
    public static final int REQUEST_DO_SHAREER = 2015;
    public static final int REQUEST_GET_SHAREER = 2014;
    public static final int REQUEST_VIEW_FILE_DETAIL = 3001;
    public static final String TAG = "IMSessionSearchFragment";
    private static final int UI_MODE_FILES = 1;
    private static final int UI_MODE_MESSAGES = 2;
    /* access modifiers changed from: private */
    public ImageButton mBtnClearSearchView;
    private TextView mBtnSortBy;
    @Nullable
    private String mContextAnchorMsgGUID;
    @Nullable
    private String mContextMsgReqId;
    private EditText mEdtSearch;
    /* access modifiers changed from: private */
    public MMContentSearchFilesListView mFilesListView;
    private Runnable mFilterRunnabel;
    private Handler mHandler = new Handler();
    private boolean mHasBlockedByIBFiles = false;
    private boolean mHasBlockedByIBMsgs = false;
    @NonNull
    private IIMCallbackUIListener mIMCallbackUIListener = new SimpleIMCallbackUIListener() {
        public void Indicate_SearchMessageResponse(String str, int i, @Nullable MessageContentSearchResponse messageContentSearchResponse) {
            IMSessionSearchFragment.this.Indicate_SearchMessageResponse(str, i, messageContentSearchResponse);
        }

        public void Indicate_SearchFileResponse(String str, int i, FileFilterSearchResults fileFilterSearchResults) {
            IMSessionSearchFragment.this.Indicate_SearchFileResponse(str, i, fileFilterSearchResults);
        }

        public void Indicate_LocalSearchMSGResponse(String str, MessageContentSearchResponse messageContentSearchResponse) {
            IMSessionSearchFragment.this.Indicate_LocalSearchMSGResponse(str, messageContentSearchResponse);
        }

        public void Indicate_LocalSearchFileResponse(String str, FileFilterSearchResults fileFilterSearchResults) {
            IMSessionSearchFragment.this.Indicate_LocalSearchFileResponse(str, fileFilterSearchResults);
        }
    };
    private boolean mIsMessageFirst = false;
    private boolean mIsOwnerMode = false;
    /* access modifiers changed from: private */
    public MMContentSearchMessagesListView mMessagesListView;
    private View mPanelEmptyView;
    private View mPanelFiles;
    private View mPanelMessages;
    private View mPanelSortBy;
    private View mPanelTitleBar;
    private int mResultSortType = ZMIMUtils.getSearchMessageSortType();
    @Nullable
    private String mShareReqId;
    private TextView mTxtBlockedByIBFiles;
    private TextView mTxtBlockedByIBMsg;
    private View mTxtContentLoading;
    private View mTxtEmptyView;
    private TextView mTxtLoadingError;
    /* access modifiers changed from: private */
    public TextView mTxtTabFilesLabel;
    /* access modifiers changed from: private */
    public TextView mTxtTabMessagesLabel;
    private int mUIMode = 1;
    @Nullable
    private String mUnshareReqId;
    @Nullable
    private WaitingDialog mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_PreviewDownloaded(String str, @NonNull String str2, int i) {
            IMSessionSearchFragment.this.Indicate_PreviewDownloaded(str, str2, i);
        }

        public void Indicate_FileDeleted(String str, @NonNull String str2, int i) {
            IMSessionSearchFragment.this.Indicate_FileDeleted(str, str2, i);
        }

        public void Indicate_FileShared(String str, @NonNull String str2, String str3, String str4, String str5, int i) {
            IMSessionSearchFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileUnshared(String str, @NonNull String str2, int i) {
            IMSessionSearchFragment.this.Indicate_FileUnshared(str, str2, i);
        }

        public void Indicate_FileActionStatus(int i, @NonNull String str, String str2, String str3, String str4, String str5) {
            IMSessionSearchFragment.this.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }

        public void Indicate_FileAttachInfoUpdate(String str, String str2, int i) {
            IMSessionSearchFragment.this.Indicate_FileAttachInfoUpdate(str, str2, i);
        }
    };
    /* access modifiers changed from: private */
    public boolean mbIgnoreKeyboardCloseEvent = false;
    private boolean mbKeyboardOpen = false;
    /* access modifiers changed from: private */
    @Nullable
    public String sessionId = null;

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

    static class SortByMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_MOST_RECENT = 1;
        public static final int ACTION_MOST_RELEVANT = 0;

        public SortByMenuItem(String str, int i, boolean z) {
            super(i, str, null, z);
        }
    }

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
                        IMSessionSearchFragment iMSessionSearchFragment = (IMSessionSearchFragment) fragmentManager.findFragmentByTag(IMSessionSearchFragment.class.getName());
                        if (iMSessionSearchFragment != null) {
                            iMSessionSearchFragment.unshareZoomFile(UnshareAlertDialog.this.mFileId, UnshareAlertDialog.this.mAction);
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

    public void onZoomFileClick(String str, List<String> list) {
    }

    public static void showAsFragment(Fragment fragment, String str, int i, boolean z, boolean z2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_CONTENT_MODE, z);
        bundle.putBoolean(ARG_MESSAGE_FIRST, z2);
        bundle.putString("session_id", str);
        SimpleActivity.show(fragment, IMSessionSearchFragment.class.getName(), bundle, i, 2);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mIsOwnerMode = arguments.getBoolean(ARG_CONTENT_MODE, false);
            this.mIsMessageFirst = arguments.getBoolean(ARG_MESSAGE_FIRST, false);
            this.sessionId = arguments.getString("session_id");
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_im_session_search_fragment, viewGroup, false);
        this.mPanelSortBy = inflate.findViewById(C4558R.C4560id.panel_sort_by);
        this.mBtnSortBy = (TextView) inflate.findViewById(C4558R.C4560id.sort_by_button);
        if (this.mResultSortType == 2) {
            this.mBtnSortBy.setText(C4558R.string.zm_lbl_search_sort_by_relevant_119637);
        } else {
            this.mBtnSortBy.setText(C4558R.string.zm_lbl_search_sort_by_recent_119637);
        }
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnClearSearchView = (ImageButton) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mFilesListView = (MMContentSearchFilesListView) inflate.findViewById(C4558R.C4560id.listViewContentFiles);
        this.mMessagesListView = (MMContentSearchMessagesListView) inflate.findViewById(C4558R.C4560id.listViewContentMessages);
        if (this.mIsMessageFirst) {
            this.mPanelFiles = inflate.findViewById(C4558R.C4560id.panelMessages);
            this.mPanelMessages = inflate.findViewById(C4558R.C4560id.panelFiles);
            this.mTxtTabFilesLabel = (TextView) inflate.findViewById(C4558R.C4560id.txtTabMessagesLabel);
            this.mTxtTabMessagesLabel = (TextView) inflate.findViewById(C4558R.C4560id.txtTabFilesLabel);
            this.mTxtTabFilesLabel.setText(C4558R.string.zm_tab_content_search_contents_115433);
            this.mTxtTabMessagesLabel.setText(C4558R.string.zm_tab_content_search_messages);
            this.mEdtSearch.setHint(C4558R.string.zm_hint_search_messages_18680);
            this.mUIMode = 2;
        } else {
            this.mPanelFiles = inflate.findViewById(C4558R.C4560id.panelFiles);
            this.mPanelMessages = inflate.findViewById(C4558R.C4560id.panelMessages);
            this.mTxtTabFilesLabel = (TextView) inflate.findViewById(C4558R.C4560id.txtTabFilesLabel);
            this.mTxtTabMessagesLabel = (TextView) inflate.findViewById(C4558R.C4560id.txtTabMessagesLabel);
            this.mEdtSearch.setHint(C4558R.string.zm_hint_search_content_67667);
            this.mUIMode = 1;
        }
        this.mTxtLoadingError = (TextView) inflate.findViewById(C4558R.C4560id.txtLoadingError);
        this.mTxtContentLoading = inflate.findViewById(C4558R.C4560id.txtContentLoading);
        this.mTxtBlockedByIBFiles = (TextView) inflate.findViewById(C4558R.C4560id.txtBlockedByIBFile);
        this.mTxtBlockedByIBMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtBlockedByIBMsg);
        this.mPanelEmptyView = inflate.findViewById(C4558R.C4560id.panelEmptyView);
        this.mTxtEmptyView = inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mPanelTitleBar.setVisibility(PTApp.getInstance().isFileTransferDisabled() ? 8 : 0);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mFilesListView.setListener(this);
        this.mMessagesListView.setParentFragment(this);
        this.mFilesListView.setPullDownRefreshEnabled(false);
        this.mEdtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    IMSessionSearchFragment.this.startSearch();
                }
                return false;
            }
        });
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                IMSessionSearchFragment.this.mBtnClearSearchView.setVisibility(editable.length() != 0 ? 0 : 8);
                IMSessionSearchFragment.this.startSearch();
            }
        });
        this.mBtnSortBy.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mPanelFiles.setOnClickListener(this);
        this.mPanelMessages.setOnClickListener(this);
        this.mTxtLoadingError.setOnClickListener(this);
        this.mTxtLoadingError.setText(Html.fromHtml(getString(C4558R.string.zm_lbl_content_load_error)));
        this.mFilesListView.setIsOwnerMode(this.mIsOwnerMode);
        if (bundle != null) {
            this.mUIMode = bundle.getInt("uiMode", 1);
            this.mIsOwnerMode = bundle.getBoolean("mIsOwnerMode", false);
            this.mContextMsgReqId = bundle.getString("mContextMsgReqId");
            this.mContextAnchorMsgGUID = bundle.getString("mContextAnchorMsgGUID");
            this.mUnshareReqId = bundle.getString("mUnshareReqId");
            this.mShareReqId = bundle.getString("mShareReqId");
            this.mbIgnoreKeyboardCloseEvent = bundle.getBoolean("mbIgnoreKeyboardCloseEvent");
            int i = bundle.getInt("mPanelTitleBar", -1);
            if (i != -1) {
                this.mPanelTitleBar.setVisibility(i);
            }
        }
        updateUIMode();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        IMCallbackUI.getInstance().addListener(this.mIMCallbackUIListener);
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
            int intExtra = intent.getIntExtra(MMContentFileViewerFragment.RESULT_ACTION, 0);
            String stringExtra2 = intent.getStringExtra(MMContentFileViewerFragment.RESULT_FILE_WEB_ID);
            String stringExtra3 = intent.getStringExtra("reqId");
            if (stringExtra2 != null) {
                dealResultForZoomFileViewer(intExtra, stringExtra2, stringExtra3);
            }
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        if (bundle == null) {
            getActivity().getWindow().setSoftInputMode(21);
        }
        super.onActivityCreated(bundle);
    }

    public void onDestroyView() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        IMCallbackUI.getInstance().removeListener(this.mIMCallbackUIListener);
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
    }

    public void openSoftKeyboard() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    public void onKeyboardOpen() {
        this.mbKeyboardOpen = true;
        this.mbIgnoreKeyboardCloseEvent = false;
        this.mEdtSearch.requestFocus();
    }

    public void onKeyboardClosed() {
        if (this.mbKeyboardOpen) {
            this.mbKeyboardOpen = false;
        }
    }

    private boolean isResultEmpty() {
        MMContentSearchFilesListView mMContentSearchFilesListView = this.mFilesListView;
        boolean isResultEmpty = mMContentSearchFilesListView != null ? mMContentSearchFilesListView.isResultEmpty() : true;
        MMContentSearchMessagesListView mMContentSearchMessagesListView = this.mMessagesListView;
        return mMContentSearchMessagesListView != null ? isResultEmpty & mMContentSearchMessagesListView.isResultEmpty() : isResultEmpty;
    }

    private void checkContentFileIsEmpty() {
        if (!this.mMessagesListView.isLoading() && !this.mFilesListView.isLoading()) {
            if ((!this.mFilesListView.isLoadSuccess() || this.mFilesListView.isEmpty()) && this.mMessagesListView.isLoadSuccess() && !this.mMessagesListView.isEmpty()) {
                this.mUIMode = 2;
                updateUIMode();
                updateEmptyViewStatus();
            }
        }
    }

    private void updateEmptyViewStatus(boolean z) {
        if (z) {
            int i = 8;
            this.mPanelEmptyView.setVisibility(8);
            boolean z2 = this.mUIMode == 2;
            boolean isEmpty = this.mMessagesListView.isEmpty();
            View view = this.mPanelSortBy;
            if (!isEmpty && z2) {
                i = 0;
            }
            view.setVisibility(i);
        } else {
            updateEmptyViewStatus();
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mMessagesListView.setBackgroundColor(ContextCompat.getColor(activity, C4558R.color.zm_white));
            this.mFilesListView.setBackgroundColor(ContextCompat.getColor(activity, C4558R.color.zm_white));
        }
    }

    /* access modifiers changed from: private */
    public void updateEmptyViewStatus() {
        boolean z;
        boolean z2;
        boolean z3;
        int i = 0;
        if (this.mUIMode == 1) {
            z3 = this.mFilesListView.isEmpty();
            z2 = this.mFilesListView.isLoading();
            z = this.mFilesListView.isLoadSuccess();
            this.mPanelSortBy.setVisibility(8);
        } else {
            z3 = this.mMessagesListView.isEmpty();
            z2 = this.mMessagesListView.isLoading();
            z = this.mMessagesListView.isLoadSuccess();
            this.mPanelSortBy.setVisibility(z3 ? 8 : 0);
        }
        this.mPanelEmptyView.setVisibility(z3 & (this.mEdtSearch.getText().toString().trim().length() != 0) ? 0 : 8);
        if (z2) {
            this.mTxtContentLoading.setVisibility(0);
            this.mTxtEmptyView.setVisibility(8);
            this.mTxtLoadingError.setVisibility(8);
            this.mTxtBlockedByIBMsg.setVisibility(8);
            return;
        }
        this.mTxtContentLoading.setVisibility(8);
        if (this.mUIMode == 1) {
            if (this.mHasBlockedByIBFiles) {
                this.mTxtBlockedByIBMsg.setVisibility(8);
                this.mTxtBlockedByIBFiles.setVisibility(0);
                this.mTxtEmptyView.setVisibility(8);
                this.mTxtLoadingError.setVisibility(8);
                return;
            }
            this.mTxtBlockedByIBMsg.setVisibility(8);
            this.mTxtBlockedByIBFiles.setVisibility(8);
            this.mTxtEmptyView.setVisibility(z ? 0 : 8);
            TextView textView = this.mTxtLoadingError;
            if (z) {
                i = 8;
            }
            textView.setVisibility(i);
        } else if (this.mHasBlockedByIBMsgs) {
            this.mTxtBlockedByIBMsg.setVisibility(0);
            this.mTxtBlockedByIBFiles.setVisibility(8);
            this.mTxtEmptyView.setVisibility(8);
            this.mTxtLoadingError.setVisibility(8);
        } else {
            this.mTxtBlockedByIBMsg.setVisibility(8);
            this.mTxtBlockedByIBFiles.setVisibility(8);
            this.mTxtEmptyView.setVisibility(z ? 0 : 8);
            TextView textView2 = this.mTxtLoadingError;
            if (z) {
                i = 8;
            }
            textView2.setVisibility(i);
        }
    }

    private void doShareFile(ArrayList<String> arrayList, String str) {
        MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, str, this, 2015);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("uiMode", this.mUIMode);
        bundle.putBoolean("mIsOwnerMode", this.mIsOwnerMode);
        bundle.putString("mContextMsgReqId", this.mContextMsgReqId);
        bundle.putString("mContextAnchorMsgGUID", this.mContextAnchorMsgGUID);
        bundle.putString("mUnshareReqId", this.mUnshareReqId);
        bundle.putString("mShareReqId", this.mShareReqId);
        bundle.putInt("mPanelTitleBar", this.mPanelTitleBar.getVisibility());
        bundle.putBoolean("mbIgnoreKeyboardCloseEvent", this.mbIgnoreKeyboardCloseEvent);
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    private void dealResultForZoomFileViewer(int i, @NonNull String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str) && i == 1) {
            Indicate_FileDeleted(str2, str, 0);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileShared(String str, @NonNull String str2, String str3, String str4, String str5, int i) {
        if (StringUtil.isSameString(str, this.mShareReqId)) {
            this.mFilesListView.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileUnshared(String str, @NonNull String str2, int i) {
        if (StringUtil.isSameString(str, this.mUnshareReqId)) {
            this.mFilesListView.Indicate_FileUnshared(str, str2, i);
        }
    }

    public void Indicate_FileActionStatus(int i, @NonNull String str, String str2, String str3, String str4, String str5) {
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
    public void Indicate_FileDeleted(String str, @NonNull String str2, int i) {
        this.mFilesListView.Indicate_FileDeleted(str, str2, i);
    }

    public void Indicate_SearchFileResponse(String str, int i, @Nullable FileFilterSearchResults fileFilterSearchResults) {
        updateEmptyViewStatus(this.mFilesListView.Indicate_SearchFileResponse(str, i, fileFilterSearchResults));
        checkContentFileIsEmpty();
        if (i == 0 && fileFilterSearchResults != null) {
            this.mTxtTabFilesLabel.setText(String.format("%s(%d)", new Object[]{getString(C4558R.string.zm_tab_content_search_contents_115433), Integer.valueOf(this.mFilesListView.getTotalCount())}));
        }
    }

    public void Indicate_LocalSearchFileResponse(String str, @Nullable FileFilterSearchResults fileFilterSearchResults) {
        updateEmptyViewStatus(this.mFilesListView.Indicate_LocalSearchFileResponse(str, fileFilterSearchResults));
        checkContentFileIsEmpty();
        if (fileFilterSearchResults != null) {
            this.mTxtTabFilesLabel.setText(String.format("%s(%d)", new Object[]{getString(C4558R.string.zm_tab_content_search_contents_115433), Integer.valueOf(this.mFilesListView.getTotalCount())}));
        }
    }

    public void Indicate_SearchMessageResponse(String str, int i, @Nullable MessageContentSearchResponse messageContentSearchResponse) {
        if (messageContentSearchResponse == null || messageContentSearchResponse.getSearchResponseCount() <= 0 || PTApp.getInstance().getZoomMessenger() != null) {
            updateEmptyViewStatus(this.mMessagesListView.Indicate_SearchMessageResponse(str, i, messageContentSearchResponse));
            checkContentFileIsEmpty();
            if (i == 0 && messageContentSearchResponse != null) {
                this.mTxtTabMessagesLabel.setText(String.format("%s(%d)", new Object[]{getString(C4558R.string.zm_tab_content_search_messages), Integer.valueOf(this.mMessagesListView.getTotalCount())}));
            }
        }
    }

    public void Indicate_LocalSearchMSGResponse(String str, @Nullable MessageContentSearchResponse messageContentSearchResponse) {
        if (messageContentSearchResponse == null || messageContentSearchResponse.getSearchResponseCount() <= 0 || PTApp.getInstance().getZoomMessenger() != null) {
            updateEmptyViewStatus(this.mMessagesListView.Indicate_LocalSearchMSGResponse(str, messageContentSearchResponse));
            checkContentFileIsEmpty();
            if (messageContentSearchResponse != null) {
                this.mTxtTabMessagesLabel.setText(String.format("%s(%d)", new Object[]{getString(C4558R.string.zm_tab_content_search_messages), Integer.valueOf(this.mMessagesListView.getTotalCount())}));
            }
        }
    }

    public void Indicate_PreviewDownloaded(String str, @NonNull String str2, int i) {
        this.mFilesListView.Indicate_PreviewDownloaded(str, str2, i);
    }

    public void Indicate_FileAttachInfoUpdate(String str, String str2, int i) {
        this.mFilesListView.Indicate_FileAttachInfoUpdate(str, str2, i);
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

    private void updateUIMode() {
        int i = 0;
        switch (this.mUIMode) {
            case 1:
                this.mPanelMessages.setSelected(false);
                this.mPanelFiles.setSelected(true);
                this.mMessagesListView.setVisibility(8);
                this.mPanelSortBy.setVisibility(8);
                this.mFilesListView.setVisibility(0);
                return;
            case 2:
                this.mPanelMessages.setSelected(true);
                this.mPanelFiles.setSelected(false);
                this.mMessagesListView.setVisibility(0);
                View view = this.mPanelSortBy;
                if (this.mMessagesListView.isResultEmpty()) {
                    i = 8;
                }
                view.setVisibility(i);
                this.mFilesListView.setVisibility(8);
                return;
            default:
                return;
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnCancel) {
                onClickBtnCancel();
            } else if (id == C4558R.C4560id.btnClearSearchView) {
                onClickBtnClearSearchView();
            } else if (id == C4558R.C4560id.txtLoadingError) {
                onClickTxtLoadingError();
            }
            if (view == this.mPanelFiles) {
                onClickPanelContents();
            } else if (view == this.mPanelMessages) {
                onClickPanelMessages();
            } else if (view == this.mBtnSortBy) {
                onClickBtnSortBy();
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
                    IMSessionSearchFragment.this.onSortTypeChanged((SortByMenuItem) zMMenuAdapter.getItem(i));
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
            this.mBtnSortBy.setText(C4558R.string.zm_lbl_search_sort_by_relevant_119637);
        } else if (action == 1) {
            this.mBtnSortBy.setText(C4558R.string.zm_lbl_search_sort_by_recent_119637);
            i = 1;
        }
        if (i != this.mResultSortType) {
            this.mResultSortType = i;
            this.mMessagesListView.setSortType(i);
            ZMIMUtils.setSearchMessageSortType(i);
            startSearch();
        }
    }

    private void onClickTxtLoadingError() {
        if (!this.mFilesListView.isLoadSuccess()) {
            this.mFilesListView.searchContent(this.sessionId);
        }
        if (!this.mMessagesListView.isLoadSuccess()) {
            this.mMessagesListView.searchMessage(this.sessionId);
        }
        updateEmptyViewStatus();
    }

    private void onClickPanelMessages() {
        this.mUIMode = 2;
        updateUIMode();
        updateEmptyViewStatus();
    }

    private void onClickPanelContents() {
        this.mUIMode = 1;
        updateUIMode();
        updateEmptyViewStatus();
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
    }

    private void onClickBtnCancel() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().hide(this).commit();
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

    public void onZoomFileCancelTransfer(@NonNull String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            PendingFileInfo downloadPendingInfoByWebFileId = PendingFileDataHelper.getInstance().getDownloadPendingInfoByWebFileId(str);
            if (downloadPendingInfoByWebFileId != null) {
                String reqId = downloadPendingInfoByWebFileId.getReqId();
                if (!StringUtil.isEmptyOrNull(reqId)) {
                    MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                    if (zoomFileContentMgr != null && zoomFileContentMgr.cancelFileTransfer(reqId, str)) {
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
                            IMSessionSearchFragment.this.onSelectSharerActionContextMenuItem((SharerActionContextMenuItem) zMMenuAdapter.getItem(i), z);
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

    public void onShow() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            EditText editText = this.mEdtSearch;
            if (editText != null) {
                editText.setText("");
                this.mMessagesListView.clearSearch();
                this.mFilesListView.clearSearch();
                this.mTxtTabFilesLabel.setText(C4558R.string.zm_tab_content_search_contents_115433);
                this.mTxtTabMessagesLabel.setText(C4558R.string.zm_tab_content_search_messages);
                this.mMessagesListView.setBackgroundColor(ContextCompat.getColor(activity, C4558R.color.zm_transparent));
                this.mFilesListView.setBackgroundColor(ContextCompat.getColor(activity, C4558R.color.zm_transparent));
                this.mPanelSortBy.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void startSearch() {
        final String trim = this.mEdtSearch.getText().toString().trim();
        if (!StringUtil.isEmptyOrNull(trim)) {
            Runnable runnable = this.mFilterRunnabel;
            if (runnable != null) {
                this.mHandler.removeCallbacks(runnable);
            }
            this.mFilterRunnabel = new Runnable() {
                public void run() {
                    if (!TextUtils.isEmpty(trim)) {
                        trim.toLowerCase(CompatUtils.getLocalDefault());
                    } else {
                        String str = trim;
                    }
                    FragmentActivity activity = IMSessionSearchFragment.this.getActivity();
                    if (activity != null) {
                        IMSessionSearchFragment.this.mFilesListView.setFilter(trim, IMSessionSearchFragment.this.sessionId);
                        IMSessionSearchFragment.this.mMessagesListView.setFilter(trim, IMSessionSearchFragment.this.sessionId);
                        IMSessionSearchFragment.this.mMessagesListView.setBackgroundColor(ContextCompat.getColor(activity, C4558R.color.zm_white));
                        IMSessionSearchFragment.this.mFilesListView.setBackgroundColor(ContextCompat.getColor(activity, C4558R.color.zm_white));
                        IMSessionSearchFragment.this.updateEmptyViewStatus();
                        IMSessionSearchFragment.this.mbIgnoreKeyboardCloseEvent = true;
                        IMSessionSearchFragment.this.mTxtTabFilesLabel.setText(C4558R.string.zm_tab_content_search_contents_115433);
                        IMSessionSearchFragment.this.mTxtTabMessagesLabel.setText(C4558R.string.zm_tab_content_search_messages);
                        ZoomLogEventTracking.eventTrackSearch(trim, IMSessionSearchFragment.this.sessionId);
                        ZoomLogEventTracking.eventTrackSearch();
                    }
                }
            };
            this.mHandler.postDelayed(this.mFilterRunnabel, 300);
        }
    }
}
