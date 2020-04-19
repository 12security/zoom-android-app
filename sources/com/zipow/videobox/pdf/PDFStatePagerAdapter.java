package com.zipow.videobox.pdf;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.zipow.videobox.pdf.PDFDoc.PDFDocListener;
import java.util.HashMap;

public class PDFStatePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "PDFStatePagerAdapter";
    private PDFDisplayListener mDisplayListener;
    private int mDisplayPage;
    @Nullable
    private PDFDoc mDocument;
    private String mFileName;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public PDFStatePagerAdapterListener mListener;
    @Nullable
    private PDFManager mPDFMgr;
    private int mPageCount;
    @NonNull
    private HashMap<Integer, PDFPageFragment> mPageFragments = new HashMap<>();
    private String mPassword;
    @NonNull
    private PDFDocListener mPdfDocListener = new PDFDocListener() {
        public void onRenderPageErr(final int i) {
            PDFStatePagerAdapter.this.mHandler.post(new Runnable() {
                public void run() {
                    if (PDFStatePagerAdapter.this.mListener != null) {
                        PDFStatePagerAdapter.this.mListener.onRenderPageErr(i);
                    }
                }
            });
        }

        public void onLoadPageErr(final int i) {
            PDFStatePagerAdapter.this.mHandler.post(new Runnable() {
                public void run() {
                    if (PDFStatePagerAdapter.this.mListener != null) {
                        PDFStatePagerAdapter.this.mListener.onLoadPageErr(i);
                    }
                }
            });
        }
    };
    private boolean mSuccess = false;

    public interface PDFStatePagerAdapterListener {
        void onLoadPageErr(int i);

        void onRenderPageErr(int i);
    }

    public PDFStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Nullable
    public Fragment getItem(int i) {
        if (this.mPageFragments.containsKey(Integer.valueOf(i))) {
            Fragment fragment = (Fragment) this.mPageFragments.get(Integer.valueOf(i));
            if (fragment != null) {
                return fragment;
            }
        }
        PDFPageFragment newInstance = PDFPageFragment.newInstance(this.mFileName, this.mPassword, i);
        this.mPageFragments.remove(Integer.valueOf(i));
        this.mPageFragments.put(Integer.valueOf(i), newInstance);
        return newInstance;
    }

    public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
        super.destroyItem(viewGroup, i, obj);
        this.mPageFragments.remove(Integer.valueOf(i));
    }

    public int getCount() {
        return this.mPageCount;
    }

    public void setPrimaryItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
        super.setPrimaryItem(viewGroup, i, obj);
        this.mDisplayPage = i;
        refreshDisplayPage(i);
    }

    private void refreshDisplayPage(int i) {
        for (Integer intValue : this.mPageFragments.keySet()) {
            int intValue2 = intValue.intValue();
            PDFPageFragment pDFPageFragment = (PDFPageFragment) this.mPageFragments.get(Integer.valueOf(intValue2));
            if (intValue2 == i) {
                pDFPageFragment.startDisplay(this.mDisplayListener);
            } else {
                pDFPageFragment.stopDisplay();
            }
        }
    }

    public void close() {
        PDFDoc pDFDoc = this.mDocument;
        if (pDFDoc != null) {
            this.mPDFMgr.closeDocument(pDFDoc);
            this.mDocument = null;
        }
        PDFPageFragment.shutdownSingleExecutor();
        this.mSuccess = false;
    }

    public boolean open(String str, String str2, PDFStatePagerAdapterListener pDFStatePagerAdapterListener, PDFDisplayListener pDFDisplayListener) {
        this.mFileName = str;
        this.mPassword = str2;
        this.mListener = pDFStatePagerAdapterListener;
        this.mDisplayListener = pDFDisplayListener;
        this.mPDFMgr = PDFManager.getInstance();
        PDFPageFragment.startSingleExecutor();
        String str3 = this.mFileName;
        if (str3 == null || str3.length() <= 0) {
            return false;
        }
        if (this.mSuccess) {
            return true;
        }
        this.mDocument = this.mPDFMgr.createDocument(this.mFileName, this.mPassword);
        this.mDocument.registerListener(this.mPdfDocListener);
        try {
            this.mDocument.openDoc();
            this.mPageCount = this.mDocument.getPageCount();
            this.mSuccess = true;
            notifyDataSetChanged();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean renderPage(int i, @Nullable Bitmap bitmap) {
        if (bitmap != null && this.mSuccess) {
            PDFDoc pDFDoc = this.mDocument;
            if (pDFDoc != null && i < this.mPageCount && i >= 0) {
                if (!this.mDocument.copyBitmap(pDFDoc.renderPage(i, bitmap.getWidth(), bitmap.getHeight(), 0), bitmap)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public void loadPage(int i) {
        refreshDisplayPage(i);
    }

    public boolean canPageVerticalScroll(float f) {
        PDFPageFragment pDFPageFragment = (PDFPageFragment) this.mPageFragments.get(Integer.valueOf(this.mDisplayPage));
        if (pDFPageFragment == null) {
            return false;
        }
        return pDFPageFragment.canScrollVertical((int) f);
    }
}
