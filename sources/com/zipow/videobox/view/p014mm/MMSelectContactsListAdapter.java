package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.MemCache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListAdapter */
public class MMSelectContactsListAdapter extends QuickSearchListDataAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_ALL_COUNT = 3;
    private static final int ITEM_TYPE_EVERYONE = 1;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_SLASH_COMMAND = 2;
    private MemCache<String, Bitmap> mAvatarCache;
    private int mChoiceMode = 0;
    private boolean mContainsEveryOne = false;
    @Nullable
    private Context mContext;
    private String mFilter;
    private boolean mIsAlterHost = false;
    private boolean mIsShowEmail = false;
    private boolean mIsSlashCommand = false;
    @NonNull
    private HashMap<String, MMSelectContactsListItem> mItemMap = new HashMap<>();
    @NonNull
    private List<MMSelectContactsListItem> mItems = new ArrayList();
    private boolean mLazyLoadAvatarDisabled = false;
    @NonNull
    private List<String> mLoadedContactJids = new ArrayList();
    /* access modifiers changed from: private */
    public MMSelectContactsListView mSelectContactsListView;

    public int getViewTypeCount() {
        return 3;
    }

    public MMSelectContactsListAdapter(@Nullable Context context, MMSelectContactsListView mMSelectContactsListView) {
        this.mContext = context;
        this.mSelectContactsListView = mMSelectContactsListView;
    }

    public void setAvatarMemCache(MemCache<String, Bitmap> memCache) {
        this.mAvatarCache = memCache;
    }

    public void clear() {
        this.mItems.clear();
        this.mItemMap.clear();
    }

    public void setChoiceMode(int i) {
        this.mChoiceMode = i;
    }

    public void addItem(@Nullable MMSelectContactsListItem mMSelectContactsListItem) {
        if (mMSelectContactsListItem != null) {
            this.mItems.add(mMSelectContactsListItem);
            this.mItemMap.put(mMSelectContactsListItem.screenName, mMSelectContactsListItem);
        }
    }

    public void setHasEveryone(boolean z) {
        this.mContainsEveryOne = z;
    }

    public void setmIsSlashCommand(boolean z) {
        this.mIsSlashCommand = z;
    }

    @NonNull
    public List<String> getmLoadedContactJids() {
        return this.mLoadedContactJids;
    }

    public void clearmLoadedContactJids() {
        if (!CollectionsUtil.isListEmpty(this.mLoadedContactJids)) {
            this.mLoadedContactJids.clear();
        }
    }

    public void updateItem(@Nullable MMSelectContactsListItem mMSelectContactsListItem) {
        int findItem = findItem(mMSelectContactsListItem.itemId);
        if (findItem >= 0) {
            this.mItems.set(findItem, mMSelectContactsListItem);
        } else {
            this.mItems.add(mMSelectContactsListItem);
        }
        this.mItemMap.put(mMSelectContactsListItem.screenName, mMSelectContactsListItem);
    }

    public int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (str.equals(((MMSelectContactsListItem) this.mItems.get(i)).itemId)) {
                return i;
            }
        }
        return -1;
    }

    public int findItemByEmail(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (StringUtil.isSameStringForNotAllowNull(str, ((MMSelectContactsListItem) this.mItems.get(i)).getEmail())) {
                return i;
            }
        }
        return -1;
    }

    public void removeItem(String str) {
        int findItem = findItem(str);
        if (findItem >= 0) {
            removeItemAt(findItem);
        }
    }

    public void removeItemByEmail(String str) {
        int findItemByEmail = findItemByEmail(str);
        if (findItemByEmail >= 0) {
            removeItemAt(findItemByEmail);
        }
    }

    public void removeItemAt(int i) {
        if (i >= 0 && i < this.mItems.size()) {
            MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) this.mItems.get(i);
            if (mMSelectContactsListItem != null) {
                MMSelectContactsListItem mMSelectContactsListItem2 = (MMSelectContactsListItem) this.mItemMap.get(mMSelectContactsListItem.screenName);
                if (mMSelectContactsListItem2 != null && StringUtil.isSameString(mMSelectContactsListItem.buddyJid, mMSelectContactsListItem2.getBuddyJid())) {
                    this.mItemMap.remove(mMSelectContactsListItem.screenName);
                }
            }
            this.mItems.remove(i);
        }
    }

    @Nullable
    public MMSelectContactsListItem findFirstItemWithScreenName(@Nullable String str, int i) {
        if (str == null || i < 0 || !this.mItemMap.containsKey(str)) {
            return null;
        }
        return (MMSelectContactsListItem) this.mItemMap.get(str);
    }

    public int getCount() {
        int size = this.mItems.size();
        return (!this.mContainsEveryOne || !StringUtil.isEmptyOrNull(this.mFilter)) ? size : size + 1;
    }

    @Nullable
    public Object getItem(int i) {
        if (this.mContainsEveryOne && StringUtil.isEmptyOrNull(this.mFilter)) {
            i--;
        }
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return this.mItems.get(i);
    }

    @Nullable
    public MMSelectContactsListItem getItemById(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        for (MMSelectContactsListItem mMSelectContactsListItem : this.mItems) {
            if (str.equals(mMSelectContactsListItem.itemId)) {
                return mMSelectContactsListItem;
            }
        }
        return null;
    }

    @Nullable
    public MMSelectContactsListItem getItemByEmail(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        for (MMSelectContactsListItem mMSelectContactsListItem : this.mItems) {
            if (StringUtil.isSameStringForNotAllowNull(str, mMSelectContactsListItem.getEmail())) {
                return mMSelectContactsListItem;
            }
        }
        return null;
    }

    public long getItemId(int i) {
        return (long) ((MMSelectContactsListItem) getItem(i)).itemId.hashCode();
    }

    public int getItemViewType(int i) {
        if (this.mIsSlashCommand) {
            return 2;
        }
        return (i != 0 || !this.mContainsEveryOne || !StringUtil.isEmptyOrNull(this.mFilter)) ? 0 : 1;
    }

    public void filter(@Nullable String str) {
        this.mFilter = str;
        if (!StringUtil.isEmptyOrNull(str)) {
            Locale localDefault = CompatUtils.getLocalDefault();
            for (int size = this.mItems.size() - 1; size >= 0; size--) {
                MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) this.mItems.get(size);
                boolean z = false;
                boolean z2 = mMSelectContactsListItem.screenName != null && mMSelectContactsListItem.screenName.toLowerCase(localDefault).contains(str);
                if (mMSelectContactsListItem.email != null && mMSelectContactsListItem.email.toLowerCase(localDefault).contains(str)) {
                    z = true;
                }
                if (!z2 && !z) {
                    MMSelectContactsListItem mMSelectContactsListItem2 = (MMSelectContactsListItem) this.mItems.get(size);
                    if (mMSelectContactsListItem2 != null) {
                        MMSelectContactsListItem mMSelectContactsListItem3 = (MMSelectContactsListItem) this.mItemMap.get(mMSelectContactsListItem2.screenName);
                        if (mMSelectContactsListItem3 != null && StringUtil.isSameString(mMSelectContactsListItem2.buddyJid, mMSelectContactsListItem3.getBuddyJid())) {
                            this.mItemMap.remove(mMSelectContactsListItem2.screenName);
                        }
                    }
                    this.mItems.remove(size);
                }
            }
        }
    }

    public void setLazyLoadAvatarDisabled(boolean z) {
        this.mLazyLoadAvatarDisabled = z;
    }

    public void setmIsShowEmail(boolean z) {
        this.mIsShowEmail = z;
    }

    public void setmIsAlterHost(boolean z) {
        this.mIsAlterHost = z;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = null;
        if (i < 0 || i >= getCount()) {
            return null;
        }
        switch (getItemViewType(i)) {
            case 0:
                view2 = createNormalItemView(i, view, viewGroup, this.mLazyLoadAvatarDisabled, this.mIsShowEmail, this.mIsAlterHost);
                break;
            case 1:
                view2 = createEveryoneItemView(i, view, viewGroup);
                break;
            case 2:
                view2 = createSlashCommandItemView(i, view, viewGroup, this.mLazyLoadAvatarDisabled);
                break;
        }
        return view2;
    }

    @NonNull
    private View createEveryoneItemView(int i, @Nullable View view, ViewGroup viewGroup) {
        MMSelectContactsListItemView mMSelectContactsListItemView;
        if (view == null) {
            mMSelectContactsListItemView = new MMSelectContactsListItemView(this.mContext);
            mMSelectContactsListItemView.setHidePresencePanel(true);
            mMSelectContactsListItemView.setCheckVisible(false);
            mMSelectContactsListItemView.setContactsDesc(this.mContext.getString(C4558R.string.zm_lbl_notify_everyone_59554));
        } else {
            mMSelectContactsListItemView = (MMSelectContactsListItemView) view;
        }
        mMSelectContactsListItemView.setNotes(null, this.mIsAlterHost);
        mMSelectContactsListItemView.setScreenName(this.mContext.getString(C4558R.string.zm_lbl_select_everyone));
        mMSelectContactsListItemView.setAvatar(C4558R.C4559drawable.zm_ic_avatar_group);
        mMSelectContactsListItemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MMSelectContactsListAdapter.this.mSelectContactsListView != null) {
                    MMSelectContactsListAdapter.this.mSelectContactsListView.onClickEveryone();
                }
            }
        });
        return mMSelectContactsListItemView;
    }

    private View createSlashCommandItemView(int i, View view, ViewGroup viewGroup, boolean z) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) getItem(i);
        this.mLoadedContactJids.remove(mMSelectContactsListItem.getBuddyJid());
        this.mLoadedContactJids.add(mMSelectContactsListItem.getBuddyJid());
        return mMSelectContactsListItem.getView(this.mContext, view, this.mChoiceMode == 0, this.mChoiceMode == 1, this.mAvatarCache, z, true, false);
    }

    private View createNormalItemView(int i, View view, ViewGroup viewGroup, boolean z, boolean z2, boolean z3) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) getItem(i);
        if (mMSelectContactsListItem == null) {
            return null;
        }
        this.mLoadedContactJids.remove(mMSelectContactsListItem.getBuddyJid());
        this.mLoadedContactJids.add(mMSelectContactsListItem.getBuddyJid());
        return mMSelectContactsListItem.getView(this.mContext, view, this.mChoiceMode == 0, this.mChoiceMode == 1, this.mAvatarCache, z, z2, z3);
    }

    public void sort() {
        Collections.sort(this.mItems, new MMBuddyItemComparator(CompatUtils.getLocalDefault()));
    }

    @Nullable
    public String getItemSortKey(Object obj) {
        if (!(obj instanceof MMSelectContactsListItem)) {
            return "";
        }
        MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) obj;
        String str = mMSelectContactsListItem.sortKey;
        if (StringUtil.isEmptyOrNull(str)) {
            str = mMSelectContactsListItem.email;
        }
        if (str == null) {
            str = "";
        }
        return str;
    }
}
