package com.zipow.videobox.view.roundedview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class RoundedImageView extends ImageView {
    public static final float DEFAULT_BORDER_WIDTH = 0.0f;
    public static final float DEFAULT_RADIUS = 0.0f;
    public static final TileMode DEFAULT_TILE_MODE = TileMode.CLAMP;
    private static final ScaleType[] SCALE_TYPES = {ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE};
    public static final String TAG = "RoundedImageView";
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_UNDEFINED = -2;
    @Nullable
    private Drawable mBackgroundDrawable;
    private int mBackgroundResource;
    @NonNull
    private ColorStateList mBorderColor;
    private float mBorderWidth;
    @Nullable
    private ColorFilter mColorFilter;
    private boolean mColorMod;
    private final float[] mCornerRadii;
    @Nullable
    private Drawable mDrawable;
    private boolean mHasColorFilter;
    private boolean mIsOval;
    private boolean mMutateBackground;
    private int mResource;
    private ScaleType mScaleType;
    private TileMode mTileModeX;
    private TileMode mTileModeY;

    /* renamed from: com.zipow.videobox.view.roundedview.RoundedImageView$1 */
    static /* synthetic */ class C39811 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                android.widget.ImageView$ScaleType[] r0 = android.widget.ImageView.ScaleType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$widget$ImageView$ScaleType = r0
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0014 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x001f }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER_CROP     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x002a }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER_INSIDE     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0035 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_CENTER     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0040 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_START     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x004b }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_END     // Catch:{ NoSuchFieldError -> 0x004b }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0056 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_XY     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.roundedview.RoundedImageView.C39811.<clinit>():void");
        }
    }

    public RoundedImageView(Context context) {
        super(context);
        this.mCornerRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.mBorderWidth = 0.0f;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        TileMode tileMode = DEFAULT_TILE_MODE;
        this.mTileModeX = tileMode;
        this.mTileModeY = tileMode;
    }

    public RoundedImageView(@NonNull Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundedImageView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCornerRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.mBorderWidth = 0.0f;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        TileMode tileMode = DEFAULT_TILE_MODE;
        this.mTileModeX = tileMode;
        this.mTileModeY = tileMode;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.RoundedImageView, i, 0);
        int i2 = obtainStyledAttributes.getInt(C4558R.styleable.RoundedImageView_android_scaleType, -1);
        if (i2 >= 0) {
            setScaleType(SCALE_TYPES[i2]);
        } else {
            setScaleType(ScaleType.FIT_CENTER);
        }
        float dimensionPixelSize = (float) obtainStyledAttributes.getDimensionPixelSize(C4558R.styleable.RoundedImageView_riv_corner_radius, -1);
        this.mCornerRadii[0] = (float) obtainStyledAttributes.getDimensionPixelSize(C4558R.styleable.RoundedImageView_riv_corner_radius_top_left, -1);
        this.mCornerRadii[1] = (float) obtainStyledAttributes.getDimensionPixelSize(C4558R.styleable.RoundedImageView_riv_corner_radius_top_right, -1);
        this.mCornerRadii[2] = (float) obtainStyledAttributes.getDimensionPixelSize(C4558R.styleable.RoundedImageView_riv_corner_radius_bottom_right, -1);
        this.mCornerRadii[3] = (float) obtainStyledAttributes.getDimensionPixelSize(C4558R.styleable.RoundedImageView_riv_corner_radius_bottom_left, -1);
        int length = this.mCornerRadii.length;
        boolean z = false;
        for (int i3 = 0; i3 < length; i3++) {
            float[] fArr = this.mCornerRadii;
            if (fArr[i3] < 0.0f) {
                fArr[i3] = 0.0f;
            } else {
                z = true;
            }
        }
        if (!z) {
            if (dimensionPixelSize < 0.0f) {
                dimensionPixelSize = 0.0f;
            }
            int length2 = this.mCornerRadii.length;
            for (int i4 = 0; i4 < length2; i4++) {
                this.mCornerRadii[i4] = dimensionPixelSize;
            }
        }
        this.mBorderWidth = (float) obtainStyledAttributes.getDimensionPixelSize(C4558R.styleable.RoundedImageView_riv_border_width, -1);
        if (this.mBorderWidth < 0.0f) {
            this.mBorderWidth = 0.0f;
        }
        this.mBorderColor = obtainStyledAttributes.getColorStateList(C4558R.styleable.RoundedImageView_riv_border_color);
        if (this.mBorderColor == null) {
            this.mBorderColor = ColorStateList.valueOf(-16777216);
        }
        this.mMutateBackground = obtainStyledAttributes.getBoolean(C4558R.styleable.RoundedImageView_riv_mutate_background, false);
        this.mIsOval = obtainStyledAttributes.getBoolean(C4558R.styleable.RoundedImageView_riv_oval, false);
        int i5 = obtainStyledAttributes.getInt(C4558R.styleable.RoundedImageView_riv_tile_mode, -2);
        if (i5 != -2) {
            setTileModeX(parseTileMode(i5));
            setTileModeY(parseTileMode(i5));
        }
        int i6 = obtainStyledAttributes.getInt(C4558R.styleable.RoundedImageView_riv_tile_mode_x, -2);
        if (i6 != -2) {
            setTileModeX(parseTileMode(i6));
        }
        int i7 = obtainStyledAttributes.getInt(C4558R.styleable.RoundedImageView_riv_tile_mode_y, -2);
        if (i7 != -2) {
            setTileModeY(parseTileMode(i7));
        }
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);
        if (this.mMutateBackground) {
            super.setBackgroundDrawable(this.mBackgroundDrawable);
        }
        obtainStyledAttributes.recycle();
    }

    private static TileMode parseTileMode(int i) {
        switch (i) {
            case 0:
                return TileMode.CLAMP;
            case 1:
                return TileMode.REPEAT;
            case 2:
                return TileMode.MIRROR;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(@NonNull ScaleType scaleType) {
        if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            switch (C39811.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public void setImageDrawable(Drawable drawable) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromBitmap(bitmap);
        updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageResource(@DrawableRes int i) {
        if (this.mResource != i) {
            this.mResource = i;
            this.mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(this.mDrawable);
        }
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources resources = getResources();
        Drawable drawable = null;
        if (resources == null) {
            return null;
        }
        int i = this.mResource;
        if (i != 0) {
            try {
                drawable = resources.getDrawable(i);
            } catch (Exception e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to find resource: ");
                sb.append(this.mResource);
                Log.w(str, sb.toString(), e);
                this.mResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(drawable);
    }

    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    public void setBackgroundResource(@DrawableRes int i) {
        if (this.mBackgroundResource != i) {
            this.mBackgroundResource = i;
            this.mBackgroundDrawable = resolveBackgroundResource();
            setBackgroundDrawable(this.mBackgroundDrawable);
        }
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundDrawable = new ColorDrawable(i);
        setBackgroundDrawable(this.mBackgroundDrawable);
    }

    private Drawable resolveBackgroundResource() {
        Resources resources = getResources();
        Drawable drawable = null;
        if (resources == null) {
            return null;
        }
        int i = this.mBackgroundResource;
        if (i != 0) {
            try {
                drawable = resources.getDrawable(i);
            } catch (Exception e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to find resource: ");
                sb.append(this.mBackgroundResource);
                Log.w(str, sb.toString(), e);
                this.mBackgroundResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(drawable);
    }

    private void updateDrawableAttrs() {
        updateAttrs(this.mDrawable, this.mScaleType);
    }

    private void updateBackgroundDrawableAttrs(boolean z) {
        if (this.mMutateBackground) {
            if (z) {
                this.mBackgroundDrawable = RoundedDrawable.fromDrawable(this.mBackgroundDrawable);
            }
            updateAttrs(this.mBackgroundDrawable, ScaleType.FIT_XY);
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mColorFilter != colorFilter) {
            this.mColorFilter = colorFilter;
            this.mHasColorFilter = true;
            this.mColorMod = true;
            applyColorMod();
            invalidate();
        }
    }

    private void applyColorMod() {
        Drawable drawable = this.mDrawable;
        if (drawable != null && this.mColorMod) {
            this.mDrawable = drawable.mutate();
            if (this.mHasColorFilter) {
                this.mDrawable.setColorFilter(this.mColorFilter);
            }
        }
    }

    private void updateAttrs(@Nullable Drawable drawable, ScaleType scaleType) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                RoundedDrawable roundedDrawable = (RoundedDrawable) drawable;
                roundedDrawable.setScaleType(scaleType).setBorderWidth(this.mBorderWidth).setBorderColor(this.mBorderColor).setOval(this.mIsOval).setTileModeX(this.mTileModeX).setTileModeY(this.mTileModeY);
                float[] fArr = this.mCornerRadii;
                roundedDrawable.setCornerRadius(fArr[0], fArr[1], fArr[2], fArr[3]);
                applyColorMod();
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                int numberOfLayers = layerDrawable.getNumberOfLayers();
                for (int i = 0; i < numberOfLayers; i++) {
                    updateAttrs(layerDrawable.getDrawable(i), scaleType);
                }
            }
        }
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable drawable) {
        this.mBackgroundDrawable = drawable;
        updateBackgroundDrawableAttrs(true);
        super.setBackgroundDrawable(this.mBackgroundDrawable);
    }

    public float getCornerRadius() {
        return getMaxCornerRadius();
    }

    public float getMaxCornerRadius() {
        float f = 0.0f;
        for (float max : this.mCornerRadii) {
            f = Math.max(max, f);
        }
        return f;
    }

    public float getCornerRadius(int i) {
        return this.mCornerRadii[i];
    }

    public void setCornerRadiusDimen(@DimenRes int i) {
        float dimension = getResources().getDimension(i);
        setCornerRadius(dimension, dimension, dimension, dimension);
    }

    public void setCornerRadiusDimen(int i, @DimenRes int i2) {
        setCornerRadius(i, (float) getResources().getDimensionPixelSize(i2));
    }

    public void setCornerRadius(float f) {
        setCornerRadius(f, f, f, f);
    }

    public void setCornerRadius(int i, float f) {
        float[] fArr = this.mCornerRadii;
        if (fArr[i] != f) {
            fArr[i] = f;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public void setCornerRadius(float f, float f2, float f3, float f4) {
        float[] fArr = this.mCornerRadii;
        if (fArr[0] != f || fArr[1] != f2 || fArr[2] != f4 || fArr[3] != f3) {
            float[] fArr2 = this.mCornerRadii;
            fArr2[0] = f;
            fArr2[1] = f2;
            fArr2[3] = f3;
            fArr2[2] = f4;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public void setBorderWidth(@DimenRes int i) {
        setBorderWidth(getResources().getDimension(i));
    }

    public void setBorderWidth(float f) {
        if (this.mBorderWidth != f) {
            this.mBorderWidth = f;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    @ColorInt
    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    public void setBorderColor(@ColorInt int i) {
        setBorderColor(ColorStateList.valueOf(i));
    }

    @Nullable
    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    public void setBorderColor(@Nullable ColorStateList colorStateList) {
        if (!this.mBorderColor.equals(colorStateList)) {
            if (colorStateList == null) {
                colorStateList = ColorStateList.valueOf(-16777216);
            }
            this.mBorderColor = colorStateList;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            if (this.mBorderWidth > 0.0f) {
                invalidate();
            }
        }
    }

    public boolean isOval() {
        return this.mIsOval;
    }

    public void setOval(boolean z) {
        this.mIsOval = z;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public TileMode getTileModeX() {
        return this.mTileModeX;
    }

    public void setTileModeX(TileMode tileMode) {
        if (this.mTileModeX != tileMode) {
            this.mTileModeX = tileMode;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public TileMode getTileModeY() {
        return this.mTileModeY;
    }

    public void setTileModeY(TileMode tileMode) {
        if (this.mTileModeY != tileMode) {
            this.mTileModeY = tileMode;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public boolean mutatesBackground() {
        return this.mMutateBackground;
    }

    public void mutateBackground(boolean z) {
        if (this.mMutateBackground != z) {
            this.mMutateBackground = z;
            updateBackgroundDrawableAttrs(true);
            invalidate();
        }
    }
}
