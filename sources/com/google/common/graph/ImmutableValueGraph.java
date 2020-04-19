package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;

@Beta
public final class ImmutableValueGraph<N, V> extends ValueBackedImpl<N, V> implements ValueGraph<N, V> {
    private ImmutableValueGraph(ValueGraph<N, V> valueGraph) {
        super(ValueGraphBuilder.from(valueGraph), getNodeConnections(valueGraph), (long) valueGraph.edges().size());
    }

    public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> valueGraph) {
        return valueGraph instanceof ImmutableValueGraph ? (ImmutableValueGraph) valueGraph : new ImmutableValueGraph(valueGraph);
    }

    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> immutableValueGraph) {
        return (ImmutableValueGraph) Preconditions.checkNotNull(immutableValueGraph);
    }

    private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> valueGraph) {
        Builder builder = ImmutableMap.builder();
        for (Object next : valueGraph.nodes()) {
            builder.put(next, connectionsOf(valueGraph, next));
        }
        return builder.build();
    }

    private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> valueGraph, final N n) {
        C14151 r0 = new Function<N, V>() {
            public V apply(N n) {
                return valueGraph.edgeValue(n, n);
            }
        };
        return valueGraph.isDirected() ? DirectedGraphConnections.ofImmutable(valueGraph.predecessors(n), Maps.asMap(valueGraph.successors(n), (Function<? super K, V>) r0)) : UndirectedGraphConnections.ofImmutable(Maps.asMap(valueGraph.adjacentNodes(n), (Function<? super K, V>) r0));
    }

    public V edgeValue(Object obj, Object obj2) {
        return this.backingValueGraph.edgeValue(obj, obj2);
    }

    public V edgeValueOrDefault(Object obj, Object obj2, @Nullable V v) {
        return this.backingValueGraph.edgeValueOrDefault(obj, obj2, v);
    }

    public String toString() {
        return this.backingValueGraph.toString();
    }
}
