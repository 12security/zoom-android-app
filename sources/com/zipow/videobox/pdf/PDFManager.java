package com.zipow.videobox.pdf;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import p021us.zoom.androidlib.util.FileUtils;

public class PDFManager {
    private static final String TAG = "PDFManager";
    @Nullable
    private static PDFManager instance;
    @NonNull
    private ConcurrentHashMap<String, PDFDoc> mPDFDocs = new ConcurrentHashMap<>();
    private boolean mSDKInitialled = false;

    @NonNull
    public static synchronized PDFManager getInstance() {
        synchronized (PDFManager.class) {
            if (instance != null) {
                PDFManager pDFManager = instance;
                return pDFManager;
            }
            instance = new PDFManager();
            PDFManager pDFManager2 = instance;
            return pDFManager2;
        }
    }

    private void initialSDK() {
        if (!this.mSDKInitialled) {
            PdfiumSDK.initLibrary(0);
            this.mSDKInitialled = true;
        }
    }

    private void destroySDK() {
        if (this.mSDKInitialled) {
            PdfiumSDK.destroyLibrary();
            this.mSDKInitialled = false;
        }
    }

    @Nullable
    public PDFDoc openDocument(@NonNull String str, String str2) {
        PDFDoc document = getDocument(str);
        return document == null ? createDocument(str, str2) : document;
    }

    @NonNull
    public PDFDoc createDocument(@NonNull String str, String str2) {
        initialSDK();
        PDFDoc pDFDoc = new PDFDoc(str, str2);
        this.mPDFDocs.put(str, pDFDoc);
        return pDFDoc;
    }

    @Nullable
    public PDFDoc getDocument(@Nullable String str) {
        if (str == null || str.length() <= 0 || !this.mPDFDocs.containsKey(str)) {
            return null;
        }
        return (PDFDoc) this.mPDFDocs.get(str);
    }

    public void closeDocument(String str) {
        PDFDoc document = getDocument(str);
        if (document != null) {
            document.close();
        }
        if (document != null && this.mPDFDocs.containsValue(document)) {
            this.mPDFDocs.remove(document);
        }
        if (this.mPDFDocs.isEmpty()) {
            destroySDK();
        }
    }

    public void closeDocument(@Nullable PDFDoc pDFDoc) {
        if (pDFDoc != null) {
            pDFDoc.close();
            if (this.mPDFDocs.containsValue(pDFDoc)) {
                this.mPDFDocs.remove(pDFDoc);
            }
            if (this.mPDFDocs.isEmpty()) {
                destroySDK();
            }
        }
    }

    public void closeAllDocuments() {
        for (PDFDoc close : this.mPDFDocs.values()) {
            close.close();
        }
        this.mPDFDocs.clear();
        destroySDK();
    }

    public boolean isValidPDFUri(Context context, Uri uri) {
        if (uri == null) {
            return false;
        }
        String pathFromUri = FileUtils.getPathFromUri(context, uri);
        if (pathFromUri != null && pathFromUri.toLowerCase().endsWith(".pdf")) {
            return true;
        }
        return "application/pdf".equalsIgnoreCase(context.getContentResolver().getType(uri));
    }

    public String savePDFToLocalFile(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if (HttpHost.DEFAULT_SCHEME_NAME.equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
            try {
                return FileUtils.downloadFile(context, new URL(uri.toString()));
            } catch (MalformedURLException unused) {
            }
        } else {
            if (Param.CONTENT.equalsIgnoreCase(scheme)) {
                return FileUtils.saveFileFromContentProvider(context, uri);
            }
            return null;
        }
    }
}
