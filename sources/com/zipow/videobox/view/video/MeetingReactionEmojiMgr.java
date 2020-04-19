package com.zipow.videobox.view.video;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.videomeetings.C4558R;

public class MeetingReactionEmojiMgr {
    private static final MeetingReactionEmojiMgr ourInstance = new MeetingReactionEmojiMgr();

    public static MeetingReactionEmojiMgr getInstance() {
        return ourInstance;
    }

    private MeetingReactionEmojiMgr() {
    }

    @Nullable
    public Drawable getMeetingReactionDrawable(int i, int i2) {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext == null) {
            return null;
        }
        Resources resources = globalContext.getResources();
        if (resources != null) {
            switch (i) {
                case 1:
                    return resources.getDrawable(getClapId(i2), null);
                case 2:
                    return resources.getDrawable(getThumbupId(i2), null);
            }
        }
        return null;
    }

    private int getClapId(int i) {
        switch (i) {
            case 1:
                return C4558R.C4559drawable.reaction_1f44f;
            case 2:
                return C4558R.C4559drawable.reaction_1f44f_1f3fb;
            case 3:
                return C4558R.C4559drawable.reaction_1f44f_1f3fc;
            case 4:
                return C4558R.C4559drawable.reaction_1f44f_1f3fd;
            case 5:
                return C4558R.C4559drawable.reaction_1f44f_1f3fe;
            case 6:
                return C4558R.C4559drawable.reaction_1f44f_1f3ff;
            default:
                return C4558R.C4559drawable.reaction_1f44f;
        }
    }

    private int getThumbupId(int i) {
        switch (i) {
            case 1:
                return C4558R.C4559drawable.reaction_1f44d;
            case 2:
                return C4558R.C4559drawable.reaction_1f44d_1f3fb;
            case 3:
                return C4558R.C4559drawable.reaction_1f44d_1f3fc;
            case 4:
                return C4558R.C4559drawable.reaction_1f44d_1f3fd;
            case 5:
                return C4558R.C4559drawable.reaction_1f44d_1f3fe;
            case 6:
                return C4558R.C4559drawable.reaction_1f44d_1f3ff;
            default:
                return C4558R.C4559drawable.reaction_1f44d;
        }
    }
}
