package com.revolverobotics.kubisdk;

import java.util.ArrayList;

public interface IKubiManagerDelegate {
    void kubiDeviceFound(KubiManager kubiManager, KubiSearchResult kubiSearchResult);

    void kubiManagerFailed(KubiManager kubiManager, int i);

    void kubiManagerStatusChanged(KubiManager kubiManager, int i, int i2);

    void kubiScanComplete(KubiManager kubiManager, ArrayList<KubiSearchResult> arrayList);
}
