package com.zipow.videobox.share;

import android.graphics.Canvas;
import com.zipow.annotate.AnnotateDrawingView.AnnoClearType;
import com.zipow.annotate.AnnotateDrawingView.AnnoTipType;

public interface IDrawingViewListener {
    void onAnnoWidthChanged(int i);

    void onBeginEditing(int i, int i2);

    void onBitmapChanged(Canvas canvas);

    void onClearClicked(AnnoClearType annoClearType);

    void onDismissAllTip();

    void onEndEditing();

    void onLongPressed(boolean z);

    void onNewPageClicked();

    void onPageManagementClicked();

    void onRepaint();

    void onSavePageSnapshot(int i);

    void onSaveWbClicked();

    void onShapeRecognitionChecked(boolean z);

    void onShowAnnoTip(AnnoTipType annoTipType, int i);

    void setAnnoWindow(int i, int i2, float f, float f2, float f3);
}
