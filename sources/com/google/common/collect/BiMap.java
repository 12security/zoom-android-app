package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface BiMap<K, V> extends Map<K, V> {
    @CanIgnoreReturnValue
    @Nullable
    V forcePut(@Nullable K k, @Nullable V v);

    BiMap<V, K> inverse();

    @CanIgnoreReturnValue
    @Nullable
    V put(@Nullable K k, @Nullable V v);

    void putAll(Map<? extends K, ? extends V> map);

    Set<V> values();
}
