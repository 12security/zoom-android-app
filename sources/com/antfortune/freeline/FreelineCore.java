package com.antfortune.freeline;

import android.app.Application;
import android.util.Log;

public class FreelineCore {
    private static final String TAG = "Freeline";

    public static void init(Application application, Application application2) {
        markFreeline();
    }

    public static void init(Application application) {
        markFreeline();
    }

    private static void markFreeline() {
        Log.i(TAG, "Freeline with runtime-no-op loaded!");
    }
}
