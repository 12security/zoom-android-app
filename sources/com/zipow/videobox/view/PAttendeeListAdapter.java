package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.util.PAttendeeItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;

public class PAttendeeListAdapter extends BaseAdapter {
    private Context mContext;
    @NonNull
    private ArrayList<PAttendeeItem> mItems = new ArrayList<>();

    public PAttendeeListAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItems(@NonNull List<PAttendeeItem> list) {
        this.mItems.addAll(list);
    }

    public void updateItem(@NonNull CmmUser cmmUser, @NonNull PAttendeeItem pAttendeeItem, int i) {
        if (cmmUser.isViewOnlyUserCanTalk()) {
            int findItem = findItem(pAttendeeItem.nodeID);
            if (findItem >= 0) {
                this.mItems.set(findItem, pAttendeeItem);
            } else if (i != 1) {
                this.mItems.add(pAttendeeItem);
            }
        }
    }

    public void removeItem(long j) {
        int findItem = findItem(j);
        if (findItem >= 0 && findItem < this.mItems.size()) {
            this.mItems.remove(findItem);
        }
    }

    public void filter(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            for (int size = this.mItems.size() - 1; size >= 0; size--) {
                String str2 = ((PAttendeeItem) this.mItems.get(size)).name;
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
            if (((PAttendeeItem) it.next()).nodeID == j) {
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
    public PAttendeeItem getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return (PAttendeeItem) this.mItems.get(i);
    }

    public long getItemId(int i) {
        PAttendeeItem item = getItem(i);
        if (item != null) {
            return item.nodeID;
        }
        return 0;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        PAttendeeItem item = getItem(i);
        if (item == null) {
            return null;
        }
        return item.getView(this.mContext, view);
    }

    public void sort() {
        Collections.sort(this.mItems, new PAttendeeItemComparator(CompatUtils.getLocalDefault()));
    }
}
