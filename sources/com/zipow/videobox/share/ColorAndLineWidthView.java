package com.zipow.videobox.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.widget.PopupWindowCompat;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.annotate.AnnoToolType;
import com.zipow.annotate.ShareScreenAnnoToolbar;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ColorAndLineWidthView extends FrameLayout implements OnClickListener {
    private static final String TAG = "com.zipow.videobox.share.ColorAndLineWidthView";
    private LayoutParams layoutParams;
    private View lineWidth_12;
    private View lineWidth_2;
    private View lineWidth_4;
    private View lineWidth_8;
    private AnnoDataMgr mAnnoDataMgr = AnnoDataMgr.getInstance();
    /* access modifiers changed from: private */
    public View mArrowUpView;
    /* access modifiers changed from: private */
    public View mArrowdownView;
    private ColorTable mColorTable;
    public int mHeight;
    public int mWidth;
    private WindowManager mWindowManager;
    private PopupWindow popupWindow;

    public ColorAndLineWidthView(@NonNull Context context) {
        super(context);
        init();
    }

    public ColorAndLineWidthView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        View inflate = inflate(getContext(), C4558R.layout.zm_annocolorlayout, null);
        this.mColorTable = (ColorTable) inflate.findViewById(C4558R.C4560id.colorTable);
        this.lineWidth_2 = inflate.findViewById(C4558R.C4560id.show_width_2);
        this.lineWidth_2.setOnClickListener(this);
        this.lineWidth_4 = inflate.findViewById(C4558R.C4560id.show_width_4);
        this.lineWidth_4.setOnClickListener(this);
        this.lineWidth_8 = inflate.findViewById(C4558R.C4560id.show_width_8);
        this.lineWidth_8.setOnClickListener(this);
        this.lineWidth_12 = inflate.findViewById(C4558R.C4560id.show_width_12);
        this.lineWidth_12.setOnClickListener(this);
        this.mArrowdownView = inflate.findViewById(C4558R.C4560id.show_arrow_down);
        this.mArrowUpView = inflate.findViewById(C4558R.C4560id.show_arrow_up);
        this.mArrowdownView.setVisibility(0);
        this.mArrowUpView.setVisibility(8);
        inflate.invalidate();
        addView(inflate);
        this.mWidth = UIUtil.dip2px(getContext(), 240.0f);
        this.mHeight = UIUtil.dip2px(getContext(), 182.0f);
    }

    public void setListener(IColorChangedListener iColorChangedListener) {
        this.mColorTable.setOnColorChangedListener(iColorChangedListener);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        this.lineWidth_2.setBackgroundResource(C4558R.color.zm_transparent);
        this.lineWidth_4.setBackgroundResource(C4558R.color.zm_transparent);
        this.lineWidth_8.setBackgroundResource(C4558R.color.zm_transparent);
        this.lineWidth_12.setBackgroundResource(C4558R.color.zm_transparent);
        if (id == C4558R.C4560id.show_width_2) {
            this.mAnnoDataMgr.setLineWidth(2);
        } else if (id == C4558R.C4560id.show_width_4) {
            this.mAnnoDataMgr.setLineWidth(4);
        } else if (id == C4558R.C4560id.show_width_8) {
            this.mAnnoDataMgr.setLineWidth(8);
        } else if (id == C4558R.C4560id.show_width_12) {
            this.mAnnoDataMgr.setLineWidth(12);
        }
        view.setBackgroundResource(C4558R.C4559drawable.zm_corner_bg_blue);
    }

    public void showAsPopupWindow() {
        this.popupWindow = new PopupWindow(this, this.mWidth, this.mHeight);
        this.popupWindow.setBackgroundDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_transparent));
        this.popupWindow.setFocusable(true);
        this.popupWindow.setOutsideTouchable(true);
    }

    public void showInWindowManager(@NonNull WindowManager windowManager) {
        this.mWindowManager = windowManager;
        this.layoutParams = new LayoutParams();
        this.layoutParams.type = ShareScreenAnnoToolbar.getWindowLayoutParamsType(getContext());
        this.layoutParams.flags |= 1320;
        LayoutParams layoutParams2 = this.layoutParams;
        layoutParams2.format = 1;
        layoutParams2.width = this.mWidth;
        layoutParams2.height = this.mHeight;
        windowManager.addView(this, layoutParams2);
        setVisibility(4);
    }

    public boolean isShowing() {
        PopupWindow popupWindow2 = this.popupWindow;
        if (popupWindow2 != null) {
            return popupWindow2.isShowing();
        }
        boolean z = false;
        if (this.mWindowManager == null) {
            return false;
        }
        if (getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    public void dismiss() {
        PopupWindow popupWindow2 = this.popupWindow;
        if (popupWindow2 != null) {
            popupWindow2.dismiss();
        } else if (this.mWindowManager != null) {
            setVisibility(4);
        }
    }

    public void show(@NonNull View view) {
        if (this.popupWindow != null) {
            PopupWindowCompat.showAsDropDown(this.popupWindow, view, (view.getWidth() - UIUtil.dip2px(getContext(), 240.0f)) / 2, 0, GravityCompat.START);
            updateSelection();
        }
    }

    public void show(@NonNull final View view, int i, int i2, final boolean z) {
        WindowManager windowManager = this.mWindowManager;
        if (windowManager != null) {
            LayoutParams layoutParams2 = this.layoutParams;
            layoutParams2.gravity = 53;
            layoutParams2.x = i;
            layoutParams2.y = i2;
            windowManager.updateViewLayout(this, layoutParams2);
            post(new Runnable() {
                public void run() {
                    View view;
                    if (z) {
                        ColorAndLineWidthView.this.mArrowUpView.setVisibility(0);
                        ColorAndLineWidthView.this.mArrowdownView.setVisibility(8);
                        view = ColorAndLineWidthView.this.mArrowUpView;
                    } else {
                        ColorAndLineWidthView.this.mArrowUpView.setVisibility(8);
                        ColorAndLineWidthView.this.mArrowdownView.setVisibility(0);
                        view = ColorAndLineWidthView.this.mArrowdownView;
                    }
                    int[] iArr = new int[2];
                    ColorAndLineWidthView.this.getLocationOnScreen(iArr);
                    int i = iArr[0];
                    view.getLocationOnScreen(iArr);
                    int width = ((iArr[0] - i) + (view.getWidth() / 2)) - (view.getWidth() / 2);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.gravity = 19;
                    layoutParams.leftMargin = width;
                    view.setLayoutParams(layoutParams);
                    ColorAndLineWidthView.this.setVisibility(0);
                }
            });
            updateSelection();
        }
    }

    private void updateSelection() {
        this.lineWidth_2.setBackgroundResource(C4558R.color.zm_transparent);
        this.lineWidth_4.setBackgroundResource(C4558R.color.zm_transparent);
        this.lineWidth_8.setBackgroundResource(C4558R.color.zm_transparent);
        this.lineWidth_12.setBackgroundResource(C4558R.color.zm_transparent);
        int lineWidth = this.mAnnoDataMgr.getLineWidth(AnnoToolType.ANNO_TOOL_TYPE_PEN);
        if (2 == lineWidth) {
            this.lineWidth_2.setBackgroundResource(C4558R.C4559drawable.zm_corner_bg_blue);
        } else if (4 == lineWidth) {
            this.lineWidth_4.setBackgroundResource(C4558R.C4559drawable.zm_corner_bg_blue);
        } else if (8 == lineWidth) {
            this.lineWidth_8.setBackgroundResource(C4558R.C4559drawable.zm_corner_bg_blue);
        } else if (12 == lineWidth) {
            this.lineWidth_12.setBackgroundResource(C4558R.C4559drawable.zm_corner_bg_blue);
        }
    }
}
