package p021us.zoom.androidlib.app.preference;

import android.content.Context;
import android.content.SharedPreferences.Editor;

/* renamed from: us.zoom.androidlib.app.preference.ZMPreferences */
/* compiled from: ZMPreferencesUtils */
class ZMPreferences {
    ZMPreferences() {
    }

    static synchronized boolean putString(Context context, String str, String str2, String str3) {
        boolean commit;
        synchronized (ZMPreferences.class) {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putString(str2, str3);
            commit = edit.commit();
        }
        return commit;
    }

    static synchronized boolean remove(Context context, String str, String str2) {
        boolean commit;
        synchronized (ZMPreferences.class) {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.remove(str2);
            commit = edit.commit();
        }
        return commit;
    }

    static synchronized String getString(Context context, String str, String str2) {
        String string;
        synchronized (ZMPreferences.class) {
            string = getString(context, str, str2, null);
        }
        return string;
    }

    static synchronized String getString(Context context, String str, String str2, String str3) {
        String string;
        synchronized (ZMPreferences.class) {
            string = context.getSharedPreferences(str, 0).getString(str2, str3);
        }
        return string;
    }

    static synchronized boolean putInt(Context context, String str, String str2, int i) {
        boolean commit;
        synchronized (ZMPreferences.class) {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putInt(str2, i);
            commit = edit.commit();
        }
        return commit;
    }

    static synchronized int getInt(Context context, String str, String str2) {
        int i;
        synchronized (ZMPreferences.class) {
            i = getInt(context, str, str2, -1);
        }
        return i;
    }

    static synchronized int getInt(Context context, String str, String str2, int i) {
        int i2;
        synchronized (ZMPreferences.class) {
            i2 = context.getSharedPreferences(str, 0).getInt(str2, i);
        }
        return i2;
    }

    static synchronized boolean putLong(Context context, String str, String str2, long j) {
        boolean commit;
        synchronized (ZMPreferences.class) {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putLong(str2, j);
            commit = edit.commit();
        }
        return commit;
    }

    static synchronized long getLong(Context context, String str, String str2) {
        long j;
        synchronized (ZMPreferences.class) {
            j = getLong(context, str, str2, -1);
        }
        return j;
    }

    static synchronized long getLong(Context context, String str, String str2, long j) {
        long j2;
        synchronized (ZMPreferences.class) {
            j2 = context.getSharedPreferences(str, 0).getLong(str2, j);
        }
        return j2;
    }

    static synchronized boolean putFloat(Context context, String str, String str2, float f) {
        boolean commit;
        synchronized (ZMPreferences.class) {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putFloat(str2, f);
            commit = edit.commit();
        }
        return commit;
    }

    static Editor getEditor(Context context, String str) {
        return context.getSharedPreferences(str, 0).edit();
    }

    static synchronized float getFloat(Context context, String str, String str2) {
        float f;
        synchronized (ZMPreferences.class) {
            f = getFloat(context, str, str2, -1.0f);
        }
        return f;
    }

    static synchronized float getFloat(Context context, String str, String str2, float f) {
        float f2;
        synchronized (ZMPreferences.class) {
            f2 = context.getSharedPreferences(str, 0).getFloat(str2, f);
        }
        return f2;
    }

    static synchronized boolean putBoolean(Context context, String str, String str2, boolean z) {
        boolean commit;
        synchronized (ZMPreferences.class) {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putBoolean(str2, z);
            commit = edit.commit();
        }
        return commit;
    }

    static synchronized boolean getBoolean(Context context, String str, String str2) {
        boolean z;
        synchronized (ZMPreferences.class) {
            z = getBoolean(context, str, str2, false);
        }
        return z;
    }

    static synchronized boolean getBoolean(Context context, String str, String str2, boolean z) {
        boolean z2;
        synchronized (ZMPreferences.class) {
            z2 = context.getSharedPreferences(str, 0).getBoolean(str2, z);
        }
        return z2;
    }
}
