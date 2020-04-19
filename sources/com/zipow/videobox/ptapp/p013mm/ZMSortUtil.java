package com.zipow.videobox.ptapp.p013mm;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.view.ISearchableItem;
import com.zipow.videobox.view.p014mm.MMChatsListItem;
import com.zipow.videobox.view.p014mm.MMZoomGroup;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.ZMSortUtil */
public class ZMSortUtil {

    /* renamed from: com.zipow.videobox.ptapp.mm.ZMSortUtil$BuddyComparator */
    private static class BuddyComparator implements Comparator<String> {
        private Map<String, Long> mBuddiesWithStamp;

        BuddyComparator(Map<String, Long> map) {
            this.mBuddiesWithStamp = map;
        }

        public int compare(String str, String str2) {
            Map<String, Long> map = this.mBuddiesWithStamp;
            if (map == null) {
                return 0;
            }
            Long l = (Long) map.get(str);
            Long l2 = (Long) this.mBuddiesWithStamp.get(str2);
            if (l == null && l2 == null) {
                return 0;
            }
            if (l == null) {
                return 1;
            }
            if (l2 == null) {
                return -1;
            }
            int i = ((l.longValue() - l2.longValue()) > 0 ? 1 : ((l.longValue() - l2.longValue()) == 0 ? 0 : -1));
            if (i == 0) {
                return 0;
            }
            return i > 0 ? -1 : 1;
        }
    }

    /* renamed from: com.zipow.videobox.ptapp.mm.ZMSortUtil$GroupComparator */
    private static class GroupComparator implements Comparator<MMZoomGroup> {
        private Collator mCollator;
        private Map<MMZoomGroup, Long> mGroupsWithStamp;

        GroupComparator(Map<MMZoomGroup, Long> map, Locale locale) {
            this.mGroupsWithStamp = map;
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@Nullable MMZoomGroup mMZoomGroup, @NonNull MMZoomGroup mMZoomGroup2) {
            Map<MMZoomGroup, Long> map = this.mGroupsWithStamp;
            if (map == null || mMZoomGroup == mMZoomGroup2) {
                return 0;
            }
            Long l = (Long) map.get(mMZoomGroup);
            Long l2 = (Long) this.mGroupsWithStamp.get(mMZoomGroup2);
            if (l != null && l2 != null) {
                int i = ((l.longValue() - l2.longValue()) > 0 ? 1 : ((l.longValue() - l2.longValue()) == 0 ? 0 : -1));
                if (i == 0) {
                    return 0;
                }
                return i > 0 ? -1 : 1;
            } else if (l != null) {
                return -1;
            } else {
                if (l2 != null || mMZoomGroup == null) {
                    return 1;
                }
                return this.mCollator.compare(mMZoomGroup.getSortKey(), mMZoomGroup2.getSortKey());
            }
        }
    }

    /* renamed from: com.zipow.videobox.ptapp.mm.ZMSortUtil$SearchableItemComparator */
    private static class SearchableItemComparator implements Comparator<ISearchableItem> {
        private Collator mCollator;

        SearchableItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(ISearchableItem iSearchableItem, ISearchableItem iSearchableItem2) {
            String str;
            if (iSearchableItem == iSearchableItem2) {
                return 0;
            }
            if (iSearchableItem.getMatchScore() > iSearchableItem2.getMatchScore()) {
                return 1;
            }
            if (iSearchableItem.getMatchScore() < iSearchableItem2.getMatchScore()) {
                return -1;
            }
            if (iSearchableItem.getPriority() > iSearchableItem2.getPriority()) {
                return 1;
            }
            if (iSearchableItem.getPriority() < iSearchableItem2.getPriority()) {
                return -1;
            }
            if (iSearchableItem.getTimeStamp() < iSearchableItem2.getTimeStamp()) {
                return 1;
            }
            if (iSearchableItem.getTimeStamp() > iSearchableItem2.getTimeStamp()) {
                return -1;
            }
            String title = iSearchableItem.getTitle();
            String title2 = iSearchableItem2.getTitle();
            Collator collator = this.mCollator;
            String lowerCase = title == null ? "" : title.toLowerCase();
            if (title2 == null) {
                str = "";
            } else {
                str = title2.toLowerCase();
            }
            return collator.compare(lowerCase, str);
        }
    }

    /* renamed from: com.zipow.videobox.ptapp.mm.ZMSortUtil$SessionComparator */
    private static class SessionComparator implements Comparator<MMChatsListItem> {
        private Collator mCollator;

        SessionComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        private String getItemSortKey(MMChatsListItem mMChatsListItem) {
            return SortUtil.getSortKey(mMChatsListItem.getTitle(), CompatUtils.getLocalDefault());
        }

        public int compare(@NonNull MMChatsListItem mMChatsListItem, @NonNull MMChatsListItem mMChatsListItem2) {
            if (mMChatsListItem == mMChatsListItem2) {
                return 0;
            }
            if (mMChatsListItem.getTimeStamp() > mMChatsListItem2.getTimeStamp()) {
                return -1;
            }
            if (mMChatsListItem.getTimeStamp() < mMChatsListItem2.getTimeStamp()) {
                return 1;
            }
            return this.mCollator.compare(getItemSortKey(mMChatsListItem), getItemSortKey(mMChatsListItem2));
        }
    }

    @NonNull
    public static List<MMChatsListItem> sortSessions(@NonNull List<MMChatsListItem> list) {
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList, new SessionComparator(CompatUtils.getLocalDefault()));
        return arrayList;
    }

    @NonNull
    public static List<MMZoomGroup> sortGroups(@NonNull List<MMZoomGroup> list) {
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return list;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return list;
        }
        HashMap hashMap = new HashMap();
        for (MMZoomGroup mMZoomGroup : list) {
            if (mMZoomGroup != null && !StringUtil.isEmptyOrNull(mMZoomGroup.getGroupId())) {
                ZoomChatSession findSessionById = zoomMessenger.findSessionById(mMZoomGroup.getGroupId());
                if (findSessionById != null) {
                    ZoomMessage lastMessage = findSessionById.getLastMessage();
                    if (lastMessage != null) {
                        hashMap.put(mMZoomGroup, Long.valueOf(lastMessage.getStamp()));
                    }
                }
            }
        }
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList, new GroupComparator(hashMap, CompatUtils.getLocalDefault()));
        return arrayList;
    }

    @Nullable
    public static List<String> sortBuddies(List<String> list) {
        return sortBuddies(list, 0, null);
    }

    @Nullable
    public static List<String> sortBuddies(List<String> list, int i, String str) {
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return list;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return list;
        }
        List<String> sortBuddies2 = zoomMessenger.sortBuddies2(list, i, str);
        if (sortBuddies2 == null) {
            return list;
        }
        HashMap hashMap = new HashMap();
        int i2 = 0;
        while (i2 < sortBuddies2.size()) {
            String str2 = (String) sortBuddies2.get(i2);
            if (!TextUtils.isEmpty(str2)) {
                ZoomChatSession findSessionById = zoomMessenger.findSessionById(str2);
                if (findSessionById != null) {
                    ZoomMessage lastMessage = findSessionById.getLastMessage();
                    if (lastMessage != null) {
                        sortBuddies2.remove(i2);
                        hashMap.put(str2, Long.valueOf(lastMessage.getStamp()));
                        i2--;
                    }
                }
            }
            i2++;
        }
        if (hashMap.size() == 0) {
            return sortBuddies2;
        }
        ArrayList arrayList = new ArrayList(hashMap.keySet());
        Collections.sort(arrayList, new BuddyComparator(hashMap));
        arrayList.addAll(sortBuddies2);
        return arrayList;
    }

    @Nullable
    public static List<String> sortContactSearchResult(List<String> list) {
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return list;
        }
        SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
        if (searchMgr == null) {
            return list;
        }
        return searchMgr.sortContactSearchResult(list);
    }

    @NonNull
    public static List<ISearchableItem> sortSessionsAndBuddies(@NonNull List<ISearchableItem> list) {
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList, new SearchableItemComparator(CompatUtils.getLocalDefault()));
        return arrayList;
    }
}
