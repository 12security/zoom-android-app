package com.zipow.videobox.view;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.Touch;
import android.text.style.ReplacementSpan;
import android.view.MotionEvent;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.widget.ZMEditText;

public class ZMReplaceSpanMovementMethod extends ArrowKeyMovementMethod {
    @Nullable
    private static ZMReplaceSpanMovementMethod mInstance;
    private float mDownX = -1.0f;
    private float mDownY = -1.0f;

    @NonNull
    public static ZMReplaceSpanMovementMethod getInstance() {
        if (mInstance == null) {
            mInstance = new ZMReplaceSpanMovementMethod();
        }
        return mInstance;
    }

    public boolean onTouchEvent(@Nullable final TextView textView, @Nullable Spannable spannable, @NonNull MotionEvent motionEvent) {
        if (textView == null || spannable == null) {
            return super.onTouchEvent(textView, spannable, motionEvent);
        }
        int x = (((int) motionEvent.getX()) - textView.getTotalPaddingLeft()) + textView.getScrollX();
        int y = (((int) motionEvent.getY()) - textView.getTotalPaddingTop()) + textView.getScrollY();
        Layout layout = textView.getLayout();
        float f = (float) x;
        int offsetForHorizontal = layout.getOffsetForHorizontal(layout.getLineForVertical(y), f);
        int i = layout.getPrimaryHorizontal(offsetForHorizontal) < f ? offsetForHorizontal + 1 : offsetForHorizontal - 1;
        ReplacementSpan[] replacementSpanArr = (ReplacementSpan[]) spannable.getSpans(i, i, ReplacementSpan.class);
        if (replacementSpanArr.length > 0) {
            final int spanEnd = spannable.getSpanEnd(replacementSpanArr[0]);
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mDownX = motionEvent.getX();
                this.mDownY = motionEvent.getY();
                textView.setCursorVisible(false);
            } else if (action == 1) {
                float x2 = motionEvent.getX();
                float y2 = motionEvent.getY();
                float f2 = this.mDownX;
                float f3 = (f2 - x2) * (f2 - x2);
                float f4 = this.mDownY;
                if (f3 + ((f4 - y2) * (f4 - y2)) < 80.0f) {
                    textView.getHandler().post(new Runnable() {
                        public void run() {
                            TextView textView = textView;
                            if (textView instanceof ZMEditText) {
                                ((ZMEditText) textView).setSelection(spanEnd);
                            }
                            textView.setCursorVisible(true);
                        }
                    });
                } else {
                    textView.setCursorVisible(true);
                }
                this.mDownX = -1.0f;
                this.mDownY = -1.0f;
            }
        }
        return Touch.onTouchEvent(textView, spannable, motionEvent);
    }
}
