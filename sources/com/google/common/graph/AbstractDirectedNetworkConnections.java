package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

abstract class AbstractDirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
    protected final Map<E, N> inEdgeMap;
    protected final Map<E, N> outEdgeMap;
    /* access modifiers changed from: private */
    public int selfLoopCount;

    protected AbstractDirectedNetworkConnections(Map<E, N> map, Map<E, N> map2, int i) {
        this.inEdgeMap = (Map) Preconditions.checkNotNull(map);
        this.outEdgeMap = (Map) Preconditions.checkNotNull(map2);
        this.selfLoopCount = Graphs.checkNonNegative(i);
        Preconditions.checkState(i <= map.size() && i <= map2.size());
    }

    public Set<N> adjacentNodes() {
        return Sets.union(predecessors(), successors());
    }

    public Set<E> incidentEdges() {
        return new AbstractSet<E>() {
            public UnmodifiableIterator<E> iterator() {
                return Iterators.unmodifiableIterator((AbstractDirectedNetworkConnections.this.selfLoopCount == 0 ? Iterables.concat(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()) : Sets.union(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet())).iterator());
            }

            public int size() {
                return IntMath.saturatedAdd(AbstractDirectedNetworkConnections.this.inEdgeMap.size(), AbstractDirectedNetworkConnections.this.outEdgeMap.size() - AbstractDirectedNetworkConnections.this.selfLoopCount);
            }

            public boolean contains(@Nullable Object obj) {
                return AbstractDirectedNetworkConnections.this.inEdgeMap.containsKey(obj) || AbstractDirectedNetworkConnections.this.outEdgeMap.containsKey(obj);
            }
        };
    }

    public Set<E> inEdges() {
        return Collections.unmodifiableSet(this.inEdgeMap.keySet());
    }

    public Set<E> outEdges() {
        return Collections.unmodifiableSet(this.outEdgeMap.keySet());
    }

    public N oppositeNode(Object obj) {
        return Preconditions.checkNotNull(this.outEdgeMap.get(obj));
    }

    public N removeInEdge(Object obj, boolean z) {
        if (z) {
            int i = this.selfLoopCount - 1;
            this.selfLoopCount = i;
            Graphs.checkNonNegative(i);
        }
        return Preconditions.checkNotNull(this.inEdgeMap.remove(obj));
    }

    public N removeOutEdge(Object obj) {
        return Preconditions.checkNotNull(this.outEdgeMap.remove(obj));
    }

    public void addInEdge(E e, N n, boolean z) {
        boolean z2 = true;
        if (z) {
            int i = this.selfLoopCount + 1;
            this.selfLoopCount = i;
            Graphs.checkPositive(i);
        }
        if (this.inEdgeMap.put(e, n) != null) {
            z2 = false;
        }
        Preconditions.checkState(z2);
    }

    public void addOutEdge(E e, N n) {
        Preconditions.checkState(this.outEdgeMap.put(e, n) == null);
    }
}
