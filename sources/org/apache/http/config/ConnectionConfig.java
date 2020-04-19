package org.apache.http.config;

import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class ConnectionConfig implements Cloneable {
    public static final ConnectionConfig DEFAULT = new Builder().build();
    private final int bufferSize;
    private final Charset charset;
    private final int fragmentSizeHint;
    private final CodingErrorAction malformedInputAction;
    private final MessageConstraints messageConstraints;
    private final CodingErrorAction unmappableInputAction;

    public static class Builder {
        private int bufferSize;
        private Charset charset;
        private int fragmentSizeHint = -1;
        private CodingErrorAction malformedInputAction;
        private MessageConstraints messageConstraints;
        private CodingErrorAction unmappableInputAction;

        Builder() {
        }

        public Builder setBufferSize(int i) {
            this.bufferSize = i;
            return this;
        }

        public Builder setFragmentSizeHint(int i) {
            this.fragmentSizeHint = i;
            return this;
        }

        public Builder setCharset(Charset charset2) {
            this.charset = charset2;
            return this;
        }

        public Builder setMalformedInputAction(CodingErrorAction codingErrorAction) {
            this.malformedInputAction = codingErrorAction;
            if (codingErrorAction != null && this.charset == null) {
                this.charset = Consts.ASCII;
            }
            return this;
        }

        public Builder setUnmappableInputAction(CodingErrorAction codingErrorAction) {
            this.unmappableInputAction = codingErrorAction;
            if (codingErrorAction != null && this.charset == null) {
                this.charset = Consts.ASCII;
            }
            return this;
        }

        public Builder setMessageConstraints(MessageConstraints messageConstraints2) {
            this.messageConstraints = messageConstraints2;
            return this;
        }

        public ConnectionConfig build() {
            Charset charset2 = this.charset;
            Charset charset3 = (charset2 != null || (this.malformedInputAction == null && this.unmappableInputAction == null)) ? charset2 : Consts.ASCII;
            int i = this.bufferSize;
            int i2 = i > 0 ? i : 8192;
            int i3 = this.fragmentSizeHint;
            ConnectionConfig connectionConfig = new ConnectionConfig(i2, i3 >= 0 ? i3 : i2, charset3, this.malformedInputAction, this.unmappableInputAction, this.messageConstraints);
            return connectionConfig;
        }
    }

    ConnectionConfig(int i, int i2, Charset charset2, CodingErrorAction codingErrorAction, CodingErrorAction codingErrorAction2, MessageConstraints messageConstraints2) {
        this.bufferSize = i;
        this.fragmentSizeHint = i2;
        this.charset = charset2;
        this.malformedInputAction = codingErrorAction;
        this.unmappableInputAction = codingErrorAction2;
        this.messageConstraints = messageConstraints2;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public int getFragmentSizeHint() {
        return this.fragmentSizeHint;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public CodingErrorAction getMalformedInputAction() {
        return this.malformedInputAction;
    }

    public CodingErrorAction getUnmappableInputAction() {
        return this.unmappableInputAction;
    }

    public MessageConstraints getMessageConstraints() {
        return this.messageConstraints;
    }

    /* access modifiers changed from: protected */
    public ConnectionConfig clone() throws CloneNotSupportedException {
        return (ConnectionConfig) super.clone();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[bufferSize=");
        sb.append(this.bufferSize);
        sb.append(", fragmentSizeHint=");
        sb.append(this.fragmentSizeHint);
        sb.append(", charset=");
        sb.append(this.charset);
        sb.append(", malformedInputAction=");
        sb.append(this.malformedInputAction);
        sb.append(", unmappableInputAction=");
        sb.append(this.unmappableInputAction);
        sb.append(", messageConstraints=");
        sb.append(this.messageConstraints);
        sb.append("]");
        return sb.toString();
    }

    public static Builder custom() {
        return new Builder();
    }

    public static Builder copy(ConnectionConfig connectionConfig) {
        Args.notNull(connectionConfig, "Connection config");
        return new Builder().setBufferSize(connectionConfig.getBufferSize()).setCharset(connectionConfig.getCharset()).setFragmentSizeHint(connectionConfig.getFragmentSizeHint()).setMalformedInputAction(connectionConfig.getMalformedInputAction()).setUnmappableInputAction(connectionConfig.getUnmappableInputAction()).setMessageConstraints(connectionConfig.getMessageConstraints());
    }
}
