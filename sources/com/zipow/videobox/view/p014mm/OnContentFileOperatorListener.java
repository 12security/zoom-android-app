package com.zipow.videobox.view.p014mm;

import java.util.List;

/* renamed from: com.zipow.videobox.view.mm.OnContentFileOperatorListener */
public interface OnContentFileOperatorListener {
    void onZoomFileCancelTransfer(String str);

    void onZoomFileClick(String str);

    void onZoomFileClick(String str, List<String> list);

    void onZoomFileIntegrationClick(String str);

    void onZoomFileShared(String str);

    void onZoomFileSharerAction(String str, MMZoomShareAction mMZoomShareAction, boolean z, boolean z2);
}
