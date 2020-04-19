package com.zipow.videobox.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.PListItemComparator;
import com.zipow.videobox.util.PListItemNewComparator;
import com.zipow.videobox.view.PListView.StatusPListItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PListAdapter extends BaseAdapter {
    private boolean isEnableWaitingList;
    private boolean isInSearchProgress = false;
    private boolean isWebinar = false;
    private int mAttendeeLabelPos = -1;
    /* access modifiers changed from: private */
    public Context mContext;
    @NonNull
    private ArrayList<PListItem> mExcludeViewItems = new ArrayList<>();
    private int mOnHoldLabelPos = -1;
    private PAttendeeListAdapter mPAttendeeListAdapter;
    private int mPListLabelPos = -1;
    private int mPListSeachPos = -1;
    private PListView mPListView;
    @NonNull
    private ArrayList<PListItem> mViewPListItems = new ArrayList<>();
    private WaitingListAdapter mWaitingAdapter;

    public PListAdapter(Context context, PListView pListView) {
        this.mContext = context;
        this.mPListView = pListView;
        this.mWaitingAdapter = new WaitingListAdapter(context);
        this.mPAttendeeListAdapter = new PAttendeeListAdapter(context);
    }

    public void clear() {
        this.mExcludeViewItems.clear();
        this.mViewPListItems.clear();
        this.mWaitingAdapter.clear();
        this.mPAttendeeListAdapter.clear();
    }

    public void addWaitItems(@NonNull List<WaitingListItem> list) {
        this.mWaitingAdapter.addItems(list);
    }

    public void addViewPlistItems(@NonNull List<PListItem> list) {
        this.mViewPListItems.addAll(list);
    }

    public void addExcludeViewPlistItems(@NonNull List<PListItem> list) {
        this.mExcludeViewItems.addAll(list);
    }

    public void addPAttendeeItems(@NonNull List<PAttendeeItem> list) {
        this.mPAttendeeListAdapter.addItems(list);
    }

    public void setEnableWaitingList(boolean z) {
        this.isEnableWaitingList = z;
    }

    public void setIsWebinar(boolean z) {
        this.isWebinar = z;
    }

    public void updateItem(@NonNull CmmUser cmmUser, boolean z, boolean z2, int i) {
        if (z2 && cmmUser.inSilentMode() && z) {
            this.mWaitingAdapter.updateItem(cmmUser, new WaitingListItem(cmmUser), i);
        } else if (!cmmUser.isViewOnlyUserCanTalk()) {
            updatePlistItem(cmmUser, new PListItem(cmmUser), i);
        } else {
            this.mPAttendeeListAdapter.updateItem(cmmUser, new PAttendeeItem(cmmUser), i);
        }
    }

    public void updateItem(@NonNull CmmUser cmmUser, boolean z, int i) {
        if (z) {
            this.mWaitingAdapter.updateItem(cmmUser, new WaitingListItem(cmmUser), i);
        }
        if (cmmUser.isViewOnlyUserCanTalk()) {
            this.mPAttendeeListAdapter.updateItem(cmmUser, new PAttendeeItem(cmmUser), i);
        } else {
            updatePlistItem(cmmUser, new PListItem(cmmUser), i);
        }
    }

    public void onLeavingSilentModeStatusChanged(@NonNull CmmUser cmmUser, boolean z, int i) {
        if (z) {
            this.mWaitingAdapter.updateItem(cmmUser, new WaitingListItem(cmmUser), i);
        }
    }

    public void removeItem(long j, boolean z) {
        CmmUserList userList = ConfMgr.getInstance().getUserList();
        if (userList != null) {
            CmmUser leftUserById = userList.getLeftUserById(j);
            if (leftUserById != null) {
                if (leftUserById.isViewOnlyUserCanTalk()) {
                    this.mPAttendeeListAdapter.removeItem(j);
                }
                boolean z2 = false;
                if (z) {
                    z2 = this.mWaitingAdapter.removeItem(j);
                }
                if (!z2) {
                    removePlistItem(j);
                }
            }
        }
    }

    private void removePlistItem(long j) {
        int findViewPListItem = findViewPListItem(j);
        if (findViewPListItem == -1 || findViewPListItem >= this.mViewPListItems.size()) {
            int findExcludePListItem = findExcludePListItem(j);
            if (findExcludePListItem >= 0 && findExcludePListItem < this.mExcludeViewItems.size()) {
                this.mExcludeViewItems.remove(findExcludePListItem);
                return;
            }
            return;
        }
        removePlistItemFromView(findViewPListItem, j);
    }

    public void filter(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            for (int size = this.mViewPListItems.size() - 1; size >= 0; size--) {
                PListItem pListItem = (PListItem) this.mViewPListItems.get(size);
                if (pListItem != null && !pListItem.containsKeyInScreenName(str)) {
                    this.mViewPListItems.remove(size);
                }
            }
            int i = 0;
            while (i < this.mExcludeViewItems.size()) {
                PListItem pListItem2 = (PListItem) this.mExcludeViewItems.get(i);
                if (pListItem2 != null) {
                    if (!pListItem2.containsKeyInScreenName(str)) {
                        this.mExcludeViewItems.remove(i);
                    } else if (this.mViewPListItems.size() < ZMConfiguration.MAX_PLIST_REFRESH_NOW_USER_COUNT) {
                        this.mViewPListItems.add(pListItem2);
                        this.mExcludeViewItems.remove(i);
                    } else {
                        i++;
                    }
                }
            }
            this.mWaitingAdapter.filter(str);
            if (this.isWebinar) {
                this.mPAttendeeListAdapter.filter(str);
            }
        }
    }

    private void updatePlistItem(@NonNull CmmUser cmmUser, @NonNull PListItem pListItem, int i) {
        pListItem.setWebinar(this.isWebinar);
        boolean inSilentMode = cmmUser.inSilentMode();
        int findViewPListItem = findViewPListItem(pListItem.userId);
        if (findViewPListItem < 0) {
            int findExcludePListItem = findExcludePListItem(pListItem.userId);
            if (findExcludePListItem >= 0) {
                if (inSilentMode || i == 1) {
                    this.mExcludeViewItems.remove(findExcludePListItem);
                } else {
                    updatePlistExcludeViewItem(findExcludePListItem, pListItem);
                }
            } else if (!inSilentMode && i != 1) {
                addPlistItemToView(pListItem, cmmUser);
            }
        } else if (inSilentMode || i == 1) {
            removePlistItemFromView(findViewPListItem, pListItem.userId);
        } else {
            updatePlistViewItem(findViewPListItem, pListItem);
        }
    }

    private void updatePlistExcludeViewItem(int i, PListItem pListItem) {
        int size = this.mViewPListItems.size() - 1;
        PListItem pListItem2 = (PListItem) this.mViewPListItems.get(size);
        if (ConfLocalHelper.getPriorityPlistItem(pListItem2) > ConfLocalHelper.getPriorityPlistItem(pListItem)) {
            this.mViewPListItems.set(size, pListItem);
            this.mExcludeViewItems.set(i, pListItem2);
            return;
        }
        this.mExcludeViewItems.set(i, pListItem);
    }

    private void updatePlistViewItem(int i, PListItem pListItem) {
        this.mViewPListItems.set(i, pListItem);
    }

    private void addPlistItemToView(@NonNull PListItem pListItem, @NonNull CmmUser cmmUser) {
        if (this.mViewPListItems.size() >= ZMConfiguration.MAX_PLIST_REFRESH_NOW_USER_COUNT) {
            int priorityPlistItem = ConfLocalHelper.getPriorityPlistItem(pListItem, cmmUser);
            int size = this.mViewPListItems.size() - 1;
            PListItem pListItem2 = (PListItem) this.mViewPListItems.get(size);
            int priorityPlistItem2 = ConfLocalHelper.getPriorityPlistItem(pListItem2);
            if (priorityPlistItem2 > priorityPlistItem) {
                this.mViewPListItems.set(size, pListItem);
                pListItem = pListItem2;
                priorityPlistItem = priorityPlistItem2;
            }
            if (priorityPlistItem == StatusPListItem.Others.ordinal()) {
                this.mExcludeViewItems.add(pListItem);
            } else {
                this.mExcludeViewItems.add(0, pListItem);
            }
        } else {
            this.mViewPListItems.add(pListItem);
        }
    }

    private void removePlistItemFromView(int i, long j) {
        this.mViewPListItems.remove(i);
        if (this.mViewPListItems.size() < ZMConfiguration.MAX_PLIST_REFRESH_NOW_USER_COUNT && !this.mExcludeViewItems.isEmpty()) {
            this.mViewPListItems.add((PListItem) this.mExcludeViewItems.get(0));
            this.mExcludeViewItems.remove(0);
        }
    }

    public void setInSearchProgress(boolean z) {
        this.isInSearchProgress = z;
    }

    public int getCount() {
        return checkCount();
    }

    private int checkCount() {
        int size = this.mViewPListItems.size();
        int count = this.mWaitingAdapter.getCount();
        int count2 = this.mPAttendeeListAdapter.getCount();
        int i = 0;
        if (count > 0) {
            this.mOnHoldLabelPos = 0;
            i = 1;
        } else {
            this.mOnHoldLabelPos = -1;
        }
        int i2 = i + count;
        if (this.isWebinar || count > 0) {
            this.mPListLabelPos = i2;
            i2++;
        } else {
            this.mPListLabelPos = -1;
        }
        if (this.isInSearchProgress || size <= 7) {
            this.mPListSeachPos = -1;
        } else {
            this.mPListSeachPos = i2;
            i2++;
        }
        int i3 = i2 + size;
        if (this.isWebinar || count2 > 0) {
            this.mAttendeeLabelPos = i3;
            i3++;
        } else {
            this.mAttendeeLabelPos = -1;
        }
        return i3 + count2;
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        if (i == this.mOnHoldLabelPos || i == this.mPListLabelPos || i == this.mPListSeachPos || i == this.mAttendeeLabelPos) {
            return Integer.valueOf(i);
        }
        int size = this.mViewPListItems.size();
        int count = this.mWaitingAdapter.getCount();
        int count2 = this.mPAttendeeListAdapter.getCount();
        int i2 = this.mOnHoldLabelPos >= 0 ? i - 1 : i;
        if (count > 0 && i2 < count) {
            return this.mWaitingAdapter.getItem(i2);
        }
        int i3 = i2 - count;
        if (this.mPListLabelPos >= 0) {
            i3--;
        }
        if (this.mPListSeachPos >= 0) {
            i3--;
        }
        if (i3 < size) {
            return this.mViewPListItems.get(i3);
        }
        int i4 = i3 - size;
        if (this.mAttendeeLabelPos >= 0) {
            i4--;
        }
        if (i4 < count2) {
            return this.mPAttendeeListAdapter.getItem(i4);
        }
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        Object item = getItem(i);
        if (item == null) {
            return 0;
        }
        if (item instanceof PListItem) {
            return ((PListItem) item).userId;
        }
        if (item instanceof WaitingListItem) {
            return ((WaitingListItem) item).userId;
        }
        if (item instanceof PAttendeeItem) {
            return ((PAttendeeItem) item).nodeID;
        }
        return 0;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        Object item = getItem(i);
        if (item == null) {
            return null;
        }
        if (item instanceof PListItem) {
            return ((PListItem) item).getView(this.mPListView, this.mContext, view);
        }
        if (item instanceof WaitingListItem) {
            return ((WaitingListItem) item).getView(this.mContext, view);
        }
        if (item instanceof PAttendeeItem) {
            return ((PAttendeeItem) item).getView(this.mContext, view);
        }
        if (i == this.mOnHoldLabelPos) {
            return getOnHoldLabelView(this.mContext, view, viewGroup);
        }
        if (i == this.mPListLabelPos) {
            return getPlistLabelView(this.mContext, view, viewGroup);
        }
        if (i == this.mAttendeeLabelPos) {
            return getPAttendeeListLabelView(this.mContext, view, viewGroup);
        }
        if (i == this.mPListSeachPos) {
            return getSearchDummyView(this.mContext, view, viewGroup);
        }
        return null;
    }

    private int findViewPListItem(long j) {
        Iterator it = this.mViewPListItems.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (((PListItem) it.next()).userId == j) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private int findExcludePListItem(long j) {
        Iterator it = this.mExcludeViewItems.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (((PListItem) it.next()).userId == j) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private View getOnHoldLabelView(@NonNull Context context, View view, ViewGroup viewGroup) {
        String str;
        if (view == null || !"onHoldLabel".equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_plist_cate_label, null);
            view.setTag("onHoldLabel");
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.btn_admit_all);
        textView2.setEnabled(ConfMgr.getInstance().getConfDataHelper().ismEnableAdmitAll());
        textView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ConfMgr.getInstance().admitAllSilentUsersIntoMeeting();
            }
        });
        int count = this.mWaitingAdapter.getCount();
        if (this.isEnableWaitingList) {
            str = context.getString(C4558R.string.zm_lbl_people_in_waiting, new Object[]{Integer.valueOf(count)});
            textView2.setText(C4558R.string.zm_btn_admit_all_39690);
        } else {
            str = context.getString(C4558R.string.zm_lbl_people_on_hold, new Object[]{Integer.valueOf(count)});
            textView2.setText(C4558R.string.zm_btn_take_off_all_39690);
        }
        if (count >= 2) {
            textView2.setVisibility(0);
        } else {
            textView2.setVisibility(8);
        }
        textView.setText(str);
        return view;
    }

    private View getPlistLabelView(@NonNull Context context, View view, ViewGroup viewGroup) {
        String str;
        if (view == null || !"plistLabelView".equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_plist_cate_label, null);
            view.setTag("plistLabelView");
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
        ((TextView) view.findViewById(C4558R.C4560id.btn_admit_all)).setVisibility(8);
        int size = this.mViewPListItems.size() + this.mExcludeViewItems.size();
        if (this.isWebinar) {
            str = context.getString(C4558R.string.zm_lbl_participants_in_meeting, new Object[]{Integer.valueOf(size)});
        } else {
            str = context.getString(C4558R.string.zm_lbl_participants_in_waiting, new Object[]{Integer.valueOf(size)});
        }
        textView.setText(str);
        return view;
    }

    private View getPAttendeeListLabelView(@NonNull Context context, View view, ViewGroup viewGroup) {
        if (view == null || !"pAttendeeListLabelView".equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_plist_cate_label, null);
            view.setTag("pAttendeeListLabelView");
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
        ((TextView) view.findViewById(C4558R.C4560id.btn_admit_all)).setVisibility(8);
        String str = "";
        if (this.isWebinar) {
            str = context.getString(C4558R.string.zm_webinar_txt_attendees, new Object[]{Integer.valueOf(ConfMgr.getInstance().getViewOnlyUserCount())});
        }
        textView.setText(str);
        return view;
    }

    private View getSearchDummyView(Context context, View view, ViewGroup viewGroup) {
        if (view == null || !"searchDummyView".equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_plist_search_dummy, null);
            view.setTag("searchDummyView");
        }
        view.findViewById(C4558R.C4560id.edtSearchDummy).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ZMActivity zMActivity = (ZMActivity) PListAdapter.this.mContext;
                if (zMActivity != null) {
                    zMActivity.onSearchRequested();
                }
            }
        });
        return view;
    }

    public void sortAll() {
        sortPanelist();
        sortAttendee();
    }

    public void sortPanelist() {
        if (VERSION.SDK_INT <= 19 || this.mViewPListItems.size() > ZMConfiguration.MAX_PLIST_ATTENDEES_CHANGE_SORT) {
            PListItemNewComparator.updatePlistItems(this.mViewPListItems);
            Collections.sort(this.mViewPListItems, new PListItemNewComparator(CompatUtils.getLocalDefault()));
            return;
        }
        Collections.sort(this.mViewPListItems, new PListItemComparator(CompatUtils.getLocalDefault()));
    }

    public void sortAttendee() {
        this.mPAttendeeListAdapter.sort();
    }
}
