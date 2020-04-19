package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMUpArrowView */
public class ZMUpArrowView extends View {
    private int mArrowWidth;
    private int mDividerHeight;
    private int mLeftDelta;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    public ZMUpArrowView(Context context) {
        super(context);
        init(context, null);
    }

    public ZMUpArrowView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ZMUpArrowView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, @Nullable AttributeSet attributeSet) {
        int color = getResources().getColor(C4409R.color.zm_listview_divider);
        this.mDividerHeight = UIUtil.dip2px(context, 1.0f);
        this.mArrowWidth = UIUtil.dip2px(context, 12.0f);
        this.mLeftDelta = UIUtil.dip2px(context, 24.0f);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMUpArrowView);
            color = obtainStyledAttributes.getColor(C4409R.styleable.ZMUpArrowView_zm_up_arrow_divider_color, color);
            this.mDividerHeight = (int) obtainStyledAttributes.getDimension(C4409R.styleable.ZMUpArrowView_zm_up_arrow_divider_height, (float) this.mDividerHeight);
            this.mArrowWidth = (int) obtainStyledAttributes.getDimension(C4409R.styleable.ZMUpArrowView_zm_up_arrow_width, (float) this.mArrowWidth);
            this.mLeftDelta = (int) obtainStyledAttributes.getDimension(C4409R.styleable.ZMUpArrowView_zm_up_arrow_left_delta, (float) this.mLeftDelta);
            obtainStyledAttributes.recycle();
        }
        this.mPaint.setColor(color);
        this.mPaint.setStrokeWidth((float) this.mDividerHeight);
        this.mPaint.setStyle(Style.STROKE);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPath.reset();
        int width = getWidth();
        float height = (float) (getHeight() - this.mDividerHeight);
        this.mPath.moveTo(0.0f, height);
        this.mPath.lineTo((float) (this.mLeftDelta - (this.mArrowWidth / 2)), height);
        this.mPath.lineTo((float) this.mLeftDelta, 0.0f);
        this.mPath.lineTo((float) (this.mLeftDelta + (this.mArrowWidth / 2)), height);
        this.mPath.lineTo((float) width, height);
        canvas.drawPath(this.mPath, this.mPaint);
    }

    public void setmLeftDelta(int i) {
        this.mLeftDelta = i;
        postInvalidate();
    }
}
