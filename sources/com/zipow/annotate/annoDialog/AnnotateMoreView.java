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
import com.zipow.videobox.share.IDrawingViewListener;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMCheckedTextView;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateMoreView extends FrameLayout {
    /* access modifiers changed from: private */
    public IDrawingViewListener mListeners;
    private View mNewPageView;
    private View mNewWhiteboardSeparator;
    private TextView mPageCount;
    private View mPageNumView;
    private View mSaveSeparator;
    /* access modifiers changed from: private */
    public ZMCheckedTextView mShapeRecogniton;
    private int mTotalPageNum = 1;
    private PopupWindow popupWindow;

    public AnnotateMoreView(@NonNull Context context) {
        super(context);
        init();
    }

    public AnnotateMoreView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public AnnotateMoreView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View inflate = inflate(getContext(), C4558R.layout.zm_anno_more_tip, null);
        this.mShapeRecogniton = (ZMCheckedTextView) inflate.findViewById(C4558R.C4560id.id_more_tip_shapeRecognition);
        this.mShapeRecogniton.setChecked(isShapeRecognitionChecked());
        inflate.findViewById(C4558R.C4560id.item_smart_recognition).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnnotateMoreView.this.mShapeRecogniton.setChecked(!AnnotateMoreView.this.mShapeRecogniton.isChecked());
                if (AnnotateMoreView.this.mListeners != null) {
                    AnnotateMoreView.this.mListeners.onShapeRecognitionChecked(AnnotateMoreView.this.mShapeRecogniton.isChecked());
                }
                PreferenceUtil.saveIntValue(PreferenceUtil.ANNOTATE_SHAPE_RECOGNITION, AnnotateMoreView.this.mShapeRecogniton.isChecked() ? 1 : 0);
            }
        });
        inflate.findViewById(C4558R.C4560id.item_save_white_board).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateMoreView.this.mListeners != null) {
                    AnnotateMoreView.this.mListeners.onSaveWbClicked();
                }
            }
        });
        this.mNewPageView = inflate.findViewById(C4558R.C4560id.item_new_white_board);
        this.mNewPageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateMoreView.this.mListeners != null) {
                    AnnotateMoreView.this.mListeners.onNewPageClicked();
                }
            }
        });
        this.mPageNumView = inflate.findViewById(C4558R.C4560id.item_view_all_white_board);
        this.mPageNumView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnotateMoreView.this.mListeners != null) {
                    AnnotateMoreView.this.mListeners.onPageManagementClicked();
                }
            }
        });
        this.mPageCount = (TextView) inflate.findViewById(C4558R.C4560id.id_more_tip_page_num);
        this.mPageCount.setText(String.valueOf(this.mTotalPageNum));
        this.mNewWhiteboardSeparator = inflate.findViewById(C4558R.C4560id.id_newWhiteboard_separator);
        this.mSaveSeparator = inflate.findViewById(C4558R.C4560id.id_more_tip_view_all_wb_separator);
        addView(inflate);
        checkVisibility();
    }

    public void registerUpdateListener(IDrawingViewListener iDrawingViewListener) {
        this.mListeners = iDrawingViewListener;
    }

    public void show(View view) {
        checkVisibility();
        if (this.popupWindow == null) {
            measure(0, 0);
            this.popupWindow = new PopupWindow(this, getMeasuredWidth(), getMeasuredHeight());
            this.popupWindow.setBackgroundDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_corner_bg_white_gray));
            this.popupWindow.setFocusable(true);
            this.popupWindow.setOutsideTouchable(true);
        } else {
            measure(0, 0);
            this.popupWindow.setWidth(getMeasuredWidth());
            this.popupWindow.setHeight(getMeasuredHeight());
        }
        if (!this.popupWindow.isShowing()) {
            PopupWindowCompat.showAsDropDown(this.popupWindow, view, (view.getWidth() - getMeasuredWidth()) / 2, 0, GravityCompat.START);
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                this.mShapeRecogniton.sendAccessibilityEvent(8);
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

    public boolean isShapeRecognitionChecked() {
        return PreferenceUtil.readIntValue(PreferenceUtil.ANNOTATE_SHAPE_RECOGNITION, -1) == 1;
    }

    public void onWBPageNumChanged(int i) {
        this.mTotalPageNum = i;
        TextView textView = this.mPageCount;
        if (textView != null) {
            textView.setText(String.valueOf(this.mTotalPageNum));
        }
        checkVisibility();
    }

    public void checkVisibility() {
        boolean isTablet = UIUtil.isTablet(getContext());
        boolean isLandscapeMode = UIUtil.isLandscapeMode(getContext());
        View view = this.mNewPageView;
        int i = 8;
        if (view != null) {
            view.setVisibility((!isLandscapeMode || !isTablet) ? 0 : 8);
        }
        View view2 = this.mNewWhiteboardSeparator;
        if (view2 != null) {
            view2.setVisibility((!isLandscapeMode || !isTablet) ? 0 : 8);
        }
        View view3 = this.mPageNumView;
        if (view3 != null) {
            view3.setVisibility(((!isLandscapeMode || !isTablet) && this.mTotalPageNum > 1) ? 0 : 8);
        }
        View view4 = this.mSaveSeparator;
        if (view4 != null) {
            if ((!isLandscapeMode || !isTablet) && this.mTotalPageNum > 1) {
                i = 0;
            }
            view4.setVisibility(i);
        }
    }
}
