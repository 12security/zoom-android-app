package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMMaterialEditText */
public class ZMMaterialEditText extends EditText {
    private boolean enableLine = true;
    private PathEffect mDisablePathEffect;
    private int mLineDisableColor;
    private int mLineNormalColor;
    private int mLinefocusColor;

    public void setBackground(Drawable drawable) {
    }

    public void setBackgroundColor(int i) {
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable drawable) {
    }

    public void setBackgroundResource(int i) {
    }

    public ZMMaterialEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public ZMMaterialEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public ZMMaterialEditText(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attributeSet) {
        Resources resources = getResources();
        if (resources != null) {
            int dip2px = UIUtil.dip2px(getContext(), 4.0f);
            this.mDisablePathEffect = new DashPathEffect(new float[]{(float) (dip2px / 2), (float) dip2px}, 1.0f);
            super.setBackgroundColor(0);
            this.mLineNormalColor = resources.getColor(C4409R.color.zm_highlight);
            this.mLineDisableColor = resources.getColor(C4409R.color.zm_highlight_disabled);
            this.mLinefocusColor = resources.getColor(C4409R.color.zm_highlight);
            if (attributeSet != null) {
                TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C4409R.styleable.ZMMaterialEdt);
                this.mLineNormalColor = obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialEdt_zm_edtNormalColor, this.mLineNormalColor);
                this.mLineDisableColor = obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialEdt_zm_edtDisableColor, this.mLineDisableColor);
                this.mLinefocusColor = obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialEdt_zm_edtFocusColor, this.mLinefocusColor);
                obtainStyledAttributes.recycle();
            }
        }
    }

    public void setEnableLine(boolean z) {
        this.enableLine = z;
    }

    public int getLineNormalColor() {
        return this.mLineNormalColor;
    }

    public void setLineNormalColor(int i) {
        this.mLineNormalColor = i;
    }

    public int getLineDisableColor() {
        return this.mLineDisableColor;
    }

    public void setLineDisableColor(int i) {
        this.mLineDisableColor = i;
    }

    public int getLinefocusColor() {
        return this.mLinefocusColor;
    }

    public void setLinefocusColor(int i) {
        this.mLinefocusColor = i;
    }

    public void setTextAppearance(Context context, int i) {
        if (i != 0) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i, C4409R.styleable.ZMMaterialEdt);
            this.mLineNormalColor = obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialEdt_zm_edtNormalColor, this.mLineNormalColor);
            this.mLineDisableColor = obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialEdt_zm_edtDisableColor, this.mLineDisableColor);
            this.mLinefocusColor = obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialEdt_zm_edtFocusColor, this.mLinefocusColor);
            obtainStyledAttributes.recycle();
        }
        super.setTextAppearance(context, i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.enableLine) {
            TextPaint paint = getPaint();
            int dip2px = UIUtil.dip2px(getContext(), 3.0f);
            int i = this.mLineNormalColor;
            paint.setPathEffect(null);
            if (!isEnabled()) {
                i = this.mLineDisableColor;
                dip2px = UIUtil.dip2px(getContext(), 2.0f);
                paint.setPathEffect(this.mDisablePathEffect);
            } else if (isFocused()) {
                i = this.mLinefocusColor;
            }
            paint.setColor(i);
            paint.setStrokeWidth((float) dip2px);
            paint.setAntiAlias(true);
            float measuredHeight = (float) (getMeasuredHeight() + getScrollY());
            canvas.drawLine(0.0f, measuredHeight, (float) getMeasuredWidth(), measuredHeight, paint);
            paint.setPathEffect(null);
        }
    }
}
