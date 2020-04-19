package com.dropbox.core.util;

import com.dropbox.core.util.DumpWriter.Multiline;
import com.dropbox.core.util.DumpWriter.Plain;

public abstract class Dumpable {
    /* access modifiers changed from: protected */
    public abstract void dumpFields(DumpWriter dumpWriter);

    /* access modifiers changed from: protected */
    public String getTypeName() {
        return null;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    public final void toString(StringBuilder sb) {
        new Plain(sb).mo10668v(this);
    }

    public final String toStringMultiline() {
        StringBuilder sb = new StringBuilder();
        toStringMultiline(sb, 0, true);
        return sb.toString();
    }

    public final void toStringMultiline(StringBuilder sb, int i, boolean z) {
        new Multiline(sb, 2, i, z).mo10668v(this);
    }
}
