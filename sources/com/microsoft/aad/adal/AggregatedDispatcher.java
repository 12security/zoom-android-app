package com.microsoft.aad.adal;

import java.util.ArrayList;
import java.util.List;

final class AggregatedDispatcher extends DefaultDispatcher {
    AggregatedDispatcher(IDispatcher iDispatcher) {
        super(iDispatcher);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void flush(java.lang.String r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x003f }
            r0.<init>()     // Catch:{ all -> 0x003f }
            com.microsoft.aad.adal.IDispatcher r1 = r3.getDispatcher()     // Catch:{ all -> 0x003f }
            if (r1 != 0) goto L_0x000e
            monitor-exit(r3)
            return
        L_0x000e:
            java.util.Map r1 = r3.getObjectsToBeDispatched()     // Catch:{ all -> 0x003f }
            java.lang.Object r4 = r1.remove(r4)     // Catch:{ all -> 0x003f }
            java.util.List r4 = (java.util.List) r4     // Catch:{ all -> 0x003f }
            if (r4 == 0) goto L_0x003d
            boolean r1 = r4.isEmpty()     // Catch:{ all -> 0x003f }
            if (r1 == 0) goto L_0x0021
            goto L_0x003d
        L_0x0021:
            r1 = 0
        L_0x0022:
            int r2 = r4.size()     // Catch:{ all -> 0x003f }
            if (r1 >= r2) goto L_0x0034
            java.lang.Object r2 = r4.get(r1)     // Catch:{ all -> 0x003f }
            com.microsoft.aad.adal.IEvents r2 = (com.microsoft.aad.adal.IEvents) r2     // Catch:{ all -> 0x003f }
            r2.processEvent(r0)     // Catch:{ all -> 0x003f }
            int r1 = r1 + 1
            goto L_0x0022
        L_0x0034:
            com.microsoft.aad.adal.IDispatcher r4 = r3.getDispatcher()     // Catch:{ all -> 0x003f }
            r4.dispatchEvent(r0)     // Catch:{ all -> 0x003f }
            monitor-exit(r3)
            return
        L_0x003d:
            monitor-exit(r3)
            return
        L_0x003f:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.aad.adal.AggregatedDispatcher.flush(java.lang.String):void");
    }

    /* access modifiers changed from: 0000 */
    public void receive(String str, IEvents iEvents) {
        List list = (List) getObjectsToBeDispatched().get(str);
        if (list == null) {
            list = new ArrayList();
        }
        list.add(iEvents);
        getObjectsToBeDispatched().put(str, list);
    }
}
