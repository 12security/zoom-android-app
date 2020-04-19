package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMTip */
public class ZMTip extends LinearLayout {
    public static final int ARROW_DIRECTION_DOWN = 3;
    public static final int ARROW_DIRECTION_LEFT = 0;
    public static final int ARROW_DIRECTION_NONE = -1;
    public static final int ARROW_DIRECTION_RIGHT = 2;
    public static final int ARROW_DIRECTION_UP = 1;
    public static final int LAYOUT_GRAVITY_BOTTOM = 3;
    public static final int LAYOUT_GRAVITY_LEFT = 0;
    public static final int LAYOUT_GRAVITY_NONE = -1;
    public static final int LAYOUT_GRAVITY_RIGHT = 1;
    public static final int LAYOUT_GRAVITY_TOP = 2;
    public static final int LAYOUT_OVERLYING_FROM_BOTTOM = 1;
    public static final int LAYOUT_OVERLYING_FROM_CENTER = 0;
    public static final int LAYOUT_OVERLYING_FROM_TOP = 2;
    private View mAnchor;
    private int mArcSize;
    private int mArrowDir = -1;
    private int mArrowHeight = 0;
    private int mArrowWidth = 0;
    private int mBorderMarginBottom = 0;
    private int mBorderMarginLeft = 0;
    private int mBorderMarginRight = 0;
    private int mBorderMarginTop = 0;
    private int mClrBackground;
    private int mClrBorder;
    private int mClrShadow;
    private int mDistanceToAnchor = 0;
    private Drawable mDrawableBackground;
    private int mGravity = -1;
    private int mGravityPadding = 0;
    private int mOverlyingType = 0;
    private Paint mPaint;
    private int mPreferredX;
    private int mPreferredY;
    private int mShadowDx;
    private int mShadowDy;
    private float mShadowRadius = 0.0f;
    private float mStrokeWidth;
    private boolean mbPreferredPosSetted = false;

    public ZMTip(Context context) {
        super(context);
        init(context, null);
    }

    public ZMTip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mPaint = new Paint();
        this.mPaint.setColor(-536870912);
        this.mPaint.setAntiAlias(true);
        setWillNotDraw(false);
        setWillNotCacheDrawing(true);
        this.mArrowWidth = UIUtil.dip2px(context, 16.0f);
        this.mArrowHeight = this.mArrowWidth / 2;
        this.mStrokeWidth = (float) UIUtil.dip2px(context, 1.0f);
        this.mArcSize = UIUtil.dip2px(context, 6.0f);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C4409R.styleable.ZMTip, C4409R.attr.zm_tipAppearance, 0);
        TypedValue typedValue = new TypedValue();
        obtainStyledAttributes.getValue(C4409R.styleable.ZMTip_zm_background, typedValue);
        if (typedValue.type == 1 || typedValue.type == 3) {
            this.mDrawableBackground = obtainStyledAttributes.getDrawable(C4409R.styleable.ZMTip_zm_background);
            this.mClrBackground = obtainStyledAttributes.getColor(C4409R.styleable.ZMTip_zm_backgroundColorIfHardwareAccelerated, -522725417);
        } else {
            this.mClrBackground = obtainStyledAttributes.getColor(C4409R.styleable.ZMTip_zm_background, -522725417);
        }
        this.mClrBorder = obtainStyledAttributes.getColor(C4409R.styleable.ZMTip_zm_borderColor, -520093697);
        setShadow(4.0f, 0, 2, obtainStyledAttributes.getColor(C4409R.styleable.ZMTip_zm_shadowColor, -13610096));
        obtainStyledAttributes.recycle();
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    public void setBackgroundColor(int i) {
        this.mClrBackground = i;
        this.mDrawableBackground = null;
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public int getBackgroundColor() {
        return this.mClrBackground;
    }

    public int getLayoutGravity() {
        return this.mGravity;
    }

    public int getLayoutGravityPadding() {
        return this.mGravityPadding;
    }

    public void setLayoutGravity(int i, int i2) {
        this.mGravity = i;
        this.mGravityPadding = i2;
    }

    public int getOverlyingType() {
        return this.mOverlyingType;
    }

    public void setOverlyingType(int i) {
        this.mOverlyingType = i;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        this.mDrawableBackground = drawable;
        this.mClrBackground = 0;
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public Drawable getBackgroundDrawable() {
        return this.mDrawableBackground;
    }

    public void setBorderColor(int i) {
        this.mClrBorder = i;
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public int getBorderColor() {
        return this.mClrBorder;
    }

    public void setShadow(float f, int i, int i2, int i3) {
        this.mShadowRadius = f;
        this.mClrShadow = i3;
        this.mShadowDx = i;
        this.mShadowDy = i2;
        updatePaddings();
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public void setShadowColor(int i) {
        this.mClrShadow = i;
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public int getShadowColor() {
        return this.mClrShadow;
    }

    public void setCornerArcSize(int i) {
        this.mArcSize = i;
        updatePaddings();
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public int getCornerArcSize() {
        return this.mArcSize;
    }

    public void setArrowSize(int i, int i2) {
        this.mArrowWidth = i;
        this.mArrowHeight = i2;
        updatePaddings();
    }

    public int getArrowWidth() {
        return this.mArrowWidth;
    }

    public int getArrowHeight() {
        return this.mArrowHeight;
    }

    public void setAnchor(View view, int i) {
        if (this.mAnchor != view) {
            this.mAnchor = view;
            this.mArrowDir = i;
            updatePaddings();
        }
    }

    public void setDistanceToAnchor(int i) {
        this.mDistanceToAnchor = i;
        if (getVisibility() == 0 && (getParent() instanceof ZMTipLayer)) {
            getParent().requestLayout();
        }
    }

    public int getDistanceToAnchor() {
        return this.mDistanceToAnchor;
    }

    private void updatePaddings() {
        if (getVisibility() == 0 && (getParent() instanceof ZMTipLayer)) {
            getParent().requestLayout();
        }
        float f = this.mShadowRadius;
        int i = this.mShadowDx;
        this.mBorderMarginLeft = (int) (f - ((float) i));
        int i2 = this.mShadowDy;
        this.mBorderMarginTop = (int) (f - ((float) i2));
        this.mBorderMarginRight = (int) (((float) i) + f);
        this.mBorderMarginBottom = (int) (f + ((float) i2));
        float f2 = this.mStrokeWidth;
        int i3 = ((int) f2) + this.mBorderMarginLeft;
        int i4 = ((int) f2) + this.mBorderMarginTop;
        int i5 = ((int) f2) + this.mBorderMarginRight;
        int i6 = ((int) f2) + this.mBorderMarginBottom;
        switch (this.mArrowDir) {
            case 0:
                int i7 = this.mArrowHeight;
                if (i7 < i3) {
                    i3 += i7;
                    break;
                } else {
                    this.mBorderMarginLeft = 0;
                    i3 = i7;
                    break;
                }
            case 1:
                int i8 = this.mArrowHeight;
                if (i8 < i4) {
                    i4 += i8;
                    break;
                } else {
                    this.mBorderMarginTop = 0;
                    i4 = i8;
                    break;
                }
            case 2:
                int i9 = this.mArrowHeight;
                if (i9 < i5) {
                    i5 += i9;
                    break;
                } else {
                    this.mBorderMarginRight = 0;
                    i5 = i9;
                    break;
                }
            case 3:
                int i10 = this.mArrowHeight;
                if (i10 < i6) {
                    i6 += i10;
                    break;
                } else {
                    this.mBorderMarginBottom = 0;
                    i6 = i10;
                    break;
                }
        }
        setPadding(i3, i4, i5, i6);
    }

    public View getAnchor() {
        return this.mAnchor;
    }

    public int getArrowDirection() {
        return this.mArrowDir;
    }

    public void setPreferredPosition(int i, int i2) {
        this.mPreferredX = i;
        this.mPreferredY = i2;
        this.mbPreferredPosSetted = true;
    }

    public void resetPreferredPosition() {
        this.mPreferredX = 0;
        this.mPreferredY = 0;
        this.mbPreferredPosSetted = false;
    }

    public int getPreferredX() {
        return this.mPreferredX;
    }

    public int getPreferredY() {
        return this.mPreferredY;
    }

    public boolean isPreferredPositionSetted() {
        return this.mbPreferredPosSetted;
    }

    public void show(ZMTipLayer zMTipLayer) {
        if (zMTipLayer.indexOfChild(this) < 0) {
            zMTipLayer.addView(this);
        }
        zMTipLayer.requestLayout();
        if (getVisibility() == 0) {
            startAnimation(AnimationUtils.loadAnimation(getContext(), C4409R.anim.zm_tip_fadein));
        }
    }

    public void dismiss() {
        ZMTipLayer zMTipLayer = (ZMTipLayer) getParent();
        if (zMTipLayer != null) {
            zMTipLayer.removeView(this);
            zMTipLayer.requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int arrowDirection = getArrowDirection();
        View view = this.mAnchor;
        Rect absoluteRect = view != null ? UIUtil.getAbsoluteRect(view) : null;
        Rect absoluteRect2 = UIUtil.getAbsoluteRect(this);
        if (absoluteRect != null) {
            absoluteRect.offset(-absoluteRect2.left, -absoluteRect2.top);
        }
        Path path = new Path();
        if (absoluteRect == null || arrowDirection != 0) {
            i = this.mBorderMarginLeft;
            if (arrowDirection == 3) {
                path.moveTo((float) i, (float) (((getHeight() - this.mBorderMarginBottom) - this.mArrowHeight) - this.mArcSize));
            } else {
                path.moveTo((float) i, (float) ((getHeight() - this.mBorderMarginBottom) - this.mArcSize));
            }
            if (arrowDirection == 1) {
                path.lineTo((float) i, (float) (this.mBorderMarginTop + this.mArrowHeight + this.mArcSize));
                i2 = this.mBorderMarginTop + this.mArrowHeight;
            } else {
                path.lineTo((float) i, (float) (this.mBorderMarginTop + this.mArcSize));
                i2 = this.mBorderMarginTop;
            }
        } else {
            int i9 = (absoluteRect.top + absoluteRect.bottom) / 2;
            i = this.mBorderMarginLeft + this.mArrowHeight;
            float f = (float) i;
            path.moveTo(f, (float) ((getHeight() - this.mBorderMarginBottom) - this.mArcSize));
            path.lineTo(f, (float) ((this.mArrowWidth / 2) + i9));
            path.lineTo((float) this.mBorderMarginLeft, (float) i9);
            path.lineTo(f, (float) (i9 - (this.mArrowWidth / 2)));
            path.lineTo(f, (float) (this.mBorderMarginTop + this.mArcSize));
            i2 = this.mBorderMarginTop;
        }
        path.arcTo(getArcOval(i, i2), 180.0f, 90.0f);
        if (absoluteRect == null || arrowDirection != 1) {
            i3 = this.mBorderMarginTop;
            if (arrowDirection == 2) {
                path.lineTo((float) (((getWidth() - this.mBorderMarginRight) - this.mArrowHeight) - this.mArcSize), (float) i3);
                i4 = ((getWidth() - this.mBorderMarginRight) - this.mArrowHeight) - (this.mArcSize * 2);
            } else {
                path.lineTo((float) ((getWidth() - this.mBorderMarginRight) - this.mArcSize), (float) i3);
                i4 = (getWidth() - this.mBorderMarginRight) - (this.mArcSize * 2);
            }
        } else {
            int i10 = (absoluteRect.left + absoluteRect.right) / 2;
            i3 = this.mBorderMarginTop + this.mArrowHeight;
            float f2 = (float) i3;
            path.lineTo((float) (i10 - (this.mArrowWidth / 2)), f2);
            path.lineTo((float) i10, (float) this.mBorderMarginTop);
            path.lineTo((float) (i10 + (this.mArrowWidth / 2)), f2);
            path.lineTo((float) ((getWidth() - this.mBorderMarginRight) - this.mArcSize), f2);
            i4 = (getWidth() - this.mBorderMarginRight) - (this.mArcSize * 2);
        }
        path.arcTo(getArcOval(i4, i3), 270.0f, 90.0f);
        if (absoluteRect == null || arrowDirection != 2) {
            int width = getWidth() - this.mBorderMarginRight;
            if (arrowDirection == 3) {
                path.lineTo((float) width, (float) (((getHeight() - this.mBorderMarginBottom) - this.mArrowHeight) - this.mArcSize));
                i5 = ((getHeight() - this.mBorderMarginBottom) - this.mArrowHeight) - (this.mArcSize * 2);
            } else {
                path.lineTo((float) width, (float) ((getHeight() - this.mBorderMarginBottom) - this.mArcSize));
                i5 = (getHeight() - this.mBorderMarginBottom) - (this.mArcSize * 2);
            }
            i6 = width - (this.mArcSize * 2);
        } else {
            int i11 = (absoluteRect.top + absoluteRect.bottom) / 2;
            int width2 = (getWidth() - this.mBorderMarginRight) - this.mArrowHeight;
            float f3 = (float) width2;
            path.lineTo(f3, (float) (i11 - (this.mArrowWidth / 2)));
            path.lineTo((float) (getWidth() - this.mBorderMarginRight), (float) i11);
            path.lineTo(f3, (float) (i11 + (this.mArrowWidth / 2)));
            path.lineTo(f3, (float) ((getHeight() - this.mBorderMarginBottom) - this.mArcSize));
            int i12 = width2 - (this.mArcSize * 2);
            i5 = (getHeight() - this.mBorderMarginBottom) - (this.mArcSize * 2);
            i6 = i12;
        }
        path.arcTo(getArcOval(i6, i5), 0.0f, 90.0f);
        if (absoluteRect == null || arrowDirection != 3) {
            int height = getHeight() - this.mBorderMarginBottom;
            if (arrowDirection == 0) {
                path.lineTo((float) (this.mBorderMarginLeft + this.mArrowHeight + this.mArcSize), (float) height);
                i8 = this.mBorderMarginLeft + this.mArrowHeight;
            } else {
                path.lineTo((float) (this.mBorderMarginLeft + this.mArcSize), (float) height);
                i8 = this.mBorderMarginLeft;
            }
            i7 = height - (this.mArcSize * 2);
        } else {
            int i13 = (absoluteRect.left + absoluteRect.right) / 2;
            int height2 = (getHeight() - this.mBorderMarginBottom) - this.mArrowHeight;
            float f4 = (float) height2;
            path.lineTo((float) ((this.mArrowWidth / 2) + i13), f4);
            path.lineTo((float) i13, (float) (getHeight() - this.mBorderMarginBottom));
            path.lineTo((float) (i13 - (this.mArrowWidth / 2)), f4);
            path.lineTo((float) (this.mBorderMarginLeft + this.mArcSize), f4);
            i8 = this.mBorderMarginLeft;
            i7 = height2 - (this.mArcSize * 2);
        }
        path.arcTo(getArcOval(i8, i7), 90.0f, 90.0f);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setShadowLayer(this.mShadowRadius, (float) this.mShadowDx, (float) this.mShadowDy, this.mClrShadow);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setColor(this.mClrBorder);
        canvas.drawPath(path, this.mPaint);
        if (this.mDrawableBackground == null || _zmIsHardwareAccelerated()) {
            this.mPaint.setStyle(Style.FILL);
            this.mPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            this.mPaint.setStrokeWidth(0.0f);
            this.mPaint.setColor(this.mClrBackground);
            canvas.drawPath(path, this.mPaint);
        } else {
            this.mDrawableBackground.setBounds(0, 0, getWidth(), getHeight());
            canvas.clipPath(path);
            this.mDrawableBackground.draw(canvas);
        }
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setColor(this.mClrBorder);
        canvas.drawPath(path, this.mPaint);
    }

    @SuppressLint({"NewApi"})
    private boolean _zmIsHardwareAccelerated() {
        return isHardwareAccelerated();
    }

    private RectF getArcOval(int i, int i2) {
        RectF rectF = new RectF();
        rectF.left = (float) i;
        rectF.top = (float) i2;
        int i3 = this.mArcSize;
        rectF.right = (float) (i + (i3 * 2));
        rectF.bottom = (float) (i2 + (i3 * 2));
        return rectF;
    }
}
