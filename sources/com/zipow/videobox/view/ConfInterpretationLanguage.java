package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.InterpretationMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class ConfInterpretationLanguage extends LinearLayout {
    private ImageView showLanguageImg;
    private TextView showLanguageName;

    public ConfInterpretationLanguage(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ConfInterpretationLanguage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfInterpretationLanguage(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setFocusable(false);
        setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LanguageInterpretationDialog.showAsActivity((ZMActivity) ConfInterpretationLanguage.this.getContext());
            }
        });
        setVisibility(8);
    }

    public void refresh() {
        setVisibility(8);
        InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
        if (interpretationObj != null && ConfLocalHelper.isInterpretationStarted(interpretationObj) && !ConfLocalHelper.isInterpreter(interpretationObj)) {
            int participantActiveLan = interpretationObj.getParticipantActiveLan();
            if (participantActiveLan != -1) {
                this.showLanguageImg = (ImageView) findViewById(C4558R.C4560id.showLanguageImg);
                this.showLanguageName = (TextView) findViewById(C4558R.C4560id.showLanguageName);
                this.showLanguageImg.setImageResource(InterpretationMgr.LAN_RES_IDS[participantActiveLan]);
                String string = getResources().getString(InterpretationMgr.LAN_NAME_IDS[participantActiveLan]);
                this.showLanguageName.setText(string);
                setContentDescription(getResources().getString(C4558R.string.zm_accessibility_language_interpretation_88102, new Object[]{string}));
                setVisibility(0);
            }
        }
    }
}
