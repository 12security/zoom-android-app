package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectGroupListView */
public class MMSelectGroupListView extends ListView {
    private static boolean mIsContanMUC = true;
    private MMSelectGroupAdapter mAdapter;

    /* renamed from: com.zipow.videobox.view.mm.MMSelectGroupListView$MMSelectGroupAdapter */
    static class MMSelectGroupAdapter extends BaseAdapter {
        private static final String TAG_GROUPS_ITEM = "item";
        private static final String TAG_GROUPS_LABEL = "label";
        private Context mContext;
        @NonNull
        private List<MMZoomGroup> mData = new ArrayList();
        @NonNull
        private List<MMZoomGroup> mDisplayData = new ArrayList();
        private boolean mIsMultSelect = false;
        private String mKey;
        @Nullable
        private List<String> mPreSelects = null;
        @NonNull
        private ArrayList<String> mSelects = new ArrayList<>();

        public long getItemId(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public MMSelectGroupAdapter(Context context) {
            this.mContext = context;
        }

        public void setFilter(String str) {
            this.mKey = str;
        }

        public boolean isGroupSelected(String str) {
            return this.mSelects.contains(str);
        }

        public void updateGroup(String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(str);
                    if (groupById != null) {
                        if (!groupById.amIInGroup() || groupById.getBuddyCount() <= 2) {
                            removeGroup(str);
                            this.mSelects.remove(str);
                        } else {
                            this.mData.add(MMZoomGroup.initWithZoomGroup(groupById));
                        }
                    }
                }
            }
        }

        public void removeGroup(String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                this.mSelects.remove(str);
                int i = 0;
                while (true) {
                    if (i >= this.mData.size()) {
                        break;
                    } else if (StringUtil.isSameString(str, ((MMZoomGroup) this.mData.get(i)).getGroupId())) {
                        this.mData.remove(i);
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }

        public void setData(@NonNull List<MMZoomGroup> list) {
            if (!CollectionsUtil.isListEmpty(list)) {
                this.mData.clear();
                for (MMZoomGroup mMZoomGroup : list) {
                    if (mMZoomGroup.getMemberCount() > 2) {
                        this.mData.add(mMZoomGroup);
                    }
                }
                Collections.sort(this.mData, new MMZoomGroupComparator(CompatUtils.getLocalDefault()));
            }
        }

        public void onItemClicked(String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                List<String> list = this.mPreSelects;
                if (list == null || !list.contains(str)) {
                    if (!this.mSelects.contains(str)) {
                        this.mSelects.add(str);
                    } else {
                        this.mSelects.remove(str);
                    }
                }
            }
        }

        public void unselectBuddy(String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                this.mSelects.remove(str);
            }
        }

        public void setIsMultSelect(boolean z) {
            this.mIsMultSelect = z;
        }

        public void setPreSelects(@Nullable List<String> list) {
            this.mPreSelects = list;
        }

        @NonNull
        public ArrayList<String> getSelectedBuddies() {
            return this.mSelects;
        }

        public int getCount() {
            return this.mDisplayData.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= this.mDisplayData.size()) {
                return null;
            }
            return this.mDisplayData.get(i);
        }

        public int getItemViewType(int i) {
            return getItem(i) instanceof MMZoomGroup ? 0 : 1;
        }

        public void notifyDataSetChanged() {
            sortAll();
            super.notifyDataSetChanged();
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (item == null) {
                return null;
            }
            if (item instanceof MMZoomGroup) {
                return createGroupsItem((MMZoomGroup) item, view, viewGroup);
            }
            return createGroupsLabel(item.toString(), view, viewGroup);
        }

        @NonNull
        private View createGroupsLabel(String str, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !TAG_GROUPS_LABEL.equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_listview_label_item, null);
                view.setTag(TAG_GROUPS_LABEL);
            }
            ((TextView) view.findViewById(C4558R.C4560id.txtHeaderLabel)).setText(str);
            return view;
        }

        @NonNull
        private View createGroupsItem(@NonNull MMZoomGroup mMZoomGroup, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"item".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_contacts_group_item, null);
                view.setTag(TAG_GROUPS_LABEL);
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtGroupName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtMemberNo);
            TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.txtGroupdes);
            CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(C4558R.C4560id.check);
            ((AvatarView) view.findViewById(C4558R.C4560id.avatarView)).show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_avatar_group, null));
            textView.setText(mMZoomGroup.getGroupName());
            textView2.setText(String.format("(%s)", new Object[]{Integer.valueOf(mMZoomGroup.getMemberCount())}));
            if (this.mIsMultSelect) {
                checkedTextView.setVisibility(0);
                List<String> list = this.mPreSelects;
                if (list == null || !list.contains(mMZoomGroup.getGroupId())) {
                    checkedTextView.setEnabled(true);
                    checkedTextView.setChecked(this.mSelects.contains(mMZoomGroup.getGroupId()));
                } else {
                    checkedTextView.setEnabled(false);
                    checkedTextView.setChecked(true);
                }
            } else {
                checkedTextView.setVisibility(8);
            }
            textView3.setVisibility(8);
            return view;
        }

        private void sortAll() {
            this.mDisplayData.clear();
            for (MMZoomGroup mMZoomGroup : this.mData) {
                if (!StringUtil.isEmptyOrNull(mMZoomGroup.getGroupName()) && (StringUtil.isEmptyOrNull(this.mKey) || mMZoomGroup.getGroupName().contains(this.mKey))) {
                    List<String> list = this.mPreSelects;
                    if (list == null || !list.contains(mMZoomGroup.getGroupId())) {
                        this.mDisplayData.add(mMZoomGroup);
                    }
                }
            }
            Collections.sort(this.mDisplayData, new MMZoomGroupComparator(CompatUtils.getLocalDefault()));
        }
    }

    public MMSelectGroupListView(Context context) {
        super(context);
        init();
    }

    public MMSelectGroupListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMSelectGroupListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mAdapter = new MMSelectGroupAdapter(getContext());
        setAdapter(this.mAdapter);
    }

    public void updateUI() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < zoomMessenger.getGroupCount(); i++) {
                ZoomGroup groupAt = zoomMessenger.getGroupAt(i);
                if (groupAt != null && (mIsContanMUC || groupAt.isRoom())) {
                    arrayList.add(MMZoomGroup.initWithZoomGroup(groupAt));
                }
            }
            this.mAdapter.setData(arrayList);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    public MMZoomGroup getItem(int i) {
        Object item = this.mAdapter.getItem(i - getHeaderViewsCount());
        if (item instanceof MMZoomGroup) {
            return (MMZoomGroup) item;
        }
        return null;
    }

    public void updateGroup(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mAdapter.updateGroup(str);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void removeGroup(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mAdapter.removeGroup(str);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setFilter(String str) {
        this.mAdapter.setFilter(str);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onItemClicked(String str) {
        this.mAdapter.onItemClicked(str);
        this.mAdapter.notifyDataSetChanged();
    }

    public void unselectBuddy(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mAdapter.unselectBuddy(str);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setIsMultSelect(boolean z) {
        this.mAdapter.setIsMultSelect(z);
    }

    public void setmIsContanMUC(boolean z) {
        mIsContanMUC = z;
    }

    public void setPreSelects(List<String> list) {
        this.mAdapter.setPreSelects(list);
    }

    @NonNull
    public ArrayList<String> getSelectedBuddies() {
        return this.mAdapter.getSelectedBuddies();
    }

    public boolean isGroupSelected(String str) {
        return this.mAdapter.isGroupSelected(str);
    }
}
