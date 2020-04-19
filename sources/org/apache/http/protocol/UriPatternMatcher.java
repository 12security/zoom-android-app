package org.apache.http.protocol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.SAFE)
public class UriPatternMatcher<T> {
    private final Map<String, T> map = new HashMap();

    public synchronized Set<Entry<String, T>> entrySet() {
        return new HashSet(this.map.entrySet());
    }

    public synchronized void register(String str, T t) {
        Args.notNull(str, "URI request pattern");
        this.map.put(str, t);
    }

    public synchronized void unregister(String str) {
        if (str != null) {
            this.map.remove(str);
        }
    }

    @Deprecated
    public synchronized void setHandlers(Map<String, T> map2) {
        Args.notNull(map2, "Map of handlers");
        this.map.clear();
        this.map.putAll(map2);
    }

    @Deprecated
    public synchronized void setObjects(Map<String, T> map2) {
        Args.notNull(map2, "Map of handlers");
        this.map.clear();
        this.map.putAll(map2);
    }

    @Deprecated
    public synchronized Map<String, T> getObjects() {
        return this.map;
    }

    public synchronized T lookup(String str) {
        T t;
        Args.notNull(str, "Request path");
        t = this.map.get(str);
        if (t == null) {
            String str2 = null;
            for (String str3 : this.map.keySet()) {
                if (matchUriRequestPattern(str3, str) && (str2 == null || str2.length() < str3.length() || (str2.length() == str3.length() && str3.endsWith("*")))) {
                    t = this.map.get(str3);
                    str2 = str3;
                }
            }
        }
        return t;
    }

    /* access modifiers changed from: protected */
    public boolean matchUriRequestPattern(String str, String str2) {
        boolean z = true;
        if (str.equals("*")) {
            return true;
        }
        if ((!str.endsWith("*") || !str2.startsWith(str.substring(0, str.length() - 1))) && (!str.startsWith("*") || !str2.endsWith(str.substring(1, str.length())))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return this.map.toString();
    }
}
