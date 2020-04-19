package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ToolbarDragView extends RelativeLayout {
    @Nullable
    private GestureDetector mGD;
    @Nullable
    private ToolbarScrollListener mListener;

    public static class ToolbarScrollListener extends SimpleOnGestureListener {
        public void onTouchEventUp() {
        }
    }

    @RequiresApi(api = 21)
    public ToolbarDragView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public ToolbarDragView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ToolbarDragView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ToolbarDragView(Context context) {
        super(context);
    }

    public void setGestureDetectorListener(@Nullable ToolbarScrollListener toolbarScrollListener) {
        this.mListener = toolbarScrollListener;
        if (toolbarScrollListener == null) {
            this.mGD = null;
            return;
        }
        this.mGD = new GestureDetector(getContext(), toolbarScrollListener);
        this.mGD.setIsLongpressEnabled(false);
    }

    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        if (this.mGD == null) {
            return super.onTouchEvent(motionEvent);
        }
        if (this.mListener != null && motionEvent.getAction() == 1) {
            this.mListener.onTouchEventUp();
        }
        return this.mGD.onTouchEvent(motionEvent);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        GestureDetector gestureDetector = this.mGD;
        if (gestureDetector != null) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }
}
