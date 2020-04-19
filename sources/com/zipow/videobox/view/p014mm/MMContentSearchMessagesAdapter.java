package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchResponse;
import com.zipow.videobox.ptapp.IMProtos.MessageSearchResult;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MemCache;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.MMContentSearchMessagesAdapter */
public class MMContentSearchMessagesAdapter extends BaseAdapter {
    private MemCache<String, Drawable> mAvatarCache;
    private Context mContext;
    @NonNull
    private List<String> mLoadedNeedRrefreshJids = new ArrayList();
    @NonNull
    private List<MMContentMessageItem> mMessageList = new ArrayList();
    @Nullable
    private String myJid;

    public long getItemId(int i) {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    @NonNull
    public List<String> getmLoadedNeedRrefreshJids() {
        return this.mLoadedNeedRrefreshJids;
    }

    public void clearmLoadedNeedRrefreshJids() {
        if (!CollectionsUtil.isListEmpty(this.mLoadedNeedRrefreshJids)) {
            this.mLoadedNeedRrefreshJids.clear();
        }
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        if (!TextUtils.isEmpty(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                boolean z = false;
                for (int i = 0; i < this.mMessageList.size(); i++) {
                    MMContentMessageItem mMContentMessageItem = (MMContentMessageItem) this.mMessageList.get(i);
                    if (mMContentMessageItem != null && !TextUtils.isEmpty(mMContentMessageItem.getSenderJid()) && TextUtils.equals(mMContentMessageItem.getSenderJid(), str)) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(mMContentMessageItem.getSessionId());
                        if (sessionById != null) {
                            ZoomMessage messageById = sessionById.getMessageById(mMContentMessageItem.getMsgId());
                            if (messageById != null) {
                                MMContentMessageItem initWithZoomMessage = MMContentMessageItem.initWithZoomMessage(this.mContext, mMContentMessageItem, messageById);
                                if (initWithZoomMessage != null) {
                                    this.mMessageList.set(i, initWithZoomMessage);
                                    z = true;
                                }
                            }
                        }
                    }
                }
                if (z) {
                    notifyDataSetChanged();
                }
            }
        }
    }

    public MMContentSearchMessagesAdapter(Context context) {
        this.mContext = context;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                this.myJid = myself.getJid();
            }
        }
    }

    public void addSearchedFiles(@Nullable MessageContentSearchResponse messageContentSearchResponse) {
        if (messageContentSearchResponse != null && messageContentSearchResponse.getSearchResponseCount() != 0) {
            ArrayList arrayList = new ArrayList();
            for (MessageSearchResult initWithFileFilterSearchResult : messageContentSearchResponse.getSearchResponseList()) {
                MMContentMessageItem initWithFileFilterSearchResult2 = MMContentMessageItem.initWithFileFilterSearchResult(initWithFileFilterSearchResult, this.mContext);
                if (initWithFileFilterSearchResult2 != null && !initWithFileFilterSearchResult2.isBlockedByIBMsg()) {
                    arrayList.add(initWithFileFilterSearchResult2);
                }
            }
            mergeMessages(arrayList);
        }
    }

    private void mergeMessages(@Nullable List<MMContentMessageItem> list) {
        if (list != null && list.size() != 0) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            linkedHashSet.addAll(this.mMessageList);
            linkedHashSet.addAll(list);
            this.mMessageList = new ArrayList(linkedHashSet);
        }
    }

    public void addLocalSearchedFiles(@Nullable List<MessageSearchResult> list) {
        if (list != null && list.size() != 0) {
            for (MessageSearchResult initWithFileFilterSearchResult : list) {
                MMContentMessageItem initWithFileFilterSearchResult2 = MMContentMessageItem.initWithFileFilterSearchResult(initWithFileFilterSearchResult, this.mContext);
                if (initWithFileFilterSearchResult2 != null && !initWithFileFilterSearchResult2.isBlockedByIBMsg()) {
                    this.mMessageList.add(initWithFileFilterSearchResult2);
                }
            }
        }
    }

    public void setAvatarCache(MemCache<String, Drawable> memCache) {
        this.mAvatarCache = memCache;
    }

    public boolean isDataEmpty() {
        return this.mMessageList.isEmpty();
    }

    public void removeItem(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            int i = 0;
            while (true) {
                if (i >= this.mMessageList.size()) {
                    break;
                } else if (StringUtil.isSameString(((MMContentMessageItem) this.mMessageList.get(i)).getSessionId(), str)) {
                    this.mMessageList.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    public void clearAll() {
        this.mMessageList.clear();
    }

    public int getCount() {
        return this.mMessageList.size();
    }

    @Nullable
    public MMContentMessageItem getItem(int i) {
        if (i < 0 || i >= this.mMessageList.size()) {
            return null;
        }
        return (MMContentMessageItem) this.mMessageList.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        return createFileItemView(i, view, viewGroup);
    }

    @Nullable
    private View createFileItemView(int i, View view, ViewGroup viewGroup) {
        MMContentMessageItem item = getItem(i);
        if (!TextUtils.isEmpty(item.getSenderJid()) && TextUtils.isEmpty(item.getSenderName())) {
            this.mLoadedNeedRrefreshJids.remove(item.getSenderJid());
            this.mLoadedNeedRrefreshJids.add(item.getSenderJid());
        }
        return item.getView(this.mContext, i, view, viewGroup, this.myJid, this.mAvatarCache);
    }
}
