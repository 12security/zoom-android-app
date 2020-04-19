package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.videomeetings.C4558R;

public class OnSilentView extends LinearLayout {
    private boolean isWaitingRoom;
    private OnHoldView vOnHoldView;
    private WaitingRoomView vWaitingRoomView;

    public OnSilentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public OnSilentView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.vOnHoldView = (OnHoldView) findViewById(C4558R.C4560id.vOnHoldView);
        this.vWaitingRoomView = (WaitingRoomView) findViewById(C4558R.C4560id.vWaitingRoomView);
        updateData();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_on_silent_view, this);
    }

    public void setTitlePadding(int i, int i2, int i3, int i4) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (confContext.supportPutUserinWaitingListUponEntryFeature()) {
                WaitingRoomView waitingRoomView = this.vWaitingRoomView;
                if (waitingRoomView != null) {
                    waitingRoomView.setTitlePadding(i, i2, i3, i4);
                }
            } else {
                OnHoldView onHoldView = this.vOnHoldView;
                if (onHoldView != null) {
                    onHoldView.setTitlePadding(i, i2, i3, i4);
                }
            }
        }
    }

    public void updateData() {
        if (!isInEditMode()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && this.vWaitingRoomView != null && this.vOnHoldView != null) {
                if (confContext.supportPutUserinWaitingListUponEntryFeature()) {
                    this.isWaitingRoom = true;
                    this.vWaitingRoomView.setVisibility(0);
                    this.vOnHoldView.setVisibility(8);
                    this.vWaitingRoomView.updateData();
                } else {
                    this.isWaitingRoom = false;
                    this.vWaitingRoomView.setVisibility(8);
                    this.vOnHoldView.setVisibility(0);
                    this.vOnHoldView.updateData();
                }
            }
        }
    }

    public void hideWaitingRoomSignInBtn() {
        if (!isInEditMode()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (!(confContext == null || this.vWaitingRoomView == null || !confContext.supportPutUserinWaitingListUponEntryFeature())) {
                this.vWaitingRoomView.hideWaitingRoomSignInBtn();
            }
        }
    }

    public void refreshMessage() {
        if (this.isWaitingRoom) {
            int[] unreadChatMessageIndexes = ConfMgr.getInstance().getUnreadChatMessageIndexes();
            if (unreadChatMessageIndexes != null) {
                this.vWaitingRoomView.setUnreadMsgCount(unreadChatMessageIndexes.length);
            }
        }
    }
}
