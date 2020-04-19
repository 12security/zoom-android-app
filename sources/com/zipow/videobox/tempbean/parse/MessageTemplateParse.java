package com.zipow.videobox.tempbean.parse;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zipow.videobox.tempbean.IZoomMessageTemplate;
import p021us.zoom.androidlib.util.ZMLog;

public class MessageTemplateParse {
    private static final String TAG = "MessageTemplateParse";

    public static IZoomMessageTemplate parse(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JsonElement parse = new JsonParser().parse(str);
            if (parse.isJsonObject()) {
                return IZoomMessageTemplate.parse(parse.getAsJsonObject());
            }
        } catch (Exception e) {
            ZMLog.m280e(TAG, e.getMessage(), new Object[0]);
        }
        return null;
    }
}
