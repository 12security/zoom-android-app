package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.fragment.PAttendeeListActionDialog;
import com.zipow.videobox.fragment.PListItemActionDialog;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.WebinarRaiseHandListItem.WebinarAttendeeItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.videomeetings.C4558R;

public class WebinarRaiseHandListView extends ListView implements OnItemClickListener {
    private WebinarRaiseHandAdapter mAdapter;

    static class WebinarRaiseHandAdapter extends BaseAdapter {
        private final ArrayList<WebinarRaiseHandListItem> mAttendeeItems = new ArrayList<>();
        private final Context mContext;
        private final ArrayList<WebinarRaiseHandListItem> mPanelistItems = new ArrayList<>();

        public long getItemId(int i) {
            return (long) i;
        }

        public WebinarRaiseHandAdapter(Context context) {
            this.mContext = context;
        }

        @NonNull
        public ArrayList<WebinarRaiseHandListItem> getmAttendeeItems() {
            return this.mAttendeeItems;
        }

        @NonNull
        public ArrayList<WebinarRaiseHandListItem> getmPanelistItems() {
            return this.mPanelistItems;
        }

        public void clear() {
            this.mAttendeeItems.clear();
            this.mPanelistItems.clear();
        }

        public int getRaiseHandCount() {
            return this.mPanelistItems.size() + this.mAttendeeItems.size();
        }

        public int getCount() {
            int i = 0;
            if (this.mPanelistItems.size() > 0) {
                i = 0 + this.mPanelistItems.size() + 1;
            }
            return this.mAttendeeItems.size() > 0 ? i + this.mAttendeeItems.size() + 1 : i;
        }

        @Nullable
        public Object getItem(int i) {
            int size = this.mPanelistItems.size();
            int size2 = this.mAttendeeItems.size();
            Object obj = null;
            if (i == 0 || getCount() == 0 || i >= getCount()) {
                return null;
            }
            if (size <= 0 || i >= size + 1) {
                if (size > 0) {
                    int i2 = size + 1;
                    if (i > i2) {
                        obj = this.mAttendeeItems.get((i - i2) - 1);
                    }
                }
                if (size == 0 && i < size2 + 1) {
                    obj = this.mAttendeeItems.get(i - 1);
                }
            } else {
                obj = this.mPanelistItems.get(i - 1);
            }
            return obj;
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (this.mPanelistItems.size() > 0 && i == 0) {
                return getGroupTitleView(i, view, this.mContext.getString(C4558R.string.zm_webinar_txt_panelists, new Object[]{Integer.valueOf(this.mPanelistItems.size())}));
            } else if (!(this.mPanelistItems.size() == 0 && i == 0) && (this.mPanelistItems.size() <= 0 || i != this.mPanelistItems.size() + 1)) {
                Object item = getItem(i);
                if (!(item instanceof WebinarRaiseHandListItem)) {
                    return null;
                }
                return ((WebinarRaiseHandListItem) item).getView(this.mContext, view);
            } else {
                return getGroupTitleView(i, view, this.mContext.getString(C4558R.string.zm_webinar_txt_attendees, new Object[]{Integer.valueOf(this.mAttendeeItems.size())}));
            }
        }

        private View getGroupTitleView(int i, View view, String str) {
            if (view == null || !"groupname".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_listview_label_item, null);
                view.setTag("groupname");
            }
            ((TextView) view.findViewById(C4558R.C4560id.txtHeaderLabel)).setText(str);
            return view;
        }
    }

    public WebinarRaiseHandListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public WebinarRaiseHandListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public WebinarRaiseHandListView(Context context) {
        super(context);
        initView();
    }

    public int getRaiseHandCount() {
        return this.mAdapter.getRaiseHandCount();
    }

    private void initView() {
        this.mAdapter = new WebinarRaiseHandAdapter(getContext());
        if (!isInEditMode()) {
            loadAllItems(this.mAdapter);
        }
        setOnItemClickListener(this);
        setAdapter(this.mAdapter);
    }

    private void loadAllItems(WebinarRaiseHandAdapter webinarRaiseHandAdapter) {
        loadAllWebinarPanelistRaiseHand();
        loadAllWebinarAttendeeRaiseHand();
    }

    private void loadAllWebinarPanelistRaiseHand() {
        CmmUserList userList = ConfMgr.getInstance().getUserList();
        if (userList != null) {
            BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
            boolean isInBOMeeting = bOMgr != null ? bOMgr.isInBOMeeting() : false;
            int userCount = userList.getUserCount();
            ArrayList arrayList = this.mAdapter.getmPanelistItems();
            arrayList.clear();
            for (int i = 0; i < userCount; i++) {
                CmmUser userAt = userList.getUserAt(i);
                if (userAt != null && !userAt.isMMRUser() && ((isInBOMeeting || !userAt.isInBOMeeting()) && userAt.getRaiseHandState() && !userAt.isViewOnlyUserCanTalk())) {
                    arrayList.add(new WebinarRaiseHandListItem(userAt));
                }
            }
            if (!arrayList.isEmpty()) {
                Collections.sort(arrayList, new WebinarAttendeeItemComparator(CompatUtils.getLocalDefault()));
            }
        }
    }

    private void loadAllWebinarAttendeeRaiseHand() {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null) {
            List<ZoomQABuddy> raisedHandAttendees = raiseHandAPIObj.getRaisedHandAttendees();
            ArrayList arrayList = this.mAdapter.getmAttendeeItems();
            arrayList.clear();
            if (raisedHandAttendees != null) {
                for (ZoomQABuddy webinarRaiseHandListItem : raisedHandAttendees) {
                    arrayList.add(new WebinarRaiseHandListItem(webinarRaiseHandListItem));
                }
                if (!arrayList.isEmpty()) {
                    Collections.sort(arrayList, new WebinarAttendeeItemComparator(CompatUtils.getLocalDefault()));
                }
            }
        }
    }

    public void reloadAllAttendees() {
        loadAllWebinarAttendeeRaiseHand();
        this.mAdapter.notifyDataSetChanged();
    }

    public void reloadAllPanelist() {
        loadAllWebinarPanelistRaiseHand();
        this.mAdapter.notifyDataSetChanged();
    }

    public void onConfAllowRaiseHandStatusChanged() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (!ConfLocalHelper.isHostCoHost() || confStatusObj == null || !confStatusObj.isAllowRaiseHand()) {
            this.mAdapter.clear();
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mAdapter.getItem(i);
        if (item != null && (item instanceof WebinarRaiseHandListItem)) {
            WebinarRaiseHandListItem webinarRaiseHandListItem = (WebinarRaiseHandListItem) item;
            int itemType = webinarRaiseHandListItem.getItemType();
            if (itemType == WebinarRaiseHandListItem.ITEM_TYPE_PANELIST) {
                PListItemActionDialog.show(((ZMActivity) getContext()).getSupportFragmentManager(), webinarRaiseHandListItem.getUserId());
            } else if (itemType == WebinarRaiseHandListItem.ITEM_TYPE_ATTENDEE) {
                ZMActivity zMActivity = (ZMActivity) getContext();
                if (zMActivity != null && ConfLocalHelper.isNeedShowAttendeeActionList()) {
                    ConfChatAttendeeItem confChatAttendeeItem = webinarRaiseHandListItem.getConfChatAttendeeItem();
                    if (confChatAttendeeItem != null) {
                        PAttendeeListActionDialog.show(zMActivity.getSupportFragmentManager(), confChatAttendeeItem);
                    }
                }
            }
        }
    }
}
