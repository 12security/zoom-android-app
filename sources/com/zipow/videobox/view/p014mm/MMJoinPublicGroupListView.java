package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomPublicRoomSearchData;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMJoinPublicGroupListView */
public class MMJoinPublicGroupListView extends ListView implements OnScrollListener, OnItemClickListener {
    private static final int MAX_GROUPS_SIZE = 20;
    private PublicGroupAdater mAdapter;
    @NonNull
    private ArrayList<String> mCannotSelectGroups = new ArrayList<>();
    private String mFilter;
    private OnItemSelectChangeListener mOnItemSelectChangeListener;

    /* renamed from: com.zipow.videobox.view.mm.MMJoinPublicGroupListView$OnItemSelectChangeListener */
    public interface OnItemSelectChangeListener {
        void onItemSelectChange();
    }

    /* renamed from: com.zipow.videobox.view.mm.MMJoinPublicGroupListView$PublicGroupAdater */
    static class PublicGroupAdater extends BaseAdapter {
        private Context mContext;
        @NonNull
        private ArrayList<MMZoomXMPPRoom> mGroups = new ArrayList<>();
        @NonNull
        private ArrayList<MMZoomXMPPRoom> mSelectGroups = new ArrayList<>();

        public long getItemId(int i) {
            return 0;
        }

        public void addItems(@NonNull List<MMZoomXMPPRoom> list) {
            this.mGroups.addAll(list);
        }

        public void addItem(MMZoomXMPPRoom mMZoomXMPPRoom) {
            this.mGroups.add(mMZoomXMPPRoom);
        }

        public boolean onItemClick(int i) {
            if (i < 0 || i >= this.mGroups.size()) {
                return false;
            }
            MMZoomXMPPRoom mMZoomXMPPRoom = (MMZoomXMPPRoom) this.mGroups.get(i);
            if (mMZoomXMPPRoom == null || mMZoomXMPPRoom.isJoined()) {
                return false;
            }
            if (this.mSelectGroups.contains(mMZoomXMPPRoom)) {
                this.mSelectGroups.remove(mMZoomXMPPRoom);
            } else {
                this.mSelectGroups.add(mMZoomXMPPRoom);
            }
            return true;
        }

        public void clearAll() {
            this.mGroups.clear();
            this.mSelectGroups.clear();
        }

        public PublicGroupAdater(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return this.mGroups.size();
        }

        public MMZoomXMPPRoom getItem(int i) {
            return (MMZoomXMPPRoom) this.mGroups.get(i);
        }

        @NonNull
        public ArrayList<MMZoomXMPPRoom> getSelectGroups() {
            return this.mSelectGroups;
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(this.mContext, C4558R.layout.zm_public_group_item, null);
            }
            MMZoomXMPPRoom item = getItem(i);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtGroupName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtJoined);
            StringBuilder sb = new StringBuilder();
            sb.append(item.getName());
            sb.append(String.format("(%s)", new Object[]{Long.valueOf(item.getCount())}));
            textView.setText(sb.toString());
            if (item.isJoined()) {
                textView2.setVisibility(0);
                textView.setTextColor(this.mContext.getResources().getColor(C4558R.color.zm_ui_kit_color_gray_747487));
            } else {
                textView2.setVisibility(8);
                textView.setTextColor(this.mContext.getResources().getColor(C4558R.color.zm_ui_kit_color_black_232333));
            }
            return view;
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    public MMJoinPublicGroupListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMJoinPublicGroupListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMJoinPublicGroupListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mAdapter = new PublicGroupAdater(getContext());
        setAdapter(this.mAdapter);
        setOnScrollListener(this);
        setOnItemClickListener(this);
        updateCannotSelectGroups();
    }

    private void updateCannotSelectGroups() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            for (int i = 0; i < zoomMessenger.getGroupCount(); i++) {
                ZoomGroup groupAt = zoomMessenger.getGroupAt(i);
                if (groupAt != null) {
                    this.mCannotSelectGroups.add(groupAt.getGroupID());
                }
            }
        }
    }

    @NonNull
    public ArrayList<MMZoomXMPPRoom> getSelectGroups() {
        return this.mAdapter.getSelectGroups();
    }

    public boolean doSearchPublicGroups(String str) {
        if (StringUtil.isSameString(str, this.mFilter)) {
            return false;
        }
        this.mFilter = str;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomPublicRoomSearchData publicRoomSearchData = zoomMessenger.getPublicRoomSearchData();
        if (publicRoomSearchData == null) {
            return false;
        }
        boolean search = publicRoomSearchData.search(this.mFilter, 20);
        if (search) {
            this.mAdapter.clearAll();
            this.mAdapter.notifyDataSetChanged();
        }
        return search;
    }

    private void loadMore() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomPublicRoomSearchData publicRoomSearchData = zoomMessenger.getPublicRoomSearchData();
            if (publicRoomSearchData != null && publicRoomSearchData.hasMoreDataOnServerSide()) {
                publicRoomSearchData.getNextPage();
            }
        }
    }

    public void onSearchResponse(int i, int i2, int i3) {
        if (i == 0 && i3 != 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomPublicRoomSearchData publicRoomSearchData = zoomMessenger.getPublicRoomSearchData();
                if (publicRoomSearchData != null) {
                    if (i2 == 0) {
                        this.mAdapter.clearAll();
                    }
                    while (i2 < publicRoomSearchData.getRoomCount()) {
                        MMZoomXMPPRoom zoomXMPPRoomAt = publicRoomSearchData.getZoomXMPPRoomAt(i2);
                        if (zoomXMPPRoomAt != null) {
                            zoomXMPPRoomAt.setJoined(this.mCannotSelectGroups.contains(zoomXMPPRoomAt.getJid()));
                            this.mAdapter.addItem(zoomXMPPRoomAt);
                        }
                        i2++;
                    }
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void setOnItemSelectChangeListener(OnItemSelectChangeListener onItemSelectChangeListener) {
        this.mOnItemSelectChangeListener = onItemSelectChangeListener;
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (i != 0 && i + i2 >= i3) {
            loadMore();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.mAdapter.onItemClick(i)) {
            this.mAdapter.notifyDataSetChanged();
            OnItemSelectChangeListener onItemSelectChangeListener = this.mOnItemSelectChangeListener;
            if (onItemSelectChangeListener != null) {
                onItemSelectChangeListener.onItemSelectChange();
            }
        }
    }
}
