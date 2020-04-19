package p015io.reactivex.internal.operators.observable;

import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.annotations.Nullable;
import p015io.reactivex.functions.Predicate;
import p015io.reactivex.internal.observers.BasicFuseableObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFilter */
public final class ObservableFilter<T> extends AbstractObservableWithUpstream<T, T> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFilter$FilterObserver */
    static final class FilterObserver<T> extends BasicFuseableObserver<T, T> {
        final Predicate<? super T> filter;

        FilterObserver(Observer<? super T> observer, Predicate<? super T> predicate) {
            super(observer);
            this.filter = predicate;
        }

        public void onNext(T t) {
            if (this.sourceMode == 0) {
                try {
                    if (this.filter.test(t)) {
                        this.downstream.onNext(t);
                    }
                } catch (Throwable th) {
                    fail(th);
                }
            } else {
                this.downstream.onNext(null);
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        @Nullable
        public T poll() throws Exception {
            T poll;
            do {
                poll = this.f366qd.poll();
                if (poll == null) {
                    break;
                }
            } while (!this.filter.test(poll));
            return poll;
        }
    }

    public ObservableFilter(ObservableSource<T> observableSource, Predicate<? super T> predicate2) {
        super(observableSource);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new FilterObserver(observer, this.predicate));
    }
}
