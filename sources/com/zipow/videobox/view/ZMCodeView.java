package com.zipow.videobox.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.net.URL;

public class ZMCodeView extends WebView {
    private static final String TAG = "ZMCodeView";
    @Nullable
    private String content;
    private OnContentChangedListener onContentChangedListener;
    private boolean zoomSupport = false;

    private static class NetworkLoader extends AsyncTask<Void, Void, String> {
        private WeakReference<ZMCodeView> codeView;
        private final URL url;

        private NetworkLoader(ZMCodeView zMCodeView, URL url2) {
            this.url = url2;
            this.codeView = new WeakReference<>(zMCodeView);
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x003a A[SYNTHETIC, Splitter:B:18:0x003a] */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x0041 A[SYNTHETIC, Splitter:B:25:0x0041] */
        @androidx.annotation.Nullable
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String doInBackground(java.lang.Void... r4) {
            /*
                r3 = this;
                r4 = 0
                java.net.URL r0 = r3.url     // Catch:{ IOException -> 0x003e, all -> 0x0035 }
                java.net.URLConnection r0 = r0.openConnection()     // Catch:{ IOException -> 0x003e, all -> 0x0035 }
                java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x003e, all -> 0x0035 }
                java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x003e, all -> 0x0035 }
                java.io.InputStream r0 = r0.getInputStream()     // Catch:{ IOException -> 0x003e, all -> 0x0035 }
                r2.<init>(r0)     // Catch:{ IOException -> 0x003e, all -> 0x0035 }
                r1.<init>(r2)     // Catch:{ IOException -> 0x003e, all -> 0x0035 }
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0033, all -> 0x0031 }
                r0.<init>()     // Catch:{ IOException -> 0x0033, all -> 0x0031 }
            L_0x001a:
                java.lang.String r2 = r1.readLine()     // Catch:{ IOException -> 0x0033, all -> 0x0031 }
                if (r2 == 0) goto L_0x0029
                r0.append(r2)     // Catch:{ IOException -> 0x0033, all -> 0x0031 }
                java.lang.String r2 = "\n"
                r0.append(r2)     // Catch:{ IOException -> 0x0033, all -> 0x0031 }
                goto L_0x001a
            L_0x0029:
                java.lang.String r4 = r0.toString()     // Catch:{ IOException -> 0x0033, all -> 0x0031 }
                r1.close()     // Catch:{ IOException -> 0x0030 }
            L_0x0030:
                return r4
            L_0x0031:
                r4 = move-exception
                goto L_0x0038
            L_0x0033:
                goto L_0x003f
            L_0x0035:
                r0 = move-exception
                r1 = r4
                r4 = r0
            L_0x0038:
                if (r1 == 0) goto L_0x003d
                r1.close()     // Catch:{ IOException -> 0x003d }
            L_0x003d:
                throw r4
            L_0x003e:
                r1 = r4
            L_0x003f:
                if (r1 == 0) goto L_0x0044
                r1.close()     // Catch:{ IOException -> 0x0044 }
            L_0x0044:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ZMCodeView.NetworkLoader.doInBackground(java.lang.Void[]):java.lang.String");
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            WeakReference<ZMCodeView> weakReference = this.codeView;
            ZMCodeView zMCodeView = weakReference != null ? (ZMCodeView) weakReference.get() : null;
            if (zMCodeView != null) {
                zMCodeView.setSource(str);
            }
        }
    }

    public interface OnContentChangedListener {
        void onContentChanged();
    }

    public ZMCodeView(Context context) {
        super(context);
        initView(context);
    }

    public ZMCodeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public ZMCodeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @TargetApi(21)
    public ZMCodeView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        loadUrl(BasicWebViewClient.BLANK_PAGE);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(this.zoomSupport);
        settings.setDisplayZoomControls(false);
        setScrollBarStyle(0);
        setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i >= 100) {
                    ZMCodeView.this.injectCodeCss();
                    ZMCodeView.this.injectCodeJs();
                }
            }
        });
        setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
            }
        });
    }

    private void changeZoomSettings(boolean z) {
        this.zoomSupport = z;
        getSettings().setSupportZoom(z);
    }

    public void setOnContentChangedListener(OnContentChangedListener onContentChangedListener2) {
        this.onContentChangedListener = onContentChangedListener2;
    }

    public void setSource(@Nullable String str) {
        if (str != null && str.length() != 0) {
            this.content = str;
            loadDataWithBaseURL("file:///android_asset/", this.content, "text/html", "utf-8", null);
            OnContentChangedListener onContentChangedListener2 = this.onContentChangedListener;
            if (onContentChangedListener2 != null) {
                onContentChangedListener2.onContentChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void injectCodeJs() {
        loadUrl("javascript:try{if(document.head && !document.getElementById('injectScript')) {var injectScript = document.createElement('script');injectScript.src='file:///android_asset/zm_code.js';injectScript.onload=function(){documentReady();};injectScript.id='injectScript';document.head.appendChild(injectScript);}}catch(e) {}");
    }

    /* access modifiers changed from: private */
    public void injectCodeCss() {
        loadUrl("javascript:try{if(document.head && !document.getElementById('injectCss')) {var injectCss = document.createElement('link');injectCss.setAttribute('rel', 'stylesheet');injectCss.setAttribute('href', 'styles/zm_style.css');injectCss.id='injectCss';document.head.appendChild(injectCss);}}catch(e) {}");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0051, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0052, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0054, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0055, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x005f, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        r0.addSuppressed(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0064, code lost:
        r1.close();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0051 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x0006] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x005b A[SYNTHETIC, Splitter:B:42:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0064 A[Catch:{ IOException -> 0x0069 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSource(@androidx.annotation.NonNull java.io.File r7) {
        /*
            r6 = this;
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0068 }
            r1.<init>(r7)     // Catch:{ IOException -> 0x0068 }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
            r7.<init>(r2)     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x003c, all -> 0x0039 }
            r2.<init>()     // Catch:{ Throwable -> 0x003c, all -> 0x0039 }
        L_0x0015:
            java.lang.String r3 = r7.readLine()     // Catch:{ Throwable -> 0x003c, all -> 0x0039 }
            if (r3 == 0) goto L_0x002c
            r2.append(r3)     // Catch:{ Throwable -> 0x003c, all -> 0x0039 }
            java.lang.String r4 = "<br />"
            boolean r3 = r3.endsWith(r4)     // Catch:{ Throwable -> 0x003c, all -> 0x0039 }
            if (r3 == 0) goto L_0x0015
            java.lang.String r3 = "\n"
            r2.append(r3)     // Catch:{ Throwable -> 0x003c, all -> 0x0039 }
            goto L_0x0015
        L_0x002c:
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x003c, all -> 0x0039 }
            r7.close()     // Catch:{ Throwable -> 0x0037 }
            r1.close()     // Catch:{ IOException -> 0x0069 }
            goto L_0x0069
        L_0x0037:
            r7 = move-exception
            goto L_0x0056
        L_0x0039:
            r2 = move-exception
            r3 = r0
            goto L_0x0042
        L_0x003c:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x003e }
        L_0x003e:
            r3 = move-exception
            r5 = r3
            r3 = r2
            r2 = r5
        L_0x0042:
            if (r3 == 0) goto L_0x004d
            r7.close()     // Catch:{ Throwable -> 0x0048, all -> 0x0051 }
            goto L_0x0050
        L_0x0048:
            r7 = move-exception
            r3.addSuppressed(r7)     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
            goto L_0x0050
        L_0x004d:
            r7.close()     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
        L_0x0050:
            throw r2     // Catch:{ Throwable -> 0x0054, all -> 0x0051 }
        L_0x0051:
            r7 = move-exception
            r2 = r0
            goto L_0x0059
        L_0x0054:
            r7 = move-exception
            r2 = r0
        L_0x0056:
            r0 = r7
            throw r0     // Catch:{ all -> 0x0058 }
        L_0x0058:
            r7 = move-exception
        L_0x0059:
            if (r0 == 0) goto L_0x0064
            r1.close()     // Catch:{ Throwable -> 0x005f }
            goto L_0x0067
        L_0x005f:
            r1 = move-exception
            r0.addSuppressed(r1)     // Catch:{ IOException -> 0x0069 }
            goto L_0x0067
        L_0x0064:
            r1.close()     // Catch:{ IOException -> 0x0069 }
        L_0x0067:
            throw r7     // Catch:{ IOException -> 0x0069 }
        L_0x0068:
            r2 = r0
        L_0x0069:
            if (r2 != 0) goto L_0x006c
            goto L_0x006f
        L_0x006c:
            r6.setSource(r2)
        L_0x006f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ZMCodeView.setSource(java.io.File):void");
    }

    public void setSource(URL url) {
        new NetworkLoader(url).execute(new Void[0]);
    }

    public void refresh() {
        if (this.content != null) {
            loadUrl(BasicWebViewClient.BLANK_PAGE);
            setSource(this.content);
        }
    }

    public void setZoomSupportEnabled(boolean z) {
        changeZoomSettings(z);
    }
}
