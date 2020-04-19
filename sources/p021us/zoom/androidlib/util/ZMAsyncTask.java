package p021us.zoom.androidlib.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* renamed from: us.zoom.androidlib.util.ZMAsyncTask */
public abstract class ZMAsyncTask<Params, Progress, Result> {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor = THREAD_POOL_EXECUTOR;
    private static final InternalHandler sHandler = new InternalHandler();
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue(10);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(@NonNull Runnable runnable) {
            StringBuilder sb = new StringBuilder();
            sb.append("ZMAsyncTask #");
            sb.append(this.mCount.getAndIncrement());
            return new Thread(runnable, sb.toString());
        }
    };
    private final FutureTask<Result> mFuture = new FutureTask<Result>(this.mWorker) {
        /* access modifiers changed from: protected */
        public void done() {
            try {
                ZMAsyncTask.this.postResultIfNotInvoked(get());
            } catch (InterruptedException e) {
                Log.w(ZMAsyncTask.LOG_TAG, e);
            } catch (ExecutionException e2) {
                throw new RuntimeException("An error occured while executing doInBackground()", e2.getCause());
            } catch (CancellationException unused) {
                ZMAsyncTask.this.postResultIfNotInvoked(null);
            } catch (Throwable th) {
                throw new RuntimeException("An error occured while executing doInBackground()", th);
            }
        }
    };
    private volatile Status mStatus = Status.PENDING;
    /* access modifiers changed from: private */
    public final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable<Params, Result>() {
        public Result call() throws Exception {
            ZMAsyncTask.this.mTaskInvoked.set(true);
            ZMAsyncTask zMAsyncTask = ZMAsyncTask.this;
            return zMAsyncTask.postResult(zMAsyncTask.doInBackground(this.mParams));
        }
    };

    /* renamed from: us.zoom.androidlib.util.ZMAsyncTask$AsyncTaskResult */
    private static class AsyncTaskResult<Data> {
        final Data[] mData;
        final ZMAsyncTask mTask;

        AsyncTaskResult(ZMAsyncTask zMAsyncTask, Data... dataArr) {
            this.mTask = zMAsyncTask;
            this.mData = dataArr;
        }
    }

    /* renamed from: us.zoom.androidlib.util.ZMAsyncTask$InternalHandler */
    private static class InternalHandler extends Handler {
        private InternalHandler() {
        }

        public void handleMessage(Message message) {
            AsyncTaskResult asyncTaskResult = (AsyncTaskResult) message.obj;
            switch (message.what) {
                case 1:
                    asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
                    return;
                case 2:
                    asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: us.zoom.androidlib.util.ZMAsyncTask$Status */
    public enum Status {
        PENDING,
        RUNNING,
        FINISHED
    }

    /* renamed from: us.zoom.androidlib.util.ZMAsyncTask$WorkerRunnable */
    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;

        private WorkerRunnable() {
        }
    }

    /* access modifiers changed from: protected */
    public abstract Result doInBackground(Params... paramsArr);

    /* access modifiers changed from: protected */
    public void onCancelled() {
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Result result) {
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Progress... progressArr) {
    }

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 128, 1, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }

    public static void init() {
        sHandler.getLooper();
    }

    public static void setDefaultExecutor(Executor executor) {
        sDefaultExecutor = executor;
    }

    /* access modifiers changed from: private */
    public void postResultIfNotInvoked(Result result) {
        if (!this.mTaskInvoked.get()) {
            postResult(result);
        }
    }

    /* access modifiers changed from: private */
    public Result postResult(Result result) {
        sHandler.obtainMessage(1, new AsyncTaskResult(this, result)).sendToTarget();
        return result;
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Result result) {
        onCancelled();
    }

    public final boolean isCancelled() {
        return this.mFuture.isCancelled();
    }

    public final boolean cancel(boolean z) {
        return this.mFuture.cancel(z);
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(j, timeUnit);
    }

    public final ZMAsyncTask<Params, Progress, Result> execute(Params... paramsArr) {
        return executeOnExecutor(sDefaultExecutor, paramsArr);
    }

    public final ZMAsyncTask<Params, Progress, Result> executeOnExecutor(Executor executor, Params... paramsArr) {
        if (this.mStatus != Status.PENDING) {
            switch (this.mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }
        this.mStatus = Status.RUNNING;
        onPreExecute();
        this.mWorker.mParams = paramsArr;
        executor.execute(this.mFuture);
        return this;
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    /* access modifiers changed from: protected */
    public final void publishProgress(Progress... progressArr) {
        if (!isCancelled()) {
            sHandler.obtainMessage(2, new AsyncTaskResult(this, progressArr)).sendToTarget();
        }
    }

    /* access modifiers changed from: private */
    public void finish(Result result) {
        if (isCancelled()) {
            onCancelled(result);
        } else {
            onPostExecute(result);
        }
        this.mStatus = Status.FINISHED;
    }
}
