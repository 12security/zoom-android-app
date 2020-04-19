package com.zipow.videobox.view.sip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.zipow.videobox.view.sip.SipInCallPanelView.PanelButtonItem;
import p021us.zoom.videomeetings.C4558R;

public class SipInCallPanelItemView extends FrameLayout {
    private ImageView mPanelImage;
    private TextView mPanelText;
    private View mPanelView;

    public SipInCallPanelItemView(@NonNull Context context) {
        super(context);
        initViews();
    }

    public SipInCallPanelItemView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public SipInCallPanelItemView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    @RequiresApi(api = 21)
    public SipInCallPanelItemView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), C4558R.layout.zm_sip_in_call_panel_item_view, this);
        this.mPanelView = findViewById(C4558R.C4560id.panelView);
        this.mPanelImage = (ImageView) findViewById(C4558R.C4560id.panelImage);
        this.mPanelText = (TextView) findViewById(C4558R.C4560id.panelText);
    }

    public void bindView(PanelButtonItem panelButtonItem) {
        if (panelButtonItem != null) {
            this.mPanelImage.setImageDrawable(panelButtonItem.getIcon());
            this.mPanelImage.setContentDescription(panelButtonItem.getLabel());
            this.mPanelImage.setEnabled(!panelButtonItem.isDisable());
            this.mPanelImage.setSelected(panelButtonItem.isSelected());
            this.mPanelText.setSelected(panelButtonItem.isSelected());
            this.mPanelText.setEnabled(!panelButtonItem.isDisable());
            this.mPanelText.setText(panelButtonItem.getLabel());
        }
    }
}
