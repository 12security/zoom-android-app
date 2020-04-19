package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

class ConfigurableNetwork<N, E> extends AbstractNetwork<N, E> {
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<E> edgeOrder;
    protected final MapIteratorCache<E, N> edgeToReferenceNode;
    private final boolean isDirected;
    protected final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    ConfigurableNetwork(NetworkBuilder<? super N, ? super E> networkBuilder) {
        this(networkBuilder, networkBuilder.nodeOrder.createMap(((Integer) networkBuilder.expectedNodeCount.mo29274or(Integer.valueOf(10))).intValue()), networkBuilder.edgeOrder.createMap(((Integer) networkBuilder.expectedEdgeCount.mo29274or(Integer.valueOf(20))).intValue()));
    }

    ConfigurableNetwork(NetworkBuilder<? super N, ? super E> networkBuilder, Map<N, NetworkConnections<N, E>> map, Map<E, N> map2) {
        this.isDirected = networkBuilder.directed;
        this.allowsParallelEdges = networkBuilder.allowsParallelEdges;
        this.allowsSelfLoops = networkBuilder.allowsSelfLoops;
        this.nodeOrder = networkBuilder.nodeOrder.cast();
        this.edgeOrder = networkBuilder.edgeOrder.cast();
        this.nodeConnections = map instanceof TreeMap ? new MapRetrievalCache<>(map) : new MapIteratorCache<>(map);
        this.edgeToReferenceNode = new MapIteratorCache<>(map2);
    }

    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    public Set<E> edges() {
        return this.edgeToReferenceNode.unmodifiableKeySet();
    }

    public boolean isDirected() {
        return this.isDirected;
    }

    public boolean allowsParallelEdges() {
        return this.allowsParallelEdges;
    }

    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    public ElementOrder<E> edgeOrder() {
        return this.edgeOrder;
    }

    public Set<E> incidentEdges(Object obj) {
        return checkedConnections(obj).incidentEdges();
    }

    public EndpointPair<N> incidentNodes(Object obj) {
        Object checkedReferenceNode = checkedReferenceNode(obj);
        return EndpointPair.m212of((Network<?, ?>) this, checkedReferenceNode, ((NetworkConnections) this.nodeConnections.get(checkedReferenceNode)).oppositeNode(obj));
    }

    public Set<N> adjacentNodes(Object obj) {
        return checkedConnections(obj).adjacentNodes();
    }

    public Set<E> edgesConnecting(Object obj, Object obj2) {
        NetworkConnections checkedConnections = checkedConnections(obj);
        if (!this.allowsSelfLoops && obj == obj2) {
            return ImmutableSet.m155of();
        }
        Preconditions.checkArgument(containsNode(obj2), "Node %s is not an element of this graph.", obj2);
        return checkedConnections.edgesConnecting(obj2);
    }

    public Set<E> inEdges(Object obj) {
        return checkedConnections(obj).inEdges();
    }

    public Set<E> outEdges(Object obj) {
        return checkedConnections(obj).outEdges();
    }

    public Set<N> predecessors(Object obj) {
        return checkedConnections(obj).predecessors();
    }

    public Set<N> successors(Object obj) {
        return checkedConnections(obj).successors();
    }

    /* access modifiers changed from: protected */
    public final NetworkConnections<N, E> checkedConnections(Object obj) {
        NetworkConnections<N, E> networkConnections = (NetworkConnections) this.nodeConnections.get(obj);
        if (networkConnections != null) {
            return networkConnections;
        }
        Preconditions.checkNotNull(obj);
        throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[]{obj}));
    }

    /* access modifiers changed from: protected */
    public final N checkedReferenceNode(Object obj) {
        N n = this.edgeToReferenceNode.get(obj);
        if (n != null) {
            return n;
        }
        Preconditions.checkNotNull(obj);
        throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", new Object[]{obj}));
    }

    /* access modifiers changed from: protected */
    public final boolean containsNode(@Nullable Object obj) {
        return this.nodeConnections.containsKey(obj);
    }

    /* access modifiers changed from: protected */
    public final boolean containsEdge(@Nullable Object obj) {
        return this.edgeToReferenceNode.containsKey(obj);
    }
}
