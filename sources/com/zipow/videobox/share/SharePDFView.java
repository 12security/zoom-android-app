package com.zipow.videobox.share;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.pdf.PDFView;
import com.zipow.videobox.pdf.PDFView.PDFViewListener;
import p021us.zoom.videomeetings.C4558R;

public class SharePDFView extends ShareBaseView implements PDFViewListener {
    private static final String TAG = "SharePDFView";
    private PDFView mPDFView;
    private boolean mSuccess = false;
    private View mToolbar;

    public void onPDFViewClicked() {
    }

    public SharePDFView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SharePDFView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public SharePDFView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(C4558R.layout.zm_share_pdf_view, null, false);
        this.mPDFView = (PDFView) inflate.findViewById(C4558R.C4560id.pdfPage);
        this.mToolbar = inflate.findViewById(C4558R.C4560id.sharePdfToolbar);
        PDFView pDFView = this.mPDFView;
        if (pDFView != null) {
            pDFView.setListener(this);
        }
        addView(inflate);
    }

    public boolean setPdfFile(String str, String str2) {
        this.mSuccess = this.mPDFView.setPdfFile(str, str2);
        return this.mSuccess;
    }

    public void stop() {
        if (this.mSuccess) {
            this.mPDFView.closeFile();
        }
    }

    public void drawShareContent(@Nullable Canvas canvas) {
        if (canvas != null) {
            View pageContent = this.mPDFView.getPageContent();
            if (pageContent != null) {
                pageContent.draw(canvas);
            }
        }
    }

    public int getShareContentWidth() {
        return this.mPDFView.getPageWidth();
    }

    public int getShareContentHeight() {
        return this.mPDFView.getPageHeight();
    }

    public void setDrawingMode(boolean z) {
        if (z) {
            this.mToolbar.setVisibility(0);
            this.mPDFView.setSeekBarVisible(4);
            return;
        }
        this.mToolbar.setVisibility(8);
    }

    public boolean hasBottomBar() {
        return this.mPDFView.needShowSeekBar();
    }

    public void onPDFViewPageChanged() {
        notifyRefresh();
    }
}
