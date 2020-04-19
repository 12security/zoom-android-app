package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConfChatListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int ITEM_TYPE_PRIVATE = 1;
    public static final int ITEM_TYPE_PUBLIC = 0;
    @NonNull
    private List<ConfChatItem> items = new ArrayList();
    @Nullable
    private Context mContext;

    public long getItemId(int i) {
        return (long) i;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public ConfChatListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.items.clear();
    }

    public void addItem(@Nullable ConfChatItem confChatItem) {
        int findItem = findItem(confChatItem.f337id);
        if (findItem >= 0) {
            this.items.set(findItem, confChatItem);
        } else {
            this.items.add(confChatItem);
        }
    }

    public int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.items.size(); i++) {
            if (str.equals(((ConfChatItem) this.items.get(i)).f337id)) {
                return i;
            }
        }
        return -1;
    }

    public int getCount() {
        return this.items.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return this.items.get(i);
    }

    public int getItemViewType(int i) {
        ConfChatItem confChatItem = (ConfChatItem) getItem(i);
        if (confChatItem != null) {
            return confChatItem.type;
        }
        return 0;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        ConfChatItem confChatItem = (ConfChatItem) getItem(i);
        if (confChatItem == null) {
            return null;
        }
        return confChatItem.getView(this.mContext, view);
    }
}
