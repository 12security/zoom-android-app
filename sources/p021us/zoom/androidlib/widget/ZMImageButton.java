package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/* renamed from: us.zoom.androidlib.widget.ZMImageButton */
public class ZMImageButton extends ImageButton {
    /* access modifiers changed from: private */
    public OnClickListener mOnClickListener;

    public ZMImageButton(Context context) {
        super(context);
    }

    public ZMImageButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ZMImageButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        super.setOnClickListener(new OnClickListener() {
            private long mLastClickTime = 0;

            public void onClick(View view) {
                if (ZMImageButton.this.mOnClickListener != null) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    if (elapsedRealtime - this.mLastClickTime > 500) {
                        ZMImageButton.this.mOnClickListener.onClick(view);
                    }
                    this.mLastClickTime = elapsedRealtime;
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
