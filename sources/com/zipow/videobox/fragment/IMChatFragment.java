package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.IMSession;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMBuddyItem;
import com.zipow.videobox.view.IMChatView;
import com.zipow.videobox.view.IMChatView.Listener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;

public class IMChatFragment extends ZMFragment implements Listener {
    public static final String ARG_BUDDYITEM = "buddyItem";
    public static final String ARG_MYNAME = "myName";
    private static final String TAG = "IMChatFragment";
    @Nullable
    private IMBuddyItem mBuddyItem;
    private IMChatView mChatView;
    @Nullable
    private String mMyName;

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mChatView = new IMChatView(getActivity());
        this.mChatView.setListener(this);
        return this.mChatView;
    }

    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        boolean z = false;
        if (arguments != null) {
            this.mBuddyItem = (IMBuddyItem) arguments.getSerializable("buddyItem");
            this.mMyName = arguments.getString("myName");
            IMBuddyItem iMBuddyItem = this.mBuddyItem;
            if (iMBuddyItem == null || iMBuddyItem.userId == null || this.mBuddyItem.screenName == null || this.mMyName == null) {
                IMBuddyItem iMBuddyItem2 = this.mBuddyItem;
                return;
            }
            if (getUnreadMessageCount(this.mBuddyItem.userId) > 0) {
                z = true;
            }
            this.mChatView.reloadData(this.mBuddyItem, this.mMyName);
        }
        if (z) {
            NotificationMgr.removeMessageNotificationMM(getActivity());
        }
        this.mChatView.scrollToBottom(true);
    }

    private int getUnreadMessageCount(String str) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper == null) {
            return 0;
        }
        IMSession sessionBySessionName = iMHelper.getSessionBySessionName(str);
        if (sessionBySessionName == null) {
            return 0;
        }
        return sessionBySessionName.getUnreadMessageCount();
    }

    public void onCallPlistChanged() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            this.mChatView.onCallPlistChanged();
        }
    }

    public void onCallStatusChanged(long j) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            this.mChatView.onCallStatusChanged(j);
        }
    }

    public void onIMReceived(@NonNull IMMessage iMMessage) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            this.mChatView.onIMReceived(iMMessage);
        }
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            this.mChatView.onIMBuddyPresence(buddyItem);
        }
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            this.mChatView.onIMBuddyPic(buddyItem);
        }
    }

    public void onWebLogin(long j) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isActive()) {
            this.mChatView.onWebLogin(j);
        }
    }

    @Nullable
    public Object getChatToUserId() {
        IMBuddyItem iMBuddyItem = this.mBuddyItem;
        if (iMBuddyItem != null) {
            return iMBuddyItem.userId;
        }
        return null;
    }

    public void onClickButtonBack() {
        FragmentActivity activity = getActivity();
        if (activity != null && !UIMgr.isLargeMode(activity)) {
            activity.finish();
        }
    }
}
