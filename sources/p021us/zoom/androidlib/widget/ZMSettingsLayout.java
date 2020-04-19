package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMSettingsLayout */
public class ZMSettingsLayout extends LinearLayout {
    private static final int INVALID_SPACING = Integer.MIN_VALUE;
    private float mCategorySpacing = -2.14748365E9f;

    public ZMSettingsLayout(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ZMSettingsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet, 0);
    }

    @SuppressLint({"NewApi"})
    public ZMSettingsLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, attributeSet, i);
    }

    private void initView(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C4409R.styleable.ZMSettingsLayout, C4409R.attr.zm_settingsLayoutAppearance, 0);
        if (obtainStyledAttributes != null) {
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.ZMSettingsLayout_zm_settingsCategorySpacing, Integer.MIN_VALUE);
            if (dimensionPixelSize != Integer.MIN_VALUE) {
                this.mCategorySpacing = (float) dimensionPixelSize;
            }
            obtainStyledAttributes.recycle();
        }
        if (i > 0) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMSettingsCategory, i, 0);
            if (obtainStyledAttributes2 != null) {
                int dimensionPixelSize2 = obtainStyledAttributes2.getDimensionPixelSize(C4409R.styleable.ZMSettingsLayout_zm_settingsCategorySpacing, Integer.MIN_VALUE);
                if (dimensionPixelSize2 != Integer.MIN_VALUE) {
                    this.mCategorySpacing = (float) dimensionPixelSize2;
                }
                obtainStyledAttributes2.recycle();
            }
        }
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMSettingsCategory);
            int dimensionPixelSize3 = obtainStyledAttributes3.getDimensionPixelSize(C4409R.styleable.ZMSettingsLayout_zm_settingsCategorySpacing, Integer.MIN_VALUE);
            if (dimensionPixelSize3 != Integer.MIN_VALUE) {
                this.mCategorySpacing = (float) dimensionPixelSize3;
            }
            obtainStyledAttributes3.recycle();
        }
        if (this.mCategorySpacing == -2.14748365E9f) {
            try {
                this.mCategorySpacing = context.getResources().getDimension(C4409R.dimen.zm_setting_item_group_spacing);
            } catch (Exception unused) {
                this.mCategorySpacing = 45.0f;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int childCount = getChildCount();
        boolean z = true;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if ((childAt instanceof ZMSettingsCategory) && childAt.getVisibility() == 0) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (!z) {
                    layoutParams.topMargin = (int) this.mCategorySpacing;
                }
                childAt.setLayoutParams(layoutParams);
                z = false;
            }
        }
        super.onMeasure(i, i2);
    }
}
