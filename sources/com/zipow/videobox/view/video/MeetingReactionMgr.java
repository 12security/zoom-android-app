package com.zipow.videobox.view.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI.IEmojiReactionListener;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.util.HashMap;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.videomeetings.C4558R;

public class MeetingReactionMgr implements IEmojiReactionListener {
    private static final long DISMISS_TIME = 10000;
    private static final String TAG = "MeetingReactionMgr";
    private static final MeetingReactionMgr mInstance = new MeetingReactionMgr();
    private HashMap<String, Float> mEmojiHWRatioMap = new HashMap<>();
    private DismissHandler mHandler = new DismissHandler();
    private boolean mInitiated = true;
    private boolean mInitiating = false;
    @NonNull
    private ListenerList mVideoUnits = new ListenerList();

    private static class DismissHandler extends Handler {
        private HashMap<VideoUnit, DismissRunnable> mTokenMap;

        private DismissHandler() {
            this.mTokenMap = new HashMap<>();
        }

        /* access modifiers changed from: 0000 */
        public void postDismiss(@NonNull VideoUnit videoUnit) {
            DismissRunnable dismissRunnable = new DismissRunnable(videoUnit);
            this.mTokenMap.put(videoUnit, dismissRunnable);
            postDelayed(dismissRunnable, MeetingReactionMgr.DISMISS_TIME);
        }

        /* access modifiers changed from: 0000 */
        public void removeDismiss(@NonNull VideoUnit videoUnit) {
            DismissRunnable dismissRunnable = (DismissRunnable) this.mTokenMap.get(videoUnit);
            this.mTokenMap.remove(videoUnit);
            if (dismissRunnable != null) {
                removeCallbacks(dismissRunnable);
            }
        }
    }

    private static class DismissRunnable implements Runnable {
        private VideoUnit mTarget;

        DismissRunnable(@NonNull VideoUnit videoUnit) {
            this.mTarget = videoUnit;
        }

        public void run() {
            this.mTarget.removeMeetingReaction();
        }
    }

    private int emojiPxSize2FontSize(int i) {
        return (int) (((double) i) / 3.5d);
    }

    public String getAccTxt(String str) {
        return "";
    }

    public static MeetingReactionMgr getInstance() {
        return mInstance;
    }

    private MeetingReactionMgr() {
    }

    public boolean isMeetingReactionEnable() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = false;
        if (confContext == null) {
            return false;
        }
        boolean isWebinar = confContext.isWebinar();
        boolean isEmojiReactionEnabled = confContext.isEmojiReactionEnabled();
        boolean isNoVideoMeeting = ConfMgr.getInstance().isNoVideoMeeting();
        if (this.mInitiated && isEmojiReactionEnabled && !isWebinar && !isNoVideoMeeting) {
            z = true;
        }
        return z;
    }

    public void checkShowMeetingReaction(VideoUnit videoUnit) {
        if (videoUnit.isVideoShowing()) {
            CmmUser userById = ConfMgr.getInstance().getUserById(videoUnit.getUser());
            if (userById != null) {
                int emojiReactionType = userById.getEmojiReactionType();
                int emojiReactionSkinTone = userById.getEmojiReactionSkinTone();
                if (emojiReactionType == 0 || emojiReactionSkinTone == 0) {
                    this.mHandler.removeDismiss(videoUnit);
                    videoUnit.removeMeetingReaction();
                } else if (isMeetingReactionEnable()) {
                    videoUnit.showMeetingReaction(emojiReactionType, emojiReactionSkinTone);
                    this.mHandler.removeDismiss(videoUnit);
                    this.mHandler.postDismiss(videoUnit);
                }
            }
        }
    }

    public void refreshMainVideoEmojiPos() {
        for (IListener iListener : this.mVideoUnits.getAll()) {
            VideoUnit videoUnit = (VideoUnit) iListener;
            if (videoUnit.isMainVideo()) {
                checkShowMeetingReaction(videoUnit);
            }
        }
    }

    public void registerUnit(@NonNull VideoUnit videoUnit) {
        this.mVideoUnits.add(videoUnit);
    }

    public void unregisterUnit(@NonNull VideoUnit videoUnit) {
        this.mHandler.removeDismiss(videoUnit);
        this.mVideoUnits.remove(videoUnit);
    }

    public void unregisterAllUnits() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public float getEmojiHWRatio(String str) {
        Float f = (Float) this.mEmojiHWRatioMap.get(str);
        if (f == null || f.floatValue() == 0.0f) {
            TextView textView = new TextView(VideoBoxApplication.getGlobalContext());
            textView.setText(CommonEmojiHelper.getInstance().tranToEmojiText(str));
            textView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
            f = Float.valueOf(((float) textView.getMeasuredHeight()) / ((float) textView.getMeasuredWidth()));
            this.mEmojiHWRatioMap.put(str, f);
        }
        return f.floatValue();
    }

    public float getEmojiHWRatio(int i, int i2) {
        Drawable meetingReactionDrawable = MeetingReactionEmojiMgr.getInstance().getMeetingReactionDrawable(i, i2);
        if (meetingReactionDrawable == null) {
            return 0.0f;
        }
        return ((float) meetingReactionDrawable.getIntrinsicHeight()) / ((float) meetingReactionDrawable.getIntrinsicWidth());
    }

    @Nullable
    public Bitmap getMeetingReactionBitmap(int i, int i2, int i3, int i4) {
        Drawable meetingReactionDrawable = MeetingReactionEmojiMgr.getInstance().getMeetingReactionDrawable(i, i2);
        Bitmap bitmap = null;
        if (meetingReactionDrawable == null) {
            return null;
        }
        try {
            bitmap = Bitmap.createBitmap(i3, i4, Config.ARGB_8888);
        } catch (OutOfMemoryError unused) {
        }
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            meetingReactionDrawable.setBounds(0, 0, i3, i4);
            meetingReactionDrawable.draw(canvas);
        }
        return bitmap;
    }

    @Nullable
    public Bitmap getMeetingReactionBitmap(String str, int i, int i2) {
        Bitmap bitmap;
        TextView textView = new TextView(VideoBoxApplication.getGlobalContext());
        textView.setTextSize((float) emojiPxSize2FontSize(i));
        textView.setShadowLayer(10.0f, 5.0f, 5.0f, Color.argb(30, 0, 0, 0));
        textView.setText(CommonEmojiHelper.getInstance().tranToEmojiText(str));
        textView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        try {
            bitmap = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Config.ARGB_8888);
        } catch (OutOfMemoryError unused) {
            bitmap = null;
        }
        if (bitmap != null) {
            textView.draw(new Canvas(bitmap));
        }
        return bitmap;
    }

    public String getAccTxt(int i) {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext == null) {
            return "";
        }
        switch (i) {
            case 1:
                return globalContext.getString(C4558R.string.zm_accessibility_btn_meeting_reactions_clap_122373);
            case 2:
                return globalContext.getString(C4558R.string.zm_accessibility_btn_meeting_reactions_thumbup_122373);
            default:
                return "";
        }
    }

    public void onEmojiReactionReceived(long j, String str) {
        dispatchEmojiReactionEvent(j);
    }

    public void onEmojiReactionReceived(long j, int i, int i2) {
        dispatchEmojiReactionEvent(j);
    }

    private void dispatchEmojiReactionEvent(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            for (IListener iListener : this.mVideoUnits.getAll()) {
                VideoUnit videoUnit = (VideoUnit) iListener;
                if (confStatusObj.isSameUser(videoUnit.getUser(), j)) {
                    checkShowMeetingReaction(videoUnit);
                }
            }
        }
    }
}
