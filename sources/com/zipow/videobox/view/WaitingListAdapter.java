package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;

public class WaitingListAdapter extends BaseAdapter {
    private Context mContext;
    @NonNull
    private ArrayList<WaitingListItem> mItems = new ArrayList<>();

    public WaitingListAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItems(@NonNull List<WaitingListItem> list) {
        this.mItems.addAll(list);
    }

    public void updateItem(@NonNull CmmUser cmmUser, @NonNull WaitingListItem waitingListItem, int i) {
        boolean isUserOnHold = ConfMgr.getInstance().isUserOnHold(cmmUser);
        int findItem = findItem(waitingListItem.userId);
        if (findItem >= 0) {
            if (!isUserOnHold || i == 1) {
                this.mItems.remove(findItem);
            } else {
                this.mItems.set(findItem, waitingListItem);
            }
        } else if (isUserOnHold && i != 1) {
            this.mItems.add(waitingListItem);
        }
    }

    public boolean removeItem(long j) {
        int findItem = findItem(j);
        if (findItem < 0 || findItem >= this.mItems.size()) {
            return false;
        }
        this.mItems.remove(findItem);
        return true;
    }

    public void filter(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            for (int size = this.mItems.size() - 1; size >= 0; size--) {
                String str2 = ((WaitingListItem) this.mItems.get(size)).screenName;
                if (str2 == null) {
                    str2 = "";
                }
                if (!str2.toLowerCase(CompatUtils.getLocalDefault()).contains(str)) {
                    this.mItems.remove(size);
                }
            }
        }
    }

    private int findItem(long j) {
        Iterator it = this.mItems.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (((WaitingListItem) it.next()).userId == j) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int getCount() {
        return this.mItems.size();
    }

    @Nullable
    public WaitingListItem getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return (WaitingListItem) this.mItems.get(i);
    }

    public long getItemId(int i) {
        WaitingListItem item = getItem(i);
        if (item != null) {
            return item.userId;
        }
        return 0;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        WaitingListItem item = getItem(i);
        if (item == null) {
            return null;
        }
        return item.getView(this.mContext, view);
    }
}
