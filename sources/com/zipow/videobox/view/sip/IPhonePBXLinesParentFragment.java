package com.zipow.videobox.view.sip;

public interface IPhonePBXLinesParentFragment {
    boolean isHasShow();

    void onAcceptCallResult(String str);

    void onPickSipResult(String str, String str2);

    void onPickupCallResult(String str);

    void onSelectLastAccessibilityId(String str);
}
