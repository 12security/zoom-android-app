package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: com.zipow.videobox.view.mm.MMChatBuddiesGridView */
public class MMChatBuddiesGridView extends GridView implements OnScrollListener {
    private static final String TAG = "MMChatBuddiesGridView";
    private BuddyOperationListener buddyOperationListener;
    /* access modifiers changed from: private */
    public MMChatBuddiesGridViewAdapter mAdapter;
    private boolean mIsGroup = false;
    private boolean mIsScrolled = false;

    /* renamed from: com.zipow.videobox.view.mm.MMChatBuddiesGridView$BuddyOperationListener */
    public interface BuddyOperationListener {
        void onClickAddBtn();

        void onClickBuddyItem(MMBuddyItem mMBuddyItem);

        void onRemoveBuddy(MMBuddyItem mMBuddyItem);
    }

    public MMChatBuddiesGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public MMChatBuddiesGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MMChatBuddiesGridView(Context context) {
        super(context);
        initView(context);
    }

    public void setMax(int i) {
        this.mAdapter.setMax(i);
    }

    public void setBuddyOperationListener(BuddyOperationListener buddyOperationListener2) {
        this.buddyOperationListener = buddyOperationListener2;
    }

    private void initView(Context context) {
        setNumColumns(4);
        this.mAdapter = new MMChatBuddiesGridViewAdapter(context, this);
        if (isInEditMode()) {
            _editmode_loadAllBuddies();
        }
        setAdapter(this.mAdapter);
        setOnScrollListener(this);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
                if (MMChatBuddiesGridView.this.mAdapter.ismIsRobot() || !MMChatBuddiesGridView.this.mAdapter.isRemoveMode() || motionEvent.getAction() == 0 || MMChatBuddiesGridView.this.isClickOnItems(motionEvent)) {
                    return false;
                }
                MMChatBuddiesGridView.this.setIsRemoveMode(false);
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mAdapter.getMax() != 0) {
            super.onMeasure(i, MeasureSpec.makeMeasureSpec(UIUtil.dip2px(getContext(), 200000.0f), Integer.MIN_VALUE));
        } else {
            super.onMeasure(i, i2);
        }
    }

    /* access modifiers changed from: private */
    public boolean isClickOnItems(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            int top = childAt.getTop();
            int left = childAt.getLeft();
            int right = childAt.getRight();
            int bottom = childAt.getBottom();
            if (x > ((float) left) && x < ((float) right) && y > ((float) top) && y < ((float) bottom)) {
                return true;
            }
        }
        return false;
    }

    private void _editmode_loadAllBuddies() {
        int i = 0;
        while (i < 3) {
            MMBuddyItem mMBuddyItem = new MMBuddyItem();
            StringBuilder sb = new StringBuilder();
            sb.append("Buddy ");
            i++;
            sb.append(i);
            mMBuddyItem.setScreenName(sb.toString());
            this.mAdapter.addItem(mMBuddyItem);
        }
        this.mAdapter.setGroupOperatorable(true);
    }

    public void setIsRemoveMode(boolean z) {
        this.mAdapter.setIsRemoveMode(z);
        this.mAdapter.notifyDataSetChanged();
    }

    public void updateBuddyByJid(String str) {
        boolean z = false;
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            Object item = this.mAdapter.getItem(i);
            if (item instanceof MMBuddyItem) {
                MMBuddyItem mMBuddyItem = (MMBuddyItem) item;
                if (TextUtils.equals(str, mMBuddyItem.buddyJid)) {
                    mMBuddyItem.updateInfo();
                    z = true;
                }
            }
        }
        if (z) {
            this.mAdapter.sort();
            notifyDataSetChanged();
        }
    }

    public void loadAllBuddies(IMAddrBookItem iMAddrBookItem, String str, String str2) {
        MMBuddyItem mMBuddyItem;
        this.mAdapter.clear();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!StringUtil.isEmptyOrNull(str2)) {
                this.mIsGroup = true;
                ZoomGroup groupById = zoomMessenger.getGroupById(str2);
                if (groupById != null) {
                    String groupOwner = groupById.getGroupOwner();
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        this.mAdapter.setGroupOperatorable(groupById.isGroupOperatorable());
                        List groupAdmins = groupById.getGroupAdmins();
                        if (!StringUtil.isEmptyOrNull(str2)) {
                            if (groupAdmins == null) {
                                groupAdmins = new ArrayList();
                            }
                            if (CollectionsUtil.isListEmpty(groupAdmins)) {
                                groupAdmins.add(groupOwner);
                            }
                        }
                        this.mAdapter.setGroupAdmin(groupAdmins);
                        int buddyCount = groupById.getBuddyCount();
                        for (int i = 0; i < buddyCount; i++) {
                            ZoomBuddy buddyAt = groupById.getBuddyAt(i);
                            if (buddyAt != null) {
                                MMBuddyItem mMBuddyItem2 = new MMBuddyItem(buddyAt, IMAddrBookItem.fromZoomBuddy(buddyAt));
                                if (StringUtil.isSameString(buddyAt.getJid(), myself.getJid())) {
                                    mMBuddyItem2.setIsMySelf(true);
                                    String screenName = myself.getScreenName();
                                    if (!StringUtil.isEmptyOrNull(screenName)) {
                                        mMBuddyItem2.setScreenName(screenName);
                                    }
                                }
                                if (StringUtil.isSameString(groupOwner, buddyAt.getJid())) {
                                    mMBuddyItem2.setSortKey("!");
                                } else {
                                    mMBuddyItem2.setSortKey(SortUtil.getSortKey(mMBuddyItem2.screenName, CompatUtils.getLocalDefault()));
                                }
                                this.mAdapter.addItem(mMBuddyItem2);
                                int max = this.mAdapter.getMax();
                                if (max > 0 && this.mAdapter.getCount() >= max) {
                                    break;
                                }
                            }
                        }
                        this.mAdapter.sort();
                    }
                }
            } else {
                this.mIsGroup = false;
                this.mAdapter.setGroupOperatorable(false);
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                if (buddyWithJID != null) {
                    mMBuddyItem = new MMBuddyItem(buddyWithJID, iMAddrBookItem);
                } else {
                    mMBuddyItem = new MMBuddyItem(iMAddrBookItem);
                }
                if (mMBuddyItem.isRobot()) {
                    this.mAdapter.setmIsRobot(true);
                }
                this.mAdapter.addItem(mMBuddyItem);
            }
        }
    }

    public void notifyDataSetChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    @Nullable
    public List<MMBuddyItem> getAllItems() {
        MMChatBuddiesGridViewAdapter mMChatBuddiesGridViewAdapter = this.mAdapter;
        if (mMChatBuddiesGridViewAdapter == null) {
            return null;
        }
        return mMChatBuddiesGridViewAdapter.getBuddyItems();
    }

    public void onClickAddBtn() {
        if (this.mAdapter.isRemoveMode()) {
            setIsRemoveMode(false);
            return;
        }
        BuddyOperationListener buddyOperationListener2 = this.buddyOperationListener;
        if (buddyOperationListener2 != null) {
            buddyOperationListener2.onClickAddBtn();
        }
        ZoomLogEventTracking.eventTrackAddBuddy(this.mIsGroup);
    }

    public void onClickRemoveBtn() {
        this.mAdapter.setIsRemoveMode(true);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onClickBuddyRemoveBtn(MMBuddyItem mMBuddyItem) {
        BuddyOperationListener buddyOperationListener2 = this.buddyOperationListener;
        if (buddyOperationListener2 != null) {
            buddyOperationListener2.onRemoveBuddy(mMBuddyItem);
        }
    }

    public void onClickBuddyItem(MMBuddyItem mMBuddyItem) {
        if (this.mAdapter.isRemoveMode()) {
            setIsRemoveMode(false);
            return;
        }
        BuddyOperationListener buddyOperationListener2 = this.buddyOperationListener;
        if (buddyOperationListener2 != null) {
            buddyOperationListener2.onClickBuddyItem(mMBuddyItem);
        }
    }

    private void refreshVCardsOfVisibleItems(int i, int i2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList = new ArrayList();
            while (i < i2 + 1) {
                Object item = this.mAdapter.getItem(i);
                if (item != null && (item instanceof MMBuddyItem)) {
                    arrayList.add(((MMBuddyItem) item).getBuddyJid());
                }
                i++;
            }
            if (arrayList.size() > 0) {
                zoomMessenger.refreshBuddyVCards(arrayList);
            }
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        this.mIsScrolled = true;
        if (i == 0) {
            refreshVCardsOfVisibleItems(absListView.getFirstVisiblePosition(), absListView.getLastVisiblePosition());
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (!this.mIsScrolled) {
            refreshVCardsOfVisibleItems(i, i2 + i);
        }
    }
}
