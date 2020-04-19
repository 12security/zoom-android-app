package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMSettingsCategory */
public class ZMSettingsCategory extends LinearLayout {
    private Drawable mBackground = null;
    private int mBackgroundColor = 0;
    private int mBottomDividerHeight = 1;
    private int mCenterDividerHeight = 1;
    private Drawable mDrawableBottomDivider = null;
    private Drawable mDrawableCenterDivider = null;
    private Drawable mDrawableTopDivider = null;
    private boolean mHasBackground = false;
    private int mMinItemHeight = 0;
    private int mResDrawableSettingsItemSelector = C4409R.C4410drawable.zm_setting_option_item_no_line;
    private int mTopDividerHeight = 1;
    private boolean mbShowBottomDivider = true;
    private boolean mbShowCenterDivider = true;
    private boolean mbShowTopDivider = true;

    public ZMSettingsCategory(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ZMSettingsCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet, 0);
    }

    @SuppressLint({"NewApi"})
    public ZMSettingsCategory(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, attributeSet, i);
    }

    private void initView(Context context, AttributeSet attributeSet, int i) {
        if (context != null) {
            int i2 = -1;
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C4409R.styleable.ZMSettingsCategory, C4409R.attr.zm_settingsCategoryAppearance, 0);
            if (obtainStyledAttributes != null) {
                this.mbShowTopDivider = obtainStyledAttributes.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showTopDivider, this.mbShowTopDivider);
                this.mbShowBottomDivider = obtainStyledAttributes.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showBottomDivider, this.mbShowBottomDivider);
                this.mbShowCenterDivider = obtainStyledAttributes.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showCenterDivider, this.mbShowCenterDivider);
                this.mResDrawableSettingsItemSelector = obtainStyledAttributes.getResourceId(C4409R.styleable.ZMSettingsCategory_zm_settingsItemSelector, this.mResDrawableSettingsItemSelector);
                this.mDrawableTopDivider = obtainStyledAttributes.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_topDivider);
                this.mDrawableCenterDivider = obtainStyledAttributes.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_centerDivider);
                this.mDrawableBottomDivider = obtainStyledAttributes.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_bottomDivider);
                this.mMinItemHeight = obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.ZMSettingsCategory_zm_seetingsItemMinHeight, this.mMinItemHeight);
                if (obtainStyledAttributes.hasValue(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground)) {
                    this.mHasBackground = true;
                    this.mBackground = obtainStyledAttributes.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground);
                    this.mBackgroundColor = obtainStyledAttributes.getColor(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground, this.mBackgroundColor);
                }
                i2 = obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.ZMSettingsCategory_zm_dividerHeight, -1);
                obtainStyledAttributes.recycle();
            }
            if (i > 0) {
                TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMSettingsCategory, i, 0);
                if (obtainStyledAttributes2 != null) {
                    this.mbShowTopDivider = obtainStyledAttributes2.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showTopDivider, this.mbShowTopDivider);
                    this.mbShowBottomDivider = obtainStyledAttributes2.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showBottomDivider, this.mbShowBottomDivider);
                    this.mbShowCenterDivider = obtainStyledAttributes2.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showCenterDivider, this.mbShowCenterDivider);
                    this.mResDrawableSettingsItemSelector = obtainStyledAttributes2.getResourceId(C4409R.styleable.ZMSettingsCategory_zm_settingsItemSelector, this.mResDrawableSettingsItemSelector);
                    this.mMinItemHeight = obtainStyledAttributes2.getDimensionPixelSize(C4409R.styleable.ZMSettingsCategory_zm_seetingsItemMinHeight, this.mMinItemHeight);
                    if (obtainStyledAttributes2.hasValue(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground)) {
                        this.mHasBackground = true;
                        this.mBackground = obtainStyledAttributes2.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground);
                        this.mBackgroundColor = obtainStyledAttributes2.getColor(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground, this.mBackgroundColor);
                    }
                    i2 = obtainStyledAttributes2.getDimensionPixelSize(C4409R.styleable.ZMSettingsCategory_zm_dividerHeight, i2);
                    Drawable drawable = obtainStyledAttributes2.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_topDivider);
                    if (drawable != null) {
                        this.mDrawableTopDivider = drawable;
                    }
                    Drawable drawable2 = obtainStyledAttributes2.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_centerDivider);
                    if (drawable2 != null) {
                        this.mDrawableCenterDivider = drawable2;
                    }
                    Drawable drawable3 = obtainStyledAttributes2.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_bottomDivider);
                    if (drawable3 != null) {
                        this.mDrawableBottomDivider = drawable3;
                    }
                    obtainStyledAttributes2.recycle();
                }
            }
            if (attributeSet != null) {
                TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMSettingsCategory);
                this.mbShowTopDivider = obtainStyledAttributes3.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showTopDivider, this.mbShowTopDivider);
                this.mbShowBottomDivider = obtainStyledAttributes3.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showBottomDivider, this.mbShowBottomDivider);
                this.mbShowCenterDivider = obtainStyledAttributes3.getBoolean(C4409R.styleable.ZMSettingsCategory_zm_showCenterDivider, this.mbShowCenterDivider);
                this.mResDrawableSettingsItemSelector = obtainStyledAttributes3.getResourceId(C4409R.styleable.ZMSettingsCategory_zm_settingsItemSelector, this.mResDrawableSettingsItemSelector);
                this.mMinItemHeight = obtainStyledAttributes3.getDimensionPixelSize(C4409R.styleable.ZMSettingsCategory_zm_seetingsItemMinHeight, this.mMinItemHeight);
                if (obtainStyledAttributes3.hasValue(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground)) {
                    this.mHasBackground = true;
                    this.mBackground = obtainStyledAttributes3.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground);
                    this.mBackgroundColor = obtainStyledAttributes3.getColor(C4409R.styleable.ZMSettingsCategory_zm_settingsCategoryBackground, this.mBackgroundColor);
                }
                i2 = obtainStyledAttributes3.getDimensionPixelSize(C4409R.styleable.ZMSettingsCategory_zm_dividerHeight, i2);
                Drawable drawable4 = obtainStyledAttributes3.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_topDivider);
                if (drawable4 != null) {
                    this.mDrawableTopDivider = drawable4;
                }
                Drawable drawable5 = obtainStyledAttributes3.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_centerDivider);
                if (drawable5 != null) {
                    this.mDrawableCenterDivider = drawable5;
                }
                Drawable drawable6 = obtainStyledAttributes3.getDrawable(C4409R.styleable.ZMSettingsCategory_zm_bottomDivider);
                if (drawable6 != null) {
                    this.mDrawableBottomDivider = drawable6;
                }
                obtainStyledAttributes3.recycle();
            }
            if (this.mDrawableTopDivider == null) {
                this.mDrawableTopDivider = getResources().getDrawable(C4409R.C4410drawable.zm_settings_top_divider);
            }
            if (this.mDrawableCenterDivider == null) {
                this.mDrawableCenterDivider = getResources().getDrawable(C4409R.C4410drawable.zm_settings_center_divider);
            }
            if (this.mDrawableBottomDivider == null) {
                this.mDrawableBottomDivider = getResources().getDrawable(C4409R.C4410drawable.zm_settings_bottom_divider);
            }
            if (i2 == 0) {
                this.mbShowTopDivider = false;
                this.mbShowCenterDivider = false;
                this.mbShowBottomDivider = false;
            } else if (i2 > 0) {
                this.mTopDividerHeight = i2;
                this.mCenterDividerHeight = i2;
                this.mBottomDividerHeight = i2;
            } else {
                Drawable drawable7 = this.mDrawableTopDivider;
                if (drawable7 != null) {
                    this.mTopDividerHeight = drawable7.getIntrinsicHeight();
                }
                Drawable drawable8 = this.mDrawableCenterDivider;
                if (drawable8 != null) {
                    this.mCenterDividerHeight = drawable8.getIntrinsicHeight();
                }
                Drawable drawable9 = this.mDrawableBottomDivider;
                if (drawable9 != null) {
                    this.mBottomDividerHeight = drawable9.getIntrinsicHeight();
                }
            }
            if (isInEditMode()) {
                if (this.mTopDividerHeight < 2 && this.mDrawableTopDivider != null) {
                    this.mTopDividerHeight = 2;
                }
                if (this.mCenterDividerHeight < 2 && this.mDrawableCenterDivider != null) {
                    this.mCenterDividerHeight = 2;
                }
                if (this.mBottomDividerHeight < 2 && this.mDrawableBottomDivider != null) {
                    this.mBottomDividerHeight = 2;
                }
            }
            if (this.mHasBackground) {
                Drawable drawable10 = this.mBackground;
                if (drawable10 != null) {
                    setBackgroundDrawable(drawable10);
                } else {
                    setBackgroundColor(this.mBackgroundColor);
                }
                setPadding(0, 0, 0, 0);
            }
            setWillNotDraw(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int childCount = getChildCount();
        View view = null;
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() == 0) {
                if (view != null) {
                    int paddingLeft = view.getPaddingLeft();
                    int paddingTop = view.getPaddingTop();
                    int paddingRight = view.getPaddingRight();
                    int paddingBottom = view.getPaddingBottom();
                    view.setBackgroundResource(this.mResDrawableSettingsItemSelector);
                    view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    if (i3 == 1 && this.mbShowTopDivider && this.mDrawableTopDivider != null) {
                        layoutParams.topMargin = this.mTopDividerHeight;
                    } else if (i3 <= 1 || !this.mbShowCenterDivider || this.mDrawableCenterDivider == null) {
                        layoutParams.topMargin = 0;
                    } else {
                        layoutParams.topMargin = this.mCenterDividerHeight;
                    }
                    view.setLayoutParams(layoutParams);
                    view.setMinimumHeight(this.mMinItemHeight);
                }
                i3++;
                view = childAt;
            }
        }
        if (view != null) {
            int paddingLeft2 = view.getPaddingLeft();
            int paddingTop2 = view.getPaddingTop();
            int paddingRight2 = view.getPaddingRight();
            int paddingBottom2 = view.getPaddingBottom();
            view.setBackgroundResource(this.mResDrawableSettingsItemSelector);
            view.setPadding(paddingLeft2, paddingTop2, paddingRight2, paddingBottom2);
            LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
            if (i3 == 1 && this.mbShowTopDivider && this.mDrawableTopDivider != null) {
                layoutParams2.topMargin = this.mTopDividerHeight;
            } else if (i3 <= 1 || !this.mbShowCenterDivider || this.mDrawableCenterDivider == null) {
                layoutParams2.topMargin = 0;
            } else {
                layoutParams2.topMargin = this.mCenterDividerHeight;
            }
            if (!this.mbShowBottomDivider || this.mDrawableBottomDivider == null) {
                layoutParams2.bottomMargin = 0;
            } else {
                layoutParams2.bottomMargin = this.mBottomDividerHeight;
            }
            view.setLayoutParams(layoutParams2);
            view.setMinimumHeight(this.mMinItemHeight);
        }
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int childCount = getChildCount();
        View view = null;
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() == 0) {
                if (view != null) {
                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    int left = view.getLeft();
                    int right = view.getRight();
                    int top = view.getTop();
                    int i3 = top - layoutParams.topMargin;
                    if (i == 1 && this.mbShowTopDivider) {
                        drawTopDivider(canvas, left, i3, right, top);
                    } else if (i > 1 && this.mbShowCenterDivider) {
                        drawCenterDivider(canvas, left, i3, right, top);
                    }
                }
                i++;
                view = childAt;
            }
        }
        if (view != null) {
            LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
            int left2 = view.getLeft();
            int right2 = view.getRight();
            int top2 = view.getTop();
            int i4 = top2 - layoutParams2.topMargin;
            if (i == 1 && this.mbShowTopDivider) {
                drawTopDivider(canvas, left2, i4, right2, top2);
            } else if (i > 1 && this.mbShowCenterDivider) {
                drawCenterDivider(canvas, left2, i4, right2, top2);
            }
            if (this.mbShowBottomDivider) {
                int bottom = view.getBottom();
                drawBottomDivider(canvas, left2, bottom, right2, bottom + layoutParams2.bottomMargin);
            }
        }
    }

    private void drawTopDivider(Canvas canvas, int i, int i2, int i3, int i4) {
        Drawable drawable = this.mDrawableTopDivider;
        if (drawable != null) {
            drawable.setBounds(i, i2, i3, i4);
            this.mDrawableTopDivider.draw(canvas);
        }
    }

    private void drawCenterDivider(Canvas canvas, int i, int i2, int i3, int i4) {
        Drawable drawable = this.mDrawableCenterDivider;
        if (drawable != null) {
            drawable.setBounds(i, i2, i3, i4);
            this.mDrawableCenterDivider.draw(canvas);
        }
    }

    private void drawBottomDivider(Canvas canvas, int i, int i2, int i3, int i4) {
        Drawable drawable = this.mDrawableBottomDivider;
        if (drawable != null) {
            drawable.setBounds(i, i2, i3, i4);
            this.mDrawableBottomDivider.draw(canvas);
        }
    }
}
