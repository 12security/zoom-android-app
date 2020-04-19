package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.fragment.MMSelectSessionAndBuddyFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper.PendingFileInfo;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSessionContentsFragment */
public class MMSessionContentsFragment extends ZMDialogFragment implements OnClickListener, OnContentFileOperatorListener {
    private static final String ARGS_EXTRA_FILE_MODE = "fileMode";
    private static final String ARGS_EXTRA_SESSION_ID = "sessionid";
    private static final String ARGS_SHARE_FILE_ID = "shareFileId";
    public static final int FILE_MODE_ALL_FILES = 0;
    public static final int FILE_MODE_IMAGES = 1;
    public static final int REQUEST_DO_SHAREER = 2015;
    public static final int REQUEST_GET_SHAREER = 2014;
    public static final int REQUEST_VIEW_FILE_DETAIL = 3001;
    private MMContentFilesListView mFileListview;
    private int mFileMode;
    @Nullable
    private String mSessionId;
    @Nullable
    private String mShareReqId;
    private TextView mTxtLoadingError;
    private TextView mTxtTitle;
    @Nullable
    private String mUnshareReqId;
    @Nullable
    private WaitingDialog mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void FT_DownloadByFileID_OnProgress(String str, @Nullable String str2, int i, int i2, int i3) {
            MMSessionContentsFragment.this.FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
        }

        public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
            MMSessionContentsFragment.this.Indicate_PreviewDownloaded(str, str2, i);
        }

        public void Indicate_FileDownloaded(String str, @Nullable String str2, int i) {
            MMSessionContentsFragment.this.Indicate_FileDownloaded(str, str2, i);
        }

        public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
            MMSessionContentsFragment.this.Indicate_FileDeleted(str, str2, i);
        }

        public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
            MMSessionContentsFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
            MMSessionContentsFragment.this.Indicate_FileUnshared(str, str2, i);
        }

        public void Indicate_RenameFileResponse(int i, String str, @Nullable String str2, String str3) {
            MMSessionContentsFragment.this.Indicate_RenameFileResponse(i, str, str2, str3);
        }

        public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
            MMSessionContentsFragment.this.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }

        public void Indicate_QuerySessionFilesResponse(String str, String str2, int i, @Nullable List<String> list, long j, long j2) {
            MMSessionContentsFragment.this.Indicate_QuerySessionFilesResponse(str, str2, i, list, j, j2);
        }

        public void Indicate_NewFileSharedByOthers(@Nullable String str) {
            MMSessionContentsFragment.this.Indicate_NewFileSharedByOthers(str);
        }

        public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
            MMSessionContentsFragment.this.NotifyOutdatedHistoryRemoved(list, j);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMSessionContentsFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void Indicate_FileAttachInfoUpdate(String str, @Nullable String str2, int i) {
            MMSessionContentsFragment.this.Indicate_FileAttachInfoUpdate(str, str2, i);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMSessionContentsFragment$SharerActionContextMenuItem */
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

    /* renamed from: com.zipow.videobox.view.mm.MMSessionContentsFragment$UnshareAlertDialog */
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
            return new Builder(getActivity()).setMessage(C4558R.string.zm_alert_unshare_msg_59554).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FragmentManager fragmentManager = UnshareAlertDialog.this.getFragmentManager();
                    if (fragmentManager != null) {
                        MMSessionContentsFragment mMSessionContentsFragment = (MMSessionContentsFragment) fragmentManager.findFragmentByTag(MMSessionContentsFragment.class.getName());
                        if (mMSessionContentsFragment != null) {
                            mMSessionContentsFragment.unshareZoomFile(UnshareAlertDialog.this.mFileId, UnshareAlertDialog.this.mAction);
                        }
                    }
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
        }
    }

    public void onZoomFileSharerAction(String str, MMZoomShareAction mMZoomShareAction, boolean z, boolean z2) {
    }

    public static void showAsActivity(Fragment fragment, String str, int i, int i2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString(ARGS_EXTRA_SESSION_ID, str);
            bundle.putInt(ARGS_EXTRA_FILE_MODE, i);
            SimpleActivity.show(fragment, MMSessionContentsFragment.class.getName(), bundle, i2, true, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_session_content, viewGroup, false);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mFileListview = (MMContentFilesListView) inflate.findViewById(C4558R.C4560id.listViewFiles);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mFileListview.setOnContentFileOperatorListener(this);
        this.mFileListview.setupEmptyView(inflate.findViewById(C4558R.C4560id.panelEmptyView));
        this.mTxtLoadingError = (TextView) inflate.findViewById(C4558R.C4560id.txtLoadingError);
        this.mTxtLoadingError.setText(Html.fromHtml(getString(C4558R.string.zm_lbl_content_load_error)));
        this.mTxtLoadingError.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSessionId = arguments.getString(ARGS_EXTRA_SESSION_ID);
            this.mFileMode = arguments.getInt(ARGS_EXTRA_FILE_MODE, 0);
        }
        if (bundle != null) {
            this.mUnshareReqId = bundle.getString("mUnshareReqId");
            this.mShareReqId = bundle.getString("mShareReqId");
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        this.mFileListview.setSessionId(this.mSessionId);
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mUnshareReqId", this.mUnshareReqId);
        bundle.putString("mShareReqId", this.mShareReqId);
    }

    public void onDestroyView() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        refreshUI();
    }

    private void refreshUI() {
        if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
            switch (this.mFileMode) {
                case 0:
                    this.mFileListview.filter(0);
                    this.mTxtTitle.setText(C4558R.string.zm_mm_lbl_group_files);
                    this.mFileListview.loadData(false);
                    break;
                case 1:
                    this.mFileListview.filter(1);
                    this.mTxtTitle.setText(C4558R.string.zm_mm_lbl_group_images);
                    this.mFileListview.loadData(false);
                    break;
            }
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                dismiss();
            } else if (id == C4558R.C4560id.txtLoadingError) {
                onClickTxtLoadingError();
            }
        }
    }

    private void onClickTxtLoadingError() {
        this.mFileListview.loadData(true);
    }

    public void FT_DownloadByFileID_OnProgress(String str, @Nullable String str2, int i, int i2, int i3) {
        this.mFileListview.onDownloadByFileIDOnProgress(str, str2, i, i2, i3);
    }

    public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
        this.mFileListview.Indicate_PreviewDownloaded(str, str2, i);
    }

    public void Indicate_FileDownloaded(String str, @Nullable String str2, int i) {
        this.mFileListview.Indicate_FileDownloaded(str, str2, i);
    }

    /* access modifiers changed from: private */
    public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
        this.mFileListview.Indicate_FileDeleted(str, str2, i);
    }

    /* access modifiers changed from: private */
    public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
        if (StringUtil.isSameString(str, this.mShareReqId)) {
            this.mFileListview.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
        if (StringUtil.isSameString(str, this.mUnshareReqId)) {
            this.mFileListview.Indicate_FileUnshared(str, str2, i);
        }
    }

    public void Indicate_RenameFileResponse(int i, String str, @Nullable String str2, String str3) {
        this.mFileListview.Indicate_RenameFileResponse(i, str, str2, str3);
    }

    public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
        this.mFileListview.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
    }

    public void Indicate_QuerySessionFilesResponse(String str, String str2, int i, @Nullable List<String> list, long j, long j2) {
        this.mFileListview.Indicate_QuerySessionFilesResponse(str, str2, i, list, j, j2);
    }

    public void Indicate_NewFileSharedByOthers(@Nullable String str) {
        this.mFileListview.Indicate_NewFileSharedByOthers(str);
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        this.mFileListview.onIndicateInfoUpdatedWithJID(str);
    }

    public void Indicate_FileAttachInfoUpdate(String str, @Nullable String str2, int i) {
        this.mFileListview.Indicate_FileAttachInfoUpdate(str, str2, i);
    }

    public void NotifyOutdatedHistoryRemoved(@Nullable List<String> list, long j) {
        if (list != null && list.contains(this.mSessionId)) {
            this.mFileListview.setEraseTime(j, true);
            this.mFileListview.notifyDataSetChanged(true);
        }
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

    public void dismiss() {
        finishFragment(true);
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

    private void dealResultForZoomFileViewer(int i, @Nullable String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str) && i == 1) {
            Indicate_FileDeleted(str2, str, 0);
        }
    }

    private void doShareFile(ArrayList<String> arrayList, String str) {
        MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, str, this, 2015);
    }

    private void eventTrackOpenContent() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomLogEventTracking.eventTrackOpenContent(sessionById.isGroup());
            }
        }
    }

    public void onZoomFileClick(String str, @Nullable List<String> list) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (list == null || list.isEmpty()) {
                onZoomFileClick(str);
            } else {
                MMContentsViewerFragment.showAsActivity(this, this.mSessionId, str, list, 3001);
                eventTrackOpenContent();
            }
        }
    }

    public void onZoomFileClick(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MMContentFileViewerFragment.showAsActivity((Fragment) this, this.mSessionId, "", "", str, 3001);
            eventTrackOpenContent();
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
            if (!StringUtil.isEmptyOrNull(str2)) {
                MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                if (zoomFileContentMgr != null && zoomFileContentMgr.cancelFileTransfer(str2, str)) {
                    this.mFileListview.endFileTransfer(str);
                    PendingFileDataHelper.getInstance().removeUploadPendingFile(str);
                    PendingFileDataHelper.getInstance().removeDownloadPendingFile(str);
                }
            }
        }
    }

    public void onZoomFileIntegrationClick(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ZMIMUtils.openUrl(getActivity(), str);
        }
    }

    private void onSelectSharerActionContextMenuItem(@Nullable SharerActionContextMenuItem sharerActionContextMenuItem) {
        if (sharerActionContextMenuItem != null) {
            switch (sharerActionContextMenuItem.getAction()) {
                case 0:
                    MMChatActivity.showAsGroupChat((ZMActivity) getActivity(), sharerActionContextMenuItem.mShareAction.getSharee());
                    break;
                case 1:
                    UnshareAlertDialog.showUnshareAlertDialog(getFragmentManager(), sharerActionContextMenuItem.mFileId, sharerActionContextMenuItem.mShareAction);
                    break;
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
}
