package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.eventbus.ZMStarEvent;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI.ICrawlerLinkPreviewUIListener;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI.SimpleCrawlerLinkPreviewUIListener;
import com.zipow.videobox.ptapp.IMProtos.CrawlLinkResponse;
import com.zipow.videobox.ptapp.IMProtos.StarredGuidInfo;
import com.zipow.videobox.ptapp.IMProtos.StarredGuidList;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.LinkPreviewHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAddonListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeMsgHref;
import com.zipow.videobox.view.p014mm.MMCommentsFragment;
import com.zipow.videobox.view.p014mm.MMContentMessageItem.MMContentMessageAnchorInfo;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class StarredMessageFragment extends ZMDialogFragment {
    private static final int ACTION_JUMP_TO = 2;
    private static final int ACTION_UNSTAR_MESSAGE = 1;
    public static final String ARG_SESSION_ID = "session";
    private static final int MESSAGE_STEP = 50;
    private static final int MESSAGE_TYPE_DISPLAY = 1;
    private static final int MESSAGE_TYPE_END = 2;
    /* access modifiers changed from: private */
    public ListView listView;
    /* access modifiers changed from: private */
    @Nullable
    public StarredMessageAdapter mAdapter;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    if (StarredMessageFragment.this.mAdapter != null) {
                        StarredMessageFragment.this.mAdapter.addOrUpdateItem((List) message.obj);
                        StarredMessageFragment.this.listView.setSelection(StarredMessageFragment.this.mAdapter.getCount() - 1);
                        break;
                    }
                    break;
                case 2:
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null && !StarredMessageFragment.this.syncList.isEmpty()) {
                        HashMap hashMap = new HashMap();
                        for (StarredMessageItem starredMessageItem : StarredMessageFragment.this.syncList) {
                            String access$300 = starredMessageItem.sessionID;
                            if (hashMap.containsKey(access$300)) {
                                ((List) hashMap.get(access$300)).add(Long.valueOf(starredMessageItem.svrTime));
                            } else {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(Long.valueOf(starredMessageItem.svrTime));
                                hashMap.put(access$300, arrayList);
                            }
                        }
                        zoomMessenger.starMessageSyncMessages(hashMap);
                        break;
                    } else {
                        return;
                    }
            }
        }
    };
    @Nullable
    private ICrawlerLinkPreviewUIListener mICrawlerLinkPreviewUIListener = new SimpleCrawlerLinkPreviewUIListener() {
        public void OnLinkCrawlResult(@Nullable CrawlLinkResponse crawlLinkResponse) {
            if (crawlLinkResponse != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(crawlLinkResponse.getSessionId());
                    if (sessionById != null) {
                        ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(crawlLinkResponse.getMsgGuid());
                        if (!(messageByXMPPGuid == null || StarredMessageFragment.this.mAdapter == null)) {
                            StarredMessageFragment.this.mAdapter.addOrUpdateItem(new StarredMessageItem(crawlLinkResponse.getSessionId(), messageByXMPPGuid.getMessageID()));
                        }
                    }
                }
            }
        }

        public void OnDownloadFavicon(int i, String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                StarredMessageItem starredMessageItem = (StarredMessageItem) StarredMessageFragment.this.mLinkPreviewReqIds.remove(str);
                if (i == 0 && StarredMessageFragment.this.mAdapter != null) {
                    StarredMessageFragment.this.mAdapter.addOrUpdateItem(starredMessageItem);
                }
            }
        }

        public void OnDownloadImage(int i, String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                StarredMessageItem starredMessageItem = (StarredMessageItem) StarredMessageFragment.this.mLinkPreviewReqIds.remove(str);
                if (i == 0 && StarredMessageFragment.this.mAdapter != null) {
                    StarredMessageFragment.this.mAdapter.addOrUpdateItem(starredMessageItem);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public HashMap<String, StarredMessageItem> mLinkPreviewReqIds = new HashMap<>();
    @NonNull
    private Map<String, Set<Long>> mStarMessags = new HashMap();
    @Nullable
    private IZoomMessengerUIListener messengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onConfirmPreviewPicFileDownloaded(String str, String str2, int i) {
            StarredMessageItem starredMessageItem = new StarredMessageItem(str, str2);
            if (starredMessageItem.messageItem != null && StarredMessageFragment.this.mAdapter != null) {
                starredMessageItem.messageItem.isPreviewDownloadFailed = i != 0;
                starredMessageItem.messageItem.fileDownloadResultCode = i;
                if (i == 0) {
                    StarredMessageFragment.this.mAdapter.addOrUpdateItem(new StarredMessageItem(str, str2));
                } else {
                    StarredMessageFragment.this.mAdapter.notifyDataSetChanged();
                }
            }
        }

        public void notify_StarMessageDataUpdate() {
            super.notify_StarMessageDataUpdate();
            StarredMessageFragment starredMessageFragment = StarredMessageFragment.this;
            starredMessageFragment.srcList = starredMessageFragment.buildSrcData();
            StarredMessageFragment.this.syncList = new ArrayList();
            StarredMessageFragment starredMessageFragment2 = StarredMessageFragment.this;
            starredMessageFragment2.getDisplayListAndsyncData(starredMessageFragment2.srcList, StarredMessageFragment.this.syncList, 50);
        }

        public void indicate_BuddyBlockedByIB(List<String> list) {
            if (!CollectionsUtil.isListEmpty(list) && StarredMessageFragment.this.mAdapter != null) {
                StarredMessageFragment.this.mAdapter.removeItem(list);
            }
        }

        public void notify_StarMessagesData(String str, int i, @NonNull byte[] bArr) {
            if (i == 0) {
                try {
                    StarredGuidList parseFrom = StarredGuidList.parseFrom(bArr);
                    if (parseFrom != null) {
                        ArrayList arrayList = new ArrayList();
                        for (int i2 = 0; i2 < parseFrom.getStarredGuidInfoCount(); i2++) {
                            StarredGuidInfo starredGuidInfo = parseFrom.getStarredGuidInfo(i2);
                            if (starredGuidInfo != null) {
                                for (int i3 = 0; i3 < starredGuidInfo.getValueCount(); i3++) {
                                    StarredMessageItem starredMessageItem = new StarredMessageItem(starredGuidInfo.getKey(), starredGuidInfo.getValue(i3));
                                    if (starredMessageItem.messageItem != null) {
                                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                                        if (zoomMessenger != null) {
                                            ZoomChatSession sessionById = zoomMessenger.getSessionById(starredMessageItem.sessionID);
                                            if (sessionById != null) {
                                                sessionById.checkAutoDownloadForMessage(starredMessageItem.messageItem.messageId);
                                            }
                                        }
                                        arrayList.add(starredMessageItem);
                                    }
                                }
                            }
                        }
                        StarredMessageFragment.this.mAdapter.addOrUpdateItem((List<StarredMessageItem>) arrayList);
                        StarredMessageFragment.this.listView.setSelection(StarredMessageFragment.this.mAdapter.getCount() - 1);
                    }
                } catch (Exception unused) {
                }
            }
        }
    };
    @Nullable
    private String sessionID;
    /* access modifiers changed from: private */
    @NonNull
    public List<StarredMessageItem> srcList = new ArrayList();
    /* access modifiers changed from: private */
    @NonNull
    public List<StarredMessageItem> syncList = new ArrayList();

    class DistinguishRunnable implements Runnable {
        private int index;
        private List<StarredMessageItem> list;
        private int step;
        private List<StarredMessageItem> syncList;

        public DistinguishRunnable(List<StarredMessageItem> list2, List<StarredMessageItem> list3, int i, int i2) {
            this.list = list2;
            this.syncList = list3;
            this.index = i;
            this.step = i2;
        }

        public void run() {
            Handler access$1200 = StarredMessageFragment.this.mHandler;
            StarredMessageFragment starredMessageFragment = StarredMessageFragment.this;
            List<StarredMessageItem> list2 = this.list;
            List<StarredMessageItem> list3 = this.syncList;
            int i = this.index;
            access$1200.obtainMessage(1, starredMessageFragment.getDisplayListAndsyncData(list2, list3, i, this.step + i)).sendToTarget();
            List<StarredMessageItem> list4 = this.list;
            if (list4 != null && !list4.isEmpty() && this.index + this.step >= this.list.size()) {
                StarredMessageFragment.this.mHandler.obtainMessage(2).sendToTarget();
            }
        }
    }

    class StarredMessageAdapter extends BaseAdapter {
        @NonNull
        List<StarredMessageItem> list = new ArrayList();
        private Context mContext;

        public long getItemId(int i) {
            return (long) i;
        }

        public int getViewTypeCount() {
            return 56;
        }

        public StarredMessageAdapter(Context context) {
            this.mContext = context;
        }

        public void addOrUpdateItem(@Nullable List<StarredMessageItem> list2) {
            if (list2 != null && !list2.isEmpty()) {
                for (StarredMessageItem addOrUpdateItem : list2) {
                    addOrUpdateItem(addOrUpdateItem);
                }
            }
        }

        public void addOrUpdateItem(@Nullable StarredMessageItem starredMessageItem) {
            if (starredMessageItem != null && StarredMessageFragment.this.isStarMsg(starredMessageItem) && !starredMessageItem.isBlockedByIB()) {
                int indexOf = this.list.indexOf(starredMessageItem);
                if (indexOf >= 0) {
                    this.list.set(indexOf, starredMessageItem);
                } else {
                    this.list.add(starredMessageItem);
                }
                if (this.list.size() > 1) {
                    Collections.sort(this.list, new Comparator<StarredMessageItem>() {
                        public int compare(@NonNull StarredMessageItem starredMessageItem, @NonNull StarredMessageItem starredMessageItem2) {
                            if (starredMessageItem2.svrTime == starredMessageItem.svrTime) {
                                return 0;
                            }
                            return starredMessageItem.svrTime > starredMessageItem2.svrTime ? 1 : -1;
                        }
                    });
                }
                List<String> downloadLinkPreview = LinkPreviewHelper.downloadLinkPreview(starredMessageItem.messageItem);
                if (!CollectionsUtil.isListEmpty(downloadLinkPreview)) {
                    for (String put : downloadLinkPreview) {
                        StarredMessageFragment.this.mLinkPreviewReqIds.put(put, starredMessageItem);
                    }
                }
                notifyDataSetChanged();
            }
        }

        public void removeItem(@Nullable StarredMessageItem starredMessageItem) {
            if (starredMessageItem != null) {
                this.list.remove(starredMessageItem);
                notifyDataSetChanged();
            }
        }

        public void removeItem(String str, long j) {
            if (str != null && j != 0) {
                boolean z = false;
                Iterator it = this.list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    StarredMessageItem starredMessageItem = (StarredMessageItem) it.next();
                    if (j == starredMessageItem.svrTime && TextUtils.equals(starredMessageItem.sessionID, str)) {
                        it.remove();
                        z = true;
                        break;
                    }
                }
                if (z) {
                    notifyDataSetChanged();
                }
            }
        }

        public void removeItem(@Nullable List<String> list2) {
            boolean z;
            if (!CollectionsUtil.isListEmpty(list2)) {
                Iterator it = this.list.iterator();
                while (true) {
                    z = true;
                    if (!it.hasNext()) {
                        z = false;
                        break;
                    }
                    StarredMessageItem starredMessageItem = (StarredMessageItem) it.next();
                    if (starredMessageItem.messageItem != null) {
                        if (list2.contains(starredMessageItem.messageItem.fromJid)) {
                            it.remove();
                            break;
                        }
                    } else if (list2.contains(starredMessageItem.sessionID)) {
                        it.remove();
                        break;
                    }
                }
                if (z) {
                    notifyDataSetChanged();
                }
            }
        }

        public void clear() {
            this.list.clear();
            notifyDataSetChanged();
        }

        public void notifyDataSetChanged() {
            ArrayList arrayList = new ArrayList();
            for (StarredMessageItem starredMessageItem : this.list) {
                if (StarredMessageFragment.this.isStarMsg(starredMessageItem)) {
                    arrayList.add(starredMessageItem);
                }
            }
            this.list.clear();
            this.list.addAll(arrayList);
            super.notifyDataSetChanged();
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int i) {
            return this.list.get(i);
        }

        public int getItemViewType(int i) {
            StarredMessageItem starredMessageItem = (StarredMessageItem) getItem(i);
            if (starredMessageItem.messageItem == null) {
                return 0;
            }
            return starredMessageItem.messageItem.messageType;
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            AbsMessageView starredMessageView = MMMessageItem.getStarredMessageView(this.mContext, getItemViewType(i), view);
            if (starredMessageView == null) {
                return new View(this.mContext);
            }
            final StarredMessageItem starredMessageItem = (StarredMessageItem) getItem(i);
            starredMessageView.setMessageItem(starredMessageItem.messageItem);
            starredMessageView.setOnClickMessageListener(new OnClickMessageListener() {
                public void onClickMessage(MMMessageItem mMMessageItem) {
                    StarredMessageFragment.this.showSelectContextDialog(starredMessageItem);
                }
            });
            starredMessageView.setOnClickAvatarListener(new OnClickAvatarListener() {
                public void onClickAvatar(MMMessageItem mMMessageItem) {
                    StarredMessageFragment.this.showSelectContextDialog(starredMessageItem);
                }
            });
            starredMessageView.setOnClickAddonListener(new OnClickAddonListener() {
                public void onClickAddon(NodeMsgHref nodeMsgHref) {
                    StarredMessageFragment.this.showSelectContextDialog(starredMessageItem);
                }
            });
            starredMessageView.setOnClickStatusImageListener(new OnClickStatusImageListener() {
                public void onClickStatusImage(@NonNull MMMessageItem mMMessageItem) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                        if (sessionById != null && mMMessageItem.messageType == 4) {
                            sessionById.checkAutoDownloadForMessage(mMMessageItem.messageId);
                            mMMessageItem.isPreviewDownloadFailed = false;
                            StarredMessageAdapter.this.notifyDataSetChanged();
                        }
                    }
                }
            });
            return starredMessageView;
        }
    }

    class StarredMessageItem {
        @Nullable
        MMMessageItem messageItem;
        /* access modifiers changed from: private */
        public String sessionID;
        /* access modifiers changed from: private */
        public long svrTime;

        public StarredMessageItem(String str, long j) {
            this.sessionID = str;
            this.svrTime = j;
        }

        public StarredMessageItem(String str, String str2) {
            this.sessionID = str;
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        ZoomMessage messageById = sessionById.getMessageById(str2);
                        if (messageById != null) {
                            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                            if (zoomFileContentMgr != null) {
                                boolean isSameString = StringUtil.isSameString(messageById.getSenderID(), myself.getJid());
                                this.messageItem = MMMessageItem.initWithZoomMessage(messageById, str, zoomMessenger, sessionById.isGroup(), isSameString, StarredMessageFragment.this.getActivity(), IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), zoomFileContentMgr, true);
                                MMMessageItem mMMessageItem = this.messageItem;
                                if (mMMessageItem != null) {
                                    this.svrTime = mMMessageItem.serverSideTime;
                                }
                            }
                        }
                    }
                }
            }
        }

        @Nullable
        public MMMessageItem updateMessageItem(@Nullable ZoomMessenger zoomMessenger, @NonNull ZoomMessage zoomMessage) {
            if (zoomMessenger == null) {
                return null;
            }
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.sessionID);
            if (sessionById == null) {
                return null;
            }
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself == null) {
                return null;
            }
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr == null) {
                return null;
            }
            ZoomMessage zoomMessage2 = zoomMessage;
            ZoomMessenger zoomMessenger2 = zoomMessenger;
            this.messageItem = MMMessageItem.initWithZoomMessage(zoomMessage2, this.sessionID, zoomMessenger2, sessionById.isGroup(), StringUtil.isSameString(zoomMessage.getSenderID(), myself.getJid()), StarredMessageFragment.this.getActivity(), IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), zoomFileContentMgr, true);
            return this.messageItem;
        }

        public boolean isBlockedByIB() {
            String str;
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return false;
            }
            MMMessageItem mMMessageItem = this.messageItem;
            if (mMMessageItem != null) {
                str = mMMessageItem.fromJid;
            } else {
                str = this.sessionID;
            }
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                return buddyWithJID.isIMBlockedByIB();
            }
            return false;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof StarredMessageItem)) {
                return false;
            }
            if (((StarredMessageItem) obj).svrTime == this.svrTime) {
                z = true;
            }
            return z;
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_starred_message, viewGroup, false);
        this.listView = (ListView) inflate.findViewById(C4558R.C4560id.zm_fragment_starred_message_listView);
        ZoomMessengerUI.getInstance().addListener(this.messengerUIListener);
        CrawlerLinkPreviewUI.getInstance().addListener(this.mICrawlerLinkPreviewUIListener);
        EventBus.getDefault().register(this);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.sessionID = arguments.getString("session");
        }
        Context context = getContext();
        if (context != null) {
            this.srcList = buildSrcData();
            this.syncList = new ArrayList();
            this.mAdapter = new StarredMessageAdapter(context);
            this.listView.setAdapter(this.mAdapter);
            this.listView.setEmptyView(getView().findViewById(C4558R.C4560id.zm_fragment_starred_message_emptyView));
            getDisplayListAndsyncData(this.srcList, this.syncList, 50);
            this.listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(@NonNull AdapterView<?> adapterView, View view, int i, long j) {
                    StarredMessageFragment.this.showSelectContextDialog((StarredMessageItem) adapterView.getAdapter().getItem(i));
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZMStarEvent zMStarEvent) {
        if (isAdded() && zMStarEvent != null && zMStarEvent.msgSvr != 0 && zMStarEvent.sessionId != null && this.mAdapter != null) {
            Set set = (Set) this.mStarMessags.get(zMStarEvent.sessionId);
            if (zMStarEvent.isStar) {
                if (set == null) {
                    set = new HashSet();
                    this.mStarMessags.put(zMStarEvent.sessionId, set);
                }
                set.add(Long.valueOf(zMStarEvent.msgSvr));
                StarredMessageItem starredMessageItem = new StarredMessageItem(zMStarEvent.sessionId, zMStarEvent.msgSvr);
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(starredMessageItem.sessionID);
                    if (sessionById != null) {
                        ZoomMessage messageByServerTime = sessionById.getMessageByServerTime(zMStarEvent.msgSvr, true);
                        if (messageByServerTime != null) {
                            starredMessageItem.updateMessageItem(zoomMessenger, messageByServerTime);
                            this.mAdapter.addOrUpdateItem(starredMessageItem);
                        }
                    }
                }
            } else {
                if (set != null) {
                    set.remove(Long.valueOf(zMStarEvent.msgSvr));
                }
                this.mAdapter.removeItem(zMStarEvent.sessionId, zMStarEvent.msgSvr);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showSelectContextDialog(final StarredMessageItem starredMessageItem) {
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZMSimpleMenuItem(2, getString(C4558R.string.zm_mm_starred_message_jump_to_chat_owp40)));
        arrayList.add(new ZMSimpleMenuItem(1, getString(C4558R.string.zm_mm_unstar_message_65147)));
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        ZMAlertDialog create = new Builder(getActivity()).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                StarredMessageFragment.this.onSelectContextMenuItem((ZMSimpleMenuItem) zMMenuAdapter.getItem(i), starredMessageItem);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    public boolean isStarMsg(@Nullable StarredMessageItem starredMessageItem) {
        if (starredMessageItem == null) {
            return false;
        }
        Set set = (Set) this.mStarMessags.get(starredMessageItem.sessionID);
        if (set == null) {
            return false;
        }
        return set.contains(Long.valueOf(starredMessageItem.svrTime));
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(@Nullable ZMSimpleMenuItem zMSimpleMenuItem, @Nullable StarredMessageItem starredMessageItem) {
        if (zMSimpleMenuItem != null && starredMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    if (zMSimpleMenuItem.getAction() == 2) {
                        if (starredMessageItem.messageItem != null) {
                            MMContentMessageAnchorInfo mMContentMessageAnchorInfo = new MMContentMessageAnchorInfo();
                            mMContentMessageAnchorInfo.setMsgGuid(starredMessageItem.messageItem.messageXMPPId);
                            mMContentMessageAnchorInfo.setSendTime(starredMessageItem.messageItem.serverSideTime);
                            if (starredMessageItem.messageItem.isGroupMessage) {
                                mMContentMessageAnchorInfo.setSessionId(starredMessageItem.sessionID);
                            } else if (!StringUtil.isSameString(myself.getJid(), starredMessageItem.sessionID)) {
                                mMContentMessageAnchorInfo.setSessionId(starredMessageItem.sessionID);
                            } else if (!StringUtil.isSameString(myself.getJid(), starredMessageItem.messageItem.fromJid)) {
                                mMContentMessageAnchorInfo.setSessionId(starredMessageItem.sessionID);
                            } else if (UIMgr.isMyNotes(starredMessageItem.sessionID)) {
                                mMContentMessageAnchorInfo.setSessionId(starredMessageItem.sessionID);
                            } else {
                                return;
                            }
                            if (starredMessageItem.messageItem.isComment) {
                                mMContentMessageAnchorInfo.setComment(true);
                                mMContentMessageAnchorInfo.setThrId(starredMessageItem.messageItem.threadId);
                                mMContentMessageAnchorInfo.setThrSvr(starredMessageItem.messageItem.threadTime);
                                MMCommentsFragment.showMsgContextInActivity(this, mMContentMessageAnchorInfo);
                            } else {
                                MMThreadsFragment.showMsgContextInActivity(this, mMContentMessageAnchorInfo);
                            }
                        }
                    } else if (zMSimpleMenuItem.getAction() == 1) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(starredMessageItem.sessionID);
                        if (sessionById != null && sessionById.discardStarMessage(starredMessageItem.svrTime)) {
                            StarredMessageAdapter starredMessageAdapter = this.mAdapter;
                            if (starredMessageAdapter != null) {
                                starredMessageAdapter.removeItem(starredMessageItem);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    @NonNull
    public List<StarredMessageItem> buildSrcData() {
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (TextUtils.isEmpty(this.sessionID)) {
                Map starMessageGetAll = zoomMessenger.starMessageGetAll();
                this.mStarMessags.clear();
                if (starMessageGetAll != null) {
                    for (Entry entry : starMessageGetAll.entrySet()) {
                        String str = (String) entry.getKey();
                        List<Long> list = (List) entry.getValue();
                        if (list != null) {
                            for (Long longValue : list) {
                                arrayList.add(new StarredMessageItem(str, longValue.longValue()));
                            }
                            this.mStarMessags.put(str, new HashSet(list));
                        }
                    }
                }
            } else {
                List<String> allStarredMessages = zoomMessenger.getAllStarredMessages(this.sessionID);
                this.mStarMessags.clear();
                HashSet hashSet = new HashSet();
                this.mStarMessags.put(this.sessionID, hashSet);
                if (allStarredMessages != null && !allStarredMessages.isEmpty()) {
                    for (String parseLong : allStarredMessages) {
                        try {
                            long parseLong2 = Long.parseLong(parseLong);
                            hashSet.add(Long.valueOf(parseLong2));
                            arrayList.add(new StarredMessageItem(this.sessionID, parseLong2));
                        } catch (Exception unused) {
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void getDisplayListAndsyncData(List<StarredMessageItem> list, List<StarredMessageItem> list2, int i) {
        int size = list.size();
        int i2 = 0;
        while (i2 < size) {
            Handler handler = this.mHandler;
            DistinguishRunnable distinguishRunnable = new DistinguishRunnable(list, list2, i2, i);
            handler.post(distinguishRunnable);
            i2 += i;
        }
    }

    /* access modifiers changed from: private */
    @Nullable
    public List<StarredMessageItem> getDisplayListAndsyncData(@Nullable List<StarredMessageItem> list, @Nullable List<StarredMessageItem> list2, int i, int i2) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return arrayList;
        }
        int size = list.size();
        if (i >= size) {
            return arrayList;
        }
        if (i2 > size) {
            i2 = size;
        }
        while (i < i2) {
            StarredMessageItem starredMessageItem = (StarredMessageItem) list.get(i);
            ZoomChatSession sessionById = zoomMessenger.getSessionById(starredMessageItem.sessionID);
            if (sessionById != null) {
                ZoomMessage messageByServerTime = sessionById.getMessageByServerTime(starredMessageItem.svrTime, true);
                if (messageByServerTime != null) {
                    sessionById.checkAutoDownloadForMessage(messageByServerTime.getMessageID());
                    if (starredMessageItem.updateMessageItem(zoomMessenger, messageByServerTime) != null) {
                        arrayList.add(starredMessageItem);
                    }
                } else if (list2 != null) {
                    list2.add(starredMessageItem);
                }
            }
            i++;
        }
        return arrayList;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ZoomMessengerUI.getInstance().removeListener(this.messengerUIListener);
        CrawlerLinkPreviewUI.getInstance().removeListener(this.mICrawlerLinkPreviewUIListener);
        EventBus.getDefault().unregister(this);
    }
}
