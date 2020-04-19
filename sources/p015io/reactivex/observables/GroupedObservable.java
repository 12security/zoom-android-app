package p015io.reactivex.observables;

import p015io.reactivex.Observable;
import p015io.reactivex.annotations.Nullable;

/* renamed from: io.reactivex.observables.GroupedObservable */
public abstract class GroupedObservable<K, T> extends Observable<T> {
    final K key;

    protected GroupedObservable(@Nullable K k) {
        this.key = k;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }
}
