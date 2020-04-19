package com.zipow.videobox.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.MMChatListFooter;
import com.zipow.videobox.view.p014mm.MMChatsListItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMSearchAdapter extends BaseAdapter {
    private Context mContext;
    @NonNull
    private List<String> mLoadedContactJids = new ArrayList();
    @NonNull
    private List<Object> searchData = new ArrayList();

    public static class ItemViewMore {
    }

    public static class ItemWebSearching {
    }

    public long getItemId(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 6;
    }

    public IMSearchAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.searchData.clear();
    }

    public void addAllItems(@NonNull List<Object> list) {
        this.searchData.addAll(list);
    }

    public void removeItem(Object obj) {
        int i = 0;
        if (obj instanceof MMChatsListItem) {
            MMChatsListItem mMChatsListItem = (MMChatsListItem) obj;
            while (true) {
                if (i >= this.searchData.size()) {
                    break;
                }
                Object obj2 = this.searchData.get(i);
                if ((obj2 instanceof MMChatsListItem) && TextUtils.equals(((MMChatsListItem) obj2).getSessionId(), mMChatsListItem.getSessionId())) {
                    this.searchData.remove(i);
                    break;
                }
                i++;
            }
        } else if (obj instanceof IMAddrBookItem) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) obj;
            while (true) {
                if (i >= this.searchData.size()) {
                    break;
                }
                Object obj3 = this.searchData.get(i);
                if ((obj3 instanceof IMAddrBookItem) && TextUtils.equals(((IMAddrBookItem) obj3).getJid(), iMAddrBookItem.getJid())) {
                    this.searchData.remove(i);
                    break;
                }
                i++;
            }
        }
        notifyDataSetChanged();
    }

    public void updateItem(Object obj) {
        boolean z = false;
        if (obj instanceof MMChatsListItem) {
            MMChatsListItem mMChatsListItem = (MMChatsListItem) obj;
            int i = 0;
            while (true) {
                if (i >= this.searchData.size()) {
                    break;
                }
                Object obj2 = this.searchData.get(i);
                if ((obj2 instanceof MMChatsListItem) && TextUtils.equals(((MMChatsListItem) obj2).getSessionId(), mMChatsListItem.getSessionId())) {
                    this.searchData.set(i, mMChatsListItem);
                    z = true;
                    break;
                }
                i++;
            }
        } else if (obj instanceof IMAddrBookItem) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) obj;
            int i2 = 0;
            while (true) {
                if (i2 >= this.searchData.size()) {
                    break;
                }
                Object obj3 = this.searchData.get(i2);
                if (obj3 instanceof IMAddrBookItem) {
                    if (TextUtils.equals(((IMAddrBookItem) obj3).getJid(), iMAddrBookItem.getJid())) {
                        this.searchData.set(i2, iMAddrBookItem);
                        z = true;
                        break;
                    }
                } else if (obj3 instanceof MMChatsListItem) {
                    MMChatsListItem mMChatsListItem2 = (MMChatsListItem) obj3;
                    if (!mMChatsListItem2.isGroup() && mMChatsListItem2.getFromContact() != null && TextUtils.equals(iMAddrBookItem.getJid(), mMChatsListItem2.getFromContact().getJid())) {
                        mMChatsListItem2.setFromContact(iMAddrBookItem);
                        z = true;
                        break;
                    }
                } else {
                    continue;
                }
                i2++;
            }
        }
        if (z) {
            notifyDataSetChanged();
        }
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

    public int getCount() {
        return this.searchData.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= this.searchData.size()) {
            return null;
        }
        return this.searchData.get(i);
    }

    public int getItemViewType(int i) {
        Object item = getItem(i);
        if (item == null || (item instanceof IMAddrBookItem)) {
            return 0;
        }
        if (item instanceof MMChatsListItem) {
            return 1;
        }
        if (item instanceof ItemWebSearching) {
            return 2;
        }
        if (item instanceof MMChatListFooter) {
            return 3;
        }
        return item instanceof ItemViewMore ? 4 : 5;
    }

    @Nullable
    public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
        Object item = getItem(i);
        if (item == null) {
            return null;
        }
        if (item instanceof MMChatsListItem) {
            MMChatsListItem mMChatsListItem = (MMChatsListItem) item;
            if (!mMChatsListItem.isGroup() && mMChatsListItem.getFromContact() != null) {
                this.mLoadedContactJids.remove(mMChatsListItem.getFromContact().getJid());
                this.mLoadedContactJids.add(mMChatsListItem.getFromContact().getJid());
            }
            return mMChatsListItem.getView(this.mContext, view, viewGroup);
        }
        int i2 = 0;
        if (item instanceof IMAddrBookItem) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) item;
            this.mLoadedContactJids.remove(iMAddrBookItem.getJid());
            this.mLoadedContactJids.add(iMAddrBookItem.getJid());
            return iMAddrBookItem.getView(this.mContext, view, false, true);
        }
        if (item instanceof ItemWebSearching) {
            if (view == null || !"zm_search_web_searching".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_list_load_more_footer, null);
                view.findViewById(C4558R.C4560id.panelLoadMoreView).setVisibility(0);
                view.setTag("zm_search_web_searching");
            }
        } else if (item instanceof MMChatListFooter) {
            return ((MMChatListFooter) item).getView(this.mContext, view, viewGroup);
        } else {
            if (!(item instanceof ItemViewMore)) {
                String obj = item.toString();
                if (view == null || !"zm_search_category_item".equals(view.getTag())) {
                    view = View.inflate(this.mContext, C4558R.layout.zm_search_category_item, null);
                    view.setTag("zm_search_category_item");
                }
                ((TextView) view.findViewById(C4558R.C4560id.txtCategoryItem)).setText(obj);
                View findViewById = view.findViewById(C4558R.C4560id.viewPaddingTop);
                if (i == 0) {
                    i2 = 8;
                }
                findViewById.setVisibility(i2);
            } else if (view == null || !"zm_search_view_more".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_search_view_more, null);
                view.setTag("zm_search_view_more");
            }
        }
        return view;
    }
}
