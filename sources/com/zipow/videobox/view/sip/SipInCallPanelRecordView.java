package com.zipow.videobox.view.sip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.zipow.videobox.view.sip.SipInCallPanelView.PanelButtonItem;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class SipInCallPanelRecordView extends FrameLayout {
    private final String TAG = SipInCallPanelRecordView.class.getSimpleName();
    private RecordView mPanelImage;
    private TextView mPanelText;
    private View mPanelView;

    public SipInCallPanelRecordView(@NonNull Context context) {
        super(context);
        initViews();
    }

    public SipInCallPanelRecordView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public SipInCallPanelRecordView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    @RequiresApi(api = 21)
    public SipInCallPanelRecordView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), C4558R.layout.zm_sip_in_call_panel_record_view, this);
        this.mPanelView = findViewById(C4558R.C4560id.panelView);
        this.mPanelImage = (RecordView) findViewById(C4558R.C4560id.panelImage);
        this.mPanelText = (TextView) findViewById(C4558R.C4560id.panelText);
    }

    public void bindView(PanelButtonItem panelButtonItem) {
        if (panelButtonItem != null) {
            this.mPanelImage.setRecordEnbaled(!panelButtonItem.isDisable());
            this.mPanelImage.setSelected(panelButtonItem.isSelected());
            if (!StringUtil.isEmptyOrNull(panelButtonItem.getLabel())) {
                this.mPanelImage.setContentDescription(getResources().getString(C4558R.string.zm_accessibility_sip_call_keypad_44057, new Object[]{panelButtonItem.getLabel()}));
            }
            this.mPanelText.setEnabled(!panelButtonItem.isDisable());
            this.mPanelText.setSelected(panelButtonItem.isSelected());
            this.mPanelText.setText(panelButtonItem.getLabel());
        }
    }

    public void setContentDescription(String str) {
        this.mPanelImage.setContentDescription(str);
    }

    public void start() {
        this.mPanelImage.start();
    }

    public void stop() {
        this.mPanelImage.stop();
    }

    public void stopped() {
        this.mPanelImage.stopped();
    }

    public void recording() {
        this.mPanelImage.recording();
    }
}
