package com.zipow.videobox.sdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;

public class PromptProxyServerTaskManager {
    @Nullable
    private static PromptProxyServerTaskManager instance;
    private boolean mRunImmediately = true;
    @NonNull
    private ArrayList<PromptProxyServerTask> mTaskQueue = new ArrayList<>();

    @NonNull
    public static synchronized PromptProxyServerTaskManager getInstance() {
        PromptProxyServerTaskManager promptProxyServerTaskManager;
        synchronized (PromptProxyServerTaskManager.class) {
            if (instance == null) {
                instance = new PromptProxyServerTaskManager();
            }
            promptProxyServerTaskManager = instance;
        }
        return promptProxyServerTaskManager;
    }

    private PromptProxyServerTaskManager() {
    }

    public void run(@NonNull PromptProxyServerTask promptProxyServerTask) {
        if (!this.mRunImmediately) {
            updateTask(promptProxyServerTask);
        } else {
            promptProxyServerTask.run();
        }
    }

    private void runQueuedTasks() {
        while (!this.mTaskQueue.isEmpty()) {
            ((PromptProxyServerTask) this.mTaskQueue.remove(0)).run();
        }
    }

    public void setRunImmediatelyEnabled(boolean z) {
        this.mRunImmediately = z;
        if (this.mRunImmediately) {
            runQueuedTasks();
        }
    }

    private void updateTask(@Nullable PromptProxyServerTask promptProxyServerTask) {
        if (promptProxyServerTask != null) {
            Iterator it = this.mTaskQueue.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                PromptProxyServerTask promptProxyServerTask2 = (PromptProxyServerTask) it.next();
                if (promptProxyServerTask2 != null && promptProxyServerTask2.getName() != null && promptProxyServerTask2.getName().equals(promptProxyServerTask.getName())) {
                    this.mTaskQueue.remove(promptProxyServerTask2);
                    break;
                }
            }
            this.mTaskQueue.add(promptProxyServerTask);
        }
    }
}
