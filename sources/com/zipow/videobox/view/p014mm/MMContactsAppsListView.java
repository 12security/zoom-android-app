package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.eventbus.ZMContactsBuddLongClickyEvent;
import com.zipow.videobox.fragment.IMAddrBookListFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.widget.QuickSearchListView;

/* renamed from: com.zipow.videobox.view.mm.MMContactsAppsListView */
public class MMContactsAppsListView extends QuickSearchListView implements OnItemClickListener, OnScrollListener, OnItemLongClickListener {
    private static final int MSG_REFRESH_BUDDY_VCARDS = 1;
    private static final String TAG = "MMContactsAppsListView";
    private MMContactsAppAdapter mAdapter;
    @NonNull
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                MMContactsAppsListView.this.refreshBuddyVcards();
                sendEmptyMessageDelayed(1, 2000);
            }
        }
    };
    private IMAddrBookListFragment mParentFragment;

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public MMContactsAppsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMContactsAppsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMContactsAppsListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mAdapter = new MMContactsAppAdapter(getContext());
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        getListView().setOnScrollListener(this);
        setOnItemLongClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeMessages(1);
        super.onDetachedFromWindow();
    }

    public void filter(String str) {
        this.mAdapter.filter(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004f A[EDGE_INSN: B:30:0x004f->B:22:0x004f ?: BREAK  
    EDGE_INSN: B:30:0x004f->B:22:0x004f ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBuddyInfoUpdate(@androidx.annotation.Nullable java.util.List<java.lang.String> r4, @androidx.annotation.Nullable java.util.List<java.lang.String> r5) {
        /*
            r3 = this;
            com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance r0 = com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.getInsatance()
            boolean r1 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r4)
            r2 = 1
            if (r1 != 0) goto L_0x0029
            java.util.Iterator r4 = r4.iterator()
        L_0x000f:
            boolean r1 = r4.hasNext()
            if (r1 == 0) goto L_0x0029
            java.lang.Object r1 = r4.next()
            java.lang.String r1 = (java.lang.String) r1
            com.zipow.videobox.view.IMAddrBookItem r1 = r0.getBuddyByJid(r1)
            if (r1 == 0) goto L_0x000f
            boolean r1 = r1.getIsRobot()
            if (r1 == 0) goto L_0x000f
            r4 = 1
            goto L_0x002a
        L_0x0029:
            r4 = 0
        L_0x002a:
            if (r4 != 0) goto L_0x004f
            boolean r1 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r5)
            if (r1 != 0) goto L_0x004f
            java.util.Iterator r5 = r5.iterator()
        L_0x0036:
            boolean r1 = r5.hasNext()
            if (r1 == 0) goto L_0x004f
            java.lang.Object r1 = r5.next()
            java.lang.String r1 = (java.lang.String) r1
            com.zipow.videobox.view.IMAddrBookItem r1 = r0.getBuddyByJid(r1)
            if (r1 == 0) goto L_0x0036
            boolean r1 = r1.getIsRobot()
            if (r1 == 0) goto L_0x0036
            r4 = 1
        L_0x004f:
            if (r4 == 0) goto L_0x0054
            r3.refreshAllData()
        L_0x0054:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMContactsAppsListView.onBuddyInfoUpdate(java.util.List, java.util.List):void");
    }

    public void refreshAllData() {
        this.mAdapter.clearAll();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddyGroup buddyGroupByType = zoomMessenger.getBuddyGroupByType(61);
            if (buddyGroupByType != null) {
                int buddyCount = buddyGroupByType.getBuddyCount();
                for (int i = 0; i < buddyCount; i++) {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyGroupByType.getBuddyAt(i));
                    if (fromZoomBuddy != null) {
                        this.mAdapter.addOrUpdateItem(fromZoomBuddy);
                    }
                }
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setParentFragment(IMAddrBookListFragment iMAddrBookListFragment) {
        this.mParentFragment = iMAddrBookListFragment;
    }

    public void setEmptyView(View view) {
        getListView().setEmptyView(view);
    }

    public int getCount() {
        return this.mAdapter.getCount();
    }

    public boolean hasApps() {
        return this.mAdapter.hasApps();
    }

    public boolean isParentFragmentResumed() {
        IMAddrBookListFragment iMAddrBookListFragment = this.mParentFragment;
        if (iMAddrBookListFragment == null) {
            return false;
        }
        return iMAddrBookListFragment.isResumed();
    }

    /* access modifiers changed from: private */
    public void refreshBuddyVcards() {
        List waitRefreshJids = this.mAdapter.getWaitRefreshJids();
        HashSet hashSet = new HashSet();
        int childCount = getListView().getChildCount() * 2;
        for (int size = waitRefreshJids.size() - 1; size >= 0; size--) {
            hashSet.add(waitRefreshJids.get(size));
            if (hashSet.size() >= childCount) {
                break;
            }
        }
        if (hashSet.size() != 0) {
            this.mAdapter.clearWaitRefreshJids();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(hashSet);
                zoomMessenger.refreshBuddyVCards(arrayList);
            }
        }
    }

    private void onItemClick(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            MMChatActivity.showAsOneToOneChat((ZMActivity) getContext(), iMAddrBookItem, iMAddrBookItem.getJid());
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition instanceof IMAddrBookItem) {
            onItemClick((IMAddrBookItem) itemAtPosition);
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 2) {
            this.mHandler.removeMessages(1);
        } else if (!this.mHandler.hasMessages(1)) {
            this.mHandler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (!(itemAtPosition instanceof IMAddrBookItem)) {
            return false;
        }
        EventBus.getDefault().post(new ZMContactsBuddLongClickyEvent((IMAddrBookItem) itemAtPosition, null));
        return true;
    }
}
