package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.PresenceStateView;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContactsRoomAdapter */
public class MMContactsRoomAdapter extends QuickSearchListDataAdapter {
    @NonNull
    private List<IMAddrBookItem> mCacheDatas = new ArrayList();
    private Context mContext;
    @NonNull
    private List<IMAddrBookItem> mDisplayDatas = new ArrayList();
    @Nullable
    private String mFilter;
    @NonNull
    private List<String> mWaitRefreshJids = new ArrayList();

    /* renamed from: com.zipow.videobox.view.mm.MMContactsRoomAdapter$RoomComparator */
    static class RoomComparator implements Comparator<IMAddrBookItem> {
        private Collator mCollator;

        public RoomComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull IMAddrBookItem iMAddrBookItem, @NonNull IMAddrBookItem iMAddrBookItem2) {
            if (iMAddrBookItem == iMAddrBookItem2) {
                return 0;
            }
            return this.mCollator.compare(iMAddrBookItem.getSortKey(), iMAddrBookItem2.getSortKey());
        }
    }

    public long getItemId(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public MMContactsRoomAdapter(Context context) {
        this.mContext = context;
    }

    public void clearAll() {
        this.mCacheDatas.clear();
    }

    public void addOrUpdateItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            int findGroup = findGroup(iMAddrBookItem);
            if (findGroup == -1) {
                this.mCacheDatas.add(iMAddrBookItem);
            } else {
                this.mCacheDatas.set(findGroup, iMAddrBookItem);
            }
        }
    }

    @NonNull
    public List<String> getWaitRefreshJids() {
        return this.mWaitRefreshJids;
    }

    public void clearWaitRefreshJids() {
        this.mWaitRefreshJids.clear();
    }

    public void filter(@Nullable String str) {
        String str2;
        if (!StringUtil.isSameString(str, this.mFilter)) {
            if (str == null) {
                str2 = null;
            } else {
                str2 = str.toLowerCase(CompatUtils.getLocalDefault());
            }
            this.mFilter = str2;
            notifyDataSetChanged();
        }
    }

    public void addAllItems(@Nullable List<IMAddrBookItem> list) {
        this.mCacheDatas.clear();
        if (list != null) {
            this.mCacheDatas.addAll(list);
        }
    }

    public void removeItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            int findGroup = findGroup(iMAddrBookItem);
            if (findGroup != -1) {
                this.mCacheDatas.remove(findGroup);
            }
        }
    }

    public boolean isContainRoom(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        for (int i = 0; i < this.mCacheDatas.size(); i++) {
            if (StringUtil.isSameString(((IMAddrBookItem) this.mCacheDatas.get(i)).getJid(), str)) {
                return true;
            }
        }
        return false;
    }

    private int findGroup(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem == null) {
            return -1;
        }
        for (int i = 0; i < this.mCacheDatas.size(); i++) {
            if (StringUtil.isSameString(((IMAddrBookItem) this.mCacheDatas.get(i)).getJid(), iMAddrBookItem.getJid())) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasRooms() {
        return !this.mCacheDatas.isEmpty();
    }

    public int getCount() {
        return this.mDisplayDatas.size();
    }

    private void updateDisplayDatas() {
        this.mDisplayDatas.clear();
        boolean showOfflineBuddies = PTSettingHelper.getShowOfflineBuddies();
        if (!StringUtil.isEmptyOrNull(this.mFilter)) {
            Locale localDefault = CompatUtils.getLocalDefault();
            for (IMAddrBookItem iMAddrBookItem : this.mCacheDatas) {
                String screenName = iMAddrBookItem.getScreenName();
                if (screenName != null && screenName.toLowerCase(localDefault).contains(this.mFilter)) {
                    if (showOfflineBuddies) {
                        this.mDisplayDatas.add(iMAddrBookItem);
                    } else if (iMAddrBookItem.getIsDesktopOnline()) {
                        this.mDisplayDatas.add(iMAddrBookItem);
                    }
                }
            }
        } else if (showOfflineBuddies) {
            this.mDisplayDatas.addAll(this.mCacheDatas);
        } else {
            for (IMAddrBookItem iMAddrBookItem2 : this.mCacheDatas) {
                if (iMAddrBookItem2.getIsDesktopOnline()) {
                    this.mDisplayDatas.add(iMAddrBookItem2);
                }
            }
        }
    }

    public void notifyDataSetChanged() {
        updateDisplayDatas();
        super.notifyDataSetChanged();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= this.mDisplayDatas.size()) {
            return null;
        }
        return this.mDisplayDatas.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        return createGroupsItem(i, view, viewGroup);
    }

    @NonNull
    private View createGroupsItem(int i, @Nullable View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(this.mContext, C4558R.layout.zm_contact_room_item, null);
        }
        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) getItem(i);
        this.mWaitRefreshJids.add(iMAddrBookItem.getJid());
        ((TextView) view.findViewById(C4558R.C4560id.txtRoomName)).setText(iMAddrBookItem.getScreenName());
        ((PresenceStateView) view.findViewById(C4558R.C4560id.presenceStateView)).setState(iMAddrBookItem);
        return view;
    }

    public String getItemSortKey(Object obj) {
        if (!(obj instanceof IMAddrBookItem)) {
            return "*";
        }
        String sortKey = ((IMAddrBookItem) obj).getSortKey();
        if (sortKey == null) {
            sortKey = "";
        }
        return sortKey;
    }
}
