package p015io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import org.reactivestreams.Subscriber;
import p015io.reactivex.Flowable;
import p015io.reactivex.annotations.Nullable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.fuseable.ConditionalSubscriber;
import p015io.reactivex.internal.subscriptions.BasicQueueSubscription;
import p015io.reactivex.internal.subscriptions.EmptySubscription;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable */
public final class FlowableFromIterable<T> extends Flowable<T> {
    final Iterable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$BaseRangeSubscription */
    static abstract class BaseRangeSubscription<T> extends BasicQueueSubscription<T> {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;

        /* renamed from: it */
        Iterator<? extends T> f390it;
        boolean once;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        public final int requestFusion(int i) {
            return i & 1;
        }

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseRangeSubscription(Iterator<? extends T> it) {
            this.f390it = it;
        }

        @Nullable
        public final T poll() {
            Iterator<? extends T> it = this.f390it;
            if (it == null) {
                return null;
            }
            if (!this.once) {
                this.once = true;
            } else if (!it.hasNext()) {
                return null;
            }
            return ObjectHelper.requireNonNull(this.f390it.next(), "Iterator.next() returned a null value");
        }

        public final boolean isEmpty() {
            Iterator<? extends T> it = this.f390it;
            return it == null || !it.hasNext();
        }

        public final void clear() {
            this.f390it = null;
        }

        public final void request(long j) {
            if (SubscriptionHelper.validate(j) && BackpressureHelper.add(this, j) == 0) {
                if (j == Long.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(j);
                }
            }
        }

        public final void cancel() {
            this.cancelled = true;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$IteratorConditionalSubscription */
    static final class IteratorConditionalSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final ConditionalSubscriber<? super T> downstream;

        IteratorConditionalSubscription(ConditionalSubscriber<? super T> conditionalSubscriber, Iterator<? extends T> it) {
            super(it);
            this.downstream = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator it = this.f390it;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
            while (!this.cancelled) {
                try {
                    Object next = it.next();
                    if (!this.cancelled) {
                        if (next == null) {
                            conditionalSubscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        conditionalSubscriber.tryOnNext(next);
                        if (!this.cancelled) {
                            try {
                                if (!it.hasNext()) {
                                    if (!this.cancelled) {
                                        conditionalSubscriber.onComplete();
                                    }
                                    return;
                                }
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                conditionalSubscriber.onError(th);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    conditionalSubscriber.onError(th2);
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Iterator it = this.f390it;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
            long j2 = j;
            long j3 = 0;
            while (true) {
                if (j3 == j2) {
                    j2 = get();
                    if (j3 == j2) {
                        j2 = addAndGet(-j3);
                        if (j2 != 0) {
                            j3 = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!this.cancelled) {
                    try {
                        Object next = it.next();
                        if (!this.cancelled) {
                            if (next == null) {
                                conditionalSubscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                                return;
                            }
                            boolean tryOnNext = conditionalSubscriber.tryOnNext(next);
                            if (!this.cancelled) {
                                try {
                                    if (!it.hasNext()) {
                                        if (!this.cancelled) {
                                            conditionalSubscriber.onComplete();
                                        }
                                        return;
                                    } else if (tryOnNext) {
                                        j3++;
                                    }
                                } catch (Throwable th) {
                                    Exceptions.throwIfFatal(th);
                                    conditionalSubscriber.onError(th);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        conditionalSubscriber.onError(th2);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$IteratorSubscription */
    static final class IteratorSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final Subscriber<? super T> downstream;

        IteratorSubscription(Subscriber<? super T> subscriber, Iterator<? extends T> it) {
            super(it);
            this.downstream = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator it = this.f390it;
            Subscriber<? super T> subscriber = this.downstream;
            while (!this.cancelled) {
                try {
                    Object next = it.next();
                    if (!this.cancelled) {
                        if (next == null) {
                            subscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        subscriber.onNext(next);
                        if (!this.cancelled) {
                            try {
                                if (!it.hasNext()) {
                                    if (!this.cancelled) {
                                        subscriber.onComplete();
                                    }
                                    return;
                                }
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                subscriber.onError(th);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    subscriber.onError(th2);
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Iterator it = this.f390it;
            Subscriber<? super T> subscriber = this.downstream;
            long j2 = j;
            long j3 = 0;
            while (true) {
                if (j3 == j2) {
                    j2 = get();
                    if (j3 == j2) {
                        j2 = addAndGet(-j3);
                        if (j2 != 0) {
                            j3 = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!this.cancelled) {
                    try {
                        Object next = it.next();
                        if (!this.cancelled) {
                            if (next == null) {
                                subscriber.onError(new NullPointerException("Iterator.next() returned a null value"));
                                return;
                            }
                            subscriber.onNext(next);
                            if (!this.cancelled) {
                                try {
                                    if (!it.hasNext()) {
                                        if (!this.cancelled) {
                                            subscriber.onComplete();
                                        }
                                        return;
                                    }
                                    j3++;
                                } catch (Throwable th) {
                                    Exceptions.throwIfFatal(th);
                                    subscriber.onError(th);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        subscriber.onError(th2);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    public FlowableFromIterable(Iterable<? extends T> iterable) {
        this.source = iterable;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        try {
            subscribe(subscriber, this.source.iterator());
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }

    public static <T> void subscribe(Subscriber<? super T> subscriber, Iterator<? extends T> it) {
        try {
            if (!it.hasNext()) {
                EmptySubscription.complete(subscriber);
                return;
            }
            if (subscriber instanceof ConditionalSubscriber) {
                subscriber.onSubscribe(new IteratorConditionalSubscription((ConditionalSubscriber) subscriber, it));
            } else {
                subscriber.onSubscribe(new IteratorSubscription(subscriber, it));
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
