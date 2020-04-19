package com.zipow.videobox.view.p014mm;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.ptapp.IMProtos.EmojiComment;
import com.zipow.videobox.ptapp.IMProtos.EmojiDetailInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.ThreadDataUI;
import com.zipow.videobox.ptapp.ThreadDataUI.IThreadDataUIListener;
import com.zipow.videobox.ptapp.ThreadDataUI.SimpleThreadDataUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDetailListFragment */
public class ReactionEmojiDetailListFragment extends ZMDialogFragment implements OnRecyclerViewListener {
    public static final String ARG_EMOJI = "arg_emoji";
    public static final String ARG_MSG_ID = "arg_msg_id";
    public static final String ARG_SESSION_ID = "arg_session_id";
    private boolean isLoaded = false;
    private ReactionEmojiDetailListAdapter listViewAdapter;
    private ProgressBar loadingProgress;
    private String mEmoji;
    private String mMsgID;
    private String mSessionID;
    private IThreadDataUIListener mThreadDataUIListener = new SimpleThreadDataUIListener() {
        public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
            ReactionEmojiDetailListFragment.this.OnFetchEmojiDetailInfo(str, str2, str3, str4, z);
        }
    };
    private String mXmsReqID;
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_BuddyPresenceChanged(String str) {
            ReactionEmojiDetailListFragment.this.Indicate_BuddyPresenceChanged(str);
        }

        public void On_MyPresenceChanged(int i, int i2) {
            ReactionEmojiDetailListFragment.this.On_MyPresenceChanged(i, i2);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            ReactionEmojiDetailListFragment.this.onIndicateInfoUpdatedWithJID(str);
        }
    };
    private RecyclerView recyclerView;

    public boolean onItemLongClick(View view, int i) {
        return false;
    }

    public static ReactionEmojiDetailListFragment newInstance(String str, String str2, String str3) {
        ReactionEmojiDetailListFragment reactionEmojiDetailListFragment = new ReactionEmojiDetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SESSION_ID, str);
        bundle.putString(ARG_MSG_ID, str2);
        bundle.putString(ARG_EMOJI, str3);
        reactionEmojiDetailListFragment.setArguments(bundle);
        return reactionEmojiDetailListFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSessionID = arguments.getString(ARG_SESSION_ID);
            this.mMsgID = arguments.getString(ARG_MSG_ID);
            this.mEmoji = arguments.getString(ARG_EMOJI);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_reaction_emoji_detail_list_fragment, viewGroup, false);
        this.listViewAdapter = new ReactionEmojiDetailListAdapter(getActivity());
        this.listViewAdapter.setOnRecyclerViewListener(this);
        this.recyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.list_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.listViewAdapter);
        this.loadingProgress = (ProgressBar) inflate.findViewById(C4558R.C4560id.loading_progress);
        ThreadDataUI.getInstance().addListener(this.mThreadDataUIListener);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ThreadDataUI.getInstance().removeListener(this.mThreadDataUIListener);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onResume() {
        super.onResume();
        loadData();
    }

    /* access modifiers changed from: private */
    public void Indicate_BuddyPresenceChanged(String str) {
        this.listViewAdapter.updateBuddy(str);
    }

    /* access modifiers changed from: private */
    public void On_MyPresenceChanged(int i, int i2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                this.listViewAdapter.updateBuddy(myself.getJid());
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        this.listViewAdapter.updateBuddy(str);
    }

    public void loadData() {
        if (!this.isLoaded) {
            loadData(false);
        }
    }

    private void loadData(boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                EmojiDetailInfo messageEmojiDetailInfo = threadDataProvider.getMessageEmojiDetailInfo(z, this.mSessionID, this.mMsgID, this.mEmoji);
                if (messageEmojiDetailInfo != null) {
                    ArrayList arrayList = new ArrayList();
                    this.mXmsReqID = messageEmojiDetailInfo.getXmsReqId();
                    if (StringUtil.isEmptyOrNull(this.mXmsReqID)) {
                        this.isLoaded = true;
                        finishLoading();
                    }
                    if (messageEmojiDetailInfo.getCommentsCount() != 0) {
                        finishLoading();
                        final HashMap hashMap = new HashMap();
                        for (EmojiComment emojiComment : messageEmojiDetailInfo.getCommentsList()) {
                            String jid = emojiComment.getJid();
                            IMAddrBookItem buddyByJid = ZMBuddySyncInstance.getInsatance().getBuddyByJid(jid, true);
                            if (buddyByJid != null) {
                                arrayList.add(buddyByJid);
                                hashMap.put(jid, Long.valueOf(emojiComment.getCommentT()));
                            }
                        }
                        Collections.sort(arrayList, new Comparator<IMAddrBookItem>() {
                            public int compare(IMAddrBookItem iMAddrBookItem, IMAddrBookItem iMAddrBookItem2) {
                                long j;
                                Long l = (Long) hashMap.get(iMAddrBookItem.getJid());
                                Long l2 = (Long) hashMap.get(iMAddrBookItem2.getJid());
                                long j2 = 0;
                                if (l == null) {
                                    j = 0;
                                } else {
                                    j = l.longValue();
                                }
                                if (l2 != null) {
                                    j2 = l2.longValue();
                                }
                                return Long.compare(j, j2);
                            }
                        });
                        this.listViewAdapter.setData(arrayList);
                    }
                }
            }
        }
    }

    public void onItemClick(View view, int i) {
        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.listViewAdapter.getItem(i);
        if (iMAddrBookItem != null && !iMAddrBookItem.isPending()) {
            showUserActions(iMAddrBookItem);
        }
    }

    /* access modifiers changed from: private */
    public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
        if (StringUtil.isSameString(str, this.mXmsReqID) && StringUtil.isSameString(str2, this.mSessionID) && StringUtil.isSameString(str3, this.mMsgID) && StringUtil.isSameString(str4, this.mEmoji)) {
            finishLoading();
            if (z) {
                this.isLoaded = true;
                loadData(true);
            }
        }
    }

    public void showUserActions(IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if ((myself != null && TextUtils.equals(myself.getJid(), iMAddrBookItem.getJid())) || iMAddrBookItem.getAccountStatus() == 2) {
                        return;
                    }
                    if (iMAddrBookItem.getIsRobot()) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(iMAddrBookItem.getJid());
                        if (buddyWithJID != null) {
                            MMChatActivity.showAsOneToOneChat(zMActivity, buddyWithJID);
                        }
                        return;
                    }
                    AddrBookItemDetailsActivity.show(zMActivity, iMAddrBookItem, 106);
                }
            }
        }
    }

    private void finishLoading() {
        ProgressBar progressBar = this.loadingProgress;
        if (progressBar != null) {
            progressBar.setVisibility(8);
        }
    }
}
