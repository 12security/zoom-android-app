package com.zipow.annotate.annoDialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.core.widget.PopupWindowCompat;
import com.zipow.annotate.AnnotateDrawingView.AnnoClearType;
import com.zipow.videobox.share.IDrawingViewListener;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateClearView extends FrameLayout {
    TextView mClearAll;
    TextView mClearMy;
    TextView mClearOthers;
    /* access modifiers changed from: private */
    public IDrawingViewListener mListeners;
    private PopupWindow popupWindow;

    public AnnotateClearView(@NonNull Context context) {
        super(context);
        init();
    }

    public AnnotateClearView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public AnnotateClearView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View inflate = inflate(getContext(), C4558R.layout.zm_anno_clear_tip, null);
        this.mClearMy = (TextView) inflate.findViewById(C4558R.C4560id.id_clear_tip_clear_my);
        this.mClearMy.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateClearView.this.mListeners != null) {
                    AnnotateClearView.this.mListeners.onClearClicked(AnnoClearType.ANNO_CLEAR_MY);
                }
            }
        });
        this.mClearAll = (TextView) inflate.findViewById(C4558R.C4560id.id_clear_tip_clear_all);
        this.mClearAll.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateClearView.this.mListeners != null) {
                    AnnotateClearView.this.mListeners.onClearClicked(AnnoClearType.ANNO_CLEAR_ALL);
                }
            }
        });
        this.mClearOthers = (TextView) inflate.findViewById(C4558R.C4560id.id_clear_tip_clear_others);
        this.mClearOthers.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateClearView.this.mListeners != null) {
                    AnnotateClearView.this.mListeners.onClearClicked(AnnoClearType.ANNO_CLEAR_OTHERS);
                }
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
            this.popupWindow.setBackgroundDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_corner_bg_white_gray));
            this.popupWindow.setFocusable(true);
            this.popupWindow.setOutsideTouchable(true);
        }
        if (!this.popupWindow.isShowing()) {
            PopupWindowCompat.showAsDropDown(this.popupWindow, view, (view.getWidth() - getMeasuredWidth()) / 2, 0, GravityCompat.START);
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                this.mClearMy.sendAccessibilityEvent(8);
            }
        }
    }

    public boolean dismiss() {
        PopupWindow popupWindow2 = this.popupWindow;
        if (popupWindow2 != null) {
            popupWindow2.dismiss();
        }
        return false;
    }
}
