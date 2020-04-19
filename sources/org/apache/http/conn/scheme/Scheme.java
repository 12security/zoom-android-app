package org.apache.http.conn.scheme;

import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public final class Scheme {
    private final int defaultPort;
    private final boolean layered;
    private final String name;
    private final SchemeSocketFactory socketFactory;
    private String stringRep;

    public Scheme(String str, int i, SchemeSocketFactory schemeSocketFactory) {
        Args.notNull(str, "Scheme name");
        Args.check(i > 0 && i <= 65535, "Port is invalid");
        Args.notNull(schemeSocketFactory, "Socket factory");
        this.name = str.toLowerCase(Locale.ENGLISH);
        this.defaultPort = i;
        if (schemeSocketFactory instanceof SchemeLayeredSocketFactory) {
            this.layered = true;
            this.socketFactory = schemeSocketFactory;
        } else if (schemeSocketFactory instanceof LayeredSchemeSocketFactory) {
            this.layered = true;
            this.socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory) schemeSocketFactory);
        } else {
            this.layered = false;
            this.socketFactory = schemeSocketFactory;
        }
    }

    @Deprecated
    public Scheme(String str, SocketFactory socketFactory2, int i) {
        Args.notNull(str, "Scheme name");
        Args.notNull(socketFactory2, "Socket factory");
        Args.check(i > 0 && i <= 65535, "Port is invalid");
        this.name = str.toLowerCase(Locale.ENGLISH);
        if (socketFactory2 instanceof LayeredSocketFactory) {
            this.socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory) socketFactory2);
            this.layered = true;
        } else {
            this.socketFactory = new SchemeSocketFactoryAdaptor(socketFactory2);
            this.layered = false;
        }
        this.defaultPort = i;
    }

    public final int getDefaultPort() {
        return this.defaultPort;
    }

    @Deprecated
    public final SocketFactory getSocketFactory() {
        SchemeSocketFactory schemeSocketFactory = this.socketFactory;
        if (schemeSocketFactory instanceof SchemeSocketFactoryAdaptor) {
            return ((SchemeSocketFactoryAdaptor) schemeSocketFactory).getFactory();
        }
        if (this.layered) {
            return new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory) schemeSocketFactory);
        }
        return new SocketFactoryAdaptor(schemeSocketFactory);
    }

    public final SchemeSocketFactory getSchemeSocketFactory() {
        return this.socketFactory;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isLayered() {
        return this.layered;
    }

    public final int resolvePort(int i) {
        return i <= 0 ? this.defaultPort : i;
    }

    public final String toString() {
        if (this.stringRep == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name);
            sb.append(':');
            sb.append(Integer.toString(this.defaultPort));
            this.stringRep = sb.toString();
        }
        return this.stringRep;
    }

    public final boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scheme)) {
            return false;
        }
        Scheme scheme = (Scheme) obj;
        if (!(this.name.equals(scheme.name) && this.defaultPort == scheme.defaultPort && this.layered == scheme.layered)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.defaultPort), (Object) this.name), this.layered);
    }
}
