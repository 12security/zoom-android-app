package com.zipow.videobox.view.sip;

import java.util.List;

public interface IPBXHistoryAdapter<T> {
    boolean removeCall(String str);

    void setDeleteMode(boolean z);

    void updateData(List<T> list);
}
