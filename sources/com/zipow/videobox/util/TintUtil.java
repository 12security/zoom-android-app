package com.zipow.videobox.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class TintUtil {
    @NonNull
    public static Drawable tintColor(@NonNull Context context, @DrawableRes int i, @ColorRes int i2) {
        return tintColor(context, ContextCompat.getDrawable(context, i), i2);
    }

    @NonNull
    public static Drawable tintColor(@NonNull Context context, @NonNull Drawable drawable, @ColorRes int i) {
        return tintColor(drawable, ContextCompat.getColor(context, i));
    }

    @NonNull
    public static Drawable tintColor(@NonNull Drawable drawable, @ColorInt int i) {
        Drawable mutate = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(mutate, i);
        return mutate;
    }

    @NonNull
    public static Drawable tintColorList(@NonNull Context context, @DrawableRes int i, @ColorRes int i2) {
        return tintColorList(context, ContextCompat.getDrawable(context, i), i2);
    }

    @NonNull
    public static Drawable tintColorList(@NonNull Context context, @NonNull Drawable drawable, @ColorRes int i) {
        return tintColorList(drawable, ContextCompat.getColorStateList(context, i));
    }

    @NonNull
    public static Drawable tintColorList(@NonNull Drawable drawable, @NonNull ColorStateList colorStateList) {
        Drawable mutate = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTintList(mutate, colorStateList);
        return mutate;
    }
}
