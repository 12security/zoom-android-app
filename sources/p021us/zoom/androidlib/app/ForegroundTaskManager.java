package p021us.zoom.androidlib.app;

import android.os.Handler;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: us.zoom.androidlib.app.ForegroundTaskManager */
public class ForegroundTaskManager {
    private static final String TAG = "ForegroundTaskManager";
    private static ForegroundTaskManager instance;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public ArrayList<ForegroundTask> mTaskQueue = new ArrayList<>();

    public static synchronized ForegroundTaskManager getInstance() {
        ForegroundTaskManager foregroundTaskManager;
        synchronized (ForegroundTaskManager.class) {
            if (instance == null) {
                instance = new ForegroundTaskManager();
            }
            foregroundTaskManager = instance;
        }
        return foregroundTaskManager;
    }

    private ForegroundTaskManager() {
    }

    public void runInForeground(final ForegroundTask foregroundTask) {
        if (foregroundTask != null) {
            final ZMActivity frontActivity = ZMActivity.getFrontActivity();
            if ((frontActivity == null || !frontActivity.isActive() || frontActivity.isFinishing() || !foregroundTask.isValidActivity(frontActivity.getClass().getName())) && (!foregroundTask.isOtherProcessSupported() || !foregroundTask.hasAnotherProcessAtFront())) {
                if (!foregroundTask.isMultipleInstancesAllowed()) {
                    updateTask(foregroundTask);
                } else {
                    this.mTaskQueue.add(foregroundTask);
                }
            } else if (!foregroundTask.isOtherProcessSupported() || !foregroundTask.hasAnotherProcessAtFront()) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        ZMActivity frontActivity = ZMActivity.getFrontActivity();
                        ZMActivity zMActivity = frontActivity;
                        if (zMActivity != frontActivity || zMActivity == null || !zMActivity.isActive() || frontActivity.isFinishing()) {
                            ForegroundTaskManager.this.mTaskQueue.add(foregroundTask);
                        } else if (!foregroundTask.isExpired()) {
                            ForegroundTaskManager.this.executeTask(foregroundTask, frontActivity);
                        }
                    }
                });
            } else {
                executeTask(foregroundTask, null);
            }
        }
    }

    public boolean containsTask(@NonNull String str) {
        Iterator it = this.mTaskQueue.iterator();
        while (it.hasNext()) {
            ForegroundTask foregroundTask = (ForegroundTask) it.next();
            if (foregroundTask != null && str.equals(foregroundTask.getName())) {
                return true;
            }
        }
        return false;
    }

    public void removeTask(ForegroundTask foregroundTask) {
        this.mTaskQueue.remove(foregroundTask);
    }

    /* access modifiers changed from: protected */
    public void onActivityMoveToFront(ZMActivity zMActivity) {
        if (!this.mTaskQueue.isEmpty()) {
            runQueuedTasks(zMActivity);
        }
    }

    public void onAnotherProcessMoveToFront(String str) {
        if (!this.mTaskQueue.isEmpty() && str != null) {
            runQueuedTasksOnAnotherProcess(str);
        }
    }

    private void runQueuedTasksOnAnotherProcess(String str) {
        if (str != null) {
            ArrayList arrayList = new ArrayList();
            while (!this.mTaskQueue.isEmpty()) {
                ForegroundTask foregroundTask = (ForegroundTask) this.mTaskQueue.remove(0);
                if (!foregroundTask.isExpired()) {
                    if (!foregroundTask.isOtherProcessSupported() || !foregroundTask.isValidActivity(str)) {
                        arrayList.add(foregroundTask);
                    } else {
                        executeTask(foregroundTask, null);
                    }
                }
            }
            this.mTaskQueue.addAll(arrayList);
        }
    }

    private void runQueuedTasks(ZMActivity zMActivity) {
        if (zMActivity != null) {
            ArrayList arrayList = new ArrayList();
            while (!this.mTaskQueue.isEmpty()) {
                ForegroundTask foregroundTask = (ForegroundTask) this.mTaskQueue.remove(0);
                if (!foregroundTask.isExpired()) {
                    if (!foregroundTask.isValidActivity(zMActivity.getClass().getName())) {
                        arrayList.add(foregroundTask);
                    } else {
                        executeTask(foregroundTask, zMActivity);
                    }
                }
            }
            this.mTaskQueue.addAll(arrayList);
        }
    }

    /* access modifiers changed from: private */
    public void executeTask(ForegroundTask foregroundTask, ZMActivity zMActivity) {
        foregroundTask.run(zMActivity);
    }

    private void updateTask(ForegroundTask foregroundTask) {
        if (foregroundTask != null) {
            Iterator it = this.mTaskQueue.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ForegroundTask foregroundTask2 = (ForegroundTask) it.next();
                if (foregroundTask2 != null && foregroundTask2.getName() != null && foregroundTask2.getName().equals(foregroundTask.getName())) {
                    this.mTaskQueue.remove(foregroundTask2);
                    break;
                }
            }
            this.mTaskQueue.add(foregroundTask);
        }
    }
}
