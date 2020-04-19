package p015io.reactivex.internal.schedulers;

import p015io.reactivex.Scheduler.Worker;
import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport */
public interface SchedulerMultiWorkerSupport {

    /* renamed from: io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport$WorkerCallback */
    public interface WorkerCallback {
        void onWorker(int i, @NonNull Worker worker);
    }

    void createWorkers(int i, @NonNull WorkerCallback workerCallback);
}
