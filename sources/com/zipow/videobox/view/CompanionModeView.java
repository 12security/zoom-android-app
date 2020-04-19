package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import p021us.zoom.videomeetings.C4558R;

public class CompanionModeView extends LinearLayout {
    private TextView txtTopic;

    public CompanionModeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public CompanionModeView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.txtTopic = (TextView) findViewById(C4558R.C4560id.txtTopic);
        updateData();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_companion_mode_view, this);
    }

    public void updateData() {
        if (!isInEditMode()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    this.txtTopic.setText(meetingItem.getTopic());
                }
            }
        }
    }
}
