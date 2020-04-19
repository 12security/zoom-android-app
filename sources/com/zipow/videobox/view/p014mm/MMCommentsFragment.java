package com.zipow.videobox.view.p014mm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import com.box.androidsdk.content.models.BoxFile;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.MMCommentActivity;
import com.zipow.videobox.MMCommentActivity.ThreadUnreadInfo;
import com.zipow.videobox.MMImageListActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.eventbus.ZMChatSession;
import com.zipow.videobox.eventbus.ZMFileAction;
import com.zipow.videobox.eventbus.ZMStarEvent;
import com.zipow.videobox.fragment.FileTransferFragment;
import com.zipow.videobox.fragment.MMChatInputFragment;
import com.zipow.videobox.fragment.MMChatInputFragment.OnChatInputListener;
import com.zipow.videobox.fragment.MMEditMessageFragment;
import com.zipow.videobox.fragment.MMSelectSessionAndBuddyFragment;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI.ICrawlerLinkPreviewUIListener;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI.SimpleCrawlerLinkPreviewUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMCallbackUI.IIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI.SimpleIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMProtos.CommentDataResult;
import com.zipow.videobox.ptapp.IMProtos.CommentInfo;
import com.zipow.videobox.ptapp.IMProtos.CrawlLinkResponse;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.ptapp.IMProtos.MessageInfo;
import com.zipow.videobox.ptapp.IMProtos.MessageInfoList;
import com.zipow.videobox.ptapp.IMProtos.MessageInput;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfo;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfoMap;
import com.zipow.videobox.ptapp.IMProtos.ThrCommentState;
import com.zipow.videobox.ptapp.IMProtos.ThrCommentStates;
import com.zipow.videobox.ptapp.IMProtos.ThreadDataResult;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.ThreadDataUI;
import com.zipow.videobox.ptapp.ThreadDataUI.IThreadDataUIListener;
import com.zipow.videobox.ptapp.ThreadDataUI.SimpleThreadDataUIListener;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistory;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.tempbean.IMessageTemplateActionItem;
import com.zipow.videobox.util.ChatImgSaveHelper;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LinkPreviewHelper;
import com.zipow.videobox.util.MMMessageHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMActionMsgUtil;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.FloatingEmojis;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.JoinConfView.CannotJoinDialog;
import com.zipow.videobox.view.floatingtext.FloatingText;
import com.zipow.videobox.view.p014mm.MMAddonMessage.AddonNode;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeMsgHref;
import com.zipow.videobox.view.p014mm.MMContentMessageItem.MMContentMessageAnchorInfo;
import com.zipow.videobox.view.p014mm.MMThreadsRecyclerView.ThreadsUICallBack;
import com.zipow.videobox.view.p014mm.ReactionEmojiDialog.Builder;
import com.zipow.videobox.view.p014mm.ReactionEmojiDialog.OnReactionEmojiListener;
import com.zipow.videobox.view.p014mm.VoiceRecordView.OnVoiceRecordListener;
import com.zipow.videobox.view.p014mm.message.FontStyle;
import com.zipow.videobox.view.p014mm.message.MessageContextMenuItem;
import com.zipow.videobox.view.p014mm.message.ReactionContextMenuDialog;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.io.File;
import java.io.FileInputStream;
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
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.ZMToast;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMCommentsFragment */
public class MMCommentsFragment extends ZMDialogFragment implements OnClickListener, ThreadsUICallBack, OnVoiceRecordListener, KeyboardListener, OnChatInputListener, ExtListener, SensorEventListener {
    private static final String ARG_ANCHOR_MSG = "anchorMsg";
    private static final String ARG_BUDDY_ID = "buddyId";
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_FORWARD_MESSAGE_ID = "forward_message_id";
    private static final String ARG_GROUP_ID = "groupId";
    private static final String ARG_IS_GROUP = "isGroup";
    private static final String ARG_SEND_INTENT = "sendIntent";
    private static final String ARG_SHARED_MESSAGE_ID = "messageid";
    private static final String ARG_THREAD_ID = "threadId";
    private static final String ARG_THREAD_SVR = "threadSvr";
    private static final String ARG_THREAD_UNREAD_INFO = "ThreadUnreadInfo";
    private static final int E2E_HINT_TYPE_DEFAULT = 0;
    private static final int E2E_HINT_TYPE_TRY_LATER = 2;
    private static final int E2E_HINT_TYPE_USER_CLOSE = 3;
    private static final int E2E_HINT_TYPE_WAIT_ONLINE = 1;
    public static final int REQUEST_CODE_SAVE_EMOJI = 6001;
    public static final int REQUEST_CODE_SAVE_FILE_IMAGE = 5002;
    public static final int REQUEST_CODE_SAVE_IMAGE = 5001;
    private static final int REQUEST_DO_FORWARD_MESSAGE = 115;
    private static final int REQUEST_EDIT_MESSAGE = 4001;
    private static final int REQUEST_GET_FORWARD_MESSAGE = 114;
    private static final int REQUEST_GET_SHAREES = 109;
    public static final int REQUEST_JUMP_MARKUNREAD = 116;
    private static final int REQUEST_PERMISSION_MIC = 7001;
    private static final String TAG = "MMCommentsFragment";
    /* access modifiers changed from: private */
    public MMContentMessageAnchorInfo mAnchorMessageItem;
    /* access modifiers changed from: private */
    public Set<String> mAtAllListFromSyncHint = new HashSet();
    /* access modifiers changed from: private */
    public ArrayList<String> mAtListFromSyncHint = new ArrayList<>();
    private Set<String> mAtMeListFromSyncHint = new HashSet();
    /* access modifiers changed from: private */
    public Runnable mAutoMarkReadRunnable = new Runnable() {
        public void run() {
            MMCommentsFragment.this.autoMarkRead();
        }
    };
    private View mBtnJump;
    /* access modifiers changed from: private */
    public String mBuddyId;
    /* access modifiers changed from: private */
    public Map<MMMessageItem, Long> mCacheMsgs = new HashMap();
    /* access modifiers changed from: private */
    public MMChatInputFragment mChatInputFragment;
    private Runnable mCheckShowedMsgTask = new Runnable() {
        public void run() {
            if (MMCommentsFragment.this.mCommentsRecyclerView.isLayoutReady() && MMCommentsFragment.this.mCacheMsgs.size() > 0) {
                Iterator it = MMCommentsFragment.this.mCacheMsgs.entrySet().iterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    Long l = (Long) entry.getValue();
                    MMMessageItem mMMessageItem = (MMMessageItem) entry.getKey();
                    if (mMMessageItem == null || l == null) {
                        it.remove();
                    } else if (System.currentTimeMillis() - l.longValue() >= 500) {
                        it.remove();
                        if (!StringUtil.isEmptyOrNull(mMMessageItem.messageId) && MMCommentsFragment.this.mCommentsRecyclerView.isMsgShow(mMMessageItem.messageId)) {
                            boolean z = false;
                            if (MMCommentsFragment.this.mAtListFromSyncHint != null && MMCommentsFragment.this.mAtAllListFromSyncHint.remove(mMMessageItem.messageId)) {
                                z = true;
                            }
                            if (TextUtils.equals(mMMessageItem.messageId, MMCommentsFragment.this.mNewMsgMarkId)) {
                                MMCommentsFragment.this.mNewMsgMarkId = null;
                                z = true;
                            }
                            if (mMMessageItem.isUnread) {
                                z = true;
                            }
                            if (z) {
                                MMCommentsFragment.this.updateBottomHint();
                            }
                        }
                    }
                }
            }
            MMCommentsFragment.this.mHandler.postDelayed(this, 100);
        }
    };
    /* access modifiers changed from: private */
    public MMCommentsRecyclerView mCommentsRecyclerView;
    private ReactionContextMenuDialog mContextMenuDialog;
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
            MMCommentsFragment.this.OnLinkCrawlResult(crawlLinkResponse);
        }

        public void OnDownloadFavicon(int i, String str) {
            MMCommentsFragment.this.OnDownloadFavicon(i, str);
        }

        public void OnDownloadImage(int i, String str) {
            MMCommentsFragment.this.OnDownloadImage(i, str);
        }
    };
    private IMAddrBookItem mIMAddrBookItem;
    private IIMCallbackUIListener mIMCallbackUIListener = new SimpleIMCallbackUIListener() {
        public void OnUnsupportMessageRecevied(int i, String str, String str2, String str3) {
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsE2EChat;
    /* access modifiers changed from: private */
    public boolean mIsGroup = false;
    private boolean mIsMyNostes = false;
    private boolean mIsThreadReactionChanged = false;
    /* access modifiers changed from: private */
    public ZMKeyboardDetector mKeyboardDetector;
    private ArrayList<String> mMarkUnreadMessages = new ArrayList<>();
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    private IZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_DownloadFileByUrlIml(String str, int i) {
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
        }

        public void onNotify_SessionMarkUnreadCtx(String str, int i, String str2, List<String> list) {
        }

        public void onBeginConnect() {
            MMCommentsFragment.this.onBeginConnect();
        }

        public void onConnectReturn(int i) {
            MMCommentsFragment.this.onConnectReturn(i);
        }

        public boolean onIndicateMessageReceived(String str, String str2, String str3) {
            return MMCommentsFragment.this.onIndicateMessageReceived(str, str2, str3);
        }

        public void onConfirm_MessageSent(String str, String str2, int i) {
            MMCommentsFragment.this.onConfirm_MessageSent(str, str2, i);
        }

        public void onNotify_ChatSessionUpdate(String str) {
            MMCommentsFragment.this.onNotify_ChatSessionUpdate(str);
        }

        public void onConfirmPreviewPicFileDownloaded(String str, String str2, int i) {
            MMCommentsFragment.this.onConfirmPreviewPicFileDownloaded(str, str2, i);
        }

        public void FT_OnSent(String str, String str2, int i) {
            MMCommentsFragment.this.FT_OnSent(str, str2, i);
        }

        public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
            MMCommentsFragment.this.FT_OnProgress(str, str2, i, j, j2);
        }

        public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
            MMCommentsFragment.this.FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
        }

        public void FT_OnResumed(String str, String str2, int i) {
            MMCommentsFragment.this.FT_OnResumed(str, str2, i);
        }

        public void E2E_MessageStateUpdate(String str, String str2, int i) {
            MMCommentsFragment.this.E2E_MessageStateUpdate(str, str2, i);
        }

        public void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5) {
            MMCommentsFragment.this.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }

        public void Indicate_MessageDeleted(String str, String str2) {
            MMCommentsFragment.this.Indicate_MessageDeleted(str, str2);
        }

        public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
            MMCommentsFragment.this.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }

        public void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i) {
            MMCommentsFragment.this.Indicate_FileForwarded(str, str2, str3, str4, i);
        }

        public void onConfirmFileDownloaded(String str, String str2, int i) {
            MMCommentsFragment.this.onConfirmFileDownloaded(str, str2, i);
        }

        public void Indicate_FileDownloaded(String str, String str2, int i) {
            MMCommentsFragment.this.Indicate_FileDownloaded(str, str2, i);
        }

        public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
            MMCommentsFragment.this.FT_OnDownloadByMsgIDTimeOut(str, str2);
        }

        public void FT_UploadFileInChatTimeOut(String str, String str2) {
            MMCommentsFragment.this.FT_UploadFileInChatTimeOut(str, str2);
        }

        public void Indicate_BlockedUsersUpdated() {
            MMCommentsFragment.this.Indicate_BlockedUsersUpdated();
        }

        public void Indicate_BlockedUsersAdded(List<String> list) {
            MMCommentsFragment.this.Indicate_BlockedUsersAdded(list);
        }

        public void Indicate_BlockedUsersRemoved(List<String> list) {
            MMCommentsFragment.this.Indicate_BlockedUsersRemoved(list);
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            MMCommentsFragment.this.On_DestroyGroup(i, str, str2, str3, j);
        }

        public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, long j2, boolean z) {
            MMCommentsFragment.this.Indicate_RevokeMessageResult(str, str2, str3, str4, j2, z);
        }

        public void Notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
            MMCommentsFragment.this.notify_ChatSessionMarkUnreadUpdate(sessionMessageInfoMap);
        }

        public void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
            MMCommentsFragment.this.Indicate_EditMessageResultIml(str, str2, str3, j, j2, z);
        }

        public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
            MMCommentsFragment.this.NotifyOutdatedHistoryRemoved(list, j);
        }

        public void NotifyChatUnavailable(String str, String str2) {
            if (!MMCommentsFragment.this.mIsGroup && !TextUtils.isEmpty(str) && MMCommentsFragment.this.mSessionId.equals(str)) {
                MMCommentsFragment.this.onIndicateMessageReceived(str, null, str2);
            }
        }

        public void NotifyCallUnavailable(String str, long j) {
            if (MMCommentsFragment.this.mSessionId.equals(str)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    if (buddyWithJID != null) {
                        String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                        ZMActivity zMActivity = (ZMActivity) MMCommentsFragment.this.getActivity();
                        if (zMActivity != null) {
                            ZMToast.show((Context) zMActivity, (CharSequence) String.format(MMCommentsFragment.this.getString(C4558R.string.zm_mm_lbl_xxx_declined_the_call_62107), new Object[]{buddyDisplayName}), 1);
                        }
                    }
                }
                ZmPtUtils.onCallError(j);
            }
        }

        public void NotifyDeleteMsgFailed(String str, String str2) {
            ZMActivity zMActivity = (ZMActivity) MMCommentsFragment.this.getActivity();
            if (zMActivity != null && zMActivity.isActive()) {
                Toast.makeText(zMActivity, str2, 1).show();
            }
        }

        public void notify_StarMessageDataUpdate() {
            if (MMCommentsFragment.this.mCommentsRecyclerView != null) {
                MMCommentsFragment.this.mCommentsRecyclerView.refreshStarMsgs();
                MMCommentsFragment.this.mCommentsRecyclerView.notifyDataSetChanged();
            }
        }

        public void onRemoveBuddy(String str, int i) {
            if (i == 0 && !MMCommentsFragment.this.mIsGroup && StringUtil.isSameString(str, MMCommentsFragment.this.mBuddyId)) {
                MMCommentsFragment.this.dismiss();
            }
        }

        public void Indicate_FileMessageDeleted(String str, String str2) {
            MMCommentsFragment.this.Indicate_FileMessageDeleted(str, str2);
        }

        public void Indicate_BuddyPresenceChanged(String str) {
            MMCommentsFragment.this.onIndicate_BuddyPresenceChanged(str);
        }
    };
    /* access modifiers changed from: private */
    public String mNewMsgMarkId;
    private ArrayList<MessageInfo> mOldMarkUnreadMessages = null;
    private int mOldVolume = -1;
    private View mPanelBottomHint;
    private View mPanelE2EHint;
    /* access modifiers changed from: private */
    public String mPendingPlayMsgId;
    private HashMap<String, Integer> mPendingUploadFileRatios = new HashMap<>();
    /* access modifiers changed from: private */
    public MMMessageItem mPlayingMessage;
    private ReactionEmojiDialog mReactionEmojiDialog;
    private File mSavingEmoji;
    private File mSavingFile;
    private MMMessageItem mSavingMsg;
    private String mSelectedPhoneNumber = null;
    private Map<CharSequence, Long> mSentReactions = new HashMap();
    /* access modifiers changed from: private */
    public String mSessionId;
    private Set<String> mShowedNewMessages = new HashSet();
    /* access modifiers changed from: private */
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private IThreadDataUIListener mThreadDataUIListener = new SimpleThreadDataUIListener() {
        public void OnGetCommentData(CommentDataResult commentDataResult) {
            MMCommentsFragment.this.OnGetCommentData(commentDataResult);
        }

        public void OnThreadContextUpdate(String str, String str2) {
            MMCommentsFragment.this.OnThreadContextUpdate(str, str2);
        }

        public void OnThreadContextSynced(String str, String str2, String str3) {
            MMCommentsFragment.this.OnThreadContextSynced(str, str2, str3);
        }

        public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
            MMCommentsFragment.this.OnFetchEmojiDetailInfo(str, str2, str3, str4, z);
        }

        public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
            MMCommentsFragment.this.OnFetchEmojiCountInfo(str, str2, list, z);
        }

        public void OnMessageEmojiInfoUpdated(String str, String str2) {
            MMCommentsFragment.this.OnMessageEmojiInfoUpdated(str, str2);
        }

        public void OnEmojiCountInfoLoadedFromDB(String str) {
            MMCommentsFragment.this.OnEmojiCountInfoLoadedFromDB(str);
        }

        public void OnMSGDBExistence(String str, String str2, String str3, boolean z) {
            MMCommentsFragment.this.onMSGDBExistence(str2, str3, z);
        }

        public void OnGetThreadData(ThreadDataResult threadDataResult) {
            MMCommentsFragment.this.OnGetThreadData(threadDataResult);
        }
    };
    private String mThreadId;
    /* access modifiers changed from: private */
    public MMMessageItem mThreadItem;
    private int mThreadSortType;
    private long mThreadSvr;
    private View mTitleBar;
    private TextView mTxtBottomHint;
    private TextView mTxtDisableMsg;
    private TextView mTxtE2EHintMsg;
    private TextView mTxtMarkUnread;
    private TextView mTxtMention;
    private TextView mTxtNewMsgMark;
    private TextView mTxtTitle;
    private Runnable mUnSupportEmojiRunnable = new Runnable() {
        public void run() {
            UnSupportEmojiDialog.show((ZMActivity) MMCommentsFragment.this.getActivity());
        }
    };
    /* access modifiers changed from: private */
    public ThreadUnreadInfo mUnreadInfo;
    /* access modifiers changed from: private */
    public int mUnreadReplyCount;
    private VoiceRecordView mVoiceRecordView;
    private int mVolumeChangedTo = -1;
    /* access modifiers changed from: private */
    public ProgressDialog mWaitingDialog;
    /* access modifiers changed from: private */
    public String mWaitingDialogId;
    private boolean mbVolumeChanged = false;
    private View panelActions;

    /* renamed from: com.zipow.videobox.view.mm.MMCommentsFragment$LinkMenuItem */
    static class LinkMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 1;
        public static final int ACTION_OPEN_LINK = 0;

        public LinkMenuItem(String str, int i) {
            super(i, str);
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMCommentsFragment$MeetingNoMenuItem */
    static class MeetingNoMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_CALL = 1;
        public static final int ACTION_COPY = 2;
        public static final int ACTION_JOIN_MEETING = 0;

        public MeetingNoMenuItem(String str, int i) {
            super(i, str);
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMCommentsFragment$MessageSendErrorMenuItem */
    static class MessageSendErrorMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_DELETE_MESSAGE = 1;
        public static final int ACTION_TRY = 0;

        public MessageSendErrorMenuItem(String str, int i) {
            super(i, str);
        }
    }

    /* access modifiers changed from: private */
    public void OnThreadContextUpdate(String str, String str2) {
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onAddComment(MMMessageItem mMMessageItem) {
    }

    public void onClickAction(String str, String str2) {
    }

    public void onClickActionMore(String str, List<AddonNode> list) {
    }

    public void onClickEditTemplate(String str, String str2, String str3) {
    }

    public void onClickGiphyBtn(MMMessageItem mMMessageItem, View view) {
    }

    public void onClickSelectTemplate(String str, String str2) {
    }

    public void onClickTemplateActionMore(View view, String str, String str2, List<IMessageTemplateActionItem> list) {
    }

    public void onHideComment(MMMessageItem mMMessageItem) {
    }

    public void onJumpResult(boolean z) {
    }

    public void onMoreComment(MMMessageItem mMMessageItem) {
    }

    public void onMoreReply(View view, MMMessageItem mMMessageItem) {
    }

    public void onPbxSmsSent(String str, String str2) {
    }

    public void onSayHi() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public void onShowInvitationsSent(int i) {
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static MMCommentsFragment findMMCommentsFragment(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        return (MMCommentsFragment) fragmentManager.findFragmentByTag(MMCommentsFragment.class.getName());
    }

    public static void showMsgContextInActivity(Fragment fragment, MMContentMessageAnchorInfo mMContentMessageAnchorInfo) {
        showMsgContextInActivity(fragment, mMContentMessageAnchorInfo, null, 0);
    }

    public static void showMsgContextInActivity(Fragment fragment, MMContentMessageAnchorInfo mMContentMessageAnchorInfo, ThreadUnreadInfo threadUnreadInfo, int i) {
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
                    MMCommentsFragment mMCommentsFragment = new MMCommentsFragment();
                    Bundle bundle = new Bundle();
                    if (z) {
                        bundle.putString(ARG_GROUP_ID, sessionId);
                        bundle.putBoolean(ARG_IS_GROUP, true);
                    } else {
                        bundle.putSerializable(ARG_CONTACT, iMAddrBookItem);
                        bundle.putString(ARG_BUDDY_ID, sessionId);
                    }
                    bundle.putSerializable("anchorMsg", mMContentMessageAnchorInfo);
                    if (threadUnreadInfo != null) {
                        bundle.putSerializable(ARG_THREAD_UNREAD_INFO, threadUnreadInfo);
                    }
                    mMCommentsFragment.setArguments(bundle);
                    SimpleActivity.show(fragment, MMCommentsFragment.class.getName(), bundle, i);
                }
            }
        }
    }

    public static void showAsGroupChatInActivity(ZMActivity zMActivity, String str, String str2, long j, Intent intent, ThreadUnreadInfo threadUnreadInfo) {
        if (zMActivity != null && str != null && (str2 != null || j != 0)) {
            MMCommentsFragment mMCommentsFragment = new MMCommentsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_GROUP_ID, str);
            bundle.putString("threadId", str2);
            bundle.putBoolean(ARG_IS_GROUP, true);
            bundle.putParcelable(ARG_SEND_INTENT, intent);
            bundle.putLong(ARG_THREAD_SVR, j);
            if (threadUnreadInfo != null) {
                bundle.putSerializable(ARG_THREAD_UNREAD_INFO, threadUnreadInfo);
            }
            mMCommentsFragment.setArguments(bundle);
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, mMCommentsFragment, MMCommentsFragment.class.getName()).commit();
        }
    }

    public static void showAsOneToOneInActivity(ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem, String str, String str2, long j, Intent intent, ThreadUnreadInfo threadUnreadInfo) {
        if (zMActivity != null && str != null && (str2 != null || j != 0)) {
            MMCommentsFragment mMCommentsFragment = new MMCommentsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_CONTACT, iMAddrBookItem);
            bundle.putString(ARG_BUDDY_ID, str);
            bundle.putString("threadId", str2);
            bundle.putBoolean(ARG_IS_GROUP, false);
            bundle.putParcelable(ARG_SEND_INTENT, intent);
            bundle.putLong(ARG_THREAD_SVR, j);
            if (threadUnreadInfo != null) {
                bundle.putSerializable(ARG_THREAD_UNREAD_INFO, threadUnreadInfo);
            }
            mMCommentsFragment.setArguments(bundle);
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, mMCommentsFragment, MMCommentsFragment.class.getName()).commit();
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_comment, viewGroup, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mGroupId = arguments.getString(ARG_GROUP_ID);
            this.mBuddyId = arguments.getString(ARG_BUDDY_ID);
            this.mIsGroup = arguments.getBoolean(ARG_IS_GROUP);
        }
        this.mTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mKeyboardDetector = (ZMKeyboardDetector) inflate.findViewById(C4558R.C4560id.keyboardDetector);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mCommentsRecyclerView = (MMCommentsRecyclerView) inflate.findViewById(C4558R.C4560id.commentsRecyclerView);
        this.panelActions = inflate.findViewById(C4558R.C4560id.panelActions);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(C4558R.C4560id.swipeRefreshLayout);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mPanelBottomHint = inflate.findViewById(C4558R.C4560id.panelBottomHint);
        this.mTxtMarkUnread = (TextView) inflate.findViewById(C4558R.C4560id.txtMarkUnread);
        this.mTxtMention = (TextView) inflate.findViewById(C4558R.C4560id.txtMention);
        this.mTxtNewMsgMark = (TextView) inflate.findViewById(C4558R.C4560id.txtNewMsgMark);
        this.mTxtBottomHint = (TextView) inflate.findViewById(C4558R.C4560id.txtBottomHint);
        this.mPanelE2EHint = inflate.findViewById(C4558R.C4560id.panelE2EHint);
        this.mTxtE2EHintMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtE2EHintMsg);
        this.mBtnJump = inflate.findViewById(C4558R.C4560id.btnJump);
        this.mTxtDisableMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtDisableMsg);
        this.mVoiceRecordView = (VoiceRecordView) inflate.findViewById(C4558R.C4560id.panelVoiceRcdHint);
        this.mCommentsRecyclerView.setUICallBack(this);
        this.mKeyboardDetector.setKeyboardListener(this);
        this.mTxtBottomHint.setOnClickListener(this);
        this.mTxtMarkUnread.setOnClickListener(this);
        this.mTxtMention.setOnClickListener(this);
        this.mTxtNewMsgMark.setOnClickListener(this);
        this.mBtnJump.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnE2EHintClose).setOnClickListener(this);
        this.mVoiceRecordView.setVisibility(8);
        CrawlerLinkPreviewUI.getInstance().addListener(this.mICrawlerLinkPreviewUIListener);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
        IMCallbackUI.getInstance().addListener(this.mIMCallbackUIListener);
        ThreadDataUI.getInstance().addListener(this.mThreadDataUIListener);
        EventBus.getDefault().register(this);
        this.mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                MMCommentsFragment.this.mSwipeRefreshLayout.setRefreshing(false);
                MMCommentsFragment.this.loadMoreComments();
            }
        });
        this.mCommentsRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                if (i == 0) {
                    if (MMCommentsFragment.this.mIsE2EChat) {
                        if (PTApp.getInstance().getZoomMessenger() != null) {
                            if (MMCommentsFragment.this.mCommentsRecyclerView.tryDecryptVisiableE2EMesssage()) {
                                MMCommentsFragment.this.showE2EMessageDecryptTimeoutHint();
                            } else {
                                MMCommentsFragment.this.closeE2EMessageDecryptTimeoutHint();
                            }
                        } else {
                            return;
                        }
                    }
                    MMCommentsFragment.this.updateBottomHint();
                } else if (MMCommentsFragment.this.mKeyboardDetector.isKeyboardOpen()) {
                    UIUtil.closeSoftKeyboard(MMCommentsFragment.this.getActivity(), MMCommentsFragment.this.mCommentsRecyclerView);
                }
            }

            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                MMCommentsFragment.this.mHandler.removeCallbacks(MMCommentsFragment.this.mAutoMarkReadRunnable);
                MMCommentsFragment.this.mHandler.postDelayed(MMCommentsFragment.this.mAutoMarkReadRunnable, 1000);
                if (MMCommentsFragment.this.mAnchorMessageItem == null && !MMCommentsFragment.this.mCommentsRecyclerView.isLocalDataDirty() && MMCommentsFragment.this.mCommentsRecyclerView.isAtBottom()) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(MMCommentsFragment.this.mSessionId);
                        if (!(sessionById == null || MMCommentsFragment.this.mThreadItem == null)) {
                            sessionById.cleanUnreadCommentsForThread(MMCommentsFragment.this.mThreadItem.serverSideTime);
                        }
                    }
                    MMCommentsFragment.this.mUnreadReplyCount = 0;
                }
            }
        });
        return inflate;
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

    public String getTitle() {
        if (this.mIsGroup) {
            return getGroupTitle();
        }
        return getBuddyName();
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
        } else if (groupById.getBuddyCount() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(groupName);
            sb.append(" (");
            sb.append(groupById.getBuddyCount());
            sb.append(")");
            groupName = sb.toString();
        }
        return groupName;
    }

    public void updateTitleBar() {
        if (isAdded()) {
            if (this.mAnchorMessageItem != null) {
                this.mBtnJump.setVisibility(0);
            }
            switch (ZoomMessengerUI.getInstance().getConnectionStatus()) {
                case -1:
                case 0:
                case 1:
                    TextView textView = this.mTxtTitle;
                    if (textView != null) {
                        textView.setText(C4558R.string.zm_title_replies_88133);
                        break;
                    }
                    break;
                case 2:
                    TextView textView2 = this.mTxtTitle;
                    if (textView2 != null) {
                        textView2.setText(C4558R.string.zm_mm_title_chats_connecting);
                        break;
                    }
                    break;
            }
            TextView textView3 = this.mTxtTitle;
            if (textView3 != null) {
                textView3.requestLayout();
            }
        }
    }

    public boolean onBackPressed() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null && mMChatInputFragment.onBackPressed()) {
            return true;
        }
        checkIfThreadReactionChanged();
        checkLoadingSuccessWhenExit();
        return false;
    }

    public void onStart() {
        super.onStart();
        this.mCommentsRecyclerView.setIsShow(true);
        this.mCommentsRecyclerView.refreshStarMsgs();
        updateTitleBar();
    }

    public void onStop() {
        this.mCommentsRecyclerView.setIsShow(false);
        super.onStop();
    }

    /* access modifiers changed from: private */
    public void E2E_MessageStateUpdate(String str, String str2, int i) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mCommentsRecyclerView.e2eMessageStateUpdate(str, str2, i);
            if ((i == 11 || i == 13) && this.mCommentsRecyclerView.isMsgShow(str2)) {
                showE2EMessageDecryptTimeoutHint();
            } else if (this.mE2EHintType != 3 && this.mCommentsRecyclerView.isAllVisiableMessageDecrypted()) {
                closeE2EMessageDecryptTimeoutHint();
            }
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnSent(String str, String str2, int i) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            this.mPendingUploadFileRatios.remove(str2);
            if (this.mCommentsRecyclerView != null) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null && zMActivity.isActive()) {
                    ZMIMUtils.axAnnounceForAccessibility(this.mCommentsRecyclerView, getString(C4558R.string.zm_msg_file_state_uploaded_69051));
                }
                this.mCommentsRecyclerView.FT_OnSent(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
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
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.FT_OnProgress(str, str2, i, j, j2);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FT_FileOP(ZMFileAction zMFileAction) {
        String sessionID = zMFileAction.getSessionID();
        String messageID = zMFileAction.getMessageID();
        int action = zMFileAction.getAction();
        if (StringUtil.isSameString(sessionID, this.mSessionId)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                if (action == 2) {
                    mMCommentsRecyclerView.FT_OnCancel(sessionID, messageID);
                } else if (action == 1) {
                    mMCommentsRecyclerView.FT_OnPaused(sessionID, messageID);
                } else if (action == 3) {
                    mMCommentsRecyclerView.FT_OnDownloaded(sessionID, messageID);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnResumed(String str, String str2, int i) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.FT_OnResumed(str, str2, i);
            }
        }
    }

    public void onKeyboardOpen() {
        if (this.mAnchorMessageItem == null) {
            this.mCommentsRecyclerView.scrollToBottom(true);
        }
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            if (this.mAnchorMessageItem == null) {
                mMCommentsRecyclerView.showLatestComments();
            }
            this.mCommentsRecyclerView.stopScroll();
        }
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.onKeyboardOpen();
        }
    }

    public void onKeyboardClosed() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.onKeyboardClosed();
        }
    }

    public void onDestroyView() {
        ThreadDataUI.getInstance().removeListener(this.mThreadDataUIListener);
        IMCallbackUI.getInstance().removeListener(this.mIMCallbackUIListener);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        CrawlerLinkPreviewUI.getInstance().removeListener(this.mICrawlerLinkPreviewUIListener);
        EventBus.getDefault().unregister(this);
        destroyFloatingView();
        destroyFloatingEmojis();
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZMStarEvent zMStarEvent) {
        if (isAdded()) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.refreshStarMsgs();
                this.mCommentsRecyclerView.notifyDataSetChanged();
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

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getBuddyName() {
        /*
            r4 = this;
            boolean r0 = r4.mIsGroup
            r1 = 0
            if (r0 != 0) goto L_0x004b
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r0 = r0.getZoomMessenger()
            if (r0 == 0) goto L_0x0027
            java.lang.String r2 = r4.mBuddyId
            com.zipow.videobox.ptapp.mm.ZoomBuddy r0 = r0.getBuddyWithJID(r2)
            if (r0 == 0) goto L_0x001e
            com.zipow.videobox.view.IMAddrBookItem r2 = r4.mIMAddrBookItem
            java.lang.String r0 = com.zipow.videobox.ptapp.p013mm.BuddyNameUtil.getBuddyDisplayName(r0, r2)
            goto L_0x0028
        L_0x001e:
            com.zipow.videobox.view.IMAddrBookItem r0 = r4.mIMAddrBookItem
            if (r0 == 0) goto L_0x0027
            java.lang.String r0 = r0.getScreenName()
            goto L_0x0028
        L_0x0027:
            r0 = r1
        L_0x0028:
            if (r0 == 0) goto L_0x002b
            r1 = r0
        L_0x002b:
            com.zipow.videobox.view.IMAddrBookItem r2 = r4.mIMAddrBookItem
            if (r2 == 0) goto L_0x003b
            boolean r2 = r2.isZoomRoomContact()
            if (r2 == 0) goto L_0x003b
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_title_zoom_room_prex
            java.lang.String r1 = r4.getString(r1)
        L_0x003b:
            boolean r2 = r4.mIsMyNostes
            if (r2 == 0) goto L_0x004b
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_mm_msg_my_notes_65147
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r0
            java.lang.String r1 = r4.getString(r1, r2)
        L_0x004b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsFragment.getBuddyName():java.lang.String");
    }

    public void checkEditMessage(@NonNull String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str);
                if (messageByXMPPGuid != null) {
                    if (messageByXMPPGuid.isMessageAtEveryone() || messageByXMPPGuid.isMessageAtMe()) {
                        if (!this.mAtListFromSyncHint.contains(str)) {
                            this.mAtListFromSyncHint.add(str);
                        }
                    } else if (this.mAtListFromSyncHint.contains(str)) {
                        this.mAtListFromSyncHint.remove(str);
                    }
                    if (messageByXMPPGuid.isMessageAtMe()) {
                        this.mAtMeListFromSyncHint.add(str);
                    } else {
                        this.mAtMeListFromSyncHint.remove(str);
                    }
                    if (messageByXMPPGuid.isMessageAtEveryone()) {
                        this.mAtAllListFromSyncHint.add(str);
                    } else {
                        this.mAtAllListFromSyncHint.remove(str);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x0226  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x014c  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x018f  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0196  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateBottomHint() {
        /*
            r13 = this;
            boolean r0 = r13.isAdded()
            if (r0 == 0) goto L_0x0232
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r0 = r13.mCommentsRecyclerView
            boolean r0 = r0.isLayoutReady()
            if (r0 == 0) goto L_0x0232
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r0 = r13.mCommentsRecyclerView
            boolean r0 = r0.isDataEmpty()
            if (r0 == 0) goto L_0x0018
            goto L_0x0232
        L_0x0018:
            android.content.Context r0 = r13.getContext()
            if (r0 != 0) goto L_0x001f
            return
        L_0x001f:
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r13.mAnchorMessageItem
            r2 = 8
            if (r1 != 0) goto L_0x022c
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r1 = r13.mCommentsRecyclerView
            if (r1 != 0) goto L_0x002b
            goto L_0x022c
        L_0x002b:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r1 = r1.getZoomMessenger()
            if (r1 != 0) goto L_0x0036
            return
        L_0x0036:
            java.lang.String r3 = r13.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r1 = r1.getSessionById(r3)
            if (r1 != 0) goto L_0x003f
            return
        L_0x003f:
            android.view.View r3 = r13.mPanelBottomHint
            r4 = 0
            r3.setVisibility(r4)
            java.util.ArrayList<com.zipow.videobox.ptapp.IMProtos$MessageInfo> r3 = r13.mOldMarkUnreadMessages
            boolean r3 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r3)
            r5 = 2
            r6 = 1
            r7 = 0
            if (r3 == 0) goto L_0x0057
            android.widget.TextView r3 = r13.mTxtMarkUnread
            r3.setVisibility(r2)
            r8 = 0
            goto L_0x00bc
        L_0x0057:
            r3 = 0
            r8 = 0
        L_0x0059:
            java.util.ArrayList<com.zipow.videobox.ptapp.IMProtos$MessageInfo> r9 = r13.mOldMarkUnreadMessages
            int r9 = r9.size()
            if (r3 >= r9) goto L_0x0079
            java.util.ArrayList<com.zipow.videobox.ptapp.IMProtos$MessageInfo> r8 = r13.mOldMarkUnreadMessages
            java.lang.Object r8 = r8.get(r3)
            com.zipow.videobox.ptapp.IMProtos$MessageInfo r8 = (com.zipow.videobox.ptapp.IMProtos.MessageInfo) r8
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r9 = r13.mCommentsRecyclerView
            long r10 = r8.getSvrTime()
            int r8 = r9.getMsgDirection(r10)
            if (r8 == 0) goto L_0x0076
            goto L_0x0079
        L_0x0076:
            int r3 = r3 + 1
            goto L_0x0059
        L_0x0079:
            if (r8 != 0) goto L_0x0081
            android.widget.TextView r3 = r13.mTxtMarkUnread
            r3.setVisibility(r2)
            goto L_0x00bc
        L_0x0081:
            android.widget.TextView r3 = r13.mTxtMarkUnread
            r3.setVisibility(r4)
            if (r8 != r5) goto L_0x0094
            android.widget.TextView r3 = r13.mTxtMarkUnread
            int r9 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_arrow_white_down
            android.graphics.drawable.Drawable r9 = androidx.core.content.ContextCompat.getDrawable(r0, r9)
            r3.setCompoundDrawablesWithIntrinsicBounds(r7, r7, r9, r7)
            goto L_0x009f
        L_0x0094:
            android.widget.TextView r3 = r13.mTxtMarkUnread
            int r9 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_arrow_white_up
            android.graphics.drawable.Drawable r9 = androidx.core.content.ContextCompat.getDrawable(r0, r9)
            r3.setCompoundDrawablesWithIntrinsicBounds(r7, r7, r9, r7)
        L_0x009f:
            java.util.ArrayList<com.zipow.videobox.ptapp.IMProtos$MessageInfo> r3 = r13.mOldMarkUnreadMessages
            int r3 = r3.size()
            android.content.res.Resources r9 = r13.getResources()
            int r10 = p021us.zoom.videomeetings.C4558R.plurals.zm_lbl_mark_unread_150170
            java.lang.Object[] r11 = new java.lang.Object[r6]
            java.lang.Integer r12 = java.lang.Integer.valueOf(r3)
            r11[r4] = r12
            java.lang.String r3 = r9.getQuantityString(r10, r3, r11)
            android.widget.TextView r9 = r13.mTxtMarkUnread
            r9.setText(r3)
        L_0x00bc:
            java.util.ArrayList<java.lang.String> r3 = r13.mAtListFromSyncHint
            boolean r3 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r3)
            if (r3 == 0) goto L_0x00ca
            android.widget.TextView r3 = r13.mTxtMention
            r3.setVisibility(r2)
            goto L_0x0101
        L_0x00ca:
            java.util.ArrayList<java.lang.String> r3 = r13.mAtListFromSyncHint
            java.util.Iterator r3 = r3.iterator()
        L_0x00d0:
            boolean r9 = r3.hasNext()
            if (r9 == 0) goto L_0x00f5
            java.lang.Object r9 = r3.next()
            java.lang.String r9 = (java.lang.String) r9
            boolean r10 = android.text.TextUtils.isEmpty(r9)
            if (r10 == 0) goto L_0x00e6
            r3.remove()
            goto L_0x00d0
        L_0x00e6:
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r8 = r13.mCommentsRecyclerView
            int r8 = r8.getMsgDirection(r9)
            if (r8 == 0) goto L_0x00f1
            r10 = -1
            if (r8 != r10) goto L_0x00f6
        L_0x00f1:
            r3.remove()
            goto L_0x00d0
        L_0x00f5:
            r9 = r7
        L_0x00f6:
            boolean r3 = android.text.TextUtils.isEmpty(r9)
            if (r3 == 0) goto L_0x0103
            android.widget.TextView r3 = r13.mTxtMention
            r3.setVisibility(r2)
        L_0x0101:
            r3 = 0
            goto L_0x014a
        L_0x0103:
            if (r8 != r5) goto L_0x0111
            android.widget.TextView r3 = r13.mTxtMention
            int r8 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_arrow_white_down
            android.graphics.drawable.Drawable r8 = androidx.core.content.ContextCompat.getDrawable(r0, r8)
            r3.setCompoundDrawablesWithIntrinsicBounds(r7, r7, r8, r7)
            goto L_0x011c
        L_0x0111:
            android.widget.TextView r3 = r13.mTxtMention
            int r8 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_arrow_white_up
            android.graphics.drawable.Drawable r8 = androidx.core.content.ContextCompat.getDrawable(r0, r8)
            r3.setCompoundDrawablesWithIntrinsicBounds(r7, r7, r8, r7)
        L_0x011c:
            java.util.ArrayList<java.lang.String> r3 = r13.mAtListFromSyncHint
            int r3 = r3.size()
            android.content.res.Resources r8 = r13.getResources()
            java.util.Set<java.lang.String> r10 = r13.mAtMeListFromSyncHint
            boolean r9 = r10.contains(r9)
            if (r9 == 0) goto L_0x0131
            int r9 = p021us.zoom.videomeetings.C4558R.plurals.zm_lbl_message_at_me_150170
            goto L_0x0133
        L_0x0131:
            int r9 = p021us.zoom.videomeetings.C4558R.plurals.zm_lbl_message_at_all_150170
        L_0x0133:
            java.lang.Object[] r10 = new java.lang.Object[r6]
            java.lang.Integer r11 = java.lang.Integer.valueOf(r3)
            r10[r4] = r11
            java.lang.String r3 = r8.getQuantityString(r9, r3, r10)
            android.widget.TextView r8 = r13.mTxtMention
            r8.setText(r3)
            android.widget.TextView r3 = r13.mTxtMention
            r3.setVisibility(r4)
            r3 = 1
        L_0x014a:
            if (r3 != 0) goto L_0x018f
            int r8 = r13.mUnreadReplyCount
            long r8 = (long) r8
            int r10 = r13.mThreadSortType
            if (r10 != r6) goto L_0x015d
            com.zipow.videobox.view.mm.MMMessageItem r10 = r13.mThreadItem
            if (r10 == 0) goto L_0x015d
            long r8 = r10.serverSideTime
            long r8 = r1.getUnreadCommentCount(r8)
        L_0x015d:
            r10 = 0
            int r1 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r1 != 0) goto L_0x0169
            android.widget.TextView r1 = r13.mTxtBottomHint
            r1.setVisibility(r2)
            goto L_0x0194
        L_0x0169:
            android.widget.TextView r1 = r13.mTxtBottomHint
            r1.setVisibility(r4)
            android.widget.TextView r1 = r13.mTxtBottomHint
            r10 = 99
            int r3 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r3 <= 0) goto L_0x0179
            java.lang.String r3 = "99+"
            goto L_0x018a
        L_0x0179:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r8)
            java.lang.String r8 = ""
            r3.append(r8)
            java.lang.String r3 = r3.toString()
        L_0x018a:
            r1.setText(r3)
            r3 = 1
            goto L_0x0194
        L_0x018f:
            android.widget.TextView r1 = r13.mTxtBottomHint
            r1.setVisibility(r2)
        L_0x0194:
            if (r3 != 0) goto L_0x0226
            java.lang.String r1 = r13.mNewMsgMarkId
            java.lang.String r3 = "LAST_MSG_MARK_MSGID"
            boolean r1 = android.text.TextUtils.equals(r1, r3)
            if (r1 == 0) goto L_0x01b2
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r1 = r13.mCommentsRecyclerView
            boolean r1 = r1.isAtBottom()
            if (r1 == 0) goto L_0x01b2
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r1 = r13.mCommentsRecyclerView
            boolean r1 = r1.isLocalDataDirty()
            if (r1 != 0) goto L_0x01b2
            r13.mNewMsgMarkId = r7
        L_0x01b2:
            java.lang.String r1 = r13.mNewMsgMarkId
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x01c0
            android.widget.TextView r0 = r13.mTxtNewMsgMark
            r0.setVisibility(r2)
            goto L_0x022b
        L_0x01c0:
            java.lang.String r1 = r13.mNewMsgMarkId
            java.lang.String r3 = "MSGID_NEW_comment_MARK_ID"
            boolean r1 = android.text.TextUtils.equals(r1, r3)
            if (r1 == 0) goto L_0x01d4
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r1 = r13.mCommentsRecyclerView
            boolean r1 = r1.isCommentHistoryReady()
            if (r1 != 0) goto L_0x01d4
            r1 = 1
            goto L_0x01d5
        L_0x01d4:
            r1 = 0
        L_0x01d5:
            java.lang.String r3 = r13.mNewMsgMarkId
            java.lang.String r8 = "LAST_MSG_MARK_MSGID"
            boolean r3 = android.text.TextUtils.equals(r3, r8)
            if (r3 == 0) goto L_0x01e0
            r1 = 2
        L_0x01e0:
            if (r1 != 0) goto L_0x01ea
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r1 = r13.mCommentsRecyclerView
            java.lang.String r3 = r13.mNewMsgMarkId
            int r1 = r1.getMsgDirection(r3)
        L_0x01ea:
            if (r1 != r6) goto L_0x0204
            android.widget.TextView r1 = r13.mTxtNewMsgMark
            r1.setVisibility(r4)
            android.widget.TextView r1 = r13.mTxtNewMsgMark
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_jump_first_68444
            r1.setText(r2)
            android.widget.TextView r1 = r13.mTxtNewMsgMark
            int r2 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_arrow_red_up
            android.graphics.drawable.Drawable r0 = androidx.core.content.ContextCompat.getDrawable(r0, r2)
            r1.setCompoundDrawablesWithIntrinsicBounds(r7, r7, r0, r7)
            goto L_0x022b
        L_0x0204:
            if (r1 != r5) goto L_0x021e
            android.widget.TextView r1 = r13.mTxtNewMsgMark
            r1.setVisibility(r4)
            android.widget.TextView r1 = r13.mTxtNewMsgMark
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_jump_latest_68444
            r1.setText(r2)
            android.widget.TextView r1 = r13.mTxtNewMsgMark
            int r2 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_arrow_red_down
            android.graphics.drawable.Drawable r0 = androidx.core.content.ContextCompat.getDrawable(r0, r2)
            r1.setCompoundDrawablesWithIntrinsicBounds(r7, r7, r0, r7)
            goto L_0x022b
        L_0x021e:
            r13.mNewMsgMarkId = r7
            android.widget.TextView r0 = r13.mTxtNewMsgMark
            r0.setVisibility(r2)
            goto L_0x022b
        L_0x0226:
            android.widget.TextView r0 = r13.mTxtNewMsgMark
            r0.setVisibility(r2)
        L_0x022b:
            return
        L_0x022c:
            android.view.View r0 = r13.mPanelBottomHint
            r0.setVisibility(r2)
            return
        L_0x0232:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsFragment.updateBottomHint():void");
    }

    private void initInputFragment() {
        if ((!TextUtils.isEmpty(this.mSessionId) || this.mChatInputFragment != null) && this.mThreadItem != null) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                this.mChatInputFragment = new MMChatInputFragment();
                this.mChatInputFragment.setOnChatInputListener(this);
                this.mChatInputFragment.setKeyboardDetector(this.mKeyboardDetector);
                Bundle bundle = new Bundle();
                bundle.putString("sessionId", this.mSessionId);
                bundle.putString("threadId", this.mThreadId);
                this.mChatInputFragment.setArguments(bundle);
                beginTransaction.add(C4558R.C4560id.panelActions, (Fragment) this.mChatInputFragment);
                beginTransaction.commit();
            }
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments == null) {
            dismiss();
            return;
        }
        this.mIsGroup = arguments.getBoolean(ARG_IS_GROUP);
        this.mIMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
        this.mBuddyId = arguments.getString(ARG_BUDDY_ID);
        this.mGroupId = arguments.getString(ARG_GROUP_ID);
        this.mSessionId = this.mIsGroup ? this.mGroupId : this.mBuddyId;
        this.mAnchorMessageItem = (MMContentMessageAnchorInfo) arguments.getSerializable("anchorMsg");
        this.mIsMyNostes = UIMgr.isMyNotes(this.mSessionId);
        this.mThreadId = arguments.getString("threadId");
        this.mThreadSvr = arguments.getLong(ARG_THREAD_SVR, 0);
        this.mUnreadInfo = (ThreadUnreadInfo) arguments.getSerializable(ARG_THREAD_UNREAD_INFO);
        MMContentMessageAnchorInfo mMContentMessageAnchorInfo = this.mAnchorMessageItem;
        if (mMContentMessageAnchorInfo != null) {
            this.mCommentsRecyclerView.setAnchorMessageItem(mMContentMessageAnchorInfo);
            this.panelActions.setVisibility(8);
            this.mThreadId = this.mAnchorMessageItem.getThrId();
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                this.mThreadSortType = threadDataProvider.getThreadSortType();
                checkE2EStatus();
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself == null) {
                    finishFragment(false);
                    return;
                }
                ThreadUnreadInfo threadUnreadInfo = this.mUnreadInfo;
                if (threadUnreadInfo != null) {
                    this.mOldMarkUnreadMessages = threadUnreadInfo.mMarkUnreadMsgs;
                    if (this.mUnreadInfo.mAtMeMsgIds != null) {
                        this.mAtMeListFromSyncHint = new HashSet(this.mUnreadInfo.mAtMeMsgIds);
                    }
                    if (this.mUnreadInfo.mAtAllMsgIds != null) {
                        this.mAtAllListFromSyncHint = new HashSet(this.mUnreadInfo.mAtAllMsgIds);
                    }
                    this.mAtListFromSyncHint = this.mUnreadInfo.mAtMsgIds == null ? new ArrayList<>() : this.mUnreadInfo.mAtMsgIds;
                }
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mIsGroup ? this.mGroupId : this.mBuddyId);
                if (sessionById != null) {
                    this.mSessionId = sessionById.getSessionId();
                    ZoomMessage messageById = sessionById.getMessageById(this.mThreadId);
                    if (messageById != null) {
                        this.mThreadItem = MMMessageItem.initWithZoomMessage(messageById, this.mSessionId, zoomMessenger, this.mIsGroup, TextUtils.equals(messageById.getSenderID(), myself.getJid()), getContext(), this.mIMAddrBookItem, null);
                        ThreadUnreadInfo threadUnreadInfo2 = this.mUnreadInfo;
                        if (threadUnreadInfo2 != null && threadUnreadInfo2.unreadCount == 0) {
                            this.mUnreadInfo.unreadCount = (int) sessionById.getUnreadCommentCount(messageById.getServerSideTime());
                        }
                        if (this.mAnchorMessageItem == null && this.mThreadSortType == 1) {
                            sessionById.cleanUnreadCommentsForThread(messageById.getServerSideTime());
                            this.mUnreadReplyCount = 0;
                        }
                    }
                    this.mCommentsRecyclerView.setUnreadInfo(this.mUnreadInfo);
                    if (this.mThreadItem == null && TextUtils.isEmpty(this.mThreadId) && this.mThreadSvr == 0) {
                        finishFragment(false);
                        return;
                    }
                    this.mCommentsRecyclerView.setThreadInfo(this.mSessionId, this.mThreadItem, this.mIsGroup, this.mThreadId, this.mThreadSvr);
                    initInputFragment();
                    return;
                }
                finishFragment(false);
            }
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 116) {
            if (i2 == 0 && intent != null) {
                MMContentMessageAnchorInfo mMContentMessageAnchorInfo = (MMContentMessageAnchorInfo) intent.getSerializableExtra("anchorMsg");
                if (mMContentMessageAnchorInfo != null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                        if (sessionById != null) {
                            sessionById.unmarkMessageAsUnread(mMContentMessageAnchorInfo.getMsgGuid());
                            if (!CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
                                Iterator it = this.mOldMarkUnreadMessages.iterator();
                                while (it.hasNext()) {
                                    if (TextUtils.equals(((MessageInfo) it.next()).getGuid(), mMContentMessageAnchorInfo.getMsgGuid())) {
                                        it.remove();
                                    }
                                }
                                updateBottomHint();
                            }
                        }
                    }
                }
            }
        } else if (i == 114 && i2 == -1 && intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String string = extras.getString(ARG_FORWARD_MESSAGE_ID);
                if (!StringUtil.isEmptyOrNull(string)) {
                    String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                    if (!StringUtil.isEmptyOrNull(stringExtra)) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(stringExtra);
                        if (arrayList.size() > 0) {
                            doForwardMessage(arrayList, string);
                        }
                    }
                }
            }
        } else if (i == 109 && i2 == -1 && intent != null) {
            Bundle extras2 = intent.getExtras();
            if (extras2 != null) {
                String string2 = extras2.getString(ARG_SHARED_MESSAGE_ID);
                if (!StringUtil.isEmptyOrNull(string2)) {
                    String stringExtra2 = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                    if (!StringUtil.isEmptyOrNull(stringExtra2)) {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(stringExtra2);
                        if (arrayList2.size() > 0) {
                            doShareFile(arrayList2, string2);
                        }
                    }
                }
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

    /* access modifiers changed from: private */
    public void onConfirm_MessageSent(String str, String str2, int i) {
        if (!StringUtil.isEmptyOrNull(this.mSessionId) && this.mSessionId.equals(str) && !StringUtil.isEmptyOrNull(str2)) {
            onMessageSent(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.Indicate_FileShared(str, str2, str3, str4, str5, i);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.Indicate_FileForwarded(str, str2, str3, str4, i);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileDownloaded(String str, String str2, int i) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.Indicate_FileDownloaded(str, str2, i);
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.FT_OnDownloadByMsgIDTimeOut(str, str2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void FT_UploadFileInChatTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.FT_UploadFileInChatTimeOut(str, str2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_MessageDeleted(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null && mMCommentsRecyclerView.deleteMessage(str2)) {
                updateTitleBar();
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.Indicate_FileActionStatus(i, str, str2, str3, str4, str5);
        }
    }

    /* access modifiers changed from: private */
    public void On_DestroyGroup(final int i, String str, final String str2, String str3, long j) {
        if (StringUtil.isSameString(str2, this.mSessionId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("DestroyGroup") {
                public void run(IUIElement iUIElement) {
                    if (StringUtil.isSameString(str2, MMCommentsFragment.this.mGroupId) && i == 0) {
                        MMCommentsFragment.this.dismiss();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, boolean z) {
        if (StringUtil.isSameString(str2, this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    if (z) {
                        if (!TextUtils.equals(str3, this.mThreadId) || !this.mCommentsRecyclerView.isDataEmpty() || this.mCommentsRecyclerView.isLoading(1) || this.mCommentsRecyclerView.isLoading(2)) {
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
                            if (sessionById != null) {
                                this.mCommentsRecyclerView.onRecallMessage(true, sessionById.getMessageById(str4), str3);
                            }
                        } else {
                            dismiss();
                        }
                    } else if (StringUtil.isSameString(myself.getJid(), str)) {
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction("RevokeMessageFailed") {
                            public void run(IUIElement iUIElement) {
                                SimpleMessageDialog.newInstance(C4558R.string.zm_mm_lbl_delete_failed_64189).show(MMCommentsFragment.this.getFragmentManager(), "RevokeMessage");
                            }
                        });
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_FileMessageDeleted(String str, String str2) {
        if (!TextUtils.equals(str2, this.mThreadId) || !this.mCommentsRecyclerView.isDataEmpty() || this.mCommentsRecyclerView.isLoading(1) || this.mCommentsRecyclerView.isLoading(2)) {
            if (TextUtils.equals(str, this.mSessionId)) {
                this.mCommentsRecyclerView.Indicate_FileMessageDeleted(str, str2);
            }
            return;
        }
        dismiss();
    }

    /* access modifiers changed from: private */
    public void notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
        if (sessionMessageInfoMap != null && sessionMessageInfoMap.getInfosCount() != 0 && !CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            Iterator it = sessionMessageInfoMap.getInfosList().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SessionMessageInfo sessionMessageInfo = (SessionMessageInfo) it.next();
                if (TextUtils.equals(sessionMessageInfo.getSession(), this.mSessionId)) {
                    boolean z = false;
                    Iterator it2 = sessionMessageInfo.getInfoList().getInfoListList().iterator();
                    if (it2.hasNext() && TextUtils.equals(((MessageInfo) it2.next()).getThr(), this.mThreadId)) {
                        z = true;
                    }
                    MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
                    if (mMCommentsRecyclerView != null && z) {
                        mMCommentsRecyclerView.updateVisibleMessageState();
                        updateBottomHint();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
        if (StringUtil.isSameString(str2, this.mSessionId)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.updateMessage(str3);
                checkEditMessage(str3);
                updateBottomHint();
            }
        }
    }

    public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
        if (list != null && list.contains(this.mSessionId)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.NotifyOutdatedHistoryRemoved(j);
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersUpdated() {
        updateDisableMsg();
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersAdded(List<String> list) {
        if (!StringUtil.isEmptyOrNull(this.mBuddyId) && list.contains(this.mBuddyId)) {
            updateDisableMsg();
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_BlockedUsersRemoved(List<String> list) {
        if (!StringUtil.isEmptyOrNull(this.mBuddyId) && list.contains(this.mBuddyId)) {
            updateDisableMsg();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicate_BuddyPresenceChanged(String str) {
        if (TextUtils.equals(str, this.mSessionId)) {
            checkE2EStatus();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        tryDecryptVisiableE2EMesssageWhenBuddyOnline(arrayList);
    }

    private void tryDecryptVisiableE2EMesssageWhenBuddyOnline(List<String> list) {
        if (this.mIsE2EChat && !this.mHasAutoDecryptWhenBuddyOnline && !CollectionsUtil.isListEmpty(list)) {
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null && mMCommentsRecyclerView.hasVisiableMessageDecryptedTimeout()) {
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
                    this.mCommentsRecyclerView.tryDecryptVisiableE2EMesssage();
                }
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

    public void onResume() {
        super.onResume();
        updateDisableMsg();
        this.mHandler.postDelayed(this.mCheckShowedMsgTask, 100);
        this.mCommentsRecyclerView.loadComments(true);
        if (this.mCommentsRecyclerView.isLoading() && this.mCommentsRecyclerView.isDataEmpty()) {
            this.mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    public void onPause() {
        this.mHandler.removeCallbacks(this.mCheckShowedMsgTask);
        dismissContextMenuDialog();
        dismissReactionEmojiDialog();
        destroyFloatingEmojis();
        super.onPause();
    }

    public void checkAllShowMsgs() {
        List<MMMessageItem> allShowMsgs = this.mCommentsRecyclerView.getAllShowMsgs();
        if (!CollectionsUtil.isCollectionEmpty(allShowMsgs)) {
            for (MMMessageItem onMessageShowed : allShowMsgs) {
                onMessageShowed(onMessageShowed);
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            dismiss();
        } else if (id == C4558R.C4560id.txtBottomHint) {
            onClickTxtBottomHint();
        } else if (id == C4558R.C4560id.txtMarkUnread) {
            onClickTxtMarkUnread();
        } else if (id == C4558R.C4560id.txtNewMsgMark) {
            onClickTxtNewMsgMark();
        } else if (id == C4558R.C4560id.txtMention) {
            onClickTxtMention();
        } else if (id == C4558R.C4560id.btnE2EHintClose) {
            onClickBtnE2EHintClose();
        } else if (id == C4558R.C4560id.btnJump) {
            onClickBtnJump();
        }
    }

    public void dismiss() {
        checkIfThreadReactionChanged();
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        checkLoadingSuccessWhenExit();
        finishFragment(true);
    }

    private void checkIfThreadReactionChanged() {
        if (this.mThreadItem != null && this.mIsThreadReactionChanged) {
            EventBus.getDefault().post(new ZMChatSession(this.mThreadItem.sessionId, this.mThreadItem.messageXMPPId, 3));
            this.mIsThreadReactionChanged = false;
        }
    }

    private void onClickBtnE2EHintClose() {
        closeE2EMessageDecryptTimeoutHint();
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkLoadingSuccessWhenExit() {
        /*
            r4 = this;
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            java.lang.String r1 = "threadId"
            java.lang.String r2 = r4.mThreadId
            r0.putExtra(r1, r2)
            java.util.ArrayList<java.lang.String> r1 = r4.mMarkUnreadMessages
            boolean r1 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r1)
            if (r1 != 0) goto L_0x001b
            java.lang.String r1 = "UNREADMSGS"
            java.util.ArrayList<java.lang.String> r2 = r4.mMarkUnreadMessages
            r0.putExtra(r1, r2)
        L_0x001b:
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r4.mAnchorMessageItem
            if (r1 == 0) goto L_0x003e
            java.lang.String r2 = "anchorMsg"
            r0.putExtra(r2, r1)
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r1 = r4.mCommentsRecyclerView
            boolean r1 = r1.isDataEmpty()
            if (r1 == 0) goto L_0x002e
            r1 = 1
            goto L_0x003f
        L_0x002e:
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r1 = r4.mCommentsRecyclerView
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r2 = r4.mAnchorMessageItem
            long r2 = r2.getServerTime()
            com.zipow.videobox.view.mm.MMMessageItem r1 = r1.findMessageItemByStamp(r2)
            if (r1 != 0) goto L_0x003e
            r1 = 0
            goto L_0x003f
        L_0x003e:
            r1 = -1
        L_0x003f:
            androidx.fragment.app.FragmentActivity r2 = r4.getActivity()
            if (r2 == 0) goto L_0x0048
            r2.setResult(r1, r0)
        L_0x0048:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsFragment.checkLoadingSuccessWhenExit():void");
    }

    private void onClickBtnJump() {
        if (!StringUtil.isEmptyOrNull(this.mThreadId) && !StringUtil.isEmptyOrNull(this.mSessionId)) {
            ThreadUnreadInfo threadUnreadInfo = new ThreadUnreadInfo();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(this.mThreadId);
                    if (messageById != null) {
                        List unreadAtAllMessages = sessionById.getUnreadAtAllMessages();
                        if (!CollectionsUtil.isCollectionEmpty(unreadAtAllMessages)) {
                            threadUnreadInfo.mAtAllMsgIds = new ArrayList<>(unreadAtAllMessages);
                        }
                        List unreadAtMeMessages = sessionById.getUnreadAtMeMessages();
                        if (!CollectionsUtil.isCollectionEmpty(unreadAtMeMessages)) {
                            threadUnreadInfo.mAtMeMsgIds = new ArrayList<>(unreadAtMeMessages);
                        }
                        List unreadAllMentionedMessages = sessionById.getUnreadAllMentionedMessages();
                        if (!CollectionsUtil.isCollectionEmpty(unreadAllMentionedMessages)) {
                            threadUnreadInfo.mAtMsgIds = new ArrayList<>(unreadAllMentionedMessages);
                        }
                        MessageInfoList markUnreadMessages = sessionById.getMarkUnreadMessages();
                        if (markUnreadMessages != null && markUnreadMessages.getInfoListCount() > 0) {
                            threadUnreadInfo.mMarkUnreadMsgs = new ArrayList<>();
                            for (MessageInfo messageInfo : markUnreadMessages.getInfoListList()) {
                                if (TextUtils.equals(messageInfo.getThr(), this.mThreadId)) {
                                    threadUnreadInfo.mMarkUnreadMsgs.add(messageInfo);
                                }
                            }
                        }
                        ThrCommentStates sessionUnreadCommentCount = sessionById.getSessionUnreadCommentCount();
                        if (sessionUnreadCommentCount != null && sessionUnreadCommentCount.getStatesCount() > 0) {
                            long serverSideTime = messageById.getServerSideTime();
                            Iterator it = sessionUnreadCommentCount.getStatesList().iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                ThrCommentState thrCommentState = (ThrCommentState) it.next();
                                if (thrCommentState.getThrT() == serverSideTime) {
                                    threadUnreadInfo.readTime = thrCommentState.getReadTime();
                                    threadUnreadInfo.unreadCount = (int) thrCommentState.getUnreadCommentCount();
                                    break;
                                }
                            }
                        }
                        if (this.mIsGroup) {
                            MMCommentActivity.showAsGroupChat(this, this.mGroupId, this.mThreadId, null, threadUnreadInfo, 0);
                        } else {
                            MMCommentActivity.showAsOneToOneChat((Fragment) this, this.mIMAddrBookItem, this.mBuddyId, this.mThreadId, threadUnreadInfo, 0);
                        }
                    }
                }
            }
        }
    }

    private void onClickTxtMarkUnread() {
        if (!CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            int i = 0;
            while (true) {
                if (i >= this.mOldMarkUnreadMessages.size()) {
                    break;
                }
                MessageInfo messageInfo = (MessageInfo) this.mOldMarkUnreadMessages.get(i);
                long svrTime = messageInfo.getSvrTime();
                if (this.mCommentsRecyclerView.getMsgDirection(svrTime) == 0) {
                    i++;
                } else if (this.mCommentsRecyclerView.scrollToMessage(svrTime)) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            MMCommentsFragment.this.checkAllShowMsgs();
                            MMCommentsFragment.this.updateBottomHint();
                        }
                    });
                } else {
                    MMContentMessageAnchorInfo mMContentMessageAnchorInfo = new MMContentMessageAnchorInfo();
                    mMContentMessageAnchorInfo.setThrSvr(svrTime);
                    mMContentMessageAnchorInfo.setThrId(messageInfo.getThr());
                    mMContentMessageAnchorInfo.setComment(true);
                    mMContentMessageAnchorInfo.setMsgGuid(messageInfo.getGuid());
                    mMContentMessageAnchorInfo.setSendTime(messageInfo.getSvrTime());
                    mMContentMessageAnchorInfo.setServerTime(messageInfo.getSvrTime());
                    mMContentMessageAnchorInfo.setFromMarkUnread(true);
                    mMContentMessageAnchorInfo.setmType(1);
                    mMContentMessageAnchorInfo.setSessionId(this.mSessionId);
                    ThreadUnreadInfo threadUnreadInfo = new ThreadUnreadInfo();
                    threadUnreadInfo.mMarkUnreadMsgs = this.mOldMarkUnreadMessages;
                    showMsgContextInActivity(this, mMContentMessageAnchorInfo, threadUnreadInfo, 116);
                }
            }
            if (CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
                this.mTxtMarkUnread.setVisibility(8);
            }
        }
    }

    private void onClickTxtMention() {
        if (!CollectionsUtil.isListEmpty(this.mAtListFromSyncHint)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.getSessionById(this.mSessionId) != null) {
                while (this.mAtListFromSyncHint.size() > 0) {
                    String str = (String) this.mAtListFromSyncHint.remove(0);
                    int msgDirection = this.mCommentsRecyclerView.getMsgDirection(str);
                    if (msgDirection != 0 && msgDirection != -1 && this.mCommentsRecyclerView.scrollToMessage(str)) {
                        this.mHandler.post(new Runnable() {
                            public void run() {
                                MMCommentsFragment.this.checkAllShowMsgs();
                                MMCommentsFragment.this.updateBottomHint();
                            }
                        });
                        return;
                    }
                }
                if (CollectionsUtil.isListEmpty(this.mAtListFromSyncHint)) {
                    this.mTxtMention.setVisibility(8);
                }
            }
        }
    }

    private void onClickTxtNewMsgMark() {
        if (TextUtils.isEmpty(this.mNewMsgMarkId)) {
            this.mTxtNewMsgMark.setVisibility(8);
            return;
        }
        int msgDirection = this.mCommentsRecyclerView.getMsgDirection(this.mNewMsgMarkId);
        if (msgDirection == 0) {
            this.mTxtNewMsgMark.setVisibility(8);
            return;
        }
        if (msgDirection == 2 || TextUtils.equals(this.mNewMsgMarkId, MMMessageItem.LAST_MSG_MARK_MSGID)) {
            if (this.mCommentsRecyclerView.isLocalDataDirty()) {
                this.mCommentsRecyclerView.loadComments(false, true);
                if (this.mCommentsRecyclerView.isLoading(1) || this.mCommentsRecyclerView.isLoading(2)) {
                    SwipeRefreshLayout swipeRefreshLayout = this.mSwipeRefreshLayout;
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
            } else {
                this.mCommentsRecyclerView.scrollToBottom(true);
            }
        } else if (!this.mCommentsRecyclerView.scrollToMessage(this.mNewMsgMarkId)) {
            this.mCommentsRecyclerView.loadComments(false, false, this.mNewMsgMarkId);
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                MMCommentsFragment.this.checkAllShowMsgs();
                MMCommentsFragment.this.updateBottomHint();
            }
        });
        this.mTxtNewMsgMark.setVisibility(8);
        this.mNewMsgMarkId = null;
    }

    private void onClickTxtBottomHint() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                if (this.mCommentsRecyclerView.isLocalDataDirty()) {
                    this.mCommentsRecyclerView.loadComments(false, true);
                } else {
                    this.mCommentsRecyclerView.scrollToBottom(true);
                }
                MMMessageItem mMMessageItem = this.mThreadItem;
                if (mMMessageItem != null && this.mThreadSortType == 1) {
                    sessionById.cleanUnreadCommentsForThread(mMMessageItem.serverSideTime);
                    this.mUnreadReplyCount = 0;
                }
                this.mTxtBottomHint.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnLinkCrawlResult(CrawlLinkResponse crawlLinkResponse) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.updateMessage(crawlLinkResponse.getMsgGuid());
            if (this.mCommentsRecyclerView.isAtBottom()) {
                this.mCommentsRecyclerView.scrollToBottom(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnDownloadFavicon(int i, String str) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.OnUpdateLinkCrawl(i, str);
        }
    }

    /* access modifiers changed from: private */
    public void OnDownloadImage(int i, String str) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.OnUpdateLinkCrawl(i, str);
        }
    }

    public void onMessageSent(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId) && !TextUtils.isEmpty(str2)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    ZoomMessage messagePtr = threadDataProvider.getMessagePtr(str, str2);
                    if (messagePtr != null && messagePtr.isComment() && TextUtils.equals(messagePtr.getThreadID(), this.mThreadId)) {
                        updateTitleBar();
                        this.mCommentsRecyclerView.updateMessage(messagePtr, false);
                        this.mCommentsRecyclerView.scrollToBottom(false);
                        if (!this.mIsE2EChat) {
                            LinkPreviewHelper.doCrawLinkPreview(this.mSessionId, str2, messagePtr.getBody());
                        }
                        showFloatingEmojisIfMatched(messagePtr.getMessageID(), messagePtr.getBody(), messagePtr.getServerSideTime(), false);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnGetCommentData(CommentDataResult commentDataResult) {
        ZoomMessage zoomMessage;
        if (commentDataResult != null && TextUtils.equals(commentDataResult.getChannel(), this.mSessionId) && this.mCommentsRecyclerView.OnGetCommentData(commentDataResult)) {
            boolean isLoading = this.mCommentsRecyclerView.isLoading(commentDataResult.getDir());
            this.mSwipeRefreshLayout.setRefreshing(this.mCommentsRecyclerView.isDataEmpty() && isLoading);
            if (!isLoading && this.mThreadItem == null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                    if (threadDataProvider != null) {
                        long j = this.mThreadSvr;
                        if (j == 0) {
                            zoomMessage = threadDataProvider.getMessagePtr(this.mSessionId, this.mThreadId);
                        } else {
                            zoomMessage = threadDataProvider.getMessagePtr(this.mSessionId, j);
                        }
                        if (zoomMessage != null) {
                            ZoomBuddy myself = zoomMessenger.getMyself();
                            if (myself != null) {
                                this.mThreadItem = MMMessageItem.initWithZoomMessage(zoomMessage, this.mSessionId, zoomMessenger, this.mIsGroup, TextUtils.equals(zoomMessage.getSenderID(), myself.getJid()), getContext(), this.mIMAddrBookItem, null);
                                MMMessageItem mMMessageItem = this.mThreadItem;
                                if (mMMessageItem != null) {
                                    if (this.mThreadId == null) {
                                        this.mThreadId = mMMessageItem.messageId;
                                    }
                                    initInputFragment();
                                    this.mCommentsRecyclerView.setThreadInfo(this.mSessionId, this.mThreadItem, this.mIsGroup, this.mThreadId, this.mThreadSvr);
                                }
                                updateTitleBar();
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnThreadContextSynced(String str, String str2, String str3) {
        ZoomMessage zoomMessage;
        if (this.mThreadItem == null && TextUtils.equals(str, this.mSessionId) && TextUtils.equals(str2, this.mThreadId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                    if (threadDataProvider != null) {
                        long j = this.mThreadSvr;
                        if (j == 0) {
                            zoomMessage = threadDataProvider.getMessagePtr(this.mSessionId, this.mThreadId);
                        } else {
                            zoomMessage = threadDataProvider.getMessagePtr(this.mSessionId, j);
                        }
                        if (zoomMessage != null) {
                            this.mThreadItem = MMMessageItem.initWithZoomMessage(zoomMessage, this.mSessionId, zoomMessenger, this.mIsGroup, TextUtils.equals(zoomMessage.getSenderID(), myself.getJid()), getContext(), this.mIMAddrBookItem, null);
                            initInputFragment();
                        }
                        MMMessageItem mMMessageItem = this.mThreadItem;
                        if (mMMessageItem == null) {
                            finishFragment(false);
                            return;
                        }
                        if (this.mThreadId == null) {
                            this.mThreadId = mMMessageItem.messageId;
                        }
                        this.mCommentsRecyclerView.setThreadInfo(this.mSessionId, this.mThreadItem, this.mIsGroup, this.mThreadId, this.mThreadSvr);
                        this.mCommentsRecyclerView.loadComments(true);
                        updateTitleBar();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            this.mCommentsRecyclerView.OnFetchEmojiDetailInfo(str, str2, str3, str4, z);
        }
    }

    /* access modifiers changed from: private */
    public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            this.mCommentsRecyclerView.OnFetchEmojiCountInfo(str, str2, list, z);
        }
    }

    /* access modifiers changed from: private */
    public void OnMessageEmojiInfoUpdated(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mCommentsRecyclerView.OnMessageEmojiInfoUpdated(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void OnEmojiCountInfoLoadedFromDB(String str) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mCommentsRecyclerView.OnEmojiCountInfoLoadedFromDB(str);
        }
    }

    /* access modifiers changed from: private */
    public void onConfirmFileDownloaded(String str, String str2, int i) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            EventBus.getDefault().post(new ZMFileAction(this.mSessionId, str2, 3));
            EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
            final String str3 = str;
            final String str4 = str2;
            final int i2 = i;
            C364015 r1 = new EventAction("") {
                public void run(IUIElement iUIElement) {
                    MMCommentsFragment mMCommentsFragment = (MMCommentsFragment) iUIElement;
                    if (mMCommentsFragment != null) {
                        mMCommentsFragment.handleOnConfirmFileDownload(str3, str4, i2);
                    }
                }
            };
            nonNullEventTaskManagerOrThrowException.push(r1);
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_ChatSessionUpdate(String str) {
        if (!StringUtil.isEmptyOrNull(this.mSessionId) && this.mSessionId.equals(str)) {
            updateDisableMsg();
        }
    }

    /* access modifiers changed from: private */
    public void handleOnConfirmFileDownload(String str, String str2, int i) {
        dismissWaitingDownloadDialog(str2);
        FragmentActivity activity = getActivity();
        if (activity != null && StringUtil.isSameString(this.mSessionId, str) && StringUtil.isSameString(this.mPendingPlayMsgId, str2)) {
            this.mPendingPlayMsgId = null;
            MMMessageItem itemByMessageId = this.mCommentsRecyclerView.getItemByMessageId(str2);
            if (itemByMessageId != null) {
                switch (itemByMessageId.messageType) {
                    case 2:
                    case 3:
                        if (itemByMessageId.isFileDownloaded && !StringUtil.isEmptyOrNull(itemByMessageId.localFilePath) && new File(itemByMessageId.localFilePath).exists()) {
                            if (!playAudioMessage(itemByMessageId)) {
                                Toast.makeText(activity, C4558R.string.zm_mm_msg_play_audio_failed, 1).show();
                                break;
                            }
                        } else if (i != 0) {
                            Toast.makeText(activity, C4558R.string.zm_mm_msg_download_audio_failed, 1).show();
                            break;
                        }
                        break;
                }
            }
        }
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
                            this.mCommentsRecyclerView.updateMessage(messageById);
                            this.mCommentsRecyclerView.scrollToBottom(false);
                        }
                    }
                }
            } else {
                MMMessageItem itemByMessageId = this.mCommentsRecyclerView.getItemByMessageId(str2);
                if (itemByMessageId != null) {
                    itemByMessageId.isPreviewDownloadFailed = true;
                    if (isResumed()) {
                        this.mCommentsRecyclerView.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnGetThreadData(ThreadDataResult threadDataResult) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView != null) {
            mMCommentsRecyclerView.OnGetThreadData(threadDataResult);
            if (this.mThreadItem == null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                    if (threadDataProvider != null) {
                        ZoomMessage messagePtr = threadDataProvider.getMessagePtr(this.mSessionId, this.mThreadSvr);
                        if (messagePtr != null) {
                            ZoomBuddy myself = zoomMessenger.getMyself();
                            if (myself != null) {
                                this.mThreadItem = MMMessageItem.initWithZoomMessage(messagePtr, this.mSessionId, zoomMessenger, this.mIsGroup, TextUtils.equals(messagePtr.getSenderID(), myself.getJid()), getContext(), this.mIMAddrBookItem, null);
                                MMMessageItem mMMessageItem = this.mThreadItem;
                                if (mMMessageItem != null) {
                                    if (this.mThreadId == null) {
                                        this.mThreadId = mMMessageItem.messageId;
                                    }
                                    initInputFragment();
                                    this.mCommentsRecyclerView.setThreadInfo(this.mSessionId, this.mThreadItem, this.mIsGroup, this.mThreadId, this.mThreadSvr);
                                }
                                updateTitleBar();
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onMSGDBExistence(String str, String str2, boolean z) {
        ZoomMessage zoomMessage;
        if (TextUtils.equals(str, this.mSessionId) && TextUtils.equals(this.mThreadId, str2)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    if (z) {
                        long j = this.mThreadSvr;
                        if (j == 0) {
                            zoomMessage = threadDataProvider.getMessagePtr(this.mSessionId, this.mThreadId);
                        } else {
                            zoomMessage = threadDataProvider.getMessagePtr(this.mSessionId, j);
                        }
                        if (zoomMessage != null) {
                            ZoomBuddy myself = zoomMessenger.getMyself();
                            if (myself != null) {
                                this.mThreadItem = MMMessageItem.initWithZoomMessage(zoomMessage, this.mSessionId, zoomMessenger, this.mIsGroup, TextUtils.equals(zoomMessage.getSenderID(), myself.getJid()), getContext(), this.mIMAddrBookItem, null);
                                MMMessageItem mMMessageItem = this.mThreadItem;
                                if (mMMessageItem != null) {
                                    if (this.mThreadId == null) {
                                        this.mThreadId = mMMessageItem.messageId;
                                    }
                                    this.mCommentsRecyclerView.setThreadInfo(str, this.mThreadItem, this.mIsGroup, this.mThreadId, this.mThreadSvr);
                                    updateTitleBar();
                                    this.mCommentsRecyclerView.notifyDataSetChanged();
                                    this.mCommentsRecyclerView.loadComments(true);
                                    initInputFragment();
                                }
                            }
                        }
                    } else {
                        threadDataProvider.syncSingleThreadContext(str, str2, 0);
                    }
                }
            }
        }
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
                    MMCommentsFragment.this.mPendingPlayMsgId = null;
                    MMCommentsFragment.this.mWaitingDialogId = null;
                    MMCommentsFragment.this.mWaitingDialog = null;
                }
            });
            this.mWaitingDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    MMCommentsFragment.this.mWaitingDialogId = null;
                    MMCommentsFragment.this.mWaitingDialog = null;
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

    /* access modifiers changed from: private */
    public void onBeginConnect() {
        if (isResumed()) {
            updateTitleBar();
        }
    }

    /* access modifiers changed from: private */
    public void onConnectReturn(final int i) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.push(new EventAction() {
                public void run(IUIElement iUIElement) {
                    if (i == 0) {
                        MMCommentsFragment.this.mSwipeRefreshLayout.setEnabled(true);
                    }
                    MMCommentsFragment.this.mCommentsRecyclerView.onConnectReturn(i);
                    MMCommentsFragment.this.updateTitleBar();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public boolean onIndicateMessageReceived(String str, String str2, String str3) {
        if (this.mAnchorMessageItem != null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || StringUtil.isEmptyOrNull(this.mSessionId) || !this.mSessionId.equals(str) || StringUtil.isEmptyOrNull(str3)) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
        if (sessionById == null) {
            return false;
        }
        ZoomMessage messageById = sessionById.getMessageById(str3);
        if (messageById == null || !messageById.isComment() || !TextUtils.equals(messageById.getThreadID(), this.mThreadId)) {
            return false;
        }
        if (messageById.isOfflineMessage() && !this.mCommentsRecyclerView.isLocalDataDirty()) {
            return false;
        }
        this.mUnreadReplyCount++;
        if (this.mCommentsRecyclerView.isLocalDataDirty()) {
            updateBottomHint();
            return false;
        }
        updateTitleBar();
        sessionById.checkAutoDownloadForMessage(str3);
        MMMessageItem onReceivedMessage = onReceivedMessage(messageById);
        if (onReceivedMessage != null) {
            this.mCommentsRecyclerView.checkAndDecodeE2EMsg(zoomMessenger, onReceivedMessage);
        }
        if (messageById.isUnread() && (messageById.isMessageAtEveryone() || messageById.isMessageAtMe())) {
            this.mAtListFromSyncHint.add(str3);
            if (messageById.isMessageAtEveryone()) {
                this.mAtAllListFromSyncHint.add(str3);
            } else {
                this.mAtMeListFromSyncHint.add(str3);
            }
        }
        if (!this.mCommentsRecyclerView.isInAutoScrollBottomMode() || this.mCommentsRecyclerView.isLocalDataDirty()) {
            updateBottomHint();
        }
        showFloatingEmojisIfMatched(str3, messageById.getBody(), messageById.getServerSideTime(), false);
        return false;
    }

    private MMMessageItem onReceivedMessage(ZoomMessage zoomMessage) {
        MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
        if (mMCommentsRecyclerView == null || this.mAnchorMessageItem != null) {
            return null;
        }
        MMMessageItem updateMessage = mMCommentsRecyclerView.updateMessage(zoomMessage, false);
        this.mCommentsRecyclerView.scrollToBottom(false);
        return updateMessage;
    }

    public void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj) {
        if (obj instanceof MMMessageItem) {
            dismissContextMenuDialog();
            dismissReactionEmojiDialog();
            onEmojiSelect(view, i, (MMMessageItem) obj, charSequence);
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

    private void deleteLocalMessage(MMMessageItem mMMessageItem, ZoomChatSession zoomChatSession) {
        zoomChatSession.deleteLocalMessage(mMMessageItem.messageId);
        this.mCommentsRecyclerView.deleteMessage(mMMessageItem.messageId);
    }

    private void resendMessage(MMMessageItem mMMessageItem) {
        boolean z;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                Resources resources = getResources();
                if (!mMMessageItem.isE2E || mMMessageItem.messageType != 5) {
                    z = sessionById.resendPendingMessage(mMMessageItem.messageId, mMMessageItem.isE2E ? resources.getString(C4558R.string.zm_msg_e2e_fake_message) : "");
                } else {
                    z = sessionById.resendPendingE2EImageMessage(mMMessageItem.messageId, resources.getString(C4558R.string.zm_msg_e2e_fake_message), mMMessageItem.localFilePath);
                }
                if (z) {
                    mMMessageItem.messageState = 1;
                    this.mCommentsRecyclerView.notifyDataSetChanged();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void loadMoreComments() {
        if (this.mCommentsRecyclerView.loadMoreComments(1)) {
            this.mSwipeRefreshLayout.setEnabled(false);
            this.mCommentsRecyclerView.insertTimedChatMsg();
        }
    }

    private void markUnreadMessage(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                if (sessionById != null) {
                    if (sessionById.markMessageAsUnread(mMMessageItem.messageXMPPId)) {
                        this.mCommentsRecyclerView.updateMessage(mMMessageItem.messageId);
                        this.mMarkUnreadMessages.add(mMMessageItem.messageId);
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
            if (sessionById != null && sessionById.isMessageMarkUnread(mMMessageItem.messageXMPPId) && sessionById.unmarkMessageAsUnread(mMMessageItem.messageXMPPId)) {
                this.mMarkUnreadMessages.remove(mMMessageItem.messageId);
                this.mCommentsRecyclerView.updateMessage(mMMessageItem.messageId);
                if (this.mOldMarkUnreadMessages != null) {
                    long j = mMMessageItem.serverSideTime;
                    Iterator it = this.mOldMarkUnreadMessages.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (((MessageInfo) it.next()).getSvrTime() == j) {
                                it.remove();
                                break;
                            }
                        } else {
                            break;
                        }
                    }
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
                    MMCommentsFragment.this.startMeeting(j);
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

    public void forwardMessage(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FORWARD_MESSAGE_ID, str);
        MMSelectSessionAndBuddyFragment.showAsFragment(this, bundle, false, false, 114);
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
        int childCount = this.mCommentsRecyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mCommentsRecyclerView.getChildAt(i);
            if (childAt instanceof AbsMessageView) {
                MMMessageItem messageItem = ((AbsMessageView) childAt).getMessageItem();
                if (messageItem != null && isContainInOldMarkUnreads(messageItem.serverSideTime)) {
                    markAsReadMessage(messageItem);
                }
            }
        }
    }

    public boolean isContainInOldMarkUnreads(long j) {
        if (CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            return false;
        }
        Iterator it = this.mOldMarkUnreadMessages.iterator();
        while (it.hasNext()) {
            if (((MessageInfo) it.next()).getSvrTime() == j) {
                return true;
            }
        }
        return false;
    }

    public void editMessage(MMMessageItem mMMessageItem) {
        MMEditMessageFragment.showAsFragment(this, this.mSessionId, mMMessageItem.messageXMPPId, 4001);
    }

    public void onCommentSent(String str, String str2, String str3) {
        if (TextUtils.equals(this.mThreadId, str2)) {
            if (this.mCommentsRecyclerView.isLocalDataDirty()) {
                this.mCommentsRecyclerView.loadComments(false, true);
            } else {
                this.mCommentsRecyclerView.updateMessage(str3, false);
            }
        }
        if (!this.mIsE2EChat) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    ZoomMessage messagePtr = threadDataProvider.getMessagePtr(str, str2);
                    if (messagePtr != null) {
                        LinkPreviewHelper.doCrawLinkPreview(this.mSessionId, str2, messagePtr.getBody());
                    }
                }
            }
        }
    }

    public void onViewInitReady() {
        if (this.mChatInputFragment != null) {
            this.mVoiceRecordView.initRecordInfo(this, getSessionDataPath(), this.mChatInputFragment.getBtnHoldToTalk());
        }
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
                                this.mCommentsRecyclerView.updateMessageEmojiCountInfo(mMMessageItem, false);
                                showFloatingView(view, i, z);
                                this.mIsThreadReactionChanged = true;
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

    public void onMessageShowed(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            showFloatingEmojisIfMatched(mMMessageItem.messageId, mMMessageItem.message, mMMessageItem.serverSideTime, true);
            if (this.mCommentsRecyclerView.isLayoutReady()) {
                this.mCacheMsgs.put(mMMessageItem, Long.valueOf(System.currentTimeMillis()));
            }
        }
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
        replyComment(mMMessageItem);
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
                    if (!StringUtil.isEmptyOrNull(str)) {
                        this.mIsThreadReactionChanged = true;
                    }
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

    public boolean onLongClickAddReactionLabel(final View view, final MMMessageItem mMMessageItem) {
        if (!PTApp.getInstance().isWebSignedOn()) {
            return false;
        }
        if (closeKeyboardIfOpened(view)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MMCommentsFragment.this.showAddReactionDialog(view, mMMessageItem);
                }
            }, 100);
        } else {
            showAddReactionDialog(view, mMMessageItem);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void showAddReactionDialog(View view, final MMMessageItem mMMessageItem) {
        if (!closeKeyboardIfOpened(view) && view != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                Rect messageLocationOnScreen = this.mCommentsRecyclerView.getMessageLocationOnScreen(mMMessageItem);
                if (messageLocationOnScreen != null) {
                    int height = this.mTitleBar.getHeight();
                    int i = messageLocationOnScreen.top;
                    int i2 = messageLocationOnScreen.bottom - messageLocationOnScreen.top;
                    int computeVerticalScrollRange = this.mCommentsRecyclerView.computeVerticalScrollRange();
                    int computeVerticalScrollOffset = this.mCommentsRecyclerView.computeVerticalScrollOffset();
                    int i3 = i > 0 ? ((computeVerticalScrollRange - computeVerticalScrollOffset) - i) - i2 : (computeVerticalScrollRange - computeVerticalScrollOffset) - (i2 + i);
                    dismissReactionEmojiDialog();
                    this.mReactionEmojiDialog = new Builder(zMActivity).setAnchor(i, i2, height, i3, new OnReactionEmojiListener() {
                        public void onReactionEmojiDialogShowed(boolean z, final int i) {
                            boolean z2 = false;
                            if (z) {
                                MMCommentsFragment.this.mCommentsRecyclerView.scrollBy(0, i);
                                return;
                            }
                            if (i >= 0) {
                                if (MMCommentsFragment.this.mCommentsRecyclerView.computeVerticalScrollRange() < MMCommentsFragment.this.mCommentsRecyclerView.getHeight()) {
                                    z2 = true;
                                }
                                if (i <= 0 || !z2) {
                                    MMCommentsFragment.this.mCommentsRecyclerView.setMessageViewMargin(mMMessageItem, i);
                                } else {
                                    MMCommentsFragment.this.mCommentsRecyclerView.setMessageViewMargin(mMMessageItem, (MMCommentsFragment.this.mCommentsRecyclerView.getHeight() + i) - MMCommentsFragment.this.mCommentsRecyclerView.computeVerticalScrollRange());
                                }
                            }
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    MMCommentsFragment.this.mCommentsRecyclerView.scrollBy(0, i);
                                }
                            }, 100);
                        }

                        public void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj) {
                            MMCommentsFragment.this.onReactionEmojiClick(view, i, charSequence, obj);
                        }
                    }).setData(mMMessageItem).create();
                    this.mReactionEmojiDialog.setCanceledOnTouchOutside(true);
                    this.mReactionEmojiDialog.show();
                }
            }
        }
    }

    public void onReachReactionLimit() {
        showReactionEmojiLimitDialog();
    }

    public void onNewMsgIdReady(String str) {
        this.mNewMsgMarkId = str;
        updateBottomHint();
    }

    public void onLayoutCompleted() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                MMCommentsFragment.this.updateBottomHint();
                if (MMCommentsFragment.this.mUnreadInfo != null && MMCommentsFragment.this.mUnreadInfo.autoOpenKeyboard && MMCommentsFragment.this.mChatInputFragment != null) {
                    MMCommentsFragment.this.mChatInputFragment.openSoftKeyboard();
                }
            }
        }, 500);
    }

    public void onUnSupportEmojiReceived(String str) {
        if (!StringUtil.isEmptyOrNull(str) && !CommonEmojiHelper.getInstance().isEmojiInstalled() && getActivity() != null && UnSupportEmojiDialog.getUnSupportEmojiDialog((ZMActivity) getActivity()) == null) {
            this.mHandler.removeCallbacks(this.mUnSupportEmojiRunnable);
            this.mHandler.postDelayed(this.mUnSupportEmojiRunnable, 100);
        }
    }

    public void onLoadingMore() {
        loadMoreComments();
    }

    public boolean onShowContextMenu(final View view, final MMMessageItem mMMessageItem) {
        if (!PTApp.getInstance().isWebSignedOn()) {
            return true;
        }
        if (closeKeyboardIfOpened(view)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MMCommentsFragment.this.showContextMenu(view, mMMessageItem);
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

    private boolean isAnnouncement() {
        if (!this.mIsGroup) {
            return false;
        }
        return ZMIMUtils.isAnnouncement(this.mGroupId);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x01de  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x021b  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x022c  */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x0270  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x02f3  */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x031d  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0361  */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03d7  */
    /* JADX WARNING: Removed duplicated region for block: B:210:0x043e  */
    /* JADX WARNING: Removed duplicated region for block: B:222:0x0498  */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x0570  */
    /* JADX WARNING: Removed duplicated region for block: B:260:0x0590 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:261:0x0591  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0104  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void showContextMenu(android.view.View r21, com.zipow.videobox.view.p014mm.MMMessageItem r22) {
        /*
            r20 = this;
            r0 = r20
            r1 = r22
            android.content.Context r2 = r20.getContext()
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
            long r7 = r1.serverSideTime
            r9 = 0
            r11 = 1
            int r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r7 == 0) goto L_0x0034
            r7 = 1
            goto L_0x0035
        L_0x0034:
            r7 = 0
        L_0x0035:
            int r8 = r1.messageState
            r9 = 4
            if (r8 != r9) goto L_0x003c
            r8 = 1
            goto L_0x003d
        L_0x003c:
            r8 = 0
        L_0x003d:
            int r10 = r1.messageState
            if (r10 != r11) goto L_0x0043
            r10 = 1
            goto L_0x0044
        L_0x0043:
            r10 = 0
        L_0x0044:
            int r13 = r1.messageState
            r14 = 6
            if (r13 != r14) goto L_0x004b
            r13 = 1
            goto L_0x004c
        L_0x004b:
            r13 = 0
        L_0x004c:
            r15 = 2
            if (r8 == 0) goto L_0x0063
            int r12 = r1.messageType
            r14 = 44
            if (r12 == r14) goto L_0x0063
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r12 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r14 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_resend_message
            java.lang.String r14 = r2.getString(r14)
            r12.<init>(r14, r15)
            r6.add(r12)
        L_0x0063:
            if (r8 != 0) goto L_0x00a0
            if (r10 != 0) goto L_0x00a0
            if (r13 != 0) goto L_0x00a0
            boolean r12 = r1.isThread
            if (r12 == 0) goto L_0x00a0
            if (r7 == 0) goto L_0x00a0
            com.zipow.videobox.ptapp.ThreadDataProvider r12 = r3.getThreadDataProvider()
            if (r12 == 0) goto L_0x00a0
            java.lang.String r14 = r0.mSessionId
            java.lang.String r9 = r1.messageId
            boolean r9 = r12.isThreadFollowed(r14, r9)
            if (r9 == 0) goto L_0x0090
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r9 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r12 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_unfollow_thread_88133
            java.lang.String r12 = r2.getString(r12)
            r14 = 24
            r9.<init>(r12, r14)
            r6.add(r9)
            goto L_0x00a0
        L_0x0090:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r9 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r12 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_follow_thread_88133
            java.lang.String r12 = r2.getString(r12)
            r14 = 23
            r9.<init>(r12, r14)
            r6.add(r9)
        L_0x00a0:
            boolean r9 = r0.mIsGroup
            if (r9 != 0) goto L_0x00b6
            java.lang.String r9 = r0.mBuddyId
            com.zipow.videobox.ptapp.mm.ZoomBuddy r9 = r3.getBuddyWithJID(r9)
            if (r9 == 0) goto L_0x00b6
            int r9 = r9.getAccountStatus()
            if (r9 != 0) goto L_0x00b4
            r9 = 1
            goto L_0x00b7
        L_0x00b4:
            r9 = 0
            goto L_0x00b7
        L_0x00b6:
            r9 = 1
        L_0x00b7:
            boolean r12 = r0.mIsGroup
            if (r12 != 0) goto L_0x00c5
            java.lang.String r12 = r0.mBuddyId
            boolean r12 = r3.blockUserIsBlocked(r12)
            if (r12 == 0) goto L_0x00c5
            r12 = 1
            goto L_0x00c6
        L_0x00c5:
            r12 = 0
        L_0x00c6:
            if (r9 == 0) goto L_0x00d4
            if (r12 != 0) goto L_0x00d4
            java.lang.String r14 = r0.mSessionId
            boolean r14 = r1.isDeleteable(r14)
            if (r14 == 0) goto L_0x00d4
            r14 = 1
            goto L_0x00d5
        L_0x00d4:
            r14 = 0
        L_0x00d5:
            boolean r11 = r1.isE2E
            if (r11 != 0) goto L_0x00e2
            int r11 = r3.e2eGetMyOption()
            if (r11 != r15) goto L_0x00e0
            goto L_0x00e2
        L_0x00e0:
            r11 = 0
            goto L_0x00e3
        L_0x00e2:
            r11 = 1
        L_0x00e3:
            boolean r16 = r22.isMessageE2EWaitDecrypt()
            int r15 = r3.msgCopyGetOption()
            r17 = r5
            r5 = 1
            if (r15 != r5) goto L_0x00f2
            r5 = 1
            goto L_0x00f3
        L_0x00f2:
            r5 = 0
        L_0x00f3:
            boolean r15 = com.zipow.videobox.util.ZMIMUtils.isReplyEnable()
            if (r15 == 0) goto L_0x0101
            boolean r15 = r20.isAnnouncement()
            if (r15 != 0) goto L_0x0101
            r15 = 1
            goto L_0x0102
        L_0x0101:
            r15 = 0
        L_0x0102:
            if (r8 != 0) goto L_0x01a7
            r18 = r12
            int r12 = r1.messageType
            r19 = r9
            r9 = 37
            if (r12 == r9) goto L_0x018d
            int r9 = r1.messageType
            r12 = 38
            if (r9 != r12) goto L_0x0116
            goto L_0x018d
        L_0x0116:
            com.zipow.videobox.ptapp.PTApp r9 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r9 = r9.isFileTransferDisabled()
            if (r9 != 0) goto L_0x01ab
            int r9 = r1.messageType
            r12 = 2
            if (r9 == r12) goto L_0x01ab
            int r9 = r1.messageType
            r12 = 3
            if (r9 == r12) goto L_0x01ab
            boolean r9 = r1.isE2E
            if (r9 != 0) goto L_0x01ab
            int r3 = r3.e2eGetMyOption()
            r9 = 2
            if (r3 == r9) goto L_0x01ab
            int r3 = r1.messageState
            if (r3 == r12) goto L_0x013d
            int r3 = r1.messageState
            if (r3 != r9) goto L_0x01ab
        L_0x013d:
            boolean r3 = r22.isNormalFileMsg()
            if (r3 == 0) goto L_0x0152
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r9 = r2.getString(r9)
            r12 = 4
            r3.<init>(r9, r12)
            r6.add(r3)
        L_0x0152:
            boolean r3 = r22.isNormalImageMsg()
            if (r3 == 0) goto L_0x01ab
            boolean r3 = r1.isStikcerMsg
            if (r3 == 0) goto L_0x016a
            java.lang.String r3 = r4.getJid()
            java.lang.String r9 = r1.fromJid
            boolean r3 = android.text.TextUtils.equals(r3, r9)
            if (r3 == 0) goto L_0x016a
            r3 = 1
            goto L_0x016b
        L_0x016a:
            r3 = 0
        L_0x016b:
            if (r3 != 0) goto L_0x01ab
            java.lang.String r3 = r1.fileId
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L_0x017d
            java.lang.String r3 = r1.localFilePath
            boolean r3 = com.zipow.videobox.util.ImageUtil.isValidImageFile(r3)
            if (r3 == 0) goto L_0x01ab
        L_0x017d:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_save_emoji
            java.lang.String r9 = r2.getString(r9)
            r12 = 6
            r3.<init>(r9, r12)
            r6.add(r3)
            goto L_0x01ab
        L_0x018d:
            boolean r9 = r1.isE2E
            if (r9 != 0) goto L_0x01ab
            boolean r3 = r3.isCodeSnippetDisabled()
            if (r3 != 0) goto L_0x01ab
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r9 = r2.getString(r9)
            r12 = 4
            r3.<init>(r9, r12)
            r6.add(r3)
            goto L_0x01ab
        L_0x01a7:
            r19 = r9
            r18 = r12
        L_0x01ab:
            if (r15 == 0) goto L_0x01cf
            java.lang.String r3 = r4.getJid()
            java.lang.String r4 = r1.fromJid
            boolean r3 = android.text.TextUtils.equals(r3, r4)
            if (r3 != 0) goto L_0x01cf
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r3 = r0.mAnchorMessageItem
            if (r3 != 0) goto L_0x01cf
            if (r7 == 0) goto L_0x01cf
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_add_reply_88133
            java.lang.String r4 = r2.getString(r4)
            r7 = 21
            r3.<init>(r4, r7)
            r6.add(r3)
        L_0x01cf:
            int r3 = r1.messageType
            r4 = 11
            r7 = 14
            r9 = 16
            r12 = 15
            switch(r3) {
                case 0: goto L_0x0498;
                case 1: goto L_0x0498;
                case 2: goto L_0x043e;
                case 3: goto L_0x043e;
                case 4: goto L_0x03d7;
                case 5: goto L_0x03d7;
                case 6: goto L_0x056a;
                case 7: goto L_0x056a;
                case 8: goto L_0x056a;
                case 9: goto L_0x056a;
                case 10: goto L_0x0361;
                case 11: goto L_0x0361;
                case 12: goto L_0x01dc;
                case 13: goto L_0x01dc;
                case 14: goto L_0x01dc;
                case 15: goto L_0x01dc;
                case 16: goto L_0x01dc;
                case 17: goto L_0x01dc;
                case 18: goto L_0x01dc;
                case 19: goto L_0x01dc;
                case 20: goto L_0x01dc;
                case 21: goto L_0x031d;
                case 22: goto L_0x031d;
                case 23: goto L_0x031d;
                case 24: goto L_0x01dc;
                case 25: goto L_0x01dc;
                case 26: goto L_0x01dc;
                case 27: goto L_0x03d7;
                case 28: goto L_0x03d7;
                case 29: goto L_0x02f3;
                case 30: goto L_0x01dc;
                case 31: goto L_0x01dc;
                case 32: goto L_0x0270;
                case 33: goto L_0x0270;
                case 34: goto L_0x0498;
                case 35: goto L_0x0498;
                case 36: goto L_0x01dc;
                case 37: goto L_0x022c;
                case 38: goto L_0x022c;
                case 39: goto L_0x021b;
                case 40: goto L_0x031d;
                case 41: goto L_0x01de;
                case 42: goto L_0x01dc;
                case 43: goto L_0x01dc;
                case 44: goto L_0x031d;
                case 45: goto L_0x0361;
                case 46: goto L_0x0361;
                default: goto L_0x01dc;
            }
        L_0x01dc:
            goto L_0x056a
        L_0x01de:
            if (r16 != 0) goto L_0x056a
            if (r5 == 0) goto L_0x01f1
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_copy_message
            java.lang.String r4 = r2.getString(r4)
            r5 = 1
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x01f1:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x056a
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x020b
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r9)
            r6.add(r3)
            goto L_0x056a
        L_0x020b:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
            goto L_0x056a
        L_0x021b:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r4 = r2.getString(r4)
            r5 = 4
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x056a
        L_0x022c:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0253
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0245
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r9)
            r6.add(r3)
            goto L_0x0253
        L_0x0245:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
        L_0x0253:
            if (r14 == 0) goto L_0x056a
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r20.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x056a
        L_0x0270:
            if (r8 != 0) goto L_0x02e3
            if (r10 != 0) goto L_0x02e3
            if (r13 != 0) goto L_0x02e3
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x029d
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x028f
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r9)
            r6.add(r3)
            goto L_0x029d
        L_0x028f:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r12)
            r6.add(r3)
        L_0x029d:
            if (r11 != 0) goto L_0x02b8
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r3 = r3.isFileTransferDisabled()
            if (r3 != 0) goto L_0x02b8
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_save_emoji
            java.lang.String r5 = r2.getString(r5)
            r7 = 6
            r3.<init>(r5, r7)
            r6.add(r3)
        L_0x02b8:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_mm_btn_save_image
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r4)
            r6.add(r3)
            if (r14 == 0) goto L_0x056a
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r20.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x056a
        L_0x02e3:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r7)
            r6.add(r3)
            goto L_0x056a
        L_0x02f3:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x056a
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x030d
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r9)
            r6.add(r3)
            goto L_0x056a
        L_0x030d:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
            goto L_0x056a
        L_0x031d:
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.sip.CallHistoryMgr r3 = r3.getCallHistoryMgr()
            if (r3 != 0) goto L_0x0329
            goto L_0x056a
        L_0x0329:
            java.lang.String r4 = r1.messageXMPPId
            com.zipow.videobox.sip.CallHistory r3 = r3.getCallHistoryByID(r4)
            if (r3 != 0) goto L_0x0333
            goto L_0x056a
        L_0x0333:
            java.lang.String r3 = r3.getNumber()
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x056a
            if (r5 == 0) goto L_0x034f
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_copy_url
            java.lang.String r4 = r2.getString(r4)
            r5 = 13
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x034f:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_mm_join_meeting_21854
            java.lang.String r4 = r2.getString(r4)
            r5 = 12
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x056a
        L_0x0361:
            if (r8 != 0) goto L_0x03c7
            if (r10 != 0) goto L_0x03c7
            if (r13 != 0) goto L_0x03c7
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x038e
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x0380
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r9)
            r6.add(r3)
            goto L_0x038e
        L_0x0380:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
        L_0x038e:
            int r3 = r1.messageType
            r4 = 46
            if (r3 == r4) goto L_0x039a
            int r3 = r1.messageType
            r4 = 45
            if (r3 != r4) goto L_0x03aa
        L_0x039a:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_copy_link_68764
            java.lang.String r4 = r2.getString(r4)
            r5 = 17
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x03aa:
            if (r14 == 0) goto L_0x056a
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r20.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x056a
        L_0x03c7:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r7)
            r6.add(r3)
            goto L_0x056a
        L_0x03d7:
            if (r8 != 0) goto L_0x0420
            if (r10 != 0) goto L_0x0420
            if (r13 != 0) goto L_0x0420
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0404
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x03f6
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r9)
            r6.add(r3)
            goto L_0x0404
        L_0x03f6:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r12)
            r6.add(r3)
        L_0x0404:
            if (r14 == 0) goto L_0x042e
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r5 = r2.getString(r5)
            android.content.res.Resources r7 = r20.getResources()
            int r8 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r7 = r7.getColor(r8)
            r8 = 0
            r3.<init>(r5, r8, r7)
            r6.add(r3)
            goto L_0x042e
        L_0x0420:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r7)
            r6.add(r3)
        L_0x042e:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_mm_btn_save_image
            java.lang.String r5 = r2.getString(r5)
            r3.<init>(r5, r4)
            r6.add(r3)
            goto L_0x056a
        L_0x043e:
            if (r8 != 0) goto L_0x0488
            if (r10 != 0) goto L_0x0488
            if (r13 != 0) goto L_0x0488
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x046b
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x045d
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r9)
            r6.add(r3)
            goto L_0x046b
        L_0x045d:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
        L_0x046b:
            if (r14 == 0) goto L_0x056a
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r20.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x056a
        L_0x0488:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r7)
            r6.add(r3)
            goto L_0x056a
        L_0x0498:
            if (r5 == 0) goto L_0x04a9
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_copy_message
            java.lang.String r4 = r2.getString(r4)
            r14 = 1
            r3.<init>(r4, r14)
            r6.add(r3)
        L_0x04a9:
            if (r8 != 0) goto L_0x055c
            if (r10 != 0) goto L_0x055c
            if (r13 != 0) goto L_0x055c
            if (r16 != 0) goto L_0x0519
            if (r5 == 0) goto L_0x04c3
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_share
            java.lang.String r4 = r2.getString(r4)
            r5 = 20
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x04c3:
            java.lang.String r3 = r1.sessionId
            boolean r3 = com.zipow.videobox.util.UIMgr.isMyNotes(r3)
            if (r3 != 0) goto L_0x04f2
            boolean r3 = r0.isMessageMarkUnread(r1)
            if (r3 == 0) goto L_0x04e2
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_mark_as_read_14491
            java.lang.String r4 = r2.getString(r4)
            r5 = 10
            r3.<init>(r4, r5)
            r6.add(r3)
            goto L_0x04f2
        L_0x04e2:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_mark_unread_14491
            java.lang.String r4 = r2.getString(r4)
            r5 = 9
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x04f2:
            boolean r3 = r1.isE2E
            if (r3 != 0) goto L_0x0519
            boolean r3 = r0.isMessageStarred(r1)
            if (r3 == 0) goto L_0x050b
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_unstar_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r9)
            r6.add(r3)
            goto L_0x0519
        L_0x050b:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_star_message_65147
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r12)
            r6.add(r3)
        L_0x0519:
            if (r19 == 0) goto L_0x0525
            if (r18 != 0) goto L_0x0525
            boolean r3 = r22.isEditable()
            if (r3 == 0) goto L_0x0525
            r3 = 1
            goto L_0x0526
        L_0x0525:
            r3 = 0
        L_0x0526:
            if (r3 == 0) goto L_0x056a
            if (r11 == 0) goto L_0x0532
            java.lang.String r3 = r1.sessionId
            boolean r3 = com.zipow.videobox.util.UIMgr.isMyNotes(r3)
            if (r3 == 0) goto L_0x0542
        L_0x0532:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_edit_message_19884
            java.lang.String r4 = r2.getString(r4)
            r5 = 8
            r3.<init>(r4, r5)
            r6.add(r3)
        L_0x0542:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            android.content.res.Resources r5 = r20.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_red_E02828
            int r5 = r5.getColor(r7)
            r7 = 0
            r3.<init>(r4, r7, r5)
            r6.add(r3)
            goto L_0x056a
        L_0x055c:
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_delete
            java.lang.String r4 = r2.getString(r4)
            r3.<init>(r4, r7)
            r6.add(r3)
        L_0x056a:
            boolean r3 = r6.isEmpty()
            if (r3 != 0) goto L_0x058a
            com.zipow.videobox.view.mm.message.MessageContextMenuItem r3 = new com.zipow.videobox.view.mm.message.MessageContextMenuItem
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_btn_cancel
            java.lang.String r4 = r2.getString(r4)
            r5 = 22
            android.content.res.Resources r7 = r20.getResources()
            int r8 = p021us.zoom.videomeetings.C4558R.color.zm_ui_kit_color_blue_0E71EB
            int r7 = r7.getColor(r8)
            r3.<init>(r4, r5, r7)
            r6.add(r3)
        L_0x058a:
            boolean r3 = r6.isEmpty()
            if (r3 == 0) goto L_0x0591
            return
        L_0x0591:
            r3 = r17
            r3.setData(r6)
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r4 = r0.mCommentsRecyclerView
            android.graphics.Rect r4 = r4.getMessageLocationOnScreen(r1)
            if (r4 != 0) goto L_0x059f
            return
        L_0x059f:
            int r5 = r4.top
            int r6 = r4.bottom
            int r4 = r4.top
            int r6 = r6 - r4
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r4 = r0.mCommentsRecyclerView
            int r4 = r4.computeVerticalScrollRange()
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r7 = r0.mCommentsRecyclerView
            int r7 = r7.computeVerticalScrollOffset()
            if (r5 <= 0) goto L_0x05b7
            int r4 = r4 - r7
            int r4 = r4 - r5
            goto L_0x05b8
        L_0x05b7:
            int r4 = r4 - r7
        L_0x05b8:
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog r7 = r0.mContextMenuDialog
            if (r7 == 0) goto L_0x05bf
            r7 = 0
            r0.mContextMenuDialog = r7
        L_0x05bf:
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r2 = com.zipow.videobox.view.p014mm.message.ReactionContextMenuDialog.builder(r2)
            com.zipow.videobox.view.mm.MMCommentsFragment$24 r7 = new com.zipow.videobox.view.mm.MMCommentsFragment$24
            r7.<init>(r3, r1)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r2 = r2.setAdapter(r3, r7)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r2 = r2.setLocation(r5, r6, r4)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r1 = r2.setMessageItem(r1)
            r2 = 1
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder r1 = r1.setReplyStyle(r2)
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog r1 = r1.build()
            r0.mContextMenuDialog = r1
            com.zipow.videobox.view.mm.message.ReactionContextMenuDialog r1 = r0.mContextMenuDialog
            androidx.fragment.app.FragmentManager r2 = r20.getFragmentManager()
            r1.show(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsFragment.showContextMenu(android.view.View, com.zipow.videobox.view.mm.MMMessageItem):void");
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
                            replyComment(mMMessageItem);
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

    public void shareMessage(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SHARED_MESSAGE_ID, str);
        MMSelectSessionAndBuddyFragment.showAsFragment(this, bundle, false, false, 109);
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

    private void replyComment(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null && this.mIsGroup) {
            IMAddrBookItem iMAddrBookItemFromMMMessageItem = getIMAddrBookItemFromMMMessageItem(mMMessageItem);
            if (iMAddrBookItemFromMMMessageItem != null) {
                this.mChatInputFragment.onATBuddySelect(iMAddrBookItemFromMMMessageItem);
            }
        }
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

    public void startMonitorProximity() {
        try {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                SensorManager sensorManager = (SensorManager) activity.getSystemService("sensor");
                if (sensorManager != null) {
                    Sensor defaultSensor = sensorManager.getDefaultSensor(8);
                    if (defaultSensor != null) {
                        sensorManager.registerListener(this, defaultSensor, 3);
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    public void restoreVolume() {
        try {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                this.mbVolumeChanged = false;
                this.mOldVolume = -1;
                this.mVolumeChangedTo = -1;
                return;
            }
            if (this.mbVolumeChanged && this.mOldVolume >= 0) {
                AudioManager audioManager = (AudioManager) activity.getSystemService("audio");
                if (audioManager != null && audioManager.getStreamVolume(3) == this.mVolumeChangedTo) {
                    audioManager.setStreamVolume(3, this.mOldVolume, 0);
                }
            }
            this.mbVolumeChanged = false;
            this.mOldVolume = -1;
            this.mVolumeChangedTo = -1;
        } catch (Exception unused) {
        } catch (Throwable th) {
            this.mbVolumeChanged = false;
            this.mOldVolume = -1;
            this.mVolumeChangedTo = -1;
            throw th;
        }
    }

    public void stopMonitorProximity() {
        try {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                SensorManager sensorManager = (SensorManager) activity.getSystemService("sensor");
                if (sensorManager != null) {
                    sensorManager.unregisterListener(this);
                }
            }
        } catch (Exception unused) {
        }
    }

    public boolean stopPlayAudioMessage() {
        MMMessageItem mMMessageItem = this.mPlayingMessage;
        if (mMMessageItem != null) {
            mMMessageItem.isPlaying = false;
            this.mPlayingMessage = null;
        }
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            return true;
        }
        try {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
        } catch (Exception unused) {
        }
        this.mMediaPlayer = null;
        this.mCommentsRecyclerView.notifyDataSetChanged();
        stopMonitorProximity();
        restoreVolume();
        return true;
    }

    private void setMessageAsPlayed(MMMessageItem mMMessageItem) {
        mMMessageItem.isPlayed = true;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageById = sessionById.getMessageById(mMMessageItem.messageId);
                if (messageById != null) {
                    messageById.setAsPlayed(true);
                }
            }
        }
    }

    public boolean playAudioMessage(MMMessageItem mMMessageItem) {
        if (mMMessageItem == null) {
            return false;
        }
        if (this.mPlayingMessage != null) {
            stopPlayAudioMessage();
        }
        this.mMediaPlayer = new MediaPlayer();
        this.mPlayingMessage = mMMessageItem;
        try {
            this.mbVolumeChanged = false;
            this.mOldVolume = -1;
            this.mVolumeChangedTo = -1;
            startMonitorProximity();
            this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    } catch (Exception unused) {
                    }
                    MMCommentsFragment.this.mMediaPlayer = null;
                    if (MMCommentsFragment.this.mPlayingMessage != null) {
                        MMCommentsFragment.this.mPlayingMessage.isPlaying = false;
                        MMCommentsFragment.this.mPlayingMessage = null;
                    }
                    MMCommentsFragment.this.mCommentsRecyclerView.notifyDataSetChanged();
                    MMCommentsFragment.this.stopMonitorProximity();
                    MMCommentsFragment.this.restoreVolume();
                }
            });
            this.mMediaPlayer.setDataSource(new FileInputStream(mMMessageItem.localFilePath).getFD());
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();
            mMMessageItem.isPlaying = true;
            setMessageAsPlayed(mMMessageItem);
            this.mCommentsRecyclerView.notifyDataSetChanged();
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return false;
            }
            AudioManager audioManager = (AudioManager) activity.getSystemService("audio");
            if (audioManager != null) {
                this.mOldVolume = audioManager.getStreamVolume(3);
                double streamMaxVolume = (double) audioManager.getStreamMaxVolume(3);
                if (((double) this.mOldVolume) <= 0.6d * streamMaxVolume) {
                    this.mVolumeChangedTo = (int) (streamMaxVolume * 0.8d);
                    audioManager.setStreamVolume(3, this.mVolumeChangedTo, 0);
                    this.mbVolumeChanged = true;
                }
            }
            return true;
        } catch (Exception unused) {
            this.mPlayingMessage = null;
            stopMonitorProximity();
            return false;
        }
    }

    public void onClickMessage(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            boolean z = false;
            switch (mMMessageItem.messageType) {
                case 2:
                case 3:
                    if (mMMessageItem.isPlaying) {
                        stopPlayAudioMessage();
                        return;
                    }
                    if (StringUtil.isEmptyOrNull(mMMessageItem.localFilePath) || !new File(mMMessageItem.localFilePath).exists()) {
                        z = true;
                    } else if (!playAudioMessage(mMMessageItem)) {
                        new File(mMMessageItem.localFilePath).delete();
                        z = true;
                    }
                    if (z) {
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            if (mMMessageItem.messageType != 3 || mMMessageItem.messageState == 2 || mMMessageItem.messageState == 3) {
                                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                                if (sessionById != null) {
                                    if (sessionById.downloadFileForMessage(mMMessageItem.messageId)) {
                                        mMMessageItem.isDownloading = true;
                                        this.mPendingPlayMsgId = mMMessageItem.messageId;
                                        this.mCommentsRecyclerView.onStartToDownloadFileForMessage(mMMessageItem);
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
                case 4:
                case 5:
                case 27:
                case 28:
                case 32:
                case 33:
                    if ((mMMessageItem.messageType != 5 && mMMessageItem.messageType != 32 && mMMessageItem.messageType != 28) || (mMMessageItem.messageState != 4 && mMMessageItem.messageState != 1)) {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(mMMessageItem);
                            MMImageListActivity.launch((Context) activity, mMMessageItem.sessionId, mMMessageItem.messageXMPPId, (List<MMMessageItem>) arrayList);
                            break;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 10:
                case 11:
                    FragmentActivity activity2 = getActivity();
                    if (activity2 != null) {
                        if (!ZMIMUtils.isGotoEnhancedFileTransfer(mMMessageItem)) {
                            MMContentFileViewerFragment.showAsActivity((ZMActivity) activity2, this.mSessionId, mMMessageItem.messageId, mMMessageItem.messageXMPPId, mMMessageItem.fileId, 0);
                            break;
                        } else {
                            FileTransferFragment.showAsActivity((ZMActivity) activity2, mMMessageItem, 0);
                            break;
                        }
                    } else {
                        return;
                    }
                case 45:
                case 46:
                    if (mMMessageItem.messageType != 45 || (mMMessageItem.messageState != 4 && mMMessageItem.messageState != 1)) {
                        MMMessageHelper.openWithOtherApp(mMMessageItem);
                        break;
                    } else {
                        return;
                    }
                    break;
            }
            eventTrackOpenFile(mMMessageItem.messageType);
        }
    }

    public void eventTrackOpenFile(int i) {
        switch (i) {
            case 4:
            case 5:
            case 27:
            case 28:
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        ZoomLogEventTracking.eventTrackOpenFile(sessionById.isGroup());
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void showMessageSendErrorMenu(final MMMessageItem mMMessageItem) {
        Context context = getContext();
        if (context != null) {
            ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
            final ArrayList arrayList = new ArrayList(2);
            arrayList.add(new MessageSendErrorMenuItem(context.getString(C4558R.string.zm_mm_lbl_try_again_70196), 0));
            arrayList.add(new MessageSendErrorMenuItem(context.getString(C4558R.string.zm_mm_lbl_delete_message_70196), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            ZMAlertDialog create = new ZMAlertDialog.Builder(context).setTitle((CharSequence) context.getString(C4558R.string.zm_mm_msg_could_not_send_70196)).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMCommentsFragment.this.onSelectMessageSendErrorMenuItem((MessageSendErrorMenuItem) arrayList.get(i), mMMessageItem);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    public void onSelectMessageSendErrorMenuItem(MessageSendErrorMenuItem messageSendErrorMenuItem, MMMessageItem mMMessageItem) {
        if (messageSendErrorMenuItem != null && mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    boolean isConnectionGood = zoomMessenger.isConnectionGood();
                    switch (messageSendErrorMenuItem.getAction()) {
                        case 0:
                            if (!isConnectionGood) {
                                Context context = getContext();
                                if (context != null) {
                                    ZMToast.show(context, (CharSequence) context.getString(C4558R.string.zm_mm_msg_network_unavailable), 0);
                                    break;
                                }
                            } else {
                                resendMessage(mMMessageItem);
                                break;
                            }
                            break;
                        case 1:
                            deleteLocalMessage(mMMessageItem, sessionById);
                            break;
                    }
                }
            }
        }
    }

    private IMAddrBookItem getIMAddrBookItemFromMMMessageItem(MMMessageItem mMMessageItem) {
        if (mMMessageItem == null) {
            return null;
        }
        IMAddrBookItem iMAddrBookItem = mMMessageItem.fromContact;
        if (iMAddrBookItem == null) {
            if (TextUtils.isEmpty(mMMessageItem.fromJid)) {
                return null;
            }
            iMAddrBookItem = ZMBuddySyncInstance.getInsatance().getBuddyByJid(mMMessageItem.fromJid, true);
        }
        return iMAddrBookItem;
    }

    /* JADX WARNING: Removed duplicated region for block: B:66:0x00d9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClickStatusImage(com.zipow.videobox.view.p014mm.MMMessageItem r9) {
        /*
            r8 = this;
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.isWebSignedOn()
            if (r0 != 0) goto L_0x000b
            return
        L_0x000b:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r0 = r0.getZoomMessenger()
            if (r0 != 0) goto L_0x0016
            return
        L_0x0016:
            java.lang.String r1 = r8.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r1 = r0.getSessionById(r1)
            if (r1 != 0) goto L_0x001f
            return
        L_0x001f:
            boolean r2 = r1.isGroup()
            if (r2 == 0) goto L_0x0032
            com.zipow.videobox.ptapp.mm.ZoomGroup r2 = r1.getSessionGroup()
            if (r2 == 0) goto L_0x0031
            boolean r2 = r2.amIInGroup()
            if (r2 != 0) goto L_0x0032
        L_0x0031:
            return
        L_0x0032:
            boolean r2 = r9.isE2E
            if (r2 == 0) goto L_0x007b
            boolean r2 = r0.isConnectionGood()
            if (r2 != 0) goto L_0x003d
            return
        L_0x003d:
            boolean r2 = r9.isMessageE2EWaitDecrypt()
            if (r2 == 0) goto L_0x007b
            java.lang.String r2 = r8.mSessionId
            java.lang.String r3 = r9.messageId
            int r0 = r0.e2eTryDecodeMessage(r2, r3)
            if (r0 != 0) goto L_0x0062
            java.lang.String r0 = r9.messageId
            com.zipow.videobox.ptapp.mm.ZoomMessage r0 = r1.getMessageById(r0)
            if (r0 == 0) goto L_0x0075
            java.lang.CharSequence r1 = r0.getBody()
            r9.message = r1
            int r0 = r0.getMessageState()
            r9.messageState = r0
            goto L_0x0075
        L_0x0062:
            r1 = 37
            if (r0 != r1) goto L_0x0075
            r0 = 3
            r9.messageState = r0
            android.content.res.Resources r0 = r8.getResources()
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_message_decrypting
            java.lang.String r0 = r0.getString(r1)
            r9.message = r0
        L_0x0075:
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r9 = r8.mCommentsRecyclerView
            r9.notifyDataSetChanged()
            return
        L_0x007b:
            int r0 = r9.messageType
            r2 = 11
            r3 = 5
            r4 = 0
            if (r0 == r2) goto L_0x0093
            int r0 = r9.messageType
            r2 = 45
            if (r0 == r2) goto L_0x0093
            int r0 = r9.messageType
            if (r0 == r3) goto L_0x0093
            int r0 = r9.messageType
            r2 = 28
            if (r0 != r2) goto L_0x00c6
        L_0x0093:
            com.zipow.videobox.ptapp.mm.ZoomMessage$FileTransferInfo r0 = r9.transferInfo
            r2 = 18
            r5 = 1
            if (r0 == 0) goto L_0x00a6
            int r6 = r0.state
            r7 = 2
            if (r6 == r7) goto L_0x00c7
            int r0 = r0.state
            if (r0 != r2) goto L_0x00a4
            goto L_0x00c7
        L_0x00a4:
            r5 = 0
            goto L_0x00c7
        L_0x00a6:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.MMFileContentMgr r0 = r0.getZoomFileContentMgr()
            if (r0 == 0) goto L_0x00c6
            java.lang.String r6 = r8.mSessionId
            java.lang.String r7 = r9.messageXMPPId
            com.zipow.videobox.ptapp.mm.ZoomFile r6 = r0.getFileWithMessageID(r6, r7)
            if (r6 == 0) goto L_0x00c6
            int r7 = r6.getFileTransferState()
            if (r7 != r2) goto L_0x00c1
            goto L_0x00c2
        L_0x00c1:
            r5 = 0
        L_0x00c2:
            r0.destroyFileObject(r6)
            goto L_0x00c7
        L_0x00c6:
            r5 = 0
        L_0x00c7:
            r0 = 4
            if (r5 != 0) goto L_0x00d2
            int r2 = r9.messageState
            if (r2 == r0) goto L_0x00d2
            int r2 = r9.messageState
            if (r2 != r3) goto L_0x00d5
        L_0x00d2:
            r8.showMessageSendErrorMenu(r9)
        L_0x00d5:
            int r2 = r9.messageType
            if (r2 != r0) goto L_0x00e5
            java.lang.String r0 = r9.messageId
            r1.checkAutoDownloadForMessage(r0)
            r9.isPreviewDownloadFailed = r4
            com.zipow.videobox.view.mm.MMCommentsRecyclerView r9 = r8.mCommentsRecyclerView
            r9.notifyDataSetChanged()
        L_0x00e5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsFragment.onClickStatusImage(com.zipow.videobox.view.mm.MMMessageItem):void");
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
            MMCommentsRecyclerView mMCommentsRecyclerView = this.mCommentsRecyclerView;
            if (mMCommentsRecyclerView != null) {
                mMCommentsRecyclerView.FT_Cancel(mMMessageItem);
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
            ZMAlertDialog create = new ZMAlertDialog.Builder(activity).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMCommentsFragment.this.onSelectMeetingNoMenuItem((MeetingNoMenuItem) zMMenuAdapter.getItem(i), str);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
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

    private boolean closeKeyboardIfOpened(View view) {
        ZMKeyboardDetector zMKeyboardDetector = this.mKeyboardDetector;
        if (zMKeyboardDetector == null || !zMKeyboardDetector.isKeyboardOpen()) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), view);
        return true;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent != null && sensorEvent.sensor != null && sensorEvent.sensor.getType() == 8 && sensorEvent.values != null && sensorEvent.values.length > 0 && !HeadsetUtil.getInstance().isWiredHeadsetOn() && !HeadsetUtil.getInstance().isBluetoothScoAudioOn()) {
            boolean z = true;
            if (((int) sensorEvent.sensor.getMaximumRange()) > 3) {
                if (sensorEvent.values[0] > 3.0f) {
                    z = false;
                }
                routeAudioToEarSpeaker(z);
                return;
            }
            if (sensorEvent.values[0] >= sensorEvent.sensor.getMaximumRange()) {
                z = false;
            }
            routeAudioToEarSpeaker(z);
        }
    }

    private void routeAudioToEarSpeaker(boolean z) {
        boolean z2;
        Context context = getContext();
        if (context != null) {
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                try {
                    this.mMediaPlayer.pause();
                    z2 = true;
                } catch (Exception unused) {
                    z2 = false;
                }
                AudioManager audioManager = (AudioManager) context.getSystemService("audio");
                if (z) {
                    if (!(audioManager == null || audioManager.getMode() == 2)) {
                        audioManager.setMode(2);
                    }
                } else if (!(audioManager == null || audioManager.getMode() == 0)) {
                    audioManager.setMode(0);
                }
                if (z2) {
                    try {
                        this.mMediaPlayer.start();
                    } catch (Exception unused2) {
                    }
                }
            }
        }
    }

    private boolean checkIfEmojiPraised(MMMessageItem mMMessageItem, CharSequence charSequence) {
        boolean z = false;
        if (mMMessageItem == null || TextUtils.isEmpty(charSequence)) {
            return false;
        }
        if (mMMessageItem.getEmojiCountItems() == null || mMMessageItem.getEmojiCountItems().size() == 0) {
            this.mCommentsRecyclerView.updateMessageEmojiCountInfo(mMMessageItem, true);
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

    private void showFloatingView(View view, int i, boolean z) {
        if (view != null) {
            FloatingText floatingText = this.mFloatingText;
            if (floatingText != null) {
                floatingText.detachFromWidow();
                this.mFloatingText = null;
            }
            this.mFloatingText = new FloatingText.Builder(getActivity()).setText(z ? "+1" : "-1").setWindowOffset(i).build();
            this.mFloatingText.attachToWindow();
            this.mFloatingText.startFloating(view);
        }
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
            if (!z || this.mCommentsRecyclerView.isUnreadNewMessage(j)) {
                this.mShowedNewMessages.add(str);
                if (charSequence.toString().toLowerCase().contains("happy birthday") && this.mFloatingEmojisTask == null) {
                    this.mFloatingEmojisTask = new Runnable() {
                        public void run() {
                            MMCommentsFragment.this.showFloatingEmojis();
                            MMCommentsFragment.this.mFloatingEmojisTask = null;
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

    private void showReactionEmojiLimitDialog() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            new ZMAlertDialog.Builder(zMActivity).setTitle(C4558R.string.zm_lbl_reach_reaction_limit_title_88133).setMessage(C4558R.string.zm_lbl_reach_reaction_limit_message_88133).setPositiveButton(C4558R.string.zm_btn_got_it, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
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

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C365930 r2 = new EventAction("MMCommentsFragmentPermissionResult") {
            public void run(IUIElement iUIElement) {
                ((MMCommentsFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("MMCommentsFragmentPermissionResult", r2);
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 5001) {
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
            ZMAlertDialog create = new ZMAlertDialog.Builder(activity).setTitleView(textView).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMCommentsFragment.this.onSelectLinkMenuItem((LinkMenuItem) zMMenuAdapter.getItem(i), str);
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

    public void onRecordEnd(String str, long j) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int i = 1;
            this.mCommentsRecyclerView.scrollToBottom(true);
            MessageInput.Builder newBuilder = MessageInput.newBuilder();
            newBuilder.setMsgType(2);
            if (this.mThreadItem != null) {
                i = 2;
            }
            newBuilder.setMsgSubType(i);
            newBuilder.setIsE2EMessage(this.mIsE2EChat);
            newBuilder.setSessionID(this.mSessionId);
            newBuilder.setE2EMessageFakeBody(getString(C4558R.string.zm_msg_e2e_fake_message));
            newBuilder.setIsMyNote(this.mIsMyNostes);
            newBuilder.setLocalFilePath(str);
            newBuilder.setLenInSeconds((int) j);
            if (this.mThreadItem != null) {
                CommentInfo.Builder newBuilder2 = CommentInfo.newBuilder();
                newBuilder2.setThrId(this.mThreadItem.messageId);
                newBuilder2.setThrTime(this.mThreadItem.serverSideTime);
                newBuilder2.setThrOwnerJid(this.mThreadItem.fromJid);
                newBuilder.setCommentInfo(newBuilder2);
            }
            zoomMessenger.sendMessage(newBuilder.build());
        }
    }
}
