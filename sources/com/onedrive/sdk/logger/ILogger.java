package com.onedrive.sdk.logger;

public interface ILogger {
    LoggerLevel getLoggingLevel();

    void logDebug(String str);

    void logError(String str, Throwable th);

    void setLoggingLevel(LoggerLevel loggerLevel);
}
