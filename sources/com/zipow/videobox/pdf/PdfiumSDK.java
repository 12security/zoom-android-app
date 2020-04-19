package com.zipow.videobox.pdf;

import android.graphics.Bitmap;

public class PdfiumSDK {
    public static final int FPDFBitmap_BGR = 2;
    public static final int FPDFBitmap_BGRA = 4;
    public static final int FPDFBitmap_BGRx = 3;
    public static final int FPDFBitmap_Gray = 1;
    public static final int FPDF_ANNOT = 1;
    public static final int FPDF_DEBUG_INFO = 128;
    public static final int FPDF_GRAYSCALE = 8;
    public static final int FPDF_LCD_TEXT = 2;
    public static final int FPDF_NO_CATCH = 256;
    public static final int FPDF_NO_NATIVETEXT = 4;
    public static final int FPDF_PRINTING = 2048;
    public static final int FPDF_RENDER_FORCEHALFTONE = 1024;
    public static final int FPDF_RENDER_LIMITEDIMAGECACHE = 512;
    public static final int FPDF_REVERSE_BYTE_ORDER = 16;

    public static native void FillPDFBitmapByRect(long j, int i, int i2, int i3, int i4, long j2) throws PDFParameterException, PDFUnknownErrorException;

    public static native void closeDocument(long j);

    public static native void closePage(long j);

    public static native void copyPDFBitmap(long j, Bitmap bitmap) throws PDFParameterException, PDFUnknownErrorException;

    public static native long createPDFBitmap(int i, int i2, int i3) throws PDFParameterException, PDFUnknownErrorException;

    public static native void destroyFPDFBitmap(long j);

    public static native void destroyLibrary();

    public static native long getDocPermissions(long j) throws PDFUnknownErrorException;

    public static native int getFileVersion(long j) throws PDFParameterException, PDFUnknownErrorException;

    public static native int getPageCount(long j);

    public static native double getPageHeight(long j) throws PDFParameterException;

    public static native double getPageWidth(long j) throws PDFParameterException;

    public static native void initLibrary(int i);

    public static native long loadDocument(String str, String str2) throws PDFUnknownErrorException, PDFFileAccessException, PDFFormatException, PDFParameterException, PDFPasswordException;

    public static native long loadPage(long j, int i) throws PDFParameterException, PDFPageErrorException;

    public static native void renderPageBitmap(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6) throws PDFParameterException;

    static {
        System.loadLibrary("zoom_pdfium");
    }
}
