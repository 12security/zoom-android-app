package com.zipow.videobox.confapp.meeting.optimize;

import android.util.SparseArray;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZMConfUserEventPolicy extends ZMBaseConfPolicy {
    private static final int MAX_BATCH_ONE_EVENT = 1000;
    @Nullable
    private CallBack mCallBack;
    @NonNull
    private SparseIntArray mPerformUserCmdTimes;
    @NonNull
    private SparseIntArray mPerformUserEventTimes;
    @NonNull
    private SparseIntArray mUnProcessUserCmdNumbers;
    @NonNull
    private SparseIntArray mUnProcessUserEventNumbers;
    @NonNull
    private SparseArray<List<Long>> mUserCmdList;
    @NonNull
    private SparseArray<Set<Long>> mUserCmdSet;
    @NonNull
    private SparseArray<Set<ZMConfUserActionInfo>> mUserCmdUserActionInfoSet;
    @NonNull
    private SparseArray<List<ZMConfUserActionInfo>> mUserEvents;

    public interface CallBack {
        void onUserCmdForBatchUsers(boolean z, int i, @NonNull Collection<Long> collection);

        void onUserCmdUserActionForBatchUsers(boolean z, int i, @NonNull Set<ZMConfUserActionInfo> set);

        void onUserEventForBatchUsers(boolean z, int i, @NonNull List<ZMConfUserActionInfo> list);
    }

    public ZMConfUserEventPolicy() {
        this.mUnProcessUserEventNumbers = new SparseIntArray();
        this.mUserEvents = new SparseArray<>();
        this.mPerformUserEventTimes = new SparseIntArray();
        this.mUserCmdList = new SparseArray<>();
        this.mUserCmdSet = new SparseArray<>();
        this.mUserCmdUserActionInfoSet = new SparseArray<>();
        this.mUnProcessUserCmdNumbers = new SparseIntArray();
        this.mPerformUserCmdTimes = new SparseIntArray();
        this.mIntervalIdle = 200;
        this.mPerformUserEventTimes.put(0, 10);
        this.mPerformUserEventTimes.put(2, 5);
        this.mPerformUserEventTimes.put(1, 10);
        this.mPerformUserCmdTimes.put(4, 5);
        this.mPerformUserCmdTimes.put(9, 5);
        this.mPerformUserCmdTimes.put(12, 5);
        this.mPerformUserCmdTimes.put(21, 5);
    }

    public void start(@NonNull CallBack callBack) {
        this.mCallBack = callBack;
        super.start();
    }

    public void end() {
        super.end();
        this.mCallBack = null;
        this.mUnProcessUserEventNumbers.clear();
        this.mUserEvents.clear();
        this.mUnProcessUserCmdNumbers.clear();
        this.mUserCmdList.clear();
        this.mUserCmdSet.clear();
        this.mUserCmdUserActionInfoSet.clear();
    }

    public void onIdle() {
        processUserEvent(0);
        processUserEvent(1);
        processUserEvent(2);
        processUserCmdSet(4);
        processUserCmdUserActionSet(9);
        processUserCmdSet(12);
        processUserCmdSet(21);
    }

    public void onReceiveUserEvent(int i, ZMConfUserActionInfo zMConfUserActionInfo) {
        List list = (List) this.mUserEvents.get(i);
        if (list == null) {
            list = new ArrayList();
            this.mUserEvents.put(i, list);
        }
        list.add(zMConfUserActionInfo);
    }

    private void processUserEvent(int i) {
        int i2;
        List list = (List) this.mUserEvents.get(i);
        if (list == null) {
            i2 = 0;
        } else {
            i2 = list.size();
        }
        if (list != null) {
            int i3 = this.mPerformUserEventTimes.get(i);
            boolean z = ((long) (i2 - this.mUnProcessUserEventNumbers.get(i, 0))) < this.mIntervalIdle / ((long) (i3 * 2));
            if ((z && ((long) i2) > this.mIntervalIdle / ((long) i3)) || i2 >= 1000) {
                onUserEventForBatchUsers(true, i, list);
                list.clear();
            } else if (z) {
                onUserEventForBatchUsers(false, i, list);
                list.clear();
            }
            this.mUnProcessUserEventNumbers.put(i, list.size());
        }
    }

    public void onReceiveUserCmdForSet(int i, long j, int i2) {
        Set set = (Set) this.mUserCmdUserActionInfoSet.get(i);
        if (set == null) {
            set = new HashSet();
            this.mUserCmdUserActionInfoSet.put(i, set);
        }
        set.add(new ZMConfUserActionInfo(j, i2));
    }

    private void processUserCmdUserActionSet(int i) {
        int i2;
        Set set = (Set) this.mUserCmdUserActionInfoSet.get(i);
        if (set == null) {
            i2 = 0;
        } else {
            i2 = set.size();
        }
        if (set != null) {
            int i3 = this.mPerformUserCmdTimes.get(i);
            boolean z = ((long) (i2 - this.mUnProcessUserCmdNumbers.get(i, 0))) < this.mIntervalIdle / ((long) (i3 * 2));
            if ((z && ((long) i2) > this.mIntervalIdle / ((long) i3)) || i2 >= 1000) {
                onUserCmdUserActionForBatchUsers(true, i, set);
                set.clear();
            } else if (z) {
                onUserCmdUserActionForBatchUsers(false, i, set);
                set.clear();
            }
            this.mUnProcessUserCmdNumbers.put(i, set.size());
        }
    }

    public void onReceiveUserCmdForSet(int i, long j) {
        Set set = (Set) this.mUserCmdSet.get(i);
        if (set == null) {
            set = new HashSet();
            this.mUserCmdSet.put(i, set);
        }
        set.add(Long.valueOf(j));
    }

    private void processUserCmdSet(int i) {
        int i2;
        Set set = (Set) this.mUserCmdSet.get(i);
        if (set == null) {
            i2 = 0;
        } else {
            i2 = set.size();
        }
        if (set != null) {
            int i3 = this.mPerformUserCmdTimes.get(i);
            boolean z = ((long) (i2 - this.mUnProcessUserCmdNumbers.get(i, 0))) < this.mIntervalIdle / ((long) (i3 * 2));
            if ((z && ((long) i2) > this.mIntervalIdle / ((long) i3)) || i2 >= 1000) {
                onUserCmdForBatchUsers(true, i, set);
                set.clear();
            } else if (z) {
                onUserCmdForBatchUsers(false, i, set);
                set.clear();
            }
            this.mUnProcessUserCmdNumbers.put(i, set.size());
        }
    }

    public void onReceiveUserCmdForList(int i, long j) {
        List list = (List) this.mUserCmdList.get(i);
        if (list == null) {
            list = new ArrayList();
            this.mUserCmdList.put(i, list);
        }
        list.add(Long.valueOf(j));
    }

    private void processUserCmdList(int i) {
        int i2;
        List list = (List) this.mUserCmdList.get(i);
        if (list == null) {
            i2 = 0;
        } else {
            i2 = list.size();
        }
        if (list != null) {
            int i3 = this.mPerformUserCmdTimes.get(i);
            boolean z = ((long) (i2 - this.mUnProcessUserCmdNumbers.get(i, 0))) < this.mIntervalIdle / ((long) (i3 * 2));
            if ((z && ((long) i2) > this.mIntervalIdle / ((long) i3)) || i2 >= 1000) {
                onUserCmdForBatchUsers(true, i, list);
                list.clear();
            } else if (z) {
                onUserCmdForBatchUsers(false, i, list);
                list.clear();
            }
            this.mUnProcessUserCmdNumbers.put(i, list.size());
        }
    }

    private void onUserEventForBatchUsers(boolean z, int i, @NonNull List<ZMConfUserActionInfo> list) {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onUserEventForBatchUsers(z, i, list);
        }
    }

    private void onUserCmdForBatchUsers(boolean z, int i, @NonNull Collection<Long> collection) {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onUserCmdForBatchUsers(z, i, collection);
        }
    }

    private void onUserCmdUserActionForBatchUsers(boolean z, int i, @NonNull Set<ZMConfUserActionInfo> set) {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onUserCmdUserActionForBatchUsers(z, i, set);
        }
    }
}
