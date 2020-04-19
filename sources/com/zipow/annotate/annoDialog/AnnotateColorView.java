package com.zipow.annotate.annoDialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import androidx.core.widget.PopupWindowCompat;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.videobox.share.IColorChangedListener;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateColorView extends FrameLayout implements OnClickListener {
    private AnnoDataMgr mAnnoDataMgr;
    private TextView mBlackColor;
    private TextView mBlueColor;
    private int mCurColor;
    private TextView mGreenColor;
    private IColorChangedListener mListeners;
    private TextView mRedColor;
    private TextView mYellowColor;
    private PopupWindow popupWindow;

    public AnnotateColorView(Context context) {
        super(context);
        init();
    }

    public AnnotateColorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.mAnnoDataMgr = AnnoDataMgr.getInstance();
        View inflate = inflate(getContext(), C4558R.layout.zm_anno_color_tip, null);
        this.mBlackColor = (TextView) inflate.findViewById(C4558R.C4560id.id_wb_black_color_tip);
        this.mRedColor = (TextView) inflate.findViewById(C4558R.C4560id.id_wb_red_color_tip);
        this.mYellowColor = (TextView) inflate.findViewById(C4558R.C4560id.id_wb_yellow_color_tip);
        this.mGreenColor = (TextView) inflate.findViewById(C4558R.C4560id.id_wb_green_color_tip);
        this.mBlueColor = (TextView) inflate.findViewById(C4558R.C4560id.id_wb_blue_color_tip);
        this.mBlackColor.setOnClickListener(this);
        this.mRedColor.setOnClickListener(this);
        this.mYellowColor.setOnClickListener(this);
        this.mGreenColor.setOnClickListener(this);
        this.mBlueColor.setOnClickListener(this);
        addView(inflate);
    }

    public void registerUpdateListener(IColorChangedListener iColorChangedListener) {
        if (iColorChangedListener != null) {
            this.mListeners = iColorChangedListener;
        }
    }

    public void setCurColor(int i) {
        this.mCurColor = i;
        updateSelection();
    }

    public void show(View view) {
        if (this.popupWindow == null) {
            measure(0, 0);
            this.popupWindow = new PopupWindow(this, getMeasuredWidth(), getMeasuredHeight());
            this.popupWindow.setBackgroundDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_corner_bg_white_gray_50));
            this.popupWindow.setFocusable(true);
            this.popupWindow.setOutsideTouchable(true);
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                this.mBlackColor.sendAccessibilityEvent(8);
            }
        }
        if (!this.popupWindow.isShowing()) {
            PopupWindowCompat.showAsDropDown(this.popupWindow, view, (view.getWidth() - getMeasuredWidth()) / 2, 0, GravityCompat.START);
        }
    }

    public boolean dismiss() {
        PopupWindow popupWindow2 = this.popupWindow;
        if (popupWindow2 != null) {
            popupWindow2.dismiss();
        }
        return false;
    }

    public void onClick(View view) {
        setColorSelected(false, view);
        int colorByIndex = this.mAnnoDataMgr.getColorByIndex(0);
        if (view == this.mBlackColor) {
            colorByIndex = this.mAnnoDataMgr.getColorByIndex(0);
        } else if (view == this.mRedColor) {
            colorByIndex = this.mAnnoDataMgr.getColorByIndex(1);
        } else if (view == this.mYellowColor) {
            colorByIndex = this.mAnnoDataMgr.getColorByIndex(2);
        } else if (view == this.mGreenColor) {
            colorByIndex = this.mAnnoDataMgr.getColorByIndex(3);
        } else if (view == this.mBlueColor) {
            colorByIndex = this.mAnnoDataMgr.getColorByIndex(4);
        }
        IColorChangedListener iColorChangedListener = this.mListeners;
        if (iColorChangedListener != null) {
            iColorChangedListener.onColorPicked(colorByIndex);
        }
        dismiss();
    }

    private void updateSelection() {
        TextView textView = this.mCurColor == this.mAnnoDataMgr.getColorByIndex(0) ? this.mBlackColor : this.mCurColor == this.mAnnoDataMgr.getColorByIndex(1) ? this.mRedColor : this.mCurColor == this.mAnnoDataMgr.getColorByIndex(2) ? this.mYellowColor : this.mCurColor == this.mAnnoDataMgr.getColorByIndex(3) ? this.mGreenColor : this.mCurColor == this.mAnnoDataMgr.getColorByIndex(4) ? this.mBlueColor : null;
        if (textView == null) {
            textView = this.mBlackColor;
        }
        setColorSelected(false, textView);
    }

    private void setColorSelected(boolean z, View view) {
        this.mBlackColor.setSelected(z);
        this.mRedColor.setSelected(z);
        this.mYellowColor.setSelected(z);
        this.mGreenColor.setSelected(z);
        this.mBlueColor.setSelected(z);
        if (view != null) {
            view.setSelected(true);
        }
    }
}
