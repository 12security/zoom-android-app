package com.zipow.videobox.view.sip;

import android.view.View;
import androidx.annotation.NonNull;

public interface IPhonePBXParentFragment {
    void displayCoverView(@NonNull PBXCallHistory pBXCallHistory, View view, boolean z);

    void enterSelectMode();

    boolean getUserVisibleHint();

    boolean isHasShow();

    boolean isInSelectMode();

    void onBlockNumber(PBXCallHistory pBXCallHistory);

    void onListViewDatasetChanged(boolean z);

    void onPickSipResult(String str, String str2);

    void onSelectLastAccessibilityId(String str);

    void updateEmptyView();
}
