package p021us.zoom.androidlib.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

/* renamed from: us.zoom.androidlib.util.ResourcesUtil */
public class ResourcesUtil {
    private static final String TAG = "ResourcesUtil";

    public static boolean getBoolean(Resources resources, int i, boolean z) {
        if (resources == null) {
            return z;
        }
        try {
            z = resources.getBoolean(i);
        } catch (Exception unused) {
        }
        return z;
    }

    public static boolean getBoolean(Context context, int i, boolean z) {
        return context == null ? z : getBoolean(context.getResources(), i, z);
    }

    public static boolean getBoolean(View view, int i, boolean z) {
        return view == null ? z : getBoolean(view.getResources(), i, z);
    }

    public static String getString(Resources resources, int i) {
        String str = null;
        if (resources == null) {
            return null;
        }
        try {
            str = resources.getString(i);
        } catch (Exception unused) {
        }
        return str;
    }

    public static String getString(Context context, int i) {
        if (context == null) {
            return null;
        }
        return getString(context.getResources(), i);
    }

    public static String getString(View view, int i) {
        if (view == null) {
            return null;
        }
        return getString(view.getResources(), i);
    }

    public static int getInteger(Resources resources, int i, int i2) {
        if (resources == null) {
            return i2;
        }
        try {
            i2 = resources.getInteger(i);
        } catch (Exception unused) {
        }
        return i2;
    }

    public static int getInteger(Context context, int i, int i2) {
        return context == null ? i2 : getInteger(context.getResources(), i, i2);
    }

    public static int getInteger(View view, int i, int i2) {
        return view == null ? i2 : getInteger(view.getResources(), i, i2);
    }
}
