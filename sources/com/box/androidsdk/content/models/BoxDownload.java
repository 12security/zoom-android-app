package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.BoxDateFormat;
import com.box.androidsdk.content.utils.SdkUtils;
import java.io.File;
import java.util.Date;

public class BoxDownload extends BoxJsonObject {
    private static final String FIELD_CONTENT_LENGTH = "content_length";
    private static final String FIELD_CONTENT_TYPE = "content_type";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_END_RANGE = "end_range";
    private static final String FIELD_EXPIRATION = "expiration";
    private static final String FIELD_FILE_NAME = "file_name";
    private static final String FIELD_START_RANGE = "start_range";
    private static final String FIELD_TOTAL_RANGE = "total_range";

    public File getOutputFile() {
        return null;
    }

    public BoxDownload(String str, long j, String str2, String str3, String str4, String str5) {
        if (!SdkUtils.isEmptyString(str)) {
            setFileName(str);
        }
        this.mProperties.put(FIELD_CONTENT_LENGTH, Long.valueOf(j));
        if (!SdkUtils.isEmptyString(str2)) {
            this.mProperties.put("content_type", str2);
        }
        if (!SdkUtils.isEmptyString(str3)) {
            setContentRange(str3);
        }
        if (!SdkUtils.isEmptyString(str4)) {
            this.mProperties.put(FIELD_DATE, parseDate(str4));
        }
        if (!SdkUtils.isEmptyString(str5)) {
            this.mProperties.put(FIELD_EXPIRATION, parseDate(str5));
        }
    }

    /* access modifiers changed from: protected */
    public void setFileName(String str) {
        String[] split;
        String str2;
        for (String str3 : str.split(";")) {
            if (str3.startsWith("filename=")) {
                if (str3.endsWith("\"")) {
                    str2 = str3.substring(str3.indexOf("\"") + 1, str3.length() - 1);
                } else {
                    str2 = str3.substring(9);
                }
                this.mProperties.put(FIELD_FILE_NAME, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setContentRange(String str) {
        int lastIndexOf = str.lastIndexOf("/");
        int indexOf = str.indexOf("-");
        this.mProperties.put(FIELD_START_RANGE, Long.valueOf(Long.parseLong(str.substring(str.indexOf("bytes") + 6, indexOf))));
        this.mProperties.put(FIELD_END_RANGE, Long.valueOf(Long.parseLong(str.substring(indexOf + 1, lastIndexOf))));
        this.mProperties.put(FIELD_TOTAL_RANGE, Long.valueOf(Long.parseLong(str.substring(lastIndexOf + 1))));
    }

    public String getFileName() {
        return (String) this.mProperties.get(FIELD_FILE_NAME);
    }

    public Long getContentLength() {
        return (Long) this.mProperties.get(FIELD_CONTENT_LENGTH);
    }

    public String getContentType() {
        return (String) this.mProperties.get("content_type");
    }

    public Long getStartRange() {
        return (Long) this.mProperties.get(FIELD_START_RANGE);
    }

    public Long getEndRange() {
        return (Long) this.mProperties.get(FIELD_END_RANGE);
    }

    public Long getTotalRange() {
        return (Long) this.mProperties.get(FIELD_TOTAL_RANGE);
    }

    public Date getDate() {
        return (Date) this.mProperties.get(FIELD_DATE);
    }

    public Date getExpiration() {
        return (Date) this.mProperties.get(FIELD_EXPIRATION);
    }

    private static final Date parseDate(String str) {
        try {
            return BoxDateFormat.parseHeaderDate(str);
        } catch (Exception unused) {
            return null;
        }
    }
}
