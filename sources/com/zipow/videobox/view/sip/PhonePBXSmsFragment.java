package com.zipow.videobox.view.sip;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.zipow.videobox.PBXSMSActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.ptapp.PTAppProtos.PBXSessionUnreadCount;
import com.zipow.videobox.ptapp.PTAppProtos.PBXSessionUnreadCountList;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.ZMBuddyListListener;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.IPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.SimpleIPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.ISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.sip.sms.IPBXMessageSessionItem;
import com.zipow.videobox.view.sip.sms.PhonePBXMessageSessionRecyclerView;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXSmsFragment extends ZMDialogFragment implements OnClickListener, OnRecyclerViewListener {
    private ImageView mBtnKeyboard;
    private ImageView mBtnNewChat;
    private ZMAlertDialog mContextMenuDialog;
    private View mLayoutEmpty;
    private ISIPLineMgrEventSinkListener mLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            PhonePBXSmsFragment.this.checkSelfDirectNumber();
        }
    };
    /* access modifiers changed from: private */
    public String mLoadMoreReqId;
    private IPBXMessageEventSinkUIListener mMessageEventSinkUIListener = new SimpleIPBXMessageEventSinkUIListener() {
        public void OnFullSyncedSessions(int i) {
            super.OnFullSyncedSessions(i);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.initData();
            PhonePBXSmsFragment.this.updateEmptyView();
        }

        public void OnSyncedNewSessions(int i, String str, List<String> list, List<String> list2, List<String> list3) {
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.syncSessions(list, list2, list3);
            PhonePBXSmsFragment.this.updateEmptyView();
        }

        public void OnNewSessionCreated(String str, String str2) {
            super.OnNewSessionCreated(str, str2);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.addNewSession(str);
            if (!TextUtils.isEmpty(str2)) {
                PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.deleteSession(str2);
            }
            PhonePBXSmsFragment.this.updateEmptyView();
        }

        public void OnSessionUpdated(String str) {
            super.OnSessionUpdated(str);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str);
        }

        public void OnSessionsDeleted(List<String> list) {
            super.OnSessionsDeleted(list);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.syncSessions(null, null, list);
            PhonePBXSmsFragment.this.updateEmptyView();
        }

        public void OnRequestDoneForDeleteSessions(int i, String str, List<String> list) {
            super.OnRequestDoneForDeleteSessions(i, str, list);
            if (i == 0) {
                PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.syncSessions(null, null, list);
                PhonePBXSmsFragment.this.updateEmptyView();
                return;
            }
            EventTaskManager eventTaskManager = PhonePBXSmsFragment.this.getEventTaskManager();
            if (eventTaskManager != null) {
                eventTaskManager.pushLater(new EventAction("PhonePBXSmsFragment.OnRequestDoneForDeleteSessions") {
                    public void run(IUIElement iUIElement) {
                        if (iUIElement instanceof PhonePBXSmsFragment) {
                            FragmentActivity activity = PhonePBXSmsFragment.this.getActivity();
                            if (activity instanceof ZMActivity) {
                                DialogUtils.showAlertDialog((ZMActivity) activity, C4558R.string.zm_sip_delete_session_error_117773, C4558R.string.zm_sip_try_later_117773, C4558R.string.zm_btn_ok);
                            }
                        }
                    }
                });
            }
        }

        public void OnRequestDoneForSyncOldSessions(int i, String str, List<String> list) {
            super.OnRequestDoneForSyncOldSessions(i, str, list);
            if (TextUtils.equals(str, PhonePBXSmsFragment.this.mLoadMoreReqId) && !CollectionsUtil.isListEmpty(list)) {
                PhonePBXSmsFragment.this.mLoadMoreReqId = null;
                if (i == 0) {
                    PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.loadMore();
                }
            }
        }

        public void OnRequestDoneForMarkSessionAsRead(int i, String str, String str2) {
            super.OnRequestDoneForMarkSessionAsRead(i, str, str2);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str2);
        }

        public void OnMessagesDeleted(String str, List<String> list) {
            super.OnMessagesDeleted(str, list);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str);
        }

        public void OnNewMessageCreated(String str, String str2) {
            super.OnNewMessageCreated(str, str2);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str);
        }

        public void OnSessionUnreadMessageCountUpdated(PBXSessionUnreadCountList pBXSessionUnreadCountList) {
            super.OnSessionUnreadMessageCountUpdated(pBXSessionUnreadCountList);
            List<PBXSessionUnreadCount> itemsList = pBXSessionUnreadCountList.getItemsList();
            if (!CollectionsUtil.isListEmpty(itemsList)) {
                ArrayList arrayList = new ArrayList();
                for (PBXSessionUnreadCount sessionId : itemsList) {
                    arrayList.add(sessionId.getSessionId());
                }
                PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.syncSessions(null, arrayList, null);
            }
        }

        public void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list) {
            super.OnRequestDoneForUpdateMessageReadStatus(i, str, str2, list);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str2);
        }

        public void OnRequestDoneForSendMessage(int i, String str, String str2, String str3, String str4) {
            super.OnRequestDoneForSendMessage(i, str, str2, str3, str4);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str2);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateLocalSession(str3);
        }

        public void OnRequestDoneForDeleteMessage(int i, String str, String str2, List<String> list) {
            super.OnRequestDoneForDeleteMessage(i, str, str2, list);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str2);
        }

        public void OnMessageUpdated(String str, String str2) {
            super.OnMessageUpdated(str, str2);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateSession(str);
        }

        public void OnNewLocalSessionCreated(String str) {
            super.OnNewLocalSessionCreated(str);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.addNewLocalSession(str);
            PhonePBXSmsFragment.this.updateEmptyView();
        }

        public void OnLocalSessionDeleted(String str) {
            super.OnLocalSessionDeleted(str);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.deleteSession(str);
            PhonePBXSmsFragment.this.updateEmptyView();
        }

        public void OnLocalSessionMessageDeleted(String str, String str2) {
            super.OnLocalSessionMessageDeleted(str, str2);
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateLocalSession(str);
            PhonePBXSmsFragment.this.updateEmptyView();
        }
    };
    /* access modifiers changed from: private */
    public PhonePBXMessageSessionRecyclerView mPhonePBXMessageSessionRecyclerView;
    private SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnZPNSLoginStatus(int i) {
            super.OnZPNSLoginStatus(i);
            if (i == 1) {
                PhonePBXSmsFragment.this.requestSyncNewSessions();
            }
        }
    };
    private ZMBuddyListListener mZMBuddyListListener = new ZMBuddyListListener() {
        public void onBuddyListUpdate() {
            PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateContactNames();
        }

        public void onBuddyInfoUpdate(List<String> list, List<String> list2) {
            if (!CollectionsUtil.isListEmpty(list2)) {
                PhonePBXSmsFragment.this.mPhonePBXMessageSessionRecyclerView.updateContactNames();
            }
        }
    };

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_pbx_sms, viewGroup, false);
        this.mBtnKeyboard = (ImageView) inflate.findViewById(C4558R.C4560id.iv_keypad);
        this.mBtnNewChat = (ImageView) inflate.findViewById(C4558R.C4560id.iv_new_chat);
        this.mLayoutEmpty = inflate.findViewById(C4558R.C4560id.layout_empty);
        this.mPhonePBXMessageSessionRecyclerView = (PhonePBXMessageSessionRecyclerView) inflate.findViewById(C4558R.C4560id.rv_session_list);
        this.mPhonePBXMessageSessionRecyclerView.setOnRecyclerViewListener(this);
        this.mPhonePBXMessageSessionRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                if (i == 0) {
                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    Adapter adapter = recyclerView.getAdapter();
                    if ((layoutManager instanceof LinearLayoutManager) && adapter != null && ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1 && !((PhonePBXMessageSessionRecyclerView) recyclerView).loadMore() && CmmSIPMessageManager.getInstance().hasMoreOldSessionsToSync() && TextUtils.isEmpty(PhonePBXSmsFragment.this.mLoadMoreReqId)) {
                        PhonePBXSmsFragment.this.mLoadMoreReqId = CmmSIPMessageManager.getInstance().requestSyncMoreOldSessions(50);
                    }
                }
            }
        });
        this.mBtnKeyboard.setOnClickListener(this);
        this.mBtnNewChat.setOnClickListener(this);
        CmmSIPMessageManager.getInstance().addListener(this.mMessageEventSinkUIListener);
        return inflate;
    }

    public void updateEmptyView() {
        if (this.mPhonePBXMessageSessionRecyclerView.getCount() == 0) {
            this.mLayoutEmpty.setVisibility(0);
            this.mPhonePBXMessageSessionRecyclerView.setVisibility(8);
            return;
        }
        this.mLayoutEmpty.setVisibility(8);
        this.mPhonePBXMessageSessionRecyclerView.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void checkSelfDirectNumber() {
        this.mBtnNewChat.setVisibility(CollectionsUtil.isListEmpty(CmmSIPCallManager.getInstance().getDirectNumberList()) ? 8 : 0);
    }

    public void onClick(View view) {
        if (view == this.mBtnKeyboard) {
            onClickKeyboard();
        } else if (view == this.mBtnNewChat) {
            FragmentActivity activity = getActivity();
            if (activity instanceof ZMActivity) {
                PBXSMSActivity.showAsToNumbers((ZMActivity) activity, null);
            }
        }
    }

    private void onClickKeyboard() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhonePBXTabFragment) {
            ((PhonePBXTabFragment) parentFragment).openKeyboard();
        }
    }

    public void onDestroyView() {
        CmmSIPMessageManager.getInstance().removeListener(this.mMessageEventSinkUIListener);
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        checkSelfDirectNumber();
        updateEmptyView();
    }

    public void onStart() {
        super.onStart();
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
        ZMBuddySyncInstance.getInsatance().addListener(this.mZMBuddyListListener);
    }

    public void onStop() {
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
        ZMBuddySyncInstance.getInsatance().removeListener(this.mZMBuddyListListener);
        super.onStop();
    }

    public void onItemClick(View view, int i) {
        if (((ZMActivity) getActivity()) != null) {
            IPBXMessageSessionItem item = this.mPhonePBXMessageSessionRecyclerView.getItem(i);
            if (item != null && !TextUtils.isEmpty(item.getID())) {
                PBXSMSActivity.showAsSession((ZMActivity) getActivity(), item.getID());
            }
        }
    }

    public boolean onItemLongClick(View view, int i) {
        return itemLongClick(i);
    }

    /* access modifiers changed from: private */
    public void requestSyncNewSessions() {
        IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
        if (messageAPI != null) {
            messageAPI.requestSyncNewSessions();
        }
    }

    private void dismissContextMenuDialog() {
        ZMAlertDialog zMAlertDialog = this.mContextMenuDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mContextMenuDialog.dismiss();
            this.mContextMenuDialog = null;
        }
    }

    private boolean itemLongClick(int i) {
        if (CmmSIPCallManager.getInstance().isLoginConflict()) {
            return false;
        }
        dismissContextMenuDialog();
        final IPBXMessageSessionItem item = this.mPhonePBXMessageSessionRecyclerView.getItem(Math.max(0, i));
        if (item == null) {
            return false;
        }
        boolean hasDataNetwork = NetworkUtil.hasDataNetwork(getContext());
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
        ArrayList arrayList = new ArrayList();
        List others = item.getOthers();
        if (others != null && others.size() == 1 && PTApp.getInstance().hasMessenger() && ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(((PBXMessageContact) others.get(0)).getPhoneNumber()) == null) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_mi_create_new_contact), 8));
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_mi_add_to_existing_contact), 9));
        }
        if (hasDataNetwork) {
            arrayList.add(new PhonePBXContextMenuItem(getContext().getString(C4558R.string.zm_sip_sms_delete_session_117773), 1));
        }
        if (CollectionsUtil.isListEmpty(arrayList)) {
            return false;
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        this.mContextMenuDialog = new Builder(getContext()).setTitle((CharSequence) item.getDisplayName()).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PhonePBXContextMenuItem phonePBXContextMenuItem = (PhonePBXContextMenuItem) zMMenuAdapter.getItem(i);
                String str = null;
                PBXMessageContact pBXMessageContact = CollectionsUtil.isListEmpty(item.getOthers()) ? null : (PBXMessageContact) item.getOthers().get(0);
                if (pBXMessageContact != null) {
                    str = pBXMessageContact.getPhoneNumber();
                }
                int action = phonePBXContextMenuItem.getAction();
                if (action != 1) {
                    switch (action) {
                        case 8:
                            ZMPhoneUtils.addNumberToPhoneContact(PhonePBXSmsFragment.this.getContext(), str, false);
                            break;
                        case 9:
                            ZMPhoneUtils.addNumberToPhoneContact(PhonePBXSmsFragment.this.getContext(), str, true);
                            break;
                    }
                } else {
                    final String id = item.getID();
                    if (!TextUtils.isEmpty(id)) {
                        final FragmentActivity activity = PhonePBXSmsFragment.this.getActivity();
                        if (activity instanceof ZMActivity) {
                            DialogUtils.showAlertDialog((ZMActivity) activity, PhonePBXSmsFragment.this.getString(C4558R.string.zm_sip_title_delete_session_117773), PhonePBXSmsFragment.this.getString(C4558R.string.zm_sip_lbl_delete_session_117773), C4558R.string.zm_btn_delete, C4558R.string.zm_btn_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CmmSIPMessageManager instance = CmmSIPMessageManager.getInstance();
                                    if (instance.isLocalSession(id)) {
                                        instance.deleteLocalSession(id);
                                    } else if (TextUtils.isEmpty(instance.requestDeleteSession(id))) {
                                        DialogUtils.showAlertDialog((ZMActivity) activity, C4558R.string.zm_sip_delete_session_error_117773, C4558R.string.zm_sip_try_later_117773, C4558R.string.zm_btn_ok);
                                    } else {
                                        instance.requestMarkSessionAsRead(id);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }).create();
        this.mContextMenuDialog.setCanceledOnTouchOutside(true);
        this.mContextMenuDialog.show();
        return true;
    }
}
