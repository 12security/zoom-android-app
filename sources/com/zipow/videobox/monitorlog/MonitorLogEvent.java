package com.zipow.videobox.monitorlog;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import com.zipow.videobox.ptapp.MonitorLogService;

public class MonitorLogEvent {
    private static final int NONE_ZOOMLOGSUBEVENT = -1;
    private final int mClientType;
    private final int mEvent;
    private final int mLocation;
    private final SparseBooleanArray mSparseBooleanArray;
    private final SparseArray<Double> mSparseDoubleArray;
    private final SparseIntArray mSparseIntArray;
    private final SparseArrayCompat<Long> mSparseLongArray;
    private final SparseArray<String> mSparseStringArray;
    private final int mSubEvent;

    public static class MonitorLogEventBuilder {
        private int mClientType;
        private int mEvent;
        private int mLocation;
        @NonNull
        private SparseBooleanArray mSparseBooleanArray = new SparseBooleanArray();
        @NonNull
        private SparseArray<Double> mSparseDoubleArray = new SparseArray<>();
        @NonNull
        private SparseIntArray mSparseIntArray = new SparseIntArray();
        @NonNull
        private SparseArrayCompat<Long> mSparseLongArray = new SparseArrayCompat<>();
        @NonNull
        private SparseArray<String> mSparseStringArray = new SparseArray<>();
        private int mSubEvent;

        @NonNull
        public MonitorLogEventBuilder newBasicInfo(int i, int i2, int i3, int i4) {
            this.mClientType = i;
            this.mLocation = i2;
            this.mEvent = i3;
            this.mSubEvent = i4;
            return this;
        }

        @NonNull
        public MonitorLogEventBuilder newBasicInfo(int i, int i2, int i3) {
            this.mClientType = i;
            this.mLocation = i2;
            this.mEvent = i3;
            this.mSubEvent = -1;
            return this;
        }

        @NonNull
        public MonitorLogEventBuilder putBooleanAttribute(int i, boolean z) {
            this.mSparseBooleanArray.put(i, z);
            return this;
        }

        @NonNull
        public MonitorLogEventBuilder putIntAttribute(int i, int i2) {
            this.mSparseIntArray.put(i, i2);
            return this;
        }

        @NonNull
        public MonitorLogEventBuilder putLongAttribute(int i, long j) {
            this.mSparseLongArray.put(i, Long.valueOf(j));
            return this;
        }

        @NonNull
        public MonitorLogEventBuilder putStringAttribute(int i, String str) {
            this.mSparseStringArray.put(i, str);
            return this;
        }

        @NonNull
        public MonitorLogEventBuilder putDoubleAttribute(int i, double d) {
            this.mSparseDoubleArray.put(i, Double.valueOf(d));
            return this;
        }

        @NonNull
        public MonitorLogEvent create() {
            MonitorLogEvent monitorLogEvent = new MonitorLogEvent(this.mClientType, this.mLocation, this.mEvent, this.mSubEvent, this.mSparseBooleanArray, this.mSparseIntArray, this.mSparseLongArray, this.mSparseStringArray, this.mSparseDoubleArray);
            return monitorLogEvent;
        }
    }

    private MonitorLogEvent(int i, int i2, int i3, int i4, SparseBooleanArray sparseBooleanArray, SparseIntArray sparseIntArray, SparseArrayCompat<Long> sparseArrayCompat, SparseArray<String> sparseArray, SparseArray<Double> sparseArray2) {
        this.mClientType = i;
        this.mLocation = i2;
        this.mEvent = i3;
        this.mSubEvent = i4;
        this.mSparseBooleanArray = sparseBooleanArray;
        this.mSparseIntArray = sparseIntArray;
        this.mSparseLongArray = sparseArrayCompat;
        this.mSparseStringArray = sparseArray;
        this.mSparseDoubleArray = sparseArray2;
    }

    public boolean write() {
        return MonitorLogService.eventTrack(this);
    }

    public int getmClientType() {
        return this.mClientType;
    }

    public int getmLocation() {
        return this.mLocation;
    }

    public int getmEvent() {
        return this.mEvent;
    }

    public int getmSubEvent() {
        return this.mSubEvent;
    }

    public SparseBooleanArray getmSparseBooleanArray() {
        return this.mSparseBooleanArray;
    }

    public SparseIntArray getmSparseIntArray() {
        return this.mSparseIntArray;
    }

    public SparseArrayCompat<Long> getmSparseLongArray() {
        return this.mSparseLongArray;
    }

    public SparseArray<String> getmSparseStringArray() {
        return this.mSparseStringArray;
    }

    public SparseArray<Double> getmSparseDoubleArray() {
        return this.mSparseDoubleArray;
    }
}
