package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import java.util.Set;

@Beta
public abstract class ImmutableGraph<N> extends ForwardingGraph<N> {

    static class ValueBackedImpl<N, V> extends ImmutableGraph<N> {
        protected final ValueGraph<N, V> backingValueGraph;

        ValueBackedImpl(AbstractGraphBuilder<? super N> abstractGraphBuilder, ImmutableMap<N, GraphConnections<N, V>> immutableMap, long j) {
            this.backingValueGraph = new ConfigurableValueGraph(abstractGraphBuilder, immutableMap, j);
        }

        /* access modifiers changed from: protected */
        public Graph<N> delegate() {
            return this.backingValueGraph;
        }
    }

    public /* bridge */ /* synthetic */ Set adjacentNodes(Object obj) {
        return super.adjacentNodes(obj);
    }

    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    public /* bridge */ /* synthetic */ int degree(Object obj) {
        return super.degree(obj);
    }

    public /* bridge */ /* synthetic */ Set edges() {
        return super.edges();
    }

    public /* bridge */ /* synthetic */ int inDegree(Object obj) {
        return super.inDegree(obj);
    }

    public /* bridge */ /* synthetic */ boolean isDirected() {
        return super.isDirected();
    }

    public /* bridge */ /* synthetic */ ElementOrder nodeOrder() {
        return super.nodeOrder();
    }

    public /* bridge */ /* synthetic */ Set nodes() {
        return super.nodes();
    }

    public /* bridge */ /* synthetic */ int outDegree(Object obj) {
        return super.outDegree(obj);
    }

    public /* bridge */ /* synthetic */ Set predecessors(Object obj) {
        return super.predecessors(obj);
    }

    public /* bridge */ /* synthetic */ Set successors(Object obj) {
        return super.successors(obj);
    }

    ImmutableGraph() {
    }

    public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
        return graph instanceof ImmutableGraph ? (ImmutableGraph) graph : new ValueBackedImpl(GraphBuilder.from(graph), getNodeConnections(graph), (long) graph.edges().size());
    }

    @Deprecated
    public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> immutableGraph) {
        return (ImmutableGraph) Preconditions.checkNotNull(immutableGraph);
    }

    private static <N> ImmutableMap<N, GraphConnections<N, Presence>> getNodeConnections(Graph<N> graph) {
        Builder builder = ImmutableMap.builder();
        for (Object next : graph.nodes()) {
            builder.put(next, connectionsOf(graph, next));
        }
        return builder.build();
    }

    private static <N> GraphConnections<N, Presence> connectionsOf(Graph<N> graph, N n) {
        Function constant = Functions.constant(Presence.EDGE_EXISTS);
        return graph.isDirected() ? DirectedGraphConnections.ofImmutable(graph.predecessors(n), Maps.asMap(graph.successors(n), constant)) : UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(n), constant));
    }
}
