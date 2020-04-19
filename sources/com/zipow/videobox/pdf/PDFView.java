package com.zipow.videobox.pdf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.zipow.videobox.pdf.PDFViewPage.PDFViewPageListener;
import p021us.zoom.videomeetings.C4558R;

public class PDFView extends FrameLayout {
    @NonNull
    private static String TAG = "PDFView";
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentPage = 0;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    @Nullable
    public PDFViewListener mListener = null;
    @NonNull
    private PDFViewPageListener mPDFViewPageListener = new PDFViewPageListener() {
        public void onPDFDisplayRefresh() {
            if (PDFView.this.mListener != null) {
                PDFView.this.mListener.onPDFViewPageChanged();
            }
        }

        public void onRenderPageErr(int i) {
            PDFView.this.showPageErrorInfo(i);
        }

        public void onLoaderPageErr(int i) {
            PDFView.this.showPageErrorInfo(i);
        }

        public void onClickEvent() {
            if (PDFView.this.mListener != null) {
                PDFView.this.mListener.onPDFViewClicked();
            }
            if (PDFView.this.needShowSeekBar()) {
                if (PDFView.this.mSeekBar.getVisibility() == 0) {
                    PDFView.this.mSeekBar.setVisibility(4);
                } else {
                    PDFView.this.mSeekBar.setVisibility(0);
                }
            }
        }
    };
    @Nullable
    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageSelected(int i) {
            PDFView.this.mPageView.loadPage(i);
            if (PDFView.this.mListener != null) {
                PDFView.this.mListener.onPDFViewPageChanged();
            }
            if (PDFView.this.needShowSeekBar()) {
                PDFView.this.mSeekBar.setProgress(i);
            }
        }

        public void onPageScrolled(int i, float f, int i2) {
            if (PDFView.this.mListener != null) {
                PDFView.this.mListener.onPDFViewPageChanged();
            }
        }
    };
    private View mPageContainer;
    /* access modifiers changed from: private */
    public int mPageCount = 0;
    /* access modifiers changed from: private */
    public TextView mPageNumView;
    /* access modifiers changed from: private */
    public PDFViewPage mPageView;
    /* access modifiers changed from: private */
    public SeekBar mSeekBar;
    @NonNull
    private OnSeekBarChangeListener mSeekBarListener = new OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (i >= PDFView.this.mPageCount) {
                PDFView pDFView = PDFView.this;
                pDFView.mCurrentPage = pDFView.mPageCount;
            } else {
                PDFView.this.mCurrentPage = i;
            }
            if (PDFView.this.mCurrentPage < 0) {
                PDFView.this.mCurrentPage = 0;
            }
            if (PDFView.this.mPageNumView.getVisibility() == 0) {
                PDFView.this.mPageNumView.setText(PDFView.this.mContext.getString(C4558R.string.zm_lbl_pdf_page_number, new Object[]{Integer.valueOf(PDFView.this.mCurrentPage + 1)}));
            }
            PDFView.this.mHandler.removeCallbacks(PDFView.this.mThumbImageRenderRunnable);
            PDFView.this.mHandler.postDelayed(PDFView.this.mThumbImageRenderRunnable, 100);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (PDFView.this.needShowSeekBar() && seekBar == PDFView.this.mSeekBar) {
                PDFView.this.mThumInfo.setVisibility(0);
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (PDFView.this.needShowSeekBar() && seekBar == PDFView.this.mSeekBar) {
                PDFView.this.mHandler.removeCallbacks(PDFView.this.mThumbImageRenderRunnable);
                PDFView.this.mThumInfo.setVisibility(8);
                if (PDFView.this.mSuccess && PDFView.this.mCurrentPage >= 0 && PDFView.this.mCurrentPage < PDFView.this.mPageCount) {
                    PDFView.this.mPageView.setCurrentItem(PDFView.this.mCurrentPage);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mSuccess = false;
    private ImageView mThumImageView;
    /* access modifiers changed from: private */
    public View mThumInfo;
    @Nullable
    private Bitmap mThumbImageBitmap;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mThumbImageRenderRunnable = new Runnable() {
        public void run() {
            PDFView.this.showPageThumb();
        }
    };

    public interface PDFViewListener {
        void onPDFViewClicked();

        void onPDFViewPageChanged();
    }

    public PDFView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PDFView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public PDFView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @SuppressLint({"NewApi"})
    public PDFView(@NonNull Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View.inflate(context, C4558R.layout.zm_pdf_view, this);
        this.mPageView = (PDFViewPage) findViewById(C4558R.C4560id.pdfPageView);
        this.mPageContainer = findViewById(C4558R.C4560id.pageContainer);
        this.mSeekBar = (SeekBar) findViewById(C4558R.C4560id.pdfSeekBar);
        if (!isInEditMode()) {
            this.mSeekBar.setOnSeekBarChangeListener(this.mSeekBarListener);
            this.mPageView.setPDFViewPageListener(this.mPDFViewPageListener);
            this.mPageView.setOnPageChangeListener(this.mPageChangeListener);
        }
        this.mThumInfo = findViewById(C4558R.C4560id.thumbInfo);
        this.mThumImageView = (ImageView) findViewById(C4558R.C4560id.thumbImage);
        this.mPageNumView = (TextView) findViewById(C4558R.C4560id.pageNum);
    }

    public void setSeekBarBottomPadding(int i) {
        SeekBar seekBar = this.mSeekBar;
        if (seekBar != null && this.mThumInfo != null) {
            float f = (float) i;
            seekBar.setY(seekBar.getY() - f);
            View view = this.mThumInfo;
            view.setY(view.getY() - f);
        }
    }

    public boolean setPdfFile(String str, String str2) {
        this.mSuccess = this.mPageView.openFile(str, str2);
        if (!this.mSuccess) {
            return false;
        }
        this.mPageCount = this.mPageView.getPageCount();
        if (this.mPageCount <= 0) {
            return false;
        }
        if (needShowSeekBar()) {
            this.mSeekBar.setMax(this.mPageCount - 1);
        } else {
            this.mSeekBar.setVisibility(4);
        }
        return true;
    }

    public void closeFile() {
        if (this.mSuccess) {
            this.mPageView.closeFile();
            this.mPageCount = 0;
            this.mSuccess = false;
        }
    }

    public int getPageWidth() {
        return this.mPageView.getWidth();
    }

    public int getPageHeight() {
        return this.mPageView.getHeight();
    }

    public View getPageContent() {
        return this.mPageContainer;
    }

    public void setSeekBarVisible(int i) {
        if (this.mSeekBar != null && needShowSeekBar()) {
            this.mSeekBar.setVisibility(i);
        }
    }

    public void setListener(@Nullable PDFViewListener pDFViewListener) {
        this.mListener = pDFViewListener;
    }

    private void checkThumbImage() {
        int width = this.mThumImageView.getWidth();
        int height = this.mThumImageView.getHeight();
        if (width <= 0 || height <= 0) {
            this.mThumbImageBitmap = null;
            this.mThumImageView.setImageBitmap(null);
            return;
        }
        Bitmap bitmap = this.mThumbImageBitmap;
        if (bitmap != null) {
            if (bitmap.getWidth() != width || this.mThumbImageBitmap.getHeight() != height) {
                this.mThumbImageBitmap.recycle();
            } else {
                return;
            }
        }
        this.mThumbImageBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        this.mThumImageView.setImageBitmap(this.mThumbImageBitmap);
    }

    /* access modifiers changed from: private */
    public void showPageThumb() {
        if (this.mThumImageView.getVisibility() == 0) {
            checkThumbImage();
            Bitmap bitmap = this.mThumbImageBitmap;
            if (bitmap != null) {
                this.mPageView.renderPage(this.mCurrentPage, bitmap);
                this.mThumImageView.invalidate();
            }
        }
    }

    public boolean needShowSeekBar() {
        return this.mPageCount > 4;
    }

    /* access modifiers changed from: private */
    public void showPageErrorInfo(int i) {
        Toast.makeText(this.mContext, this.mContext.getResources().getString(C4558R.string.zm_msg_pdf_page_err, new Object[]{Integer.valueOf(i)}), 1).show();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SeekBar seekBar = this.mSeekBar;
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(null);
        }
        PDFViewPage pDFViewPage = this.mPageView;
        if (pDFViewPage != null) {
            pDFViewPage.setPDFViewPageListener(null);
            this.mPageView.setOnPageChangeListener(null);
        }
        this.mHandler.removeCallbacksAndMessages(null);
    }
}
