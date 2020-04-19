package com.zipow.videobox.ptapp;

import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.androidlib.util.UIUtil;

public class PTAppHelper {
    public static boolean openURL(String str) {
        return UIUtil.openURL(VideoBoxApplication.getInstance(), str);
    }
}
