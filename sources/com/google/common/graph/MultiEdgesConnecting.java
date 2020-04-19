package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

abstract class MultiEdgesConnecting<E> extends AbstractSet<E> {
    private final Map<E, ?> outEdgeToNode;
    /* access modifiers changed from: private */
    public final Object targetNode;

    MultiEdgesConnecting(Map<E, ?> map, Object obj) {
        this.outEdgeToNode = (Map) Preconditions.checkNotNull(map);
        this.targetNode = Preconditions.checkNotNull(obj);
    }

    public UnmodifiableIterator<E> iterator() {
        final Iterator it = this.outEdgeToNode.entrySet().iterator();
        return new AbstractIterator<E>() {
            /* access modifiers changed from: protected */
            public E computeNext() {
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    if (MultiEdgesConnecting.this.targetNode.equals(entry.getValue())) {
                        return entry.getKey();
                    }
                }
                return endOfData();
            }
        };
    }

    public boolean contains(@Nullable Object obj) {
        return this.targetNode.equals(this.outEdgeToNode.get(obj));
    }
}
