package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum BoundType {
    OPEN {
        /* access modifiers changed from: 0000 */
        public BoundType flip() {
            return CLOSED;
        }
    },
    CLOSED {
        /* access modifiers changed from: 0000 */
        public BoundType flip() {
            return OPEN;
        }
    };

    /* access modifiers changed from: 0000 */
    public abstract BoundType flip();

    static BoundType forBoolean(boolean z) {
        return z ? CLOSED : OPEN;
    }
}
