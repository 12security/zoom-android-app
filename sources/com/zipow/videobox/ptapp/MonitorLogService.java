package com.zipow.videobox.ptapp;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.monitorlog.MonitorLogEvent;
import java.util.Map;
import java.util.Map.Entry;

public class MonitorLogService {
    private static final String TAG = "MonitorLogService";
    private long mNativeHandle = 0;

    private native void addAttributeBooleanImpl(long j, int i, boolean z);

    private native void addAttributeDoubleImpl(long j, int i, double d);

    private native void addAttributeIntImpl(long j, int i, int i2);

    private native void addAttributeLongImpl(long j, int i, long j2);

    private native void addAttributeStringImpl(long j, int i, String str);

    private native boolean addLogItemImpl(long j, long j2);

    private native void initEventBasicInfoImpl(long j, int i, int i2, int i3, int i4);

    private native void initEventBasicInfoStrImpl(long j, String str, String str2, String str3, String str4);

    private native long makeItemImpl(long j);

    public MonitorLogService(long j) {
        this.mNativeHandle = j;
    }

    private boolean addLogItem(@NonNull MonitorLogEvent monitorLogEvent) {
        long makeItemImpl = makeItemImpl(this.mNativeHandle);
        if (makeItemImpl == 0) {
            return false;
        }
        initEventBasicInfoImpl(makeItemImpl, monitorLogEvent.getmClientType(), monitorLogEvent.getmLocation(), monitorLogEvent.getmEvent(), monitorLogEvent.getmSubEvent());
        SparseBooleanArray sparseBooleanArray = monitorLogEvent.getmSparseBooleanArray();
        int size = sparseBooleanArray.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                addAttributeBooleanImpl(makeItemImpl, sparseBooleanArray.keyAt(i), sparseBooleanArray.valueAt(i));
            }
        }
        SparseArray sparseArray = monitorLogEvent.getmSparseDoubleArray();
        int size2 = sparseArray.size();
        if (size2 > 0) {
            for (int i2 = 0; i2 < size2; i2++) {
                addAttributeDoubleImpl(makeItemImpl, sparseArray.keyAt(i2), ((Double) sparseArray.valueAt(i2)).doubleValue());
            }
        }
        SparseIntArray sparseIntArray = monitorLogEvent.getmSparseIntArray();
        int size3 = sparseIntArray.size();
        if (size3 > 0) {
            for (int i3 = 0; i3 < size3; i3++) {
                addAttributeIntImpl(makeItemImpl, sparseIntArray.keyAt(i3), sparseIntArray.valueAt(i3));
            }
        }
        SparseArrayCompat sparseArrayCompat = monitorLogEvent.getmSparseLongArray();
        int size4 = sparseArrayCompat.size();
        if (size4 > 0) {
            for (int i4 = 0; i4 < size4; i4++) {
                addAttributeLongImpl(makeItemImpl, sparseArrayCompat.keyAt(i4), ((Long) sparseArrayCompat.valueAt(i4)).longValue());
            }
        }
        SparseArray sparseArray2 = monitorLogEvent.getmSparseStringArray();
        int size5 = sparseArray2.size();
        if (size5 > 0) {
            for (int i5 = 0; i5 < size5; i5++) {
                addAttributeStringImpl(makeItemImpl, sparseArray2.keyAt(i5), (String) sparseArray2.valueAt(i5));
            }
        }
        return addLogItemImpl(this.mNativeHandle, makeItemImpl);
    }

    private boolean addLogItem(int i, int i2, int i3, int i4, @Nullable Map<Integer, Object> map) {
        long makeItemImpl = makeItemImpl(this.mNativeHandle);
        if (makeItemImpl == 0) {
            return false;
        }
        initEventBasicInfoImpl(makeItemImpl, i, i2, i3, i4);
        if (map != null) {
            for (Entry entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Boolean) {
                    addAttributeBooleanImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Boolean) value).booleanValue());
                } else if (value instanceof Double) {
                    addAttributeDoubleImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Double) value).doubleValue());
                } else if (value instanceof Integer) {
                    addAttributeIntImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    addAttributeLongImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Long) value).longValue());
                } else if (value instanceof String) {
                    addAttributeStringImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), (String) value);
                }
            }
        }
        return addLogItemImpl(this.mNativeHandle, makeItemImpl);
    }

    private boolean addLogItem(String str, String str2, String str3, String str4, @Nullable Map<Integer, Object> map) {
        long makeItemImpl = makeItemImpl(this.mNativeHandle);
        if (makeItemImpl == 0) {
            return false;
        }
        initEventBasicInfoStrImpl(makeItemImpl, str, str2, str3, str4);
        if (map != null) {
            for (Entry entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Boolean) {
                    addAttributeBooleanImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Boolean) value).booleanValue());
                } else if (value instanceof Double) {
                    addAttributeDoubleImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Double) value).doubleValue());
                } else if (value instanceof Integer) {
                    addAttributeIntImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    addAttributeLongImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), ((Long) value).longValue());
                } else if (value instanceof String) {
                    addAttributeStringImpl(makeItemImpl, ((Integer) entry.getKey()).intValue(), (String) value);
                }
            }
        }
        return addLogItemImpl(this.mNativeHandle, makeItemImpl);
    }

    public static boolean eventTrack(String str, String str2, String str3, String str4, Map<Integer, Object> map) {
        MonitorLogService monitorLogService = getMonitorLogService();
        if (monitorLogService == null) {
            return false;
        }
        return monitorLogService.addLogItem(str, str2, str3, str4, map);
    }

    public static boolean eventTrack(int i, int i2, int i3, int i4, Map<Integer, Object> map) {
        MonitorLogService monitorLogService = getMonitorLogService();
        if (monitorLogService == null) {
            return false;
        }
        return monitorLogService.addLogItem(i, i2, i3, i4, map);
    }

    public static boolean eventTrack(@NonNull MonitorLogEvent monitorLogEvent) {
        MonitorLogService monitorLogService = getMonitorLogService();
        if (monitorLogService == null) {
            return false;
        }
        return monitorLogService.addLogItem(monitorLogEvent);
    }

    @Nullable
    private static final MonitorLogService getMonitorLogService() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance == null || !instance.isConfApp()) {
            return PTApp.getInstance().getMonitorLogService();
        }
        return ConfMgr.getInstance().getMonitorLogService();
    }
}
