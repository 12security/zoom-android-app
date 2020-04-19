package com.zipow.videobox.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfChatMessage;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.util.ArrayList;
import java.util.List;

public class ConfChatListViewOld extends ListView {
    private static final int TIME_NEW_MESSAGE_REFRESH_DELAY = 1000;
    /* access modifiers changed from: private */
    public ConfChatListAdapter mAdapter;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mNewMsgRefreshTask;
    /* access modifiers changed from: private */
    @NonNull
    public List<String> mPendingItems = new ArrayList();

    public ConfChatListViewOld(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ConfChatListViewOld(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfChatListViewOld(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mAdapter = new ConfChatListAdapter(getContext());
        if (isInEditMode()) {
            _editmode_loadAllMessageItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
    }

    private void _editmode_loadAllMessageItems(@NonNull ConfChatListAdapter confChatListAdapter) {
        for (int i = 0; i < 5; i++) {
            ConfChatItem confChatItem = new ConfChatItem();
            int i2 = i % 2;
            confChatItem.senderName = i2 == 0 ? "Zoom" : "Reed Yang";
            confChatItem.content = "Hi, Zoom! I like you!";
            confChatItem.time = System.currentTimeMillis();
            confChatItem.type = i2 == 0 ? 0 : 1;
            confChatListAdapter.addItem(confChatItem);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int lastVisiblePosition = getLastVisiblePosition();
        super.onLayout(z, i, i2, i3, i4);
        if (z && lastVisiblePosition >= 0) {
            post(new Runnable() {
                public void run() {
                    ConfChatListViewOld.this.scrollToBottom(true);
                }
            });
        }
    }

    public void loadMessages(long j) {
        ConfMgr instance = ConfMgr.getInstance();
        String[] chatMessagesByUser = instance.getChatMessagesByUser(j, false);
        ConfChatItem confChatItem = null;
        if (chatMessagesByUser != null) {
            for (int i = 0; i < chatMessagesByUser.length; i++) {
                ConfChatMessage chatMessageItemByID = instance.getChatMessageItemByID(chatMessagesByUser[i]);
                if (chatMessageItemByID != null) {
                    instance.setChatMessageAsReaded(chatMessagesByUser[i]);
                    if (confChatItem == null || chatMessageItemByID.getTimeStamp() - confChatItem.time > 60000 || confChatItem.sender != chatMessageItemByID.getSenderID() || chatMessageItemByID.getReceiverID() != confChatItem.receiver) {
                        if (confChatItem != null) {
                            this.mAdapter.addItem(confChatItem);
                        }
                        confChatItem = new ConfChatItem(chatMessageItemByID);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(confChatItem.content);
                        sb.append(FontStyleHelper.SPLITOR);
                        sb.append(chatMessageItemByID.getMessageContent());
                        confChatItem.content = sb.toString();
                    }
                }
            }
        }
        if (confChatItem != null) {
            this.mAdapter.addItem(confChatItem);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        addChatMessage(str, confStatusObj != null && confStatusObj.isMyself(j));
    }

    private void addChatMessage(String str, boolean z) {
        this.mPendingItems.add(str);
        Runnable runnable = this.mNewMsgRefreshTask;
        if (runnable == null) {
            this.mNewMsgRefreshTask = new Runnable() {
                /* JADX WARNING: Removed duplicated region for block: B:21:0x008c  */
                /* JADX WARNING: Removed duplicated region for block: B:28:0x001a A[SYNTHETIC] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r9 = this;
                        com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
                        com.zipow.videobox.view.ConfChatListViewOld r1 = com.zipow.videobox.view.ConfChatListViewOld.this
                        java.util.List r1 = r1.mPendingItems
                        int r1 = r1.size()
                        if (r1 <= 0) goto L_0x00aa
                        com.zipow.videobox.view.ConfChatListViewOld r1 = com.zipow.videobox.view.ConfChatListViewOld.this
                        java.util.List r1 = r1.mPendingItems
                        java.util.Iterator r1 = r1.iterator()
                    L_0x001a:
                        boolean r2 = r1.hasNext()
                        r3 = 0
                        if (r2 == 0) goto L_0x009c
                        java.lang.Object r2 = r1.next()
                        java.lang.String r2 = (java.lang.String) r2
                        com.zipow.videobox.confapp.ConfChatMessage r2 = r0.getChatMessageItemByID(r2)
                        if (r2 != 0) goto L_0x002e
                        return
                    L_0x002e:
                        com.zipow.videobox.view.ConfChatListViewOld r4 = com.zipow.videobox.view.ConfChatListViewOld.this
                        com.zipow.videobox.view.ConfChatListAdapter r4 = r4.mAdapter
                        int r4 = r4.getCount()
                        if (r4 <= 0) goto L_0x0089
                        com.zipow.videobox.view.ConfChatListViewOld r5 = com.zipow.videobox.view.ConfChatListViewOld.this
                        com.zipow.videobox.view.ConfChatListAdapter r5 = r5.mAdapter
                        int r4 = r4 + -1
                        java.lang.Object r4 = r5.getItem(r4)
                        com.zipow.videobox.view.ConfChatItem r4 = (com.zipow.videobox.view.ConfChatItem) r4
                        if (r4 == 0) goto L_0x0089
                        long r5 = r2.getTimeStamp()
                        long r7 = r4.time
                        long r5 = r5 - r7
                        r7 = 60000(0xea60, double:2.9644E-319)
                        int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                        if (r5 > 0) goto L_0x0089
                        long r5 = r4.sender
                        long r7 = r2.getSenderID()
                        int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                        if (r5 != 0) goto L_0x0089
                        long r5 = r4.receiver
                        long r7 = r2.getReceiverID()
                        int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                        if (r5 != 0) goto L_0x0089
                        java.lang.StringBuilder r5 = new java.lang.StringBuilder
                        r5.<init>()
                        java.lang.String r6 = r4.content
                        r5.append(r6)
                        java.lang.String r6 = "\n"
                        r5.append(r6)
                        java.lang.String r6 = r2.getMessageContent()
                        r5.append(r6)
                        java.lang.String r5 = r5.toString()
                        r4.content = r5
                        goto L_0x008a
                    L_0x0089:
                        r3 = 1
                    L_0x008a:
                        if (r3 == 0) goto L_0x001a
                        com.zipow.videobox.view.ConfChatItem r3 = new com.zipow.videobox.view.ConfChatItem
                        r3.<init>(r2)
                        com.zipow.videobox.view.ConfChatListViewOld r2 = com.zipow.videobox.view.ConfChatListViewOld.this
                        com.zipow.videobox.view.ConfChatListAdapter r2 = r2.mAdapter
                        r2.addItem(r3)
                        goto L_0x001a
                    L_0x009c:
                        com.zipow.videobox.view.ConfChatListViewOld r0 = com.zipow.videobox.view.ConfChatListViewOld.this
                        com.zipow.videobox.view.ConfChatListAdapter r0 = r0.mAdapter
                        r0.notifyDataSetChanged()
                        com.zipow.videobox.view.ConfChatListViewOld r0 = com.zipow.videobox.view.ConfChatListViewOld.this
                        r0.scrollToBottom(r3)
                    L_0x00aa:
                        com.zipow.videobox.view.ConfChatListViewOld r0 = com.zipow.videobox.view.ConfChatListViewOld.this
                        java.util.List r0 = r0.mPendingItems
                        r0.clear()
                        com.zipow.videobox.view.ConfChatListViewOld r0 = com.zipow.videobox.view.ConfChatListViewOld.this
                        android.os.Handler r0 = r0.mHandler
                        com.zipow.videobox.view.ConfChatListViewOld r1 = com.zipow.videobox.view.ConfChatListViewOld.this
                        java.lang.Runnable r1 = r1.mNewMsgRefreshTask
                        r2 = 1000(0x3e8, double:4.94E-321)
                        r0.postDelayed(r1, r2)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ConfChatListViewOld.C34502.run():void");
                }
            };
            this.mHandler.post(this.mNewMsgRefreshTask);
        } else if (z) {
            this.mHandler.removeCallbacks(runnable);
            this.mNewMsgRefreshTask.run();
            this.mHandler.postDelayed(this.mNewMsgRefreshTask, 1000);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        Runnable runnable = this.mNewMsgRefreshTask;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mNewMsgRefreshTask = null;
        }
        super.onDetachedFromWindow();
    }

    public void scrollToBottom(boolean z) {
        int lastVisiblePosition = getLastVisiblePosition();
        final int count = getCount() - 1;
        if (z) {
            if (count == lastVisiblePosition && getChildCount() == 1) {
                post(new Runnable() {
                    public void run() {
                        int i = 0;
                        View childAt = ConfChatListViewOld.this.getChildAt(0);
                        if (childAt != null) {
                            if (childAt.getHeight() > ConfChatListViewOld.this.getHeight()) {
                                i = ConfChatListViewOld.this.getHeight() - childAt.getHeight();
                            }
                            ConfChatListViewOld.this.setSelectionFromTop(count, i);
                        }
                    }
                });
            } else {
                setSelection(count);
            }
        } else if (count - lastVisiblePosition >= 5) {
        } else {
            if (count == lastVisiblePosition && getChildCount() == 1) {
                post(new Runnable() {
                    public void run() {
                        int i = 0;
                        View childAt = ConfChatListViewOld.this.getChildAt(0);
                        if (childAt != null) {
                            if (childAt.getHeight() > ConfChatListViewOld.this.getHeight()) {
                                i = ConfChatListViewOld.this.getHeight() - childAt.getHeight();
                            }
                            if (VERSION.SDK_INT >= 16) {
                                ConfChatListViewOld.this.smoothScrollToPositionFromTop(count, i);
                            } else {
                                ConfChatListViewOld.this.setSelectionFromTop(count, i);
                            }
                        }
                    }
                });
            } else if (VERSION.SDK_INT >= 16) {
                smoothScrollToPosition(count);
            } else {
                setSelection(count);
            }
        }
    }
}
