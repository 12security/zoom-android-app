package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.CrawlLinkResponse;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class CrawlerLinkPreviewUI {
    private static final String TAG = "CrawlerLinkPreviewUI";
    @Nullable
    private static CrawlerLinkPreviewUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface ICrawlerLinkPreviewUIListener extends IListener {
        void OnDownloadFavicon(int i, String str);

        void OnDownloadImage(int i, String str);

        void OnLinkCrawlResult(CrawlLinkResponse crawlLinkResponse);
    }

    public static abstract class SimpleCrawlerLinkPreviewUIListener implements ICrawlerLinkPreviewUIListener {
        public void OnDownloadFavicon(int i, String str) {
        }

        public void OnDownloadImage(int i, String str) {
        }

        public void OnLinkCrawlResult(CrawlLinkResponse crawlLinkResponse) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public void addListener(@Nullable ICrawlerLinkPreviewUIListener iCrawlerLinkPreviewUIListener) {
        if (iCrawlerLinkPreviewUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iCrawlerLinkPreviewUIListener) {
                    removeListener((ICrawlerLinkPreviewUIListener) all[i]);
                }
            }
            this.mListenerList.add(iCrawlerLinkPreviewUIListener);
        }
    }

    public void removeListener(ICrawlerLinkPreviewUIListener iCrawlerLinkPreviewUIListener) {
        this.mListenerList.remove(iCrawlerLinkPreviewUIListener);
    }

    @NonNull
    public static synchronized CrawlerLinkPreviewUI getInstance() {
        CrawlerLinkPreviewUI crawlerLinkPreviewUI;
        synchronized (CrawlerLinkPreviewUI.class) {
            if (instance == null) {
                instance = new CrawlerLinkPreviewUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            crawlerLinkPreviewUI = instance;
        }
        return crawlerLinkPreviewUI;
    }

    private CrawlerLinkPreviewUI() {
        init();
    }

    private boolean initialized() {
        return this.mNativeHandle != 0;
    }

    private void init() {
        try {
            this.mNativeHandle = nativeInit();
        } catch (Throwable unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void OnLinkCrawlResult(byte[] bArr) {
        try {
            OnLinkCrawlResultImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnLinkCrawlResultImpl(@Nullable byte[] bArr) {
        if (bArr != null) {
            try {
                CrawlLinkResponse parseFrom = CrawlLinkResponse.parseFrom(bArr);
                if (parseFrom != null) {
                    IListener[] all = this.mListenerList.getAll();
                    if (all != null) {
                        for (IListener iListener : all) {
                            ((ICrawlerLinkPreviewUIListener) iListener).OnLinkCrawlResult(parseFrom);
                        }
                    }
                }
            } catch (InvalidProtocolBufferException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnDownloadImage(int i, String str) {
        try {
            OnDownloadImageImpl(i, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnDownloadImageImpl(int i, String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ICrawlerLinkPreviewUIListener) iListener).OnDownloadImage(i, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnDownloadFavicon(int i, String str) {
        try {
            OnDownloadFaviconImpl(i, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnDownloadFaviconImpl(int i, String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ICrawlerLinkPreviewUIListener) iListener).OnDownloadFavicon(i, str);
            }
        }
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }
}
