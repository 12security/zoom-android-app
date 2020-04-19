package p021us.zoom.androidlib.util;

import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import java.util.Iterator;
import java.util.Vector;

/* renamed from: us.zoom.androidlib.util.EventTaskManager */
public final class EventTaskManager {
    private static final String TAG = "EventTaskManager";
    private Vector<EventTask> mCachedTasks = new Vector<>();
    private boolean mDestroyed = false;
    private Handler mHandler = new Handler();
    private IUIElement mUI;

    /* renamed from: us.zoom.androidlib.util.EventTaskManager$EventTask */
    class EventTask {
        EventAction action;

        /* renamed from: id */
        String f518id;

        EventTask(String str, EventAction eventAction) {
            this.f518id = str;
            this.action = eventAction;
        }
    }

    public void onStart(IUIElement iUIElement) {
    }

    public void destroy() {
        this.mCachedTasks.clear();
        this.mDestroyed = true;
        this.mUI = null;
    }

    public void onPause(IUIElement iUIElement) {
        this.mUI = null;
    }

    public void onResume(IUIElement iUIElement) {
        this.mDestroyed = false;
        this.mUI = iUIElement;
        executeCachedTasks();
    }

    public void onStop(IUIElement iUIElement) {
        this.mUI = null;
    }

    public void onUIDestroy(IUIElement iUIElement) {
        this.mUI = null;
    }

    public boolean isUIActive() {
        return this.mUI != null;
    }

    public boolean hasPendingTask() {
        return this.mCachedTasks.size() > 0;
    }

    public void push(EventAction eventAction) {
        push(null, eventAction, false);
    }

    public void push(String str, EventAction eventAction) {
        push(str, eventAction, false);
    }

    public void pushLater(EventAction eventAction) {
        push(null, eventAction, true);
    }

    public void pushLater(String str, EventAction eventAction) {
        push(str, eventAction, true);
    }

    private void push(final String str, final EventAction eventAction, boolean z) {
        if (eventAction != null && !this.mDestroyed) {
            if (z || Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        EventTaskManager.this.pushOnUiThread(str, eventAction);
                    }
                });
            } else {
                pushOnUiThread(str, eventAction);
            }
        }
    }

    /* access modifiers changed from: private */
    public void pushOnUiThread(String str, EventAction eventAction) {
        if (!this.mDestroyed) {
            if (isUIActive()) {
                executeTask(eventAction);
            } else {
                cacheTask(str, eventAction);
            }
        }
    }

    private void executeTask(EventAction eventAction) {
        IUIElement iUIElement = this.mUI;
        if (!(iUIElement instanceof Fragment) || ((Fragment) iUIElement).isAdded()) {
            eventAction.run(this.mUI);
        }
    }

    private void cacheTask(String str, EventAction eventAction) {
        EventTask eventTask = new EventTask(str, eventAction);
        if (str == null || str.length() == 0) {
            this.mCachedTasks.add(eventTask);
            return;
        }
        removePrevUniqueTask(str);
        this.mCachedTasks.add(eventTask);
    }

    private void removePrevUniqueTask(String str) {
        for (int i = 0; i < this.mCachedTasks.size(); i++) {
            if (str.equals(((EventTask) this.mCachedTasks.get(i)).f518id)) {
                this.mCachedTasks.remove(i);
                return;
            }
        }
    }

    private void executeCachedTasks() {
        Iterator it = this.mCachedTasks.iterator();
        while (it.hasNext()) {
            executeTask(((EventTask) it.next()).action);
        }
        this.mCachedTasks.clear();
    }
}
