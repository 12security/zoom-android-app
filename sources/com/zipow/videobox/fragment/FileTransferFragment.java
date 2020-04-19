package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.eventbus.ZMFileAction;
import com.zipow.videobox.eventbus.ZMFileTransfer;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.p014mm.HintDialogFragment;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMShareZoomFileDialogFragment;
import com.zipow.videobox.view.p014mm.MMZoomFile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil.MimeType;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class FileTransferFragment extends ZMDialogFragment implements OnClickListener {
    public static final String ARGS_FILE_NAME = "fileName";
    public static final String ARGS_TRANSFER_SIZE = "transferSize";
    private static final String ARGS_ZOOM_FILE_WEB_ID = "zoomFileWebId";
    public static final String ARGS_ZOOM_MESSAGE_ID = "messageId";
    public static final String ARGS_ZOOM_MESSAGE_XMPP_ID = "xmppId";
    public static final String ARGS_ZOOM_SESSION_ID = "sessionId";
    public static final String ARGS_ZOOM_SUPPORT_BREAD_POINT = "supportBreakPoint";
    public static final int AUTO_DOWNLOAD_FILE_SIZE = 2097152;
    private static final int REQUEST_CODE_SHOW_FILE_NO_EXIST = 2;
    public static final int REQUEST_GET_SHAREER = 1;
    private static final String TAG = "FileTransferFragment";
    private View mBack;
    private Button mBtnMain;
    private View mBtnMore;
    private TextView mCancel;
    private Context mContext;
    @Nullable
    private String mFileName;
    @Nullable
    private String mFileWebId;
    private ImageView mImgFile;
    private boolean mIsE2EFile;
    private boolean mIsFileUnavailable;
    @Nullable
    private String mMessageID;
    @Nullable
    private String mMessageXMPPID;
    @NonNull
    private IZoomMessengerUIListener mMessengerListener = new SimpleZoomMessengerUIListener() {
        public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        }

        public void Indicate_FileDownloaded(String str, String str2, int i) {
            FileTransferFragment.this.Indicate_FileDownloaded(str, str2, i);
        }

        public void onConfirm_MessageSent(String str, String str2, int i) {
            FileTransferFragment.this.onConfirm_MessageSent(str, str2, i);
        }

        public void Indicate_RenameFileResponse(int i, String str, String str2, String str3) {
            FileTransferFragment.this.Indicate_RenameFileResponse(i, str, str2, str3);
        }

        public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
            FileTransferFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileUnshared(String str, String str2, int i) {
            FileTransferFragment.this.Indicate_FileUnshared(str, str2, i);
        }

        public void onConnectReturn(int i) {
            FileTransferFragment.this.onConnectReturn(i);
        }

        public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
            FileTransferFragment.this.FT_OnProgress(str, str2, i, j, j2);
        }

        public void onConfirmFileDownloaded(String str, String str2, int i) {
            FileTransferFragment.this.onConfirmFileDownloaded(str, str2, i);
        }

        public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
            FileTransferFragment.this.FT_OnDownloadByMsgIDTimeOut(str, str2);
        }

        public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
            FileTransferFragment.this.FT_OnDownloadByFileIDTimeOut(str, str2);
        }

        public void FT_OnSent(String str, String str2, int i) {
            FileTransferFragment.this.FT_OnSent(str, str2, i);
        }
    };
    @Nullable
    private String mSessionId;
    private boolean mSupportResumeFromBreakPoint = false;
    private long mTransferSize;
    private TextView mTxtFileNameTtile;
    private TextView mTxtTranslateSpeed;
    private ProgressBar mUploadProgressBar;
    private String totalSize;

    public static class MoreContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 2;
        public static final int ACTION_DELETE = 1;
        public static final int ACTION_OTHER_APP = 7;
        public static final int ACTION_RENAME = 3;
        public static final int ACTION_SAVE_EMOJI = 6;
        public static final int ACTION_SAVE_IMAGE = 4;
        public static final int ACTION_SHARE = 5;

        public MoreContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity, @Nullable MMMessageItem mMMessageItem, int i) {
        if (zMActivity != null && mMMessageItem != null && !StringUtil.isEmptyOrNull(mMMessageItem.sessionId) && !StringUtil.isEmptyOrNull(mMMessageItem.messageId)) {
            Bundle bundle = new Bundle();
            bundle.putString("messageId", mMMessageItem.messageId);
            bundle.putString("sessionId", mMMessageItem.sessionId);
            bundle.putString(ARGS_ZOOM_MESSAGE_XMPP_ID, mMMessageItem.messageXMPPId);
            bundle.putBoolean(ARGS_ZOOM_SUPPORT_BREAD_POINT, ZMIMUtils.isFileTransferResumeEnabled(mMMessageItem.sessionId) && !mMMessageItem.isE2E);
            if (mMMessageItem.transferInfo != null) {
                bundle.putLong(ARGS_TRANSFER_SIZE, mMMessageItem.transferInfo.transferredSize);
            }
            if (!StringUtil.isEmptyOrNull(mMMessageItem.fileId)) {
                bundle.putString("zoomFileWebId", mMMessageItem.fileId);
            }
            SimpleActivity.show(zMActivity, FileTransferFragment.class.getName(), bundle, i, true, 1);
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment, String str, int i) {
        if (fragment != null && !StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("zoomFileWebId", str);
            SimpleActivity.show(fragment, FileTransferFragment.class.getName(), bundle, i, true, 1);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mFileWebId = arguments.getString("zoomFileWebId");
            this.mSessionId = arguments.getString("sessionId");
            this.mMessageXMPPID = arguments.getString(ARGS_ZOOM_MESSAGE_XMPP_ID);
            this.mSupportResumeFromBreakPoint = arguments.getBoolean(ARGS_ZOOM_SUPPORT_BREAD_POINT);
            this.mFileName = arguments.getString(ARGS_FILE_NAME);
            this.mMessageID = arguments.getString("messageId");
            this.mTransferSize = arguments.getLong(ARGS_TRANSFER_SIZE);
        }
        checkIsE2EFile();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_file_download_view, viewGroup, false);
        this.mBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnMore = inflate.findViewById(C4558R.C4560id.btnMore);
        this.mImgFile = (ImageView) inflate.findViewById(C4558R.C4560id.imgFile);
        this.mTxtFileNameTtile = (TextView) inflate.findViewById(C4558R.C4560id.fileName);
        this.mTxtTranslateSpeed = (TextView) inflate.findViewById(C4558R.C4560id.txtTranslateSpeed);
        this.mUploadProgressBar = (ProgressBar) inflate.findViewById(C4558R.C4560id.progress);
        this.mBtnMain = (Button) inflate.findViewById(C4558R.C4560id.btnMain);
        this.mCancel = (TextView) inflate.findViewById(C4558R.C4560id.cancel);
        this.mBack.setOnClickListener(this);
        this.mBtnMore.setOnClickListener(this);
        this.mBtnMain.setOnClickListener(this);
        this.mCancel.setOnClickListener(this);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        autoDownloadIfNeed();
        init();
    }

    public void onResume() {
        super.onResume();
        ZoomMessengerUI.getInstance().addListener(this.mMessengerListener);
        refreshUI();
    }

    private void init() {
        MMZoomFile mMZoomFile = getMMZoomFile();
        if (mMZoomFile != null) {
            try {
                String fileSizeString = FileUtils.toFileSizeString(this.mContext, this.mTransferSize);
                this.totalSize = FileUtils.toFileSizeString(this.mContext, (long) mMZoomFile.getFileSize());
                this.mTxtTranslateSpeed.setText(getResources().getString(C4558R.string.zm_lbl_translate_speed, new Object[]{fileSizeString, this.totalSize, "0"}));
                this.mTxtFileNameTtile.setText(mMZoomFile.getFileName());
                this.mUploadProgressBar.setProgress((int) ((this.mTransferSize * 100) / ((long) mMZoomFile.getFileSize())));
            } catch (Exception unused) {
            }
        }
    }

    public void onPause() {
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerListener);
        refreshFileStatus();
        super.onPause();
    }

    public void refreshFileStatus() {
        if (!StringUtil.isEmptyOrNull(this.mMessageID) && !StringUtil.isEmptyOrNull(this.mSessionId)) {
            EventBus.getDefault().post(new ZMFileTransfer(this.mMessageID, this.mSessionId));
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnMore) {
            onClickBtnMore();
        } else if (id == C4558R.C4560id.btnMain) {
            onClickBtnMain();
            refreshUI();
        } else if (id == C4558R.C4560id.cancel) {
            if (getFileTransferState() != 4) {
                cancel(1);
            }
            refreshUI();
        }
    }

    private void checkIsE2EFile() {
        if (!StringUtil.isEmptyOrNull(this.mMessageXMPPID) && !StringUtil.isEmptyOrNull(this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mMessageXMPPID);
                    if (messageByXMPPGuid != null) {
                        this.mIsE2EFile = messageByXMPPGuid.isE2EMessage();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileDownloaded(String str, String str2, int i) {
        if (i == 5061) {
            this.mIsFileUnavailable = true;
        } else {
            this.mIsFileUnavailable = false;
        }
        if (StringUtil.isSameString(str2, this.mFileWebId) && isResumed()) {
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void onConfirm_MessageSent(String str, String str2, int i) {
        if (StringUtil.isSameString(this.mSessionId, str) && StringUtil.isSameString(this.mMessageID, str2)) {
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_RenameFileResponse(int i, String str, String str2, String str3) {
        if (StringUtil.isSameString(str2, this.mFileWebId)) {
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
        if (StringUtil.isSameString(str2, this.mFileWebId) && isResumed()) {
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileUnshared(String str, String str2, int i) {
        if (StringUtil.isSameString(str2, this.mFileWebId) && isResumed()) {
            refreshUI();
        }
    }

    public void onConnectReturn(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isConnectionGood() && isResumed()) {
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId) && StringUtil.isSameString(str2, this.mMessageID)) {
            cancel(4);
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str2, this.mFileWebId)) {
            cancel(4);
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnSent(String str, String str2, int i) {
        if (StringUtil.isSameString(str, this.mSessionId) && StringUtil.isSameString(str2, this.mMessageID)) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null && zMActivity.isActive()) {
                View view = getView();
                if (view != null) {
                    ZMIMUtils.axAnnounceForAccessibility(view, getString(C4558R.string.zm_msg_file_state_uploaded_69051));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onConfirmFileDownloaded(String str, String str2, int i) {
        if (i == 5061) {
            this.mIsFileUnavailable = true;
        } else {
            this.mIsFileUnavailable = false;
        }
        if (StringUtil.isSameString(str, this.mSessionId) && StringUtil.isSameString(str2, this.mMessageID)) {
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
        if (StringUtil.isSameString(str, this.mSessionId) && StringUtil.isSameString(str2, this.mMessageID)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithMessageID = zoomFileContentMgr.getFileWithMessageID(str, this.mMessageXMPPID);
                if (fileWithMessageID != null) {
                    String fileSizeString = FileUtils.toFileSizeString(getActivity(), j);
                    String fileSizeString2 = FileUtils.toFileSizeString(getActivity(), (long) fileWithMessageID.getFileSize());
                    String fileSizeString3 = FileUtils.toFileSizeString(getActivity(), j2);
                    this.totalSize = fileSizeString2;
                    this.mTxtTranslateSpeed.setText(getResources().getString(C4558R.string.zm_lbl_translate_speed, new Object[]{fileSizeString, fileSizeString2, fileSizeString3}));
                    this.mTxtTranslateSpeed.setVisibility(0);
                    this.mUploadProgressBar.setProgress(i);
                    this.mUploadProgressBar.setVisibility(0);
                    if (i == 100) {
                        refreshUI();
                    }
                }
            }
        }
    }

    public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        if (StringUtil.isSameString(str2, this.mFileWebId)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str2);
                if (fileWithWebFileID != null) {
                    String fileSizeString = FileUtils.toFileSizeString(getActivity(), (long) i2);
                    String fileSizeString2 = FileUtils.toFileSizeString(getActivity(), (long) fileWithWebFileID.getFileSize());
                    String fileSizeString3 = FileUtils.toFileSizeString(getActivity(), (long) i3);
                    this.totalSize = fileSizeString2;
                    this.mTxtTranslateSpeed.setText(getResources().getString(C4558R.string.zm_lbl_translate_speed, new Object[]{fileSizeString, fileSizeString2, fileSizeString3}));
                    this.mTxtTranslateSpeed.setVisibility(0);
                    this.mUploadProgressBar.setProgress(i);
                    this.mUploadProgressBar.setVisibility(0);
                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                    if (i >= 100) {
                        refreshUI();
                    }
                }
            }
        }
    }

    private void onClickBtnMore() {
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
        List moreContextMenuItems = getMoreContextMenuItems();
        if (moreContextMenuItems != null && moreContextMenuItems.size() > 0) {
            zMMenuAdapter.addAll(moreContextMenuItems);
            ZMAlertDialog create = new Builder(getActivity()).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FileTransferFragment.this.onSelectContextMenuItem((MoreContextMenuItem) zMMenuAdapter.getItem(i));
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    private int getMessageState(String str, String str2) {
        int i = 0;
        if (!StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return 0;
            }
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById == null) {
                return 0;
            }
            ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str2);
            if (messageByXMPPGuid == null) {
                return 0;
            }
            i = messageByXMPPGuid.getMessageState();
        }
        return i;
    }

    private int getFileTransferState() {
        if (!StringUtil.isEmptyOrNull(this.mSessionId) && !StringUtil.isEmptyOrNull(this.mMessageID)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return -1;
            }
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById == null) {
                return -1;
            }
            ZoomMessage messageById = sessionById.getMessageById(this.mMessageID);
            if (messageById == null) {
                return -1;
            }
            FileTransferInfo fileTransferInfo = messageById.getFileTransferInfo();
            if (fileTransferInfo != null) {
                return fileTransferInfo.state;
            }
        }
        MMZoomFile mMZoomFile = getMMZoomFile();
        if (mMZoomFile != null) {
            return mMZoomFile.getFileTransferState();
        }
        return -1;
    }

    private void onClickBtnMain() {
        int fileTransferState = getFileTransferState();
        int messageState = getMessageState(this.mSessionId, this.mMessageXMPPID);
        if (messageState == 4 || fileTransferState == 2 || messageState == 6) {
            resendMessage();
        } else if (10 == fileTransferState || 1 == fileTransferState) {
            if (this.mSupportResumeFromBreakPoint) {
                pause();
            }
        } else if (12 == fileTransferState || 3 == fileTransferState) {
            if (this.mSupportResumeFromBreakPoint) {
                resume();
            } else if (12 == fileTransferState) {
                download();
            } else {
                resendMessage();
            }
        } else if (18 == fileTransferState || fileTransferState == 0 || fileTransferState == 11 || ((13 == fileTransferState || 4 == fileTransferState) && !isFileDownloaded())) {
            download();
        } else if (13 == fileTransferState || 4 == fileTransferState) {
            open();
        }
    }

    private void autoDownloadIfNeed() {
        int fileTransferState = getFileTransferState();
        if (18 == fileTransferState || fileTransferState == 0 || (((13 == fileTransferState || 4 == fileTransferState) && !isFileDownloaded()) || (12 == fileTransferState && !this.mSupportResumeFromBreakPoint))) {
            MMZoomFile mMZoomFile = getMMZoomFile();
            int dataNetworkType = NetworkUtil.getDataNetworkType(this.mContext);
            if (dataNetworkType == 1 || (dataNetworkType == 2 && mMZoomFile != null && mMZoomFile.getFileSize() <= 2097152)) {
                download();
            }
        }
    }

    private void onClickBtnBack() {
        finishFragment(true);
    }

    private void showMessageCannotDownloadForNoStorage() {
        Toast.makeText(this.mContext, C4558R.string.zm_ft_alert_cannot_download_for_no_storage, 1).show();
    }

    private String buildFileDownloadPath(String str) {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (externalStoragePublicDirectory == null || !externalStoragePublicDirectory.canWrite()) {
            return null;
        }
        return FileUtils.makeNewFilePathWithName(externalStoragePublicDirectory.getAbsolutePath(), str);
    }

    private void refreshUI() {
        int fileTransferState = getFileTransferState();
        int messageState = getMessageState(this.mSessionId, this.mMessageXMPPID);
        updateShareButton(messageState, fileTransferState);
        this.mBtnMain.setVisibility(0);
        if (messageState == 4 || fileTransferState == 2 || messageState == 6) {
            this.mBtnMain.setText(C4558R.string.zm_msg_resend_70707);
            this.mUploadProgressBar.setVisibility(4);
            this.mCancel.setVisibility(4);
            if (messageState == 6) {
                this.mUploadProgressBar.setProgress(0);
            }
        } else if ((10 == fileTransferState && (messageState == 2 || messageState == 3 || messageState == 7)) || (1 == fileTransferState && messageState == 1)) {
            this.mUploadProgressBar.setVisibility(0);
            this.mCancel.setVisibility(0);
            if (this.mSupportResumeFromBreakPoint) {
                this.mBtnMain.setText(C4558R.string.zm_record_btn_pause);
            } else {
                this.mBtnMain.setVisibility(4);
            }
        } else if ((12 == fileTransferState && (messageState == 2 || messageState == 3)) || (3 == fileTransferState && (messageState == 1 || messageState == 4))) {
            this.mBtnMain.setText(C4558R.string.zm_record_btn_resume);
            this.mUploadProgressBar.setVisibility(0);
            this.mCancel.setVisibility(0);
            if (!this.mSupportResumeFromBreakPoint) {
                this.mBtnMain.setVisibility(4);
            }
            String charSequence = this.mTxtTranslateSpeed.getText().toString();
            if (!StringUtil.isEmptyOrNull(charSequence)) {
                int indexOf = charSequence.indexOf("(");
                if (indexOf == -1) {
                    indexOf = charSequence.indexOf("（");
                }
                if (indexOf != -1) {
                    this.mTxtTranslateSpeed.setText(getString(C4558R.string.zm_lbl_file_transfer_paused_70707, charSequence.substring(0, indexOf)));
                }
            }
        } else if ((18 == fileTransferState && (messageState == 3 || messageState == 2 || messageState == 6 || messageState == 7)) || ((fileTransferState == 0 && (messageState == 3 || ((messageState == 2 && !isFileDownloaded()) || messageState == 7))) || ((fileTransferState == 11 && (messageState == 3 || messageState == 2 || messageState == 7)) || ((13 == fileTransferState || 4 == fileTransferState) && !isFileDownloaded())))) {
            this.mBtnMain.setText(C4558R.string.zm_btn_download);
            this.mUploadProgressBar.setVisibility(4);
            this.mTxtTranslateSpeed.setText(this.totalSize);
            this.mCancel.setVisibility(4);
            if (this.mIsFileUnavailable && fileTransferState == 11) {
                this.mBtnMain.setBackgroundColor(getResources().getColor(C4558R.color.zm_ui_kit_color_gray_EDEDF4));
                this.mBtnMain.setTextColor(getResources().getColor(C4558R.color.zm_text_grey));
                this.mBtnMain.setClickable(false);
                HintDialogFragment.showHintDialog(getFragmentManager(), this, 2, null, getResources().getString(C4558R.string.zm_content_file_downloaded_result_is_unavailable_text_89710), getResources().getString(C4558R.string.zm_btn_ok), null);
            }
        } else if (13 == fileTransferState || 4 == fileTransferState) {
            this.mBtnMain.setText(C4558R.string.zm_btn_open_70707);
            this.mUploadProgressBar.setVisibility(4);
            this.mTxtTranslateSpeed.setText("");
            this.mCancel.setVisibility(4);
        }
    }

    private void updateShareButton(int i, int i2) {
        int i3 = 0;
        boolean z = true;
        if (!(i == 1 || i == 6 || i == 4 || i2 == 18 || i2 == 2 || i2 == 1)) {
            z = false;
        }
        View view = this.mBtnMore;
        if (view != null) {
            if (z) {
                i3 = 4;
            }
            view.setVisibility(i3);
        }
    }

    private void open() {
        MMZoomFile mMZoomFile = getMMZoomFile();
        if (mMZoomFile != null && !StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath())) {
            boolean z = false;
            MimeType mimeTypeOfFile = AndroidAppUtil.getMimeTypeOfFile(mMZoomFile.getFileName());
            if (mimeTypeOfFile != null) {
                if (mimeTypeOfFile.fileType == 7) {
                    z = AndroidAppUtil.openFile(getActivity(), new File(mMZoomFile.getLocalPath()), true);
                } else {
                    z = AndroidAppUtil.openFile(getActivity(), new File(mMZoomFile.getLocalPath()));
                }
            }
            if (!z) {
                new Builder(getActivity()).setMessage(C4558R.string.zm_lbl_system_not_support_preview).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) null).show();
            }
        }
    }

    private boolean isNetworkConnected() {
        if (NetworkUtil.hasDataNetwork(this.mContext)) {
            return true;
        }
        Toast.makeText(this.mContext, C4558R.string.zm_mm_msg_network_unavailable, 1).show();
        return false;
    }

    @Nullable
    private MMZoomFile getMMZoomFile() {
        return ZMIMUtils.getMMZoomFile(this.mSessionId, this.mMessageXMPPID, this.mFileWebId);
    }

    private boolean isFileDownloaded() {
        return ZMIMUtils.isFileDownloaded(this.mSessionId, this.mMessageXMPPID, this.mFileWebId);
    }

    private void download() {
        if (isNetworkConnected()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                MMZoomFile mMZoomFile = getMMZoomFile();
                if (mMZoomFile != null) {
                    if (mMZoomFile.isFileDownloading()) {
                        FT_DownloadByFileID_OnProgress(null, this.mFileWebId, 0, 0, 0);
                        return;
                    }
                    if (!mMZoomFile.isFileDownloaded() || StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath()) || !new File(mMZoomFile.getLocalPath()).exists()) {
                        FT_DownloadByFileID_OnProgress(null, this.mFileWebId, 0, 0, 0);
                        if (!StringUtil.isEmptyOrNull(this.mMessageID)) {
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                            if (sessionById != null && sessionById.getMessageById(this.mMessageID) != null) {
                                sessionById.downloadFileForMessage(this.mMessageID);
                            }
                        }
                    }
                }
            }
        }
    }

    private void resume() {
        if (isNetworkConnected()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.FT_Resume(this.mSessionId, this.mMessageID, "");
            }
        }
    }

    private void pause() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.FT_Pause(this.mSessionId, this.mMessageID);
            EventBus.getDefault().post(new ZMFileAction(this.mSessionId, this.mMessageID, 1));
        }
    }

    private void cancel(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.FT_Cancel(this.mSessionId, this.mMessageID, i);
            EventBus.getDefault().post(new ZMFileAction(this.mSessionId, this.mMessageID, 2));
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(MoreContextMenuItem moreContextMenuItem) {
        if (moreContextMenuItem.getAction() == 5) {
            onClickBtnShare();
        }
    }

    private List<MoreContextMenuItem> getMoreContextMenuItems() {
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || getMMZoomFile() == null || zoomMessenger.getMyself() == null) {
            return null;
        }
        if (!this.mIsE2EFile && zoomMessenger.e2eGetMyOption() != 2 && !PTApp.getInstance().isFileTransferDisabled() && getFileTransferState() != 1) {
            arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_btn_share), 5));
        }
        return arrayList;
    }

    private void onClickBtnShare() {
        MMSelectSessionAndBuddyFragment.showAsFragment(this, new Bundle(), false, false, 1);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        switch (i) {
            case 1:
                if (i2 == -1 && intent != null && intent.getExtras() != null) {
                    String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                    if (!StringUtil.isEmptyOrNull(stringExtra)) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(stringExtra);
                        if (arrayList.size() > 0) {
                            doShareFile(arrayList);
                            break;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            case 2:
                if (i2 == -1) {
                    onClickBtnBack();
                    break;
                }
                break;
        }
    }

    private void openFileForMessage(MMMessageItem mMMessageItem) {
        String str = mMMessageItem.localFilePath;
        if (!StringUtil.isEmptyOrNull(str)) {
            AndroidAppUtil.openFile(this.mContext, new File(str));
        }
    }

    private void doShareFile(ArrayList<String> arrayList) {
        if (TextUtils.isEmpty(this.mSessionId) || TextUtils.isEmpty(this.mMessageXMPPID)) {
            MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, this.mFileWebId);
            return;
        }
        MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, this.mFileWebId, this.mMessageXMPPID, this.mSessionId, null, 0);
    }

    private void resendMessage() {
        if (isNetworkConnected()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    if (sessionById.resendPendingMessage(this.mMessageID, this.mIsE2EFile ? getResources().getString(C4558R.string.zm_msg_e2e_fake_message) : "")) {
                        refreshUI();
                    }
                }
            }
        }
    }
}
