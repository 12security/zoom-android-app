package com.zipow.videobox.view.p014mm;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.dialog.ShareAlertDialog;
import com.zipow.videobox.fragment.IMSearchFragment;
import com.zipow.videobox.fragment.MMSelectSessionAndBuddyFragment;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.ptapp.AutoStreamConflictChecker;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.FileInfoChecker;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZMUtils;
import com.zipow.videobox.view.IMView.OnFragmentShowListener;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper.PendingFileInfo;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFileListActivity;
import p021us.zoom.androidlib.app.ZMLocalFileListAdapter;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IDownloadFileListener;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMPopupWindow;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.thirdparty.box.BoxFileListAdapter;
import p021us.zoom.thirdparty.dropbox.DropboxFileListAdapter;
import p021us.zoom.thirdparty.dropbox.ZMDropbox;
import p021us.zoom.thirdparty.googledrive.GoogleDrive;
import p021us.zoom.thirdparty.googledrive.GoogleDriveFileListAdapter;
import p021us.zoom.thirdparty.login.util.IPicker;
import p021us.zoom.thirdparty.login.util.IPickerResult;
import p021us.zoom.thirdparty.onedrive.OneDriveBusinessFileListAdapter;
import p021us.zoom.thirdparty.onedrive.OneDriveFileListAdapter;
import p021us.zoom.thirdparty.onedrive.OneDrivePicker;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentFragment */
public class MMContentFragment extends ZMDialogFragment implements OnClickListener, OnFragmentShowListener, OnContentFileOperatorListener {
    private static final String ARGS_SHARE_FILE_ID = "shareFileId";
    private static final String EXTRA_FILE_TYPE = "fileType";
    private static final String EXTRA_UI_MODE = "uiMode";
    private static final String EXTRA_UPLOAD_REQUEST_IDS = "requestIds";
    private static final int FILE_TYPE_FILES = 2;
    private static final int FILE_TYPE_IMAGES = 1;
    public static final int REQUEST_CHOOSE_PICTURE = 1004;
    public static final int REQUEST_DOCUMENT = 1010;
    public static final int REQUEST_DOCUMENT_BUSIENSS_PICKER = 1015;
    public static final int REQUEST_DOCUMENT_PICKER = 1014;
    public static final int REQUEST_GET_SHAREER = 2014;
    private static final int REQUEST_PERMISSION_BY_WRITE_EXTERNAL_STORAGE = 10000;
    public static final int REQUEST_UPLOAD_FAILED = 3002;
    public static final int REQUEST_VIEW_FILE_DETAIL = 3001;
    private static final String TAG = "MMContentFragment";
    private static final int UI_MODE_PERSONAL = 1;
    private static final int UI_MODE_SHARED = 0;
    private Button mBtnBack;
    private View mBtnFilter;
    private ImageButton mBtnSearch;
    private ImageButton mBtnUploadFile;
    private ImageView mDownArrow;
    /* access modifiers changed from: private */
    @Nullable
    public ProgressDialog mDownloadFileWaitingDialog = null;
    private View mEdtSearch;
    /* access modifiers changed from: private */
    public int mFileType = 2;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsAnimating = false;
    private MMContentFilesListView mListViewPersonalFiles;
    private MMContentFilesListView mListViewSharedFiles;
    private IPTUIListener mNetworkStateReceiver;
    private View mPanelEmptyView;
    private View mPanelPersonal;
    private View mPanelShared;
    private IPicker mPicker;
    /* access modifiers changed from: private */
    @Nullable
    public ZMPopupWindow mPopupWindow;
    @Nullable
    private ArrayList<String> mReqIds = new ArrayList<>();
    /* access modifiers changed from: private */
    @Nullable
    public ZMAsyncURLDownloadFile mTaskDownloadFile;
    private TextView mTxtLoadingError;
    /* access modifiers changed from: private */
    public int mUIMode = 1;
    private ViewSwitcher mViewSwitcher;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void FT_UploadToMyList_OnProgress(@Nullable String str, int i, int i2, int i3) {
            MMContentFragment.this.FT_UploadToMyList_OnProgress(str, i, i2, i3);
        }

        public void FT_DownloadByFileID_OnProgress(String str, @Nullable String str2, int i, int i2, int i3) {
            MMContentFragment.this.FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
        }

        public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
            MMContentFragment.this.Indicate_PreviewDownloaded(str, str2, i);
        }

        public void Indicate_FileDownloaded(String str, @Nullable String str2, int i) {
            MMContentFragment.this.Indicate_FileDownloaded(str, str2, i);
        }

        public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
            MMContentFragment.this.Indicate_FileDeleted(str, str2, i);
        }

        public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
            MMContentFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
            MMContentFragment.this.Indicate_FileUnshared(str, str2, i);
        }

        public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
            MMContentFragment.this.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }

        public void Indicate_NewFileSharedByOthers(@Nullable String str) {
            MMContentFragment.this.Indicate_NewFileSharedByOthers(str);
        }

        public void Indicate_NewPersonalFile(@Nullable String str) {
            MMContentFragment.this.Indicate_NewPersonalFile(str);
        }

        public void Indicate_FileStatusUpdated(@Nullable String str) {
            MMContentFragment.this.Indicate_FileStatusUpdated(str);
        }

        public void Indicate_UploadToMyFiles_Sent(@Nullable String str, @Nullable String str2, int i) {
            MMContentFragment.this.Indicate_UploadToMyFiles_Sent(str, str2, i);
        }

        public void Indicate_QueryMyFilesResponse(String str, int i, @Nullable List<String> list, long j, long j2) {
            MMContentFragment.this.Indicate_QueryMyFilesResponse(str, i, list, j, j2);
        }

        public void Indicate_QueryFilesSharedWithMeResponse(String str, int i, List<String> list, long j, long j2) {
            MMContentFragment.this.Indicate_QueryFilesSharedWithMeResponse(str, i, list, j, j2);
        }

        public void Indicate_QueryAllFilesResponse(String str, int i, @Nullable List<String> list, long j, long j2) {
            MMContentFragment.this.Indicate_QueryAllFilesResponse(str, i, list, j, j2);
        }

        public void Indicate_RenameFileResponse(int i, String str, @Nullable String str2, String str3) {
            MMContentFragment.this.Indicate_RenameFileResponse(i, str, str2, str3);
        }

        public void FT_UploadToMyList_TimeOut(@Nullable String str) {
            MMContentFragment.this.FT_UploadToMyList_TimeOut(str);
        }

        public void FT_OnDownloadByFileIDTimeOut(String str, @Nullable String str2) {
            MMContentFragment.this.FT_OnDownloadByFileIDTimeOut(str, str2);
        }

        public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
            MMContentFragment.this.NotifyOutdatedHistoryRemoved(list, j);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMContentFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void Indicate_FileAttachInfoUpdate(String str, @Nullable String str2, int i) {
            MMContentFragment.this.Indicate_FileAttachInfoUpdate(str, str2, i);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMContentFragment$DownloadFileListener */
    private class DownloadFileListener implements IDownloadFileListener {
        private Uri mInput;

        public DownloadFileListener(Uri uri, long j, String str) {
            this.mInput = uri;
        }

        public void onDownloadCompleted(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, @Nullable Uri uri, @Nullable String str) {
            if (uri != null && uri == this.mInput) {
                MMContentFragment.this.dismissDownloadFileWaitingDialog();
                if (!StringUtil.isEmptyOrNull(str)) {
                    MMContentFragment.this.uploadFile(str);
                }
            }
        }

        public void onDownloadFailed(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, @Nullable Uri uri) {
            if (uri != null && uri == this.mInput) {
                MMContentFragment.this.dismissDownloadFileWaitingDialog();
                String path = uri.getPath();
                if (StringUtil.isEmptyOrNull(path)) {
                    ShareAlertDialog.showDialog(MMContentFragment.this.getFragmentManager(), MMContentFragment.this.getString(C4558R.string.zm_msg_load_file_fail_without_name), false);
                } else {
                    String pathLastName = AndroidAppUtil.getPathLastName(path);
                    ShareAlertDialog.showDialog(MMContentFragment.this.getFragmentManager(), MMContentFragment.this.getString(C4558R.string.zm_msg_load_file_fail, pathLastName), false);
                }
            }
        }

        public void onDownloadProgress(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, long j, long j2) {
            MMContentFragment.this.updateProgressWaitingDialog(j, j2);
        }

        public void onDownloadCanceled(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, @Nullable Uri uri) {
            if (uri != null && uri == this.mInput) {
                MMContentFragment.this.dismissDownloadFileWaitingDialog();
            }
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMContentFragment$SharerActionContextMenuItem */
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

    /* renamed from: com.zipow.videobox.view.mm.MMContentFragment$UploadContextMenuItem */
    public static class UploadContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_BOX = 4;
        public static final int ACTION_DROPBOX = 1;
        public static final int ACTION_GOOGLE_DRIVE = 5;
        public static final int ACTION_NATIVE_FILES = 2;
        public static final int ACTION_ONE_DRIVE = 3;
        public static final int ACTION_ONE_DRIVE_BUSINESS = 6;
        public static final int ACTION_PHOTOS = 0;

        public UploadContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMContentFragment$UploadFailedAlertDialog */
    public static class UploadFailedAlertDialog extends ZMDialogFragment {
        public static final String RESULT_ARGS_UPLOAD_FILES = "uploadFiles";
        @Nullable
        private TextView mTxtMsg;

        public static void showUploadFailedAlertDialog(@Nullable FragmentManager fragmentManager, @Nullable Fragment fragment, int i) {
            if (fragmentManager != null) {
                UploadFailedAlertDialog uploadFailedAlertDialog = (UploadFailedAlertDialog) fragmentManager.findFragmentByTag(UploadFailedAlertDialog.class.getName());
                if (uploadFailedAlertDialog != null) {
                    uploadFailedAlertDialog.updateDialogMsg();
                    return;
                }
                UploadFailedAlertDialog uploadFailedAlertDialog2 = new UploadFailedAlertDialog();
                uploadFailedAlertDialog2.setArguments(new Bundle());
                if (fragment != null) {
                    uploadFailedAlertDialog2.setTargetFragment(fragment, i);
                }
                uploadFailedAlertDialog2.show(fragmentManager, UploadFailedAlertDialog.class.getName());
            }
        }

        public UploadFailedAlertDialog() {
            setCancelable(true);
        }

        private String getMsg() {
            ArrayList<String> uploadFailedFiles = PendingFileDataHelper.getInstance().getUploadFailedFiles();
            StringBuffer stringBuffer = new StringBuffer();
            for (String str : uploadFailedFiles) {
                if (FileUtils.fileIsExists(str)) {
                    stringBuffer.append(FileUtils.getFileName(str));
                    stringBuffer.append(FontStyleHelper.SPLITOR);
                }
            }
            return stringBuffer.length() > 0 ? stringBuffer.substring(0, stringBuffer.length() - 1) : "";
        }

        public void updateDialogMsg() {
            TextView textView = this.mTxtMsg;
            if (textView != null) {
                textView.setText(getMsg());
            }
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            this.mTxtMsg = new TextView(getActivity());
            this.mTxtMsg.setTextAppearance(getActivity(), C4558R.style.ZMTextView_Normal);
            this.mTxtMsg.setGravity(17);
            this.mTxtMsg.setText(getMsg());
            int dip2px = UIUtil.dip2px(getActivity(), 10.0f);
            this.mTxtMsg.setPadding(dip2px, 0, dip2px, 0);
            return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_upload_files_failed).setView(this.mTxtMsg).setPositiveButton(C4558R.string.zm_btn_retry, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    UploadFailedAlertDialog.this.retryUploadFiles();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
        }

        public void onDismiss(DialogInterface dialogInterface) {
            super.onDismiss(dialogInterface);
            PendingFileDataHelper.getInstance().clearUploadFailedFiles();
        }

        /* access modifiers changed from: private */
        public void retryUploadFiles() {
            ArrayList uploadFailedFiles = PendingFileDataHelper.getInstance().getUploadFailedFiles();
            if (uploadFailedFiles.size() > 0) {
                Fragment targetFragment = getTargetFragment();
                if (targetFragment != null) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(RESULT_ARGS_UPLOAD_FILES, uploadFailedFiles);
                    targetFragment.onActivityResult(getTargetRequestCode(), -1, intent);
                }
            }
        }
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, MMContentFragment.class.getName(), bundle, 0, false, true);
    }

    public static void showAsActivity(Fragment fragment) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, MMContentFragment.class.getName(), bundle, 0, false, true);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_content, viewGroup, false);
        this.mPanelShared = inflate.findViewById(C4558R.C4560id.panelShared);
        this.mPanelPersonal = inflate.findViewById(C4558R.C4560id.panelPerson);
        this.mEdtSearch = inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnFilter = inflate.findViewById(C4558R.C4560id.panelTitleLeft);
        this.mDownArrow = (ImageView) inflate.findViewById(C4558R.C4560id.icon_down_arrow);
        this.mBtnUploadFile = (ImageButton) inflate.findViewById(C4558R.C4560id.btnUploadFile);
        this.mViewSwitcher = (ViewSwitcher) inflate.findViewById(C4558R.C4560id.view_switcher);
        this.mListViewSharedFiles = (MMContentFilesListView) inflate.findViewById(C4558R.C4560id.listViewSharedFiles);
        this.mListViewPersonalFiles = (MMContentFilesListView) inflate.findViewById(C4558R.C4560id.listViewPersonalFiles);
        this.mTxtLoadingError = (TextView) inflate.findViewById(C4558R.C4560id.txtLoadingError);
        this.mBtnSearch = (ImageButton) inflate.findViewById(C4558R.C4560id.btnSearch);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mPanelEmptyView = inflate.findViewById(C4558R.C4560id.panelEmptyView);
        this.mListViewSharedFiles.setMode(false);
        this.mListViewPersonalFiles.setMode(true);
        this.mListViewSharedFiles.setOnContentFileOperatorListener(this);
        this.mListViewPersonalFiles.setOnContentFileOperatorListener(this);
        this.mListViewSharedFiles.setupEmptyView(this.mPanelEmptyView);
        this.mListViewPersonalFiles.setupEmptyView(this.mPanelEmptyView);
        this.mEdtSearch.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mPanelShared.setOnClickListener(this);
        this.mPanelPersonal.setOnClickListener(this);
        this.mBtnFilter.setOnClickListener(this);
        this.mBtnUploadFile.setOnClickListener(this);
        this.mTxtLoadingError.setOnClickListener(this);
        this.mBtnSearch.setOnClickListener(this);
        this.mTxtLoadingError.setText(Html.fromHtml(getString(C4558R.string.zm_lbl_content_load_error)));
        if (bundle != null) {
            this.mUIMode = bundle.getInt(EXTRA_UI_MODE, 0);
            this.mFileType = bundle.getInt(EXTRA_FILE_TYPE, 2);
            ArrayList<String> stringArrayList = bundle.getStringArrayList(EXTRA_UPLOAD_REQUEST_IDS);
            if (stringArrayList != null) {
                this.mReqIds = stringArrayList;
            }
            updateUIMode(this.mUIMode);
            selectFileType(this.mFileType);
            if (this.mListViewPersonalFiles.getCount() > 0 || this.mListViewSharedFiles.getCount() > 0) {
                refreshData();
            }
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onDestroyView() {
        ZMPopupWindow zMPopupWindow = this.mPopupWindow;
        if (zMPopupWindow != null) {
            if (zMPopupWindow.isShowing()) {
                this.mPopupWindow.dismiss();
            }
            this.mPopupWindow = null;
        }
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onDestroyView();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt(EXTRA_FILE_TYPE, this.mFileType);
        bundle.putInt(EXTRA_UI_MODE, this.mUIMode);
        bundle.putStringArrayList(EXTRA_UPLOAD_REQUEST_IDS, this.mReqIds);
    }

    /* access modifiers changed from: private */
    public void FT_UploadToMyList_OnProgress(@Nullable String str, int i, int i2, int i3) {
        this.mListViewPersonalFiles.FT_UploadToMyList_OnProgress(str, i, i2, i3);
        this.mListViewSharedFiles.FT_UploadToMyList_OnProgress(str, i, i2, i3);
    }

    public void FT_DownloadByFileID_OnProgress(String str, @Nullable String str2, int i, int i2, int i3) {
        this.mListViewPersonalFiles.onDownloadByFileIDOnProgress(str, str2, i, i2, i3);
        this.mListViewSharedFiles.onDownloadByFileIDOnProgress(str, str2, i, i2, i3);
    }

    public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
        this.mListViewPersonalFiles.Indicate_PreviewDownloaded(str, str2, i);
        this.mListViewSharedFiles.Indicate_PreviewDownloaded(str, str2, i);
    }

    public void Indicate_FileDownloaded(String str, @Nullable String str2, int i) {
        this.mListViewPersonalFiles.Indicate_FileDownloaded(str, str2, i);
        this.mListViewSharedFiles.Indicate_FileDownloaded(str, str2, i);
    }

    /* access modifiers changed from: private */
    public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
        this.mListViewPersonalFiles.Indicate_FileDeleted(str, str2, i);
        this.mListViewSharedFiles.Indicate_FileDeleted(str, str2, i);
    }

    /* access modifiers changed from: private */
    public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
        this.mListViewPersonalFiles.Indicate_FileShared(str, str2, str3, str4, str5, i);
        this.mListViewSharedFiles.Indicate_FileShared(str, str2, str3, str4, str5, i);
    }

    /* access modifiers changed from: private */
    public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
        this.mListViewPersonalFiles.Indicate_FileUnshared(str, str2, i);
        this.mListViewSharedFiles.Indicate_FileUnshared(str, str2, i);
    }

    /* access modifiers changed from: private */
    public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
        this.mListViewSharedFiles.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
    }

    /* access modifiers changed from: private */
    public void Indicate_NewFileSharedByOthers(@Nullable String str) {
        this.mListViewSharedFiles.Indicate_NewFileSharedByOthers(str);
    }

    /* access modifiers changed from: private */
    public void Indicate_NewPersonalFile(@Nullable String str) {
        this.mListViewPersonalFiles.Indicate_NewPersonalFile(str);
        this.mListViewSharedFiles.Indicate_NewPersonalFile(str);
    }

    /* access modifiers changed from: private */
    public void Indicate_FileStatusUpdated(@Nullable String str) {
        this.mListViewPersonalFiles.Indicate_FileStatusUpdated(str);
        this.mListViewSharedFiles.Indicate_FileStatusUpdated(str);
    }

    /* access modifiers changed from: private */
    public void Indicate_UploadToMyFiles_Sent(@Nullable String str, @Nullable String str2, int i) {
        this.mListViewPersonalFiles.Indicate_UploadToMyFiles_Sent(str, str2, i);
        this.mListViewSharedFiles.Indicate_UploadToMyFiles_Sent(str, str2, i);
        this.mHandler.post(new Runnable() {
            public void run() {
                MMContentFragment.this.updateUploadState();
            }
        });
        if (i != 0) {
            EventTaskManager eventTaskManager = getEventTaskManager();
            if (eventTaskManager != null) {
                eventTaskManager.pushLater(new EventAction("MMContentFragment.uploadFailed") {
                    public void run(IUIElement iUIElement) {
                        MMContentFragment mMContentFragment = (MMContentFragment) iUIElement;
                        if (mMContentFragment != null) {
                            mMContentFragment.onUploadFileFailed();
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_QueryMyFilesResponse(String str, int i, @Nullable List<String> list, long j, long j2) {
        this.mListViewPersonalFiles.Indicate_QueryMyFilesResponse(str, i, list, j, j2);
    }

    /* access modifiers changed from: private */
    public void Indicate_QueryFilesSharedWithMeResponse(String str, int i, List<String> list, long j, long j2) {
        this.mListViewSharedFiles.Indicate_QueryFilesSharedWithMeResponse(str, i, list, j, j2);
    }

    /* access modifiers changed from: private */
    public void Indicate_QueryAllFilesResponse(String str, int i, @Nullable List<String> list, long j, long j2) {
        this.mListViewSharedFiles.Indicate_QueryAllFilesResponse(str, i, list, j, j2);
    }

    public void Indicate_RenameFileResponse(int i, String str, @Nullable String str2, String str3) {
        this.mListViewSharedFiles.Indicate_RenameFileResponse(i, str, str2, str3);
        this.mListViewPersonalFiles.Indicate_RenameFileResponse(i, str, str2, str3);
    }

    /* access modifiers changed from: private */
    public void onUploadFileFailed() {
        UploadFailedAlertDialog.showUploadFailedAlertDialog(getFragmentManager(), this, 3002);
    }

    /* access modifiers changed from: private */
    public void FT_UploadToMyList_TimeOut(@Nullable String str) {
        Indicate_UploadToMyFiles_Sent(str, str, -1);
    }

    /* access modifiers changed from: private */
    public void FT_OnDownloadByFileIDTimeOut(String str, @Nullable String str2) {
        this.mListViewPersonalFiles.Indicate_FileDownloaded(str, str2, -1);
        this.mListViewSharedFiles.Indicate_FileDownloaded(str, str2, -1);
    }

    public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
        upateDataListViewWithEraseTime(j, true);
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        MMContentFilesListView mMContentFilesListView = this.mListViewSharedFiles;
        if (mMContentFilesListView != null) {
            mMContentFilesListView.onIndicateInfoUpdatedWithJID(str);
        }
    }

    public void Indicate_FileAttachInfoUpdate(String str, @Nullable String str2, int i) {
        MMContentFilesListView mMContentFilesListView = this.mListViewSharedFiles;
        if (mMContentFilesListView != null) {
            mMContentFilesListView.Indicate_FileAttachInfoUpdate(str, str2, i);
        }
    }

    public void onResume() {
        super.onResume();
        updateUIMode(this.mUIMode);
        updateUploadState();
        upateDataListView();
    }

    private void upateDataListViewWithEraseTime(long j, boolean z) {
        this.mListViewPersonalFiles.setEraseTime(j, z);
        this.mListViewSharedFiles.setEraseTime(j, z);
        upateDataListView();
    }

    private void upateDataListView() {
        this.mListViewPersonalFiles.notifyDataSetChanged(true);
        this.mListViewSharedFiles.notifyDataSetChanged(true);
    }

    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void updateUploadState() {
        if (PendingFileDataHelper.getInstance().getUploadPendingFileSize() >= 5) {
            this.mBtnUploadFile.setEnabled(false);
        } else {
            this.mBtnUploadFile.setEnabled(true);
        }
    }

    /* access modifiers changed from: private */
    public void refreshData() {
        MMContentFilesListView mMContentFilesListView = this.mListViewSharedFiles;
        if (mMContentFilesListView != null) {
            MMContentFilesListView mMContentFilesListView2 = this.mListViewPersonalFiles;
            if (mMContentFilesListView2 != null) {
                if (this.mUIMode == 1) {
                    mMContentFilesListView2.loadData(false);
                } else {
                    mMContentFilesListView.loadData(false);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateUIMode(int i) {
        this.mUIMode = i;
        int i2 = this.mUIMode;
        if (i2 == 1) {
            this.mPanelShared.setSelected(false);
            this.mPanelPersonal.setSelected(true);
            if (this.mFileType == 2) {
                this.mListViewPersonalFiles.filter(0);
            } else {
                this.mListViewPersonalFiles.filter(1);
            }
        } else if (i2 == 0) {
            this.mPanelShared.setSelected(true);
            this.mPanelPersonal.setSelected(false);
            if (this.mFileType == 2) {
                this.mListViewSharedFiles.filter(0);
            } else {
                this.mListViewSharedFiles.filter(1);
            }
        }
        selectFileType(this.mFileType);
    }

    public void closeWaitDialog(String str) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            Fragment findFragmentByTag = fragmentManager.findFragmentByTag(str);
            if (findFragmentByTag instanceof WaitingDialog) {
                ((WaitingDialog) findFragmentByTag).dismiss();
            }
        }
    }

    public void showWaitDialog(String str) {
        closeWaitDialog(str);
        WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
        newInstance.setCancelable(true);
        newInstance.show(getFragmentManager(), str);
    }

    public void onClick(View view) {
        if (view == this.mPanelShared) {
            onClickPanelShared();
        } else if (view == this.mPanelPersonal) {
            onClickPanelPersonal();
        } else if (view == this.mBtnFilter) {
            onClickTxtFileType();
        } else if (view == this.mBtnUploadFile) {
            onClickBtnUploadFile();
        } else if (view == this.mEdtSearch) {
            onClickPanelOperator();
        } else if (view == this.mBtnBack) {
            dismiss();
        } else if (view == this.mTxtLoadingError) {
            onClickTxtLoadingError();
        } else if (view == this.mBtnSearch) {
            onClickBtnSearch();
        }
    }

    public void dismiss() {
        finishFragment(0);
    }

    public void onClickBtnSearch() {
        if (PTApp.getInstance().isWebSignedOn()) {
            IMSearchFragment.showAsFragment(this, 0);
        }
    }

    private void onClickTxtLoadingError() {
        if (this.mUIMode == 1) {
            this.mListViewPersonalFiles.loadData(true);
        } else {
            this.mListViewSharedFiles.loadData(true);
        }
    }

    private void onClickPanelOperator() {
        if (PTApp.getInstance().isWebSignedOn()) {
            boolean z = true;
            if (this.mUIMode != 1) {
                z = false;
            }
            MMContentSearchFragment.showAsFragment(this, z);
        }
    }

    private void onClickPanelConnectionAlert() {
        IMActivity iMActivity = (IMActivity) getActivity();
        if (iMActivity != null) {
            if (!NetworkUtil.hasDataNetwork(iMActivity)) {
                Toast.makeText(iMActivity, C4558R.string.zm_alert_network_disconnected, 1).show();
                return;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (zoomMessenger.isStreamConflict()) {
                    AutoStreamConflictChecker.getInstance().showStreamConflictMessage(getActivity());
                } else {
                    zoomMessenger.trySignon();
                }
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C37104 r2 = new EventAction("MMContentFragmentPermissionResult") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((MMContentFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                }
            };
            eventTaskManager.pushLater("MMContentFragmentPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null && i == 10000 && checkExternalSoragePermission()) {
            uploadFile();
        }
    }

    private boolean checkExternalSoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private void onClickBtnUploadFile() {
        if (checkExternalSoragePermission()) {
            uploadFile();
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        zm_requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 10000);
    }

    private void uploadFile() {
        IMActivity iMActivity = (IMActivity) getActivity();
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(iMActivity, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_image), 0));
        arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_all_file), 2));
        arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_box), 4));
        if (iMActivity != null && ZMDropbox.getInstance().isDropboxLoginSupported(iMActivity)) {
            arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_dropbox), 1));
        }
        if (iMActivity != null && GoogleDrive.canAuthGoogleViaBrowser(iMActivity) && !ZMUtils.isItuneApp(iMActivity)) {
            arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_google_drive), 5));
        }
        arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_one_drive), 3));
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        TextView textView = new TextView(getActivity());
        if (VERSION.SDK_INT < 23) {
            textView.setTextAppearance(getActivity(), C4558R.style.ZMTextView_Medium);
        } else {
            textView.setTextAppearance(C4558R.style.ZMTextView_Medium);
        }
        int dip2px = UIUtil.dip2px(getActivity(), 20.0f);
        textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
        textView.setText(C4558R.string.zm_lbl_content_upload_file_59554);
        ZMAlertDialog create = new Builder(getActivity()).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMContentFragment.this.onSelectContextMenuItem((UploadContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    private void onClickTxtFileType() {
        ZMPopupWindow zMPopupWindow = this.mPopupWindow;
        if (zMPopupWindow == null || !zMPopupWindow.isShowing()) {
            playRotateAnimation(true);
        }
        showFileTypePopWindow();
    }

    private void onClickPanelShared() {
        if (this.mUIMode != 0) {
            this.mViewSwitcher.showNext();
            updateUIMode(0);
            refreshData();
        }
    }

    private void onClickPanelPersonal() {
        if (this.mUIMode != 1) {
            this.mViewSwitcher.showPrevious();
            updateUIMode(1);
            refreshData();
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(UploadContextMenuItem uploadContextMenuItem) {
        switch (uploadContextMenuItem.getAction()) {
            case 0:
                AndroidAppUtil.selectImageNoDefault((Fragment) this, C4558R.string.zm_select_a_image, 1004);
                return;
            case 1:
                ZMFileListActivity.startFileListActivity((Fragment) this, DropboxFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_upload, getString(C4558R.string.zm_mm_msg_upload_file_prompt));
                return;
            case 2:
                ZMFileListActivity.startFileListActivity((Fragment) this, ZMLocalFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_upload, getString(C4558R.string.zm_mm_msg_upload_file_prompt));
                return;
            case 3:
            case 6:
                boolean z = uploadContextMenuItem.getAction() == 6;
                if (OneDrivePicker.hasPicker(getActivity(), z)) {
                    this.mPicker = OneDrivePicker.createPicker(z ? 1015 : 1014, null, z);
                    IPicker iPicker = this.mPicker;
                    if (iPicker != null) {
                        iPicker.startPicking((ZMDialogFragment) this);
                        return;
                    } else {
                        ZMFileListActivity.startFileListActivity((Fragment) this, z ? OneDriveBusinessFileListAdapter.class : OneDriveFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_upload, getString(C4558R.string.zm_mm_msg_upload_file_prompt));
                        return;
                    }
                } else {
                    ZMFileListActivity.startFileListActivity((Fragment) this, z ? OneDriveBusinessFileListAdapter.class : OneDriveFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_upload, getString(C4558R.string.zm_mm_msg_upload_file_prompt));
                    return;
                }
            case 4:
                ZMFileListActivity.startFileListActivity((Fragment) this, BoxFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_upload, getString(C4558R.string.zm_mm_msg_upload_file_prompt));
                return;
            case 5:
                ZMFileListActivity.startFileListActivity((Fragment) this, GoogleDriveFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_upload, getString(C4558R.string.zm_mm_msg_upload_file_prompt));
                return;
            default:
                return;
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        Uri uri = null;
        switch (i) {
            case 1004:
                if (intent != null) {
                    uri = intent.getData();
                }
                if (i2 == -1 && uri != null) {
                    String pathFromUri = ImageUtil.getPathFromUri(getActivity(), uri);
                    if (pathFromUri == null) {
                        alertImageInvalid();
                        return;
                    }
                    FileInfoChecker zoomFileInfoChecker = PTApp.getInstance().getZoomFileInfoChecker();
                    if (zoomFileInfoChecker == null || !zoomFileInfoChecker.isGifFile(pathFromUri) || zoomFileInfoChecker.isLegalGif(pathFromUri)) {
                        uploadFile(pathFromUri);
                        break;
                    } else {
                        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_illegal_image, false).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                        return;
                    }
                }
                break;
            case 1010:
                if (i2 != -1) {
                    if (i2 == 0 && intent != null) {
                        Bundle extras = intent.getExtras();
                        if (extras != null) {
                            String string = extras.getString(ZMFileListActivity.FAILED_PROMT);
                            if (StringUtil.isEmptyOrNull(string)) {
                                string = getString(C4558R.string.zm_alert_auth_token_failed_msg);
                            }
                            ShareAlertDialog.showDialog(getFragmentManager(), string, false);
                            break;
                        }
                    }
                } else if (intent != null) {
                    Bundle extras2 = intent.getExtras();
                    if (extras2 != null) {
                        String string2 = extras2.getString(ZMFileListActivity.SELECTED_FILE_PATH);
                        String string3 = extras2.getString(ZMFileListActivity.SELECTED_FILE_NAME);
                        if (!StringUtil.isEmptyOrNull(string2) && !StringUtil.isEmptyOrNull(string3)) {
                            uploadFile(string2, string3);
                            break;
                        }
                    }
                }
                break;
            case 1014:
            case 1015:
                if (i2 != 0) {
                    if (i2 == -1) {
                        if (this.mPicker == null) {
                            this.mPicker = OneDrivePicker.createPicker(i, null, i == 1015);
                        }
                        if (intent != null) {
                            IPickerResult pickerResult = this.mPicker.getPickerResult(i, i2, intent);
                            if (pickerResult != null) {
                                if (pickerResult.acceptFileType()) {
                                    Uri link = pickerResult.getLink();
                                    if (!pickerResult.isLocal()) {
                                        asyncDownloadFile(link, pickerResult.getSize(), FileUtils.makeNewFilePathWithName(AppUtil.getCachePath(), pickerResult.getName()));
                                        break;
                                    } else {
                                        String path = link.getPath();
                                        if (path != null) {
                                            uploadFile(path);
                                            break;
                                        }
                                    }
                                } else {
                                    ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_alert_unsupported_format), false);
                                    break;
                                }
                            } else {
                                ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_msg_load_file_fail_without_name), false);
                                break;
                            }
                        }
                    } else {
                        ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_msg_load_file_fail_without_name), false);
                        break;
                    }
                }
                break;
            case 2014:
                if (i2 == -1 && intent != null) {
                    Bundle extras3 = intent.getExtras();
                    if (extras3 != null) {
                        String string4 = extras3.getString(ARGS_SHARE_FILE_ID);
                        if (!StringUtil.isEmptyOrNull(string4)) {
                            String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                            if (!StringUtil.isEmptyOrNull(stringExtra)) {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(stringExtra);
                                if (arrayList.size() > 0) {
                                    doShareFile(arrayList, string4);
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
            case 3001:
                if (i2 == -1 && intent != null) {
                    dealResultForZoomFileViewer(intent.getIntExtra(MMContentFileViewerFragment.RESULT_ACTION, 0), intent.getStringExtra(MMContentFileViewerFragment.RESULT_FILE_WEB_ID), intent.getStringExtra("reqId"));
                    break;
                }
            case 3002:
                if (i2 == -1 && intent != null) {
                    ArrayList stringArrayListExtra = intent.getStringArrayListExtra(UploadFailedAlertDialog.RESULT_ARGS_UPLOAD_FILES);
                    if (stringArrayListExtra != null && stringArrayListExtra.size() > 0) {
                        Iterator it = stringArrayListExtra.iterator();
                        while (it.hasNext()) {
                            uploadFile((String) it.next());
                        }
                        break;
                    }
                }
        }
    }

    private void dealResultForZoomFileViewer(int i, @Nullable String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str) && i == 1) {
            Indicate_FileDeleted(str2, str, 0);
        }
    }

    private void doShareFile(ArrayList<String> arrayList, String str) {
        MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, str);
    }

    private void asyncDownloadFile(Uri uri, long j, String str) {
        if (j >= 536870912) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_msg_file_too_large).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            return;
        }
        ZMAsyncURLDownloadFile zMAsyncURLDownloadFile = this.mTaskDownloadFile;
        if (zMAsyncURLDownloadFile != null) {
            zMAsyncURLDownloadFile.cancel(true);
            this.mTaskDownloadFile = null;
        }
        DownloadFileListener downloadFileListener = new DownloadFileListener(uri, j, str);
        ZMAsyncURLDownloadFile zMAsyncURLDownloadFile2 = new ZMAsyncURLDownloadFile(uri, j, str, downloadFileListener);
        this.mTaskDownloadFile = zMAsyncURLDownloadFile2;
        showDownloadFileWaitingDialog(getString(C4558R.string.zm_msg_download_file_size, FileUtils.toFileSizeString(getActivity(), 0)));
        this.mTaskDownloadFile.execute((Params[]) new Void[0]);
    }

    private void showDownloadFileWaitingDialog(String str) {
        if (this.mDownloadFileWaitingDialog == null) {
            this.mDownloadFileWaitingDialog = new ProgressDialog(getActivity());
            this.mDownloadFileWaitingDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    if (MMContentFragment.this.mTaskDownloadFile != null && !MMContentFragment.this.mTaskDownloadFile.isCancelled()) {
                        MMContentFragment.this.mTaskDownloadFile.cancel(true);
                    }
                    MMContentFragment.this.mTaskDownloadFile = null;
                    MMContentFragment.this.mDownloadFileWaitingDialog = null;
                }
            });
            this.mDownloadFileWaitingDialog.requestWindowFeature(1);
            this.mDownloadFileWaitingDialog.setMessage(str);
            this.mDownloadFileWaitingDialog.setCanceledOnTouchOutside(false);
            this.mDownloadFileWaitingDialog.setCancelable(true);
            this.mDownloadFileWaitingDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void dismissDownloadFileWaitingDialog() {
        ProgressDialog progressDialog = this.mDownloadFileWaitingDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mDownloadFileWaitingDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void updateProgressWaitingDialog(long j, long j2) {
        ProgressDialog progressDialog = this.mDownloadFileWaitingDialog;
        if (progressDialog != null) {
            if (j > 0) {
                long j3 = (j2 * 100) / j;
                progressDialog.setMessage(getString(C4558R.string.zm_msg_download_file_progress, Long.valueOf(j3)));
            } else {
                progressDialog.setMessage(getString(C4558R.string.zm_msg_download_file_size, FileUtils.toFileSizeString(getActivity(), j2)));
            }
        }
    }

    /* access modifiers changed from: private */
    public void uploadFile(@NonNull String str) {
        File file = new File(str);
        if (file.exists() && file.isFile()) {
            uploadFile(str, file.getName());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x009b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void uploadFile(@androidx.annotation.NonNull java.lang.String r9, java.lang.String r10) {
        /*
            r8 = this;
            java.io.File r0 = new java.io.File
            r0.<init>(r9)
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r10)
            if (r1 != 0) goto L_0x00a8
            boolean r1 = r0.exists()
            if (r1 == 0) goto L_0x00a8
            boolean r1 = r0.isFile()
            if (r1 != 0) goto L_0x0019
            goto L_0x00a8
        L_0x0019:
            long r1 = r0.length()
            r3 = 536870912(0x20000000, double:2.652494739E-315)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 < 0) goto L_0x0038
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_msg_file_too_large
            com.zipow.videobox.fragment.SimpleMessageDialog r9 = com.zipow.videobox.fragment.SimpleMessageDialog.newInstance(r9)
            androidx.fragment.app.FragmentManager r10 = r8.getFragmentManager()
            java.lang.Class<com.zipow.videobox.fragment.SimpleMessageDialog> r0 = com.zipow.videobox.fragment.SimpleMessageDialog.class
            java.lang.String r0 = r0.getName()
            r9.show(r10, r0)
            return
        L_0x0038:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.MMFileContentMgr r1 = r1.getZoomFileContentMgr()
            if (r1 != 0) goto L_0x0043
            return
        L_0x0043:
            java.lang.String r2 = r0.getName()
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isSameString(r10, r2)
            if (r2 != 0) goto L_0x006a
            java.io.File r2 = r0.getParentFile()
            if (r2 == 0) goto L_0x006a
            java.io.File r2 = r0.getParentFile()
            java.lang.String r2 = r2.getAbsolutePath()
            java.io.File r2 = p021us.zoom.androidlib.util.FileUtils.createUnDuplicateNameFile(r10, r2)
            if (r2 == 0) goto L_0x006a
            r0.renameTo(r2)
            java.lang.String r9 = r2.getAbsolutePath()
            r6 = r9
            goto L_0x006b
        L_0x006a:
            r6 = r9
        L_0x006b:
            java.lang.String r3 = r1.uploadFile(r6)
            boolean r9 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            if (r9 != 0) goto L_0x009b
            com.zipow.videobox.view.mm.MMContentFilesListView r9 = r8.mListViewPersonalFiles
            long r1 = r0.length()
            int r1 = (int) r1
            r9.addUploadFile(r3, r10, r1)
            com.zipow.videobox.view.mm.MMContentFilesListView r9 = r8.mListViewSharedFiles
            long r1 = r0.length()
            int r1 = (int) r1
            r9.addUploadFile(r3, r10, r1)
            com.zipow.videobox.view.mm.PendingFileDataHelper r2 = com.zipow.videobox.view.p014mm.PendingFileDataHelper.getInstance()
            long r0 = r0.length()
            int r5 = (int) r0
            r7 = 0
            r4 = r10
            r2.addUploadPendingFile(r3, r4, r5, r6, r7)
            r8.updateUploadState()
            goto L_0x00a7
        L_0x009b:
            android.os.Handler r9 = r8.mHandler
            com.zipow.videobox.view.mm.MMContentFragment$7 r10 = new com.zipow.videobox.view.mm.MMContentFragment$7
            r10.<init>(r6)
            r0 = 100
            r9.postDelayed(r10, r0)
        L_0x00a7:
            return
        L_0x00a8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMContentFragment.uploadFile(java.lang.String, java.lang.String):void");
    }

    private void alertImageInvalid() {
        ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_alert_invalid_image), true);
    }

    /* access modifiers changed from: private */
    public void selectFileType(int i) {
        this.mFileType = i;
        switch (i) {
        }
    }

    /* access modifiers changed from: private */
    public void playRotateAnimation(boolean z) {
        float f = 0.0f;
        float f2 = z ? 0.0f : 180.0f;
        if (z) {
            f = 180.0f;
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mDownArrow, View.ROTATION, new float[]{f2, f});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(300);
        animatorSet.play(ofFloat);
        animatorSet.start();
    }

    private void showFileTypePopWindow() {
        if (this.mPopupWindow == null) {
            View inflate = View.inflate(getActivity(), C4558R.layout.zm_mm_content_file_type_pop, null);
            View findViewById = inflate.findViewById(C4558R.C4560id.panelTypeFiles);
            View findViewById2 = inflate.findViewById(C4558R.C4560id.panelTypeImages);
            int i = 4;
            inflate.findViewById(C4558R.C4560id.imgTypeFiles).setVisibility(this.mFileType == 2 ? 0 : 4);
            View findViewById3 = inflate.findViewById(C4558R.C4560id.imgTypeImages);
            if (this.mFileType == 1) {
                i = 0;
            }
            findViewById3.setVisibility(i);
            inflate.measure(0, 0);
            this.mPopupWindow = new ZMPopupWindow(inflate, inflate.getMeasuredWidth(), inflate.getMeasuredHeight());
            this.mPopupWindow.setOnDismissListener(new OnDismissListener() {
                public void onDismiss() {
                    MMContentFragment.this.playRotateAnimation(false);
                }
            });
            this.mPopupWindow.setAnimationStyle(C4558R.style.DropDownAnimation);
            C37159 r0 = new OnClickListener() {
                public void onClick(View view) {
                    int id = view.getId();
                    if (id == C4558R.C4560id.panelTypeFiles) {
                        MMContentFragment.this.selectFileType(2);
                    } else if (id == C4558R.C4560id.panelTypeImages) {
                        MMContentFragment.this.selectFileType(1);
                    }
                    int i = 0;
                    MMContentFragment.this.mPopupWindow.getContentView().findViewById(C4558R.C4560id.imgTypeFiles).setVisibility(MMContentFragment.this.mFileType == 2 ? 0 : 4);
                    View findViewById = MMContentFragment.this.mPopupWindow.getContentView().findViewById(C4558R.C4560id.imgTypeImages);
                    if (MMContentFragment.this.mFileType != 1) {
                        i = 4;
                    }
                    findViewById.setVisibility(i);
                    MMContentFragment mMContentFragment = MMContentFragment.this;
                    mMContentFragment.updateUIMode(mMContentFragment.mUIMode);
                    MMContentFragment.this.refreshData();
                    MMContentFragment.this.mPopupWindow.dismiss();
                }
            };
            findViewById.setOnClickListener(r0);
            findViewById2.setOnClickListener(r0);
        }
        this.mPopupWindow.showAsDropDown(this.mBtnFilter, UIUtil.dip2px(getActivity(), 5.0f), 0);
    }

    public void onShow() {
        refreshData();
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
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_SHARE_FILE_ID, str);
        MMSelectSessionAndBuddyFragment.showAsFragment(this, bundle, false, false, 2014);
    }

    public void onZoomFileCancelTransfer(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            String str2 = null;
            if (!PendingFileDataHelper.getInstance().isFileUploadNow(str)) {
                PendingFileInfo downloadPendingInfoByWebFileId = PendingFileDataHelper.getInstance().getDownloadPendingInfoByWebFileId(str);
                if (downloadPendingInfoByWebFileId != null) {
                    str2 = downloadPendingInfoByWebFileId.reqId;
                }
            } else {
                str2 = str;
            }
            if (StringUtil.isEmptyOrNull(str2)) {
                this.mListViewPersonalFiles.endFileTransfer(str);
                this.mListViewSharedFiles.endFileTransfer(str);
                return;
            }
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null && zoomFileContentMgr.cancelFileTransfer(str2, str)) {
                this.mListViewPersonalFiles.endFileTransfer(str);
                this.mListViewSharedFiles.endFileTransfer(str);
                PendingFileDataHelper.getInstance().removeUploadPendingFile(str);
                PendingFileDataHelper.getInstance().removeDownloadPendingFile(str);
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
                            MMContentFragment.this.onSelectSharerActionContextMenuItem((SharerActionContextMenuItem) zMMenuAdapter.getItem(i), z);
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

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 0).show();
        }
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "WaitingDialog");
        }
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }
}
