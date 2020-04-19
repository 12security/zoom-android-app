package p015io.reactivex.internal.fuseable;

import p015io.reactivex.SingleSource;

/* renamed from: io.reactivex.internal.fuseable.HasUpstreamSingleSource */
public interface HasUpstreamSingleSource<T> {
    SingleSource<T> source();
}
