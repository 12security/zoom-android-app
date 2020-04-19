package com.zipow.videobox.confapp.meeting.optimize;

import android.util.SparseArray;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;

public class ZMConfPListUserEventPolicy extends ZMBaseConfPolicy {
    private static final int MAX_BATCH_ALL_EVENTS = 4000;
    private static final int MAX_BATCH_ONE_EVENT = 1000;
    public static final int USER_EVENT_LEAVE_SILENT_MODE = -11;
    public static final int USER_EVENT_UPDATE_NOT_SORT = -10;
    @Nullable
    private CallBack mCallBack;
    @NonNull
    private SparseIntArray mLastUnProcessEventNumbers;
    @NonNull
    private SparseIntArray mPerformUserEventTimes;
    @NonNull
    private SparseArray<LinkedList<String>> mUserEvents;

    public interface CallBack {
        void onPerformExtraActionForUsers(int i, @Nullable Collection<String> collection);

        void onRefreshAll(boolean z);

        void onSmallBatchUsers(int i, @Nullable Collection<String> collection);
    }

    public ZMConfPListUserEventPolicy() {
        this.mLastUnProcessEventNumbers = new SparseIntArray();
        this.mUserEvents = new SparseArray<>();
        this.mPerformUserEventTimes = new SparseIntArray();
        this.mIntervalIdle = 200;
        this.mPerformUserEventTimes.put(0, 5);
        this.mPerformUserEventTimes.put(2, 5);
        this.mPerformUserEventTimes.put(1, 5);
        this.mPerformUserEventTimes.put(-10, 5);
        this.mPerformUserEventTimes.put(-11, 5);
    }

    public void setmCallBack(@Nullable CallBack callBack) {
        this.mCallBack = callBack;
    }

    public void end() {
        super.end();
        setmCallBack(null);
        this.mLastUnProcessEventNumbers.clear();
        this.mUserEvents.clear();
    }

    public void onIdle() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        LinkedList linkedList = (LinkedList) this.mUserEvents.get(0);
        LinkedList linkedList2 = (LinkedList) this.mUserEvents.get(2);
        LinkedList linkedList3 = (LinkedList) this.mUserEvents.get(1);
        LinkedList linkedList4 = (LinkedList) this.mUserEvents.get(-10);
        LinkedList linkedList5 = (LinkedList) this.mUserEvents.get(-11);
        if (linkedList == null) {
            i = 0;
        } else {
            i = linkedList.size();
        }
        if (linkedList2 == null) {
            i2 = 0;
        } else {
            i2 = linkedList2.size();
        }
        if (linkedList4 == null) {
            i3 = 0;
        } else {
            i3 = linkedList4.size();
        }
        if (linkedList3 == null) {
            i4 = 0;
        } else {
            i4 = linkedList3.size();
        }
        if (linkedList5 == null) {
            i5 = 0;
        } else {
            i5 = linkedList5.size();
        }
        int i11 = i + i2 + i4 + i3 + i5;
        if (i11 != 0) {
            if (i11 >= 4000) {
                onRefreshAll(false);
                if (linkedList != null) {
                    linkedList.clear();
                }
                if (linkedList2 != null) {
                    onPerformExtraActionForUsers(2, linkedList2);
                    linkedList2.clear();
                }
                if (linkedList4 != null) {
                    onPerformExtraActionForUsers(-10, linkedList4);
                    linkedList4.clear();
                }
                if (linkedList3 != null) {
                    onPerformExtraActionForUsers(1, linkedList3);
                    linkedList3.clear();
                }
                if (linkedList5 != null) {
                    onPerformExtraActionForUsers(-11, linkedList5);
                    linkedList5.clear();
                }
            } else {
                boolean z = i5 > 0 && processEvent(linkedList5, -11, i5, i3 > 0 && processEvent(linkedList4, -10, i3, i2 > 0 && processEvent(linkedList2, 2, i2, i > 0 && processEvent(linkedList, 0, i, false))));
                if (i4 > 0) {
                    processEvent(linkedList3, 1, i4, z);
                }
            }
            if (linkedList == null) {
                i6 = 0;
            } else {
                i6 = linkedList.size();
            }
            if (linkedList2 == null) {
                i7 = 0;
            } else {
                i7 = linkedList2.size();
            }
            if (linkedList3 == null) {
                i8 = 0;
            } else {
                i8 = linkedList3.size();
            }
            if (linkedList4 == null) {
                i9 = 0;
            } else {
                i9 = linkedList4.size();
            }
            if (linkedList5 == null) {
                i10 = 0;
            } else {
                i10 = linkedList5.size();
            }
            this.mLastUnProcessEventNumbers.put(0, i6);
            this.mLastUnProcessEventNumbers.put(2, i7);
            this.mLastUnProcessEventNumbers.put(1, i8);
            this.mLastUnProcessEventNumbers.put(-10, i9);
            this.mLastUnProcessEventNumbers.put(-11, i10);
        }
    }

    public void onReceiveUserEvent(int i, long j) {
        LinkedList linkedList = (LinkedList) this.mUserEvents.get(i);
        if (linkedList == null) {
            linkedList = new LinkedList();
            this.mUserEvents.put(i, linkedList);
        }
        linkedList.add(String.valueOf(j));
    }

    private void onRefreshAll(boolean z) {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onRefreshAll(z);
        }
    }

    private void onSmallBatchUsers(int i, @Nullable LinkedList<String> linkedList) {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onSmallBatchUsers(i, linkedList);
        }
    }

    private void onPerformExtraActionForUsers(int i, @Nullable LinkedList<String> linkedList) {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onPerformExtraActionForUsers(i, linkedList);
        }
    }

    private boolean processEvent(@Nullable LinkedList<String> linkedList, int i, int i2, boolean z) {
        if (linkedList != null) {
            int i3 = this.mPerformUserEventTimes.get(i);
            boolean z2 = ((long) (i2 - this.mLastUnProcessEventNumbers.get(i, 0))) < this.mIntervalIdle / ((long) (i3 * 2));
            if ((z2 && ((long) i2) > this.mIntervalIdle / ((long) i3)) || i2 >= 1000) {
                if (!z) {
                    onRefreshAll(false);
                    z = true;
                }
                onPerformExtraActionForUsers(i, linkedList);
                linkedList.clear();
            } else if (z2) {
                onSmallBatchUsers(i, linkedList);
                linkedList.clear();
            }
        }
        return z;
    }
}
