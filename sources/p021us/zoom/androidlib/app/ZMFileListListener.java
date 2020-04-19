package p021us.zoom.androidlib.app;

/* renamed from: us.zoom.androidlib.app.ZMFileListListener */
public interface ZMFileListListener {
    void onOpenDirFailed(String str);

    void onOpenFileFailed(String str);

    void onReLogin();

    void onRefresh();

    void onSelectedFile(String str, String str2);

    void onSharedFileLink(String str, String str2, String str3, long j, int i);

    void onStarted(boolean z, String str);

    void onStarting();

    void onUpdateWaitingMessage(String str);

    void onWaitingEnd();

    void onWaitingStart(String str);
}
