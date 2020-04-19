package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

interface NetworkConnections<N, E> {
    void addInEdge(E e, N n, boolean z);

    void addOutEdge(E e, N n);

    Set<N> adjacentNodes();

    Set<E> edgesConnecting(Object obj);

    Set<E> inEdges();

    Set<E> incidentEdges();

    N oppositeNode(Object obj);

    Set<E> outEdges();

    Set<N> predecessors();

    @CanIgnoreReturnValue
    N removeInEdge(Object obj, boolean z);

    @CanIgnoreReturnValue
    N removeOutEdge(Object obj);

    Set<N> successors();
}
