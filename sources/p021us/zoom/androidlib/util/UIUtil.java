package p021us.zoom.androidlib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.util.ZMDomainUtil;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;

/* renamed from: us.zoom.androidlib.util.UIUtil */
public class UIUtil {
    public static final String AT_ALL_SUFFIX = ".atall";
    private static final int MIUI_VERSION_NOT_SET = -2;
    public static final int MIUI_VERSION_UNDEFINED = -1;
    public static final int MIUI_VERSION_V2 = 0;
    public static final int MIUI_VERSION_V3 = 1;
    public static final int MIUI_VERSION_V4 = 2;
    public static final int MIUI_VERSION_V5 = 3;
    public static final int MIUI_VERSION_V6 = 4;
    private static final int TABLET_MIN_SCREEN_SIZE = 520;
    private static final String TAG = "UIUtil";
    private static int gMiuiVersionCode = -2;
    private static WakeLock gProxiWakeLock;

    /* renamed from: us.zoom.androidlib.util.UIUtil$CommandResult */
    public static class CommandResult {
        public String errorMsg;
        public int result;
        public String successMsg;

        public CommandResult(int i, String str, String str2) {
            this.result = i;
            this.successMsg = str;
            this.errorMsg = str2;
        }
    }

    public static int dip2px(Context context, float f) {
        return context == null ? (int) f : (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static float px2dip(Context context, int i) {
        if (context == null) {
            return (float) i;
        }
        return ((float) i) / context.getResources().getDisplayMetrics().density;
    }

    public static boolean isNavtiveSupportIOSEmoji() {
        return VERSION.SDK_INT >= 17;
    }

    public static int sp2px(Context context, float f) {
        return context == null ? (int) f : (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static float px2sp(Context context, int i) {
        if (context == null) {
            return (float) i;
        }
        return ((float) i) / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float getDisplayMinWidthInDip(Context context) {
        return px2dip(context, Math.min(getDisplayWidth(context), getDisplayHeight(context)));
    }

    public static boolean isFullScreen(Activity activity) {
        return activity != null && (activity.getWindow().getAttributes().flags & 1024) == 1024;
    }

    public static void closeSoftKeyboard(Context context, View view, int i) {
        if (context != null && view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), i);
            }
        }
    }

    public static void closeSoftKeyboard(Context context, View view) {
        closeSoftKeyboard(context, view, 0);
    }

    public static void closeSoftKeyboardInActivity(ZMActivity zMActivity) {
        if (zMActivity != null) {
            Window window = zMActivity.getWindow();
            if (window != null) {
                IBinder windowToken = window.getDecorView().getWindowToken();
                if (windowToken != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) zMActivity.getSystemService("input_method");
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
                    }
                }
            }
        }
    }

    public static void openSoftKeyboard(Context context, View view, int i) {
        if (context != null && view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(view, i);
            }
        }
    }

    public static void openSoftKeyboard(Context context, View view) {
        openSoftKeyboard(context, view, 0);
    }

    public static int getDisplayWidth(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return 0;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        if (!OsUtil.isAtLeastICS_MR1()) {
            return defaultDisplay.getWidth();
        }
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.x;
    }

    public static float getDisplayWidthInDip(@NonNull Context context) {
        return px2dip(context, getDisplayWidth(context));
    }

    public static int getDisplayHeight(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return 0;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        if (!OsUtil.isAtLeastICS_MR1()) {
            return defaultDisplay.getHeight();
        }
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.y;
    }

    public static float getDisplayHeightInDip(Context context) {
        return px2dip(context, getDisplayHeight(context));
    }

    public static int getMetricWith(Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getMetricHeight(Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getCurrentOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static boolean isPortraitMode(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    public static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static boolean isXLargeScreen(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 4;
    }

    public static boolean isLargeScreen(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static boolean isPhone(Context context) {
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        return packageManager.hasSystemFeature("android.hardware.telephony");
    }

    public static boolean isSmallScreen(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (getDisplayHeightInDip(context) < 520.0f || getDisplayWidthInDip(context) < 520.0f) {
            z = true;
        }
        return z;
    }

    public static boolean isTabletOrTV(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (isTablet(context) || isTV(context)) {
            z = true;
        }
        return z;
    }

    public static boolean isTablet(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (getDisplayHeightInDip(context) >= 520.0f && getDisplayWidthInDip(context) >= 520.0f && !isTV(context)) {
            z = true;
        }
        return z;
    }

    public static boolean isTV(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        if (VERSION.SDK_INT < 21) {
            return packageManager.hasSystemFeature("android.hardware.type.television");
        }
        if (packageManager.hasSystemFeature("android.hardware.type.television") || packageManager.hasSystemFeature("android.software.leanback")) {
            z = true;
        }
        return z;
    }

    public static String getScreenCategoryName(Context context) {
        int i = context.getResources().getConfiguration().screenLayout & 15;
        if (i >= 4) {
            return "xlarge";
        }
        if (i >= 3) {
            return "large";
        }
        return i >= 2 ? "normal" : "small";
    }

    @NonNull
    public static Rect getAbsoluteRect(@NonNull View view) {
        Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            Rect absoluteRect = getAbsoluteRect((ViewGroup) parent);
            rect.left += absoluteRect.left;
            rect.top += absoluteRect.top;
            rect.right += absoluteRect.left;
            rect.bottom += absoluteRect.top;
        }
        return rect;
    }

    public static boolean openURL(Context context, String str) {
        String str2;
        if (str == null || str.length() == 0) {
            return false;
        }
        try {
            String scheme = Uri.parse(str).getScheme();
            if (StringUtil.isEmptyOrNull(scheme)) {
                StringBuilder sb = new StringBuilder();
                sb.append(ZMDomainUtil.ZM_URL_HTTP);
                sb.append(str);
                str2 = sb.toString();
            } else {
                int length = scheme.length();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(scheme.toLowerCase());
                sb2.append(str.substring(length));
                str2 = sb2.toString();
            }
            Uri parse = Uri.parse(str2);
            Intent intent = new Intent();
            intent.addFlags(268435456);
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(parse);
            context.startActivity(intent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static int getDefaultOrientation(Context context) {
        int i;
        int i2;
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return 0;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        switch (defaultDisplay.getRotation()) {
            case 0:
            case 2:
                i2 = defaultDisplay.getWidth();
                i = defaultDisplay.getHeight();
                break;
            case 1:
            case 3:
                i2 = defaultDisplay.getHeight();
                i = defaultDisplay.getWidth();
                break;
            default:
                i = 0;
                i2 = 0;
                break;
        }
        if (i2 > i) {
            return 0;
        }
        return 1;
    }

    public static synchronized void startProximityScreenOffWakeLock(Context context) {
        synchronized (UIUtil.class) {
            if (gProxiWakeLock == null || !gProxiWakeLock.isHeld()) {
                try {
                    PowerManager powerManager = (PowerManager) context.getSystemService("power");
                    if (powerManager != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(UIUtil.class.getName());
                        sb.append(":proximitiy");
                        gProxiWakeLock = powerManager.newWakeLock(32, sb.toString());
                        if (gProxiWakeLock != null) {
                            gProxiWakeLock.acquire();
                        }
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    ZMLog.m289w(TAG, e, "startProximityScreenOffWakeLock failure", new Object[0]);
                }
            } else {
                return;
            }
        }
        return;
    }

    public static synchronized void stopProximityScreenOffWakeLock() {
        synchronized (UIUtil.class) {
            if (gProxiWakeLock != null) {
                try {
                    gProxiWakeLock.release();
                } catch (Exception e) {
                    ZMLog.m289w(TAG, e, "stopProximityScreenOffWakeLock failure", new Object[0]);
                }
                gProxiWakeLock = null;
            }
        }
    }

    public static boolean gProxiWakeLockHeld() {
        WakeLock wakeLock = gProxiWakeLock;
        return wakeLock != null && wakeLock.isHeld();
    }

    public static void buildLinkTextView(TextView textView, String str, final OnClickListener onClickListener) {
        if (!StringUtil.isEmptyOrNull(str)) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new URLSpan("") {
                public void onClick(View view) {
                    OnClickListener onClickListener = onClickListener;
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    }
                }
            }, 0, str.length(), 33);
            textView.setText(spannableString);
        }
    }

    @Nullable
    public static Dialog showSimpleMessageDialog(Activity activity, String str, String str2) {
        if (activity == null) {
            return null;
        }
        ZMAlertDialog create = new Builder(activity).setTitle((CharSequence) str).setMessage(str2).setCancelable(true).setPositiveButton(C4409R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.show();
        return create;
    }

    @Nullable
    public static Dialog showSimpleMessageDialog(Activity activity, int i, int i2) {
        String str = null;
        if (activity == null) {
            return null;
        }
        String string = i > 0 ? activity.getString(i) : null;
        if (i2 > 0) {
            str = activity.getString(i2);
        }
        return showSimpleMessageDialog(activity, string, str);
    }

    @NonNull
    public static ProgressDialog showSimpleWaitingDialog(@NonNull Activity activity, String str) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.requestWindowFeature(1);
        progressDialog.setMessage(str);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }

    @NonNull
    public static ProgressDialog showSimpleWaitingDialog(@NonNull Activity activity, int i) {
        return showSimpleWaitingDialog(activity, i > 0 ? activity.getString(i) : "");
    }

    @SuppressLint({"NewApi"})
    public static boolean setTranslucentStatus(Activity activity, boolean z) {
        if (VERSION.SDK_INT < 19 || activity == null) {
            return false;
        }
        Window window = activity.getWindow();
        if (window == null) {
            return false;
        }
        window.setFlags(67108864, 67108864);
        if (VERSION.SDK_INT >= 21) {
            window.clearFlags(Integer.MIN_VALUE);
        }
        return true;
    }

    public static void renderStatueBar(Activity activity, boolean z, int i) {
        if (VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            if (VERSION.SDK_INT >= 23) {
                View decorView = window.getDecorView();
                int systemUiVisibility = decorView.getSystemUiVisibility();
                decorView.setSystemUiVisibility(z ? systemUiVisibility | 8192 : systemUiVisibility & -8193);
            }
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(ContextCompat.getColor(activity, i));
        }
    }

    public static String getSystemProperty(String str, String str2) {
        if (str == null) {
            return str2;
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            return properties.getProperty(str, str2);
        } catch (Exception unused) {
            return str2;
        }
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return dip2px(context, 25.0f);
    }

    public static int getMIUIVersionCode() {
        if (gMiuiVersionCode == -2) {
            String systemProperty = getSystemProperty("ro.miui.ui.version.code", "-1");
            gMiuiVersionCode = -1;
            try {
                gMiuiVersionCode = Integer.parseInt(systemProperty);
            } catch (Exception unused) {
            }
        }
        return gMiuiVersionCode;
    }

    public static CommandResult execCmd(String str, boolean z) {
        return execCmd(new String[]{str}, z, true);
    }

    public static CommandResult execCmd(List<String> list, boolean z, boolean z2) {
        return execCmd(list == null ? null : (String[]) list.toArray(new String[0]), z, z2);
    }

    public static String getProperty(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("getprop ");
        sb.append(str);
        CommandResult execCmd = execCmd(sb.toString(), false);
        return !TextUtils.isEmpty(execCmd.successMsg) ? execCmd.successMsg : "";
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r14v1, types: [java.lang.StringBuilder] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r0v2, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r14v2 */
    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.Closeable[]] */
    /* JADX WARNING: type inference failed for: r14v3 */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r8v1 */
    /* JADX WARNING: type inference failed for: r14v4 */
    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r14v5, types: [java.io.Closeable[]] */
    /* JADX WARNING: type inference failed for: r8v3 */
    /* JADX WARNING: type inference failed for: r14v6 */
    /* JADX WARNING: type inference failed for: r14v7 */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r14v8 */
    /* JADX WARNING: type inference failed for: r8v4 */
    /* JADX WARNING: type inference failed for: r14v9 */
    /* JADX WARNING: type inference failed for: r8v7 */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.io.Closeable[]] */
    /* JADX WARNING: type inference failed for: r14v10 */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r8v8 */
    /* JADX WARNING: type inference failed for: r14v11 */
    /* JADX WARNING: type inference failed for: r14v12, types: [java.lang.StringBuilder] */
    /* JADX WARNING: type inference failed for: r7v8 */
    /* JADX WARNING: type inference failed for: r7v9, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r8v10 */
    /* JADX WARNING: type inference failed for: r8v11 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r8v12 */
    /* JADX WARNING: type inference failed for: r8v13, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r14v13 */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* JADX WARNING: type inference failed for: r14v14 */
    /* JADX WARNING: type inference failed for: r14v15 */
    /* JADX WARNING: type inference failed for: r14v16 */
    /* JADX WARNING: type inference failed for: r14v17 */
    /* JADX WARNING: type inference failed for: r14v18 */
    /* JADX WARNING: type inference failed for: r14v19 */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r8v15 */
    /* JADX WARNING: type inference failed for: r8v16 */
    /* JADX WARNING: type inference failed for: r8v17 */
    /* JADX WARNING: type inference failed for: r8v18 */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x009a, code lost:
        r7 = 0;
        r14 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00ad, code lost:
        if (r13 != null) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00af, code lost:
        r13.destroy();
        r14 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00b3, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00b4, code lost:
        r8 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00b6, code lost:
        r12 = null;
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00ca, code lost:
        r13.destroy();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00de, code lost:
        if (r13 != null) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00e3, code lost:
        if (r12 != null) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00e5, code lost:
        r12 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00e7, code lost:
        r12 = r12.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00eb, code lost:
        if (r14 != 0) goto L_0x00ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00ee, code lost:
        r0 = r14.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00f5, code lost:
        return new p021us.zoom.androidlib.util.UIUtil.CommandResult(r1, r12, r0);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r14v3
      assigns: []
      uses: []
      mth insns count: 122
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b3 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:13:0x0025] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ca  */
    /* JADX WARNING: Unknown variable types count: 20 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static p021us.zoom.androidlib.util.UIUtil.CommandResult execCmd(java.lang.String[] r12, boolean r13, boolean r14) {
        /*
            r0 = 0
            r1 = -1
            if (r12 == 0) goto L_0x00fc
            int r2 = r12.length
            if (r2 != 0) goto L_0x0009
            goto L_0x00fc
        L_0x0009:
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x00f6 }
            if (r13 == 0) goto L_0x0012
            java.lang.String r13 = "su"
            goto L_0x0014
        L_0x0012:
            java.lang.String r13 = "sh"
        L_0x0014:
            java.lang.Process r13 = r2.exec(r13)     // Catch:{ IOException -> 0x00f6 }
            r2 = 2
            r3 = 3
            r4 = 1
            r5 = 0
            java.io.DataOutputStream r6 = new java.io.DataOutputStream     // Catch:{ Throwable -> 0x00ce, all -> 0x00ba }
            java.io.OutputStream r7 = r13.getOutputStream()     // Catch:{ Throwable -> 0x00ce, all -> 0x00ba }
            r6.<init>(r7)     // Catch:{ Throwable -> 0x00ce, all -> 0x00ba }
            int r7 = r12.length     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            r8 = 0
        L_0x0027:
            if (r8 >= r7) goto L_0x0040
            r9 = r12[r8]     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            if (r9 != 0) goto L_0x002e
            goto L_0x003d
        L_0x002e:
            byte[] r9 = r9.getBytes()     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            r6.write(r9)     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            java.lang.String r9 = "\n"
            r6.writeBytes(r9)     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            r6.flush()     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
        L_0x003d:
            int r8 = r8 + 1
            goto L_0x0027
        L_0x0040:
            java.lang.String r12 = "exit\n"
            r6.writeBytes(r12)     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            r6.flush()     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            int r1 = r13.waitFor()     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            if (r14 == 0) goto L_0x009e
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            r12.<init>()     // Catch:{ Throwable -> 0x00b6, all -> 0x00b3 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009c, all -> 0x00b3 }
            r14.<init>()     // Catch:{ Throwable -> 0x009c, all -> 0x00b3 }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x009a, all -> 0x00b3 }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x009a, all -> 0x00b3 }
            java.io.InputStream r9 = r13.getInputStream()     // Catch:{ Throwable -> 0x009a, all -> 0x00b3 }
            java.nio.charset.Charset r10 = p021us.zoom.androidlib.util.CompatUtils.getStardardCharSetUTF8()     // Catch:{ Throwable -> 0x009a, all -> 0x00b3 }
            r8.<init>(r9, r10)     // Catch:{ Throwable -> 0x009a, all -> 0x00b3 }
            r7.<init>(r8)     // Catch:{ Throwable -> 0x009a, all -> 0x00b3 }
            java.io.BufferedReader r8 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0098, all -> 0x0094 }
            java.io.InputStreamReader r9 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x0098, all -> 0x0094 }
            java.io.InputStream r10 = r13.getErrorStream()     // Catch:{ Throwable -> 0x0098, all -> 0x0094 }
            java.nio.charset.Charset r11 = p021us.zoom.androidlib.util.CompatUtils.getStardardCharSetUTF8()     // Catch:{ Throwable -> 0x0098, all -> 0x0094 }
            r9.<init>(r10, r11)     // Catch:{ Throwable -> 0x0098, all -> 0x0094 }
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0098, all -> 0x0094 }
        L_0x007c:
            java.lang.String r9 = r7.readLine()     // Catch:{ Throwable -> 0x0092, all -> 0x0090 }
            if (r9 == 0) goto L_0x0086
            r12.append(r9)     // Catch:{ Throwable -> 0x0092, all -> 0x0090 }
            goto L_0x007c
        L_0x0086:
            java.lang.String r9 = r8.readLine()     // Catch:{ Throwable -> 0x0092, all -> 0x0090 }
            if (r9 == 0) goto L_0x00a2
            r14.append(r9)     // Catch:{ Throwable -> 0x0092, all -> 0x0090 }
            goto L_0x0086
        L_0x0090:
            r12 = move-exception
            goto L_0x0096
        L_0x0092:
            goto L_0x00d3
        L_0x0094:
            r12 = move-exception
            r8 = r0
        L_0x0096:
            r0 = r7
            goto L_0x00bd
        L_0x0098:
            r8 = r0
            goto L_0x00d3
        L_0x009a:
            r7 = r0
            goto L_0x00d2
        L_0x009c:
            r14 = r0
            goto L_0x00b8
        L_0x009e:
            r12 = r0
            r14 = r12
            r7 = r14
            r8 = r7
        L_0x00a2:
            java.io.Closeable[] r3 = new java.io.Closeable[r3]
            r3[r5] = r6
            r3[r4] = r7
            r3[r2] = r8
            closeIO(r3)
            if (r13 == 0) goto L_0x00e1
        L_0x00af:
            r13.destroy()
            goto L_0x00e1
        L_0x00b3:
            r12 = move-exception
            r8 = r0
            goto L_0x00bd
        L_0x00b6:
            r12 = r0
            r14 = r12
        L_0x00b8:
            r7 = r14
            goto L_0x00d2
        L_0x00ba:
            r12 = move-exception
            r6 = r0
            r8 = r6
        L_0x00bd:
            java.io.Closeable[] r14 = new java.io.Closeable[r3]
            r14[r5] = r6
            r14[r4] = r0
            r14[r2] = r8
            closeIO(r14)
            if (r13 == 0) goto L_0x00cd
            r13.destroy()
        L_0x00cd:
            throw r12
        L_0x00ce:
            r12 = r0
            r14 = r12
            r6 = r14
            r7 = r6
        L_0x00d2:
            r8 = r7
        L_0x00d3:
            java.io.Closeable[] r3 = new java.io.Closeable[r3]
            r3[r5] = r6
            r3[r4] = r7
            r3[r2] = r8
            closeIO(r3)
            if (r13 == 0) goto L_0x00e1
            goto L_0x00af
        L_0x00e1:
            us.zoom.androidlib.util.UIUtil$CommandResult r13 = new us.zoom.androidlib.util.UIUtil$CommandResult
            if (r12 != 0) goto L_0x00e7
            r12 = r0
            goto L_0x00eb
        L_0x00e7:
            java.lang.String r12 = r12.toString()
        L_0x00eb:
            if (r14 != 0) goto L_0x00ee
            goto L_0x00f2
        L_0x00ee:
            java.lang.String r0 = r14.toString()
        L_0x00f2:
            r13.<init>(r1, r12, r0)
            return r13
        L_0x00f6:
            us.zoom.androidlib.util.UIUtil$CommandResult r12 = new us.zoom.androidlib.util.UIUtil$CommandResult
            r12.<init>(r1, r0, r0)
            return r12
        L_0x00fc:
            us.zoom.androidlib.util.UIUtil$CommandResult r12 = new us.zoom.androidlib.util.UIUtil$CommandResult
            r12.<init>(r1, r0, r0)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.UIUtil.execCmd(java.lang.String[], boolean, boolean):us.zoom.androidlib.util.UIUtil$CommandResult");
    }

    public static void closeIO(Closeable... closeableArr) {
        if (closeableArr != null) {
            for (Closeable closeable : closeableArr) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException unused) {
                    }
                }
            }
        }
    }

    public static boolean hasBuildProperty(String str) {
        return !TextUtils.isEmpty(getProperty(str));
    }

    public static boolean isMIUI() {
        return hasBuildProperty("ro.miui.ui.version.code") || hasBuildProperty("ro.miui.ui.version.name");
    }

    public static boolean isFlymeOS() {
        String str = Build.FINGERPRINT;
        if (str == null) {
            return false;
        }
        return str.contains("Meizu");
    }

    public static boolean isEMUI() {
        return !StringUtil.isEmptyOrNull(getProperty("ro.build.version.emui"));
    }

    public static boolean isImmersedModeSupported() {
        if (getMIUIVersionCode() >= 4) {
            return true;
        }
        if (!isFlymeOS() || VERSION.SDK_INT < 19) {
            return false;
        }
        return true;
    }

    public static boolean isSamsung() {
        return "samsung".equals(Build.BRAND);
    }

    public static boolean setStatusBarDarkMode(Activity activity, boolean z) {
        if (activity == null) {
            return false;
        }
        if (getMIUIVersionCode() >= 4) {
            return setStatusBarDarkMode_MIUI(activity, z);
        }
        if (!isFlymeOS() || VERSION.SDK_INT < 19) {
            return false;
        }
        return setStatusBarDarkMode_FlymeOS(activity, z);
    }

    private static boolean setStatusBarDarkMode_MIUI(Activity activity, boolean z) {
        Class cls = activity.getWindow().getClass();
        try {
            int i = Class.forName("android.view.MiuiWindowManager$LayoutParams").getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(null);
            Method method = cls.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
            Window window = activity.getWindow();
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(z ? i : 0);
            objArr[1] = Integer.valueOf(i);
            method.invoke(window, objArr);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean setStatusBarDarkMode_FlymeOS(Activity activity, boolean z) {
        Window window = activity.getWindow();
        if (window != null) {
            try {
                LayoutParams attributes = window.getAttributes();
                Field declaredField = LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                window.setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static boolean setNotificationMessageCount(Context context, @Nullable Notification notification, int i) {
        try {
            if (isMIUI() && notification != null) {
                Object newInstance = Class.forName("android.app.MiuiNotification").newInstance();
                Field declaredField = newInstance.getClass().getDeclaredField("messageCount");
                declaredField.setAccessible(true);
                declaredField.set(newInstance, Integer.valueOf(i));
                notification.getClass().getField("extraNotification").set(notification, newInstance);
            }
            setBadgeNumForEMUI(context, i);
            setBadgeNumForSamsung(context, i);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void setBadgeNumForEMUI(Context context, int i) {
        if (isEMUI()) {
            boolean z = false;
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            if (frontActivity != null && frontActivity.isActive()) {
                z = true;
            }
            if (i <= 0 || !z) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("package", context.getPackageName());
                    bundle.putString("class", "com.zipow.videobox.LauncherActivity");
                    bundle.putInt("badgenumber", i);
                    context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void setBadgeNumForSamsung(Context context, int i) {
        if (isSamsung()) {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", i);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", "com.zipow.videobox.LauncherActivity");
            context.sendBroadcast(intent);
        }
    }

    public static void showWaitingDialog(FragmentManager fragmentManager, int i, String str) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str) && i != 0) {
            WaitingDialog newInstance = WaitingDialog.newInstance(i);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, str);
        }
    }

    public static void showWaitingDialog(FragmentManager fragmentManager, String str, String str2) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str2)) {
            WaitingDialog newInstance = WaitingDialog.newInstance(str);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, str2);
        }
    }

    public static void dismissWaitingDialog(FragmentManager fragmentManager, String str) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str)) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag(str);
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public static void updateFileFromDatabase(Context context, File file) {
        if (VERSION.SDK_INT >= 19) {
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, null);
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("file://");
        sb.append(Environment.getExternalStorageDirectory());
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb.toString())));
    }

    public static boolean isScreenLocked(@NonNull Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
        return keyguardManager != null && keyguardManager.isKeyguardLocked();
    }

    public static boolean isLegalSpanIndex(@Nullable Spanned spanned, int i, int i2) {
        if (!TextUtils.isEmpty(spanned) && i >= 0 && i2 >= 0 && i2 >= i && i < spanned.length() && i2 < spanned.length()) {
            return true;
        }
        return false;
    }

    public static String generateAtallSessionId(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(AT_ALL_SUFFIX);
        return sb.toString();
    }
}
