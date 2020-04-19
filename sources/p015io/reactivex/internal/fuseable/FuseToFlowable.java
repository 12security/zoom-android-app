package p015io.reactivex.internal.fuseable;

import p015io.reactivex.Flowable;

/* renamed from: io.reactivex.internal.fuseable.FuseToFlowable */
public interface FuseToFlowable<T> {
    Flowable<T> fuseToFlowable();
}
