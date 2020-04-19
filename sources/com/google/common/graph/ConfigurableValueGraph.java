package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

class ConfigurableValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean allowsSelfLoops;
    protected long edgeCount;
    private final boolean isDirected;
    protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    ConfigurableValueGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder) {
        this(abstractGraphBuilder, abstractGraphBuilder.nodeOrder.createMap(((Integer) abstractGraphBuilder.expectedNodeCount.mo29274or(Integer.valueOf(10))).intValue()), 0);
    }

    ConfigurableValueGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder, Map<N, GraphConnections<N, V>> map, long j) {
        this.isDirected = abstractGraphBuilder.directed;
        this.allowsSelfLoops = abstractGraphBuilder.allowsSelfLoops;
        this.nodeOrder = abstractGraphBuilder.nodeOrder.cast();
        this.nodeConnections = map instanceof TreeMap ? new MapRetrievalCache<>(map) : new MapIteratorCache<>(map);
        this.edgeCount = Graphs.checkNonNegative(j);
    }

    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    public boolean isDirected() {
        return this.isDirected;
    }

    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    public Set<N> adjacentNodes(Object obj) {
        return checkedConnections(obj).adjacentNodes();
    }

    public Set<N> predecessors(Object obj) {
        return checkedConnections(obj).predecessors();
    }

    public Set<N> successors(Object obj) {
        return checkedConnections(obj).successors();
    }

    public V edgeValueOrDefault(Object obj, Object obj2, @Nullable V v) {
        GraphConnections graphConnections = (GraphConnections) this.nodeConnections.get(obj);
        if (graphConnections == null) {
            return v;
        }
        V value = graphConnections.value(obj2);
        return value == null ? v : value;
    }

    /* access modifiers changed from: protected */
    public long edgeCount() {
        return this.edgeCount;
    }

    /* access modifiers changed from: protected */
    public final GraphConnections<N, V> checkedConnections(Object obj) {
        GraphConnections<N, V> graphConnections = (GraphConnections) this.nodeConnections.get(obj);
        if (graphConnections != null) {
            return graphConnections;
        }
        Preconditions.checkNotNull(obj);
        throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[]{obj}));
    }

    /* access modifiers changed from: protected */
    public final boolean containsNode(@Nullable Object obj) {
        return this.nodeConnections.containsKey(obj);
    }
}
