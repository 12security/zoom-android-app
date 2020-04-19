package com.zipow.videobox.ptapp.delegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.ptapp.PTUI.IPTCommonEventListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.IPresentToRoomStatusListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class PTUIDelegation {
    public static final int PT_COMMON_EVENT_CALL_ACCEPTED = 1;
    public static final int PT_COMMON_EVENT_CALL_DECLINED = 2;
    public static final int PT_CUSTOM_EVENT_CALL_ERROR = 1;
    private static final String TAG = "PTUIDelegation";
    @Nullable
    private static PTUIDelegation instance;
    @NonNull
    private ListenerList mIMListenerList = new ListenerList();
    @NonNull
    private ListenerList mPTCommonEventListenerList = new ListenerList();
    @NonNull
    private ListenerList mPTListenerList = new ListenerList();
    @NonNull
    private ListenerList mPresentToRoomStatusListener = new ListenerList();

    @Retention(RetentionPolicy.SOURCE)
    public @interface PTCustomEvent {
    }

    private PTUIDelegation() {
    }

    @NonNull
    public static synchronized PTUIDelegation getInstance() {
        PTUIDelegation pTUIDelegation;
        synchronized (PTUIDelegation.class) {
            if (instance == null) {
                instance = new PTUIDelegation();
            }
            pTUIDelegation = instance;
        }
        return pTUIDelegation;
    }

    public void addPTUIListener(@Nullable IPTUIListener iPTUIListener) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            PTUI.getInstance().addPTUIListener(iPTUIListener);
        } else if (iPTUIListener != null) {
            IListener[] all = this.mPTListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i].getClass() == iPTUIListener.getClass()) {
                    removePTUIListener((IPTUIListener) all[i]);
                }
            }
            this.mPTListenerList.add(iPTUIListener);
        }
    }

    public void removePTUIListener(IPTUIListener iPTUIListener) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            PTUI.getInstance().removePTUIListener(iPTUIListener);
        } else {
            this.mPTListenerList.remove(iPTUIListener);
        }
    }

    public void addIMListener(@Nullable IIMListener iIMListener) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            PTUI.getInstance().addIMListener(iIMListener);
        } else if (iIMListener != null) {
            IListener[] all = this.mIMListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i].getClass() == iIMListener.getClass()) {
                    removeIMListener((IIMListener) all[i]);
                }
            }
            this.mIMListenerList.add(iIMListener);
        }
    }

    public void removeIMListener(IIMListener iIMListener) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            PTUI.getInstance().removeIMListener(iIMListener);
        } else {
            this.mIMListenerList.remove(iIMListener);
        }
    }

    public void addPresentToRoomStatusListener(@Nullable IPresentToRoomStatusListener iPresentToRoomStatusListener) {
        if (iPresentToRoomStatusListener != null) {
            this.mPresentToRoomStatusListener.add(iPresentToRoomStatusListener);
        }
    }

    public void removePresentToRoomStatusListener(IPresentToRoomStatusListener iPresentToRoomStatusListener) {
        this.mPresentToRoomStatusListener.remove(iPresentToRoomStatusListener);
    }

    public void addPTCommonEventListener(@Nullable IPTCommonEventListener iPTCommonEventListener) {
        if (iPTCommonEventListener != null) {
            this.mPTCommonEventListenerList.add(iPTCommonEventListener);
        }
    }

    public void removePTCommonEventListener(IPTCommonEventListener iPTCommonEventListener) {
        this.mPTCommonEventListenerList.remove(iPTCommonEventListener);
    }

    public void dispatchPTAppEvent(int i, long j) {
        if (i != 8) {
            switch (i) {
                case 0:
                    onWebLogin(j);
                    break;
                case 1:
                    onWebLogout(j);
                    break;
            }
        } else {
            onIMLogin(j);
        }
        IListener[] all = this.mPTListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPTUIListener) iListener).onPTAppEvent(i, j);
            }
        }
    }

    public void dispatchPTAppCustomEvent(int i, long j) {
        IListener[] all = this.mPTListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPTUIListener) iListener).onPTAppCustomEvent(i, j);
            }
        }
    }

    public void sinkPTPresentToRoomEvent(int i) {
        IListener[] all = this.mPresentToRoomStatusListener.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPresentToRoomStatusListener) iListener).presentToRoomStatusUpdate(i);
            }
        }
    }

    public void dispatchPTCommonEvent(int i, byte[] bArr) {
        IListener[] all = this.mPTCommonEventListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPTCommonEventListener) iListener).onPTCommonEvent(i, bArr);
            }
        }
    }

    private void onWebLogin(long j) {
        PTAppDelegation.getInstance().setWebSignedOn(j == 0);
        PTAppDelegation.getInstance().resetPTLoginType();
    }

    private void onIMLogin(long j) {
        PTAppDelegation.getInstance().getIMHelper().setIMSignedOn(j == 0);
        PTAppDelegation.getInstance().resetPTLoginType();
    }

    private void onWebLogout(long j) {
        PTAppDelegation.getInstance().setWebSignedOn(false);
        PTAppDelegation.getInstance().resetPTLoginType();
    }

    public void sinkIMLocalStatusChanged(int i) {
        IMHelperDelegation iMHelper = PTAppDelegation.getInstance().getIMHelper();
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
                iMHelper.setIMSignedOn(false);
                break;
            case 4:
                iMHelper.setIMSignedOn(true);
                PTAppDelegation.getInstance().resetPTLoginType();
                break;
        }
        IListener[] all = this.mIMListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IIMListener) iListener).onIMLocalStatusChanged(i);
            }
        }
    }

    public void sinkIMReceived(@NonNull byte[] bArr) {
        try {
            IMMessage parseFrom = IMMessage.parseFrom(bArr);
            IListener[] all = this.mIMListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMListener) iListener).onIMReceived(parseFrom);
                }
            }
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    public void sinkIMBuddyPresence(@NonNull byte[] bArr) {
        try {
            BuddyItem parseFrom = BuddyItem.parseFrom(bArr);
            IListener[] all = this.mIMListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMListener) iListener).onIMBuddyPresence(parseFrom);
                }
            }
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    public void sinkIMBuddyPic(@NonNull byte[] bArr) {
        try {
            BuddyItem parseFrom = BuddyItem.parseFrom(bArr);
            IListener[] all = this.mIMListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMListener) iListener).onIMBuddyPic(parseFrom);
                }
            }
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    public void sinkIMBuddySort() {
        IListener[] all = this.mIMListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IIMListener) iListener).onIMBuddySort();
            }
        }
    }

    public void sinkNetworkState(boolean z) {
        IListener[] all = this.mPTListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPTUIListener) iListener).onDataNetworkStatusChanged(z);
            }
        }
    }
}
