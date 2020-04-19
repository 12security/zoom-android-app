package com.revolverobotics.kubisdk;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.Date;
import java.util.UUID;

@TargetApi(18)
public class Kubi extends GattInterface {
    public static final int ANGLE_PAN_MAX = 150;
    public static final int ANGLE_PAN_MIN = -150;
    public static final int ANGLE_PAN_ZERO = 0;
    public static final int ANGLE_TILT_MAX = 45;
    public static final int ANGLE_TILT_MIN = -45;
    public static final int ANGLE_TILT_ZERO = 0;
    public static final float DEFAULT_SPEED = 52.3f;
    public static final int DIRECTION_PAN_LEFT = 1;
    public static final int DIRECTION_PAN_NONE = 2;
    public static final int DIRECTION_PAN_RIGHT = -1;
    public static final int DIRECTION_PAN_STOP = 0;
    public static final int DIRECTION_TILT_DOWN = -1;
    public static final int DIRECTION_TILT_NONE = 2;
    public static final int DIRECTION_TILT_STOP = 0;
    public static final int DIRECTION_TILT_UP = 1;
    public static final int GESTURE_BOW = 0;
    public static final int GESTURE_NOD = 1;
    public static final int GESTURE_SCAN = 3;
    public static final int GESTURE_SHAKE = 2;
    public static final float MAX_SPEED = 100.0f;
    public static final int MIN_SPEED_VAL = 1;
    private static final int RSSI_REQUEST_INTERVAL = 3000;
    private static final float SERVO_SPEED_COEFF = 0.6686217f;
    private static final byte SERVO_SPEED_REGISTER = 32;
    public static final int SPEED_MIN = 2;
    public static final int SPEED_PAN_DEFAULT = 78;
    public static final int SPEED_PAN_MAX = 150;
    public static final int SPEED_PAN_MIN = 2;
    public static final int SPEED_TILT_DEFAULT = 47;
    public static final int SPEED_TILT_MAX = 105;
    public static final int SPEED_TILT_MIN = 2;
    private final UUID BATTERY_STATUS_UUID = UUID.fromString("0000E105-0000-1000-8000-00805F9B34FB");
    private final UUID BATTERY_UUID = UUID.fromString("0000E101-0000-1000-8000-00805F9B34FB");
    private final UUID BUTTON_UUID = UUID.fromString("0000E10A-0000-1000-8000-00805F9B34FB");
    private final UUID KUBI_SERVICE_UUID = UUID.fromString("0000E001-0000-1000-8000-00805F9B34FB");
    private final UUID LED_COLOR_UUID = UUID.fromString("0000E104-0000-1000-8000-00805F9B34FB");
    private final UUID REGISTER_WRITE1P_UUID = UUID.fromString("00009141-0000-1000-8000-00805F9B34FB");
    private final UUID REGISTER_WRITE2P_UUID = UUID.fromString("00009142-0000-1000-8000-00805F9B34FB");
    private final UUID SERVO_ERROR_ID_UUID = UUID.fromString("0000E103-0000-1000-8000-00805F9B34FB");
    private final UUID SERVO_ERROR_UUID = UUID.fromString("0000E102-0000-1000-8000-00805F9B34FB");
    private final UUID SERVO_HORIZONTAL_UUID = UUID.fromString("00009145-0000-1000-8000-00805F9B34FB");
    private final UUID SERVO_SERVICE_UUID = UUID.fromString("2A001800-2803-2801-2800-1D9FF2D5C442");
    private final UUID SERVO_VERTICAL_UUID = UUID.fromString("00009146-0000-1000-8000-00805F9B34FB");
    private BluetoothGattCharacteristic battery;
    private BluetoothGattCharacteristic batteryStatus;
    private BluetoothGattCharacteristic button;
    private BluetoothGattService kubiService;
    private BluetoothGattCharacteristic ledColor;
    private Context mContext;
    BluetoothDevice mDevice;
    private Handler mHandler;
    KubiManager mKubiManager;
    int mRSSI;
    /* access modifiers changed from: private */
    public float nodTemp = 0.0f;
    private float panAngle = 0.0f;
    private int panDirection = 0;
    private long panFinishTime = 0;
    private int panSpeed = 0;
    private long panStartTime = 0;
    private float panVelocity = 0.0f;
    /* access modifiers changed from: private */
    public float previousPanAngle = 0.0f;
    /* access modifiers changed from: private */
    public float previousTiltAngle = 0.0f;
    private BluetoothGattCharacteristic registerWrite1p;
    private BluetoothGattCharacteristic registerWrite2p;
    private BluetoothGattCharacteristic servoError;
    private BluetoothGattCharacteristic servoErrorID;
    private BluetoothGattCharacteristic servoHorizontal;
    private BluetoothGattService servoService;
    private BluetoothGattCharacteristic servoVertical;
    /* access modifiers changed from: private */
    public float shakeTemp = 0.0f;
    private float tiltAngle = 0.0f;
    private int tiltDirection = 0;
    private long tiltFinishTime = 0;
    private int tiltSpeed = 0;
    private long tiltStartTime = 0;
    private float tiltVelocity = 0.0f;

    public static int servoAngle(float f) {
        return (int) (((f + 150.0f) * 1023.0f) / 300.0f);
    }

    /* access modifiers changed from: protected */
    public void characteristicValueRead(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public Kubi(Context context, KubiManager kubiManager, BluetoothDevice bluetoothDevice) {
        this.mDevice = bluetoothDevice;
        this.mKubiManager = kubiManager;
        this.mHandler = new Handler();
        this.mContext = context;
        connect();
    }

    public void connect() {
        this.mGatt = this.mDevice.connectGatt(this.mContext, true, this);
    }

    public static int servoSpeed(float f) {
        return (int) Math.min(((double) f) * 1.53d, 100.0d);
    }

    public float getPan() {
        return this.previousPanAngle;
    }

    public float getTilt() {
        return this.previousTiltAngle;
    }

    public int getRSSI() {
        return this.mRSSI;
    }

    public String getName() {
        return this.mDevice.getName();
    }

    public void moveTo(float f, float f2) {
        moveTo(f, f2, 52.3f, true);
    }

    public void moveTo(float f, float f2, float f3) {
        moveTo(f, f2, f3, true);
    }

    public void moveTo(float f, float f2, float f3, boolean z) {
        int i;
        int i2;
        if (this.servoHorizontal != null && this.servoVertical != null) {
            int servoAngle = servoAngle(f);
            int servoAngle2 = servoAngle(f2);
            if (!z) {
                i2 = servoSpeed(f3);
                i = i2;
            } else {
                int abs = (int) Math.abs(f - this.previousPanAngle);
                int abs2 = (int) Math.abs(f2 - this.previousTiltAngle);
                if (abs > abs2) {
                    int servoSpeed = servoSpeed(f3);
                    int i3 = (int) ((((float) abs2) / ((float) abs)) * ((float) servoSpeed));
                    i = servoSpeed;
                    i2 = i3;
                } else if (abs2 > abs) {
                    i2 = servoSpeed(f3);
                    i = (int) ((((float) abs) / ((float) abs2)) * ((float) i2));
                } else {
                    i2 = servoSpeed(f3);
                    i = i2;
                }
            }
            if (i2 < 1) {
                i2 = 1;
            }
            if (i < 1) {
                i = 1;
            }
            super.enqueueWrite(this.registerWrite2p, new byte[]{1, 32, (byte) i, (byte) (i >> 8)});
            super.enqueueWrite(this.registerWrite2p, new byte[]{2, 32, (byte) i2, (byte) (i2 >> 8)});
            super.enqueueWrite(this.servoHorizontal, new byte[]{(byte) (servoAngle >> 8), (byte) servoAngle});
            super.enqueueWrite(this.servoVertical, new byte[]{(byte) (servoAngle2 >> 8), (byte) servoAngle2});
            this.previousPanAngle = f;
            this.previousTiltAngle = f2;
        }
    }

    public void moveTo(float f, float f2, float f3, boolean z, float f4, float f5) {
        int i;
        int i2;
        if (this.servoHorizontal != null && this.servoVertical != null) {
            int servoAngle = servoAngle(f);
            int servoAngle2 = servoAngle(f2);
            if (!z) {
                int servoSpeed = servoSpeed(f4);
                i = servoSpeed;
                i2 = servoSpeed(f5);
            } else {
                int abs = (int) Math.abs(f - this.previousPanAngle);
                int abs2 = (int) Math.abs(f2 - this.previousTiltAngle);
                if (abs > abs2) {
                    int servoSpeed2 = servoSpeed(f3);
                    int i3 = (int) ((((float) abs2) / ((float) abs)) * ((float) servoSpeed2));
                    i = servoSpeed2;
                    i2 = i3;
                } else if (abs2 > abs) {
                    i2 = servoSpeed(f3);
                    i = (int) ((((float) abs) / ((float) abs2)) * ((float) i2));
                } else {
                    i2 = servoSpeed(f3);
                    i = i2;
                }
            }
            if (i2 < 1) {
                i2 = 1;
            }
            if (i < 1) {
                i = 1;
            }
            super.enqueueWrite(this.registerWrite2p, new byte[]{1, 32, (byte) i, (byte) (i >> 8)});
            super.enqueueWrite(this.registerWrite2p, new byte[]{2, 32, (byte) i2, (byte) (i2 >> 8)});
            super.enqueueWrite(this.servoHorizontal, new byte[]{(byte) (servoAngle >> 8), (byte) servoAngle});
            super.enqueueWrite(this.servoVertical, new byte[]{(byte) (servoAngle2 >> 8), (byte) servoAngle2});
            this.previousPanAngle = f;
            this.previousTiltAngle = f2;
        }
    }

    public void unlockDevice() {
        int[] iArr = {1, 24, 0};
        super.enqueueWrite(this.registerWrite1p, new byte[]{(byte) iArr[0], (byte) iArr[1], (byte) iArr[2]});
        int[] iArr2 = {2, 24, 0};
        super.enqueueWrite(this.registerWrite1p, new byte[]{(byte) iArr2[0], (byte) iArr2[1], (byte) iArr2[2]});
    }

    public void lockDevice() {
        int[] iArr = {1, 24, 1};
        super.enqueueWrite(this.registerWrite1p, new byte[]{(byte) iArr[0], (byte) iArr[1], (byte) iArr[2]});
        int[] iArr2 = {2, 24, 1};
        super.enqueueWrite(this.registerWrite1p, new byte[]{(byte) iArr2[0], (byte) iArr2[1], (byte) iArr2[2]});
    }

    public void disconnect() {
        this.mGatt.close();
        this.mKubiManager.onKubiDisconnect(this);
    }

    public void setIndicatorColor(byte b, byte b2, byte b3) {
        super.enqueueWrite(this.ledColor, new byte[]{b, b2, b3});
    }

    @NonNull
    public String getKubiID() {
        String name = this.mDevice.getName();
        return name.substring(name.length() - 6);
    }

    public void performGesture(int i) {
        switch (i) {
            case 0:
                bow();
                return;
            case 1:
                nod();
                return;
            case 2:
                shake();
                return;
            case 3:
                scan();
                return;
            default:
                return;
        }
    }

    private void bow() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.previousPanAngle, 10.0f, 52.3f, false);
            }
        }, 200);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.previousPanAngle, -27.0f, 52.3f, false);
            }
        }, 700);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.previousPanAngle, 0.0f, 52.3f, false);
            }
        }, 1650);
    }

    private void shake() {
        this.shakeTemp = this.previousPanAngle;
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.shakeTemp - 15.0f, Kubi.this.previousTiltAngle, 52.3f, false);
            }
        }, 200);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.shakeTemp + 15.0f, Kubi.this.previousTiltAngle, 52.3f, false);
            }
        }, 500);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.shakeTemp, Kubi.this.previousTiltAngle, 52.3f, false);
            }
        }, 1250);
    }

    private void nod() {
        this.nodTemp = this.previousTiltAngle;
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.previousPanAngle, -15.0f, 52.3f, false);
            }
        }, 200);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.previousPanAngle, 0.0f, 52.3f, false);
            }
        }, 500);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.previousPanAngle, -15.0f, 52.3f, false);
            }
        }, 800);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi kubi = Kubi.this;
                kubi.moveTo(kubi.previousPanAngle, Kubi.this.nodTemp);
            }
        }, 1100);
    }

    private void scan() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi.this.moveTo(-120.0f, 0.0f, 52.3f, false);
            }
        }, 200);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi.this.moveTo(-60.0f, 0.0f, 52.3f, false);
            }
        }, 3000);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi.this.moveTo(0.0f, 0.0f, 52.3f, false);
            }
        }, 5000);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi.this.moveTo(60.0f, 0.0f, 52.3f, false);
            }
        }, 7000);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi.this.moveTo(120.0f, 0.0f, 52.3f, false);
            }
        }, 9000);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Kubi.this.moveTo(0.0f, 0.0f, 52.3f, false);
            }
        }, 11000);
    }

    private void requestRSSI() {
        this.mGatt.readRemoteRssi();
    }

    /* access modifiers changed from: private */
    public void sendOnReady() {
        KubiManager kubiManager = this.mKubiManager;
        if (kubiManager != null) {
            kubiManager.onKubiReady(this);
        }
    }

    public void onConnectionStateChange(@NonNull BluetoothGatt bluetoothGatt, int i, int i2) {
        if (i2 == 2) {
            Log.i("Kubi", "Kubi connected.");
            this.mGatt = bluetoothGatt;
            bluetoothGatt.discoverServices();
        } else if (i2 == 0) {
            this.mKubiManager.onKubiDisconnect(this);
            Log.i("Kubi", "Kubi disconnected.");
        }
    }

    public void onServicesDiscovered(@NonNull BluetoothGatt bluetoothGatt, int i) {
        if (i == 0) {
            this.servoService = bluetoothGatt.getService(this.SERVO_SERVICE_UUID);
            this.kubiService = bluetoothGatt.getService(this.KUBI_SERVICE_UUID);
            BluetoothGattService bluetoothGattService = this.servoService;
            if (bluetoothGattService == null || this.kubiService == null) {
                this.mKubiManager.disconnect();
                return;
            }
            this.registerWrite1p = bluetoothGattService.getCharacteristic(this.REGISTER_WRITE1P_UUID);
            this.registerWrite2p = this.servoService.getCharacteristic(this.REGISTER_WRITE2P_UUID);
            this.servoHorizontal = this.servoService.getCharacteristic(this.SERVO_HORIZONTAL_UUID);
            this.servoVertical = this.servoService.getCharacteristic(this.SERVO_VERTICAL_UUID);
            this.battery = this.kubiService.getCharacteristic(this.BATTERY_UUID);
            this.servoError = this.kubiService.getCharacteristic(this.SERVO_ERROR_UUID);
            this.servoErrorID = this.kubiService.getCharacteristic(this.SERVO_ERROR_ID_UUID);
            this.ledColor = this.kubiService.getCharacteristic(this.LED_COLOR_UUID);
            this.batteryStatus = this.kubiService.getCharacteristic(this.BATTERY_STATUS_UUID);
            this.button = this.kubiService.getCharacteristic(this.BUTTON_UUID);
            if (this.mKubiManager != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Kubi.this.sendOnReady();
                    }
                });
                requestRSSI();
                return;
            }
            return;
        }
        Log.e("Kubi", "Unable to discover services.");
    }

    public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i, int i2) {
        if (i2 == 0) {
            this.mRSSI = i;
            this.mKubiManager.onKubiUpdateRSSI(this, i);
        }
    }

    public void sendPanData(float f, int i) {
        if (this.registerWrite2p != null && this.servoHorizontal != null) {
            int servoAngle = servoAngle(f);
            super.enqueueWrite(this.registerWrite2p, new byte[]{1, 32, (byte) i, (byte) (i >> 8)});
            super.enqueueWrite(this.servoHorizontal, new byte[]{(byte) (servoAngle >> 8), (byte) servoAngle});
        }
    }

    public void sendTiltData(float f, int i) {
        if (this.registerWrite2p != null && this.servoVertical != null) {
            int servoAngle = servoAngle(f);
            super.enqueueWrite(this.registerWrite2p, new byte[]{2, 32, (byte) i, (byte) (i >> 8)});
            super.enqueueWrite(this.servoVertical, new byte[]{(byte) (servoAngle >> 8), (byte) servoAngle});
        }
    }

    public void moveToPan(float f, int i) {
        this.previousPanAngle = getCurrentPanAngle();
        if (Float.isNaN(this.previousPanAngle)) {
            this.previousPanAngle = this.panAngle;
        }
        if (f > 150.0f) {
            f = 150.0f;
        }
        if (f < -150.0f) {
            f = -150.0f;
        }
        if (i > 150) {
            i = 150;
        }
        if (i < 2) {
            i = 2;
        }
        this.panVelocity = ((float) i) * SERVO_SPEED_COEFF;
        if (f < this.previousPanAngle) {
            this.panVelocity = -this.panVelocity;
        }
        sendPanData(f, i);
        this.panStartTime = new Date().getTime();
        this.panAngle = f;
        this.panSpeed = i;
        this.panDirection = this.panVelocity >= 0.0f ? 1 : -1;
    }

    public void moveToTilt(float f, int i) {
        this.previousTiltAngle = getCurrentTiltAngle();
        if (Float.isNaN(this.previousTiltAngle)) {
            this.previousTiltAngle = this.tiltAngle;
        }
        if (f > 45.0f) {
            f = 45.0f;
        }
        if (f < -45.0f) {
            f = -45.0f;
        }
        if (i > 105) {
            i = 105;
        }
        if (i < 2) {
            i = 2;
        }
        this.tiltVelocity = ((float) i) * SERVO_SPEED_COEFF;
        if (f < this.previousTiltAngle) {
            this.tiltVelocity = -this.tiltVelocity;
        }
        sendTiltData(f, i);
        this.tiltStartTime = new Date().getTime();
        this.tiltAngle = f;
        this.tiltSpeed = i;
        this.tiltDirection = this.tiltVelocity >= 0.0f ? 1 : -1;
    }

    private float getCurrentPanAngle() {
        if (Float.isNaN(this.previousPanAngle) || Float.isNaN(this.panAngle) || this.panStartTime <= 0) {
            return Float.NaN;
        }
        float time = ((float) (new Date().getTime() - this.panStartTime)) / 1000.0f;
        float f = this.previousPanAngle;
        float f2 = this.panVelocity;
        float f3 = f + (time * f2);
        if ((f2 <= 0.0f || f3 >= this.panAngle) && (this.panVelocity >= 0.0f || f3 <= this.panAngle)) {
            return this.panAngle;
        }
        return f3;
    }

    private float getCurrentTiltAngle() {
        if (Float.isNaN(this.previousTiltAngle) || Float.isNaN(this.tiltAngle) || this.tiltStartTime <= 0) {
            return Float.NaN;
        }
        float time = ((float) (new Date().getTime() - this.tiltStartTime)) / 1000.0f;
        float f = this.previousTiltAngle;
        float f2 = this.tiltVelocity;
        float f3 = f + (time * f2);
        if ((f2 <= 0.0f || f3 >= this.tiltAngle) && (this.tiltVelocity >= 0.0f || f3 <= this.tiltAngle)) {
            return this.tiltAngle;
        }
        return f3;
    }

    public void moveInPanDirectionWithSpeed(int i, int i2) {
        if (i != 2) {
            if (i2 < 2 || i2 > 150) {
                i2 = 78;
            }
            setPanDirectionAndSpeed(i, Math.abs(i2));
        }
    }

    public void moveInTiltDirectionWithSpeed(int i, int i2) {
        if (i != 2) {
            if (i2 < 2 || i2 > 105) {
                i2 = 47;
            }
            setTiltDirectionAndSpeed(i, Math.abs(i2));
        }
    }

    public void setPanDirectionAndSpeed(int i, int i2) {
        switch (i) {
            case -1:
                moveToPan(-150.0f, i2);
                return;
            case 0:
                stopPanMove();
                return;
            case 1:
                moveToPan(150.0f, i2);
                return;
            default:
                return;
        }
    }

    public void setTiltDirectionAndSpeed(int i, int i2) {
        switch (i) {
            case -1:
                moveToTilt(-45.0f, i2);
                return;
            case 0:
                stopTiltMove();
                return;
            case 1:
                moveToTilt(45.0f, i2);
                return;
            default:
                return;
        }
    }

    public void stopPanMove() {
        float currentPanAngle = getCurrentPanAngle();
        if (!Float.isNaN(currentPanAngle)) {
            moveToPan(currentPanAngle, this.panSpeed);
            this.panDirection = 0;
        }
    }

    public void stopTiltMove() {
        float currentTiltAngle = getCurrentTiltAngle();
        if (!Float.isNaN(currentTiltAngle)) {
            moveToTilt(currentTiltAngle, this.tiltSpeed);
            this.panDirection = 0;
        }
    }
}
