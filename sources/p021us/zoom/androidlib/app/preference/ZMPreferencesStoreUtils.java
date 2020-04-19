package p021us.zoom.androidlib.app.preference;

import android.content.Context;

/* renamed from: us.zoom.androidlib.app.preference.ZMPreferencesStoreUtils */
public class ZMPreferencesStoreUtils {
    public static final String PREFERENCE_PROVIDER_DEFAULT_SP_NAME = "PREFERENCE_PROVIDER_DEFAULT_SP_NAME";

    public static boolean putString(Context context, String str, String str2, String str3, boolean z) {
        return z ? ZMPreferencesProvider.putString(context, str, str2, str3) : ZMPreferences.putString(context, str, str2, str3);
    }

    public static boolean remove(Context context, String str, String str2, boolean z) {
        return z ? ZMPreferencesProvider.remove(context, str, str2) : ZMPreferences.remove(context, str, str2);
    }

    public static String getString(Context context, String str, String str2, boolean z) {
        return getString(context, str, str2, null, z);
    }

    public static String getString(Context context, String str, String str2, String str3, boolean z) {
        return z ? ZMPreferencesProvider.getString(context, str, str2, str3) : ZMPreferences.getString(context, str, str2, str3);
    }

    public static boolean putInt(Context context, String str, String str2, int i, boolean z) {
        return z ? ZMPreferencesProvider.putInt(context, str, str2, i) : ZMPreferences.putInt(context, str, str2, i);
    }

    public static int getInt(Context context, String str, String str2, boolean z) {
        return getInt(context, str, str2, -1, z);
    }

    public static int getInt(Context context, String str, String str2, int i, boolean z) {
        return z ? ZMPreferencesProvider.getInt(context, str, str2, i) : ZMPreferences.getInt(context, str, str2, i);
    }

    public static boolean putLong(Context context, String str, String str2, long j, boolean z) {
        return z ? ZMPreferencesProvider.putLong(context, str, str2, j) : ZMPreferences.putLong(context, str, str2, j);
    }

    public static long getLong(Context context, String str, String str2, boolean z) {
        return getLong(context, str, str2, -1, z);
    }

    public static long getLong(Context context, String str, String str2, long j, boolean z) {
        return z ? ZMPreferencesProvider.getLong(context, str, str2, j) : ZMPreferences.getLong(context, str, str2, j);
    }

    public static boolean putFloat(Context context, String str, String str2, float f, boolean z) {
        return z ? ZMPreferencesProvider.putFloat(context, str, str2, f) : ZMPreferences.putFloat(context, str, str2, f);
    }

    public static float getFloat(Context context, String str, String str2, boolean z) {
        return getFloat(context, str, str2, -1.0f, z);
    }

    public static float getFloat(Context context, String str, String str2, float f, boolean z) {
        return z ? ZMPreferencesProvider.getFloat(context, str, str2, f) : ZMPreferences.getFloat(context, str, str2, f);
    }

    public static boolean putBoolean(Context context, String str, String str2, boolean z, boolean z2) {
        return z2 ? ZMPreferencesProvider.putBoolean(context, str, str2, z) : ZMPreferences.putBoolean(context, str, str2, z);
    }

    public static boolean getBoolean(Context context, String str, String str2, boolean z) {
        return getBoolean(context, str, str2, false, z);
    }

    public static boolean getBoolean(Context context, String str, String str2, boolean z, boolean z2) {
        return z2 ? ZMPreferencesProvider.getBoolean(context, str, str2, z) : ZMPreferences.getBoolean(context, str, str2, z);
    }
}
