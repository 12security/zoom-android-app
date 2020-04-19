package p015io.reactivex.schedulers;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.schedulers.SchedulerRunnableIntrospection */
public interface SchedulerRunnableIntrospection {
    @NonNull
    Runnable getWrappedRunnable();
}
