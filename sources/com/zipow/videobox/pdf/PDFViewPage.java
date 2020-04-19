package com.zipow.videobox.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.pdf.PDFStatePagerAdapter.PDFStatePagerAdapterListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMViewPager;

public class PDFViewPage extends ZMViewPager implements PDFStatePagerAdapterListener, PDFDisplayListener {
    private final float TOUCH_SENS = 5.0f;
    private PDFStatePagerAdapter mAdapter;
    /* access modifiers changed from: private */
    public int mClickCount = 0;
    @NonNull
    private Runnable mClickRunnable = new Runnable() {
        public void run() {
            if (PDFViewPage.this.mListener != null && PDFViewPage.this.mClickCount < 2) {
                PDFViewPage.this.mListener.onClickEvent();
            }
            PDFViewPage.this.mClickCount = 0;
        }
    };
    private Context mContext;
    @Nullable
    private String mFileName;
    private float mLastTouchX = 0.0f;
    /* access modifiers changed from: private */
    public PDFViewPageListener mListener;
    private String mPassword;
    private boolean mStarted = false;
    private SwitchPageRunnable mSwitchRunnable;

    public interface PDFViewPageListener {
        void onClickEvent();

        void onLoaderPageErr(int i);

        void onPDFDisplayRefresh();

        void onRenderPageErr(int i);
    }

    public static class SimplePDFViewPageListener implements PDFViewPageListener {
        public void onClickEvent() {
        }

        public void onLoaderPageErr(int i) {
        }

        public void onPDFDisplayRefresh() {
        }

        public void onRenderPageErr(int i) {
        }
    }

    protected class SwitchPageRunnable implements Runnable {
        private boolean mUp = false;
        private PDFViewPage mViewPage;

        public SwitchPageRunnable(PDFViewPage pDFViewPage) {
            this.mViewPage = pDFViewPage;
        }

        public void setSwitchFlag(boolean z) {
            this.mUp = z;
        }

        public void run() {
            PDFViewPage pDFViewPage = this.mViewPage;
            if (pDFViewPage != null && pDFViewPage.isStarted()) {
                if (!this.mUp) {
                    int currentItem = this.mViewPage.getCurrentItem() + 1;
                    if (currentItem < this.mViewPage.getPageCount()) {
                        this.mViewPage.setCurrentItem(currentItem);
                    }
                } else {
                    int currentItem2 = this.mViewPage.getCurrentItem() - 1;
                    if (currentItem2 >= 0) {
                        this.mViewPage.setCurrentItem(currentItem2);
                    }
                }
            }
        }
    }

    public PDFViewPage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public PDFViewPage(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mAdapter = new PDFStatePagerAdapter(((ZMActivity) this.mContext).getSupportFragmentManager());
        setAdapter(this.mAdapter);
    }

    public void setPDFViewPageListener(PDFViewPageListener pDFViewPageListener) {
        this.mListener = pDFViewPageListener;
    }

    public boolean openFile(@Nullable String str, String str2) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        String str3 = this.mFileName;
        if (str3 != null && !str3.equals(str)) {
            closeFile();
        }
        if (this.mStarted) {
            return true;
        }
        this.mFileName = str;
        this.mPassword = str2;
        if (!this.mAdapter.open(this.mFileName, this.mPassword, this, this)) {
            return false;
        }
        this.mStarted = true;
        return true;
    }

    public void closeFile() {
        SwitchPageRunnable switchPageRunnable = this.mSwitchRunnable;
        if (switchPageRunnable != null) {
            removeCallbacks(switchPageRunnable);
        }
        removeCallbacks(this.mClickRunnable);
        PDFStatePagerAdapter pDFStatePagerAdapter = this.mAdapter;
        if (pDFStatePagerAdapter != null) {
            pDFStatePagerAdapter.close();
        }
        this.mStarted = false;
    }

    public void loadPage(int i) {
        PDFStatePagerAdapter pDFStatePagerAdapter = this.mAdapter;
        if (pDFStatePagerAdapter != null) {
            pDFStatePagerAdapter.loadPage(i);
        }
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public int getPageCount() {
        if (this.mStarted) {
            PDFStatePagerAdapter pDFStatePagerAdapter = this.mAdapter;
            if (pDFStatePagerAdapter != null) {
                return pDFStatePagerAdapter.getCount();
            }
        }
        return 0;
    }

    public boolean renderPage(int i, @Nullable Bitmap bitmap) {
        if (bitmap != null && this.mStarted) {
            PDFStatePagerAdapter pDFStatePagerAdapter = this.mAdapter;
            if (pDFStatePagerAdapter != null) {
                return pDFStatePagerAdapter.renderPage(i, bitmap);
            }
        }
        return false;
    }

    public void onPDFDisplayRefresh() {
        PDFViewPageListener pDFViewPageListener = this.mListener;
        if (pDFViewPageListener != null) {
            pDFViewPageListener.onPDFDisplayRefresh();
        }
    }

    public void onRenderPageErr(int i) {
        PDFViewPageListener pDFViewPageListener = this.mListener;
        if (pDFViewPageListener != null) {
            pDFViewPageListener.onRenderPageErr(i);
        }
    }

    public void onLoadPageErr(int i) {
        PDFViewPageListener pDFViewPageListener = this.mListener;
        if (pDFViewPageListener != null) {
            pDFViewPageListener.onLoaderPageErr(i);
        }
    }

    public boolean onInterceptTouchEvent(@NonNull MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mLastTouchX = motionEvent.getX();
        } else if (action == 1) {
            if (Math.abs(motionEvent.getX() - this.mLastTouchX) >= ((float) UIUtil.dip2px(this.mContext, 5.0f)) || this.mListener == null) {
                this.mClickCount = 0;
            } else {
                this.mClickCount++;
                removeCallbacks(this.mClickRunnable);
                postDelayed(this.mClickRunnable, 300);
            }
            this.mLastTouchX = 0.0f;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public boolean dispatchGenericPointerEvent(@NonNull MotionEvent motionEvent) {
        if (motionEvent.getAction() == 8) {
            float axisValue = motionEvent.getAxisValue(9);
            if (!this.mAdapter.canPageVerticalScroll(axisValue)) {
                int i = (axisValue > 0.0f ? 1 : (axisValue == 0.0f ? 0 : -1));
                if (i != 0) {
                    boolean z = i > 0;
                    SwitchPageRunnable switchPageRunnable = this.mSwitchRunnable;
                    if (switchPageRunnable != null) {
                        removeCallbacks(switchPageRunnable);
                        this.mSwitchRunnable.setSwitchFlag(z);
                    } else {
                        this.mSwitchRunnable = new SwitchPageRunnable(this);
                        this.mSwitchRunnable.setSwitchFlag(z);
                    }
                    postDelayed(this.mSwitchRunnable, 50);
                }
            }
        }
        return super.dispatchGenericPointerEvent(motionEvent);
    }
}
