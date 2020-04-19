package p015io.reactivex.internal.schedulers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p015io.reactivex.Completable;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.Flowable;
import p015io.reactivex.Scheduler;
import p015io.reactivex.Scheduler.Worker;
import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.disposables.Disposables;
import p015io.reactivex.functions.Function;
import p015io.reactivex.internal.util.ExceptionHelper;
import p015io.reactivex.processors.FlowableProcessor;
import p015io.reactivex.processors.UnicastProcessor;

/* renamed from: io.reactivex.internal.schedulers.SchedulerWhen */
public class SchedulerWhen extends Scheduler implements Disposable {
    static final Disposable DISPOSED = Disposables.disposed();
    static final Disposable SUBSCRIBED = new SubscribedDisposable();
    private final Scheduler actualScheduler;
    private Disposable disposable;
    private final FlowableProcessor<Flowable<Completable>> workerProcessor = UnicastProcessor.create().toSerialized();

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$CreateWorkerFunction */
    static final class CreateWorkerFunction implements Function<ScheduledAction, Completable> {
        final Worker actualWorker;

        /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$CreateWorkerFunction$WorkerCompletable */
        final class WorkerCompletable extends Completable {
            final ScheduledAction action;

            WorkerCompletable(ScheduledAction scheduledAction) {
                this.action = scheduledAction;
            }

            /* access modifiers changed from: protected */
            public void subscribeActual(CompletableObserver completableObserver) {
                completableObserver.onSubscribe(this.action);
                this.action.call(CreateWorkerFunction.this.actualWorker, completableObserver);
            }
        }

        CreateWorkerFunction(Worker worker) {
            this.actualWorker = worker;
        }

        public Completable apply(ScheduledAction scheduledAction) {
            return new WorkerCompletable(scheduledAction);
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$DelayedAction */
    static class DelayedAction extends ScheduledAction {
        private final Runnable action;
        private final long delayTime;
        private final TimeUnit unit;

        DelayedAction(Runnable runnable, long j, TimeUnit timeUnit) {
            this.action = runnable;
            this.delayTime = j;
            this.unit = timeUnit;
        }

        /* access modifiers changed from: protected */
        public Disposable callActual(Worker worker, CompletableObserver completableObserver) {
            return worker.schedule(new OnCompletedAction(this.action, completableObserver), this.delayTime, this.unit);
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$ImmediateAction */
    static class ImmediateAction extends ScheduledAction {
        private final Runnable action;

        ImmediateAction(Runnable runnable) {
            this.action = runnable;
        }

        /* access modifiers changed from: protected */
        public Disposable callActual(Worker worker, CompletableObserver completableObserver) {
            return worker.schedule(new OnCompletedAction(this.action, completableObserver));
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$OnCompletedAction */
    static class OnCompletedAction implements Runnable {
        final Runnable action;
        final CompletableObserver actionCompletable;

        OnCompletedAction(Runnable runnable, CompletableObserver completableObserver) {
            this.action = runnable;
            this.actionCompletable = completableObserver;
        }

        public void run() {
            try {
                this.action.run();
            } finally {
                this.actionCompletable.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$QueueWorker */
    static final class QueueWorker extends Worker {
        private final FlowableProcessor<ScheduledAction> actionProcessor;
        private final Worker actualWorker;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        QueueWorker(FlowableProcessor<ScheduledAction> flowableProcessor, Worker worker) {
            this.actionProcessor = flowableProcessor;
            this.actualWorker = worker;
        }

        public void dispose() {
            if (this.unsubscribed.compareAndSet(false, true)) {
                this.actionProcessor.onComplete();
                this.actualWorker.dispose();
            }
        }

        public boolean isDisposed() {
            return this.unsubscribed.get();
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable runnable, long j, @NonNull TimeUnit timeUnit) {
            DelayedAction delayedAction = new DelayedAction(runnable, j, timeUnit);
            this.actionProcessor.onNext(delayedAction);
            return delayedAction;
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable runnable) {
            ImmediateAction immediateAction = new ImmediateAction(runnable);
            this.actionProcessor.onNext(immediateAction);
            return immediateAction;
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$ScheduledAction */
    static abstract class ScheduledAction extends AtomicReference<Disposable> implements Disposable {
        /* access modifiers changed from: protected */
        public abstract Disposable callActual(Worker worker, CompletableObserver completableObserver);

        ScheduledAction() {
            super(SchedulerWhen.SUBSCRIBED);
        }

        /* access modifiers changed from: 0000 */
        public void call(Worker worker, CompletableObserver completableObserver) {
            Disposable disposable = (Disposable) get();
            if (disposable != SchedulerWhen.DISPOSED && disposable == SchedulerWhen.SUBSCRIBED) {
                Disposable callActual = callActual(worker, completableObserver);
                if (!compareAndSet(SchedulerWhen.SUBSCRIBED, callActual)) {
                    callActual.dispose();
                }
            }
        }

        public boolean isDisposed() {
            return ((Disposable) get()).isDisposed();
        }

        public void dispose() {
            Disposable disposable;
            Disposable disposable2 = SchedulerWhen.DISPOSED;
            do {
                disposable = (Disposable) get();
                if (disposable == SchedulerWhen.DISPOSED) {
                    return;
                }
            } while (!compareAndSet(disposable, disposable2));
            if (disposable != SchedulerWhen.SUBSCRIBED) {
                disposable.dispose();
            }
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$SubscribedDisposable */
    static final class SubscribedDisposable implements Disposable {
        public void dispose() {
        }

        public boolean isDisposed() {
            return false;
        }

        SubscribedDisposable() {
        }
    }

    public SchedulerWhen(Function<Flowable<Flowable<Completable>>, Completable> function, Scheduler scheduler) {
        this.actualScheduler = scheduler;
        try {
            this.disposable = ((Completable) function.apply(this.workerProcessor)).subscribe();
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public void dispose() {
        this.disposable.dispose();
    }

    public boolean isDisposed() {
        return this.disposable.isDisposed();
    }

    @NonNull
    public Worker createWorker() {
        Worker createWorker = this.actualScheduler.createWorker();
        FlowableProcessor serialized = UnicastProcessor.create().toSerialized();
        Flowable map = serialized.map(new CreateWorkerFunction(createWorker));
        QueueWorker queueWorker = new QueueWorker(serialized, createWorker);
        this.workerProcessor.onNext(map);
        return queueWorker;
    }
}
