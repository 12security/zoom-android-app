package com.google.common.graph;

import java.util.Set;

abstract class ForwardingGraph<N> extends AbstractGraph<N> {
    /* access modifiers changed from: protected */
    public abstract Graph<N> delegate();

    ForwardingGraph() {
    }

    public Set<N> nodes() {
        return delegate().nodes();
    }

    public Set<EndpointPair<N>> edges() {
        return delegate().edges();
    }

    public boolean isDirected() {
        return delegate().isDirected();
    }

    public boolean allowsSelfLoops() {
        return delegate().allowsSelfLoops();
    }

    public ElementOrder<N> nodeOrder() {
        return delegate().nodeOrder();
    }

    public Set<N> adjacentNodes(Object obj) {
        return delegate().adjacentNodes(obj);
    }

    public Set<N> predecessors(Object obj) {
        return delegate().predecessors(obj);
    }

    public Set<N> successors(Object obj) {
        return delegate().successors(obj);
    }

    public int degree(Object obj) {
        return delegate().degree(obj);
    }

    public int inDegree(Object obj) {
        return delegate().inDegree(obj);
    }

    public int outDegree(Object obj) {
        return delegate().outDegree(obj);
    }
}
