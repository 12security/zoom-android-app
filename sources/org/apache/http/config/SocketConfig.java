package org.apache.http.config;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class SocketConfig implements Cloneable {
    public static final SocketConfig DEFAULT = new Builder().build();
    private final int backlogSize;
    private final int rcvBufSize;
    private final int sndBufSize;
    private final boolean soKeepAlive;
    private final int soLinger;
    private final boolean soReuseAddress;
    private final int soTimeout;
    private final boolean tcpNoDelay;

    public static class Builder {
        private int backlogSize;
        private int rcvBufSize;
        private int sndBufSize;
        private boolean soKeepAlive;
        private int soLinger = -1;
        private boolean soReuseAddress;
        private int soTimeout;
        private boolean tcpNoDelay = true;

        Builder() {
        }

        public Builder setSoTimeout(int i) {
            this.soTimeout = i;
            return this;
        }

        public Builder setSoReuseAddress(boolean z) {
            this.soReuseAddress = z;
            return this;
        }

        public Builder setSoLinger(int i) {
            this.soLinger = i;
            return this;
        }

        public Builder setSoKeepAlive(boolean z) {
            this.soKeepAlive = z;
            return this;
        }

        public Builder setTcpNoDelay(boolean z) {
            this.tcpNoDelay = z;
            return this;
        }

        public Builder setSndBufSize(int i) {
            this.sndBufSize = i;
            return this;
        }

        public Builder setRcvBufSize(int i) {
            this.rcvBufSize = i;
            return this;
        }

        public Builder setBacklogSize(int i) {
            this.backlogSize = i;
            return this;
        }

        public SocketConfig build() {
            SocketConfig socketConfig = new SocketConfig(this.soTimeout, this.soReuseAddress, this.soLinger, this.soKeepAlive, this.tcpNoDelay, this.sndBufSize, this.rcvBufSize, this.backlogSize);
            return socketConfig;
        }
    }

    SocketConfig(int i, boolean z, int i2, boolean z2, boolean z3, int i3, int i4, int i5) {
        this.soTimeout = i;
        this.soReuseAddress = z;
        this.soLinger = i2;
        this.soKeepAlive = z2;
        this.tcpNoDelay = z3;
        this.sndBufSize = i3;
        this.rcvBufSize = i4;
        this.backlogSize = i5;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public boolean isSoReuseAddress() {
        return this.soReuseAddress;
    }

    public int getSoLinger() {
        return this.soLinger;
    }

    public boolean isSoKeepAlive() {
        return this.soKeepAlive;
    }

    public boolean isTcpNoDelay() {
        return this.tcpNoDelay;
    }

    public int getSndBufSize() {
        return this.sndBufSize;
    }

    public int getRcvBufSize() {
        return this.rcvBufSize;
    }

    public int getBacklogSize() {
        return this.backlogSize;
    }

    /* access modifiers changed from: protected */
    public SocketConfig clone() throws CloneNotSupportedException {
        return (SocketConfig) super.clone();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[soTimeout=");
        sb.append(this.soTimeout);
        sb.append(", soReuseAddress=");
        sb.append(this.soReuseAddress);
        sb.append(", soLinger=");
        sb.append(this.soLinger);
        sb.append(", soKeepAlive=");
        sb.append(this.soKeepAlive);
        sb.append(", tcpNoDelay=");
        sb.append(this.tcpNoDelay);
        sb.append(", sndBufSize=");
        sb.append(this.sndBufSize);
        sb.append(", rcvBufSize=");
        sb.append(this.rcvBufSize);
        sb.append(", backlogSize=");
        sb.append(this.backlogSize);
        sb.append("]");
        return sb.toString();
    }

    public static Builder custom() {
        return new Builder();
    }

    public static Builder copy(SocketConfig socketConfig) {
        Args.notNull(socketConfig, "Socket config");
        return new Builder().setSoTimeout(socketConfig.getSoTimeout()).setSoReuseAddress(socketConfig.isSoReuseAddress()).setSoLinger(socketConfig.getSoLinger()).setSoKeepAlive(socketConfig.isSoKeepAlive()).setTcpNoDelay(socketConfig.isTcpNoDelay()).setSndBufSize(socketConfig.getSndBufSize()).setRcvBufSize(socketConfig.getRcvBufSize()).setBacklogSize(socketConfig.getBacklogSize());
    }
}
