package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class ZMPMIMeetingOptionLayout extends ZMBaseMeetingOptionLayout {
    private PMIEditMeetingListener mPMIEditMeetingListener;

    public interface PMIEditMeetingListener {
        void onJBHChange();
    }

    public ZMPMIMeetingOptionLayout(Context context) {
        super(context);
    }

    public ZMPMIMeetingOptionLayout(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setmPMIEditMeetingListener(PMIEditMeetingListener pMIEditMeetingListener) {
        this.mPMIEditMeetingListener = pMIEditMeetingListener;
    }

    public int getLayout() {
        return C4558R.layout.zm_pmi_meeting_options;
    }

    public void checkShowVideoOptions(boolean z) {
        if (z) {
            this.mOptionHostVideo.setVisibility(8);
            this.mOptionAttendeeVideo.setVisibility(8);
            return;
        }
        this.mOptionHostVideo.setVisibility(0);
        this.mOptionAttendeeVideo.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void onClickEnableJBH() {
        super.onClickEnableJBH();
        PMIEditMeetingListener pMIEditMeetingListener = this.mPMIEditMeetingListener;
        if (pMIEditMeetingListener != null) {
            pMIEditMeetingListener.onJBHChange();
        }
    }
}
