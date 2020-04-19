package com.zipow.videobox.view.p014mm.contentfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZMSortUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.p014mm.ContentFileDeleteDialogFragment;
import com.zipow.videobox.view.p014mm.ContentFileMenuDialogFragment;
import com.zipow.videobox.view.p014mm.MMChatsListItem;
import com.zipow.videobox.view.p014mm.contentfile.ContentFileChatListAdapter.OnRecyclerViewListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMToast;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.contentfile.ContentFileChatListFragment */
public class ContentFileChatListFragment extends ZMDialogFragment implements OnRecyclerViewListener {
    @NonNull
    private static String FILE_ID = "file_id";
    @NonNull
    private static String FILE_IS_HIDE_DELETE_ITEM = "file_is_hide_delete_item";
    @NonNull
    private static String FILE_SHARE_OPERATOR_SESSION_ID_LIST = "file_share_operator_session_id_list";
    @NonNull
    private static String FILE_SHARE_SESSION_ID_LIST = "file_share_session_id_list";
    private static int REQUEST_CODE_SHOW_MENU = 1;
    @NonNull
    private static String TAG = "ContentFileChatListFragment";
    /* access modifiers changed from: private */
    public Runnable delayHandleMsgRunnable;
    @Nullable
    private String fileOwnerJid;
    private ImageView mCancelBtn;
    private ContentFileChatListAdapter mChatListAdapter;
    private RecyclerView mChatListRecyclerView;
    @Nullable
    private String mFileId;
    @Nullable
    private ArrayList<String> mFileShareOperatorSessionIds;
    @Nullable
    private ArrayList<String> mFileShareSessionIds;
    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<String> mUpdateIdList = new ArrayList<>();
    /* access modifiers changed from: private */
    @Nullable
    public MyHandler myHandler;
    @Nullable
    private String myJid;
    @NonNull
    private IZoomMessengerUIListener zoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onConnectReturn(int i) {
            ContentFileChatListFragment.this.onConnectReturn(i);
        }

        public boolean onIndicateMessageReceived(String str, String str2, String str3) {
            ContentFileChatListFragment.this.pushUpdateSessionIdToList(str);
            return super.onIndicateMessageReceived(str, str2, str3);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            ContentFileChatListFragment.this.pushUpdateSessionIdToList(str);
        }

        public void onNotify_ChatSessionUpdate(String str) {
            ContentFileChatListFragment.this.pushUpdateSessionIdToList(str);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            ContentFileChatListFragment.this.pushUpdateSessionIdToList(str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            ContentFileChatListFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }

        public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, long j2, boolean z) {
            if (z) {
                ContentFileChatListFragment.this.pushUpdateSessionIdToList(str2);
            }
        }

        public void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
            if (z) {
                ContentFileChatListFragment.this.pushUpdateSessionIdToList(str2);
            }
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.contentfile.ContentFileChatListFragment$MyHandler */
    private static class MyHandler extends Handler {
        private WeakReference<Context> mContext;

        public MyHandler(Context context) {
            this.mContext = new WeakReference<>(context);
        }

        public void handleMessage(Message message) {
            if (this.mContext.get() != null) {
                super.handleMessage(message);
            }
        }
    }

    public static void showAsFragment(Object obj, String str, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        showAsFragment(obj, str, arrayList, arrayList2, -1);
    }

    public static void showAsFragment(Object obj, String str, ArrayList<String> arrayList, ArrayList<String> arrayList2, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(FILE_ID, str);
        bundle.putStringArrayList(FILE_SHARE_SESSION_ID_LIST, arrayList);
        bundle.putStringArrayList(FILE_SHARE_OPERATOR_SESSION_ID_LIST, arrayList2);
        if (obj instanceof Fragment) {
            SimpleActivity.show((Fragment) obj, ContentFileChatListFragment.class.getName(), bundle, i, 2);
        } else if (obj instanceof ZMActivity) {
            SimpleActivity.show((ZMActivity) obj, ContentFileChatListFragment.class.getName(), bundle, i, false);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.fragment_content_file_chat_list, viewGroup, false);
        this.mCancelBtn = (ImageView) inflate.findViewById(C4558R.C4560id.zm_file_chat_list_title_cancel_btn);
        this.mChatListRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.content_file_list_view);
        this.mChatListAdapter = new ContentFileChatListAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.mChatListRecyclerView.setAdapter(this.mChatListAdapter);
        this.mChatListRecyclerView.setLayoutManager(linearLayoutManager);
        this.mChatListAdapter.setOnRecyclerViewListener(this);
        this.mCancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ContentFileChatListFragment.this.finishFragment(true);
            }
        });
        this.myHandler = new MyHandler(getContext());
        this.delayHandleMsgRunnable = new Runnable() {
            public void run() {
                if (CollectionsUtil.isListEmpty(ContentFileChatListFragment.this.mUpdateIdList)) {
                    ContentFileChatListFragment.this.myHandler.postDelayed(ContentFileChatListFragment.this.delayHandleMsgRunnable, 2000);
                    return;
                }
                if (ContentFileChatListFragment.this.mUpdateIdList.size() > 20) {
                    ContentFileChatListFragment.this.getChatList();
                } else {
                    Iterator it = ContentFileChatListFragment.this.mUpdateIdList.iterator();
                    while (it.hasNext()) {
                        ContentFileChatListFragment.this.updateSessionInfo((String) it.next());
                    }
                }
                ContentFileChatListFragment.this.mUpdateIdList.clear();
                ContentFileChatListFragment.this.myHandler.postDelayed(ContentFileChatListFragment.this.delayHandleMsgRunnable, 2000);
            }
        };
        ZoomMessengerUI.getInstance().addListener(this.zoomMessengerUIListener);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mFileId = arguments.getString(FILE_ID);
            this.mFileShareSessionIds = arguments.getStringArrayList(FILE_SHARE_SESSION_ID_LIST);
            this.mFileShareOperatorSessionIds = arguments.getStringArrayList(FILE_SHARE_OPERATOR_SESSION_ID_LIST);
            init();
        }
    }

    public void onDestroyView() {
        MyHandler myHandler2 = this.myHandler;
        if (myHandler2 != null) {
            myHandler2.removeCallbacks(this.delayHandleMsgRunnable);
        }
        ZoomMessengerUI.getInstance().removeListener(this.zoomMessengerUIListener);
        super.onDestroyView();
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i != REQUEST_CODE_SHOW_MENU || i2 != 2 || intent == null) {
            return;
        }
        if (StringUtil.isEmptyOrNull(intent.getStringExtra(ContentFileDeleteDialogFragment.UNSHARE_FILE_RESULT_ID_CODE))) {
            ErrorMsgDialog.newInstance(getString(C4558R.string.zm_alert_unshare_file_failed), -1).show(getFragmentManager(), ErrorMsgDialog.class.getName());
        } else {
            finishFragment(true);
        }
    }

    private void init() {
        getChatList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                this.myJid = myself.getJid();
            }
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(this.mFileId);
                if (fileWithWebFileID != null) {
                    this.fileOwnerJid = fileWithWebFileID.getOwner();
                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                }
            }
            MyHandler myHandler2 = this.myHandler;
            if (myHandler2 != null) {
                myHandler2.post(this.delayHandleMsgRunnable);
            }
        }
    }

    /* access modifiers changed from: private */
    public void getChatList() {
        if (!CollectionsUtil.isListEmpty(this.mFileShareSessionIds)) {
            ArrayList arrayList = new ArrayList();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                for (int i = 0; i < this.mFileShareSessionIds.size(); i++) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById((String) this.mFileShareSessionIds.get(i));
                    if (sessionById != null) {
                        MMChatsListItem fromZoomChatSession = MMChatsListItem.fromZoomChatSession(sessionById, zoomMessenger, getContext(), true);
                        if (fromZoomChatSession != null) {
                            arrayList.add(fromZoomChatSession);
                        }
                    }
                }
                if (!CollectionsUtil.isListEmpty(arrayList)) {
                    List sortSessions = ZMSortUtil.sortSessions(arrayList);
                    if (!CollectionsUtil.isListEmpty(sortSessions)) {
                        this.mChatListAdapter.setData(sortSessions);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onConnectReturn(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isConnectionGood() && isResumed()) {
            getChatList();
        }
    }

    /* access modifiers changed from: private */
    public void pushUpdateSessionIdToList(String str) {
        if (!TextUtils.isEmpty(str) && !this.mUpdateIdList.contains(str)) {
            this.mUpdateIdList.add(str);
        }
    }

    /* access modifiers changed from: private */
    public void updateSessionInfo(String str) {
        this.mChatListAdapter.updateSeesionInfo(str);
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, final String str2, long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("NotifyGroupDestroy") {
            public void run(IUIElement iUIElement) {
                FragmentActivity activity = ContentFileChatListFragment.this.getActivity();
                if (activity != null) {
                    ZMToast.show((Context) activity, (CharSequence) activity.getString(C4558R.string.zm_mm_msg_group_disbanded_by_admin_59554, new Object[]{str2}), 1);
                    if (ContentFileChatListFragment.this.isResumed()) {
                        ContentFileChatListFragment.this.finishFragment(true);
                    }
                }
            }
        });
    }

    public void onItemClick(String str, String str2) {
        if (!TextUtils.isEmpty(this.mFileId) && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (!NetworkUtil.hasDataNetwork(getActivity())) {
                showConnectionError();
            } else {
                ContentFileMenuDialogFragment.showContentFileMeunDialog(getFragmentManager(), this, REQUEST_CODE_SHOW_MENU, this.mFileId, str, str2, (!CollectionsUtil.isListEmpty(this.mFileShareOperatorSessionIds) && this.mFileShareOperatorSessionIds.contains(str)) || StringUtil.isSameString(this.fileOwnerJid, this.myJid));
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }
}
