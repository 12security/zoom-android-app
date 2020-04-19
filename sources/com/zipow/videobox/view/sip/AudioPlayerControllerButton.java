package com.zipow.videobox.view.sip;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import p021us.zoom.videomeetings.C4558R;

public class AudioPlayerControllerButton extends LinearLayout {
    private boolean isPlayering = false;
    private ImageView mImageView;
    @NonNull
    private final ProgressBar mProgressBar;

    public AudioPlayerControllerButton(Context context) {
        super(context);
        this.mProgressBar = new ProgressBar(context, null, 16842873);
        init();
    }

    public AudioPlayerControllerButton(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mProgressBar = new ProgressBar(context, null, 16842873);
        init();
    }

    public AudioPlayerControllerButton(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mProgressBar = new ProgressBar(context, null, 16842873);
        init();
    }

    @RequiresApi(api = 21)
    public AudioPlayerControllerButton(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mProgressBar = new ProgressBar(context, null, 16842873);
        init();
    }

    private void init() {
        setGravity(17);
        setOrientation(1);
        this.mProgressBar.setIndeterminateDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_sip_player_loading));
        this.mProgressBar.setIndeterminate(false);
        addView(this.mProgressBar);
        this.mImageView = new ImageView(getContext());
        addView(this.mImageView);
        onPause();
    }

    public void onLoading() {
        this.mImageView.setVisibility(8);
        this.mProgressBar.setVisibility(0);
        this.isPlayering = false;
    }

    public void onPause() {
        this.mProgressBar.setVisibility(8);
        this.mImageView.setVisibility(0);
        this.mImageView.setImageResource(C4558R.C4559drawable.zm_sip_audio_play);
        setBackgroundResource(C4558R.color.zm_transparent);
        setContentDescription(getResources().getString(C4558R.string.zm_accessibility_sip_play_voicemail_button));
        this.isPlayering = false;
    }

    public void onPlay() {
        this.mProgressBar.setVisibility(8);
        this.mImageView.setVisibility(0);
        this.mImageView.setImageResource(C4558R.C4559drawable.zm_sip_pause);
        setBackgroundResource(C4558R.color.zm_transparent);
        setContentDescription(getResources().getString(C4558R.string.zm_accessibility_sip_pause_voicemail_button));
        this.isPlayering = true;
    }

    public boolean isPlaying() {
        return this.isPlayering;
    }

    public boolean isLoading() {
        return this.mProgressBar.getVisibility() == 0;
    }
}
