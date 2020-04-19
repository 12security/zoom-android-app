package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonPattern {
    public abstract boolean equals(Object obj);

    /* access modifiers changed from: 0000 */
    public abstract int flags();

    public abstract int hashCode();

    /* access modifiers changed from: 0000 */
    public abstract CommonMatcher matcher(CharSequence charSequence);

    /* access modifiers changed from: 0000 */
    public abstract String pattern();

    public abstract String toString();

    CommonPattern() {
    }
}
