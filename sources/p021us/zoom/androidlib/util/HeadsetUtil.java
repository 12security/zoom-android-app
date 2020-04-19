package p021us.zoom.androidlib.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import androidx.annotation.Nullable;
import java.util.List;

/* renamed from: us.zoom.androidlib.util.HeadsetUtil */
public class HeadsetUtil extends BroadcastReceiver {
    private static final String A2DP_ACTION_CONNECTION_STATE_CHANGED = getStaticStringField("android.bluetooth.BluetoothA2dp", "ACTION_CONNECTION_STATE_CHANGED");
    private static final String A2DP_EXTRA_STATE = getStaticStringField("android.bluetooth.BluetoothA2dp", "EXTRA_STATE");
    private static final String ACTION_SCO_AUDIO_STATE_UPDATED = "android.media.ACTION_SCO_AUDIO_STATE_UPDATED";
    private static final String BLUETOOTH_ACTION_CONNECTION_STATE_CHANGED = getStaticStringField("android.bluetooth.BluetoothHeadset", "ACTION_CONNECTION_STATE_CHANGED");
    private static final String BLUETOOTH_EXTRA_STATE = getStaticStringField("android.bluetooth.BluetoothProfile", "EXTRA_STATE");
    private static final int BT_STATE_CONNECTED = 2;
    private static final int BT_STATE_DISCONNECTED = 0;
    private static final int BT_STATE_PLAYING = 4;
    public static final String TAG = "HeadsetUtil";
    private static HeadsetUtil instance = null;
    /* access modifiers changed from: private */
    public AudioManager mAudioManager;
    /* access modifiers changed from: private */
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    /* access modifiers changed from: private */
    public BluetoothDevice mBluetoothDevice;
    /* access modifiers changed from: private */
    public BluetoothHeadset mBluetoothHeadset;
    /* access modifiers changed from: private */
    public boolean mBluetoothHeadsetOn = false;
    private boolean mBluetoothScoAudioOn = false;
    private Context mContext;
    private Handler mHandler = new Handler();
    private Object mHeadsetProfileListener = new ServiceListener() {
        public void onServiceDisconnected(int i) {
            if (HeadsetUtil.this.mAudioManager != null) {
                HeadsetUtil.this.mAudioManager.stopBluetoothSco();
            }
            if (!(HeadsetUtil.this.mBluetoothAdapter == null || HeadsetUtil.this.mBluetoothHeadset == null)) {
                HeadsetUtil.this.mBluetoothAdapter.closeProfileProxy(1, HeadsetUtil.this.mBluetoothHeadset);
                HeadsetUtil.this.mBluetoothHeadset = null;
            }
            HeadsetUtil.this.mStartBluetoothScoCalled = false;
            HeadsetUtil.this.resetSco();
            HeadsetUtil.this.checkBTConnectionStatus();
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) bluetoothProfile;
            HeadsetUtil.this.mBluetoothHeadset = bluetoothHeadset;
            List connectedDevices = bluetoothHeadset.getConnectedDevices();
            if (connectedDevices.size() > 0) {
                HeadsetUtil.this.mBluetoothDevice = (BluetoothDevice) connectedDevices.get(0);
                HeadsetUtil.this.mBluetoothHeadsetOn = true;
                HeadsetUtil.this.resetSco();
                HeadsetUtil.this.postNotifyHeadsetConnectionChanged();
            }
        }
    };
    private boolean mIsStartingSco;
    private boolean mIsStoppingSco;
    private ListenerList mListenerList = new ListenerList();
    /* access modifiers changed from: private */
    public boolean mStartBluetoothScoCalled = false;
    /* access modifiers changed from: private */
    public boolean mWiredHeadsetOn = false;

    /* renamed from: us.zoom.androidlib.util.HeadsetUtil$IHeadsetConnectionListener */
    public interface IHeadsetConnectionListener extends IListener {
        void onBluetoothScoAudioStatus(boolean z);

        void onHeadsetStatusChanged(boolean z, boolean z2);
    }

    public static synchronized HeadsetUtil getInstance() {
        HeadsetUtil headsetUtil;
        synchronized (HeadsetUtil.class) {
            if (instance == null) {
                instance = new HeadsetUtil();
            }
            headsetUtil = instance;
        }
        return headsetUtil;
    }

    private HeadsetUtil() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0057 A[Catch:{ Exception -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initialize(android.content.Context r3, boolean r4) {
        /*
            r2 = this;
            r2.mContext = r3
            android.content.IntentFilter r0 = new android.content.IntentFilter
            r0.<init>()
            java.lang.String r1 = "android.intent.action.HEADSET_PLUG"
            r0.addAction(r1)
            java.lang.String r1 = "android.media.SCO_AUDIO_STATE_CHANGED"
            r0.addAction(r1)
            java.lang.String r1 = "android.bluetooth.adapter.action.STATE_CHANGED"
            r0.addAction(r1)
            java.lang.String r1 = BLUETOOTH_ACTION_CONNECTION_STATE_CHANGED
            if (r1 == 0) goto L_0x0020
            if (r4 == 0) goto L_0x0020
            r0.addAction(r1)
            goto L_0x0027
        L_0x0020:
            java.lang.String r1 = A2DP_ACTION_CONNECTION_STATE_CHANGED
            if (r1 == 0) goto L_0x0027
            r0.addAction(r1)
        L_0x0027:
            r3.registerReceiver(r2, r0)     // Catch:{ Exception -> 0x0060 }
            android.content.Context r0 = r2.mContext     // Catch:{ Exception -> 0x0060 }
            java.lang.String r1 = "audio"
            java.lang.Object r0 = r0.getSystemService(r1)     // Catch:{ Exception -> 0x0060 }
            android.media.AudioManager r0 = (android.media.AudioManager) r0     // Catch:{ Exception -> 0x0060 }
            r2.mAudioManager = r0     // Catch:{ Exception -> 0x0060 }
            android.media.AudioManager r0 = r2.mAudioManager     // Catch:{ Exception -> 0x0060 }
            boolean r0 = r0.isWiredHeadsetOn()     // Catch:{ Exception -> 0x0060 }
            r2.mWiredHeadsetOn = r0     // Catch:{ Exception -> 0x0060 }
            android.media.AudioManager r0 = r2.mAudioManager     // Catch:{ Exception -> 0x0060 }
            boolean r0 = r0.isBluetoothA2dpOn()     // Catch:{ Exception -> 0x0060 }
            r1 = 1
            if (r0 != 0) goto L_0x0052
            android.media.AudioManager r0 = r2.mAudioManager     // Catch:{ Exception -> 0x0060 }
            boolean r0 = r0.isBluetoothScoOn()     // Catch:{ Exception -> 0x0060 }
            if (r0 == 0) goto L_0x0050
            goto L_0x0052
        L_0x0050:
            r0 = 0
            goto L_0x0053
        L_0x0052:
            r0 = 1
        L_0x0053:
            r2.mBluetoothHeadsetOn = r0     // Catch:{ Exception -> 0x0060 }
            if (r4 == 0) goto L_0x0060
            android.bluetooth.BluetoothAdapter r4 = r2.mBluetoothAdapter     // Catch:{ Exception -> 0x0060 }
            java.lang.Object r0 = r2.mHeadsetProfileListener     // Catch:{ Exception -> 0x0060 }
            android.bluetooth.BluetoothProfile$ServiceListener r0 = (android.bluetooth.BluetoothProfile.ServiceListener) r0     // Catch:{ Exception -> 0x0060 }
            r4.getProfileProxy(r3, r0, r1)     // Catch:{ Exception -> 0x0060 }
        L_0x0060:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.HeadsetUtil.initialize(android.content.Context, boolean):void");
    }

    public void addListener(IHeadsetConnectionListener iHeadsetConnectionListener) {
        this.mListenerList.add(iHeadsetConnectionListener);
    }

    public void removeListener(IHeadsetConnectionListener iHeadsetConnectionListener) {
        this.mListenerList.remove(iHeadsetConnectionListener);
    }

    public void onReceive(Context context, Intent intent) {
        String str = A2DP_ACTION_CONNECTION_STATE_CHANGED;
        boolean z = true;
        if (str != null && str.equals(intent.getAction())) {
            int intExtra = intent.getIntExtra(A2DP_EXTRA_STATE, -1);
            if (intExtra == 2 || intExtra == 4) {
                this.mBluetoothHeadsetOn = true;
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (bluetoothDevice != null) {
                    this.mBluetoothDevice = bluetoothDevice;
                }
                postNotifyHeadsetConnectionChanged();
            } else if (intExtra == 0) {
                this.mBluetoothHeadsetOn = false;
                this.mBluetoothScoAudioOn = false;
                this.mStartBluetoothScoCalled = false;
                postNotifyHeadsetConnectionChanged();
            }
        } else if ("android.media.SCO_AUDIO_STATE_CHANGED".equals(intent.getAction()) || ACTION_SCO_AUDIO_STATE_UPDATED.equals(intent.getAction())) {
            int intExtra2 = intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1);
            this.mBluetoothScoAudioOn = intExtra2 == 1;
            if (intExtra2 == 1) {
                this.mIsStartingSco = false;
            } else if (intExtra2 == 0) {
                this.mIsStoppingSco = false;
            } else if (intExtra2 != 2) {
                resetSco();
            }
            if (this.mBluetoothScoAudioOn && !this.mStartBluetoothScoCalled) {
                this.mBluetoothScoAudioOn = false;
            }
            notifyBluetoothScoAudioStatus(this.mBluetoothScoAudioOn);
        } else {
            String str2 = BLUETOOTH_ACTION_CONNECTION_STATE_CHANGED;
            if (str2 != null && str2.equals(intent.getAction())) {
                int intExtra3 = intent.getIntExtra(BLUETOOTH_EXTRA_STATE, -1);
                if (intExtra3 == 2 || intExtra3 == 4) {
                    this.mBluetoothHeadsetOn = true;
                    BluetoothDevice bluetoothDevice2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (bluetoothDevice2 != null) {
                        this.mBluetoothDevice = bluetoothDevice2;
                    }
                    postNotifyHeadsetConnectionChanged();
                } else if (intExtra3 == 0) {
                    this.mStartBluetoothScoCalled = false;
                    checkBTConnectionStatus();
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            HeadsetUtil.this.checkBTConnectionStatus();
                        }
                    }, 3000);
                }
            } else if ("android.intent.action.HEADSET_PLUG".equals(intent.getAction())) {
                if (intent.getIntExtra("state", -1) != 1) {
                    z = false;
                }
                this.mWiredHeadsetOn = z;
                postNotifyHeadsetConnectionChanged();
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction()) && intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0) == 12 && this.mBluetoothAdapter != null && this.mBluetoothHeadset == null) {
                resetSco();
                this.mBluetoothAdapter.getProfileProxy(this.mContext, (ServiceListener) this.mHeadsetProfileListener, 1);
            }
        }
    }

    private boolean hasBlueHeadSet() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            BluetoothHeadset bluetoothHeadset = this.mBluetoothHeadset;
            if (bluetoothHeadset != null && !CollectionsUtil.isListEmpty(bluetoothHeadset.getConnectedDevices())) {
                return true;
            }
        }
        return false;
    }

    public boolean ismIsStartingSco() {
        return this.mIsStartingSco;
    }

    public boolean ismIsStoppingSco() {
        return this.mIsStoppingSco;
    }

    @Nullable
    public String getConnectedBTName() {
        BluetoothDevice bluetoothDevice = this.mBluetoothDevice;
        if (bluetoothDevice != null) {
            return bluetoothDevice.getName();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void checkBTConnectionStatus() {
        boolean z = this.mBluetoothHeadsetOn;
        boolean z2 = this.mBluetoothScoAudioOn;
        boolean hasBlueHeadSet = hasBlueHeadSet();
        if (!hasBlueHeadSet && this.mAudioManager.isBluetoothScoOn()) {
            this.mAudioManager.stopBluetoothSco();
        }
        AudioManager audioManager = this.mAudioManager;
        boolean z3 = true;
        if (audioManager != null) {
            this.mBluetoothHeadsetOn = hasBlueHeadSet && (audioManager.isBluetoothA2dpOn() || this.mAudioManager.isBluetoothScoOn());
        }
        AudioManager audioManager2 = this.mAudioManager;
        if (audioManager2 != null) {
            if (!hasBlueHeadSet || !audioManager2.isBluetoothScoOn()) {
                z3 = false;
            }
            this.mBluetoothScoAudioOn = z3;
        }
        boolean z4 = this.mBluetoothScoAudioOn;
        if (z2 != z4) {
            notifyBluetoothScoAudioStatus(z4);
        }
        if (z != this.mBluetoothHeadsetOn) {
            postNotifyHeadsetConnectionChanged();
        }
    }

    public boolean isBluetoothHeadsetOn() {
        return this.mBluetoothHeadsetOn;
    }

    public boolean isWiredHeadsetOn() {
        return this.mWiredHeadsetOn;
    }

    public boolean isBluetoothScoAudioOn() {
        return this.mBluetoothScoAudioOn;
    }

    public boolean isBTAndWiredHeadsetsOn() {
        return isBluetoothHeadsetOn() && isWiredHeadsetOn();
    }

    public void startBluetoothSco() {
        Context context = this.mContext;
        if (context != null) {
            if (this.mAudioManager == null) {
                try {
                    this.mAudioManager = (AudioManager) context.getSystemService("audio");
                } catch (Exception unused) {
                }
            }
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                try {
                    if (!audioManager.isBluetoothScoOn()) {
                        this.mIsStartingSco = true;
                        this.mAudioManager.startBluetoothSco();
                    }
                } catch (Exception unused2) {
                    this.mIsStartingSco = false;
                }
                this.mStartBluetoothScoCalled = true;
            }
        }
    }

    public void stopBluetoothSco() {
        Context context = this.mContext;
        if (context != null) {
            if (this.mAudioManager == null) {
                try {
                    this.mAudioManager = (AudioManager) context.getSystemService("audio");
                } catch (Exception unused) {
                }
            }
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                this.mIsStoppingSco = true;
                audioManager.stopBluetoothSco();
                this.mStartBluetoothScoCalled = false;
                this.mBluetoothScoAudioOn = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void postNotifyHeadsetConnectionChanged() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                HeadsetUtil headsetUtil = HeadsetUtil.this;
                headsetUtil.notifyHeadsetConnectionChanged(headsetUtil.mWiredHeadsetOn, HeadsetUtil.this.mBluetoothHeadsetOn);
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void notifyHeadsetConnectionChanged(boolean z, boolean z2) {
        for (IListener iListener : this.mListenerList.getAll()) {
            ((IHeadsetConnectionListener) iListener).onHeadsetStatusChanged(z, z2);
        }
    }

    private void notifyBluetoothScoAudioStatus(boolean z) {
        for (IListener iListener : this.mListenerList.getAll()) {
            ((IHeadsetConnectionListener) iListener).onBluetoothScoAudioStatus(z);
        }
    }

    /* access modifiers changed from: private */
    public void resetSco() {
        this.mIsStartingSco = false;
        this.mIsStoppingSco = false;
    }

    public void enterA2dpMode() {
        resetSco();
        notifyBluetoothScoAudioStatus(this.mBluetoothScoAudioOn);
    }

    private static String getStaticStringField(String str, String str2) {
        try {
            Class cls = Class.forName(str);
            try {
                try {
                    return (String) cls.getField(str2).get(cls);
                } catch (Exception unused) {
                    return null;
                }
            } catch (Exception unused2) {
                return null;
            }
        } catch (ClassNotFoundException unused3) {
            return null;
        }
    }
}
