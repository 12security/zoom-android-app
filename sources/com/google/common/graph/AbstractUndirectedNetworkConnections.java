package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

abstract class AbstractUndirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
    protected final Map<E, N> incidentEdgeMap;

    protected AbstractUndirectedNetworkConnections(Map<E, N> map) {
        this.incidentEdgeMap = (Map) Preconditions.checkNotNull(map);
    }

    public Set<N> predecessors() {
        return adjacentNodes();
    }

    public Set<N> successors() {
        return adjacentNodes();
    }

    public Set<E> incidentEdges() {
        return Collections.unmodifiableSet(this.incidentEdgeMap.keySet());
    }

    public Set<E> inEdges() {
        return incidentEdges();
    }

    public Set<E> outEdges() {
        return incidentEdges();
    }

    public N oppositeNode(Object obj) {
        return Preconditions.checkNotNull(this.incidentEdgeMap.get(obj));
    }

    public N removeInEdge(Object obj, boolean z) {
        if (!z) {
            return removeOutEdge(obj);
        }
        return null;
    }

    public N removeOutEdge(Object obj) {
        return Preconditions.checkNotNull(this.incidentEdgeMap.remove(obj));
    }

    public void addInEdge(E e, N n, boolean z) {
        if (!z) {
            addOutEdge(e, n);
        }
    }

    public void addOutEdge(E e, N n) {
        Preconditions.checkState(this.incidentEdgeMap.put(e, n) == null);
    }
}
