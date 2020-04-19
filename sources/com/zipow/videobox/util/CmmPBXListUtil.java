package com.zipow.videobox.util;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.ICmmPBXHistoryItemBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class CmmPBXListUtil {
    private static final String TAG = "CmmPBXListUtil";

    public static <T extends ICmmPBXHistoryItemBean> List<T> addLatestList(@Nullable List<T> list, @Nullable List<T> list2) {
        if (CollectionsUtil.isListEmpty(list) && CollectionsUtil.isListEmpty(list2)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (!CollectionsUtil.isListEmpty(list)) {
            arrayList.addAll(list);
            if (CollectionsUtil.isListEmpty(list2)) {
                return arrayList;
            }
            boolean z = false;
            int i = -1;
            while (list2.size() > 0 && !z) {
                ICmmPBXHistoryItemBean iCmmPBXHistoryItemBean = (ICmmPBXHistoryItemBean) list2.get(0);
                if (iCmmPBXHistoryItemBean == null) {
                    list2.remove(0);
                } else {
                    int i2 = i + 1;
                    while (true) {
                        if (i2 >= arrayList.size()) {
                            break;
                        }
                        ICmmPBXHistoryItemBean iCmmPBXHistoryItemBean2 = (ICmmPBXHistoryItemBean) arrayList.get(i2);
                        if (iCmmPBXHistoryItemBean2 != null) {
                            if (iCmmPBXHistoryItemBean.getCreateTime() <= iCmmPBXHistoryItemBean2.getCreateTime()) {
                                if (iCmmPBXHistoryItemBean.getCreateTime() == iCmmPBXHistoryItemBean2.getCreateTime() && TextUtils.equals(iCmmPBXHistoryItemBean.getId(), iCmmPBXHistoryItemBean2.getId())) {
                                    list2.remove(0);
                                    break;
                                }
                                z = i2 >= arrayList.size() - 1;
                            } else {
                                arrayList.add(i2, iCmmPBXHistoryItemBean);
                                list2.remove(0);
                                break;
                            }
                        } else {
                            arrayList.remove(i2);
                            i2--;
                        }
                        i2++;
                    }
                    i = i2;
                }
            }
            if (!CollectionsUtil.isListEmpty(list2)) {
                arrayList.addAll(list2);
            }
            return arrayList;
        }
        if (!CollectionsUtil.isListEmpty(list2)) {
            arrayList.addAll(list2);
        }
        return arrayList;
    }

    @NonNull
    public static <T extends ICmmPBXHistoryItemBean> List<T> sortListByCreateTime(@NonNull List<T> list) {
        if (CollectionsUtil.isListEmpty(list)) {
            return list;
        }
        Collections.sort(list, new Comparator<T>() {
            public int compare(@Nullable T t, @Nullable T t2) {
                if (t == null || t2 == null) {
                    return 0;
                }
                return t.getCreateTime() > t2.getCreateTime() ? -1 : 1;
            }
        });
        return list;
    }
}
