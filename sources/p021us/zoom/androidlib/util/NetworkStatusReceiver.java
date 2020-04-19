package p021us.zoom.androidlib.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/* renamed from: us.zoom.androidlib.util.NetworkStatusReceiver */
public class NetworkStatusReceiver extends BroadcastReceiver {
    private static final int MESSAGE_INIT = 0;
    private static final int MESSAGE_RECEIVE_BROADCAST = 1;
    private Handler handler;
    HandlerThread handlerThread = new HandlerThread("NetworkStatusReceiver");
    /* access modifiers changed from: private */
    public boolean mLastConnection = false;
    /* access modifiers changed from: private */
    public String mLastIpAddress;
    /* access modifiers changed from: private */
    public int mLastType = 0;
    /* access modifiers changed from: private */
    public ListenerList mListener = new ListenerList();
    /* access modifiers changed from: private */
    public Handler mainHandler = new Handler(Looper.getMainLooper());

    /* renamed from: us.zoom.androidlib.util.NetworkStatusReceiver$NetworkStatusListener */
    public interface NetworkStatusListener extends IListener {
        void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2);
    }

    /* renamed from: us.zoom.androidlib.util.NetworkStatusReceiver$SimpleNetworkStatusListener */
    public static class SimpleNetworkStatusListener implements NetworkStatusListener {
        public void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2) {
        }
    }

    public NetworkStatusReceiver(final Context context) {
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper()) {
            public void handleMessage(Message message) {
                final boolean hasDataNetwork = NetworkUtil.hasDataNetwork(context);
                final int dataNetworkType = NetworkUtil.getDataNetworkType(context);
                final String networkIP = NetworkUtil.getNetworkIP(context);
                if (message.what != 0) {
                    NetworkStatusReceiver.this.mainHandler.post(new Runnable() {
                        public void run() {
                            IListener[] all = NetworkStatusReceiver.this.mListener.getAll();
                            if (all != null && all.length > 0) {
                                int length = all.length;
                                for (int i = 0; i < length; i++) {
                                    ((NetworkStatusListener) all[i]).networkStatusChanged(hasDataNetwork, dataNetworkType, networkIP, NetworkStatusReceiver.this.mLastConnection, NetworkStatusReceiver.this.mLastType, NetworkStatusReceiver.this.mLastIpAddress);
                                }
                            }
                            NetworkStatusReceiver.this.mLastConnection = hasDataNetwork;
                            NetworkStatusReceiver.this.mLastType = dataNetworkType;
                            NetworkStatusReceiver.this.mLastIpAddress = networkIP;
                        }
                    });
                    return;
                }
                NetworkStatusReceiver.this.mLastConnection = hasDataNetwork;
                NetworkStatusReceiver.this.mLastType = dataNetworkType;
                NetworkStatusReceiver.this.mLastIpAddress = networkIP;
            }
        };
    }

    public void addListener(NetworkStatusListener networkStatusListener) {
        if (networkStatusListener != null) {
            IListener[] all = this.mListener.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == networkStatusListener) {
                    removeListener((NetworkStatusListener) all[i]);
                }
            }
            this.mListener.add(networkStatusListener);
        }
    }

    public void removeListener(NetworkStatusListener networkStatusListener) {
        this.mListener.remove(networkStatusListener);
    }

    public boolean isListenerEmpty() {
        return this.mListener.size() == 0;
    }

    public void registerReceiver(Context context) {
        if (context != null) {
            context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.handler.sendEmptyMessage(0);
        }
    }

    public void unregisterReceiver(Context context) {
        if (context != null) {
            this.handlerThread.quit();
            context.unregisterReceiver(this);
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (context != null && intent != null) {
            String action = intent.getAction();
            if (!StringUtil.isEmptyOrNull(action) && "android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                this.handler.sendEmptyMessage(1);
            }
        }
    }

    private void notifyStatusChanged(Context context) {
        IListener[] all = this.mListener.getAll();
        boolean hasDataNetwork = NetworkUtil.hasDataNetwork(context);
        int dataNetworkType = NetworkUtil.getDataNetworkType(context);
        String networkIP = NetworkUtil.getNetworkIP(context);
        for (IListener iListener : all) {
            ((NetworkStatusListener) iListener).networkStatusChanged(hasDataNetwork, dataNetworkType, networkIP, this.mLastConnection, this.mLastType, this.mLastIpAddress);
        }
        this.mLastConnection = hasDataNetwork;
        this.mLastType = dataNetworkType;
        this.mLastIpAddress = networkIP;
    }
}
