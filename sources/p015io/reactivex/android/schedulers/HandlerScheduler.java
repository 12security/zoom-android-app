package p015io.reactivex.android.schedulers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import java.util.concurrent.TimeUnit;
import p015io.reactivex.Scheduler;
import p015io.reactivex.Scheduler.Worker;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.disposables.Disposables;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.android.schedulers.HandlerScheduler */
final class HandlerScheduler extends Scheduler {
    private final boolean async;
    private final Handler handler;

    /* renamed from: io.reactivex.android.schedulers.HandlerScheduler$HandlerWorker */
    private static final class HandlerWorker extends Worker {
        private final boolean async;
        private volatile boolean disposed;
        private final Handler handler;

        HandlerWorker(Handler handler2, boolean z) {
            this.handler = handler2;
            this.async = z;
        }

        @SuppressLint({"NewApi"})
        public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            if (runnable == null) {
                throw new NullPointerException("run == null");
            } else if (timeUnit == null) {
                throw new NullPointerException("unit == null");
            } else if (this.disposed) {
                return Disposables.disposed();
            } else {
                ScheduledRunnable scheduledRunnable = new ScheduledRunnable(this.handler, RxJavaPlugins.onSchedule(runnable));
                Message obtain = Message.obtain(this.handler, scheduledRunnable);
                obtain.obj = this;
                if (this.async) {
                    obtain.setAsynchronous(true);
                }
                this.handler.sendMessageDelayed(obtain, timeUnit.toMillis(j));
                if (!this.disposed) {
                    return scheduledRunnable;
                }
                this.handler.removeCallbacks(scheduledRunnable);
                return Disposables.disposed();
            }
        }

        public void dispose() {
            this.disposed = true;
            this.handler.removeCallbacksAndMessages(this);
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    /* renamed from: io.reactivex.android.schedulers.HandlerScheduler$ScheduledRunnable */
    private static final class ScheduledRunnable implements Runnable, Disposable {
        private final Runnable delegate;
        private volatile boolean disposed;
        private final Handler handler;

        ScheduledRunnable(Handler handler2, Runnable runnable) {
            this.handler = handler2;
            this.delegate = runnable;
        }

        public void run() {
            try {
                this.delegate.run();
            } catch (Throwable th) {
                RxJavaPlugins.onError(th);
            }
        }

        public void dispose() {
            this.handler.removeCallbacks(this);
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    HandlerScheduler(Handler handler2, boolean z) {
        this.handler = handler2;
        this.async = z;
    }

    @SuppressLint({"NewApi"})
    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        if (runnable == null) {
            throw new NullPointerException("run == null");
        } else if (timeUnit != null) {
            ScheduledRunnable scheduledRunnable = new ScheduledRunnable(this.handler, RxJavaPlugins.onSchedule(runnable));
            Message obtain = Message.obtain(this.handler, scheduledRunnable);
            if (this.async) {
                obtain.setAsynchronous(true);
            }
            this.handler.sendMessageDelayed(obtain, timeUnit.toMillis(j));
            return scheduledRunnable;
        } else {
            throw new NullPointerException("unit == null");
        }
    }

    public Worker createWorker() {
        return new HandlerWorker(this.handler, this.async);
    }
}
