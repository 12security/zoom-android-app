package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class NameAbbrAvatarDrawable extends Drawable {
    private static final int[] BG_COLORS_IDS = {C4558R.color.zm_abbr_avatar_bg_1, C4558R.color.zm_abbr_avatar_bg_2, C4558R.color.zm_abbr_avatar_bg_3, C4558R.color.zm_abbr_avatar_bg_4, C4558R.color.zm_abbr_avatar_bg_5, C4558R.color.zm_abbr_avatar_bg_6, C4558R.color.zm_abbr_avatar_bg_7, C4558R.color.zm_abbr_avatar_bg_8, C4558R.color.zm_abbr_avatar_bg_9, C4558R.color.zm_abbr_avatar_bg_10, C4558R.color.zm_abbr_avatar_bg_11, C4558R.color.zm_abbr_avatar_bg_12};
    private static final String TAG = "NameAbbrAvatarDrawable";
    private int mAlpha = -1;
    private int mColorBg = -11908018;
    private int mColorFg = -1;
    private ColorFilter mColorFilter;
    private String mNameAbbr = "XX";

    public int getOpacity() {
        return 0;
    }

    public NameAbbrAvatarDrawable(@Nullable String str, @Nullable String str2) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (str != null && str2 != null && instance != null) {
            this.mColorBg = getBackgroundColorBySeedString(instance, str2);
            this.mColorFg = instance.getResources().getColor(C4558R.color.zm_abbr_avatar_fg);
            this.mNameAbbr = generateNameAbbr(instance, str);
        }
    }

    private int getBackgroundColorBySeedString(@NonNull Context context, @NonNull String str) {
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            i = (i + str.charAt(i2)) % 12;
        }
        return context.getResources().getColor(BG_COLORS_IDS[i]);
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x001a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String generateNameAbbr(android.content.Context r2, java.lang.String r3) {
        /*
            r1 = this;
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_config_name_abbreviation_generator
            java.lang.String r2 = p021us.zoom.androidlib.util.ResourcesUtil.getString(r2, r0)
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r2)
            if (r0 != 0) goto L_0x0017
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch:{ Exception -> 0x0017 }
            java.lang.Object r2 = r2.newInstance()     // Catch:{ Exception -> 0x0017 }
            us.zoom.androidlib.util.INameAbbrGenerator r2 = (p021us.zoom.androidlib.util.INameAbbrGenerator) r2     // Catch:{ Exception -> 0x0017 }
            goto L_0x0018
        L_0x0017:
            r2 = 0
        L_0x0018:
            if (r2 != 0) goto L_0x001f
            us.zoom.androidlib.util.DefaultNameAbbrGenerator r2 = new us.zoom.androidlib.util.DefaultNameAbbrGenerator
            r2.<init>()
        L_0x001f:
            java.util.Locale r0 = p021us.zoom.androidlib.util.CompatUtils.getLocalDefault()
            java.lang.String r2 = r2.getNameAbbreviation(r3, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.NameAbbrAvatarDrawable.generateNameAbbr(android.content.Context, java.lang.String):java.lang.String");
    }

    public void draw(@NonNull Canvas canvas) {
        int measureText;
        int i;
        Rect bounds = getBounds();
        int i2 = bounds.left;
        int i3 = bounds.top;
        int width = bounds.width();
        int height = bounds.height();
        int i4 = width / 2;
        int sqrt = (((int) Math.sqrt((double) ((i4 * i4) / 2))) * 4) / 3;
        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(new TextView(VideoBoxApplication.getInstance()).getTypeface());
        textPaint.setColor(this.mColorFg);
        textPaint.setColorFilter(this.mColorFilter);
        textPaint.setAlpha(this.mAlpha);
        textPaint.setAntiAlias(true);
        int dip2px = UIUtil.dip2px(VideoBoxApplication.getInstance(), 7.0f);
        do {
            textPaint.setTextSize((float) sqrt);
            measureText = (int) textPaint.measureText(this.mNameAbbr);
            i = (width - measureText) / 2;
            sqrt -= 2;
            if (i >= dip2px) {
                break;
            }
        } while (measureText > i);
        canvas.drawColor(this.mColorBg);
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        canvas.drawText(this.mNameAbbr, (float) (i2 + i), ((float) i3) + (((float) (height / 2)) - (((fontMetrics.bottom - fontMetrics.top) / 2.0f) + fontMetrics.top)), textPaint);
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }
}
