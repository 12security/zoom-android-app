package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

@Beta
public abstract class AbstractValueGraph<N, V> extends AbstractGraph<N> implements ValueGraph<N, V> {
    public V edgeValue(Object obj, Object obj2) {
        V edgeValueOrDefault = edgeValueOrDefault(obj, obj2, null);
        if (edgeValueOrDefault != null) {
            return edgeValueOrDefault;
        }
        Preconditions.checkArgument(nodes().contains(obj), "Node %s is not an element of this graph.", obj);
        Preconditions.checkArgument(nodes().contains(obj2), "Node %s is not an element of this graph.", obj2);
        throw new IllegalArgumentException(String.format("Edge connecting %s to %s is not present in this graph.", new Object[]{obj, obj2}));
    }

    public String toString() {
        return String.format("%s, nodes: %s, edges: %s", new Object[]{String.format("isDirected: %s, allowsSelfLoops: %s", new Object[]{Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops())}), nodes(), edgeValueMap()});
    }

    private Map<EndpointPair<N>, V> edgeValueMap() {
        return Maps.asMap(edges(), (Function<? super K, V>) new Function<EndpointPair<N>, V>() {
            public V apply(EndpointPair<N> endpointPair) {
                return AbstractValueGraph.this.edgeValue(endpointPair.nodeU(), endpointPair.nodeV());
            }
        });
    }
}
