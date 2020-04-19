package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;

@GwtIncompatible
class ImmutableMapEntry<K, V> extends ImmutableEntry<K, V> {

    static final class NonTerminalImmutableBiMapEntry<K, V> extends NonTerminalImmutableMapEntry<K, V> {
        private final transient ImmutableMapEntry<K, V> nextInValueBucket;

        NonTerminalImmutableBiMapEntry(K k, V v, ImmutableMapEntry<K, V> immutableMapEntry, ImmutableMapEntry<K, V> immutableMapEntry2) {
            super(k, v, immutableMapEntry);
            this.nextInValueBucket = immutableMapEntry2;
        }

        /* access modifiers changed from: 0000 */
        @Nullable
        public ImmutableMapEntry<K, V> getNextInValueBucket() {
            return this.nextInValueBucket;
        }
    }

    static class NonTerminalImmutableMapEntry<K, V> extends ImmutableMapEntry<K, V> {
        private final transient ImmutableMapEntry<K, V> nextInKeyBucket;

        /* access modifiers changed from: 0000 */
        public final boolean isReusable() {
            return false;
        }

        NonTerminalImmutableMapEntry(K k, V v, ImmutableMapEntry<K, V> immutableMapEntry) {
            super(k, v);
            this.nextInKeyBucket = immutableMapEntry;
        }

        /* access modifiers changed from: 0000 */
        @Nullable
        public final ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public ImmutableMapEntry<K, V> getNextInKeyBucket() {
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public ImmutableMapEntry<K, V> getNextInValueBucket() {
        return null;
    }

    /* access modifiers changed from: 0000 */
    public boolean isReusable() {
        return true;
    }

    static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int i) {
        return new ImmutableMapEntry[i];
    }

    ImmutableMapEntry(K k, V v) {
        super(k, v);
        CollectPreconditions.checkEntryNotNull(k, v);
    }

    ImmutableMapEntry(ImmutableMapEntry<K, V> immutableMapEntry) {
        super(immutableMapEntry.getKey(), immutableMapEntry.getValue());
    }
}
