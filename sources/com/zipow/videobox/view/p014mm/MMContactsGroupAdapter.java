package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.GroupMemberSynchronizer;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContactsGroupAdapter */
public class MMContactsGroupAdapter extends BaseAdapter {
    private static final String TAG_GROUPS_ITEM = "item";
    private static final String TAG_GROUPS_LABEL = "label";
    private static final int TYPE_GROUPS_ITEM = 1;
    private static final int TYPE_GROUPS_LABEL = 0;
    private static final int TYPE_GROUPS_STARRED = 2;
    private Context mContext;
    @NonNull
    private List<Object> mDisplayDatas = new ArrayList();
    @Nullable
    private String mFilter;
    @NonNull
    private List<MMZoomGroup> mGroups = new ArrayList();

    /* renamed from: com.zipow.videobox.view.mm.MMContactsGroupAdapter$MMZoomGroupComparator */
    static class MMZoomGroupComparator implements Comparator<MMZoomGroup> {
        private Collator mCollator;

        public MMZoomGroupComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@Nullable MMZoomGroup mMZoomGroup, @NonNull MMZoomGroup mMZoomGroup2) {
            if (mMZoomGroup == mMZoomGroup2) {
                return 0;
            }
            if (mMZoomGroup == null) {
                return 1;
            }
            return this.mCollator.compare(mMZoomGroup.getSortKey(), mMZoomGroup2.getSortKey());
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMContactsGroupAdapter$StarredItem */
    class StarredItem {
        String label;

        public StarredItem(String str) {
            this.label = str;
        }
    }

    public long getItemId(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 3;
    }

    public MMContactsGroupAdapter(Context context) {
        this.mContext = context;
    }

    public void clearAll() {
        this.mGroups.clear();
    }

    public void addOrUpdateItem(@Nullable MMZoomGroup mMZoomGroup) {
        if (mMZoomGroup != null) {
            GroupMemberSynchronizer groupMemberSynchronizer = PTApp.getInstance().getGroupMemberSynchronizer();
            if (groupMemberSynchronizer != null) {
                if (mMZoomGroup.getMemberCount() > 0 || groupMemberSynchronizer.needReadGroupMemberFromDB(mMZoomGroup.getGroupId())) {
                    int findGroupIndex = findGroupIndex(mMZoomGroup);
                    if (findGroupIndex == -1) {
                        this.mGroups.add(mMZoomGroup);
                    } else {
                        this.mGroups.set(findGroupIndex, mMZoomGroup);
                    }
                } else {
                    groupMemberSynchronizer.safeSyncGroupMemberFromXmpp(mMZoomGroup.getGroupId());
                }
            }
        }
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

    public void removeItem(@Nullable MMZoomGroup mMZoomGroup) {
        if (mMZoomGroup != null) {
            int findGroupIndex = findGroupIndex(mMZoomGroup);
            if (findGroupIndex != -1) {
                this.mGroups.remove(findGroupIndex);
            }
        }
    }

    public void removeItem(String str) {
        if (!TextUtils.isEmpty(str)) {
            int findGroupIndex = findGroupIndex(str);
            if (findGroupIndex != -1) {
                this.mGroups.remove(findGroupIndex);
            }
        }
    }

    private int findGroupIndex(@Nullable MMZoomGroup mMZoomGroup) {
        if (mMZoomGroup == null) {
            return -1;
        }
        return findGroupIndex(mMZoomGroup.getGroupId());
    }

    private int findGroupIndex(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        for (int i = 0; i < this.mGroups.size(); i++) {
            if (StringUtil.isSameString(((MMZoomGroup) this.mGroups.get(i)).getGroupId(), str)) {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    public MMZoomGroup findGroup(String str) {
        int findGroupIndex = findGroupIndex(str);
        if (findGroupIndex < 0) {
            return null;
        }
        return (MMZoomGroup) this.mGroups.get(findGroupIndex);
    }

    public int getCount() {
        return this.mDisplayDatas.size();
    }

    private void updateDisplayDatas() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        boolean isEmptyOrNull = StringUtil.isEmptyOrNull(this.mFilter);
        Locale localDefault = CompatUtils.getLocalDefault();
        for (MMZoomGroup mMZoomGroup : this.mGroups) {
            if (mMZoomGroup.isPublic()) {
                if (isEmptyOrNull || mMZoomGroup.getGroupName().toLowerCase(localDefault).contains(this.mFilter)) {
                    arrayList2.add(mMZoomGroup);
                }
            } else if (isEmptyOrNull || mMZoomGroup.getGroupName().toLowerCase(localDefault).contains(this.mFilter)) {
                arrayList.add(mMZoomGroup);
            }
        }
        this.mDisplayDatas.clear();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && isEmptyOrNull) {
            List<String> starSessionGetAll = zoomMessenger.starSessionGetAll();
            if (starSessionGetAll != null) {
                for (String sessionById : starSessionGetAll) {
                    ZoomChatSession sessionById2 = zoomMessenger.getSessionById(sessionById);
                    if (sessionById2 != null && sessionById2.isGroup()) {
                        ZoomGroup sessionGroup = sessionById2.getSessionGroup();
                        if (sessionGroup != null && sessionGroup.isRoom() && !sessionGroup.isBroadcast()) {
                            arrayList3.add(MMZoomGroup.initWithZoomGroup(sessionGroup));
                        }
                    }
                }
                if (arrayList3.size() > 0) {
                    Collections.sort(arrayList3, new MMZoomGroupComparator(localDefault));
                    this.mDisplayDatas.add(new StarredItem(this.mContext.getString(C4558R.string.zm_mm_starred_title_name_owp40)));
                    this.mDisplayDatas.addAll(arrayList3);
                }
            }
        }
        if (arrayList.size() > 0) {
            Collections.sort(arrayList, new MMZoomGroupComparator(localDefault));
            this.mDisplayDatas.add(this.mContext.getString(C4558R.string.zm_lbl_contact_private_groups_59554, new Object[]{Integer.valueOf(arrayList.size())}));
            this.mDisplayDatas.addAll(arrayList);
        }
        if (arrayList2.size() > 0) {
            Collections.sort(arrayList2, new MMZoomGroupComparator(localDefault));
            this.mDisplayDatas.add(this.mContext.getString(C4558R.string.zm_lbl_contact_public_groups_59554, new Object[]{Integer.valueOf(arrayList2.size())}));
            this.mDisplayDatas.addAll(arrayList2);
        }
    }

    public void notifyDataSetChanged() {
        updateDisplayDatas();
        super.notifyDataSetChanged();
    }

    public int getItemViewType(int i) {
        if (i < 0 || i >= this.mDisplayDatas.size()) {
            return 0;
        }
        Object obj = this.mDisplayDatas.get(i);
        if (obj instanceof MMZoomGroup) {
            return 1;
        }
        if (!(obj instanceof String) && (obj instanceof StarredItem)) {
            return 2;
        }
        return 0;
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
        switch (getItemViewType(i)) {
            case 0:
                return createGroupsLabel(i, view, viewGroup);
            case 1:
                return createGroupsItem(i, view, viewGroup);
            case 2:
                return createGroupsStarred(i, view, viewGroup);
            default:
                return null;
        }
    }

    @NonNull
    private View createGroupsStarred(int i, @Nullable View view, ViewGroup viewGroup) {
        if (view == null || !TAG_GROUPS_LABEL.equals(view.getTag())) {
            view = View.inflate(this.mContext, C4558R.layout.zm_listview_label_item, null);
            view.setTag(TAG_GROUPS_LABEL);
        }
        ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgLabel);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtHeaderLabel);
        Object item = getItem(i);
        if (item instanceof StarredItem) {
            imageView.setVisibility(0);
            textView.setText(((StarredItem) item).label);
        }
        return view;
    }

    @NonNull
    private View createGroupsLabel(int i, @Nullable View view, ViewGroup viewGroup) {
        if (view == null || !TAG_GROUPS_LABEL.equals(view.getTag())) {
            view = View.inflate(this.mContext, C4558R.layout.zm_listview_label_item, null);
            view.setTag(TAG_GROUPS_LABEL);
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtHeaderLabel);
        Object item = getItem(i);
        if (item != null) {
            textView.setText(item.toString());
        }
        return view;
    }

    @NonNull
    private View createGroupsItem(int i, @Nullable View view, ViewGroup viewGroup) {
        String str;
        if (view == null || !"item".equals(view.getTag())) {
            view = View.inflate(this.mContext, C4558R.layout.zm_contacts_group_item, null);
            view.setTag(TAG_GROUPS_LABEL);
        }
        MMZoomGroup mMZoomGroup = (MMZoomGroup) getItem(i);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtGroupName);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtMemberNo);
        TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.txtGroupdes);
        ((AvatarView) view.findViewById(C4558R.C4560id.avatarView)).show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_avatar_group, null));
        textView.setText(mMZoomGroup.getGroupName());
        textView2.setText(String.format("(%s)", new Object[]{Integer.valueOf(mMZoomGroup.getMemberCount())}));
        if (mMZoomGroup.isPublic()) {
            textView3.setVisibility(0);
            if (!StringUtil.isEmptyOrNull(mMZoomGroup.getGroupOwnerName())) {
                str = String.format("<b>%s</b>", new Object[]{mMZoomGroup.getGroupOwnerName()});
            } else {
                str = String.format("<b>%s</b>", new Object[]{mMZoomGroup.getAdminScreenName()});
            }
            textView3.setText(Html.fromHtml(this.mContext.getString(C4558R.string.zm_lbl_contact_group_description, new Object[]{str})));
        } else {
            textView3.setVisibility(8);
        }
        return view;
    }
}
