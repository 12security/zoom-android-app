package com.zipow.videobox.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.view.ConfChatAttendeeItem.WebinarAttendeeItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;

public class ConfRaiseHandListView extends ListView {
    private static final String TAG = "ConfRaiseHandListView";
    private ConfRaiseHandAdapter mAdapter;

    static class ConfRaiseHandAdapter extends BaseAdapter {
        private Context mContext;
        @NonNull
        private ArrayList<ConfChatAttendeeItem> mItems = new ArrayList<>();

        public long getItemId(int i) {
            return 0;
        }

        public ConfRaiseHandAdapter(Context context) {
            this.mContext = context;
        }

        public void addWebinarAttendeeItem(ZoomQABuddy zoomQABuddy) {
            addWebinarAttendeeItem(new ConfChatAttendeeItem(zoomQABuddy));
        }

        public void addWebinarAttendeeItem(long j) {
            ConfChatAttendeeItem webinarAttendeeItemByNodeId = ConfChatAttendeeItem.getWebinarAttendeeItemByNodeId(j);
            if (webinarAttendeeItemByNodeId != null) {
                addWebinarAttendeeItem(webinarAttendeeItemByNodeId);
            }
        }

        private void addWebinarAttendeeItem(@Nullable ConfChatAttendeeItem confChatAttendeeItem) {
            if (confChatAttendeeItem != null) {
                int searchAttendee = searchAttendee(confChatAttendeeItem);
                if (searchAttendee < 0) {
                    this.mItems.add((-searchAttendee) - 1, confChatAttendeeItem);
                } else {
                    this.mItems.set(searchAttendee, confChatAttendeeItem);
                }
                notifyDataSetChanged();
            }
        }

        public void clear() {
            this.mItems.clear();
        }

        public void removeWebinarAttendeeItem(long j) {
            for (int i = 0; i < this.mItems.size(); i++) {
                if (((ConfChatAttendeeItem) this.mItems.get(i)).nodeID == j) {
                    this.mItems.remove(i);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        public void removeWebinarAttendeeItem(String str) {
            for (int i = 0; i < this.mItems.size(); i++) {
                if (TextUtils.equals(str, ((ConfChatAttendeeItem) this.mItems.get(i)).jid)) {
                    this.mItems.remove(i);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        private int searchAttendee(@NonNull ConfChatAttendeeItem confChatAttendeeItem) {
            int binarySearch = Collections.binarySearch(this.mItems, confChatAttendeeItem, new WebinarAttendeeItemComparator(CompatUtils.getLocalDefault()));
            if (binarySearch < 0) {
                return binarySearch;
            }
            for (int i = binarySearch; i < this.mItems.size(); i++) {
                ConfChatAttendeeItem confChatAttendeeItem2 = (ConfChatAttendeeItem) this.mItems.get(i);
                if (StringUtil.isSameString(confChatAttendeeItem2.jid, confChatAttendeeItem.jid)) {
                    return i;
                }
                if (!StringUtil.isSameString(confChatAttendeeItem2.getSortKey(), confChatAttendeeItem.getSortKey())) {
                    break;
                }
            }
            for (int i2 = binarySearch; i2 >= 0; i2--) {
                ConfChatAttendeeItem confChatAttendeeItem3 = (ConfChatAttendeeItem) this.mItems.get(i2);
                if (StringUtil.isSameString(confChatAttendeeItem3.jid, confChatAttendeeItem.jid)) {
                    return i2;
                }
                if (!StringUtil.isSameString(confChatAttendeeItem3.getSortKey(), confChatAttendeeItem.getSortKey())) {
                    break;
                }
            }
            return (-binarySearch) - 1;
        }

        public int getCount() {
            return this.mItems.size();
        }

        public Object getItem(int i) {
            return this.mItems.get(i);
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (!(item instanceof ConfChatAttendeeItem)) {
                return null;
            }
            return ((ConfChatAttendeeItem) item).getView(this.mContext, view);
        }
    }

    public ConfRaiseHandListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ConfRaiseHandListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfRaiseHandListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mAdapter = new ConfRaiseHandAdapter(getContext());
        if (!isInEditMode()) {
            loadAllItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
    }

    private void loadAllItems(@NonNull ConfRaiseHandAdapter confRaiseHandAdapter) {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null) {
            List<ZoomQABuddy> raisedHandAttendees = raiseHandAPIObj.getRaisedHandAttendees();
            if (raisedHandAttendees != null) {
                for (ZoomQABuddy addWebinarAttendeeItem : raisedHandAttendees) {
                    confRaiseHandAdapter.addWebinarAttendeeItem(addWebinarAttendeeItem);
                }
            }
        }
    }

    public void reloadAllItems() {
        this.mAdapter.clear();
        loadAllItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void addWebinarAttendeeItem(long j) {
        this.mAdapter.addWebinarAttendeeItem(j);
    }

    public void onConfAllowRaiseHandStatusChanged() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (myself == null || ((!myself.isHost() && !myself.isCoHost()) || confStatusObj == null || !confStatusObj.isAllowRaiseHand())) {
            this.mAdapter.clear();
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void removeWebinarAttendeeItem(long j) {
        this.mAdapter.removeWebinarAttendeeItem(j);
    }

    public void removeWebinarAttendeeItem(String str) {
        this.mAdapter.removeWebinarAttendeeItem(str);
    }
}
