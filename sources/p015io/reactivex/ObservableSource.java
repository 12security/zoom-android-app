package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.ObservableSource */
public interface ObservableSource<T> {
    void subscribe(@NonNull Observer<? super T> observer);
}
