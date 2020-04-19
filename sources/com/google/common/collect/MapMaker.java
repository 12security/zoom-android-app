package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@GwtCompatible(emulated = true)
public final class MapMaker {
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int UNSET_INT = -1;
    int concurrencyLevel = -1;
    int initialCapacity = -1;
    Equivalence<Object> keyEquivalence;
    Strength keyStrength;
    boolean useCustomMap;
    Strength valueStrength;

    /* access modifiers changed from: 0000 */
    @GwtIncompatible
    @CanIgnoreReturnValue
    public MapMaker keyEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", (Object) this.keyEquivalence);
        this.keyEquivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
        this.useCustomMap = true;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public Equivalence<Object> getKeyEquivalence() {
        return (Equivalence) MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
    }

    @CanIgnoreReturnValue
    public MapMaker initialCapacity(int i) {
        boolean z = true;
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        if (i < 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.initialCapacity = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public int getInitialCapacity() {
        int i = this.initialCapacity;
        if (i == -1) {
            return 16;
        }
        return i;
    }

    @CanIgnoreReturnValue
    public MapMaker concurrencyLevel(int i) {
        boolean z = true;
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        if (i <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.concurrencyLevel = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public int getConcurrencyLevel() {
        int i = this.concurrencyLevel;
        if (i == -1) {
            return 4;
        }
        return i;
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    public MapMaker weakKeys() {
        return setKeyStrength(Strength.WEAK);
    }

    /* access modifiers changed from: 0000 */
    public MapMaker setKeyStrength(Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", (Object) this.keyStrength);
        this.keyStrength = (Strength) Preconditions.checkNotNull(strength);
        if (strength != Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public Strength getKeyStrength() {
        return (Strength) MoreObjects.firstNonNull(this.keyStrength, Strength.STRONG);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    public MapMaker weakValues() {
        return setValueStrength(Strength.WEAK);
    }

    /* access modifiers changed from: 0000 */
    public MapMaker setValueStrength(Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", (Object) this.valueStrength);
        this.valueStrength = (Strength) Preconditions.checkNotNull(strength);
        if (strength != Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public Strength getValueStrength() {
        return (Strength) MoreObjects.firstNonNull(this.valueStrength, Strength.STRONG);
    }

    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            return new ConcurrentHashMap(getInitialCapacity(), 0.75f, getConcurrencyLevel());
        }
        return MapMakerInternalMap.create(this);
    }

    /* access modifiers changed from: 0000 */
    @GwtIncompatible
    public <K, V> MapMakerInternalMap<K, V, ?, ?> makeCustomMap() {
        return MapMakerInternalMap.create(this);
    }

    public String toString() {
        ToStringHelper stringHelper = MoreObjects.toStringHelper((Object) this);
        int i = this.initialCapacity;
        if (i != -1) {
            stringHelper.add("initialCapacity", i);
        }
        int i2 = this.concurrencyLevel;
        if (i2 != -1) {
            stringHelper.add("concurrencyLevel", i2);
        }
        Strength strength = this.keyStrength;
        if (strength != null) {
            stringHelper.add("keyStrength", (Object) Ascii.toLowerCase(strength.toString()));
        }
        Strength strength2 = this.valueStrength;
        if (strength2 != null) {
            stringHelper.add("valueStrength", (Object) Ascii.toLowerCase(strength2.toString()));
        }
        if (this.keyEquivalence != null) {
            stringHelper.addValue((Object) "keyEquivalence");
        }
        return stringHelper.toString();
    }
}
