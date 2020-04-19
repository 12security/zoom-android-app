package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLine;

public class CmmSIPUser {
    private long mNativeHandle;

    private native String getExtensionImpl(long j);

    private native String getIDImpl(long j);

    private native String getJidImpl(long j);

    private native long getLineByIDImpl(long j, String str);

    private native long getLineByIndexImpl(long j, int i);

    private native int getLineCountImpl(long j);

    private native byte[] getLineProtoByIDImpl(long j, String str);

    private native byte[] getLineProtoByIndexImpl(long j, int i);

    private native String getUserNameImpl(long j);

    public CmmSIPUser(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getIDImpl(j);
    }

    @Nullable
    public String getExtension() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getExtensionImpl(j);
    }

    @Nullable
    public String getUserName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getUserNameImpl(j);
    }

    @Nullable
    public String getJid() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getJidImpl(j);
    }

    public int getLineCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLineCountImpl(j);
    }

    @Nullable
    public CmmSIPLine getLineByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long lineByIndexImpl = getLineByIndexImpl(j, i);
        if (lineByIndexImpl != 0) {
            return new CmmSIPLine(lineByIndexImpl);
        }
        return null;
    }

    @Nullable
    public CmmSIPLine getLineByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long lineByIDImpl = getLineByIDImpl(j, str);
        if (lineByIDImpl != 0) {
            return new CmmSIPLine(lineByIDImpl);
        }
        return null;
    }

    @Nullable
    public CmmSIPLine getLineProtoByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] lineProtoByIndexImpl = getLineProtoByIndexImpl(j, i);
        if (lineProtoByIndexImpl == null || lineProtoByIndexImpl.length <= 0) {
            return null;
        }
        try {
            return CmmSIPLine.parseFrom(lineProtoByIndexImpl);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public CmmSIPLine getLineProtoByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] lineProtoByIDImpl = getLineProtoByIDImpl(j, str);
        if (lineProtoByIDImpl == null || lineProtoByIDImpl.length <= 0) {
            return null;
        }
        try {
            return CmmSIPLine.parseFrom(lineProtoByIDImpl);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }
}
