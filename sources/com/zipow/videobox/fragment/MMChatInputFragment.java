package com.zipow.videobox.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
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
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.dialog.InformationBarriesDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.dialog.ShareAlertDialog;
import com.zipow.videobox.eventbus.ZMAtBuddyClickEvent;
import com.zipow.videobox.eventbus.ZMTemplateSelectProcessingEvent;
import com.zipow.videobox.fragment.InviteFragment.InviteFailedDialog;
import com.zipow.videobox.photopicker.PhotoPicker;
import com.zipow.videobox.ptapp.IMProtos.AtInfoItem;
import com.zipow.videobox.ptapp.IMProtos.AtInfoList;
import com.zipow.videobox.ptapp.IMProtos.CommentInfo;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationData;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrations;
import com.zipow.videobox.ptapp.IMProtos.FontStyte;
import com.zipow.videobox.ptapp.IMProtos.MessageInput;
import com.zipow.videobox.ptapp.IMProtos.RobotCommand;
import com.zipow.videobox.ptapp.IMProtos.StickerInfo;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack.IZoomPrivateStickerUIListener;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack.SimpleZoomPrivateStickerUIListener;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessage;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageSession;
import com.zipow.videobox.tempbean.IMessageTemplateSelect;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItemGroup;
import com.zipow.videobox.tempbean.IZoomMessageTemplate;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.TextCommandHelper;
import com.zipow.videobox.util.TextCommandHelper.SpanBean;
import com.zipow.videobox.util.TintUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMActionMsgUtil;
import com.zipow.videobox.util.ZMActionMsgUtil.ActionType;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.CommandEditText;
import com.zipow.videobox.view.CommandEditText.OnCommandActionListener;
import com.zipow.videobox.view.CommandEditText.SendMsgType;
import com.zipow.videobox.view.GiphyPreviewView.GiphyPreviewItem;
import com.zipow.videobox.view.GiphyPreviewView.OnGiphyPreviewItemClickListener;
import com.zipow.videobox.view.GiphyPreviewView.OnSearchListener;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import com.zipow.videobox.view.p014mm.MMContentFragment.UploadContextMenuItem;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMSlashCommandPopupView;
import com.zipow.videobox.view.p014mm.MMSlashCommandPopupView.OnSlashCommandClickListener;
import com.zipow.videobox.view.p014mm.MMSlashCommandPopupView.SlashItem;
import com.zipow.videobox.view.p014mm.PhoneLabelFragment;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import com.zipow.videobox.view.p014mm.sticker.StickerEvent;
import com.zipow.videobox.view.p014mm.sticker.StickerInputView;
import com.zipow.videobox.view.p014mm.sticker.StickerInputView.OnGiphyPreviewBackClickListener;
import com.zipow.videobox.view.p014mm.sticker.StickerInputView.OnPrivateStickerSelectListener;
import com.zipow.videobox.view.p014mm.sticker.StickerInputView.OnStickerSelectListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableEmitter;
import p015io.reactivex.ObservableOnSubscribe;
import p015io.reactivex.android.schedulers.AndroidSchedulers;
import p015io.reactivex.disposables.CompositeDisposable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.schedulers.Schedulers;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFileListActivity;
import p021us.zoom.androidlib.app.ZMLocalFileListAdapter;
import p021us.zoom.androidlib.data.FileInfo;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IDownloadFileListener;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
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

public class MMChatInputFragment extends ZMDialogFragment implements OnClickListener, OnPrivateStickerSelectListener, OnSearchListener, OnGiphyPreviewItemClickListener, OnStickerSelectListener, OnGiphyPreviewBackClickListener, OnCommandActionListener {
    private static final String ARG_ANCHOR_MSG = "anchorMsg";
    public static final String ARG_ANNOUNCEMENT = "isAnnounceMent";
    private static final String ARG_BUDDY_ID = "buddyId";
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_EVENT_ID = "eventid";
    private static final String ARG_GROUP_ID = "groupId";
    public static final String ARG_INPUT_MODE = "inputMode";
    private static final String ARG_IS_GROUP = "isGroup";
    public static final String ARG_PBX_SESSION = "pbxSession";
    private static final String ARG_SEND_INTENT = "sendIntent";
    public static final String ARG_SESSION_ID = "sessionId";
    private static final String ARG_SHARED_MESSAGE_ID = "messageid";
    public static final String ARG_THREAD_ID = "threadId";
    private static final int CHOOSE_PICTURE_MAX_COUNT = 9;
    public static final int INPUT_MODE_IM = 0;
    public static final int INPUT_MODE_PBX = 1;
    private static final int MAX_IMAGE_FILE_SIZE = 1048576;
    public static final int MAX_MESSAGE_LENGTH = 4096;
    public static final int MAX_SMS_MESSAGE_LENGTH = 500;
    private static final int MODE_EMOJI = 3;
    private static final int MODE_KEYBOARD = 0;
    private static final int MODE_MORE = 2;
    private static final int MODE_VOICE = 1;
    public static final int NOTIYY_GROUP_MEMBER_COUNT = 1000;
    private static final int REQUEST_CAPTURE_PHOTO = 101;
    private static final int REQUEST_CHAT_INFO = 102;
    public static final int REQUEST_CHOOSE_PHOTO = 7001;
    private static final int REQUEST_CHOOSE_PICTURE = 100;
    public static final int REQUEST_CODE_SAVE_EMOJI = 6001;
    public static final int REQUEST_CODE_SAVE_FILE_IMAGE = 5002;
    public static final int REQUEST_CODE_SAVE_IMAGE = 5001;
    private static final int REQUEST_CONFIRM_SEND_IMAGE = 103;
    public static final int REQUEST_DOCUMENT = 1010;
    public static final int REQUEST_DOCUMENT_BUSINESS_PICKER = 1015;
    public static final int REQUEST_DOCUMENT_PICKER = 1014;
    public static final int REQUEST_DOCUMENT_SHARE_LINK = 1016;
    public static final int REQUEST_EDIT_MESSAGE = 4001;
    public static final int REQUEST_GET_SHAREER = 2014;
    private static final int REQUEST_GET_SHAREES = 109;
    private static final int REQUEST_PERMISSION_BY_TAKE_PHOTO = 106;
    private static final int REQUEST_PERMISSION_MIC = 11;
    private static final int REQUEST_SELECT_CHANNEL = 114;
    private static final int REQUEST_SELECT_CONTACT = 105;
    private static final int REQUEST_SELECT_FILE_TO_SEND = 104;
    private static final int REQUEST_SELECT_SLASH_CONTACT = 110;
    private static final int REQUEST_SELECT_TEMPLETE_CHANNEL = 112;
    private static final int REQUEST_SELECT_TEMPLETE_CONTACT = 111;
    private static final int REQUEST_SELECT_TEMPLETE_COUSTOM = 113;
    private static final int REQUEST_STORAGE_BY_MESSAGE_OPERATION = 107;
    public static final int REQUEST_VIEW_FILE_DETAIL = 3001;
    private static final String TAG = "MMChatInputFragment";
    /* access modifiers changed from: private */
    public boolean mActionCopyMsg = false;
    private AsyncSharedLinkTask mAsyncSharedLinkTask;
    private View mBtnCamera;
    private ImageButton mBtnEmoji;
    private Button mBtnHoldToTalk;
    private ImageButton mBtnMoreOpts;
    private ImageButton mBtnSend;
    private View mBtnSendFile;
    private View mBtnSendPicture;
    private ImageButton mBtnSetModeKeyboard;
    private ImageButton mBtnSetModeVoice;
    private View mBtnVideoCall;
    private View mBtnVoiceCall;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    /* access modifiers changed from: private */
    public ProgressDialog mDownloadFileWaitingDialog = null;
    private TextWatcher mEditMsgWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            if (MMChatInputFragment.this.mmSlashCommandPopupView != null) {
                MMChatInputFragment.this.mmSlashCommandPopupView.setmFilter(editable.toString());
            }
            MMChatInputFragment.this.updateSendBtnPanels();
            if (editable.length() == 0 && MMChatInputFragment.this.mActionCopyMsg) {
                MMChatInputFragment.this.mActionCopyMsg = false;
            }
        }
    };
    /* access modifiers changed from: private */
    public CommandEditText mEdtMessage;
    private MMMessageItem mEmojiItem;
    private ExecutorService mExecutor = Executors.newFixedThreadPool(2);
    private Handler mHandler = new Handler();
    private IMAddrBookItem mIMAddrBookItem;
    private Uri mImageUri;
    private View mImgE2EFlag;
    private ImageButton mImgOptVideoCall;
    private ImageButton mImgOptVoiceCall;
    private int mInputMode = 0;
    private boolean mIsAnnounceMent;
    private boolean mIsE2EChat = false;
    private boolean mIsEmojiReactionMode = false;
    private boolean mIsGroup;
    private boolean mIsMyNotes = false;
    /* access modifiers changed from: private */
    public boolean mIsSendingHttpMsg = false;
    /* access modifiers changed from: private */
    @Nullable
    public ZMKeyboardDetector mKeyboardDetector;
    /* access modifiers changed from: private */
    public View mLineBelowSend;
    /* access modifiers changed from: private */
    public int mMode = 0;
    private OnChatInputListener mOnChatInputListener;
    private View mPanelActions;
    private View mPanelCamera;
    private StickerInputView mPanelEmojis;
    private View mPanelMoreOpts;
    private View mPanelMoreOptsRow2;
    private View mPanelSend;
    private View mPanelSendText;
    private View mPanelSendbtns;
    @Nullable
    private String mPbxFromNumber;
    @Nullable
    private List<String> mPbxToNumbers = null;
    private IPicker mPicker;
    private RecyclerView mRecyclerView;
    private String mSelectedPhoneNumber = null;
    private String mSessionId;
    private IZoomPrivateStickerUIListener mStickerListener = new SimpleZoomPrivateStickerUIListener() {
        public void OnStickerDownloaded(String str, int i) {
            MMChatInputFragment.this.OnStickerDownloaded(str, i);
        }

        public void OnNewStickerUploaded(String str, int i, String str2) {
            MMChatInputFragment.this.OnNewStickerUploaded(str, i, str2);
        }

        public void OnMakePrivateSticker(int i, String str, String str2) {
            MMChatInputFragment.this.OnMakePrivateSticker(i, str, str2);
        }

        public void OnDiscardPrivateSticker(int i, String str) {
            MMChatInputFragment.this.OnDiscardPrivateSticker(i, str);
        }

        public void OnPrivateStickersUpdated() {
            MMChatInputFragment.this.OnPrivateStickersUpdated();
        }
    };
    /* access modifiers changed from: private */
    public ZMAsyncURLDownloadFile mTaskDownloadFile;
    @Nullable
    private MMMessageItem mThreadItem;
    @Nullable
    private TextView mTxtCharatersLeft;
    private TextView mTxtOptVideoCall;
    private TextView mTxtOptVoiceCall;
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_GetHotGiphyInfoResult(int i, String str, List<String> list, String str2, String str3) {
            MMChatInputFragment.this.Indicate_GetHotGiphyInfoResult(i, str, list, str2, str3);
        }

        public void Indicate_GetGIFFromGiphyResultIml(int i, String str, List<String> list, String str2, String str3) {
            MMChatInputFragment.this.Indicate_GetGIFFromGiphyResultIml(i, str, list, str2, str3);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMChatInputFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void onConnectReturn(int i) {
            MMChatInputFragment.this.onConnectReturn(i);
        }

        public void E2E_MyStateUpdate(int i) {
            MMChatInputFragment.this.E2E_MyStateUpdate(i);
        }

        public void E2E_SessionStateUpdate(String str, String str2, int i, int i2) {
            MMChatInputFragment.this.E2E_SessionStateUpdate(str, str2, i, i2);
        }

        public void Indicate_BlockedUsersUpdated() {
            MMChatInputFragment.this.Indicate_BlockedUsersUpdated();
        }

        public void Indicate_BlockedUsersAdded(List<String> list) {
            MMChatInputFragment.this.Indicate_BlockedUsersAdded(list);
        }

        public void Indicate_BlockedUsersRemoved(List<String> list) {
            MMChatInputFragment.this.Indicate_BlockedUsersRemoved(list);
        }

        public void Indicate_BuddyPresenceChanged(String str) {
            MMChatInputFragment.this.Indicate_BuddyPresenceChanged(str);
        }

        public void NotifyInfoBarriesMsg(String str, String str2) {
            MMChatInputFragment.this.NotifyInfoBarriesMsg(str, str2);
        }
    };
    /* access modifiers changed from: private */
    public MMSlashCommandPopupView mmSlashCommandPopupView;

    private class AsyncSharedLinkTask extends ZMAsyncTask<String, Void, String> {
        private FileIntegrationInfo mFileIntegrationInfo;
        private String mFolder;
        private String mMessage;

        public AsyncSharedLinkTask(FileIntegrationInfo fileIntegrationInfo, String str, String str2) {
            this.mFileIntegrationInfo = fileIntegrationInfo;
            this.mFolder = str;
            this.mMessage = str2;
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            FileWriter fileWriter;
            FileIntegrationInfo fileIntegrationInfo = this.mFileIntegrationInfo;
            if (fileIntegrationInfo == null) {
                return "";
            }
            String fileName = fileIntegrationInfo.getFileName();
            if (StringUtil.isEmptyOrNull(fileName)) {
                return "";
            }
            File file = new File(this.mFolder, fileName);
            if (file.exists()) {
                file.delete();
            }
            if (!FileUtils.createFile(file.getAbsolutePath(), true)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.mMessage);
            sb.append(MMChatInputFragment.this.getString(C4558R.string.zm_msg_share_file_download_link_79752, this.mFileIntegrationInfo.getPreviewUrl()));
            String sb2 = sb.toString();
            try {
                fileWriter = new FileWriter(file, false);
                fileWriter.write(sb2);
                fileWriter.close();
            } catch (IOException unused) {
            } catch (Throwable th) {
                r2.addSuppressed(th);
            }
            return file.getAbsolutePath();
            throw th;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                MMChatInputFragment.this.sendSharedLink(this.mFileIntegrationInfo, str, this.mMessage);
            }
        }
    }

    private class DownloadFileListener implements IDownloadFileListener {
        private Uri mInput;

        public DownloadFileListener(Uri uri, long j, String str) {
            this.mInput = uri;
        }

        public void onDownloadCompleted(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri, String str) {
            if (uri != null && uri == this.mInput) {
                MMChatInputFragment.this.dismissDownloadFileWaitingDialog();
                if (!StringUtil.isEmptyOrNull(str)) {
                    MMChatInputFragment.this.uploadFile(str);
                }
            }
        }

        public void onDownloadFailed(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri) {
            if (uri != null && uri == this.mInput) {
                MMChatInputFragment.this.dismissDownloadFileWaitingDialog();
                String path = uri.getPath();
                if (StringUtil.isEmptyOrNull(path)) {
                    ShareAlertDialog.showDialog(MMChatInputFragment.this.getFragmentManager(), MMChatInputFragment.this.getString(C4558R.string.zm_msg_load_file_fail_without_name), false);
                } else {
                    String pathLastName = AndroidAppUtil.getPathLastName(path);
                    ShareAlertDialog.showDialog(MMChatInputFragment.this.getFragmentManager(), MMChatInputFragment.this.getString(C4558R.string.zm_msg_load_file_fail, pathLastName), false);
                }
            }
        }

        public void onDownloadProgress(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, long j, long j2) {
            MMChatInputFragment.this.updateProgressWaitingDialog(j, j2);
        }

        public void onDownloadCanceled(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri) {
            if (uri != null && uri == this.mInput) {
                MMChatInputFragment.this.dismissDownloadFileWaitingDialog();
            }
        }
    }

    public interface OnChatInputListener {
        void onCommentSent(String str, String str2, String str3);

        void onMessageSent(String str, String str2);

        void onPbxSmsSent(@Nullable String str, @Nullable String str2);

        void onShowInvitationsSent(int i);

        void onViewInitReady();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mInputMode = arguments.getInt(ARG_INPUT_MODE, 0);
            if (this.mInputMode == 1) {
                this.mSessionId = arguments.getString(ARG_PBX_SESSION);
                updateUIForPBX();
                this.mEdtMessage.enableStoreCommand(true, this.mSessionId, null);
                this.mEdtMessage.addTextChangedListener(this.mEditMsgWatcher);
            } else {
                this.mSessionId = arguments.getString("sessionId");
                this.mIsAnnounceMent = arguments.getBoolean(ARG_ANNOUNCEMENT);
                if (!TextUtils.isEmpty(this.mSessionId)) {
                    String string = arguments.getString("threadId");
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                        if (sessionById != null) {
                            if (!TextUtils.isEmpty(string)) {
                                ZoomBuddy myself = zoomMessenger.getMyself();
                                if (myself != null) {
                                    ZoomMessage messageById = sessionById.getMessageById(string);
                                    if (messageById != null) {
                                        boolean equals = TextUtils.equals(messageById.getSenderID(), myself.getJid());
                                        this.mThreadItem = MMMessageItem.initWithZoomMessage(messageById, this.mSessionId, zoomMessenger, this.mIsGroup, equals, getContext(), this.mIMAddrBookItem, null);
                                    }
                                } else {
                                    return;
                                }
                            }
                            setSessionInfo(this.mSessionId, sessionById.isGroup(), UIMgr.isMyNotes(this.mSessionId));
                            List checkIfNeedUpdateHotGiphyInfo = zoomMessenger.checkIfNeedUpdateHotGiphyInfo();
                            if (checkIfNeedUpdateHotGiphyInfo == null || checkIfNeedUpdateHotGiphyInfo.isEmpty()) {
                                zoomMessenger.getHotGiphyInfo(this.mSessionId, 8);
                            } else {
                                this.mPanelEmojis.Indicate_GetHotGiphyInfoResult(0, "", checkIfNeedUpdateHotGiphyInfo, "", this.mSessionId);
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
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(@NonNull ZMAtBuddyClickEvent zMAtBuddyClickEvent) {
        String jid = zMAtBuddyClickEvent.getJid();
        if (!TextUtils.isEmpty(jid)) {
            MMMessageItem messageItem = zMAtBuddyClickEvent.getMessageItem();
            if (messageItem != null && TextUtils.equals(this.mSessionId, messageItem.sessionId)) {
                if (messageItem.isComment) {
                    if (this.mThreadItem == null) {
                        return;
                    }
                } else if (this.mThreadItem != null) {
                    return;
                }
                IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
                if (TextUtils.equals(jid, MMSelectContactsFragment.JID_SELECTED_EVERYONE) || TextUtils.equals(jid, UIUtil.generateAtallSessionId(this.mSessionId))) {
                    iMAddrBookItem.setScreenName(getString(C4558R.string.zm_lbl_select_everyone));
                    iMAddrBookItem.setJid(MMSelectContactsFragment.JID_SELECTED_EVERYONE);
                } else {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(jid);
                        if (buddyWithJID != null) {
                            iMAddrBookItem = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                        }
                    }
                }
                onATBuddySelect(iMAddrBookItem);
            }
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_chat_input, viewGroup, false);
        this.mPanelSend = inflate.findViewById(C4558R.C4560id.panelSend);
        this.mBtnSetModeVoice = (ImageButton) inflate.findViewById(C4558R.C4560id.btnSetModeVoice);
        this.mBtnSetModeKeyboard = (ImageButton) inflate.findViewById(C4558R.C4560id.btnSetModeKeyboard);
        this.mBtnMoreOpts = (ImageButton) inflate.findViewById(C4558R.C4560id.btnMoreOpts);
        this.mBtnSend = (ImageButton) inflate.findViewById(C4558R.C4560id.btnSend);
        this.mBtnHoldToTalk = (Button) inflate.findViewById(C4558R.C4560id.btnHoldToTalk);
        this.mPanelMoreOpts = inflate.findViewById(C4558R.C4560id.panelMoreOpts);
        this.mPanelSendText = inflate.findViewById(C4558R.C4560id.panelSendText);
        this.mEdtMessage = (CommandEditText) inflate.findViewById(C4558R.C4560id.edtMessage);
        this.mBtnSendPicture = inflate.findViewById(C4558R.C4560id.btnSendPicture);
        this.mBtnCamera = inflate.findViewById(C4558R.C4560id.btnCamera);
        this.mBtnVoiceCall = inflate.findViewById(C4558R.C4560id.btnVoiceCall);
        this.mBtnVideoCall = inflate.findViewById(C4558R.C4560id.btnVideoCall);
        this.mImgOptVoiceCall = (ImageButton) inflate.findViewById(C4558R.C4560id.imgOptVoiceCall);
        this.mTxtOptVoiceCall = (TextView) inflate.findViewById(C4558R.C4560id.txtOptVoiceCall);
        this.mTxtOptVideoCall = (TextView) inflate.findViewById(C4558R.C4560id.txtOptVideoCall);
        this.mImgOptVideoCall = (ImageButton) inflate.findViewById(C4558R.C4560id.imgOptVideoCall);
        this.mPanelActions = inflate.findViewById(C4558R.C4560id.panelActions);
        this.mPanelMoreOptsRow2 = inflate.findViewById(C4558R.C4560id.panelMoreOptsRow2);
        this.mPanelCamera = inflate.findViewById(C4558R.C4560id.panelCamera);
        this.mImgE2EFlag = inflate.findViewById(C4558R.C4560id.imgE2EFlag);
        this.mPanelSendbtns = inflate.findViewById(C4558R.C4560id.panelSendbtns);
        this.mBtnSendFile = inflate.findViewById(C4558R.C4560id.btnSendFile);
        this.mBtnEmoji = (ImageButton) inflate.findViewById(C4558R.C4560id.btnEmoji);
        this.mPanelEmojis = (StickerInputView) inflate.findViewById(C4558R.C4560id.panelEmojis);
        this.mLineBelowSend = inflate.findViewById(C4558R.C4560id.lineBelowSend);
        this.mTxtCharatersLeft = (TextView) inflate.findViewById(C4558R.C4560id.txtCharatersLeft);
        this.mBtnSetModeVoice.setOnClickListener(this);
        this.mBtnSetModeKeyboard.setOnClickListener(this);
        this.mBtnMoreOpts.setOnClickListener(this);
        this.mBtnSend.setOnClickListener(this);
        this.mBtnSendPicture.setOnClickListener(this);
        this.mBtnCamera.setOnClickListener(this);
        this.mBtnVoiceCall.setOnClickListener(this);
        this.mBtnSendFile.setOnClickListener(this);
        this.mBtnVideoCall.setOnClickListener(this);
        this.mBtnEmoji.setOnClickListener(this);
        this.mPanelEmojis.setEmojiInputEditText(this.mEdtMessage);
        this.mPanelEmojis.setOnPrivateStickerSelectListener(this);
        this.mPanelEmojis.setmGiphyPreviewItemClickListener(this);
        this.mPanelEmojis.setOnsearchListener(this);
        this.mPanelEmojis.setmOnGiphySelectListener(this);
        this.mPanelEmojis.setmOnGiphyPreviewBackClickListener(this);
        updateUIMode(this.mMode);
        this.mEdtMessage.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i != 66 || keyEvent.getAction() != 0 || !keyEvent.isCtrlPressed()) {
                    return false;
                }
                MMChatInputFragment.this.onClickBtnSend(null);
                return true;
            }
        });
        this.mEdtMessage.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (!z) {
                    MMChatInputFragment.this.mLineBelowSend.setVisibility(4);
                } else if ((MMChatInputFragment.this.mKeyboardDetector == null || !MMChatInputFragment.this.mKeyboardDetector.isKeyboardOpen()) && MMChatInputFragment.this.mMode != 3) {
                    MMChatInputFragment.this.mLineBelowSend.setVisibility(4);
                } else {
                    MMChatInputFragment.this.mLineBelowSend.setVisibility(0);
                }
            }
        });
        if (this.mInputMode == 1 || PTApp.getInstance().isFileTransferDisabled()) {
            disableFileTransfer();
        }
        OnChatInputListener onChatInputListener = this.mOnChatInputListener;
        if (onChatInputListener != null) {
            onChatInputListener.onViewInitReady();
        }
        return inflate;
    }

    private void destroyDisposable() {
        CompositeDisposable compositeDisposable = this.mCompositeDisposable;
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public void setFocusable(boolean z) {
        this.mEdtMessage.setFocusable(z);
    }

    public void onDestroy() {
        destroyDisposable();
        this.mHandler.removeCallbacksAndMessages(null);
        AsyncSharedLinkTask asyncSharedLinkTask = this.mAsyncSharedLinkTask;
        if (asyncSharedLinkTask != null && !asyncSharedLinkTask.isCancelled()) {
            this.mAsyncSharedLinkTask.cancel(true);
            this.mAsyncSharedLinkTask = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onCommandAction(int i) {
        if (i == 1) {
            selectSlashBuddy();
        } else if (i == 2) {
            selectATBuddy();
        } else if (i == 3) {
            selectChannel();
        }
    }

    public void setPbxSessionId(@NonNull String str) {
        this.mSessionId = str;
        if (this.mInputMode != 1) {
            this.mInputMode = 1;
            updateUIForPBX();
        }
    }

    public void setPbxFromNumber(@Nullable String str) {
        this.mPbxFromNumber = str;
        if (this.mInputMode != 1) {
            this.mInputMode = 1;
            updateUIForPBX();
        }
    }

    public void setPbxToNumbers(@Nullable List<String> list) {
        this.mPbxToNumbers = list;
        if (this.mInputMode != 1) {
            this.mInputMode = 1;
        }
        updateBtnSend4Pbx();
    }

    private void updateBtnSend4Pbx() {
        ImageButton imageButton = this.mBtnSend;
        if (imageButton != null) {
            imageButton.setEnabled(this.mEdtMessage.length() != 0 && (!CollectionsUtil.isListEmpty(this.mPbxToNumbers) || !TextUtils.isEmpty(this.mSessionId)));
        }
    }

    public Button getBtnHoldToTalk() {
        return this.mBtnHoldToTalk;
    }

    public void onKeyboardOpen() {
        if (this.mEdtMessage.isShown() && !this.mEdtMessage.hasFocus()) {
            this.mEdtMessage.requestFocus();
        }
        this.mMode = 0;
        updateUIMode(this.mMode);
        ZMKeyboardDetector zMKeyboardDetector = this.mKeyboardDetector;
        if (zMKeyboardDetector != null) {
            this.mPanelEmojis.setKeyboardHeight(zMKeyboardDetector.getKeyboardHeight());
        }
    }

    public void onKeyboardClosed() {
        if (this.mMode != 3) {
            this.mLineBelowSend.setVisibility(4);
        } else if (this.mPanelEmojis.getMode() == 0) {
            this.mPanelSend.setVisibility(0);
            this.mPanelEmojis.setVisibility(0);
        } else {
            this.mPanelSend.setVisibility(8);
            this.mPanelEmojis.setmGiphyPreviewVisible(0);
            this.mPanelEmojis.setVisibility(0);
        }
    }

    private void checkE2EStatus() {
        if (!this.mIsMyNotes && !this.mIsAnnounceMent) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            boolean z = false;
            if (zoomMessenger == null) {
                this.mIsE2EChat = false;
                return;
            }
            int e2eGetMyOption = zoomMessenger.e2eGetMyOption();
            if (e2eGetMyOption == 2) {
                this.mIsE2EChat = true;
            } else if (this.mIsGroup) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mSessionId);
                if (groupById != null) {
                    this.mIsE2EChat = groupById.isForceE2EGroup();
                }
            } else {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                if (buddyWithJID != null) {
                    if (buddyWithJID.getE2EAbility(e2eGetMyOption) == 2) {
                        z = true;
                    }
                    this.mIsE2EChat = z;
                }
            }
        }
    }

    public void showKeyBoradMode() {
        updateUIMode(0);
    }

    public void openSoftKeyboard() {
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtMessage);
    }

    private void setSessionInfo(String str, boolean z, boolean z2) {
        this.mSessionId = str;
        this.mIsGroup = z;
        this.mIsMyNotes = z2;
        if (!z && !TextUtils.isEmpty(str)) {
            this.mIMAddrBookItem = ZMBuddySyncInstance.getInsatance().getBuddyByJid(str, true);
        }
        checkE2EStatus();
        updateE2EStatus();
        if (!this.mIsGroup) {
            this.mTxtOptVideoCall.setText(C4558R.string.zm_btn_video_call);
            View view = this.mBtnVideoCall;
            view.setContentDescription(view.getResources().getString(C4558R.string.zm_btn_video_call));
        } else {
            this.mTxtOptVideoCall.setText(C4558R.string.zm_mm_opt_video_call);
            View view2 = this.mBtnVideoCall;
            view2.setContentDescription(view2.getResources().getString(C4558R.string.zm_mm_opt_video_call));
        }
        updateVoiceCallBtnUI();
        if (this.mIsE2EChat || PTApp.getInstance().isFileTransferDisabled()) {
            this.mPanelEmojis.enableCustomSticker(false);
        }
        if (this.mIsAnnounceMent) {
            this.mEdtMessage.setHint(C4558R.string.zm_msg_announcements_hint_108966);
        } else {
            this.mEdtMessage.setHint(C4558R.string.zm_lbl_type_message_120867);
        }
        this.mEdtMessage.addTextChangedListener(this.mEditMsgWatcher);
        CommandEditText commandEditText = this.mEdtMessage;
        String str2 = this.mSessionId;
        MMMessageItem mMMessageItem = this.mThreadItem;
        commandEditText.enableStoreCommand(false, str2, mMMessageItem == null ? null : mMMessageItem.messageId);
        this.mEdtMessage.setOnCommandActionListener(this);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    public void setKeyboardDetector(ZMKeyboardDetector zMKeyboardDetector) {
        this.mKeyboardDetector = zMKeyboardDetector;
    }

    public void setOnChatInputListener(OnChatInputListener onChatInputListener) {
        this.mOnChatInputListener = onChatInputListener;
    }

    private void selectSlashBuddy() {
        if (((ZMActivity) getActivity()) != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (!this.mIsGroup) {
                    String str = this.mSessionId;
                    if (str != null) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                        if (buddyWithJID == null || buddyWithJID.isRobot()) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                if (!this.mIsMyNotes && !StringUtil.isEmptyOrNull(this.mSessionId) && getActivity() != null) {
                    this.mmSlashCommandPopupView = new MMSlashCommandPopupView(getActivity(), this.mPanelActions, this.mSessionId);
                    this.mmSlashCommandPopupView.setOnSlashCommandClickListener(new OnSlashCommandClickListener() {
                        public void onSlashCommandClick(@Nullable SlashItem slashItem) {
                            if (slashItem != null && slashItem.getCommand() != null && slashItem.getProfix() != null) {
                                if (slashItem.getType() == 1) {
                                    String trim = slashItem.getCommand().getCommand().trim();
                                    if (TextUtils.equals(trim, slashItem.getProfix().trim())) {
                                        trim = "";
                                    } else if (trim.startsWith(slashItem.getProfix())) {
                                        trim = trim.replace(slashItem.getProfix(), "").trim();
                                    }
                                    MMChatInputFragment.this.onSlashCommandSelect(slashItem.getProfix(), trim, slashItem.getJid());
                                    return;
                                }
                                MMChatInputFragment.this.onSlashCommandSelect(slashItem.getProfix(), slashItem.getCommand().getCommand().trim(), slashItem.getJid());
                            }
                        }
                    });
                    this.mmSlashCommandPopupView.show();
                    if (getView() != null) {
                        getView().performHapticFeedback(0);
                    }
                }
            }
        }
    }

    private void selectChannel() {
        if (!this.mIsGroup) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                if (buddyWithJID == null || buddyWithJID.isRobot()) {
                    return;
                }
            } else {
                return;
            }
        }
        if (((ZMActivity) getActivity()) != null) {
            MMSelectGroupFragment.showAsActivity(this, false, false, null, getString(C4558R.string.zm_mm_title_select_channel_113595), 114, null);
        }
    }

    private void selectATBuddy() {
        if (this.mIsGroup) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                String string = zMActivity.getString(C4558R.string.zm_mm_title_select_a_contact);
                String string2 = zMActivity.getString(C4558R.string.zm_btn_ok);
                SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
                selectContactsParamter.title = string;
                selectContactsParamter.btnOkText = string2;
                selectContactsParamter.isSingleChoice = true;
                selectContactsParamter.isAnimBottomTop = true;
                selectContactsParamter.groupId = this.mSessionId;
                selectContactsParamter.includeRobot = false;
                MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, 105, (Bundle) null);
            }
        }
    }

    private void jump2SelectTemplateContact(String str, String str2, String str3, IMessageTemplateSelect iMessageTemplateSelect) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && iMessageTemplateSelect != null) {
            String string = zMActivity.getString(C4558R.string.zm_mm_title_select_a_contact);
            String string2 = zMActivity.getString(C4558R.string.zm_btn_ok);
            SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
            selectContactsParamter.title = string;
            selectContactsParamter.btnOkText = string2;
            selectContactsParamter.isSingleChoice = true;
            selectContactsParamter.isAnimBottomTop = true;
            selectContactsParamter.groupId = this.mSessionId;
            selectContactsParamter.includeRobot = false;
            selectContactsParamter.isContainsAllInGroup = false;
            selectContactsParamter.includeMe = true;
            List<IMessageTemplateSelectItem> selectedItems = iMessageTemplateSelect.getSelectedItems();
            if (selectedItems != null && !selectedItems.isEmpty()) {
                ArrayList<String> arrayList = new ArrayList<>();
                for (IMessageTemplateSelectItem value : selectedItems) {
                    arrayList.add(value.getValue());
                }
                selectContactsParamter.preSelectedItems = arrayList;
            }
            Bundle bundle = new Bundle();
            bundle.putString("sessionId", str);
            bundle.putString(ARG_SHARED_MESSAGE_ID, str2);
            bundle.putString("eventid", str3);
            MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, 111, bundle);
        }
    }

    private void jump2SelectTemplateChannel(String str, String str2, String str3, IMessageTemplateSelect iMessageTemplateSelect) {
        ArrayList arrayList;
        if (((ZMActivity) getActivity()) != null && iMessageTemplateSelect != null) {
            List<IMessageTemplateSelectItem> selectedItems = iMessageTemplateSelect.getSelectedItems();
            if (selectedItems == null || selectedItems.isEmpty()) {
                arrayList = null;
            } else {
                ArrayList arrayList2 = new ArrayList();
                for (IMessageTemplateSelectItem value : selectedItems) {
                    arrayList2.add(value.getValue());
                }
                arrayList = arrayList2;
            }
            Bundle bundle = new Bundle();
            bundle.putString("sessionId", str);
            bundle.putString(ARG_SHARED_MESSAGE_ID, str2);
            bundle.putString("eventid", str3);
            MMSelectGroupFragment.showAsActivity(this, false, arrayList, getString(C4558R.string.zm_lbl_notification_add_exception_group_59554), 112, bundle);
        }
    }

    private void jump2SelectTemplateCustom(String str, String str2, String str3, IMessageTemplateSelect iMessageTemplateSelect) {
        if (((ZMActivity) getActivity()) != null && iMessageTemplateSelect != null) {
            List selectedItems = iMessageTemplateSelect.getSelectedItems();
            List<IMessageTemplateSelectItemGroup> groupItems = iMessageTemplateSelect.getGroupItems();
            ArrayList arrayList = new ArrayList();
            if (groupItems != null && !groupItems.isEmpty()) {
                for (IMessageTemplateSelectItemGroup iMessageTemplateSelectItemGroup : groupItems) {
                    if (iMessageTemplateSelectItemGroup != null) {
                        List items = iMessageTemplateSelectItemGroup.getItems();
                        if (items != null) {
                            arrayList.addAll(items);
                        }
                    }
                }
            }
            Bundle bundle = new Bundle();
            bundle.putString("sessionId", str);
            bundle.putString(ARG_SHARED_MESSAGE_ID, str2);
            bundle.putString("eventid", str3);
            MMSelectCustomFragment.showAsActivity(this, false, selectedItems, arrayList, iMessageTemplateSelect.getText() == null ? "" : iMessageTemplateSelect.getText(), 113, bundle);
        }
    }

    public void selectTemplateMessage(String str, String str2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str);
                if (messageByXMPPGuid != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                        if (zoomFileContentMgr != null) {
                            MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(messageByXMPPGuid, this.mSessionId, zoomMessenger, sessionById.isGroup(), StringUtil.isSameString(messageByXMPPGuid.getSenderID(), myself.getJid()), getActivity(), IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), zoomFileContentMgr);
                            if (initWithZoomMessage != null) {
                                IZoomMessageTemplate iZoomMessageTemplate = initWithZoomMessage.template;
                                if (iZoomMessageTemplate != null) {
                                    IMessageTemplateSelect findSelectItem = iZoomMessageTemplate.findSelectItem(str2);
                                    if (findSelectItem != null) {
                                        switch (findSelectItem.getSelectType()) {
                                            case 1:
                                                jump2SelectTemplateContact(this.mSessionId, str, str2, findSelectItem);
                                                break;
                                            case 2:
                                                jump2SelectTemplateChannel(this.mSessionId, str, str2, findSelectItem);
                                                break;
                                            case 3:
                                                jump2SelectTemplateCustom(this.mSessionId, str, str2, findSelectItem);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateUIForPBX() {
        if (this.mInputMode == 1) {
            ImageButton imageButton = this.mBtnSetModeVoice;
            if (imageButton != null) {
                imageButton.setVisibility(8);
                this.mPanelEmojis.hideGiphyAndSticker(true);
                disableFileTransfer();
                updateBtnSend4Pbx();
                this.mBtnMoreOpts.setVisibility(8);
                this.mBtnSend.setVisibility(0);
                this.mPanelSendText.setPadding(UIUtil.dip2px(getContext(), 16.0f), this.mPanelSendText.getPaddingTop(), this.mPanelSendText.getPaddingRight(), this.mPanelSendText.getPaddingBottom());
                this.mEdtMessage.addTextChangedListener(this.mEditMsgWatcher);
            }
        }
    }

    private void updateUIMode(int i) {
        updateUIMode(i, true);
    }

    private void updateUIMode(int i, boolean z) {
        this.mPanelSend.setVisibility(0);
        if (isRobot()) {
            this.mBtnSetModeKeyboard.setVisibility(8);
            this.mBtnHoldToTalk.setVisibility(8);
            this.mPanelMoreOpts.setVisibility(8);
            this.mPanelSendText.setVisibility(0);
            this.mBtnSetModeVoice.setVisibility(8);
            this.mBtnMoreOpts.setVisibility(8);
            this.mBtnEmoji.setVisibility(8);
            this.mBtnSend.setVisibility(0);
            return;
        }
        if (this.mIsAnnounceMent) {
            this.mBtnSetModeKeyboard.setVisibility(8);
            this.mBtnHoldToTalk.setVisibility(8);
            this.mPanelMoreOpts.setVisibility(0);
            this.mPanelSendText.setVisibility(0);
            this.mBtnSetModeVoice.setVisibility(0);
            this.mBtnMoreOpts.setVisibility(0);
            this.mBtnEmoji.setVisibility(0);
            this.mBtnSend.setVisibility(0);
        }
        switch (i) {
            case 0:
                if (this.mPanelEmojis.getMode() == 0 || this.mPanelEmojis.getMode() == 3) {
                    this.mPanelSend.setVisibility(0);
                    this.mPanelEmojis.setVisibility(8);
                    if (z) {
                        this.mEdtMessage.requestFocus();
                    }
                } else {
                    this.mPanelSend.setVisibility(8);
                    this.mPanelEmojis.setmGiphyPreviewVisible(8);
                }
                this.mBtnSetModeVoice.setVisibility((this.mEdtMessage.length() != 0 || this.mInputMode == 1) ? 8 : 0);
                this.mBtnSend.setVisibility((this.mEdtMessage.length() != 0 || this.mInputMode == 1) ? 0 : 8);
                this.mBtnSetModeKeyboard.setVisibility(8);
                this.mBtnHoldToTalk.setVisibility(8);
                this.mPanelMoreOpts.setVisibility(8);
                this.mPanelSendText.setVisibility(0);
                this.mBtnMoreOpts.setImageResource(C4558R.C4559drawable.zm_mm_more_btn);
                this.mBtnEmoji.setImageResource(C4558R.C4559drawable.zm_mm_emoji_btn);
                break;
            case 1:
                this.mBtnSetModeVoice.setVisibility(8);
                this.mBtnSetModeKeyboard.setVisibility(0);
                this.mBtnHoldToTalk.setVisibility(0);
                this.mPanelMoreOpts.setVisibility(8);
                this.mPanelSendText.setVisibility(8);
                this.mBtnMoreOpts.setImageResource(C4558R.C4559drawable.zm_mm_more_btn);
                this.mPanelEmojis.setVisibility(8);
                this.mBtnEmoji.setImageResource(C4558R.C4559drawable.zm_mm_emoji_btn);
                break;
            case 2:
                this.mBtnSetModeVoice.setVisibility((this.mEdtMessage.length() != 0 || this.mInputMode == 1) ? 8 : 0);
                this.mBtnSetModeKeyboard.setVisibility(8);
                this.mBtnHoldToTalk.setVisibility(8);
                this.mPanelMoreOpts.setVisibility(0);
                this.mPanelSendText.setVisibility(0);
                this.mBtnMoreOpts.setImageResource(C4558R.C4559drawable.zm_mm_less_btn);
                this.mPanelEmojis.setVisibility(8);
                this.mBtnEmoji.setImageResource(C4558R.C4559drawable.zm_mm_emoji_btn);
                break;
            case 3:
                UIUtil.closeSoftKeyboard(getActivity(), this.mEdtMessage);
                this.mLineBelowSend.setVisibility(0);
                this.mBtnSetModeVoice.setVisibility((this.mEdtMessage.length() != 0 || this.mInputMode == 1) ? 8 : 0);
                this.mBtnSetModeKeyboard.setVisibility(8);
                this.mBtnHoldToTalk.setVisibility(8);
                this.mPanelMoreOpts.setVisibility(8);
                this.mPanelSendText.setVisibility(0);
                this.mBtnMoreOpts.setImageResource(C4558R.C4559drawable.zm_mm_more_btn);
                ZMKeyboardDetector zMKeyboardDetector = this.mKeyboardDetector;
                if (zMKeyboardDetector != null && !zMKeyboardDetector.isKeyboardOpen()) {
                    if (this.mPanelEmojis.getMode() == 0) {
                        this.mPanelSend.setVisibility(0);
                        this.mPanelEmojis.setVisibility(0);
                    } else {
                        this.mPanelEmojis.setmGiphyPreviewVisible(0);
                        this.mPanelSend.setVisibility(8);
                        this.mLineBelowSend.setVisibility(8);
                        this.mPanelEmojis.setVisibility(0);
                    }
                }
                this.mBtnEmoji.setImageResource(C4558R.C4559drawable.zm_mm_setmode_keyboard_btn);
                break;
        }
    }

    private void updateVoiceCallBtnUI() {
        if (this.mIsGroup || this.mIMAddrBookItem == null || !CmmSIPCallManager.getInstance().isPBXActive() || CollectionsUtil.isCollectionEmpty(this.mIMAddrBookItem.getPhoneCallNumbersForPBX())) {
            this.mTxtOptVoiceCall.setText(C4558R.string.zm_btn_audio_call);
            View view = this.mBtnVoiceCall;
            view.setContentDescription(view.getResources().getString(C4558R.string.zm_btn_audio_call));
            this.mTxtOptVoiceCall.setContentDescription(this.mBtnVoiceCall.getResources().getString(C4558R.string.zm_btn_audio_call));
            return;
        }
        this.mTxtOptVoiceCall.setText(C4558R.string.zm_btn_audio_call_and_pbx_call);
        View view2 = this.mBtnVoiceCall;
        view2.setContentDescription(view2.getResources().getString(C4558R.string.zm_btn_audio_call_and_pbx_call));
        this.mTxtOptVoiceCall.setContentDescription(this.mBtnVoiceCall.getResources().getString(C4558R.string.zm_btn_audio_call_and_pbx_call));
    }

    public void onStart() {
        super.onStart();
        NotificationMgr.removeMessageNotificationMM(getActivity(), this.mSessionId);
        PrivateStickerUICallBack.getInstance().addListener(this.mStickerListener);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
    }

    public void onStop() {
        super.onStop();
        NotificationMgr.removeMessageNotificationMM(getActivity(), this.mSessionId);
        PrivateStickerUICallBack.getInstance().removeListener(this.mStickerListener);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onResume() {
        super.onResume();
        checkE2EStatus();
        StickerInputView stickerInputView = this.mPanelEmojis;
        if (stickerInputView != null) {
            stickerInputView.reloadAll();
        }
        updateInputLayout();
        updatePanelActions();
    }

    public void onPause() {
        updateDraft();
        super.onPause();
    }

    private boolean isAnnouncement() {
        if (!this.mIsGroup) {
            return false;
        }
        return ZMIMUtils.isAnnouncement(this.mSessionId);
    }

    private void updatePanelActions() {
        if (this.mThreadItem == null || !ZMIMUtils.isReplyDisabled()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    boolean z = true;
                    if (sessionById.isGroup()) {
                        ZoomGroup sessionGroup = sessionById.getSessionGroup();
                        if (sessionGroup == null) {
                            this.mPanelActions.setVisibility(8);
                        } else {
                            this.mPanelActions.setVisibility(0);
                            if (!sessionGroup.amIInGroup() || this.mIsAnnounceMent || this.mThreadItem != null) {
                                z = false;
                            }
                            enableCall(z);
                        }
                    } else {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                        boolean z2 = buddyWithJID != null ? buddyWithJID.getAccountStatus() == 0 : true;
                        if (zoomMessenger.blockUserIsBlocked(this.mSessionId) || !z2) {
                            this.mPanelActions.setVisibility(8);
                        } else {
                            this.mPanelActions.setVisibility(0);
                        }
                        if (this.mIsMyNotes || this.mIsAnnounceMent || this.mThreadItem != null) {
                            z = false;
                        }
                        enableCall(z);
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.mPanelActions.setVisibility(8);
    }

    private void enableCall(boolean z) {
        if (getContext() != null) {
            if (z) {
                if (((long) PTApp.getInstance().getCallStatus()) == 2) {
                    this.mTxtOptVideoCall.setText(C4558R.string.zm_mm_opt_invite_to_meeting_66217);
                } else {
                    this.mTxtOptVideoCall.setText(C4558R.string.zm_mm_opt_video_call);
                }
                this.mImgOptVideoCall.setImageResource(C4558R.C4559drawable.zm_mm_opt_panel_videocall_icon);
                this.mTxtOptVideoCall.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_gray_747487));
                this.mImgOptVoiceCall.setImageResource(C4558R.C4559drawable.zm_mm_opt_panel_voicecall_icon);
                this.mTxtOptVoiceCall.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_gray_747487));
                this.mBtnVoiceCall.setEnabled(true);
                this.mBtnVideoCall.setEnabled(true);
            } else {
                this.mImgOptVideoCall.setImageDrawable(TintUtil.tintColor(getContext(), C4558R.C4559drawable.zm_mm_opt_panel_videocall_icon, C4558R.color.zm_ui_kit_color_gray_BABACC));
                this.mTxtOptVideoCall.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_gray_BABACC));
                this.mImgOptVoiceCall.setImageDrawable(TintUtil.tintColor(getContext(), C4558R.C4559drawable.zm_mm_opt_panel_voicecall_icon, C4558R.color.zm_ui_kit_color_gray_BABACC));
                this.mTxtOptVoiceCall.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_gray_BABACC));
                this.mBtnVoiceCall.setEnabled(false);
                this.mBtnVideoCall.setEnabled(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateSendBtnPanels() {
        if (isRobot() || this.mInputMode == 1) {
            this.mBtnSetModeVoice.setVisibility(8);
            this.mBtnSend.setVisibility(0);
            updateBtnSend4Pbx();
            if (this.mTxtCharatersLeft == null) {
                return;
            }
            if (this.mEdtMessage.length() >= 480) {
                this.mTxtCharatersLeft.setVisibility(0);
                TextView textView = this.mTxtCharatersLeft;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(500 - this.mEdtMessage.length());
                textView.setText(sb.toString());
                return;
            }
            this.mTxtCharatersLeft.setVisibility(8);
        } else if (this.mEdtMessage.length() == 0) {
            this.mBtnSetModeVoice.setVisibility(0);
            this.mBtnSend.setVisibility(8);
        } else {
            this.mBtnSetModeVoice.setVisibility(8);
            this.mBtnSend.setVisibility(0);
            this.mBtnSend.setEnabled(true);
        }
    }

    private boolean isRobot() {
        if (!this.mIsGroup) {
            IMAddrBookItem iMAddrBookItem = this.mIMAddrBookItem;
            if (iMAddrBookItem != null && iMAddrBookItem.getIsRobot()) {
                return true;
            }
        }
        return false;
    }

    private void updateInputLayout() {
        boolean z = true;
        if (this.mIsAnnounceMent) {
            this.mBtnSetModeKeyboard.setVisibility(8);
            this.mBtnHoldToTalk.setVisibility(8);
            this.mPanelMoreOpts.setVisibility(0);
            this.mPanelSendText.setVisibility(0);
            this.mBtnSetModeVoice.setVisibility(0);
            this.mBtnMoreOpts.setVisibility(0);
            this.mBtnEmoji.setVisibility(0);
            this.mBtnSend.setVisibility(0);
            ImageButton imageButton = this.mBtnSend;
            if (this.mEdtMessage.length() == 0) {
                z = false;
            }
            imageButton.setEnabled(z);
            return;
        }
        IMAddrBookItem iMAddrBookItem = this.mIMAddrBookItem;
        if (iMAddrBookItem != null) {
            if (iMAddrBookItem.isZoomRoomContact()) {
                this.mBtnSetModeVoice.setVisibility(8);
                this.mPanelMoreOptsRow2.setVisibility(8);
                this.mPanelCamera.setVisibility(8);
                this.mEdtMessage.setEnabled(false);
                this.mEdtMessage.setClickable(false);
                this.mEdtMessage.setLongClickable(false);
                this.mPanelSendbtns.setVisibility(8);
                this.mEdtMessage.setText("");
                this.mEdtMessage.setHint(C4558R.string.zm_hint_cannot_chat_zoomroom);
            } else if (this.mIMAddrBookItem.getIsRobot()) {
                this.mBtnSetModeKeyboard.setVisibility(8);
                this.mBtnHoldToTalk.setVisibility(8);
                this.mPanelMoreOpts.setVisibility(8);
                this.mPanelSendText.setVisibility(0);
                this.mBtnSetModeVoice.setVisibility(8);
                this.mBtnMoreOpts.setVisibility(8);
                this.mBtnEmoji.setVisibility(8);
                this.mBtnSend.setVisibility(0);
                ImageButton imageButton2 = this.mBtnSend;
                if (this.mEdtMessage.length() == 0) {
                    z = false;
                }
                imageButton2.setEnabled(z);
                this.mPanelSendText.setPadding(UIUtil.dip2px(getActivity(), 5.0f), this.mPanelSendText.getPaddingTop(), this.mPanelSendText.getPaddingRight(), this.mPanelSendText.getPaddingBottom());
            }
        }
    }

    private void disableFileTransfer() {
        this.mPanelMoreOptsRow2.setVisibility(8);
        this.mPanelCamera.setVisibility(8);
    }

    private void enableFileTransfer() {
        this.mPanelMoreOptsRow2.setVisibility(0);
        this.mPanelCamera.setVisibility(0);
    }

    private void updateE2EStatus() {
        if (!isRobot()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (this.mIsGroup || !zoomMessenger.blockUserIsBlocked(this.mSessionId)) {
                    updateSendBtnPanels();
                    if (this.mIsE2EChat) {
                        this.mEdtMessage.setEnabled(true);
                        this.mEdtMessage.setClickable(true);
                        this.mEdtMessage.setLongClickable(true);
                        this.mPanelSendbtns.setVisibility(0);
                        CommandEditText commandEditText = this.mEdtMessage;
                        commandEditText.setPadding(commandEditText.getPaddingLeft(), this.mEdtMessage.getPaddingTop(), UIUtil.dip2px(getActivity(), 18.0f), this.mEdtMessage.getPaddingBottom());
                        this.mEdtMessage.setHint(C4558R.string.zm_hint_send_e2e_msg);
                        this.mImgE2EFlag.setVisibility(0);
                        this.mPanelEmojis.setGiphyVisiable(8);
                        updatePanelActions();
                    } else {
                        if (!this.mIsAnnounceMent) {
                            this.mEdtMessage.setHint(C4558R.string.zm_lbl_type_message_120867);
                        }
                        this.mImgE2EFlag.setVisibility(8);
                        this.mPanelEmojis.setGiphyVisiable(0);
                        CommandEditText commandEditText2 = this.mEdtMessage;
                        commandEditText2.setPadding(commandEditText2.getPaddingLeft(), this.mEdtMessage.getPaddingTop(), this.mEdtMessage.getPaddingLeft(), this.mEdtMessage.getPaddingBottom());
                        updatePanelActions();
                    }
                }
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("mMode", this.mMode);
        Uri uri = this.mImageUri;
        if (uri != null) {
            bundle.putString("mImageUri", uri.toString());
        }
    }

    private void updateDraft() {
        Editable editableText = this.mEdtMessage.getEditableText();
        boolean z = false;
        if (this.mThreadItem == null) {
            if (TextUtils.isEmpty(editableText)) {
                TextCommandHelper instance = TextCommandHelper.getInstance();
                if (this.mInputMode == 1) {
                    z = true;
                }
                instance.clearTextCommand(z, this.mSessionId);
            } else if (TextCommandHelper.getInstance().isSlashCommand(editableText)) {
                TextCommandHelper.getInstance().checkAndStoreSlashCommand(this.mSessionId, editableText);
            } else if (TextCommandHelper.getInstance().isAtCommand(editableText) || TextCommandHelper.getInstance().isChannelCommand(editableText)) {
                TextCommandHelper.getInstance().checkAndStoreNonSlashCommand(this.mSessionId, editableText);
            } else {
                TextCommandHelper instance2 = TextCommandHelper.getInstance();
                if (this.mInputMode == 1) {
                    z = true;
                }
                instance2.storeText(z, this.mSessionId, editableText.toString());
            }
        } else if (TextUtils.isEmpty(editableText)) {
            TextCommandHelper instance3 = TextCommandHelper.getInstance();
            if (this.mInputMode == 1) {
                z = true;
            }
            instance3.clearTextCommand(z, this.mSessionId, this.mThreadItem.messageId);
        } else if (TextCommandHelper.getInstance().isSlashCommand(editableText)) {
            TextCommandHelper.getInstance().checkAndStoreSlashCommand(this.mSessionId, this.mThreadItem.messageId, editableText);
        } else if (TextCommandHelper.getInstance().isAtCommand(editableText) || TextCommandHelper.getInstance().isChannelCommand(editableText)) {
            TextCommandHelper.getInstance().checkAndStoreNonSlashCommand(this.mSessionId, this.mThreadItem.messageId, editableText);
        } else {
            TextCommandHelper instance4 = TextCommandHelper.getInstance();
            if (this.mInputMode == 1) {
                z = true;
            }
            instance4.storeText(z, this.mSessionId, this.mThreadItem.messageId, editableText.toString());
        }
    }

    public boolean onBackPressed() {
        MMSlashCommandPopupView mMSlashCommandPopupView = this.mmSlashCommandPopupView;
        if (mMSlashCommandPopupView != null && mMSlashCommandPopupView.isShowing()) {
            this.mmSlashCommandPopupView.dismiss();
        }
        int i = this.mMode;
        int i2 = 0;
        if (i == 2) {
            if (this.mEdtMessage.getVisibility() != 0) {
                i2 = 1;
            }
            this.mMode = i2;
            updateUIMode(this.mMode);
            return true;
        } else if (i == 3) {
            if (this.mEdtMessage.getVisibility() != 0) {
                i2 = 1;
            }
            this.mMode = i2;
            this.mPanelEmojis.onBackPressed();
            updateUIMode(this.mMode);
            return true;
        } else {
            if (i == 0) {
                StickerInputView stickerInputView = this.mPanelEmojis;
                if (stickerInputView != null && (stickerInputView.getMode() == 1 || this.mPanelEmojis.getMode() == 2)) {
                    this.mPanelEmojis.onBackPressed();
                    updateUIMode(this.mMode);
                    return true;
                }
            }
            return false;
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnSetModeVoice) {
            onClickBtnSetModeVoice(view);
        } else if (id == C4558R.C4560id.btnSetModeKeyboard) {
            onClickBtnSetModeKeyboard(view);
        } else if (id == C4558R.C4560id.btnMoreOpts) {
            onClickBtnMoreOpts(view);
        } else if (id == C4558R.C4560id.btnSend) {
            onClickBtnSend(view);
        } else if (id == C4558R.C4560id.btnSendPicture) {
            onClickBtnSendPicture();
        } else if (id == C4558R.C4560id.btnCamera) {
            onClickBtnCamera();
        } else if (id == C4558R.C4560id.btnVoiceCall) {
            onClickBtnVoiceCall();
        } else if (id == C4558R.C4560id.btnVideoCall) {
            onClickBtnVideoCall();
        } else if (id == C4558R.C4560id.btnSendFile) {
            onClickBtnSendFile();
        } else if (id == C4558R.C4560id.btnEmoji) {
            onClickBtnEmoji();
        }
    }

    private void onClickBtnEmoji() {
        if (PTApp.getInstance().isWebSignedOn()) {
            if (this.mEdtMessage.isShown()) {
                this.mEdtMessage.requestFocus();
            }
            if (this.mMode == 3) {
                UIUtil.openSoftKeyboard(getActivity(), this.mEdtMessage);
            } else {
                this.mMode = 3;
                updateUIMode(this.mMode);
            }
        }
    }

    private void onClickBtnSendFile() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_all_file), 2));
        if (zMActivity != null && ZMDropbox.getInstance().isDropboxLoginSupported(zMActivity)) {
            arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_dropbox), 1));
        }
        arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_box), 4));
        if (zMActivity != null && GoogleDrive.canAuthGoogleViaBrowser(zMActivity)) {
            arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_google_drive), 5));
        }
        arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_one_drive), 3));
        if (OsUtil.isAtLeastJB_MR2()) {
            arrayList.add(new UploadContextMenuItem(getString(C4558R.string.zm_btn_share_one_drive_business_36279), 6));
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        ZMAlertDialog create = new Builder(getActivity()).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMChatInputFragment.this.onSelectContextMenuItem((UploadContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    private boolean checkSendNetWorkForReady(@NonNull ZMActivity zMActivity, int i) {
        if ((i != 4 && i != 1 && i != 5 && i != 3 && i != 6) || NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            return true;
        }
        SimpleMessageDialog.newInstance(C4558R.string.zm_alert_network_disconnected).show(zMActivity.getSupportFragmentManager(), SimpleMessageDialog.class.getName());
        return false;
    }

    private boolean isShareLinkEnabled(int i) {
        return !ZMIMUtils.isE2EChat(this.mSessionId) && isFileIntegrationEnabled(i);
    }

    private boolean isFileIntegrationEnabled(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        FileIntegrations listForFileIntegrationShare = zoomMessenger.getListForFileIntegrationShare();
        if (listForFileIntegrationShare == null || listForFileIntegrationShare.getDataCount() <= 0) {
            return false;
        }
        for (FileIntegrationData type : listForFileIntegrationShare.getDataList()) {
            if (type.getType() == i) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(UploadContextMenuItem uploadContextMenuItem) {
        if (getActivity() != null && checkSendNetWorkForReady((ZMActivity) getActivity(), uploadContextMenuItem.getAction())) {
            String str = "";
            boolean z = true;
            int i = 1016;
            switch (uploadContextMenuItem.getAction()) {
                case 1:
                    boolean isShareLinkEnabled = isShareLinkEnabled(1);
                    if (!isShareLinkEnabled) {
                        i = 1010;
                    }
                    ZMFileListActivity.startFileListActivity((Fragment) this, DropboxFileListAdapter.class, i, (String[]) null, (String) null, C4558R.string.zm_btn_send, getString(C4558R.string.zm_mm_msg_send_file_prompt), isShareLinkEnabled);
                    str = ZoomLogEventTracking.ACTION_DROPBOX;
                    break;
                case 2:
                    if (OsUtil.isAtLeastQ()) {
                        openSystemSAF();
                    } else {
                        ZMFileListActivity.startFileListActivity((Fragment) this, ZMLocalFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_send, getString(C4558R.string.zm_mm_msg_send_file_prompt));
                    }
                    str = ZoomLogEventTracking.ACTION_NATIVE_FILES;
                    break;
                case 3:
                case 6:
                    boolean isShareLinkEnabled2 = isShareLinkEnabled(2);
                    if (uploadContextMenuItem.getAction() != 6) {
                        z = false;
                    }
                    if (OneDrivePicker.hasPicker(getActivity(), z)) {
                        this.mPicker = OneDrivePicker.createPicker(z ? 1015 : 1014, null, z);
                        IPicker iPicker = this.mPicker;
                        if (iPicker != null) {
                            iPicker.startPicking((ZMDialogFragment) this);
                        } else {
                            ZMFileListActivity.startFileListActivity((Fragment) this, z ? OneDriveBusinessFileListAdapter.class : OneDriveFileListAdapter.class, isShareLinkEnabled2 ? 1016 : 1010, (String[]) null, (String) null, C4558R.string.zm_btn_send, getString(C4558R.string.zm_mm_msg_send_file_prompt), isShareLinkEnabled2);
                        }
                    } else {
                        ZMFileListActivity.startFileListActivity((Fragment) this, z ? OneDriveBusinessFileListAdapter.class : OneDriveFileListAdapter.class, isShareLinkEnabled2 ? 1016 : 1010, (String[]) null, (String) null, C4558R.string.zm_btn_send, getString(C4558R.string.zm_mm_msg_send_file_prompt), isShareLinkEnabled2);
                    }
                    if (uploadContextMenuItem.getAction() != 3) {
                        str = ZoomLogEventTracking.ACTION_ONE_DRIVE_BUSINESS;
                        break;
                    } else {
                        str = ZoomLogEventTracking.ACTION_ONE_DRIVE;
                        break;
                    }
                case 4:
                    ZMFileListActivity.startFileListActivity((Fragment) this, BoxFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_send, getString(C4558R.string.zm_mm_msg_send_file_prompt));
                    str = ZoomLogEventTracking.ACTION_BOX;
                    break;
                case 5:
                    ZMFileListActivity.startFileListActivity((Fragment) this, GoogleDriveFileListAdapter.class, 1010, (String[]) null, (String) null, C4558R.string.zm_btn_send, getString(C4558R.string.zm_mm_msg_send_file_prompt));
                    str = ZoomLogEventTracking.ACTION_GOOGLE_DRIVE;
                    break;
            }
            if (!StringUtil.isEmptyOrNull(str)) {
                ZoomLogEventTracking.eventTrackFileUpload(str, this.mIsGroup);
            }
        }
    }

    private void onClickBtnSetModeVoice(View view) {
        if (PTApp.getInstance().isWebSignedOn()) {
            UIUtil.closeSoftKeyboard(getActivity(), view);
            this.mMode = 1;
            updateUIMode(this.mMode);
        }
    }

    private void onClickBtnSetModeKeyboard(View view) {
        this.mMode = 0;
        updateUIMode(this.mMode);
        this.mEdtMessage.requestFocus();
    }

    private void onClickBtnMoreOpts(View view) {
        if (PTApp.getInstance().isWebSignedOn()) {
            UIUtil.closeSoftKeyboard(getActivity(), view);
            if (this.mMode != 2) {
                this.mMode = 2;
                updateUIMode(this.mMode);
            } else {
                this.mMode = this.mEdtMessage.getVisibility() == 0 ? 0 : 1;
                updateUIMode(this.mMode);
            }
            this.mEdtMessage.clearFocus();
            this.mBtnSendPicture.requestFocus();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClickBtnSend(android.view.View r10) {
        /*
            r9 = this;
            com.zipow.videobox.ptapp.PTApp r10 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r10 = r10.isWebSignedOn()
            if (r10 != 0) goto L_0x000b
            return
        L_0x000b:
            com.zipow.videobox.view.mm.MMSlashCommandPopupView r10 = r9.mmSlashCommandPopupView
            if (r10 == 0) goto L_0x001a
            boolean r10 = r10.isShowing()
            if (r10 == 0) goto L_0x001a
            com.zipow.videobox.view.mm.MMSlashCommandPopupView r10 = r9.mmSlashCommandPopupView
            r10.dismiss()
        L_0x001a:
            boolean r10 = r9.mIsGroup
            r0 = 1
            r1 = 0
            if (r10 == 0) goto L_0x007d
            com.zipow.videobox.view.CommandEditText r10 = r9.mEdtMessage
            r2 = 2
            java.util.List r10 = r10.getTextCommand(r2)
            boolean r2 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r10)
            if (r2 != 0) goto L_0x007d
            java.util.Iterator r10 = r10.iterator()
        L_0x0031:
            boolean r2 = r10.hasNext()
            if (r2 == 0) goto L_0x007d
            java.lang.Object r2 = r10.next()
            com.zipow.videobox.util.TextCommandHelper$SpanBean r2 = (com.zipow.videobox.util.TextCommandHelper.SpanBean) r2
            java.lang.String r3 = r2.getJid()
            java.lang.String r4 = "jid_select_everyone"
            boolean r3 = android.text.TextUtils.equals(r3, r4)
            if (r3 != 0) goto L_0x0059
            java.lang.String r2 = r2.getJid()
            java.lang.String r3 = r9.mSessionId
            java.lang.String r3 = p021us.zoom.androidlib.util.UIUtil.generateAtallSessionId(r3)
            boolean r2 = android.text.TextUtils.equals(r2, r3)
            if (r2 == 0) goto L_0x0031
        L_0x0059:
            com.zipow.videobox.ptapp.PTApp r10 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r10 = r10.getZoomMessenger()
            if (r10 == 0) goto L_0x007d
            java.lang.String r2 = r9.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomGroup r10 = r10.getGroupById(r2)
            if (r10 == 0) goto L_0x007a
            int r2 = r10.getBuddyCount()
            r3 = 1000(0x3e8, float:1.401E-42)
            if (r2 < r3) goto L_0x007a
            int r10 = r10.getBuddyCount()
            r2 = r10
            r10 = 1
            goto L_0x007f
        L_0x007a:
            r10 = 0
            r2 = 0
            goto L_0x007f
        L_0x007d:
            r10 = 0
            r2 = 0
        L_0x007f:
            if (r10 == 0) goto L_0x00a9
            androidx.fragment.app.FragmentActivity r10 = r9.getActivity()
            r3 = r10
            us.zoom.androidlib.app.ZMActivity r3 = (p021us.zoom.androidlib.app.ZMActivity) r3
            int r10 = p021us.zoom.videomeetings.C4558R.string.zm_mm_atall_notify_title_113595
            java.lang.String r4 = r9.getString(r10)
            int r10 = p021us.zoom.videomeetings.C4558R.string.zm_mm_atall_notify_message_113595
            java.lang.Object[] r0 = new java.lang.Object[r0]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0[r1] = r2
            java.lang.String r5 = r9.getString(r10, r0)
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_atall_notify_button_113595
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_btn_cancel
            com.zipow.videobox.fragment.MMChatInputFragment$8 r8 = new com.zipow.videobox.fragment.MMChatInputFragment$8
            r8.<init>()
            com.zipow.videobox.util.DialogUtils.showAlertDialog(r3, r4, r5, r6, r7, r8)
            goto L_0x00ae
        L_0x00a9:
            com.zipow.videobox.view.CommandEditText r10 = r9.mEdtMessage
            r9.sendText(r10)
        L_0x00ae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMChatInputFragment.onClickBtnSend(android.view.View):void");
    }

    /* access modifiers changed from: private */
    public boolean sendText(CommandEditText commandEditText) {
        if (commandEditText == null) {
            return false;
        }
        if (this.mInputMode == 1) {
            return sendPbxSms(commandEditText.getText().toString());
        }
        SendMsgType judgeSlashCommand = commandEditText.judgeSlashCommand(this.mSessionId, !this.mActionCopyMsg);
        String str = "";
        List textCommand = commandEditText.getTextCommand(1);
        if (!textCommand.isEmpty()) {
            str = ((SpanBean) textCommand.get(0)).getJid();
        }
        if (!sendText(commandEditText.getText().toString(), str, judgeSlashCommand)) {
            return false;
        }
        commandEditText.setText("");
        return true;
    }

    private boolean sendPbxSms(String str) {
        IPBXMessageSession iPBXMessageSession;
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        if (TextUtils.isEmpty(this.mSessionId) && !ZMPhoneUtils.isValidPhoneNumbers(this.mPbxToNumbers)) {
            FragmentActivity activity = getActivity();
            if (activity instanceof ZMActivity) {
                DialogUtils.showAlertDialog((ZMActivity) activity, C4558R.string.zm_sip_invalid_recipient_117773, C4558R.string.zm_sip_invalid_recipient_msg_117773, C4558R.string.zm_btn_ok);
            }
            return false;
        } else if (!NetworkUtil.hasDataNetwork(getContext())) {
            return false;
        } else {
            IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
            if (messageAPI == null) {
                return false;
            }
            if (this.mSessionId == null) {
                iPBXMessageSession = null;
            } else {
                iPBXMessageSession = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionId);
            }
            if (iPBXMessageSession == null) {
                String str2 = this.mSessionId;
                if (str2 != null && messageAPI.getMessageCountInLocalSession(str2) > 0) {
                    IPBXMessage messageByIndexInLocalSession = messageAPI.getMessageByIndexInLocalSession(this.mSessionId, 0);
                    if (messageByIndexInLocalSession != null) {
                        List<PBXMessageContact> toContacts = messageByIndexInLocalSession.getToContacts();
                        if (!CollectionsUtil.isCollectionEmpty(toContacts)) {
                            ArrayList arrayList = new ArrayList();
                            for (PBXMessageContact phoneNumber : toContacts) {
                                String phoneNumber2 = phoneNumber.getPhoneNumber();
                                if (!TextUtils.isEmpty(phoneNumber2)) {
                                    arrayList.add(phoneNumber2);
                                }
                            }
                            if (arrayList.size() > 0) {
                                if (StringUtil.isEmptyOrNull(this.mPbxFromNumber)) {
                                    OnChatInputListener onChatInputListener = this.mOnChatInputListener;
                                    if (onChatInputListener != null) {
                                        onChatInputListener.onPbxSmsSent(this.mSessionId, null);
                                    }
                                    return false;
                                }
                                String sendMessage = CmmSIPMessageManager.getInstance().sendMessage(this.mSessionId, str, this.mPbxFromNumber, arrayList);
                                if (StringUtil.isEmptyOrNull(sendMessage)) {
                                    return false;
                                }
                                OnChatInputListener onChatInputListener2 = this.mOnChatInputListener;
                                if (onChatInputListener2 != null) {
                                    onChatInputListener2.onPbxSmsSent(this.mSessionId, sendMessage);
                                }
                                this.mEdtMessage.setText("");
                                return true;
                            }
                        }
                    }
                } else if (!StringUtil.isEmptyOrNull(this.mPbxFromNumber) && !CollectionsUtil.isCollectionEmpty(this.mPbxToNumbers)) {
                    if (StringUtil.isEmptyOrNull(this.mSessionId)) {
                        this.mSessionId = messageAPI.generateLocalSid(this.mPbxFromNumber, this.mPbxToNumbers);
                        if (StringUtil.isEmptyOrNull(this.mSessionId)) {
                            return false;
                        }
                    }
                    String sendMessage2 = CmmSIPMessageManager.getInstance().sendMessage(this.mSessionId, str, this.mPbxFromNumber, this.mPbxToNumbers);
                    if (StringUtil.isEmptyOrNull(sendMessage2)) {
                        return false;
                    }
                    OnChatInputListener onChatInputListener3 = this.mOnChatInputListener;
                    if (onChatInputListener3 != null) {
                        onChatInputListener3.onPbxSmsSent(this.mSessionId, sendMessage2);
                    }
                    this.mEdtMessage.setText("");
                    return true;
                }
                return false;
            } else if (StringUtil.isEmptyOrNull(this.mPbxFromNumber)) {
                OnChatInputListener onChatInputListener4 = this.mOnChatInputListener;
                if (onChatInputListener4 != null) {
                    onChatInputListener4.onPbxSmsSent(this.mSessionId, null);
                }
                return false;
            } else {
                String requestSendMessage = iPBXMessageSession.requestSendMessage(str);
                if (StringUtil.isEmptyOrNull(requestSendMessage)) {
                    return false;
                }
                OnChatInputListener onChatInputListener5 = this.mOnChatInputListener;
                if (onChatInputListener5 != null) {
                    onChatInputListener5.onPbxSmsSent(this.mSessionId, requestSendMessage);
                }
                this.mEdtMessage.setText("");
                return true;
            }
        }
    }

    public boolean sendText(String str, String str2, SendMsgType sendMsgType) {
        boolean z;
        if (StringUtil.isEmptyOrSpace(str)) {
            return false;
        }
        int i = this.mInputMode == 1 ? 500 : 4096;
        if (str.length() > i) {
            str = str.substring(0, i);
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
        if (sessionById == null || !NetworkUtil.hasDataNetwork(getActivity()) || ZoomMessengerUI.getInstance().getConnectionStatus() == 0) {
            return false;
        }
        String str3 = "";
        switch (sendMsgType) {
            case SLASH_COMMAND:
                sessionById.sendAddonCommand(str, str2);
                if (!sessionById.isGroup()) {
                    ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                    if (sessionBuddy != null && sessionBuddy.isRobot()) {
                        String[] split = str.split(OAuth.SCOPE_DELIMITER);
                        if (split.length > 1) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (int i2 = 1; i2 < split.length; i2++) {
                                stringBuffer.append(split[i2]);
                                if (i2 != split.length - 1) {
                                    stringBuffer.append(OAuth.SCOPE_DELIMITER);
                                }
                            }
                            str = stringBuffer.toString();
                            break;
                        }
                    } else {
                        RobotCommand.Builder newBuilder = RobotCommand.newBuilder();
                        newBuilder.setCommand(str);
                        newBuilder.setJid(str2);
                        newBuilder.setShortDescription("");
                        zoomMessenger.setLastUsedRobotCommand(newBuilder.build());
                        return true;
                    }
                } else {
                    RobotCommand.Builder newBuilder2 = RobotCommand.newBuilder();
                    newBuilder2.setCommand(str);
                    newBuilder2.setJid(str2);
                    newBuilder2.setShortDescription("");
                    zoomMessenger.setLastUsedRobotCommand(newBuilder2.build());
                    return true;
                }
                break;
            case MESSAGE:
                break;
            case GIPHY:
                String trim = str.replace("/giphy", "").trim();
                if (!TextUtils.isEmpty(trim)) {
                    zoomMessenger.getGiphyInfoByStr(trim, this.mSessionId, 1);
                }
                return true;
            default:
                if (TextUtils.isEmpty(str3)) {
                    Toast.makeText(getActivity(), C4558R.string.zm_hint_msg_send_failed, 1).show();
                    return false;
                } else if (sessionById.getMessageById(str3) == null) {
                    return false;
                } else {
                    OnChatInputListener onChatInputListener = this.mOnChatInputListener;
                    if (onChatInputListener != null) {
                        onChatInputListener.onMessageSent(this.mSessionId, str3);
                    }
                    return true;
                }
        }
        ArrayList arrayList = new ArrayList();
        ArrayList<SpanBean> arrayList2 = new ArrayList<>();
        int i3 = 2;
        arrayList2.addAll(this.mEdtMessage.getTextCommand(2));
        arrayList2.addAll(this.mEdtMessage.getTextCommand(3));
        if (!arrayList2.isEmpty()) {
            z = false;
            for (SpanBean spanBean : arrayList2) {
                if ((spanBean.getType() != 3 || !TextUtils.equals(spanBean.getJid(), this.mSessionId)) && StringUtil.isSameString(this.mEdtMessage.getText().subSequence(spanBean.getStart(), spanBean.getEnd()).toString(), spanBean.getLabel()) && spanBean.getEnd() < 4096) {
                    AtInfoItem.Builder newBuilder3 = AtInfoItem.newBuilder();
                    newBuilder3.setJid(spanBean.getJid());
                    newBuilder3.setPositionStart(spanBean.getStart());
                    newBuilder3.setPositionEnd(spanBean.getEnd() - 2);
                    if (spanBean.getType() == 2) {
                        newBuilder3.setType(1);
                    } else if (spanBean.getType() == 3) {
                        newBuilder3.setType(3);
                    } else {
                        newBuilder3.setType(0);
                    }
                    if ((StringUtil.isSameString(spanBean.getJid(), MMSelectContactsFragment.JID_SELECTED_EVERYONE) || TextUtils.equals(spanBean.getJid(), UIUtil.generateAtallSessionId(this.mSessionId))) && spanBean.getType() == 2) {
                        newBuilder3.setType(2);
                        newBuilder3.setJid(UIUtil.generateAtallSessionId(this.mSessionId));
                        z = true;
                    }
                    arrayList.add(newBuilder3.build());
                }
            }
        } else {
            z = false;
        }
        MessageInput.Builder newBuilder4 = MessageInput.newBuilder();
        newBuilder4.setMsgType(0);
        if (this.mThreadItem == null) {
            i3 = 1;
        }
        newBuilder4.setMsgSubType(i3);
        newBuilder4.setIsE2EMessage(this.mIsE2EChat);
        newBuilder4.setSessionID(this.mSessionId);
        newBuilder4.setBody(str);
        newBuilder4.setE2EMessageFakeBody(getString(C4558R.string.zm_msg_e2e_fake_message));
        newBuilder4.setIsAtAllGroupMembers(z);
        newBuilder4.setIsMyNote(this.mIsMyNotes);
        FontStyte buildFromCharSequence = FontStyleHelper.buildFromCharSequence(this.mEdtMessage.getText());
        if (buildFromCharSequence != null) {
            newBuilder4.setFontStyte(buildFromCharSequence);
        }
        if (!CollectionsUtil.isListEmpty(arrayList)) {
            AtInfoList.Builder newBuilder5 = AtInfoList.newBuilder();
            newBuilder5.addAllAtInfoItem(arrayList);
            newBuilder4.setAtInfoList(newBuilder5.build());
        }
        if (this.mThreadItem != null) {
            CommentInfo.Builder newBuilder6 = CommentInfo.newBuilder();
            newBuilder6.setThrId(this.mThreadItem.messageId);
            newBuilder6.setThrTime(this.mThreadItem.serverSideTime);
            newBuilder6.setThrOwnerJid(this.mThreadItem.fromJid);
            newBuilder4.setCommentInfo(newBuilder6);
        }
        String sendMessage = zoomMessenger.sendMessage(newBuilder4.build());
        if (TextUtils.isEmpty(sendMessage)) {
            return false;
        }
        OnChatInputListener onChatInputListener2 = this.mOnChatInputListener;
        if (onChatInputListener2 != null) {
            MMMessageItem mMMessageItem = this.mThreadItem;
            if (mMMessageItem != null) {
                onChatInputListener2.onCommentSent(this.mSessionId, mMMessageItem.messageId, sendMessage);
            } else {
                onChatInputListener2.onMessageSent(str3, sendMessage);
            }
        }
        return true;
    }

    public void sendFile(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            MessageInput.Builder newBuilder = MessageInput.newBuilder();
            newBuilder.setMsgType(10);
            newBuilder.setMsgSubType(this.mThreadItem == null ? 1 : 2);
            newBuilder.setIsE2EMessage(this.mIsE2EChat);
            newBuilder.setSessionID(this.mSessionId);
            newBuilder.setE2EMessageFakeBody(getString(C4558R.string.zm_msg_e2e_fake_message));
            newBuilder.setLocalFilePath(str);
            if (this.mThreadItem != null) {
                CommentInfo.Builder newBuilder2 = CommentInfo.newBuilder();
                newBuilder2.setThrId(this.mThreadItem.messageId);
                newBuilder2.setThrTime(this.mThreadItem.serverSideTime);
                newBuilder2.setThrOwnerJid(this.mThreadItem.fromJid);
                newBuilder.setCommentInfo(newBuilder2);
            }
            newBuilder.setIsMyNote(this.mIsMyNotes);
            String sendMessage = zoomMessenger.sendMessage(newBuilder.build());
            if (!StringUtil.isEmptyOrNull(sendMessage)) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(sendMessage);
                    if (messageById != null) {
                        OnChatInputListener onChatInputListener = this.mOnChatInputListener;
                        if (onChatInputListener != null) {
                            onChatInputListener.onMessageSent(this.mSessionId, messageById.getMessageID());
                        }
                    }
                }
            }
        }
    }

    private void onClickBtnSendPicture() {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            choosePhoto();
        } else {
            zm_requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, REQUEST_CHOOSE_PHOTO);
        }
    }

    private boolean checkCameraAndExternalSoragePermission() {
        return VERSION.SDK_INT < 23 || (checkSelfPermission("android.permission.CAMERA") == 0 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0);
    }

    private void onClickBtnCamera() {
        if (checkCameraAndExternalSoragePermission()) {
            takePhoto();
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (checkSelfPermission("android.permission.CAMERA") != 0) {
            arrayList.add("android.permission.CAMERA");
        }
        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        zm_requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 106);
    }

    public void choosePhoto() {
        PhotoPicker.builder().setPhotoCount(9).setShowCamera(false).setShowGif(true).setPreviewEnabled(true).start((Fragment) this, 100);
    }

    public void takePhoto() {
        Uri uri;
        if (getActivity() != null) {
            String newFilePathForTakingPhoto = ImageUtil.getNewFilePathForTakingPhoto();
            if (OsUtil.isAtLeastQ()) {
                uri = ImageUtil.createImageUri();
                this.mImageUri = uri;
            } else if (OsUtil.isAtLeastN()) {
                Uri uriForFile = FileProvider.getUriForFile(getActivity(), getResources().getString(C4558R.string.zm_app_provider), new File(newFilePathForTakingPhoto));
                StringBuilder sb = new StringBuilder();
                sb.append("file://");
                sb.append(newFilePathForTakingPhoto);
                this.mImageUri = Uri.parse(sb.toString());
                uri = uriForFile;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("file://");
                sb2.append(newFilePathForTakingPhoto);
                uri = Uri.parse(sb2.toString());
                this.mImageUri = uri;
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            if (OsUtil.isAtLeastN()) {
                intent.addFlags(3);
            }
            intent.putExtra("output", uri);
            try {
                startActivityForResult(intent, 101);
            } catch (Exception unused) {
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        String str;
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            if (stringArrayListExtra != null && !stringArrayListExtra.isEmpty()) {
                onResultChoosePhoto(stringArrayListExtra);
                String str2 = "";
                for (String str3 : stringArrayListExtra) {
                    if (str3.startsWith("content:")) {
                        str = AndroidAppUtil.getFileExtendName(FileUtils.getPathFromUri(VideoBoxApplication.getInstance(), Uri.parse(str3)));
                    } else {
                        str = AndroidAppUtil.getFileExtendName(str3);
                    }
                    if (!StringUtil.isEmptyOrNull(str)) {
                        String replaceAll = str.replaceAll("[.]", "");
                        if (!StringUtil.isEmptyOrNull(str2)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str2);
                            sb.append(PreferencesConstants.COOKIE_DELIMITER);
                            str2 = sb.toString();
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(replaceAll);
                        str2 = sb2.toString();
                    }
                }
                if (!StringUtil.isEmptyOrNull(str2)) {
                    ZoomLogEventTracking.eventTrackSendImage(str2, this.mIsGroup);
                }
            }
        } else {
            boolean z = true;
            if (i == 101 && i2 == -1) {
                Uri uri = this.mImageUri;
                if (uri != null) {
                    if (!StringUtil.isEmptyOrNull(uri.getPath())) {
                        AndroidAppUtil.addImageToGallery(getActivity(), new File(this.mImageUri.getPath()));
                    }
                    onSelectedPhoto(this.mImageUri, true);
                }
            } else if (i == 103 && i2 == -1) {
                if (intent != null) {
                    String stringExtra = intent.getStringExtra(MMImageSendConfirmFragment.RESULT_IMAGE_PATH);
                    if (!StringUtil.isEmptyOrNull(stringExtra)) {
                        sendImage(stringExtra);
                    }
                }
            } else if (i == 104 && i2 == -1 && intent != null) {
                sendFile(intent.getStringExtra(ZMFileListActivity.SELECTED_FILE_PATH));
            } else if (i == 105 && i2 == -1 && intent != null) {
                ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
                if (arrayList != null && arrayList.size() == 1) {
                    onATBuddySelect((IMAddrBookItem) arrayList.get(0));
                }
            } else if (i == 114 && i2 == -1 && intent != null) {
                ArrayList stringArrayListExtra2 = intent.getStringArrayListExtra(MMSelectGroupFragment.RESULT_SELECT_GROUPS);
                if (!CollectionsUtil.isListEmpty(stringArrayListExtra2)) {
                    onChannelSelect((String) stringArrayListExtra2.get(0));
                }
            } else if (i == 110 && i2 == -1 && intent != null) {
                ArrayList arrayList2 = (ArrayList) intent.getSerializableExtra("selectedItems");
                if (arrayList2 != null && arrayList2.size() == 1) {
                    onSlashCommandSelect((IMAddrBookItem) arrayList2.get(0));
                }
            } else if (i == 1010) {
                if (i2 == -1) {
                    if (intent != null) {
                        Uri data = intent.getData();
                        if (data == null || !OsUtil.isAtLeastQ()) {
                            Bundle extras = intent.getExtras();
                            if (extras != null) {
                                String string = extras.getString(ZMFileListActivity.SELECTED_FILE_PATH);
                                String string2 = extras.getString(ZMFileListActivity.SELECTED_FILE_NAME);
                                if (!StringUtil.isEmptyOrNull(string) && !StringUtil.isEmptyOrNull(string2)) {
                                    uploadFile(string, string2);
                                }
                            }
                        } else {
                            uploadFileByUri(data);
                        }
                    }
                } else if (i2 == 0 && intent != null) {
                    Bundle extras2 = intent.getExtras();
                    if (extras2 != null) {
                        String string3 = extras2.getString(ZMFileListActivity.FAILED_PROMT);
                        if (StringUtil.isEmptyOrNull(string3)) {
                            string3 = getString(C4558R.string.zm_alert_auth_token_failed_msg);
                        }
                        ShareAlertDialog.showDialog(getFragmentManager(), string3, false);
                    }
                }
            } else if (i == 1016) {
                if (i2 == -1) {
                    if (intent != null) {
                        Bundle extras3 = intent.getExtras();
                        if (extras3 != null) {
                            String string4 = extras3.getString(ZMFileListActivity.SHARED_FILE_ID);
                            String string5 = extras3.getString(ZMFileListActivity.SHARED_FILE_LINK);
                            String string6 = extras3.getString(ZMFileListActivity.SELECTED_FILE_NAME);
                            long j = extras3.getLong(ZMFileListActivity.SHARED_FILE_SIZE, 0);
                            int i3 = extras3.getInt(ZMFileListActivity.SHARED_FILE_TYPE, 0);
                            if (!StringUtil.isEmptyOrNull(string5) && !StringUtil.isEmptyOrNull(string6)) {
                                prepareToSendSharedLink(string4, i3, string6, string5, j);
                            }
                        }
                    }
                } else if (i2 == 0 && intent != null) {
                    Bundle extras4 = intent.getExtras();
                    if (extras4 != null) {
                        String string7 = extras4.getString(ZMFileListActivity.FAILED_PROMT);
                        if (StringUtil.isEmptyOrNull(string7)) {
                            string7 = getString(C4558R.string.zm_alert_auth_token_failed_msg);
                        }
                        ShareAlertDialog.showDialog(getFragmentManager(), string7, false);
                    }
                }
            } else if ((i == 1014 || i == 1015) && intent != null) {
                if (i2 != 0) {
                    if (i2 != -1) {
                        ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_msg_load_file_fail_without_name), false);
                        return;
                    }
                    if (this.mPicker == null) {
                        if (getActivity() != null) {
                            if (i != 1015) {
                                z = false;
                            }
                            this.mPicker = OneDrivePicker.createPicker(i, null, z);
                        } else {
                            return;
                        }
                    }
                    IPickerResult pickerResult = this.mPicker.getPickerResult(i, i2, intent);
                    if (pickerResult == null) {
                        ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_msg_load_file_fail_without_name), false);
                    } else if (!pickerResult.acceptFileType()) {
                        ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_alert_unsupported_format), false);
                    } else {
                        Uri link = pickerResult.getLink();
                        if (pickerResult.isLocal()) {
                            uploadFile(link.getPath());
                        } else {
                            asyncDownloadFile(link, pickerResult.getSize(), FileUtils.makeNewFilePathWithName(getSessionDataPath(), pickerResult.getName()));
                        }
                    }
                }
            } else if (i == 112 && i2 == -1 && intent != null) {
                ArrayList<String> stringArrayListExtra3 = intent.getStringArrayListExtra(MMSelectGroupFragment.RESULT_SELECT_GROUPS);
                if (stringArrayListExtra3 != null && !stringArrayListExtra3.isEmpty()) {
                    ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
                    if (zoomMessageTemplate != null) {
                        String stringExtra2 = intent.getStringExtra("sessionId");
                        String stringExtra3 = intent.getStringExtra(ARG_SHARED_MESSAGE_ID);
                        String stringExtra4 = intent.getStringExtra("eventid");
                        ArrayList arrayList3 = new ArrayList();
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        for (String str4 : stringArrayListExtra3) {
                            IMessageTemplateSelectItem iMessageTemplateSelectItem = new IMessageTemplateSelectItem();
                            if (zoomMessenger != null) {
                                ZoomGroup groupById = zoomMessenger.getGroupById(str4);
                                if (groupById != null) {
                                    String groupName = groupById.getGroupName();
                                    if (!TextUtils.isEmpty(groupName)) {
                                        iMessageTemplateSelectItem.setText(groupName);
                                    }
                                }
                            }
                            iMessageTemplateSelectItem.setValue(str4);
                            arrayList3.add(iMessageTemplateSelectItem);
                        }
                        boolean sendSelectCommand = zoomMessageTemplate.sendSelectCommand(stringExtra2, stringExtra3, stringExtra4, arrayList3);
                        EventBus eventBus = EventBus.getDefault();
                        ZMTemplateSelectProcessingEvent zMTemplateSelectProcessingEvent = new ZMTemplateSelectProcessingEvent(sendSelectCommand, stringExtra2, stringExtra3, stringExtra4, arrayList3);
                        eventBus.post(zMTemplateSelectProcessingEvent);
                    }
                }
            } else if (i == 111 && i2 == -1 && intent != null) {
                ArrayList arrayList4 = (ArrayList) intent.getSerializableExtra("selectedItems");
                if (arrayList4 != null && !arrayList4.isEmpty()) {
                    ZoomMessageTemplate zoomMessageTemplate2 = PTApp.getInstance().getZoomMessageTemplate();
                    if (zoomMessageTemplate2 != null) {
                        String stringExtra5 = intent.getStringExtra("sessionId");
                        String stringExtra6 = intent.getStringExtra(ARG_SHARED_MESSAGE_ID);
                        String stringExtra7 = intent.getStringExtra("eventid");
                        ArrayList arrayList5 = new ArrayList();
                        Iterator it = arrayList4.iterator();
                        while (it.hasNext()) {
                            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) it.next();
                            IMessageTemplateSelectItem iMessageTemplateSelectItem2 = new IMessageTemplateSelectItem();
                            iMessageTemplateSelectItem2.setText(iMAddrBookItem.getScreenName());
                            iMessageTemplateSelectItem2.setValue(iMAddrBookItem.getJid());
                            arrayList5.add(iMessageTemplateSelectItem2);
                        }
                        boolean sendSelectCommand2 = zoomMessageTemplate2.sendSelectCommand(stringExtra5, stringExtra6, stringExtra7, arrayList5);
                        EventBus eventBus2 = EventBus.getDefault();
                        ZMTemplateSelectProcessingEvent zMTemplateSelectProcessingEvent2 = new ZMTemplateSelectProcessingEvent(sendSelectCommand2, stringExtra5, stringExtra6, stringExtra7, arrayList5);
                        eventBus2.post(zMTemplateSelectProcessingEvent2);
                    }
                }
            } else if (i == 113 && i2 == -1 && intent != null) {
                ArrayList<String> stringArrayListExtra4 = intent.getStringArrayListExtra("selectItems");
                if (stringArrayListExtra4 != null && !stringArrayListExtra4.isEmpty()) {
                    ZoomMessageTemplate zoomMessageTemplate3 = PTApp.getInstance().getZoomMessageTemplate();
                    if (zoomMessageTemplate3 != null) {
                        String stringExtra8 = intent.getStringExtra("sessionId");
                        String stringExtra9 = intent.getStringExtra(ARG_SHARED_MESSAGE_ID);
                        String stringExtra10 = intent.getStringExtra("eventid");
                        ArrayList arrayList6 = new ArrayList();
                        for (String parse : stringArrayListExtra4) {
                            try {
                                JsonElement parse2 = new JsonParser().parse(parse);
                                if (parse2.isJsonObject()) {
                                    arrayList6.add(IMessageTemplateSelectItem.parse(parse2.getAsJsonObject()));
                                }
                            } catch (Exception unused) {
                            }
                        }
                        boolean sendSelectCommand3 = zoomMessageTemplate3.sendSelectCommand(stringExtra8, stringExtra9, stringExtra10, arrayList6);
                        EventBus eventBus3 = EventBus.getDefault();
                        ZMTemplateSelectProcessingEvent zMTemplateSelectProcessingEvent3 = new ZMTemplateSelectProcessingEvent(sendSelectCommand3, stringExtra8, stringExtra9, stringExtra10, arrayList6);
                        eventBus3.post(zMTemplateSelectProcessingEvent3);
                    }
                }
            }
        }
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
                    if (MMChatInputFragment.this.mTaskDownloadFile != null && !MMChatInputFragment.this.mTaskDownloadFile.isCancelled()) {
                        MMChatInputFragment.this.mTaskDownloadFile.cancel(true);
                    }
                    MMChatInputFragment.this.mTaskDownloadFile = null;
                    MMChatInputFragment.this.mDownloadFileWaitingDialog = null;
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
    public String getSessionDataPath() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        String sessionDataFolder = zoomMessenger.getSessionDataFolder(this.mSessionId);
        if (!StringUtil.isEmptyOrNull(sessionDataFolder)) {
            File file = new File(sessionDataFolder);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return sessionDataFolder;
    }

    public void sendImageByUri(@NonNull final Uri uri) {
        this.mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<String>() {
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                String guessContentTypeFromUri = FileUtils.guessContentTypeFromUri(VideoBoxApplication.getNonNullInstance(), uri);
                if (StringUtil.isEmptyOrNull(guessContentTypeFromUri) || !AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(guessContentTypeFromUri)) {
                    String createTempFile = AppUtil.createTempFile("pic", MMChatInputFragment.this.getSessionDataPath(), "jpg");
                    String createTempFile2 = AppUtil.createTempFile("pic", MMChatInputFragment.this.getSessionDataPath(), "jpg");
                    if (!FileUtils.copyFileFromUri(VideoBoxApplication.getNonNullInstance(), uri, createTempFile)) {
                        observableEmitter.onNext("");
                    } else if (ImageUtil.compressImage(createTempFile, createTempFile2, 1048576)) {
                        observableEmitter.onNext(createTempFile2);
                    } else {
                        observableEmitter.onNext("");
                    }
                } else {
                    String createTempFile3 = AppUtil.createTempFile("pic", MMChatInputFragment.this.getSessionDataPath(), "gif");
                    if (FileUtils.copyFileFromUri(VideoBoxApplication.getNonNullInstance(), uri, createTempFile3)) {
                        observableEmitter.onNext(createTempFile3);
                    } else {
                        observableEmitter.onNext("");
                    }
                }
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.m266io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<? super T>) new Consumer<String>() {
            public void accept(String str) throws Exception {
                if (!StringUtil.isEmptyOrNull(str) && !ZMActivity.isActivityDestroyed(MMChatInputFragment.this.getActivity())) {
                    MMChatInputFragment.this.sendImage(str);
                }
            }
        }));
    }

    public void sendImageByPath(@NonNull final String str) {
        this.mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<String>() {
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                if (AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(str))) {
                    String createTempFile = AppUtil.createTempFile("pic", MMChatInputFragment.this.getSessionDataPath(), "gif");
                    if (FileUtils.copyFile(str, createTempFile)) {
                        observableEmitter.onNext(createTempFile);
                    } else {
                        observableEmitter.onNext("");
                    }
                } else {
                    String createTempFile2 = AppUtil.createTempFile("pic", MMChatInputFragment.this.getSessionDataPath(), "jpg");
                    if (ImageUtil.compressImage(str, createTempFile2, 1048576)) {
                        observableEmitter.onNext(createTempFile2);
                    } else {
                        observableEmitter.onNext("");
                    }
                }
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.m266io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<? super T>) new Consumer<String>() {
            public void accept(String str) throws Exception {
                if (!StringUtil.isEmptyOrNull(str) && !ZMActivity.isActivityDestroyed(MMChatInputFragment.this.getActivity())) {
                    MMChatInputFragment.this.sendImage(str);
                }
            }
        }));
    }

    public void onResultChoosePhoto(List<String> list) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String str = (String) list.get(i);
                if (str.startsWith("content:")) {
                    sendImageByUri(Uri.parse(str));
                } else {
                    sendImageByPath(str);
                }
            }
        }
    }

    public void uploadFile(String str) {
        File file = new File(str);
        if (file.exists() && file.isFile()) {
            uploadFile(str, file.getName());
        }
    }

    private boolean checkFileSize(long j) {
        if (j <= 536870912) {
            return false;
        }
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_file_too_large).show(getFragmentManager(), SimpleMessageDialog.class.getName());
        return true;
    }

    private void showFileFormatNotSupportDialog() {
        SimpleMessageDialog.newInstance(getString(C4558R.string.zm_msg_file_format_not_support_msg_110716), getString(C4558R.string.zm_msg_file_format_not_support_title_110716)).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    private void uploadFile(String str, String str2) {
        String str3;
        String str4;
        File file;
        File file2 = new File(str);
        if (!StringUtil.isEmptyOrNull(str2) && file2.exists() && file2.isFile()) {
            if (!PTApp.getInstance().isFileTypeAllowSendInChat(AndroidAppUtil.getFileExtendName(str2) != null ? AndroidAppUtil.getFileExtendName(str2) : "")) {
                showFileFormatNotSupportDialog();
            } else if (!checkFileSize(file2.length())) {
                if (!StringUtil.isSameString(str2, file2.getName())) {
                    File file3 = new File(file2.getParentFile(), str2);
                    if (file3.exists()) {
                        File parentFile = file3.getParentFile();
                        String name = file3.getName();
                        int lastIndexOf = name.lastIndexOf(".");
                        if (lastIndexOf >= 0) {
                            str3 = name.substring(0, lastIndexOf);
                            str4 = name.substring(lastIndexOf);
                        } else {
                            str4 = "";
                            str3 = name;
                        }
                        int i = 2;
                        while (true) {
                            file = new File(parentFile, String.format("%s(%d)%s", new Object[]{str3, Integer.valueOf(i), str4}));
                            if (!file.exists()) {
                                break;
                            }
                            i++;
                        }
                        file3 = file;
                    }
                    file2.renameTo(file3);
                    str = file3.getAbsolutePath();
                }
                sendFile(str);
            }
        }
    }

    private String getFileIntegrationName(int i) {
        return i == 1 ? getString(C4558R.string.zm_btn_share_dropbox) : "";
    }

    private void prepareToSendSharedLink(String str, int i, String str2, String str3, long j) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(AppUtil.getDataPath());
                sb.append("/");
                sb.append(myself.getJid());
                sb.append("/fileintegration/");
                sb.append(UUID.randomUUID().toString());
                String sb2 = sb.toString();
                String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(myself, null);
                this.mAsyncSharedLinkTask = new AsyncSharedLinkTask(FileIntegrationInfo.newBuilder().setId(str).setFileName(str2).setType(i).setPreviewUrl(str3).setFileSize(j).build(), sb2, getString(C4558R.string.zm_msg_share_file_unsupported_68764, buddyDisplayName, getFileIntegrationName(i), getString(C4558R.string.zm_app_name)));
                try {
                    this.mAsyncSharedLinkTask.execute((Params[]) new String[0]);
                } catch (RejectedExecutionException e) {
                    ZMLog.m281e(TAG, e, "AsyncSharedLinkTask execute rejected!", new Object[0]);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendSharedLink(FileIntegrationInfo fileIntegrationInfo, String str, String str2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            MessageInput.Builder newBuilder = MessageInput.newBuilder();
            newBuilder.setBody(str2);
            newBuilder.setMsgType(15);
            newBuilder.setMsgSubType(this.mThreadItem == null ? 1 : 2);
            newBuilder.setIsE2EMessage(this.mIsE2EChat);
            newBuilder.setSessionID(this.mSessionId);
            newBuilder.setE2EMessageFakeBody(getString(C4558R.string.zm_msg_e2e_fake_message));
            newBuilder.setLocalFilePath(str);
            newBuilder.setFileIntegration(fileIntegrationInfo);
            if (this.mThreadItem != null) {
                CommentInfo.Builder newBuilder2 = CommentInfo.newBuilder();
                newBuilder2.setThrId(this.mThreadItem.messageId);
                newBuilder2.setThrTime(this.mThreadItem.serverSideTime);
                newBuilder2.setThrOwnerJid(this.mThreadItem.fromJid);
                newBuilder.setCommentInfo(newBuilder2);
            }
            newBuilder.setIsMyNote(this.mIsMyNotes);
            String sendMessage = zoomMessenger.sendMessage(newBuilder.build());
            if (!StringUtil.isEmptyOrNull(sendMessage)) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null && sessionById.getMessageById(sendMessage) != null) {
                    OnChatInputListener onChatInputListener = this.mOnChatInputListener;
                    if (onChatInputListener != null) {
                        onChatInputListener.onMessageSent(this.mSessionId, sendMessage);
                    }
                }
            }
        }
    }

    public void onChannelSelect(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(str);
                if (groupById != null) {
                    String groupDisplayName = groupById.getGroupDisplayName(getContext());
                    StringBuilder sb = new StringBuilder();
                    sb.append(TextCommandHelper.CHANNEL_CMD_CHAR);
                    sb.append(groupDisplayName);
                    sb.append(OAuth.SCOPE_DELIMITER);
                    String sb2 = sb.toString();
                    int selectionStart = this.mEdtMessage.getSelectionStart();
                    if (selectionStart > 0) {
                        int i = selectionStart - 1;
                        if (this.mEdtMessage.getEditableText().charAt(i) == '#') {
                            this.mEdtMessage.getEditableText().delete(i, selectionStart);
                            selectionStart = i;
                        }
                    }
                    this.mEdtMessage.addTextCommand(3, sb2, str, selectionStart);
                    if (this.mMode != 0) {
                        this.mMode = 0;
                        updateUIMode(this.mMode);
                        this.mEdtMessage.requestFocus();
                        UIUtil.openSoftKeyboard(getActivity(), this.mEdtMessage);
                    }
                }
            }
        }
    }

    public void onATBuddySelect(IMAddrBookItem iMAddrBookItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null && iMAddrBookItem != null && !TextUtils.isEmpty(myself.getJid()) && !TextUtils.isEmpty(iMAddrBookItem.getJid()) && !TextUtils.equals(myself.getJid(), iMAddrBookItem.getJid())) {
                StringBuilder sb = new StringBuilder();
                sb.append(TextCommandHelper.REPLY_AT_CHAR);
                sb.append(iMAddrBookItem.getScreenName());
                sb.append(OAuth.SCOPE_DELIMITER);
                String sb2 = sb.toString();
                int selectionStart = this.mEdtMessage.getSelectionStart();
                if (selectionStart > 0) {
                    int i = selectionStart - 1;
                    if (this.mEdtMessage.getEditableText().charAt(i) == '@') {
                        this.mEdtMessage.getEditableText().delete(i, selectionStart);
                        selectionStart = i;
                    }
                }
                this.mEdtMessage.addTextCommand(2, sb2, iMAddrBookItem.getJid(), selectionStart);
                if (this.mMode != 0) {
                    this.mMode = 0;
                    updateUIMode(this.mMode);
                    this.mEdtMessage.requestFocus();
                    UIUtil.openSoftKeyboard(getActivity(), this.mEdtMessage);
                }
            }
        }
    }

    private void onSlashCommandSelect(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null && iMAddrBookItem.getIsRobot() && !TextUtils.isEmpty(iMAddrBookItem.getRobotCmdPrefix()) && this.mEdtMessage != null) {
            onSlashCommandSelect(iMAddrBookItem.getRobotCmdPrefix(), "", iMAddrBookItem.getJid());
        }
    }

    /* access modifiers changed from: private */
    public void onSlashCommandSelect(@NonNull String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str)) {
            this.mEdtMessage.addTextCommand(1, str, str2, str3, 0);
            if (this.mMode != 0) {
                this.mMode = 0;
                updateUIMode(this.mMode);
                this.mEdtMessage.requestFocus();
                UIUtil.openSoftKeyboard(getActivity(), this.mEdtMessage);
            }
        }
    }

    public void onClickBtnVoiceCall() {
        if (this.mIsGroup || this.mIMAddrBookItem == null || !CmmSIPCallManager.getInstance().isPBXActive() || CollectionsUtil.isCollectionEmpty(this.mIMAddrBookItem.getPhoneCallNumbersForPBX())) {
            if (PTApp.getInstance().getCallStatus() == 0) {
                startConference(this.mIsGroup ? 6 : 0);
            } else {
                alertAlreadyInMeeting();
            }
            ZoomLogEventTracking.eventTrackVoiceCall(this.mIsGroup);
            return;
        }
        PhoneLabelFragment.show(getFragmentManager(), this.mIMAddrBookItem);
    }

    public void onClickBtnVideoCall() {
        int callStatus = PTApp.getInstance().getCallStatus();
        if (callStatus == 0) {
            startConference(this.mIsGroup ? 3 : 1);
        } else if (callStatus == 2) {
            inviteToMeetingConference();
        } else {
            alertAlreadyInMeeting();
        }
        ZoomLogEventTracking.eventTrackVideoCall(this.mIsGroup);
    }

    private void alertAlreadyInMeeting() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_cannot_start_call_while_in_another_meeting, 1).show();
        }
    }

    private void startConference(int i) {
        checkStartConference(i);
    }

    private void checkStartConference(final int i) {
        if (getActivity() != null) {
            MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(getActivity(), new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    MMChatInputFragment.this.onStartConference(i);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onStartConference(final int i) {
        if (this.mIsGroup) {
            new Builder(getActivity()).setTitle(C4558R.string.zm_title_start_group_call).setMessage(C4558R.string.zm_msg_confirm_group_call).setPositiveButton(C4558R.string.zm_btn_yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMChatInputFragment.this.callABContact(i);
                }
            }).setNegativeButton(C4558R.string.zm_btn_no, (DialogInterface.OnClickListener) null).show();
        } else {
            callABContact(i);
        }
    }

    /* access modifiers changed from: private */
    public void callABContact(int i) {
        int i2;
        if (getArguments() != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (!this.mIsGroup) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                        if (buddyWithJID != null) {
                            String jid = buddyWithJID.getJid();
                            if (!StringUtil.isEmptyOrNull(jid)) {
                                i2 = ConfActivity.inviteToVideoCall(activity, jid, i);
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        ZoomGroup groupById = zoomMessenger.getGroupById(this.mSessionId);
                        if (groupById != null) {
                            i2 = ConfActivity.startGroupCall(activity, groupById.getGroupID(), i);
                        } else {
                            return;
                        }
                    }
                    if (i2 != 0) {
                        StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), i2);
                    }
                }
            }
        }
    }

    private void inviteToMeetingConference() {
        MeetingInfoProto activeMeetingItem = PTApp.getInstance().getActiveMeetingItem();
        if (activeMeetingItem != null) {
            final String id = activeMeetingItem.getId();
            final long meetingNumber = activeMeetingItem.getMeetingNumber();
            if (this.mIsGroup) {
                new Builder(getActivity()).setTitle(C4558R.string.zm_title_start_group_call).setMessage(C4558R.string.zm_msg_confirm_invite_group_meeting_66217).setPositiveButton(C4558R.string.zm_btn_yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MMChatInputFragment.this.inviteBuddiesToConf(id, meetingNumber);
                    }
                }).setNegativeButton(C4558R.string.zm_btn_no, (DialogInterface.OnClickListener) null).show();
            } else {
                inviteBuddiesToConf(id, meetingNumber);
            }
        }
    }

    /* access modifiers changed from: private */
    public void inviteBuddiesToConf(String str, long j) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList = new ArrayList();
            if (!this.mIsGroup) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                if (buddyWithJID != null) {
                    String jid = buddyWithJID.getJid();
                    if (!StringUtil.isEmptyOrNull(jid)) {
                        arrayList.add(jid);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                arrayList.add(this.mSessionId);
            } else {
                return;
            }
            String[] strArr = new String[arrayList.size()];
            arrayList.toArray(strArr);
            if (PTAppDelegation.getInstance().inviteBuddiesToConf(strArr, null, str, j, getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null && zMActivity.isActive()) {
                    new InviteFailedDialog().show(getFragmentManager(), InviteFailedDialog.class.getName());
                }
            } else {
                showTipInvitationsSent(strArr.length);
            }
        }
    }

    private void showTipInvitationsSent(int i) {
        OnChatInputListener onChatInputListener = this.mOnChatInputListener;
        if (onChatInputListener != null) {
            onChatInputListener.onShowInvitationsSent(i);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C267417 r2 = new EventAction("MMChatFragmentPermissionResult") {
            public void run(IUIElement iUIElement) {
                ((MMChatInputFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("MMChatFragmentPermissionResult", r2);
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 106 && checkCameraAndExternalSoragePermission()) {
                takePhoto();
            } else if (i == 7001) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
                    choosePhoto();
                }
            } else if (i == 11 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
                String str = this.mSelectedPhoneNumber;
                if (str != null) {
                    callSip(str);
                }
                this.mSelectedPhoneNumber = null;
            }
        }
    }

    public void onSelectedPhoto(Uri uri, boolean z) {
        ZoomLogEventTracking.eventTrackCapturePhoto(this.mIsGroup);
        MMImageSendConfirmFragment.showAsActivity(this, uri.toString(), getSessionDataPath(), z, 103);
    }

    public void sendImage(String str) {
        if (getContext() != null && !StringUtil.isEmptyOrNull(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                String imageMimeType = ImageUtil.getImageMimeType(str);
                MessageInput.Builder newBuilder = MessageInput.newBuilder();
                newBuilder.setMsgSubType(this.mThreadItem == null ? 1 : 2);
                newBuilder.setIsE2EMessage(this.mIsE2EChat);
                newBuilder.setSessionID(this.mSessionId);
                newBuilder.setLocalFilePath(str);
                newBuilder.setE2EMessageFakeBody(getString(C4558R.string.zm_msg_e2e_fake_message));
                if (this.mThreadItem != null) {
                    CommentInfo.Builder newBuilder2 = CommentInfo.newBuilder();
                    newBuilder2.setThrId(this.mThreadItem.messageId);
                    newBuilder2.setThrTime(this.mThreadItem.serverSideTime);
                    newBuilder2.setThrOwnerJid(this.mThreadItem.fromJid);
                    newBuilder.setCommentInfo(newBuilder2);
                }
                newBuilder.setIsMyNote(this.mIsMyNotes);
                if (AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(imageMimeType)) {
                    newBuilder.setMsgType(6);
                } else if (AndroidAppUtil.IMAGE_MIME_TYPE_PNG.equals(imageMimeType)) {
                    newBuilder.setMsgType(5);
                } else {
                    newBuilder.setMsgType(1);
                }
                String sendMessage = zoomMessenger.sendMessage(newBuilder.build());
                if (!StringUtil.isEmptyOrNull(sendMessage)) {
                    OnChatInputListener onChatInputListener = this.mOnChatInputListener;
                    if (onChatInputListener != null) {
                        onChatInputListener.onMessageSent(this.mSessionId, sendMessage);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onConnectReturn(int i) {
        if (isResumed()) {
            updateE2EStatus();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        if ((this.mIsGroup || StringUtil.isSameString(str, this.mSessionId)) && isResumed()) {
            updateE2EStatus();
        }
    }

    /* access modifiers changed from: private */
    public void E2E_MyStateUpdate(int i) {
        updateE2EStatus();
    }

    /* access modifiers changed from: private */
    public void E2E_SessionStateUpdate(String str, String str2, int i, int i2) {
        updateE2EStatus();
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersUpdated() {
        updatePanelActions();
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersAdded(List<String> list) {
        if (list != null && !StringUtil.isEmptyOrNull(this.mSessionId) && list.contains(this.mSessionId)) {
            updatePanelActions();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersRemoved(List<String> list) {
        if (list != null && !StringUtil.isEmptyOrNull(this.mSessionId) && list.contains(this.mSessionId)) {
            updatePanelActions();
        }
    }

    /* access modifiers changed from: private */
    public void NotifyInfoBarriesMsg(String str, String str2) {
        if (getActivity() != null && this.mEdtMessage != null) {
            InformationBarriesDialog.show((Context) getActivity(), str2);
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtMessage);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BuddyPresenceChanged(String str) {
        if (TextUtils.equals(str, this.mSessionId)) {
            boolean z = this.mIsE2EChat;
            checkE2EStatus();
            if (z != this.mIsE2EChat) {
                updateE2EStatus();
                if (this.mIsE2EChat || PTApp.getInstance().isFileTransferDisabled()) {
                    this.mPanelEmojis.enableCustomSticker(false);
                } else {
                    this.mPanelEmojis.enableCustomSticker(true);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_GetGIFFromGiphyResultIml(int i, String str, List<String> list, String str2, String str3) {
        this.mPanelEmojis.Indicate_GetGIFFromGiphyResultIml(i, str, list, str2, str3);
    }

    /* access modifiers changed from: private */
    public void Indicate_GetHotGiphyInfoResult(int i, String str, List<String> list, String str2, String str3) {
        if (i == 0) {
            this.mPanelEmojis.Indicate_GetHotGiphyInfoResult(i, str, list, str2, str3);
        }
    }

    public void handleActionMsg(String str, String str2) {
        ActionType parseType = ActionType.parseType(str2);
        if (parseType != null) {
            Map parseActionMsgParams = ZMActionMsgUtil.parseActionMsgParams(str2);
            switch (parseType) {
                case SENDHTTPMSG:
                    if (!this.mIsSendingHttpMsg && parseActionMsgParams != null) {
                        this.mIsSendingHttpMsg = true;
                        new Timer().schedule(new TimerTask() {
                            public void run() {
                                MMChatInputFragment.this.mIsSendingHttpMsg = false;
                            }
                        }, 1000);
                        ZMActionMsgUtil.sendHttpMsg(parseActionMsgParams);
                        break;
                    } else {
                        return;
                    }
                case SENDMSG:
                    if (parseActionMsgParams != null && parseActionMsgParams.containsKey("message")) {
                        String str3 = (String) parseActionMsgParams.get("type");
                        if (!TextUtils.isEmpty(str3) && !"2".equals(str3)) {
                            if (!TextUtils.isEmpty(str3) && "1".equals(str3)) {
                                sendText((String) parseActionMsgParams.get("message"), str, SendMsgType.MESSAGE);
                                break;
                            }
                        } else {
                            sendText((String) parseActionMsgParams.get("message"), str, SendMsgType.SLASH_COMMAND);
                            break;
                        }
                    }
                    break;
                case COPYMSG:
                    if (parseActionMsgParams != null && parseActionMsgParams.containsKey("type")) {
                        String str4 = (String) parseActionMsgParams.get("type");
                        if (!TextUtils.isEmpty(str4) && !"2".equals(str4)) {
                            if (!TextUtils.isEmpty(str4) && "1".equals(str4)) {
                                this.mActionCopyMsg = true;
                                this.mEdtMessage.setText((CharSequence) parseActionMsgParams.get("message"));
                                CommandEditText commandEditText = this.mEdtMessage;
                                commandEditText.setSelection(commandEditText.getText().length());
                                break;
                            }
                        } else {
                            String str5 = (String) parseActionMsgParams.get("message");
                            if (!TextUtils.isEmpty(str5)) {
                                String[] split = str5.split(OAuth.SCOPE_DELIMITER);
                                if (split.length > 0) {
                                    this.mEdtMessage.setText(str5);
                                    this.mEdtMessage.addTextCommand(1, split[0], split.length > 1 ? split[1] : "", str, 0);
                                    CommandEditText commandEditText2 = this.mEdtMessage;
                                    commandEditText2.setSelection(commandEditText2.getText().length());
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnNewStickerUploaded(String str, int i, String str2) {
        StickerInputView stickerInputView = this.mPanelEmojis;
        if (stickerInputView != null) {
            stickerInputView.OnNewStickerUploaded(str, i, str2);
        }
    }

    /* access modifiers changed from: private */
    public void OnMakePrivateSticker(int i, String str, String str2) {
        StickerInputView stickerInputView = this.mPanelEmojis;
        if (stickerInputView != null) {
            stickerInputView.OnMakePrivateSticker(i, str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void OnDiscardPrivateSticker(int i, String str) {
        StickerInputView stickerInputView = this.mPanelEmojis;
        if (stickerInputView != null) {
            stickerInputView.OnDiscardPrivateSticker(i, str);
        }
    }

    /* access modifiers changed from: private */
    public void OnPrivateStickersUpdated() {
        StickerInputView stickerInputView = this.mPanelEmojis;
        if (stickerInputView != null) {
            stickerInputView.OnPrivateStickersUpdated();
        }
    }

    /* access modifiers changed from: private */
    public void OnStickerDownloaded(String str, int i) {
        StickerInputView stickerInputView = this.mPanelEmojis;
        if (stickerInputView != null) {
            stickerInputView.onStickerDownloaded(str, i);
        }
    }

    public void onSearch(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (TextUtils.isEmpty(str)) {
                List checkIfNeedUpdateHotGiphyInfo = zoomMessenger.checkIfNeedUpdateHotGiphyInfo();
                if (checkIfNeedUpdateHotGiphyInfo == null || checkIfNeedUpdateHotGiphyInfo.isEmpty()) {
                    zoomMessenger.getHotGiphyInfo(this.mSessionId, 8);
                } else {
                    this.mPanelEmojis.Indicate_GetHotGiphyInfoResult(0, "", checkIfNeedUpdateHotGiphyInfo, "", this.mSessionId);
                }
            } else {
                zoomMessenger.getGiphyInfoByStr(str, this.mSessionId, 8);
            }
            this.mPanelEmojis.setmGiphyPreviewVisible(0);
        }
    }

    public void onGiphyPreviewItemClick(GiphyPreviewItem giphyPreviewItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomMessenger.getMyself());
            int i = C4558R.string.zm_mm_giphy_unsupport;
            Object[] objArr = new Object[1];
            objArr[0] = fromZoomBuddy == null ? "" : fromZoomBuddy.getScreenName();
            String string = getString(i, objArr);
            String[] strArr = new String[1];
            MessageInput.Builder newBuilder = MessageInput.newBuilder();
            if (!TextUtils.isEmpty(string)) {
                newBuilder.setBody(string);
            }
            newBuilder.setMsgType(12);
            newBuilder.setSessionID(this.mSessionId);
            if (this.mThreadItem != null) {
                newBuilder.setMsgSubType(2);
                CommentInfo.Builder newBuilder2 = CommentInfo.newBuilder();
                newBuilder2.setThrId(this.mThreadItem.messageId);
                newBuilder2.setThrTime(this.mThreadItem.serverSideTime);
                newBuilder2.setThrOwnerJid(this.mThreadItem.fromJid);
                newBuilder.setCommentInfo(newBuilder2);
            } else {
                newBuilder.setMsgSubType(1);
            }
            newBuilder.setGiphyID(giphyPreviewItem.getInfo().getId());
            zoomMessenger.sendMessageForGiphy(newBuilder.build(), strArr);
        }
    }

    public void onGiphyPreviewBack() {
        this.mMode = this.mEdtMessage.getVisibility() == 0 ? 0 : 1;
        updateUIMode(this.mMode);
    }

    public void onStickerSelect(View view) {
        int id = view.getId();
        this.mMode = 3;
        if (id == C4558R.C4560id.panelEmojiType) {
            updateUIMode(this.mMode);
        } else if (id == C4558R.C4560id.panelStickerType) {
            updateUIMode(this.mMode);
        } else if (id == C4558R.C4560id.panelGiphyType) {
            updateUIMode(this.mMode);
            if (!this.mPanelEmojis.hasGiphyData()) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    List checkIfNeedUpdateHotGiphyInfo = zoomMessenger.checkIfNeedUpdateHotGiphyInfo();
                    if (checkIfNeedUpdateHotGiphyInfo == null || checkIfNeedUpdateHotGiphyInfo.isEmpty()) {
                        zoomMessenger.getHotGiphyInfo(this.mSessionId, 8);
                    } else {
                        this.mPanelEmojis.Indicate_GetHotGiphyInfoResult(0, "", checkIfNeedUpdateHotGiphyInfo, "", this.mSessionId);
                    }
                }
            }
        }
    }

    private void click2CallSip(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (!NetworkUtil.hasDataNetwork(getContext())) {
                showSipUnavailable();
            } else if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    callSip(str);
                }
            } else {
                this.mSelectedPhoneNumber = str;
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 11);
            }
        }
    }

    private void callSip(String str) {
        CmmSIPCallManager.getInstance().callPeer(str);
    }

    private void showSipUnavailable() {
        if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
            SimpleMessageDialog.newInstance(getString(C4558R.string.zm_sip_error_network_disconnected_61381), false).show(getActivity().getSupportFragmentManager(), SimpleMessageDialog.class.getSimpleName());
        }
    }

    public void onPrivateStickerSelect(StickerEvent stickerEvent) {
        if (stickerEvent != null && !StringUtil.isEmptyOrNull(stickerEvent.getStickerId())) {
            MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
            if (zoomPrivateStickerMgr != null) {
                StickerInfo.Builder newBuilder = StickerInfo.newBuilder();
                newBuilder.setFileId(stickerEvent.getStickerId());
                newBuilder.setStatus(stickerEvent.getStatus());
                if (stickerEvent.getStickerPath() != null) {
                    newBuilder.setUploadingPath(stickerEvent.getStickerPath());
                }
                if (this.mThreadItem == null) {
                    if (zoomPrivateStickerMgr.sendSticker(newBuilder.build(), this.mSessionId) != 1) {
                        Toast.makeText(getActivity(), C4558R.string.zm_hint_sticker_send_failed, 1).show();
                    }
                } else if (zoomPrivateStickerMgr.sendStickerReply(newBuilder.build(), this.mSessionId, this.mThreadItem.sessionId, this.mThreadItem.messageId) != 1) {
                    Toast.makeText(getActivity(), C4558R.string.zm_hint_sticker_send_failed, 1).show();
                }
            }
        }
    }

    private void openSystemSAF() {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("*/*");
        startActivityForResult(intent, 1010);
    }

    private void uploadFileByUri(@NonNull final Uri uri) {
        String str;
        String str2;
        FileInfo dumpImageMetaData = FileUtils.dumpImageMetaData(VideoBoxApplication.getNonNullInstance(), uri);
        if (dumpImageMetaData == null || !checkFileSize(dumpImageMetaData.getSize())) {
            if (dumpImageMetaData == null) {
                str = "";
            } else {
                str = dumpImageMetaData.getExt();
            }
            if (StringUtil.isEmptyOrNull(str)) {
                String pathFromUri = FileUtils.getPathFromUri(VideoBoxApplication.getNonNullInstance(), uri);
                if (!StringUtil.isEmptyOrNull(pathFromUri)) {
                    str = AndroidAppUtil.getFileExtendName(pathFromUri);
                } else {
                    str = AndroidAppUtil.getFileExtendNameFromMimType(VideoBoxApplication.getNonNullInstance().getContentResolver().getType(uri));
                }
            }
            if (!PTApp.getInstance().isFileTypeAllowSendInChat(str)) {
                showFileFormatNotSupportDialog();
                return;
            }
            if (dumpImageMetaData == null) {
                str2 = "";
            } else {
                str2 = dumpImageMetaData.getDisplayName();
            }
            final String createTempFile = AppUtil.createTempFile(str2, getSessionDataPath(), str);
            this.mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<String>() {
                public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                    if (FileUtils.copyFileFromUri(VideoBoxApplication.getNonNullInstance(), uri, createTempFile)) {
                        observableEmitter.onNext(createTempFile);
                    } else {
                        observableEmitter.onNext("");
                    }
                    observableEmitter.onComplete();
                }
            }).subscribeOn(Schedulers.m266io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<? super T>) new Consumer<String>() {
                public void accept(String str) throws Exception {
                    if (!StringUtil.isEmptyOrNull(str)) {
                        MMChatInputFragment.this.uploadFile(str);
                    }
                }
            }));
        }
    }

    public void resetKeyBoard() {
        this.mMode = 0;
        updateUIMode(this.mMode, false);
    }
}
