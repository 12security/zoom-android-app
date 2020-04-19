package com.zipow.videobox.pdf;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class PDFDoc {
    private static final int DEFAULT_ALPHA = 255;
    private static final long DEFAULT_BG_COLOR = -1;
    public static final int PDF_PRINT_DPI = 72;
    private final String TAG = PDFDoc.class.getSimpleName();
    private long mDocument = 0;
    private String mFileName;
    @NonNull
    private ListenerList mListeners = new ListenerList();
    @NonNull
    private Object mLock = new Object();
    private int mPageCount = 0;
    @Nullable
    private long[] mPageHandles;
    private String mPassword;
    @NonNull
    private List<PDF_BITMAP> mPdfBitmaps = new ArrayList();

    public interface PDFDocListener extends IListener {
        void onLoadPageErr(int i);

        void onRenderPageErr(int i);
    }

    private class PDF_BITMAP {
        public int alpha;
        public int height;
        public long pdfBitmap;
        public int width;

        public PDF_BITMAP(long j, int i, int i2, int i3) {
            this.pdfBitmap = j;
            this.width = i;
            this.height = i2;
            this.alpha = i3;
        }
    }

    public PDFDoc(String str, String str2) {
        this.mFileName = str;
        this.mPassword = str2;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public void openDoc() throws PDFUnknownErrorException, PDFFileAccessException, PDFFormatException, PDFParameterException, PDFPasswordException {
        String str = this.mFileName;
        if (str == null || str.length() <= 0) {
            throw new PDFParameterException("File name Error");
        }
        synchronized (this.mLock) {
            if (this.mDocument == 0) {
                this.mDocument = PdfiumSDK.loadDocument(this.mFileName, this.mPassword);
                this.mPageCount = PdfiumSDK.getPageCount(this.mDocument);
                if (this.mPageCount > 0) {
                    this.mPageHandles = new long[this.mPageCount];
                    return;
                }
                close();
                this.mDocument = 0;
                throw new PDFUnknownErrorException("Page numbers is 0!");
            }
        }
    }

    public void close() {
        synchronized (this.mLock) {
            releaseAllPDFBitmaps();
            closeAllPages();
            PdfiumSDK.closeDocument(this.mDocument);
            this.mPageCount = 0;
            this.mPageHandles = null;
            this.mDocument = 0;
        }
    }

    public int getPageCount() {
        int i;
        synchronized (this.mLock) {
            i = this.mPageCount;
        }
        return i;
    }

    private boolean checkPageIndex(int i) {
        if (this.mDocument != 0) {
            int i2 = this.mPageCount;
            if (i2 > 0 && i < i2) {
                return true;
            }
        }
        return false;
    }

    public long renderPage(int i, int i2, int i3, int i4) {
        int i5 = i2;
        int i6 = i3;
        if (i < 0 || i5 <= 0 || i6 <= 0) {
            return 0;
        }
        synchronized (this.mLock) {
            long page = getPage(i);
            if (page == 0) {
                return 0;
            }
            long createPDFBitmap = createPDFBitmap(i5, i6, 255);
            if (createPDFBitmap == 0) {
                notifyRenderPageErr(i);
                return 0;
            }
            try {
                PdfiumSDK.FillPDFBitmapByRect(createPDFBitmap, 0, 0, i2, i3, -1);
                PdfiumSDK.renderPageBitmap(createPDFBitmap, page, 0, 0, i2, i3, i4, 16);
                return createPDFBitmap;
            } catch (Exception unused) {
                notifyRenderPageErr(i);
                return 0;
            }
        }
    }

    public boolean copyBitmap(long j, @Nullable Bitmap bitmap) {
        if (bitmap == null || j == 0 || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
            return false;
        }
        try {
            synchronized (this.mLock) {
                PdfiumSDK.copyPDFBitmap(j, bitmap);
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public void releasePDFBitmap(long j) {
        if (j != 0) {
            synchronized (this.mLock) {
                PdfiumSDK.destroyFPDFBitmap(j);
            }
        }
    }

    private long createPDFBitmap(int i, int i2, int i3) {
        if (i <= 0 || i2 <= 0) {
            return 0;
        }
        try {
            return PdfiumSDK.createPDFBitmap(i, i2, 255);
        } catch (Exception unused) {
            return 0;
        }
    }

    private void releaseAllPDFBitmaps() {
        for (PDF_BITMAP pdf_bitmap : this.mPdfBitmaps) {
            PdfiumSDK.destroyFPDFBitmap(pdf_bitmap.pdfBitmap);
        }
        this.mPdfBitmaps.clear();
    }

    private long getPage(int i) {
        if (!checkPageIndex(i)) {
            notifyLoadPageErr(i);
            return 0;
        }
        long[] jArr = this.mPageHandles;
        long j = jArr != null ? jArr[i] : 0;
        if (j == 0) {
            try {
                j = PdfiumSDK.loadPage(this.mDocument, i);
                this.mPageHandles[i] = j;
            } catch (Exception unused) {
                notifyLoadPageErr(i);
                return 0;
            }
        }
        return j;
    }

    public boolean openPage(int i) {
        long page;
        synchronized (this.mLock) {
            page = getPage(i);
        }
        return page != 0;
    }

    public double getPageWidth(int i) throws PDFParameterException, PDFUnknownErrorException {
        double pageWidth;
        synchronized (this.mLock) {
            long page = getPage(i);
            if (page != 0) {
                pageWidth = PdfiumSDK.getPageWidth(page);
            } else {
                throw new PDFUnknownErrorException("Get page error");
            }
        }
        return pageWidth;
    }

    public double getPageHeight(int i) throws PDFParameterException, PDFUnknownErrorException {
        double pageHeight;
        synchronized (this.mLock) {
            long page = getPage(i);
            if (page != 0) {
                pageHeight = PdfiumSDK.getPageHeight(page);
            } else {
                throw new PDFUnknownErrorException("Get page error");
            }
        }
        return pageHeight;
    }

    public void closePage(int i) {
        synchronized (this.mLock) {
            releasePage(i);
        }
    }

    private void releasePage(int i) {
        if (checkPageIndex(i)) {
            long j = this.mPageHandles[i];
            if (j > 0) {
                PdfiumSDK.closePage(j);
                this.mPageHandles[i] = 0;
            }
        }
    }

    private void closeAllPages() {
        for (int i = 0; i < this.mPageCount; i++) {
            closePage(i);
        }
    }

    public void registerListener(@Nullable PDFDocListener pDFDocListener) {
        if (pDFDocListener != null) {
            synchronized (this.mLock) {
                this.mListeners.add(pDFDocListener);
            }
        }
    }

    public void unregisterListener(@Nullable PDFDocListener pDFDocListener) {
        if (pDFDocListener != null) {
            synchronized (this.mLock) {
                this.mListeners.remove(pDFDocListener);
            }
        }
    }

    private void notifyRenderPageErr(int i) {
        for (IListener iListener : this.mListeners.getAll()) {
            ((PDFDocListener) iListener).onRenderPageErr(i);
        }
    }

    private void notifyLoadPageErr(int i) {
        for (IListener iListener : this.mListeners.getAll()) {
            ((PDFDocListener) iListener).onLoadPageErr(i);
        }
    }
}
