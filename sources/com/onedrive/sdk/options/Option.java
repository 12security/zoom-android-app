package com.onedrive.sdk.options;

public class Option {
    private final String mName;
    private final String mValue;

    protected Option(String str, String str2) {
        this.mName = str;
        this.mValue = str2;
    }

    public String getName() {
        return this.mName;
    }

    public String getValue() {
        return this.mValue;
    }
}
