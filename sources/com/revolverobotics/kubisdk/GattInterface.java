package com.revolverobotics.kubisdk;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

@TargetApi(18)
public abstract class GattInterface extends BluetoothGattCallback {
    @NonNull
    private Queue<byte[]> dataQueue = new LinkedList();
    private boolean idle = true;
    @Nullable
    public BluetoothGatt mGatt = null;
    protected Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    @NonNull
    public Queue<BluetoothGattCharacteristic> readQueue = new LinkedList();
    @NonNull
    private Queue<BluetoothGattCharacteristic> writeQueue = new LinkedList();

    /* access modifiers changed from: protected */
    public abstract void characteristicValueRead(BluetoothGattCharacteristic bluetoothGattCharacteristic);

    /* access modifiers changed from: protected */
    public void enqueueWrite(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        if (this.mGatt != null) {
            this.writeQueue.add(bluetoothGattCharacteristic);
            this.dataQueue.add(bArr);
            if (this.idle) {
                this.idle = false;
                executeNextWrite();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void enqueueRead(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (this.mGatt != null) {
            this.readQueue.add(bluetoothGattCharacteristic);
            if (this.idle) {
                this.idle = false;
                executeNextRead();
            }
        }
    }

    private void executeNextWrite() {
        if (this.mGatt != null) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = (BluetoothGattCharacteristic) this.writeQueue.peek();
            if (bluetoothGattCharacteristic != null) {
                bluetoothGattCharacteristic.setValue((byte[]) this.dataQueue.peek());
                if (!this.mGatt.writeCharacteristic(bluetoothGattCharacteristic)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unable to write to characteristic ");
                    sb.append(bluetoothGattCharacteristic.getUuid().toString());
                    Log.e("GattWriter", sb.toString());
                }
            }
        }
    }

    private void executeNextRead() {
        if (this.mGatt != null) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = (BluetoothGattCharacteristic) this.readQueue.peek();
            if (bluetoothGattCharacteristic != null && !this.mGatt.readCharacteristic(bluetoothGattCharacteristic)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to write to characteristic ");
                sb.append(bluetoothGattCharacteristic.getUuid().toString());
                Log.e("GattWriter", sb.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public void performNextAction() {
        if (this.writeQueue.size() > 0) {
            executeNextWrite();
        } else if (this.readQueue.size() > 0) {
            executeNextRead();
        } else {
            this.idle = true;
        }
    }

    public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        if (bluetoothGattCharacteristic == ((BluetoothGattCharacteristic) this.writeQueue.peek()) && i == 0) {
            this.writeQueue.poll();
            this.dataQueue.poll();
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                GattInterface.this.performNextAction();
            }
        });
    }

    public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        if (bluetoothGattCharacteristic == ((BluetoothGattCharacteristic) this.readQueue.peek()) && i == 0) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GattInterface gattInterface = GattInterface.this;
                    gattInterface.characteristicValueRead((BluetoothGattCharacteristic) gattInterface.readQueue.poll());
                }
            });
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                GattInterface.this.performNextAction();
            }
        });
    }
}
