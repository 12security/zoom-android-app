package com.zipow.cmmlib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class AppContext {
    @NonNull
    public static final String APP_NAME_CHAT;
    @NonNull
    public static final String APP_NAME_VIDEO;
    public static final String PREFER_NAME_AVATAR_CACHE_INDEX = "SSBAvatarCacheIndex";
    public static final String PREFER_NAME_CHAT = "config";
    public static final String PREFER_NAME_UCINDEX = "SSBSUCIndex";
    public static final String PREFER_NAME_VIDEO = "confparams";
    private static final String TAG = AppContext.class.getSimpleName();
    /* access modifiers changed from: private */
    @Nullable
    public static Context s_context;
    @NonNull
    private static HashMap<String, AppContextImpl> s_contextImplMap = new HashMap<>();
    /* access modifiers changed from: private */
    public static Handler s_handler;
    private String mPreferenceName;

    static class AppContextImpl {
        @Nullable
        private Properties cachedProps = null;
        private boolean isChanged = false;
        @NonNull
        private ReentrantLock lock = new ReentrantLock();
        private String mPreferenceName;
        private long propsFileTimestamp = 0;
        @NonNull
        private Runnable taskDelayCommit = new Runnable() {
            public void run() {
                AppContextImpl.this.endTransaction();
            }
        };
        @Nullable
        private Properties transactionProps = null;

        public AppContextImpl(String str) {
            this.mPreferenceName = str;
        }

        private File getProperptyFile() {
            if (AppContext.s_context == null) {
                return null;
            }
            File filesDir = AppContext.s_context.getFilesDir();
            if (filesDir == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(filesDir);
            sb.append("/data");
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file.getAbsolutePath());
            sb2.append("/");
            sb2.append(this.mPreferenceName);
            sb2.append(".ini");
            return new File(sb2.toString());
        }

        public boolean beginTransaction() {
            if (!this.lock.isHeldByCurrentThread()) {
                this.lock.lock();
            }
            if (this.transactionProps != null) {
                return true;
            }
            this.transactionProps = loadProperties();
            return true;
        }

        /* JADX INFO: finally extract failed */
        public boolean endTransaction() {
            if (!this.lock.isHeldByCurrentThread()) {
                this.lock.lock();
            }
            Properties properties = this.transactionProps;
            if (properties == null) {
                this.lock.unlock();
                return false;
            }
            try {
                if (this.isChanged) {
                    if (properties.isEmpty()) {
                        File properptyFile = getProperptyFile();
                        if (properptyFile != null && properptyFile.exists()) {
                            properptyFile.delete();
                        }
                    } else {
                        saveProperties(this.transactionProps);
                    }
                }
                this.transactionProps = null;
                this.isChanged = false;
                this.lock.unlock();
                return true;
            } catch (Throwable th) {
                this.transactionProps = null;
                this.isChanged = false;
                this.lock.unlock();
                throw th;
            }
        }

        public String queryWithKey(String str, String str2) {
            if (AppContext.s_context == null) {
                return "";
            }
            Properties properties = null;
            this.lock.lock();
            Properties properties2 = this.transactionProps;
            if (properties2 != null) {
                properties = properties2;
            }
            if (properties == null) {
                properties = loadProperties();
            }
            this.lock.unlock();
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(".");
            sb.append(str);
            String property = properties.getProperty(sb.toString());
            if (property == null) {
                property = "";
            }
            return property;
        }

        /* JADX WARNING: Removed duplicated region for block: B:29:0x0044 A[SYNTHETIC, Splitter:B:29:0x0044] */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x0050 A[SYNTHETIC, Splitter:B:37:0x0050] */
        @androidx.annotation.NonNull
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.util.Properties loadProperties() {
            /*
                r7 = this;
                java.util.Properties r0 = new java.util.Properties
                r0.<init>()
                r1 = 0
                r7.isChanged = r1
                r1 = 0
                java.io.File r2 = r7.getProperptyFile()     // Catch:{ Exception -> 0x0041 }
                if (r2 == 0) goto L_0x003e
                boolean r3 = r2.exists()     // Catch:{ Exception -> 0x003c }
                if (r3 != 0) goto L_0x0016
                goto L_0x003e
            L_0x0016:
                long r3 = r7.getTimestamp()     // Catch:{ Exception -> 0x003c }
                java.util.Properties r5 = r7.cachedProps     // Catch:{ Exception -> 0x003c }
                if (r5 == 0) goto L_0x0027
                long r5 = r7.propsFileTimestamp     // Catch:{ Exception -> 0x003c }
                int r5 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
                if (r5 != 0) goto L_0x0027
                java.util.Properties r0 = r7.cachedProps     // Catch:{ Exception -> 0x003c }
                return r0
            L_0x0027:
                java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Exception -> 0x003c }
                r5.<init>(r2)     // Catch:{ Exception -> 0x003c }
                r0.load(r5)     // Catch:{ Exception -> 0x003a, all -> 0x0037 }
                r7.propsFileTimestamp = r3     // Catch:{ Exception -> 0x003a, all -> 0x0037 }
                r7.cachedProps = r0     // Catch:{ Exception -> 0x003a, all -> 0x0037 }
                r5.close()     // Catch:{ Exception -> 0x0053 }
                goto L_0x0053
            L_0x0037:
                r0 = move-exception
                r1 = r5
                goto L_0x0048
            L_0x003a:
                r1 = r5
                goto L_0x0042
            L_0x003c:
                goto L_0x0042
            L_0x003e:
                return r0
            L_0x003f:
                r0 = move-exception
                goto L_0x0048
            L_0x0041:
                r2 = r1
            L_0x0042:
                if (r2 == 0) goto L_0x004e
                r2.delete()     // Catch:{ all -> 0x003f }
                goto L_0x004e
            L_0x0048:
                if (r1 == 0) goto L_0x004d
                r1.close()     // Catch:{ Exception -> 0x004d }
            L_0x004d:
                throw r0
            L_0x004e:
                if (r1 == 0) goto L_0x0053
                r1.close()     // Catch:{ Exception -> 0x0053 }
            L_0x0053:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.cmmlib.AppContext.AppContextImpl.loadProperties():java.util.Properties");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0033, code lost:
            if (r3 != null) goto L_0x001e;
         */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x002b A[SYNTHETIC, Splitter:B:14:0x002b] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean saveProperties(@androidx.annotation.NonNull java.util.Properties r7) {
            /*
                r6 = this;
                r0 = 0
                r1 = 0
                java.io.File r2 = r6.getProperptyFile()     // Catch:{ Exception -> 0x0032, all -> 0x0027 }
                if (r2 != 0) goto L_0x0009
                return r1
            L_0x0009:
                java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0032, all -> 0x0027 }
                r3.<init>(r2)     // Catch:{ Exception -> 0x0032, all -> 0x0027 }
                r7.store(r3, r0)     // Catch:{ Exception -> 0x0033, all -> 0x0025 }
                r1 = 1
                long r4 = r6.getTimestamp()     // Catch:{ Exception -> 0x0033, all -> 0x0025 }
                long r4 = r6.newTimestamp(r4)     // Catch:{ Exception -> 0x0033, all -> 0x0025 }
                r6.propsFileTimestamp = r4     // Catch:{ Exception -> 0x0033, all -> 0x0025 }
                r6.cachedProps = r7     // Catch:{ Exception -> 0x0033, all -> 0x0025 }
            L_0x001e:
                r3.flush()     // Catch:{ Exception -> 0x0036 }
                r3.close()     // Catch:{ Exception -> 0x0036 }
                goto L_0x0036
            L_0x0025:
                r7 = move-exception
                goto L_0x0029
            L_0x0027:
                r7 = move-exception
                r3 = r0
            L_0x0029:
                if (r3 == 0) goto L_0x0031
                r3.flush()     // Catch:{ Exception -> 0x0031 }
                r3.close()     // Catch:{ Exception -> 0x0031 }
            L_0x0031:
                throw r7
            L_0x0032:
                r3 = r0
            L_0x0033:
                if (r3 == 0) goto L_0x0036
                goto L_0x001e
            L_0x0036:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.cmmlib.AppContext.AppContextImpl.saveProperties(java.util.Properties):boolean");
        }

        public boolean setKeyValue(String str, @Nullable String str2, String str3) {
            boolean z;
            if (AppContext.s_context == null) {
                return false;
            }
            Properties properties = null;
            this.lock.lock();
            Properties properties2 = this.transactionProps;
            if (properties2 != null) {
                properties = properties2;
                z = true;
            } else {
                z = false;
            }
            if (properties == null) {
                properties = loadProperties();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str3);
            sb.append(".");
            sb.append(str);
            String sb2 = sb.toString();
            if (str2 == null) {
                if (properties.containsKey(sb2)) {
                    properties.remove(sb2);
                    this.isChanged = true;
                }
            } else if (!StringUtil.isSameString(properties.getProperty(sb2), str2)) {
                properties.setProperty(sb2, str2);
                this.isChanged = true;
            }
            if (!z && this.isChanged) {
                saveProperties(properties);
                this.isChanged = false;
            }
            this.lock.unlock();
            return true;
        }

        public boolean setKeyValueDelayCommit(final String str, final String str2, final String str3, long j) {
            boolean z;
            int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
            if (i <= 0 || Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId()) {
                if (i > 0) {
                    beginTransaction();
                }
                z = setKeyValue(str, str2, str3);
            } else {
                AppContext.s_handler.post(new Runnable() {
                    public void run() {
                        AppContextImpl.this.beginTransaction();
                        AppContextImpl.this.setKeyValue(str, str2, str3);
                    }
                });
                z = true;
            }
            if (i > 0) {
                AppContext.s_handler.removeCallbacks(this.taskDelayCommit);
                AppContext.s_handler.postDelayed(this.taskDelayCommit, j);
            }
            return z;
        }

        public boolean eraseAll() {
            Properties properties;
            boolean z;
            if (AppContext.s_context == null) {
                return false;
            }
            this.lock.lock();
            Properties properties2 = this.transactionProps;
            if (properties2 != null) {
                properties = properties2;
                z = true;
            } else {
                properties = null;
                z = false;
            }
            if (!z) {
                File properptyFile = getProperptyFile();
                if (properptyFile != null && properptyFile.exists()) {
                    properptyFile.delete();
                }
                this.propsFileTimestamp = newTimestamp(getTimestamp());
                this.cachedProps = null;
                this.isChanged = false;
            } else if (!properties.isEmpty()) {
                properties.clear();
                this.isChanged = true;
            }
            this.lock.unlock();
            return true;
        }

        private long newTimestamp(long j) {
            ZoomAppPropData instance = ZoomAppPropData.getInstance();
            if (instance == null) {
                return 0;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis == j) {
                currentTimeMillis++;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("timestamp-");
            sb.append(this.mPreferenceName);
            instance.setInt64(sb.toString(), currentTimeMillis);
            return currentTimeMillis;
        }

        private long getTimestamp() {
            ZoomAppPropData instance = ZoomAppPropData.getInstance();
            if (instance == null) {
                return 0;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("timestamp-");
            sb.append(this.mPreferenceName);
            return instance.queryInt64(sb.toString(), 1);
        }
    }

    private static native boolean BAASecurityIsEnabledImpl();

    static {
        System.loadLibrary("zoom_stlport");
        System.loadLibrary("crypto_sb");
        System.loadLibrary("ssl_sb");
        System.loadLibrary("cmmlib");
        switch (ZMBuildConfig.BUILD_TARGET) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                APP_NAME_CHAT = "ZoomChat";
                APP_NAME_VIDEO = "ZoomVideo";
                break;
            default:
                APP_NAME_CHAT = "PT";
                APP_NAME_VIDEO = "Meet";
                break;
        }
    }

    public static void initialize(@NonNull Context context) {
        s_context = context;
        s_handler = new Handler();
    }

    public AppContext(String str) {
        this.mPreferenceName = str;
    }

    @NonNull
    private AppContextImpl getContextImpl() {
        AppContextImpl appContextImpl;
        synchronized (s_contextImplMap) {
            appContextImpl = (AppContextImpl) s_contextImplMap.get(this.mPreferenceName);
            if (appContextImpl == null) {
                appContextImpl = new AppContextImpl(this.mPreferenceName);
                s_contextImplMap.put(this.mPreferenceName, appContextImpl);
            }
        }
        return appContextImpl;
    }

    public boolean beginTransaction() {
        return getContextImpl().beginTransaction();
    }

    public boolean endTransaction() {
        return getContextImpl().endTransaction();
    }

    public String queryWithKey(String str, String str2) {
        return getContextImpl().queryWithKey(str, str2);
    }

    public boolean setKeyValue(String str, String str2, String str3) {
        return getContextImpl().setKeyValue(str, str2, str3);
    }

    public boolean setKeyValueDelayCommit(String str, String str2, String str3, long j) {
        return getContextImpl().setKeyValueDelayCommit(str, str2, str3, j);
    }

    public boolean eraseAll() {
        return getContextImpl().eraseAll();
    }

    public static boolean BAASecurity_IsEnabled() {
        return BAASecurityIsEnabledImpl();
    }
}
