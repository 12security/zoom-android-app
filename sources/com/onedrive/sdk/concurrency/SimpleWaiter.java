package com.onedrive.sdk.concurrency;

public class SimpleWaiter {
    private final Object mInternalLock = new Object();
    private boolean mTriggerState;

    public void waitForSignal() {
        synchronized (this.mInternalLock) {
            if (!this.mTriggerState) {
                try {
                    this.mInternalLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void signal() {
        synchronized (this.mInternalLock) {
            this.mTriggerState = true;
            this.mInternalLock.notifyAll();
        }
    }
}
