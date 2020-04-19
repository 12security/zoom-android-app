package p015io.reactivex.internal.operators.single;

import java.util.Arrays;
import java.util.NoSuchElementException;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.SingleSource;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Function;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.single.SingleZipIterable */
public final class SingleZipIterable<T, R> extends Single<R> {
    final Iterable<? extends SingleSource<? extends T>> sources;
    final Function<? super Object[], ? extends R> zipper;

    /* renamed from: io.reactivex.internal.operators.single.SingleZipIterable$SingletonArrayFunc */
    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return ObjectHelper.requireNonNull(SingleZipIterable.this.zipper.apply(new Object[]{t}), "The zipper returned a null value");
        }
    }

    public SingleZipIterable(Iterable<? extends SingleSource<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        this.sources = iterable;
        this.zipper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> singleObserver) {
        SingleSource[] singleSourceArr = new SingleSource[8];
        try {
            SingleSource[] singleSourceArr2 = singleSourceArr;
            int i = 0;
            for (SingleSource singleSource : this.sources) {
                if (singleSource == null) {
                    EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), singleObserver);
                    return;
                }
                if (i == singleSourceArr2.length) {
                    singleSourceArr2 = (SingleSource[]) Arrays.copyOf(singleSourceArr2, (i >> 2) + i);
                }
                int i2 = i + 1;
                singleSourceArr2[i] = singleSource;
                i = i2;
            }
            if (i == 0) {
                EmptyDisposable.error((Throwable) new NoSuchElementException(), singleObserver);
            } else if (i == 1) {
                singleSourceArr2[0].subscribe(new MapSingleObserver(singleObserver, new SingletonArrayFunc()));
            } else {
                ZipCoordinator zipCoordinator = new ZipCoordinator(singleObserver, i, this.zipper);
                singleObserver.onSubscribe(zipCoordinator);
                for (int i3 = 0; i3 < i && !zipCoordinator.isDisposed(); i3++) {
                    singleSourceArr2[i3].subscribe(zipCoordinator.observers[i3]);
                }
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
