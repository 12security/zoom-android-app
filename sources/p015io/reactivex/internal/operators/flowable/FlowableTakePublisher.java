package p015io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p015io.reactivex.Flowable;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTakePublisher */
public final class FlowableTakePublisher<T> extends Flowable<T> {
    final long limit;
    final Publisher<T> source;

    public FlowableTakePublisher(Publisher<T> publisher, long j) {
        this.source = publisher;
        this.limit = j;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new TakeSubscriber(subscriber, this.limit));
    }
}
