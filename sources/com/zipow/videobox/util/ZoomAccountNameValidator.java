package com.zipow.videobox.util;

import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

public class ZoomAccountNameValidator implements IAccountNameValidator {
    @Nullable
    public String validate(String str) {
        if (StringUtil.isValidEmailAddress(str)) {
            return str;
        }
        return null;
    }
}
