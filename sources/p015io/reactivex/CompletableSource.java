package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.CompletableSource */
public interface CompletableSource {
    void subscribe(@NonNull CompletableObserver completableObserver);
}
