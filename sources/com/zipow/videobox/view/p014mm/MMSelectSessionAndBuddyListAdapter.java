package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMAddrBookItemView;
import com.zipow.videobox.view.PresenceStateView;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListAdapter */
public class MMSelectSessionAndBuddyListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    @Nullable
    private Context mContext;
    @NonNull
    private List<Object> mItemData = new ArrayList();
    @NonNull
    private List<String> mLoadedContactJids = new ArrayList();

    /* renamed from: com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListAdapter$ItemViewMore */
    public static class ItemViewMore {
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getViewTypeCount() {
        return 4;
    }

    public MMSelectSessionAndBuddyListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItemData.clear();
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

    public void addItems(@Nullable List<Object> list) {
        if (list != null) {
            this.mItemData.addAll(list);
        }
    }

    public void removeItem(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            int i = 0;
            while (true) {
                if (i >= this.mItemData.size()) {
                    break;
                }
                Object obj = this.mItemData.get(i);
                if (!(obj instanceof MMZoomGroup)) {
                    if ((obj instanceof IMAddrBookItem) && TextUtils.equals(str, ((IMAddrBookItem) obj).getJid())) {
                        this.mItemData.remove(i);
                        break;
                    }
                } else if (TextUtils.equals(((MMZoomGroup) obj).getGroupId(), str)) {
                    this.mItemData.remove(i);
                    break;
                }
                i++;
            }
            notifyDataSetChanged();
        }
    }

    public void updateItem(Object obj) {
        boolean z = false;
        if (obj instanceof MMZoomGroup) {
            MMZoomGroup mMZoomGroup = (MMZoomGroup) obj;
            int i = 0;
            while (true) {
                if (i >= this.mItemData.size()) {
                    break;
                }
                Object obj2 = this.mItemData.get(i);
                if ((obj2 instanceof MMZoomGroup) && TextUtils.equals(((MMZoomGroup) obj2).getGroupId(), mMZoomGroup.getGroupId())) {
                    this.mItemData.set(i, mMZoomGroup);
                    z = true;
                    break;
                }
                i++;
            }
        } else if (obj instanceof IMAddrBookItem) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) obj;
            int i2 = 0;
            while (true) {
                if (i2 >= this.mItemData.size()) {
                    break;
                }
                Object obj3 = this.mItemData.get(i2);
                if ((obj3 instanceof IMAddrBookItem) && TextUtils.equals(((IMAddrBookItem) obj3).getJid(), iMAddrBookItem.getJid())) {
                    this.mItemData.set(i2, iMAddrBookItem);
                    z = true;
                    break;
                }
                i2++;
            }
        }
        if (z) {
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return this.mItemData.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return this.mItemData.get(i);
    }

    public int getItemViewType(int i) {
        Object item = getItem(i);
        if (item instanceof IMAddrBookItem) {
            return 0;
        }
        if (item instanceof MMZoomGroup) {
            return 1;
        }
        return item instanceof ItemViewMore ? 2 : 3;
    }

    @Nullable
    public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        Object item = getItem(i);
        if (item == null) {
            return null;
        }
        if (item instanceof IMAddrBookItem) {
            return getBuddyItemView(item, view, viewGroup);
        }
        if (item instanceof MMZoomGroup) {
            return getGroupItemView(item, view, viewGroup);
        }
        if (item instanceof ItemViewMore) {
            if (view == null || !"zm_search_view_more".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_search_view_more, null);
                view.setTag("zm_search_view_more");
            }
            return view;
        }
        String obj = item.toString();
        if (view == null || !"zm_share_category_item".equals(view.getTag())) {
            view = View.inflate(this.mContext, C4558R.layout.zm_listview_label_item, null);
            view.setTag("zm_share_category_item");
        }
        ((TextView) view.findViewById(C4558R.C4560id.txtHeaderLabel)).setText(obj);
        return view;
    }

    private View getContactsItemView(Object obj, @NonNull Context context, @Nullable View view, ViewGroup viewGroup) {
        LayoutInflater from = LayoutInflater.from(context);
        if (from == null) {
            return null;
        }
        int i = 0;
        if (view == null || !"contactsItem".equals(view.getTag())) {
            view = from.inflate(C4558R.layout.zm_share_item, viewGroup, false);
            view.setTag("contactsItem");
        }
        PresenceStateView presenceStateView = (PresenceStateView) view.findViewById(C4558R.C4560id.imgPresence);
        ZMEllipsisTextView zMEllipsisTextView = (ZMEllipsisTextView) view.findViewById(C4558R.C4560id.txtContactName);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtGroupType);
        if (obj instanceof MMZoomGroup) {
            MMZoomGroup mMZoomGroup = (MMZoomGroup) obj;
            presenceStateView.setVisibility(8);
            if (!mMZoomGroup.isPublic()) {
                i = 8;
            }
            textView.setVisibility(i);
            zMEllipsisTextView.setText(mMZoomGroup.getGroupName());
        } else if (obj instanceof IMAddrBookItem) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) obj;
            this.mLoadedContactJids.remove(iMAddrBookItem.getJid());
            this.mLoadedContactJids.add(iMAddrBookItem.getJid());
            textView.setVisibility(8);
            String screenName = iMAddrBookItem.getScreenName();
            boolean z = iMAddrBookItem.getAccountStatus() == 0;
            int i2 = iMAddrBookItem.getAccountStatus() == 1 ? C4558R.string.zm_lbl_deactivated_62074 : iMAddrBookItem.getAccountStatus() == 2 ? C4558R.string.zm_lbl_terminated_62074 : 0;
            if (iMAddrBookItem.isMyNote()) {
                presenceStateView.setVisibility(8);
                zMEllipsisTextView.setText(context.getString(C4558R.string.zm_mm_msg_my_notes_65147, new Object[]{screenName}));
            } else {
                presenceStateView.setVisibility(0);
                zMEllipsisTextView.setEllipsisText(screenName, i2);
                presenceStateView.setState(iMAddrBookItem);
                presenceStateView.setmTxtDeviceTypeGone();
            }
            view.setAlpha(z ? 1.0f : 0.5f);
        }
        return view;
    }

    @Nullable
    private View getBuddyItemView(Object obj, @Nullable View view, ViewGroup viewGroup) {
        IMAddrBookItemView iMAddrBookItemView;
        if (this.mContext == null) {
            return null;
        }
        if (view == null || !"buddyItem".equals(view.getTag())) {
            iMAddrBookItemView = new IMAddrBookItemView(this.mContext);
            iMAddrBookItemView.setTag("buddyItem");
        } else {
            iMAddrBookItemView = (IMAddrBookItemView) view;
        }
        iMAddrBookItemView.setAddrBookItem((IMAddrBookItem) obj, false, true, false);
        return iMAddrBookItemView;
    }

    @Nullable
    private View getGroupItemView(Object obj, @Nullable View view, ViewGroup viewGroup) {
        String str;
        if (this.mContext == null) {
            return null;
        }
        if (view == null || !"groupItem".equals(view.getTag())) {
            view = View.inflate(this.mContext, C4558R.layout.zm_contacts_group_item, null);
            view.setTag("groupItem");
        }
        MMZoomGroup mMZoomGroup = (MMZoomGroup) obj;
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
