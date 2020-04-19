package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Iterator;

final class ConfigurableMutableNetwork<N, E> extends ConfigurableNetwork<N, E> implements MutableNetwork<N, E> {
    ConfigurableMutableNetwork(NetworkBuilder<? super N, ? super E> networkBuilder) {
        super(networkBuilder);
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
    private NetworkConnections<N, E> addNodeInternal(N n) {
        NetworkConnections<N, E> newConnections = newConnections();
        Preconditions.checkState(this.nodeConnections.put(n, newConnections) == null);
        return newConnections;
    }

    @CanIgnoreReturnValue
    public boolean addEdge(N n, N n2, E e) {
        Preconditions.checkNotNull(n, "nodeU");
        Preconditions.checkNotNull(n2, "nodeV");
        Preconditions.checkNotNull(e, "edge");
        boolean z = false;
        if (containsEdge(e)) {
            EndpointPair incidentNodes = incidentNodes(e);
            EndpointPair of = EndpointPair.m212of((Network<?, ?>) this, n, n2);
            Preconditions.checkArgument(incidentNodes.equals(of), "Edge %s already exists between the following nodes: %s, so it cannot be reused to connect the following nodes: %s.", e, incidentNodes, of);
            return false;
        }
        NetworkConnections networkConnections = (NetworkConnections) this.nodeConnections.get(n);
        if (!allowsParallelEdges()) {
            if (networkConnections == null || !networkConnections.successors().contains(n2)) {
                z = true;
            }
            Preconditions.checkArgument(z, "Nodes %s and %s are already connected by a different edge. To construct a graph that allows parallel edges, call allowsParallelEdges(true) on the Builder.", (Object) n, (Object) n2);
        }
        boolean equals = n.equals(n2);
        if (!allowsSelfLoops()) {
            Preconditions.checkArgument(!equals, "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", (Object) n);
        }
        if (networkConnections == null) {
            networkConnections = addNodeInternal(n);
        }
        networkConnections.addOutEdge(e, n2);
        NetworkConnections networkConnections2 = (NetworkConnections) this.nodeConnections.get(n2);
        if (networkConnections2 == null) {
            networkConnections2 = addNodeInternal(n2);
        }
        networkConnections2.addInEdge(e, n, equals);
        this.edgeToReferenceNode.put(e, n);
        return true;
    }

    @CanIgnoreReturnValue
    public boolean removeNode(Object obj) {
        Preconditions.checkNotNull(obj, "node");
        NetworkConnections networkConnections = (NetworkConnections) this.nodeConnections.get(obj);
        if (networkConnections == null) {
            return false;
        }
        Iterator it = ImmutableList.copyOf((Collection<? extends E>) networkConnections.incidentEdges()).iterator();
        while (it.hasNext()) {
            removeEdge(it.next());
        }
        this.nodeConnections.remove(obj);
        return true;
    }

    @CanIgnoreReturnValue
    public boolean removeEdge(Object obj) {
        Preconditions.checkNotNull(obj, "edge");
        Object obj2 = this.edgeToReferenceNode.get(obj);
        boolean z = false;
        if (obj2 == null) {
            return false;
        }
        NetworkConnections networkConnections = (NetworkConnections) this.nodeConnections.get(obj2);
        Object oppositeNode = networkConnections.oppositeNode(obj);
        NetworkConnections networkConnections2 = (NetworkConnections) this.nodeConnections.get(oppositeNode);
        networkConnections.removeOutEdge(obj);
        if (allowsSelfLoops() && obj2.equals(oppositeNode)) {
            z = true;
        }
        networkConnections2.removeInEdge(obj, z);
        this.edgeToReferenceNode.remove(obj);
        return true;
    }

    private NetworkConnections<N, E> newConnections() {
        return isDirected() ? allowsParallelEdges() ? DirectedMultiNetworkConnections.m209of() : DirectedNetworkConnections.m210of() : allowsParallelEdges() ? UndirectedMultiNetworkConnections.m215of() : UndirectedNetworkConnections.m216of();
    }
}
