package com.microsoft.aad.adal;

import android.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultDispatcher {
    private final IDispatcher mDispatcher;
    private final Map<String, List<IEvents>> mObjectsToBeDispatched;

    private DefaultDispatcher() {
        this.mObjectsToBeDispatched = new HashMap();
        this.mDispatcher = null;
    }

    DefaultDispatcher(IDispatcher iDispatcher) {
        this.mObjectsToBeDispatched = new HashMap();
        this.mDispatcher = iDispatcher;
    }

    /* access modifiers changed from: 0000 */
    public synchronized void flush(String str) {
    }

    /* access modifiers changed from: 0000 */
    public void receive(String str, IEvents iEvents) {
        if (this.mDispatcher != null) {
            HashMap hashMap = new HashMap();
            for (Pair pair : iEvents.getEvents()) {
                hashMap.put(pair.first, pair.second);
            }
            this.mDispatcher.dispatchEvent(hashMap);
        }
    }

    /* access modifiers changed from: 0000 */
    public IDispatcher getDispatcher() {
        return this.mDispatcher;
    }

    /* access modifiers changed from: 0000 */
    public Map<String, List<IEvents>> getObjectsToBeDispatched() {
        return this.mObjectsToBeDispatched;
    }
}
