package com.zipow.videobox.view.sip;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface PBXFilterType {
    public static final int NONE = 0;
    public static final int NORMAL = 1;
    public static final int SMS = 2;
}
