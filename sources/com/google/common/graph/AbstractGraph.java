package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractGraph<N> implements Graph<N> {
    /* access modifiers changed from: protected */
    public long edgeCount() {
        long j = 0;
        for (Object degree : nodes()) {
            j += (long) degree(degree);
        }
        Preconditions.checkState((1 & j) == 0);
        return j >>> 1;
    }

    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>() {
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return EndpointPairIterator.m213of(AbstractGraph.this);
            }

            public int size() {
                return Ints.saturatedCast(AbstractGraph.this.edgeCount());
            }

            public boolean contains(@Nullable Object obj) {
                boolean z = false;
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                EndpointPair endpointPair = (EndpointPair) obj;
                if (AbstractGraph.this.isDirected() == endpointPair.isOrdered() && AbstractGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV())) {
                    z = true;
                }
                return z;
            }
        };
    }

    public int degree(Object obj) {
        if (isDirected()) {
            return IntMath.saturatedAdd(predecessors(obj).size(), successors(obj).size());
        }
        Set adjacentNodes = adjacentNodes(obj);
        return IntMath.saturatedAdd(adjacentNodes.size(), (!allowsSelfLoops() || !adjacentNodes.contains(obj)) ? 0 : 1);
    }

    public int inDegree(Object obj) {
        return isDirected() ? predecessors(obj).size() : degree(obj);
    }

    public int outDegree(Object obj) {
        return isDirected() ? successors(obj).size() : degree(obj);
    }

    public String toString() {
        return String.format("%s, nodes: %s, edges: %s", new Object[]{String.format("isDirected: %s, allowsSelfLoops: %s", new Object[]{Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops())}), nodes(), edges()});
    }
}
