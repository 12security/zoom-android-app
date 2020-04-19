package p015io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import p015io.reactivex.Flowable;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.internal.util.ExceptionHelper;
import p015io.reactivex.internal.util.NotificationLite;
import p015io.reactivex.subscribers.DefaultSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent */
public final class BlockingFlowableMostRecent<T> implements Iterable<T> {
    final T initialValue;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent$MostRecentSubscriber */
    static final class MostRecentSubscriber<T> extends DefaultSubscriber<T> {
        volatile Object value;

        /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent$MostRecentSubscriber$Iterator */
        final class Iterator implements java.util.Iterator<T> {
            private Object buf;

            Iterator() {
            }

            public boolean hasNext() {
                this.buf = MostRecentSubscriber.this.value;
                return !NotificationLite.isComplete(this.buf);
            }

            public T next() {
                Object obj = null;
                try {
                    if (this.buf == null) {
                        obj = MostRecentSubscriber.this.value;
                    }
                    if (NotificationLite.isComplete(this.buf)) {
                        throw new NoSuchElementException();
                    } else if (!NotificationLite.isError(this.buf)) {
                        T value = NotificationLite.getValue(this.buf);
                        this.buf = obj;
                        return value;
                    } else {
                        throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
                    }
                } finally {
                    this.buf = obj;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Read only iterator");
            }
        }

        MostRecentSubscriber(T t) {
            this.value = NotificationLite.next(t);
        }

        public void onComplete() {
            this.value = NotificationLite.complete();
        }

        public void onError(Throwable th) {
            this.value = NotificationLite.error(th);
        }

        public void onNext(T t) {
            this.value = NotificationLite.next(t);
        }

        public Iterator getIterable() {
            return new Iterator<>();
        }
    }

    public BlockingFlowableMostRecent(Flowable<T> flowable, T t) {
        this.source = flowable;
        this.initialValue = t;
    }

    public Iterator<T> iterator() {
        MostRecentSubscriber mostRecentSubscriber = new MostRecentSubscriber(this.initialValue);
        this.source.subscribe((FlowableSubscriber<? super T>) mostRecentSubscriber);
        return mostRecentSubscriber.getIterable();
    }
}
