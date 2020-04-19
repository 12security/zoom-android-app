package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.FlowableOnSubscribe */
public interface FlowableOnSubscribe<T> {
    void subscribe(@NonNull FlowableEmitter<T> flowableEmitter) throws Exception;
}
