package com.google.common.graph;

final class ConfigurableMutableGraph<N> extends ForwardingGraph<N> implements MutableGraph<N> {
    private final MutableValueGraph<N, Presence> backingValueGraph;

    ConfigurableMutableGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder) {
        this.backingValueGraph = new ConfigurableMutableValueGraph(abstractGraphBuilder);
    }

    /* access modifiers changed from: protected */
    public Graph<N> delegate() {
        return this.backingValueGraph;
    }

    public boolean addNode(N n) {
        return this.backingValueGraph.addNode(n);
    }

    public boolean putEdge(N n, N n2) {
        return this.backingValueGraph.putEdgeValue(n, n2, Presence.EDGE_EXISTS) == null;
    }

    public boolean removeNode(Object obj) {
        return this.backingValueGraph.removeNode(obj);
    }

    public boolean removeEdge(Object obj, Object obj2) {
        return this.backingValueGraph.removeEdge(obj, obj2) != null;
    }
}
