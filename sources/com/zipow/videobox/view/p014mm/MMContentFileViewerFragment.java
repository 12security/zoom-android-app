package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.fragment.MMSelectSessionAndBuddyFragment;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.pdf.PDFView;
import com.zipow.videobox.pdf.PDFView.PDFViewListener;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.GifException;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LazyLoadDrawable;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.util.ZMGlideRequestListener;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ZMGifView;
import com.zipow.videobox.view.p014mm.message.FontStyle;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil.MimeType;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentFileViewerFragment */
public class MMContentFileViewerFragment extends ZMDialogFragment implements OnClickListener {
    public static final int ACTION_DELETE = 1;
    private static final String ARGS_SHARE_FILE_ID = "shareFileId";
    private static final String ARGS_ZOOM_FILE_WEB_ID = "zoomFileWebId";
    public static final String ARGS_ZOOM_MESSAGE_ID = "messageId";
    public static final String ARGS_ZOOM_MESSAGE_XMPP_ID = "messageXPPId";
    public static final String ARGS_ZOOM_SESSION_ID = "sessionId";
    protected static final int DISPLAY_ANIM_DURATION = 200;
    private static final int MAX_IMAGE_SIZE_IN_AREA = 1000000;
    private static final int REQUEST_CODE_SAVE_EMOJI = 3102;
    private static final int REQUEST_CODE_SAVE_IMAGE = 3101;
    private static final int REQUEST_CODE_SHOW_FILE_NO_EXIST = 3103;
    public static final int REQUEST_GET_SHAREER = 1;
    public static final String RESULT_ACTION = "action";
    public static final String RESULT_FILE_WEB_ID = "zoomFileWebId";
    public static final String RESULT_REQ_ID = "reqId";
    private static final String TAG = "MMContentFileViewerFragment";
    private Button mBtnDownload;
    private ImageButton mBtnMore;
    private ImageButton mBtnShare;
    private Button mBtnViewFile;
    @Nullable
    private String mFileWebId;
    /* access modifiers changed from: private */
    public ProgressBar mGiphyProgress;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private SubsamplingScaleImageView mImageview;
    private ImageView mImgFileType;
    private ZMGifView mImgGifView;
    private boolean mIsE2EFile;
    private boolean mIsFileUnavailable;
    @Nullable
    private String mMessageID;
    @Nullable
    private String mMessageXMPPID;
    @NonNull
    private IZoomMessengerUIListener mMessengerListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_FileDownloaded(String str, String str2, int i) {
            MMContentFileViewerFragment.this.Indicate_FileDownloaded(str, str2, i);
        }

        public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
            MMContentFileViewerFragment.this.FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
        }

        public void Indicate_RenameFileResponse(int i, String str, String str2, String str3) {
            MMContentFileViewerFragment.this.Indicate_RenameFileResponse(i, str, str2, str3);
        }

        public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
            MMContentFileViewerFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileUnshared(String str, String str2, int i) {
            MMContentFileViewerFragment.this.Indicate_FileUnshared(str, str2, i);
        }

        public void Indicate_FileStatusUpdated(String str) {
            MMContentFileViewerFragment.this.Indicate_FileStatusUpdated(str);
        }

        public void onConnectReturn(int i) {
            MMContentFileViewerFragment.this.onConnectReturn(i);
        }

        public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
            MMContentFileViewerFragment.this.FT_OnProgress(str, str2, i, j, j2);
        }

        public void onConfirmFileDownloaded(String str, String str2, int i) {
            MMContentFileViewerFragment.this.onConfirmFileDownloaded(str, str2, i);
        }

        public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
            MMContentFileViewerFragment.this.FT_OnDownloadByMsgIDTimeOut(str, str2);
        }

        public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
            MMContentFileViewerFragment.this.FT_OnDownloadByFileIDTimeOut(str, str2);
        }
    };
    /* access modifiers changed from: private */
    public View mPanelBottomBar;
    private View mPanelContent;
    private View mPanelNormalFile;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    private PDFView mPdfView;
    @Nullable
    private String mSessionId;
    private TextView mTxtFileDes;
    private TextView mTxtFileNameTtile;
    private TextView mTxtFileSharees;
    /* access modifiers changed from: private */
    public TextView mTxtMessage;
    private TextView mTxtNotSupportPreview;
    private TextView mTxtTranslateSpeed;
    private ProgressBar mUploadProgressBar;
    /* access modifiers changed from: private */
    public View mViewPlaceHolder;
    /* access modifiers changed from: private */
    @Nullable
    public ProgressDialog mWaitingSaveDialog;

    /* renamed from: com.zipow.videobox.view.mm.MMContentFileViewerFragment$MoreContextMenuItem */
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

    /* renamed from: com.zipow.videobox.view.mm.MMContentFileViewerFragment$SetFileNameDialog */
    public static class SetFileNameDialog extends ZMDialogFragment implements TextWatcher, OnEditorActionListener {
        private static final String ARG_FILE_NAME = "fileName";
        private Button mBtnOK = null;
        private EditText mEdtFileName = null;

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public static void showSetNameDialog(FragmentManager fragmentManager, String str) {
            SetFileNameDialog setFileNameDialog = new SetFileNameDialog();
            Bundle bundle = new Bundle();
            bundle.putString("fileName", str);
            setFileNameDialog.setArguments(bundle);
            setFileNameDialog.show(fragmentManager, SetFileNameDialog.class.getName());
        }

        public SetFileNameDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            String str = "";
            Bundle arguments = getArguments();
            if (arguments != null) {
                str = arguments.getString("fileName");
            }
            View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_content_set_file_name, null, false);
            this.mEdtFileName = (EditText) inflate.findViewById(C4558R.C4560id.edtFileName);
            if (str != null) {
                this.mEdtFileName.setText(str);
            }
            this.mEdtFileName.setImeOptions(2);
            this.mEdtFileName.setOnEditorActionListener(this);
            this.mEdtFileName.addTextChangedListener(this);
            return new Builder(getActivity()).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }

        public void onResume() {
            super.onResume();
            this.mBtnOK = ((ZMAlertDialog) getDialog()).getButton(-1);
            Button button = this.mBtnOK;
            if (button != null) {
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (SetFileNameDialog.this.checkInput()) {
                            SetFileNameDialog.this.onClickBtnOK();
                        }
                    }
                });
            }
            updateButtons();
        }

        public void onCancel(DialogInterface dialogInterface) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mBtnOK);
        }

        /* access modifiers changed from: private */
        public void onClickBtnOK() {
            UIUtil.closeSoftKeyboard(getActivity(), this.mBtnOK);
            String trim = this.mEdtFileName.getText().toString().trim();
            if (trim.length() == 0) {
                this.mEdtFileName.requestFocus();
                return;
            }
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    MMContentFileViewerFragment findMyProfileFragment = MMContentFileViewerFragment.findMyProfileFragment(supportFragmentManager);
                    if (findMyProfileFragment != null) {
                        findMyProfileFragment.setFileName(trim);
                        dismissAllowingStateLoss();
                    }
                }
            }
        }

        public void afterTextChanged(Editable editable) {
            updateButtons();
        }

        private void updateButtons() {
            Button button = this.mBtnOK;
            if (button != null) {
                button.setEnabled(checkInput());
            }
        }

        /* access modifiers changed from: private */
        public boolean checkInput() {
            return !StringUtil.isEmptyOrNull(this.mEdtFileName.getText().toString().trim());
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 2) {
                return false;
            }
            onClickBtnOK();
            return true;
        }
    }

    public static void showAsActivity(Fragment fragment, String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("zoomFileWebId", str);
        SimpleActivity.show(fragment, MMContentFileViewerFragment.class.getName(), bundle, i, true, 1);
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("zoomFileWebId", str);
        SimpleActivity.show(zMActivity, MMContentFileViewerFragment.class.getName(), bundle, i, true, 1);
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity, String str, String str2, String str3, String str4, int i) {
        if (!StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str2) && zMActivity != null) {
            Bundle bundle = new Bundle();
            bundle.putString("messageId", str2);
            bundle.putString("sessionId", str);
            bundle.putString(ARGS_ZOOM_MESSAGE_XMPP_ID, str3);
            if (!StringUtil.isEmptyOrNull(str4)) {
                bundle.putString("zoomFileWebId", str4);
            }
            SimpleActivity.show(zMActivity, MMContentFileViewerFragment.class.getName(), bundle, i, true, 1);
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment, String str, String str2, String str3, String str4, int i) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("messageId", str2);
            bundle.putString("sessionId", str);
            bundle.putString(ARGS_ZOOM_MESSAGE_XMPP_ID, str3);
            if (!StringUtil.isEmptyOrNull(str4)) {
                bundle.putString("zoomFileWebId", str4);
            }
            SimpleActivity.show(fragment, MMContentFileViewerFragment.class.getName(), bundle, i, true, 1);
        }
    }

    @Nullable
    public static MMContentFileViewerFragment findMyProfileFragment(FragmentManager fragmentManager) {
        return (MMContentFileViewerFragment) fragmentManager.findFragmentByTag(MMContentFileViewerFragment.class.getName());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_content_file_viewer, viewGroup, false);
        this.mGiphyProgress = (ProgressBar) inflate.findViewById(C4558R.C4560id.zm_content_file_giphy_progress);
        this.mTxtFileNameTtile = (TextView) inflate.findViewById(C4558R.C4560id.txtFileNameTitle);
        this.mTxtFileDes = (TextView) inflate.findViewById(C4558R.C4560id.txtFileDes);
        this.mImgGifView = (ZMGifView) inflate.findViewById(C4558R.C4560id.imgGifView);
        this.mImageview = (SubsamplingScaleImageView) inflate.findViewById(C4558R.C4560id.imageview);
        this.mPanelNormalFile = inflate.findViewById(C4558R.C4560id.panelNormalFile);
        this.mTxtFileSharees = (TextView) inflate.findViewById(C4558R.C4560id.txtFileSharees);
        this.mImgFileType = (ImageView) inflate.findViewById(C4558R.C4560id.imgFileType);
        this.mBtnDownload = (Button) inflate.findViewById(C4558R.C4560id.btnDownload);
        this.mBtnViewFile = (Button) inflate.findViewById(C4558R.C4560id.btnViewFile);
        this.mPdfView = (PDFView) inflate.findViewById(C4558R.C4560id.pdfView);
        this.mTxtTranslateSpeed = (TextView) inflate.findViewById(C4558R.C4560id.txtTranslateSpeed);
        this.mUploadProgressBar = (ProgressBar) inflate.findViewById(C4558R.C4560id.uploadProgressBar);
        this.mTxtNotSupportPreview = (TextView) inflate.findViewById(C4558R.C4560id.txtNotSupportPreview);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mPanelBottomBar = inflate.findViewById(C4558R.C4560id.panelBottomBar);
        this.mBtnShare = (ImageButton) inflate.findViewById(C4558R.C4560id.btnShare);
        this.mViewPlaceHolder = inflate.findViewById(C4558R.C4560id.viewPlaceHolder);
        this.mTxtMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mBtnMore = (ImageButton) inflate.findViewById(C4558R.C4560id.btnMore);
        this.mPanelContent = inflate.findViewById(C4558R.C4560id.panelContent);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mFileWebId = arguments.getString("zoomFileWebId");
            this.mSessionId = arguments.getString("sessionId");
            this.mMessageXMPPID = arguments.getString(ARGS_ZOOM_MESSAGE_XMPP_ID);
            this.mMessageID = arguments.getString("messageId");
        }
        inflate.findViewById(C4558R.C4560id.btnClose).setOnClickListener(this);
        this.mBtnShare.setOnClickListener(this);
        this.mBtnMore.setOnClickListener(this);
        this.mPanelContent.setOnClickListener(this);
        this.mBtnViewFile.setOnClickListener(this);
        this.mBtnDownload.setOnClickListener(this);
        this.mImageview.setOnClickListener(this);
        this.mViewPlaceHolder.setOnClickListener(this);
        this.mPanelBottomBar.setOnClickListener(this);
        this.mPanelTitleBar.setOnClickListener(this);
        this.mPdfView.setListener(new PDFViewListener() {
            public void onPDFViewPageChanged() {
            }

            public void onPDFViewClicked() {
                MMContentFileViewerFragment.this.onTapContent();
            }
        });
        checkIsE2EFile();
        return inflate;
    }

    public void onStop() {
        this.mPdfView.closeFile();
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        ZoomMessengerUI.getInstance().addListener(this.mMessengerListener);
        this.mPdfView.setSeekBarBottomPadding(UIUtil.dip2px(getActivity(), 40.0f));
        refreshUI();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        autoDownloadIfNeeded();
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

    private boolean checkIsGroupOpt() {
        if (!TextUtils.isEmpty(this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return false;
            }
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById == null) {
                return false;
            }
            ZoomGroup sessionGroup = sessionById.getSessionGroup();
            if (sessionGroup != null) {
                return sessionGroup.isGroupOperatorable();
            }
        }
        return false;
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
    public void Indicate_RenameFileResponse(int i, String str, String str2, String str3) {
        if (StringUtil.isSameString(str2, this.mFileWebId)) {
            dismissWaitingDialog();
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

    /* access modifiers changed from: private */
    public void Indicate_FileStatusUpdated(String str) {
        if (StringUtil.isSameString(str, this.mFileWebId) && isResumed()) {
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
            refreshUI();
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str2, this.mFileWebId)) {
            refreshUI();
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
                    this.mTxtTranslateSpeed.setText(getString(C4558R.string.zm_lbl_translate_speed, fileSizeString, fileSizeString2, fileSizeString3));
                    this.mTxtTranslateSpeed.setVisibility(0);
                    this.mUploadProgressBar.setProgress(i);
                    this.mUploadProgressBar.setVisibility(0);
                    this.mBtnDownload.setVisibility(8);
                    this.mBtnViewFile.setVisibility(8);
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
                    this.mTxtTranslateSpeed.setText(getString(C4558R.string.zm_lbl_translate_speed, fileSizeString, fileSizeString2, fileSizeString3));
                    this.mTxtTranslateSpeed.setVisibility(0);
                    this.mUploadProgressBar.setProgress(i);
                    this.mUploadProgressBar.setVisibility(0);
                    this.mBtnDownload.setVisibility(8);
                    this.mBtnViewFile.setVisibility(8);
                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onTapContent() {
        TranslateAnimation translateAnimation;
        TranslateAnimation translateAnimation2;
        if (this.mPanelTitleBar.getVisibility() != 0) {
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelBottomBar.setVisibility(0);
            translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, (float) (-this.mPanelTitleBar.getHeight()), 0.0f);
            translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) this.mPanelBottomBar.getHeight(), 0.0f);
        } else {
            translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-this.mPanelTitleBar.getHeight()));
            translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) this.mPanelBottomBar.getHeight());
            C36873 r2 = new AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    MMContentFileViewerFragment.this.mPanelTitleBar.setVisibility(4);
                    MMContentFileViewerFragment.this.mPanelBottomBar.setVisibility(4);
                }
            };
            translateAnimation2.setAnimationListener(r2);
            translateAnimation.setAnimationListener(r2);
        }
        translateAnimation2.setInterpolator(new DecelerateInterpolator());
        translateAnimation2.setDuration(200);
        this.mPanelTitleBar.startAnimation(translateAnimation2);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setDuration(200);
        this.mPanelBottomBar.startAnimation(translateAnimation);
    }

    public void onPause() {
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerListener);
        super.onPause();
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i != 1) {
            if (i == REQUEST_CODE_SHOW_FILE_NO_EXIST && i2 == -1) {
                onClickBtnClose();
            }
        } else if (i2 == -1 && intent != null && intent.getExtras() != null) {
            String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
            if (!StringUtil.isEmptyOrNull(stringExtra)) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(stringExtra);
                if (arrayList.size() > 0) {
                    doShareFile(arrayList);
                }
            }
        }
    }

    private void doShareFile(ArrayList<String> arrayList) {
        if (TextUtils.isEmpty(this.mSessionId) || TextUtils.isEmpty(this.mMessageXMPPID)) {
            MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, this.mFileWebId);
            return;
        }
        MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, this.mFileWebId, this.mMessageXMPPID, this.mSessionId, null, 0);
    }

    private void autoDownloadIfNeeded() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                MMZoomFile mMZoomFile = getMMZoomFile(zoomFileContentMgr);
                if (mMZoomFile != null) {
                    if (mMZoomFile.isFileDownloading()) {
                        FT_DownloadByFileID_OnProgress(null, this.mFileWebId, 0, 0, 0);
                        return;
                    }
                    if (!mMZoomFile.isFileDownloaded() || StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath()) || !new File(mMZoomFile.getLocalPath()).exists()) {
                        FT_DownloadByFileID_OnProgress(null, this.mFileWebId, 0, 0, 0);
                        boolean z = false;
                        if (!StringUtil.isEmptyOrNull(this.mMessageID)) {
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                            if (sessionById != null) {
                                ZoomMessage messageById = sessionById.getMessageById(this.mMessageID);
                                if (messageById != null && messageById.getFileTransferInfo() != null) {
                                    this.mViewPlaceHolder.setVisibility(8);
                                    sessionById.downloadFileForMessage(this.mMessageID);
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            String str = this.mFileWebId;
                            String downloadFile = zoomFileContentMgr.downloadFile(str, PendingFileDataHelper.getContenFilePath(str, mMZoomFile.getFileName()));
                            if (!StringUtil.isEmptyOrNull(downloadFile)) {
                                this.mTxtTranslateSpeed.setVisibility(0);
                                this.mUploadProgressBar.setVisibility(0);
                                this.mBtnDownload.setVisibility(8);
                                this.mBtnViewFile.setVisibility(8);
                                FT_DownloadByFileID_OnProgress(downloadFile, this.mFileWebId, 0, 0, 0);
                                this.mViewPlaceHolder.setVisibility(8);
                            } else {
                                showDownloadErrorUI();
                            }
                        }
                        if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                            ZoomChatSession sessionById2 = zoomMessenger.getSessionById(this.mSessionId);
                            if (sessionById2 != null) {
                                z = sessionById2.isGroup();
                            }
                        }
                        ZoomLogEventTracking.eventTrackDownloadFile(z);
                    }
                }
            }
        }
    }

    private boolean isImageFile(String str) {
        boolean z = false;
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase(CompatUtils.getLocalDefault());
        if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif")) {
            z = true;
        }
        return z;
    }

    private boolean isPdfFile(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            return str.toLowerCase(CompatUtils.getLocalDefault()).endsWith(".pdf");
        }
        return false;
    }

    private void showDownloadErrorUI() {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ZoomFile zoomFile = getZoomFile(zoomFileContentMgr);
            if (zoomFile != null) {
                MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(zoomFile, zoomFileContentMgr);
                this.mViewPlaceHolder.setVisibility(0);
                this.mTxtMessage.setText(ZmPtUtils.isImageFile(initWithZoomFile.getFileType()) ? C4558R.string.zm_mm_msg_download_image_failed : C4558R.string.zm_mm_msg_download_other_failed);
                this.mTxtTranslateSpeed.setVisibility(4);
                this.mUploadProgressBar.setVisibility(4);
                this.mBtnDownload.setVisibility(8);
            }
        }
    }

    private void downloadGiphy(@Nullable String str) {
        this.mGiphyProgress.setVisibility(0);
        ImageLoader.getInstance().displayGif(this.mImgGifView, str, (ZMGlideRequestListener) new ZMGlideRequestListener() {
            public void onSuccess(String str) {
                MMContentFileViewerFragment.this.mGiphyProgress.setVisibility(8);
            }

            public void onError(String str, GifException gifException) {
                MMContentFileViewerFragment.this.mGiphyProgress.setVisibility(8);
                MMContentFileViewerFragment.this.mViewPlaceHolder.setVisibility(0);
                MMContentFileViewerFragment.this.mTxtMessage.setText(C4558R.string.zm_mm_msg_download_image_failed);
            }
        });
    }

    private void refreshGiphyUI(@Nullable ZoomMessenger zoomMessenger) {
        String str;
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = myself.getJid();
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mMessageXMPPID);
                    if (messageByXMPPGuid != null) {
                        if (CollectionsUtil.isListEmpty(getGiphyMoreContextMenuItems())) {
                            this.mBtnMore.setVisibility(8);
                        } else {
                            this.mBtnMore.setVisibility(0);
                        }
                        this.mPanelNormalFile.setVisibility(8);
                        this.mImageview.setVisibility(8);
                        this.mPdfView.setVisibility(8);
                        this.mTxtTranslateSpeed.setVisibility(8);
                        this.mUploadProgressBar.setVisibility(8);
                        this.mTxtNotSupportPreview.setVisibility(8);
                        this.mTxtFileSharees.setText(getString(C4558R.string.zm_lbl_content_no_share));
                        this.mBtnDownload.setVisibility(8);
                        this.mBtnViewFile.setVisibility(8);
                        String formatSharedTime = formatSharedTime(messageByXMPPGuid.getServerSideTime());
                        if (StringUtil.isSameString(messageByXMPPGuid.getSenderID(), jid)) {
                            str = getString(C4558R.string.zm_lbl_content_you);
                        } else {
                            str = messageByXMPPGuid.getSenderName();
                        }
                        GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(messageByXMPPGuid.getGiphyID());
                        if (giphyInfo != null) {
                            File cacheFile = ImageLoader.getInstance().getCacheFile(giphyInfo.getPcUrl());
                            if (cacheFile == null || !cacheFile.exists()) {
                                downloadGiphy(giphyInfo.getPcUrl());
                            } else {
                                this.mTxtFileNameTtile.setText(cacheFile.getName());
                                TextView textView = this.mTxtFileDes;
                                StringBuilder sb = new StringBuilder();
                                sb.append(FileUtils.toFileSizeString(getActivity(), giphyInfo.getPcSize()));
                                sb.append(Broker.CALLER_CACHEKEY_PREFIX);
                                sb.append(str);
                                sb.append(PreferencesConstants.COOKIE_DELIMITER);
                                sb.append(formatSharedTime);
                                textView.setText(sb.toString());
                                ImageLoader.getInstance().displayGif(this.mImgGifView, (View) null, giphyInfo.getPcUrl());
                            }
                        }
                    }
                }
            }
        }
    }

    private void refreshUI() {
        String str;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = myself.getJid();
                MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                if (zoomFileContentMgr != null) {
                    MMZoomFile mMZoomFile = getMMZoomFile(zoomFileContentMgr);
                    if (mMZoomFile == null) {
                        refreshGiphyUI(zoomMessenger);
                        return;
                    }
                    if (CollectionsUtil.isListEmpty(getMoreContextMenuItems())) {
                        this.mBtnMore.setVisibility(8);
                    } else {
                        this.mBtnMore.setVisibility(0);
                    }
                    this.mTxtFileNameTtile.setText(mMZoomFile.getFileName());
                    String formatSharedTime = formatSharedTime(mMZoomFile.getTimeStamp());
                    if (StringUtil.isSameString(mMZoomFile.getOwnerJid(), jid)) {
                        str = getString(C4558R.string.zm_lbl_content_you);
                    } else {
                        str = mMZoomFile.getOwnerName();
                    }
                    TextView textView = this.mTxtFileDes;
                    StringBuilder sb = new StringBuilder();
                    sb.append(FileUtils.toFileSizeString(getActivity(), (long) mMZoomFile.getFileSize()));
                    sb.append(Broker.CALLER_CACHEKEY_PREFIX);
                    sb.append(str);
                    sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    sb.append(formatSharedTime);
                    textView.setText(sb.toString());
                    String str2 = "";
                    List<MMZoomShareAction> shareAction = mMZoomFile.getShareAction();
                    if (shareAction != null) {
                        StringBuffer stringBuffer = new StringBuffer();
                        if (!StringUtil.isSameString(mMZoomFile.getOwnerJid(), jid)) {
                            for (int size = shareAction.size() - 1; size >= 0; size--) {
                                MMZoomShareAction mMZoomShareAction = (MMZoomShareAction) shareAction.get(size);
                                String sharee = mMZoomShareAction.getSharee();
                                if (TextUtils.equals(sharee, jid)) {
                                    mMZoomShareAction.setSharee(mMZoomFile.getOwnerJid());
                                    mMZoomShareAction.setIsToMe(true);
                                }
                                if (StringUtil.isEmptyOrNull(sharee) || mMZoomShareAction.isBlockedByIB(getContext())) {
                                    shareAction.remove(size);
                                } else if (!StringUtil.isSameString(sharee, jid) && !mMZoomShareAction.isGroup() && !mMZoomShareAction.isMUC() && (!StringUtil.isSameString(sharee, mMZoomFile.getOwnerJid()) || !mMZoomShareAction.isToMe())) {
                                    shareAction.remove(size);
                                }
                            }
                        } else {
                            for (int size2 = shareAction.size() - 1; size2 >= 0; size2--) {
                                MMZoomShareAction mMZoomShareAction2 = (MMZoomShareAction) shareAction.get(size2);
                                if (StringUtil.isEmptyOrNull(mMZoomShareAction2.getSharee()) || mMZoomShareAction2.isBlockedByIB(getContext())) {
                                    shareAction.remove(size2);
                                } else if (!mMZoomShareAction2.isBuddy(getActivity()) && !mMZoomShareAction2.isGroup() && !mMZoomShareAction2.isMUC()) {
                                    shareAction.remove(size2);
                                }
                            }
                        }
                        for (MMZoomShareAction mMZoomShareAction3 : shareAction) {
                            if (StringUtil.isSameString(mMZoomShareAction3.getSharee(), jid) || (StringUtil.isSameString(mMZoomShareAction3.getSharee(), mMZoomFile.getOwnerJid()) && mMZoomShareAction3.isToMe())) {
                                stringBuffer.append(mMZoomFile.getOwnerName());
                            } else {
                                stringBuffer.append(mMZoomShareAction3.getShareeName(getActivity()));
                            }
                            stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                        }
                        str2 = stringBuffer.length() == 0 ? StringUtil.isSameString(mMZoomFile.getOwnerJid(), jid) ? "" : getString(C4558R.string.zm_lbl_content_share_in_buddy, str) : getString(C4558R.string.zm_lbl_content_share_in_group, stringBuffer.subSequence(0, stringBuffer.length() - 1));
                    }
                    if (str2.length() > 0) {
                        this.mTxtFileSharees.setText(str2);
                    } else {
                        this.mTxtFileSharees.setText(getString(C4558R.string.zm_lbl_content_no_share));
                    }
                    if (mMZoomFile.isFileDownloading()) {
                        this.mTxtTranslateSpeed.setVisibility(0);
                        this.mUploadProgressBar.setVisibility(0);
                        this.mBtnDownload.setVisibility(8);
                        this.mBtnViewFile.setVisibility(8);
                        this.mTxtNotSupportPreview.setVisibility(8);
                    } else if (StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath()) || !mMZoomFile.isFileDownloaded()) {
                        this.mBtnDownload.setVisibility(8);
                        this.mBtnViewFile.setVisibility(8);
                    } else {
                        this.mBtnDownload.setVisibility(8);
                        this.mBtnViewFile.setVisibility(0);
                    }
                    if (mMZoomFile.isFileDownloaded()) {
                        this.mTxtTranslateSpeed.setVisibility(8);
                        this.mUploadProgressBar.setVisibility(8);
                        this.mTxtNotSupportPreview.setVisibility(8);
                    }
                    if (StringUtil.isEmptyOrNull(mMZoomFile.getPicturePreviewPath()) || !new File(mMZoomFile.getPicturePreviewPath()).exists()) {
                        this.mImgFileType.setImageResource(AndroidAppUtil.getIconForFile(mMZoomFile.getFileName()));
                    } else {
                        this.mImgFileType.setImageDrawable(new LazyLoadDrawable(mMZoomFile.getPicturePreviewPath()));
                    }
                    if (mMZoomFile.getFileType() == 100 && !StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath()) && new File(mMZoomFile.getLocalPath()).exists() && ImageUtil.isValidImageFile(mMZoomFile.getLocalPath())) {
                        if (AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(mMZoomFile.getLocalPath()))) {
                            mMZoomFile.setFileType(5);
                        } else {
                            mMZoomFile.setFileType(1);
                        }
                    }
                    if (mMZoomFile.getFileType() == 5 && mMZoomFile.getLocalPath() != null && !AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(mMZoomFile.getLocalPath()))) {
                        mMZoomFile.setFileType(1);
                    }
                    int fileType = mMZoomFile.getFileType();
                    if (fileType != 100) {
                        switch (fileType) {
                            case 1:
                            case 4:
                                if (!StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath())) {
                                    Bitmap decodeFile = ZMBitmapFactory.decodeFile(mMZoomFile.getLocalPath(), MAX_IMAGE_SIZE_IN_AREA, false, false);
                                    if (decodeFile != null) {
                                        this.mImageview.setImage(ImageSource.bitmap(decodeFile));
                                        setSensorOrientation();
                                    }
                                }
                                this.mImgGifView.setVisibility(8);
                                this.mPdfView.setVisibility(8);
                                if (!StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath()) && new File(mMZoomFile.getLocalPath()).exists()) {
                                    this.mPanelNormalFile.setVisibility(8);
                                    this.mImageview.setVisibility(0);
                                    break;
                                } else {
                                    this.mPanelNormalFile.setVisibility(0);
                                    this.mImageview.setVisibility(8);
                                    this.mBtnDownload.setVisibility(8);
                                    this.mBtnViewFile.setVisibility(8);
                                    this.mUploadProgressBar.setVisibility(0);
                                    this.mTxtNotSupportPreview.setVisibility(8);
                                    break;
                                }
                                break;
                            case 2:
                            case 3:
                                this.mPanelNormalFile.setVisibility(0);
                                this.mImageview.setVisibility(8);
                                this.mImgGifView.setVisibility(8);
                                this.mPdfView.setVisibility(8);
                                break;
                            case 5:
                                this.mImageview.setVisibility(8);
                                this.mPdfView.setVisibility(8);
                                this.mImgGifView.setGifResourse(mMZoomFile.getLocalPath());
                                if (!StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath()) && new File(mMZoomFile.getLocalPath()).exists()) {
                                    this.mPanelNormalFile.setVisibility(8);
                                    this.mImgGifView.setVisibility(0);
                                    setSensorOrientation();
                                    break;
                                } else {
                                    this.mPanelNormalFile.setVisibility(0);
                                    this.mImgGifView.setVisibility(8);
                                    this.mBtnDownload.setVisibility(8);
                                    this.mBtnViewFile.setVisibility(8);
                                    this.mUploadProgressBar.setVisibility(0);
                                    this.mTxtNotSupportPreview.setVisibility(8);
                                    break;
                                }
                                break;
                        }
                    } else {
                        this.mImageview.setVisibility(8);
                        this.mImgGifView.setVisibility(8);
                        if (!mMZoomFile.isFileDownloaded() || !isPdfFile(mMZoomFile.getFileName()) || StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath())) {
                            this.mPanelNormalFile.setVisibility(0);
                        } else {
                            this.mPdfView.setPdfFile(mMZoomFile.getLocalPath(), null);
                            this.mPdfView.setVisibility(0);
                            this.mPanelNormalFile.setVisibility(8);
                            this.mPanelContent.setOnClickListener(null);
                        }
                        if (isImageFile(mMZoomFile.getFileName()) || isPdfFile(mMZoomFile.getFileName())) {
                            this.mTxtNotSupportPreview.setVisibility(8);
                        }
                    }
                    int fileTransferState = mMZoomFile.getFileTransferState();
                    boolean z = fileTransferState == 11;
                    if (!z && !NetworkUtil.hasDataNetwork(getActivity())) {
                        z = fileTransferState != 13;
                    }
                    if (this.mIsFileUnavailable && fileTransferState == 11) {
                        HintDialogFragment.showHintDialog(getFragmentManager(), this, REQUEST_CODE_SHOW_FILE_NO_EXIST, null, getResources().getString(C4558R.string.zm_content_file_downloaded_result_is_unavailable_text_89710), getResources().getString(C4558R.string.zm_btn_ok), null);
                    }
                    if (z) {
                        this.mViewPlaceHolder.setVisibility(0);
                        this.mTxtMessage.setText(ZmPtUtils.isImageFile(mMZoomFile.getFileType()) ? C4558R.string.zm_mm_msg_download_image_failed : C4558R.string.zm_mm_msg_download_other_failed);
                        this.mTxtTranslateSpeed.setVisibility(4);
                        this.mUploadProgressBar.setVisibility(4);
                        this.mBtnDownload.setVisibility(8);
                    } else {
                        this.mViewPlaceHolder.setVisibility(8);
                    }
                }
            }
        }
    }

    private void setSensorOrientation() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setRequestedOrientation(4);
        }
    }

    private String formatSharedTime(long j) {
        int dateDiff = TimeUtil.dateDiff(j, System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", CompatUtils.getLocalDefault());
        Date date = new Date(j);
        String format = simpleDateFormat.format(date);
        if (dateDiff == 1) {
            return getString(C4558R.string.zm_lbl_content_time_today_format, format);
        }
        String format2 = new SimpleDateFormat("MMM d", CompatUtils.getLocalDefault()).format(date);
        return getString(C4558R.string.zm_lbl_content_time_other_day_format, format2, format);
    }

    private void downloadFile() {
        boolean z;
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ZoomFile zoomFile = getZoomFile(zoomFileContentMgr);
            if (zoomFile != null) {
                MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(zoomFile, zoomFileContentMgr);
                if (!initWithZoomFile.isFileDownloading()) {
                    if (!initWithZoomFile.isFileDownloaded() || StringUtil.isEmptyOrNull(initWithZoomFile.getLocalPath())) {
                        if (!StringUtil.isEmptyOrNull(this.mMessageID)) {
                            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                            if (zoomMessenger != null) {
                                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                                if (sessionById != null) {
                                    z = sessionById.downloadFileForMessage(this.mMessageID);
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            String str = this.mFileWebId;
                            z = !StringUtil.isEmptyOrNull(zoomFileContentMgr.downloadFile(str, PendingFileDataHelper.getContenFilePath(str, initWithZoomFile.getFileName())));
                        }
                        if (z) {
                            this.mTxtTranslateSpeed.setVisibility(0);
                            this.mUploadProgressBar.setVisibility(0);
                            this.mBtnDownload.setVisibility(8);
                            this.mBtnViewFile.setVisibility(8);
                            FT_DownloadByFileID_OnProgress("", initWithZoomFile.getWebID(), 0, 0, 0);
                            this.mViewPlaceHolder.setVisibility(8);
                        } else {
                            showDownloadErrorUI();
                        }
                    }
                }
            }
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnClose) {
                onClickBtnClose();
            } else if (id == C4558R.C4560id.btnMore) {
                onClickBtnMore();
            } else if (id == C4558R.C4560id.btnViewFile) {
                onClickBtnViewFile();
            } else if (id == C4558R.C4560id.btnDownload) {
                onClickBtnDownload();
            } else if (id == C4558R.C4560id.btnShare) {
                onClickBtnShare();
            } else if (id == C4558R.C4560id.panelContent) {
                onClickPanelContent();
            } else if (id == C4558R.C4560id.viewPlaceHolder) {
                onClickViewPlaceHolder();
            } else if (id == C4558R.C4560id.imageview) {
                onTapContent();
            }
        }
    }

    private void onClickViewPlaceHolder() {
        this.mViewPlaceHolder.setVisibility(8);
        downloadFile();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mMessageXMPPID);
                if (messageByXMPPGuid != null) {
                    GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(messageByXMPPGuid.getGiphyID());
                    if (giphyInfo != null) {
                        downloadGiphy(giphyInfo.getPcUrl());
                    }
                }
            }
        }
    }

    private void onClickPanelContent() {
        onTapContent();
    }

    private void onClickBtnShare() {
        MMSelectSessionAndBuddyFragment.showAsFragment(this, new Bundle(), false, false, 1);
    }

    private void onClickBtnDownload() {
        downloadFile();
    }

    private void onClickBtnViewFile() {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ZoomFile zoomFile = getZoomFile(zoomFileContentMgr);
            if (zoomFile != null) {
                MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(zoomFile, zoomFileContentMgr);
                if (!StringUtil.isEmptyOrNull(initWithZoomFile.getLocalPath())) {
                    boolean z = false;
                    MimeType mimeTypeOfFile = AndroidAppUtil.getMimeTypeOfFile(initWithZoomFile.getFileName());
                    if (mimeTypeOfFile != null) {
                        if (mimeTypeOfFile.fileType == 7) {
                            z = AndroidAppUtil.openFile(getActivity(), new File(initWithZoomFile.getLocalPath()), true);
                        } else {
                            z = AndroidAppUtil.openFile(getActivity(), new File(initWithZoomFile.getLocalPath()));
                        }
                    }
                    if (!z) {
                        new Builder(getActivity()).setMessage(C4558R.string.zm_lbl_system_not_support_preview).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) null).show();
                    }
                }
            }
        }
    }

    private void onClickBtnMore() {
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
        List moreContextMenuItems = getMoreContextMenuItems();
        if (moreContextMenuItems == null || moreContextMenuItems.size() == 0) {
            moreContextMenuItems = getGiphyMoreContextMenuItems();
            if (moreContextMenuItems == null || moreContextMenuItems.size() == 0) {
                return;
            }
        }
        zMMenuAdapter.addAll(moreContextMenuItems);
        ZMAlertDialog create = new Builder(getActivity()).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMContentFileViewerFragment.this.onSelectContextMenuItem((MoreContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    private List<MoreContextMenuItem> getGiphyMoreContextMenuItems() {
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
        if (sessionById == null) {
            return null;
        }
        ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mMessageXMPPID);
        if (messageByXMPPGuid == null || messageByXMPPGuid.getMessageType() != 12) {
            return null;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr == null) {
            return null;
        }
        MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(messageByXMPPGuid, this.mSessionId, zoomMessenger, sessionById.isGroup(), StringUtil.isSameString(messageByXMPPGuid.getSenderID(), myself.getJid()), getActivity(), IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), zoomFileContentMgr);
        if (initWithZoomMessage == null) {
            return null;
        }
        if (!(initWithZoomMessage.isE2E || zoomMessenger.e2eGetMyOption() == 2) && !PTApp.getInstance().isFileTransferDisabled()) {
            arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_mm_lbl_save_emoji), 6));
        }
        arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_mm_btn_save_image), 4));
        if (initWithZoomMessage.isDeleteable(this.mSessionId)) {
            arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_lbl_delete), 1));
        }
        return arrayList;
    }

    private List<MoreContextMenuItem> getMoreContextMenuItems() {
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr == null) {
            return null;
        }
        MMZoomFile mMZoomFile = getMMZoomFile(zoomFileContentMgr);
        if (mMZoomFile == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        boolean z = zoomMessenger.e2eGetMyOption() == 2;
        boolean isFileTransferDisabled = PTApp.getInstance().isFileTransferDisabled();
        if (!z && !this.mIsE2EFile && !isFileTransferDisabled) {
            arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_btn_share), 5));
        }
        if (ZmPtUtils.isImageFile(mMZoomFile.getFileType())) {
            String localPath = mMZoomFile.getLocalPath();
            if (!StringUtil.isEmptyOrNull(localPath) && new File(localPath).exists() && ImageUtil.isValidImageFile(localPath)) {
                arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_mm_btn_save_image), 4));
            }
            if (!z && !this.mIsE2EFile && !isFileTransferDisabled && ImageUtil.isValidImageFile(localPath)) {
                arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_mm_lbl_save_emoji), 6));
            }
        }
        if (isPdfFile(mMZoomFile.getFileName()) && mMZoomFile.isFileDownloaded() && AndroidAppUtil.hasActivityToOpenFile(getActivity(), new File(mMZoomFile.getLocalPath()))) {
            arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_btn_open_with_app_14906), 7));
        }
        if (!isFileTransferDisabled && !StringUtil.isEmptyOrNull(this.mFileWebId) && ((checkIsGroupOpt() && !z) || TextUtils.equals(myself.getJid(), mMZoomFile.getOwnerJid()))) {
            arrayList.add(new MoreContextMenuItem(getString(C4558R.string.zm_btn_delete), 1));
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(MoreContextMenuItem moreContextMenuItem) {
        int action = moreContextMenuItem.getAction();
        if (action != 1) {
            switch (action) {
                case 3:
                    onSelectContextMenuItemRenameFile();
                    return;
                case 4:
                    onSelectContextMenuItemSaveImageFile();
                    return;
                case 5:
                    onClickBtnShare();
                    return;
                case 6:
                    saveEmoji();
                    return;
                case 7:
                    openWithOtherAPP();
                    return;
                default:
                    return;
            }
        } else {
            onSelectContextMenuItemDeleteFile();
        }
    }

    private void openWithOtherAPP() {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ZoomFile zoomFile = getZoomFile(zoomFileContentMgr);
            if (zoomFile != null) {
                AndroidAppUtil.openFile(getActivity(), new File(MMZoomFile.initWithZoomFile(zoomFile, zoomFileContentMgr).getLocalPath()));
            }
        }
    }

    private void saveGiphyEmoji(@Nullable File file) {
        if (file != null && file.exists()) {
            final MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
            if (zoomPrivateStickerMgr != null) {
                new ZMAsyncTask<File, Void, String>() {
                    /* access modifiers changed from: protected */
                    /* JADX WARNING: Code restructure failed: missing block: B:68:0x015a, code lost:
                        r0 = th;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:69:0x015b, code lost:
                        r1 = null;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:73:0x015f, code lost:
                        r1 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0160, code lost:
                        r11 = r1;
                        r1 = r0;
                        r0 = r11;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0172, code lost:
                        r0 = th;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0173, code lost:
                        r1 = null;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0177, code lost:
                        r1 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0178, code lost:
                        r11 = r1;
                        r1 = r0;
                        r0 = r11;
                     */
                    /* JADX WARNING: Failed to process nested try/catch */
                    /* JADX WARNING: Removed duplicated region for block: B:68:0x015a A[ExcHandler: all (th java.lang.Throwable), Splitter:B:36:0x0120] */
                    /* JADX WARNING: Removed duplicated region for block: B:83:0x0172 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:34:0x011b] */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public java.lang.String doInBackground(java.io.File... r13) {
                        /*
                            r12 = this;
                            r0 = 0
                            r1 = r13[r0]
                            if (r1 != 0) goto L_0x0008
                            java.lang.String r13 = ""
                            return r13
                        L_0x0008:
                            r13 = r13[r0]
                            boolean r0 = r13.exists()
                            if (r0 != 0) goto L_0x0013
                            java.lang.String r13 = ""
                            return r13
                        L_0x0013:
                            java.io.File r0 = com.zipow.videobox.util.ImageUtil.getZoomGalleryPath()
                            if (r0 != 0) goto L_0x001c
                            java.lang.String r13 = ""
                            return r13
                        L_0x001c:
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r2 = r0.getPath()
                            r1.append(r2)
                            java.lang.String r2 = java.io.File.separator
                            r1.append(r2)
                            java.lang.String r2 = r13.getName()
                            r1.append(r2)
                            java.lang.String r1 = r1.toString()
                            java.lang.String r2 = r13.getName()
                            java.lang.String r2 = r2.toLowerCase()
                            java.lang.String r3 = ".png"
                            boolean r2 = r2.endsWith(r3)
                            if (r2 != 0) goto L_0x00f0
                            java.lang.String r2 = r13.getName()
                            java.lang.String r2 = r2.toLowerCase()
                            java.lang.String r3 = ".jpg"
                            boolean r2 = r2.endsWith(r3)
                            if (r2 != 0) goto L_0x00f0
                            java.lang.String r2 = r13.getName()
                            java.lang.String r2 = r2.toLowerCase()
                            java.lang.String r3 = ".gif"
                            boolean r2 = r2.endsWith(r3)
                            if (r2 == 0) goto L_0x006a
                            goto L_0x00f0
                        L_0x006a:
                            java.lang.String r2 = r13.getAbsolutePath()
                            java.lang.String r2 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r2)
                            java.lang.String r3 = "image/gif"
                            boolean r3 = r2.equalsIgnoreCase(r3)
                            if (r3 == 0) goto L_0x009c
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r0 = ".gif"
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                            goto L_0x010c
                        L_0x009c:
                            java.lang.String r3 = "image/jpeg"
                            boolean r3 = r2.equalsIgnoreCase(r3)
                            if (r3 == 0) goto L_0x00c6
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r0 = ".jpg"
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                            goto L_0x010c
                        L_0x00c6:
                            java.lang.String r3 = "image/png"
                            boolean r2 = r2.equalsIgnoreCase(r3)
                            if (r2 == 0) goto L_0x010c
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r0 = ".png"
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                            goto L_0x010c
                        L_0x00f0:
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                        L_0x010c:
                            java.io.File r0 = new java.io.File
                            r0.<init>(r1)
                            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x019f }
                            r2.<init>(r13)     // Catch:{ Exception -> 0x019f }
                            r13 = 0
                            java.nio.channels.FileChannel r9 = r2.getChannel()     // Catch:{ Throwable -> 0x018e }
                            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0175, all -> 0x0172 }
                            r10.<init>(r0)     // Catch:{ Throwable -> 0x0175, all -> 0x0172 }
                            java.nio.channels.FileChannel r0 = r10.getChannel()     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                            r5 = 0
                            long r7 = r9.size()     // Catch:{ Throwable -> 0x0143, all -> 0x0140 }
                            r3 = r0
                            r4 = r9
                            r3.transferFrom(r4, r5, r7)     // Catch:{ Throwable -> 0x0143, all -> 0x0140 }
                            if (r0 == 0) goto L_0x0134
                            r0.close()     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                        L_0x0134:
                            r10.close()     // Catch:{ Throwable -> 0x0175, all -> 0x0172 }
                            if (r9 == 0) goto L_0x013c
                            r9.close()     // Catch:{ Throwable -> 0x018e }
                        L_0x013c:
                            r2.close()     // Catch:{ Exception -> 0x019f }
                            return r1
                        L_0x0140:
                            r1 = move-exception
                            r3 = r13
                            goto L_0x0149
                        L_0x0143:
                            r1 = move-exception
                            throw r1     // Catch:{ all -> 0x0145 }
                        L_0x0145:
                            r3 = move-exception
                            r11 = r3
                            r3 = r1
                            r1 = r11
                        L_0x0149:
                            if (r0 == 0) goto L_0x0159
                            if (r3 == 0) goto L_0x0156
                            r0.close()     // Catch:{ Throwable -> 0x0151, all -> 0x015a }
                            goto L_0x0159
                        L_0x0151:
                            r0 = move-exception
                            r3.addSuppressed(r0)     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                            goto L_0x0159
                        L_0x0156:
                            r0.close()     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                        L_0x0159:
                            throw r1     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                        L_0x015a:
                            r0 = move-exception
                            r1 = r13
                            goto L_0x0163
                        L_0x015d:
                            r0 = move-exception
                            throw r0     // Catch:{ all -> 0x015f }
                        L_0x015f:
                            r1 = move-exception
                            r11 = r1
                            r1 = r0
                            r0 = r11
                        L_0x0163:
                            if (r1 == 0) goto L_0x016e
                            r10.close()     // Catch:{ Throwable -> 0x0169, all -> 0x0172 }
                            goto L_0x0171
                        L_0x0169:
                            r3 = move-exception
                            r1.addSuppressed(r3)     // Catch:{ Throwable -> 0x0175, all -> 0x0172 }
                            goto L_0x0171
                        L_0x016e:
                            r10.close()     // Catch:{ Throwable -> 0x0175, all -> 0x0172 }
                        L_0x0171:
                            throw r0     // Catch:{ Throwable -> 0x0175, all -> 0x0172 }
                        L_0x0172:
                            r0 = move-exception
                            r1 = r13
                            goto L_0x017b
                        L_0x0175:
                            r0 = move-exception
                            throw r0     // Catch:{ all -> 0x0177 }
                        L_0x0177:
                            r1 = move-exception
                            r11 = r1
                            r1 = r0
                            r0 = r11
                        L_0x017b:
                            if (r9 == 0) goto L_0x018b
                            if (r1 == 0) goto L_0x0188
                            r9.close()     // Catch:{ Throwable -> 0x0183 }
                            goto L_0x018b
                        L_0x0183:
                            r3 = move-exception
                            r1.addSuppressed(r3)     // Catch:{ Throwable -> 0x018e }
                            goto L_0x018b
                        L_0x0188:
                            r9.close()     // Catch:{ Throwable -> 0x018e }
                        L_0x018b:
                            throw r0     // Catch:{ Throwable -> 0x018e }
                        L_0x018c:
                            r0 = move-exception
                            goto L_0x0190
                        L_0x018e:
                            r13 = move-exception
                            throw r13     // Catch:{ all -> 0x018c }
                        L_0x0190:
                            if (r13 == 0) goto L_0x019b
                            r2.close()     // Catch:{ Throwable -> 0x0196 }
                            goto L_0x019e
                        L_0x0196:
                            r1 = move-exception
                            r13.addSuppressed(r1)     // Catch:{ Exception -> 0x019f }
                            goto L_0x019e
                        L_0x019b:
                            r2.close()     // Catch:{ Exception -> 0x019f }
                        L_0x019e:
                            throw r0     // Catch:{ Exception -> 0x019f }
                        L_0x019f:
                            java.lang.String r13 = ""
                            return r13
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMContentFileViewerFragment.C36906.doInBackground(java.io.File[]):java.lang.String");
                    }

                    /* access modifiers changed from: protected */
                    public void onPostExecute(String str) {
                        if (!TextUtils.isEmpty(str)) {
                            zoomPrivateStickerMgr.uploadAndMakePrivateSticker(str);
                        }
                        super.onPostExecute(str);
                    }
                }.execute((Params[]) new File[]{file});
            }
        }
    }

    private void saveEmoji() {
        int i;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!TextUtils.isEmpty(this.mSessionId)) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mMessageXMPPID);
                    if (messageByXMPPGuid != null && messageByXMPPGuid.getMessageType() == 12) {
                        GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(messageByXMPPGuid.getGiphyID());
                        if (giphyInfo != null) {
                            File cacheFile = ImageLoader.getInstance().getCacheFile(giphyInfo.getPcUrl());
                            if (cacheFile == null || !cacheFile.exists()) {
                                cacheFile = ImageLoader.getInstance().getCacheFile(giphyInfo.getMobileUrl());
                                if (cacheFile == null || !cacheFile.exists()) {
                                    return;
                                }
                            }
                            if (cacheFile.length() >= FontStyle.FontStyle_PNG) {
                                SimpleMessageDialog.newInstance(C4558R.string.zm_msg_sticker_too_large, false).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                                return;
                            }
                            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                                saveGiphyEmoji(cacheFile);
                            } else {
                                zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_CODE_SAVE_EMOJI);
                            }
                            return;
                        }
                        return;
                    }
                }
            }
            MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
            if (zoomPrivateStickerMgr != null) {
                String str = this.mFileWebId;
                if (StringUtil.isEmptyOrNull(str)) {
                    MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                    if (zoomFileContentMgr != null) {
                        ZoomFile fileWithMessageID = zoomFileContentMgr.getFileWithMessageID(this.mSessionId, this.mMessageXMPPID);
                        if (fileWithMessageID != null) {
                            str = fileWithMessageID.getWebFileID();
                            long fileSize = (long) fileWithMessageID.getFileSize();
                            zoomFileContentMgr.destroyFileObject(fileWithMessageID);
                            if (fileSize > FontStyle.FontStyle_PNG) {
                                SimpleMessageDialog.newInstance(C4558R.string.zm_msg_sticker_too_large, false).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
                if (StringUtil.isEmptyOrNull(str)) {
                    MMZoomFile mMZoomFile = getMMZoomFile(PTApp.getInstance().getZoomFileContentMgr());
                    if (mMZoomFile == null || !ImageUtil.isValidImageFile(mMZoomFile.getLocalPath())) {
                        return;
                    }
                    if (mMZoomFile.getFileSize() > 8388608) {
                        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_sticker_too_large, false).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                        return;
                    }
                    i = zoomPrivateStickerMgr.makePrivateSticker(mMZoomFile.getLocalPath());
                } else {
                    i = zoomPrivateStickerMgr.makePrivateSticker(str);
                }
                if (i != 0) {
                    if (i != 2) {
                        switch (i) {
                            case 4:
                                break;
                            case 5:
                                break;
                        }
                    }
                    Toast.makeText(getActivity(), C4558R.string.zm_msg_duplicate_emoji, 1).show();
                }
                Toast.makeText(getActivity(), C4558R.string.zm_mm_msg_save_emoji_failed, 1).show();
            }
        }
    }

    @Nullable
    private ZoomFile getZoomFile(@Nullable MMFileContentMgr mMFileContentMgr) {
        if (mMFileContentMgr == null) {
            return null;
        }
        if (StringUtil.isEmptyOrNull(this.mMessageXMPPID) || StringUtil.isEmptyOrNull(this.mSessionId)) {
            return mMFileContentMgr.getFileWithWebFileID(this.mFileWebId);
        }
        return mMFileContentMgr.getFileWithMessageID(this.mSessionId, this.mMessageXMPPID);
    }

    @Nullable
    private MMZoomFile getMMZoomFile(MMFileContentMgr mMFileContentMgr) {
        return ZMIMUtils.getMMZoomFile(mMFileContentMgr, this.mSessionId, this.mMessageXMPPID, this.mFileWebId);
    }

    private void saveImage(@Nullable final String str) {
        if (!StringUtil.isEmptyOrNull(str) && new File(str).exists() && ImageUtil.isValidImageFile(str)) {
            C36917 r0 = new Thread("SaveImage") {
                /* JADX WARNING: Code restructure failed: missing block: B:100:0x0105, code lost:
                    r0 = th;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:114:?, code lost:
                    r19.close();
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:119:0x0123, code lost:
                    r19.close();
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:121:0x0127, code lost:
                    r0 = move-exception;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:122:0x0128, code lost:
                    r2 = r0;
                    r3 = r8;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:123:0x012b, code lost:
                    r0 = move-exception;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:124:0x012c, code lost:
                    r2 = r0;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:126:?, code lost:
                    throw r2;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:127:0x012e, code lost:
                    r0 = move-exception;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:128:0x012f, code lost:
                    r3 = r2;
                    r2 = r0;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:94:0x00fb, code lost:
                    r0 = move-exception;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
                    r3.addSuppressed(r0);
                 */
                /* JADX WARNING: Exception block dominator not found, dom blocks: [B:45:0x00a5, B:92:0x00f7] */
                /* JADX WARNING: Failed to process nested try/catch */
                /* JADX WARNING: Removed duplicated region for block: B:100:0x0105 A[ExcHandler: all (th java.lang.Throwable), PHI: r19 
                  PHI: (r19v6 java.io.FileOutputStream) = (r19v7 java.io.FileOutputStream), (r19v7 java.io.FileOutputStream), (r19v7 java.io.FileOutputStream), (r19v12 java.io.FileOutputStream), (r19v12 java.io.FileOutputStream), (r19v12 java.io.FileOutputStream), (r19v12 java.io.FileOutputStream), (r19v12 java.io.FileOutputStream), (r19v12 java.io.FileOutputStream) binds: [B:92:0x00f7, B:96:0x00fd, B:93:?, B:69:0x00d1, B:70:?, B:58:0x00c0, B:59:?, B:45:0x00a5, B:46:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:45:0x00a5] */
                /* JADX WARNING: Removed duplicated region for block: B:113:0x0119 A[SYNTHETIC, Splitter:B:113:0x0119] */
                /* JADX WARNING: Removed duplicated region for block: B:119:0x0123 A[Catch:{ Throwable -> 0x012b, all -> 0x0127, all -> 0x012e }] */
                /* JADX WARNING: Removed duplicated region for block: B:121:0x0127 A[ExcHandler: all (r0v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:31:0x007d] */
                /* JADX WARNING: Removed duplicated region for block: B:91:0x00f5  */
                /* JADX WARNING: Unknown top exception splitter block from list: {B:60:0x00c3=Splitter:B:60:0x00c3, B:71:0x00d4=Splitter:B:71:0x00d4, B:47:0x00a8=Splitter:B:47:0x00a8} */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r20 = this;
                        r1 = r20
                        java.lang.String r0 = r3
                        boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
                        if (r0 == 0) goto L_0x000b
                        return
                    L_0x000b:
                        java.io.File r0 = new java.io.File
                        java.lang.String r2 = r3
                        r0.<init>(r2)
                        boolean r2 = r0.exists()
                        if (r2 != 0) goto L_0x0019
                        return
                    L_0x0019:
                        boolean r2 = p021us.zoom.androidlib.util.OsUtil.isAtLeastQ()
                        r3 = 1
                        if (r2 == 0) goto L_0x0037
                        android.content.Context r2 = com.zipow.videobox.VideoBoxApplication.getGlobalContext()
                        if (r2 != 0) goto L_0x0027
                        return
                    L_0x0027:
                        android.net.Uri r4 = p021us.zoom.androidlib.util.FileUtils.insertFileIntoMediaStore(r2, r0)
                        if (r4 == 0) goto L_0x0159
                        boolean r0 = p021us.zoom.androidlib.util.FileUtils.copyFileToUri(r2, r0, r4)
                        if (r0 == 0) goto L_0x0159
                        r1._onSaveImageDone(r3)
                        return
                    L_0x0037:
                        java.io.File r2 = com.zipow.videobox.util.ImageUtil.getZoomGalleryPath()
                        if (r2 != 0) goto L_0x003e
                        return
                    L_0x003e:
                        java.lang.StringBuilder r4 = new java.lang.StringBuilder
                        r4.<init>()
                        java.lang.String r2 = r2.getPath()
                        r4.append(r2)
                        java.lang.String r2 = java.io.File.separator
                        r4.append(r2)
                        java.lang.String r2 = r0.getName()
                        r4.append(r2)
                        java.lang.String r2 = r4.toString()
                        java.io.File r4 = new java.io.File
                        r4.<init>(r2)
                        boolean r5 = r4.exists()
                        r6 = 0
                        if (r5 == 0) goto L_0x0073
                        long r8 = r4.length()
                        int r5 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
                        if (r5 <= 0) goto L_0x0073
                        r1._onSaveImageDone(r3)
                        return
                    L_0x0073:
                        java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0159 }
                        r5.<init>(r0)     // Catch:{ Exception -> 0x0159 }
                        r8 = 0
                        java.nio.channels.FileChannel r15 = r5.getChannel()     // Catch:{ Throwable -> 0x0146 }
                        java.io.FileOutputStream r13 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                        r13.<init>(r4)     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                        java.nio.channels.FileChannel r16 = r13.getChannel()     // Catch:{ Throwable -> 0x010f, all -> 0x0109 }
                        r11 = 0
                        long r17 = r15.size()     // Catch:{ Throwable -> 0x00eb, all -> 0x00e5 }
                        r9 = r16
                        r10 = r15
                        r19 = r13
                        r13 = r17
                        long r9 = r9.transferFrom(r10, r11, r13)     // Catch:{ Throwable -> 0x00e3, all -> 0x00e1 }
                        int r0 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1))
                        if (r0 <= 0) goto L_0x00cf
                        com.zipow.videobox.view.mm.MMContentFileViewerFragment r0 = com.zipow.videobox.view.p014mm.MMContentFileViewerFragment.this     // Catch:{ Throwable -> 0x00e3, all -> 0x00e1 }
                        androidx.fragment.app.FragmentActivity r0 = r0.getActivity()     // Catch:{ Throwable -> 0x00e3, all -> 0x00e1 }
                        if (r0 != 0) goto L_0x00b4
                        if (r16 == 0) goto L_0x00a8
                        r16.close()     // Catch:{ Throwable -> 0x0107, all -> 0x0105 }
                    L_0x00a8:
                        r19.close()     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                        if (r15 == 0) goto L_0x00b0
                        r15.close()     // Catch:{ Throwable -> 0x0146 }
                    L_0x00b0:
                        r5.close()     // Catch:{ Exception -> 0x0159 }
                        return
                    L_0x00b4:
                        java.lang.String r2 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r2)     // Catch:{ Throwable -> 0x00e3, all -> 0x00e1 }
                        p021us.zoom.androidlib.util.AndroidAppUtil.addImageToGallery(r0, r4, r2)     // Catch:{ Throwable -> 0x00e3, all -> 0x00e1 }
                        r1._onSaveImageDone(r3)     // Catch:{ Throwable -> 0x00e3, all -> 0x00e1 }
                        if (r16 == 0) goto L_0x00c3
                        r16.close()     // Catch:{ Throwable -> 0x0107, all -> 0x0105 }
                    L_0x00c3:
                        r19.close()     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                        if (r15 == 0) goto L_0x00cb
                        r15.close()     // Catch:{ Throwable -> 0x0146 }
                    L_0x00cb:
                        r5.close()     // Catch:{ Exception -> 0x0159 }
                        return
                    L_0x00cf:
                        if (r16 == 0) goto L_0x00d4
                        r16.close()     // Catch:{ Throwable -> 0x0107, all -> 0x0105 }
                    L_0x00d4:
                        r19.close()     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                        if (r15 == 0) goto L_0x00dc
                        r15.close()     // Catch:{ Throwable -> 0x0146 }
                    L_0x00dc:
                        r5.close()     // Catch:{ Exception -> 0x0159 }
                        goto L_0x0159
                    L_0x00e1:
                        r0 = move-exception
                        goto L_0x00e8
                    L_0x00e3:
                        r0 = move-exception
                        goto L_0x00ee
                    L_0x00e5:
                        r0 = move-exception
                        r19 = r13
                    L_0x00e8:
                        r2 = r0
                        r3 = r8
                        goto L_0x00f3
                    L_0x00eb:
                        r0 = move-exception
                        r19 = r13
                    L_0x00ee:
                        r2 = r0
                        throw r2     // Catch:{ all -> 0x00f0 }
                    L_0x00f0:
                        r0 = move-exception
                        r3 = r2
                        r2 = r0
                    L_0x00f3:
                        if (r16 == 0) goto L_0x0104
                        if (r3 == 0) goto L_0x0101
                        r16.close()     // Catch:{ Throwable -> 0x00fb, all -> 0x0105 }
                        goto L_0x0104
                    L_0x00fb:
                        r0 = move-exception
                        r4 = r0
                        r3.addSuppressed(r4)     // Catch:{ Throwable -> 0x0107, all -> 0x0105 }
                        goto L_0x0104
                    L_0x0101:
                        r16.close()     // Catch:{ Throwable -> 0x0107, all -> 0x0105 }
                    L_0x0104:
                        throw r2     // Catch:{ Throwable -> 0x0107, all -> 0x0105 }
                    L_0x0105:
                        r0 = move-exception
                        goto L_0x010c
                    L_0x0107:
                        r0 = move-exception
                        goto L_0x0112
                    L_0x0109:
                        r0 = move-exception
                        r19 = r13
                    L_0x010c:
                        r2 = r0
                        r3 = r8
                        goto L_0x0117
                    L_0x010f:
                        r0 = move-exception
                        r19 = r13
                    L_0x0112:
                        r2 = r0
                        throw r2     // Catch:{ all -> 0x0114 }
                    L_0x0114:
                        r0 = move-exception
                        r3 = r2
                        r2 = r0
                    L_0x0117:
                        if (r3 == 0) goto L_0x0123
                        r19.close()     // Catch:{ Throwable -> 0x011d, all -> 0x0127 }
                        goto L_0x0126
                    L_0x011d:
                        r0 = move-exception
                        r4 = r0
                        r3.addSuppressed(r4)     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                        goto L_0x0126
                    L_0x0123:
                        r19.close()     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                    L_0x0126:
                        throw r2     // Catch:{ Throwable -> 0x012b, all -> 0x0127 }
                    L_0x0127:
                        r0 = move-exception
                        r2 = r0
                        r3 = r8
                        goto L_0x0131
                    L_0x012b:
                        r0 = move-exception
                        r2 = r0
                        throw r2     // Catch:{ all -> 0x012e }
                    L_0x012e:
                        r0 = move-exception
                        r3 = r2
                        r2 = r0
                    L_0x0131:
                        if (r15 == 0) goto L_0x0142
                        if (r3 == 0) goto L_0x013f
                        r15.close()     // Catch:{ Throwable -> 0x0139 }
                        goto L_0x0142
                    L_0x0139:
                        r0 = move-exception
                        r4 = r0
                        r3.addSuppressed(r4)     // Catch:{ Throwable -> 0x0146 }
                        goto L_0x0142
                    L_0x013f:
                        r15.close()     // Catch:{ Throwable -> 0x0146 }
                    L_0x0142:
                        throw r2     // Catch:{ Throwable -> 0x0146 }
                    L_0x0143:
                        r0 = move-exception
                        r2 = r0
                        goto L_0x0149
                    L_0x0146:
                        r0 = move-exception
                        r8 = r0
                        throw r8     // Catch:{ all -> 0x0143 }
                    L_0x0149:
                        if (r8 == 0) goto L_0x0155
                        r5.close()     // Catch:{ Throwable -> 0x014f }
                        goto L_0x0158
                    L_0x014f:
                        r0 = move-exception
                        r3 = r0
                        r8.addSuppressed(r3)     // Catch:{ Exception -> 0x0159 }
                        goto L_0x0158
                    L_0x0155:
                        r5.close()     // Catch:{ Exception -> 0x0159 }
                    L_0x0158:
                        throw r2     // Catch:{ Exception -> 0x0159 }
                    L_0x0159:
                        r0 = 0
                        r1._onSaveImageDone(r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMContentFileViewerFragment.C36917.run():void");
                }

                private void _onSaveImageDone(final boolean z) {
                    MMContentFileViewerFragment.this.mHandler.post(new Runnable() {
                        public void run() {
                            MMContentFileViewerFragment.this.onSaveImageDone(z);
                        }
                    });
                }
            };
            showWaitingDialog();
            r0.start();
        }
    }

    /* access modifiers changed from: private */
    public void onSaveImageDone(boolean z) {
        dismissWaitingDialog();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, z ? C4558R.string.zm_mm_msg_saved_to_album : C4558R.string.zm_mm_msg_saved_to_album_failed_102727, 0).show();
        }
    }

    private void onSelectContextMenuItemSaveImageFile() {
        String str;
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                MMZoomFile mMZoomFile = getMMZoomFile(zoomFileContentMgr);
                if (mMZoomFile == null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                        if (sessionById != null) {
                            ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mMessageXMPPID);
                            if (messageByXMPPGuid != null) {
                                GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(messageByXMPPGuid.getGiphyID());
                                if (giphyInfo != null) {
                                    str = ImageLoader.getInstance().getCacheFilePath(giphyInfo.getPcUrl());
                                } else {
                                    return;
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
                } else {
                    str = mMZoomFile.getLocalPath();
                }
                saveImage(str);
            } else {
                return;
            }
        } else {
            zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_CODE_SAVE_IMAGE);
        }
        ZoomLogEventTracking.eventTrackSaveImage(this.mSessionId);
    }

    private void onSelectContextMenuItemRenameFile() {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ZoomFile zoomFile = getZoomFile(zoomFileContentMgr);
            if (zoomFile != null) {
                SetFileNameDialog.showSetNameDialog(getFragmentManager(), zoomFile.getFileName());
                zoomFileContentMgr.destroyFileObject(zoomFile);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setFileName(String str) {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null && !StringUtil.isEmptyOrNull(zoomFileContentMgr.renameFileByWebFileID(this.mFileWebId, str))) {
            showWaitingDialog();
        }
    }

    private void showWaitingDialog() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ProgressDialog progressDialog = this.mWaitingSaveDialog;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            this.mWaitingSaveDialog = new ProgressDialog(activity);
            this.mWaitingSaveDialog.requestWindowFeature(1);
            this.mWaitingSaveDialog.setMessage(activity.getString(C4558R.string.zm_msg_waiting));
            this.mWaitingSaveDialog.setCanceledOnTouchOutside(false);
            this.mWaitingSaveDialog.setCancelable(true);
            this.mWaitingSaveDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    MMContentFileViewerFragment.this.mWaitingSaveDialog = null;
                }
            });
            this.mWaitingSaveDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    MMContentFileViewerFragment.this.mWaitingSaveDialog = null;
                }
            });
            this.mWaitingSaveDialog.show();
        }
    }

    private void dismissWaitingDialog() {
        ProgressDialog progressDialog = this.mWaitingSaveDialog;
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (Exception unused) {
            }
        }
    }

    private void onSelectContextMenuItemDeleteFile() {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            if (!TextUtils.isEmpty(this.mMessageID)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        ZoomMessage messageById = sessionById.getMessageById(this.mMessageID);
                        if (messageById != null) {
                            ZoomBuddy myself = zoomMessenger.getMyself();
                            if (myself != null) {
                                MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(messageById, this.mSessionId, zoomMessenger, sessionById.isGroup(), StringUtil.isSameString(messageById.getSenderID(), myself.getJid()), getActivity(), IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), zoomFileContentMgr);
                                if (initWithZoomMessage != null && initWithZoomMessage.deleteMessage(getActivity())) {
                                    dismiss();
                                }
                            }
                        }
                    }
                }
            } else if (!StringUtil.isEmptyOrNull(this.mFileWebId)) {
                ZoomFile zoomFile = getZoomFile(zoomFileContentMgr);
                if (zoomFile != null) {
                    final MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(zoomFile, zoomFileContentMgr);
                    if (TextUtils.isEmpty(this.mSessionId)) {
                        String shrinkFileName = FileUtils.shrinkFileName(initWithZoomFile.getFileName(), 30);
                        if (shrinkFileName == null) {
                            shrinkFileName = "";
                        }
                        new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_msg_delete_file_confirm, shrinkFileName)).setMessage(C4558R.string.zm_msg_delete_file_warning_59554).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setPositiveButton(C4558R.string.zm_btn_delete, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MMContentFileViewerFragment.this.deleteFile(initWithZoomFile);
                            }
                        }).create().show();
                    } else {
                        deleteFile(initWithZoomFile);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void deleteFile(@Nullable MMZoomFile mMZoomFile) {
        if (mMZoomFile != null) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                String deleteFile = zoomFileContentMgr.deleteFile(mMZoomFile, this.mSessionId);
                if (!StringUtil.isEmptyOrNull(deleteFile)) {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_ACTION, 1);
                        intent.putExtra("zoomFileWebId", mMZoomFile.getWebID());
                        intent.putExtra("reqId", deleteFile);
                        activity.setResult(-1, intent);
                        dismiss();
                    }
                } else if (StringUtil.isEmptyOrNull(deleteFile)) {
                    ErrorMsgDialog.newInstance(getString(C4558R.string.zm_alert_unshare_file_failed), -1).show(getFragmentManager(), ErrorMsgDialog.class.getName());
                }
            }
        }
    }

    private void onClickBtnClose() {
        dismiss();
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C368512 r1 = new EventAction("MMContentFileViewerFragmentPermissionResult") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((MMContentFileViewerFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                }
            };
            eventTaskManager.pushLater(r1);
        }
    }

    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        String str;
        if (i == REQUEST_CODE_SAVE_IMAGE) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                if (zoomFileContentMgr != null) {
                    MMZoomFile mMZoomFile = getMMZoomFile(zoomFileContentMgr);
                    if (mMZoomFile == null) {
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                            if (sessionById != null) {
                                ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mMessageXMPPID);
                                if (messageByXMPPGuid != null) {
                                    GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(messageByXMPPGuid.getGiphyID());
                                    if (giphyInfo != null) {
                                        str = ImageLoader.getInstance().getCacheFilePath(giphyInfo.getPcUrl());
                                    } else {
                                        return;
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
                    } else {
                        str = mMZoomFile.getLocalPath();
                    }
                    saveImage(str);
                }
            }
        } else if (i == REQUEST_CODE_SAVE_EMOJI && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
            ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger2 != null) {
                ZoomChatSession sessionById2 = zoomMessenger2.getSessionById(this.mSessionId);
                if (sessionById2 != null) {
                    ZoomMessage messageByXMPPGuid2 = sessionById2.getMessageByXMPPGuid(this.mMessageXMPPID);
                    if (messageByXMPPGuid2 != null) {
                        GiphyMsgInfo giphyInfo2 = zoomMessenger2.getGiphyInfo(messageByXMPPGuid2.getGiphyID());
                        if (giphyInfo2 != null) {
                            saveGiphyEmoji(ImageLoader.getInstance().getCacheFile(giphyInfo2.getPcUrl()));
                        }
                    }
                }
            }
        }
    }
}
