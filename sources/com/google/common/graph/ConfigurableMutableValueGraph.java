package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

final class ConfigurableMutableValueGraph<N, V> extends ConfigurableValueGraph<N, V> implements MutableValueGraph<N, V> {
    ConfigurableMutableValueGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder) {
        super(abstractGraphBuilder);
    }

    @CanIgnoreReturnValue
    public boolean addNode(N n) {
        Preconditions.checkNotNull(n, "node");
        if (containsNode(n)) {
            return false;
        }
        addNodeInternal(n);
        return true;
    }

    @CanIgnoreReturnValue
    private GraphConnections<N, V> addNodeInternal(N n) {
        GraphConnections<N, V> newConnections = newConnections();
        Preconditions.checkState(this.nodeConnections.put(n, newConnections) == null);
        return newConnections;
    }

    @CanIgnoreReturnValue
    public V putEdgeValue(N n, N n2, V v) {
        Preconditions.checkNotNull(n, "nodeU");
        Preconditions.checkNotNull(n2, "nodeV");
        Preconditions.checkNotNull(v, "value");
        if (!allowsSelfLoops()) {
            Preconditions.checkArgument(!n.equals(n2), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", (Object) n);
        }
        GraphConnections graphConnections = (GraphConnections) this.nodeConnections.get(n);
        if (graphConnections == null) {
            graphConnections = addNodeInternal(n);
        }
        V addSuccessor = graphConnections.addSuccessor(n2, v);
        GraphConnections graphConnections2 = (GraphConnections) this.nodeConnections.get(n2);
        if (graphConnections2 == null) {
            graphConnections2 = addNodeInternal(n2);
        }
        graphConnections2.addPredecessor(n, v);
        if (addSuccessor == null) {
            long j = this.edgeCount + 1;
            this.edgeCount = j;
            Graphs.checkPositive(j);
        }
        return addSuccessor;
    }

    @CanIgnoreReturnValue
    public boolean removeNode(Object obj) {
        Preconditions.checkNotNull(obj, "node");
        GraphConnections graphConnections = (GraphConnections) this.nodeConnections.get(obj);
        if (graphConnections == null) {
            return false;
        }
        if (allowsSelfLoops() && graphConnections.removeSuccessor(obj) != null) {
            graphConnections.removePredecessor(obj);
            this.edgeCount--;
        }
        for (Object withoutCaching : graphConnections.successors()) {
            ((GraphConnections) this.nodeConnections.getWithoutCaching(withoutCaching)).removePredecessor(obj);
            this.edgeCount--;
        }
        if (isDirected()) {
            for (Object withoutCaching2 : graphConnections.predecessors()) {
                Preconditions.checkState(((GraphConnections) this.nodeConnections.getWithoutCaching(withoutCaching2)).removeSuccessor(obj) != null);
                this.edgeCount--;
            }
        }
        this.nodeConnections.remove(obj);
        Graphs.checkNonNegative(this.edgeCount);
        return true;
    }

    @CanIgnoreReturnValue
    public V removeEdge(Object obj, Object obj2) {
        Preconditions.checkNotNull(obj, "nodeU");
        Preconditions.checkNotNull(obj2, "nodeV");
        GraphConnections graphConnections = (GraphConnections) this.nodeConnections.get(obj);
        GraphConnections graphConnections2 = (GraphConnections) this.nodeConnections.get(obj2);
        if (graphConnections == null || graphConnections2 == null) {
            return null;
        }
        V removeSuccessor = graphConnections.removeSuccessor(obj2);
        if (removeSuccessor != null) {
            graphConnections2.removePredecessor(obj);
            long j = this.edgeCount - 1;
            this.edgeCount = j;
            Graphs.checkNonNegative(j);
        }
        return removeSuccessor;
    }

    private GraphConnections<N, V> newConnections() {
        return isDirected() ? DirectedGraphConnections.m208of() : UndirectedGraphConnections.m214of();
    }
}
