package com.zipow.videobox.view.sip;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.videomeetings.C4558R;

public class RecordView extends LinearLayout {
    private boolean isStarting = false;
    private boolean isStopping = false;
    private ImageView mImageView;
    @NonNull
    private final ProgressBar mProgressBar = new ProgressBar(getContext(), null, 16842873);

    public RecordView(Context context) {
        super(context);
        init();
    }

    public RecordView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RecordView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    @RequiresApi(api = 21)
    public RecordView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private void init() {
        setGravity(17);
        setOrientation(1);
        addView(this.mProgressBar);
        this.mImageView = new ImageView(getContext());
        this.mImageView.setScaleType(ScaleType.CENTER_INSIDE);
        this.mImageView.setImageDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_sip_ic_record_off));
        addView(this.mImageView);
        this.mProgressBar.setVisibility(8);
        if (OsUtil.isAtLeastJB()) {
            this.mImageView.setImportantForAccessibility(2);
            this.mProgressBar.setImportantForAccessibility(2);
        }
    }

    public void reset() {
        stopped();
        this.isStarting = false;
    }

    public void start() {
        this.mProgressBar.setVisibility(0);
        this.mImageView.setVisibility(8);
        this.isStarting = true;
    }

    public void recording() {
        this.mProgressBar.setVisibility(8);
        this.mImageView.setVisibility(0);
        if (isEnabled()) {
            this.mImageView.setImageResource(C4558R.C4559drawable.zm_sip_ic_record_on);
        }
        setSelected(true);
        this.isStarting = false;
    }

    public void stop() {
        setSelected(false);
        this.mProgressBar.setVisibility(0);
        this.mImageView.setVisibility(8);
        this.isStopping = true;
    }

    public void stopped() {
        setSelected(false);
        this.mProgressBar.setVisibility(8);
        this.mImageView.setVisibility(0);
        if (isEnabled()) {
            this.mImageView.setImageDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_sip_ic_record_off));
        }
        this.isStopping = false;
    }

    public boolean isStarting() {
        return this.isStarting;
    }

    public boolean isStopping() {
        return this.isStopping;
    }

    public void setRecordEnbaled(boolean z) {
        this.mImageView.setImageResource(z ? C4558R.C4559drawable.zm_sip_ic_record_off : C4558R.C4559drawable.zm_sip_ic_record_disable);
        setEnabled(z);
    }

    public void setSelected(boolean z) {
        super.setSelected(z);
        if (!isEnabled()) {
            this.mImageView.setImageResource(C4558R.C4559drawable.zm_sip_ic_record_disable);
        } else if (z) {
            this.mImageView.setImageResource(C4558R.C4559drawable.zm_sip_ic_record_on);
        } else {
            this.mImageView.setImageResource(C4558R.C4559drawable.zm_sip_ic_record_off);
        }
    }
}
