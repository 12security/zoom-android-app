package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.UserManager;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;

public class zzck {
    private static volatile UserManager zzzg;
    private static volatile boolean zzzh = (!zzji());

    private zzck() {
    }

    public static boolean zzji() {
        return VERSION.SDK_INT >= 24;
    }

    public static boolean isUserUnlocked(Context context) {
        return !zzji() || zzn(context);
    }

    @RequiresApi(24)
    @TargetApi(24)
    private static boolean zzn(Context context) {
        boolean z;
        boolean z2 = zzzh;
        if (!z2) {
            boolean z3 = z2;
            int i = 1;
            while (true) {
                if (i > 2) {
                    z2 = z3;
                    break;
                }
                UserManager zzo = zzo(context);
                if (zzo == null) {
                    zzzh = true;
                    return true;
                }
                try {
                    if (!zzo.isUserUnlocked()) {
                        if (zzo.isUserRunning(Process.myUserHandle())) {
                            z = false;
                            zzzh = z;
                            z2 = z;
                        }
                    }
                    z = true;
                    zzzh = z;
                    z2 = z;
                } catch (NullPointerException e) {
                    Log.w("DirectBootUtils", "Failed to check if user is unlocked", e);
                    zzzg = null;
                    i++;
                }
            }
            if (z2) {
                zzzg = null;
            }
        }
        return z2;
    }

    @RequiresApi(24)
    @VisibleForTesting
    @TargetApi(24)
    private static UserManager zzo(Context context) {
        UserManager userManager = zzzg;
        if (userManager == null) {
            synchronized (zzck.class) {
                userManager = zzzg;
                if (userManager == null) {
                    UserManager userManager2 = (UserManager) context.getSystemService(UserManager.class);
                    zzzg = userManager2;
                    userManager = userManager2;
                }
            }
        }
        return userManager;
    }
}
