package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.IMChatFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.view.IMBuddyItem;
import p021us.zoom.androidlib.app.ZMActivity;

public class IMChatActivity extends ZMActivity implements IPTUIListener, IIMListener {
    public static final String INTENT_EXTRA_BUDDYITEM = "buddyItem";
    public static final String INTENT_EXTRA_MYNAME = "myName";
    private static final String TAG = "IMChatActivity";
    private IMBuddyItem mBuddyItem;
    private String mMyName;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onIMBuddySort() {
    }

    public void onIMLocalStatusChanged(int i) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public void onSubscriptionRequest() {
    }

    public void onSubscriptionUpdate() {
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper == null || !iMHelper.isIMSignedOn()) {
            finish();
            return;
        }
        Intent intent = getIntent();
        this.mBuddyItem = (IMBuddyItem) intent.getSerializableExtra("buddyItem");
        this.mMyName = intent.getStringExtra("myName");
        IMBuddyItem iMBuddyItem = this.mBuddyItem;
        if (iMBuddyItem == null || iMBuddyItem.userId == null || this.mBuddyItem.screenName == null || this.mMyName == null) {
            IMBuddyItem iMBuddyItem2 = this.mBuddyItem;
            finish();
            return;
        }
        if (bundle == null) {
            IMChatFragment iMChatFragment = new IMChatFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("buddyItem", this.mBuddyItem);
            bundle2.putString("myName", this.mMyName);
            iMChatFragment.setArguments(bundle2);
            getSupportFragmentManager().beginTransaction().add(16908290, iMChatFragment, IMChatFragment.class.getName()).commit();
        }
        PTUI.getInstance().addPTUIListener(this);
        PTUI.getInstance().addIMListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            PTUI.getInstance().removePTUIListener(this);
            PTUI.getInstance().removeIMListener(this);
        }
    }

    public void onResume() {
        super.onResume();
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper == null || iMHelper.getIMLocalStatus() == 0) {
            finish();
        }
    }

    @Nullable
    private IMChatFragment getChatFragment() {
        return (IMChatFragment) getSupportFragmentManager().findFragmentByTag(IMChatFragment.class.getName());
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            sinkWebLogin(j);
        } else if (i != 14) {
            switch (i) {
                case 22:
                    sinkCallStatusChanged(j);
                    return;
                case 23:
                    sinkCallPlistChanged();
                    return;
                default:
                    return;
            }
        } else {
            sinkIMLogout(j);
        }
    }

    private void sinkCallPlistChanged() {
        if (isActive()) {
            IMChatFragment chatFragment = getChatFragment();
            if (chatFragment != null) {
                chatFragment.onCallPlistChanged();
            }
        }
    }

    private void sinkCallStatusChanged(long j) {
        if (isActive()) {
            IMChatFragment chatFragment = getChatFragment();
            if (chatFragment != null) {
                chatFragment.onCallStatusChanged(j);
            }
        }
    }

    private void sinkIMLogout(long j) {
        if (isActive() && j == 0) {
            finish();
        }
    }

    private void sinkWebLogin(long j) {
        if (isActive() && j == 0) {
            IMChatFragment chatFragment = getChatFragment();
            if (chatFragment != null) {
                chatFragment.onWebLogin(j);
            }
        }
    }

    public void onIMReceived(@NonNull IMMessage iMMessage) {
        if (isActive()) {
            IMChatFragment chatFragment = getChatFragment();
            if (chatFragment != null) {
                chatFragment.onIMReceived(iMMessage);
            }
        }
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
        if (isActive()) {
            IMChatFragment chatFragment = getChatFragment();
            if (chatFragment != null) {
                chatFragment.onIMBuddyPresence(buddyItem);
            }
        }
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
        if (isActive()) {
            IMChatFragment chatFragment = getChatFragment();
            if (chatFragment != null) {
                chatFragment.onIMBuddyPic(buddyItem);
            }
        }
    }
}
