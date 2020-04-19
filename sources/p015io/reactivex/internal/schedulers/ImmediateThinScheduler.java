package p015io.reactivex.internal.schedulers;

import java.util.concurrent.TimeUnit;
import p015io.reactivex.Scheduler;
import p015io.reactivex.Scheduler.Worker;
import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.disposables.Disposables;

/* renamed from: io.reactivex.internal.schedulers.ImmediateThinScheduler */
public final class ImmediateThinScheduler extends Scheduler {
    static final Disposable DISPOSED = Disposables.empty();
    public static final Scheduler INSTANCE = new ImmediateThinScheduler();
    static final Worker WORKER = new ImmediateThinWorker();

    /* renamed from: io.reactivex.internal.schedulers.ImmediateThinScheduler$ImmediateThinWorker */
    static final class ImmediateThinWorker extends Worker {
        public void dispose() {
        }

        public boolean isDisposed() {
            return false;
        }

        ImmediateThinWorker() {
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable runnable) {
            runnable.run();
            return ImmediateThinScheduler.DISPOSED;
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable runnable, long j, @NonNull TimeUnit timeUnit) {
            throw new UnsupportedOperationException("This scheduler doesn't support delayed execution");
        }

        @NonNull
        public Disposable schedulePeriodically(@NonNull Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            throw new UnsupportedOperationException("This scheduler doesn't support periodic execution");
        }
    }

    static {
        DISPOSED.dispose();
    }

    private ImmediateThinScheduler() {
    }

    @NonNull
    public Disposable scheduleDirect(@NonNull Runnable runnable) {
        runnable.run();
        return DISPOSED;
    }

    @NonNull
    public Disposable scheduleDirect(@NonNull Runnable runnable, long j, TimeUnit timeUnit) {
        throw new UnsupportedOperationException("This scheduler doesn't support delayed execution");
    }

    @NonNull
    public Disposable schedulePeriodicallyDirect(@NonNull Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        throw new UnsupportedOperationException("This scheduler doesn't support periodic execution");
    }

    @NonNull
    public Worker createWorker() {
        return WORKER;
    }
}
