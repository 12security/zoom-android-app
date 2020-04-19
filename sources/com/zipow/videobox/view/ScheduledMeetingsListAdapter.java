package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class ScheduledMeetingsListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_PMI = 1;
    private Context mContext;
    @NonNull
    private ArrayList<ScheduledMeetingItem> mItems = new ArrayList<>();

    public int getViewTypeCount() {
        return 2;
    }

    public ScheduledMeetingsListAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItem(@Nullable ScheduledMeetingItem scheduledMeetingItem) {
        if (this.mItems.indexOf(scheduledMeetingItem) == -1) {
            this.mItems.add(scheduledMeetingItem);
        }
    }

    public void sort() {
        if (!CollectionsUtil.isListEmpty(this.mItems)) {
            Collections.sort(this.mItems, new Comparator<ScheduledMeetingItem>() {
                public int compare(@NonNull ScheduledMeetingItem scheduledMeetingItem, @NonNull ScheduledMeetingItem scheduledMeetingItem2) {
                    int extendMeetingType = scheduledMeetingItem.getExtendMeetingType();
                    if (extendMeetingType == 1) {
                        return -1;
                    }
                    int extendMeetingType2 = scheduledMeetingItem2.getExtendMeetingType();
                    if (extendMeetingType2 == 1) {
                        return 1;
                    }
                    if (extendMeetingType == -999) {
                        return -1;
                    }
                    if (extendMeetingType2 == -999) {
                        return 1;
                    }
                    if (scheduledMeetingItem.isHostByLabel()) {
                        return -1;
                    }
                    if (scheduledMeetingItem2.isHostByLabel()) {
                        return 1;
                    }
                    boolean z = scheduledMeetingItem.isRecurring() && !scheduledMeetingItem.ismIsRecCopy();
                    boolean z2 = scheduledMeetingItem2.isRecurring() && !scheduledMeetingItem2.ismIsRecCopy();
                    if (z && !z2) {
                        return 1;
                    }
                    if (!z && z2) {
                        return -1;
                    }
                    if (!z || !z2) {
                        int i = ((scheduledMeetingItem.getRealStartTime() - scheduledMeetingItem2.getRealStartTime()) > 0 ? 1 : ((scheduledMeetingItem.getRealStartTime() - scheduledMeetingItem2.getRealStartTime()) == 0 ? 0 : -1));
                        if (i != 0) {
                            return i > 0 ? 1 : -1;
                        }
                        if (scheduledMeetingItem.ismIsLabel()) {
                            return -1;
                        }
                        if (scheduledMeetingItem2.ismIsLabel()) {
                            return 1;
                        }
                        return 0;
                    } else if (scheduledMeetingItem.ismIsLabel()) {
                        return -1;
                    } else {
                        if (scheduledMeetingItem2.ismIsLabel()) {
                            return 1;
                        }
                        return 0;
                    }
                }
            });
        }
    }

    public int findItem(long j) {
        for (int i = 0; i < this.mItems.size(); i++) {
            if (j == ((ScheduledMeetingItem) this.mItems.get(i)).getMeetingNo()) {
                return i;
            }
        }
        return -1;
    }

    public void removeItem(long j) {
        int findItem = findItem(j);
        if (findItem >= 0) {
            removeItemAt(findItem);
        }
    }

    public void removeItemAt(int i) {
        if (i >= 0 && i < this.mItems.size()) {
            this.mItems.remove(i);
        }
    }

    public int getCount() {
        return this.mItems.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return this.mItems.get(i);
    }

    public long getItemId(int i) {
        ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) getItem(i);
        if (scheduledMeetingItem != null) {
            return scheduledMeetingItem.getMeetingNo();
        }
        return 0;
    }

    public int getItemViewType(int i) {
        ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) getItem(i);
        return (scheduledMeetingItem == null || scheduledMeetingItem.getExtendMeetingType() != 1) ? 0 : 1;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) getItem(i);
        if (scheduledMeetingItem == null) {
            return null;
        }
        return scheduledMeetingItem.getView(this.mContext, view, viewGroup);
    }
}
