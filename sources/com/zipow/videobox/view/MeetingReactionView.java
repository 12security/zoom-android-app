package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ZMSettingHelper;
import com.zipow.videobox.view.video.MeetingReactionEmojiMgr;
import p021us.zoom.videomeetings.C4558R;

public class MeetingReactionView extends LinearLayout implements OnClickListener {
    private ImageView mBtnClap;
    private ImageView mBtnThumbup;
    private OnSelectListener mListener;

    public interface OnSelectListener {
        void onSelectMeetingReaction(int i, int i2);

        void onSelectMeetingReaction(String str);
    }

    public MeetingReactionView(Context context) {
        super(context);
        init(context);
    }

    public MeetingReactionView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MeetingReactionView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, C4558R.layout.zm_meeting_reaction_view, this);
        this.mBtnClap = (ImageView) findViewById(C4558R.C4560id.btnClap);
        this.mBtnThumbup = (ImageView) findViewById(C4558R.C4560id.btnThumbup);
        this.mBtnClap.setOnClickListener(this);
        this.mBtnThumbup.setOnClickListener(this);
        int meetingReactionSkinToneType = ZMSettingHelper.getMeetingReactionSkinToneType();
        Drawable meetingReactionDrawable = MeetingReactionEmojiMgr.getInstance().getMeetingReactionDrawable(1, meetingReactionSkinToneType);
        Drawable meetingReactionDrawable2 = MeetingReactionEmojiMgr.getInstance().getMeetingReactionDrawable(2, meetingReactionSkinToneType);
        this.mBtnClap.setImageDrawable(meetingReactionDrawable);
        this.mBtnThumbup.setImageDrawable(meetingReactionDrawable2);
    }

    public void setListener(OnSelectListener onSelectListener) {
        this.mListener = onSelectListener;
    }

    public void onClick(View view) {
        if (this.mListener != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnClap) {
                this.mListener.onSelectMeetingReaction(1, ZMSettingHelper.getMeetingReactionSkinToneType());
            } else if (id == C4558R.C4560id.btnThumbup) {
                this.mListener.onSelectMeetingReaction(2, ZMSettingHelper.getMeetingReactionSkinToneType());
            }
        }
    }
}
