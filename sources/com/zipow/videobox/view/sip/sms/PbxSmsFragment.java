package com.zipow.videobox.view.sip.sms;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
import com.google.android.gms.common.util.CollectionUtils;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.MMChatInputFragment;
import com.zipow.videobox.fragment.MMChatInputFragment.OnChatInputListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.IPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.SimpleIPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.IPBXMessageSession;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.ISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.BigRoundListDialog;
import com.zipow.videobox.view.BigRoundListDialog.DialogCallback;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ZMListAdapter;
import com.zipow.videobox.view.ZMReplaceSpanMovementMethod;
import com.zipow.videobox.view.p014mm.LinkPreviewMetaInfo;
import com.zipow.videobox.view.p014mm.PhoneLabelFragment;
import com.zipow.videobox.view.sip.PBXCallerIDListAdapter;
import com.zipow.videobox.view.sip.PBXDirectorySearchListView;
import com.zipow.videobox.view.sip.sms.PbxSmsRecyleView.PbxSmsUICallBack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.IZMListItem;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMEditText;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class PbxSmsFragment extends ZMDialogFragment implements OnChatInputListener, KeyboardListener, OnClickListener, PbxSmsUICallBack, IPBXMessageSelectContact {
    private static final String ARG_SESSION_ID = "sessionid";
    private static final String ARG_TO_NUMBERS = "toNumbers";
    private static final String TAG = "PbxSmsFragment";
    private Button mBtnBack;
    private ImageButton mBtnPhoneCall;
    private View mBtnSearch;
    /* access modifiers changed from: private */
    public MMChatInputFragment mChatInputFragment;
    /* access modifiers changed from: private */
    public PBXDirectorySearchListView mDirectoryListView;
    /* access modifiers changed from: private */
    public ZMEditText mEdtSelectedContact;
    /* access modifiers changed from: private */
    @Nullable
    public String mFromNumber;
    @NonNull
    private List<SenderNumberListItem> mFromNumberItems = new ArrayList();
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private ImageView mImgTitleInfo;
    /* access modifiers changed from: private */
    public ZMKeyboardDetector mKeyboardDetector;
    /* access modifiers changed from: private */
    public View mLayoutContent;
    /* access modifiers changed from: private */
    public View mLayoutSelectContact;
    private View mLayoutSenderNumber;
    private ISIPLineMgrEventSinkListener mLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            PbxSmsFragment.this.updateSenderNumberItems(true);
        }
    };
    @Nullable
    private ArrayList<PBXMessageContact> mMemberList;
    @Nullable
    private View mPanelActions;
    private View mPanelTitleCenter;
    private Set<String> mReqIds = new HashSet();
    private SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnZPNSLoginStatus(int i) {
            super.OnZPNSLoginStatus(i);
            if (i == 1) {
                PbxSmsFragment.this.requestSyncNewMessages();
                if (PbxSmsFragment.this.mSmsRecyleView != null) {
                    PbxSmsFragment.this.mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            super.OnPBXFeatureOptionsChanged(list);
            if (list != null && list.size() != 0 && ZMPhoneUtils.isPBXFeatureOptionChanged(list, CmmSIPMessageManager.getInstance().getMessageEnabledBit()) && !CmmSIPMessageManager.getInstance().isMessageEnabled()) {
                PbxSmsFragment.this.onClickBtnBack();
            }
        }
    };
    @Nullable
    private String mSelectedCallId;
    /* access modifiers changed from: private */
    public BigRoundListDialog mSenderNumberDialog;
    /* access modifiers changed from: private */
    @Nullable
    public String mSessionId;
    /* access modifiers changed from: private */
    public PbxSmsRecyleView mSmsRecyleView;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout mSwipeRefreshLayout;
    /* access modifiers changed from: private */
    @Nullable
    public ArrayList<String> mToNumbers;
    private TextView mTvSenderNumber;
    @Nullable
    private TextView mTxtDisableMsg;
    private TextView mTxtTitle;
    private Runnable mUnreadCheckRunnable = new Runnable() {
        public void run() {
            PbxSmsFragment.this.setMsgAsRead();
            PbxSmsFragment.this.mHandler.postDelayed(this, 500);
        }
    };
    @NonNull
    private Set<String> mUnreadMsgIds = new HashSet();
    private IPBXMessageEventSinkUIListener messageEventSinkUIListener = new SimpleIPBXMessageEventSinkUIListener() {
        public void OnSyncedNewMessages(int i, String str, String str2, List<String> list, List<String> list2, List<String> list3) {
            PbxSmsFragment.this.OnSyncedNewMessages(i, str2, list, list2, list3);
        }

        public void OnRequestDoneForSendMessage(int i, String str, String str2, String str3, String str4) {
            PbxSmsFragment.this.OnRequestDoneForSendMessage(i, str, str2, str3, str4);
        }

        public void OnNewSessionCreated(String str, String str2) {
            PbxSmsFragment.this.OnNewSessionCreated(str, str2);
        }

        public void OnSessionUpdated(String str) {
            PbxSmsFragment.this.OnSessionUpdated(str);
        }

        public void OnRequestDoneForQuerySessionByFromToNumbers(int i, String str, String str2) {
            PbxSmsFragment.this.OnRequestDoneForQuerySessionByFromToNumbers(i, str, str2);
        }

        public void OnRequestDoneForDeleteSessions(int i, String str, List<String> list) {
            PbxSmsFragment.this.OnRequestDoneForDeleteSessions(i, str, list);
        }

        public void OnFullSyncedMessages(int i, String str) {
            PbxSmsFragment.this.OnFullSyncedMessages(i, str);
        }

        public void OnNewMessageCreated(String str, String str2) {
            PbxSmsFragment.this.OnNewMessageCreated(str, str2);
        }

        public void OnMessageUpdated(String str, String str2) {
            PbxSmsFragment.this.OnMessageUpdated(str, str2);
        }

        public void OnMessagesDeleted(String str, List<String> list) {
            PbxSmsFragment.this.OnMessagesDeleted(str, list);
        }

        public void OnRequestDoneForSyncOldMessages(int i, String str, String str2, List<String> list) {
            PbxSmsFragment.this.OnRequestDoneForSyncOldMessages(i, str, str2, list);
        }

        public void OnRequestDoneForDeleteMessage(int i, String str, String str2, List<String> list) {
            PbxSmsFragment.this.OnRequestDoneForDeleteMessage(i, str, str2, list);
        }

        public void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list) {
            PbxSmsFragment.this.OnRequestDoneForUpdateMessageReadStatus(i, str, str2, list);
        }

        public void OnSessionsDeleted(List<String> list) {
            PbxSmsFragment.this.OnSessionsDeleted(list);
        }
    };

    public static class DeleteMsgDialog extends ZMDialogFragment {
        private static final String ARG_MESSAGE = "message_id";
        private static final String ARG_SESSION_ID = "session_id";

        public static void showDialog(@NonNull FragmentManager fragmentManager, @NonNull String str, @NonNull String str2) {
            DeleteMsgDialog deleteMsgDialog = new DeleteMsgDialog();
            Bundle bundle = new Bundle();
            bundle.putString("message_id", str);
            bundle.putString("session_id", str2);
            deleteMsgDialog.setArguments(bundle);
            deleteMsgDialog.show(fragmentManager, DeleteMsgDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            final String string = arguments.getString("message_id");
            final String string2 = arguments.getString("session_id");
            if (TextUtils.isEmpty(string2) || TextUtils.isEmpty(string)) {
                return createEmptyDialog();
            }
            return new Builder(getActivity()).setCancelable(true).setTitle(C4558R.string.zm_sip_title_delete_message_117773).setMessage(C4558R.string.zm_sip_lbl_delete_message_117773).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).setPositiveButton(C4558R.string.zm_btn_delete, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(string2);
                    if (sessionById != null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(string);
                        if (TextUtils.isEmpty(sessionById.requestDeleteMessage(arrayList))) {
                            FragmentActivity activity = DeleteMsgDialog.this.getActivity();
                            if (activity instanceof ZMActivity) {
                                DialogUtils.showAlertDialog((ZMActivity) activity, C4558R.string.zm_sip_delete_message_error_117773, C4558R.string.zm_sip_try_later_117773, C4558R.string.zm_btn_ok);
                            }
                        }
                    }
                }
            }).create();
        }
    }

    static class MessageMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 2;
        public static final int ACTION_DELETE_MESSAGE = 1;
        public static final int ACTION_TRY = 0;

        public MessageMenuItem(String str, int i) {
            super(i, str);
        }
    }

    /* access modifiers changed from: private */
    public void OnSessionUpdated(String str) {
    }

    public void onClickLinkPreview(PBXMessageItem pBXMessageItem, LinkPreviewMetaInfo linkPreviewMetaInfo) {
    }

    public void onClickMeetingNO(String str) {
    }

    public void onClickMessage(PBXMessageItem pBXMessageItem) {
    }

    public void onCommentSent(String str, String str2, String str3) {
    }

    public void onLayoutCompleted() {
    }

    public void onMessageSent(String str, String str2) {
    }

    public void onShowInvitationsSent(int i) {
    }

    public boolean onShowLinkContextMenu(View view, PBXMessageItem pBXMessageItem, String str) {
        return false;
    }

    public void onViewInitReady() {
    }

    public static void showAsSession(@NonNull ZMActivity zMActivity, @NonNull String str) {
        PbxSmsFragment pbxSmsFragment = new PbxSmsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SESSION_ID, str);
        pbxSmsFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, pbxSmsFragment, PbxSmsFragment.class.getName()).commit();
    }

    public static void showAsToNumbers(@NonNull ZMActivity zMActivity, @Nullable ArrayList<String> arrayList) {
        PbxSmsFragment pbxSmsFragment = new PbxSmsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TO_NUMBERS, arrayList);
        pbxSmsFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, pbxSmsFragment, PbxSmsFragment.class.getName()).commit();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_pbx_sms, viewGroup, false);
        this.mLayoutSelectContact = inflate.findViewById(C4558R.C4560id.layout_select_contact);
        this.mLayoutContent = inflate.findViewById(C4558R.C4560id.layout_content);
        this.mEdtSelectedContact = (ZMEditText) inflate.findViewById(C4558R.C4560id.et_selected_contact);
        this.mSmsRecyleView = (PbxSmsRecyleView) inflate.findViewById(C4558R.C4560id.smsRecyleView);
        this.mDirectoryListView = (PBXDirectorySearchListView) inflate.findViewById(C4558R.C4560id.directoryListView);
        this.mLayoutSenderNumber = inflate.findViewById(C4558R.C4560id.layout_select_sender_number);
        this.mTvSenderNumber = (TextView) inflate.findViewById(C4558R.C4560id.tv_sender_number);
        this.mKeyboardDetector = (ZMKeyboardDetector) inflate.findViewById(C4558R.C4560id.keyboardDetector);
        this.mKeyboardDetector.setKeyboardListener(this);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSearch = inflate.findViewById(C4558R.C4560id.btnSearch);
        this.mPanelTitleCenter = inflate.findViewById(C4558R.C4560id.panelTitleCenter);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mImgTitleInfo = (ImageView) inflate.findViewById(C4558R.C4560id.imgTitleInfo);
        this.mBtnPhoneCall = (ImageButton) inflate.findViewById(C4558R.C4560id.btnPhoneCall);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(C4558R.C4560id.swipeRefreshLayout);
        this.mTxtDisableMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtDisableMsg);
        this.mPanelActions = inflate.findViewById(C4558R.C4560id.panelActions);
        this.mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                PbxSmsFragment.this.loadHistorySms();
            }
        });
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSearch.setOnClickListener(this);
        this.mPanelTitleCenter.setOnClickListener(this);
        this.mTvSenderNumber.setOnClickListener(this);
        this.mBtnPhoneCall.setOnClickListener(this);
        this.mSmsRecyleView.setUICallBack(this);
        CmmSIPMessageManager.getInstance().addListener(this.messageEventSinkUIListener);
        this.mSmsRecyleView.addOnScrollListener(new OnScrollListener() {
            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
            }

            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                if (i != 0 && PbxSmsFragment.this.mKeyboardDetector.isKeyboardOpen()) {
                    UIUtil.closeSoftKeyboard(PbxSmsFragment.this.getActivity(), PbxSmsFragment.this.mSmsRecyleView);
                }
            }
        });
        return inflate;
    }

    public void onDestroyView() {
        CmmSIPMessageManager.getInstance().removeListener(this.messageEventSinkUIListener);
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    public void onStart() {
        super.onStart();
        this.mHandler.postDelayed(this.mUnreadCheckRunnable, 500);
        requestSyncNewMessages();
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (PbxSmsFragment.this.mLayoutSelectContact.getVisibility() == 0 && CollectionsUtil.isListEmpty(PbxSmsFragment.this.mToNumbers)) {
                    PbxSmsFragment.this.mEdtSelectedContact.requestFocus();
                }
                View view = PbxSmsFragment.this.getView();
                if (view != null && !PbxSmsFragment.this.mKeyboardDetector.isKeyboardOpen() && !TextUtils.isEmpty(PbxSmsFragment.this.mFromNumber)) {
                    UIUtil.openSoftKeyboard(PbxSmsFragment.this.getActivity(), view.findFocus());
                }
            }
        }, 300);
    }

    /* access modifiers changed from: private */
    public void requestSyncNewMessages() {
        if (this.mSessionId != null) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionId);
            if (sessionById != null) {
                sessionById.requestSyncNewMessages();
            }
        }
    }

    public void onStop() {
        this.mHandler.removeCallbacks(this.mUnreadCheckRunnable);
        setMsgAsRead();
        markSessionAsRead();
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        this.mSmsRecyleView.loadAllMsgs(true);
        if (this.mSmsRecyleView.isLoading()) {
            this.mSwipeRefreshLayout.setRefreshing(true);
        }
        this.mDirectoryListView.onResume();
        markSessionAsRead();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments == null) {
            dismiss();
            return;
        }
        this.mSessionId = arguments.getString(ARG_SESSION_ID);
        this.mToNumbers = (ArrayList) arguments.getSerializable(ARG_TO_NUMBERS);
        initInputFragment();
        updateSenderNumberItems(false);
        if (TextUtils.isEmpty(this.mSessionId)) {
            initDirectory();
        } else {
            checkAndInitSession();
        }
        initToNumberInSearchEdt();
    }

    /* access modifiers changed from: private */
    public void setMsgAsRead() {
        if (!TextUtils.isEmpty(this.mSessionId) && !this.mUnreadMsgIds.isEmpty()) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionId);
            if (sessionById != null) {
                sessionById.requestUpdateMessageReadStatus(new ArrayList(this.mUnreadMsgIds), 1);
                this.mUnreadMsgIds.clear();
            }
        }
    }

    private void markSessionAsRead() {
        if (!TextUtils.isEmpty(this.mSessionId) && this.mUnreadMsgIds.isEmpty()) {
            CmmSIPMessageManager.getInstance().requestMarkSessionAsRead(this.mSessionId);
            NotificationMgr.removePBXMessageNotification(getActivity(), this.mSessionId);
        }
    }

    /* access modifiers changed from: private */
    public void loadHistorySms() {
        if (this.mSmsRecyleView.hasMoreHistory()) {
            this.mSmsRecyleView.loadMoreMsgs();
            if (!this.mSmsRecyleView.isLoading()) {
                this.mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            return;
        }
        this.mSwipeRefreshLayout.setEnabled(false);
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    private void checkAndInitSession() {
        if (!TextUtils.isEmpty(this.mSessionId)) {
            this.mSmsRecyleView.setSessionId(this.mSessionId);
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionId);
            if (sessionById != null) {
                sessionById.initialize();
            }
            checkDirectNumberMathched(false);
            updateTitleBar(sessionById);
        }
    }

    /* access modifiers changed from: private */
    public void OnSyncedNewMessages(int i, String str, List<String> list, List<String> list2, List<String> list3) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mSmsRecyleView.OnSyncedNewMessages(i, str, list, list2, list3);
        }
    }

    /* access modifiers changed from: private */
    public void OnRequestDoneForSendMessage(int i, String str, String str2, String str3, String str4) {
        if (TextUtils.equals(str2, this.mSessionId) || TextUtils.equals(str3, this.mSessionId)) {
            if (!TextUtils.isEmpty(str2) && (TextUtils.isEmpty(this.mSessionId) || !TextUtils.equals(this.mSessionId, str2))) {
                this.mSessionId = str2;
                this.mSmsRecyleView.setSessionId(str2);
                this.mChatInputFragment.setPbxSessionId(str2);
                IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionId);
                if (sessionById != null) {
                    sessionById.initialize();
                    this.mSmsRecyleView.loadAllMsgs(false);
                    sessionById.requestSyncNewMessages();
                    updateTitleBar(sessionById);
                }
            }
            this.mSmsRecyleView.addNewMessage(str4);
            if (i == 7016) {
                EventTaskManager eventTaskManager = getEventTaskManager();
                if (eventTaskManager != null) {
                    eventTaskManager.pushLater(new EventAction("PbxSmsFragment.OnRequestDoneForSendMessage") {
                        public void run(IUIElement iUIElement) {
                            if (iUIElement instanceof PbxSmsFragment) {
                                FragmentActivity activity = PbxSmsFragment.this.getActivity();
                                if (activity instanceof ZMActivity) {
                                    DialogUtils.showAlertDialog((ZMActivity) activity, C4558R.string.zm_sip_rate_limit_117773, C4558R.string.zm_sip_rate_limit_msg_117773, C4558R.string.zm_btn_ok);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnNewSessionCreated(String str, String str2) {
        if (TextUtils.equals(str2, this.mSessionId) && !TextUtils.isEmpty(str)) {
            this.mSessionId = str;
            this.mSmsRecyleView.setSessionId(str);
        }
    }

    private void OnUnreadMessageCountInSessionUpdated(String str, int i) {
        if (TextUtils.equals(str, this.mSessionId)) {
        }
    }

    /* access modifiers changed from: private */
    public void OnRequestDoneForQuerySessionByFromToNumbers(int i, String str, String str2) {
        if (i == 0 && str != null && this.mReqIds.remove(str) && !TextUtils.isEmpty(str2)) {
            this.mSessionId = str2;
            this.mSmsRecyleView.setSessionId(str2);
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionId);
            if (sessionById != null) {
                sessionById.initialize();
                updateTitleBar(sessionById);
            }
            this.mSmsRecyleView.loadAllMsgs(false);
            this.mChatInputFragment.setPbxSessionId(str2);
        }
    }

    /* access modifiers changed from: private */
    public void OnRequestDoneForDeleteSessions(int i, String str, List<String> list) {
        if (i == 0 && list != null && list.contains(this.mSessionId)) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void OnFullSyncedMessages(int i, String str) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mSmsRecyleView.loadAllMsgs(false);
        }
    }

    private void OnForceFullSyncMessages(String str) {
        if (TextUtils.equals(str, this.mSessionId)) {
            this.mSmsRecyleView.loadAllMsgs(false);
        }
    }

    /* access modifiers changed from: private */
    public void OnNewMessageCreated(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId) && !TextUtils.isEmpty(str2)) {
            PbxSmsRecyleView pbxSmsRecyleView = this.mSmsRecyleView;
            if (pbxSmsRecyleView != null) {
                pbxSmsRecyleView.addNewMessage(str2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnMessageUpdated(String str, String str2) {
        if (TextUtils.equals(str, this.mSessionId) && !TextUtils.isEmpty(str2)) {
            PbxSmsRecyleView pbxSmsRecyleView = this.mSmsRecyleView;
            if (pbxSmsRecyleView != null) {
                pbxSmsRecyleView.updateMessage(str2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnMessagesDeleted(String str, List<String> list) {
        if (TextUtils.equals(str, this.mSessionId)) {
            PbxSmsRecyleView pbxSmsRecyleView = this.mSmsRecyleView;
            if (pbxSmsRecyleView != null) {
                pbxSmsRecyleView.OnRequestDoneForDeleteMessage(0, null, str, list);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnRequestDoneForSyncOldMessages(int i, String str, String str2, List<String> list) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            this.mSmsRecyleView.OnRequestDoneForSyncOldMessages(i, str, str2, list);
            if (!this.mSmsRecyleView.isLoading()) {
                this.mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnRequestDoneForDeleteMessage(int i, String str, String str2, List<String> list) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            if (i != 0) {
                EventTaskManager eventTaskManager = getEventTaskManager();
                if (eventTaskManager != null) {
                    eventTaskManager.pushLater(new EventAction("PbxSmsFragment.OnRequestDoneForDeleteMessage") {
                        public void run(IUIElement iUIElement) {
                            if (iUIElement instanceof PbxSmsFragment) {
                                FragmentActivity activity = PbxSmsFragment.this.getActivity();
                                if (activity instanceof ZMActivity) {
                                    DialogUtils.showAlertDialog((ZMActivity) activity, C4558R.string.zm_sip_delete_message_error_117773, C4558R.string.zm_sip_try_later_117773, C4558R.string.zm_btn_ok);
                                }
                            }
                        }
                    });
                }
                return;
            }
            PbxSmsRecyleView pbxSmsRecyleView = this.mSmsRecyleView;
            if (pbxSmsRecyleView != null) {
                pbxSmsRecyleView.OnRequestDoneForDeleteMessage(i, str, str2, list);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list) {
        if (TextUtils.equals(str2, this.mSessionId)) {
            PbxSmsRecyleView pbxSmsRecyleView = this.mSmsRecyleView;
            if (pbxSmsRecyleView != null) {
                pbxSmsRecyleView.OnRequestDoneForUpdateMessageReadStatus(i, str, str2, list);
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnSessionsDeleted(List<String> list) {
        if (list != null && list.contains(this.mSessionId)) {
            dismiss();
        }
    }

    public static PbxSmsFragment findPBXSMSFragment(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        return (PbxSmsFragment) fragmentManager.findFragmentByTag(PbxSmsFragment.class.getName());
    }

    private void updateTitleBar(@Nullable IPBXMessageSession iPBXMessageSession) {
        if (!TextUtils.isEmpty(this.mSessionId)) {
            this.mBtnBack.setText("");
            this.mBtnBack.setBackgroundResource(C4558R.C4559drawable.zm_btn_back_white);
            IPBXMessageSessionItem iPBXMessageSessionItem = null;
            if (iPBXMessageSession != null) {
                iPBXMessageSessionItem = IPBXMessageSessionItem.fromMessageSession(iPBXMessageSession);
            } else {
                IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
                if (messageAPI != null) {
                    iPBXMessageSessionItem = IPBXMessageSessionItem.fromMessageSession(this.mSessionId, messageAPI);
                }
            }
            if (iPBXMessageSessionItem != null) {
                List others = iPBXMessageSessionItem.getOthers();
                if (!CollectionsUtil.isListEmpty(others)) {
                    PBXMessageContact me = iPBXMessageSessionItem.getMe();
                    if (me != null) {
                        updateMemberList(me, others);
                    }
                    if (others.size() == 1) {
                        this.mBtnPhoneCall.setTag(((PBXMessageContact) others.get(0)).getPhoneNumber());
                        this.mBtnPhoneCall.setVisibility(0);
                    }
                    this.mTxtTitle.setText(iPBXMessageSessionItem.getDisplayName());
                    this.mTxtTitle.setContentDescription(getString(C4558R.string.zm_accessibility_button_99142, iPBXMessageSessionItem.getDisplayName()));
                    this.mTxtTitle.requestLayout();
                    this.mImgTitleInfo.setVisibility(0);
                }
            }
        }
    }

    private void updateMemberList(@NonNull PBXMessageContact pBXMessageContact, @NonNull List<PBXMessageContact> list) {
        if (this.mMemberList == null) {
            this.mMemberList = new ArrayList<>();
        }
        this.mMemberList.clear();
        IMAddrBookItem iMAddrBookItem = null;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                iMAddrBookItem = IMAddrBookItem.fromZoomBuddy(myself);
            }
        }
        PBXMessageContact pBXMessageContact2 = new PBXMessageContact(pBXMessageContact.getPhoneNumber(), getString(C4558R.string.zm_title_direct_number_31439), iMAddrBookItem);
        pBXMessageContact2.setDisplayName(pBXMessageContact.getDisplayName());
        this.mMemberList.add(pBXMessageContact2);
        for (PBXMessageContact fromProto : list) {
            this.mMemberList.add(PBXMessageContact.fromProto(fromProto));
        }
    }

    public boolean onBackPressed() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment == null || !mMChatInputFragment.isAdded() || !this.mChatInputFragment.onBackPressed()) {
            return false;
        }
        return true;
    }

    private void initToNumberInSearchEdt() {
        String str;
        String str2;
        if (!CollectionUtils.isEmpty(this.mToNumbers) && getContext() != null) {
            Editable editableText = this.mEdtSelectedContact.getEditableText();
            Iterator it = this.mToNumbers.iterator();
            while (it.hasNext()) {
                String str3 = (String) it.next();
                IMAddrBookItem iMAddrBookItemByNumber = ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(str3);
                PBXMessageContact pBXMessageContact = new PBXMessageContact(str3, iMAddrBookItemByNumber);
                PBXMessageSelectContactSpan pBXMessageSelectContactSpan = new PBXMessageSelectContactSpan(getContext(), pBXMessageContact, ZMPhoneUtils.isValidPhoneNumber(pBXMessageContact.getPhoneNumber()));
                if (iMAddrBookItemByNumber == null) {
                    str = null;
                } else {
                    str = iMAddrBookItemByNumber.getScreenName();
                }
                if (!TextUtils.isEmpty(str)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TextUtils.ellipsize(str, this.mEdtSelectedContact.getPaint(), (float) UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 150.0f), TruncateAt.END));
                    sb.append("");
                    str2 = sb.toString();
                } else {
                    str2 = pBXMessageContact.getPhoneNumber();
                }
                int length = editableText.length();
                int length2 = str2.length() + length;
                editableText.append(str2);
                editableText.setSpan(pBXMessageSelectContactSpan, length, length2, 33);
                this.mEdtSelectedContact.setSelection(length2);
                this.mEdtSelectedContact.setCursorVisible(true);
            }
        }
    }

    private void initInputFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            this.mChatInputFragment = new MMChatInputFragment();
            this.mChatInputFragment.setOnChatInputListener(this);
            this.mChatInputFragment.setKeyboardDetector(this.mKeyboardDetector);
            Bundle bundle = new Bundle();
            bundle.putString(MMChatInputFragment.ARG_PBX_SESSION, this.mSessionId);
            bundle.putInt(MMChatInputFragment.ARG_INPUT_MODE, 1);
            bundle.putString(MMChatInputFragment.ARG_PBX_SESSION, this.mSessionId);
            this.mChatInputFragment.setArguments(bundle);
            beginTransaction.add(C4558R.C4560id.panelActions, (Fragment) this.mChatInputFragment);
            beginTransaction.commit();
            if (!CollectionsUtil.isCollectionEmpty(this.mToNumbers)) {
                this.mChatInputFragment.setPbxToNumbers(this.mToNumbers);
            }
        }
    }

    private void initDirectory() {
        this.mLayoutSelectContact.setVisibility(0);
        this.mDirectoryListView.setFilterType(2);
        this.mDirectoryListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Object itemAtPosition = PbxSmsFragment.this.mDirectoryListView.getItemAtPosition(i);
                if (itemAtPosition instanceof IMAddrBookItem) {
                    IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) itemAtPosition;
                    List externalCloudNumbers = iMAddrBookItem.getExternalCloudNumbers();
                    if (CollectionsUtil.isListEmpty(externalCloudNumbers) || externalCloudNumbers.size() != 1) {
                        if (iMAddrBookItem.isFromPhoneContacts()) {
                            List phoneNumberList = iMAddrBookItem.getContact().getPhoneNumberList();
                            if (phoneNumberList != null && phoneNumberList.size() == 1) {
                                PbxSmsFragment.this.selectContact(new PBXMessageContact((String) phoneNumberList.get(0), iMAddrBookItem), true);
                                return;
                            }
                        }
                        PhoneLabelFragment.show(PbxSmsFragment.this.getChildFragmentManager(), iMAddrBookItem, 1001);
                        return;
                    }
                    PbxSmsFragment.this.selectContact(new PBXMessageContact((String) externalCloudNumbers.get(0), iMAddrBookItem), true);
                }
            }
        });
        this.mEdtSelectedContact.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Editable text = PbxSmsFragment.this.mEdtSelectedContact.getText();
                if (i3 < i2) {
                    final PBXMessageSelectContactSpan[] pBXMessageSelectContactSpanArr = (PBXMessageSelectContactSpan[]) text.getSpans(i3 + i, i + i2, PBXMessageSelectContactSpan.class);
                    if (pBXMessageSelectContactSpanArr.length > 0) {
                        PbxSmsFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                if (PbxSmsFragment.this.isResumed()) {
                                    for (PBXMessageSelectContactSpan item : pBXMessageSelectContactSpanArr) {
                                        PbxSmsFragment.this.selectContact(item.getItem(), false);
                                    }
                                }
                            }
                        });
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                PbxSmsFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (PbxSmsFragment.this.isResumed()) {
                            PbxSmsFragment.this.formatSearchEditText();
                            Editable text = PbxSmsFragment.this.mEdtSelectedContact.getText();
                            PBXMessageSelectContactSpan[] pBXMessageSelectContactSpanArr = (PBXMessageSelectContactSpan[]) text.getSpans(0, text.length(), PBXMessageSelectContactSpan.class);
                            if (pBXMessageSelectContactSpanArr.length > 0) {
                                PBXMessageSelectContactSpan pBXMessageSelectContactSpan = pBXMessageSelectContactSpanArr[0];
                                text.delete(0, text.getSpanStart(pBXMessageSelectContactSpan));
                                text.delete(text.getSpanEnd(pBXMessageSelectContactSpan), text.length());
                                PbxSmsFragment.this.mDirectoryListView.setVisibility(8);
                                PbxSmsFragment.this.mLayoutContent.setVisibility(0);
                            } else {
                                PbxSmsFragment.this.mDirectoryListView.filter(PbxSmsFragment.this.getFilter());
                                if (PbxSmsFragment.this.mDirectoryListView.hasData()) {
                                    PbxSmsFragment.this.mDirectoryListView.setVisibility(0);
                                    PbxSmsFragment.this.mLayoutContent.setVisibility(8);
                                } else {
                                    PbxSmsFragment.this.mDirectoryListView.setVisibility(8);
                                    PbxSmsFragment.this.mLayoutContent.setVisibility(0);
                                }
                            }
                            PbxSmsFragment.this.updateToNumbers();
                        }
                    }
                });
            }
        });
        this.mEdtSelectedContact.setMovementMethod(ZMReplaceSpanMovementMethod.getInstance());
        this.mEdtSelectedContact.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, @Nullable KeyEvent keyEvent) {
                boolean z = false;
                if (keyEvent == null) {
                    return false;
                }
                if (i == 6 || (keyEvent.getKeyCode() == 66 && keyEvent.getAction() == 0)) {
                    z = true;
                }
                return z;
            }
        });
        this.mEdtSelectedContact.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                FragmentActivity activity = PbxSmsFragment.this.getActivity();
                if (activity != null && !activity.isFinishing()) {
                    if (z) {
                        if (CollectionUtils.isEmpty(PbxSmsFragment.this.getSelectContacs())) {
                            PbxSmsFragment.this.mEdtSelectedContact.setText(PbxSmsFragment.this.mEdtSelectedContact.getEditableText().toString());
                        }
                        PbxSmsFragment.this.mChatInputFragment.resetKeyBoard();
                    }
                    if (!z && !PbxSmsFragment.this.mDirectoryListView.hasData() && !TextUtils.isEmpty(PbxSmsFragment.this.getFilter().trim())) {
                        PbxSmsFragment pbxSmsFragment = PbxSmsFragment.this;
                        pbxSmsFragment.selectContact(new PBXMessageContact(pbxSmsFragment.getFilter()), true);
                    }
                }
            }
        });
        updateSelectSenderNumber();
    }

    public void onPbxSmsSent(@Nullable String str, @Nullable String str2) {
        if (TextUtils.isEmpty(this.mSessionId)) {
            this.mSessionId = str;
            this.mSmsRecyleView.setSessionId(this.mSessionId);
            if (!TextUtils.isEmpty(str) && CmmSIPMessageManager.getInstance().isLocalSession(str)) {
                CmmSIPMessageManager.getInstance().notifyNewLocalSessionCreated(str);
                updateTitleBar(null);
                this.mSmsRecyleView.loadAllMsgs(false);
            }
        }
        if (TextUtils.isEmpty(this.mFromNumber)) {
            this.mSmsRecyleView.addSystemMessage(getString(C4558R.string.zm_sip_lbl_no_match_number_117773));
        } else {
            this.mSmsRecyleView.addNewMessage(str2);
        }
        hideSelectContactViews();
    }

    public void onKeyboardOpen() {
        if (this.mChatInputFragment != null && (this.mLayoutSelectContact.getVisibility() != 0 || !this.mEdtSelectedContact.hasFocus())) {
            this.mChatInputFragment.onKeyboardOpen();
        }
        this.mSmsRecyleView.scrollToBottom(true);
    }

    public void onKeyboardClosed() {
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.onKeyboardClosed();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.tv_sender_number) {
            onClickSendNumber();
        } else if (id == C4558R.C4560id.btnPhoneCall) {
            onClickPhoneCall();
        } else if (id == C4558R.C4560id.btnSearch || id == C4558R.C4560id.panelTitleCenter) {
            onClickPanelTitleCenter();
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnBack() {
        dismiss();
    }

    private void onClickSendNumber() {
        FragmentActivity activity = getActivity();
        if (activity != null && !CollectionsUtil.isListEmpty(this.mFromNumberItems)) {
            BigRoundListDialog bigRoundListDialog = this.mSenderNumberDialog;
            if (bigRoundListDialog == null || !bigRoundListDialog.isShowing()) {
                this.mSenderNumberDialog = new BigRoundListDialog(activity);
                this.mSenderNumberDialog.setTitle(C4558R.string.zm_sip_title_number_117773);
                PBXCallerIDListAdapter pBXCallerIDListAdapter = new PBXCallerIDListAdapter(getActivity());
                pBXCallerIDListAdapter.setList(this.mFromNumberItems);
                this.mSenderNumberDialog.setAdapter(pBXCallerIDListAdapter);
                this.mSenderNumberDialog.setDialogCallback(new DialogCallback() {
                    public void onCancel() {
                    }

                    public void onClose() {
                    }

                    public void onItemSelected(int i) {
                        ZMListAdapter adapter = PbxSmsFragment.this.mSenderNumberDialog.getAdapter();
                        if (adapter != null) {
                            int i2 = 0;
                            while (i2 < adapter.getList().size()) {
                                IZMListItem item = adapter.getItem(i2);
                                if (item instanceof SenderNumberListItem) {
                                    ((SenderNumberListItem) item).setSelected(i2 == i);
                                }
                                i2++;
                            }
                        }
                        PbxSmsFragment.this.updateSelectSenderNumber();
                    }
                });
                if (getActivity() != null && !getActivity().isFinishing()) {
                    this.mSenderNumberDialog.show();
                }
                return;
            }
            this.mSenderNumberDialog.dismiss();
            this.mSenderNumberDialog = null;
        }
    }

    private void onClickPhoneCall() {
        Object tag = this.mBtnPhoneCall.getTag();
        if (tag instanceof String) {
            String str = (String) tag;
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                ZMPhoneUtils.callSip(str, this.mTxtTitle.getText().toString());
            } else {
                this.mSelectedCallId = str;
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 11);
            }
        }
    }

    private void onClickPanelTitleCenter() {
        if (!TextUtils.isEmpty(this.mSessionId)) {
            ArrayList<PBXMessageContact> arrayList = this.mMemberList;
            if (arrayList != null) {
                PBXMessageSessionInfoFragment.show(this, arrayList);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null && i == 11 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
            String str = this.mSelectedCallId;
            if (str != null) {
                ZMPhoneUtils.callSip(str, this.mTxtTitle.getText().toString());
            }
            this.mSelectedCallId = null;
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C420315 r2 = new EventAction("PbxSmsFragmentPermissionResult") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof PbxSmsFragment) {
                        PbxSmsFragment pbxSmsFragment = (PbxSmsFragment) iUIElement;
                        if (pbxSmsFragment.isAdded()) {
                            pbxSmsFragment.handleRequestPermissionResult(i2, strArr2, iArr2);
                        }
                    }
                }
            };
            eventTaskManager.pushLater("PbxSmsFragmentPermissionResult", r2);
        }
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    /* access modifiers changed from: private */
    @NonNull
    public String getFilter() {
        String str = "";
        Editable text = this.mEdtSelectedContact.getText();
        PBXMessageSelectContactSpan[] pBXMessageSelectContactSpanArr = (PBXMessageSelectContactSpan[]) text.getSpans(0, text.length(), PBXMessageSelectContactSpan.class);
        if (pBXMessageSelectContactSpanArr.length <= 0) {
            return text.toString();
        }
        int spanEnd = text.getSpanEnd(pBXMessageSelectContactSpanArr[pBXMessageSelectContactSpanArr.length - 1]);
        int length = text.length();
        return spanEnd < length ? text.subSequence(spanEnd, length).toString() : str;
    }

    /* access modifiers changed from: private */
    @NonNull
    public List<IMAddrBookItem> getSelectContacs() {
        ArrayList arrayList = new ArrayList();
        PBXMessageSelectContactSpan[] pBXMessageSelectContactSpanArr = (PBXMessageSelectContactSpan[]) StringUtil.getSortedSpans(this.mEdtSelectedContact.getEditableText(), PBXMessageSelectContactSpan.class);
        if (pBXMessageSelectContactSpanArr == null) {
            return arrayList;
        }
        for (PBXMessageSelectContactSpan item : pBXMessageSelectContactSpanArr) {
            IMAddrBookItem item2 = item.getItem().getItem();
            if (item2 != null) {
                arrayList.add(item2);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void updateToNumbers() {
        this.mToNumbers = new ArrayList<>();
        Editable editableText = this.mEdtSelectedContact.getEditableText();
        PBXMessageSelectContactSpan[] pBXMessageSelectContactSpanArr = (PBXMessageSelectContactSpan[]) StringUtil.getSortedSpans(editableText, PBXMessageSelectContactSpan.class);
        if (pBXMessageSelectContactSpanArr != null) {
            for (PBXMessageSelectContactSpan pBXMessageSelectContactSpan : pBXMessageSelectContactSpanArr) {
                if (pBXMessageSelectContactSpan.getItem().getItem() != null) {
                    this.mToNumbers.add(ZMPhoneUtils.formatPhoneNumberAsE164(pBXMessageSelectContactSpan.getItem().getPhoneNumber()));
                }
            }
        }
        if (this.mToNumbers.isEmpty()) {
            String trim = editableText.toString().trim();
            if (trim.length() > 0) {
                this.mToNumbers.add(ZMPhoneUtils.formatPhoneNumberAsE164(trim));
            }
        }
        MMChatInputFragment mMChatInputFragment = this.mChatInputFragment;
        if (mMChatInputFragment != null) {
            mMChatInputFragment.setPbxToNumbers(this.mToNumbers);
        }
    }

    /* access modifiers changed from: private */
    public void formatSearchEditText() {
        int i;
        Editable editableText = this.mEdtSelectedContact.getEditableText();
        PBXMessageSelectContactSpan[] pBXMessageSelectContactSpanArr = (PBXMessageSelectContactSpan[]) StringUtil.getSortedSpans(editableText, PBXMessageSelectContactSpan.class);
        if (pBXMessageSelectContactSpanArr != null && pBXMessageSelectContactSpanArr.length > 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editableText);
            boolean z = false;
            for (int i2 = 0; i2 < pBXMessageSelectContactSpanArr.length; i2++) {
                int spanStart = spannableStringBuilder.getSpanStart(pBXMessageSelectContactSpanArr[i2]);
                if (i2 == 0) {
                    i = 0;
                } else {
                    i = spannableStringBuilder.getSpanEnd(pBXMessageSelectContactSpanArr[i2 - 1]);
                }
                if (spanStart != i) {
                    CharSequence subSequence = spannableStringBuilder.subSequence(i, spanStart);
                    spannableStringBuilder.replace(i, spanStart, "");
                    int spanEnd = spannableStringBuilder.getSpanEnd(pBXMessageSelectContactSpanArr[pBXMessageSelectContactSpanArr.length - 1]);
                    spannableStringBuilder.replace(spanEnd, spanEnd, subSequence);
                    z = true;
                }
            }
            if (z) {
                this.mEdtSelectedContact.setText(spannableStringBuilder);
                this.mEdtSelectedContact.setSelection(spannableStringBuilder.length());
            }
        }
    }

    public void selectContact(@NonNull PBXMessageContact pBXMessageContact, boolean z) {
        PBXMessageSelectContactSpan pBXMessageSelectContactSpan;
        String str;
        Context context = getContext();
        if (context != null) {
            Editable text = this.mEdtSelectedContact.getText();
            int i = 0;
            PBXMessageSelectContactSpan[] pBXMessageSelectContactSpanArr = (PBXMessageSelectContactSpan[]) text.getSpans(0, text.length(), PBXMessageSelectContactSpan.class);
            String str2 = null;
            if (pBXMessageSelectContactSpanArr != null) {
                int length = pBXMessageSelectContactSpanArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    pBXMessageSelectContactSpan = pBXMessageSelectContactSpanArr[i2];
                    if (StringUtil.isSameString(pBXMessageSelectContactSpan.getItem().getPhoneNumber(), pBXMessageContact.getPhoneNumber())) {
                        break;
                    }
                    i2++;
                }
            }
            pBXMessageSelectContactSpan = null;
            if (z) {
                if (this.mToNumbers == null) {
                    this.mToNumbers = new ArrayList<>();
                }
                if (pBXMessageSelectContactSpanArr != null && pBXMessageSelectContactSpanArr.length > 0 && this.mToNumbers.contains(pBXMessageContact.getPhoneNumber())) {
                    formatSearchEditText();
                    return;
                } else if (pBXMessageSelectContactSpan != null) {
                    pBXMessageSelectContactSpan.setItem(pBXMessageContact);
                    return;
                } else {
                    if (pBXMessageSelectContactSpanArr != null) {
                        i = pBXMessageSelectContactSpanArr.length;
                    }
                    if (i > 0) {
                        int spanEnd = text.getSpanEnd(pBXMessageSelectContactSpanArr[i - 1]);
                        int length2 = text.length();
                        if (spanEnd < length2) {
                            text.delete(spanEnd, length2);
                        }
                    } else {
                        text.clear();
                    }
                    if (pBXMessageContact.getItem() == null) {
                        pBXMessageContact.setItem(ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(pBXMessageContact.getPhoneNumber()));
                    }
                    PBXMessageSelectContactSpan pBXMessageSelectContactSpan2 = new PBXMessageSelectContactSpan(context, pBXMessageContact, ZMPhoneUtils.isValidPhoneNumber(pBXMessageContact.getPhoneNumber()));
                    IMAddrBookItem item = pBXMessageContact.getItem();
                    if (item != null) {
                        str2 = item.getScreenName();
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(TextUtils.ellipsize(str2, this.mEdtSelectedContact.getPaint(), (float) UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 150.0f), TruncateAt.END));
                        sb.append("");
                        str = sb.toString();
                    } else {
                        str = pBXMessageContact.getPhoneNumber();
                    }
                    int length3 = text.length();
                    int length4 = str.length() + length3;
                    text.append(str);
                    text.setSpan(pBXMessageSelectContactSpan2, length3, length4, 33);
                    this.mEdtSelectedContact.setSelection(length4);
                }
            } else if (pBXMessageSelectContactSpan != null) {
                int spanStart = text.getSpanStart(pBXMessageSelectContactSpan);
                int spanEnd2 = text.getSpanEnd(pBXMessageSelectContactSpan);
                if (spanStart >= 0 && spanEnd2 >= 0 && spanEnd2 >= spanStart) {
                    text.delete(spanStart, spanEnd2);
                    text.removeSpan(pBXMessageSelectContactSpan);
                }
            } else {
                return;
            }
            this.mEdtSelectedContact.setCursorVisible(true);
        }
    }

    /* access modifiers changed from: private */
    public void updateSelectSenderNumber() {
        this.mLayoutSenderNumber.setVisibility(this.mFromNumberItems.size() > 1 ? 0 : 8);
        this.mFromNumber = getSelectedSenderNumber();
        this.mTvSenderNumber.setText(this.mFromNumber);
        this.mChatInputFragment.setPbxFromNumber(this.mFromNumber);
    }

    /* access modifiers changed from: private */
    public void updateSenderNumberItems(boolean z) {
        this.mFromNumberItems.clear();
        List directNumberList = CmmSIPCallManager.getInstance().getDirectNumberList();
        if (!CollectionsUtil.isListEmpty(directNumberList)) {
            int size = directNumberList.size();
            int i = 0;
            while (i < size) {
                this.mFromNumberItems.add(new SenderNumberListItem(ZMPhoneUtils.formatPhoneNumber((String) directNumberList.get(i)), i == 0));
                i++;
            }
        }
        checkDirectNumberMathched(z);
    }

    private void checkDirectNumberMathched(boolean z) {
        PBXMessageContact pBXMessageContact;
        boolean z2;
        if (this.mPanelActions != null && this.mTxtDisableMsg != null && !StringUtil.isEmptyOrNull(this.mSessionId)) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionId);
            if (sessionById == null) {
                IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
                if (messageAPI != null) {
                    pBXMessageContact = IPBXMessageSessionItem.fromMessageSession(this.mSessionId, messageAPI).getMe();
                } else {
                    return;
                }
            } else {
                pBXMessageContact = sessionById.getMe();
            }
            if (pBXMessageContact != null) {
                String phoneNumber = pBXMessageContact.getPhoneNumber();
                Iterator it = this.mFromNumberItems.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (ZMPhoneUtils.isNumberMatched(phoneNumber, ((SenderNumberListItem) it.next()).getLabel())) {
                            z2 = true;
                            break;
                        }
                    } else {
                        z2 = false;
                        break;
                    }
                }
                if (z2) {
                    this.mFromNumber = phoneNumber;
                    this.mChatInputFragment.setPbxFromNumber(phoneNumber);
                    this.mPanelActions.setVisibility(0);
                    this.mTxtDisableMsg.setVisibility(8);
                } else {
                    this.mFromNumber = null;
                    this.mChatInputFragment.setPbxFromNumber(null);
                    if (!z) {
                        this.mPanelActions.setVisibility(8);
                        this.mTxtDisableMsg.setVisibility(0);
                        this.mTxtDisableMsg.setText(C4558R.string.zm_sip_lbl_no_match_number_117773);
                    }
                }
            }
        }
    }

    @Nullable
    private String getSelectedSenderNumber() {
        if (this.mFromNumberItems.size() == 0) {
            return null;
        }
        for (IZMListItem iZMListItem : this.mFromNumberItems) {
            if (iZMListItem.isSelected()) {
                return iZMListItem.getLabel();
            }
        }
        return ((SenderNumberListItem) this.mFromNumberItems.get(0)).getLabel();
    }

    private boolean closeKeyboardIfOpened(View view) {
        ZMKeyboardDetector zMKeyboardDetector = this.mKeyboardDetector;
        if (zMKeyboardDetector == null || !zMKeyboardDetector.isKeyboardOpen()) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), view);
        return true;
    }

    public void onMessageShowed(@NonNull PBXMessageItem pBXMessageItem) {
        if (pBXMessageItem.getRequestStatus() != 1 && pBXMessageItem.getReadStatus() != 1 && pBXMessageItem.getDirection() == 2) {
            this.mUnreadMsgIds.add(pBXMessageItem.getId());
        }
    }

    public boolean onShowContextMenu(View view, final PBXMessageItem pBXMessageItem) {
        if (view == null || pBXMessageItem == null) {
            return false;
        }
        if (closeKeyboardIfOpened(view)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    PbxSmsFragment.this.showContextMenu(pBXMessageItem);
                }
            }, 100);
        } else {
            showContextMenu(pBXMessageItem);
        }
        return true;
    }

    public void onClickStatusImage(PBXMessageItem pBXMessageItem) {
        if (pBXMessageItem != null) {
            showMessageSendErrorMenu(pBXMessageItem);
        }
    }

    public void showMessageSendErrorMenu(@NonNull final PBXMessageItem pBXMessageItem) {
        Context context = getContext();
        if (context != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
            ArrayList arrayList = new ArrayList(2);
            arrayList.add(new MessageMenuItem(context.getString(C4558R.string.zm_mm_lbl_try_again_70196), 0));
            arrayList.add(new MessageMenuItem(context.getString(C4558R.string.zm_mm_lbl_delete_message_70196), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            ZMAlertDialog create = new Builder(context).setTitle((CharSequence) context.getString(C4558R.string.zm_sip_title_send_failed_117773)).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String str;
                    if (!TextUtils.isEmpty(PbxSmsFragment.this.mSessionId)) {
                        Object item = zMMenuAdapter.getItem(i);
                        if (item instanceof MessageMenuItem) {
                            MessageMenuItem messageMenuItem = (MessageMenuItem) item;
                            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(PbxSmsFragment.this.mSessionId);
                            switch (messageMenuItem.getAction()) {
                                case 0:
                                    if (sessionById == null) {
                                        str = CmmSIPMessageManager.getInstance().requestRetrySendMessage(PbxSmsFragment.this.mSessionId, pBXMessageItem.getId());
                                    } else {
                                        str = sessionById.requestRetrySendMessage(pBXMessageItem.getId());
                                    }
                                    if (!TextUtils.isEmpty(str)) {
                                        PbxSmsFragment.this.mSmsRecyleView.addNewMessage(pBXMessageItem.getId());
                                        break;
                                    }
                                    break;
                                case 1:
                                    PbxSmsFragment.this.deleteMsg(pBXMessageItem);
                                    break;
                            }
                        }
                    }
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void showContextMenu(@NonNull final PBXMessageItem pBXMessageItem) {
        Context context = getContext();
        if (context != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
            ArrayList arrayList = new ArrayList(2);
            arrayList.add(new MessageMenuItem(context.getString(C4558R.string.zm_mm_msg_copy_82273), 2));
            arrayList.add(new MessageMenuItem(context.getString(C4558R.string.zm_mm_lbl_delete_message_70196), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            ZMAlertDialog create = new Builder(context).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!TextUtils.isEmpty(PbxSmsFragment.this.mSessionId)) {
                        Object item = zMMenuAdapter.getItem(i);
                        if (item instanceof MessageMenuItem) {
                            PbxSmsFragment.this.selectContextMenuItem((MessageMenuItem) item, pBXMessageItem);
                        }
                    }
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void selectContextMenuItem(@NonNull MessageMenuItem messageMenuItem, @NonNull PBXMessageItem pBXMessageItem) {
        switch (messageMenuItem.getAction()) {
            case 1:
                deleteMsg(pBXMessageItem);
                return;
            case 2:
                AndroidAppUtil.copyText(getContext(), pBXMessageItem.getText());
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void deleteMsg(@NonNull PBXMessageItem pBXMessageItem) {
        IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
        if (messageAPI != null) {
            String str = this.mSessionId;
            if (str != null) {
                if (messageAPI.getMessageCountInLocalSession(str) > 0) {
                    ArrayList arrayList = new ArrayList(1);
                    arrayList.add(pBXMessageItem.getId());
                    messageAPI.deleteMessagesInLocalSession(this.mSessionId, arrayList);
                    PbxSmsRecyleView pbxSmsRecyleView = this.mSmsRecyleView;
                    if (pbxSmsRecyleView != null) {
                        pbxSmsRecyleView.OnRequestDoneForDeleteMessage(0, null, this.mSessionId, arrayList);
                        CmmSIPMessageManager.getInstance().notifyLocalSessionMessageDeleted(this.mSessionId, pBXMessageItem.getId());
                        if (messageAPI.getMessageCountInLocalSession(this.mSessionId) == 0) {
                            CmmSIPMessageManager.getInstance().deleteLocalSession(this.mSessionId);
                            dismiss();
                        }
                    }
                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null && this.mSessionId != null) {
                        DeleteMsgDialog.showDialog(fragmentManager, pBXMessageItem.getId(), this.mSessionId);
                    }
                }
            }
        }
    }

    private void hideSelectContactViews() {
        if (this.mLayoutSelectContact.getVisibility() == 0) {
            this.mLayoutSelectContact.setVisibility(8);
            this.mDirectoryListView.setVisibility(8);
            this.mLayoutSenderNumber.setVisibility(8);
            this.mLayoutContent.setVisibility(0);
        }
    }
}
