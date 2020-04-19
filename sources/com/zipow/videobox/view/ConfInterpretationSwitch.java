package com.zipow.videobox.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.InterpretationMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ConfInterpretationSwitch extends LinearLayout implements OnClickListener {
    /* access modifiers changed from: private */
    public Runnable action = null;
    @Nullable
    private InterpretationMgr mInterpretationObj;
    private ConfInterpretationLanguageBtn showLan1;
    private ConfInterpretationLanguageBtn showLan2;
    /* access modifiers changed from: private */
    public View showSwitchTip;
    ZMAlertDialog zmAlertDialog = null;

    public ConfInterpretationSwitch(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ConfInterpretationSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfInterpretationSwitch(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setFocusable(false);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.showSwitchTip = findViewById(C4558R.C4560id.showSwitchTip);
        this.showLan1 = (ConfInterpretationLanguageBtn) findViewById(C4558R.C4560id.showLan1);
        this.showLan2 = (ConfInterpretationLanguageBtn) findViewById(C4558R.C4560id.showLan2);
        this.showLan1.setOnClickListener(this);
        this.showLan2.setOnClickListener(this);
        setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.action;
        if (runnable != null) {
            this.showSwitchTip.removeCallbacks(runnable);
        }
    }

    public void refresh() {
        setVisibility(8);
        InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
        if (interpretationObj != null && ConfLocalHelper.isInterpreter(interpretationObj)) {
            if (interpretationObj.isNeedShowInterpreterTip()) {
                interpretationObj.setNeedShowInterpreterTip(false);
                if (this.zmAlertDialog == null) {
                    this.zmAlertDialog = new Builder(getContext()).setTitle(C4558R.string.zm_title_welcome_88102).setMessage(C4558R.string.zm_msg_interpreter_88102).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok_88102, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ConfInterpretationSwitch.this.show();
                        }
                    }).create();
                }
                this.zmAlertDialog.show();
            } else {
                ZMAlertDialog zMAlertDialog = this.zmAlertDialog;
                if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
                    return;
                }
                if (getVisibility() == 0) {
                    refreshLan();
                    return;
                }
                show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void show() {
        setVisibility(0);
        this.action = new Runnable() {
            public void run() {
                ConfInterpretationSwitch.this.showSwitchTip.setVisibility(8);
                ConfInterpretationSwitch.this.action = null;
            }
        };
        this.showSwitchTip.postDelayed(this.action, 5000);
        refreshLan();
    }

    private void refreshLan() {
        this.mInterpretationObj = ConfMgr.getInstance().getInterpretationObj();
        InterpretationMgr interpretationMgr = this.mInterpretationObj;
        if (interpretationMgr != null) {
            int[] interpreterLans = interpretationMgr.getInterpreterLans();
            if (interpreterLans != null && interpreterLans.length >= 2) {
                this.showLan1.refresh(interpreterLans[0]);
                this.showLan2.refresh(interpreterLans[1]);
                int interpreterActiveLan = this.mInterpretationObj.getInterpreterActiveLan();
                if (interpreterActiveLan == interpreterLans[0]) {
                    this.showLan1.setSelected(true);
                    this.showLan2.setSelected(false);
                } else if (interpreterActiveLan == interpreterLans[1]) {
                    this.showLan1.setSelected(false);
                    this.showLan2.setSelected(true);
                } else {
                    setInterpreterActiveLan(this.showLan2);
                }
            }
        }
    }

    public void onClick(View view) {
        if (view.isSelected()) {
            ConfInterpretationLanguageBtn confInterpretationLanguageBtn = this.showLan1;
            if (view == confInterpretationLanguageBtn) {
                confInterpretationLanguageBtn = this.showLan2;
            }
            setInterpreterActiveLan(confInterpretationLanguageBtn);
            return;
        }
        ConfInterpretationLanguageBtn confInterpretationLanguageBtn2 = this.showLan1;
        if (view != confInterpretationLanguageBtn2) {
            confInterpretationLanguageBtn2 = this.showLan2;
        }
        setInterpreterActiveLan(confInterpretationLanguageBtn2);
    }

    private void setInterpreterActiveLan(ConfInterpretationLanguageBtn confInterpretationLanguageBtn) {
        ConfInterpretationLanguageBtn confInterpretationLanguageBtn2 = this.showLan1;
        if (confInterpretationLanguageBtn == confInterpretationLanguageBtn2) {
            confInterpretationLanguageBtn2 = this.showLan2;
        }
        InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
        if (interpretationObj != null && interpretationObj.setInterpreterActiveLan(confInterpretationLanguageBtn.getInterpreterLan())) {
            confInterpretationLanguageBtn2.setSelected(false);
            confInterpretationLanguageBtn.setSelected(true);
        }
    }
}
