package p021us.zoom.androidlib.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Collections;
import java.util.List;

/* renamed from: us.zoom.androidlib.util.AccessibilityUtil */
public class AccessibilityUtil {
    public static final long DELAY_SEND_FOCUS_EVENT = 200;
    public static final long DELAY_SHOW_TIP = 1000;
    private static final String TALK_BACK_SETTING_ACTIVITY_NAME = "com.android.talkback.TalkBackPreferencesActivity";

    public static boolean isSpokenFeedbackEnabled(@Nullable Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled() && !getEnabledServicesFor(accessibilityManager, 1).isEmpty()) {
            z = true;
        }
        return z;
    }

    public static boolean isTalkBack(Context context) {
        if (context == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            List<AccessibilityServiceInfo> enabledServicesFor = getEnabledServicesFor(accessibilityManager, 1);
            if (enabledServicesFor != null) {
                for (AccessibilityServiceInfo settingsActivityName : enabledServicesFor) {
                    String settingsActivityName2 = settingsActivityName.getSettingsActivityName();
                    if (!StringUtil.isEmptyOrNull(settingsActivityName2) && settingsActivityName2.equals(TALK_BACK_SETTING_ACTIVITY_NAME)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static List<AccessibilityServiceInfo> getEnabledServicesFor(AccessibilityManager accessibilityManager, int i) {
        if (accessibilityManager == null) {
            return Collections.emptyList();
        }
        return accessibilityManager.getEnabledAccessibilityServiceList(i);
    }

    public static void sendAccessibilityFocusEvent(View view) {
        if (view != null) {
            Context context = view.getContext();
            if (context != null && isSpokenFeedbackEnabled(context)) {
                view.sendAccessibilityEvent(8);
            }
        }
    }

    public static void announceNoInterruptForAccessibilityCompat(View view, int i) {
        if (view != null) {
            Context context = view.getContext();
            if (context != null) {
                announceForAccessibilityCompat(view, context.getApplicationContext().getString(i), false);
            }
        }
    }

    public static void announceForAccessibilityCompat(View view, int i) {
        if (view != null) {
            Context context = view.getContext();
            if (context != null) {
                announceForAccessibilityCompat(view, context.getApplicationContext().getString(i), true);
            }
        }
    }

    public static void announceForAccessibilityCompat(View view, CharSequence charSequence) {
        announceForAccessibilityCompat(view, charSequence, true);
    }

    public static void announceForAccessibilityCompat(View view, CharSequence charSequence, boolean z) {
        if (view != null) {
            Context context = view.getContext();
            if (context != null) {
                Context applicationContext = context.getApplicationContext();
                AccessibilityManager accessibilityManager = (AccessibilityManager) applicationContext.getSystemService("accessibility");
                if (accessibilityManager != null) {
                    int i = 16384;
                    if (z) {
                        try {
                            accessibilityManager.interrupt();
                            if (!OsUtil.isAtLeastJB()) {
                                i = 8;
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            if (!OsUtil.isAtLeastJB()) {
                                i = 8;
                            }
                        } catch (Throwable th) {
                            if (!OsUtil.isAtLeastJB()) {
                                i = 8;
                            }
                            AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
                            obtain.getText().add(charSequence);
                            obtain.setEnabled(view.isEnabled());
                            obtain.setClassName(view.getClass().getName());
                            obtain.setPackageName(applicationContext.getPackageName());
                            AccessibilityEventCompat.asRecord(obtain).setSource(view);
                            try {
                                accessibilityManager.sendAccessibilityEvent(obtain);
                            } catch (IllegalStateException unused) {
                            }
                            throw th;
                        }
                        AccessibilityEvent obtain2 = AccessibilityEvent.obtain(i);
                        obtain2.getText().add(charSequence);
                        obtain2.setEnabled(view.isEnabled());
                        obtain2.setClassName(view.getClass().getName());
                        obtain2.setPackageName(applicationContext.getPackageName());
                        AccessibilityEventCompat.asRecord(obtain2).setSource(view);
                        try {
                            accessibilityManager.sendAccessibilityEvent(obtain2);
                        } catch (IllegalStateException unused2) {
                        }
                    } else {
                        if (!OsUtil.isAtLeastJB()) {
                            i = 8;
                        }
                        AccessibilityEvent obtain3 = AccessibilityEvent.obtain(i);
                        obtain3.getText().add(charSequence);
                        obtain3.setEnabled(view.isEnabled());
                        obtain3.setClassName(view.getClass().getName());
                        obtain3.setPackageName(applicationContext.getPackageName());
                        AccessibilityEventCompat.asRecord(obtain3).setSource(view);
                        accessibilityManager.sendAccessibilityEvent(obtain3);
                    }
                }
            }
        }
    }

    public static boolean getIsAccessibilityFocused(View view) {
        if (view.getWindowToken() == null) {
            return false;
        }
        AccessibilityNodeInfoCompat createNodeInfoFromView = createNodeInfoFromView(view);
        boolean isAccessibilityFocused = createNodeInfoFromView.isAccessibilityFocused();
        createNodeInfoFromView.recycle();
        return isAccessibilityFocused;
    }

    private static AccessibilityNodeInfoCompat createNodeInfoFromView(View view) {
        AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain();
        ViewCompat.onInitializeAccessibilityNodeInfo(view, obtain);
        return obtain;
    }
}
