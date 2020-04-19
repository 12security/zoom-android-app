package com.zipow.cmmlib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.mainboard.Mainboard;

public class ZoomAppPropData {
    public static final String KEY_ALTERNATE_HOST_CACHE = "android.alternate.host.cache";
    public static final String KEY_MEETINGLIST_FILTER_HOSTID = "meetinglist.filter.hostid";
    public static final String SETTINGS_VERSION = "android.settings_version";
    public static final String ZOOM_DATA_DEFAULT_SECTION_NAME = AppContext.APP_NAME_CHAT;
    @NonNull
    private static ZoomAppPropData instance = new ZoomAppPropData();

    private native boolean queryBoolImpl(String str, boolean z, String str2);

    private native long queryInt64Impl(String str, long j, String str2);

    private native int queryIntImpl(String str, int i, String str2);

    @NonNull
    private native String queryWithKeyImpl(String str, String str2, String str3);

    private native boolean setBoolImpl(String str, boolean z, String str2);

    private native boolean setInt64Impl(String str, long j, String str2);

    private native boolean setIntImpl(String str, int i, String str2);

    private native boolean setKeyValueImpl(String str, String str2, String str3);

    @NonNull
    private native String sharedQueryWithKeyImpl(String str, String str2, String str3);

    private native boolean sharedSetKeyValueImpl(String str, String str2, String str3);

    public static ZoomAppPropData getInstance() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            return null;
        }
        return instance;
    }

    private ZoomAppPropData() {
    }

    public boolean setKeyValue(String str, @Nullable String str2, String str3) {
        if (str2 == null) {
            str2 = "";
        }
        return setKeyValueImpl(str, str2, str3);
    }

    public boolean setKeyValue(String str, String str2) {
        return setKeyValue(str, str2, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    @Nullable
    public String queryWithKey(String str, @Nullable String str2, String str3) {
        String queryWithKeyImpl = queryWithKeyImpl(str, str2 == null ? "" : str2, str3);
        if (str2 != null || !"".equals(queryWithKeyImpl)) {
            return queryWithKeyImpl;
        }
        return null;
    }

    @Nullable
    public String queryWithKey(String str, String str2) {
        return queryWithKey(str, str2, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    public int queryInt(String str, int i, String str2) {
        return queryIntImpl(str, i, str2);
    }

    public int queryInt(String str, int i) {
        return queryInt(str, i, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    public boolean queryBool(String str, boolean z, String str2) {
        return queryBoolImpl(str, z, str2);
    }

    public boolean queryBool(String str, boolean z) {
        return queryBool(str, z, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    public long queryInt64(String str, long j, String str2) {
        return queryInt64Impl(str, j, str2);
    }

    public long queryInt64(String str, long j) {
        return queryInt64(str, j, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    public boolean setInt(String str, int i, String str2) {
        return setIntImpl(str, i, str2);
    }

    public boolean setInt(String str, int i) {
        return setInt(str, i, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    public boolean setBool(String str, boolean z, String str2) {
        return setBoolImpl(str, z, str2);
    }

    public boolean setBool(String str, boolean z) {
        return setBool(str, z, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    public boolean setInt64(String str, long j, String str2) {
        return setInt64Impl(str, j, str2);
    }

    public boolean setInt64(String str, long j) {
        return setInt64(str, j, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    public boolean shared_setKeyValue(String str, @Nullable String str2, String str3) {
        if (str2 == null) {
            str2 = "";
        }
        return sharedSetKeyValueImpl(str, str2, str3);
    }

    public boolean shared_setKeyValue(String str, String str2) {
        return shared_setKeyValue(str, str2, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }

    @Nullable
    public String shared_queryWithKey(String str, @Nullable String str2, String str3) {
        String sharedQueryWithKeyImpl = sharedQueryWithKeyImpl(str, str2 == null ? "" : str2, str3);
        if (str2 != null || !"".equals(sharedQueryWithKeyImpl)) {
            return sharedQueryWithKeyImpl;
        }
        return null;
    }

    @Nullable
    public String shared_queryWithKey(String str, String str2) {
        return shared_queryWithKey(str, str2, ZOOM_DATA_DEFAULT_SECTION_NAME);
    }
}
