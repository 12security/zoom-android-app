package com.onedrive.sdk.logger;

import android.util.Log;

public class DefaultLogger implements ILogger {
    private LoggerLevel mLevel = LoggerLevel.Error;

    /* renamed from: com.onedrive.sdk.logger.DefaultLogger$1 */
    static /* synthetic */ class C18071 {
        static final /* synthetic */ int[] $SwitchMap$com$onedrive$sdk$logger$LoggerLevel = new int[LoggerLevel.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                com.onedrive.sdk.logger.LoggerLevel[] r0 = com.onedrive.sdk.logger.LoggerLevel.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$onedrive$sdk$logger$LoggerLevel = r0
                int[] r0 = $SwitchMap$com$onedrive$sdk$logger$LoggerLevel     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.onedrive.sdk.logger.LoggerLevel r1 = com.onedrive.sdk.logger.LoggerLevel.Debug     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$onedrive$sdk$logger$LoggerLevel     // Catch:{ NoSuchFieldError -> 0x001f }
                com.onedrive.sdk.logger.LoggerLevel r1 = com.onedrive.sdk.logger.LoggerLevel.Error     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.onedrive.sdk.logger.DefaultLogger.C18071.<clinit>():void");
        }
    }

    public void logDebug(String str) {
    }

    public void logError(String str, Throwable th) {
    }

    public void setLoggingLevel(LoggerLevel loggerLevel) {
        String tag = getTag();
        StringBuilder sb = new StringBuilder();
        sb.append("Setting logging level to ");
        sb.append(loggerLevel);
        Log.i(tag, sb.toString());
        this.mLevel = loggerLevel;
    }

    public LoggerLevel getLoggingLevel() {
        return this.mLevel;
    }

    private String getTag() {
        try {
            StringBuilder sb = new StringBuilder();
            String className = Thread.currentThread().getStackTrace()[4].getClassName();
            sb.append(className.substring(className.lastIndexOf(".") + 1));
            sb.append("[");
            sb.append(Thread.currentThread().getStackTrace()[4].getMethodName());
            sb.append("] - ");
            sb.append(Thread.currentThread().getStackTrace()[4].getLineNumber());
            return sb.toString();
        } catch (Exception e) {
            Log.e("DefaultLogger", e.getMessage());
            return null;
        }
    }
}
