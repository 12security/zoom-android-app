package com.zipow.videobox.util;

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ChatImgSaveHelper {
    private static final String TAG = "ChatImgSaveHelper";
    @Nullable
    private static ChatImgSaveHelper mInstance;
    @NonNull
    private List<String> mDownloadLists = new ArrayList();
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler(Looper.getMainLooper());
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onConfirmFileDownloaded(String str, String str2, int i) {
            ChatImgSaveHelper.this.saveImage(str, str2, i);
        }
    };

    private ChatImgSaveHelper() {
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
    }

    @NonNull
    public static ChatImgSaveHelper getInstance() {
        if (mInstance == null) {
            synchronized (ChatImgSaveHelper.class) {
                if (mInstance == null) {
                    mInstance = new ChatImgSaveHelper();
                }
            }
        }
        return mInstance;
    }

    public void downloadAndSaveImage(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("$");
        sb.append(str2);
        String sb2 = sb.toString();
        if (!this.mDownloadLists.contains(sb2)) {
            this.mDownloadLists.add(sb2);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    sessionById.downloadFileForMessage(str2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void saveImage(String str, String str2, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("$");
        sb.append(str2);
        String sb2 = sb.toString();
        if (this.mDownloadLists.contains(sb2)) {
            this.mDownloadLists.remove(sb2);
            if (i == 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                    if (sessionById != null) {
                        ZoomMessage messageById = sessionById.getMessageById(str2);
                        if (messageById != null) {
                            String localFilePath = messageById.getLocalFilePath();
                            if (!StringUtil.isEmptyOrNull(localFilePath)) {
                                saveImage(new File(localFilePath));
                            }
                        }
                    }
                }
            }
        }
    }

    private void saveImage(@Nullable final File file) {
        if (file != null && file.exists()) {
            if (VERSION.SDK_INT <= 23 || VideoBoxApplication.getInstance().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                new Thread("SaveImage") {
                    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00c4, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00c5, code lost:
                        r2 = r0;
                        r3 = null;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00c8, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00c9, code lost:
                        r2 = r0;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
                        throw r2;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00cb, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00cc, code lost:
                        r3 = r2;
                        r2 = r0;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00de, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00df, code lost:
                        r2 = r0;
                        r3 = null;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:81:0x00e2, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:82:0x00e3, code lost:
                        r2 = r0;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
                        throw r2;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:85:0x00e5, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:86:0x00e6, code lost:
                        r3 = r2;
                        r2 = r0;
                     */
                    /* JADX WARNING: Failed to process nested try/catch */
                    /* JADX WARNING: Removed duplicated region for block: B:62:0x00c4 A[ExcHandler: all (r0v18 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:27:0x0073] */
                    /* JADX WARNING: Removed duplicated region for block: B:79:0x00de A[ExcHandler: all (r0v14 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:25:0x006e] */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                            r17 = this;
                            r1 = r17
                            boolean r0 = p021us.zoom.androidlib.util.OsUtil.isAtLeastQ()
                            r2 = 1
                            if (r0 == 0) goto L_0x0024
                            android.content.Context r0 = com.zipow.videobox.VideoBoxApplication.getGlobalContext()
                            if (r0 != 0) goto L_0x0010
                            return
                        L_0x0010:
                            java.io.File r3 = r3
                            android.net.Uri r3 = p021us.zoom.androidlib.util.FileUtils.insertFileIntoMediaStore(r0, r3)
                            if (r3 == 0) goto L_0x0110
                            java.io.File r4 = r3
                            boolean r0 = p021us.zoom.androidlib.util.FileUtils.copyFileToUri(r0, r4, r3)
                            if (r0 == 0) goto L_0x0110
                            r1._onSaveImageDone(r2)
                            return
                        L_0x0024:
                            java.io.File r0 = com.zipow.videobox.util.ImageUtil.getZoomGalleryPath()
                            if (r0 != 0) goto L_0x002b
                            return
                        L_0x002b:
                            java.lang.StringBuilder r3 = new java.lang.StringBuilder
                            r3.<init>()
                            java.lang.String r0 = r0.getPath()
                            r3.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r3.append(r0)
                            java.io.File r0 = r3
                            java.lang.String r0 = r0.getName()
                            r3.append(r0)
                            java.lang.String r0 = r3.toString()
                            java.io.File r3 = new java.io.File
                            r3.<init>(r0)
                            boolean r4 = r3.exists()
                            r5 = 0
                            if (r4 == 0) goto L_0x0062
                            long r7 = r3.length()
                            int r4 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
                            if (r4 <= 0) goto L_0x0062
                            r1._onSaveImageDone(r2)
                            return
                        L_0x0062:
                            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0110 }
                            java.io.File r7 = r3     // Catch:{ Exception -> 0x0110 }
                            r4.<init>(r7)     // Catch:{ Exception -> 0x0110 }
                            r7 = 0
                            java.nio.channels.FileChannel r14 = r4.getChannel()     // Catch:{ Throwable -> 0x00fd }
                            java.io.FileOutputStream r15 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00e2, all -> 0x00de }
                            r15.<init>(r3)     // Catch:{ Throwable -> 0x00e2, all -> 0x00de }
                            java.nio.channels.FileChannel r16 = r15.getChannel()     // Catch:{ Throwable -> 0x00c8, all -> 0x00c4 }
                            r10 = 0
                            long r12 = r14.size()     // Catch:{ Throwable -> 0x00ac, all -> 0x00a8 }
                            r8 = r16
                            r9 = r14
                            long r8 = r8.transferFrom(r9, r10, r12)     // Catch:{ Throwable -> 0x00ac, all -> 0x00a8 }
                            int r5 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1))
                            if (r5 <= 0) goto L_0x0096
                            com.zipow.videobox.VideoBoxApplication r5 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Throwable -> 0x00ac, all -> 0x00a8 }
                            java.lang.String r0 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r0)     // Catch:{ Throwable -> 0x00ac, all -> 0x00a8 }
                            p021us.zoom.androidlib.util.AndroidAppUtil.addImageToGallery(r5, r3, r0)     // Catch:{ Throwable -> 0x00ac, all -> 0x00a8 }
                            r1._onSaveImageDone(r2)     // Catch:{ Throwable -> 0x00ac, all -> 0x00a8 }
                        L_0x0096:
                            if (r16 == 0) goto L_0x009b
                            r16.close()     // Catch:{ Throwable -> 0x00c8, all -> 0x00c4 }
                        L_0x009b:
                            r15.close()     // Catch:{ Throwable -> 0x00e2, all -> 0x00de }
                            if (r14 == 0) goto L_0x00a3
                            r14.close()     // Catch:{ Throwable -> 0x00fd }
                        L_0x00a3:
                            r4.close()     // Catch:{ Exception -> 0x0110 }
                            goto L_0x0110
                        L_0x00a8:
                            r0 = move-exception
                            r2 = r0
                            r3 = r7
                            goto L_0x00b2
                        L_0x00ac:
                            r0 = move-exception
                            r2 = r0
                            throw r2     // Catch:{ all -> 0x00af }
                        L_0x00af:
                            r0 = move-exception
                            r3 = r2
                            r2 = r0
                        L_0x00b2:
                            if (r16 == 0) goto L_0x00c3
                            if (r3 == 0) goto L_0x00c0
                            r16.close()     // Catch:{ Throwable -> 0x00ba, all -> 0x00c4 }
                            goto L_0x00c3
                        L_0x00ba:
                            r0 = move-exception
                            r5 = r0
                            r3.addSuppressed(r5)     // Catch:{ Throwable -> 0x00c8, all -> 0x00c4 }
                            goto L_0x00c3
                        L_0x00c0:
                            r16.close()     // Catch:{ Throwable -> 0x00c8, all -> 0x00c4 }
                        L_0x00c3:
                            throw r2     // Catch:{ Throwable -> 0x00c8, all -> 0x00c4 }
                        L_0x00c4:
                            r0 = move-exception
                            r2 = r0
                            r3 = r7
                            goto L_0x00ce
                        L_0x00c8:
                            r0 = move-exception
                            r2 = r0
                            throw r2     // Catch:{ all -> 0x00cb }
                        L_0x00cb:
                            r0 = move-exception
                            r3 = r2
                            r2 = r0
                        L_0x00ce:
                            if (r3 == 0) goto L_0x00da
                            r15.close()     // Catch:{ Throwable -> 0x00d4, all -> 0x00de }
                            goto L_0x00dd
                        L_0x00d4:
                            r0 = move-exception
                            r5 = r0
                            r3.addSuppressed(r5)     // Catch:{ Throwable -> 0x00e2, all -> 0x00de }
                            goto L_0x00dd
                        L_0x00da:
                            r15.close()     // Catch:{ Throwable -> 0x00e2, all -> 0x00de }
                        L_0x00dd:
                            throw r2     // Catch:{ Throwable -> 0x00e2, all -> 0x00de }
                        L_0x00de:
                            r0 = move-exception
                            r2 = r0
                            r3 = r7
                            goto L_0x00e8
                        L_0x00e2:
                            r0 = move-exception
                            r2 = r0
                            throw r2     // Catch:{ all -> 0x00e5 }
                        L_0x00e5:
                            r0 = move-exception
                            r3 = r2
                            r2 = r0
                        L_0x00e8:
                            if (r14 == 0) goto L_0x00f9
                            if (r3 == 0) goto L_0x00f6
                            r14.close()     // Catch:{ Throwable -> 0x00f0 }
                            goto L_0x00f9
                        L_0x00f0:
                            r0 = move-exception
                            r5 = r0
                            r3.addSuppressed(r5)     // Catch:{ Throwable -> 0x00fd }
                            goto L_0x00f9
                        L_0x00f6:
                            r14.close()     // Catch:{ Throwable -> 0x00fd }
                        L_0x00f9:
                            throw r2     // Catch:{ Throwable -> 0x00fd }
                        L_0x00fa:
                            r0 = move-exception
                            r2 = r0
                            goto L_0x0100
                        L_0x00fd:
                            r0 = move-exception
                            r7 = r0
                            throw r7     // Catch:{ all -> 0x00fa }
                        L_0x0100:
                            if (r7 == 0) goto L_0x010c
                            r4.close()     // Catch:{ Throwable -> 0x0106 }
                            goto L_0x010f
                        L_0x0106:
                            r0 = move-exception
                            r3 = r0
                            r7.addSuppressed(r3)     // Catch:{ Exception -> 0x0110 }
                            goto L_0x010f
                        L_0x010c:
                            r4.close()     // Catch:{ Exception -> 0x0110 }
                        L_0x010f:
                            throw r2     // Catch:{ Exception -> 0x0110 }
                        L_0x0110:
                            r0 = 0
                            r1._onSaveImageDone(r0)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ChatImgSaveHelper.C33572.run():void");
                    }

                    private void _onSaveImageDone(final boolean z) {
                        ChatImgSaveHelper.this.mHandler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(VideoBoxApplication.getInstance(), z ? C4558R.string.zm_mm_msg_saved_to_album : C4558R.string.zm_mm_msg_saved_to_album_failed_102727, 0).show();
                            }
                        });
                    }
                }.start();
            }
        }
    }
}
