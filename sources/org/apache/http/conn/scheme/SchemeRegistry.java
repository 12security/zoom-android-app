package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.SAFE)
@Deprecated
public final class SchemeRegistry {
    private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap<>();

    public final Scheme getScheme(String str) {
        Scheme scheme = get(str);
        if (scheme != null) {
            return scheme;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Scheme '");
        sb.append(str);
        sb.append("' not registered.");
        throw new IllegalStateException(sb.toString());
    }

    public final Scheme getScheme(HttpHost httpHost) {
        Args.notNull(httpHost, "Host");
        return getScheme(httpHost.getSchemeName());
    }

    public final Scheme get(String str) {
        Args.notNull(str, "Scheme name");
        return (Scheme) this.registeredSchemes.get(str);
    }

    public final Scheme register(Scheme scheme) {
        Args.notNull(scheme, "Scheme");
        return (Scheme) this.registeredSchemes.put(scheme.getName(), scheme);
    }

    public final Scheme unregister(String str) {
        Args.notNull(str, "Scheme name");
        return (Scheme) this.registeredSchemes.remove(str);
    }

    public final List<String> getSchemeNames() {
        return new ArrayList(this.registeredSchemes.keySet());
    }

    public void setItems(Map<String, Scheme> map) {
        if (map != null) {
            this.registeredSchemes.clear();
            this.registeredSchemes.putAll(map);
        }
    }
}
