package p021us.zoom.androidlib.app.preference;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;

/* renamed from: us.zoom.androidlib.app.preference.ZMBasePreferencesProvider */
public abstract class ZMBasePreferencesProvider extends ContentProvider {
    public static String AUTHORITIES_KEY = "authorities_key";
    public static String AUTHORITIES_SPNAME = "authorities_spname";
    public static final int BOOLEAN_CONTENT_URI_CODE = 105;
    public static String COLUMNNAME = "SPCOLUMNNAME";
    public static final int DELETE_CONTENT_URI_CODE = 106;
    public static final int FLOAT_CONTENT_URI_CODE = 104;
    public static final int INTEGER_CONTENT_URI_CODE = 101;
    public static final int LONG_CONTENT_URI_CODE = 102;
    public static final int PUTS_CONTENT_URI_CODE = 107;
    public static final int STRING_CONTENT_URI_CODE = 100;
    private String mBooleanPath = "boolean/*/*/";
    private String mDeletePath = "delete/*/*/";
    private String mFloatPath = "float/*/*/";
    private String mIntegerPath = "integer/*/*/";
    private String mLongPath = "long/*/*/";
    private String mPutsPath = "puts";
    private String mStringPath = "string/*/*/";
    private UriMatcher mUriMatcher;

    /* renamed from: us.zoom.androidlib.app.preference.ZMBasePreferencesProvider$Model */
    private class Model {
        private Object defValue;
        private String key;
        private String spName;

        private Model() {
        }

        public String getSpName() {
            return this.spName;
        }

        public void setSpName(String str) {
            this.spName = str;
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String str) {
            this.key = str;
        }

        public Object getDefValue() {
            return this.defValue;
        }

        public void setDefValue(Object obj) {
            this.defValue = obj;
        }
    }

    public abstract String getAuthorities();

    public String getType(@NonNull Uri uri) {
        return null;
    }

    public boolean onCreate() {
        String authorities = getAuthorities();
        ZMPreferences.putString(getContext(), AUTHORITIES_SPNAME, AUTHORITIES_KEY, authorities);
        this.mUriMatcher = new UriMatcher(-1);
        this.mUriMatcher.addURI(authorities, this.mStringPath, 100);
        UriMatcher uriMatcher = this.mUriMatcher;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mStringPath);
        sb.append("*/");
        uriMatcher.addURI(authorities, sb.toString(), 100);
        this.mUriMatcher.addURI(authorities, this.mIntegerPath, 101);
        UriMatcher uriMatcher2 = this.mUriMatcher;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mIntegerPath);
        sb2.append("*/");
        uriMatcher2.addURI(authorities, sb2.toString(), 101);
        this.mUriMatcher.addURI(authorities, this.mLongPath, 102);
        UriMatcher uriMatcher3 = this.mUriMatcher;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mLongPath);
        sb3.append("*/");
        uriMatcher3.addURI(authorities, sb3.toString(), 102);
        this.mUriMatcher.addURI(authorities, this.mFloatPath, 104);
        UriMatcher uriMatcher4 = this.mUriMatcher;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFloatPath);
        sb4.append("*/");
        uriMatcher4.addURI(authorities, sb4.toString(), 104);
        this.mUriMatcher.addURI(authorities, this.mBooleanPath, 105);
        UriMatcher uriMatcher5 = this.mUriMatcher;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.mBooleanPath);
        sb5.append("*/");
        uriMatcher5.addURI(authorities, sb5.toString(), 105);
        this.mUriMatcher.addURI(authorities, this.mDeletePath, 106);
        this.mUriMatcher.addURI(authorities, this.mPutsPath, 107);
        return false;
    }

    public Cursor query(@NonNull Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Model model = getModel(uri);
        if (model == null) {
            return null;
        }
        return buildCursor(getContext(), model, this.mUriMatcher.match(uri));
    }

    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        Model model = getModel(uri);
        if (model == null) {
            return null;
        }
        int match = this.mUriMatcher.match(uri);
        if (match == 100 || match == 101 || match == 102 || match == 104 || match == 105 || match == 107) {
            insert(getContext(), contentValues, model);
        }
        return uri;
    }

    public int delete(@NonNull Uri uri, String str, String[] strArr) {
        Model model = getModel(uri);
        if (model == null) {
            return -1;
        }
        int match = this.mUriMatcher.match(uri);
        if (match == 100 || match == 101 || match == 102 || match == 104 || match == 105) {
            delete(getContext(), model);
        }
        return 0;
    }

    public int update(@NonNull Uri uri, ContentValues contentValues, String str, String[] strArr) {
        Model model = getModel(uri);
        if (model == null) {
            return -1;
        }
        int match = this.mUriMatcher.match(uri);
        if (match == 100 || match == 101 || match == 102 || match == 104 || match == 105) {
            insert(getContext(), contentValues, model);
        }
        return 0;
    }

    private void delete(Context context, Model model) {
        Editor editor = ZMPreferences.getEditor(context, model.getSpName());
        editor.remove(model.getKey());
        editor.apply();
    }

    private void insert(Context context, ContentValues contentValues, Model model) {
        Editor editor = ZMPreferences.getEditor(context, model.getSpName());
        for (String str : contentValues.keySet()) {
            Object obj = contentValues.get(str);
            if (obj instanceof Integer) {
                StringBuilder sb = new StringBuilder();
                sb.append(obj);
                sb.append("");
                editor.putInt(str, Integer.parseInt(sb.toString()));
            } else if (obj instanceof Long) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(obj);
                sb2.append("");
                editor.putLong(str, Long.parseLong(sb2.toString()));
            } else if (obj instanceof Float) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(obj);
                sb3.append("");
                editor.putFloat(str, Float.parseFloat(sb3.toString()));
            } else if (obj instanceof Boolean) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(obj);
                sb4.append("");
                editor.putBoolean(str, Boolean.valueOf(sb4.toString()).booleanValue());
            } else {
                StringBuilder sb5 = new StringBuilder();
                if (obj == null) {
                    obj = "";
                }
                sb5.append(obj);
                sb5.append("");
                editor.putString(str, sb5.toString());
            }
        }
        editor.apply();
    }

    private Cursor buildCursor(Context context, Model model, int i) {
        Object obj;
        Object defValue = model.getDefValue();
        switch (i) {
            case 100:
                if (defValue != null) {
                    obj = ZMPreferences.getString(context, model.getSpName(), model.getKey(), String.valueOf(defValue));
                    break;
                } else {
                    obj = ZMPreferences.getString(context, model.getSpName(), model.getKey());
                    break;
                }
            case 101:
                if (defValue != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(defValue);
                    sb.append("");
                    if (!TextUtils.isDigitsOnly(sb.toString())) {
                        defValue = Integer.valueOf(-1);
                    }
                    String spName = model.getSpName();
                    String key = model.getKey();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(defValue);
                    sb2.append("");
                    obj = Integer.valueOf(ZMPreferences.getInt(context, spName, key, Integer.parseInt(sb2.toString())));
                    break;
                } else {
                    obj = Integer.valueOf(ZMPreferences.getInt(context, model.getSpName(), model.getKey()));
                    break;
                }
            case 102:
                if (defValue != null) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(defValue);
                    sb3.append("");
                    if (!TextUtils.isDigitsOnly(sb3.toString())) {
                        defValue = Integer.valueOf(-1);
                    }
                    String spName2 = model.getSpName();
                    String key2 = model.getKey();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(defValue);
                    sb4.append("");
                    obj = Long.valueOf(ZMPreferences.getLong(context, spName2, key2, Long.parseLong(sb4.toString())));
                    break;
                } else {
                    obj = Long.valueOf(ZMPreferences.getLong(context, model.getSpName(), model.getKey()));
                    break;
                }
            case 104:
                if (defValue != null) {
                    String spName3 = model.getSpName();
                    String key3 = model.getKey();
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(defValue);
                    sb5.append("");
                    obj = Float.valueOf(ZMPreferences.getFloat(context, spName3, key3, Float.parseFloat(sb5.toString())));
                    break;
                } else {
                    obj = Float.valueOf(ZMPreferences.getFloat(context, model.getSpName(), model.getKey()));
                    break;
                }
            case 105:
                if (defValue != null) {
                    StringBuilder sb6 = new StringBuilder();
                    String spName4 = model.getSpName();
                    String key4 = model.getKey();
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(defValue);
                    sb7.append("");
                    sb6.append(ZMPreferences.getBoolean(context, spName4, key4, Boolean.valueOf(sb7.toString()).booleanValue()));
                    sb6.append("");
                    obj = sb6.toString();
                    break;
                } else {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(ZMPreferences.getBoolean(context, model.getSpName(), model.getKey()));
                    sb8.append("");
                    obj = sb8.toString();
                    break;
                }
            default:
                obj = null;
                break;
        }
        if (obj == null) {
            return null;
        }
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{COLUMNNAME});
        matrixCursor.addRow(new Object[]{obj});
        return matrixCursor;
    }

    private Model getModel(Uri uri) {
        try {
            Model model = new Model();
            model.setSpName((String) uri.getPathSegments().get(1));
            if (uri.getPathSegments().size() > 2) {
                model.setKey((String) uri.getPathSegments().get(2));
            }
            if (uri.getPathSegments().size() > 3) {
                model.setDefValue(uri.getPathSegments().get(3));
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
