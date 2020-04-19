package p015io.reactivex.flowables;

import p015io.reactivex.Flowable;
import p015io.reactivex.annotations.Nullable;

/* renamed from: io.reactivex.flowables.GroupedFlowable */
public abstract class GroupedFlowable<K, T> extends Flowable<T> {
    final K key;

    protected GroupedFlowable(@Nullable K k) {
        this.key = k;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }
}
