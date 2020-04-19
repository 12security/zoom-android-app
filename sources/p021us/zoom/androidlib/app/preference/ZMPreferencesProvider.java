package p021us.zoom.androidlib.app.preference;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/* renamed from: us.zoom.androidlib.app.preference.ZMPreferencesProvider */
class ZMPreferencesProvider {
    ZMPreferencesProvider() {
    }

    static boolean putString(Context context, String str, String str2, String str3) {
        Uri buildUri = buildUri(context, 100, str, str2, str3);
        ContentResolver contentResolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(str2, str3);
            contentResolver.insert(buildUri, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean remove(Context context, String str, String str2) {
        try {
            context.getContentResolver().delete(buildUri(context, 106, str, str2, null), null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static String getString(Context context, String str, String str2) {
        return getString(context, str, str2, "");
    }

    static String getString(Context context, String str, String str2, String str3) {
        Cursor query = context.getContentResolver().query(buildUri(context, 100, str, str2, str3), null, null, null, null);
        if (query == null) {
            return str3;
        }
        if (query.moveToNext()) {
            str3 = query.getString(query.getColumnIndex(ZMBasePreferencesProvider.COLUMNNAME));
        }
        query.close();
        return str3;
    }

    static boolean putInt(Context context, String str, String str2, int i) {
        Uri buildUri = buildUri(context, 101, str, str2, Integer.valueOf(i));
        ContentResolver contentResolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(str2, Integer.valueOf(i));
            contentResolver.insert(buildUri, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static int getInt(Context context, String str, String str2) {
        return getInt(context, str, str2, -1);
    }

    static int getInt(Context context, String str, String str2, int i) {
        Cursor query = context.getContentResolver().query(buildUri(context, 101, str, str2, Integer.valueOf(i)), null, null, null, null);
        if (query == null) {
            return i;
        }
        if (query.moveToNext()) {
            i = query.getInt(query.getColumnIndex(ZMBasePreferencesProvider.COLUMNNAME));
        }
        query.close();
        return i;
    }

    static boolean putLong(Context context, String str, String str2, long j) {
        Uri buildUri = buildUri(context, 102, str, str2, Long.valueOf(j));
        ContentResolver contentResolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(str2, Long.valueOf(j));
            contentResolver.insert(buildUri, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static long getLong(Context context, String str, String str2) {
        return getLong(context, str, str2, -1);
    }

    static long getLong(Context context, String str, String str2, long j) {
        Cursor query = context.getContentResolver().query(buildUri(context, 102, str, str2, Long.valueOf(j)), null, null, null, null);
        if (query == null) {
            return j;
        }
        if (query.moveToNext()) {
            j = query.getLong(query.getColumnIndex(ZMBasePreferencesProvider.COLUMNNAME));
        }
        query.close();
        return j;
    }

    static boolean putFloat(Context context, String str, String str2, float f) {
        Uri buildUri = buildUri(context, 104, str, str2, Float.valueOf(f));
        ContentResolver contentResolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(str2, Float.valueOf(f));
            contentResolver.insert(buildUri, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static float getFloat(Context context, String str, String str2) {
        return getFloat(context, str, str2, -1.0f);
    }

    static float getFloat(Context context, String str, String str2, float f) {
        Cursor query = context.getContentResolver().query(buildUri(context, 104, str, str2, Float.valueOf(f)), null, null, null, null);
        if (query == null) {
            return f;
        }
        if (query.moveToNext()) {
            f = query.getFloat(query.getColumnIndex(ZMBasePreferencesProvider.COLUMNNAME));
        }
        query.close();
        return f;
    }

    static boolean putBoolean(Context context, String str, String str2, boolean z) {
        Uri buildUri = buildUri(context, 105, str, str2, Boolean.valueOf(z));
        ContentResolver contentResolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(str2, Boolean.valueOf(z));
            contentResolver.insert(buildUri, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean getBoolean(Context context, String str, String str2) {
        return getBoolean(context, str, str2, false);
    }

    static boolean getBoolean(Context context, String str, String str2, boolean z) {
        Cursor query = context.getContentResolver().query(buildUri(context, 105, str, str2, Boolean.valueOf(z)), null, null, null, null);
        if (query == null) {
            return z;
        }
        if (query.moveToNext()) {
            z = Boolean.valueOf(query.getString(query.getColumnIndex(ZMBasePreferencesProvider.COLUMNNAME))).booleanValue();
        }
        query.close();
        return z;
    }

    static boolean put(Context context, String str, ContentValues contentValues) {
        try {
            context.getContentResolver().insert(buildUri(context, 107, str, null, null), contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Uri buildUri(Context context, int i, String str, String str2, Object obj) {
        String string = ZMPreferences.getString(context, ZMBasePreferencesProvider.AUTHORITIES_SPNAME, ZMBasePreferencesProvider.AUTHORITIES_KEY);
        switch (i) {
            case 100:
                StringBuilder sb = new StringBuilder();
                sb.append("content://");
                sb.append(string);
                sb.append("/string/");
                sb.append(str);
                sb.append("/");
                sb.append(str2);
                sb.append("/");
                sb.append(obj);
                return Uri.parse(sb.toString());
            case 101:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("content://");
                sb2.append(string);
                sb2.append("/integer/");
                sb2.append(str);
                sb2.append("/");
                sb2.append(str2);
                sb2.append("/");
                sb2.append(obj);
                return Uri.parse(sb2.toString());
            case 102:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("content://");
                sb3.append(string);
                sb3.append("/long/");
                sb3.append(str);
                sb3.append("/");
                sb3.append(str2);
                sb3.append("/");
                sb3.append(obj);
                return Uri.parse(sb3.toString());
            case 104:
                StringBuilder sb4 = new StringBuilder();
                sb4.append("content://");
                sb4.append(string);
                sb4.append("/float/");
                sb4.append(str);
                sb4.append("/");
                sb4.append(str2);
                sb4.append("/");
                sb4.append(obj);
                return Uri.parse(sb4.toString());
            case 105:
                StringBuilder sb5 = new StringBuilder();
                sb5.append("content://");
                sb5.append(string);
                sb5.append("/boolean/");
                sb5.append(str);
                sb5.append("/");
                sb5.append(str2);
                sb5.append("/");
                sb5.append(obj);
                return Uri.parse(sb5.toString());
            case 106:
                StringBuilder sb6 = new StringBuilder();
                sb6.append("content://");
                sb6.append(string);
                sb6.append("/delete/");
                sb6.append(str);
                sb6.append("/");
                sb6.append(str2);
                return Uri.parse(sb6.toString());
            case 107:
                StringBuilder sb7 = new StringBuilder();
                sb7.append("content://");
                sb7.append(string);
                sb7.append("/puts");
                return Uri.parse(sb7.toString());
            default:
                return null;
        }
    }
}
