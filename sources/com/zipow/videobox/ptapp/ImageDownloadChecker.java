package com.zipow.videobox.ptapp;

import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class ImageDownloadChecker {
    private static final int IMAGE_DOWNLOAD_TIMEOUT = 60000;
    private static ImageDownloadChecker instance;
    /* access modifiers changed from: private */
    @NonNull
    public List<ImageDownloadInfo> mDownloadedImages = new ArrayList();
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    @NonNull
    private IZoomMessengerUIListener mListener = new SimpleZoomMessengerUIListener() {
        public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
            ImageDownloadChecker.this.FT_OnProgress(str, str2, i, j, j2);
        }

        public void onConfirmFileDownloaded(String str, String str2, int i) {
            ImageDownloadChecker.this.onConfirmFileDownloaded(str, str2, i);
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public List<ImageDownloadTimeoutListener> mListeners = new ArrayList();
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mTimeoutCheckTask = new Runnable() {
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            int i = 0;
            while (i < ImageDownloadChecker.this.mDownloadedImages.size()) {
                ImageDownloadInfo imageDownloadInfo = (ImageDownloadInfo) ImageDownloadChecker.this.mDownloadedImages.get(i);
                if (imageDownloadInfo == null) {
                    ImageDownloadChecker.this.mDownloadedImages.remove(i);
                    i--;
                } else if (currentTimeMillis - imageDownloadInfo.timeStamp > 60000) {
                    ImageDownloadChecker.this.mDownloadedImages.remove(i);
                    i--;
                    for (ImageDownloadTimeoutListener imageDownloadTimeoutListener : ImageDownloadChecker.this.mListeners) {
                        if (imageDownloadTimeoutListener != null) {
                            imageDownloadTimeoutListener.onImageDownloadTimeout(imageDownloadInfo.sessionId, imageDownloadInfo.msgId);
                        }
                    }
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        zoomMessenger.FT_Cancel(imageDownloadInfo.sessionId, imageDownloadInfo.msgId, 1);
                    }
                }
                i++;
            }
            if (ImageDownloadChecker.this.mDownloadedImages.size() > 0) {
                ImageDownloadChecker.this.mHandler.postDelayed(ImageDownloadChecker.this.mTimeoutCheckTask, 1000);
            }
        }
    };

    static class ImageDownloadInfo {
        long completeSize;
        String msgId;
        String sessionId;
        long timeStamp;

        ImageDownloadInfo() {
        }
    }

    public interface ImageDownloadTimeoutListener {
        void onImageDownloadTimeout(String str, String str2);
    }

    private ImageDownloadChecker() {
    }

    public void addTimeoutListener(@Nullable ImageDownloadTimeoutListener imageDownloadTimeoutListener) {
        if (imageDownloadTimeoutListener != null) {
            if (!this.mListeners.contains(imageDownloadTimeoutListener)) {
                this.mListeners.add(imageDownloadTimeoutListener);
            }
            this.mHandler.removeCallbacks(this.mTimeoutCheckTask);
            this.mHandler.postDelayed(this.mTimeoutCheckTask, 1000);
        }
    }

    public void removeTimeoutListener(@Nullable ImageDownloadTimeoutListener imageDownloadTimeoutListener) {
        if (imageDownloadTimeoutListener != null) {
            this.mListeners.remove(imageDownloadTimeoutListener);
        }
    }

    public void startChecker() {
        ZoomMessengerUI.getInstance().addListener(this.mListener);
    }

    public void stopChecker() {
        ZoomMessengerUI.getInstance().removeListener(this.mListener);
    }

    public static ImageDownloadChecker getInstance() {
        if (instance == null) {
            instance = new ImageDownloadChecker();
        }
        return instance;
    }

    public void listenerImageDownload(String str, String str2) {
        ImageDownloadInfo imageDownloadInfo = new ImageDownloadInfo();
        imageDownloadInfo.sessionId = str;
        imageDownloadInfo.msgId = str2;
        imageDownloadInfo.timeStamp = System.currentTimeMillis();
        this.mDownloadedImages.add(imageDownloadInfo);
        this.mHandler.removeCallbacks(this.mTimeoutCheckTask);
        this.mHandler.postDelayed(this.mTimeoutCheckTask, 1000);
    }

    /* access modifiers changed from: private */
    public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
        int findImageDownloadinfo = findImageDownloadinfo(str, str2);
        if (findImageDownloadinfo != -1) {
            ImageDownloadInfo imageDownloadInfo = (ImageDownloadInfo) this.mDownloadedImages.get(findImageDownloadinfo);
            if (j != imageDownloadInfo.completeSize) {
                imageDownloadInfo.completeSize = j;
                imageDownloadInfo.timeStamp = System.currentTimeMillis();
            }
        }
    }

    private int findImageDownloadinfo(String str, String str2) {
        for (int i = 0; i < this.mDownloadedImages.size(); i++) {
            ImageDownloadInfo imageDownloadInfo = (ImageDownloadInfo) this.mDownloadedImages.get(i);
            if (StringUtil.isSameString(imageDownloadInfo.sessionId, str) && StringUtil.isSameString(imageDownloadInfo.msgId, str2)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public void onConfirmFileDownloaded(String str, String str2, int i) {
        List<ImageDownloadInfo> list = this.mDownloadedImages;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(PreferencesConstants.COOKIE_DELIMITER);
        sb.append(str2);
        list.remove(sb.toString());
        if (this.mDownloadedImages.size() == 0) {
            this.mHandler.removeCallbacks(this.mTimeoutCheckTask);
        }
    }
}
