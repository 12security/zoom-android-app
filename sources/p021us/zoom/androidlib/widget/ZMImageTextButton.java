package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMImageTextButton */
public class ZMImageTextButton extends Button {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int mImage;
    private int mImageTextOrientation = 0;
    /* access modifiers changed from: private */
    public OnClickListener mOnClickListener;

    public ZMImageTextButton(Context context) {
        super(context);
    }

    public ZMImageTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ZMImageTextButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (context != null && attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMImageTextButton);
            int i = obtainStyledAttributes.getInt(C4409R.styleable.ZMImageTextButton_zmImageTextOrientation, -1);
            if (i >= 0) {
                setImageTextOrientation(i);
            }
            int resourceId = obtainStyledAttributes.getResourceId(C4409R.styleable.ZMImageTextButton_zm_image, -1);
            if (resourceId != -1) {
                setImageResource(resourceId);
            }
            obtainStyledAttributes.recycle();
            super.setOnClickListener(new OnClickListener() {
                private long mLastClickTime = 0;

                public void onClick(View view) {
                    if (ZMImageTextButton.this.mOnClickListener != null) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (elapsedRealtime - this.mLastClickTime > 500) {
                            ZMImageTextButton.this.mOnClickListener.onClick(view);
                        }
                        this.mLastClickTime = elapsedRealtime;
                    }
                }
            });
        }
    }

    public void setImageResource(int i) {
        this.mImage = i;
        if (isShown()) {
            requestLayout();
        }
    }

    public int getImage() {
        return this.mImage;
    }

    public void setImageTextOrientation(int i) {
        if (i == 0 || i == 1) {
            this.mImageTextOrientation = i;
        }
    }

    public int getImageTextOrientation() {
        return this.mImageTextOrientation;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mImage > 0) {
            if ("".equals(getText().toString())) {
                setBackgroundResource(this.mImage);
            } else {
                int i3 = this.mImageTextOrientation;
                if (i3 == 0) {
                    setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(this.mImage), null, null, null);
                } else if (i3 == 1) {
                    setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(this.mImage), null, null);
                }
            }
        }
        super.onMeasure(i, i2);
    }
}
