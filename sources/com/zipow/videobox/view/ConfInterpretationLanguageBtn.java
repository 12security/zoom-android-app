package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zipow.videobox.confapp.InterpretationMgr;
import p021us.zoom.videomeetings.C4558R;

public class ConfInterpretationLanguageBtn extends LinearLayout {
    private int mInterpreterLan;
    private ImageView showLanguageImg;
    private TextView showLanguageName;

    private void initView() {
    }

    public ConfInterpretationLanguageBtn(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ConfInterpretationLanguageBtn(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfInterpretationLanguageBtn(Context context) {
        super(context);
        initView();
    }

    public int getInterpreterLan() {
        return this.mInterpreterLan;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.showLanguageImg = (ImageView) findViewById(C4558R.C4560id.showLanguageImg);
        this.showLanguageName = (TextView) findViewById(C4558R.C4560id.showLanguageName);
        setBackgroundResource(C4558R.C4559drawable.zm_transparent);
        this.showLanguageName.setTextColor(getResources().getColor(C4558R.color.zm_white));
    }

    public void setSelected(boolean z) {
        if (getVisibility() == 0) {
            if (z) {
                setBackgroundResource(C4558R.C4559drawable.zm_corner_bg_white);
                this.showLanguageName.setTextColor(getResources().getColor(C4558R.color.zm_black));
            } else {
                setBackgroundResource(C4558R.C4559drawable.zm_transparent);
                this.showLanguageName.setTextColor(getResources().getColor(C4558R.color.zm_white));
            }
            super.setSelected(z);
        }
    }

    public void refresh(int i) {
        setVisibility(0);
        this.showLanguageImg.setImageResource(InterpretationMgr.LAN_RES_IDS[i]);
        this.showLanguageName.setText(getResources().getString(InterpretationMgr.LAN_NAME_IDS[i]));
        this.mInterpreterLan = i;
    }
}
