package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;
import p021us.zoom.androidlib.util.CaptionStyleCompat;
import p021us.zoom.androidlib.util.CaptionUtil;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.CaptionTextView */
public class CaptionTextView extends TextView {
    private Paint mBgPaint;
    private RectF mBgRect;
    private int mShadowRadius;
    private int mTwoDpInPx;

    public CaptionTextView(Context context) {
        super(context);
        initView(context);
    }

    public CaptionTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public CaptionTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @SuppressLint({"NewApi"})
    public CaptionTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        context.getResources().getDisplayMetrics();
        this.mTwoDpInPx = UIUtil.dip2px(context, 2.0f);
        this.mShadowRadius = this.mTwoDpInPx;
        if (isInEditMode()) {
            setTextSize(16.0f);
            setBackgroundColor(-16777216);
            setTextColor(-1);
            return;
        }
        initCaptionInfo();
    }

    private void refresh() {
        initCaptionInfo();
    }

    private void initCaptionInfo() {
        setTextSize(CaptionUtil.getCaptionFontScale(getContext()) * 16.0f);
        Locale locale = CaptionUtil.getLocale(getContext());
        if (locale != null && VERSION.SDK_INT >= 17) {
            setLocaleV17(locale);
        }
        CaptionStyleCompat captionStyle = CaptionUtil.getCaptionStyle(getContext());
        if (captionStyle.typeface != null) {
            setTypeface(captionStyle.typeface);
        }
        setTextColor(captionStyle.foregroundColor);
    }

    @TargetApi(17)
    private void setLocaleV17(Locale locale) {
        setTextLocale(locale);
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        refresh();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            super.onDraw(canvas);
            return;
        }
        ColorStateList textColors = getTextColors();
        CaptionStyleCompat captionStyle = CaptionUtil.getCaptionStyle(getContext());
        TextPaint paint = getPaint();
        paint.setStyle(Style.FILL);
        paint.setColor(captionStyle.foregroundColor);
        boolean z = false;
        setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        if (this.mBgPaint == null) {
            this.mBgPaint = new Paint();
        }
        this.mBgPaint.setStyle(Style.FILL);
        this.mBgPaint.setColor(captionStyle.backgroundColor);
        this.mBgPaint.setAntiAlias(true);
        if (this.mBgRect == null) {
            this.mBgRect = new RectF();
        }
        RectF rectF = this.mBgRect;
        rectF.left = 0.0f;
        rectF.top = 0.0f;
        rectF.right = (float) getWidth();
        this.mBgRect.bottom = (float) getHeight();
        float dip2px = (float) UIUtil.dip2px(getContext(), 5.0f);
        canvas.drawRoundRect(this.mBgRect, dip2px, dip2px, this.mBgPaint);
        switch (captionStyle.edgeType) {
            case 1:
                paint.setStyle(Style.STROKE);
                paint.setStrokeJoin(Join.ROUND);
                paint.setStrokeMiter(10.0f);
                paint.setStrokeWidth((float) UIUtil.dip2px(getContext(), 2.0f));
                setTextColor(captionStyle.edgeColor);
                super.onDraw(canvas);
                paint.setStyle(Style.FILL);
                setTextColor(textColors);
                super.onDraw(canvas);
                break;
            case 2:
                int i = this.mShadowRadius;
                float f = (float) i;
                setShadowLayer((float) i, f, f, captionStyle.edgeColor);
                super.onDraw(canvas);
                break;
            case 3:
            case 4:
                if (captionStyle.edgeType == 3) {
                    z = true;
                }
                float f2 = ((float) (z ? this.mShadowRadius : -this.mShadowRadius)) / 2.0f;
                setShadowLayer((float) this.mShadowRadius, f2, f2, captionStyle.edgeColor);
                super.onDraw(canvas);
                break;
            default:
                super.onDraw(canvas);
                break;
        }
    }
}
