package p015io.reactivex.internal.operators.observable;

import java.util.Collection;
import java.util.concurrent.Callable;
import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.annotations.Nullable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Function;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.observers.BasicFuseableObserver;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDistinct */
public final class ObservableDistinct<T, K> extends AbstractObservableWithUpstream<T, T> {
    final Callable<? extends Collection<? super K>> collectionSupplier;
    final Function<? super T, K> keySelector;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDistinct$DistinctObserver */
    static final class DistinctObserver<T, K> extends BasicFuseableObserver<T, T> {
        final Collection<? super K> collection;
        final Function<? super T, K> keySelector;

        DistinctObserver(Observer<? super T> observer, Function<? super T, K> function, Collection<? super K> collection2) {
            super(observer);
            this.keySelector = function;
            this.collection = collection2;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode == 0) {
                    try {
                        if (this.collection.add(ObjectHelper.requireNonNull(this.keySelector.apply(t), "The keySelector returned a null key"))) {
                            this.downstream.onNext(t);
                        }
                    } catch (Throwable th) {
                        fail(th);
                    }
                } else {
                    this.downstream.onNext(null);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.collection.clear();
            this.downstream.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.collection.clear();
                this.downstream.onComplete();
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
            } while (!this.collection.add(ObjectHelper.requireNonNull(this.keySelector.apply(poll), "The keySelector returned a null key")));
            return poll;
        }

        public void clear() {
            this.collection.clear();
            super.clear();
        }
    }

    public ObservableDistinct(ObservableSource<T> observableSource, Function<? super T, K> function, Callable<? extends Collection<? super K>> callable) {
        super(observableSource);
        this.keySelector = function;
        this.collectionSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        try {
            this.source.subscribe(new DistinctObserver(observer, this.keySelector, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}