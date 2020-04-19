package com.zipow.annotate.annoDialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.core.widget.PopupWindowCompat;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.annotate.AnnoToolType;
import com.zipow.videobox.share.IDrawingViewListener;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateLineView extends FrameLayout {
    private TextView mLine12;
    /* access modifiers changed from: private */
    public TextView mLine2;
    /* access modifiers changed from: private */
    public TextView mLine4;
    /* access modifiers changed from: private */
    public TextView mLine8;
    /* access modifiers changed from: private */
    public IDrawingViewListener mListeners;
    private PopupWindow popupWindow;

    public AnnotateLineView(@NonNull Context context) {
        super(context);
        init();
    }

    public AnnotateLineView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public AnnotateLineView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View inflate = inflate(getContext(), C4558R.layout.zm_anno_line_tip, null);
        this.mLine2 = (TextView) inflate.findViewById(C4558R.C4560id.line2Btn);
        this.mLine2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateLineView.this.mListeners != null) {
                    AnnotateLineView.this.mListeners.onAnnoWidthChanged(2);
                }
                AnnotateLineView annotateLineView = AnnotateLineView.this;
                annotateLineView.updateSelection(annotateLineView.mLine2);
            }
        });
        this.mLine4 = (TextView) inflate.findViewById(C4558R.C4560id.line4Btn);
        this.mLine4.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateLineView.this.mListeners != null) {
                    AnnotateLineView.this.mListeners.onAnnoWidthChanged(4);
                }
                AnnotateLineView annotateLineView = AnnotateLineView.this;
                annotateLineView.updateSelection(annotateLineView.mLine4);
            }
        });
        this.mLine8 = (TextView) inflate.findViewById(C4558R.C4560id.line8Btn);
        this.mLine8.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateLineView.this.mListeners != null) {
                    AnnotateLineView.this.mListeners.onAnnoWidthChanged(8);
                }
                AnnotateLineView annotateLineView = AnnotateLineView.this;
                annotateLineView.updateSelection(annotateLineView.mLine8);
            }
        });
        this.mLine12 = (TextView) inflate.findViewById(C4558R.C4560id.line12Btn);
        this.mLine12.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateLineView.this.mListeners != null) {
                    AnnotateLineView.this.mListeners.onAnnoWidthChanged(12);
                }
                AnnotateLineView annotateLineView = AnnotateLineView.this;
                annotateLineView.updateSelection(annotateLineView.mLine8);
            }
        });
        addView(inflate);
    }

    public void registerUpdateListener(IDrawingViewListener iDrawingViewListener) {
        this.mListeners = iDrawingViewListener;
    }

    public void show(View view) {
        if (this.popupWindow == null) {
            measure(0, 0);
            this.popupWindow = new PopupWindow(this, getMeasuredWidth(), getMeasuredHeight());
            this.popupWindow.setBackgroundDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_corner_bg_white_gray_50));
            this.popupWindow.setFocusable(true);
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setOnDismissListener(new OnDismissListener() {
                public void onDismiss() {
                    AnnotateLineView.this.mListeners.onAnnoWidthChanged(0);
                }
            });
        }
        if (!this.popupWindow.isShowing()) {
            PopupWindowCompat.showAsDropDown(this.popupWindow, view, (view.getWidth() - getMeasuredWidth()) / 2, 0, GravityCompat.START);
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                this.mLine2.sendAccessibilityEvent(8);
            }
            updateSelection(null);
        }
    }

    public boolean dismiss() {
        PopupWindow popupWindow2 = this.popupWindow;
        if (popupWindow2 != null) {
            popupWindow2.dismiss();
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void updateSelection(View view) {
        boolean z;
        if (view == null) {
            int lineWidth = AnnoDataMgr.getInstance().getLineWidth(AnnoToolType.ANNO_TOOL_TYPE_PEN);
            if (2 == lineWidth) {
                view = this.mLine2;
                z = false;
            } else if (4 == lineWidth) {
                view = this.mLine4;
                z = false;
            } else if (8 == lineWidth) {
                view = this.mLine8;
                z = false;
            } else if (12 == lineWidth) {
                view = this.mLine12;
                z = false;
            } else {
                z = false;
            }
        } else {
            z = true;
        }
        if (view != null) {
            this.mLine2.setSelected(false);
            this.mLine4.setSelected(false);
            this.mLine8.setSelected(false);
            this.mLine12.setSelected(false);
            view.setSelected(true);
        }
        if (z) {
            dismiss();
        }
    }
}
