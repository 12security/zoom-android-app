package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IMMessageListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    @NonNull
    private List<IMMessageItem> items = new ArrayList();
    @Nullable
    private Context mContext;

    public long getItemId(int i) {
        return (long) i;
    }

    public int getViewTypeCount() {
        return 11;
    }

    public IMMessageListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.items.clear();
    }

    public void addItem(@Nullable IMMessageItem iMMessageItem) {
        int findItem = findItem(iMMessageItem.nativeHandle);
        if (findItem >= 0) {
            this.items.set(findItem, iMMessageItem);
        } else {
            this.items.add(iMMessageItem);
        }
    }

    public int findItem(long j) {
        if (j == 0) {
            return -1;
        }
        for (int i = 0; i < this.items.size(); i++) {
            if (j == ((IMMessageItem) this.items.get(i)).nativeHandle) {
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
        IMMessageItem iMMessageItem = (IMMessageItem) getItem(i);
        if (iMMessageItem != null) {
            return iMMessageItem.messageType;
        }
        return 0;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        IMMessageItem iMMessageItem = (IMMessageItem) getItem(i);
        if (iMMessageItem == null) {
            return null;
        }
        return iMMessageItem.getView(this.mContext, view);
    }
}
