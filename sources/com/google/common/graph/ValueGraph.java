package com.google.common.graph;

import com.google.common.annotations.Beta;
import javax.annotation.Nullable;

@Beta
public interface ValueGraph<N, V> extends Graph<N> {
    V edgeValue(Object obj, Object obj2);

    V edgeValueOrDefault(Object obj, Object obj2, @Nullable V v);

    boolean equals(@Nullable Object obj);

    int hashCode();
}
