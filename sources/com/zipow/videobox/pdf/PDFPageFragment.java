package com.zipow.videobox.pdf;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.UiModeUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.androidlib.util.ZMAsyncTask.Status;
import p021us.zoom.androidlib.widget.TouchImageView;
import p021us.zoom.androidlib.widget.TouchImageView.OnViewPortChangedListener;
import p021us.zoom.videomeetings.C4558R;

public class PDFPageFragment extends ZMFragment {
    private static final String PDF_FILE = "pdf_file";
    private static final String PDF_PAGE_NUM = "pdf_page_num";
    private static final String PDF_PSW = "pdf_password";
    @Nullable
    private static ExecutorService mSingleExecutor;
    private final String TAG = PDFPageFragment.class.getSimpleName();
    /* access modifiers changed from: private */
    @Nullable
    public ZMAsyncTask<Void, Void, Long> mAsyncRender;
    @Nullable
    private Bitmap mBitmap;
    private boolean mDisplay = false;
    /* access modifiers changed from: private */
    @Nullable
    public PDFDisplayListener mDisplayListener;
    @Nullable
    private PDFDoc mDoc;
    @Nullable
    private String mFileName = null;
    /* access modifiers changed from: private */
    public TouchImageView mImageView;
    /* access modifiers changed from: private */
    public boolean mIsLoading = false;
    /* access modifiers changed from: private */
    public long mPDFBitmap = 0;
    private int mPageNum = -1;
    @Nullable
    private String mPassword = null;
    @Nullable
    private PDFManager mPdfMgr;
    /* access modifiers changed from: private */
    public int mRenderHeight;
    /* access modifiers changed from: private */
    public int mRenderWidth;
    private boolean mViewCreated = false;
    @Nullable
    private ProgressDialog mWaitingDialog = null;

    @NonNull
    public static PDFPageFragment newInstance(String str, String str2, int i) {
        PDFPageFragment pDFPageFragment = new PDFPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PDF_FILE, str);
        bundle.putString(PDF_PSW, str2);
        bundle.putInt(PDF_PAGE_NUM, i);
        pDFPageFragment.setArguments(bundle);
        return pDFPageFragment;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_pdf_page, viewGroup, false);
        this.mImageView = (TouchImageView) inflate.findViewById(C4558R.C4560id.imageview);
        this.mImageView.setOnViewPortChangedListener(new OnViewPortChangedListener() {
            public void onViewPortChanged() {
                if (PDFPageFragment.this.mDisplayListener != null && PDFPageFragment.this.mImageView.getDrawable() != null) {
                    PDFPageFragment.this.mDisplayListener.onPDFDisplayRefresh();
                }
            }
        });
        Bundle arguments = getArguments();
        if (bundle != null) {
            this.mFileName = bundle.getString(PDF_FILE);
            this.mPassword = bundle.getString(PDF_PSW);
            this.mPageNum = bundle.getInt(PDF_PAGE_NUM, -1);
        } else if (arguments != null) {
            this.mFileName = arguments.getString(PDF_FILE);
            this.mPassword = arguments.getString(PDF_PSW);
            this.mPageNum = arguments.getInt(PDF_PAGE_NUM, -1);
        }
        this.mPdfMgr = PDFManager.getInstance();
        this.mDoc = this.mPdfMgr.openDocument(this.mFileName, this.mPassword);
        this.mViewCreated = true;
        return inflate;
    }

    public void onResume() {
        super.onResume();
        if (this.mDisplay && this.mIsLoading) {
            showWaitingDialog();
        }
    }

    public void startDisplay(PDFDisplayListener pDFDisplayListener) {
        if (this.mViewCreated && !this.mDisplay) {
            this.mDisplay = true;
            this.mDisplayListener = pDFDisplayListener;
            if (this.mPDFBitmap == 0) {
                showWaitingDialog();
                asyncRenderPage();
            } else {
                displayPage();
            }
        }
    }

    public void stopDisplay() {
        if (this.mDisplay) {
            this.mDisplay = false;
            this.mDisplayListener = null;
            releaseBitmap();
        }
    }

    public boolean canScrollVertical(int i) {
        TouchImageView touchImageView = this.mImageView;
        if (touchImageView == null || !this.mDisplay) {
            return false;
        }
        return touchImageView.canScrollVertical(i);
    }

    private void showWaitingDialog() {
        if (this.mWaitingDialog == null) {
            String string = getString(C4558R.string.zm_msg_loading);
            this.mWaitingDialog = new ProgressDialog(getActivity());
            this.mWaitingDialog.requestWindowFeature(1);
            this.mWaitingDialog.setMessage(string);
            this.mWaitingDialog.setCanceledOnTouchOutside(false);
            this.mWaitingDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void dismissWaitingDialog() {
        ProgressDialog progressDialog = this.mWaitingDialog;
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                this.mWaitingDialog.dismiss();
            }
            this.mWaitingDialog = null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0039, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void asyncRenderPage() {
        /*
            r3 = this;
            monitor-enter(r3)
            int r0 = r3.mPageNum     // Catch:{ all -> 0x003a }
            if (r0 >= 0) goto L_0x0007
            monitor-exit(r3)
            return
        L_0x0007:
            boolean r0 = r3.mIsLoading     // Catch:{ all -> 0x003a }
            if (r0 == 0) goto L_0x000d
            monitor-exit(r3)
            return
        L_0x000d:
            r0 = 1
            r3.mIsLoading = r0     // Catch:{ all -> 0x003a }
            r3.releaseBitmap()     // Catch:{ all -> 0x003a }
            int r0 = r3.mPageNum     // Catch:{ all -> 0x003a }
            r3.prepareRenderSize(r0)     // Catch:{ all -> 0x003a }
            r3.checkRenderSizeAndCreateBitmap()     // Catch:{ all -> 0x003a }
            com.zipow.videobox.pdf.PDFPageFragment$2 r0 = new com.zipow.videobox.pdf.PDFPageFragment$2     // Catch:{ all -> 0x003a }
            r0.<init>()     // Catch:{ all -> 0x003a }
            r3.mAsyncRender = r0     // Catch:{ all -> 0x003a }
            java.util.concurrent.ExecutorService r0 = mSingleExecutor     // Catch:{ all -> 0x003a }
            r1 = 0
            if (r0 != 0) goto L_0x002f
            us.zoom.androidlib.util.ZMAsyncTask<java.lang.Void, java.lang.Void, java.lang.Long> r0 = r3.mAsyncRender     // Catch:{ all -> 0x003a }
            java.lang.Void[] r1 = new java.lang.Void[r1]     // Catch:{ all -> 0x003a }
            r0.execute((Params[]) r1)     // Catch:{ all -> 0x003a }
            goto L_0x0038
        L_0x002f:
            us.zoom.androidlib.util.ZMAsyncTask<java.lang.Void, java.lang.Void, java.lang.Long> r0 = r3.mAsyncRender     // Catch:{ all -> 0x003a }
            java.util.concurrent.ExecutorService r2 = mSingleExecutor     // Catch:{ all -> 0x003a }
            java.lang.Void[] r1 = new java.lang.Void[r1]     // Catch:{ all -> 0x003a }
            r0.executeOnExecutor(r2, r1)     // Catch:{ all -> 0x003a }
        L_0x0038:
            monitor-exit(r3)
            return
        L_0x003a:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.pdf.PDFPageFragment.asyncRenderPage():void");
    }

    /* access modifiers changed from: private */
    public void displayPage() {
        if (this.mDisplay) {
            long j = this.mPDFBitmap;
            if (j != 0) {
                Bitmap bitmap = this.mBitmap;
                if (bitmap != null && this.mRenderWidth > 0 && this.mRenderHeight > 0) {
                    this.mDoc.copyBitmap(j, bitmap);
                    this.mImageView.setImageBitmap(this.mBitmap);
                    PDFDisplayListener pDFDisplayListener = this.mDisplayListener;
                    if (pDFDisplayListener != null) {
                        pDFDisplayListener.onPDFDisplayRefresh();
                    }
                }
            }
        }
    }

    private void checkRenderSizeAndCreateBitmap() {
        Bitmap bitmap = null;
        boolean z = false;
        do {
            try {
                if (this.mRenderWidth > 0 && this.mRenderHeight > 0) {
                    bitmap = Bitmap.createBitmap(this.mRenderWidth, this.mRenderHeight, Config.ARGB_8888);
                }
                z = true;
                continue;
            } catch (OutOfMemoryError unused) {
                this.mRenderWidth /= 2;
                this.mRenderHeight /= 2;
                continue;
            }
        } while (!z);
        if (bitmap == null) {
            showToast(getString(C4558R.string.zm_msg_pdf_page_err, Integer.valueOf(this.mPageNum)));
            return;
        }
        Bitmap bitmap2 = this.mBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        this.mBitmap = bitmap;
    }

    private void releaseBitmap() {
        long j = this.mPDFBitmap;
        if (j != 0) {
            PDFDoc pDFDoc = this.mDoc;
            if (pDFDoc != null) {
                pDFDoc.releasePDFBitmap(j);
            }
        }
        this.mPDFBitmap = 0;
        TouchImageView touchImageView = this.mImageView;
        if (touchImageView != null) {
            touchImageView.setImageBitmap(null);
        }
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mBitmap = null;
        }
    }

    private void prepareRenderSize(int i) {
        if (UiModeUtil.isInDesktopMode(getContext())) {
            prepareRenderSizeForDestop(i);
        } else {
            prepareRenderSizeForNormal(i);
        }
    }

    private void prepareRenderSizeForNormal(int i) {
        float displayWidthInDip = UIUtil.getDisplayWidthInDip(getActivity()) * 2.0f;
        float displayHeightInDip = UIUtil.getDisplayHeightInDip(getActivity()) * 2.0f;
        this.mRenderWidth = 0;
        this.mRenderHeight = 0;
        try {
            double pageWidth = (this.mDoc.getPageWidth(i) * 160.0d) / 72.0d;
            double pageHeight = (this.mDoc.getPageHeight(i) * 160.0d) / 72.0d;
            double d = (double) displayWidthInDip;
            if (pageWidth <= d) {
                if (pageWidth <= ((double) displayHeightInDip)) {
                    this.mRenderWidth = (int) pageWidth;
                    this.mRenderHeight = (int) pageHeight;
                    this.mRenderWidth = (this.mRenderWidth / 4) * 4;
                    this.mRenderHeight = (this.mRenderHeight / 4) * 4;
                }
            }
            if (pageWidth > 0.0d && pageHeight > 0.0d) {
                double d2 = ((double) displayHeightInDip) * pageWidth;
                double d3 = d * pageHeight;
                if (d2 >= d3) {
                    this.mRenderWidth = (int) (d2 / pageHeight);
                    this.mRenderHeight = (int) displayHeightInDip;
                } else {
                    this.mRenderWidth = (int) displayWidthInDip;
                    this.mRenderHeight = (int) (d3 / pageWidth);
                }
            }
        } catch (Exception unused) {
        }
        this.mRenderWidth = (this.mRenderWidth / 4) * 4;
        this.mRenderHeight = (this.mRenderHeight / 4) * 4;
    }

    private void prepareRenderSizeForDestop(int i) {
        int metricWith = UIUtil.getMetricWith(getActivity());
        int metricHeight = UIUtil.getMetricHeight(getActivity());
        this.mRenderWidth = 0;
        this.mRenderHeight = 0;
        try {
            double pageWidth = this.mDoc.getPageWidth(i);
            double pageHeight = this.mDoc.getPageHeight(i);
            double d = (double) metricWith;
            double d2 = (double) metricHeight;
            double d3 = (pageWidth / pageHeight) * d2;
            if (d >= d3) {
                d2 = (pageHeight * d) / pageWidth;
                d3 = d;
            }
            this.mRenderWidth = (UIUtil.dip2px(getActivity(), (float) d3) / 4) * 4;
            this.mRenderHeight = (UIUtil.dip2px(getActivity(), (float) d2) / 4) * 4;
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public long renderPage(int i, int i2) {
        PDFDoc pDFDoc = this.mDoc;
        if (pDFDoc != null) {
            int i3 = this.mPageNum;
            if (i3 >= 0 && pDFDoc.openPage(i3)) {
                return this.mDoc.renderPage(this.mPageNum, i, i2, 0);
            }
            return 0;
        }
        return 0;
    }

    public void onDestroy() {
        ZMAsyncTask<Void, Void, Long> zMAsyncTask = this.mAsyncRender;
        if (zMAsyncTask != null && !zMAsyncTask.isCancelled() && this.mAsyncRender.getStatus() == Status.RUNNING) {
            this.mAsyncRender.cancel(true);
            this.mAsyncRender = null;
        }
        dismissWaitingDialog();
        super.onDestroy();
        releaseBitmap();
        PDFDoc pDFDoc = this.mDoc;
        if (pDFDoc != null) {
            pDFDoc.closePage(this.mPageNum);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(PDF_FILE, this.mFileName);
        bundle.putString(PDF_PSW, this.mPassword);
        bundle.putInt(PDF_PAGE_NUM, this.mPageNum);
    }

    public static void startSingleExecutor() {
        if (mSingleExecutor == null) {
            mSingleExecutor = Executors.newSingleThreadExecutor();
        }
    }

    public static void shutdownSingleExecutor() {
        ExecutorService executorService = mSingleExecutor;
        if (executorService != null && !executorService.isShutdown()) {
            mSingleExecutor.shutdown();
        }
        mSingleExecutor = null;
    }

    private void showToast(String str) {
        Toast.makeText(getActivity(), str, 1).show();
    }
}
