package p021us.zipow.mdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ZMBundleTypeAdapterFactory;

/* renamed from: us.zipow.mdm.ZMMdmManager */
public class ZMMdmManager {
    private static final String CHOICE_SUFFIX = "choice:";
    private static final String TAG = "us.zipow.mdm.ZMMdmManager";
    @NonNull
    private static SparseArray<String> mSuffixToIndexes = new SparseArray<>();
    private static final ZMMdmManager ourInstance = new ZMMdmManager();
    private final Gson mGson = new GsonBuilder().registerTypeAdapterFactory(new ZMBundleTypeAdapterFactory()).create();
    @NonNull
    private BroadcastReceiver mRestrictionChangesReceiver = new BroadcastReceiver() {
        @RequiresApi(api = 21)
        public void onReceive(Context context, Intent intent) {
            ZMMdmManager.this.onPolicyUpdated();
        }
    };
    @NonNull
    private SparseArray<JsonObject> mSourcePolicySArray = new SparseArray<>();

    static {
        mSuffixToIndexes.put(4, "recommend:");
        mSuffixToIndexes.put(64, "mandatory:");
    }

    private ZMMdmManager() {
    }

    @NonNull
    public static ZMMdmManager getInstance() {
        return ourInstance;
    }

    @RequiresApi(api = 21)
    public void registerRestrictionChangesReceiver(@NonNull Context context) {
        context.registerReceiver(this.mRestrictionChangesReceiver, new IntentFilter("android.intent.action.APPLICATION_RESTRICTIONS_CHANGED"));
    }

    /* access modifiers changed from: private */
    @RequiresApi(api = 21)
    public void onPolicyUpdated() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider != null) {
            zoomMdmPolicyProvider.onPolicyUpdated();
        }
    }

    @RequiresApi(api = 21)
    public synchronized void initPolicyComplete() {
    }

    @Nullable
    public ZoomMdmPolicyProvider getZoomMdmPolicyProvider() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider;
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance == null) {
            return null;
        }
        if (instance.isConfApp()) {
            zoomMdmPolicyProvider = ConfMgr.getInstance().getZoomMdmPolicyProvider();
        } else {
            zoomMdmPolicyProvider = PTApp.getInstance().getZoomMdmPolicyProvider();
        }
        return zoomMdmPolicyProvider;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00f1, code lost:
        return false;
     */
    @androidx.annotation.RequiresApi(api = 21)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean refreshPolicy() {
        /*
            r11 = this;
            monitor-enter(r11)
            android.content.Context r0 = com.zipow.videobox.VideoBoxApplication.getGlobalContext()     // Catch:{ all -> 0x00f2 }
            r1 = 0
            if (r0 != 0) goto L_0x000a
            monitor-exit(r11)
            return r1
        L_0x000a:
            java.lang.String r2 = "restrictions"
            java.lang.Object r2 = r0.getSystemService(r2)     // Catch:{ all -> 0x00f2 }
            android.content.RestrictionsManager r2 = (android.content.RestrictionsManager) r2     // Catch:{ all -> 0x00f2 }
            if (r2 == 0) goto L_0x00f0
            android.os.Bundle r2 = r2.getApplicationRestrictions()     // Catch:{ all -> 0x00f2 }
            if (r2 == 0) goto L_0x00f0
            android.content.res.Resources r0 = r0.getResources()     // Catch:{ all -> 0x00f2 }
            int r3 = p021us.zoom.videomeetings.C4558R.array.zm_mdm_three_choice_entry_values_51221     // Catch:{ all -> 0x00f2 }
            java.lang.String[] r0 = r0.getStringArray(r3)     // Catch:{ all -> 0x00f2 }
            com.google.gson.Gson r3 = r11.mGson     // Catch:{ all -> 0x00f2 }
            com.google.gson.JsonElement r2 = r3.toJsonTree(r2)     // Catch:{ all -> 0x00f2 }
            com.google.gson.JsonObject r2 = r2.getAsJsonObject()     // Catch:{ all -> 0x00f2 }
            if (r2 != 0) goto L_0x0032
            monitor-exit(r11)
            return r1
        L_0x0032:
            java.util.Set r3 = r2.keySet()     // Catch:{ all -> 0x00f2 }
            boolean r4 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r3)     // Catch:{ all -> 0x00f2 }
            if (r4 == 0) goto L_0x003e
            monitor-exit(r11)
            return r1
        L_0x003e:
            android.util.SparseArray<com.google.gson.JsonObject> r4 = r11.mSourcePolicySArray     // Catch:{ all -> 0x00f2 }
            r4.clear()     // Catch:{ all -> 0x00f2 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x00f2 }
        L_0x0047:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x00f2 }
            if (r4 == 0) goto L_0x00ed
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x00f2 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x00f2 }
            boolean r5 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r4)     // Catch:{ all -> 0x00f2 }
            if (r5 == 0) goto L_0x005a
            goto L_0x0047
        L_0x005a:
            r5 = 0
        L_0x005b:
            android.util.SparseArray<java.lang.String> r6 = mSuffixToIndexes     // Catch:{ all -> 0x00f2 }
            int r6 = r6.size()     // Catch:{ all -> 0x00f2 }
            if (r5 >= r6) goto L_0x0047
            android.util.SparseArray<java.lang.String> r6 = mSuffixToIndexes     // Catch:{ all -> 0x00f2 }
            java.lang.Object r6 = r6.valueAt(r5)     // Catch:{ all -> 0x00f2 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x00f2 }
            boolean r6 = r4.startsWith(r6)     // Catch:{ all -> 0x00f2 }
            if (r6 == 0) goto L_0x00e9
            android.util.SparseArray<java.lang.String> r6 = mSuffixToIndexes     // Catch:{ all -> 0x00f2 }
            int r6 = r6.keyAt(r5)     // Catch:{ all -> 0x00f2 }
            android.util.SparseArray<com.google.gson.JsonObject> r7 = r11.mSourcePolicySArray     // Catch:{ all -> 0x00f2 }
            java.lang.Object r7 = r7.get(r6)     // Catch:{ all -> 0x00f2 }
            com.google.gson.JsonObject r7 = (com.google.gson.JsonObject) r7     // Catch:{ all -> 0x00f2 }
            if (r7 != 0) goto L_0x008b
            com.google.gson.JsonObject r7 = new com.google.gson.JsonObject     // Catch:{ all -> 0x00f2 }
            r7.<init>()     // Catch:{ all -> 0x00f2 }
            android.util.SparseArray<com.google.gson.JsonObject> r8 = r11.mSourcePolicySArray     // Catch:{ all -> 0x00f2 }
            r8.put(r6, r7)     // Catch:{ all -> 0x00f2 }
        L_0x008b:
            android.util.SparseArray<java.lang.String> r6 = mSuffixToIndexes     // Catch:{ all -> 0x00f2 }
            java.lang.Object r6 = r6.valueAt(r5)     // Catch:{ all -> 0x00f2 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x00f2 }
            int r6 = r6.length()     // Catch:{ all -> 0x00f2 }
            java.lang.String r6 = r4.substring(r6)     // Catch:{ all -> 0x00f2 }
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r6)     // Catch:{ all -> 0x00f2 }
            if (r8 != 0) goto L_0x00e9
            java.lang.String r8 = "choice:"
            boolean r8 = r6.startsWith(r8)     // Catch:{ all -> 0x00f2 }
            if (r8 == 0) goto L_0x00e0
            r8 = 7
            java.lang.String r6 = r6.substring(r8)     // Catch:{ all -> 0x00f2 }
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r6)     // Catch:{ all -> 0x00f2 }
            if (r8 == 0) goto L_0x00b5
            goto L_0x00e9
        L_0x00b5:
            com.google.gson.JsonElement r8 = r2.get(r4)     // Catch:{ all -> 0x00f2 }
            if (r8 != 0) goto L_0x00bc
            goto L_0x00e9
        L_0x00bc:
            java.lang.String r8 = r8.getAsString()     // Catch:{ all -> 0x00f2 }
            r9 = r0[r1]     // Catch:{ all -> 0x00f2 }
            boolean r9 = p021us.zoom.androidlib.util.StringUtil.isSameString(r8, r9)     // Catch:{ all -> 0x00f2 }
            if (r9 == 0) goto L_0x00c9
            goto L_0x00e9
        L_0x00c9:
            com.google.gson.JsonPrimitive r9 = new com.google.gson.JsonPrimitive     // Catch:{ all -> 0x00f2 }
            r10 = 2
            r10 = r0[r10]     // Catch:{ all -> 0x00f2 }
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isSameString(r8, r10)     // Catch:{ all -> 0x00f2 }
            if (r8 == 0) goto L_0x00d7
            java.lang.Boolean r8 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x00f2 }
            goto L_0x00d9
        L_0x00d7:
            java.lang.Boolean r8 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x00f2 }
        L_0x00d9:
            r9.<init>(r8)     // Catch:{ all -> 0x00f2 }
            r7.add(r6, r9)     // Catch:{ all -> 0x00f2 }
            goto L_0x00e9
        L_0x00e0:
            com.google.gson.JsonElement r4 = r2.get(r4)     // Catch:{ all -> 0x00f2 }
            r7.add(r6, r4)     // Catch:{ all -> 0x00f2 }
            goto L_0x0047
        L_0x00e9:
            int r5 = r5 + 1
            goto L_0x005b
        L_0x00ed:
            monitor-exit(r11)
            r0 = 1
            return r0
        L_0x00f0:
            monitor-exit(r11)
            return r1
        L_0x00f2:
            r0 = move-exception
            monitor-exit(r11)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zipow.mdm.ZMMdmManager.refreshPolicy():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0043, code lost:
        return r1;
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getPolicy(int r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            android.util.SparseArray<com.google.gson.JsonObject> r0 = r5.mSourcePolicySArray     // Catch:{ all -> 0x0044 }
            int r0 = r0.size()     // Catch:{ all -> 0x0044 }
            r1 = 0
            if (r0 != 0) goto L_0x000c
            monitor-exit(r5)
            return r1
        L_0x000c:
            android.util.SparseArray<com.google.gson.JsonObject> r0 = r5.mSourcePolicySArray     // Catch:{ all -> 0x0044 }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ all -> 0x0044 }
            com.google.gson.JsonObject r0 = (com.google.gson.JsonObject) r0     // Catch:{ all -> 0x0044 }
            if (r0 == 0) goto L_0x003b
            java.lang.String r2 = TAG     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0044 }
            r3.<init>()     // Catch:{ all -> 0x0044 }
            java.lang.String r4 = "start getPolicy policy source is="
            r3.append(r4)     // Catch:{ all -> 0x0044 }
            r3.append(r6)     // Catch:{ all -> 0x0044 }
            java.lang.String r6 = "   policy="
            r3.append(r6)     // Catch:{ all -> 0x0044 }
            java.lang.String r6 = r0.toString()     // Catch:{ all -> 0x0044 }
            r3.append(r6)     // Catch:{ all -> 0x0044 }
            java.lang.String r6 = r3.toString()     // Catch:{ all -> 0x0044 }
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0044 }
            p021us.zoom.androidlib.util.ZMLog.m288w(r2, r6, r3)     // Catch:{ all -> 0x0044 }
        L_0x003b:
            if (r0 != 0) goto L_0x003e
            goto L_0x0042
        L_0x003e:
            java.lang.String r1 = r0.toString()     // Catch:{ all -> 0x0044 }
        L_0x0042:
            monitor-exit(r5)
            return r1
        L_0x0044:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zipow.mdm.ZMMdmManager.getPolicy(int):java.lang.String");
    }
}
