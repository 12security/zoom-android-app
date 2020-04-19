package com.microsoft.aad.adal;

import java.util.concurrent.atomic.AtomicReference;

public enum AuthenticationSettings {
    INSTANCE;
    
    private static final int DEFAULT_EXPIRATION_BUFFER = 300;
    private static final int DEFAULT_READ_CONNECT_TIMEOUT = 30000;
    private static final int SECRET_RAW_KEY_LENGTH = 32;
    private String mActivityPackageName;
    private String mBrokerPackageName;
    private String mBrokerSignature;
    private Class<?> mClazzDeviceCertProxy;
    private int mConnectTimeOut;
    private boolean mEnableHardwareAcceleration;
    private int mExpirationBuffer;
    private int mReadTimeOut;
    private AtomicReference<byte[]> mSecretKeyData;
    private String mSharedPrefPackageName;
    private boolean mUseBroker;

    public byte[] getSecretKeyData() {
        return (byte[]) this.mSecretKeyData.get();
    }

    public void setSecretKey(byte[] bArr) {
        if (bArr == null || bArr.length != 32) {
            throw new IllegalArgumentException("rawKey");
        }
        this.mSecretKeyData.set(bArr);
    }

    public String getBrokerPackageName() {
        return this.mBrokerPackageName;
    }

    public void setBrokerPackageName(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            this.mBrokerPackageName = str;
            return;
        }
        throw new IllegalArgumentException("packageName cannot be empty or null");
    }

    public String getBrokerSignature() {
        return this.mBrokerSignature;
    }

    public void setBrokerSignature(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            this.mBrokerSignature = str;
            return;
        }
        throw new IllegalArgumentException("brokerSignature cannot be empty or null");
    }

    public void setDeviceCertificateProxyClass(Class cls) {
        if (IDeviceCertificate.class.isAssignableFrom(cls)) {
            this.mClazzDeviceCertProxy = cls;
            return;
        }
        throw new IllegalArgumentException("clazz");
    }

    public Class<?> getDeviceCertificateProxy() {
        return this.mClazzDeviceCertProxy;
    }

    public String getActivityPackageName() {
        return this.mActivityPackageName;
    }

    public void setActivityPackageName(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            this.mActivityPackageName = str;
            return;
        }
        throw new IllegalArgumentException("activityPackageName cannot be empty or null");
    }

    @Deprecated
    public boolean getSkipBroker() {
        return !this.mUseBroker;
    }

    @Deprecated
    public void setSkipBroker(boolean z) {
        this.mUseBroker = !z;
    }

    public boolean getUseBroker() {
        return this.mUseBroker;
    }

    public void setUseBroker(boolean z) {
        this.mUseBroker = z;
    }

    public void setSharedPrefPackageName(String str) {
        this.mSharedPrefPackageName = str;
    }

    public String getSharedPrefPackageName() {
        return this.mSharedPrefPackageName;
    }

    public int getExpirationBuffer() {
        return this.mExpirationBuffer;
    }

    public void setExpirationBuffer(int i) {
        this.mExpirationBuffer = i;
    }

    public int getConnectTimeOut() {
        return this.mConnectTimeOut;
    }

    public void setConnectTimeOut(int i) {
        if (i >= 0) {
            this.mConnectTimeOut = i;
            return;
        }
        throw new IllegalArgumentException("Invalid timeOutMillis");
    }

    public int getReadTimeOut() {
        return this.mReadTimeOut;
    }

    public void setReadTimeOut(int i) {
        if (i >= 0) {
            this.mReadTimeOut = i;
            return;
        }
        throw new IllegalArgumentException("Invalid timeOutMillis");
    }

    public void setDisableWebViewHardwareAcceleration(boolean z) {
        this.mEnableHardwareAcceleration = z;
    }

    public boolean getDisableWebViewHardwareAcceleration() {
        return this.mEnableHardwareAcceleration;
    }
}
