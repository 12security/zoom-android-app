package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import com.box.androidsdk.content.models.BoxFile;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.MMChatInfoActivity;
import com.zipow.videobox.MMCommentActivity;
import com.zipow.videobox.MMCommentActivity.ThreadUnreadInfo;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.eventbus.ZMChatSession;
import com.zipow.videobox.eventbus.ZMFileAction;
import com.zipow.videobox.eventbus.ZMStarEvent;
import com.zipow.videobox.eventbus.ZMTemplateSelectProcessingEvent;
import com.zipow.videobox.fragment.MMChatInputFragment.OnChatInputListener;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI.ICrawlerLinkPreviewUIListener;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI.SimpleCrawlerLinkPreviewUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMCallbackUI.IIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI.SimpleIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMProtos.CommentDataResult;
import com.zipow.videobox.ptapp.IMProtos.CrawlLinkResponse;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.ptapp.IMProtos.LocalStorageTimeInterval;
import com.zipow.videobox.ptapp.IMProtos.MessageInfo;
import com.zipow.videobox.ptapp.IMProtos.MessageInfoList;
import com.zipow.videobox.ptapp.IMProtos.SelectParam;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfo;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfoMap;
import com.zipow.videobox.ptapp.IMProtos.ThreadDataResult;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack.IZoomPrivateStickerUIListener;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack.SimpleZoomPrivateStickerUIListener;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.ThreadDataUI;
import com.zipow.videobox.ptapp.ThreadDataUI.IThreadDataUIListener;
import com.zipow.videobox.ptapp.ThreadDataUI.SimpleThreadDataUIListener;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessageTemplateUI;
import com.zipow.videobox.ptapp.ZoomMessageTemplateUI.IZoomMessageTemplateUIListener;
import com.zipow.videobox.ptapp.ZoomMessageTemplateUI.SimpleZoomMessageTemplateUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.UnSupportMessageMgr;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMSessionsMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistory;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.tempbean.IMessageTemplateActionItem;
import com.zipow.videobox.tempbean.IMessageTemplateHead;
import com.zipow.videobox.tempbean.IMessageTemplateSelect;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;
import com.zipow.videobox.tempbean.IMessageTemplateSubHead;
import com.zipow.videobox.tempbean.IZoomMessageTemplate;
import com.zipow.videobox.util.ChatImgSaveHelper;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LinkPreviewHelper;
import com.zipow.videobox.util.MMMessageHelper;
import com.zipow.videobox.util.MMMessageHelper.MessageSyncer;
import com.zipow.videobox.util.MMMessageHelper.MessageSyncer.OnMessageSync;
import com.zipow.videobox.util.MMMessageHelper.ThrCommentState;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMActionMsgUtil;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.CommandEditText.SendMsgType;
import com.zipow.videobox.view.FloatingEmojis;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.JoinConfView.CannotJoinDialog;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.PresenceStateView;
import com.zipow.videobox.view.floatingtext.FloatingText;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.LinkPreviewMetaInfo;
import com.zipow.videobox.view.p014mm.MMAddonMessage.ActionNode;
import com.zipow.videobox.view.p014mm.MMAddonMessage.AddonNode;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeMsgHref;
import com.zipow.videobox.view.p014mm.MMCommentsEmojiCountItem;
import com.zipow.videobox.view.p014mm.MMCommentsFragment;
import com.zipow.videobox.view.p014mm.MMContentMessageItem.MMContentMessageAnchorInfo;
import com.zipow.videobox.view.p014mm.MMForwardZoomMessageDialogFragment;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMShareZoomFileDialogFragment;
import com.zipow.videobox.view.p014mm.MMThreadsRecyclerView;
import com.zipow.videobox.view.p014mm.MMThreadsRecyclerView.ThreadsUICallBack;
import com.zipow.videobox.view.p014mm.ReactionEmojiDetailDialog;
import com.zipow.videobox.view.p014mm.ReactionEmojiDialog;
import com.zipow.videobox.view.p014mm.ReactionEmojiDialog.OnReactionEmojiListener;
import com.zipow.videobox.view.p014mm.UnSupportEmojiDialog;
import com.zipow.videobox.view.p014mm.VoiceRecordView;
import com.zipow.videobox.view.p014mm.VoiceRecordView.OnVoiceRecordListener;
import com.zipow.videobox.view.p014mm.message.FontStyle;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import com.zipow.videobox.view.p014mm.message.MessageContextMenuItem;
import com.zipow.videobox.view.p014mm.message.ReactionContextMenuDialog;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile;
import p021us.zoom.androidlib.widget.IZMMenuItem;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMPopupMenu;
import p021us.zoom.androidlib.widget.ZMPopupMenu.OnMenuItemClickListener;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.ZMToast;
import p021us.zoom.videomeetings.C4558R;

public class MMThreadsFragment extends ZMDialogFragment implements ThreadsUICallBack, KeyboardListener, OnChatInputListener, ExtListener, OnVoiceRecordListener, OnClickListener, IABContactsCacheListener {
    private static final String ARG_ANCHOR_MSG = "anchorMsg";
    private static final String ARG_BUDDY_ID = "buddyId";
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_FORWARD_MESSAGE_ID = "forward_message_id";
    private static final String ARG_GROUP_ID = "groupId";
    private static final String ARG_IS_GROUP = "isGroup";
    private static final String ARG_SAVE_OPEN_TIME = "saveOpenTime";
    private static final String ARG_SEND_INTENT = "sendIntent";
    private static final String ARG_SHARED_MESSAGE_ID = "messageid";
    private static final int E2E_HINT_TYPE_DEFAULT = 0;
    private static final int E2E_HINT_TYPE_TRY_LATER = 2;
    private static final int E2E_HINT_TYPE_USER_CLOSE = 3;
    private static final int E2E_HINT_TYPE_WAIT_ONLINE = 1;
    private static final long POST_DELAY_TIME = 1000;
    private static final int REQUEST_CAPTURE_PHOTO = 101;
    private static final int REQUEST_CHAT_INFO = 102;
    private static final int REQUEST_CHOOSE_PICTURE = 100;
    public static final int REQUEST_CODE_SAVE_EMOJI = 6001;
    public static final int REQUEST_CODE_SAVE_FILE_IMAGE = 5002;
    public static final int REQUEST_CODE_SAVE_IMAGE = 5001;
    private static final int REQUEST_CONFIRM_SEND_IMAGE = 103;
    private static final int REQUEST_DO_FORWARD_MESSAGE = 115;
    public static final int REQUEST_EDIT_MESSAGE = 4001;
    private static final int REQUEST_GET_FORWARD_MESSAGE = 114;
    private static final int REQUEST_GET_SHAREES = 109;
    public static final int REQUEST_GOTO_COMMENT = 117;
    public static final int REQUEST_JUMP_MARKUNREAD = 116;
    private static final int REQUEST_PERMISSION_MIC = 7001;
    private static final int REQUEST_SELECT_CONTACT = 105;
    private static final int REQUEST_SELECT_FILE_TO_SEND = 104;
    private static final int REQUEST_SELECT_SLASH_CONTACT = 110;
    private static final int REQUEST_SELECT_TEMPLETE_CHANNEL = 112;
    private static final int REQUEST_SELECT_TEMPLETE_CONTACT = 111;
    private static final int REQUEST_SELECT_TEMPLETE_COUSTOM = 113;
    private static final int REQUEST_STORAGE_BY_MESSAGE_OPERATION = 107;
    private static final String RESULT_ARG_ANCHOR_MSG = "anchorMsg";
    private static final String TAG = "MMThreadsFragment";
    private boolean mActionCopyMsg = false;
    private MMContentMessageAnchorInfo mAnchorMessageItem;
    private Runnable mAtMsgUpdateUITask;
    /* access modifiers changed from: private */
    public Runnable mAutoMarkReadRunnable = new Runnable() {
        public void run() {
            MMThreadsFragment.this.autoMarkRead();
        }
    };
    private Button mBtnBack;
    private Button mBtnInviteE2EChat;
    private Button mBtnJump;
    private View mBtnPhoneCall;
    private View mBtnSearch;
    private View mBtnVideoCall;
    /* access modifiers changed from: private */
    public String mBuddyId;
    private String mBuddyName;
    private String mBuddyPhoneNumber;
    /* access modifiers changed from: private */
    public Map<MMMessageItem, Long> mCacheMsgs = new HashMap();
    private MMChatInputFragment mChatInputFragment;
    /* access modifiers changed from: private */
    public Runnable mChatWarnMsgAutoHiddenTask;
    private Runnable mCheckShowedMsgTask = new Runnable() {
        public void run() {
            if (MMThreadsFragment.this.mThreadsRecyclerView.isLayoutReady() && MMThreadsFragment.this.mCacheMsgs.size() > 0) {
                Iterator it = MMThreadsFragment.this.mCacheMsgs.entrySet().iterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    Long l = (Long) entry.getValue();
                    MMMessageItem mMMessageItem = (MMMessageItem) entry.getKey();
                    if (mMMessageItem == null || l == null) {
                        it.remove();
                    } else if (System.currentTimeMillis() - l.longValue() >= 500) {
                        it.remove();
                        if (!StringUtil.isEmptyOrNull(mMMessageItem.messageId) && MMThreadsFragment.this.mThreadsRecyclerView.isMsgShow(mMMessageItem.messageId)) {
                            boolean removeAtMessage = MMThreadsFragment.this.mMessageHelper.removeAtMessage(mMMessageItem.messageId);
                            if (MMThreadsFragment.this.mMessageHelper.isContainInOldMarkUnreads(mMMessageItem.serverSideTime)) {
                                removeAtMessage = true;
                            }
                            if (TextUtils.equals(mMMessageItem.messageId, MMThreadsFragment.this.mNewMsgMarkId)) {
                                MMThreadsFragment.this.mNewMsgMarkId = null;
                                removeAtMessage = true;
                            }
                            if (mMMessageItem.isUnread) {
                                removeAtMessage = true;
                            }
                            if (removeAtMessage) {
                                MMThreadsFragment.this.updateBottomHint();
                            }
                        }
                    }
                }
            }
            MMThreadsFragment.this.mHandler.postDelayed(this, 100);
        }
    };
    private MMMessageItem mCommentMsgItem;
    private ReactionContextMenuDialog mContextMenuDialog;
    private ProgressDialog mDownloadFileWaitingDialog = null;
    private int mE2EHintType = 0;
    private FloatingEmojis mFloatingEmojis;
    /* access modifiers changed from: private */
    public Runnable mFloatingEmojisTask;
    private FloatingText mFloatingText;
    /* access modifiers changed from: private */
    public String mGroupId;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private boolean mHasAutoDecryptWhenBuddyOnline = false;
    private ICrawlerLinkPreviewUIListener mICrawlerLinkPreviewUIListener = new SimpleCrawlerLinkPreviewUIListener() {
        public void OnLinkCrawlResult(CrawlLinkResponse crawlLinkResponse) {
            MMThreadsFragment.this.OnLinkCrawlResult(crawlLinkResponse);
        }

        public void OnDownloadFavicon(int i, String str) {
            MMThreadsFragment.this.OnDownloadFavicon(i, str);
        }

        public void OnDownloadImage(int i, String str) {
            MMThreadsFragment.this.OnDownloadImage(i, str);
        }
    };
    private IMAddrBookItem mIMAddrBookItem;
    private IIMCallbackUIListener mIMCallbackUIListener = new SimpleIMCallbackUIListener() {
        public void OnUnsupportMessageRecevied(int i, String str, String str2, String str3) {
            MMThreadsFragment.this.OnUnsupportMessageRecevied(i, str, str2, str3);
        }
    };
    private String mImageToSendOnSignedOn = null;
    private PresenceStateView mImgPresence;
    private View mImgTitleInfo;
    /* access modifiers changed from: private */
    public View mInvitationsTip;
    private boolean mIsCanceled = false;
    /* access modifiers changed from: private */
    public boolean mIsE2EChat = false;
    private boolean mIsFirstResume = true;
    /* access modifiers changed from: private */
    public boolean mIsGroup = false;
    private boolean mIsMyNostes = false;
    private boolean mIsReleaseToCancel = false;
    private boolean mIsRobot = false;
    private boolean mIsSendingHttpMsg = false;
    /* access modifiers changed from: private */
    public ZMKeyboardDetector mKeyboardDetector;
    private View mLineBelowSend;
    /* access modifiers changed from: private */
    public MMMessageHelper mMessageHelper;
    private OnMessageSync mMessageSync = new OnMessageSync() {
        public void onMessageSync(String str, String str2) {
            MMThreadsFragment.this.onMessageSync(str, str2);
        }
    };
    private IZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        }

        public void onBeginConnect() {
            MMThreadsFragment.this.onBeginConnect();
        }

        public void onConnectReturn(int i) {
            MMThreadsFragment.this.onConnectReturn(i);
        }

        public boolean onIndicateMessageReceived(String str, String str2, String str3) {
            return MMThreadsFragment.this.onIndicateMessageReceived(str, str2, str3);
        }

        public void onConfirm_MessageSent(String str, String str2, int i) {
            MMThreadsFragment.this.onConfirm_MessageSent(str, str2, i);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMThreadsFragment.this.onIndicateInfoUpdatedWithJID(str);
        }

        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMThreadsFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_ChatSessionUpdate(String str) {
            MMThreadsFragment.this.onNotify_ChatSessionUpdate(str);
        }

        public void onConfirmFileDownloaded(String str, String str2, int i) {
            MMThreadsFragment.this.onConfirmFileDownloaded(str, str2, i);
        }

        public void onConfirmPreviewPicFileDownloaded(String str, String str2, int i) {
            MMThreadsFragment.this.onConfirmPreviewPicFileDownloaded(str, str2, i);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMThreadsFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void onNotifyBuddyJIDUpgrade(String str, String str2, String str3) {
            MMThreadsFragment.this.onNotifyBuddyJIDUpgrade(str, str2, str3);
        }

        public void Indicate_BuddyPresenceChanged(String str) {
            MMThreadsFragment.this.onIndicate_BuddyPresenceChanged(str);
        }

        public void Indicate_OnlineBuddies(List<String> list) {
            MMThreadsFragment.this.onIndicateOnlineBuddies(list);
        }

        public void Indicate_GetContactsPresence(List<String> list, List<String> list2) {
            MMThreadsFragment.this.Indicate_GetContactsPresence(list, list2);
        }

        public void FT_OnSent(String str, String str2, int i) {
            MMThreadsFragment.this.FT_OnSent(str, str2, i);
        }

        public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
            MMThreadsFragment.this.FT_OnProgress(str, str2, i, j, j2);
        }

        public void FT_OnResumed(String str, String str2, int i) {
            MMThreadsFragment.this.FT_OnResumed(str, str2, i);
        }

        public void E2E_MessageStateUpdate(String str, String str2, int i) {
            MMThreadsFragment.this.E2E_MessageStateUpdate(str, str2, i);
        }

        public void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5) {
            MMThreadsFragment.this.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }

        public void Indicate_MessageDeleted(String str, String str2) {
            MMThreadsFragment.this.Indicate_MessageDeleted(str, str2);
        }

        public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
            MMThreadsFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i) {
            MMThreadsFragment.this.Indicate_FileForwarded(str, str2, str3, str4, i);
        }

        public void Indicate_FileDownloaded(String str, String str2, int i) {
            MMThreadsFragment.this.Indicate_FileDownloaded(str, str2, i);
        }

        public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
            MMThreadsFragment.this.FT_OnDownloadByMsgIDTimeOut(str, str2);
        }

        public void FT_UploadFileInChatTimeOut(String str, String str2) {
            MMThreadsFragment.this.FT_UploadFileInChatTimeOut(str, str2);
        }

        public void Indicate_BlockedUsersUpdated() {
            MMThreadsFragment.this.Indicate_BlockedUsersUpdated();
        }

        public void Indicate_BlockedUsersAdded(List<String> list) {
            MMThreadsFragment.this.Indicate_BlockedUsersAdded(list);
        }

        public void Indicate_BlockedUsersRemoved(List<String> list) {
            MMThreadsFragment.this.Indicate_BlockedUsersRemoved(list);
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            MMThreadsFragment.this.On_DestroyGroup(i, str, str2, str3, j);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMThreadsFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }

        public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, long j2, boolean z) {
            MMThreadsFragment.this.Indicate_RevokeMessageResult(str, str2, str3, str4, j, j2, z);
        }

        public void Indicate_DownloadFileByUrlIml(String str, int i) {
            MMThreadsFragment.this.Indicate_DownloadFileByUrlIml(str, i);
        }

        public void Indicate_SendAddonCommandResultIml(String str, boolean z) {
            MMThreadsFragment.this.Indicate_SendAddonCommandResultIml(str, z);
        }

        public void Notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
            MMThreadsFragment.this.notify_ChatSessionMarkUnreadUpdate(sessionMessageInfoMap);
        }

        public void onNotify_SessionMarkUnreadCtx(String str, int i, String str2, List<String> list) {
            MMThreadsFragment.this.onNotify_SessionMarkUnreadCtx(str, i, str2, list);
        }

        public void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
            MMThreadsFragment.this.Indicate_EditMessageResultIml(str, str2, str3, j, j2, z);
        }

        public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
            MMThreadsFragment.this.NotifyOutdatedHistoryRemoved(list, j);
        }

        public void NotifyChatUnavailable(String str, String str2) {
            if (!MMThreadsFragment.this.mIsGroup && !TextUtils.isEmpty(str) && MMThreadsFragment.this.mSessionId.equals(str)) {
                MMThreadsFragment.this.onIndicateMessageReceived(str, null, str2);
            }
        }

        public void NotifyCallUnavailable(String str, long j) {
            if (MMThreadsFragment.this.mSessionId.equals(str)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    if (buddyWithJID != null) {
                        String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                        ZMActivity zMActivity = (ZMActivity) MMThreadsFragment.this.getActivity();
                        if (zMActivity != null) {
                            ZMToast.show((Context) zMActivity, (CharSequence) String.format(MMThreadsFragment.this.getString(C4558R.string.zm_mm_lbl_xxx_declined_the_call_62107), new Object[]{buddyDisplayName}), 1);
                        }
                    }
                }
                ZmPtUtils.onCallError(j);
            }
        }

        public void NotifyDeleteMsgFailed(String str, String str2) {
            ZMActivity zMActivity = (ZMActivity) MMThreadsFragment.this.getActivity();
            if (zMActivity != null && zMActivity.isActive()) {
                Toast.makeText(zMActivity, str2, 1).show();
            }
        }

        public void notify_StarMessageDataUpdate() {
            if (MMThreadsFragment.this.mThreadsRecyclerView != null) {
                MMThreadsFragment.this.mThreadsRecyclerView.notifyStarMessageDataUpdate();
            }
        }

        public void onRemoveBuddy(String str, int i) {
            if (i == 0 && !MMThreadsFragment.this.mIsGroup && StringUtil.isSameString(str, MMThreadsFragment.this.mBuddyId)) {
                MMThreadsFragment.this.dismiss();
            }
        }

        public void Indicate_FileMessageDeleted(String str, String str2) {
            MMThreadsFragment.this.Indicate_FileMessageDeleted(str, str2);
        }

        public void Indicate_SessionOfflineMessageFinished(String str) {
            MMThreadsFragment.this.Indicate_SessionOfflineMessageFinished(str);
        }

        public void Notify_ChatSessionUnreadCountReady(List<String> list) {
            MMThreadsFragment.this.Notify_ChatSessionUnreadCountReady(list);
        }

        public void On_BroadcastUpdate(int i, String str, boolean z) {
            MMThreadsFragment.this.On_BroadcastUpdate(i, str, z);
        }
    };
    private IPTUIListener mNetworkStateReceiver;
    /* access modifiers changed from: private */
    public String mNewMsgMarkId;
    private View mPanelBottomHint;
    private View mPanelDoComment;
    private View mPanelE2EHint;
    private View mPanelMsgContextEmptyView;
    private View mPanelNotes;
    private View mPanelServerError;
    private View mPanelTimedChatHint;
    private View mPanelTitleCenter;
    /* access modifiers changed from: private */
    public View mPanelWarnMsg;
    private HashMap<String, Integer> mPendingUploadFileRatios = new HashMap<>();
    private MMMessageItem mPermissionPenddingMessage;
    private ReactionEmojiDialog mReactionEmojiDialog;
    private File mSavingEmoji;
    private File mSavingFile;
    private MMMessageItem mSavingMsg;
    /* access modifiers changed from: private */
    public IMSessionSearchFragment mSearchFragment;
    private String mSelectedPhoneNumber = null;
    private Map<CharSequence, Long> mSentReactions = new HashMap();
    private Runnable mServerErrorAutoHiddenTask;
    /* access modifiers changed from: private */
    public String mSessionId;
    private TextView mSharingScreenStatusView;
    private Set<String> mShowedNewMessages = new HashSet();
    private IZoomPrivateStickerUIListener mStickerListener = new SimpleZoomPrivateStickerUIListener() {
        public void OnSendPrivateSticker(String str, int i, String str2, String str3) {
            MMThreadsFragment.this.OnSendPrivateSticker(str, i, str2, str3);
        }

        public void OnSendStickerMsgAppended(String str, String str2) {
            MMThreadsFragment.this.OnSendStickerMsgAppended(str, str2);
        }
    };
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ZMAsyncURLDownloadFile mTaskDownloadFile;
    private TextView mThreadBody;
    private IThreadDataUIListener mThreadDataUIListener = new SimpleThreadDataUIListener() {
        public void OnGetThreadData(ThreadDataResult threadDataResult) {
            MMThreadsFragment.this.OnGetThreadData(threadDataResult);
        }

        public void OnGetCommentData(CommentDataResult commentDataResult) {
            MMThreadsFragment.this.OnGetCommentData(commentDataResult);
        }

        public void OnThreadContextUpdate(String str, String str2) {
            MMThreadsFragment.this.OnThreadContextUpdate(str, str2);
        }

        public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
            MMThreadsFragment.this.OnFetchEmojiDetailInfo(str, str2, str3, str4, z);
        }

        public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
            MMThreadsFragment.this.OnFetchEmojiCountInfo(str, str2, list, z);
        }

        public void OnMessageEmojiInfoUpdated(String str, String str2) {
            MMThreadsFragment.this.OnMessageEmojiInfoUpdated(str, str2);
        }

        public void OnEmojiCountInfoLoadedFromDB(String str) {
            MMThreadsFragment.this.OnEmojiCountInfoLoadedFromDB(str);
        }

        public void OnSyncThreadCommentCount(String str, String str2, List<String> list, boolean z) {
            MMThreadsFragment.this.OnSyncThreadCommentCount(str, str2, list, z);
        }
    };
    private TextView mThreadSender;
    private int mThreadSortType;
    /* access modifiers changed from: private */
    public MMThreadsRecyclerView mThreadsRecyclerView;
    private View mTitleBar;
    private TextView mTxtAnnouncement;
    private TextView mTxtBottomHint;
    private TextView mTxtBottomReplyDown;
    private TextView mTxtBottomReplyUp;
    private TextView mTxtDisableMsg;
    private TextView mTxtE2EHintMsg;
    private TextView mTxtMarkUnread;
    private TextView mTxtMention;
    private TextView mTxtMsgContextContentLoading;
    private TextView mTxtMsgContextLoadingError;
    private TextView mTxtNewMsgMark;
    private TextView mTxtServerError;
    private TextView mTxtTitle;
    private TextView mTxtWarnMsg;
    private Runnable mUnSupportEmojiRunnable = new Runnable() {
        public void run() {
            UnSupportEmojiDialog.show((ZMActivity) MMThreadsFragment.this.getActivity());
        }
    };
    private VoiceRecordView mVoiceRecordView;
    /* access modifiers changed from: private */
    public ProgressDialog mWaitingDialog;
    /* access modifiers changed from: private */
    public String mWaitingDialogId;
    private IZoomMessageTemplateUIListener messageTemplateUIListener = new SimpleZoomMessageTemplateUIListener() {
        public void Notify_SelectCommandResponse(boolean z, SelectParam selectParam) {
            if (MMThreadsFragment.this.mThreadsRecyclerView != null) {
                String str = "";
                String str2 = "";
                if (selectParam != null) {
                    str2 = selectParam.getMessageId();
                    str = selectParam.getSessionId();
                }
                if (TextUtils.equals(MMThreadsFragment.this.mSessionId, str)) {
                    MMThreadsFragment.this.updateMessage(str, str2);
                }
            }
        }

        public void Notify_EditRobotMessage(String str, String str2) {
            if (TextUtils.equals(MMThreadsFragment.this.mSessionId, str)) {
                MMThreadsFragment.this.updateMessage(str, str2);
            }
        }

        public void Notify_RevokeRobotMessage(String str, String str2, String str3) {
            if (StringUtil.isSameString(str, MMThreadsFragment.this.mSessionId)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null && zoomMessenger.getMyself() != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                    if (sessionById != null) {
                        MMThreadsFragment.this.mThreadsRecyclerView.onRecallMessage(true, sessionById.getMessageById(str3), str2, 0);
                    }
                }
            }
        }
    };
    private View panelActions;
    private TextView txtTimedChatHintMsg;

    private class ActionMenuItem extends ZMSimpleMenuItem {
        private String labelAction;
        private String value;

        public ActionMenuItem(String str, String str2) {
            super(0, str2);
            setLabelAction(str);
        }

        public ActionMenuItem(String str, String str2, String str3, boolean z) {
            super(0, str2);
            setLabelAction(str);
            setValue(str3);
            setmDisable(z);
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String str) {
            this.value = str;
        }

        public String getLabelAction() {
            return this.labelAction;
        }

        public void setLabelAction(String str) {
            this.labelAction = str;
        }
    }

    static class LinkMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 1;
        public static final int ACTION_OPEN_LINK = 0;

        public LinkMenuItem(String str, int i) {
            super(i, str);
        }
    }

    static class MeetingNoMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_CALL = 1;
        public static final int ACTION_COPY = 2;
        public static final int ACTION_JOIN_MEETING = 0;

        public MeetingNoMenuItem(String str, int i) {
            super(i, str);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_DownloadFileByUrlIml(String str, int i) {
    }

    private void onClickTxtReplyDown() {
    }

    private void onClickTxtReplyUp() {
    }

    public boolean isMessageDecrypted(int i) {
        return i == 7 || i == 4 || i == 1 || i == 2;
    }

    public void onClickGiphyBtn(MMMessageItem mMMessageItem, View view) {
    }

    public void onLoadingMore() {
    }

    public void onPbxSmsSent(String str, String str2) {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showMsgContextInActivity(Fragment fragment, MMContentMessageAnchorInfo mMContentMessageAnchorInfo) {
        showMsgContextInActivity(fragment, mMContentMessageAnchorInfo, 0);
    }

    public static void showMsgContextInActivity(Fragment fragment, MMContentMessageAnchorInfo mMContentMessageAnchorInfo, int i) {
        if (fragment != null && mMContentMessageAnchorInfo != null) {
            String sessionId = mMContentMessageAnchorInfo.getSessionId();
            if (!StringUtil.isEmptyOrNull(sessionId)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    boolean z = false;
                    IMAddrBookItem iMAddrBookItem = null;
                    if (zoomMessenger.getGroupById(sessionId) != null) {
                        z = true;
                    } else {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(sessionId);
                        if (buddyWithJID != null) {
                            iMAddrBookItem = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                        } else {
                            return;
                        }
                    }
                    MMThreadsFragment mMThreadsFragment = new MMThreadsFragment();
                    Bundle bundle = new Bundle();
                    if (z) {
                        bundle.putString(ARG_GROUP_ID, sessionId);
                        bundle.putBoolean(ARG_IS_GROUP, true);
                    } else {
                        bundle.putSerializable(ARG_CONTACT, iMAddrBookItem);
                        bundle.putString(ARG_BUDDY_ID, sessionId);
                    }
                    bundle.putSerializable(MMCommentActivity.RESULT_ARG_ANCHOR_MSG, mMContentMessageAnchorInfo);
                    mMThreadsFragment.setArguments(bundle);
                    SimpleActivity.show(fragment, MMThreadsFragment.class.getName(), bundle, i);
                }
            }
        }
    }

    public static void showAsGroupChatInActivity(ZMActivity zMActivity, String str, boolean z, Intent intent) {
        if (zMActivity != null && str != null) {
            MMThreadsFragment mMThreadsFragment = new MMThreadsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_GROUP_ID, str);
            bundle.putBoolean(ARG_IS_GROUP, true);
            bundle.putBoolean(ARG_SAVE_OPEN_TIME, z);
            bundle.putParcelable(ARG_SEND_INTENT, intent);
            mMThreadsFragment.setArguments(bundle);
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, mMThreadsFragment, MMThreadsFragment.class.getName()).commit();
        }
    }

    public static void showAsOneToOneInActivity(ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem, String str, boolean z, Intent intent) {
        if (zMActivity != null && str != null) {
            MMThreadsFragment mMThreadsFragment = new MMThreadsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_CONTACT, iMAddrBookItem);
            bundle.putString(ARG_BUDDY_ID, str);
            bundle.putBoolean(ARG_IS_GROUP, false);
            bundle.putBoolean(ARG_SAVE_OPEN_TIME, z);
            bundle.putParcelable(ARG_SEND_INTENT, intent);
            mMThreadsFragment.setArguments(bundle);
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, mMThreadsFragment, MMThreadsFragment.class.getName()).commit();
        }
    }

    public static MMThreadsFragment findMMCommentsFragment(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        return (MMThreadsFragment) fragmentManager.findFragmentByTag(MMThreadsFragment.class.getName());
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments == null) {
            dismiss();
            return;
        }
        boolean z = arguments.getBoolean(ARG_SAVE_OPEN_TIME);
        this.mIsGroup = arguments.getBoolean(ARG_IS_GROUP);
        this.mIMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
        this.mBuddyId = arguments.getString(ARG_BUDDY_ID);
        this.mGroupId = arguments.getString(ARG_GROUP_ID);
        this.mSessionId = this.mIsGroup ? this.mGroupId : this.mBuddyId;
        this.mAnchorMessageItem = (MMContentMessageAnchorInfo) arguments.getSerializable(MMCommentActivity.RESULT_ARG_ANCHOR_MSG);
        this.mIsMyNostes = UIMgr.isMyNotes(this.mSessionId);
        IMAddrBookItem iMAddrBookItem = this.mIMAddrBookItem;
        if (iMAddrBookItem != null) {
            this.mIsRobot = iMAddrBookItem.getIsRobot();
        }
        this.mThreadsRecyclerView.setSessionInfo(this.mSessionId, this.mIsGroup);
        this.mMessageHelper = new MMMessageHelper(this.mSessionId, this.mThreadsRecyclerView, this);
        MMContentMessageAnchorInfo mMContentMessageAnchorInfo = this.mAnchorMessageItem;
        if (mMContentMessageAnchorInfo != null) {
            this.mThreadsRecyclerView.setAnchorMessageItem(mMContentMessageAnchorInfo);
            this.mPanelMsgContextEmptyView.setVisibility(0);
            this.mBtnSearch.setVisibility(8);
            this.panelActions.setVisibility(8);
            this.mImgTitleInfo.setVisibility(8);
            this.mBtnJump.setVisibility(0);
        } else {
            this.mMessageHelper.checkUnreadComments(true);
        }
        this.mMessageHelper.checkMarkUnreadInfo();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            checkE2EStatus();
            this.mThreadsRecyclerView.setIsE2EChat(this.mIsE2EChat);
            ZoomChatSession zoomChatSession = null;
            if (!this.mIsGroup) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                if (buddyWithJID != null) {
                    this.mBuddyPhoneNumber = buddyWithJID.getPhoneNumber();
                } else {
                    IMAddrBookItem iMAddrBookItem2 = this.mIMAddrBookItem;
                    if (iMAddrBookItem2 != null) {
                        this.mBuddyPhoneNumber = iMAddrBookItem2.getNormalizedPhoneNumber(0);
                    }
                }
                if (zoomMessenger.isAnyBuddyGroupLarge()) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(this.mBuddyId);
                    zoomMessenger.subBuddyTempPresence(arrayList);
                }
                if (this.mAnchorMessageItem == null) {
                    zoomChatSession = zoomMessenger.getSessionById(this.mBuddyId);
                    if (zoomChatSession != null) {
                        this.mSessionId = zoomChatSession.getSessionId();
                        if (ZMSessionsMgr.getInstance().isSessionUnreadCountReady()) {
                            this.mThreadsRecyclerView.setUnreadMsgInfo(zoomChatSession.getUnreadMessageCount(), zoomChatSession.getReadedMsgTime());
                        }
                    }
                }
                this.mBtnVideoCall.setContentDescription(getResources().getString(C4558R.string.zm_btn_video_call));
                this.mBtnPhoneCall.setContentDescription(getResources().getString(C4558R.string.zm_btn_audio_call));
            } else if (zoomMessenger.getGroupById(this.mGroupId) != null) {
                this.mMessageHelper.checkAllATMessages();
                refreshGroupInfo();
                if (this.mAnchorMessageItem == null) {
                    zoomChatSession = zoomMessenger.getSessionById(this.mGroupId);
                    if (zoomChatSession != null) {
                        this.mSessionId = zoomChatSession.getSessionId();
                        if (ZMSessionsMgr.getInstance().isSessionUnreadCountReady()) {
                            this.mThreadsRecyclerView.setUnreadMsgInfo(zoomChatSession.getUnreadMessageCount(), zoomChatSession.getReadedMsgTime());
                        }
                    }
                }
                this.mBtnVideoCall.setContentDescription(getResources().getString(C4558R.string.zm_mm_opt_video_call));
                this.mBtnPhoneCall.setContentDescription(getResources().getString(C4558R.string.zm_mm_opt_voice_call));
            } else {
                return;
            }
            if (z && zoomChatSession != null) {
                zoomChatSession.storeLastSearchAndOpenSessionTime(CmmTime.getMMNow() / 1000);
            }
            if (this.mAnchorMessageItem == null) {
                MessageSyncer.getInstance().cleanUnreadMessageCount(this.mSessionId);
            }
            if (!this.mIsMyNostes) {
                showTimedChat();
            }
            this.mThreadsRecyclerView.setMessageHelper(this.mMessageHelper);
            notifyOpenRobotChatSession();
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_thread, viewGroup, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mGroupId = arguments.getString(ARG_GROUP_ID);
            this.mBuddyId = arguments.getString(ARG_BUDDY_ID);
            this.mIsGroup = arguments.getBoolean(ARG_IS_GROUP);
        }
        this.mKeyboardDetector = (ZMKeyboardDetector) inflate.findViewById(C4558R.C4560id.keyboardDetector);
        this.mTxtAnnouncement = (TextView) inflate.findViewById(C4558R.C4560id.txtAnnouncement);
        initInputFragment(this.mIsGroup ? this.mGroupId : this.mBuddyId);
        this.mTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(C4558R.C4560id.swipeRefreshLayout);
        this.mThreadsRecyclerView = (MMThreadsRecyclerView) inflate.findViewById(C4558R.C4560id.commentsRecyclerView);
        this.mPanelDoComment = inflate.findViewById(C4558R.C4560id.panelDoComment);
        this.mThreadSender = (TextView) inflate.findViewById(C4558R.C4560id.threadSender);
        this.mThreadBody = (TextView) inflate.findViewById(C4558R.C4560id.threadBody);
        this.mVoiceRecordView = (VoiceRecordView) inflate.findViewById(C4558R.C4560id.panelVoiceRcdHint);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mImgPresence = (PresenceStateView) inflate.findViewById(C4558R.C4560id.imgPresence);
        this.mSharingScreenStatusView = (TextView) inflate.findViewById(C4558R.C4560id.presence_status_sharing_screen_view);
        this.mBtnInviteE2EChat = (Button) inflate.findViewById(C4558R.C4560id.btnInviteE2EChat);
        this.mTxtWarnMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtWarnMsg);
        this.mPanelWarnMsg = inflate.findViewById(C4558R.C4560id.panelWarnMsg);
        this.mPanelE2EHint = inflate.findViewById(C4558R.C4560id.panelE2EHint);
        this.mBtnJump = (Button) inflate.findViewById(C4558R.C4560id.btnJump);
        this.mTxtMsgContextLoadingError = (TextView) inflate.findViewById(C4558R.C4560id.txtMsgContextLoadingError);
        this.mTxtMsgContextLoadingError.setText(Html.fromHtml(getString(C4558R.string.zm_lbl_content_load_error)));
        this.mPanelMsgContextEmptyView = inflate.findViewById(C4558R.C4560id.panelMsgContextEmptyView);
        this.mTxtMsgContextContentLoading = (TextView) inflate.findViewById(C4558R.C4560id.txtMsgContextContentLoading);
        this.mBtnSearch = inflate.findViewById(C4558R.C4560id.btnSearch);
        this.mTxtE2EHintMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtE2EHintMsg);
        this.mTxtDisableMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtDisableMsg);
        this.mTxtBottomHint = (TextView) inflate.findViewById(C4558R.C4560id.txtBottomHint);
        this.mLineBelowSend = inflate.findViewById(C4558R.C4560id.lineBelowSend);
        this.mPanelServerError = inflate.findViewById(C4558R.C4560id.panelServerError);
        this.mTxtServerError = (TextView) inflate.findViewById(C4558R.C4560id.txtServerError);
        this.mTxtBottomReplyDown = (TextView) inflate.findViewById(C4558R.C4560id.txtBottomReplyDown);
        this.mTxtBottomReplyUp = (TextView) inflate.findViewById(C4558R.C4560id.txtBottomReplyUp);
        this.txtTimedChatHintMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtTimedChatHintMsg);
        this.txtTimedChatHintMsg.setMovementMethod(LinkMovementMethod.getInstance());
        this.mPanelTimedChatHint = inflate.findViewById(C4558R.C4560id.panelTimedChatHint);
        this.mTxtMarkUnread = (TextView) inflate.findViewById(C4558R.C4560id.txtMarkUnread);
        this.mTxtMention = (TextView) inflate.findViewById(C4558R.C4560id.txtMention);
        this.mTxtNewMsgMark = (TextView) inflate.findViewById(C4558R.C4560id.txtNewMsgMark);
        this.mPanelNotes = inflate.findViewById(C4558R.C4560id.myNotesPanel);
        this.mPanelBottomHint = inflate.findViewById(C4558R.C4560id.panelBottomHint);
        this.panelActions = inflate.findViewById(C4558R.C4560id.panelActions);
        this.mPanelTitleCenter = inflate.findViewById(C4558R.C4560id.panelTitleCenter);
        this.mBtnPhoneCall = inflate.findViewById(C4558R.C4560id.btnPhoneCall);
        this.mBtnVideoCall = inflate.findViewById(C4558R.C4560id.btnVideoCall);
        this.mImgTitleInfo = inflate.findViewById(C4558R.C4560id.imgTitleInfo);
        this.mThreadsRecyclerView.setUICallBack(this);
        this.mKeyboardDetector.setKeyboardListener(this);
        this.mThreadsRecyclerView.setParentFragment(this);
        this.mBtnVideoCall.setOnClickListener(this);
        this.mBtnPhoneCall.setOnClickListener(this);
        ThreadDataUI.getInstance().addListener(this.mThreadDataUIListener);
        this.mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                MMThreadsFragment.this.loadHistoryThreads();
            }
        });
        this.mVoiceRecordView.setVisibility(8);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
        if (bundle != null) {
            this.mImageToSendOnSignedOn = bundle.getString("mImageToSendOnSignedOn");
            this.mE2EHintType = bundle.getInt("mE2EHintType");
            this.mHasAutoDecryptWhenBuddyOnline = bundle.getBoolean("mHasAutoDecryptWhenBuddyOnline");
            this.mIsFirstResume = false;
            this.mPendingUploadFileRatios = (HashMap) bundle.getSerializable("mPendingUploadFileRatios");
        }
        this.mImgPresence.setDarkMode(true);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnInviteE2EChat.setOnClickListener(this);
        this.mBtnJump.setOnClickListener(this);
        this.mTxtMsgContextLoadingError.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnE2EHintClose).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnTimedChatHintClose).setOnClickListener(this);
        this.mBtnSearch.setOnClickListener(this);
        this.mTxtBottomHint.setOnClickListener(this);
        this.mTxtMarkUnread.setOnClickListener(this);
        this.mTxtMention.setOnClickListener(this);
        this.mTxtNewMsgMark.setOnClickListener(this);
        this.mTxtBottomReplyDown.setOnClickListener(this);
        this.mTxtBottomReplyUp.setOnClickListener(this);
        this.mPanelTitleCenter.setOnClickListener(this);
        this.mKeyboardDetector.setOnClickListener(this);
        this.mThreadsRecyclerView.setOnClickListener(this);
        CrawlerLinkPreviewUI.getInstance().addListener(this.mICrawlerLinkPreviewUIListener);
        IMCallbackUI.getInstance().addListener(this.mIMCallbackUIListener);
        ZoomMessageTemplateUI.getInstance().addListener(this.messageTemplateUIListener);
        MessageSyncer.getInstance().addListener(this.mMessageSync);
        EventBus.getDefault().register(this);
        this.mThreadsRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                if (i == 0) {
                    if (MMThreadsFragment.this.mIsE2EChat) {
                        if (PTApp.getInstance().getZoomMessenger() != null) {
                            if (MMThreadsFragment.this.mThreadsRecyclerView.tryDecryptVisiableE2EMesssage()) {
                                MMThreadsFragment.this.showE2EMessageDecryptTimeoutHint();
                            } else {
                                MMThreadsFragment.this.closeE2EMessageDecryptTimeoutHint();
                            }
                        } else {
                            return;
                        }
                    }
                    MMThreadsFragment.this.updateBottomHint();
                } else if (MMThreadsFragment.this.mKeyboardDetector.isKeyboardOpen() && !MMThreadsFragment.this.isInSearchMode()) {
                    UIUtil.closeSoftKeyboard(MMThreadsFragment.this.getActivity(), MMThreadsFragment.this.mThreadsRecyclerView);
                }
            }

            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                MMThreadsFragment.this.mHandler.removeCallbacks(MMThreadsFragment.this.mAutoMarkReadRunnable);
                MMThreadsFragment.this.mHandler.postDelayed(MMThreadsFragment.this.mAutoMarkReadRunnable, 1000);
            }
        });
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                this.mThreadSortType = threadDataProvider.getThreadSortType();
            }
        }
        return inflate;
    }

    public void onDestroyView() {
        ThreadDataUI.getInstance().removeListener(this.mThreadDataUIListener);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        CrawlerLinkPreviewUI.getInstance().removeListener(this.mICrawlerLinkPreviewUIListener);
        IMCallbackUI.getInstance().removeListener(this.mIMCallbackUIListener);
        ZoomMessageTemplateUI.getInstance().removeListener(this.messageTemplateUIListener);
        MessageSyncer.getInstance().removeListener(this.mMessageSync);
        EventBus.getDefault().unregister(this);
        this.mHandler.removeCallbacksAndMessages(null);
        destroyFloatingView();
        destroyFloatingEmojis();
        super.onDestroyView();
    }

    public void onStart() {
        super.onStart();
        NotificationMgr.removeMessageNotificationMM(getActivity(), this.mSessionId);
        PrivateStickerUICallBack.getInstance().addListener(this.mStickerListener);
        this.mThreadsRecyclerView.refreshStarMsgs();
    }

    public void onStop() {
        super.onStop();
        NotificationMgr.removeMessageNotificationMM(getActivity(), this.mSessionId);
        PrivateStickerUICallBack.getInstance().removeListener(this.mStickerListener);
    }

    public void onResume() {
        super.onResume();
        this.mThreadsRecyclerView.setIsResume(true);
        checkE2EStatus();
        updateUI();
        this.mThreadsRecyclerView.loadThreads(true);
        if (this.mThreadsRecyclerView.isLoading(1)) {
            updateRefreshStatus(true);
        }
        ABContactsCache.getInstance().addListener(this);
        if (ABContactsCache.getInstance().needReloadAll()) {
            ABContactsCache.getInstance().reloadAllContacts();
        }
        if (this.mIsFirstResume) {
            this.mIsFirstResume = false;
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Bundle arguments = getArguments();
                if (arguments != null) {
                    Intent intent = (Intent) arguments.getParcelable(ARG_SEND_INTENT);
                    if (intent != null) {
                        String type = intent.getType();
                        Parcelable parcelableExtra = intent.getParcelableExtra("android.intent.extra.STREAM");
                        String stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
                        if (parcelableExtra instanceof Uri) {
                            Uri uri = (Uri) parcelableExtra;
                            if (type == null || !type.contains("image/")) {
                                String pathFromUri = FileUtils.getPathFromUri(activity, uri);
                                if (pathFromUri != null && pathFromUri.startsWith(File.separator) && new File(pathFromUri).exists() && PTApp.getInstance().getZoomMessenger() != null) {
                                    this.mChatInputFragment.uploadFile(pathFromUri);
                                }
                            } else {
                                this.mChatInputFragment.onSelectedPhoto(uri, false);
                            }
                        } else if (!StringUtil.isEmptyOrNull(stringExtra)) {
                            this.mChatInputFragment.sendText(stringExtra, null, SendMsgType.MESSAGE);
                        }
                    }
                }
            } else {
                return;
            }
        }
        updateBlockedState();
        updateActivatedState();
        startToListenNetworkEvent();
        this.mHandler.postDelayed(this.mCheckShowedMsgTask, 100);
        checkMarkUnreadMsg();
    }

    public void onPause() {
        if (this.mThreadSortType == 0 && getActivity() != null && getActivity().isFinishing() && this.mAnchorMessageItem == null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    sessionById.cleanUnreadMessageCount();
                }
            }
        }
        super.onPause();
        dismissContextMenuDialog();
        dismissReactionEmojiDialog();
        destroyFloatingEmojis();
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.setIsResume(false);
        }
        ABContactsCache.getInstance().removeListener(this);
        stopToListenNetworkEvent();
        this.mHandler.removeCallbacks(this.mCheckShowedMsgTask);
    }

    public void onDestroy() {
        super.onDestroy();
        dismissContextMenuDialog();
        dismissReactionEmojiDialog();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mImageToSendOnSignedOn", this.mImageToSendOnSignedOn);
        bundle.putInt("mE2EHintType", this.mE2EHintType);
        bundle.putBoolean("mHasAutoDecryptWhenBuddyOnline", this.mHasAutoDecryptWhenBuddyOnline);
        bundle.putSerializable("mPendingUploadFileRatios", this.mPendingUploadFileRatios);
    }

    public boolean isInSearchMode() {
        IMSessionSearchFragment iMSessionSearchFragment = this.mSearchFragment;
        return iMSessionSearchFragment != null && iMSessionSearchFragment.isVisible();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZMStarEvent zMStarEvent) {
        if (isAdded()) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.notifyStarMessageDataUpdate();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeChatSessionEvent(@NonNull ZMChatSession zMChatSession) {
        if (StringUtil.isSameString(this.mSessionId, zMChatSession.getSessionID())) {
            if (zMChatSession.getAction() == 1) {
                this.mMessageHelper.clearAllChatInfo();
                updateMessageListView(false);
            } else if (zMChatSession.getAction() == 2) {
                MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
                if (mMThreadsRecyclerView != null) {
                    mMThreadsRecyclerView.onRecallComment(zMChatSession.getSessionID(), zMChatSession.getMessageID());
                }
            } else if (zMChatSession.getAction() == 3) {
                final String messageID = zMChatSession.getMessageID();
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        MMThreadsFragment.this.syncThreadEmojiCountInfo(messageID);
                    }
                }, 1000);
            }
        }
    }

    /* access modifiers changed from: private */
    public void syncThreadEmojiCountInfo(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                MMMessageItem findThread = mMThreadsRecyclerView.findThread(str);
                if (findThread != null) {
                    this.mThreadsRecyclerView.updateMessageEmojiCountInfo(findThread, false);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshGroupInfo() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.refreshGroupInfo(this.mGroupId);
        }
    }

    public void checkAllShowMsgs() {
        List<MMMessageItem> allShowMsgs = this.mThreadsRecyclerView.getAllShowMsgs();
        if (!CollectionsUtil.isCollectionEmpty(allShowMsgs)) {
            for (MMMessageItem onMessageShowed : allShowMsgs) {
                onMessageShowed(onMessageShowed);
            }
        }
    }

    public void showSearchView(boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        int i = 0;
        if (!(zoomMessenger == null || zoomMessenger.e2eGetMyOption() == 2)) {
            View view = this.mBtnSearch;
            if (view != null && this.mAnchorMessageItem == null && this.mPanelTitleCenter != null) {
                ViewPropertyAnimator animate = view.animate();
                if (z == this.mPanelTitleCenter.isShown()) {
                    this.mPanelTitleCenter.setVisibility(z ? 4 : 0);
                }
                if (animate != null) {
                    animate.alpha(z ? 1.0f : 0.0f).setDuration(300).start();
                } else if (z != this.mBtnSearch.isShown()) {
                    View view2 = this.mBtnSearch;
                    if (!z) {
                        i = 8;
                    }
                    view2.setVisibility(i);
                }
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.panelTitleCenter) {
            onClickPanelTitle();
        } else if (id == C4558R.C4560id.btnInviteE2EChat) {
            onClickBtnInviteE2EChat();
        } else if (id == C4558R.C4560id.btnE2EHintClose) {
            onClickBtnE2EHintClose();
        } else if (id == C4558R.C4560id.btnTimedChatHintClose) {
            onClickBtnTimedChatHintClose();
        } else if (id == C4558R.C4560id.btnJump) {
            onClickBtnJump();
        } else if (id == C4558R.C4560id.txtMsgContextLoadingError) {
            onClickTxtMsgContextLoadingError();
        } else if (id == C4558R.C4560id.btnSearch) {
            onClickBtnSearch();
        } else if (id == C4558R.C4560id.txtBottomHint) {
            onClickTxtBottomHint();
        } else if (id == C4558R.C4560id.txtMarkUnread) {
            onClickTxtMarkUnread();
        } else if (id == C4558R.C4560id.txtNewMsgMark) {
            onClickTxtNewMsgMark();
        } else if (id == C4558R.C4560id.txtMention) {
            onClickTxtMention();
        } else if (id == C4558R.C4560id.txtBottomReplyDown) {
            onClickTxtReplyDown();
        } else if (id == C4558R.C4560id.txtBottomReplyUp) {
            onClickTxtReplyUp();
        } else if (id == C4558R.C4560id.btnPhoneCall) {
            onClickBtnVoiceCall();
        } else if (id == C4558R.C4560id.btnVideoCall) {
            onClickBtnVideoCall();
        }
    }

    public void onClickBtnVoiceCall() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.onClickBtnVoiceCall();
        }
    }

    public void onClickBtnVideoCall() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.onClickBtnVideoCall();
        }
    }

    private void onClickTxtMarkUnread() {
        this.mMessageHelper.jumpToNextMarkUnreadMsg();
        if (this.mMessageHelper.isOldMarkUnreadMessagesEmpty()) {
            this.mTxtMarkUnread.setVisibility(8);
        }
    }

    private void onClickTxtMention() {
        this.mMessageHelper.jumpToNextATMsg();
        if (this.mMessageHelper.isAtMsgEmpty()) {
            this.mTxtMention.setVisibility(8);
        }
    }

    private void onClickTxtNewMsgMark() {
        if (TextUtils.isEmpty(this.mNewMsgMarkId)) {
            this.mTxtNewMsgMark.setVisibility(8);
            return;
        }
        int msgDirection = this.mThreadsRecyclerView.getMsgDirection(this.mNewMsgMarkId);
        if (msgDirection == 0) {
            this.mTxtNewMsgMark.setVisibility(8);
            return;
        }
        if (msgDirection == 2) {
            if (this.mThreadsRecyclerView.isMsgDirty()) {
                this.mThreadsRecyclerView.loadThreads(false, true);
                if (this.mThreadsRecyclerView.isLoading()) {
                    updateRefreshStatus(true);
                }
            } else {
                this.mThreadsRecyclerView.scrollToBottom(true);
            }
        } else if (TextUtils.equals(this.mNewMsgMarkId, MMMessageItem.LAST_MSG_MARK_MSGID) && !this.mThreadsRecyclerView.isMsgDirty()) {
            this.mThreadsRecyclerView.scrollToBottom(true);
        } else if (!this.mThreadsRecyclerView.scrollToMessage(this.mNewMsgMarkId)) {
            this.mThreadsRecyclerView.loadThreads(false, this.mNewMsgMarkId);
            if (this.mThreadsRecyclerView.isLoading()) {
                updateRefreshStatus(true);
            }
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                MMThreadsFragment.this.checkAllShowMsgs();
                MMThreadsFragment.this.updateBottomHint();
            }
        });
        this.mTxtNewMsgMark.setVisibility(8);
        this.mNewMsgMarkId = null;
    }

    private void onClickTxtBottomHint() {
        if (this.mThreadsRecyclerView == null) {
            this.mTxtBottomHint.setVisibility(8);
            return;
        }
        if (!this.mMessageHelper.jumpToFirstUnreadThread4Reply()) {
            if (this.mThreadsRecyclerView.isMsgDirty()) {
                this.mThreadsRecyclerView.loadThreads(false, true);
                if (this.mThreadsRecyclerView.isLoading()) {
                    updateRefreshStatus(true);
                }
            } else {
                this.mThreadsRecyclerView.scrollToBottom(true);
            }
            this.mNewMsgMarkId = null;
            this.mTxtBottomHint.setVisibility(8);
            MessageSyncer.getInstance().cleanUnreadMessageCount(this.mSessionId);
        }
    }

    private void onClickBtnSearch() {
        View view = this.mBtnSearch;
        if (view == null || view.getAlpha() != 1.0f) {
            onClickPanelTitle();
        } else if (PTApp.getInstance().isWebSignedOn()) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                if (this.mSearchFragment == null) {
                    this.mSearchFragment = new IMSessionSearchFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(IMSessionSearchFragment.ARG_CONTENT_MODE, false);
                    bundle.putBoolean(IMSessionSearchFragment.ARG_MESSAGE_FIRST, true);
                    bundle.putString("session_id", this.mSessionId);
                    this.mSearchFragment.setArguments(bundle);
                    beginTransaction.add(C4558R.C4560id.panelSearch, (Fragment) this.mSearchFragment);
                }
                beginTransaction.show(this.mSearchFragment).commit();
                this.mSearchFragment.onShow();
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (MMThreadsFragment.this.isAdded()) {
                            MMThreadsFragment.this.mSearchFragment.openSoftKeyboard();
                        }
                    }
                }, 100);
            }
        }
    }

    private void onClickTxtMsgContextLoadingError() {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.loadThreads(false);
            this.mThreadsRecyclerView.updateUI();
            this.mTxtMsgContextContentLoading.setVisibility(0);
            this.mTxtMsgContextLoadingError.setVisibility(8);
        }
    }

    private void onClickBtnJump() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && !StringUtil.isEmptyOrNull(this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mSessionId);
                if (groupById != null) {
                    String groupID = groupById.getGroupID();
                    if (!StringUtil.isEmptyOrNull(groupID)) {
                        if (!groupById.amIInGroup()) {
                            SimpleMessageDialog.newInstance(C4558R.string.zm_mm_group_removed_by_owner_59554, true).show(getFragmentManager(), "SimpleMessageDialog");
                            zoomMessenger.deleteSession(this.mSessionId);
                            return;
                        }
                        MMChatActivity.showAsGroupChat(zMActivity, groupID);
                    }
                } else {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                    if (buddyWithJID != null) {
                        MMChatActivity.showAsOneToOneChat(zMActivity, buddyWithJID);
                    }
                }
            }
        }
    }

    private void onClickBtnTimedChatHintClose() {
        this.mPanelTimedChatHint.setVisibility(8);
        PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_TIMED_CHAT, true);
    }

    private void onClickBtnE2EHintClose() {
        closeE2EMessageDecryptTimeoutHint();
    }

    private void tryDecryptVisiableE2EMesssageWhenBuddyOnline(List<String> list) {
        if (this.mIsE2EChat && !this.mHasAutoDecryptWhenBuddyOnline && !CollectionsUtil.isListEmpty(list)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null && mMThreadsRecyclerView.hasVisiableMessageDecryptedTimeout()) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (this.mIsGroup) {
                        ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                        if (groupById != null) {
                            List e2EOnLineMembers = groupById.getE2EOnLineMembers();
                            if (!CollectionsUtil.isListEmpty(e2EOnLineMembers)) {
                                boolean z = false;
                                Iterator it = e2EOnLineMembers.iterator();
                                while (true) {
                                    if (it.hasNext()) {
                                        if (list.contains((String) it.next())) {
                                            z = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (!z) {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else if (list.contains(this.mBuddyId)) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                        if (buddyWithJID == null || !buddyWithJID.hasOnlineE2EResource()) {
                            return;
                        }
                    } else {
                        return;
                    }
                    this.mHasAutoDecryptWhenBuddyOnline = true;
                    this.mThreadsRecyclerView.tryDecryptVisiableE2EMesssage();
                }
            }
        }
    }

    public void closeE2EMessageDecryptTimeoutHint() {
        if (this.mE2EHintType != 0) {
            this.mE2EHintType = 3;
            this.mPanelE2EHint.setVisibility(8);
        }
    }

    public void showE2EMessageDecryptTimeoutHint() {
        String str;
        int i = this.mE2EHintType;
        if (3 != i) {
            if (2 == i) {
                this.mTxtE2EHintMsg.setText(C4558R.string.zm_msg_e2e_decrypt_later_12310);
            } else {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (this.mIsGroup) {
                        ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                        if (groupById != null) {
                            List e2EOnLineMembers = groupById.getE2EOnLineMembers();
                            if (e2EOnLineMembers == null || e2EOnLineMembers.size() == 1) {
                                this.mE2EHintType = 1;
                            } else {
                                this.mE2EHintType = 2;
                            }
                            this.mTxtE2EHintMsg.setText(this.mE2EHintType == 2 ? C4558R.string.zm_msg_e2e_decrypt_later_12310 : C4558R.string.zm_msg_e2e_key_time_out_group_59554);
                        } else {
                            return;
                        }
                    } else {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                        if (buddyWithJID != null) {
                            if (buddyWithJID.hasOnlineE2EResource()) {
                                this.mE2EHintType = 2;
                            } else {
                                this.mE2EHintType = 1;
                            }
                            TextView textView = this.mTxtE2EHintMsg;
                            if (this.mE2EHintType == 2) {
                                str = getString(C4558R.string.zm_msg_e2e_decrypt_later_12310);
                            } else {
                                str = getString(C4558R.string.zm_msg_e2e_key_time_out_buddy_12310, getBuddyName());
                            }
                            textView.setText(str);
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
            this.mPanelE2EHint.setVisibility(0);
        }
    }

    private void onClickPanelTitle() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            if (this.mIsGroup) {
                if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                        if (groupById != null && groupById.amIInGroup()) {
                            MMChatInfoActivity.showAsGroupChat((Fragment) this, this.mGroupId, 102);
                        }
                    }
                }
            } else if (!StringUtil.isEmptyOrNull(this.mBuddyId)) {
                MMChatInfoActivity.showAsOneToOneChat(zMActivity, this.mIMAddrBookItem, this.mBuddyId, 102);
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 102 && i2 == -1) {
            if (intent != null) {
                boolean booleanExtra = intent.getBooleanExtra(MMChatInfoActivity.RESULT_ARG_IS_QUIT_GROUP, false);
                boolean booleanExtra2 = intent.getBooleanExtra(MMChatInfoActivity.RESULT_ARG_IS_HISTORY_CLEARED, false);
                if (booleanExtra) {
                    dismiss();
                } else if (booleanExtra2) {
                    this.mMessageHelper.clearAllChatInfo();
                    MessageSyncer.getInstance().cleanUnreadMessageCount(this.mSessionId);
                    MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
                    if (mMThreadsRecyclerView != null) {
                        mMThreadsRecyclerView.loadThreads(false);
                    }
                    updateBottomHint();
                }
            }
        } else if (i == 109 && i2 == -1 && intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String string = extras.getString(ARG_SHARED_MESSAGE_ID);
                if (!StringUtil.isEmptyOrNull(string)) {
                    String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                    if (!StringUtil.isEmptyOrNull(stringExtra)) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(stringExtra);
                        if (arrayList.size() > 0) {
                            doShareFile(arrayList, string);
                        }
                    }
                }
            }
        } else if (i == 114 && i2 == -1 && intent != null) {
            Bundle extras2 = intent.getExtras();
            if (extras2 != null) {
                String string2 = extras2.getString(ARG_FORWARD_MESSAGE_ID);
                if (!StringUtil.isEmptyOrNull(string2)) {
                    String stringExtra2 = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                    if (!StringUtil.isEmptyOrNull(stringExtra2)) {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(stringExtra2);
                        if (arrayList2.size() > 0) {
                            doForwardMessage(arrayList2, string2);
                        }
                    }
                }
            }
        } else if (i == 4001) {
            if (i2 == -1 && intent != null && intent.getExtras() != null) {
                updateEditMsg(intent.getStringExtra("guid"));
            }
        } else if (i == 116) {
            if (intent != null) {
                MMContentMessageAnchorInfo mMContentMessageAnchorInfo = (MMContentMessageAnchorInfo) intent.getSerializableExtra(MMCommentActivity.RESULT_ARG_ANCHOR_MSG);
                if (mMContentMessageAnchorInfo != null) {
                    if (i2 == 0) {
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                            if (sessionById != null) {
                                if (mMContentMessageAnchorInfo.getServerTime() == 0) {
                                    sessionById.unmarkMessageAsUnread(mMContentMessageAnchorInfo.getMsgGuid());
                                } else {
                                    sessionById.unmarkUnreadMessageBySvrTime(mMContentMessageAnchorInfo.getServerTime());
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    if (this.mMessageHelper.refreshMarkUnreadMessage(mMContentMessageAnchorInfo.getThrId())) {
                        updateBottomHint();
                        this.mThreadsRecyclerView.notifyDataSetChanged();
                    }
                }
            }
        } else if (i == 117 && i2 == -1 && intent != null) {
            String stringExtra3 = intent.getStringExtra("threadId");
            if (!TextUtils.isEmpty(stringExtra3)) {
                MMMessageItem findThread = this.mThreadsRecyclerView.findThread(stringExtra3);
                ArrayList arrayList3 = (ArrayList) intent.getSerializableExtra(MMCommentActivity.RESULT_ARG_MARK_UNREAD_MESSAGES);
                if (!CollectionsUtil.isCollectionEmpty(arrayList3)) {
                    this.mMessageHelper.checkMarkUnreadInfo4Reply(arrayList3);
                }
                this.mMessageHelper.clearAllUnreadThreadInfo(stringExtra3);
                if (findThread != null) {
                    findThread.unreadCommentCount = 0;
                    ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger2 != null) {
                        ZoomChatSession sessionById2 = zoomMessenger2.getSessionById(this.mSessionId);
                        ThreadDataProvider threadDataProvider = zoomMessenger2.getThreadDataProvider();
                        if (!(sessionById2 == null || threadDataProvider == null)) {
                            ZoomMessage messageById = sessionById2.getMessageById(stringExtra3);
                            if (messageById != null) {
                                if (this.mAnchorMessageItem == null) {
                                    findThread.markUnreadCommentCount = this.mMessageHelper.getMarkUnreadCountInThread(stringExtra3);
                                }
                                findThread.commentsCount = messageById.getTotalCommentsCount();
                                if (!findThread.isPlayed && messageById.isPlayed()) {
                                    findThread.isPlayed = true;
                                }
                                findThread.draftReply = threadDataProvider.getThreadReplyDraft(this.mSessionId, stringExtra3);
                            }
                        }
                    }
                    this.mThreadsRecyclerView.notifyDataSetChanged();
                }
                updateBottomHint();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateMessage(String str, String str2) {
        if (this.mThreadsRecyclerView != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && TextUtils.equals(this.mSessionId, str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str2);
                    if (messageByXMPPGuid != null) {
                        this.mThreadsRecyclerView.updateMessage(messageByXMPPGuid);
                    }
                }
            }
        }
    }

    private void updateEditMsg(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str);
                if (messageByXMPPGuid != null && this.mThreadsRecyclerView != null) {
                    MMMessageHelper mMMessageHelper = this.mMessageHelper;
                    boolean z = mMMessageHelper != null && mMMessageHelper.checkEditMessage(messageByXMPPGuid);
                    MMMessageItem updateMessage = this.mThreadsRecyclerView.updateMessage(messageByXMPPGuid);
                    if (z && updateMessage == null) {
                        this.mThreadsRecyclerView.notifyDataSetChanged();
                    }
                    if (!this.mIsE2EChat) {
                        LinkPreviewHelper.doCrawLinkPreview(this.mSessionId, messageByXMPPGuid.getMessageXMPPGuid(), messageByXMPPGuid.getBody());
                    }
                }
            }
        }
    }

    public void showTimedChat() {
        Context context = getContext();
        if (context != null) {
            String string = getString(C4558R.string.zm_zoom_change_settings);
            String string2 = getString(C4558R.string.zm_zoom_learn_more);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                LocalStorageTimeInterval localStorageTimeInterval = zoomMessenger.getLocalStorageTimeInterval();
                if (localStorageTimeInterval != null) {
                    MMMessageHelper mMMessageHelper = this.mMessageHelper;
                    String timeInterval = MMMessageHelper.timeInterval(context, localStorageTimeInterval.getYear(), localStorageTimeInterval.getMonth(), localStorageTimeInterval.getDay());
                    if (localStorageTimeInterval.getYear() > 0 || localStorageTimeInterval.getMonth() > 1) {
                        this.mPanelTimedChatHint.setVisibility(8);
                    } else {
                        if (zoomMessenger.editIMSettingGetOption() == 1) {
                            this.txtTimedChatHintMsg.setText(Html.fromHtml(getString(C4558R.string.zm_mm_msg_timed_chat_33479, timeInterval, string, string2)));
                        } else {
                            this.txtTimedChatHintMsg.setText(Html.fromHtml(getString(C4558R.string.zm_mm_msg_timed_chat2_33479, timeInterval, string2)));
                        }
                        this.mPanelTimedChatHint.setVisibility(0);
                    }
                } else {
                    this.mPanelTimedChatHint.setVisibility(8);
                }
            }
            if (PreferenceUtil.readBooleanValue(PreferenceUtil.IM_TIMED_CHAT, true)) {
                this.mPanelTimedChatHint.setVisibility(8);
            }
        }
    }

    private void doShareFile(ArrayList<String> arrayList, String str) {
        MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, null, str, this.mSessionId, null, 0);
    }

    private void doForwardMessage(ArrayList<String> arrayList, String str) {
        if (arrayList != null && !arrayList.isEmpty()) {
            boolean equals = TextUtils.equals((CharSequence) arrayList.get(0), this.mSessionId);
            MMForwardZoomMessageDialogFragment.showForwardMessageDialog(getFragmentManager(), arrayList, str, this.mSessionId, equals ? this : null, equals ? 115 : 0);
        }
    }

    private void showTipInvitationsSent(int i) {
        if (this.mInvitationsTip == null) {
            inflateInvitationsSentView(i);
        } else {
            showInvitationsSentView(i);
        }
    }

    private void inflateInvitationsSentView(final int i) {
        View view = getView();
        if (view != null) {
            ViewStub viewStub = (ViewStub) view.findViewById(C4558R.C4560id.tipsViewStub);
            if (viewStub != null) {
                viewStub.setOnInflateListener(new OnInflateListener() {
                    public void onInflate(ViewStub viewStub, View view) {
                        MMThreadsFragment.this.mInvitationsTip = view;
                        MMThreadsFragment.this.showInvitationsSentView(i);
                    }
                });
                viewStub.inflate();
            }
        }
    }

    /* access modifiers changed from: private */
    public void showInvitationsSentView(int i) {
        NormalMessageTip.show(getFragmentManager(), TipMessageType.TIP_INVITATIONS_SENT.name(), null, getResources().getQuantityString(C4558R.plurals.zm_msg_invitations_sent, i, new Object[]{Integer.valueOf(i)}), C4558R.C4559drawable.zm_ic_tick, 0, 0, 3000);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C279916 r2 = new EventAction("MMChatFragmentPermissionResult") {
            public void run(IUIElement iUIElement) {
                ((MMThreadsFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("MMChatFragmentPermissionResult", r2);
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 107) {
                if (this.mPermissionPenddingMessage != null && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
                    this.mMessageHelper.handleMessageItem(this.mPermissionPenddingMessage);
                }
                this.mPermissionPenddingMessage = null;
            } else if (i == 5001) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                    ImageUtil.saveToGallery(this, this.mSavingFile);
                }
            } else if (i == 6001) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                    ImageUtil.saveToGallery(this, this.mSavingEmoji);
                }
            } else if (i == 5002) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
                    savePicture(this.mSavingMsg);
                }
            } else if (i == 7001 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
                String str = this.mSelectedPhoneNumber;
                if (str != null) {
                    ZMPhoneUtils.callSip(str, null);
                }
                this.mSelectedPhoneNumber = null;
            }
        }
    }

    public Integer getUploadFileRatio(String str) {
        return (Integer) this.mPendingUploadFileRatios.get(str);
    }

    public void addUploadFileRatio(String str, int i) {
        this.mPendingUploadFileRatios.put(str, Integer.valueOf(i));
    }

    private void sendImageOnSignedOn(String str) {
        this.mImageToSendOnSignedOn = str;
        showWaitingDialog();
    }

    /* access modifiers changed from: private */
    public void onBeginConnect() {
        if (isResumed()) {
            updateTitleBar();
            updatePanelConnectionAlert();
        }
    }

    /* access modifiers changed from: private */
    public void onConnectReturn(final int i) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.push(new EventAction() {
                public void run(IUIElement iUIElement) {
                    MMThreadsFragment.this.mThreadsRecyclerView.onConnectReturn(i);
                    MMThreadsFragment.this.updateTitleBar();
                    MMThreadsFragment.this.updatePanelConnectionAlert();
                    MMThreadsFragment.this.refreshGroupInfo();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateOnlineBuddies(List<String> list) {
        if (!(list == null || this.mThreadsRecyclerView == null)) {
            String str = this.mBuddyId;
            if (str != null && list.contains(str)) {
                onIndicateInfoUpdatedWithJID(this.mBuddyId);
            }
        }
        tryDecryptVisiableE2EMesssageWhenBuddyOnline(list);
    }

    public void Indicate_GetContactsPresence(List<String> list, List<String> list2) {
        if (this.mThreadsRecyclerView != null) {
            String str = this.mBuddyId;
            if (str != null) {
                if (list != null && list.contains(str)) {
                    onIndicateInfoUpdatedWithJID(this.mBuddyId);
                } else if (list2 != null && list2.contains(this.mBuddyId)) {
                    onIndicateInfoUpdatedWithJID(this.mBuddyId);
                }
            }
        }
        tryDecryptVisiableE2EMesssageWhenBuddyOnline(list);
    }

    /* access modifiers changed from: private */
    public void onIndicate_BuddyPresenceChanged(String str) {
        onIndicateInfoUpdatedWithJID(str);
        if (TextUtils.equals(str, this.mSessionId)) {
            boolean z = this.mIsE2EChat;
            checkE2EStatus();
            if (z != this.mIsE2EChat) {
                this.mThreadsRecyclerView.setIsE2EChat(z);
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        tryDecryptVisiableE2EMesssageWhenBuddyOnline(arrayList);
    }

    private boolean isBuddyWithJIDInSession(String str) {
        if (str == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (this.mIsGroup) {
            return zoomMessenger.isBuddyWithJIDInGroup(str, this.mSessionId);
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
        if (buddyWithJID == null) {
            return false;
        }
        return StringUtil.isSameString(str, buddyWithJID.getJid());
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        if ((this.mIsGroup || StringUtil.isSameString(str, this.mBuddyId)) && isBuddyWithJIDInSession(str)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.onIndicateInfoUpdatedWithJID(str);
            }
            checkE2EStatus();
            updateTitleBar();
            updateActivatedState();
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(int i, GroupAction groupAction, String str) {
        if (this.mIsGroup) {
            String str2 = this.mSessionId;
            if (str2 != null && str2.equals(groupAction.getGroupId()) && this.mThreadsRecyclerView != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        ZoomMessage messageById = sessionById.getMessageById(str);
                        if (messageById != null) {
                            if (isResumed()) {
                                updateTitleBar();
                            }
                            if (groupAction.getActionType() == 4 && groupAction.isMeInBuddies()) {
                                getNonNullEventTaskManagerOrThrowException().push(new EventAction("removedByOwner") {
                                    public void run(IUIElement iUIElement) {
                                        new Builder(MMThreadsFragment.this.getActivity()).setMessage(C4558R.string.zm_mm_group_removed_by_owner_59554).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                                                if (zoomMessenger != null) {
                                                    zoomMessenger.deleteSession(MMThreadsFragment.this.mSessionId);
                                                    MMThreadsFragment.this.dismiss();
                                                }
                                            }
                                        }).setCancelable(false).show();
                                    }
                                });
                                ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
                            } else if (groupAction.getActionType() != 5 || !groupAction.isActionOwnerMe()) {
                                this.mThreadsRecyclerView.onGroupMessage(i, messageById);
                            } else {
                                dismiss();
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (this.mIsGroup) {
            String str2 = this.mSessionId;
            if (str2 != null && str != null && str2.equals(str) && this.mThreadsRecyclerView != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null && zoomMessenger.getSessionById(this.mSessionId) != null) {
                    if (isResumed()) {
                        updateTitleBar();
                    }
                    if (this.mThreadsRecyclerView.isDataEmpty() && !this.mThreadsRecyclerView.isLoading(1) && !this.mThreadsRecyclerView.isLoading(2)) {
                        this.mThreadsRecyclerView.loadThreads(false);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotifyBuddyJIDUpgrade(final String str, final String str2, final String str3) {
        if (!StringUtil.isEmptyOrNull(str2)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                public void run(IUIElement iUIElement) {
                    ((MMThreadsFragment) iUIElement).handleOnNotifyBuddyJIDUpgrade(str, str2, str3);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleOnNotifyBuddyJIDUpgrade(String str, String str2, String str3) {
        if (this.mIsGroup) {
            if (this.mThreadsRecyclerView.hasMessageFromJid(str2)) {
                this.mThreadsRecyclerView.loadThreads(false);
            }
        } else if (StringUtil.isSameString(this.mBuddyId, str2)) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_ChatSessionUpdate(String str) {
        if (!StringUtil.isEmptyOrNull(this.mSessionId) && this.mSessionId.equals(str)) {
            updateUI();
            updateBlockedState();
            updateActivatedState();
        }
    }

    /* access modifiers changed from: private */
    public void onConfirm_MessageSent(String str, String str2, int i) {
        if (!StringUtil.isEmptyOrNull(this.mSessionId) && this.mSessionId.equals(str) && !StringUtil.isEmptyOrNull(str2)) {
            onMessageSent(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void onConfirmFileDownloaded(String str, String str2, int i) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
            final String str3 = str;
            final String str4 = str2;
            final int i2 = i;
            C280520 r1 = new EventAction("") {
                public void run(IUIElement iUIElement) {
                    MMThreadsFragment mMThreadsFragment = (MMThreadsFragment) iUIElement;
                    if (mMThreadsFragment != null) {
                        mMThreadsFragment.handleOnConfirmFileDownload(str3, str4, i2);
                    }
                }
            };
            nonNullEventTaskManagerOrThrowException.push(r1);
        }
    }

    /* access modifiers changed from: private */
    public void handleOnConfirmFileDownload(String str, String str2, int i) {
        dismissWaitingDownloadDialog(str2);
        this.mMessageHelper.onConfirmFileDownloaded(str, str2, i);
    }

    /* access modifiers changed from: private */
    public void onConfirmPreviewPicFileDownloaded(String str, String str2, int i) {
        if (!StringUtil.isEmptyOrNull(this.mSessionId) && this.mSessionId.equals(str)) {
            if (i == 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str2)) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                    if (sessionById != null) {
                        ZoomMessage messageById = sessionById.getMessageById(str2);
                        if (messageById != null) {
                            this.mThreadsRecyclerView.updateMessage(messageById);
                            if (this.mThreadsRecyclerView.isAtBottom()) {
                                this.mThreadsRecyclerView.scrollToBottom(true);
                            }
                        }
                    }
                }
            } else {
                MMMessageItem itemByMessageId = this.mThreadsRecyclerView.getItemByMessageId(str2);
                if (itemByMessageId != null) {
                    itemByMessageId.isPreviewDownloadFailed = true;
                    itemByMessageId.fileDownloadResultCode = i;
                    if (isResumed()) {
                        this.mThreadsRecyclerView.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean onIndicateMessageReceived(String str, String str2, String str3) {
        if (StringUtil.isEmptyOrNull(this.mSessionId) || !this.mSessionId.equals(str) || StringUtil.isEmptyOrNull(str3)) {
            return false;
        }
        ZoomMessage checkReceivedMessage = this.mMessageHelper.checkReceivedMessage(str3);
        if (checkReceivedMessage == null) {
            return false;
        }
        onReceivedMessage(checkReceivedMessage);
        if (!this.mThreadsRecyclerView.isInAutoScrollBottomMode() || checkReceivedMessage.isComment() || this.mThreadsRecyclerView.isMsgDirty()) {
            updateBottomHint();
        }
        updatePanelNotes();
        if (!checkReceivedMessage.isComment()) {
            showFloatingEmojisIfMatched(str3, checkReceivedMessage.getBody(), checkReceivedMessage.getServerSideTime(), false);
        }
        return false;
    }

    private MMMessageItem onReceivedMessage(ZoomMessage zoomMessage) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView == null) {
            return null;
        }
        return mMThreadsRecyclerView.addNewMessage(zoomMessage, true);
    }

    /* access modifiers changed from: private */
    public void FT_OnSent(String str, String str2, int i) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            this.mPendingUploadFileRatios.remove(str2);
            if (this.mThreadsRecyclerView != null) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null && zMActivity.isActive()) {
                    ZMIMUtils.axAnnounceForAccessibility(this.mThreadsRecyclerView, getString(C4558R.string.zm_msg_file_state_uploaded_69051));
                }
                this.mThreadsRecyclerView.FT_OnSent(str, str2, i);
            }
        }
    }

    private void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
        String str3 = str2;
        String str4 = str;
        if (StringUtil.isSameString(str, this.mSessionId)) {
            if (this.mPendingUploadFileRatios.containsKey(str2)) {
                this.mPendingUploadFileRatios.put(str2, Integer.valueOf(i));
            }
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.FT_OnProgress(str, str2, i, j, j2);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FT_OnPause(ZMFileAction zMFileAction) {
        String sessionID = zMFileAction.getSessionID();
        String messageID = zMFileAction.getMessageID();
        zMFileAction.getAction();
        if (StringUtil.isSameString(sessionID, this.mSessionId)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.FT_OnPause(sessionID, messageID);
            }
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnResumed(String str, String str2, int i) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.FT_OnResumed(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void E2E_MessageStateUpdate(String str, String str2, int i) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mThreadsRecyclerView.e2eMessageStateUpdate(str, str2, i);
            if ((i == 11 || i == 13) && this.mThreadsRecyclerView.isMsgShow(str2)) {
                showE2EMessageDecryptTimeoutHint();
            } else if (this.mE2EHintType != 3 && this.mThreadsRecyclerView.isAllVisiableMessageDecrypted()) {
                closeE2EMessageDecryptTimeoutHint();
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_MessageDeleted(String str, String str2) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.Indicate_MessageDeleted(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.Indicate_FileForwarded(str, str2, str3, str4, i);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileDownloaded(String str, String str2, int i) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.Indicate_FileDownloaded(str, str2, i);
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.FT_OnDownloadByMsgIDTimeOut(str, str2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void FT_UploadFileInChatTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.FT_UploadFileInChatTimeOut(str, str2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersUpdated() {
        updateBlockedState();
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersAdded(List<String> list) {
        if (!StringUtil.isEmptyOrNull(this.mBuddyId) && list.contains(this.mBuddyId)) {
            updateBlockedState();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileMessageDeleted(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mThreadsRecyclerView.Indicate_MessageDeleted(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_SessionOfflineMessageFinished(String str) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mMessageHelper.checkUnreadComments(false);
            this.mMessageHelper.checkAllATMessages();
            updateBottomHint();
        }
    }

    /* access modifiers changed from: private */
    public void Notify_ChatSessionUnreadCountReady(List<String> list) {
        if (this.mAnchorMessageItem == null) {
            int i = 0;
            this.mMessageHelper.checkUnreadComments(false);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    if (list.contains(this.mSessionId)) {
                        i = sessionById.getUnreadMessageCount();
                    }
                    this.mThreadsRecyclerView.setUnreadMsgInfo(i, sessionById.getReadedMsgTime());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void On_BroadcastUpdate(int i, String str, boolean z) {
        if (StringUtil.isSameString(this.mGroupId, str)) {
            if (z) {
                this.mTxtAnnouncement.setVisibility(8);
                MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
                if (mMChatInputFragment == null || !mMChatInputFragment.isAdded()) {
                    initInputFragment(this.mGroupId);
                }
            } else {
                this.mTxtAnnouncement.setVisibility(0);
                MMChatInputFragment mMChatInputFragment2 = this.mChatInputFragment;
                if (mMChatInputFragment2 != null && mMChatInputFragment2.isAdded()) {
                    this.mChatInputFragment.dismiss();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersRemoved(List<String> list) {
        if (!StringUtil.isEmptyOrNull(this.mBuddyId) && list.contains(this.mBuddyId)) {
            updateBlockedState();
        }
    }

    /* access modifiers changed from: private */
    public void On_DestroyGroup(final int i, String str, final String str2, String str3, long j) {
        if (StringUtil.isSameString(str2, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("DestroyGroup") {
                public void run(IUIElement iUIElement) {
                    if (StringUtil.isSameString(str2, MMThreadsFragment.this.mGroupId) && i == 0) {
                        MMThreadsFragment.this.dismiss();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("NotifyGroupDestroy") {
                public void run(IUIElement iUIElement) {
                    MMThreadsFragment.this.dismiss();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, long j2, boolean z) {
        String str5 = str2;
        if (StringUtil.isSameString(str2, this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    if (z) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
                        if (sessionById != null) {
                            String str6 = str4;
                            ZoomMessage messageById = sessionById.getMessageById(str4);
                            this.mMessageHelper.onRecallMessage(str3, j, j2);
                            this.mThreadsRecyclerView.onRecallMessage(true, messageById, str3, j2);
                            updateBottomHint();
                        }
                    } else {
                        String str7 = str;
                        if (StringUtil.isSameString(myself.getJid(), str)) {
                            getNonNullEventTaskManagerOrThrowException().push(new EventAction("RevokeMessageFailed") {
                                public void run(IUIElement iUIElement) {
                                    SimpleMessageDialog.newInstance(C4558R.string.zm_mm_lbl_delete_failed_64189).show(MMThreadsFragment.this.getFragmentManager(), "RevokeMessage");
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_SendAddonCommandResultIml(String str, boolean z) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.Indicate_SendAddonCommandResultIml(str, z);
        }
    }

    /* access modifiers changed from: private */
    public void notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
        boolean z = false;
        if (sessionMessageInfoMap != null && sessionMessageInfoMap.getInfosCount() > 0) {
            for (SessionMessageInfo session : sessionMessageInfoMap.getInfosList()) {
                if (TextUtils.equals(session.getSession(), this.mSessionId)) {
                    MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
                    if (mMThreadsRecyclerView != null) {
                        mMThreadsRecyclerView.updateVisibleMessageState();
                    }
                    checkMarkUnreadMsg();
                    z = true;
                }
            }
        }
        if (!z && this.mMessageHelper.getOldMarkUnreadMsgCount() > 0) {
            checkMarkUnreadMsg();
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_SessionMarkUnreadCtx(String str, int i, String str2, List<String> list) {
        if (this.mThreadsRecyclerView != null && TextUtils.equals(str2, this.mSessionId) && this.mAnchorMessageItem != null) {
            if (i != 0) {
                onMessageContextLoadFailed();
            } else if (list == null || list.size() <= 0 || !this.mThreadsRecyclerView.containAnchorMessage(list)) {
                if (this.mAnchorMessageItem != null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
                        if (sessionById != null) {
                            sessionById.clearAllMarkedUnreadMessage();
                        }
                    }
                }
                if (getActivity() != null) {
                    ErrorMsgDialog.newInstance(getString(C4558R.string.zm_lbl_search_result_empty), 1, true, 1000).show(getActivity().getSupportFragmentManager(), "onNotify_SessionMarkUnreadCtx");
                }
            } else if (this.mThreadsRecyclerView.Indicate_MarkUnreadContext(str, i, str2, list)) {
                this.mPanelMsgContextEmptyView.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
        if (StringUtil.isSameString(str2, this.mSessionId)) {
            updateEditMsg(str3);
            updateBottomHint();
        }
    }

    public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
        if (list != null && list.contains(this.mSessionId)) {
            this.mThreadsRecyclerView.NotifyOutdatedHistoryRemoved(j);
        }
    }

    public void onSayHi() {
        updateSayHiStatus();
        this.mChatInputFragment.sendText(getString(C4558R.string.zm_lbl_message_body_say_hi_79032), null, SendMsgType.MESSAGE);
    }

    public void updateSayHiStatus() {
        if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
            StringBuilder sb = new StringBuilder();
            sb.append(PreferenceUtil.FTE_CHAT_SESSION_SAY_HI);
            sb.append(this.mSessionId);
            if (!PreferenceUtil.readSayHiFTE(sb.toString(), false)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(PreferenceUtil.FTE_CHAT_SESSION_SAY_HI);
                sb2.append(this.mSessionId);
                PreferenceUtil.saveSayHiFTE(sb2.toString(), true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTempleActionMsg(String str, String str2, String str3, String str4, String str5) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
            if (zoomMessageTemplate != null) {
                zoomMessageTemplate.sendButtonCommand(str, str2, str3, str4, str5);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleActionMsg(String str, String str2) {
        this.mChatInputFragment.handleActionMsg(str, str2);
    }

    private void updateBlockedState() {
        if (!this.mIsGroup && !StringUtil.isEmptyOrNull(this.mBuddyId)) {
            updateTitleBar();
            updateDisableMsg();
        }
    }

    private void updateActivatedState() {
        if (!this.mIsGroup && !StringUtil.isEmptyOrNull(this.mBuddyId)) {
            updateTitleBar();
            updateDisableMsg();
        }
    }

    private void updateDisableMsg() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
            if (buddyWithJID != null) {
                boolean blockUserIsBlocked = zoomMessenger.blockUserIsBlocked(this.mBuddyId);
                if (buddyWithJID.getAccountStatus() == 1) {
                    this.mTxtDisableMsg.setText(getString(C4558R.string.zm_lbl_deactivated_by_their_account_admin_62074, getTitle()));
                    this.mTxtDisableMsg.setVisibility(0);
                } else if (blockUserIsBlocked) {
                    this.mTxtDisableMsg.setText(getString(C4558R.string.zm_msg_buddy_blocked_13433, getTitle()));
                    this.mTxtDisableMsg.setVisibility(0);
                } else {
                    this.mTxtDisableMsg.setVisibility(8);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnSendPrivateSticker(String str, int i, String str2, String str3) {
        if (i == 0 && StringUtil.isSameString(str2, this.mSessionId)) {
            onMessageSent(str2, str3);
        }
    }

    /* access modifiers changed from: private */
    public void OnSendStickerMsgAppended(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            onMessageSent(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void OnLinkCrawlResult(CrawlLinkResponse crawlLinkResponse) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.OnLinkCrawlResult(crawlLinkResponse);
        }
    }

    /* access modifiers changed from: private */
    public void OnDownloadFavicon(int i, String str) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.OnDownloadFavicon(i, str);
        }
    }

    /* access modifiers changed from: private */
    public void OnDownloadImage(int i, String str) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.OnDownloadImage(i, str);
        }
    }

    /* access modifiers changed from: private */
    public void OnUnsupportMessageRecevied(int i, String str, String str2, String str3) {
        if (i == 0 && TextUtils.equals(str2, this.mSessionId)) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.OnUnsupportMessageRecevied(i, str, str2, str3);
            }
        }
    }

    public void onMessageContextLoadFailed() {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null && mMThreadsRecyclerView.getAdapter() != null) {
            if (this.mThreadsRecyclerView.getAdapter().getItemCount() > 0) {
                this.mPanelMsgContextEmptyView.setVisibility(8);
                Toast.makeText(getActivity(), C4558R.string.zm_alert_msg_context_failed, 1).show();
            } else {
                this.mPanelMsgContextEmptyView.setVisibility(0);
                this.mTxtMsgContextLoadingError.setVisibility(0);
                this.mTxtMsgContextContentLoading.setVisibility(8);
            }
        }
    }

    public void onStartToDownloadFileForMessage(MMMessageItem mMMessageItem, boolean z) {
        if (!z) {
            showWaitingDownloadDialog(mMMessageItem.messageId);
        }
    }

    public void onWebLogin() {
        updateUI();
        if (this.mImageToSendOnSignedOn != null) {
            dismissWaitingDialog();
            this.mChatInputFragment.sendImage(this.mImageToSendOnSignedOn);
        }
        this.mImageToSendOnSignedOn = null;
    }

    private void showWaitingDownloadDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ProgressDialog progressDialog = this.mWaitingDialog;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            this.mWaitingDialog = new ProgressDialog(activity);
            this.mWaitingDialog.requestWindowFeature(1);
            this.mWaitingDialog.setMessage(activity.getString(C4558R.string.zm_msg_waiting));
            this.mWaitingDialog.setCanceledOnTouchOutside(false);
            this.mWaitingDialog.setCancelable(true);
            this.mWaitingDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    MMThreadsFragment.this.mMessageHelper.cancelPendingPlay();
                    MMThreadsFragment.this.mWaitingDialogId = null;
                    MMThreadsFragment.this.mWaitingDialog = null;
                }
            });
            this.mWaitingDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    MMThreadsFragment.this.mWaitingDialogId = null;
                    MMThreadsFragment.this.mWaitingDialog = null;
                }
            });
            this.mWaitingDialogId = str;
            this.mWaitingDialog.show();
        }
    }

    private void dismissWaitingDownloadDialog(String str) {
        if (this.mWaitingDialog != null && StringUtil.isSameString(this.mWaitingDialogId, str)) {
            this.mWaitingDialog.dismiss();
        }
    }

    public void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "WaitingDialog");
        }
    }

    public void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void onUnSupportEmojiReceived(String str) {
        if (!StringUtil.isEmptyOrNull(str) && !CommonEmojiHelper.getInstance().isEmojiInstalled()) {
            FragmentActivity activity = getActivity();
            if (activity != null && UnSupportEmojiDialog.getUnSupportEmojiDialog((ZMActivity) activity) == null) {
                this.mHandler.removeCallbacks(this.mUnSupportEmojiRunnable);
                this.mHandler.postDelayed(this.mUnSupportEmojiRunnable, 100);
            }
        }
    }

    public void onContactsCacheUpdated() {
        updateContact();
        updateTitleBar();
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            mMThreadsRecyclerView.notifyDataSetChanged();
        }
    }

    private void onClickBtnInviteE2EChat() {
        if (!this.mIsGroup) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    zoomMessenger.sendE2EFTEInvite(this.mBuddyId, getString(C4558R.string.zm_msg_e2e_get_invite, myself.getScreenName()), getString(C4558R.string.zm_msg_e2e_get_invite_for_old_client, myself.getScreenName()));
                }
            }
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        checkJumpSuccessWhenExit();
        finishFragment(true);
    }

    private void checkJumpSuccessWhenExit() {
        if (this.mAnchorMessageItem != null) {
            Intent intent = new Intent();
            intent.putExtra(MMCommentActivity.RESULT_ARG_ANCHOR_MSG, this.mAnchorMessageItem);
            int i = -1;
            if (this.mThreadsRecyclerView.isDataEmpty()) {
                i = 1;
            } else if (this.mThreadsRecyclerView.findMessageItemByStamp(this.mAnchorMessageItem.getServerTime()) == null) {
                i = 0;
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.setResult(i, intent);
            }
        }
    }

    private void checkMarkUnreadMsg() {
        this.mMessageHelper.checkOldMarkUnreadMsg();
    }

    private void updatePanelNotes() {
        if (!this.mIsMyNostes) {
            this.mPanelNotes.setVisibility(8);
        } else if (this.mThreadsRecyclerView != null) {
            boolean readBooleanValue = PreferenceUtil.readBooleanValue(PreferenceUtil.FIRST_SENT_MY_NOTES, false);
            if (readBooleanValue || !this.mThreadsRecyclerView.isDataEmpty()) {
                this.mPanelNotes.setVisibility(8);
                if (!readBooleanValue) {
                    PreferenceUtil.saveBooleanValue(PreferenceUtil.FIRST_SENT_MY_NOTES, true);
                    return;
                }
                return;
            }
            this.mPanelNotes.setVisibility(0);
        }
    }

    public void updateUI() {
        updateContact();
        updatePanelNotes();
        updateTitleBar();
        updatePanelConnectionAlert();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                if (sessionById.getUnreadMessageCount() > 0) {
                    NotificationMgr.removeMessageNotificationMM(getActivity(), this.mSessionId);
                }
                this.mThreadsRecyclerView.notifyDataSetChanged();
                updateBottomHint();
            }
        }
    }

    private void updateMessageListView(boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.getSessionById(this.mSessionId) != null) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.loadThreads(z);
                this.mThreadsRecyclerView.updateUI();
                updateBottomHint();
            }
        }
    }

    public void onNewMsgIdReady(String str) {
        this.mNewMsgMarkId = str;
        updateBottomHint();
    }

    public void setHistoryRefreshing() {
        updateRefreshStatus(true);
    }

    public void updateBottomHint() {
        boolean z;
        String str;
        if (isAdded() && this.mThreadsRecyclerView.isLayoutReady() && ((!this.mThreadsRecyclerView.isDataEmpty() || this.mThreadSortType != 0) && !this.mThreadsRecyclerView.isLoading(2) && !this.mThreadsRecyclerView.isLoading(1))) {
            Context context = getContext();
            if (context != null) {
                if (this.mAnchorMessageItem != null || this.mThreadsRecyclerView == null) {
                    this.mPanelBottomHint.setVisibility(8);
                    return;
                }
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        this.mPanelBottomHint.setVisibility(0);
                        int firstMarkUnreadMessage = this.mMessageHelper.getFirstMarkUnreadMessage();
                        if (firstMarkUnreadMessage == 0) {
                            this.mTxtMarkUnread.setVisibility(8);
                        } else {
                            this.mTxtMarkUnread.setVisibility(0);
                            if (firstMarkUnreadMessage == 2) {
                                this.mTxtMarkUnread.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_white_down), null);
                            } else {
                                this.mTxtMarkUnread.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_white_up), null);
                            }
                            int oldMarkUnreadMsgCount = this.mMessageHelper.getOldMarkUnreadMsgCount();
                            this.mTxtMarkUnread.setText(getResources().getQuantityString(C4558R.plurals.zm_lbl_mark_unread_150170, oldMarkUnreadMsgCount, new Object[]{Integer.valueOf(oldMarkUnreadMsgCount)}));
                        }
                        String firstUnVisableATMessage = this.mMessageHelper.getFirstUnVisableATMessage();
                        if (TextUtils.isEmpty(firstUnVisableATMessage)) {
                            this.mTxtMention.setVisibility(8);
                            z = false;
                        } else {
                            this.mTxtMention.setVisibility(0);
                            if (this.mThreadsRecyclerView.getMsgDirection(firstUnVisableATMessage) == 2) {
                                this.mTxtMention.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_white_down), null);
                            } else {
                                this.mTxtMention.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_white_up), null);
                            }
                            int atMsgCount = this.mMessageHelper.getAtMsgCount();
                            this.mTxtMention.setText(getResources().getQuantityString(this.mMessageHelper.hasMessageAtMe() ? C4558R.plurals.zm_lbl_message_at_me_150170 : C4558R.plurals.zm_lbl_message_at_all_150170, atMsgCount, new Object[]{Integer.valueOf(atMsgCount)}));
                            z = true;
                        }
                        if (!z) {
                            int unreadMessageCount = this.mMessageHelper.getUnreadMessageCount(sessionById);
                            boolean z2 = !this.mThreadsRecyclerView.isMsgDirty() && this.mThreadsRecyclerView.isInAutoScrollBottomMode();
                            if (unreadMessageCount == 0 || (z2 && (this.mThreadSortType == 0 || this.mMessageHelper.getUnreadMessageCount(sessionById, true) == unreadMessageCount))) {
                                this.mTxtBottomHint.setVisibility(8);
                            } else if (this.mThreadSortType != 0 || !this.mThreadsRecyclerView.isAtBottom() || this.mThreadsRecyclerView.isMsgDirty()) {
                                TextView textView = this.mTxtBottomHint;
                                Resources resources = getResources();
                                int i = C4558R.plurals.zm_lbl_new_unread_thread_88133;
                                Object[] objArr = new Object[1];
                                if (unreadMessageCount > 99) {
                                    str = "99+";
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(unreadMessageCount);
                                    sb.append("");
                                    str = sb.toString();
                                }
                                objArr[0] = str;
                                textView.setText(resources.getQuantityString(i, unreadMessageCount, objArr));
                                this.mTxtBottomHint.setVisibility(0);
                                if (this.mMessageHelper.isFirstUnreadThreadAtTop()) {
                                    this.mTxtBottomHint.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_white_up), null);
                                } else {
                                    this.mTxtBottomHint.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_white_down), null);
                                }
                                z = true;
                            } else {
                                this.mMessageHelper.setAllCommentStateAsRead();
                                MessageSyncer.getInstance().cleanUnreadMessageCount(this.mSessionId);
                                this.mTxtBottomHint.setVisibility(8);
                            }
                        } else {
                            this.mTxtBottomHint.setVisibility(8);
                        }
                        if (!z) {
                            if (TextUtils.equals(MMMessageItem.LAST_MSG_MARK_MSGID, this.mNewMsgMarkId) && this.mThreadsRecyclerView.isAtBottom() && !this.mThreadsRecyclerView.isMsgDirty()) {
                                this.mNewMsgMarkId = null;
                            }
                            if (!this.mThreadsRecyclerView.hasUnreadMark()) {
                                this.mNewMsgMarkId = null;
                            }
                            if (TextUtils.isEmpty(this.mNewMsgMarkId)) {
                                this.mTxtNewMsgMark.setVisibility(8);
                            } else {
                                int msgDirection = this.mThreadsRecyclerView.getMsgDirection(this.mNewMsgMarkId);
                                if (msgDirection == -1) {
                                    msgDirection = TextUtils.equals(this.mNewMsgMarkId, MMMessageItem.NEW_MSG_MARK_MSGID) ? 1 : 2;
                                }
                                if (msgDirection == 1) {
                                    this.mTxtNewMsgMark.setVisibility(0);
                                    this.mTxtNewMsgMark.setText(C4558R.string.zm_lbl_jump_first_68444);
                                    this.mTxtNewMsgMark.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_red_up), null);
                                } else if (msgDirection == 2) {
                                    this.mTxtNewMsgMark.setVisibility(0);
                                    this.mTxtNewMsgMark.setText(C4558R.string.zm_lbl_jump_latest_68444);
                                    this.mTxtNewMsgMark.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_ic_arrow_red_down), null);
                                } else {
                                    this.mTxtNewMsgMark.setVisibility(8);
                                }
                            }
                        } else {
                            this.mTxtNewMsgMark.setVisibility(8);
                        }
                    }
                }
            }
        }
    }

    private void startToListenNetworkEvent() {
        if (this.mNetworkStateReceiver == null) {
            this.mNetworkStateReceiver = new SimplePTUIListener() {
                public void onDataNetworkStatusChanged(boolean z) {
                    MMThreadsFragment.this.updatePanelConnectionAlert();
                }
            };
            PTUI.getInstance().addPTUIListener(this.mNetworkStateReceiver);
        }
    }

    private void stopToListenNetworkEvent() {
        if (this.mNetworkStateReceiver != null) {
            PTUI.getInstance().removePTUIListener(this.mNetworkStateReceiver);
            this.mNetworkStateReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void updatePanelConnectionAlert() {
        if (PTApp.getInstance().hasZoomMessenger() && !NetworkUtil.hasDataNetwork(getActivity())) {
            hiddenServerErrorMsg();
        }
    }

    /* access modifiers changed from: private */
    public void updateTitleBar() {
        boolean z;
        boolean z2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        int i = 8;
        if (this.mIsGroup || this.mIsMyNostes) {
            this.mImgPresence.setVisibility(8);
        } else if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
            if (buddyWithJID != null) {
                this.mImgPresence.setState(IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                this.mImgPresence.setmTxtDeviceTypeGone();
                updatePresenceForSharingScreen();
            }
        }
        boolean z3 = zoomMessenger == null || zoomMessenger.imChatGetOption() == 2;
        boolean z4 = zoomMessenger == null || zoomMessenger.e2eGetMyOption() == 2;
        if (!PTApp.getInstance().hasZoomMessenger() || z3 || z4 || isRobot()) {
            this.mBtnSearch.setVisibility(8);
        }
        if (this.mIsMyNostes) {
            TextView textView = this.mTxtTitle;
            if (textView != null) {
                textView.setEllipsize(TruncateAt.MIDDLE);
            }
        } else {
            TextView textView2 = this.mTxtTitle;
            if (textView2 != null) {
                textView2.setEllipsize(TruncateAt.END);
            }
        }
        switch (ZoomMessengerUI.getInstance().getConnectionStatus()) {
            case -1:
            case 0:
            case 1:
                TextView textView3 = this.mTxtTitle;
                if (textView3 != null) {
                    textView3.setEllipsize(TruncateAt.END);
                    String title = getTitle();
                    if (zoomMessenger != null) {
                        ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                        if (buddyWithJID2 != null) {
                            if (buddyWithJID2.getAccountStatus() == 1) {
                                title = getString(C4558R.string.zm_lbl_deactivated_62074, title);
                                this.mTxtTitle.setEllipsize(TruncateAt.MIDDLE);
                            } else if (buddyWithJID2.getAccountStatus() == 2) {
                                title = getString(C4558R.string.zm_lbl_terminated_62074, title);
                                this.mTxtTitle.setEllipsize(TruncateAt.MIDDLE);
                            }
                        }
                    }
                    this.mTxtTitle.setText(title);
                    break;
                }
                break;
            case 2:
                TextView textView4 = this.mTxtTitle;
                if (textView4 != null) {
                    textView4.setText(C4558R.string.zm_mm_title_chats_connecting);
                    break;
                }
                break;
        }
        TextView textView5 = this.mTxtTitle;
        if (textView5 != null) {
            textView5.requestLayout();
        }
        View view = this.mPanelTitleCenter;
        if (view != null) {
            MMThreadsFragment mMThreadsFragment = null;
            if (this.mAnchorMessageItem != null) {
                view.setOnClickListener(null);
                this.mBtnVideoCall.setVisibility(8);
                this.mBtnPhoneCall.setVisibility(8);
            } else if (!this.mIsGroup) {
                if (zoomMessenger != null) {
                    boolean blockUserIsBlocked = zoomMessenger.blockUserIsBlocked(this.mBuddyId);
                    ZoomBuddy buddyWithJID3 = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                    if (buddyWithJID3 != null) {
                        z2 = (buddyWithJID3.getAccountStatus() == 1) || (buddyWithJID3.getAccountStatus() == 2);
                    } else {
                        z2 = false;
                    }
                    z = blockUserIsBlocked || z2;
                } else {
                    z = false;
                }
                this.mBtnVideoCall.setVisibility((this.mIsMyNostes || this.mIsRobot || z) ? 8 : 0);
                View view2 = this.mBtnPhoneCall;
                if (!this.mIsMyNostes && !this.mIsRobot && !z) {
                    i = 0;
                }
                view2.setVisibility(i);
                View view3 = this.mPanelTitleCenter;
                IMAddrBookItem iMAddrBookItem = this.mIMAddrBookItem;
                view3.setOnClickListener((iMAddrBookItem == null || iMAddrBookItem.isZoomRoomContact()) ? null : this);
                View view4 = this.mPanelTitleCenter;
                view4.setContentDescription(view4.getResources().getString(C4558R.string.zm_description_mm_btn_muc_message_options_108993, new Object[]{getBuddyName()}));
                if (this.mIsMyNostes) {
                    this.mTxtTitle.setCompoundDrawables(null, null, null, null);
                }
            } else if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    if (!groupById.amIInGroup() || isAnnouncement()) {
                        this.mBtnVideoCall.setVisibility(8);
                        this.mBtnPhoneCall.setVisibility(8);
                    } else {
                        this.mBtnVideoCall.setVisibility(0);
                        this.mBtnPhoneCall.setVisibility(0);
                        View view5 = this.mPanelTitleCenter;
                        view5.setContentDescription(view5.getResources().getString(groupById.isRoom() ? C4558R.string.zm_description_mm_btn_channel_message_options_108993 : C4558R.string.zm_description_mm_btn_muc_message_options_108993, new Object[]{getGroupTitle()}));
                    }
                    View view6 = this.mPanelTitleCenter;
                    if (groupById.amIInGroup()) {
                        mMThreadsFragment = this;
                    }
                    view6.setOnClickListener(mMThreadsFragment);
                }
            }
        }
    }

    public void updatePresenceForSharingScreen() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
            if (buddyWithJID != null) {
                if (!zoomMessenger.isConnectionGood()) {
                    this.mSharingScreenStatusView.setVisibility(8);
                } else if (buddyWithJID.getPresence() == 2 && buddyWithJID.getPresenceStatus() == 4) {
                    this.mSharingScreenStatusView.setVisibility(0);
                } else {
                    this.mSharingScreenStatusView.setVisibility(8);
                }
            }
        }
    }

    public String getBuddyName() {
        if (this.mIsGroup) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
            if (buddyWithJID != null) {
                this.mBuddyName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, this.mIMAddrBookItem);
            } else {
                IMAddrBookItem iMAddrBookItem = this.mIMAddrBookItem;
                if (iMAddrBookItem != null) {
                    this.mBuddyName = iMAddrBookItem.getScreenName();
                }
            }
        }
        String str = this.mBuddyName;
        if (str == null) {
            str = this.mBuddyPhoneNumber;
            if (str == null) {
                str = null;
            }
        }
        IMAddrBookItem iMAddrBookItem2 = this.mIMAddrBookItem;
        String string = (iMAddrBookItem2 == null || !iMAddrBookItem2.isZoomRoomContact()) ? str : getString(C4558R.string.zm_title_zoom_room_prex);
        if (!this.mIsMyNostes) {
            return string;
        }
        return getString(C4558R.string.zm_mm_msg_my_notes_65147, this.mBuddyName);
    }

    public String getTitle() {
        String str;
        if (!isAdded()) {
            return "";
        }
        if (this.mIsGroup) {
            str = getGroupTitle();
        } else {
            str = getBuddyName();
        }
        return str;
    }

    private String getGroupTitle() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return "";
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
        if (groupById == null) {
            return "";
        }
        String groupName = groupById.getGroupName();
        if (StringUtil.isEmptyOrNull(groupName)) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                groupName = groupById.getGroupDisplayName(activity);
            }
        }
        return groupName;
    }

    private String getSessionDataPath() {
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

    /* access modifiers changed from: private */
    public void loadHistoryThreads() {
        if (this.mThreadsRecyclerView.loadMoreThreads(1)) {
            this.mSwipeRefreshLayout.setEnabled(false);
            this.mThreadsRecyclerView.insertTimedChatMsg();
        } else if (!this.mThreadsRecyclerView.isLoading(1)) {
            updateRefreshStatus(false);
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

    private void notifyOpenRobotChatSession() {
        if (isRobot()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.notifyOpenRobotChatSession(this.mSessionId);
            }
        }
    }

    private void checkE2EStatus() {
        if (!this.mIsMyNostes) {
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
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    this.mIsE2EChat = groupById.isForceE2EGroup();
                }
            } else {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                if (buddyWithJID != null) {
                    if (buddyWithJID.getE2EAbility(e2eGetMyOption) == 2) {
                        z = true;
                    }
                    this.mIsE2EChat = z;
                }
            }
        }
    }

    public void hiddenServerErrorMsg() {
        this.mPanelServerError.setVisibility(8);
        Runnable runnable = this.mServerErrorAutoHiddenTask;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mServerErrorAutoHiddenTask = null;
        }
    }

    private void showChatWarnMsg(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mTxtWarnMsg.setText(str);
            this.mPanelWarnMsg.setVisibility(0);
            Runnable runnable = this.mChatWarnMsgAutoHiddenTask;
            if (runnable != null) {
                this.mHandler.removeCallbacks(runnable);
            }
            this.mChatWarnMsgAutoHiddenTask = new Runnable() {
                public void run() {
                    MMThreadsFragment.this.mPanelWarnMsg.setVisibility(8);
                    MMThreadsFragment.this.mChatWarnMsgAutoHiddenTask = null;
                }
            };
            this.mHandler.postDelayed(this.mChatWarnMsgAutoHiddenTask, 5000);
        }
    }

    private void updateContact() {
        if (!this.mIsGroup && !StringUtil.isEmptyOrNull(this.mBuddyId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mBuddyId);
                if (buddyWithJID != null) {
                    this.mIMAddrBookItem = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                }
            }
        }
    }

    private void initInputFragment(String str) {
        if (isAnnouncement() && !isAdmin()) {
            this.mTxtAnnouncement.setVisibility(0);
        } else if (!TextUtils.isEmpty(str)) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                this.mChatInputFragment = new MMChatInputFragment();
                this.mChatInputFragment.setOnChatInputListener(this);
                this.mChatInputFragment.setKeyboardDetector(this.mKeyboardDetector);
                Bundle bundle = new Bundle();
                bundle.putString("sessionId", str);
                bundle.putBoolean(MMChatInputFragment.ARG_ANNOUNCEMENT, isAnnouncement());
                this.mChatInputFragment.setArguments(bundle);
                beginTransaction.add(C4558R.C4560id.panelActions, (Fragment) this.mChatInputFragment);
                beginTransaction.commit();
            }
        }
    }

    private boolean isAnnouncement() {
        if (!this.mIsGroup) {
            return false;
        }
        return ZMIMUtils.isAnnouncement(this.mGroupId);
    }

    private boolean isAdmin() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mGroupId);
        if (sessionById != null && sessionById.isGroup()) {
            ZoomGroup sessionGroup = sessionById.getSessionGroup();
            if (sessionGroup != null) {
                String groupOwner = sessionGroup.getGroupOwner();
                List groupAdmins = sessionGroup.getGroupAdmins();
                if (StringUtil.isSameString(groupOwner, myself.getJid()) || (!CollectionsUtil.isListEmpty(groupAdmins) && groupAdmins.contains(myself.getJid()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void shareMessage(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SHARED_MESSAGE_ID, str);
        MMSelectSessionAndBuddyFragment.showAsFragment(this, bundle, false, false, 109);
    }

    public void forwardMessage(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FORWARD_MESSAGE_ID, str);
        MMSelectSessionAndBuddyFragment.showAsFragment(this, bundle, false, false, 114);
    }

    public void editMessage(MMMessageItem mMMessageItem) {
        MMEditMessageFragment.showAsFragment(this, this.mSessionId, mMMessageItem.messageXMPPId, 4001);
    }

    public void editTemplateMessage(String str, String str2, String str3) {
        MMEditTemplateFragment.showAsFragment(this, this.mSessionId, str, str2, str3, 4001);
    }

    public boolean onBackPressed() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment == null || !mMChatInputFragment.isAdded()) {
            return false;
        }
        if (this.mChatInputFragment.onBackPressed()) {
            return true;
        }
        IMSessionSearchFragment iMSessionSearchFragment = this.mSearchFragment;
        if (iMSessionSearchFragment != null && iMSessionSearchFragment.isVisible()) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().hide(this.mSearchFragment).commit();
                return true;
            }
        }
        checkJumpSuccessWhenExit();
        return false;
    }

    private void requireCommentOnThread(MMMessageItem mMMessageItem, boolean z) {
        if (mMMessageItem != null && mMMessageItem.isThread) {
            ThreadUnreadInfo threadUnreadInfo = new ThreadUnreadInfo();
            threadUnreadInfo.mAtAllMsgIds = this.mMessageHelper.getAtMsgs(mMMessageItem.messageId, false);
            threadUnreadInfo.mAtMsgIds = this.mMessageHelper.getAllAtMsgs(mMMessageItem.messageId);
            threadUnreadInfo.mMarkUnreadMsgs = this.mMessageHelper.getAllMarkUnreadInThread(mMMessageItem.messageId);
            threadUnreadInfo.mAtMeMsgIds = this.mMessageHelper.getAtMsgs(mMMessageItem.messageId, true);
            threadUnreadInfo.autoOpenKeyboard = z;
            ThrCommentState unreadCommentState = this.mMessageHelper.getUnreadCommentState(mMMessageItem.serverSideTime);
            if (unreadCommentState != null) {
                threadUnreadInfo.readTime = unreadCommentState.unreadSvr;
                threadUnreadInfo.unreadCount = unreadCommentState.getAllUnreadCount();
            }
            if (this.mIsGroup) {
                MMCommentActivity.showAsGroupChat(this, this.mGroupId, mMMessageItem.messageId, null, threadUnreadInfo, 117);
            } else {
                MMCommentActivity.showAsOneToOneChat((Fragment) this, this.mIMAddrBookItem, this.mBuddyId, mMMessageItem.messageId, threadUnreadInfo, 117);
            }
        }
    }

    private IMAddrBookItem getIMAddrBookItemFromMMMessageItem(MMMessageItem mMMessageItem) {
        if (mMMessageItem == null) {
            return null;
        }
        IMAddrBookItem iMAddrBookItem = mMMessageItem.fromContact;
        if (iMAddrBookItem == null && mMMessageItem.fromJid != null) {
            iMAddrBookItem = ZMBuddySyncInstance.getInsatance().getBuddyByJid(mMMessageItem.fromJid, true);
        }
        return iMAddrBookItem;
    }

    /* access modifiers changed from: private */
    public void OnGetThreadData(ThreadDataResult threadDataResult) {
        if (threadDataResult != null && TextUtils.equals(threadDataResult.getChannelId(), this.mSessionId) && this.mThreadsRecyclerView.OnGetThreadData(threadDataResult)) {
            if (!threadDataResult.getStartThrRedirecte() || StringUtil.isEmptyOrNull(threadDataResult.getNewStartThr())) {
                updateRefreshStatus(this.mThreadsRecyclerView.isLoading(threadDataResult.getDir()));
                this.mHandler.post(new Runnable() {
                    public void run() {
                        MMThreadsFragment.this.updateBottomHint();
                    }
                });
                updatePanelNotes();
            } else {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        String newStartThr = threadDataResult.getNewStartThr();
                        ZoomMessage messageById = sessionById.getMessageById(newStartThr);
                        if (messageById != null) {
                            MMContentMessageAnchorInfo mMContentMessageAnchorInfo = new MMContentMessageAnchorInfo();
                            mMContentMessageAnchorInfo.setThrSvr(messageById.getServerSideTime());
                            mMContentMessageAnchorInfo.setThrId(newStartThr);
                            mMContentMessageAnchorInfo.setComment(true);
                            mMContentMessageAnchorInfo.setMsgGuid(threadDataResult.getStartThread());
                            mMContentMessageAnchorInfo.setSendTime(threadDataResult.getStartThrSvrT());
                            mMContentMessageAnchorInfo.setServerTime(threadDataResult.getStartThrSvrT());
                            mMContentMessageAnchorInfo.setmType(1);
                            mMContentMessageAnchorInfo.setSessionId(this.mSessionId);
                            ThreadUnreadInfo threadUnreadInfo = new ThreadUnreadInfo();
                            MessageInfoList markUnreadMessages = sessionById.getMarkUnreadMessages();
                            if (markUnreadMessages != null && markUnreadMessages.getInfoListCount() > 0) {
                                threadUnreadInfo.mMarkUnreadMsgs = new ArrayList<>(markUnreadMessages.getInfoListList());
                                for (MessageInfo messageInfo : markUnreadMessages.getInfoListList()) {
                                    if (messageInfo.getIsComment() && TextUtils.equals(messageInfo.getThr(), newStartThr)) {
                                        threadUnreadInfo.mMarkUnreadMsgs.add(messageInfo);
                                    }
                                }
                            }
                            MMCommentsFragment.showMsgContextInActivity(this, mMContentMessageAnchorInfo, threadUnreadInfo, 0);
                        }
                        ThreadDataUI.getInstance().removeListener(this.mThreadDataUIListener);
                        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
                        this.mPanelMsgContextEmptyView.setVisibility(8);
                        dismiss();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnGetCommentData(CommentDataResult commentDataResult) {
        if (commentDataResult != null && TextUtils.equals(commentDataResult.getChannel(), this.mSessionId)) {
            this.mThreadsRecyclerView.OnGetCommentData(commentDataResult);
        }
    }

    /* access modifiers changed from: private */
    public void OnThreadContextUpdate(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mThreadsRecyclerView.OnThreadContextUpdate(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void onMessageSync(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId) && this.mThreadsRecyclerView.onMessageSync(str, str2) != null) {
            updateBottomHint();
        }
    }

    /* access modifiers changed from: private */
    public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            this.mThreadsRecyclerView.OnFetchEmojiDetailInfo(str, str2, str3, str4, z);
        }
    }

    /* access modifiers changed from: private */
    public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            this.mThreadsRecyclerView.OnFetchEmojiCountInfo(str, str2, list, z);
        }
    }

    /* access modifiers changed from: private */
    public void OnMessageEmojiInfoUpdated(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mThreadsRecyclerView.OnMessageEmojiInfoUpdated(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void OnEmojiCountInfoLoadedFromDB(String str) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mThreadsRecyclerView.OnEmojiCountInfoLoadedFromDB(str);
        }
    }

    /* access modifiers changed from: private */
    public void OnSyncThreadCommentCount(String str, String str2, List<String> list, boolean z) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            this.mThreadsRecyclerView.OnSyncThreadCommentCount(str, str2, list, z);
        }
    }

    public void onMessageShowed(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            showFloatingEmojisIfMatched(mMMessageItem.messageId, mMMessageItem.message, mMMessageItem.serverSideTime, true);
            if (this.mThreadsRecyclerView.isLayoutReady() && !this.mThreadsRecyclerView.isLoading(1) && !this.mThreadsRecyclerView.isLoading(2)) {
                this.mCacheMsgs.put(mMMessageItem, Long.valueOf(System.currentTimeMillis()));
                if (this.mMessageHelper.onMessageShowed(mMMessageItem)) {
                    updateBottomHint();
                }
                if (mMMessageItem.needTriggerUpdate) {
                    UnSupportMessageMgr unsupportMessageMgr = PTApp.getInstance().getUnsupportMessageMgr();
                    if (unsupportMessageMgr != null) {
                        String str = this.mSessionId;
                        StringBuilder sb = new StringBuilder();
                        sb.append(mMMessageItem.serverSideTime);
                        sb.append("");
                        unsupportMessageMgr.searchUnSupportMessage(str, sb.toString());
                    }
                }
            }
        }
    }

    public boolean isMessageMarkUnread(MMMessageItem mMMessageItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
        if (sessionById == null) {
            return false;
        }
        return sessionById.isMessageMarkUnread(mMMessageItem.messageXMPPId);
    }

    public boolean isMessageStarred(MMMessageItem mMMessageItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isStarMessage(this.mSessionId, mMMessageItem.serverSideTime);
    }

    public void saveImage(MMMessageItem mMMessageItem) {
        if (mMMessageItem.messageType == 33 || mMMessageItem.messageType == 32) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(mMMessageItem.giphyID);
                if (giphyInfo != null) {
                    File cacheFile = ImageLoader.getInstance().getCacheFile(giphyInfo.getPcUrl());
                    if (cacheFile == null || !cacheFile.exists()) {
                        cacheFile = ImageLoader.getInstance().getCacheFile(giphyInfo.getMobileUrl());
                        if (cacheFile == null || !cacheFile.exists()) {
                            return;
                        }
                    }
                    if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        ImageUtil.saveToGallery(this, cacheFile);
                    } else {
                        this.mSavingFile = cacheFile;
                        zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 5001);
                    }
                }
            }
        } else if (mMMessageItem.messageType == 4 || mMMessageItem.messageType == 5 || mMMessageItem.messageType == 27 || mMMessageItem.messageType == 28) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                savePicture(mMMessageItem);
            } else {
                this.mSavingMsg = mMMessageItem;
                zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 5002);
            }
        }
    }

    private void savePicture(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            if (StringUtil.isEmptyOrNull(mMMessageItem.localFilePath) || !new File(mMMessageItem.localFilePath).exists() || !ImageUtil.isValidImageFile(mMMessageItem.localFilePath)) {
                ChatImgSaveHelper.getInstance().downloadAndSaveImage(this.mSessionId, mMMessageItem.messageId);
            } else {
                ImageUtil.saveToGallery(this, new File(mMMessageItem.localFilePath));
            }
        }
    }

    public void saveEmoji(MMMessageItem mMMessageItem) {
        ZoomLogEventTracking.eventTrackSaveEmoji(this.mIsGroup);
        if (mMMessageItem.messageType == 33 || mMMessageItem.messageType == 32) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(mMMessageItem.giphyID);
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
                        ImageUtil.saveGiphyEmoji(cacheFile);
                    } else {
                        this.mSavingEmoji = cacheFile;
                        zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 6001);
                    }
                }
            }
        } else if (mMMessageItem != null && !StringUtil.isEmptyOrNull(mMMessageItem.fileId)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(mMMessageItem.fileId);
                if (fileWithWebFileID != null) {
                    long fileSize = (long) fileWithWebFileID.getFileSize();
                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                    if (fileSize > FontStyle.FontStyle_PNG) {
                        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_sticker_too_large, false).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                        return;
                    }
                    MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
                    if (zoomPrivateStickerMgr != null) {
                        int makePrivateSticker = zoomPrivateStickerMgr.makePrivateSticker(mMMessageItem.fileId);
                        if (makePrivateSticker != 0) {
                            if (makePrivateSticker != 2) {
                                switch (makePrivateSticker) {
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
        }
    }

    public void onLayoutCompleted() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                MMThreadsFragment.this.updateBottomHint();
            }
        }, 500);
    }

    public void onMoreComment(MMMessageItem mMMessageItem) {
        requireCommentOnThread(mMMessageItem, false);
    }

    public void onHideComment(MMMessageItem mMMessageItem) {
        mMMessageItem.hasMoreReply = true;
        mMMessageItem.replyExpandNum = 0;
        this.mThreadsRecyclerView.updateThread(mMMessageItem.messageId);
    }

    public void onAddComment(MMMessageItem mMMessageItem) {
        requireCommentOnThread(mMMessageItem, true);
    }

    public void onShowFloatingText(View view, int i, boolean z) {
        showFloatingView(view, i, z);
    }

    public void onClickAddReactionLabel(View view, MMMessageItem mMMessageItem) {
        if (PTApp.getInstance().isWebSignedOn()) {
            onLongClickAddReactionLabel(view, mMMessageItem);
        }
    }

    public void onClickAddReplyLabel(View view, MMMessageItem mMMessageItem) {
        requireCommentOnThread(mMMessageItem, true);
    }

    public void onClickMoreActionLabel(View view, MMMessageItem mMMessageItem) {
        onShowContextMenu(view, mMMessageItem);
    }

    public void onClickReactionLabel(View view, MMMessageItem mMMessageItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem, boolean z) {
        String str;
        if (PTApp.getInstance().isWebSignedOn() && mMMessageItem != null && mMCommentsEmojiCountItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    if (z) {
                        str = threadDataProvider.addEmojiForMessage(mMMessageItem.sessionId, mMMessageItem.messageXMPPId, threadDataProvider.getEmojiStrKey(mMCommentsEmojiCountItem.getEmoji()));
                    } else {
                        str = threadDataProvider.removeEmojiForMessage(mMMessageItem.sessionId, mMMessageItem.messageXMPPId, threadDataProvider.getEmojiStrKey(mMCommentsEmojiCountItem.getEmoji()));
                    }
                    StringUtil.isEmptyOrNull(str);
                }
            }
        }
    }

    public boolean onLongClickReactionLabel(View view, MMMessageItem mMMessageItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem) {
        if (!PTApp.getInstance().isWebSignedOn() || mMCommentsEmojiCountItem == null) {
            return false;
        }
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity == null) {
            return false;
        }
        ReactionEmojiDetailDialog.builder(zMActivity).setData(mMMessageItem).setEmoji(mMCommentsEmojiCountItem.getEmoji()).setEnableScroll(Boolean.valueOf(true)).show(zMActivity.getSupportFragmentManager());
        return true;
    }

    /* access modifiers changed from: private */
    public void showAddReactionDialog(View view, final MMMessageItem mMMessageItem) {
        if (view != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                Rect messageLocationOnScreen = this.mThreadsRecyclerView.getMessageLocationOnScreen(mMMessageItem);
                if (messageLocationOnScreen != null) {
                    int height = this.mTitleBar.getHeight();
                    int i = messageLocationOnScreen.top;
                    int i2 = messageLocationOnScreen.bottom - messageLocationOnScreen.top;
                    int computeVerticalScrollRange = this.mThreadsRecyclerView.computeVerticalScrollRange();
                    int computeVerticalScrollOffset = this.mThreadsRecyclerView.computeVerticalScrollOffset();
                    int i3 = i > 0 ? (computeVerticalScrollRange - computeVerticalScrollOffset) - i : computeVerticalScrollRange - computeVerticalScrollOffset;
                    dismissReactionEmojiDialog();
                    this.mReactionEmojiDialog = new ReactionEmojiDialog.Builder(zMActivity).setAnchor(i, i2, height, i3, new OnReactionEmojiListener() {
                        public void onReactionEmojiDialogShowed(boolean z, final int i) {
                            boolean z2 = false;
                            if (z) {
                                MMThreadsFragment.this.mThreadsRecyclerView.scrollBy(0, i);
                                return;
                            }
                            if (i >= 0) {
                                if (MMThreadsFragment.this.mThreadsRecyclerView.computeVerticalScrollRange() < MMThreadsFragment.this.mThreadsRecyclerView.getHeight()) {
                                    z2 = true;
                                }
                                if (i <= 0 || !z2) {
                                    MMThreadsFragment.this.mThreadsRecyclerView.setMessageViewMargin(mMMessageItem, i);
                                } else {
                                    MMThreadsFragment.this.mThreadsRecyclerView.setMessageViewMargin(mMMessageItem, (MMThreadsFragment.this.mThreadsRecyclerView.getHeight() + i) - MMThreadsFragment.this.mThreadsRecyclerView.computeVerticalScrollRange());
                                }
                            }
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    MMThreadsFragment.this.mThreadsRecyclerView.scrollBy(0, i);
                                }
                            }, 100);
                        }

                        public void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj) {
                            MMThreadsFragment.this.onReactionEmojiClick(view, i, charSequence, obj);
                        }
                    }).setData(mMMessageItem).create();
                    this.mReactionEmojiDialog.setCanceledOnTouchOutside(true);
                    this.mReactionEmojiDialog.show();
                }
            }
        }
    }

    public boolean onLongClickAddReactionLabel(final View view, final MMMessageItem mMMessageItem) {
        if (!PTApp.getInstance().isWebSignedOn()) {
            return false;
        }
        if (closeKeyboardIfOpened(view)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MMThreadsFragment.this.showAddReactionDialog(view, mMMessageItem);
                }
            }, 100);
        } else {
            showAddReactionDialog(view, mMMessageItem);
        }
        return true;
    }

    public void onReachReactionLimit() {
        showReactionEmojiLimitDialog();
    }

    public void onJumpResult(boolean z) {
        if (z) {
            this.mPanelMsgContextEmptyView.setVisibility(8);
        } else {
            onMessageContextLoadFailed();
        }
        updatePanelNotes();
    }

    public boolean onShowContextMenu(final View view, final MMMessageItem mMMessageItem) {
        if (!PTApp.getInstance().isWebSignedOn()) {
            return true;
        }
        if (closeKeyboardIfOpened(view)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MMThreadsFragment.this.showContextMenu(view, mMMessageItem);
                }
            }, 100);
        } else {
            showContextMenu(view, mMMessageItem);
        }
        return true;
    }

    public boolean onShowLinkContextMenu(View view, MMMessageItem mMMessageItem, String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        String replace = str.replace("-", "").replace(OAuth.SCOPE_DELIMITER, "");
        if (ZMIMUtils.isZoomMeetingNo(replace)) {
            onClickMeetingNO(replace);
        } else {
            showLinkContextMenu(str);
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0188  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x01be  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x01cd  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0211  */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x0252  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x0263 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02c1  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x038d  */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x03bb  */
    /* JADX WARNING: Removed duplicated region for block: B:205:0x042b  */
    /* JADX WARNING: Removed duplicated region for block: B:233:0x04ea  */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x059a  */
    /* JADX WARNING: Removed duplicated region for block: B:280:0x063d  */
    /* JADX WARNING: Removed duplicated region for block: B:330:0x076b  */
    /* JADX WARNING: Removed duplicated region for block: B:333:0x078b A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:334:0x078c  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00d3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void showContextMenu(android.view.View r24, com.zipow.videobox.view.p014mm.MMMessageItem r25) {
        /*
            r23 = this;
            r0 = r23
            r1 = r25
            android.content.Context r2 = r23.getContext()
            android.app.Activity r2 = (android.app.Activity) r2
            if (r2 != 0) goto L_0x000d
            return
        L_0x000d:
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r3 = r3.getZoomMessenger()
            if (r3 != 0) goto L_0x0018
            return
        L_0x0018:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r4 = r3.getMyself()
            if (r4 != 0) goto L_0x001f
            return
        L_0x001f:
            com.zipow.videobox.view.mm.message.ReactionContextMenuListAdapter r5 = new com.zipow.videobox.view.mm.message.ReactionContextMenuListAdapter
            r5.<init>(r2, r1)
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            int r7 = r1.messageState
            r8 = 4
            r9 = 1
            if (r7 != r8) goto L_0x0031
            r7 = 1
            goto L_0x0032
        L_0x0031:
            r7 = 0
        L_0x0032:
            int r11 = r1.messageState
            if (r11 != r9) goto L_0x0038
            r11 = 1
            goto L_0x0039
        L_0x0038:
            r11 = 0
        L_0x0039:
            int r12 = r1.messageState
            r13 = 6
            if (r12 != r13) goto L_0x0040
            r12 = 1
            goto L_0x0041
        L_0x0040:
            r12 = 0
        L_0x0041:
            boolean r14 = r23.isAnnouncement()
            r15 = 2
            if (r7 == 0) goto L_0x005c
            int r10 = r1.messageType
            r13 = 44
            if (r10 == r13) goto L_0x005c
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r10 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r13 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_resend_message
            java.lang.String r13 = r2.getString(r13)
            r10.<init>(r13, r15)
            r6.add(r10)
        L_0x005c:
            boolean r10 = r0.mIsGroup
            if (r10 != 0) goto L_0x0072
            java.lang.String r10 = r0.mBuddyId
            com.zipow.videobox.ptapp.mm.ZoomBuddy r10 = r3.getBuddyWithJID(r10)
            if (r10 == 0) goto L_0x0072
            int r10 = r10.getAccountStatus()
            if (r10 != 0) goto L_0x0070
            r10 = 1
            goto L_0x0073
        L_0x0070:
            r10 = 0
            goto L_0x0073
        L_0x0072:
            r10 = 1
        L_0x0073:
            boolean r13 = r0.mIsGroup
            if (r13 != 0) goto L_0x0081
            java.lang.String r13 = r0.mBuddyId
            boolean r13 = r3.blockUserIsBlocked(r13)
            if (r13 == 0) goto L_0x0081
            r13 = 1
            goto L_0x0082
        L_0x0081:
            r13 = 0
        L_0x0082:
            if (r10 == 0) goto L_0x0090
            if (r13 != 0) goto L_0x0090
            java.lang.String r8 = r0.mSessionId
            boolean r8 = r1.isDeleteable(r8)
            if (r8 == 0) goto L_0x0090
            r8 = 1
            goto L_0x0091
        L_0x0090:
            r8 = 0
        L_0x0091:
            boolean r9 = r1.isE2E
            if (r9 != 0) goto L_0x009e
            int r9 = r3.e2eGetMyOption()
            if (r9 != r15) goto L_0x009c
            goto L_0x009e
        L_0x009c:
            r9 = 0
            goto L_0x009f
        L_0x009e:
            r9 = 1
        L_0x009f:
            boolean r16 = r25.isMessageE2EWaitDecrypt()
            int r15 = r3.msgCopyGetOption()
            r18 = r5
            r5 = 1
            if (r15 != r5) goto L_0x00ae
            r5 = 1
            goto L_0x00af
        L_0x00ae:
            r5 = 0
        L_0x00af:
            boolean r15 = com.zipow.videobox.util.ZMIMUtils.isReplyEnable()
            if (r15 == 0) goto L_0x00c1
            boolean r15 = r23.isAnnouncement()
            if (r15 != 0) goto L_0x00c1
            r20 = r9
            r19 = r10
            r15 = 1
            goto L_0x00c6
        L_0x00c1:
            r20 = r9
            r19 = r10
            r15 = 0
        L_0x00c6:
            long r9 = r1.serverSideTime
            r21 = 0
            int r9 = (r9 > r21 ? 1 : (r9 == r21 ? 0 : -1))
            if (r9 == 0) goto L_0x00d0
            r9 = 1
            goto L_0x00d1
        L_0x00d0:
            r9 = 0
        L_0x00d1:
            if (r7 != 0) goto L_0x0188
            int r10 = r1.messageType
            r21 = r13
            r13 = 37
            if (r10 == r13) goto L_0x016c
            int r10 = r1.messageType
            r13 = 38
            if (r10 != r13) goto L_0x00e3
            goto L_0x016c
        L_0x00e3:
            com.zipow.videobox.ptapp.PTApp r10 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r10 = r10.isFileTransferDisabled()
            if (r10 != 0) goto L_0x0169
            int r10 = r1.messageType
            r13 = 2
            if (r10 == r13) goto L_0x0169
            int r10 = r1.messageType
            r13 = 3
            if (r10 == r13) goto L_0x0169
            boolean r10 = r1.isE2E
            if (r10 != 0) goto L_0x0166
            int r10 = r3.e2eGetMyOption()
            r13 = 2
            if (r10 == r13) goto L_0x0166
            int r10 = r1.messageState
            r13 = 3
            if (r10 == r13) goto L_0x0111
            int r10 = r1.messageState
            r13 = 2
            if (r10 != r13) goto L_0x010d
            goto L_0x0111
        L_0x010d:
            r17 = r12
            goto L_0x018c
        L_0x0111:
            boolean r10 = r25.isNormalFileMsg()
            if (r10 == 0) goto L_0x0129
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r10 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r13 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r13 = r2.getString(r13)
            r17 = r12
            r12 = 4
            r10.<init>(r13, r12)
            r6.add(r10)
            goto L_0x012b
        L_0x0129:
            r17 = r12
        L_0x012b:
            boolean r10 = r25.isNormalImageMsg()
            if (r10 == 0) goto L_0x018c
            boolean r10 = r1.isStikcerMsg
            if (r10 == 0) goto L_0x0143
            java.lang.String r4 = r4.getJid()
            java.lang.String r10 = r1.fromJid
            boolean r4 = android.text.TextUtils.equals(r4, r10)
            if (r4 == 0) goto L_0x0143
            r4 = 1
            goto L_0x0144
        L_0x0143:
            r4 = 0
        L_0x0144:
            if (r4 != 0) goto L_0x018c
            java.lang.String r4 = r1.fileId
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 == 0) goto L_0x0156
            java.lang.String r4 = r1.localFilePath
            boolean r4 = com.zipow.videobox.util.ImageUtil.isValidImageFile(r4)
            if (r4 == 0) goto L_0x018c
        L_0x0156:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r4 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r10 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_save_emoji
            java.lang.String r10 = r2.getString(r10)
            r12 = 6
            r4.<init>(r10, r12)
            r6.add(r4)
            goto L_0x018c
        L_0x0166:
            r17 = r12
            goto L_0x018c
        L_0x0169:
            r17 = r12
            goto L_0x018c
        L_0x016c:
            r17 = r12
            boolean r4 = r1.isE2E
            if (r4 != 0) goto L_0x018c
            boolean r4 = r3.isCodeSnippetDisabled()
            if (r4 != 0) goto L_0x018c
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r4 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r10 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r10 = r2.getString(r10)
            r12 = 4
            r4.<init>(r10, r12)
            r6.add(r4)
            goto L_0x018c
        L_0x0188:
            r17 = r12
            r21 = r13
        L_0x018c:
            r4 = 24
            r10 = 23
            r12 = 21
            if (r15 == 0) goto L_0x0206
            boolean r13 = r1.isDeletedThread
            if (r13 != 0) goto L_0x019c
            boolean r13 = r1.isNotExitThread
            if (r13 == 0) goto L_0x0206
        L_0x019c:
            if (r9 == 0) goto L_0x0206
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r9 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r13 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r13 = r2.getString(r13)
            r9.<init>(r13, r12)
            r6.add(r9)
            com.zipow.videobox.ptapp.ThreadDataProvider r9 = r3.getThreadDataProvider()
            if (r9 == 0) goto L_0x01db
            if (r14 != 0) goto L_0x01db
            java.lang.String r13 = r0.mSessionId
            java.lang.String r12 = r1.messageId
            boolean r9 = r9.isThreadFollowed(r13, r12)
            if (r9 == 0) goto L_0x01cd
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r9 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r12 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_unfollow_thread_88133
            java.lang.String r12 = r2.getString(r12)
            r9.<init>(r12, r4)
            r6.add(r9)
            goto L_0x01db
        L_0x01cd:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r9 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r12 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_follow_thread_88133
            java.lang.String r12 = r2.getString(r12)
            r9.<init>(r12, r10)
            r6.add(r9)
        L_0x01db:
            boolean r9 = r1.isE2E
            if (r9 != 0) goto L_0x0206
            boolean r9 = r0.isMessageStarred(r1)
            if (r9 == 0) goto L_0x01f6
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r9 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r12 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r12 = r2.getString(r12)
            r13 = 16
            r9.<init>(r12, r13)
            r6.add(r9)
            goto L_0x0206
        L_0x01f6:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r9 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r12 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r12 = r2.getString(r12)
            r13 = 15
            r9.<init>(r12, r13)
            r6.add(r9)
        L_0x0206:
            int r9 = r1.messageType
            r12 = 11
            r13 = 14
            switch(r9) {
                case 0: goto L_0x063d;
                case 1: goto L_0x063d;
                case 2: goto L_0x059a;
                case 3: goto L_0x059a;
                case 4: goto L_0x04ea;
                case 5: goto L_0x04ea;
                case 6: goto L_0x0765;
                case 7: goto L_0x0765;
                case 8: goto L_0x0765;
                case 9: goto L_0x0765;
                case 10: goto L_0x042b;
                case 11: goto L_0x042b;
                case 12: goto L_0x020f;
                case 13: goto L_0x020f;
                case 14: goto L_0x020f;
                case 15: goto L_0x020f;
                case 16: goto L_0x020f;
                case 17: goto L_0x020f;
                case 18: goto L_0x020f;
                case 19: goto L_0x020f;
                case 20: goto L_0x020f;
                case 21: goto L_0x03bb;
                case 22: goto L_0x03bb;
                case 23: goto L_0x03bb;
                case 24: goto L_0x020f;
                case 25: goto L_0x020f;
                case 26: goto L_0x020f;
                case 27: goto L_0x04ea;
                case 28: goto L_0x04ea;
                case 29: goto L_0x038d;
                case 30: goto L_0x020f;
                case 31: goto L_0x020f;
                case 32: goto L_0x02c1;
                case 33: goto L_0x02c1;
                case 34: goto L_0x063d;
                case 35: goto L_0x063d;
                case 36: goto L_0x020f;
                case 37: goto L_0x0263;
                case 38: goto L_0x0263;
                case 39: goto L_0x0252;
                case 40: goto L_0x03bb;
                case 41: goto L_0x0211;
                case 42: goto L_0x020f;
                case 43: goto L_0x03bb;
                case 44: goto L_0x03bb;
                case 45: goto L_0x042b;
                case 46: goto L_0x042b;
                default: goto L_0x020f;
            }
        L_0x020f:
            goto L_0x0765
        L_0x0211:
            if (r16 != 0) goto L_0x0765
            if (r5 == 0) goto L_0x0224
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_copy_message
            java.lang.String r4 = r2.getString(r4)
            r5 = 1
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0224:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0765
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0240
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x0240:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x0252:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r4 = r2.getString(r4)
            r5 = 4
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x0263:
            if (r15 == 0) goto L_0x0279
            boolean r3 = r1.isThread
            if (r3 == 0) goto L_0x0279
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r4 = r2.getString(r4)
            r5 = 21
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0279:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x02a4
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0294
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x02a4
        L_0x0294:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x02a4:
            if (r8 == 0) goto L_0x0765
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r23.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x02c1:
            if (r7 != 0) goto L_0x037d
            if (r11 != 0) goto L_0x037d
            if (r17 != 0) goto L_0x037d
            com.zipow.videobox.ptapp.ThreadDataProvider r3 = r3.getThreadDataProvider()
            if (r3 == 0) goto L_0x02f6
            if (r14 != 0) goto L_0x02f6
            java.lang.String r5 = r0.mSessionId
            java.lang.String r7 = r1.messageId
            boolean r3 = r3.isThreadFollowed(r5, r7)
            if (r3 == 0) goto L_0x02e8
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_unfollow_thread_88133
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r4)
            r6.add(r3)
            goto L_0x02f6
        L_0x02e8:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_follow_thread_88133
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r10)
            r6.add(r3)
        L_0x02f6:
            if (r15 == 0) goto L_0x030c
            boolean r3 = r1.isThread
            if (r3 == 0) goto L_0x030c
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r4 = r2.getString(r4)
            r5 = 21
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x030c:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0337
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0327
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0337
        L_0x0327:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0337:
            if (r20 != 0) goto L_0x0352
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r3 = r3.isFileTransferDisabled()
            if (r3 != 0) goto L_0x0352
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_save_emoji
            java.lang.String r4 = r2.getString(r4)
            r5 = 6
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0352:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_btn_save_image
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
            if (r8 == 0) goto L_0x0765
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r23.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x037d:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r13)
            r6.add(r3)
            goto L_0x0765
        L_0x038d:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0765
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x03a9
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x03a9:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x03bb:
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.sip.CallHistoryMgr r3 = r3.getCallHistoryMgr()
            if (r3 != 0) goto L_0x03c7
            goto L_0x0765
        L_0x03c7:
            java.lang.String r4 = r1.messageXMPPId
            com.zipow.videobox.sip.CallHistory r3 = r3.getCallHistoryByID(r4)
            if (r3 != 0) goto L_0x03d1
            goto L_0x0765
        L_0x03d1:
            java.lang.String r3 = r3.getNumber()
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x0765
            if (r5 == 0) goto L_0x03ed
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_copy_url
            java.lang.String r4 = r2.getString(r4)
            r5 = 13
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x03ed:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_mm_join_meeting_21854
            java.lang.String r4 = r2.getString(r4)
            r5 = 12
            r3.<init>(r4, r5)
            r6.add(r3)
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0765
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0419
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x0419:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x042b:
            if (r7 != 0) goto L_0x04da
            if (r11 != 0) goto L_0x04da
            if (r17 != 0) goto L_0x04da
            com.zipow.videobox.ptapp.ThreadDataProvider r3 = r3.getThreadDataProvider()
            if (r3 == 0) goto L_0x0460
            if (r14 != 0) goto L_0x0460
            java.lang.String r5 = r0.mSessionId
            java.lang.String r7 = r1.messageId
            boolean r3 = r3.isThreadFollowed(r5, r7)
            if (r3 == 0) goto L_0x0452
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_unfollow_thread_88133
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r4)
            r6.add(r3)
            goto L_0x0460
        L_0x0452:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_follow_thread_88133
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r10)
            r6.add(r3)
        L_0x0460:
            if (r15 == 0) goto L_0x0476
            boolean r3 = r1.isThread
            if (r3 == 0) goto L_0x0476
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r4 = r2.getString(r4)
            r5 = 21
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0476:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x04a1
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0491
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x04a1
        L_0x0491:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x04a1:
            int r3 = r1.messageType
            r4 = 46
            if (r3 == r4) goto L_0x04ad
            int r3 = r1.messageType
            r4 = 45
            if (r3 != r4) goto L_0x04bd
        L_0x04ad:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_copy_link_68764
            java.lang.String r4 = r2.getString(r4)
            r5 = 17
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x04bd:
            if (r8 == 0) goto L_0x0765
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r23.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x04da:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r13)
            r6.add(r3)
            goto L_0x0765
        L_0x04ea:
            if (r7 != 0) goto L_0x057c
            if (r11 != 0) goto L_0x057c
            if (r17 != 0) goto L_0x057c
            com.zipow.videobox.ptapp.ThreadDataProvider r3 = r3.getThreadDataProvider()
            if (r3 == 0) goto L_0x051f
            if (r14 != 0) goto L_0x051f
            java.lang.String r5 = r0.mSessionId
            java.lang.String r7 = r1.messageId
            boolean r3 = r3.isThreadFollowed(r5, r7)
            if (r3 == 0) goto L_0x0511
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_unfollow_thread_88133
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r4)
            r6.add(r3)
            goto L_0x051f
        L_0x0511:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_follow_thread_88133
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r10)
            r6.add(r3)
        L_0x051f:
            if (r15 == 0) goto L_0x0535
            boolean r3 = r1.isThread
            if (r3 == 0) goto L_0x0535
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r4 = r2.getString(r4)
            r5 = 21
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0535:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0560
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0550
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0560
        L_0x0550:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0560:
            if (r8 == 0) goto L_0x058a
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r23.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x058a
        L_0x057c:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r13)
            r6.add(r3)
        L_0x058a:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_btn_save_image
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
            goto L_0x0765
        L_0x059a:
            if (r7 != 0) goto L_0x062d
            if (r11 != 0) goto L_0x062d
            if (r17 != 0) goto L_0x062d
            com.zipow.videobox.ptapp.ThreadDataProvider r3 = r3.getThreadDataProvider()
            if (r3 == 0) goto L_0x05cf
            if (r14 != 0) goto L_0x05cf
            java.lang.String r5 = r0.mSessionId
            java.lang.String r7 = r1.messageId
            boolean r3 = r3.isThreadFollowed(r5, r7)
            if (r3 == 0) goto L_0x05c1
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_unfollow_thread_88133
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r4)
            r6.add(r3)
            goto L_0x05cf
        L_0x05c1:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_follow_thread_88133
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r10)
            r6.add(r3)
        L_0x05cf:
            if (r15 == 0) goto L_0x05e5
            boolean r3 = r1.isThread
            if (r3 == 0) goto L_0x05e5
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r4 = r2.getString(r4)
            r5 = 21
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x05e5:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0610
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0600
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0610
        L_0x0600:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0610:
            if (r8 == 0) goto L_0x0765
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r23.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x062d:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r13)
            r6.add(r3)
            goto L_0x0765
        L_0x063d:
            if (r5 == 0) goto L_0x064f
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r8 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_copy_message
            java.lang.String r9 = r2.getString(r9)
            r12 = 1
            r8.<init>(r9, r12)
            r6.add(r8)
            goto L_0x0650
        L_0x064f:
            r12 = 1
        L_0x0650:
            if (r7 != 0) goto L_0x0757
            if (r11 != 0) goto L_0x0757
            if (r17 != 0) goto L_0x0757
            com.zipow.videobox.ptapp.ThreadDataProvider r3 = r3.getThreadDataProvider()
            if (r3 == 0) goto L_0x0685
            if (r14 != 0) goto L_0x0685
            java.lang.String r7 = r0.mSessionId
            java.lang.String r8 = r1.messageId
            boolean r3 = r3.isThreadFollowed(r7, r8)
            if (r3 == 0) goto L_0x0677
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_unfollow_thread_88133
            java.lang.String r7 = r2.getString(r7)
            r3.<init>(r7, r4)
            r6.add(r3)
            goto L_0x0685
        L_0x0677:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_follow_thread_88133
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r10)
            r6.add(r3)
        L_0x0685:
            if (r15 == 0) goto L_0x069b
            boolean r3 = r1.isThread
            if (r3 == 0) goto L_0x069b
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r4 = r2.getString(r4)
            r7 = 21
            r3.<init>(r4, r7)
            r6.add(r3)
        L_0x069b:
            if (r16 != 0) goto L_0x0709
            if (r5 == 0) goto L_0x06af
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r4 = r2.getString(r4)
            r5 = 20
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x06af:
            java.lang.String r3 = r1.sessionId
            boolean r3 = com.zipow.videobox.util.UIMgr.isMyNotes(r3)
            if (r3 != 0) goto L_0x06de
            boolean r3 = r0.isMessageMarkUnread(r1)
            if (r3 == 0) goto L_0x06ce
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_mark_as_read_14491
            java.lang.String r4 = r2.getString(r4)
            r5 = 10
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x06de
        L_0x06ce:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_mark_unread_14491
            java.lang.String r4 = r2.getString(r4)
            r5 = 9
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x06de:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0709
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x06f9
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 16
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x0709
        L_0x06f9:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r5 = 15
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0709:
            if (r19 == 0) goto L_0x0720
            if (r21 != 0) goto L_0x0720
            boolean r3 = r25.isEditable()
            if (r3 == 0) goto L_0x0720
            boolean r3 = r23.isAnnouncement()
            if (r3 == 0) goto L_0x0721
            boolean r3 = r23.isAdmin()
            if (r3 == 0) goto L_0x0720
            goto L_0x0721
        L_0x0720:
            r12 = 0
        L_0x0721:
            if (r12 == 0) goto L_0x0765
            if (r20 == 0) goto L_0x072d
            java.lang.String r3 = r1.sessionId
            boolean r3 = com.zipow.videobox.util.UIMgr.isMyNotes(r3)
            if (r3 == 0) goto L_0x073d
        L_0x072d:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_edit_message_19884
            java.lang.String r4 = r2.getString(r4)
            r5 = 8
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x073d:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r23.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x0765
        L_0x0757:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r13)
            r6.add(r3)
        L_0x0765:
            boolean r3 = r6.isEmpty()
            if (r3 != 0) goto L_0x0785
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_cancel
            java.lang.String r4 = r2.getString(r4)
            r5 = 22
            android.content.res.Resources r7 = r23.getResources()
            int r8 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_blue_0E71EB
            int r7 = r7.getColor(r8)
            r3.<init>(r4, r5, r7)
            r6.add(r3)
        L_0x0785:
            boolean r3 = r6.isEmpty()
            if (r3 == 0) goto L_0x078c
            return
        L_0x078c:
            r3 = r18
            r3.setData(r6)
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r4 = r0.mThreadsRecyclerView
            android.graphics.Rect r4 = r4.getMessageLocationOnScreen(r1)
            if (r4 != 0) goto L_0x079a
            return
        L_0x079a:
            int r5 = r4.top
            int r6 = r4.bottom
            int r4 = r4.top
            int r6 = r6 - r4
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r4 = r0.mThreadsRecyclerView
            int r4 = r4.computeVerticalScrollRange()
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r7 = r0.mThreadsRecyclerView
            int r7 = r7.computeVerticalScrollOffset()
            if (r5 <= 0) goto L_0x07b2
            int r4 = r4 - r7
            int r4 = r4 - r5
            goto L_0x07b3
        L_0x07b2:
            int r4 = r4 - r7
        L_0x07b3:
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog r7 = r0.mContextMenuDialog
            if (r7 == 0) goto L_0x07ba
            r7 = 0
            r0.mContextMenuDialog = r7
        L_0x07ba:
            androidx.fragment.app.FragmentManager r7 = r23.getFragmentManager()
            if (r7 != 0) goto L_0x07c1
            return
        L_0x07c1:
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r2 = com.zipow.videobox.view.p014mm.message.ReactionContextMenuDialog.builder(r2)
            com.zipow.videobox.fragment.MMThreadsFragment$34 r8 = new com.zipow.videobox.fragment.MMThreadsFragment$34
            r8.<init>(r3, r1)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r2 = r2.setAdapter(r3, r8)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r2 = r2.setLocation(r5, r6, r4)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r1 = r2.setMessageItem(r1)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog r1 = r1.build()
            r0.mContextMenuDialog = r1
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog r1 = r0.mContextMenuDialog
            r1.show(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMThreadsFragment.showContextMenu(android.view.View, com.zipow.videobox.view.mm.MMMessageItem):void");
    }

    private void followThread(MMMessageItem mMMessageItem, boolean z) {
        if (mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    if (z) {
                        if (threadDataProvider.followThread(this.mSessionId, mMMessageItem.messageId)) {
                            Toast.makeText(getContext(), C4558R.string.zm_lbl_follow_hint_88133, 1).show();
                        }
                    } else if (threadDataProvider.discardFollowThread(this.mSessionId, mMMessageItem.messageId)) {
                        Toast.makeText(getContext(), C4558R.string.zm_lbl_unfollow_hint_88133, 1).show();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void selectContextMenuItem(MessageContextMenuItem messageContextMenuItem, MMMessageItem mMMessageItem) {
        if (messageContextMenuItem != null && mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    boolean isConnectionGood = zoomMessenger.isConnectionGood();
                    switch (messageContextMenuItem.getAction()) {
                        case 0:
                            if (!isConnectionGood) {
                                Toast.makeText(getContext(), getResources().getString(C4558R.string.zm_mm_msg_network_unavailable), 1).show();
                                break;
                            } else {
                                mMMessageItem.deleteMessage(getActivity());
                                eventTrackDeleteMessage(mMMessageItem, this.mIsGroup);
                                break;
                            }
                        case 1:
                            if (mMMessageItem.messageType != 41) {
                                AndroidAppUtil.copyText(getContext(), mMMessageItem.message);
                                break;
                            } else {
                                IZoomMessageTemplate iZoomMessageTemplate = mMMessageItem.template;
                                StringBuffer stringBuffer = new StringBuffer();
                                if (iZoomMessageTemplate != null) {
                                    IMessageTemplateHead head = iZoomMessageTemplate.getHead();
                                    if (head != null) {
                                        stringBuffer.append(head.getText());
                                        stringBuffer.append(FontStyleHelper.SPLITOR);
                                        IMessageTemplateSubHead subHead = head.getSubHead();
                                        if (subHead != null) {
                                            stringBuffer.append(subHead.getText());
                                            stringBuffer.append(FontStyleHelper.SPLITOR);
                                        }
                                    }
                                    List body = iZoomMessageTemplate.getBody();
                                    ArrayList arrayList = new ArrayList();
                                    if (body != null) {
                                        arrayList.addAll(body);
                                    }
                                    MMMessageHelper.copyTemplateMessage(arrayList, stringBuffer);
                                    AndroidAppUtil.copyText(getContext(), stringBuffer.toString());
                                    break;
                                }
                            }
                            break;
                        case 2:
                            resendMessage(mMMessageItem);
                            break;
                        case 4:
                            shareMessage(mMMessageItem.messageXMPPId);
                            eventTrackShare(mMMessageItem.messageType, this.mIsGroup);
                            break;
                        case 6:
                            saveEmoji(mMMessageItem);
                            break;
                        case 8:
                            if (!isConnectionGood) {
                                Toast.makeText(getContext(), getResources().getString(C4558R.string.zm_mm_msg_network_unavailable), 1).show();
                                break;
                            } else {
                                editMessage(mMMessageItem);
                                ZoomLogEventTracking.eventTrackEditMessage(sessionById.isGroup());
                                break;
                            }
                        case 9:
                            if (!isConnectionGood) {
                                Toast.makeText(getContext(), getResources().getString(C4558R.string.zm_mm_msg_network_unavailable), 1).show();
                                break;
                            } else {
                                markUnreadMessage(mMMessageItem);
                                break;
                            }
                        case 10:
                            markAsReadMessage(mMMessageItem);
                            break;
                        case 11:
                            saveImage(mMMessageItem);
                            break;
                        case 12:
                            if (!isConnectionGood) {
                                Toast.makeText(getContext(), getResources().getString(C4558R.string.zm_mm_msg_network_unavailable), 1).show();
                                break;
                            } else {
                                joinMeeting(mMMessageItem);
                                break;
                            }
                        case 13:
                            copyUrl(mMMessageItem);
                            break;
                        case 14:
                            deleteLocalMessage(mMMessageItem, sessionById);
                            break;
                        case 15:
                            starMessage(mMMessageItem);
                            break;
                        case 16:
                            unstarMessage(mMMessageItem);
                            break;
                        case 17:
                            MMMessageHelper.copySharedFileLink(mMMessageItem);
                            break;
                        case 18:
                            MMMessageHelper.openWithOtherApp(mMMessageItem);
                            break;
                        case 20:
                            forwardMessage(mMMessageItem.messageXMPPId);
                            break;
                        case 21:
                            requireCommentOnThread(mMMessageItem, true);
                            break;
                        case 23:
                            followThread(mMMessageItem, true);
                            break;
                        case 24:
                            followThread(mMMessageItem, false);
                            break;
                    }
                }
            }
        }
    }

    public void onClickMessage(MMMessageItem mMMessageItem) {
        this.mMessageHelper.handleMessageItem(mMMessageItem);
    }

    public void onClickStatusImage(MMMessageItem mMMessageItem) {
        if (PTApp.getInstance().isWebSignedOn()) {
            this.mMessageHelper.clickStatusImage(mMMessageItem);
        }
    }

    public void onClickAvatar(MMMessageItem mMMessageItem) {
        if (mMMessageItem.isIncomingMessage()) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                IMAddrBookItem iMAddrBookItemFromMMMessageItem = getIMAddrBookItemFromMMMessageItem(mMMessageItem);
                if (iMAddrBookItemFromMMMessageItem != null) {
                    if (!iMAddrBookItemFromMMMessageItem.getIsRobot()) {
                        AddrBookItemDetailsActivity.show(zMActivity, iMAddrBookItemFromMMMessageItem, !this.mIsGroup, 0);
                    } else if (iMAddrBookItemFromMMMessageItem.isMyContact()) {
                        AddrBookItemDetailsActivity.show(zMActivity, iMAddrBookItemFromMMMessageItem, !this.mIsGroup, 0);
                    }
                }
            }
        }
    }

    public void onClickCancel(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.FT_Cancel(mMMessageItem);
            }
        }
    }

    public boolean onLongClickAvatar(MMMessageItem mMMessageItem) {
        if (!this.mIsGroup) {
            return false;
        }
        IMAddrBookItem iMAddrBookItemFromMMMessageItem = getIMAddrBookItemFromMMMessageItem(mMMessageItem);
        if (iMAddrBookItemFromMMMessageItem == null) {
            return false;
        }
        this.mChatInputFragment.onATBuddySelect(iMAddrBookItemFromMMMessageItem);
        return true;
    }

    public void onClickAddon(NodeMsgHref nodeMsgHref) {
        if (nodeMsgHref != null && !StringUtil.isEmptyOrNull(nodeMsgHref.getUrl())) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(nodeMsgHref.getUrl()));
                startActivity(intent);
            } catch (Exception unused) {
            }
        }
    }

    public void onClickAction(String str, String str2) {
        handleActionMsg(str, str2);
    }

    public void onClickLinkPreview(MMMessageItem mMMessageItem, LinkPreviewMetaInfo linkPreviewMetaInfo) {
        if (linkPreviewMetaInfo != null) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(linkPreviewMetaInfo.getUrl()));
                startActivity(intent);
            } catch (Exception unused) {
            }
        }
    }

    public void onClickMeetingNO(final String str) {
        Activity activity = (Activity) getContext();
        if (activity != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new MeetingNoMenuItem(activity.getString(C4558R.string.zm_btn_join_meeting), 0));
            arrayList.add(new MeetingNoMenuItem(activity.getString(C4558R.string.zm_btn_call), 1));
            arrayList.add(new MeetingNoMenuItem(activity.getString(C4558R.string.zm_btn_copy), 2));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            TextView textView = new TextView(activity);
            if (VERSION.SDK_INT < 23) {
                textView.setTextAppearance(activity, C4558R.style.ZMTextView_Medium);
            } else {
                textView.setTextAppearance(C4558R.style.ZMTextView_Medium);
            }
            int dip2px = UIUtil.dip2px(activity, 20.0f);
            textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
            textView.setText(activity.getString(C4558R.string.zm_msg_meetingno_hook_title, new Object[]{str}));
            ZMAlertDialog create = new Builder(activity).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMThreadsFragment.this.onSelectMeetingNoMenuItem((MeetingNoMenuItem) zMMenuAdapter.getItem(i), str);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    public void onClickActionMore(final String str, List<AddonNode> list) {
        if (getActivity() != null && list != null && list.size() > 0) {
            ArrayList<AddonNode> arrayList = new ArrayList<>();
            arrayList.addAll(list);
            arrayList.remove(0);
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
            for (AddonNode addonNode : arrayList) {
                if (addonNode instanceof ActionNode) {
                    ActionNode actionNode = (ActionNode) addonNode;
                    zMMenuAdapter.addItem(new ActionMenuItem(actionNode.getAction(), actionNode.getValue()));
                }
            }
            ZMAlertDialog create = new Builder(getActivity()).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMThreadsFragment.this.handleActionMsg(str, ((ActionMenuItem) zMMenuAdapter.getItem(i)).getLabelAction());
                    dialogInterface.dismiss();
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    public void onClickTemplateActionMore(View view, final String str, String str2, List<IMessageTemplateActionItem> list) {
        if (getActivity() != null && list != null && !list.isEmpty()) {
            C282537 r6 = new ZMMenuAdapter<ActionMenuItem>(getActivity(), false) {
                /* access modifiers changed from: protected */
                public int getLayoutId() {
                    return C4558R.layout.zm_mm_message_template_popup_item;
                }

                /* access modifiers changed from: protected */
                public void onBindView(@NonNull View view, @NonNull ActionMenuItem actionMenuItem) {
                    TextView textView = (TextView) view.findViewById(C4558R.C4560id.zm_template_popup_item_text);
                    if (actionMenuItem.isDisable()) {
                        view.setBackgroundResource(C4409R.color.zm_ui_kit_color_gray_E4E4ED);
                    } else {
                        view.setBackgroundResource(C4409R.color.zm_ui_kit_color_white_ffffff);
                    }
                    if (textView != null) {
                        textView.setText(actionMenuItem.getLabel());
                    }
                }
            };
            for (IMessageTemplateActionItem iMessageTemplateActionItem : list) {
                ActionMenuItem actionMenuItem = new ActionMenuItem(str2, iMessageTemplateActionItem.getText(), iMessageTemplateActionItem.getValue(), iMessageTemplateActionItem.isDisabled());
                r6.addItem(actionMenuItem);
            }
            final C282638 r0 = new ZMPopupMenu(getActivity(), getActivity(), C4558R.layout.zm_template_popup_menu, r6, view, -1, -2) {
                /* access modifiers changed from: protected */
                public void onMenuItemClick(IZMMenuItem iZMMenuItem) {
                    if (this.mMenuItemClickListener != null) {
                        this.mMenuItemClickListener.onMenuItemClick(iZMMenuItem);
                    }
                }
            };
            r0.setBackgroudResource(C4558R.color.zm_ui_kit_color_white_ffffff);
            r0.setOutSideDark(true);
            String str3 = str;
            r0.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public void onMenuItemClick(IZMMenuItem iZMMenuItem) {
                    if (iZMMenuItem instanceof ActionMenuItem) {
                        ActionMenuItem actionMenuItem = (ActionMenuItem) iZMMenuItem;
                        if (!actionMenuItem.isDisable()) {
                            MMThreadsFragment mMThreadsFragment = MMThreadsFragment.this;
                            mMThreadsFragment.handleTempleActionMsg(mMThreadsFragment.mSessionId, str, actionMenuItem.getLabelAction(), actionMenuItem.getLabel(), actionMenuItem.getValue());
                            r0.dismiss();
                        }
                    }
                }
            });
            r0.show(80, 0, 0);
        }
    }

    public void onClickEditTemplate(String str, String str2, String str3) {
        MMEditTemplateFragment.showAsFragment(this, this.mSessionId, str, str2, str3, 4001);
    }

    public void onClickSelectTemplate(String str, String str2) {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.selectTemplateMessage(str, str2);
        }
    }

    public void onKeyboardOpen() {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView != null) {
            if (this.mAnchorMessageItem == null) {
                mMThreadsRecyclerView.showLatestThreads();
            }
            this.mThreadsRecyclerView.stopScroll();
        }
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.onKeyboardOpen();
        }
        IMSessionSearchFragment iMSessionSearchFragment = this.mSearchFragment;
        if (iMSessionSearchFragment != null && iMSessionSearchFragment.isVisible()) {
            this.mSearchFragment.onKeyboardOpen();
        }
    }

    public void onKeyboardClosed() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.onKeyboardClosed();
        }
        showSearchView(false);
    }

    public void onMessageSent(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId) && this.mAnchorMessageItem == null && !TextUtils.isEmpty(str2)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    ZoomMessage messagePtr = threadDataProvider.getMessagePtr(str, str2);
                    if (messagePtr != null) {
                        this.mThreadsRecyclerView.addNewMessage(messagePtr);
                        this.mThreadsRecyclerView.showLatestThreads();
                        updatePanelNotes();
                        if (!this.mIsE2EChat) {
                            LinkPreviewHelper.doCrawLinkPreview(this.mSessionId, str2, messagePtr.getBody());
                        }
                        showFloatingEmojisIfMatched(messagePtr.getMessageID(), messagePtr.getBody(), messagePtr.getServerSideTime(), false);
                    }
                }
            }
        }
    }

    public void onCommentSent(String str, String str2, String str3) {
        if (TextUtils.equals(str, this.mSessionId) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str2)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    ZoomMessage messagePtr = threadDataProvider.getMessagePtr(str, str3);
                    if (messagePtr != null) {
                        this.mThreadsRecyclerView.addNewMessage(messagePtr);
                        this.mThreadsRecyclerView.showLatestThreads();
                    }
                }
            }
        }
    }

    public void onRecordEnd(String str, long j) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            this.mThreadsRecyclerView.scrollToBottom(true);
            zoomMessenger.sendAudio(this.mGroupId, this.mBuddyId, str, (int) j, this.mIsE2EChat, getString(C4558R.string.zm_msg_e2e_fake_message));
        }
    }

    public void onShowInvitationsSent(int i) {
        if (this.mInvitationsTip == null) {
            inflateInvitationsSentView(i);
        } else {
            showInvitationsSentView(i);
        }
    }

    public void onViewInitReady() {
        if (this.mChatInputFragment != null) {
            this.mVoiceRecordView.initRecordInfo(this, getSessionDataPath(), this.mChatInputFragment.getBtnHoldToTalk());
        }
    }

    public void onEmojiSelect(View view, int i, MMMessageItem mMMessageItem, CharSequence charSequence) {
        String str;
        if (charSequence != null) {
            Long l = (Long) this.mSentReactions.get(charSequence);
            if (l == null || System.currentTimeMillis() - l.longValue() >= 1000) {
                this.mSentReactions.put(charSequence, Long.valueOf(System.currentTimeMillis()));
                boolean z = !checkIfEmojiPraised(mMMessageItem, charSequence);
                if (mMMessageItem == null || !mMMessageItem.isReachReactionLimit()) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                        if (threadDataProvider != null) {
                            if (z) {
                                str = threadDataProvider.addEmojiForMessage(mMMessageItem.sessionId, mMMessageItem.messageXMPPId, threadDataProvider.getEmojiStrKey(charSequence.toString()));
                            } else {
                                str = threadDataProvider.removeEmojiForMessage(mMMessageItem.sessionId, mMMessageItem.messageXMPPId, threadDataProvider.getEmojiStrKey(charSequence.toString()));
                            }
                            if (!StringUtil.isEmptyOrNull(str)) {
                                this.mThreadsRecyclerView.updateMessageEmojiCountInfo(mMMessageItem, false);
                                showFloatingView(view, i, z);
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                showReactionEmojiLimitDialog();
            }
        }
    }

    private void showFloatingView(@Nullable View view, int i, boolean z) {
        FloatingText floatingText = this.mFloatingText;
        if (floatingText != null) {
            floatingText.detachFromWidow();
            this.mFloatingText = null;
        }
        this.mFloatingText = new FloatingText.Builder(getActivity()).setText(z ? "+1" : "-1").setWindowOffset(i).build();
        this.mFloatingText.attachToWindow();
        this.mFloatingText.startFloating(view);
    }

    private void destroyFloatingView() {
        FloatingText floatingText = this.mFloatingText;
        if (floatingText != null) {
            floatingText.detachFromWidow();
            this.mFloatingText = null;
        }
    }

    private void showFloatingEmojisIfMatched(@Nullable String str, @Nullable CharSequence charSequence, long j, boolean z) {
        if (!StringUtil.isEmptyOrNull(str) && !TextUtils.isEmpty(charSequence) && !this.mShowedNewMessages.contains(str)) {
            if (!z || this.mThreadsRecyclerView.isUnreadNewMessage(j)) {
                this.mShowedNewMessages.add(str);
                if (charSequence.toString().toLowerCase().contains("happy birthday") && this.mFloatingEmojisTask == null) {
                    this.mFloatingEmojisTask = new Runnable() {
                        public void run() {
                            MMThreadsFragment.this.showFloatingEmojis();
                            MMThreadsFragment.this.mFloatingEmojisTask = null;
                        }
                    };
                    this.mHandler.postDelayed(this.mFloatingEmojisTask, 300);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void showFloatingEmojis() {
        FloatingEmojis floatingEmojis = this.mFloatingEmojis;
        if (floatingEmojis != null) {
            floatingEmojis.detachFromWidow();
            this.mFloatingEmojis = null;
        }
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            this.mFloatingEmojis = new FloatingEmojis.Builder(getActivity()).addEmoji(C4558R.C4559drawable.zm_ic_cake).build();
            this.mFloatingEmojis.attachToWindow();
            this.mFloatingEmojis.startEmojiRain();
        }
    }

    private void destroyFloatingEmojis() {
        FloatingEmojis floatingEmojis = this.mFloatingEmojis;
        if (floatingEmojis != null) {
            floatingEmojis.detachFromWidow();
            this.mFloatingEmojis = null;
        }
    }

    public void deleteLocalMessage(MMMessageItem mMMessageItem, ZoomChatSession zoomChatSession) {
        zoomChatSession.deleteLocalMessage(mMMessageItem.messageId);
        this.mThreadsRecyclerView.Indicate_MessageDeleted(mMMessageItem.sessionId, mMMessageItem.messageId);
    }

    public void resendMessage(MMMessageItem mMMessageItem) {
        boolean z;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                Resources resources = getResources();
                if (!mMMessageItem.isE2E || mMMessageItem.messageType != 5) {
                    z = sessionById.resendPendingMessage(mMMessageItem.messageId, mMMessageItem.isE2E ? resources.getString(C4558R.string.zm_msg_e2e_fake_message) : "");
                } else if (StringUtil.isEmptyOrNull(mMMessageItem.localFilePath)) {
                    z = false;
                } else {
                    z = sessionById.resendPendingE2EImageMessage(mMMessageItem.messageId, resources.getString(C4558R.string.zm_msg_e2e_fake_message), mMMessageItem.localFilePath);
                }
                if (z) {
                    mMMessageItem.messageState = 1;
                    if (mMMessageItem.messageType == 5 || mMMessageItem.messageType == 28) {
                        addUploadFileRatio(mMMessageItem.messageId, 0);
                    }
                    this.mThreadsRecyclerView.notifyDataSetChanged();
                }
            }
        }
    }

    private void markUnreadMessage(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                if (sessionById != null) {
                    if (sessionById.markMessageAsUnread(mMMessageItem.messageXMPPId)) {
                        updateMessage(mMMessageItem.sessionId, mMMessageItem.messageId);
                        this.mMessageHelper.addVisiableUnreadMsg(mMMessageItem.serverSideTime);
                    }
                    ZoomLogEventTracking.eventTrackMarkUnread(this.mIsGroup);
                }
            }
        }
    }

    private void markAsReadMessage(MMMessageItem mMMessageItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null && sessionById.isMessageMarkUnread(mMMessageItem.messageXMPPId)) {
                if (sessionById.unmarkMessageAsUnread(mMMessageItem.messageXMPPId)) {
                    updateMessage(mMMessageItem.sessionId, mMMessageItem.messageId);
                    this.mMessageHelper.removeVisiableUnreadMsg(mMMessageItem.serverSideTime);
                }
                if (isResumed()) {
                    this.mMessageHelper.resetOldMarkUnreadMsgsItem(mMMessageItem.serverSideTime);
                    updateBottomHint();
                }
            }
        }
    }

    private void joinMeeting(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null && getContext() != null) {
            CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
            if (callHistoryMgr != null) {
                CallHistory callHistoryByID = callHistoryMgr.getCallHistoryByID(mMMessageItem.messageXMPPId);
                if (callHistoryByID != null) {
                    joinMeetingByNO(callHistoryByID.getNumber());
                }
            }
        }
    }

    private void joinMeetingByNO(@NonNull String str) {
        if (!NetworkUtil.hasDataNetwork(getContext())) {
            CannotJoinDialog.show((ZMActivity) getContext(), getResources().getString(C4558R.string.zm_alert_network_disconnected));
            return;
        }
        try {
            checkJoinMeeting(Long.parseLong(str));
        } catch (Exception unused) {
        }
    }

    private void checkJoinMeeting(final long j) {
        Context context = getContext();
        if (context != null) {
            MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(context, new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    MMThreadsFragment.this.startMeeting(j);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startMeeting(long j) {
        Context context = getContext();
        if (context instanceof ZMActivity) {
            ConfActivity.checkExistingCallAndJoinMeeting((ZMActivity) context, j, "", "", "");
        }
    }

    private void copyUrl(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null && getContext() != null) {
            CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
            if (callHistoryMgr != null) {
                String zoomDomain = PTApp.getInstance().getZoomDomain();
                CallHistory callHistoryByID = callHistoryMgr.getCallHistoryByID(mMMessageItem.messageXMPPId);
                if (callHistoryByID != null && zoomDomain != null && !TextUtils.isEmpty(zoomDomain.trim())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(zoomDomain);
                    sb.append("/j/");
                    sb.append(callHistoryByID.getNumber());
                    AndroidAppUtil.copyText(getContext(), sb.toString());
                }
            }
        }
    }

    private void starMessage(MMMessageItem mMMessageItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
            if (sessionById != null && !zoomMessenger.isStarMessage(mMMessageItem.sessionId, mMMessageItem.serverSideTime)) {
                sessionById.starMessage(mMMessageItem.serverSideTime);
            }
        }
    }

    private void unstarMessage(MMMessageItem mMMessageItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
            if (sessionById != null && zoomMessenger.isStarMessage(mMMessageItem.sessionId, mMMessageItem.serverSideTime)) {
                sessionById.discardStarMessage(mMMessageItem.serverSideTime);
            }
        }
    }

    /* access modifiers changed from: private */
    public void autoMarkRead() {
        int childCount = this.mThreadsRecyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mThreadsRecyclerView.getChildAt(i);
            if (childAt instanceof AbsMessageView) {
                MMMessageItem messageItem = ((AbsMessageView) childAt).getMessageItem();
                if (messageItem != null && this.mMessageHelper.isContainInOldMarkUnreads(messageItem.serverSideTime)) {
                    markAsReadMessage(messageItem);
                }
            }
        }
    }

    private void eventTrackShare(int i, boolean z) {
        String str = "";
        switch (i) {
            case 4:
            case 5:
                str = "image";
                break;
            case 10:
            case 11:
                str = BoxFile.TYPE;
                break;
        }
        ZoomLogEventTracking.eventTrackShare(z, str);
    }

    public void eventTrackDeleteMessage(MMMessageItem mMMessageItem, boolean z) {
        if (mMMessageItem != null) {
            String str = "";
            String str2 = "";
            switch (mMMessageItem.messageType) {
                case 0:
                case 1:
                case 34:
                case 35:
                    str = ZMActionMsgUtil.KEY_EVENT;
                    break;
                case 4:
                case 5:
                    str = "image";
                    break;
                case 10:
                case 11:
                    str = BoxFile.TYPE;
                    String fileExtendName = AndroidAppUtil.getFileExtendName(mMMessageItem.localFilePath);
                    if (!StringUtil.isEmptyOrNull(fileExtendName)) {
                        str2 = fileExtendName.replaceAll("[.]", "");
                        break;
                    }
                    break;
                case 27:
                case 28:
                    str = "gif";
                    break;
                case 32:
                case 33:
                    str = "giphy";
                    break;
            }
            ZoomLogEventTracking.eventTrackDeleteMessage(z, str, str2);
        }
    }

    public void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj) {
        if (obj instanceof MMMessageItem) {
            dismissContextMenuDialog();
            dismissReactionEmojiDialog();
            onEmojiSelect(view, i, (MMMessageItem) obj, charSequence);
        }
    }

    public void onMoreReply(View view, MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            requireCommentOnThread(mMMessageItem, false);
        }
    }

    private void dismissContextMenuDialog() {
        ReactionContextMenuDialog reactionContextMenuDialog = this.mContextMenuDialog;
        if (reactionContextMenuDialog != null) {
            reactionContextMenuDialog.dismiss();
            this.mContextMenuDialog = null;
        }
    }

    private void dismissReactionEmojiDialog() {
        ReactionEmojiDialog reactionEmojiDialog = this.mReactionEmojiDialog;
        if (reactionEmojiDialog != null) {
            if (reactionEmojiDialog.isShowing()) {
                this.mReactionEmojiDialog.dismiss();
            }
            this.mReactionEmojiDialog = null;
        }
    }

    private boolean checkIfEmojiPraised(MMMessageItem mMMessageItem, CharSequence charSequence) {
        boolean z = false;
        if (mMMessageItem == null || TextUtils.isEmpty(charSequence)) {
            return false;
        }
        if (mMMessageItem.getEmojiCountItems() == null || mMMessageItem.getEmojiCountItems().size() == 0) {
            this.mThreadsRecyclerView.updateMessageEmojiCountInfo(mMMessageItem, true);
        }
        if (mMMessageItem.getEmojiCountItems() == null) {
            return false;
        }
        Iterator it = mMMessageItem.getEmojiCountItems().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MMCommentsEmojiCountItem mMCommentsEmojiCountItem = (MMCommentsEmojiCountItem) it.next();
            if (!TextUtils.isEmpty(mMCommentsEmojiCountItem.getEmoji()) && mMCommentsEmojiCountItem.getEmoji().equals(charSequence.toString()) && mMCommentsEmojiCountItem.isContainMe()) {
                z = true;
                break;
            }
        }
        return z;
    }

    private boolean closeKeyboardIfOpened(View view) {
        ZMKeyboardDetector zMKeyboardDetector = this.mKeyboardDetector;
        if (zMKeyboardDetector == null || !zMKeyboardDetector.isKeyboardOpen()) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), view);
        return true;
    }

    private void showReactionEmojiLimitDialog() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            new Builder(zMActivity).setTitle(C4558R.string.zm_lbl_reach_reaction_limit_title_88133).setMessage(C4558R.string.zm_lbl_reach_reaction_limit_message_88133).setPositiveButton(C4558R.string.zm_btn_got_it, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    private void updateRefreshStatus(boolean z) {
        SwipeRefreshLayout swipeRefreshLayout = this.mSwipeRefreshLayout;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(z);
        }
        if (!z) {
            MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
            if (mMThreadsRecyclerView != null) {
                mMThreadsRecyclerView.updateEmptyView();
            }
        }
    }

    public void setSelectProgressing(String str, String str2, String str3, List<IMessageTemplateSelectItem> list) {
        if (this.mThreadsRecyclerView != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && TextUtils.equals(this.mSessionId, str)) {
            MMMessageItem findThread = this.mThreadsRecyclerView.findThread(str2);
            if (findThread != null) {
                IZoomMessageTemplate iZoomMessageTemplate = findThread.template;
                if (iZoomMessageTemplate != null) {
                    IMessageTemplateSelect findSelectItem = iZoomMessageTemplate.findSelectItem(str3);
                    if (findSelectItem != null) {
                        findSelectItem.setSelectedItems(list);
                        findSelectItem.setProgressing(true);
                        this.mThreadsRecyclerView.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(@NonNull ZMTemplateSelectProcessingEvent zMTemplateSelectProcessingEvent) {
        setSelectProgressing(zMTemplateSelectProcessingEvent.getSessionID(), zMTemplateSelectProcessingEvent.getMessageID(), zMTemplateSelectProcessingEvent.getEventID(), zMTemplateSelectProcessingEvent.getSelectItems());
        if (zMTemplateSelectProcessingEvent.isResult()) {
            updateMessage(zMTemplateSelectProcessingEvent.getSessionID(), zMTemplateSelectProcessingEvent.getMessageID());
        }
    }

    /* access modifiers changed from: private */
    public void onSelectMeetingNoMenuItem(@Nullable MeetingNoMenuItem meetingNoMenuItem, @Nullable String str) {
        if (meetingNoMenuItem != null && !StringUtil.isEmptyOrNull(str)) {
            switch (meetingNoMenuItem.getAction()) {
                case 0:
                    joinMeetingByNO(str);
                    break;
                case 1:
                    if (!CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
                        AndroidAppUtil.sendDial(getContext(), str);
                        break;
                    } else {
                        click2CallSip(str);
                        break;
                    }
                case 2:
                    AndroidAppUtil.copyText(getContext(), str);
                    Toast.makeText(getContext(), getContext().getString(C4558R.string.zm_msg_link_copied_to_clipboard_91380), 0).show();
                    break;
            }
        }
    }

    private void showSipUnavailable() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            SimpleMessageDialog.newInstance(getString(C4558R.string.zm_sip_error_network_unavailable_99728), false).show(activity.getSupportFragmentManager(), SimpleMessageDialog.class.getSimpleName());
        }
    }

    private void click2CallSip(@NonNull String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (!NetworkUtil.hasDataNetwork(getContext())) {
                showSipUnavailable();
            } else if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    ZMPhoneUtils.callSip(str, null);
                }
            } else {
                this.mSelectedPhoneNumber = str;
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 7001);
            }
        }
    }

    private void showLinkContextMenu(final String str) {
        Activity activity = (Activity) getContext();
        if (activity != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new LinkMenuItem(activity.getString(C4558R.string.zm_mm_lbl_open_link_114679), 0));
            arrayList.add(new LinkMenuItem(activity.getString(C4558R.string.zm_btn_copy), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            TextView textView = new TextView(activity);
            if (VERSION.SDK_INT < 23) {
                textView.setTextAppearance(activity, C4558R.style.ZMTextView_Medium);
            } else {
                textView.setTextAppearance(C4558R.style.ZMTextView_Medium);
            }
            int dip2px = UIUtil.dip2px(activity, 20.0f);
            textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
            textView.setText(str);
            ZMAlertDialog create = new Builder(activity).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMThreadsFragment.this.onSelectLinkMenuItem((LinkMenuItem) zMMenuAdapter.getItem(i), str);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void onSelectLinkMenuItem(@Nullable LinkMenuItem linkMenuItem, @Nullable String str) {
        if (linkMenuItem != null && !StringUtil.isEmptyOrNull(str)) {
            switch (linkMenuItem.getAction()) {
                case 0:
                    ZMIMUtils.openUrl(getContext(), str);
                    break;
                case 1:
                    AndroidAppUtil.copyText(getContext(), str);
                    Toast.makeText(getContext(), getContext().getString(C4558R.string.zm_msg_link_copied_to_clipboard_91380), 0).show();
                    break;
            }
        }
    }
}
