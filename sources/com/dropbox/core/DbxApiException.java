package com.dropbox.core;

public class DbxApiException extends DbxException {
    private static final long serialVersionUID = 0;
    private final LocalizedText userMessage;

    public DbxApiException(String str, LocalizedText localizedText, String str2) {
        super(str, str2);
        this.userMessage = localizedText;
    }

    public DbxApiException(String str, LocalizedText localizedText, String str2, Throwable th) {
        super(str, str2, th);
        this.userMessage = localizedText;
    }

    public LocalizedText getUserMessage() {
        return this.userMessage;
    }

    protected static String buildMessage(String str, LocalizedText localizedText) {
        return buildMessage(str, localizedText, null);
    }

    protected static String buildMessage(String str, LocalizedText localizedText, Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("Exception in ");
        sb.append(str);
        if (obj != null) {
            sb.append(": ");
            sb.append(obj);
        }
        if (localizedText != null) {
            sb.append(" (user message: ");
            sb.append(localizedText);
            sb.append(")");
        }
        return sb.toString();
    }
}
