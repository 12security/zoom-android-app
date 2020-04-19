package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMToolbarLayout;
import p021us.zoom.videomeetings.C4558R;

public class NonVerbalFeedbackActionView extends LinearLayout implements OnClickListener {
    private ToolbarButton mBtnBad;
    private ToolbarButton mBtnClap;
    private ToolbarButton mBtnClock;
    private ToolbarButton mBtnCoffee;
    private ToolbarButton mBtnEmojis;
    private ToolbarButton mBtnFaster;
    private ToolbarButton mBtnGood;
    private ToolbarButton mBtnNo;
    private ToolbarButton mBtnRaiseHand;
    private ToolbarButton mBtnSlower;
    private ToolbarButton mBtnYes;
    private int mFeedback = 0;
    @Nullable
    private NonVerbalFBListener mListener = null;
    private ZMToolbarLayout mPanelEmojis;

    public interface NonVerbalFBListener {
        void onClearFeedback();

        void onSetFeedback(int i);
    }

    public NonVerbalFeedbackActionView(Context context) {
        super(context);
        init(context);
    }

    public NonVerbalFeedbackActionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public NonVerbalFeedbackActionView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, C4558R.layout.zm_non_verbal_feedback_action, this);
        ArrayList<ToolbarButton> arrayList = new ArrayList<>();
        this.mBtnRaiseHand = (ToolbarButton) findViewById(C4558R.C4560id.btnRaiseHand);
        arrayList.add(this.mBtnRaiseHand);
        this.mBtnYes = (ToolbarButton) findViewById(C4558R.C4560id.btnYes);
        arrayList.add(this.mBtnYes);
        this.mBtnNo = (ToolbarButton) findViewById(C4558R.C4560id.btnNo);
        arrayList.add(this.mBtnNo);
        this.mBtnSlower = (ToolbarButton) findViewById(C4558R.C4560id.btnSlower);
        arrayList.add(this.mBtnSlower);
        this.mBtnFaster = (ToolbarButton) findViewById(C4558R.C4560id.btnFaster);
        arrayList.add(this.mBtnFaster);
        this.mBtnEmojis = (ToolbarButton) findViewById(C4558R.C4560id.btnEmojis);
        arrayList.add(this.mBtnEmojis);
        this.mBtnGood = (ToolbarButton) findViewById(C4558R.C4560id.btnGood);
        arrayList.add(this.mBtnGood);
        this.mBtnBad = (ToolbarButton) findViewById(C4558R.C4560id.btnBad);
        arrayList.add(this.mBtnBad);
        this.mBtnCoffee = (ToolbarButton) findViewById(C4558R.C4560id.btnCoffee);
        arrayList.add(this.mBtnCoffee);
        this.mBtnClock = (ToolbarButton) findViewById(C4558R.C4560id.btnClock);
        arrayList.add(this.mBtnClock);
        this.mBtnClap = (ToolbarButton) findViewById(C4558R.C4560id.btnClap);
        arrayList.add(this.mBtnClap);
        int dip2px = UIUtil.dip2px(getContext(), 3.0f);
        for (ToolbarButton padding : arrayList) {
            padding.setPadding(dip2px, dip2px, dip2px, dip2px);
        }
        this.mPanelEmojis = (ZMToolbarLayout) findViewById(C4558R.C4560id.panelEmojis);
        ToolbarButton toolbarButton = this.mBtnRaiseHand;
        if (toolbarButton != null) {
            toolbarButton.setOnClickListener(this);
        }
        ToolbarButton toolbarButton2 = this.mBtnYes;
        if (toolbarButton2 != null) {
            toolbarButton2.setOnClickListener(this);
        }
        ToolbarButton toolbarButton3 = this.mBtnNo;
        if (toolbarButton3 != null) {
            toolbarButton3.setOnClickListener(this);
        }
        ToolbarButton toolbarButton4 = this.mBtnSlower;
        if (toolbarButton4 != null) {
            toolbarButton4.setOnClickListener(this);
        }
        ToolbarButton toolbarButton5 = this.mBtnFaster;
        if (toolbarButton5 != null) {
            toolbarButton5.setOnClickListener(this);
        }
        ToolbarButton toolbarButton6 = this.mBtnEmojis;
        if (toolbarButton6 != null) {
            toolbarButton6.setOnClickListener(this);
        }
        ToolbarButton toolbarButton7 = this.mBtnGood;
        if (toolbarButton7 != null) {
            toolbarButton7.setOnClickListener(this);
        }
        ToolbarButton toolbarButton8 = this.mBtnBad;
        if (toolbarButton8 != null) {
            toolbarButton8.setOnClickListener(this);
        }
        ToolbarButton toolbarButton9 = this.mBtnCoffee;
        if (toolbarButton9 != null) {
            toolbarButton9.setOnClickListener(this);
        }
        ToolbarButton toolbarButton10 = this.mBtnClock;
        if (toolbarButton10 != null) {
            toolbarButton10.setOnClickListener(this);
        }
        ToolbarButton toolbarButton11 = this.mBtnClap;
        if (toolbarButton11 != null) {
            toolbarButton11.setOnClickListener(this);
        }
    }

    public void setListener(NonVerbalFBListener nonVerbalFBListener) {
        this.mListener = nonVerbalFBListener;
    }

    private void clearFeedbackFocus() {
        this.mBtnRaiseHand.setIconBackgroundResource(0);
        this.mBtnYes.setIconBackgroundResource(0);
        this.mBtnNo.setIconBackgroundResource(0);
        this.mBtnFaster.setIconBackgroundResource(0);
        this.mBtnSlower.setIconBackgroundResource(0);
        this.mBtnGood.setIconBackgroundResource(0);
        this.mBtnClap.setIconBackgroundResource(0);
        this.mBtnCoffee.setIconBackgroundResource(0);
        this.mBtnBad.setIconBackgroundResource(0);
        this.mBtnClock.setIconBackgroundResource(0);
    }

    public void setFeedbackFocus(int i) {
        clearFeedbackFocus();
        this.mFeedback = i;
        boolean z = true;
        switch (i) {
            case 1:
                this.mBtnRaiseHand.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 2:
                this.mBtnYes.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 3:
                this.mBtnNo.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 4:
                this.mBtnFaster.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 5:
                this.mBtnSlower.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 6:
                this.mBtnBad.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 7:
                this.mBtnGood.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 8:
                this.mBtnClap.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 9:
                this.mBtnCoffee.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
            case 10:
                this.mBtnClock.setIconBackgroundResource(C4558R.C4559drawable.zm_feedback_focus_bg);
                break;
        }
        z = false;
        if (z) {
            this.mPanelEmojis.setVisibility(0);
        } else {
            this.mPanelEmojis.setVisibility(8);
        }
    }

    public void onClick(@NonNull View view) {
        int i;
        int id = view.getId();
        if (id == C4558R.C4560id.btnRaiseHand) {
            i = 1;
        } else if (id == C4558R.C4560id.btnYes) {
            i = 2;
        } else if (id == C4558R.C4560id.btnNo) {
            i = 3;
        } else if (id == C4558R.C4560id.btnSlower) {
            i = 5;
        } else if (id == C4558R.C4560id.btnFaster) {
            i = 4;
        } else if (id == C4558R.C4560id.btnGood) {
            i = 7;
        } else if (id == C4558R.C4560id.btnBad) {
            i = 6;
        } else if (id == C4558R.C4560id.btnCoffee) {
            i = 9;
        } else if (id == C4558R.C4560id.btnClock) {
            i = 10;
        } else if (id == C4558R.C4560id.btnClap) {
            i = 8;
        } else if (id == C4558R.C4560id.btnEmojis) {
            onClickEmojis();
            return;
        } else {
            i = -1;
        }
        NonVerbalFBListener nonVerbalFBListener = this.mListener;
        if (nonVerbalFBListener != null) {
            if (i == this.mFeedback) {
                nonVerbalFBListener.onClearFeedback();
            } else {
                nonVerbalFBListener.onSetFeedback(i);
            }
        }
    }

    private void onClickEmojis() {
        ZMToolbarLayout zMToolbarLayout = this.mPanelEmojis;
        if (zMToolbarLayout != null) {
            int visibility = zMToolbarLayout.getVisibility();
            if (visibility == 0) {
                this.mPanelEmojis.setVisibility(8);
            } else if (visibility == 8) {
                this.mPanelEmojis.setVisibility(0);
            }
            invalidate();
        }
    }
}
