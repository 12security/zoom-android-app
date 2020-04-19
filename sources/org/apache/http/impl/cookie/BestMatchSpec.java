package org.apache.http.impl.cookie;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.SAFE)
@Deprecated
public class BestMatchSpec extends DefaultCookieSpec {
    public String toString() {
        return "best-match";
    }

    public BestMatchSpec(String[] strArr, boolean z) {
        super(strArr, z);
    }

    public BestMatchSpec() {
        this(null, false);
    }
}
