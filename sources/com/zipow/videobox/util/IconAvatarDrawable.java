package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.videomeetings.C4558R;

public class IconAvatarDrawable extends Drawable {
    private static final int[] BG_COLORS_IDS = {C4558R.color.zm_abbr_avatar_bg_1, C4558R.color.zm_abbr_avatar_bg_2, C4558R.color.zm_abbr_avatar_bg_3, C4558R.color.zm_abbr_avatar_bg_4, C4558R.color.zm_abbr_avatar_bg_5, C4558R.color.zm_abbr_avatar_bg_6, C4558R.color.zm_abbr_avatar_bg_7, C4558R.color.zm_abbr_avatar_bg_8, C4558R.color.zm_abbr_avatar_bg_9, C4558R.color.zm_abbr_avatar_bg_10, C4558R.color.zm_abbr_avatar_bg_11, C4558R.color.zm_abbr_avatar_bg_12};
    private static final String TAG = "IconAvatarDrawable";
    private int mAlpha = -1;
    private int mColorBg = -11908018;
    private ColorFilter mColorFilter;
    private Drawable mIcon;

    public int getOpacity() {
        return 0;
    }

    public IconAvatarDrawable(Drawable drawable, String str) {
        this.mColorBg = getBackgroundColorBySeedString(VideoBoxApplication.getInstance(), str);
        this.mIcon = drawable;
    }

    private int getBackgroundColorBySeedString(@NonNull Context context, @Nullable String str) {
        int i = 0;
        if (str != null) {
            int i2 = 0;
            while (i < str.length()) {
                i2 = (i2 + str.charAt(i)) % 12;
                i++;
            }
            i = i2;
        }
        return context.getResources().getColor(BG_COLORS_IDS[i]);
    }

    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        int i = bounds.left;
        int i2 = bounds.top;
        int width = bounds.width();
        canvas.drawColor(this.mColorBg);
        int i3 = (width * 3) / 5;
        Drawable drawable = this.mIcon;
        if (drawable != null) {
            int i4 = (width - i3) / 2;
            int i5 = (width + i3) / 2;
            drawable.setBounds(i + i4, i4 + i2, i + i5, i2 + i5);
            this.mIcon.draw(canvas);
        }
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }
}
