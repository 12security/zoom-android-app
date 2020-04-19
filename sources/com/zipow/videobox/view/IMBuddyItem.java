package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMSession;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.MemCache;
import java.io.Serializable;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class IMBuddyItem implements Serializable {
    private static final long serialVersionUID = 1;
    @Nullable
    public String avatar;
    @Nullable
    public String email;
    public boolean isNoneFriend = false;
    public boolean isPending = false;
    public int presence;
    @Nullable
    public String screenName;
    @Nullable
    public String sortKey;
    public int unreadMessageCount = 0;
    public String userId;

    public IMBuddyItem() {
    }

    public IMBuddyItem(@NonNull BuddyItem buddyItem) {
        parseFromProtoItem(buddyItem, -1);
    }

    public IMBuddyItem(@NonNull BuddyItem buddyItem, int i) {
        parseFromProtoItem(buddyItem, i);
    }

    @NonNull
    public IMBuddyItem parseFromProtoItem(@NonNull BuddyItem buddyItem) {
        return parseFromProtoItem(buddyItem, -1);
    }

    @NonNull
    public IMBuddyItem parseFromProtoItem(@NonNull BuddyItem buddyItem, int i) {
        this.screenName = buddyItem.getScreenName();
        this.userId = buddyItem.getJid();
        this.presence = buddyItem.getPresence();
        this.avatar = buddyItem.getLocalPicturePath();
        this.isPending = buddyItem.getIsPending();
        this.isNoneFriend = buddyItem.getIsNoneFriend();
        this.sortKey = SortUtil.getSortKey(this.screenName, CompatUtils.getLocalDefault());
        if (StringUtil.isEmptyOrNull(this.avatar)) {
            this.avatar = buddyItem.getPicture();
        }
        if (i >= 0) {
            this.unreadMessageCount = i;
        } else if (VideoBoxApplication.getInstance().isPTApp()) {
            this.unreadMessageCount = getUnreadMessageCount(buddyItem.getJid());
        }
        return this;
    }

    private int getUnreadMessageCount(String str) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper == null) {
            return 0;
        }
        IMSession sessionBySessionName = iMHelper.getSessionBySessionName(str);
        if (sessionBySessionName == null) {
            return 0;
        }
        return sessionBySessionName.getUnreadMessageCount();
    }

    @Nullable
    public View getView(Context context, View view, MemCache<String, Bitmap> memCache, boolean z) {
        IMBuddyItemView iMBuddyItemView;
        if (view instanceof IMBuddyItemView) {
            iMBuddyItemView = (IMBuddyItemView) view;
        } else {
            iMBuddyItemView = new IMBuddyItemView(context);
        }
        bindView(iMBuddyItemView);
        return iMBuddyItemView;
    }

    private void bindView(IMBuddyItemView iMBuddyItemView) {
        iMBuddyItemView.setBuddyListItem(this);
    }
}
