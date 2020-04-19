package p021us.zoom.thirdparty.dropbox;

import androidx.annotation.NonNull;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import java.io.Serializable;

/* renamed from: us.zoom.thirdparty.dropbox.ZMTokenPair */
public abstract class ZMTokenPair implements Serializable {
    private static final long serialVersionUID = -42727403795550313L;
    public final String key;
    public final String secret;

    public ZMTokenPair(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("'key' must be non-null");
        } else if (str.contains(Broker.CALLER_CACHEKEY_PREFIX)) {
            StringBuilder sb = new StringBuilder();
            sb.append("'key' must not contain a \"|\" character: \"");
            sb.append(str);
            sb.append("\"");
            throw new IllegalArgumentException(sb.toString());
        } else if (str2 != null) {
            this.key = str;
            this.secret = str2;
        } else {
            throw new IllegalArgumentException("'secret' must be non-null");
        }
    }

    public int hashCode() {
        return this.key.hashCode() ^ (this.secret.hashCode() << 1);
    }

    public boolean equals(Object obj) {
        return (obj instanceof ZMTokenPair) && equals((ZMTokenPair) obj);
    }

    public boolean equals(ZMTokenPair zMTokenPair) {
        return this.key.equals(zMTokenPair.key) && this.secret.equals(zMTokenPair.secret);
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{key=\"");
        sb.append(this.key);
        sb.append("\", secret=\"");
        sb.append(this.secret.charAt(0));
        sb.append("...\"}");
        return sb.toString();
    }
}
