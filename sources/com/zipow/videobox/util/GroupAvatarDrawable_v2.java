package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.videomeetings.C4558R;

public class GroupAvatarDrawable_v2 extends Drawable {
    private static final int[] BG_COLORS_IDS = {C4558R.color.zm_abbr_avatar_bg_1, C4558R.color.zm_abbr_avatar_bg_2, C4558R.color.zm_abbr_avatar_bg_3, C4558R.color.zm_abbr_avatar_bg_4, C4558R.color.zm_abbr_avatar_bg_5, C4558R.color.zm_abbr_avatar_bg_6, C4558R.color.zm_abbr_avatar_bg_7, C4558R.color.zm_abbr_avatar_bg_8, C4558R.color.zm_abbr_avatar_bg_9, C4558R.color.zm_abbr_avatar_bg_10, C4558R.color.zm_abbr_avatar_bg_11, C4558R.color.zm_abbr_avatar_bg_12};
    private int mColorBg = -11908018;
    @NonNull
    private final Drawable mIcon;

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public GroupAvatarDrawable_v2(String str) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        this.mColorBg = getBackgroundColorBySeedString(instance, str);
        this.mIcon = instance.getResources().getDrawable(C4558R.C4559drawable.zm_ic_avatar_group);
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
        this.mIcon.setBounds(getBounds());
        this.mIcon.draw(canvas);
    }
}
